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
package org.eclipse.syson.services;

import org.eclipse.syson.sysml.Comment;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.util.SysmlSwitch;

/**
 * Switch allowing to set an attribute when direct editing a node/edge.
 *
 * @author arichard
 */
public class AttributeToDirectEditSwitch extends SysmlSwitch<Element> {

    private final String newValue;

    public AttributeToDirectEditSwitch(String newValue) {
        this.newValue = newValue;
    }

    @Override
    public Element caseComment(Comment object) {
        object.setBody(this.newValue);
        return object;
    }

    @Override
    public Element caseElement(Element object) {
        object.setDeclaredName(this.newValue);
        return object;
    }
}
