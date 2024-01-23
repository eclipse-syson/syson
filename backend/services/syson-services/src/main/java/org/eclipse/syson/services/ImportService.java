/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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
package org.eclipse.syson.services;

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
 * Import-related Java services used by SysON representations.
 *
 * @author arichard
 */
public class ImportService {

    /**
     * Add import corresponding to "elementToImport" in the Package of the given {@link Element}. If an import already
     * handle the "elementToImport" in the namespace hierarchy, then no new import is added.
     * 
     * @param element
     *            the given {@link Element}.
     * @param elementToImport
     *            the element for which an import must be added.
     */
    public void handleImport(Element element, Element elementToImport) {
        List<Namespace> namespacesHierarchy = getAllNamespaces(element);
        Namespace elementToImportNamespace = elementToImport.getOwningNamespace();
        if (!namespacesHierarchy.isEmpty() && elementToImportNamespace != null) {
            List<Import> allImports = namespacesHierarchy.stream().map(ns -> ns.getOwnedImport()).flatMap(Collection::stream).toList();
            boolean existingImport = allImports.stream().anyMatch(imprt -> isImportForElement(imprt, elementToImport));
            if (!existingImport) {
                NamespaceImport namespaceImport = SysmlFactory.eINSTANCE.createNamespaceImport();
                Namespace elementNamespace = getPackageParent(element);
                if (elementNamespace == null) {
                    elementNamespace = element.getOwningNamespace();
                }
                elementNamespace.getOwnedRelationship().add(0, namespaceImport);
                namespaceImport.setImportedNamespace(elementToImportNamespace);
            }
        }
    }

    private List<Namespace> getAllNamespaces(Element element) {
        List<Namespace> namespacesHierarchy = new ArrayList<>();
        Namespace elementNamespace = element.getOwningNamespace();
        if (elementNamespace != null) {
            namespacesHierarchy.add(elementNamespace);
            namespacesHierarchy.addAll(getNamespacesHierarchy(elementNamespace));
        }
        return namespacesHierarchy;
    }

    private List<Namespace> getNamespacesHierarchy(Element element) {
        List<Namespace> namespacesHierarchy = new ArrayList<>();
        if (element != null) {
            EObject eContainer = element.eContainer();
            if (eContainer instanceof Namespace ns) {
                namespacesHierarchy.add(ns);
            }
            if (eContainer instanceof Element parentElement) {
                namespacesHierarchy.addAll(getNamespacesHierarchy(parentElement));
            }
        }
        return namespacesHierarchy;
    }

    private org.eclipse.syson.sysml.Package getPackageParent(Element element) {
        org.eclipse.syson.sysml.Package pkg = null;
        if (element != null) {
            EObject eContainer = element.eContainer();
            if (eContainer instanceof org.eclipse.syson.sysml.Package parentPkg) {
                pkg = parentPkg;
            } else if (eContainer instanceof Element parentElement) {
                pkg = getPackageParent(parentElement);
            }
        }
        return pkg;
    }

    private boolean isImportForElement(Import imprt, Element elementToImport) {
        boolean isImportForElement = false;
        if (imprt instanceof NamespaceImport namespaceImport) {
            Namespace importedNamespace = namespaceImport.getImportedNamespace();
            Namespace elementToImportNamespace = elementToImport.getOwningNamespace();
            if (importedNamespace != null && importedNamespace.equals(elementToImportNamespace)) {
                isImportForElement = true;
            }
        } else if (imprt instanceof MembershipImport membershipImport) {
            Element importedElement = membershipImport.getImportedElement();
            if (importedElement != null && importedElement.equals(elementToImport)) {
                isImportForElement = true;
            }
        }
        return isImportForElement;
    }
}
