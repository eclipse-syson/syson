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

import org.eclipse.sirius.components.core.api.IIdentityService;
import org.eclipse.syson.application.controller.editingContext.checkers.SemanticCheckerService;
import org.eclipse.syson.sysml.ConnectorAsUsage;
import org.eclipse.syson.sysml.FeatureChaining;

/**
 * Builder for a checker that verify semantic of a {@link org.eclipse.syson.sysml.ConnectorAsUsage}.
 *
 * @param <T> Type of {@link ConnectorAsUsage} to check.
 * @author Arthur Daussy
 */
public class ConnectorAsUsageCheckerBuilder<T extends  ConnectorAsUsage> {

    private String expectedSourceSemanticId;

    private String expectedTargetSemanticId;

    private String expectedSemanticContainer;

    private List<String> expectedSourceFeatureChain;

    private String expectedSourceReference;

    private List<String> expectedTargetFeatureChain;

    private String expectedTargetReference;

    private final IIdentityService identityService;

    private final Class<T> expectedType;

    private final SemanticCheckerService semanticCheckerService;

    public ConnectorAsUsageCheckerBuilder(
            IIdentityService identityService,
            Class<T> expectedType,
            SemanticCheckerService semanticCheckerService) {
        this.identityService = identityService;
        this.expectedType = expectedType;
        this.semanticCheckerService = semanticCheckerService;
    }

    public ConnectorAsUsageCheckerBuilder<T> setExpectedSemanticContainer(String anExpectedSemanticContainer) {
        this.expectedSemanticContainer = anExpectedSemanticContainer;
        return this;
    }

    public ConnectorAsUsageCheckerBuilder<T> setExpectedSourceFeatureChain(List<String> anExpectedSourceFeatureChain) {
        this.expectedSourceFeatureChain = anExpectedSourceFeatureChain;
        return this;
    }

    public ConnectorAsUsageCheckerBuilder<T> setExpectedSourceReference(String anExpectedSourceReference) {
        this.expectedSourceReference = anExpectedSourceReference;
        return this;
    }

    public ConnectorAsUsageCheckerBuilder<T> setExpectedSourceSemanticId(String anExpectedSourceSemanticId) {
        this.expectedSourceSemanticId = anExpectedSourceSemanticId;
        return this;
    }

    public ConnectorAsUsageCheckerBuilder<T> setExpectedTargetSemanticId(String anExpectedTargetSemanticId) {
        this.expectedTargetSemanticId = anExpectedTargetSemanticId;
        return this;
    }

    public ConnectorAsUsageCheckerBuilder<T> setExpectedTargetFeatureChain(List<String> anExpectedTargetFeatureChain) {
        this.expectedTargetFeatureChain = anExpectedTargetFeatureChain;
        return this;
    }

    public ConnectorAsUsageCheckerBuilder<T> setExpectedTargetReference(String anExpectedTargetReference) {
        this.expectedTargetReference = anExpectedTargetReference;
        return this;
    }

    public Runnable build(AtomicReference<String> edgeIdProvider) {
        return this.semanticCheckerService.checkElement(this.expectedType, edgeIdProvider::get,
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
                                .map(this.identityService::getId)
                                .toList())
                                .isEqualTo(this.expectedSourceFeatureChain);
                    }
                    if (this.expectedTargetReference != null) {
                        assertThat(this.identityService.getId(connectorAsUsage.getTargetFeature().get(0)))
                                .isEqualTo(this.expectedTargetReference);
                    } else if (this.expectedTargetFeatureChain != null) {
                        assertThat(connectorAsUsage.getTargetFeature().get(0).getOwnedFeatureChaining().stream()
                                .map(FeatureChaining::getChainingFeature)
                                .map(this.identityService::getId)
                                .toList())
                                .isEqualTo(this.expectedTargetFeatureChain);
                    }
                });
    }

}
