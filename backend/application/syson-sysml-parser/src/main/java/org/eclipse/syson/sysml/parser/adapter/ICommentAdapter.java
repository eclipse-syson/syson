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

import java.util.List;

import org.eclipse.emf.common.notify.Adapter;

/**
 * Preserves source comments that are not represented as SysML model elements.
 *
 * <p>
 * SysML <code>comment /* ... *&#47;</code>, documentation, and textual representation bodies are model elements and are
 * not stored here. This adapter is only for comments skipped by the grammar, for example:
 * </p>
 *
 * <pre>
 * // leading note
 * // * block note *&#47;
 * part p;
 * </pre>
 *
 * <p>
 * The parser attaches leading comments to the next model object it creates so the serializer can emit them before that
 * element without changing the metamodel.
 * </p>
 *
 * @author ylussaud
 */
public interface ICommentAdapter extends Adapter {

    /**
     * Gets the leading comments attached to the adapted object.
     *
     * @return the immutable leading comments
     */
    List<ParsedComment> getLeadingComments();

    /**
     * Gets the trailing comments attached to the adapted object.
     *
     * @return the immutable trailing comments
     */
    List<ParsedComment> getTrailingComments();

}
