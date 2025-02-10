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
package org.eclipse.syson.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import java.util.function.BiFunction;

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

    public Runnable createRunnable(String editingContextId, BiFunction<IEditingContext, IInput, IPayload> function) {
        return () -> {
            var input = new ExecuteEditingContextFunctionInput(UUID.randomUUID(), editingContextId, function);

            Mono<IPayload> result = this.executeEditingContextFunctionRunner.execute(input);
            var payload = result.block();
            assertThat(payload).isInstanceOf(ExecuteEditingContextFunctionSuccessPayload.class);
        };
    }

}
