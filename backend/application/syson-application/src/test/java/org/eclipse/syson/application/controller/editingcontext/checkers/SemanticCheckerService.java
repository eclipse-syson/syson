/*******************************************************************************
 * Copyright (c) 2024, 2026 Obeo.
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
package org.eclipse.syson.application.controller.editingcontext.checkers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionSuccessPayload;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.sysml.Element;

import reactor.test.StepVerifier.Step;

/**
 * Helper to create preset {@link ISemanticChecker} and run semantic checks on an editing context.
 *
 * @author gdaniel
 */
public class SemanticCheckerService {

    private final SemanticRunnableFactory semanticRunnableFactory;

    private final IObjectSearchService objectSearchService;

    private final String editingContextId;

    private final String rootElementId;

    public SemanticCheckerService(SemanticRunnableFactory semanticRunnableFactory, IObjectSearchService objectSearchService, String editingContextId, String rootElementId) {
        this.semanticRunnableFactory = semanticRunnableFactory;
        this.objectSearchService = objectSearchService;
        this.editingContextId = editingContextId;
        this.rootElementId = rootElementId;
    }

    public ISemanticChecker getElementInParentSemanticChecker(String parentLabel, EReference containmentReference, EClass childEClass) {
        return this.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass, null);
    }

    /**
     * Creates a semantic checker that should be checked in an editing context.
     *
     * @param parentLabel
     *                      The label of the parent element
     * @param containmentReference
     *                      The {@link EReference} that contains the element to check in the parent
     * @param childEClass
     *                      The expected child EClass
     * @param additionalCheckOnElement
     *                      A consumer to allow additional checking of the element.
     *                      The element can either be an object if the EReference is single valued, or a list of object if the referenced is multivalued.
     * @return the created semantic checker
     */
    public ISemanticChecker getElementInParentSemanticChecker(String parentLabel, EReference containmentReference, EClass childEClass, Consumer<Object> additionalCheckOnElement) {
        return new CheckElementInParent(this.objectSearchService, this.rootElementId)
                .withParentLabel(parentLabel)
                .withContainmentReference(containmentReference)
                .hasType(childEClass)
                .additionalCheckOnElement(additionalCheckOnElement);
    }

    /**
     * Do some checks on the element identified by the given id.
     *
     * @param <T>
     *            the type element under test
     * @param verifier
     *            the verifier
     * @param type
     *            the type of the element under test
     * @param idSupplier
     *            a supplier that returns the id of the semantic object
     * @param semanticChecker
     *            the checks that needs to be run
     * @deprecated this function will be removed when all the tests will be migrated to follow the same format as Sirius Web.
     * Please, use {@link SemanticCheckerService#checkElement(Class type, Supplier idSupplier, Consumer semanticChecker)}instead.
     */
    @Deprecated
    public <T extends Element> void checkElement(Step<?> verifier, Class<T> type, Supplier<String> idSupplier, Consumer<T> semanticChecker) {
        ISemanticChecker checker = editingContext -> {
            Optional<Object> optElement = this.objectSearchService.getObject(editingContext, idSupplier.get());
            assertThat(optElement).isPresent();
            Object element = optElement.get();
            assertThat(element).isInstanceOf(type);
            T castedElement = type.cast(element);
            semanticChecker.accept(castedElement);
        };
        this.checkEditingContext(checker, verifier);
    }

    /**
     * Provide a runnable that do some checks on the element identified by the given id.
     *
     * @param <T>
     *            the type element under test
     * @param type
     *            the type of the element under test
     * @param idSupplier
     *            a supplier that returns the id of the semantic object
     * @param semanticChecker
     *            the checks that needs to be run
     */
    public <T extends Element> Runnable checkElement(Class<T> type, Supplier<String> idSupplier, Consumer<T> semanticChecker) {
        ISemanticChecker checker = editingContext -> {
            Optional<Object> optElement = this.objectSearchService.getObject(editingContext, idSupplier.get());
            assertThat(optElement).isPresent();
            Object element = optElement.get();
            assertThat(element).isInstanceOf(type);
            T castedElement = type.cast(element);
            semanticChecker.accept(castedElement);
        };
        return this.checkEditingContext(checker);
    }

    /**
     * Runs semantic checks on the editing context and chains them to the provided verifier.
     *
     * @param semanticChecker
     *            the checker containing the semantic assertions to run
     * @param verifier
     *            the {@link Step} verifier to chain the check execution to
     * @deprecated this function will be removed when all the tests will be migrated to follow the same format as Sirius Web.
     * Please, use {@link SemanticCheckerService#checkEditingContext(ISemanticChecker semanticChecker)} instead.
     */
    @Deprecated
    public void checkEditingContext(ISemanticChecker semanticChecker, Step<?> verifier) {
        Runnable runnableChecker = this.semanticRunnableFactory.createRunnable(this.editingContextId,
                (editingContext, executeEditingContextFunctionInput) -> {
                    semanticChecker.check(editingContext);
                    return new ExecuteEditingContextFunctionSuccessPayload(executeEditingContextFunctionInput.id(), true);
                });

        verifier.then(runnableChecker);
    }

    /**
     * Provide a runnable that run semantic checks on the editing context.
     *
     * @param semanticChecker
     *            the checker containing the semantic assertions to run
     */
    public Runnable checkEditingContext(ISemanticChecker semanticChecker) {
        return this.semanticRunnableFactory.createRunnable(this.editingContextId,
                (editingContext, executeEditingContextFunctionInput) -> {
                    semanticChecker.check(editingContext);
                    return new ExecuteEditingContextFunctionSuccessPayload(executeEditingContextFunctionInput.id(), true);
                });
    }

}
