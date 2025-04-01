/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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
package org.eclipse.syson.sysml.parser.translation;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.TransitionFeatureKind;

/**
 * Transforms a feature name and value in the AST in an EAttribute with a value that can be set in SysON metamodel.
 *
 * @author Arthur Daussy
 */
public class EAttributeTranslator {

    private static final String QUOTE_CONST = "\'";

    private EAttribute matchingAttribute;

    private Object value;

    /**
     * Returns the attribute and the value to be set in that attribute given the AST information.
     *
     * @param ownerType
     *            the owner of the feature
     * @param astFeatureName
     *            the name of the feature in the AST
     * @param astValue
     *            the value of that feature in the AST
     */
    public void computeAttributeAndValueToSet(EClass ownerType, String astFeatureName, JsonNode astValue) {

        // Use to force the value of an attribute
        Object forcedValue = null;

        if (SysmlPackage.eINSTANCE.getMembership().isSuperTypeOf(ownerType) && "declaredName".equals(astFeatureName)) {
            // This handle alias
            this.matchingAttribute = SysmlPackage.eINSTANCE.getMembership_MemberName();
        } else if (SysmlPackage.eINSTANCE.getLiteralExpression().isSuperTypeOf(ownerType) && astFeatureName.equals("literal")) {
            this.matchingAttribute = this.getAttribute(ownerType, "value").orElse(null);
        } else if (astFeatureName.equals("isReference")) {
            forcedValue = this.handleIsReferenceAttribute(ownerType, astValue);
        } else if (SysmlPackage.eINSTANCE.getComment().isSuperTypeOf(ownerType) && "body".equals(astFeatureName)) {
            forcedValue = this.handleCommentBody(ownerType, astFeatureName, astValue);
        } else if (SysmlPackage.eINSTANCE.getFeature().isSuperTypeOf(ownerType) && "isNonunique".equals(astFeatureName)) {
            forcedValue = this.handleIsUniqueAttribute(ownerType, astValue);
        } else if (SysmlPackage.eINSTANCE.getImport().isSuperTypeOf(ownerType) && "importsAll".equals(astFeatureName)) {
            this.matchingAttribute = this.getAttribute(ownerType, "isImportAll").orElse(null);
        } else if (this.isSpecialBooleanFeature(astFeatureName)) {
            this.matchingAttribute = this.getAttribute(ownerType, astFeatureName).orElse(null);
            forcedValue = this.handleBooleansAsString(astValue, astFeatureName);
        } else {
            this.matchingAttribute = this.getAttribute(ownerType, astFeatureName).orElse(null);
        }

        if (this.matchingAttribute != null) {

            if (forcedValue != null) {
                this.value = forcedValue;
            } else {
                this.value = this.getValue(this.matchingAttribute, astValue);
            }

        }
    }

    private Object handleIsUniqueAttribute(EClass ownerType, JsonNode astValue) {
        Object forcedValue;
        this.matchingAttribute = this.getAttribute(ownerType, "isUnique").orElse(null);
        forcedValue = !astValue.asBoolean();
        return forcedValue;
    }

    private Object handleIsReferenceAttribute(EClass ownerType, JsonNode astValue) {
        Object forcedValue;
        // The isReference attribute is set to false, which indicates that the element is composite. Since
        // isReference is un-settable and derived from isComposite, we have to set isComposite to true, which
        // will automatically set isReference to false.
        this.matchingAttribute = this.getAttribute(ownerType, "isComposite").orElse(null);
        forcedValue = !astValue.asBoolean();
        return forcedValue;
    }

    private Object handleCommentBody(EClass ownerType, String astFeatureName, JsonNode astValue) {
        Object forcedValue;
        this.matchingAttribute = this.getAttribute(ownerType, astFeatureName).orElse(null);
        // This does not properly handle multiline comment
        forcedValue = this.asCleanedText(astValue).replaceFirst("^/\\*", "").replaceFirst("\\*/", "").trim();
        return forcedValue;
    }

    public Object getValue() {
        return this.value;
    }

    public EAttribute getAttribute() {
        return this.matchingAttribute;
    }

    private Object getValue(EAttribute attr, JsonNode astValue) {
        final Object result;

        if (astValue.isTextual() || astValue.isNumber()) {
            String cleanedValue = this.asCleanedText(astValue);
            EDataType eAttributeType = attr.getEAttributeType();
            if (this.isSpecialEnum(eAttributeType)) {
                result = this.convertSpecialEnumLiteral(eAttributeType, cleanedValue);
            } else {
                result = eAttributeType.getEPackage().getEFactoryInstance().createFromString(eAttributeType, cleanedValue);
            }
        } else if (astValue.isBoolean()) {
            result = astValue.asBoolean();
        } else {
            result = null;
        }
        return result;

    }

    private boolean isSpecialEnum(EDataType dataType) {
        return dataType == SysmlPackage.eINSTANCE.getTransitionFeatureKind();
    }

    private Object convertSpecialEnumLiteral(EDataType dataType, String aValue) {
        final Object customValue;
        if (dataType == SysmlPackage.eINSTANCE.getTransitionFeatureKind()) {
            customValue = this.convertTransitionFeatureKind(aValue);
        } else {
            customValue = null;
        }
        return customValue;
    }

    private TransitionFeatureKind convertTransitionFeatureKind(String aValue) {
        if (aValue != null) {
            return switch (aValue) {
                case "if" -> TransitionFeatureKind.GUARD;
                case "accept" -> TransitionFeatureKind.TRIGGER;
                case "do" -> TransitionFeatureKind.EFFECT;
                default -> {
                    // Fallback on default case
                    // The other SysIDE inputs are not identified yet
                    yield (TransitionFeatureKind) SysmlPackage.eINSTANCE.getEFactoryInstance().createFromString(SysmlPackage.eINSTANCE.getTransitionFeatureKind(), aValue);
                }
            };
        }
        return null;
    }

    /**
     * Some boolean feature in the AST are not handled a simple boolean but use custom syntax.
     */
    private boolean isSpecialBooleanFeature(String astFeatureName) {
        return switch (astFeatureName) {
            case "isAbstract" -> true;
            case "isDerived" -> true;
            case "isEnd" -> true;
            case "isReadOnly" -> true;
            default -> false;
        };
    }

    private boolean handleBooleansAsString(JsonNode astJson, String mappedName) {
        return this.handleBooleanAsString("isAbstract", mappedName, "abstract", astJson)
                || this.handleBooleanAsString("isDerived", mappedName, "derived", astJson)
                || this.handleBooleanAsString("isEnd", mappedName, "end", astJson)
                || this.handleBooleanAsString("isReadOnly", mappedName, "readonly", astJson);
    }

    private boolean handleBooleanAsString(String attributeName, String mappedName, String stringValueIfTrue, JsonNode astValue) {
        return attributeName.equals(mappedName) && stringValueIfTrue.equals(astValue.asText());
    }

    private Optional<EAttribute> getAttribute(EClass ownerType, String name) {
        EStructuralFeature feature = ownerType.getEStructuralFeature(name);
        if (feature instanceof EAttribute attr) {
            return Optional.of(attr);
        }
        return Optional.empty();
    }


    private String asCleanedText(final JsonNode node) {
        String result = null;

        if (node.isTextual()) {
            result = node.asText().replace(QUOTE_CONST, "");
        }
        if (node.isNumber()) {
            result = node.asText();
        }

        return result;
    }
}
