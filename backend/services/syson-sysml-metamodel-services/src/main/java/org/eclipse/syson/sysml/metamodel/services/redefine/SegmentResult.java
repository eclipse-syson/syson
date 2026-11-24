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
package org.eclipse.syson.sysml.metamodel.services.redefine;

import org.eclipse.syson.sysml.Feature;

/**
 * Holds the local feature resolved for one segment, or the failure cause without segment and context labels.
 *
 * @param feature
 *            the resolved local feature, or null when no feature could be resolved
 * @param error
 *            the failure cause, or null when the segment succeeds
 * @author Arthur Daussy
 */
public record SegmentResult(Feature feature, String error) {
}
