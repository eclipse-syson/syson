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
package org.eclipse.syson.sysml;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

/**
 * Converts SysML models to AST representations utilizing an external CLI tool.
 *
 * @author gescande.
 */
@Component
public class SysmlToAst {

    private final Logger logger = LoggerFactory.getLogger(SysmlToAst.class);

    private final String cliPath;

    public SysmlToAst(@Value("${org.eclipse.syson.syside.path:#{null}}") String cliPath) {
        this.cliPath = cliPath;
    }

    public InputStream convert(InputStream input, String fileExtension) {
        InputStream output = null;

        try {
            Path sysmlInputPath = this.createTempFile(input, "syson", fileExtension);

            Path sysIdeInputPath = null;
            if (this.cliPath != null) {
                sysIdeInputPath = Path.of(this.cliPath);
            } else {
                PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
                Resource resource = resolver.getResource(ResourcePatternResolver.CLASSPATH_URL_PREFIX + "syside-cli.js");
                InputStream sysIdeInputStream = resource.getInputStream();
                sysIdeInputPath = this.createTempFile(sysIdeInputStream, "syside-cli", "js");
            }

            this.logger.info("Call syside application : node " + sysIdeInputPath.toString() + " dump " + sysmlInputPath.toString());
            String[] args = { "node", sysIdeInputPath.toString(), "dump", sysmlInputPath.toString() };
            ProcessBuilder pb = new ProcessBuilder(args);
            pb = pb.redirectErrorStream(false);
            Process sysIdeProcess = pb.start();
            InputStream is = sysIdeProcess.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder builder = new StringBuilder();

            String line = br.readLine();
            if (line != null) {
                while (!line.contains("{")) {
                    line = br.readLine();
                }
                builder.append(line);
                while ((line = br.readLine()) != null) {
                    builder.append(line);
                }
            } else {
                InputStream er = sysIdeProcess.getErrorStream();
                InputStreamReader err = new InputStreamReader(er);
                BufferedReader erbr = new BufferedReader(err);
                this.logger.error("Fail to call syside application : \n " + erbr.lines().collect(Collectors.joining("\n")));
            }

            output = new ByteArrayInputStream(builder.toString().getBytes());

            sysmlInputPath.toFile().delete();
            if (this.cliPath == null) {
                sysIdeInputPath.toFile().delete();
            }

        } catch (IOException e) {
            this.logger.error(e.getMessage());
        }

        return output;
    }

    private Path createTempFile(InputStream input, String fileName, String fileExtension) throws IOException, FileNotFoundException {
        Path inputPath = Files.createTempFile(fileName, "." + fileExtension);
        OutputStream outStream = new FileOutputStream(inputPath.toString());
        byte[] buffer = new byte[8 * 1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }
        input.close();
        outStream.close();
        return inputPath;
    }
}
