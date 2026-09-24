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
package org.eclipse.syson.sysml.metamodel.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Import;
import org.eclipse.syson.sysml.MembershipImport;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.NamespaceImport;
import org.eclipse.syson.sysml.SysmlFactory;

/**
 * Atomic services that create the imports required by a SysML model mutation.
 *
 * @author arichard
 */
public class MetamodelMutationImportService {

    /**
     * Creates the namespace import needed to make {@code elementToImport} visible from the package containing
     * {@code element}, unless an existing import already provides that visibility.
     *
     * @param element
     *            the element whose package requires the import
     * @param elementToImport
     *            the element to make visible
     */
    public void handleImport(Element element, Element elementToImport) {
        List<Namespace> namespacesHierarchy = this.getAllNamespaces(element);
        Namespace elementToImportNamespace = elementToImport.getOwningNamespace();
        if (!namespacesHierarchy.isEmpty() && elementToImportNamespace != null) {
            List<Import> allImports = namespacesHierarchy.stream().map(ns -> ns.getOwnedImport()).flatMap(Collection::stream).toList();
            boolean existingImport = allImports.stream().anyMatch(imprt -> this.isImportForElement(imprt, elementToImport));
            if (!existingImport) {
                Namespace elementNamespace = this.getPackageParent(element);
                if (elementNamespace == null) {
                    elementNamespace = element.getOwningNamespace();
                }
                if (elementNamespace != null && !elementNamespace.equals(elementToImportNamespace)) {
                    NamespaceImport namespaceImport = SysmlFactory.eINSTANCE.createNamespaceImport();
                    elementNamespace.getOwnedRelationship().add(0, namespaceImport);
                    namespaceImport.setImportedNamespace(elementToImportNamespace);
                }
            }
        }
    }

    /**
     * Gets the namespaces that contain {@code element}, from its owning namespace to the root namespace.
     *
     * @param element
     *            the element whose namespace hierarchy is requested
     * @return the namespace hierarchy
     */
    private List<Namespace> getAllNamespaces(Element element) {
        List<Namespace> namespacesHierarchy = new ArrayList<>();
        Namespace elementNamespace = element.getOwningNamespace();
        if (elementNamespace != null) {
            namespacesHierarchy.add(elementNamespace);
            namespacesHierarchy.addAll(this.getNamespacesHierarchy(elementNamespace));
        }
        return namespacesHierarchy;
    }

    /**
     * Gets the namespace ancestors of {@code element}.
     *
     * @param element
     *            the element whose namespace ancestors are requested
     * @return the namespace ancestors, from closest to farthest
     */
    private List<Namespace> getNamespacesHierarchy(Element element) {
        List<Namespace> namespacesHierarchy = new ArrayList<>();
        if (element != null) {
            EObject eContainer = element.eContainer();
            if (eContainer instanceof Namespace ns) {
                namespacesHierarchy.add(ns);
            }
            if (eContainer instanceof Element parentElement) {
                namespacesHierarchy.addAll(this.getNamespacesHierarchy(parentElement));
            }
        }
        return namespacesHierarchy;
    }

    /**
     * Gets the closest package containing {@code element}.
     *
     * @param element
     *            the element whose containing package is requested
     * @return the closest containing package, or {@code null} when none exists
     */
    private org.eclipse.syson.sysml.Package getPackageParent(Element element) {
        org.eclipse.syson.sysml.Package pkg = null;
        if (element != null) {
            EObject eContainer = element.eContainer();
            if (eContainer instanceof org.eclipse.syson.sysml.Package parentPkg) {
                pkg = parentPkg;
            } else if (eContainer instanceof Element parentElement) {
                pkg = this.getPackageParent(parentElement);
            }
        }
        return pkg;
    }

    /**
     * Determines whether {@code imprt} makes {@code elementToImport} visible.
     *
     * @param imprt
     *            the import to inspect
     * @param elementToImport
     *            the element to make visible
     * @return {@code true} when the import already covers the element
     */
    private boolean isImportForElement(Import imprt, Element elementToImport) {
        boolean isImportForElement = false;
        if (imprt instanceof NamespaceImport namespaceImport) {
            Namespace importedNamespace = namespaceImport.getImportedNamespace();
            Namespace elementToImportNamespace = elementToImport.getOwningNamespace();
            if (importedNamespace != null && importedNamespace.equals(elementToImportNamespace)) {
                isImportForElement = true;
            } else if (imprt.isIsRecursive()) {
                isImportForElement = this.isParentOf(importedNamespace, elementToImportNamespace);
            }
        } else if (imprt instanceof MembershipImport membershipImport) {
            Element importedElement = membershipImport.getImportedElement();
            if (importedElement != null && importedElement.equals(elementToImport)) {
                isImportForElement = true;
            } else if (imprt.isIsRecursive()) {
                isImportForElement = this.isParentOf(importedElement, elementToImport);
            }
        }
        return isImportForElement;
    }

    /**
     * Determines whether {@code parent} contains {@code child} in the EMF containment hierarchy.
     *
     * @param parent
     *            the potential ancestor
     * @param child
     *            the potential descendant
     * @return {@code true} when the child is equal to or contained by the parent
     */
    private boolean isParentOf(EObject parent, EObject child) {
        boolean isParentOf = false;
        if (parent != null && child != null) {
            if (child.equals(parent)) {
                isParentOf = true;
            }
            EObject eContainer = child.eContainer();
            if (eContainer != null) {
                isParentOf = this.isParentOf(parent, eContainer);
            }
        }
        return isParentOf;
    }
}
