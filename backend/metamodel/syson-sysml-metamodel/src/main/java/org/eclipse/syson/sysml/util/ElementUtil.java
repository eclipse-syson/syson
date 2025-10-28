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
package org.eclipse.syson.sysml.util;

import com.fasterxml.uuid.Generators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Import;
import org.eclipse.syson.sysml.LibraryPackage;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.Specialization;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.TypeFeaturing;
import org.eclipse.syson.sysml.VisibilityKind;

/**
 * Util class for SysML elements.
 *
 * @author arichard
 */
public class ElementUtil {

    /**
     * Scheme for URI locating standard KerML libraries.
     */
    public static final String KERML_LIBRARY_SCHEME = "kermllibrary";

    /**
     * Scheme for URI locating standard SysML libraries.
     */
    public static final String SYSML_LIBRARY_SCHEME = "sysmllibrary";

    /**
     * The UUID for NameSpace_URL as required in SysMLv2 specification.
     */
    public static final UUID NAME_SPACE_URL_UUID = UUID.fromString("6ba7b811-9dad-11d1-80b4-00c04fd430c8");

    /**
     * The prefix to prepend to all names for the construction of UUIDs as required in KerML specification.
     */
    public static final String KERML_LIBRARY_BASE_URI = "https://www.omg.org/spec/KerML/";

    /**
     * The prefix to prepend to all names for the construction of UUIDs as required in SysMLv2 specification.
     */
    public static final String SYSML_LIBRARY_BASE_URI = "https://www.omg.org/spec/SysML/";

    /**
     * The source of the {@link EAnnotation} used to flag imported {@link Resource}s.
     */
    private static final String IMPORTED_EANNOTATION_SOURCE = "org.eclipse.syson.sysml.imported";

    /**
     * Checks if the given resource is one of KerML or SysML standard libraries.
     *
     * @param resource
     *            a {@link Resource}
     * @return <code>true</code> is standard resource, <code>false</code> otherwise.
     */
    public static boolean isStandardLibraryResource(Resource resource) {
        if (resource != null && resource.getURI() != null) {
            URI uri = resource.getURI();
            return uri.toString().startsWith(KERML_LIBRARY_SCHEME) || uri.toString().startsWith(SYSML_LIBRARY_SCHEME);
        }
        return false;
    }

    /**
     * Check if the given {@link Element} comes from a standard library (i.e. a {@link LibraryPackage} with its standard
     * attribute set to true) or not.
     *
     * @param element
     *            the given {@link Element}.
     * @return <code>true</code> if the given element is contained in a standard library, <code>false</code> otherwise.
     */
    public static boolean isFromStandardLibrary(Element element) {
        return element.libraryNamespace() instanceof LibraryPackage libraryPackage
                && libraryPackage.isIsStandard();
    }

    /**
     * Generate a UUID (a v5 for standard library elements a a random v4 random for others).
     *
     * @return a UUID.
     */
    public static UUID generateUUID(Element element) {
        UUID uuid = UUID.randomUUID();
        if (element instanceof LibraryPackage libraryPackage && libraryPackage.isIsStandard()) {
            Resource resource = element.eResource();
            if (resource != null) {
                String uri = resource.getURI().toString().startsWith("kermllibrary:") ? ElementUtil.KERML_LIBRARY_BASE_URI : ElementUtil.SYSML_LIBRARY_BASE_URI;
                String qualifiedName = element.getQualifiedName();
                if (qualifiedName != null) {
                    uuid = generateUUIDv5(ElementUtil.NAME_SPACE_URL_UUID, uri + qualifiedName);
                }
            }
        } else if (isFromStandardLibrary(element) && element.getDeclaredName() != null
                && !(element instanceof Import || element instanceof Membership || element instanceof Specialization || element instanceof TypeFeaturing)) {
            String qualifiedName = element.getQualifiedName();
            Namespace libraryNamespace = element.libraryNamespace();
            if (qualifiedName != null && libraryNamespace != null) {
                UUID namespaceUUID = UUID.fromString(libraryNamespace.getElementId());
                uuid = ElementUtil.generateUUIDv5(namespaceUUID, qualifiedName);
            }
        }
        return uuid;
    }

    /**
     * Generate a UUID v5 from a given namespace and a value.
     *
     * @param namespaceUUID
     *            the namespace to use to generate the UUID.
     * @param value
     *            the value (i.e. the qualified name of a SysML element) for which we want to generate the UUID.
     * @return a UUID in version 5.
     */
    public static UUID generateUUIDv5(UUID namespaceUUID, String value) {
        return Generators.nameBasedGenerator(namespaceUUID, null).generate(value);
    }

    /**
     * Sets the provided {@code resource} with the provided {@code isImported} flag.
     * <p>
     * An <i>imported</i> {@link Resource} returns {@code true} when calling {@code ElementUtil.isImported(resource)}.
     * </p>
     *
     * @param resource
     *            the {@link Resource} to set as imported
     * @param isImported
     *            the imported flag to set
     */
    public static void setIsImported(Resource resource, boolean isImported) {
        resource.getContents().forEach(eObject -> {
            if (eObject instanceof Element element) {
                if (isImported) {
                    if (element.getEAnnotation(IMPORTED_EANNOTATION_SOURCE) == null) {
                        EAnnotation importedEAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
                        importedEAnnotation.setSource(IMPORTED_EANNOTATION_SOURCE);
                        element.getEAnnotations().add(importedEAnnotation);
                    }
                } else {
                    if (element.getEAnnotation(IMPORTED_EANNOTATION_SOURCE) != null) {
                        EcoreUtil.remove(element.getEAnnotation(IMPORTED_EANNOTATION_SOURCE));
                    }
                }
            }
        });
    }

    /**
     * Returns {@code true} if the provided {@code resource} is imported.
     *
     * @param resource
     *            the {@link Resource} to check
     * @return {@code true} if the provided {@code resource} is imported
     */
    public static boolean isImported(Resource resource) {
        return resource.getContents().stream()
                .anyMatch(eObject -> eObject instanceof Element element && element.getEAnnotation(IMPORTED_EANNOTATION_SOURCE) != null);
    }

    /**
     * Find an {@link Element} that match the given name and type in the ResourceSet of the given element.
     *
     * @param object
     *            the object for which to find a corresponding type.
     * @param elementName
     *            the element name to match.
     * @param elementType
     *            the type to match.
     * @return the found element or <code>null</code>.
     */
    public <T extends Element> T findByNameAndType(EObject object, String elementName, Class<T> elementType) {
        final T result = this.findByNameAndType(this.getAllRootsInResourceSet(object), elementName, elementType);
        return result;
    }

    /**
     * Iterate over the given {@link Collection} of root elements to find a element with the given name and type.
     *
     * @param roots
     *            the elements to inspect.
     * @param elementName
     *            the name to match.
     * @param elementType
     *            the type to match.
     * @return the found element or <code>null</code>.
     */
    public <T extends Element> T findByNameAndType(Collection<EObject> roots, String elementName, Class<T> elementType) {
        String[] splitElementName = elementName.split("::");
        List<String> qualifiedName = Arrays.asList(splitElementName);
        for (final EObject root : roots) {
            final T result;
            if (qualifiedName.size() > 1) {
                result = this.findInRootByQualifiedNameAndTypeFrom(root, qualifiedName, elementType);
            } else {
                result = this.findByNameAndTypeFrom(root, elementName, elementType);
            }
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    /**
     * Count the number of existing ViewUsages inside the given Namespace.
     *
     * @param namespace
     *            the given {@link Namespace}.
     * @return the number of existing ViewUsages inside the given Namespace.
     */
    public long existingViewUsagesCountForRepresentationCreation(Namespace namespace) {
        return namespace.getOwnedMember().stream()
                .filter(member -> SysmlPackage.eINSTANCE.getViewUsage().equals(member.eClass()))
                .count();
    }

    /**
     * Iterate over the children of the given root {@link EObject} to find an {@link Element} with the given qualified
     * name and type.
     *
     * @param root
     *            the root object to iterate.
     * @param qualifiedName
     *            the qualified name to match.
     * @param elementType
     *            the type to match.
     * @return the found element or <code>null</code>.
     */
    private <T extends Element> T findInRootByQualifiedNameAndTypeFrom(EObject root, List<String> qualifiedName, Class<T> elementType) {
        T element = null;
        if (root instanceof Namespace namespace && namespace.eContainer() == null && namespace.getName() == null) {
            // Ignore top-level namespaces with no name, they aren't part of the qualified name
            element = this.findByQualifiedNameAndTypeFrom(namespace, qualifiedName, elementType);
        } else if (root instanceof Element rootElt && this.nameMatches(rootElt, qualifiedName.get(0))) {
            element = this.findByQualifiedNameAndTypeFrom(rootElt, qualifiedName.subList(1, qualifiedName.size()), elementType);
        }
        return element;
    }

    /**
     * Iterate over the children of the given parent {@link EObject} to find an {@link Element} with the given qualified
     * name and type.
     *
     * @param parent
     *            the parent object to iterate.
     * @param qualifiedName
     *            the qualified name to match.
     * @param elementType
     *            the type to match.
     * @return the found element or <code>null</code>.
     */
    private <T extends Element> T findByQualifiedNameAndTypeFrom(Element parent, List<String> qualifiedName, Class<T> elementType) {
        T element = null;

        Optional<Element> child = parent.getOwnedElement().stream()
                .filter(Element.class::isInstance)
                .map(Element.class::cast)
                .filter(elt -> this.nameMatches(elt, qualifiedName.get(0)))
                .findFirst();
        if (child.isEmpty() && parent instanceof Namespace parentNamespace) {
            // If the element is not owned by the parent it can be visible through a public import.
            child = parentNamespace.getImportedMembership().stream()
                    .filter(membership -> Objects.equals(membership.getVisibility(), VisibilityKind.PUBLIC))
                    .flatMap(membership -> membership.getRelatedElement().stream())
                    .filter(elt -> this.nameMatches(elt, qualifiedName.get(0)))
                    .findFirst();
        }
        if (child.isPresent() && qualifiedName.size() > 1) {
            element = this.findByQualifiedNameAndTypeFrom(child.get(), qualifiedName.subList(1, qualifiedName.size()), elementType);
        } else if (child.isPresent() && elementType.isInstance(child.get())) {
            element = elementType.cast(child.get());
        }
        return element;
    }

    /**
     * Iterate over the children of the given root {@link EObject} to find an {@link Element} with the given name and
     * type.
     *
     * @param root
     *            the root object to iterate.
     * @param elementName
     *            the name to match.
     * @param elementType
     *            the type to match.
     * @return the found element or <code>null</code>.
     */
    private <T extends Element> T findByNameAndTypeFrom(EObject root, String elementName, Class<T> elementType) {
        T element = null;

        if (elementType.isInstance(root) && this.nameMatches(elementType.cast(root), elementName)) {
            return elementType.cast(root);
        }

        TreeIterator<EObject> eAllContents = root.eAllContents();
        while (eAllContents.hasNext()) {
            EObject obj = eAllContents.next();
            if (elementType.isInstance(obj) && this.nameMatches(elementType.cast(obj), elementName)) {
                element = elementType.cast(obj);
                break;
            }
        }

        return element;
    }

    /**
     * Retrieves all the root elements of the resource in the resource set of the given context object.
     *
     * @param context
     *            the context object on which to execute this service.
     * @return a {@link Collection} of all the root element of the current resource set.
     */
    private Collection<EObject> getAllRootsInResourceSet(EObject context) {
        final Resource res = context.eResource();
        if (res != null && res.getResourceSet() != null) {
            final Collection<EObject> roots = new ArrayList<>();
            for (final Resource childRes : res.getResourceSet().getResources()) {
                roots.addAll(childRes.getContents());
            }
            return roots;
        }
        return Collections.emptyList();
    }

    /**
     * Check if the given element's name match the given String.
     * If there is no match with the name, then the short name is checked.
     *
     * @param element
     *         the {@link Element} to check.
     * @param name
     *         the name to match.
     * @return <code>true</code> if a match is found, <code>false</code> otherwise.
     */
    private boolean nameMatches(Element element, String name) {
        if (element == null || name == null) {
            return false;
        }
        if (this.equalsConsideringOptionalQuotes(element.getName(), name)) {
            return true;
        }
        return this.equalsConsideringOptionalQuotes(element.getShortName(), name);
    }

    private boolean equalsConsideringOptionalQuotes(String candidate, String query) {
        if (candidate == null || query == null) {
            return false;
        }
        boolean matches = candidate.strip().equals(query.strip());
        if (!matches && query.startsWith("'") && query.endsWith("'")) {
            // We give the option to quote names, but the quotes aren't part of the model.
            candidate = "'" + candidate + "'";
            matches = candidate.strip().equals(query.strip());
        }
        return matches;
    }
}
