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
import java.util.List;
import java.util.Objects;

import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.syson.sysml.ASTTransformer;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.SysmlToAst;
import org.eclipse.syson.sysml.metamodel.services.textual.utils.Status;
import org.eclipse.syson.sysml.services.api.ISysMLTextImporter;
import org.springframework.stereotype.Service;

/**
 * syside-based implementation of {@code ISysMLTextImporter}.
 *
 * @author pcdavid
 */
@Service
public class SysideSysMLTextImporter implements ISysMLTextImporter {
    private final SysmlToAst sysmlToAst;

    public SysideSysMLTextImporter(SysmlToAst sysmlToAst) {
        this.sysmlToAst = Objects.requireNonNull(sysmlToAst);
    }

    @Override
    public List<Element> importSysMLText(IEMFEditingContext emfEditingContext, Element parentElement, String textualContent, List<Message> messages) {
        List<Element> newObjects = List.of();

        // Phase 1: parse the raw text into a JSON AST using syside-cli
        var inputStream = new ByteArrayInputStream(textualContent.getBytes());
        var astParsingResult = this.sysmlToAst.convert(inputStream, ".sysml");
        messages.addAll(this.toMessages(astParsingResult.reports()));

        // Phase 2: convert the resulting JSON AST into actual SySMLv2 Elements and integrate them into
        // parentElement.
        if (astParsingResult.ast().isPresent()) {
            var transformer = new ASTTransformer();
            newObjects = transformer.convertToElements(astParsingResult.ast().get(), parentElement);
            messages.addAll(transformer.getTransformationMessages());
        }

        return newObjects;
    }

    private List<Message> toMessages(List<Status> reports) {
        return reports.stream().map((Status status) -> {
            return switch (status.severity()) {
                case INFO -> new Message(status.message(), MessageLevel.INFO);
                case WARNING -> new Message(status.message(), MessageLevel.WARNING);
                case ERROR -> new Message(status.message(), MessageLevel.ERROR);
                default -> null;
            };
        }).filter(Objects::nonNull).toList();
    }
}
