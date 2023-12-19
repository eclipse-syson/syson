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
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.SysmlPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements mapping logic specific to Membership in SysML models from AST node.
 *
 * @author gescande
 */
public class MapperMembership extends MapperVisitorInterface {

    private final Logger logger = LoggerFactory.getLogger(MapperMembership.class);

    public MapperMembership(final ObjectFinder objectFinder, final MappingState mappingState) {
        super(objectFinder, mappingState);
    }

    @Override
    public boolean canVisit(final MappingElement mapping) {
        return mapping.getSelf() != null && SysmlPackage.eINSTANCE.getMembership().isSuperTypeOf(mapping.getSelf().eClass());
    }

    @Override
    public void mappingVisit(final MappingElement mapping) {
        this.logger.debug("Add Membership to map for p  = " + mapping.getSelf());
        this.mappingState.toResolve().add(mapping);
    }

    @Override
    public void referenceVisit(final MappingElement mapping) {
        AstConstant.NODES.stream().filter(n -> mapping.getMainNode().has(n)).forEach(n -> {
            JsonNode subElement = mapping.getMainNode().get(n);
            EObject referencedObject = this.objectFinder.findObject(mapping, subElement, SysmlPackage.eINSTANCE.getElement());

            Membership eObject = (Membership) mapping.getSelf();
            Element target = (Element) referencedObject;

            if (target != null) {
                this.logger.debug("Add Membership target between " + eObject + " to " + target);
                eObject.setMemberElement(target);
                eObject.setMemberName(target.getDeclaredName());
                eObject.setMemberShortName(target.getShortName());
            } else {
                this.logger.warn("Reference Membership not found " + mapping.getMainNode());
            }
        });
    }
}
