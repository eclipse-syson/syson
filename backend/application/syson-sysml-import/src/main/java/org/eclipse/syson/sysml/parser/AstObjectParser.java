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

import java.math.BigDecimal;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.EClassImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.syson.sysml.AstConstant;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.LiteralInteger;
import org.eclipse.syson.sysml.SysmlPackage;
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
    
    public void setObjectAttribute(final EObject eObject, final JsonNode astJson) {
        for (final EAttribute attribute : eObject.eClass().getEAllAttributes()) {
            if (attribute.isDerived() || attribute.isUnsettable()) {
                continue;
            }

            final String mappedName = computeAttribute(eObject, attribute.getName());

            if (!astJson.has(mappedName) || mappedName.equals("isNonunique") || mappedName.equals("isReference")) {
                continue;
            }

            try {
                if (attribute.getEType() instanceof EEnum) {
                    final EEnum eenum = (EEnum) attribute.getEType();
                    final String value = AstConstant.asCleanedText(astJson.get(mappedName));
                    final EEnumLiteral enumLiteral = eenum.getEEnumLiteral(value);
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
                            final String name = AstConstant.asCleanedText(astJson.get(mappedName));
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
            } catch (final IllegalArgumentException e) {
                AstObjectParser.LOGGER.warn("Try to change an non changeable attribute" + eObject.eClass().getName() + "::" + mappedName);
            }
        }
    }

    public EObject createObject(final JsonNode astJson) {
        String type = astJson.findValue(AstConstant.TYPE_CONST).textValue();
    
    
        if (type.equals("MembershipReference")) {
            type = "Membership";
        }
        if (type.equals("SysMLFunction")) {
            type = "Function";
        }
        if (type.equals("TypeReference")) {
            type = "Type";
        }
        if (type.equals("FeatureReference")) {
            type = "Feature";
        }
        if (type.equals("ConjugatedPortReference")) {
            type = "ConjugatedPortDefinition";
        }
        if (type.equals("ClassifierReference")) {
            type = "Classifier";
        }
        if (type.equals("ElementReference")) {
            type = "Type";
        }
        if (type.equals("MembershipReference")) {
            type = "Membership";
        }
        
    
        if (type.equals("NamespaceReference")) {
            type = "Namespace";
        }
        if ("LiteralNumber".equals(type)) {
            final String literalValue = astJson.get(AstConstant.LITERAL_CONST).asText();
            if (literalValue != null) {
                try {
                    final BigDecimal bd = new BigDecimal(literalValue);
                    bd.intValueExact();
                    type = "LiteralInteger";
                } catch (final ArithmeticException e) {
                    type = "LiteralRational";
                }
            }
        }
        
    
        final EClassifier classif = SysmlPackage.eINSTANCE.getEClassifier(type);
        final EClassImpl eclassImpl = (EClassImpl) classif;
    
        if (classif == null) {
            return null;
        } else {
            return EcoreUtil.create(eclassImpl);
        }
    }

    private String computeAttribute(final EObject eObject, final String attributeName) {
        String computedAttributeName = attributeName;
        if (eObject instanceof LiteralInteger && "value".equals(attributeName)) {
            computedAttributeName = "literal";
        }   
        return computedAttributeName;
    }
}


