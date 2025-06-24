/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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
package org.eclipse.syson.application.controllers.diagrams.checkers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.core.api.IIdentityService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.syson.application.controller.editingContext.checkers.SemanticCheckerService;
import org.eclipse.syson.sysml.BindingConnectorAsUsage;
import org.eclipse.syson.sysml.FeatureChaining;

import reactor.test.StepVerifier.Step;

/**
 * Checker that verify semantic of a {@link BindingConnectorAsUsage}.
 *
 * @author Arthur Daussy
 */
public class BindingConnectorAsUsageChecker {

    private String expectedSourceSemanticId;

    private String expectedTargetSemanticId;

    private String expectedSemanticContainer;

    private List<String> expectedSourceFeatureChain;

    private String expectedSourceReference;

    private List<String> expectedTargetFeatureChain;

    private String expectedTargetReference;

    private final IIdentityService identityService;

    private final SemanticCheckerService semanticCheckerService;

    public BindingConnectorAsUsageChecker(
            IIdentityService identityService,
            SemanticCheckerService semanticCheckerService) {
        this.identityService = identityService;
        this.semanticCheckerService = semanticCheckerService;
    }

    public BindingConnectorAsUsageChecker setExpectedSemanticContainer(String anExpectedSemanticContainer) {
        this.expectedSemanticContainer = anExpectedSemanticContainer;
        return this;
    }

    public BindingConnectorAsUsageChecker setExpectedSourceFeatureChain(List<String> anExpectedSourceFeatureChain) {
        this.expectedSourceFeatureChain = anExpectedSourceFeatureChain;
        return this;
    }

    public BindingConnectorAsUsageChecker setExpectedSourceReference(String anExpectedSourceReference) {
        this.expectedSourceReference = anExpectedSourceReference;
        return this;
    }

    public BindingConnectorAsUsageChecker setExpectedSourceSemanticId(String anExpectedSourceSemanticId) {
        this.expectedSourceSemanticId = anExpectedSourceSemanticId;
        return this;
    }

    public BindingConnectorAsUsageChecker setExpectedTargetSemanticId(String anExpectedTargetSemanticId) {
        this.expectedTargetSemanticId = anExpectedTargetSemanticId;
        return this;
    }

    public BindingConnectorAsUsageChecker setExpectedTargetFeatureChain(List<String> anExpectedTargetFeatureChain) {
        this.expectedTargetFeatureChain = anExpectedTargetFeatureChain;
        return this;
    }

    public BindingConnectorAsUsageChecker setExpectedTargetReference(String anExpectedTargetReference) {
        this.expectedTargetReference = anExpectedTargetReference;
        return this;
    }

    public void run(Step<DiagramRefreshedEventPayload> verifier, AtomicReference<Diagram> diagram, AtomicReference<String> bindingIdProvider) {

        this.semanticCheckerService.checkElement(verifier, BindingConnectorAsUsage.class, () -> bindingIdProvider.get(),
                binding -> {
                    assertThat(this.identityService.getId(binding.getSourceFeature().getFeatureTarget()))
                            .isEqualTo(this.expectedSourceSemanticId);
                    assertThat(this.identityService.getId(binding.getTargetFeature().get(0).getFeatureTarget()))
                            .isEqualTo(this.expectedTargetSemanticId);
                    assertThat(this.identityService.getId(binding.getOwningType())).isEqualTo(this.expectedSemanticContainer);

                    if (this.expectedSourceReference != null) {
                        assertThat(this.identityService.getId(binding.getSourceFeature()))
                                .isEqualTo(this.expectedSourceReference);
                    } else if (this.expectedSourceFeatureChain != null) {
                        assertThat(binding.getSourceFeature().getOwnedFeatureChaining().stream()
                                .map(FeatureChaining::getChainingFeature)
                                .map(fc -> this.identityService.getId(fc))
                                .toList())
                                        .isEqualTo(this.expectedSourceFeatureChain);
                    }

                    if (this.expectedTargetReference != null) {
                        assertThat(this.identityService.getId(binding.getTargetFeature().get(0)))
                                .isEqualTo(this.expectedTargetReference);
                    } else if (this.expectedTargetFeatureChain != null) {
                        assertThat(binding.getTargetFeature().get(0).getOwnedFeatureChaining().stream()
                                .map(FeatureChaining::getChainingFeature)
                                .map(fc -> this.identityService.getId(fc))
                                .toList())
                                        .isEqualTo(this.expectedTargetFeatureChain);
                    }
                });
    }

}
