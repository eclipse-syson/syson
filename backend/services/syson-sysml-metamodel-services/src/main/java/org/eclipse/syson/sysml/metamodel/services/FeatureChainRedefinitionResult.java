/*******************************************************************************
 * Copyright (c) 2025, 2026 Obeo.
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
package org.eclipse.syson.sysml.metamodel.services;

import java.util.List;

import org.eclipse.syson.sysml.Feature;

/**
 * Outcome of a feature-chain redefinition. A failure has no local chain and provides user-facing messages.
 * The lists are immutable; the features remain live model objects.
 *
 * @author Arthur Daussy
 */
public record FeatureChainRedefinitionResult(boolean isSuccess, List<Feature> chain, List<String> messages) {

    public FeatureChainRedefinitionResult {
        chain = List.copyOf(chain);
        messages = List.copyOf(messages);
    }

    public static FeatureChainRedefinitionResult success(List<Feature> chain) {
        return new FeatureChainRedefinitionResult(true, chain, List.of());
    }

    public static FeatureChainRedefinitionResult failure(String message) {
        return new FeatureChainRedefinitionResult(false, List.of(), List.of(message));
    }
}
