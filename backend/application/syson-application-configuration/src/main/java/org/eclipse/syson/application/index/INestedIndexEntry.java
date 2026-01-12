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
 * A sub index entry contained in an {@link IIndexEntry}.
 *
 * <p>
 * Nested index entries are used inside regular {@link IIndexEntry} POJOs to represent elements connected to the elements represented by the {@link IIndexEntry}. In most cases,
 * {@link INestedIndexEntry} should not contain other {@link INestedIndexEntry}, in order to avoid infinite nesting depth, which cannot be stored in the index. This constraint may be relaxed for
 * {@link INestedIndexEntry} representing relationships, which can themselves contain an {@link INestedIndexEntry} representing the other end of the relationship.
 * </p>
 * <p>
 * For example, an {@link IIndexEntry} representing a SysML element can contain {@link INestedIndexEntry} representing its owned elements, but these nested entries should not contain information
 * related to their own nested elements. The same {@link IIndexEntry} can contain {@link INestedIndexEntry} representing its relationships, which themselves can contain {@link INestedIndexEntry}
 * representing the other end of the relationship, but this second level of nested entries cannot contain another level of {@link INestedIndexEntry}.
 * </p>
 * <p>
 * The fields {@code id}, {@code type}, and {@code label} should match the identifier of the semantic element, the name of its SysML type, and the label used to present it to the end user. Note that
 * these fields are serialized with a {@code @} prefix because they represent technical information, and shouldn't clash with potential fields computed from  attributes in the SysML metamodel.
 * An extra field {@code @nestedIndexEntryType} is also produced during the serialization to store the concrete type of the POJO. This lets Sirius Web find the actual type to use when deserializing
 * query results.
 * {@link INestedIndexEntry} does not provide {@code iconURLs} nor {@code editingContextId}, because they are not directly manipulated by Sirius Web (their containing {@link IIndexEntry} are).
 * </p>
 *
 * @author gdaniel
 */
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
