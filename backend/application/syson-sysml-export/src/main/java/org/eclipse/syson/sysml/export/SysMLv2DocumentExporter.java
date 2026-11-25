/*******************************************************************************
 * Copyright (c) 2024, 2026 Obeo.
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
package org.eclipse.syson.sysml.export;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.web.application.document.services.api.IDocumentExporter;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.metamodel.services.textual.SysONTextSerializer;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

/**
 * Used to export SysMlv2 Resource.
 *
 * @author gcoutable
 */
@Service
public class SysMLv2DocumentExporter implements IDocumentExporter {

    @Override
    public boolean canHandle(Resource resource, String mediaType) {
        boolean canHandle = false;
        if (MediaType.TEXT_HTML.equals(MediaType.valueOf(mediaType))) {
            canHandle = !resource.getContents().isEmpty() && resource.getContents().get(0) instanceof Element;
        }
        return canHandle;
    }

    @Override
    public Optional<byte[]> getBytes(Resource resource, String mediaType) {
        if (!resource.getContents().isEmpty() && resource.getContents().get(0) instanceof Element element) {
            return Optional.of(new SysONTextSerializer().serialize(element).getBytes(StandardCharsets.UTF_8));
        }
        return Optional.empty();
    }
}
