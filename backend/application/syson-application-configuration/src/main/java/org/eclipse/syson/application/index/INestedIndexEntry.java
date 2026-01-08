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
package org.eclipse.syson.application.index;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import org.eclipse.sirius.web.application.index.services.api.IIndexEntry;

/**
 * TODO
 *
 * @author gdaniel
 */
// TODO this is required for de-serialization, but it shouldn't be the case, we should deserialize in IndexEntry, not another implementation of IIndexEntry
// TODO if we go this way we should ignore the @nestedIndexEntryType from the index
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@nestedIndexEntryType")
public interface INestedIndexEntry {

    String NESTED_INDEX_ENTRY_TYPE_FIELD = "@nestedIndexEntryType";

    @JsonProperty(IIndexEntry.ID_FIELD)
    String id();

    @JsonProperty(IIndexEntry.TYPE_FIELD)
    String type();

    @JsonProperty(IIndexEntry.LABEL_FIELD)
    String label();


}
