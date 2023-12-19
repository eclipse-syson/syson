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
import org.eclipse.syson.sysml.Classifier;
import org.eclipse.syson.sysml.Subclassification;
import org.eclipse.syson.sysml.SysmlPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *Implements mapping logic specific to Subclassification in SysML models from AST node.
 *
 * @author gescande
 */
public class MapperSubclassification extends MapperVisitorInterface {

    private final Logger logger = LoggerFactory.getLogger(MapperSubclassification.class);

    public MapperSubclassification(final ObjectFinder objectFinder, final MappingState mappingState) {
        super(objectFinder, mappingState);
    }

    @Override
    public boolean canVisit(final MappingElement mapping) {
        return mapping.getSelf() != null && SysmlPackage.eINSTANCE.getSubclassification().isSuperTypeOf(mapping.getSelf().eClass()) && mapping.getMainNode().has(AstConstant.TARGET_REF_CONST);
    }

    @Override
    public void mappingVisit(final MappingElement mapping) {
        this.logger.debug("Add Subclassification to map for p  = " + mapping.getSelf());

        Subclassification eObject = (Subclassification) mapping.getSelf();
        if (SysmlPackage.eINSTANCE.getClassifier().isSuperTypeOf(mapping.getParent().eClass())) {
            eObject.setSubclassifier((Classifier) mapping.getParent());
        }

        if (mapping.getMainNode().has(AstConstant.TARGET_REF_CONST) && mapping.getMainNode().get(AstConstant.TARGET_REF_CONST).has(AstConstant.TEXT_CONST)) {
            eObject.setDeclaredName(AstConstant.asCleanedText(mapping.getMainNode().get(AstConstant.TARGET_REF_CONST).get(AstConstant.TEXT_CONST)));
        }

        this.mappingState.toResolve().add(mapping);
    }

    @Override
    public void referenceVisit(final MappingElement mapping) {
        JsonNode subElement = mapping.getMainNode().get(AstConstant.TARGET_REF_CONST);
        EObject referencedObject = this.objectFinder.findObject(mapping, subElement, SysmlPackage.eINSTANCE.getClassifier());

        Subclassification eObject = (Subclassification) mapping.getSelf();
        Classifier target = (Classifier) referencedObject;

        if (target != null) {
            this.logger.debug("Reference Subclassification " + eObject + " to " + target);
            eObject.setSuperclassifier(target);
        } else {
            this.logger.warn("Reference Subclassification not found " + subElement);
        }
    }
}
