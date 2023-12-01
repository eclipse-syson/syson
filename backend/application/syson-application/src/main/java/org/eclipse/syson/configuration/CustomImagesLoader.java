/*******************************************************************************
 * Copyright (c) 2023 Obeo.
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
package org.eclipse.syson.configuration;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.eclipse.sirius.web.persistence.entities.CustomImageEntity;
import org.eclipse.sirius.web.persistence.repositories.ICustomImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

/**
 * Import custom images into the database on startup.
 *
 * @author pcdavid
 */
@Component
public class CustomImagesLoader implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(CustomImagesLoader.class);

    private final ICustomImageRepository customImageRepository;

    private final String imagesPathPattern;

    private final PathMatchingResourcePatternResolver patternResolver;

    public CustomImagesLoader(ICustomImageRepository customImageRepository, @Value("${org.eclipse.sirius.web.customImages.pattern:#{null}}") String imagesPathPattern, ResourceLoader resourceLoader) {
        this.customImageRepository = Objects.requireNonNull(customImageRepository);
        this.imagesPathPattern = imagesPathPattern;
        this.patternResolver = new PathMatchingResourcePatternResolver(Objects.requireNonNull(resourceLoader));
    }

    @Override
    public void run(String... args) throws Exception {
        if (this.imagesPathPattern != null) {
            Resource[] resources = this.patternResolver.getResources(this.imagesPathPattern);
            for (Resource resource : resources) {
                Optional<String> contentType = this.getContentType(resource);
                if (contentType.isPresent() && contentType.get().startsWith("image/")) {
                    this.importImageFromResource(resource, contentType.get());
                }
            }
        }
    }

    private void importImageFromResource(Resource resource, String contentType) {
        try {
            CustomImageEntity customImageEntity = new CustomImageEntity();
            // No project set: these are global images
            customImageEntity.setLabel(Optional.ofNullable(resource.getFilename()).map(this::trimFileExtension).orElse(""));
            customImageEntity.setContentType(contentType);
            try (BufferedInputStream stream = new BufferedInputStream(resource.getInputStream())) {
                customImageEntity.setContent(stream.readAllBytes());
            }
            customImageEntity.setId(UUID.nameUUIDFromBytes(customImageEntity.getContent()));

            this.logger.debug(resource.getFilename() + ": " + customImageEntity.getId().toString());

            this.customImageRepository.save(customImageEntity);
        } catch (IOException e) {
            this.logger.warn("Error loading resource {}: {}", resource, e.getMessage());
        }
    }

    private Optional<String> getContentType(Resource resource) {
        try {
            return Optional.ofNullable(resource.getURL().openConnection().getContentType());
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    private String trimFileExtension(String fileName) {
        int extensionStart = fileName.lastIndexOf('.');
        if (extensionStart != -1) {
            return fileName.substring(0, extensionStart);
        } else {
            return fileName;
        }
    }
}
