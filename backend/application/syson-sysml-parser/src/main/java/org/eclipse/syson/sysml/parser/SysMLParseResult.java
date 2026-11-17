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
 * @param model
 *            the parsed model
 * @param danglingLinks
 *            the {@link Set} of {@link DanglingLink}
 * @param textAdapted
 *            the {@link Set} of {@link EObject} with a {@link ITextAdapter}
 * @param diagnostic
 *            the parsing {@link Diagnostic}
 * @param internalDiagnostic
 *            the internal parser {@link Diagnostic}
 * @param <T>
 *            a kind of parsed element
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public record SysMLParseResult<T>(T model, Set<DanglingLink> danglingLinks, Set<EObject> textAdapted, Diagnostic diagnostic, Diagnostic internalDiagnostic) implements ISysMLParseResult<T> {

}
