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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages the logging and reporting of import events.
 *
 * @author wldblm
 */
public class LogBook {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogBook.class);

    private static final Set<Event> EVENTS = new HashSet<>();

    private static final ResourceBundle MESSAGES = ResourceBundle.getBundle("messages");

    private static final StringBuilder REPORT = new StringBuilder();

    public static void addEvent(String messageId, EObject eObject, Object... params) {
        EVENTS.add(new Event(messageId, eObject, params));
    }

    public static void emitLogMessages() {
        for (Event event : EVENTS) {
            REPORT.append(event.toLogMessage(MESSAGES));
        }
        EVENTS.clear();
    }

    public static String getReport(String fileName) {
        emitLogMessages();
        saveReportToDownloads(REPORT.toString(), fileName);
        return REPORT.toString();
    }

    public static void saveReportToDownloads(String reportContent, String fileName) {
        try {
            String header = generateReportHeader(fileName);
            String finalReportContent = header + reportContent;

            String dateTimePattern = "yyyyMMdd_HHmm";
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimePattern);
            String formattedDateTime = now.format(formatter);

            String reportName = "report_" + fileName + "_" + formattedDateTime + ".txt";

            String userHome = System.getProperty("user.home");
            Path downloadsPath = Paths.get(userHome, "Downloads", reportName);

            Files.createDirectories(downloadsPath.getParent());

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(downloadsPath.toFile()))) {
                writer.write(finalReportContent);
            }

            REPORT.setLength(0);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    private static String generateReportHeader(String fileName) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String generationDateTime = LocalDateTime.now().format(formatter);
        return "================================================================================\n" + "SYSON IMPORT REPORT " + fileName + "\n"
                + "================================================================================\n" + "Report Generated: " + generationDateTime + "\n\n";
    }
}
