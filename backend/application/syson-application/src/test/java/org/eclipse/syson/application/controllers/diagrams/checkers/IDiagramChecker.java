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
package org.eclipse.syson.application.controllers.diagrams.checkers;

import org.eclipse.sirius.components.diagrams.Diagram;

/**
 * Checks an invariant on a diagram.
 *
 * @author gdaniel
 */
public interface IDiagramChecker {

    void check(Diagram initialDiagram, Diagram newDiagram);

}
