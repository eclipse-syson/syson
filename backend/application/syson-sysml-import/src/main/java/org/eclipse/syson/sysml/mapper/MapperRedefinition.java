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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.syson.sysml.AstConstant;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.finder.ObjectFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Implements mapping logic specific to Redefintion in SysML models from AST node.
 *
 * @author gescande
 */
public class MapperRedefinition extends MapperVisitorInterface {

    private final Logger logger = LoggerFactory.getLogger(MapperRedefinition.class);

    public MapperRedefinition(final ObjectFinder objectFinder, final MappingState mappingState) {
        super(objectFinder, mappingState);
    }

    @Override
    public boolean canVisit(final MappingElement mapping) {
        return mapping.getSelf() instanceof Redefinition && mapping.getMainNode().has(AstConstant.TARGET_REF_CONST);
    }

    @Override
    public void mappingVisit(final MappingElement mapping) {
        this.logger.debug("Add Redefinition to map for p  = " + mapping.getSelf());

        Redefinition eObject = (Redefinition) mapping.getSelf();
        eObject.setRedefiningFeature((Feature) mapping.getParent());

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
        EObject referencedObject = this.objectFinder.findObject(mapping, subElement, SysmlPackage.eINSTANCE.getFeature());

        Redefinition eObject = (Redefinition) mapping.getSelf();

        if (referencedObject instanceof Feature target) {
            this.logger.debug("Reference Redefinition " + eObject + " to " + target);
            eObject.setRedefinedFeature(target);
        } else {
            this.logger.warn("Reference Redefinition not found " + subElement);
        }
    }
}
