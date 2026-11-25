/*******************************************************************************
 * Copyright (c) 2026 Obeo.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.syson.sysml.serializer.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.syson.sysml.parser.adapter.ITextAdapter;
import org.eclipse.syson.sysml.serializer.ReferenceNameResolver;
import org.eclipse.syson.sysml.serializer.ReferencePathResolver;
import org.eclipse.syson.sysml.serializer.names.NameEscaper;
import org.eclipse.syson.sysml.serializer.references.ReferenceNameMatcher;
import org.eclipse.syson.sysml.serializer.tokens.ReferenceNameSyntax;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Feature;
import org.omg.sysml.lang.sysml.FeatureTyping;
import org.omg.sysml.lang.sysml.Import;
import org.omg.sysml.lang.sysml.Membership;
import org.omg.sysml.lang.sysml.MembershipImport;
import org.omg.sysml.lang.sysml.NamespaceImport;
import org.omg.sysml.lang.sysml.Redefinition;
import org.omg.sysml.lang.sysml.ReferenceSubsetting;
import org.omg.sysml.lang.sysml.Relationship;
import org.omg.sysml.lang.sysml.Specialization;
import org.omg.sysml.lang.sysml.Subsetting;
import org.omg.sysml.lang.sysml.SysMLPackage;
import org.omg.sysml.lang.sysml.Type;

/**
 * Chooses the text and target of one cross-reference: the installed model target, the parser spelling kept in
 * {@link DanglingReferenceIndex}, or a fallback name, and re-applies source quoting.
 * <p>
 * Look here when a reference is written with the wrong spelling (qualified instead of short, unquoted, renamed target
 * not reflected, alias path lost), when a proxy or partially resolved reference loses its name, or for import and
 * alias targets. Targets are read without resolving proxies and named through {@link StoredModel}, so that naming a
 * reference never changes the model; a proxy is named by its parser spelling or its URI. In round-trip mode, parser
 * spellings are preserved whenever they still designate the model target.
 * </p>
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class ReferenceTargets {

    private static final String QUALIFIED_QUOTED_CONJUGATION_MARKER = SysMLTokens.NAMESPACE_SEPARATOR + "'"
            + SysMLTokens.CONJUGATION_OPERATOR;

    private static final String UNNAMED_QUALIFIED_NAME_SUFFIX = "::''";

    private final boolean preserveParsedReferenceNames;

    private final ReferenceNameResolver referenceNameResolver;

    private final ReferencePathResolver referencePathResolver;

    private final ModelNavigation navigation;

    private final ContainmentNameLookup nameLookup = new ContainmentNameLookup();

    /**
     * Creates the reference queries of one serialization run.
     *
     * @param preserveParsedReferenceNames
     *            {@code true} in round-trip mode, where parser spellings win whenever they still designate the target
     * @param referenceNameResolver
     *            supplies contextual names for fallback targets; an empty or null name means "no opinion"
     * @param referencePathResolver
     *            confirms parser paths, such as alias paths, that the namespace lookup cannot resolve
     * @param navigation
     *            containment navigation
     */
    public ReferenceTargets(final boolean preserveParsedReferenceNames, final ReferenceNameResolver referenceNameResolver,
            final ReferencePathResolver referencePathResolver, final ModelNavigation navigation) {
        this.preserveParsedReferenceNames = preserveParsedReferenceNames;
        this.referenceNameResolver = referenceNameResolver;
        this.referencePathResolver = referencePathResolver;
        this.navigation = Objects.requireNonNull(navigation);
    }

    /**
     * Returns the named targets of one model reference. Installed targets, including proxies, supply model identity;
     * parser names are retained when preservation requires it, with dangling links filling missing model targets.
     *
     * @param context
     *            the object carrying the reference
     * @param referenceName
     *            the EMF reference name
     * @param danglingReferences
     *            unresolved parser references used as fallback
     * @param resolvedName
     *            renders the name of a resolved target
     * @return reference names and their targets, in model order
     */
    public List<ModelReference> referenceTargets(final Element context, final String referenceName, final DanglingReferenceIndex danglingReferences,
            final Function<Element, String> resolvedName) {
        List<ModelReference> targets = List.of();
        if (context != null && context.eClass().getEStructuralFeature(referenceName) instanceof final EReference reference) {
            targets = this.namedTargets(context, referenceName, reference, danglingReferences, resolvedName);
        }
        return List.copyOf(targets);
    }

    /**
     * Returns the text of the first target of a direct relationship with exactly the given metaclass, e.g. the
     * {@code ::>} target of a reference usage. The parser spelling wins; otherwise the first installed cross-reference
     * of the relationship supplies a fallback name. Source quoting is re-applied.
     */
    public String firstDirectRelationshipTarget(final Element element, final EClass relationshipClass,
            final DanglingReferenceIndex danglingReferences) {
        return this.navigation.ownedRelationships(element).stream()
                .filter(relationship -> this.navigation.isMetaclass(relationship, relationshipClass))
                .map(relationship -> this.relationshipTargetText(relationship, danglingReferences))
                .filter(target -> !target.isBlank())
                .findFirst()
                .orElse("");
    }

    /**
     * Returns the first resolved target of a direct relationship, when the model contains one. Dangling parser
     * references deliberately return {@code null}.
     */
    public Element firstDirectRelationshipTargetElement(final Element element, final EClass relationshipClass) {
        return this.navigation.ownedRelationships(element).stream()
                .filter(relationship -> this.navigation.isMetaclass(relationship, relationshipClass))
                .map(this::primaryRelationshipTargetElement)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns the first resolved target of a relationship, including targets of relationship specializations such as
     * imports and dependencies.
     */
    public Element firstRelationshipTargetElement(final Relationship relationship) {
        Element target = null;
        if (relationship != null) {
            target = StoredModel.targets(relationship).stream().findFirst().orElse(null);
        }
        return target;
    }

    /**
     * Returns the imported name of an import. Normalized output prefers the resolved model name; round-trip output
     * prefers the parser spelling while it still designates the imported element, and otherwise falls back to the
     * resolved name, so that a renamed, retargeted or unresolved target still produces text naming it.
     */
    public String importTargetName(final Import importRelationship, final DanglingReferenceIndex danglingReferences,
            final Function<Element, String> resolvedName) {
        final String parsedName = danglingReferences.targets(importRelationship).stream().findFirst().orElse("");
        final Element target = this.importTargetElement(importRelationship);
        String name = parsedName;
        if (!this.preserveParsedReferenceNames || name.isBlank()
                || !this.parsedNameDesignates(importRelationship, this.importReferenceName(importRelationship), parsedName, target)) {
            String modelName = "";
            if (target != null) {
                modelName = Objects.toString(this.referenceNameResolver.resolve(target, importRelationship), "");
            }
            boolean unresolvedWithSpelling = target != null && target.eIsProxy() && !parsedName.isBlank();
            if (modelName.isBlank() && !unresolvedWithSpelling) {
                modelName = SafeEmfAccess.get(() -> resolvedName.apply(target), "");
            }
            if (!modelName.isBlank()) {
                name = modelName;
            }
        }
        return name;
    }

    /**
     * Returns the element designated by an import: the member of a membership import (or the membership itself when it
     * is a proxy or has no member), the namespace of a namespace import, or {@code null} when it cannot be read.
     */
    public Element importTargetElement(final Import importRelationship) {
        return SafeEmfAccess.get(() -> this.uncheckedImportTargetElement(importRelationship), null);
    }

    /**
     * Returns the member text of a membership, such as the target of an alias ({@code alias A for <target>}): the
     * parser spelling while it still designates the installed member, else the fallback name of that member.
     */
    public String aliasTargetName(final Membership membership, final DanglingReferenceIndex danglingReferences) {
        final String parsedName = danglingReferences.targets(membership, EmfReferenceNames.MEMBER_ELEMENT).stream().findFirst().orElse("");
        String name = parsedName;
        if (parsedName.isBlank() || !this.parsedNameDesignates(membership, EmfReferenceNames.MEMBER_ELEMENT, parsedName,
                this.firstInstalledTarget(membership, EmfReferenceNames.MEMBER_ELEMENT))) {
            final String modelName = this.firstReferenceTargetName(membership, EmfReferenceNames.MEMBER_ELEMENT);
            if (!modelName.isBlank()) {
                name = modelName;
            }
        }
        return name;
    }

    /**
     * Tells whether a parser spelling may still be written for a reference whose installed target is {@code target}.
     * Nothing contradicts the spelling when no named target is installed (none, or an unresolved proxy) or when it is
     * a feature path, which the chain rendering checks segment by segment. Otherwise the spelling must still resolve
     * to the target, or end its qualified name when the spelling cannot be resolved; when it does not, the model was
     * renamed or retargeted after parsing and writing the spelling would reparse into a different model.
     *
     * @param context
     *            the element holding the reference, from which the spelling is resolved
     * @param referenceName
     *            the EMF reference name, used to ask the caller's path resolver about alias paths
     * @param parsedName
     *            the parser spelling
     * @param target
     *            the installed target, or {@code null}
     * @return {@code true} when the spelling may be written for the target
     */
    public boolean parsedNameDesignates(final Element context, final String referenceName, final String parsedName, final Element target) {
        if (target == null || target.eIsProxy() || parsedName.isBlank()
                || ReferenceNameSyntax.lastSeparatorOutsideQuotes(parsedName, SysMLTokens.PATH_SEPARATOR) >= 0) {
            return true;
        }
        return SafeEmfAccess.get(() -> this.parsedNameStillDesignates(context, referenceName, parsedName, target), true);
    }

    /**
     * Decides whether a spelling designates an installed target. A spelling that resolves names the element it
     * resolves to, unless the caller's path resolver confirms it, e.g. as an alias path. A spelling that definitely
     * fails, such as {@code Q::A} where an inner {@code Q} without {@code A} shadows an outer one, designates nothing.
     * A spelling the lookup cannot decide (a renamed target, an import, or a model whose names cannot be looked up)
     * designates the target only when it is a suffix of the target's qualified name.
     */
    private boolean parsedNameStillDesignates(final Element context, final String referenceName, final String parsedName, final Element target) {
        final ContainmentNameLookup.Lookup lookup = this.nameLookup.resolve(context, parsedName);
        boolean designated;
        if (lookup.membership() != null) {
            designated = StoredModel.memberElement(lookup.membership()) == target;
        } else if (lookup.definite()) {
            designated = false;
        } else {
            designated = this.isQualifiedNameSuffix(parsedName, target);
        }
        return designated || context.eClass().getEStructuralFeature(referenceName) instanceof final EReference reference
                && !this.referencePathResolver.resolve(context, reference, parsedName, target).isEmpty();
    }

    /** Tells whether the spelling ends the target's qualified name at a segment boundary, or is its short name. */
    private boolean isQualifiedNameSuffix(final String parsedName, final Element target) {
        final String spelling = ReferenceNameMatcher.normalized(parsedName);
        final String qualifiedName = ReferenceNameMatcher.normalized(Objects.toString(StoredModel.qualifiedName(target), ""));
        if (qualifiedName.isBlank()) {
            return this.hasMatchingReferenceLeaf(parsedName, target, "");
        }
        return qualifiedName.equals(spelling) || qualifiedName.endsWith(SysMLTokens.NAMESPACE_SEPARATOR + spelling)
                || ReferenceNameMatcher.equivalent(parsedName, new NameEscaper().escapeDeclaredName(StoredModel.shortName(target)));
    }

    /**
     * Tells whether the parser spellings of a redefinition still designate its installed redefined features, pairing
     * them by position. A simple name is checked against the members of the general types of the type owning the
     * redefining feature, where grammar rule {@code ownedRedefinition} finds it, because an ordinary lookup from the
     * redefining feature can find the redefining feature itself, implicitly named after its target. A partially
     * linked reference (different counts) is left to the parser spellings, whose positions no longer match the
     * installed targets.
     */
    public boolean parsedRedefinedNamesDesignateTargets(final Relationship relationship, final List<String> parsedNames) {
        List<Element> targets = List.of();
        if (relationship.eClass().getEStructuralFeature(EmfReferenceNames.REDEFINED_FEATURE) instanceof final EReference reference) {
            targets = this.installedTargets(relationship, reference);
        }
        boolean designated = true;
        if (targets.size() == parsedNames.size()) {
            for (int index = 0; designated && index < targets.size(); index++) {
                final String parsedName = parsedNames.get(index);
                final Element target = targets.get(index);
                designated = this.redefinedNameDesignates(relationship, parsedName, target);
            }
        }
        return designated;
    }

    /**
     * Returns the text of a relationship target looked up through the given reference names, in priority order: the
     * first model target among all names, then the first parser spelling, then the relationship's derived target.
     */
    String referenceName(final Relationship relationship, final DanglingReferenceIndex danglingReferences,
            final Function<Element, String> resolvedName, final String... referenceNames) {
        final String modelName = Arrays.stream(referenceNames)
                .map(referenceName -> this.referenceTargets(relationship, referenceName, danglingReferences, resolvedName))
                .filter(targets -> !targets.isEmpty())
                .map(targets -> targets.get(0).name())
                .filter(name -> !name.isBlank())
                .findFirst()
                .orElse("");
        String name = modelName;
        if (name.isBlank()) {
            name = Arrays.stream(referenceNames)
                    .map(referenceName -> danglingReferences.targets(relationship, referenceName).stream().findFirst().orElse(""))
                    .filter(parsedName -> !parsedName.isBlank())
                    .findFirst()
                    .orElseGet(() -> this.derivedTargetName(relationship, resolvedName));
        }
        return this.renderedReferenceName(relationship, name);
    }

    /**
     * Re-applies quoting to the last segment of a reference name when the source quoted it ({@code QUOTED_NAME}
     * marker) or when a membership target's last segment contains a dot, which would otherwise read as a feature path.
     */
    String renderedReferenceName(final Relationship relationship, final String name) {
        final int separator = ReferenceNameSyntax.lastSeparatorOutsideQuotes(name, SysMLTokens.NAMESPACE_SEPARATOR);
        int segmentStart = 0;
        if (separator >= 0) {
            segmentStart = separator + SysMLTokens.NAMESPACE_SEPARATOR.length();
        }
        final String qualifier = name.substring(0, segmentStart);
        final String lastSegment = name.substring(segmentStart);
        // Membership targets are qualified names, not dotted navigation paths.
        final boolean dottedMember = this.navigation.isMetaclass(relationship, SysMLPackage.eINSTANCE.getMembership())
                && lastSegment.contains(SysMLTokens.PATH_SEPARATOR);
        String renderedName = name;
        if (!name.isBlank() && relationship != null && (dottedMember || relationship.eAdapters().contains(ITextAdapter.QUOTED_NAME))) {
            renderedName = qualifier + this.quotedName(lastSegment);
        }
        return renderedName;
    }

    /**
     * Decides whether the parser spelling replaces the rendered model name. Rendered names for unnamed or conjugated
     * targets are never reusable; otherwise the parsed name must keep the same spelling intent (round-trip mode or same
     * leaf) and still designate the model target.
     */
    boolean shouldUseParsedReferenceName(final Element context, final String referenceName, final String parsedName, final Element target,
            final String resolvedName) {
        final boolean useParsedName;
        if (target.eIsProxy()) {
            // A caller may know the current identity behind a proxy, including a renamed external namespace.
            useParsedName = this.preserveParsedReferenceNames || Objects.toString(this.referenceNameResolver.resolve(target, context), "").isBlank();
        } else if (resolvedName.contains(UNNAMED_QUALIFIED_NAME_SUFFIX) || resolvedName.contains(QUALIFIED_QUOTED_CONJUGATION_MARKER)) {
            useParsedName = true;
        } else {
            final boolean preservesSpelling = this.preserveParsedReferenceNames || this.hasMatchingReferenceLeaf(parsedName, target, resolvedName);
            useParsedName = preservesSpelling && this.parsedNameMatchesTarget(context, referenceName, parsedName, target);
        }
        return useParsedName;
    }

    /** Returns the parser spelling of a named reference, falling back to the fallback name of its first model target. */
    String danglingName(final Element context, final DanglingReferenceIndex danglingReferences, final String referenceName) {
        return danglingReferences.targets(context, referenceName).stream()
                .findFirst()
                .filter(parsedName -> !parsedName.isBlank())
                .orElseGet(() -> this.firstReferenceTargetName(context, referenceName));
    }

    /**
     * Returns the spelling of one feature chain segment, such as a {@code chainingFeature} or the member of a feature
     * chain expression, without source quoting. The parser spelling is kept while nothing is installed, the installed
     * target is an unresolved proxy, or the spelling still names the target. Once the target was renamed or
     * retargeted, a segment that follows a path is written by the target's own name, the feature navigated from the
     * preceding segment, and a leading segment by the target's fallback name, like any other reference; writing the
     * stale spelling would reparse into a different model.
     *
     * @param relationship
     *            the relationship holding the segment target
     * @param referenceName
     *            the EMF reference holding the segment target
     * @param danglingReferences
     *            the parser spellings
     * @param followsPath
     *            whether the segment is navigated from a preceding segment or chain source
     * @return the segment text, or an empty string
     */
    String chainSegmentName(final Relationship relationship, final String referenceName, final DanglingReferenceIndex danglingReferences,
            final boolean followsPath) {
        final String parsedName = danglingReferences.targets(relationship, referenceName).stream().findFirst().orElse("");
        final Element target = this.firstInstalledTarget(relationship, referenceName);
        String name = "";
        if (target != null && !target.eIsProxy()) {
            if (followsPath) {
                name = this.renamedSegmentName(parsedName, target);
            } else if (parsedName.isBlank() || !this.parsedNameDesignates(relationship, referenceName, parsedName, target)) {
                name = this.firstReferenceTargetName(relationship, referenceName);
            }
        }
        if (name.isBlank()) {
            name = this.danglingName(relationship, danglingReferences, referenceName);
        }
        return name;
    }

    /** Returns the escaped name of a target navigated from a path, unless the parser spelling still names it. */
    private String renamedSegmentName(final String parsedName, final Element target) {
        final String targetName = Objects.toString(StoredModel.name(target), "");
        String name = "";
        if (!targetName.isBlank() && (parsedName.isBlank() || !this.hasMatchingReferenceLeaf(parsedName, target, targetName))) {
            name = new NameEscaper().escapeDeclaredName(targetName);
        }
        return name;
    }

    /**
     * Returns the fallback name of the first target of a named reference, resolving proxies in the context and
     * preferring the caller's contextual name; empty when nothing can be named.
     */
    String firstReferenceTargetName(final Element context, final String referenceName) {
        String name = "";
        if (context != null && context.eClass().getEStructuralFeature(referenceName) instanceof final EReference reference) {
            name = SafeEmfAccess.get(() -> this.resolveFirstReferenceTargetName(context, reference), "");
        }
        return name;
    }

    /** Returns the URI leaf of the first unresolved proxy among the textual cross-references of an element. */
    String firstProxyReferenceName(final Element context) {
        return this.textualCrossReferences(context)
                .map(reference -> SafeEmfAccess.get(() -> this.proxyReferenceName(context, reference), ""))
                .filter(name -> !name.isBlank())
                .findFirst()
                .orElse("");
    }

    /**
     * Returns the fallback name of the first target among the textual cross-references of an element, used when no
     * parser spelling exists, e.g. for targets installed in another Resource.
     */
    String firstExternalModelReferenceName(final Element context) {
        return this.textualCrossReferences(context)
                .map(reference -> this.firstReferenceTargetName(context, reference.getName()))
                .filter(name -> !name.isBlank())
                .findFirst()
                .orElse("");
    }

    /**
     * Returns the last path or fragment segment of a proxy URI, the only name available for an unresolved target.
     * {@code NameRenderer} names proxies by their whole URI fragment instead.
     */
    String proxyLeafName(final InternalEObject proxy) {
        final String uri = proxy.eProxyURI().toString();
        final int separator = Math.max(uri.lastIndexOf('/'), uri.lastIndexOf('#'));
        return uri.substring(separator + 1);
    }

    /**
     * Names an unresolved proxy without resolving it: the caller's contextual name, which may come from the caller's own
     * knowledge of the proxy, else the leaf of its URI.
     */
    private String proxyName(final InternalEObject proxy, final Element context) {
        final String contextualName = this.referenceNameResolver.resolve((Element) proxy, context);
        if (contextualName != null && !contextualName.isBlank()) {
            return contextualName;
        }
        return this.proxyLeafName(proxy);
    }

    /** Returns the unquoted last segment of a qualified name or feature path, to compare spellings of one target. */
    String referenceLeaf(final String name) {
        final String unquoted = name.replace(SysMLTokens.UNRESTRICTED_NAME_DELIMITER, "");
        final int namespaceSeparator = unquoted.lastIndexOf(SysMLTokens.NAMESPACE_SEPARATOR);
        final int pathSeparator = unquoted.lastIndexOf(SysMLTokens.PATH_SEPARATOR);
        String leaf = unquoted;
        if (namespaceSeparator > pathSeparator) {
            leaf = unquoted.substring(namespaceSeparator + SysMLTokens.NAMESPACE_SEPARATOR.length());
        } else if (pathSeparator >= 0) {
            leaf = unquoted.substring(pathSeparator + SysMLTokens.PATH_SEPARATOR.length());
        }
        return leaf;
    }

    private List<ModelReference> namedTargets(final Element context, final String referenceName, final EReference reference,
            final DanglingReferenceIndex danglingReferences, final Function<Element, String> resolvedName) {
        final List<Element> modelTargets = this.installedTargets(context, reference);
        final List<String> parsedNames = danglingReferences.targets(context, referenceName);
        if (parsedNames.size() > modelTargets.size()) {
            return this.partiallyLinkedTargets(context, referenceName, parsedNames, modelTargets, resolvedName);
        }
        final List<ModelReference> targets = new ArrayList<>();
        for (int index = 0; index < modelTargets.size(); index++) {
            String parsedName = "";
            if (index < parsedNames.size()) {
                parsedName = parsedNames.get(index);
            }
            this.addModelReference(targets, context, referenceName, parsedName, modelTargets.get(index), resolvedName);
        }
        return targets;
    }

    /**
     * Pairs the parser spellings of a partially linked reference with its installed targets, in both modes, so that
     * no unresolved spelling is lost. Linking installs only the targets it resolved, in source order, so the installed
     * targets follow the spellings as a subsequence: a spelling that designates the next installed target is paired
     * with it, a surplus spelling stays unresolved, and when no spelling is left over the next installed target was
     * renamed or retargeted and is named from the model.
     */
    private List<ModelReference> partiallyLinkedTargets(final Element context, final String referenceName, final List<String> parsedNames,
            final List<Element> modelTargets, final Function<Element, String> resolvedName) {
        final List<ModelReference> targets = new ArrayList<>();
        int next = 0;
        for (int index = 0; index < parsedNames.size(); index++) {
            final String parsedName = parsedNames.get(index);
            final boolean surplus = parsedNames.size() - index > modelTargets.size() - next;
            if (next < modelTargets.size() && (!surplus || this.isPairedSpelling(context, referenceName, parsedName, modelTargets.get(next),
                    resolvedName))) {
                this.addModelReference(targets, context, referenceName, parsedName, modelTargets.get(next), resolvedName);
                next++;
            } else {
                targets.add(new ModelReference(parsedName, null));
            }
        }
        return targets;
    }

    /**
     * Tells whether a spelling was written for an installed target: an unresolved proxy is recognized by its URI
     * leaf, a resolved target by {@link #parsedNameDesignates}.
     */
    private boolean isPairedSpelling(final Element context, final String referenceName, final String parsedName, final Element target,
            final Function<Element, String> resolvedName) {
        final Element namingTarget = this.namingTarget(target);
        if (namingTarget.eIsProxy()) {
            return this.isNamedBy(context, parsedName, namingTarget, resolvedName);
        }
        return this.parsedNameDesignates(context, referenceName, parsedName, namingTarget);
    }

    /**
     * Adds the reference to an installed target, resolving a proxy in the context: the parser spelling when
     * {@link #shouldUseParsedReferenceName} accepts it, else the rendered name of the target. A target without a
     * name is skipped. A conjugated port definition has no name of its own and is written <code>&#126;P</code> after
     * the port definition it conjugates, so that renaming {@code P} renames the reference too.
     */
    private void addModelReference(final List<ModelReference> targets, final Element context, final String referenceName, final String parsedName,
            final Element installedTarget, final Function<Element, String> resolvedName) {
        Element nameTarget = StoredModel.originalPortDefinition(installedTarget);
        String prefix = "";
        String spelling = parsedName;
        if (nameTarget != null) {
            prefix = SysMLTokens.CONJUGATION_OPERATOR;
            spelling = ReferenceNameSyntax.withoutConjugation(parsedName);
        } else {
            nameTarget = this.namingTarget(installedTarget);
        }
        String name = this.renderTargetName(context, nameTarget, resolvedName);
        if (!spelling.isBlank() && this.shouldUseParsedReferenceName(context, referenceName, spelling, nameTarget, name)) {
            name = spelling;
        }
        if (!name.isBlank()) {
            targets.add(new ModelReference(prefix + name, installedTarget));
        }
    }

    private boolean isNamedBy(final Element context, final String parsedName, final Element namingTarget,
            final Function<Element, String> resolvedName) {
        return this.hasMatchingReferenceLeaf(parsedName, namingTarget, this.renderTargetName(context, namingTarget, resolvedName));
    }

    /**
     * Tells whether a parsed name still designates the target: proxies keep their spelling, otherwise the name must
     * resolve to the target from an enclosing namespace or through the caller's path resolver.
     */
    private boolean parsedNameMatchesTarget(final Element context, final String referenceName, final String name, final Element target) {
        if (target.eIsProxy() || this.parsedNameResolvesToTarget(context, name, target)) {
            return true;
        }
        return context.eClass().getEStructuralFeature(referenceName) instanceof final EReference reference
                && !this.referencePathResolver.resolve(context, reference, name, target).isEmpty();
    }

    /** Resolves the name from the innermost enclosing namespace binding its first segment, which decides alone. */
    private boolean parsedNameResolvesToTarget(final Element context, final String name, final Element target) {
        final Membership membership = this.nameLookup.resolve(context, name).membership();
        return membership != null && StoredModel.memberElement(membership) == target;
    }

    private boolean hasMatchingReferenceLeaf(final String parsedName, final Element target, final String resolvedName) {
        return this.hasSameReferenceLeaf(parsedName, resolvedName)
                || this.hasSameReferenceLeaf(parsedName, Objects.toString(StoredModel.name(target), ""))
                || this.hasSameReferenceLeaf(parsedName, Objects.toString(StoredModel.shortName(target), ""));
    }

    private boolean hasSameReferenceLeaf(final String first, final String second) {
        return !second.isBlank() && this.referenceLeaf(first).equals(this.referenceLeaf(second));
    }

    /** Unwraps membership and naming-feature indirection; a proxy is its own naming target. */
    private Element namingTarget(final Element target) {
        Element namingTarget = target;
        if (target instanceof final Membership membership && StoredModel.memberElement(membership) != null) {
            namingTarget = StoredModel.memberElement(membership);
        }
        final Element unnamedFeatureTarget = this.unnamedReferenceTarget(namingTarget);
        if (unnamedFeatureTarget != null) {
            namingTarget = unnamedFeatureTarget;
        }
        return namingTarget;
    }

    /**
     * Returns the feature that names an anonymous feature (its naming feature or reference-subsetting target), such as
     * the anonymous usage created for a feature chain, or {@code null} when the feature is named or has none.
     */
    private Element unnamedReferenceTarget(final Element target) {
        Element namingFeature = null;
        if (target instanceof final Feature feature && feature.getDeclaredName() == null && feature.getDeclaredShortName() == null
                && StoredModel.name(feature) == null) {
            namingFeature = StoredModel.namingFeature(feature);
            if (namingFeature == feature) {
                namingFeature = null;
            }
            if (namingFeature == null) {
                namingFeature = this.navigation.ownedRelationships(feature).stream()
                        .filter(ReferenceSubsetting.class::isInstance)
                        .map(ReferenceSubsetting.class::cast)
                        .map(referenceSubsetting -> StoredModel.value(referenceSubsetting, SysMLPackage.eINSTANCE.getReferenceSubsetting_ReferencedFeature()))
                        .filter(Objects::nonNull)
                        .findFirst()
                        .orElse(null);
            }
        }
        return namingFeature;
    }

    /** Copies installed Element targets without resolving proxies, treating guarded failures as an empty list. */
    private List<Element> installedTargets(final Element context, final EReference reference) {
        return SafeEmfAccess.get(() -> SafeEmfAccess.unresolvedValues(context, reference).stream()
                .filter(Element.class::isInstance)
                .map(Element.class::cast)
                .toList(), List.of());
    }

    /**
     * Returns the naming target of a relationship through its primary reference (referenced, redefined, subsetted
     * feature or type), falling back to the relationship's derived target.
     */
    private Element primaryRelationshipTargetElement(final Relationship relationship) {
        Element target = null;
        if (relationship instanceof final ReferenceSubsetting referenceSubsetting) {
            target = StoredModel.value(referenceSubsetting, SysMLPackage.eINSTANCE.getReferenceSubsetting_ReferencedFeature());
        }
        final String referenceName = this.primaryReferenceName(relationship);
        if (target == null && !referenceName.isBlank()) {
            target = this.firstInstalledTarget(relationship, referenceName);
        }
        if (target == null) {
            target = this.firstRelationshipTargetElement(relationship);
        }
        if (target != null) {
            target = this.namingTarget(target);
        }
        return target;
    }

    private String primaryReferenceName(final Relationship relationship) {
        String referenceName = "";
        if (relationship instanceof ReferenceSubsetting) {
            referenceName = EmfReferenceNames.REFERENCED_FEATURE;
        } else if (relationship instanceof Redefinition) {
            referenceName = EmfReferenceNames.REDEFINED_FEATURE;
        } else if (relationship instanceof Subsetting) {
            referenceName = EmfReferenceNames.SUBSETTED_FEATURE;
        } else if (relationship instanceof FeatureTyping) {
            referenceName = EmfReferenceNames.TYPE;
        }
        return referenceName;
    }

    private Element firstInstalledTarget(final Relationship relationship, final String referenceName) {
        Element target = null;
        if (relationship.eClass().getEStructuralFeature(referenceName) instanceof final EReference reference) {
            target = this.installedTargets(relationship, reference).stream().findFirst().orElse(null);
        }
        return target;
    }

    /**
     * Decides whether one redefinition spelling designates its installed target. Qualified names and feature paths use
     * {@link #parsedNameDesignates}. A simple name must name the target, by name or short name, and the target must be
     * a member of a general type of the type owning the redefining feature, an enclosing element, or what the name
     * resolves to; a same-named feature of an unrelated type is a retarget the simple name cannot express.
     */
    private boolean redefinedNameDesignates(final Relationship relationship, final String parsedName, final Element target) {
        if (target == null || target.eIsProxy() || !this.isSimpleName(parsedName)) {
            return this.parsedNameDesignates(relationship, EmfReferenceNames.REDEFINED_FEATURE, parsedName, target);
        }
        final boolean namesTarget = this.hasSameReferenceLeaf(parsedName, Objects.toString(StoredModel.name(target), ""))
                || this.hasSameReferenceLeaf(parsedName, Objects.toString(StoredModel.shortName(target), ""));
        return namesTarget && (EcoreUtil.isAncestor(target, relationship)
                || SafeEmfAccess.get(() -> this.isMemberOfGeneralType(relationship, target), false)
                || SafeEmfAccess.get(() -> this.parsedNameResolvesToTarget(relationship, parsedName, target), false));
    }

    /** Tells whether a spelling is a single name, neither qualified nor a feature path. */
    private boolean isSimpleName(final String name) {
        return !name.isBlank()
                && ReferenceNameSyntax.lastSeparatorOutsideQuotes(name, SysMLTokens.NAMESPACE_SEPARATOR) < 0
                && ReferenceNameSyntax.lastSeparatorOutsideQuotes(name, SysMLTokens.PATH_SEPARATOR) < 0;
    }

    /**
     * Tells whether the target is a member of a general type of the type owning the redefining feature. Inherited
     * memberships cannot be used: they exclude the features that the owned features redefine.
     */
    private boolean isMemberOfGeneralType(final Relationship redefinition, final Element target) {
        Type owningType = null;
        if (redefinition.getOwningRelatedElement() instanceof final Feature redefiningFeature) {
            owningType = StoredModel.owningType(redefiningFeature);
        }
        return owningType != null && this.generalTypes(owningType).stream()
                .anyMatch(general -> this.navigation.ownedRelationships(general).stream()
                        .anyMatch(relationship -> relationship instanceof final Membership membership && StoredModel.memberElement(membership) == target));
    }

    /** Returns the general types of a type and of their own general types, through explicit specializations only. */
    private Set<Type> generalTypes(final Type type) {
        final Set<Type> generals = Collections.newSetFromMap(new IdentityHashMap<>());
        final Deque<Type> pending = new ArrayDeque<>(List.of(type));
        while (!pending.isEmpty()) {
            this.navigation.ownedRelationships(pending.pop()).stream()
                    .filter(Specialization.class::isInstance)
                    .map(this::installedPrimaryTarget)
                    .filter(general -> general instanceof Type && !general.eIsProxy())
                    .map(Type.class::cast)
                    .filter(generals::add)
                    .forEach(pending::push);
        }
        return generals;
    }

    /** Returns the installed target of the relationship's primary reference, else its derived target, unresolved. */
    private Element installedPrimaryTarget(final Relationship relationship) {
        final String referenceName = this.primaryReferenceName(relationship);
        Element target = null;
        if (!referenceName.isBlank()) {
            target = this.firstInstalledTarget(relationship, referenceName);
        }
        if (target == null) {
            target = this.firstRelationshipTargetElement(relationship);
        }
        return target;
    }

    /** Names the EMF reference that holds the imported membership or namespace of an import. */
    private String importReferenceName(final Import importRelationship) {
        String referenceName = EmfReferenceNames.IMPORTED_MEMBERSHIP;
        if (importRelationship instanceof NamespaceImport) {
            referenceName = EmfReferenceNames.IMPORTED_NAMESPACE;
        }
        return referenceName;
    }

    private Element uncheckedImportTargetElement(final Import importRelationship) {
        Element target = null;
        if (importRelationship instanceof final MembershipImport membershipImport) {
            target = this.importedMembershipTarget(StoredModel.value(membershipImport, SysMLPackage.eINSTANCE.getMembershipImport_ImportedMembership()));
        } else if (importRelationship instanceof final NamespaceImport namespaceImport) {
            target = StoredModel.value(namespaceImport, SysMLPackage.eINSTANCE.getNamespaceImport_ImportedNamespace());
        }
        return target;
    }

    /** A proxy or member-less imported membership is itself the only nameable target. */
    private Element importedMembershipTarget(final Element importedElement) {
        Element target = importedElement;
        if (importedElement instanceof final Membership importedMembership && !importedMembership.eIsProxy()
                && StoredModel.memberElement(importedMembership) != null) {
            target = StoredModel.memberElement(importedMembership);
        }
        return target;
    }

    private String quotedName(final String name) {
        String quotedName = name;
        final boolean alreadyQuoted = name.length() > 1
                && name.startsWith(SysMLTokens.UNRESTRICTED_NAME_DELIMITER)
                && name.endsWith(SysMLTokens.UNRESTRICTED_NAME_DELIMITER);
        if (!alreadyQuoted) {
            quotedName = new NameEscaper().escapeUnrestrictedName(name);
        }
        return quotedName;
    }

    private String resolveFirstReferenceTargetName(final Element context, final EReference reference) {
        return SafeEmfAccess.unresolvedValues(context, reference).stream()
                .filter(Element.class::isInstance)
                .map(target -> this.contextualFallbackTargetName((Element) target, context))
                .filter(candidate -> !candidate.isBlank())
                .findFirst()
                .orElse("");
    }

    private String proxyReferenceName(final Element context, final EReference reference) {
        return SafeEmfAccess.unresolvedValues(context, reference).stream()
                .filter(candidate -> candidate instanceof final InternalEObject proxy && proxy.eIsProxy())
                .map(proxy -> this.proxyLeafName((InternalEObject) proxy))
                .findFirst()
                .orElse("");
    }

    /** Lists the references that the textual notation writes as names: changeable, non-derived cross-references. */
    private Stream<EReference> textualCrossReferences(final Element context) {
        return context.eClass().getEAllReferences().stream()
                .filter(reference -> !reference.isContainment() && !reference.isContainer())
                .filter(reference -> !reference.isDerived() && reference.isChangeable());
    }

    /** Returns the fallback name of a resolved target, replaced by the caller's contextual name when there is one. */
    private String contextualFallbackTargetName(final Element target, final Element context) {
        String name = this.fallbackTargetName(target);
        if (!name.isBlank()) {
            // Fallback names still need the precise referencing relationship's namespace, including expression memberships.
            final String contextualName = this.referenceNameResolver.resolve(target, context);
            if (contextualName != null && !contextualName.isBlank()) {
                name = contextualName;
            }
        }
        return name;
    }

    /** Names a target by its qualified name, else its declared name, else its effective name; a proxy by its URI leaf. */
    private String fallbackTargetName(final Element target) {
        if (target instanceof final InternalEObject proxy && proxy.eIsProxy()) {
            return this.proxyLeafName(proxy);
        }
        return Stream.of(StoredModel.qualifiedName(target), target.getDeclaredName())
                .filter(name -> name != null && !name.isBlank())
                .findFirst()
                .orElseGet(() -> Objects.toString(StoredModel.name(target), ""));
    }

    /** Names the relationship's derived target, the last resort when no reference of the relationship can be named. */
    private String derivedTargetName(final Relationship relationship, final Function<Element, String> resolvedName) {
        final Element target = this.firstRelationshipTargetElement(relationship);
        String name = "";
        if (target != null) {
            name = SafeEmfAccess.get(() -> resolvedName.apply(target), "");
        }
        return name;
    }

    /**
     * Returns the first parser spelling of a relationship target, or the fallback name of the installed target when
     * the model was renamed or retargeted so that the spelling no longer designates it. Empty when the parser recorded
     * no spelling for the relationship. Source quoting is not re-applied.
     */
    public String currentParsedTargetName(final Relationship relationship, final DanglingReferenceIndex danglingReferences) {
        String name = danglingReferences.targets(relationship).stream().findFirst().orElse("");
        final Element target = this.installedPrimaryTarget(relationship);
        boolean designated;
        if (relationship instanceof Redefinition) {
            designated = this.redefinedNameDesignates(relationship, name, target);
        } else {
            designated = this.parsedNameDesignates(relationship, this.primaryReferenceName(relationship), name, target);
        }
        if (!name.isBlank() && !designated) {
            final String modelName = SafeEmfAccess.get(() -> this.contextualFallbackTargetName(this.namingTarget(target),
                    relationship), "");
            if (!modelName.isBlank()) {
                name = modelName;
            }
        }
        return name;
    }

    /**
     * Returns the parser spelling of a relationship target while it still designates the installed target, else the
     * fallback name of the relationship's first cross-reference.
     */
    private String relationshipTargetText(final Relationship relationship, final DanglingReferenceIndex danglingReferences) {
        String name = this.currentParsedTargetName(relationship, danglingReferences);
        if (name.isBlank()) {
            name = this.firstExternalModelReferenceName(relationship);
        }
        return this.renderedReferenceName(relationship, name);
    }

    private String renderTargetName(final Element context, final Element target, final Function<Element, String> resolvedName) {
        if (target instanceof final InternalEObject proxy && proxy.eIsProxy()) {
            return this.proxyName(proxy, context);
        }
        return resolvedName.apply(target);
    }
}
