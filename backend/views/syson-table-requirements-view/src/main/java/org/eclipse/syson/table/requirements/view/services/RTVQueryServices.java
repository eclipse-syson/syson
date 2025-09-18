/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.sirius.components.tables.ColumnFilter;
import org.eclipse.sirius.components.tables.ColumnSort;
import org.eclipse.syson.sysml.Documentation;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.ViewUsage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Query services for the Requirements Table View. The services declared in this class must not modify the model or the
 * table view.
 *
 * @author arichard
 */
public class RTVQueryServices {

    private final Logger logger = LoggerFactory.getLogger(RTVQueryServices.class);

    private final ObjectMapper objectMapper;

    public RTVQueryServices(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<RequirementUsage> getExposedRequirements(ViewUsage viewUsage) {
        return viewUsage.getExposedElement().stream()
                .filter(RequirementUsage.class::isInstance)
                .map(RequirementUsage.class::cast)
                .toList();
    }

    public String getDocumentationBody(RequirementUsage requirementUsage) {
        return requirementUsage.getDocumentation().stream()
                .map(Documentation::getBody)
                .findFirst()
                .orElse("");
    }

    public List<RequirementUsage> sortAndFilterRequirements(List<Object> objects, List<ColumnSort> columnSort, List<ColumnFilter> columnFilters, String gloablFilterData) {
        var requirementsElements = objects.stream().filter(RequirementUsage.class::isInstance).map(RequirementUsage.class::cast).collect(Collectors.toList());
        this.filterRequirements(columnFilters, gloablFilterData, requirementsElements);
        this.sortRequirements(columnSort, requirementsElements);
        return requirementsElements;
    }

    private void filterRequirements(List<ColumnFilter> columnFilters, String gloablFilterData, List<RequirementUsage> requirementsElements) {
        if (gloablFilterData != null && !gloablFilterData.isBlank()) {
            requirementsElements.removeIf(r -> !this.isValidGlobalFilterCandidate(r, gloablFilterData));
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
            String filterValue = this.objectMapper.readValue(columnFilter.value(), new TypeReference<>() {

            });
            isValid = candidate != null && candidate.contains(filterValue);
        } catch (JsonProcessingException exception) {
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
