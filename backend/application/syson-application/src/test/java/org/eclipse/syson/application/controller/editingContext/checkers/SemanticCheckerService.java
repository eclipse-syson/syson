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
package org.eclipse.syson.application.controller.editingContext.checkers;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionSuccessPayload;
import org.eclipse.syson.application.data.SysMLv2Identifiers;
import org.eclipse.syson.services.SemanticRunnableFactory;

import reactor.test.StepVerifier.Step;

/**
 * Helper to create preset {@link ISemanticChecker} and run semantic checks on an editing context.
 *
 * @author gdaniel
 */
public class SemanticCheckerService {

    private final SemanticRunnableFactory semanticRunnableFactory;

    private final IObjectSearchService objectSearchService;

    public SemanticCheckerService(SemanticRunnableFactory semanticRunnableFactory, IObjectSearchService objectSearchService) {
        this.semanticRunnableFactory = semanticRunnableFactory;
        this.objectSearchService = objectSearchService;
    }

    public ISemanticChecker getElementInParentSemanticChecker(String parentLabel, EReference containmentReference, EClass childEClass) {
        return new CheckElementInParent(this.objectSearchService, SysMLv2Identifiers.GENERAL_VIEW_WITH_TOP_NODES_DIAGRAM_OBJECT)
                .withParentLabel(parentLabel)
                .withContainmentReference(containmentReference)
                .hasType(childEClass);
    }

    public void checkEditingContext(ISemanticChecker semanticChecker, Step<DiagramRefreshedEventPayload> verifier) {
        Runnable runnableChecker = this.semanticRunnableFactory.createRunnable(SysMLv2Identifiers.GENERAL_VIEW_WITH_TOP_NODES_EDITING_CONTEXT_ID,
                (editingContext, executeEditingContextFunctionInput) -> {
                    semanticChecker.check(editingContext);
                    return new ExecuteEditingContextFunctionSuccessPayload(executeEditingContextFunctionInput.id(), true);
                });

        verifier.then(runnableChecker);
    }

}
