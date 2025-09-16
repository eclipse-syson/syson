/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.eclipse.sirius.components.collaborative.diagrams.api.ICustomNodeStyleDeserializer;
import org.eclipse.sirius.components.diagrams.INodeStyle;
import org.springframework.stereotype.Service;

/**
 * Use to correctly deserialize custom node style.
 *
 * @author arichard
 */
@Service
public class SysMLNoteNodeStyleDeserializer implements ICustomNodeStyleDeserializer {

    @Override
    public boolean canHandle(String type) {
        return type.equals(SysMLNoteNodeStyleProvider.NODE_SYSML_NOTE);
    }

    @Override
    public INodeStyle handle(ObjectMapper mapper, String root) throws JsonProcessingException {
        return mapper.readValue(root, SysMLNoteNodeStyle.class);
    }
}
