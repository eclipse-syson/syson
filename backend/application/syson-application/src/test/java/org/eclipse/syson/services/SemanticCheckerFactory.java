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
package org.eclipse.syson.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import java.util.function.BiFunction;

import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IInput;
import org.eclipse.sirius.components.core.api.IPayload;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionInput;
import org.eclipse.sirius.components.graphql.tests.api.IExecuteEditingContextFunctionRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

/**
 * Provides methods to run semantic checks as part of a subscription verification.
 *
 * @author gdaniel
 */
@Service
public class SemanticCheckerFactory {

    @Autowired
    private IExecuteEditingContextFunctionRunner executeEditingContextFunctionRunner;

    public Runnable createRunnableChecker(String projectId, BiFunction<IEditingContext, IInput, IPayload> function) {
        return () -> {
            var input = new ExecuteEditingContextFunctionInput(UUID.randomUUID(), projectId, function);

            Mono<IPayload> result = this.executeEditingContextFunctionRunner.execute(input);
            var payload = result.block();
            assertThat(payload).isInstanceOf(IPayload.class);
        };
    }

}
