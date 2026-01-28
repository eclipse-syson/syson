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
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.sirius.components.core.api.IIdentityService;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionSuccessPayload;
import org.eclipse.syson.services.SemanticRunnableFactory;
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

    private final SemanticRunnableFactory semanticRunnableFactory;

    private final IObjectSearchService objectSearchService;

    public ConnectorAsUsageCheckerBuilder(
            IIdentityService identityService,
            Class<T> expectedType,
            SemanticRunnableFactory semanticRunnableFactory,
            IObjectSearchService objectSearchService) {
        this.identityService = identityService;
        this.expectedType = expectedType;
        this.semanticRunnableFactory = semanticRunnableFactory;
        this.objectSearchService = objectSearchService;
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

    public Runnable build(AtomicReference<String> edgeIdProvider, String editingContextId) {
        return this.semanticRunnableFactory.createRunnable(editingContextId,
                (editingContext, executeEditingContextFunctionInput) -> {
                    Optional<Object> optElement = this.objectSearchService.getObject(editingContext, edgeIdProvider.get());
                    assertThat(optElement).isPresent();
                    Object element = optElement.get();
                    assertThat(element).isInstanceOf(this.expectedType);
                    T castedElement = this.expectedType.cast(element);
                    assertThat(this.identityService.getId(castedElement.getSourceFeature().getFeatureTarget()))
                            .isEqualTo(this.expectedSourceSemanticId);
                    assertThat(this.identityService.getId(castedElement.getTargetFeature().get(0).getFeatureTarget()))
                            .isEqualTo(this.expectedTargetSemanticId);
                    assertThat(this.identityService.getId(castedElement.getOwner())).isEqualTo(this.expectedSemanticContainer);

                    if (this.expectedSourceReference != null) {
                        assertThat(this.identityService.getId(castedElement.getSourceFeature()))
                                .isEqualTo(this.expectedSourceReference);
                    } else if (this.expectedSourceFeatureChain != null) {
                        assertThat(castedElement.getSourceFeature().getOwnedFeatureChaining().stream()
                                .map(FeatureChaining::getChainingFeature)
                                .map(this.identityService::getId)
                                .toList())
                                .isEqualTo(this.expectedSourceFeatureChain);
                    }
                    if (this.expectedTargetReference != null) {
                        assertThat(this.identityService.getId(castedElement.getTargetFeature().get(0)))
                                .isEqualTo(this.expectedTargetReference);
                    } else if (this.expectedTargetFeatureChain != null) {
                        assertThat(castedElement.getTargetFeature().get(0).getOwnedFeatureChaining().stream()
                                .map(FeatureChaining::getChainingFeature)
                                .map(this.identityService::getId)
                                .toList())
                                .isEqualTo(this.expectedTargetFeatureChain);
                    }
                    return new ExecuteEditingContextFunctionSuccessPayload(executeEditingContextFunctionInput.id(), true);
                });
    }

}
