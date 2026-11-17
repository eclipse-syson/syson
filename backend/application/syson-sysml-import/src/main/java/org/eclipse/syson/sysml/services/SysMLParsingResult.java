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
package org.eclipse.syson.sysml.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.sirius.components.representations.Message;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.NamespaceImport;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.SysmlFactory;

/**
 * Result of parsing and linking a document or fragment.
 *
 * @param root the detached root namespace, or null if no model could be recovered
 * @param messages the diagnostics to report
 * @param importsToAdd the original namespaces and libraries to import only when committing the parsed content
 * @author arichard
 */
public record SysMLParsingResult(Namespace root, List<Message> messages, Map<Namespace, Set<Namespace>> importsToAdd) {

    /**
     * Commits required imports to existing namespaces after the caller accepts the parsed content.
     * Existing imports are checked again because another operation may already have added them.
     */
    public void applyImports() {
        this.importsToAdd.forEach((owner, libraries) -> libraries.forEach(library -> {
            boolean alreadyImported = owner.getOwnedRelationship().stream()
                    .filter(NamespaceImport.class::isInstance).map(NamespaceImport.class::cast)
                    .anyMatch(namespaceImport -> namespaceImport.getImportedNamespace() == library);
            if (!alreadyImported) {
                NamespaceImport namespaceImport = SysmlFactory.eINSTANCE.createNamespaceImport();
                namespaceImport.setImportedNamespace(library);
                owner.getOwnedRelationship().add(namespaceImport);
            }
        }));
    }

    /**
     * Gets the top-level elements, retaining imports and other unowned relationships.
     *
     * @return the parsed elements
     */
    public List<Element> elements() {
        List<Element> elements = new ArrayList<>();
        if (this.root != null) {
            for (var relationship : this.root.getOwnedRelationship()) {
                if (relationship instanceof OwningMembership) {
                    elements.addAll(relationship.getOwnedRelatedElement());
                } else {
                    elements.add(relationship);
                }
            }
        }
        return elements;
    }
}
