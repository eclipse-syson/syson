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
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.syson.sysml.ASTTransformer;
import org.eclipse.syson.sysml.SysmlToAst;

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

    private void convert(String targetFilePath, File sysmlFile, ResourceSet resourceSet) throws IOException {
        SysmlToAst sysmlToAst = new SysmlToAst(null);
        ASTTransformer astTransformer = new ASTTransformer();
        try (InputStream inputStream = new FileInputStream(sysmlFile)) {
            InputStream astStream = sysmlToAst.convert(inputStream, "sysml");
            Resource resource = astTransformer.convertResource(astStream, resourceSet);

            if (resource != null && !resource.getContents().isEmpty()) {
                System.out.println("Model parsed successfully.");
                XMIResource resourceToSave = new XMIResourceImpl(URI.createFileURI(targetFilePath));
                resourceToSave.getContents().addAll(resource.getContents());
                resourceToSave.save(Collections.emptyMap());
                if (!astTransformer.getTransformationMessages().isEmpty()) {
                    final String errorMessage = astTransformer.getTransformationMessages().stream()
                            .map(message -> message.level().toString() + " - " + message.body())
                            .collect(Collectors.joining("\n", "\n", "\n"));
                    System.err.println("Error while parsing input file : " + errorMessage);
                }

            } else {
                System.err.println("Failed to parse resource or resource is empty.");
            }
        }
    }

}

