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
package org.eclipse.syson.diagram.statetransition.view;

import org.eclipse.syson.util.DescriptionNameGenerator;

/**
 * Name generator used by all State Transition View description providers.
 *
 * @author adieumegard
 */
public class STVDescriptionNameGenerator extends DescriptionNameGenerator {

    public STVDescriptionNameGenerator() {
        super("STV");
    }
}
