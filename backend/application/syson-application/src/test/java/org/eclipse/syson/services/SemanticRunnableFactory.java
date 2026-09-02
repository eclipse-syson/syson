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
package org.eclipse.syson.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import java.util.function.BiFunction;

import org.eclipse.sirius.components.collaborative.api.ChangeDescription;
import org.eclipse.sirius.components.collaborative.api.ChangeKind;
import org.eclipse.sirius.components.collaborative.api.IEditingContextEventHandler;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IInput;
import org.eclipse.sirius.components.core.api.IPayload;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionInput;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionSuccessPayload;
import org.eclipse.sirius.components.graphql.tests.api.IExecuteEditingContextFunctionRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.StepVerifier.Step;

/**
 * Provides methods to run semantic functions as part of a subscription verification.
 * <p>
 * This class is typically used in conjunction with a {@link StepVerifier} to produce a {@link Runnable} that can be
 * consumed by {@link Step#then(Runnable)}. The code executed inside the {@link Runnable} has access to the editing
 * context, which can be used to update the semantic model, or perform verifications.
 * </p>
 *
 * @author gdaniel
 */
@Service
public class SemanticRunnableFactory {

    @Autowired
    private IExecuteEditingContextFunctionRunner executeEditingContextFunctionRunner;

    /**
     * Return a {@link Runnable} giving the {@link IEditingContext} to {@code function}.
     * <p>
     * The {@link IEditingContextEventHandler} executing {@code function} will emit a {@link ChangeDescription} with {@link ChangeKind#NOTHING} in case of a success,
     * thus, it should not be used to perform change to the editing context.
     * </p>
     *
     * @param editingContextId
     *              The editing context ID identifying the {@link IEditingContext} {@code function} will have access to
     * @param function
     *              The function executed by the {@link Runnable}
     * @return a {@link Runnable} having access to the editing context
     */
    public Runnable createQueryRunnable(String editingContextId, BiFunction<IEditingContext, IInput, IPayload> function) {
        return () -> {
            var input = new ExecuteEditingContextFunctionInput(UUID.randomUUID(), editingContextId, function);

            Mono<IPayload> result = this.executeEditingContextFunctionRunner.execute(input);
            var payload = result.block();
            assertThat(payload).isInstanceOf(ExecuteEditingContextFunctionSuccessPayload.class);
        };
    }

    /**
     * Return a {@link Runnable} giving the {@link IEditingContext} to {@code function}.
     * <p>
     * The {@link IEditingContextEventHandler} executing {@code function} will emit a {@link ChangeDescription} with {@link ChangeKind#NOTHING} in case of a success,
     * thus, it can be used to perform change to the editing context.
     * </p>
     *
     * @param editingContextId
     *              The editing context ID identifying the {@link IEditingContext} {@code function} will have access to
     * @param function
     *              The function executed by the {@link Runnable}
     * @return a {@link Runnable} having access to the editing context
     */
    public Runnable createMutationRunnable(String editingContextId, BiFunction<IEditingContext, IInput, IPayload> function) {
        return this.createRunnable(editingContextId, function, ChangeKind.SEMANTIC_CHANGE);
    }

    /**
     * Return a {@link Runnable} giving the {@link IEditingContext} to {@code function}.
     * <p>
     * The {@link IEditingContextEventHandler} executing {@code function} will emit a {@link ChangeDescription} with the given {@link ChangeKind},
     * thus, the user should provide the right {@link ChangeKind}.
     * </p>
     *
     * @param editingContextId
     *              The editing context ID identifying the {@link IEditingContext} {@code function} will have access to
     * @param function
     *              The function executed by the {@link Runnable}
     * @param changeKind
     *              The change kind emit by
     * @return a {@link Runnable} having access to the editing context
     */
    public Runnable createRunnable(String editingContextId, BiFunction<IEditingContext, IInput, IPayload> function, String changeKind) {
        return () -> {
            var inputId = UUID.randomUUID();
            var input = new ExecuteEditingContextFunctionInput(UUID.randomUUID(), editingContextId, function, new ChangeDescription(changeKind, editingContextId, () -> inputId));

            Mono<IPayload> result = this.executeEditingContextFunctionRunner.execute(input);
            var payload = result.block();
            assertThat(payload).isInstanceOf(ExecuteEditingContextFunctionSuccessPayload.class);
        };
    }

}
