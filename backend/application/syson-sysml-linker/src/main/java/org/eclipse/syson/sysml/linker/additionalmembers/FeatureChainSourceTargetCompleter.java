/*******************************************************************************
 * Copyright (c) 2026 Obeo.
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
package org.eclipse.syson.sysml.linker.additionalmembers;

import org.omg.sysml.lang.sysml.FeatureChainExpression;

/**
 * Reuses parser structural preparation for programmatic and XMI feature chains.
 * Only containment and stored direction are read, so this is a PRE_LINK_ONLY rule.
 *
 * @author Arthur Daussy
 */
public final class FeatureChainSourceTargetCompleter {

    private FeatureChainSourceTargetCompleter() {
        // Utility class.
    }

    /**
     * Completes the source-target feature if it is absent.
     */
    public static void completeSourceTarget(FeatureChainExpression expression) {
        org.eclipse.syson.sysml.parser.internal.FeatureChainSourceTargetCompleter.completeSourceTarget(expression);
    }
}
