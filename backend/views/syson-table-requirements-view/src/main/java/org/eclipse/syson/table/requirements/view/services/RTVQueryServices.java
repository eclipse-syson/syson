/*******************************************************************************
 * Copyright (c) 2025, 2026 Obeo.
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
package org.eclipse.syson.table.requirements.view.services;

import java.util.Collections;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.components.core.api.IIdentityService;
import org.eclipse.sirius.components.tables.ColumnFilter;
import org.eclipse.sirius.components.tables.ColumnSort;
import org.eclipse.syson.sysml.Documentation;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.ViewUsage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ObjectMapper;

/**
 * Query services for the Requirements Table View. The services declared in this class must not modify the model or the
 * table view.
 *
 * @author arichard
 */
public class RTVQueryServices {

    private final Logger logger = LoggerFactory.getLogger(RTVQueryServices.class);

    private final ObjectMapper objectMapper;

    private final IIdentityService identityService;

    public RTVQueryServices(ObjectMapper objectMapper, IIdentityService identityService) {
        this.objectMapper = objectMapper;
        this.identityService = identityService;
    }

    /**
     * Returns the list of RequirementUsage that are visible in the Requirements Table View according to expanding/collapsing elements.
     *
     * @param viewUsage
     *         the ViewUsage containing the table.
     * @param expandedIds
     *         the list of RequirementUsage ids that are expanded in the table.
     * @param expandAll
     *         whether all rows should be expanded or not.
     * @return the list of RequirementUsage contained by the table contained by the given ViewUsage.
     */
    public List<RequirementUsage> getExposedRequirements(ViewUsage viewUsage, List<String> expandedIds, boolean expandAll) {
        var allExposedRequirementUsages = viewUsage.getExposedElement().stream()
                .filter(RequirementUsage.class::isInstance)
                .map(RequirementUsage.class::cast)
                .filter(eObject -> this.hasExpandedParent(eObject, expandedIds, expandAll))
                .toList();
        // exposed element in ViewUsage are not in correct order to by displayed in the hierarchical table.
        // we have to sort them by depth first order
        return allExposedRequirementUsages.stream()
                .sorted(this.byDepthFirstOrder(allExposedRequirementUsages))
                .toList();
    }

    /**
     * Returns the comparator that can be used to sort the given list of RequirementUsage following a depth first order.
     *
     * @param requirements
     *         the unordered list of RequirementUsage
     * @return the comparator to use on the given RequirementUsage list to sort its elements following a depth first order.
     */
    private Comparator<RequirementUsage> byDepthFirstOrder(List<RequirementUsage> requirements) {
        // 1. Identify requirements that are children of another RequirementUsage
        Set<RequirementUsage> isChild = Collections.newSetFromMap(new IdentityHashMap<>());
        requirements.forEach(e -> isChild.addAll(this.getChildren(e)));

        // 2. Roots = requirements that are not children of anyone
        List<RequirementUsage> roots = requirements.stream()
                .filter(e -> !isChild.contains(e))
                .toList();

        // 3. Compute DFS order -> positions in a Map
        Map<RequirementUsage, Integer> positions = new IdentityHashMap<>();
        int[] index = {0};
        roots.forEach(r -> this.depthFirstPositions(r, positions, index));

        // 4. Comparator based on this positions
        return Comparator.comparingInt(positions::get);
    }

    /**
     * Compute recursively depth position of the given RequirementUsage and all its children.
     *
     * @param requirementUsage
     *         the root RequirementUsage of the computation.
     * @param positions
     *         the computation Map result containing the given RequirementUsage position and those of its children.
     * @param index
     *         the position of the given RequirementUsage.
     */
    private void depthFirstPositions(RequirementUsage requirementUsage, Map<RequirementUsage, Integer> positions, int[] index) {
        positions.put(requirementUsage, index[0]++);
        for (RequirementUsage child : this.getChildren(requirementUsage)) {
            this.depthFirstPositions(child, positions, index);
        }
    }

    private List<RequirementUsage> getChildren(RequirementUsage elem) {
        return elem.getOwnedMember().stream()
                .filter(RequirementUsage.class::isInstance)
                .map(RequirementUsage.class::cast)
                .toList();
    }

    private boolean hasExpandedParent(EObject eObject, List<String> expandedIds, boolean expandAll) {
        EObject parent = eObject.eContainer().eContainer();
        if (!expandAll && parent != null && !this.isRootRequirement(eObject)) {
            var parentId = this.identityService.getId(parent);
            return expandedIds.contains(parentId) && this.hasExpandedParent(parent, expandedIds, false);
        }
        return true;
    }

    /**
     * Returns whether the given RequirementUsage has children or not.
     *
     * @param requirementUsage
     *         a RequirementUsage
     * @return {@code true} if the given RequirementUsage is not contained by another RequirementUsage and {@code false} otherwise.
     */
    public boolean hasRequirementChildren(RequirementUsage requirementUsage) {
        return requirementUsage.getOwnedMember().stream().anyMatch(RequirementUsage.class::isInstance);
    }

    /**
     * Returns the depth level of the given RequirementUsage in the Requirements Table View. This is used to indent this element according to its parent.
     *
     * @param requirementUsage
     *         a RequirementUsage
     * @return an {@code int} representing the depth of the given element. This method should return {@code 0} for root elements, {@code 1} for their direct children, and so on.
     */
    public int getRequirementDepthLevel(RequirementUsage requirementUsage) {
        if (this.isRootRequirement(requirementUsage)) {
            return 0;
        } else {
            return this.getRequirementDepthLevel((RequirementUsage) requirementUsage.getOwner()) + 1;
        }
    }

    private boolean isRootRequirement(EObject eObject) {
        return eObject instanceof RequirementUsage requirementUsage && !(requirementUsage.getOwner() instanceof RequirementUsage);
    }

    public String getDocumentationBody(RequirementUsage requirementUsage) {
        return requirementUsage.getDocumentation().stream()
                .map(Documentation::getBody)
                .findFirst()
                .orElse("");
    }

    public List<RequirementUsage> sortAndFilterRequirements(List<Object> objects, List<ColumnSort> columnSort, List<ColumnFilter> columnFilters, String globalFilterData) {
        var requirementsElements = objects.stream().filter(RequirementUsage.class::isInstance).map(RequirementUsage.class::cast).collect(Collectors.toList());
        this.filterRequirements(columnFilters, globalFilterData, requirementsElements);
        this.sortRequirements(columnSort, requirementsElements);
        return requirementsElements;
    }

    private void filterRequirements(List<ColumnFilter> columnFilters, String globalFilterData, List<RequirementUsage> requirementsElements) {
        if (globalFilterData != null && !globalFilterData.isBlank()) {
            requirementsElements.removeIf(r -> !this.isValidGlobalFilterCandidate(r, globalFilterData));
        }
        for (ColumnFilter columnFilter : columnFilters) {
            if ("DeclaredName".equals(columnFilter.id())) {
                requirementsElements.removeIf(r -> !this.isValidColumnFilterCandidate(r.getDeclaredName(), columnFilter));
            } else if ("ReqId".equals(columnFilter.id())) {
                requirementsElements.removeIf(r -> !this.isValidColumnFilterCandidate(r.getReqId(), columnFilter));
            } else if ("Documentation".equals(columnFilter.id())) {
                requirementsElements.removeIf(r -> !this.isValidColumnFilterCandidate(this.getDocumentationBody(r), columnFilter));
            }
        }
    }

    private void sortRequirements(List<ColumnSort> columnSort, List<RequirementUsage> requirementsElements) {
        for (ColumnSort colSort : columnSort) {
            if ("DeclaredName".equals(colSort.id())) {
                if (colSort.desc()) {
                    requirementsElements.sort(Comparator.comparing(RequirementUsage::getDeclaredName, String.CASE_INSENSITIVE_ORDER).reversed());
                } else {
                    requirementsElements.sort(Comparator.comparing(RequirementUsage::getDeclaredName, String.CASE_INSENSITIVE_ORDER));
                }
            } else if ("ReqId".equals(colSort.id())) {
                if (colSort.desc()) {
                    requirementsElements.sort(Comparator.comparing(RequirementUsage::getReqId, String.CASE_INSENSITIVE_ORDER).reversed());
                } else {
                    requirementsElements.sort(Comparator.comparing(RequirementUsage::getReqId, String.CASE_INSENSITIVE_ORDER));
                }
            } else if ("Documentation".equals(colSort.id())) {
                if (colSort.desc()) {
                    requirementsElements.sort(Comparator.comparing((RequirementUsage r) -> this.getDocumentationBody(r), String.CASE_INSENSITIVE_ORDER).reversed());
                } else {
                    requirementsElements.sort(Comparator.comparing((RequirementUsage r) -> this.getDocumentationBody(r), String.CASE_INSENSITIVE_ORDER));
                }
            }
        }
    }

    private boolean isValidColumnFilterCandidate(String candidate, ColumnFilter columnFilter) {
        var isValid = true;
        try {
            JavaType javaType = objectMapper.getTypeFactory().constructType(String.class);
            String filterValue = this.objectMapper.readValue(columnFilter.value(), javaType);
            isValid = candidate != null && candidate.contains(filterValue);
        } catch (JacksonException exception) {
            this.logger.warn(exception.getMessage(), exception);
        }
        return isValid;
    }

    private boolean isValidGlobalFilterCandidate(RequirementUsage requirementUsage, String globalFilterData) {
        boolean isValid = false;
        // check declaredName
        if (requirementUsage.getDeclaredName() != null && requirementUsage.getDeclaredName().contains(globalFilterData)) {
            isValid = true;
        }
        // check reqId
        if (!isValid && requirementUsage.getReqId() != null && requirementUsage.getReqId().contains(globalFilterData)) {
            isValid = true;
        }
        // check documentation body
        var documentationBody = this.getDocumentationBody(requirementUsage);
        if (!isValid && documentationBody != null && documentationBody.contains(globalFilterData)) {
            isValid = true;
        }
        return isValid;
    }
}
