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
package org.eclipse.syson.sysml;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import org.eclipse.syson.sysml.textual.utils.Status;

/**
 * Result of the parsing of the AST.
 *
 * @param ast an optional {@link InputStream} of the AST
 * @param reports parsing messages to be reported to the user
 *
 * @author Arthur Daussy
 */
public record AstParsingResult(Optional<InputStream> ast, List<Status> reports) {
}
