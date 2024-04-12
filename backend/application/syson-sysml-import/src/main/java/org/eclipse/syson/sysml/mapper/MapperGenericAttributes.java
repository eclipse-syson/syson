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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.syson.sysml.AstConstant;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.finder.ObjectFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Maps generic attributes from AST nodes to SysML model elements based on specified mappings.
 *
 * @author gescande
 */
public class MapperGenericAttributes extends MapperVisitorInterface {

    private final Logger logger = LoggerFactory.getLogger(MapperGenericAttributes.class);

    public MapperGenericAttributes(final ObjectFinder objectFinder, final MappingState mappingState) {
        super(objectFinder, mappingState);
    }

    @Override
    public boolean canVisit(final MappingElement mapping) {
        return mapping.getMainNode().has(AstConstant.TYPE_CONST) && mapping.getSelf() != null && !mapping.getSelf().eClass().getEAllAttributes().isEmpty();
    }

    @Override
    public void mappingVisit(final MappingElement mapping) {
        this.logger.debug("map attributes of  = " + mapping.getSelf());

        this.genericAttributeMapping(mapping.getSelf(), mapping.getMainNode());

        this.mappingState.done().add(mapping);
    }

    @Override
    public void referenceVisit(final MappingElement mapping) {
    }

    private void genericAttributeMapping(EObject eObject, JsonNode mainNode) {
        for (EAttribute attribute : eObject.eClass().getEAllAttributes()) {
            String mappedName = attribute.getName();

            try {
                if (!mainNode.has(mappedName) || mappedName.equals("isNonunique") || mappedName.equals(
                        "isReference")) {
                    continue;
                }
                if (attribute.getEType() instanceof EEnum) {
                    EEnum eenum = (EEnum) attribute.getEType();
                    String value = AstConstant.asCleanedText(mainNode.get(mappedName));
                    EEnumLiteral enumLiteral = eenum.getEEnumLiteral(value);
                    if (enumLiteral != null) {
                        eObject.eSet(attribute, enumLiteral.getInstance());
                    }
                } else {
                    switch (attribute.getEType().getName()) {
                        case "EDouble":
                            eObject.eSet(attribute, mainNode.get(mappedName).asDouble());
                            break;
                        case "EInteger":
                            eObject.eSet(attribute, mainNode.get(mappedName).asInt());
                            break;
                        case "EBoolean":
                            eObject.eSet(attribute, mainNode.get(mappedName).asBoolean());
                            break;
                        case "EString":
                            String name = AstConstant.asCleanedText(mainNode.get(mappedName));
                            if (!name.equals("null")) {
                                eObject.eSet(attribute, name);
                            }
                            break;
                        case "FeatureDirectionKind":
                            eObject.eSet(attribute, FeatureDirectionKind.getByName(AstConstant.asCleanedText(mainNode.get(mappedName)).toUpperCase()));
                            break;
                        default:
                            this.logger.warn("Unknown base type " + attribute.getEType().getName() + " " + mappedName + " " + mainNode.get(mappedName));
                            break;
                    }
                }
            } catch (IllegalArgumentException e) {
                this.logger.warn("Try to change an non changeable attribute" + eObject.eClass().getName() + "::" + mappedName);
            }
        }
    }
}
