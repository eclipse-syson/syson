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
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements mapping logic specific to FeatureTyping in SysML models from AST node.
 *
 * @author gescande
 */
public class MapperFeatureTyping extends MapperVisitorInterface {

    private final Logger logger = LoggerFactory.getLogger(MapperFeatureTyping.class);

    public MapperFeatureTyping(final ObjectFinder objectFinder, final MappingState mappingState) {
        super(objectFinder, mappingState);
    }

    @Override
    public boolean canVisit(final MappingElement mapping) {
        return mapping.getSelf() instanceof FeatureTyping && mapping.getMainNode().has(AstConstant.TARGET_REF_CONST);
    }

    @Override
    public void mappingVisit(final MappingElement mapping) {
        this.logger.debug("Add FeatureTyping to map for p  = " + mapping.getSelf());

        FeatureTyping eObject = (FeatureTyping) mapping.getSelf();
        eObject.getSource().add((Feature) mapping.getParent());
        eObject.setTypedFeature((Feature) mapping.getParent());

        if (mapping.getMainNode().has(AstConstant.TARGET_REF_CONST) && mapping.getMainNode().get(AstConstant.TARGET_REF_CONST).has(AstConstant.TEXT_CONST)) {
            eObject.setDeclaredName(AstConstant.asCleanedText(mapping.getMainNode().get(AstConstant.TARGET_REF_CONST).get(AstConstant.TEXT_CONST)));
        }

        this.mappingState.toResolve().add(mapping);
    }

    @Override
    public void referenceVisit(final MappingElement mapping) {
        JsonNode subElement = mapping.getMainNode().get(AstConstant.TARGET_REF_CONST);
        EObject referencedObject = this.objectFinder.findObject(mapping, subElement, SysmlPackage.eINSTANCE.getType());

        FeatureTyping eObject = (FeatureTyping) mapping.getSelf();

        if (referencedObject instanceof Type target) {
            this.logger.debug("Reference FeatureTyping " + eObject + " to " + target);
            eObject.setType(target);
            if (target instanceof Feature) {
                eObject.getTarget().add(target);
                this.logger.debug("add to " + eObject + " target " + target);
            }

        } else {
            this.logger.warn("Reference FeatureTyping not found " + subElement);
        }

    }
}
