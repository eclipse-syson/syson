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
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.FeatureValue;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.finder.ObjectFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Implements mapping logic specific to FeatureValue in SysML models from AST node.
 *
 * @author gescande
 */
public class MapperFeatureValue extends MapperVisitorInterface {

    private final Logger logger = LoggerFactory.getLogger(MapperFeatureValue.class);

    public MapperFeatureValue(final ObjectFinder objectFinder, final MappingState mappingState) {
        super(objectFinder, mappingState);
    }

    @Override
    public boolean canVisit(final MappingElement mapping) {
        return mapping.getSelf() instanceof FeatureValue && mapping.getMainNode().has(AstConstant.TARGET_CONST);
    }

    @Override
    public void mappingVisit(final MappingElement mapping) {
        this.logger.debug("Add FeatureValue to map for p  = " + mapping.getSelf());
        this.mappingState.toResolve().add(mapping);
    }

    @Override
    public void referenceVisit(final MappingElement mapping) {
        JsonNode subElement = mapping.getMainNode().get(AstConstant.TARGET_CONST);
        EObject referencedObject = this.objectFinder.findObject(mapping, subElement, SysmlPackage.eINSTANCE.getExpression());

        FeatureValue eObject = (FeatureValue) mapping.getSelf();

        if (referencedObject instanceof Expression target) {
            this.logger.debug("Reference FeatureTyping " + eObject + " to " + target);
        } else {
            this.logger.warn("Reference FeatureTyping not found " + subElement);
        }
    }
}
