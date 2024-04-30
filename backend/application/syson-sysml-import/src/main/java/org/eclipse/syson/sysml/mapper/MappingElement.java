/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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
package org.eclipse.syson.sysml.mapper;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Objects;

import org.eclipse.emf.ecore.EObject;

/**
 * Implements mapping logic specific to Element in SysML models from AST node.
 *
 * @author gescande
 */
public class MappingElement {

    private final JsonNode mainNode;

    private final EObject parent;

    private EObject self;

    public MappingElement(final JsonNode mainNode, final EObject parent) {
        Objects.requireNonNull(mainNode);
        this.mainNode = mainNode;
        this.parent = parent;
    }

    public JsonNode getMainNode() {
        return this.mainNode;
    }

    public EObject getParent() {
        return this.parent;
    }

    public EObject getSelf() {
        return this.self;
    }

    public void setSelf(EObject self) {
        this.self = self;
    }

}
