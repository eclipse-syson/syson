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
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.OperatorExpression;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.finder.ObjectFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements mapping logic specific to OperatorExpression in SysML models from AST node.
 *
 * @author gescande
 */
public class MapperOperatorExpression extends MapperVisitorInterface {

    private final Logger logger = LoggerFactory.getLogger(MapperOperatorExpression.class);

    public MapperOperatorExpression(final ObjectFinder objectFinder, final MappingState mappingState) {
        super(objectFinder, mappingState);
    }

    @Override
    public boolean canVisit(final MappingElement mapping) {
        return mapping.getSelf() instanceof OperatorExpression;
    }

    @Override
    public void mappingVisit(final MappingElement mapping) {
        this.logger.debug("Add OperatorExpression to map for p  = " + mapping.getSelf());

        if (mapping.getMainNode().has(AstConstant.OPERANDS)) {
            this.mappingState.toMap().add(new MappingElement(mapping.getMainNode().get(AstConstant.OPERANDS), mapping.getSelf()));
        }

        this.mappingState.toResolve().add(mapping);
    }

    @Override
    public void referenceVisit(final MappingElement mapping) {
        Element eObject = (Element) mapping.getSelf();
        mapping.getMainNode().get(AstConstant.OPERANDS).forEach(subElement -> {

            EObject referencedObject = this.objectFinder.findObject(mapping, subElement, SysmlPackage.eINSTANCE.getExpression());
            if (referencedObject instanceof Element target) {
                this.logger.debug("Map OperatorExpression object " + mapping.getSelf() + " operands to object " + referencedObject);
                OwningMembership relation = SysmlFactory.eINSTANCE.createOwningMembership();
                relation.setOwningRelatedElement(eObject);
                target.setOwningRelationship(relation);
            } else {
                this.logger.warn("Reference OperatorExpression not found " + subElement);
            }
        });

    }
}
