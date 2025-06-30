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
package org.eclipse.syson.services.diagrams;

import java.util.Objects;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.diagrams.tests.graphql.DiagramEventSubscriptionRunner;
import org.eclipse.sirius.web.tests.services.api.IGivenCommittedTransaction;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;

/**
 * Used to open an existing diagram and subscribe to it.
 *
 * @author gdaniel
 */
@Service
public class GivenDiagramSubscription implements IGivenDiagramSubscription {

    private final IGivenCommittedTransaction givenCommittedTransaction;

    private final DiagramEventSubscriptionRunner diagramEventSubscriptionRunner;

    public GivenDiagramSubscription(IGivenCommittedTransaction givenCommittedTransaction, DiagramEventSubscriptionRunner diagramEventSubscriptionRunner) {
        this.givenCommittedTransaction = Objects.requireNonNull(givenCommittedTransaction);
        this.diagramEventSubscriptionRunner = Objects.requireNonNull(diagramEventSubscriptionRunner);
    }

    @Override
    public Flux<DiagramRefreshedEventPayload> subscribe(DiagramEventInput diagramEventInput) {
        this.givenCommittedTransaction.commit();
        var flux = this.diagramEventSubscriptionRunner.run(diagramEventInput);
        this.givenCommittedTransaction.commit();
        return flux
                .filter(DiagramRefreshedEventPayload.class::isInstance)
                .map(DiagramRefreshedEventPayload.class::cast);
    }

}
