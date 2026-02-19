/*******************************************************************************
 * Copyright (c) 2025, 2026 Obeo.
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
package org.eclipse.syson.sysml.validation;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.sirius.components.emf.ResourceMetadataAdapter;
import org.eclipse.sirius.components.interpreter.AQLInterpreter;
import org.eclipse.sirius.components.representations.VariableManager;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.validation.rules.api.IValidationRule;
import org.eclipse.syson.sysml.validation.rules.api.IValidationRulesProvider;

/**
 * The validator for SysMLv2 elements.
 *
 * @author arichard
 */
public class SysONSysMLValidator implements EValidator {

    private final IValidationRulesProvider validationRulesProvider;

    private final AQLInterpreter aqlInterpreter;

    private final UtilService utilService;

    public SysONSysMLValidator(final IValidationRulesProvider validationRulesProvider) {
        this.validationRulesProvider = Objects.requireNonNull(validationRulesProvider);
        this.aqlInterpreter = new AQLInterpreter(validationRulesProvider.getAQLServiceClasses(),
                validationRulesProvider.getAQLServiceInstances(),
                Stream.concat(Stream.of(SysmlPackage.eINSTANCE), validationRulesProvider.getEPackages().stream())
                        .distinct().toList());
        this.utilService = new UtilService();
    }

    @Override
    public boolean validate(EObject eObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return this.validate(eObject.eClass(), eObject, diagnostics, context);
    }

    @Override
    public boolean validate(EDataType eDataType, Object value, DiagnosticChain diagnostics, Map<Object, Object> context) {
        // There are no custom EDatatypes in the SysML metamodel so this should never get called.
        return true;
    }

    @Override
    public boolean validate(EClass eClass, EObject eObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean isValid = true;
        if (eObject instanceof Element element && !this.utilService.isRootNamespace(element)) {
            isValid = this.checkAllConstraints(element, eClass, diagnostics);
        }
        return isValid;
    }

    /**
     * Check all constraints related to the given {@link Element}.
     *
     * @param element
     *            the {@link Element} on which the constraints have to be checked.
     * @param eClass
     *            the {@link EClass} of the {@link Element}.
     * @param diagnostics
     *            the {@link DiagnosticChain} to fill with new diagnostic if constraints are not respected.
     * @return <code>true</code> if all constraints are respected, <code>false</code> otherwise.
     */
    private boolean checkAllConstraints(Element element, EClass eClass, DiagnosticChain diagnostics) {
        boolean isValid = true;

        for (final IValidationRule validationRule : this.validationRulesProvider.getValidationRules(eClass)) {
            boolean validConstraint = this.executeConstraint(element, validationRule.expression());
            if (!validConstraint) {
                this.addDiagnostic(diagnostics, element, validationRule);
                isValid = false;
            }
        }

        return isValid;
    }

    /**
     * Execute the AQL expression.
     *
     * @param element
     *            the {@link Element} on which the constraints have to be checked.
     * @param aqlExpression
     *            the expression to execute.
     * @return <code>true</code> if the AQL expression returns true, <code>false</code> otherwise.
     */
    private boolean executeConstraint(Element element, String aqlExpression) {
        final Map<String, Object> variables = Map.ofEntries(Map.entry(VariableManager.SELF, element));
        return this.aqlInterpreter.evaluateExpression(variables, aqlExpression).asBoolean().orElse(Boolean.FALSE).booleanValue();
    }

    /**
     * Add a {@link BasicDiagnostic} to the given diagnostics for the given element.
     *
     * @param diagnostics
     *            the {@link DiagnosticChain} on which the diagnostic has to be added.
     * @param element
     *            the {@link Element} on which the diagnostic has to be added.
     * @param validationRule
     *            the {@link IValidationRule} associated to the diagnostic.
     * @return
     */
    private void addDiagnostic(DiagnosticChain diagnostics, Element element, IValidationRule validationRule) {
        // For now the Validation view in SysON is rather limited so our diagnostic message needs to contain as much
        // information as possible.

        // Since validation usually runs on the whole editing context, we need to make clear which document is
        // concerned.
        final String elementDocumentLabel = element.eResource().eAdapters().stream()
                .filter(ResourceMetadataAdapter.class::isInstance)
                .map(ResourceMetadataAdapter.class::cast)
                .findFirst()
                .map(ResourceMetadataAdapter::getName)
                .orElseGet(() -> element.eResource().getURI().toString());

        // Inside a document, the SysML qualified name is usually enough to uniquely identify an element.
        final String elementQualifiedName = element.getQualifiedName();
        final String elementLabel;
        if (elementQualifiedName != null) {
            elementLabel = elementQualifiedName;
        } else {
            // Fallback in case the qualified name computation leads to null.
            elementLabel = "<ID: %s>".formatted(EcoreUtil.getID(element));
        }

        final String diagnosticMessage = "#%s·····[%s/%s]‎·····Constraint on type '%s:%s' is not respected: %s".formatted(
                validationRule.ruleName(),
                elementDocumentLabel,
                elementLabel,
                validationRule.eClass().getEPackage().getNsPrefix(),
                validationRule.eClass().getName(),
                validationRule.message());

        final BasicDiagnostic basicDiagnostic = new BasicDiagnostic(
                validationRule.severity(),
                validationRule.ruleName(),
                0,
                diagnosticMessage,
                new Object[] {
                    element,
                });
        diagnostics.add(basicDiagnostic);
    }
}
