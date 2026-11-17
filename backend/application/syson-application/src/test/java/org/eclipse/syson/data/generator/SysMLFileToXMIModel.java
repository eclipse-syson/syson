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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.syson.sysml.services.SysMLParsingService;

/**
 * Converts a SyML file to its XMI representation using SysON import.
 *
 * @author Arthur Daussy
 */
public class SysMLFileToXMIModel {

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.err.println("""
                    Usage: SysMLFileToXMIModel <sysmlFilePath> <targetFilePath>
                        <sysmlFilePath> : Path to the SysML file
                        <targetFilePath> : Path to output file
                    """);
            return;
        }

        String sysmlFilePath = args[0];
        String targetFilePath = args[1];
        new SysMLFileToXMIModel().toXmi(sysmlFilePath, targetFilePath);
    }

    public void toXmi(String sysmlFilePath, String targetFilePath) throws IOException {
        File sysmlFile = new File(sysmlFilePath);
        if (!sysmlFile.exists()) {
            System.err.println("File not found: " + sysmlFilePath);
            return;
        }

        final Path targetPath = Path.of(targetFilePath);
        if (!targetPath.getFileName().toString().endsWith("xmi")) {
            System.err.println("That target file should have the extension .xmi: " + targetFilePath);
            return;
        }


        ResourceSet resourceSet = new SysMLResourceSetProvider().createSysMLResourceSet(true);

        System.out.println("Parsing SysML file: " + sysmlFilePath);

        this.convert(targetFilePath, sysmlFile, resourceSet);

        this.addHeader(targetFilePath, sysmlFile);
    }

    private void addHeader(String targetFilePath, File sysmlFile) throws IOException {
        String sysMLFileContent = Files.readString(sysmlFile.toPath(), StandardCharsets.UTF_8);
        String comment = new StringBuilder()
                .append("Model generated from a .sysml file with ").append(SysMLFileToXMIModel.class.getName()).append(" application.").append(System.lineSeparator())
                .append("Original model : ").append(System.lineSeparator())
                .append(sysMLFileContent).toString();

        new XMLHeaderWriter().writeHeader(comment, targetFilePath);
    }

    /**
     * Parses and links a textual file against the supplied project libraries before saving SysON XMI.
     *
     * @param targetFilePath the output path
     * @param sysmlFile the textual source
     * @param resourceSet the resolution context
     * @throws IOException if the source or output cannot be read or written
     */
    private void convert(String targetFilePath, File sysmlFile, ResourceSet resourceSet) throws IOException {
        try (InputStream inputStream = new FileInputStream(sysmlFile)) {
            var result = new SysMLParsingService().parseAndLink(resourceSet, null, inputStream);
            if (!result.messages().isEmpty()) {
                String messages = result.messages().stream()
                        .map(message -> message.level() + " - " + message.body())
                        .collect(Collectors.joining(System.lineSeparator()));
                System.err.println("Diagnostics while parsing input file: " + messages);
            }
            if (result.root() != null) {
                XMIResource resource = new XMIResourceImpl(URI.createFileURI(targetFilePath));
                resource.getContents().add(result.root());
                resource.save(Collections.emptyMap());
            }
        }
    }
}
