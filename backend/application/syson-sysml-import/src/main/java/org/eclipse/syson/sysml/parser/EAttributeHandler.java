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

import java.text.MessageFormat;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.syson.sysml.OccurrenceDefinition;
import org.eclipse.syson.sysml.OccurrenceUsage;
import org.eclipse.syson.sysml.parser.translation.EAttributeTranslator;
import org.eclipse.syson.sysml.utils.MessageReporter;

/**
 * Class that handles EAttribute.
 *
 * @author gescande.
 */
public class EAttributeHandler {

    private final MessageReporter messageReporter;

    public EAttributeHandler(MessageReporter messageReporter) {
        this.messageReporter = messageReporter;
    }

    public boolean isAttribute(final EObject eObject, String attrName, final JsonNode value) {
        return value.isTextual() || value.isNumber() || value.isBoolean();
    }

    public void setAttribute(final EObject eObject, String attrName, final JsonNode value) {
        EClass ownerEClass = eObject.eClass();

        if (!this.isIgnoredAttribute(attrName, eObject)) {
            EAttributeTranslator translator = new EAttributeTranslator();
            try {
                translator.computeAttributeAndValueToSet(ownerEClass, attrName, value);
                EAttribute attr = translator.getAttribute();
                if (attr != null && attr.isChangeable() && !attr.isDerived()) {
                    Object translatedValue = translator.getValue();
                    if (translatedValue != null) {
                        this.setValue(eObject, attr, translatedValue);
                    } else {
                        this.messageReporter
                                .warning(MessageFormat.format("Unable to compute a value in order to set ''{0}'' on {1} with value {2}", attr.getName(), eObject.eClass().getName(), value));
                    }
                } else {
                    this.messageReporter.warning(MessageFormat.format("Unable to find a changeable attribute to set ''{0}'' on {1} with value {2}", attrName, eObject.eClass().getName(), value));
                }
            } catch (IllegalArgumentException e) {
                this.messageReporter.error(MessageFormat.format("Error while setting attribute {0} with value {1} : {2}", attrName, value, e.getMessage()));
            }
        }
    }

    private boolean isIgnoredAttribute(String attName, EObject eObject) {
        if (attName == null || attName.isBlank()) {
            return true;
        }
        return this.isMetaAttribute(attName)
                // The AST set this attribute on all usages but in our metamodel it only exists on OccurrenceUsage and
                // OccurrenceDefinition
                || this.ignoreIsIndividualOnIncorrectType(attName, eObject)
                // Ignored isAlias attribute since we use the declared name of membership
                || this.isAlias(attName);
    }

    private boolean ignoreIsIndividualOnIncorrectType(String attName, EObject eObject) {
        return "isIndividual".equals(attName) && !(eObject instanceof OccurrenceUsage || eObject instanceof OccurrenceDefinition);
    }

    private boolean isAlias(String astFeatureName) {
        return "isAlias".equals(astFeatureName);
    }

    private boolean isMetaAttribute(String attName) {
        return attName.startsWith("$");
    }

    private void setValue(final EObject eObject, EAttribute attr, Object value) {
        if (attr.isMany()) {
            ((List<Object>) eObject.eGet(attr)).add(value);
        } else {
            eObject.eSet(attr, value);
        }
    }

}
