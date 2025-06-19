/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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
package org.eclipse.syson.application.migration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

/**
 * Allows to load the /syson-application-configuration/src/main/resources/migration/stdlib_oldId_to_elementId.txt file
 * and put all entries in a map. This map contains old Ids to elementIds of all standard libraries elements prior to
 * SysMLv2 2025-04 metamodel update.
 *
 * @author arichard
 */
@Service
public class StandardLibrariesIdsResourceToMapParser {

    private final Logger logger = LoggerFactory.getLogger(StandardLibrariesIdsResourceToMapParser.class);

    private final Map<String, String> oldIdToElementIdMap;

    public StandardLibrariesIdsResourceToMapParser(ResourceLoader resourceLoader) {
        Objects.requireNonNull(resourceLoader);
        Resource resource = resourceLoader.getResource(ResourcePatternResolver.CLASSPATH_URL_PREFIX + "migration/stdlib_oldId_to_elementId.txt");
        this.oldIdToElementIdMap = this.resourceToMap(resource);
    }

    public Map<String, String> getOldIdToElementIdMap() {
        return this.oldIdToElementIdMap;
    }

    private Map<String, String> resourceToMap(Resource resource) {
        Instant start = Instant.now();
        Map<String, String> map = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    map.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
        }
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        this.logger.info("Initialization of StandardLibrariesIdsResourceToMapParser completed in %sms".formatted(timeElapsed));
        return map;
    }
}
