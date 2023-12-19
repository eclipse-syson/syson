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
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.FlowConnectionUsage;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.SysmlPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements mapping logic specific to FlowConnectionUsage in SysML models from AST node.
 *
 * @author gescande
 */
public class MapperFlowConnectionUsage extends MapperVisitorInterface {

    private final Logger logger = LoggerFactory.getLogger(MapperFlowConnectionUsage.class);

    public MapperFlowConnectionUsage(final ObjectFinder objectFinder, final MappingState mappingState) {
        super(objectFinder, mappingState);
    }

    @Override
    public boolean canVisit(final MappingElement mapping) {
        return mapping.getSelf() != null && SysmlPackage.eINSTANCE.getFlowConnectionUsage().isSuperTypeOf(mapping.getSelf().eClass()) && mapping.getMainNode().has(AstConstant.TARGET_REF_CONST);
    }

    @Override
    public void mappingVisit(final MappingElement mapping) {
        this.logger.debug("Add FlowConnectionUsage to map for p  = " + mapping.getSelf());

        FlowConnectionUsage eObject = (FlowConnectionUsage) mapping.getSelf();
        if (mapping.getMainNode().has(AstConstant.TARGET_REF_CONST) && mapping.getMainNode().get(AstConstant.TARGET_REF_CONST).has(AstConstant.TEXT_CONST)) {
            eObject.setDeclaredName(AstConstant.asCleanedText(mapping.getMainNode().get(AstConstant.TARGET_REF_CONST).get(AstConstant.TEXT_CONST)));
        }

        this.mappingState.toResolve().add(mapping);
    }

    @Override
    public void referenceVisit(final MappingElement mapping) {
        FlowConnectionUsage eObject = (FlowConnectionUsage) mapping.getSelf();
        mapping.getMainNode().get("ends").forEach(subElement -> {

            EObject referencedObject = this.objectFinder.findObject(mapping, subElement, SysmlPackage.eINSTANCE.getFeatureMembership());
            FeatureMembership target = (FeatureMembership) referencedObject;

            if (target != null) {
                this.logger.debug("Map FlowConnectionUsage object " + mapping.getSelf() + " ends to object " + referencedObject);
                target.setOwningRelationship((Relationship) mapping.getSelf());
            } else {
                this.logger.warn("Reference FlowConnectionUsage not found " + subElement);
            }
        });

    }
}
