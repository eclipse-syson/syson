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
package org.eclipse.syson.sysml.parser;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.syson.sysml.AstConstant;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.LiteralInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * AstObjectParser.
 *
 * @author gescande.
 */
public class AstObjectParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(AstObjectParser.class);
    
    public static void setObjectAttribute(EObject eObject, JsonNode astJson) {
        for (EAttribute attribute : eObject.eClass().getEAllAttributes()) {
            if (attribute.isDerived() || attribute.isUnsettable()) {
                continue;
            }

            String mappedName = computeAttribute(eObject, attribute.getName());

            if (!astJson.has(mappedName) || mappedName.equals("isNonunique") || mappedName.equals("isReference")) {
                continue;
            }

            try {
                if (attribute.getEType() instanceof EEnum) {
                    EEnum eenum = (EEnum) attribute.getEType();
                    String value = AstConstant.asCleanedText(astJson.get(mappedName));
                    EEnumLiteral enumLiteral = eenum.getEEnumLiteral(value);
                    if (enumLiteral != null) {
                        eObject.eSet(attribute, enumLiteral.getInstance());
                    }
                } else {
                    switch (attribute.getEType().getName()) {
                        case "EDouble":
                            eObject.eSet(attribute, astJson.get(mappedName).asDouble());
                            break;
                        case "EInt":
                        case "EInteger":
                            eObject.eSet(attribute, astJson.get(mappedName).asInt());
                            break;
                        case "EBoolean":
                            eObject.eSet(attribute, astJson.get(mappedName).asBoolean());
                            break;
                        case "EString":
                            String name = AstConstant.asCleanedText(astJson.get(mappedName));
                            if (!name.equals("null")) {
                                eObject.eSet(attribute, name);
                            }
                            break;
                        case "FeatureDirectionKind":
                            eObject.eSet(attribute, FeatureDirectionKind.getByName(AstConstant.asCleanedText(astJson.get(mappedName)).toUpperCase()));
                            break;
                        default:
                            AstObjectParser.LOGGER.warn("Unknown base type " + attribute.getEType().getName() + " " + mappedName + " " + astJson.get(mappedName));
                            break;
                    }
                }
            } catch (IllegalArgumentException e) {
                AstObjectParser.LOGGER.warn("Try to change an non changeable attribute" + eObject.eClass().getName() + "::" + mappedName);
            }
        }
    }

    private static String computeAttribute(EObject eObject, String attributeName) {
        String computedAttributeName = attributeName;
        if (eObject instanceof LiteralInteger && "value".equals(attributeName)) {
            computedAttributeName = "literal";
        }   
        return computedAttributeName;
    }
}


