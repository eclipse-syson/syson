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

import java.math.BigDecimal;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * Class in charge of translating a Type field in the AST into a valid {@link EClassifier}.
 *
 * @author Arthur Daussy
 */
public class EClassifierTranslator {

    private static final String LITERAL_CONST = "literal";

    private static final String TYPE_CONST = "$type";

    public EObject createObject(JsonNode astJson) {
        EClassifier classifier = this.toEClassifier(astJson);
        if (classifier instanceof EClass eClass) {
            return eClass.getEPackage().getEFactoryInstance().create(eClass);
        } else {
            return null;
        }
    }

    private EClassifier toEClassifier(JsonNode astJson) {
        String type = astJson.findValue(TYPE_CONST).textValue();

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
            final String literalValue = astJson.get(LITERAL_CONST).asText();
            if (literalValue != null) {
                try {
                    final BigDecimal bd = new BigDecimal(literalValue);
                    bd.intValueExact();
                    type = "LiteralInteger";
                } catch (final ArithmeticException e) {
                    type = "LiteralRational";
                }
            }
            // to remove when SysIDE will release a SysMLv2 2025-04 compliant version
        } else if ("FlowConnectionUsage".equals(type)) {
            type = "FlowUsage";
        } else if ("FlowConnectionDefinition".equals(type)) {
            type = "FlowDefinition";
        } else if ("SuccessionFlowConnectionUsage".equals(type)) {
            type = "SuccessionFlowUsage";
        } else if ("ItemFlow".equals(type)) {
            type = "Flow";
        } else if ("ItemFlowEnd".equals(type)) {
            type = "FlowEnd";
        } else if ("ItemFeature".equals(type)) {
            type = "PayloadFeature";
        }

        return SysmlPackage.eINSTANCE.getEClassifier(type);
    }
}
