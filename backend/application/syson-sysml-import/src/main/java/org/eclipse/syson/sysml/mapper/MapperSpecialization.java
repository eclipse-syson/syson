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
import org.eclipse.syson.sysml.Specialization;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements mapping logic specific to Specialization in SysML models from AST node.
 *
 * @author gescande
 */
public class MapperSpecialization extends MapperVisitorInterface {

    private final Logger logger = LoggerFactory.getLogger(MapperSpecialization.class);

    public MapperSpecialization(final ObjectFinder objectFinder, final MappingState mappingState) {
        super(objectFinder, mappingState);
    }

    @Override
    public boolean canVisit(final MappingElement mapping) {
        return mapping.getSelf() != null && SysmlPackage.eINSTANCE.getSpecialization().isSuperTypeOf(mapping.getSelf().eClass()) && mapping.getMainNode().has(AstConstant.TARGET_REF_CONST);
    }

    @Override
    public void mappingVisit(final MappingElement mapping) {
        this.logger.debug("Add Specialization to map for p  = " + mapping.getSelf());

        Specialization eObject = (Specialization) mapping.getSelf();
        if (SysmlPackage.eINSTANCE.getType().isSuperTypeOf(mapping.getParent().eClass())) {
            eObject.setSpecific((Type) mapping.getParent());
        }

        if (mapping.getMainNode().has(AstConstant.TARGET_REF_CONST) && mapping.getMainNode().get(AstConstant.TARGET_REF_CONST).has(AstConstant.TEXT_CONST)) {
            eObject.setDeclaredName(AstConstant.asCleanedText(mapping.getMainNode().get(AstConstant.TARGET_REF_CONST).get(AstConstant.TEXT_CONST)));
        }

        this.mappingState.toResolve().add(mapping);
    }

    @Override
    public void referenceVisit(final MappingElement mapping) {
        if (!mapping.getMainNode().has(AstConstant.TARGET_REF_CONST)) {
            this.logger.error("Error of attended terget ref on node : " + mapping.getMainNode());
        }
        JsonNode subElement = mapping.getMainNode().get(AstConstant.TARGET_REF_CONST);
        EObject referencedObject = this.objectFinder.findObject(mapping, subElement, SysmlPackage.eINSTANCE.getType());

        Specialization eObject = (Specialization) mapping.getSelf();
        Type target = (Type) referencedObject;

        if (target != null) {
            this.logger.debug("Reference Specialization " + eObject + " to " + target);
            eObject.setGeneral(target);
        } else {
            this.logger.warn("Reference Specialization not found " + subElement);
        }
    }
}
