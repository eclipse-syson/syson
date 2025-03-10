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
package org.eclipse.syson.application.omnibox;

import java.util.Objects;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.sirius.components.core.api.IEditingContextSearchService;
import org.eclipse.sirius.web.application.editingcontext.EditingContext;
import org.eclipse.syson.application.omnibox.api.IPredicateCanEditingContextPublishSysMLProject;
import org.eclipse.syson.sysml.SysmlPackage;
import org.springframework.stereotype.Service;

/**
 * {@link IPredicateCanEditingContextPublishSysMLProject} implementation that checks whether any of the resources in the
 * editing context is a SysML resource.
 *
 * @author flatombe
 */
@Service
public class PredicateCanEditingContextPublishSysMLProject implements IPredicateCanEditingContextPublishSysMLProject {

    private final IEditingContextSearchService editingContextSearchService;

    public PredicateCanEditingContextPublishSysMLProject(final IEditingContextSearchService editingContextSearchService) {
        this.editingContextSearchService = Objects.requireNonNull(editingContextSearchService);
    }

    @Override
    public boolean test(final String editingContextId) {
        return this.editingContextSearchService.findById(editingContextId)
                .filter(EditingContext.class::isInstance)
                .map(EditingContext.class::cast)
                .map(EditingContext::getDomain)
                .map(EditingDomain::getResourceSet)
                .map(resourceSet -> resourceSet.getResources().stream()
                        .anyMatch(resource -> this.isSysMLResource(resource)))
                .orElse(false);
    }

    private boolean isSysMLResource(final Resource resource) {
        return resource.getContents().stream()
                .map(EObject::eClass)
                .map(EClass::getEPackage)
                .anyMatch(ePackage -> ePackage == SysmlPackage.eINSTANCE);
    }

}
