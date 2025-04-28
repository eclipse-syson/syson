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
package org.eclipse.syson.application.publication;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.sirius.web.application.library.services.LibraryMetadataAdapter;
import org.eclipse.sirius.web.application.studio.services.library.api.DependencyGraph;
import org.eclipse.syson.application.publication.api.ISysONLibraryDependencyCollector;
import org.springframework.stereotype.Service;

/**
 * Collects the dependencies between SysON {@link Resource}.
 *
 * @author gdaniel
 */
@Service
public class SysONLibraryDependencyCollector implements ISysONLibraryDependencyCollector {

    @Override
    public DependencyGraph<Resource> collectDependencies(ResourceSet resourceSet) {
        DependencyGraph<Resource> dependencyGraph = new DependencyGraph<>();
        for (Resource resource : resourceSet.getResources()) {
            if (this.isLibrary(resource)) {
                resource.getAllContents().forEachRemaining(eObject -> {
                    ECrossReferenceAdapter.getCrossReferenceAdapter(eObject).getInverseReferences(eObject)
                            .forEach(setting -> {
                                if (setting.getEStructuralFeature() instanceof EReference reference
                                        && !reference.isContainment()) {
                                    Resource dependentResource = setting.getEObject().eResource();
                                    if (dependentResource != null && resource != dependentResource) {
                                        // implicit have null resource
                                        dependencyGraph.addEdge(dependentResource, resource);
                                    }
                                }
                            });
                });
            }
        }
        return dependencyGraph;
    }

    private boolean isLibrary(Resource resource) {
        return resource.eAdapters().stream()
                .anyMatch(LibraryMetadataAdapter.class::isInstance);
    }

}
