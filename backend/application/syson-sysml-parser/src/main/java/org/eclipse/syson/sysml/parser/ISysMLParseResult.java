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
package org.eclipse.syson.sysml.parser;

import java.util.Set;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.syson.sysml.parser.adapter.ITextAdapter;

/**
 * SysML parsing result.
 *
 * @param <T>
 *            a kind of parsed element
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public interface ISysMLParseResult<T> {

    /**
     * Gets the parsed model.
     *
     * @return the parsed model
     */
    T model();

    /**
     * Gets the {@link Set} of {@link DanglingLink}.
     *
     * @return the {@link Set} of {@link DanglingLink}
     */
    Set<DanglingLink> danglingLinks();

    /**
     * Gets the {@link Set} of {@link EObject} with a {@link ITextAdapter}.
     *
     * @return the {@link Set} of {@link EObject} with a {@link ITextAdapter}
     */
    Set<EObject> textAdapted();

    /**
     * Gets the {@link Diagnostic} of the parsing.
     *
     * @return the {@link Diagnostic} of the parsing
     */
    Diagnostic diagnostic();

}
