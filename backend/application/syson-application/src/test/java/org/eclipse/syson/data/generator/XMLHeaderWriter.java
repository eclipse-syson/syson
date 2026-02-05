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
package org.eclipse.syson.data.generator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Helper to add a header comment on XML file.
 *
 * @author Arthur Daussy
 */
public class XMLHeaderWriter {

    /**
     * Write a header comment in a given XML file.
     *
     * @param headerContent
     *         the content to add as header
     * @param targetFile
     *         the destination file
     * @throws IOException
     *         if something goes wrong while writing
     */
    public void writeHeader(String headerContent, String targetFile) throws IOException {
        final Path pathFilePath = Path.of(targetFile);
        String xml = Files.readString(pathFilePath, StandardCharsets.UTF_8);

        String patched = this.insertMultilineComment(xml, headerContent);

        Files.writeString(pathFilePath, patched, StandardCharsets.UTF_8,
                StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
    }

    private String insertMultilineComment(String xml, String comment) {
        String safe = this.toSafeXmlComment(comment);

        String c = "\n<!--\n" + safe + "\n-->\n";

        int declEnd = xml.indexOf("?>");
        if (declEnd >= 0) {
            declEnd += 2;
            return xml.substring(0, declEnd) + c + xml.substring(declEnd);
        }
        return c + xml;
    }

    private String toSafeXmlComment(String comment) {
        if (comment == null) {
            return "";
        }

        String s = comment;
        // Forbidden in XML comment
        s = s.replace("--", "—");
        if (s.endsWith("-")) {
            s += " ";
        }

        return s;
    }

}
