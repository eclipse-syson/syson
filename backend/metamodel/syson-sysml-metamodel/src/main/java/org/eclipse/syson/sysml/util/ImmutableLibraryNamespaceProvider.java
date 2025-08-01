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
package org.eclipse.syson.sysml.util;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.syson.sysml.LibraryPackage;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.helper.EMFUtils;

/**
 * Provider of {@link Namespace} stored in immutable {@link LibraryPackage} such as SyML and KerML default libraries.
 *
 * @author Arthur Daussy
 */
public class ImmutableLibraryNamespaceProvider extends AdapterImpl implements IImmutableLibraryNamespaceProvider {

    private final Map<String, Namespace> immutableNamespaces = new HashMap<>();

    public static ImmutableLibraryNamespaceProvider get(ResourceSet rs) {
        return rs.eAdapters().stream()
                .filter(ImmutableLibraryNamespaceProvider.class::isInstance)
                .map(ImmutableLibraryNamespaceProvider.class::cast)
                .findFirst()
                .orElse(null);
    }

    public static ImmutableLibraryNamespaceProvider getFrom(EObject e) {
        if (e != null) {
            Resource resource = e.eResource();
            if (resource != null) {
                return getFrom(resource);
            }
        }
        return null;
    }

    public static ImmutableLibraryNamespaceProvider getFrom(Resource resource) {
        ResourceSet resourceSet = resource.getResourceSet();
        if (resourceSet != null) {
            return get(resourceSet);
        }
        return null;
    }

    @Override
    public Namespace getNamespaceFromStandardLibrary(String qualifiedName) {
        return this.immutableNamespaces.get(qualifiedName);
    }

    @Override
    public <T extends Namespace> T getNamespaceFromStandardLibrary(String qualifiedName, Class<T> type) {
        Namespace result = this.getNamespaceFromStandardLibrary(qualifiedName);
        if (type.isInstance(result)) {
            return type.cast(result);
        }
        return null;
    }

    /**
     * Adds {@link LibraryPackage} stored inside the root Namespace as immutable libraries.
     *
     * @param resource
     *            resource containing {@link LibraryPackage}
     */
    public void addRootImmutableLibrary(Resource resource) {
        for (EObject root : resource.getContents()) {
            if (root instanceof Namespace rootNamespace) {
                rootNamespace.getOwnedElement().stream()
                        .filter(LibraryPackage.class::isInstance)
                        .map(LibraryPackage.class::cast)
                        .filter(LibraryPackage::isIsStandard)
                        .forEach(this::addImmutableLibrary);
            }
        }

    }

    /**
     * Add a immutable {@link LibraryPackage} to this provides.
     *
     * @param lib
     *            {@link LibraryPackage}
     */
    public void addImmutableLibrary(LibraryPackage lib) {
        EMFUtils.allContainedObjectOfType(lib, Namespace.class).forEach(type -> {
            String qn = type.getQualifiedName();
            if (qn != null && !qn.isEmpty()) {
                this.immutableNamespaces.put(qn, type);
            }
        });
    }

}
