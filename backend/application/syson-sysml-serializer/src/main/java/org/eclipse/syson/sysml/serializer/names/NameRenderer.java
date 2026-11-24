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
package org.eclipse.syson.sysml.serializer.names;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.syson.sysml.parser.adapter.ITextAdapter;
import org.eclipse.syson.sysml.serializer.ReferenceNameResolver;
import org.eclipse.syson.sysml.serializer.model.StoredModel;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Membership;
import org.omg.sysml.lang.sysml.Namespace;
import org.omg.sysml.lang.sysml.OwningMembership;
import org.omg.sysml.lang.sysml.Relationship;

/**
 * Renders model names for textual SysML using a context stack and lazy model indexes.
 * Model contents and names must stay stable while the cached indexes are reused.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class NameRenderer {

    private static final ReferenceNameResolver NO_CONTEXTUAL_NAMES =
            (target, context) -> "";

    private final NameEscaper escaper = new NameEscaper();

    private final ReferenceNameResolver referenceNameResolver;

    private final boolean contextualReferenceNames;

    private final Deque<Element> contexts = new ArrayDeque<>();

    private final Map<Element, String> qualifiedNames =
            new IdentityHashMap<>();

    private final Map<Namespace, Map<String, Element>> firstMembersByName =
            new IdentityHashMap<>();

    private final Map<String, String> referenceNames = new HashMap<>();

    private final Map<Element, ReferenceContextIndex> referenceContextsByRoot =
            new IdentityHashMap<>();

    /** Creates a renderer that writes qualified names and never consults contextual names. */
    public NameRenderer() {
        this(NO_CONTEXTUAL_NAMES, false);
    }

    /**
     * Creates a renderer that asks {@code referenceNameResolver} for a contextual name first and falls back to
     * qualified names when it has no answer.
     *
     * @param referenceNameResolver
     *            the caller-defined resolver of contextual names
     */
    public NameRenderer(final ReferenceNameResolver referenceNameResolver) {
        this(referenceNameResolver, true);
    }

    private NameRenderer(final ReferenceNameResolver referenceNameResolver,
            final boolean contextualReferenceNames) {
        this.referenceNameResolver = Objects
                .requireNonNull(referenceNameResolver);
        this.contextualReferenceNames = contextualReferenceNames;
    }

    /**
     * Enters the name scope of an element being rendered; references rendered until the matching
     * {@link #endContext()} are resolved from that element. Calls must nest.
     *
     * @param context
     *            the element whose syntax is being rendered
     */
    public void beginContext(final Element context) {
        this.contexts.push(context);
    }

    /** Leaves the scope entered by the last {@link #beginContext(Element)}. */
    public void endContext() {
        this.contexts.pop();
    }

    /**
     * Returns the element's declared name in SysML syntax, quoted only when needed (non-identifier or keyword).
     *
     * @param element
     *            the element, or {@code null}
     * @return the escaped declared name, or an empty string when there is none
     */
    public String declaredName(final Element element) {
        if (element == null || element.getDeclaredName() == null) {
            return "";
        }
        return this.escaper.escapeDeclaredName(element.getDeclaredName());
    }

    /**
     * Returns the element's declared short name in SysML syntax. A short name that the source wrote quoted stays
     * quoted, so round-trip keeps {@code <'x'>} even when {@code <x>} would be valid.
     *
     * @param element
     *            the element, or {@code null}
     * @return the escaped short name, or an empty string when there is none
     */
    public String declaredShortName(final Element element) {
        String shortName = "";
        if (element != null && element.getDeclaredShortName() != null) {
            shortName = element.getDeclaredShortName();
            if (element.eAdapters().contains(ITextAdapter.QUOTED_SHORT_NAME)) {
                shortName = this.escaper.escapeUnrestrictedName(shortName);
            } else {
                shortName = this.escaper.escapeDeclaredName(shortName);
            }
        }
        return shortName;
    }

    /**
     * Returns the spelling used to reference {@code element} from the current context. In order of preference: the
     * caller's contextual name, then, for an unresolved proxy, the fragment of its URI, and otherwise the qualified name
     * when every step is unambiguous in its namespace, then the declared or effective name. A proxy is never resolved.
     *
     * @param element
     *            the referenced element, or {@code null}
     * @return the escaped reference name, or an empty string when nothing can be written
     */
    public String referenceName(final Element element) {
        String name = "";
        if (element != null && element.eIsProxy()) {
            name = this.contextualName(element)
                    .or(() -> this.proxyFragmentName(element))
                    .orElseGet(() -> this.referenceNameWithoutProxyFragment(element));
        } else if (element != null) {
            name = this.referenceNameWithoutProxyFragment(element);
        }
        return name;
    }

    /** Returns the caller's name for the element at the current reference site, when contextual naming applies. */
    private Optional<String> contextualName(final Element element) {
        Optional<String> name = Optional.empty();
        if (this.contextualReferenceNames && !this.contexts.isEmpty()) {
            final String contextualName = this.referenceNameResolver
                    .resolve(element, this.referenceContext(element));
            if (contextualName != null && !contextualName.isBlank()) {
                name = Optional.of(this.referenceName(contextualName));
            }
        }
        return name;
    }

    /** Returns the escaped URI fragment of an unresolved proxy, the only name it has, as a reference spelling. */
    private Optional<String> proxyFragmentName(final Element element) {
        Optional<String> proxyName = Optional.empty();
        if (element instanceof final InternalEObject internal
                && internal.eIsProxy() && internal.eProxyURI() != null) {
            final URI uri = internal.eProxyURI();
            final String fragment = uri.fragment();
            if (fragment != null && !fragment.isBlank()) {
                proxyName = Optional.of(this.referenceName(fragment));
            }
        }
        return proxyName;
    }

    private String referenceNameWithoutProxyFragment(final Element element) {
        final Optional<String> contextualName = this.contextualName(element);
        if (contextualName.isPresent()) {
            return contextualName.get();
        }
        final String qualifiedName = this.cachedQualifiedName(element);
        String referenceName = this.declaredName(element);
        if (qualifiedName != null && !qualifiedName.isBlank()) {
            referenceName = this.referenceName(qualifiedName);
        } else if (referenceName.isBlank()) {
            final String name = effectiveName(element);
            if (name != null) {
                referenceName = this.escaper.escapeDeclaredName(name);
            }
        }
        return referenceName;
    }

    /**
     * Narrows the current context to the first relationship inside it that targets {@code target}, so the contextual
     * resolver sees the actual reference site rather than the enclosing declaration.
     */
    private Element referenceContext(final Element target) {
        final Element context = this.contexts.peek();
        Element result = context;
        if (!(context instanceof Relationship)) {
            final Element root = containmentRoot(context);
            final ReferenceContextIndex index = this.referenceContextsByRoot
                    .computeIfAbsent(root, ReferenceContextIndex::new);
            final Relationship relationship = index.firstReferenceIn(context,
                    target);
            if (relationship != null) {
                result = relationship;
            }
        }
        return result;
    }

    private static Element containmentRoot(final Element element) {
        Element root = element;
        EObject container = root.eContainer();
        while (container instanceof final Element parent) {
            root = parent;
            container = parent.eContainer();
        }
        return root;
    }

    private static Element unwrapOwningMembership(final Element target) {
        Element candidate = target;
        if (candidate instanceof final OwningMembership membership
                && StoredModel.memberElement(membership) != null) {
            candidate = StoredModel.memberElement(membership);
        }
        return candidate;
    }

    /**
     * Returns the qualified name of {@code element}, or {@code null} when some step is unnamed or shadowed by an
     * earlier member with the same name, since such a path would resolve to another element. A step whose name is
     * shadowed is written with its short name when that one is not.
     */
    private String cachedQualifiedName(final Element element) {
        // Null is a cached result when no unambiguous qualified name can be built.
        if (this.qualifiedNames.containsKey(element)) {
            return this.qualifiedNames.get(element);
        }
        final Namespace owningNamespace = StoredModel.owningNamespace(element);
        String result = null;
        String segment = StoredModel.name(element);
        if (owningNamespace != null && (segment == null || !this.isFirstMemberWithName(owningNamespace, segment, element))) {
            segment = StoredModel.shortName(element);
        }
        if (owningNamespace != null && segment != null
                && this.isFirstMemberWithName(owningNamespace, segment, element)) {
            result = this.buildQualifiedName(this.escaper.escapeDeclaredName(segment), owningNamespace);
        }
        this.qualifiedNames.put(element, result);
        return result;
    }

    private String buildQualifiedName(final String segment,
            final Namespace owningNamespace) {
        if (StoredModel.owner(owningNamespace) == null) {
            return segment;
        }
        final String qualification = this.cachedQualifiedName(owningNamespace);
        String qualifiedName = null;
        if (qualification != null) {
            qualifiedName = qualification + SysMLTokens.NAMESPACE_SEPARATOR + segment;
        }
        return qualifiedName;
    }

    private boolean isFirstMemberWithName(
            final Namespace owningNamespace, final String name, final Element element) {
        final Map<String, Element> firstMembers = this.firstMembersByName
                .computeIfAbsent(owningNamespace,
                        NameRenderer::indexFirstMembersByName);
        return firstMembers.get(name) == element;
    }

    /**
     * Indexes the explicit memberships of a namespace, aliases included, by member name and short name, keeping the
     * member of the first one, since name lookup matches both.
     */
    private static Map<String, Element> indexFirstMembersByName(
            final Namespace owningNamespace) {
        final Map<String, Element> firstMembers = new HashMap<>();
        for (final Relationship relationship : owningNamespace
                .getOwnedRelationship()) {
            if (!relationship.isImplied()
                    && relationship instanceof final Membership membership) {
                final Element member = StoredModel.memberElement(membership);
                for (final String name : Arrays.asList(StoredModel.memberName(membership), StoredModel.memberShortName(membership))) {
                    if (name != null && !firstMembers.containsKey(name)) {
                        firstMembers.put(name, member);
                    }
                }
            }
        }
        return firstMembers;
    }

    /** Returns the effective long name, or the effective short name when the element has no long name. */
    private static String effectiveName(final Element element) {
        final String name = StoredModel.name(element);
        if (name != null) {
            return name;
        }
        return StoredModel.shortName(element);
    }

    /**
     * Escapes a reference spelling that is already structured as SysML ({@code ::}, {@code .}, <code>&#126;</code>); quoted
     * segments are kept, so the method is idempotent. Results are cached for the run.
     *
     * @param name
     *            the raw reference spelling
     * @return the escaped reference, or an empty string for a blank name
     */
    public String referenceName(final String name) {
        return this.referenceNames.computeIfAbsent(name,
                this.escaper::escapeReferenceName);
    }

    /** References indexed by containment position for one model root. */
    private static final class ReferenceContextIndex {

        private final Map<EObject, Integer> starts = new IdentityHashMap<>();

        private final Map<EObject, Integer> ends = new IdentityHashMap<>();

        private final Map<Element, List<PositionedRelationship>> byTarget =
                new IdentityHashMap<>();

        private final Map<String, List<PositionedRelationship>> byElementId =
                new HashMap<>();

        /**
         * Numbers the containment tree in pre-order, so a subtree is a contiguous position interval. Implied
         * relationships and their subtrees are never written, so they are not reference sites and are skipped.
         */
        private ReferenceContextIndex(final Element root) {
            int position = 0;
            this.starts.put(root, position++);
            final Deque<TraversalFrame> frames = new ArrayDeque<>();
            frames.push(new TraversalFrame(root, explicitContents(root)));
            while (!frames.isEmpty()) {
                final TraversalFrame frame = frames.peek();
                if (frame.children().hasNext()) {
                    final EObject child = frame.children().next();
                    final int childPosition = position++;
                    this.starts.put(child, childPosition);
                    if (child instanceof final Relationship relationship) {
                        this.indexRelationshipTargets(relationship, childPosition);
                    }
                    frames.push(new TraversalFrame(child, explicitContents(child)));
                } else {
                    this.ends.put(frame.object(), position - 1);
                    frames.pop();
                }
            }
        }

        /** Iterates over the contents of an object, except the implied relationships and thus their subtrees. */
        private static Iterator<EObject> explicitContents(final EObject object) {
            return object.eContents().stream()
                    .filter(child -> !(child instanceof final Relationship relationship && relationship.isImplied()))
                    .iterator();
        }

        /**
         * Returns the first relationship strictly inside {@code context} that targets {@code target}, or {@code null}.
         * Targets are also matched by element id, so a reference to another copy of the same element is found.
         */
        Relationship firstReferenceIn(final Element context,
                final Element target) {
            final Integer start = this.starts.get(context);
            final Integer end = this.ends.get(context);
            if (start == null || end == null) {
                return null;
            }
            PositionedRelationship first = firstInRange(
                    this.byTarget.get(target), start, end);
            final String targetId = StoredModel.existingElementId(target);
            if (!this.byElementId.isEmpty() && targetId != null) {
                final PositionedRelationship sameId = firstInRange(
                        this.byElementId.get(targetId), start, end);
                if (first == null || sameId != null
                        && sameId.position() < first.position()) {
                    first = sameId;
                }
            }
            Relationship relationship = null;
            if (first != null) {
                relationship = first.relationship();
            }
            return relationship;
        }

        private void indexRelationshipTargets(final Relationship relationship,
                final int position) {
            final PositionedRelationship positioned =
                    new PositionedRelationship(position, relationship);
            for (final Element target : StoredModel.targets(relationship)) {
                final Element comparable = unwrapOwningMembership(target);
                if (comparable != null) {
                    this.byTarget.computeIfAbsent(comparable,
                            ignored -> new ArrayList<>()).add(positioned);
                    final String elementId = StoredModel.existingElementId(comparable);
                    if (elementId != null) {
                        this.byElementId.computeIfAbsent(elementId,
                                ignored -> new ArrayList<>()).add(positioned);
                    }
                }
            }
        }

        /** Lists are in traversal order; the search interval is (contextStart, contextEnd]. */
        private static PositionedRelationship firstInRange(
                final List<PositionedRelationship> relationships,
                final int contextStart, final int contextEnd) {
            if (relationships == null || relationships.isEmpty()) {
                return null;
            }
            int low = 0;
            int high = relationships.size();
            while (low < high) {
                final int middle = (low + high) >>> 1;
                if (relationships.get(middle).position() <= contextStart) {
                    low = middle + 1;
                } else {
                    high = middle;
                }
            }
            PositionedRelationship result = null;
            if (low < relationships.size()
                    && relationships.get(low).position() <= contextEnd) {
                result = relationships.get(low);
            }
            return result;
        }
    }

    /** State of one iterative containment traversal level. */
    private record TraversalFrame(EObject object,
            Iterator<EObject> children) {
    }

    /** A relationship and its containment position. */
    private record PositionedRelationship(int position,
            Relationship relationship) {
    }
}
