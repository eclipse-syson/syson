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

import com.fasterxml.jackson.databind.JsonNode;

import java.math.BigDecimal;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.syson.sysml.AstConstant;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.LiteralInteger;
import org.eclipse.syson.sysml.SysmlPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AstObjectParser.
 *
 * @author gescande.
 */
public class AstObjectParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(AstObjectParser.class);

    public void setObjectAttribute(final EObject eObject, final JsonNode astJson) {
        for (final EAttribute attribute : eObject.eClass().getEAllAttributes()) {
            final String mappedName = this.computeAttribute(eObject, attribute.getName());

            if ("isReference".equals(mappedName) && astJson.has(mappedName) && !astJson.get(mappedName).asBoolean()) {
                // The isReference attribute is set to false, which indicates that the element is composite. Since
                // isReference is un-settable and derived from isComposite, we have to set isComposite to true, which
                // will automatically set isReference to false.
                EStructuralFeature isCompositeFeature = eObject.eClass().getEStructuralFeature(SysmlPackage.eINSTANCE.getFeature_IsComposite().getFeatureID());
                if (isCompositeFeature instanceof EAttribute isCompositeAttribute && isCompositeAttribute.getEType().getName().equals("EBoolean")) {
                    eObject.eSet(isCompositeFeature, true);
                }
            }

            if (attribute.isDerived() || attribute.isUnsettable() || !astJson.has(mappedName) || "isNonunique".equals(mappedName)) {
                continue;
            }

            try {
                if (attribute.getEType() instanceof EEnum eEnum) {
                    final String value = AstConstant.asCleanedText(astJson.get(mappedName));
                    final EEnumLiteral enumLiteral = eEnum.getEEnumLiteral(value);
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
                            String textContent = AstConstant.asCleanedText(astJson.get(mappedName));
                            if (attribute.equals(SysmlPackage.eINSTANCE.getComment_Body())) {
                                textContent = textContent.replaceFirst("^/\\*", "").replaceFirst("\\*/", "").trim();
                            }
                            if (!textContent.equals("null")) {
                                eObject.eSet(attribute, textContent);
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

        if ("MembershipReference".equals(type)) {
            type = "Membership";
        } else if ("SysMLFunction".equals(type)) {
            type = "Function";
        } else if ("TypeReference".equals(type)) {
            type = "Type";
        } else if ("FeatureReference".equals(type)) {
            type = "Feature";
        } else if ("ConjugatedPortReference".equals(type)) {
            type = "ConjugatedPortDefinition";
        } else if ("ClassifierReference".equals(type)) {
            type = "Classifier";
        } else if ("ElementReference".equals(type)) {
            type = "Type";
        } else if ("NamespaceReference".equals(type)) {
            type = "Namespace";
        } else if (type.equals("LiteralNumber")) {
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

        var classif = SysmlPackage.eINSTANCE.getEClassifier(type);

        if (classif instanceof EClass eClass) {
            return EcoreUtil.create(eClass);
        }
        return null;
    }

    private String computeAttribute(final EObject eObject, final String attributeName) {
        String computedAttributeName = attributeName;
        if (eObject instanceof LiteralInteger && attributeName.equals("value")) {
            computedAttributeName = "literal";
        }
        return computedAttributeName;
    }
}
