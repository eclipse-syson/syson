/*******************************************************************************
 * Copyright (c) 2025, 2026 Obeo.
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
package org.eclipse.syson.application.nodes;

import org.eclipse.sirius.components.collaborative.diagrams.api.ICustomNodeStyleDeserializer;
import org.eclipse.sirius.components.diagrams.INodeStyle;
import org.springframework.stereotype.Service;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.node.ObjectNode;

/**
 * Use to correctly deserialize custom node style.
 *
 * @author arichard
 */
@Service
public class SysMLImportedPackageNodeStyleDeserializer implements ICustomNodeStyleDeserializer {

    @Override
    public boolean canHandle(String type) {
        return type.equals(SysMLImportedPackageNodeStyleProvider.NODE_SYSML_IMPORTED_PACKAGE);
    }

    @Override
    public INodeStyle handle(ObjectNode root, JsonParser jsonParser, DeserializationContext context) throws JacksonException {
        return context.readTreeAsValue(root, SysMLImportedPackageNodeStyle.class);
    }
}
