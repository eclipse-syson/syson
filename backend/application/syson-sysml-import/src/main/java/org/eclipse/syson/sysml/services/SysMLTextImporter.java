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
package org.eclipse.syson.sysml.services;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.services.api.ISysMLTextImporter;
import org.springframework.stereotype.Service;

/**
 * Imports textual elements through the Java parser and linker.
 *
 * @author arichard
 */
@Service
public class SysMLTextImporter implements ISysMLTextImporter {

    private final SysMLParsingService parsingService;

    /**
     * Creates a text importer.
     *
     * @param parsingService the shared Java parsing pipeline
     */
    public SysMLTextImporter(SysMLParsingService parsingService) {
        this.parsingService = Objects.requireNonNull(parsingService);
    }

    @Override
    public List<Element> importSysMLText(IEMFEditingContext emfEditingContext, Element parentElement, String textualContent, List<Message> messages) {
        var input = new ByteArrayInputStream(textualContent.getBytes(StandardCharsets.UTF_8));
        var result = this.parsingService.parseAndLink(emfEditingContext.getDomain().getResourceSet(), parentElement, input);
        messages.addAll(result.messages());
        List<Element> elements = result.elements();
        if (result.root() != null) {
            parentElement.getOwnedRelationship().addAll(List.copyOf(result.root().getOwnedRelationship()));
            result.applyImports();
        }
        return elements;
    }
}
