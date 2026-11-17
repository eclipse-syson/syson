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
package org.eclipse.syson.sysml.parser.adapter;

/**
 * A source comment that was parsed outside the SysML model.
 *
 * @param kind
 *            the comment form
 * @param text
 *            the exact source text, including delimiters
 */
public record ParsedComment(CommentKind kind, String text) {

}
