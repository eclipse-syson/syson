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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.LibraryPackage;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.helper.EMFUtils;
import org.eclipse.syson.sysml.helper.NameHelper;

/**
 * Provider of {@link Namespace} stored in {@link LibraryPackage}. It implementation offer a way to speed computation by
 * registering immutable {@link LibraryPackage} such as SyML and KerML default libraries. The qualified name of element
 * stored in such libraries are stored in cache.
 *
 * @author Arthur Daussy
 */
public class LibraryNamespaceProvider extends AdapterImpl implements ILibraryNamespaceProvider {

    private final Map<String, Namespace> immutableLibraryNamespaces = new HashMap<>();

    private final Notifier source;

    public static LibraryNamespaceProvider get(ResourceSet rs) {
        return rs.eAdapters().stream()
                .filter(LibraryNamespaceProvider.class::isInstance)
                .map(LibraryNamespaceProvider.class::cast)
                .findFirst()
                .orElse(null);
    }

    public static LibraryNamespaceProvider getFrom(EObject e) {
        if (e != null) {
            Resource resource = e.eResource();
            if (resource != null) {
                return getFrom(resource);
            }
        }
        return null;
    }

    public static LibraryNamespaceProvider getFrom(Resource resource) {
        ResourceSet resourceSet = resource.getResourceSet();
        if (resourceSet != null) {
            return get(resourceSet);
        }
        return null;
    }

    /**
     * Simple constructor.
     *
     * @param source
     *            any notifier that is usable to compute the root {@link ResourceSet}.
     */
    public LibraryNamespaceProvider(Notifier source) {
        super();
        this.source = Objects.requireNonNull(source);
    }

    @Override
    public Namespace getNamespaceFromLibrary(String qualifiedName) {
        Namespace result = this.immutableLibraryNamespaces.get(qualifiedName);
        if (result == null) {
            result = this.resolve(qualifiedName);
        }
        return result;
    }

    @Override
    public <T extends Namespace> T getNamespaceFromLibrary(String qualifiedName, Class<T> type) {
        Namespace result = this.getNamespaceFromLibrary(qualifiedName);
        if (type.isInstance(result)) {
            return type.cast(result);
        }
        return null;
    }

    /**
     * Adds all Namespaces of the {@link LibraryPackage} stored inside the root Namespace of the given Resource to this
     * provider.<b>Only immutable</b> libraries should be added with this method. It computes once and for all the
     * qualified named of each Namespace so any subsequent change will not be taken into account by this provider.
     *
     * @param resource
     *            resource containing immutable {@link LibraryPackage}
     */
    public void addImmutableLibrariesNamespaces(Resource resource) {
        for (EObject root : resource.getContents()) {
            if (root instanceof Namespace rootNamespace) {
                rootNamespace.getOwnedElement().stream()
                        .filter(LibraryPackage.class::isInstance)
                        .map(LibraryPackage.class::cast)
                        .forEach(this::addImmutableLibraryNamespaces);
            }
        }

    }

    /**
     * Adds all Namespaces of the given {@link LibraryPackage} to this provider.<b>Only immutable</b> libraries should
     * be added with this method. It computes once and for all the qualified named of each Namespace so any subsequent
     * change will not be taken into account by this provider.
     *
     * @param lib
     *            {@link LibraryPackage}
     */
    public void addImmutableLibraryNamespaces(LibraryPackage lib) {
        EMFUtils.allContainedObjectOfType(lib, Namespace.class).forEach(type -> {
            String qn = type.getQualifiedName();
            if (qn != null && !qn.isEmpty()) {
                this.immutableLibraryNamespaces.put(qn, type);
            }
        });
    }

    private Namespace resolve(String qualifiedName) {
        // We do not want to use
        ResourceSet rs = this.getResourceSet(this.source);
        if (rs != null) {
            List<String> names = Stream.of(qualifiedName.split("::")).toList();
            if (!names.isEmpty()) {
                List<LibraryPackage> libraries = this.getRootLibraries(rs);
                if (!libraries.isEmpty()) {
                    return this.findMatch(names, libraries);
                }
            }
        }
        return null;
    }

    private List<LibraryPackage> getRootLibraries(ResourceSet rs) {
        if (rs != null) {
            return rs.getResources().stream().flatMap(r -> r.getContents().stream())
                    .filter(Namespace.class::isInstance)
                    .map(Namespace.class::cast)
                    .flatMap(nm -> nm.getOwnedElement().stream())
                    .filter(LibraryPackage.class::isInstance)
                    .map(LibraryPackage.class::cast)
                    .toList();
        }
        return List.of();
    }

    private Namespace findMatch(List<String> names, List<? extends Element> element) {
        if (!names.isEmpty()) {
            String name = NameHelper.unescapeString(names.get(0));
            Optional<Namespace> match = element.stream()
                    .filter(e -> e instanceof Namespace && (name.equals(e.getName())))
                    .map(Namespace.class::cast)
                    .findFirst();
            if (match.isPresent()) {
                if (names.size() == 1) {
                    return match.get();
                } else {
                    return this.findMatch(names.subList(1, names.size()), match.get().getOwnedElement());
                }
            }
        }
        return null;
    }

    private ResourceSet getResourceSet(Notifier n) {
        final ResourceSet result;
        if (n instanceof ResourceSet rs) {
            result = rs;
        } else if (n instanceof Resource r) {
            result = r.getResourceSet();
        } else if (n instanceof EObject eo) {
            result = this.getResourceSet(eo.eResource());
        } else {
            result = null;
        }
        return result;

    }

}
