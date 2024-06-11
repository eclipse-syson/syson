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
package org.eclipse.syson.services.diagrams.api;

import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.diagrams.Diagram;

import reactor.test.StepVerifier.Step;

/**
 * Retrieves the diagram from a subscription.
 *
 * @author gdaniel
 */
public interface IGivenDiagramReference {

    /**
     * Retrieves the diagram under test.
     * <p>
     * This method should be called on a step verifier that has been initialized with a diagram subscription. The
     * returned reference can be used in <b>other consumers and runnables</b> of the provided {@code stepVerifier} to
     * access the diagram.
     * </p>
     *
     * @param stepVerifier
     *            the step verifier used to retrieve the diagram
     * @return a reference to the diagram
     */
    AtomicReference<Diagram> getDiagram(Step<DiagramRefreshedEventPayload> stepVerifier);

}
