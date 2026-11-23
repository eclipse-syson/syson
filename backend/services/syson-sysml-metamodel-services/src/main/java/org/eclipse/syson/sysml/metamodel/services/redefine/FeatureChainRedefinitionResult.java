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
package org.eclipse.syson.sysml.metamodel.services.redefine;

import java.util.List;

import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.Type;

/**
 * Outcome of a feature-chain redefinition. A failure has no local chain and provides user-facing messages.
 * The lists are immutable; the features remain live model objects.
 * Failure locations refer to the original context and selected source, which remain meaningful after rollback.
 *
 * @param isSuccess
 *            whether the complete chain was materialized
 * @param chain
 *            the non-null local chain on success, or an empty list on failure
 * @param messages
 *            the non-null user-facing messages, empty on success
 * @param failureContext
 *            the original operation context on failure, or null on success or when no context was supplied
 * @param failedSegmentIndex
 *            the zero-based selected segment index, or null on success or for a failure concerning the whole request
 * @param failedFeature
 *            the selected source feature at the failed segment, or null on success or when the source is unavailable
 * @author Arthur Daussy
 */
public record FeatureChainRedefinitionResult(boolean isSuccess, List<Feature> chain, List<String> messages,
        Type failureContext, Integer failedSegmentIndex, Feature failedFeature) {

    /**
     * Creates a result without failure location information.
     *
     * @param isSuccess
     *            whether the operation succeeded
     * @param chain
     *            the non-null local chain, containing no null elements
     * @param messages
     *            the non-null messages, containing no null elements
     */
    public FeatureChainRedefinitionResult(boolean isSuccess, List<Feature> chain, List<String> messages) {
        this(isSuccess, chain, messages, null, null, null);
    }

    /**
     * Creates a result, taking immutable copies of its chain and messages.
     *
     * @param isSuccess
     *            whether the operation succeeded
     * @param chain
     *            the non-null local chain, containing no null elements
     * @param messages
     *            the non-null messages, containing no null elements
     * @param failureContext
     *            the original context on failure, or null when unavailable or on success
     * @param failedSegmentIndex
     *            the zero-based failing segment index, or null when unavailable or on success
     * @param failedFeature
     *            the selected source feature, or null when unavailable or on success
     */
    public FeatureChainRedefinitionResult {
        chain = List.copyOf(chain);
        messages = List.copyOf(messages);
    }

    /**
     * Creates a failed result with an empty local chain and one user-facing message.
     *
     * @param message
     *            the non-null explanation of the failure
     * @return the failed redefinition result
     */
    public static FeatureChainRedefinitionResult failure(String message) {
        return failure(message, null, null, null);
    }

    /**
     * Creates a failed result with the available location in the original selection.
     *
     * @param message
     *            the non-null user-facing explanation
     * @param context
     *            the original context, or null if it was not supplied
     * @param segmentIndex
     *            the zero-based segment index, or null for a request-level failure
     * @param source
     *            the selected source feature, or null if it is missing or no segment is identified
     * @return the failed result with an empty local chain
     */
    public static FeatureChainRedefinitionResult failure(String message, Type context, Integer segmentIndex, Feature source) {
        return new FeatureChainRedefinitionResult(false, List.of(), List.of(message), context, segmentIndex, source);
    }

    /**
     * Creates a successful result with an immutable copy of the local chain and an empty message list.
     * Features in the copied list remain live model objects.
     *
     * @param chain
     *            the non-null local feature chain in traversal order, containing no null elements
     * @return the successful redefinition result
     */
    public static FeatureChainRedefinitionResult success(List<Feature> chain) {
        return new FeatureChainRedefinitionResult(true, chain, List.of());
    }
}
