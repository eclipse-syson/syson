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
package org.eclipse.syson.services.diagrams;

import java.util.Objects;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.diagrams.tests.graphql.DiagramEventSubscriptionRunner;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.springframework.stereotype.Service;
import org.springframework.test.context.transaction.TestTransaction;

import reactor.core.publisher.Flux;

/**
 * Used to open an existing diagram and subscribe to it.
 *
 * @author gdaniel
 */
@Service
public class GivenDiagramSubscription implements IGivenDiagramSubscription {

    private final DiagramEventSubscriptionRunner diagramEventSubscriptionRunner;

    public GivenDiagramSubscription(DiagramEventSubscriptionRunner diagramEventSubscriptionRunner) {
        this.diagramEventSubscriptionRunner = Objects.requireNonNull(diagramEventSubscriptionRunner);
    }

    @Override
    public Flux<DiagramRefreshedEventPayload> subscribe(DiagramEventInput diagramEventInput) {
        var flux = this.diagramEventSubscriptionRunner.run(diagramEventInput).flux();

        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();

        return flux
                .filter(DiagramRefreshedEventPayload.class::isInstance)
                .map(DiagramRefreshedEventPayload.class::cast);
    }

}
