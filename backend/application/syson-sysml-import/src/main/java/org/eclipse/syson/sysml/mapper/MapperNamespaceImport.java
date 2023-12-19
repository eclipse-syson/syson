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
package org.eclipse.syson.sysml.mapper;

import com.fasterxml.jackson.databind.JsonNode;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.syson.sysml.AstConstant;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.NamespaceImport;
import org.eclipse.syson.sysml.SysmlPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements mapping logic specific to NamespaceImport in SysML models from AST node.
 *
 * @author gescande
 */
public class MapperNamespaceImport extends MapperVisitorInterface {

    private final Logger logger = LoggerFactory.getLogger(MapperNamespaceImport.class);

    public MapperNamespaceImport(final ObjectFinder objectFinder, final MappingState mappingState) {
        super(objectFinder, mappingState);
    }

    @Override
    public boolean canVisit(final MappingElement mapping) {
        return mapping.getSelf() != null && SysmlPackage.eINSTANCE.getNamespaceImport().isSuperTypeOf(mapping.getSelf().eClass()) && mapping.getMainNode().has(AstConstant.TARGET_REF_CONST);
    }

    @Override
    public void mappingVisit(final MappingElement mapping) {
        this.logger.debug("Add NamespaceImport to map for p  = " + mapping.getSelf());

        NamespaceImport eObject = (NamespaceImport) mapping.getSelf();
        String importText = AstConstant.asCleanedText(mapping.getMainNode().get(AstConstant.TARGET_REF_CONST).get(AstConstant.TEXT_CONST));
        if (SysmlPackage.eINSTANCE.getFeature().isSuperTypeOf(mapping.getParent().eClass())) {
            eObject.getSource().add((Feature) mapping.getParent());
        }

        eObject.setDeclaredName(importText);

        this.objectFinder.addImportNamespace(importText + "::.*");

        this.mappingState.toResolve().add(mapping);
    }

    @Override
    public void referenceVisit(final MappingElement mapping) {
        JsonNode subElement = mapping.getMainNode().get(AstConstant.TARGET_REF_CONST);
        EObject referencedObject = this.objectFinder.findObject(mapping, subElement, SysmlPackage.eINSTANCE.getNamespace());

        NamespaceImport eObject = (NamespaceImport) mapping.getSelf();
        Namespace target = (Namespace) referencedObject;

        if (target != null) {
            this.logger.debug("Reference NamespaceImport " + eObject + " to " + target);
            eObject.setImportedNamespace(target);
            eObject.getTarget().add(target);
        } else {
            this.logger.warn("Reference NamespaceImport not found " + subElement);
        }
    }
}
