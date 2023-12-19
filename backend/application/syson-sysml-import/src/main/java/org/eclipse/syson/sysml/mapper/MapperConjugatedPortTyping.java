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
import org.eclipse.syson.sysml.ConjugatedPortDefinition;
import org.eclipse.syson.sysml.ConjugatedPortTyping;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.PortConjugation;
import org.eclipse.syson.sysml.PortDefinition;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements mapping logic specific to ConjugatedPortTyping in SysML models from AST node.
 *
 * @author wldblm
 */
public class MapperConjugatedPortTyping extends MapperVisitorInterface {

    private final Logger logger = LoggerFactory.getLogger(MapperConjugatedPortTyping.class);

    public MapperConjugatedPortTyping(final ObjectFinder objectFinder, final MappingState mappingState) {
        super(objectFinder, mappingState);
    }

    @Override
    public boolean canVisit(final MappingElement mapping) {
        return mapping.getSelf() != null && SysmlPackage.eINSTANCE.getConjugatedPortTyping().isSuperTypeOf(mapping.getSelf().eClass());
    }

    @Override
    public void mappingVisit(final MappingElement mapping) {
        this.logger.debug("Add ConjugatedPortTyping to map for p  = " + mapping.getSelf());

        ConjugatedPortTyping eObject = (ConjugatedPortTyping) mapping.getSelf();
        eObject.getSource().add((Feature) mapping.getParent());

        if (mapping.getMainNode().has(AstConstant.TARGET_REF_CONST) && mapping.getMainNode().get(AstConstant.TARGET_REF_CONST).has(AstConstant.TEXT_CONST)) {
            eObject.setDeclaredName(AstConstant.asCleanedText(mapping.getMainNode().get(AstConstant.TARGET_REF_CONST).get(AstConstant.TEXT_CONST)));
        }

        this.mappingState.toResolve().add(mapping);

    }

    @Override
    public void referenceVisit(final MappingElement mapping) {
        JsonNode subElement = mapping.getMainNode().get(AstConstant.TARGET_REF_CONST);
        EObject referencedObject = this.objectFinder.findObject(mapping, subElement, SysmlPackage.eINSTANCE.getPortDefinition());

        ConjugatedPortTyping eObject = (ConjugatedPortTyping) mapping.getSelf();
        PortDefinition target = (PortDefinition) referencedObject;

        if (target != null) {
            this.logger.debug("Retrieving ConjugatedPortDefinition from PortDefintion " + target);
            ConjugatedPortDefinition conjugatedPort = target.getConjugatedPortDefinition();
            eObject.getTarget().add(target);
            if (conjugatedPort != null) {
                this.logger.debug("Reference ConjugatedPortDefinition of " + target + " to " + eObject);
                eObject.setConjugatedPortDefinition(conjugatedPort);
            } else {
                this.logger.warn("Reference ConjugatedPortDefinition not found in " + target);
                this.logger.info("Creating ConjugatedPortDefinition to " + target);
                OwningMembership owningMembership = SysmlFactory.eINSTANCE.createOwningMembership();
                target.getOwnedRelationship().add(owningMembership);

                ConjugatedPortDefinition conjugatedPortDefinition = SysmlFactory.eINSTANCE.createConjugatedPortDefinition();
                owningMembership.getOwnedRelatedElement().add(conjugatedPortDefinition);
                conjugatedPortDefinition.setDeclaredName("~" + target.getDeclaredName());

                PortConjugation portConjugation = SysmlFactory.eINSTANCE.createPortConjugation();
                portConjugation.setOriginalPortDefinition(target);
                portConjugation.setOriginalType(target);
                portConjugation.setConjugatedType(conjugatedPortDefinition);

                conjugatedPortDefinition.getOwnedRelationship().add(portConjugation);

                eObject.setConjugatedPortDefinition(conjugatedPortDefinition);
            }
        } else {
            this.logger.warn("Reference PortDefinition not found " + subElement);
        }

    }
}
