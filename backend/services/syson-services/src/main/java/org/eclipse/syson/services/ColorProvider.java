/*******************************************************************************
 * Copyright (c) 2023, 2025 Obeo.
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
package org.eclipse.syson.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.sirius.components.emf.ResourceMetadataAdapter;
import org.eclipse.sirius.components.emf.services.JSONResourceFactory;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.components.view.ColorPalette;
import org.eclipse.sirius.components.view.UserColor;
import org.eclipse.sirius.components.view.View;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * Implementation of {@link IColorProvider} for the General View diagram.
 *
 * @author arichard
 */
public class ColorProvider implements IColorProvider {

    private final Logger logger = LoggerFactory.getLogger(ColorProvider.class);

    private final View view;

    private final Optional<View> studioColorPalettesView;

    public ColorProvider(View view) {
        this.view = Objects.requireNonNull(view);
        ResourceSet resourceSet = new ResourceSetImpl();
        this.studioColorPalettesView = this.loadStudioColorPalettes(resourceSet);
    }

    @Override
    public UserColor getColor(String colorName) {
        UserColor color = null;
        if (this.studioColorPalettesView.isPresent()) {
            color = this.getColorFromPalette(this.studioColorPalettesView.get(), colorName);
        }
        if (color == null) {
            color = this.getColorFromPalette(this.view, colorName);
        }
        return color;
    }

    private UserColor getColorFromPalette(View containerView, String colorName) {
        return containerView.getColorPalettes()
                .stream()
                .map(ColorPalette::getColors)
                .flatMap(Collection::stream)
                .filter(userColor -> userColor.getName().equals(colorName))
                .findFirst()
                .orElse(null);
    }

    private Optional<View> loadStudioColorPalettes(ResourceSet resourceSet) {
        ClassPathResource classPathResource = new ClassPathResource("studioColorPalettes.json");
        URI uri = URI.createURI(IEMFEditingContext.RESOURCE_SCHEME + ":///" + UUID.nameUUIDFromBytes(classPathResource.getPath().getBytes()));
        Resource resource = null;
        Optional<Resource> existingResource = resourceSet.getResources().stream().filter(r -> uri.equals(r.getURI())).findFirst();
        if (existingResource.isEmpty()) {
            resource = new JSONResourceFactory().createResource(uri);
            try (var inputStream = new ByteArrayInputStream(classPathResource.getContentAsByteArray())) {
                resourceSet.getResources().add(resource);
                resource.load(inputStream, null);
                resource.eAdapters().add(new ResourceMetadataAdapter("studioColorPalettes", true));
            } catch (IOException exception) {
                this.logger.warn("An error occurred while loading document studioColorPalettes.json: {}.", exception.getMessage());
                resourceSet.getResources().remove(resource);
            }
        } else {
            resource = existingResource.get();
        }
        return resource.getContents()
                .stream()
                .filter(View.class::isInstance)
                .map(View.class::cast)
                .findFirst();
    }
}
