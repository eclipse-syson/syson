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
package org.eclipse.syson.sysml.metamodel.services.redefine;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.syson.sysml.Behavior;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.ReturnParameterMembership;
import org.eclipse.syson.sysml.Specialization;
import org.eclipse.syson.sysml.Step;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.helper.ImplicitParameterRedefinition;
import org.eclipse.syson.sysml.metamodel.services.MetamodelMutationElementService;
import org.eclipse.syson.sysml.metamodel.util.ElementUtil;

/**
 * Materializes positional parameters for one selection. Each instance is single-use.
 *
 * @author Arthur Daussy
 */
final class ParameterRedefinitionComputer {

    private final Type destinationOwner;

    private final Feature selectedParameter;

    private final FeatureMembershipChanges created;

    private boolean branching;

    private String error;

    /**
     * Creates a single-use computation for a selected parameter and its required local signature.
     *
     * @param owner
     *            the destination type whose local parameters are materialized
     * @param selected
     *            the parameter selected by the caller in that destination context
     * @param created
     *            the shared tracker for memberships to remove if any segment of the chain fails
     */
    // SysON requires public or private constructors, including on package-private classes.
    @SuppressWarnings("checkstyle:RedundantModifier")
    public ParameterRedefinitionComputer(Type owner, Feature selected, FeatureMembershipChanges created) {
        this.destinationOwner = Objects.requireNonNull(owner);
        this.selectedParameter = Objects.requireNonNull(selected);
        this.created = created;
    }

    /**
     * Checks the parameter ancestry and selection accessibility, then materializes the required local positions.
     * Branching inheritance completes the signature and any result. Linear inheritance completes a prefix, or only
     * the result when a result is selected and no ordinary positions require completion.
     * Any new memberships are registered with the shared tracker. The enclosing chain operation owns rollback.
     *
     * @return the selected local feature and an optional failure cause, without segment or context labels
     */
    SegmentResult redefine() {
        Map<Type, Boolean> branchingByType = new HashMap<>();
        this.checkInheritance(this.destinationOwner, new HashSet<>(), branchingByType);
        this.branching = branchingByType.getOrDefault(this.destinationOwner, false);
        // Resolve accessibility only after the cycle check: inherited memberships can recurse through parameter queries.
        if (this.error == null && this.destinationOwner.getOwnedFeature().stream()
                .noneMatch(feature -> feature.allRedefinedFeatures().contains(this.selectedParameter))
                && this.destinationOwner.getFeatureMembership().stream()
                        .noneMatch(membership -> membership.getOwnedMemberFeature() == this.selectedParameter)) {
            this.error = "the feature is not accessible from this context. Select an accessible feature chain again.";
        }
        if (this.error != null) {
            return new SegmentResult(null, this.error);
        }
        Feature local = this.materializeParameters();
        return new SegmentResult(local, this.error);
    }

    /**
     * Visits direct behavior and step parents to detect cycles and propagate the branching classification.
     * The active recursion path detects back edges. Completed classifications are cached for shared ancestors.
     * On a cycle, records the failure in error. Traversal precedes effective-feature queries that could recurse.
     *
     * @param type
     *            the type currently visited
     * @param visiting
     *            the active recursion path, updated on entry and exit
     * @param branchingByType
     *            completed classifications, updated only after a type and its parents have been checked
     */
    private void checkInheritance(Type type, Set<Type> visiting, Map<Type, Boolean> branchingByType) {
        if (branchingByType.containsKey(type)) {
            return;
        }
        if (!visiting.add(type)) {
            this.error = MessageFormat.format("the parameter inheritance contains a cycle through {0}. Resolve the cyclic specialization before trying again.", this.describeType(type));
            return;
        }
        List<Type> parents = this.findDirectParameterSupertypes(type);
        for (Type parent : parents) {
            this.checkInheritance(parent, visiting, branchingByType);
            if (this.error != null) {
                break;
            }
        }
        visiting.remove(type);
        if (this.error == null) {
            List<Type> branchParents = this.getParameterBranchParents(type, parents);
            boolean hasBranches = branchParents.stream()
                    .anyMatch(branchingByType::get) || this.hasBranchingInheritance(branchParents);
            branchingByType.put(type, hasBranches);
        }
    }

    /**
     * Checks whether an existing local direction satisfies the common requirement for its inherited position.
     * An inout requirement accepts any non-null direction. An in or out requirement requires an exact match.
     *
     * @param local
     *            the existing local parameter
     * @param required
     *            the common inherited direction to satisfy
     * @return whether the local parameter has a conforming direction
     */
    private boolean conformsToDirection(Feature local, FeatureDirectionKind required) {
        return local.getDirection() != null && (required == FeatureDirectionKind.INOUT || local.getDirection() == required);
    }

    /**
     * Builds a diagnostic label from the qualified name, falling back to the metaclass and element ID.
     *
     * @param type
     *            the type or feature to identify in an error message
     * @return the quoted qualified name or the unnamed-element description
     */
    private String describeType(Type type) {
        String name = type.getQualifiedName();
        if (name == null || name.isBlank()) {
            name = MessageFormat.format("unnamed {0} (id: {1})", type.eClass().getName(), type.getElementId());
        } else {
            name = MessageFormat.format("''{0}''", name);
        }
        return name;
    }

    /**
     * Gets the distinct direct supertypes that are behaviors or steps, including parents supplied by conjugation
     * through the metamodel supertypes API.
     *
     * @param owner
     *            the type whose parameter parents are requested
     * @return the direct parameter-bearing parents in encounter order
     */
    private List<Type> findDirectParameterSupertypes(Type owner) {
        return owner.supertypes(false).stream()
                .filter(parent -> parent instanceof Behavior || parent instanceof Step)
                .distinct()
                .toList();
    }

    /**
     * Maps owned directed features to inherited ordinary positions through their semantic redefinition targets.
     * A local feature without a matching target or owned redefinition falls back to its local ordinal.
     * Additional locals beyond the inherited signature are left outside the returned map. Results are handled separately.
     * Undirected explicit replacements of inherited positions are rejected before any parameters are materialized.
     * Reversed positions, duplicate mappings and invalid targets set error. Earlier valid mappings may still be returned.
     *
     * @param owner
     *            the destination whose local declarations are inspected
     * @param sourcesByPosition
     *            the inherited source features grouped by zero-based ordinary position
     * @param count
     *            the number of inherited ordinary positions
     * @return the existing local feature mapped to each recognized inherited position
     */
    private Map<Integer, Feature> findExistingParameters(Type owner, Map<Integer, List<Feature>> sourcesByPosition, int count) {
        Map<Integer, Feature> existing = new HashMap<Integer, Feature>();
        int previous = -1;
        int ordinal = 0;
        for (Feature local : owner.getOwnedFeature()) {
            if (local.getDirection() == null && !this.isResult(local) && !local.getOwnedRedefinition().isEmpty()) {
                int index = this.findInheritedPosition(local, sourcesByPosition);
                if (index >= 0) {
                    this.error = MessageFormat.format(
                            "local feature {0} redefines inherited parameters {1} but has no direction. Set a direction compatible with those parameters before trying again.",
                            this.describeType(local), sourcesByPosition.get(index).stream()
                                    .map(this::describeType)
                                    .toList());
                    break;
                }
            }
            if (local.getDirection() != null && !this.isResult(local)) {
                int index = this.findInheritedPosition(local, sourcesByPosition);
                if (index < 0 && local.getOwnedRedefinition().isEmpty()) {
                    index = ordinal;
                }
                if (index >= count && local.getOwnedRedefinition().isEmpty()) {
                    // KerML 1.1 Beta 2, 7.4.7.2: additional owned parameters may follow all inherited positions.
                    ordinal++;
                    continue;
                }
                if (index < 0 || index >= count) {
                    this.error = MessageFormat.format("local parameter {0} has no matching inherited position. Resolve its parameter redefinitions before trying again.", this.describeType(local));
                } else if (existing.containsKey(index)) {
                    this.error = MessageFormat.format("multiple local parameters redefine position {0}: {1} and {2}. Resolve the conflicting redefinitions before trying again.",
                            index + 1, this.describeType(existing.get(index)), this.describeType(local));
                } else if (index < previous) {
                    this.error = MessageFormat.format("local parameter {0} is out of order: inherited position {1} follows position {2}. Reorder the local parameters before trying again.",
                            this.describeType(local), index + 1, previous + 1);
                } else {
                    existing.put(index, local);
                    previous = index;
                }
                ordinal++;
            }
        }
        return existing;
    }

    /**
     * Collects the source direction along every path from the owner to the source declaring type.
     * Each conjugated owner reverses in and out while unwinding that path. Shared ancestors are revisited through
     * other paths so opposite conjugations remain visible. The path set prevents cycles within one traversal.
     *
     * @param owner
     *            the current type from which the source is inherited
     * @param source
     *            the source parameter whose contextual directions are required
     * @param path
     *            the active recursion path, restored before returning
     * @return the distinct directions in encounter order, empty if the declaring type is unreachable
     */
    private Set<FeatureDirectionKind> findInheritedDirections(Type owner, Feature source, Set<Type> path) {
        Set<FeatureDirectionKind> directions = new LinkedHashSet<FeatureDirectionKind>();
        if (source.getOwningType() == owner) {
            directions.add(source.getDirection());
        } else if (path.add(owner)) {
            for (Type parent : this.findDirectParameterSupertypes(owner)) {
                for (FeatureDirectionKind direction : this.findInheritedDirections(parent, source, path)) {
                    directions.add(this.getDirectionInContext(owner, direction));
                }
            }
            path.remove(owner);
        }
        return directions;
    }

    /**
     * Plans inherited ordinary positions and the separate result slot using the metamodel's implicit lookup.
     * The number of distinct directed parent features supplies an upper bound on the ordinary positions to inspect.
     * Private declarations may enlarge that bound but cannot become candidates or shift the selected position.
     *
     * @return non-empty source lists keyed by zero-based position, with Integer.MAX_VALUE for results
     */
    private Map<Integer, List<Feature>> findInheritedParameterPositions() {
        int upperBound = (int) this.findDirectParameterSupertypes(this.destinationOwner).stream()
                .flatMap(parent -> parent.getDirectedFeature().stream())
                .filter(parameter -> !this.isResult(parameter))
                .distinct()
                .count();
        ImplicitParameterRedefinition lookup = new ImplicitParameterRedefinition(this.selectedParameter);
        Map<Integer, List<Feature>> sourcesByPosition = new HashMap<>();
        for (int position = 0; position < upperBound; position++) {
            List<Feature> sources = lookup.findInheritedParameterTargets(this.destinationOwner, position);
            if (!sources.isEmpty()) {
                sourcesByPosition.put(position, sources);
            }
        }
        List<Feature> results = lookup.findInheritedParameterTargets(this.destinationOwner, Integer.MAX_VALUE);
        if (!results.isEmpty()) {
            sourcesByPosition.put(Integer.MAX_VALUE, results);
        }
        return sourcesByPosition;
    }

    /**
     * Finds the inherited ordinary position whose sources match the feature through redefinition.
     * Matching more than one position records an ambiguity in error.
     *
     * @param feature
     *            the selected or existing local parameter
     * @param sourcesByPosition
     *            the inherited source features grouped by zero-based ordinary position
     * @return a matching zero-based position, or -1 when none matches. On ambiguity, a match is returned with error set
     */
    private int findInheritedPosition(Feature feature, Map<Integer, List<Feature>> sourcesByPosition) {
        List<Integer> positions = sourcesByPosition.entrySet().stream()
                .filter(entry -> this.matches(feature, entry.getValue()))
                .map(Map.Entry::getKey)
                .toList();
        if (positions.size() > 1) {
            this.error = MessageFormat.format("parameter {0} redefines more than one inherited position. Resolve its parameter redefinitions before trying again.", this.describeType(feature));
        }
        return positions.stream()
                .findFirst()
                .orElse(-1);
    }

    /**
     * Combines every source direction relative to the destination, including all conjugation paths.
     * An inout requirement may narrow to the single in or out direction required by another source.
     * Conflicting in and out requirements, a missing direction or an empty set of inherited directions set error.
     *
     * @param owner
     *            the destination in which direction conformance is evaluated
     * @param sources
     *            the inherited parameters occupying one ordinary or result position
     * @return the common direction, meaningful only when error remains null
     */
    private FeatureDirectionKind getCommonDirection(Type owner, List<Feature> sources) {
        List<FeatureDirectionKind> directions = sources.stream()
                .flatMap(source -> this.findInheritedDirections(owner, source, new HashSet<>()).stream())
                .distinct()
                .toList();
        List<FeatureDirectionKind> fixed = directions.stream()
                .filter(direction -> direction != FeatureDirectionKind.INOUT)
                .toList();
        FeatureDirectionKind direction = FeatureDirectionKind.INOUT;
        if (fixed.size() == 1 && fixed.getFirst() != null) {
            direction = fixed.getFirst();
        } else if (!fixed.isEmpty() || directions.isEmpty()) {
            this.error = MessageFormat.format("inherited parameters {0} require incompatible or missing directions {1}. Resolve the parameter directions before trying again.",
                    sources.stream()
                            .map(this::describeType)
                            .toList(), directions);
        }
        return direction;
    }

    /**
     * Reverses in and out when the owner is conjugated. Inout and a missing direction pass through unchanged.
     *
     * @param owner
     *            the type whose conjugation applies at this inheritance step
     * @param direction
     *            the direction inherited from its parent, possibly null
     * @return the direction after applying this owner conjugation
     */
    private FeatureDirectionKind getDirectionInContext(Type owner, FeatureDirectionKind direction) {
        FeatureDirectionKind contextual = direction;
        if (owner.isIsConjugated()) {
            if (direction == FeatureDirectionKind.IN) {
                contextual = FeatureDirectionKind.OUT;
            } else if (direction == FeatureDirectionKind.OUT) {
                contextual = FeatureDirectionKind.IN;
            }
        }
        return contextual;
    }

    /**
     * Finds the owned-relationship index at which a missing ordinary parameter must be attached.
     * Insert before the current local parameter at that ordinal, preserving unrelated relationships.
     * Positions beyond the local prefix and the result sentinel append at the end.
     *
     * @param position
     *            the zero-based local ordinary position, or Integer.MAX_VALUE for a result
     * @return the insertion index in destinationOwner.ownedRelationship
     */
    private int getInsertionIndex(int position) {
        int insertion = this.destinationOwner.getOwnedRelationship().size();
        List<Feature> parameters = this.destinationOwner.getOwnedFeature().stream()
                .filter(parameter -> parameter.getDirection() != null && !(parameter.getOwningFeatureMembership() instanceof ReturnParameterMembership))
                .toList();
        if (position < parameters.size()) {
            insertion = this.destinationOwner.getOwnedRelationship().indexOf(parameters.get(position).getOwningFeatureMembership());
        }
        return insertion;
    }

    /**
     * Selects the direct parents used to classify independent branches for the service's materialization policy.
     * This policy assumes that library parents supplied only by virtual specializations represent language defaults.
     * They still contribute parameter constraints, but are excluded from this classification. Model parents and
     * library parents referenced by stored specializations are retained. Conjugated contexts retain all parents.
     *
     * For example, {@code action processUse : Process}, where {@code Process} declares {@code p1, p2, p3}, also has
     * implicit generalizations into the Actions library. Counting these defaults as additional branches could force
     * all three parameters to be materialized when selecting {@code p2}. Excluding them allows the linear signature
     * to require only the {@code p1, p2} prefix. This classification is a service policy, not a SysML exemption for
     * standard-library inheritance.
     *
     * @param type
     *            the type whose direct parameter parents are being classified
     * @param parents
     *            its direct behavior or step parents, including virtual library generalizations
     * @return the parents participating in branch classification, retaining their original order
     */
    private List<Type> getParameterBranchParents(Type type, List<Type> parents) {
        if (type.isIsConjugated()) {
            return parents;
        }
        Set<Type> storedGenerals = type.getOwnedRelationship().stream()
                .filter(Specialization.class::isInstance)
                .map(Specialization.class::cast)
                .map(Specialization::getGeneral)
                .collect(Collectors.toSet());
        return parents.stream()
                .filter(parent -> !ElementUtil.isStandardLibraryResource(parent.eResource()) || storedGenerals.contains(parent))
                .toList();
    }

    /**
     * Determines how many inherited ordinary positions the service must process. Branching requires the whole
     * signature. Linear inheritance extends through the selected position and every existing local mapping.
     * For example, selecting position two with an existing fourth-position replacement requires all four positions.
     *
     * @param inheritedCount
     *            the total number of inherited ordinary positions
     * @param selectedPosition
     *            the zero-based selected ordinary position, or -1 when the selection has no ordinary position
     * @param existingPositions
     *            the zero-based inherited positions already mapped to local parameters
     * @return the number of positions to process, including reused locals
     */
    private int getRequiredParameterCount(int inheritedCount, int selectedPosition, Set<Integer> existingPositions) {
        if (this.branching) {
            return inheritedCount;
        }
        int lastRequiredPosition = selectedPosition;
        for (int position : existingPositions) {
            lastRequiredPosition = Math.max(lastRequiredPosition, position);
        }
        return lastRequiredPosition + 1;
    }

    /**
     * Checks whether at least two parents remain after excluding those reachable through another supplied parent.
     * For Child -> Process -> Common plus Child -> Common, the direct Common edge is redundant.
     * Two sibling parents sharing Common remain independent. Branching inside a parent is propagated by checkInheritance.
     *
     * @param parents
     *            the direct parents retained by getParameterBranchParents
     * @return whether these direct parents contain at least two independent branches
     */
    private boolean hasBranchingInheritance(List<Type> parents) {
        return parents.stream()
                .filter(parent -> parents.stream()
                        .noneMatch(other -> other != parent && this.inheritsFrom(other, parent, new HashSet<>())))
                .limit(2)
                .count() > 1;
    }

    /**
     * Checks whether the ancestor is the supplied type or reachable through its direct behavior and step parents.
     * The shared visited set prevents repeated traversal and terminates cyclic paths.
     *
     * @param type
     *            the type from which reachability is tested
     * @param ancestor
     *            the identity of the ancestor to find
     * @param visited
     *            the types already traversed during this reachability search
     * @return whether the ancestor is reachable, including the type itself
     */
    private boolean inheritsFrom(Type type, Type ancestor, Set<Type> visited) {
        boolean inherited = type == ancestor;
        if (!inherited && visited.add(type)) {
            inherited = this.findDirectParameterSupertypes(type).stream()
                    .anyMatch(parent -> this.inheritsFrom(parent, ancestor, visited));
        }
        return inherited;
    }

    /**
     * Initializes the detached local feature against each positional source, then applies their common direction.
     * The last assignment prevents a later inout source from widening a direction narrowed by another parent.
     * An initialization failure records the offending source in error. The detached feature can be partially initialized.
     *
     * @param local
     *            the new detached parameter to initialize
     * @param sources
     *            all inherited sources sharing this position
     * @param owner
     *            the destination used for contextual property constraints
     * @param direction
     *            the common direction already checked against every source
     */
    private void initializeParameter(Feature local, List<Feature> sources, Type owner, FeatureDirectionKind direction) {
        MetamodelMutationElementService mutation = new MetamodelMutationElementService();
        for (Feature target : sources) {
            RedefinitionStatus status = mutation.initializeRedefiningFeature(local, target, owner);
            if (!status.isSuccess()) {
                this.error = MessageFormat.format("required parameter {0} cannot be redefined. {1}", this.describeType(target), status.message());
                return;
            }
        }
        // KerML validateRedefinitionDirectionConformance allows inout to narrow to a direction required by another parent.
        local.setDirection(direction);
    }

    /**
     * Identifies results by their owning ReturnParameterMembership, independently of their declared direction.
     *
     * @param feature
     *            the parameter to classify
     * @return whether the parameter occupies a result role
     */
    private boolean isResult(Feature feature) {
        return feature.getOwningFeatureMembership() instanceof ReturnParameterMembership;
    }

    /**
     * Checks whether the feature and any source belong to the same redefinition lineage.
     * The comparison accepts identity and transitive redefinition in either direction, allowing selections collected
     * before a local replacement or an intermediate parent replacement was materialized.
     *
     * @param feature
     *            the selected or local feature to match
     * @param sources
     *            the inherited candidates at one position
     * @return whether a source matches by identity or redefinition
     */
    private boolean matches(Feature feature, List<Feature> sources) {
        return sources.stream()
                .anyMatch(source -> feature.allRedefinedFeatures().contains(source) || source.allRedefinedFeatures().contains(feature));
    }

    /**
     * Uses the metamodel positional lookup to group inheritable parameter sources. The same source object
     * is considered once, and result sources are removed from ordinary positions and processed in a separate slot.
     * Existing locals determine which positions can be reused. The service then processes the required prefix or
     * full signature, stopping creation when error is set. A result is processed when selected or required by branching.
     *
     * @return the selected local feature if resolved, possibly null on failure. The caller must also inspect error
     */
    private Feature materializeParameters() {
        Feature localSelected = null;
        if (this.destinationOwner.getOwnedFeature().contains(this.selectedParameter)) {
            localSelected = this.selectedParameter;
        }
        Map<Integer, List<Feature>> sourcesByPosition = this.findInheritedParameterPositions();
        List<Feature> results = sourcesByPosition.getOrDefault(Integer.MAX_VALUE, List.of());
        sourcesByPosition.remove(Integer.MAX_VALUE);
        int count = sourcesByPosition.keySet().stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(-1) + 1;
        boolean selectedResult = this.matches(this.selectedParameter, results);
        int selectedPosition = this.findInheritedPosition(this.selectedParameter, sourcesByPosition);
        if (this.error == null && localSelected == null && selectedPosition < 0 && !selectedResult) {
            this.error = MessageFormat.format(
                    "the selected parameter has no matching inherited position in the parent behaviors {0}. Check their parameter order and explicit redefinitions before trying again.",
                    this.findDirectParameterSupertypes(this.destinationOwner).stream()
                            .map(this::describeType)
                            .toList());
            return null;
        }
        Map<Integer, Feature> existing = this.findExistingParameters(this.destinationOwner, sourcesByPosition, count);
        int requiredCount = this.getRequiredParameterCount(count, selectedPosition, existing.keySet());
        for (int index = 0; index < requiredCount && this.error == null; index++) {
            List<Feature> sources = sourcesByPosition.getOrDefault(index, List.of());
            Feature local = this.materializePosition(sources, existing.get(index), index);
            if (index == selectedPosition) {
                localSelected = local;
            }
        }
        if (this.error == null && !results.isEmpty() && (selectedResult || this.branching)) {
            Feature local = this.materializeResult(results);
            if (selectedResult) {
                localSelected = local;
            }
        }
        if (this.error == null && localSelected == null) {
            this.error = "the selected parameter has no inherited position or result. Select the feature chain again.";
        }
        return localSelected;
    }

    /**
     * Reuses the local feature for one position, or creates and attaches a feature initialized against all its sources.
     * For a creation, the selected source determines the exact metaclass and membership role when present at this
     * position. Otherwise the first source is used. The metamodel infers positional redefinitions after attachment.
     * A reused feature is checked for direction conformance. Any failure sets error for the enclosing chain rollback.
     *
     * @param sources
     *            the inherited candidates at this ordinary or result position
     * @param existing
     *            the local feature to reuse, or null when creation is required
     * @param position
     *            the zero-based ordinary position, or Integer.MAX_VALUE for the result slot
     * @return the reused or new local feature, possibly null or detached on failure
     */
    private Feature materializePosition(List<Feature> sources, Feature existing, int position) {
        if (sources.isEmpty()) {
            this.error = MessageFormat.format("no inherited parameter is available at position {0}. Complete the parent parameter declarations before trying again.", position + 1);
            return null;
        }
        Feature source = sources.getFirst();
        if (sources.contains(this.selectedParameter)) {
            source = this.selectedParameter;
        }
        FeatureDirectionKind direction = this.getCommonDirection(this.destinationOwner, sources);
        Feature local = existing;
        if (this.error == null && existing == null) {
            local = (Feature) SysmlFactory.eINSTANCE.create(source.eClass());
            this.initializeParameter(local, sources, this.destinationOwner, direction);
            if (this.error == null) {
                this.created.attach(this.destinationOwner, local, source, this.getInsertionIndex(position));
            }
        } else if (this.error == null && !this.conformsToDirection(existing, direction)) {
            this.error = MessageFormat.format("local parameter {0} has direction {1}, but its inherited position requires {2}. Correct its direction before trying again.",
                    this.describeType(existing), existing.getDirection(), direction);
        }
        return local;
    }

    /**
     * Locates the existing local result and materializes its separate slot using all inherited result sources.
     * More than one owned result records an error. A new result is appended after the current owned relationships.
     *
     * @param sources
     *            the inherited result parameters to merge
     * @return the reused or created result, possibly null on failure
     */
    private Feature materializeResult(List<Feature> sources) {
        List<Feature> results = this.destinationOwner.getOwnedFeature().stream()
                .filter(this::isResult)
                .toList();
        if (results.size() > 1) {
            this.error = "multiple local result parameters exist. Resolve the conflicting results before trying again.";
            return null;
        }
        return this.materializePosition(sources, results.stream()
                .findFirst()
                .orElse(null), Integer.MAX_VALUE);
    }
}
