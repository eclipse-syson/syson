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
package org.eclipse.syson.sysml.serializer;

/**
 * Controls serializer behavior when optional syntax can be reconstructed in multiple valid ways.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public enum SerializationMode {

    /**
     * Prefer preserving syntax-level choices captured during parsing (keyword versus symbol spellings, quoted names,
     * literal spellings, comments and, for plain text, the original whitespace), so that re-serializing a parsed
     * document changes as little as possible.
     */
    ROUND_TRIP,

    /**
     * Prefer canonical syntax derived from model structure, e.g. for models created or edited outside the textual
     * notation. Parser metadata may still be needed to interpret that structure.
     */
    NORMALIZED;
}
