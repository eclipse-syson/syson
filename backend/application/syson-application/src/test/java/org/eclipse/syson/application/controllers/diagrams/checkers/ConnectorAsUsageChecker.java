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
package org.eclipse.syson.application.controllers.diagrams.checkers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.core.api.IIdentityService;
import org.eclipse.syson.application.controller.editingContext.checkers.SemanticCheckerService;
import org.eclipse.syson.sysml.ConnectorAsUsage;
import org.eclipse.syson.sysml.FeatureChaining;

import reactor.test.StepVerifier.Step;

/**
 * Checker that verify semantic of a {@link org.eclipse.syson.sysml.ConnectorAsUsage}.
 *
 * @param <T> Type of {@link ConnectorAsUsage} to check.
 * @author Arthur Daussy
 */
public class ConnectorAsUsageChecker<T extends  ConnectorAsUsage> {

    private String expectedSourceSemanticId;

    private String expectedTargetSemanticId;

    private String expectedSemanticContainer;

    private List<String> expectedSourceFeatureChain;

    private String expectedSourceReference;

    private List<String> expectedTargetFeatureChain;

    private String expectedTargetReference;

    private final IIdentityService identityService;

    private final SemanticCheckerService semanticCheckerService;

    private final Class<T> expectedType;

    public ConnectorAsUsageChecker(
            IIdentityService identityService,
            SemanticCheckerService semanticCheckerService,
            Class<T> expectedType) {
        this.identityService = identityService;
        this.semanticCheckerService = semanticCheckerService;
        this.expectedType = expectedType;
    }

    public ConnectorAsUsageChecker<T> setExpectedSemanticContainer(String anExpectedSemanticContainer) {
        this.expectedSemanticContainer = anExpectedSemanticContainer;
        return this;
    }

    public ConnectorAsUsageChecker<T> setExpectedSourceFeatureChain(List<String> anExpectedSourceFeatureChain) {
        this.expectedSourceFeatureChain = anExpectedSourceFeatureChain;
        return this;
    }

    public ConnectorAsUsageChecker<T> setExpectedSourceReference(String anExpectedSourceReference) {
        this.expectedSourceReference = anExpectedSourceReference;
        return this;
    }

    public ConnectorAsUsageChecker<T> setExpectedSourceSemanticId(String anExpectedSourceSemanticId) {
        this.expectedSourceSemanticId = anExpectedSourceSemanticId;
        return this;
    }

    public ConnectorAsUsageChecker<T> setExpectedTargetSemanticId(String anExpectedTargetSemanticId) {
        this.expectedTargetSemanticId = anExpectedTargetSemanticId;
        return this;
    }

    public ConnectorAsUsageChecker<T> setExpectedTargetFeatureChain(List<String> anExpectedTargetFeatureChain) {
        this.expectedTargetFeatureChain = anExpectedTargetFeatureChain;
        return this;
    }

    public ConnectorAsUsageChecker<T> setExpectedTargetReference(String anExpectedTargetReference) {
        this.expectedTargetReference = anExpectedTargetReference;
        return this;
    }

    public <T extends ConnectorAsUsage> void run(Step<DiagramRefreshedEventPayload> verifier, AtomicReference<String> edgeIdProvider) {

        this.semanticCheckerService.checkElement(verifier, this.expectedType, edgeIdProvider::get,
                connectorAsUsage -> {
                    assertThat(this.identityService.getId(connectorAsUsage.getSourceFeature().getFeatureTarget()))
                            .isEqualTo(this.expectedSourceSemanticId);
                    assertThat(this.identityService.getId(connectorAsUsage.getTargetFeature().get(0).getFeatureTarget()))
                            .isEqualTo(this.expectedTargetSemanticId);
                    assertThat(this.identityService.getId(connectorAsUsage.getOwner())).isEqualTo(this.expectedSemanticContainer);

                    if (this.expectedSourceReference != null) {
                        assertThat(this.identityService.getId(connectorAsUsage.getSourceFeature()))
                                .isEqualTo(this.expectedSourceReference);
                    } else if (this.expectedSourceFeatureChain != null) {
                        assertThat(connectorAsUsage.getSourceFeature().getOwnedFeatureChaining().stream()
                                .map(FeatureChaining::getChainingFeature)
                                .map(fc -> this.identityService.getId(fc))
                                .toList())
                                        .isEqualTo(this.expectedSourceFeatureChain);
                    }
                    if (this.expectedTargetReference != null) {
                        assertThat(this.identityService.getId(connectorAsUsage.getTargetFeature().get(0)))
                                .isEqualTo(this.expectedTargetReference);
                    } else if (this.expectedTargetFeatureChain != null) {
                        assertThat(connectorAsUsage.getTargetFeature().get(0).getOwnedFeatureChaining().stream()
                                .map(FeatureChaining::getChainingFeature)
                                .map(fc -> this.identityService.getId(fc))
                                .toList())
                                        .isEqualTo(this.expectedTargetFeatureChain);
                    }
                });
    }

}
