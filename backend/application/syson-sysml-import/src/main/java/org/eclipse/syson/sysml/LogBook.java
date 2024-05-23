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

    public static void addEvent(final String messageId, final EObject eObject, final Object... params) {
        EVENTS.add(new Event(messageId, eObject, params));
    }

    public static void emitLogMessages() {
        for (final Event event : EVENTS) {
            REPORT.append(event.toLogMessage(MESSAGES));
        }
        EVENTS.clear();
    }

    public static String getReport(final String fileName) {
        emitLogMessages();
        return REPORT.toString();
    }

    public static void saveReportToDownloads(final String reportContent, final String fileName) {
        try {
            final String header = generateReportHeader(fileName);
            final String finalReportContent = header + reportContent;

            final String dateTimePattern = "yyyyMMdd_HHmm";
            final LocalDateTime now = LocalDateTime.now();
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimePattern);
            final String formattedDateTime = now.format(formatter);

            final String reportName = "report_" + fileName + "_" + formattedDateTime + ".txt";

            final String userHome = System.getProperty("user.home");
            final Path downloadsPath = Paths.get(userHome, "Downloads", reportName);

            Files.createDirectories(downloadsPath.getParent());

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(downloadsPath.toFile()))) {
                writer.write(finalReportContent);
            }

            REPORT.setLength(0);
        } catch (final IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    private static String generateReportHeader(final String fileName) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        final String generationDateTime = LocalDateTime.now().format(formatter);
        return "================================================================================\n" + "SYSON IMPORT REPORT " + fileName + "\n"
                + "================================================================================\n" + "Report Generated: " + generationDateTime + "\n\n";
    }
}
