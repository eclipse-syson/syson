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
import org.eclipse.syson.sysml.FeatureReferenceExpression;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.SysmlPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements mapping logic specific to FeatureReferenceExpression in SysML models from AST node.
 *
 * @author wldblm
 */
public class MapperFeatureReferenceExpression extends MapperVisitorInterface {

    private final Logger logger = LoggerFactory.getLogger(MapperFeatureReferenceExpression.class);

    public MapperFeatureReferenceExpression(final ObjectFinder objectFinder, final MappingState mappingState) {
        super(objectFinder, mappingState);
    }

    @Override
    public boolean canVisit(final MappingElement mapping) {
        return mapping.getSelf() != null && SysmlPackage.eINSTANCE.getFeatureReferenceExpression().isSuperTypeOf(mapping.getSelf().eClass());
    }

    @Override
    public void mappingVisit(final MappingElement mapping) {
        this.logger.debug("Add FeatureReferenceExpression to map for p  = " + mapping.getSelf());

        if (mapping.getMainNode().has(AstConstant.EXPRESSION_CONST)) {
            this.mappingState.toMap().add(new MappingElement(mapping.getMainNode().get(AstConstant.EXPRESSION_CONST), mapping.getSelf()));
        }

        this.mappingState.toResolve().add(mapping);
    }

    @Override
    public void referenceVisit(final MappingElement mapping) {
        JsonNode subElement = mapping.getMainNode().get(AstConstant.EXPRESSION_CONST);
        EObject referencedObject = this.objectFinder.findObject(mapping, subElement, SysmlPackage.eINSTANCE.getMembership());

        FeatureReferenceExpression eObject = (FeatureReferenceExpression) mapping.getSelf();
        Membership target = (Membership) referencedObject;

        if (target != null) {
            this.logger.debug("Map FeatureReferenceExpression object " + mapping.getSelf() + " expression to object " + referencedObject);
            target.setOwningRelatedElement(eObject);
        } else {
            this.logger.warn("Reference FeatureReferenceExpression not found " + subElement);
        }

    }
}
