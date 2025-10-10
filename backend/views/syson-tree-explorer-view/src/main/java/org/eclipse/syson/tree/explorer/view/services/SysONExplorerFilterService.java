/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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
package org.eclipse.syson.tree.explorer.view.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.services.api.ISysONResourceService;
import org.eclipse.syson.sysml.Expose;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.util.ElementUtil;
import org.eclipse.syson.tree.explorer.view.filters.SysONTreeFilterProvider;
import org.eclipse.syson.tree.explorer.view.services.api.ISysONExplorerFilterService;
import org.springframework.stereotype.Service;

/**
 * Services to apply filters on SysON explorer.
 * 
 * @author gdaniel
 */
@Service
public class SysONExplorerFilterService implements ISysONExplorerFilterService {

    private final UtilService utilService = new UtilService();

    private final ISysONResourceService sysONResourceService;

    public SysONExplorerFilterService(final ISysONResourceService sysONResourceService) {
        this.sysONResourceService = Objects.requireNonNull(sysONResourceService);
    }

    @Override
    public boolean isKerMLStandardLibrary(Object object) {
        return object instanceof Resource res && res.getURI() != null && res.getURI().toString().startsWith(ElementUtil.KERML_LIBRARY_SCHEME);
    }

    @Override
    public boolean isSysMLStandardLibrary(Object object) {
        return object instanceof Resource res && res.getURI() != null && res.getURI().toString().startsWith(ElementUtil.SYSML_LIBRARY_SCHEME);
    }

    @Override
    public boolean isUserLibrary(Object object) {
        return object instanceof Resource res
                && this.sysONResourceService.isImported(res)
                && !new UtilService().getLibraries(res, false).isEmpty();
    }

    @Override
    public List<Object> hideKerMLStandardLibraries(List<Object> elements) {
        return elements.stream()
                .filter(element -> !this.isKerMLStandardLibrary(element))
                .toList();
    }

    @Override
    public List<Object> hideSysMLStandardLibraries(List<Object> elements) {
        return elements.stream()
                .filter(element -> !this.isSysMLStandardLibrary(element))
                .toList();
    }

    @Override
    public List<Object> hideUserLibraries(List<Object> elements) {
        return elements.stream()
                .filter(element -> !this.isUserLibrary(element))
                .toList();
    }

    @Override
    public List<Object> hideMemberships(List<Object> elements) {
        List<Object> alteredElements = new ArrayList<>();
        elements.forEach(child -> {
            if (child instanceof Membership membership) {
                alteredElements.addAll(membership.getOwnedRelatedElement());
            } else {
                alteredElements.add(child);
            }
        });
        return alteredElements;
    }

    @Override
    public List<Object> hideRootNamespace(List<Object> elements) {
        List<Object> alteredElements = new ArrayList<>();
        elements.forEach(child -> {
            if (child instanceof Namespace namespace && this.utilService.isRootNamespace(namespace)) {
                alteredElements.addAll(namespace.getOwnedElement());
            } else {
                alteredElements.add(child);
            }
        });
        return alteredElements;
    }

    @Override
    public List<Object> hideExposeElements(List<Object> elements) {
        return elements.stream()
                .filter(element -> !(element instanceof Expose))
                .toList();
    }

    @Override
    public List<Object> applyFilters(List<?> elements, List<String> activeFilterIds) {
        List<Object> alteredElements = new ArrayList<>(elements);
        if (activeFilterIds.contains(SysONTreeFilterProvider.HIDE_MEMBERSHIPS_TREE_ITEM_FILTER_ID)) {
            alteredElements = this.hideMemberships(alteredElements);
        }
        if (activeFilterIds.contains(SysONTreeFilterProvider.HIDE_KERML_STANDARD_LIBRARIES_TREE_FILTER_ID)) {
            alteredElements = this.hideKerMLStandardLibraries(alteredElements);
        }
        if (activeFilterIds.contains(SysONTreeFilterProvider.HIDE_SYSML_STANDARD_LIBRARIES_TREE_FILTER_ID)) {
            alteredElements = this.hideSysMLStandardLibraries(alteredElements);
        }
        if (activeFilterIds.contains(SysONTreeFilterProvider.HIDE_USER_LIBRARIES_TREE_FILTER_ID)) {
            alteredElements = this.hideUserLibraries(alteredElements);
        }
        if (activeFilterIds.contains(SysONTreeFilterProvider.HIDE_ROOT_NAMESPACES_ID)) {
            alteredElements = this.hideRootNamespace(alteredElements);
        }
        if (activeFilterIds.contains(SysONTreeFilterProvider.HIDE_EXPOSE_ELEMENTS_TREE_ITEM_FILTER_ID)) {
            alteredElements = this.hideExposeElements(alteredElements);
        }
        return alteredElements;
    }

}
