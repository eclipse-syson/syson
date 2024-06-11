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
package org.eclipse.syson.services.diagrams;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramReference;
import org.springframework.stereotype.Service;

import reactor.test.StepVerifier.Step;

/**
 * Retrieves the diagram used by a test.
 *
 * @author gdaniel
 */
@Service
public class GivenDiagramReference implements IGivenDiagramReference {

    @Override
    public AtomicReference<Diagram> getDiagram(Step<DiagramRefreshedEventPayload> stepVerifier) {
        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<DiagramRefreshedEventPayload> initialDiagramContentConsumer = payload -> Optional.of(payload)
                .map(DiagramRefreshedEventPayload::diagram)
                .ifPresentOrElse(diagram::set, () -> fail("Missing diagram"));
        stepVerifier.consumeNextWith(initialDiagramContentConsumer);
        return diagram;
    }

}
