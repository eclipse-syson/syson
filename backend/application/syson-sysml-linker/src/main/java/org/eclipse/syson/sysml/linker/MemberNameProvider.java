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
package org.eclipse.syson.sysml.linker;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.omg.sysml.lang.sysml.Behavior;
import org.omg.sysml.lang.sysml.ConjugatedPortDefinition;
import org.omg.sysml.lang.sysml.ConstraintUsage;
import org.omg.sysml.lang.sysml.ConstructorExpression;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Feature;
import org.omg.sysml.lang.sysml.FeatureChaining;
import org.omg.sysml.lang.sysml.Membership;
import org.omg.sysml.lang.sysml.OwningMembership;
import org.omg.sysml.lang.sysml.PerformActionUsage;
import org.omg.sysml.lang.sysml.PortDefinition;
import org.omg.sysml.lang.sysml.Redefinition;
import org.omg.sysml.lang.sysml.ReferenceSubsetting;
import org.omg.sysml.lang.sysml.Relationship;
import org.omg.sysml.lang.sysml.RequirementConstraintMembership;
import org.omg.sysml.lang.sysml.ReturnParameterMembership;
import org.omg.sysml.lang.sysml.Specialization;
import org.omg.sysml.lang.sysml.Step;
import org.omg.sysml.lang.sysml.SysMLPackage;
import org.omg.sysml.lang.sysml.TransitionFeatureMembership;
import org.omg.sysml.lang.sysml.TransitionUsage;
import org.omg.sysml.lang.sysml.Type;
import org.omg.sysml.lang.sysml.Usage;
import org.omg.sysml.lang.sysml.VariantMembership;
import org.omg.sysml.lang.sysml.VisibilityKind;
import org.omg.sysml.util.TypeUtil;

/**
 * Computes the effective member names of a {@link Membership}.
 * <p>
 * KerML allows a membership name to be declared directly, inherited from its member element or established by
 * relationships such as redefinitions and feature chainings. This provider follows those relationships lazily and also
 * retains names from unresolved links, so a membership remains discoverable during the fixed-point linking process.
 * </p>
 *
 * @author ylussaud
 */
public class MemberNameProvider {

    /**
     * Effective names collected from a membership and from links that can establish its name.
     *
     * @param memberName
     *            the effective long name
     * @param memberShortName
     *            the effective short name
     * @param namesFromUnresolvedLinkEObjects
     *            candidate names retained from unresolved relationships
     * @param fromUnresolvedRedefinition
     *            <code>true</code> if {@link #namesFromUnresolvedLinks} has been populated via a Redefinition.redefined
     *            unresolved link, <code>false</code> otherwise
     */
    private record NameResolution(String memberName, String memberShortName, Set<String> namesFromUnresolvedLinks, boolean fromUnresolvedRedefinition) {

        Set<String> asSet() {
            Set<String> result = new LinkedHashSet<>();

            if (this.memberName != null) {
                result.add(this.memberName);
            }
            if (this.memberShortName != null) {
                result.add(this.memberShortName);
            }
            if (this.namesFromUnresolvedLinks != null) {
                result.addAll(this.namesFromUnresolvedLinks);
            }

            return result;
        }

        boolean isEmpty() {
            return this.memberName == null && this.memberShortName == null && this.namesFromUnresolvedLinks == null;
        }

    }

    private final SysMLLinker linker;

    private final SysMLMetamodelHelper sysMLMetamodelHelper = new SysMLMetamodelHelper();

    private final SysMLProxySafeNavigator sysMLEMFNavigator = new SysMLProxySafeNavigator();

    private final Map<EObject, Map<EReference, Set<String>>> namedFromUnresolvedLinks;

    /**
     * Creates a member-name provider.
     *
     * @param linker
     *            the linker used to follow relationships on demand
     * @param toResolveMap
     *            unresolved references whose last name segments can contribute effective names
     */
    public MemberNameProvider(SysMLLinker linker, Map<EObject, Map<EReference, Set<QualifiedName>>> toResolveMap) {
        this.linker = linker;
        this.namedFromUnresolvedLinks = this.getNamedFromUnresolvedLinks(toResolveMap);
    }

    private Map<EObject, Map<EReference, Set<String>>> getNamedFromUnresolvedLinks(Map<EObject, Map<EReference, Set<QualifiedName>>> toResolveMap) {
        Map<EObject, Map<EReference, Set<String>>> res = new LinkedHashMap<>();

        for (Entry<EObject, Map<EReference, Set<QualifiedName>>> ownerEntry : toResolveMap.entrySet()) {
            for (Entry<EReference, Set<QualifiedName>> referenceEntry : ownerEntry.getValue().entrySet()) {
                if (referenceEntry.getKey() == SysMLPackage.eINSTANCE.getFeatureChaining_ChainingFeature() || referenceEntry.getKey() == SysMLPackage.eINSTANCE.getRedefinition_RedefinedFeature()) {
                    Set<String> names = referenceEntry.getValue().stream().map(qn -> qn.getLastSegment()).collect(Collectors.toCollection(LinkedHashSet::new));
                    res.computeIfAbsent(ownerEntry.getKey(), o -> new LinkedHashMap<>()).computeIfAbsent(referenceEntry.getKey(), r -> new LinkedHashSet<>()).addAll(names);
                }
            }
        }

        return res;
    }

    /**
     * Gets the effective member name of the given {@link Membership}.
     *
     * @param membership
     *            the {@link Membership}
     * @return the effective member name
     */
    public String getMemberName(Membership membership) {
        return this.resolveMembershipNames(membership).memberName();
    }

    /**
     * Gets the effective member short name of the given {@link Membership}.
     *
     * @param membership
     *            the {@link Membership}
     * @return the effective member short name
     */
    public String getMemberShortName(Membership membership) {
        return this.resolveMembershipNames(membership).memberShortName();
    }

    /**
     * Gets the effective member names of the given {@link Membership}.
     *
     * @param membership
     *            the {@link Membership}
     * @return the effective member names
     */
    public Set<String> getMemberNames(Membership membership) {
        return this.resolveMembershipNames(membership).asSet();
    }

    private boolean hasDeclaredName(Element element) {
        return element.getDeclaredName() != null || element.getDeclaredShortName() != null;
    }

    private NameResolution resolveMembershipNames(Membership membership) {
        NameResolution result;
        if (membership == null) {
            result = new NameResolution(null, null, null, false);
        } else if (membership instanceof OwningMembership owningMembership) {
            // KerML v1.0, p.132-133, paragraph 8.3.2.4.8 derives ownedMemberName and
            // ownedMemberShortName from the ownedMemberElement.
            Element element = owningMembership.getOwnedMemberElement();
            if (element != null && !(element instanceof ConjugatedPortDefinition) && this.hasDeclaredName(element)) {
                result = new NameResolution(element.getDeclaredName(), element.getDeclaredShortName(), null, false);
            } else {
                result = this.resolveElementNames(element, Collections.newSetFromMap(new IdentityHashMap<>()));
            }
        } else {
            // KerML v1.0, p.125, paragraph 8.3.2.4.3 defines memberName/memberShortName as the
            // names of a member relative to its owning namespace.
            // SysML v2.0 Part 1, p.167, paragraph 8.2.2.5 AliasMember carries those names
            // explicitly for non-owning memberships.
            result = new NameResolution(membership.getMemberName(), membership.getMemberShortName(), null, false);
        }
        return result;
    }

    /**
     * Gets the parameter name for the given {@link Step} and index.
     *
     * @param step
     *            the {@link Step}
     * @param index
     *            the parameter index
     * @return the {@link NameResolution}
     */
    private NameResolution getParameterNameFromSpecialization(Step step, int index) {
        NameResolution res;

        // KerML 8.4.4.7.2 parameter redefinitions also apply to implied specializations;
        // SysML 8.3.17.15 makes a SendActionUsage specialize Actions::sendActions.
        Stream<Element> explicitGeneralTypes = step.getOwnedRelationship().stream()
                .filter(Specialization.class::isInstance)
                .map(Specialization.class::cast)
                .flatMap(specialization -> {
                    EReference specializationFeature = this.sysMLMetamodelHelper.getSpecializationFeature(specialization);
                    Set<Element> specializedElements = this.linker.get(specialization, specializationFeature);
                    return specializedElements.stream();
                });
        Stream<? extends Element> implicitGeneralTypes = TypeUtil.getImplicitGeneralTypesFor(step).stream();
        Optional<List<Feature>> optParameters = Stream.concat(explicitGeneralTypes, implicitGeneralTypes)
                .filter(e -> e instanceof Behavior || e instanceof Step)
                .map(e -> this.getParameters(e))
                .filter(parameters -> parameters.size() > index)
                .findFirst();
        if (optParameters.isPresent()) {
            List<Feature> parameters = optParameters.get();
            Feature correspondingFeature = parameters.get(index);
            res = this.resolveElementNames(correspondingFeature, Collections.newSetFromMap(new IdentityHashMap<>()));
        } else {
            res = new NameResolution(null, null, null, false);
        }

        return res;
    }

    /**
     * Gets the {@link List} of parameter for the given {@link Step} or {@link Behavior}.
     *
     * @param stepOrBehavior
     *            the {@link Step} or {@link Behavior}
     * @return the {@link List} of parameter for the given {@link Step} or {@link Behavior}, an empty {@link List} for
     *         other kind of {@link Element}
     */
    private List<Feature> getParameters(Element stepOrBehavior) {
        List<Feature> res;

        if (stepOrBehavior instanceof Behavior behavior) {
            res = behavior.getParameter();
        } else if (stepOrBehavior instanceof Step step) {
            res = step.getParameter();
        } else {
            res = Collections.emptyList();
        }

        return res;
    }

    /**
     * Resolves the effective names of the given {@link Element} while preventing cyclic traversal.
     *
     * @param element
     *            the {@link Element}
     * @param visited
     *            the {@link Set} of visited {@link Element}
     * @return the effective names of the given {@link Element} while preventing cyclic traversal
     */
    private NameResolution resolveElementNames(Element element, Set<Element> visited) {
        if (element == null || !visited.add(element)) {
            return new NameResolution(null, null, null, false);
        }

        NameResolution result;
        if (element instanceof ConjugatedPortDefinition conjugatedPortDefinition) {
            result = this.resolveConjugatedPortDefinitionNames(conjugatedPortDefinition, visited);
        } else if (element instanceof Feature feature) {
            result = this.resolveFeatureNames(feature, visited);
            // special case for Step where its parameters implicitly redefines parameters of its specializations KermML
            // 8.4.4.7.2
            if (result.isEmpty() && feature.getDirection() != null && feature.getOwningNamespace() instanceof Step step) {
                int index = step.getParameter().indexOf(feature);
                if (index >= 0) {
                    result = this.getParameterNameFromSpecialization(step, index);
                }
            }
        } else {
            // KerML v1.0, p.109-112, paragraph 8.3.2.1.2 and p.188, paragraph 8.3.3.3.4:
            // by default, an Element name is declaredName and its short name is
            // declaredShortName.
            result = new NameResolution(element.getDeclaredName(), element.getDeclaredShortName(), null, false);
        }

        visited.remove(element);
        return result;
    }

    /**
     * Resolves the effective names of a conjugated port definition.
     *
     * @param conjugatedPortDefinition
     *            the {@link ConjugatedPortDefinition}
     * @param visited
     *            the {@link Set} of visited {@link Element}
     * @return the effective names of a conjugated port definition
     */
    private NameResolution resolveConjugatedPortDefinitionNames(ConjugatedPortDefinition conjugatedPortDefinition, Set<Element> visited) {
        // SysML v2.0 Part 1, p.292, paragraph 8.3.12.2 effectiveName(): a
        // ConjugatedPortDefinition prepends "~" to the name of its
        // originalPortDefinition.
        PortDefinition originalPortDefinition = (PortDefinition) conjugatedPortDefinition.getOwningMembership().getOwningRelatedElement();
        NameResolution originalPortDefinitionNames = this.resolveElementNames(originalPortDefinition, visited);

        String memberName;
        if (originalPortDefinitionNames.memberName() == null) {
            memberName = null;
        } else {
            memberName = "~" + originalPortDefinitionNames.memberName();
        }

        return new NameResolution(memberName, conjugatedPortDefinition.getDeclaredShortName(), null, false);
    }

    /**
     * Resolves the effective names of the given {@link Feature}.
     *
     * @param feature
     *            the {@link Feature}
     * @param visited
     *            the {@link Set} of visited {@link Element}
     * @return the effective names of the given {@link Feature}
     */
    private NameResolution resolveFeatureNames(Feature feature, Set<Element> visited) {
        NameResolution result = this.resolveFeatureNamesFromDeclaredOrRelatedFeature(feature, visited);

        if (result.isEmpty() && feature.eContainer() instanceof OwningMembership argumentMembership
                && argumentMembership.eContainer() instanceof Feature constructorResult) {
            if (constructorResult.eContainer() instanceof ReturnParameterMembership resultMembership
                    && resultMembership.eContainer() instanceof ConstructorExpression constructor) {
                result = this.getConstructorArgumentName(feature, constructorResult, constructor, visited);
            }
        }
        if (result.isEmpty() && feature.isEnd() && feature.getOwningNamespace() instanceof Type owningType) {
            result = this.getEndNameFromSpecialization(feature, owningType, visited);
        }

        // Inferred fallback from KerML v1.0, p.159, paragraph 8.3.3.3.4 /featureTarget and
        // p.172, paragraph 8.3.3.3.4 "types of the last Feature in the chain are also types of
        // the chained Feature": when an unnamed chained Feature is used as a member,
        // use the terminal chaining Feature as the effective naming source.
        if (result.memberName() == null && result.memberShortName() == null && !result.fromUnresolvedRedefinition()) {
            result = this.getLastChainingFeature(feature, visited);
        }

        return result;
    }

    /**
     * Derives the name supplied by a positional constructor argument's implied redefinition. KerML 8.3.4.8.3,
     * {@code checkConstructorExpressionResultFeatureRedefinition}, and 8.4.4.9.4 map arguments to public
     * features of the instantiated type in order. KerML 8.3.3.3.4 then supplies their effective names.
     *
     * @param feature
     *            the unnamed argument feature
     * @param result
     *            the constructor's result feature
     * @param constructor
     *            the constructor expression
     * @param visited
     *            elements already visited while deriving names
     * @return the corresponding public feature's name, if available
     */
    private NameResolution getConstructorArgumentName(Feature feature, Feature result, ConstructorExpression constructor, Set<Element> visited) {
        int index = result.getOwnedFeature().indexOf(feature);
        if (index >= 0) {
            for (Relationship relationship : List.copyOf(constructor.getOwnedRelationship())) {
                if (relationship instanceof Membership membership && !(membership instanceof OwningMembership)) {
                    for (Element element : this.linker.get(membership, SysMLPackage.eINSTANCE.getMembership_MemberElement())) {
                        if (element instanceof Type type) {
                            // Owned members come first (KerML 8.3.3.1.10); most positional arguments
                            // can be named without deriving the entire inherited feature collection.
                            List<Feature> features = type.getOwnedFeature().stream()
                                    .filter(candidate -> candidate.getOwningMembership().getVisibility() == VisibilityKind.PUBLIC).toList();
                            Feature namingFeature;
                            if (features.size() > index) {
                                namingFeature = features.get(index);
                            } else {
                                namingFeature = type.getFeature().stream()
                                        .filter(candidate -> candidate.getOwningMembership().getVisibility() == VisibilityKind.PUBLIC)
                                        .skip(index).findFirst().orElse(null);
                            }
                            return this.resolveElementNames(namingFeature, visited);
                        }
                    }
                }
            }
        }
        return new NameResolution(null, null, null, false);
    }

    /**
     * Derives an unnamed end's name from its implied positional redefinition. KerML 8.3.3.3.4,
     * {@code checkFeatureEndRedefinition}, requires an owned end to redefine the end at the same position
     * in each direct supertype. KerML 8.4.4.6.1 applies this to connector ends, whose names come from
     * their typing association even before those implied redefinitions are materialized.
     *
     * @param feature
     *            the unnamed end
     * @param owningType
     *            the type owning the end
     * @param visited
     *            features already visited while deriving names
     * @return the name of the corresponding general end, if available
     */
    private NameResolution getEndNameFromSpecialization(Feature feature, Type owningType, Set<Element> visited) {
        NameResolution result = new NameResolution(null, null, null, false);
        int index = owningType.getOwnedEndFeature().indexOf(feature);
        if (index >= 0) {
            // Most ends obtain their name from an explicitly typed association. Do not derive all
            // implicit supertypes (or inherited ends) when an owned end already supplies the name.
            for (Relationship relationship : List.copyOf(owningType.getOwnedRelationship())) {
                if (relationship instanceof Specialization specialization) {
                    EReference reference = this.sysMLMetamodelHelper.getSpecializationFeature(specialization);
                    for (Element general : this.linker.get(specialization, reference)) {
                        if (general instanceof Type generalType) {
                            result = this.getGeneralEndName(generalType, index, visited);
                            if (!result.isEmpty()) {
                                return result;
                            }
                        }
                    }
                }
            }
            for (Type generalType : TypeUtil.getImplicitGeneralTypesFor(owningType)) {
                result = this.getGeneralEndName(generalType, index, visited);
                if (!result.isEmpty()) {
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Gets the name of a general type's end at the given position. Owned feature memberships precede
     * inherited memberships (KerML 8.3.3.1.10, {@code deriveTypeFeatureMembership}), so deriving
     * inherited ends is only needed when the position is beyond the owned ends.
     *
     * @param generalType
     *            the general type
     * @param index
     *            the end position
     * @param visited
     *            elements already visited while deriving names
     * @return the corresponding end's name, if available
     */
    private NameResolution getGeneralEndName(Type generalType, int index, Set<Element> visited) {
        List<Feature> ends = generalType.getOwnedEndFeature();
        if (ends.size() <= index) {
            ends = generalType.getEndFeature();
        }
        NameResolution result = new NameResolution(null, null, null, false);
        if (ends.size() > index) {
            result = this.resolveElementNames(ends.get(index), visited);
        }
        return result;
    }

    /**
     * Resolves feature names from declarations or its naming feature.
     *
     * @param feature
     *            the {@link Feature}
     * @param visited
     *            the {@link Set} of visited {@link Element}
     * @return the feature names from declarations or its naming feature
     */
    private NameResolution resolveFeatureNamesFromDeclaredOrRelatedFeature(Feature feature, Set<Element> visited) {
        // KerML v1.0, p.162, paragraph 8.3.3.3.4 effectiveName/effectiveShortName:
        // if either declaredName or declaredShortName is present, those declared
        // values define the effective names for the Feature.
        if (feature.getDeclaredName() != null || feature.getDeclaredShortName() != null) {
            return new NameResolution(feature.getDeclaredName(), feature.getDeclaredShortName(), null, false);
        }

        Feature triggerPayload = this.sysMLEMFNavigator.getTransitionTriggerPayload(feature);
        NameResolution result;
        if (triggerPayload != null) {
            result = this.resolveElementNames(triggerPayload, visited);
        } else {
            result = this.getNamingFeature(feature, visited);
            if (result.isEmpty() && feature.eContainer() instanceof TransitionFeatureMembership membership
                    && membership.eContainer() instanceof TransitionUsage) {
                // SysML 8.3.18.9 checkTransitionUsageTransitionFeatureSpecialization and 8.4.14.3:
                // transition actions implicitly redefine accepter, guard, or effect. KerML 8.3.3.3.4
                // derives an unnamed feature's name from its redefinition, even before it is materialized.
                String name = switch (membership.getKind()) {
                    case TRIGGER -> "accepter";
                    case GUARD -> "guard";
                    case EFFECT -> "effect";
                };
                result = new NameResolution(name, null, null, false);
            }
        }

        return result;
    }

    /**
     * Gets the name resolution supplied by a feature naming relationship.
     *
     * @param feature
     *            the {@link Feature}
     * @param visited
     *            the {@link Set} of visited {@link Element}
     * @return the name resolution supplied by a feature naming relationship
     */
    private NameResolution getNamingFeature(Feature feature, Set<Element> visited) {
        // KerML v1.0, p.163-164, paragraph 8.3.3.3.4 namingFeature():
        // by default, the naming Feature of a Feature is the first redefinedFeature
        // of its first ownedRedefinition.
        // Resolving a specialization can lazily transform this Feature and append relationships.
        // A snapshot keeps the effective-name traversal stable across that reentrant resolution.
        NameResolution result = new NameResolution(null, null, null, false);
        for (Relationship relationship : List.copyOf(feature.getOwnedRelationship())) {
            if (relationship instanceof Redefinition redefinition) {
                Set<Element> resolved = this.linker.get(redefinition, SysMLPackage.eINSTANCE.getRedefinition_RedefinedFeature());
                if (!resolved.isEmpty()) {
                    result = this.resolveElementNames(resolved.iterator().next(), visited);
                } else {
                    Set<String> namesFromUnresolvedLinks = this.getNameFromUnresolvedLinks(relationship, SysMLPackage.eINSTANCE.getRedefinition_RedefinedFeature());
                    result = new NameResolution(null, null, namesFromUnresolvedLinks, !namesFromUnresolvedLinks.isEmpty());
                }
                break;
            } else if (relationship instanceof ReferenceSubsetting referenceSubsetting && this.hasReferenceSubsettingName(feature)) {
                Set<Element> resolved = this.linker.get(referenceSubsetting, SysMLPackage.eINSTANCE.getReferenceSubsetting_ReferencedFeature());
                if (!resolved.isEmpty()) {
                    result = this.resolveElementNames(resolved.iterator().next(), visited);
                } else {
                    Set<String> namesFromUnresolvedLinks = this.getNameFromUnresolvedLinks(relationship, SysMLPackage.eINSTANCE.getReferenceSubsetting_ReferencedFeature());
                    result = new NameResolution(null, null, namesFromUnresolvedLinks, false);
                }
                break;
            }
        }

        return result;
    }

    /**
     * Tests whether a reference subsetting supplies this feature's implicit name. SysML v2.0, sections 8.3.6.4,
     * 8.3.17.14 and 8.3.20.4, define this naming rule for variants, performed actions (including exhibited states), and
     * constraints owned by requirement constraint memberships. Other features use KerML v1.0, section 8.3.3.3.4,
     * {@code namingFeature()}, which derives the name from the first redefinition. In particular, a satisfaction usage
     * does not inherit its satisfied requirement's name (SysML v2.0, section 8.3.21.10).
     *
     * @param feature
     *            the feature whose implicit naming rule is tested
     * @return whether its reference subsetting can supply its name
     */
    private boolean hasReferenceSubsettingName(Feature feature) {
        boolean variant = feature instanceof Usage && feature.getOwningMembership() instanceof VariantMembership;
        boolean requirementConstraint = feature instanceof ConstraintUsage && feature.getOwningMembership() instanceof RequirementConstraintMembership;
        return variant || feature instanceof PerformActionUsage || requirementConstraint;
    }

    /**
     * Gets the name resolution supplied by the last chaining feature.
     *
     * @param feature
     *            the {@link Feature}
     * @param visited
     *            the {@link Set} of visited {@link Element}
     * @return the name resolution supplied by the last chaining feature
     */
    private NameResolution getLastChainingFeature(Feature feature, Set<Element> visited) {
        Feature lastChainingFeature = null;
        Set<String> namesFromUnresolvedLinks = null;
        // Resolving a chaining Feature can add implicit relationships to the Feature currently
        // being inspected, so never retain a live EMF iterator across linker.get().
        for (Relationship relationship : List.copyOf(feature.getOwnedRelationship())) {
            if (relationship instanceof FeatureChaining) {
                Set<Element> resolved = this.linker.get(relationship, SysMLPackage.eINSTANCE.getFeatureChaining_ChainingFeature());
                namesFromUnresolvedLinks = this.getNameFromUnresolvedLinks(relationship, SysMLPackage.eINSTANCE.getFeatureChaining_ChainingFeature());
                if (!resolved.isEmpty()) {
                    lastChainingFeature = (Feature) resolved.iterator().next();
                } else {
                    lastChainingFeature = null;
                }
            }
        }

        if (lastChainingFeature == null) {
            return new NameResolution(null, null, namesFromUnresolvedLinks, false);
        } else {
            return this.resolveElementNames(lastChainingFeature, visited);
        }
    }

    /**
     * Gets names retained for an unresolved reference.
     *
     * @param element
     *            the {@link Element}
     * @param reference
     *            the {@link EReference}
     * @return names retained for an unresolved reference
     */
    private Set<String> getNameFromUnresolvedLinks(Element element, EReference reference) {
        Set<String> res;

        Map<EReference, Set<String>> referenceMap = this.namedFromUnresolvedLinks.get(element);
        if (referenceMap != null) {
            res = referenceMap.getOrDefault(reference, Collections.emptySet());
        } else {
            res = Collections.emptySet();
        }

        return res;
    }

}
