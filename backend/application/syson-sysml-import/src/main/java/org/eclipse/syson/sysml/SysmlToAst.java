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
import org.springframework.stereotype.Component;

/**
 * Converts SysML models to AST representations utilizing an external CLI tool.
 *
 * @author gescande.
 */
@Component
public class SysmlToAst {

    private final String cliPath;

    private final Logger logger = LoggerFactory.getLogger(SysmlToAst.class);

    public SysmlToAst(@Value("${org.eclipse.syson.syside.path}") String cliPath) {
        this.cliPath = cliPath;
    }

    public InputStream convert(InputStream input, String fileExtension) {
        InputStream output = null;

        try {
            Path temp = Files.createTempFile("syson", "." + fileExtension);
            OutputStream outStream = new FileOutputStream(temp.toString());

            byte[] buffer = new byte[8 * 1024];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
            input.close();
            outStream.close();
            Path path = Path.of(this.cliPath);
            this.logger.info("Call syside application : node " + path.toString() + " dump " + temp.toString());
            String[] args = { "node", path.toString(), "dump", temp.toString() };
            ProcessBuilder pb = new ProcessBuilder(args);
            pb = pb.redirectErrorStream(false);
            Process p = pb.start();
            InputStream is = p.getInputStream();
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

                InputStream er = p.getErrorStream();
                InputStreamReader err = new InputStreamReader(er);
                BufferedReader erbr = new BufferedReader(err);
                this.logger.error("Fail to call syside application : \n " + erbr.lines().collect(Collectors.joining("\n")));
            }

            output = new ByteArrayInputStream(builder.toString().getBytes());

            temp.toFile().delete();

        } catch (IOException e) {
            this.logger.error(e.getMessage());
        }

        return output;

    }

}
