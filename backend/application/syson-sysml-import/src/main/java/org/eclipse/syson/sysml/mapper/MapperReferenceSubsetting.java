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
import org.eclipse.syson.sysml.ReferenceSubsetting;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.finder.ObjectFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements mapping logic specific to ReferenceSubsetting in SysML models from AST node.
 *
 * @author gescande
 */
public class MapperReferenceSubsetting extends MapperVisitorInterface {

    private final Logger logger = LoggerFactory.getLogger(MapperReferenceSubsetting.class);

    public MapperReferenceSubsetting(final ObjectFinder objectFinder, final MappingState mappingState) {
        super(objectFinder, mappingState);
    }

    @Override
    public boolean canVisit(final MappingElement mapping) {
        return mapping.getSelf() instanceof ReferenceSubsetting;
    }

    @Override
    public void mappingVisit(final MappingElement mapping) {
        this.logger.debug("Add ReferenceSubsetting to map for p  = " + mapping.getSelf());

        ReferenceSubsetting eObject = (ReferenceSubsetting) mapping.getSelf();
        eObject.setSubsettingFeature((Feature) mapping.getParent());

        if (mapping.getMainNode().has(AstConstant.TARGET_REF_CONST) && mapping.getMainNode().get(AstConstant.TARGET_REF_CONST).has(AstConstant.TEXT_CONST)) {
            eObject.setDeclaredName(AstConstant.asCleanedText(mapping.getMainNode().get(AstConstant.TARGET_REF_CONST).get(AstConstant.TEXT_CONST)));
        }

        this.mappingState.toResolve().add(mapping);
    }

    @Override
    public void referenceVisit(final MappingElement mapping) {
        JsonNode subElement;
        if (mapping.getMainNode().has(AstConstant.TARGET_CHAIN_CONST)) {
            subElement = mapping.getMainNode().get(AstConstant.TARGET_CHAIN_CONST);
        } else {
            subElement = mapping.getMainNode().get(AstConstant.TARGET_REF_CONST);
        }
        EObject referencedObject = this.objectFinder.findObject(mapping, subElement, SysmlPackage.eINSTANCE.getFeature());

        ReferenceSubsetting eObject = (ReferenceSubsetting) mapping.getSelf();

        if (referencedObject instanceof Feature target) {
            this.logger.debug("Reference ReferenceSubsetting " + eObject + " to " + target);
            eObject.setReferencedFeature(target);
        } else {
            this.logger.warn("Reference ReferenceSubsetting not found " + subElement);
        }

    }
}
