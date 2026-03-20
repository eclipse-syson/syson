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
package org.eclipse.syson.application.views.explorer;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.core.api.IURLParser;
import org.eclipse.sirius.components.diagrams.description.DiagramDescription;
import org.eclipse.sirius.components.representations.IRepresentationDescription;
import org.eclipse.sirius.web.application.views.viewsexplorer.services.RepresentationDescriptionType;
import org.eclipse.sirius.web.application.views.viewsexplorer.services.RepresentationKind;
import org.eclipse.sirius.web.application.views.viewsexplorer.services.api.IViewsExplorerContentServiceDelegate;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.RepresentationMetadata;
import org.eclipse.syson.sysml.ViewDefinition;
import org.eclipse.syson.sysml.ViewUsage;
import org.springframework.stereotype.Service;

/**
 * Customize the retrieval of the content of the views explorer for SysON.
 *
 * @author frouene
 */
@Service
public class SysONViewsExplorerContentServiceDelegate implements IViewsExplorerContentServiceDelegate {

    private final IURLParser urlParser;

    private final IObjectSearchService objectSearchService;

    public SysONViewsExplorerContentServiceDelegate(IURLParser urlParser, IObjectSearchService objectSearchService) {
        this.urlParser = Objects.requireNonNull(urlParser);
        this.objectSearchService = Objects.requireNonNull(objectSearchService);
    }

    @Override
    public boolean canHandle(IEditingContext editingContext) {
        return true;
    }

    @Override
    public List<RepresentationKind> getContents(IEditingContext editingContext, List<RepresentationMetadata> representationMetadata, Map<String, IRepresentationDescription> representationDescriptions) {
        var descriptionTypes = this.groupByDescriptionType(editingContext, representationMetadata, representationDescriptions);
        return this.groupByKind(descriptionTypes);
    }

    private List<RepresentationDescriptionType> groupByDescriptionType(IEditingContext editingContext, List<RepresentationMetadata> allMetadata,
            Map<String, IRepresentationDescription> allDescriptions) {
        var metadataToViewDefinitionMap = allMetadata.stream()
                .flatMap(metadata ->
                        this.objectSearchService.getObject(editingContext, metadata.getTargetObjectId())
                                .stream()
                                .filter(ViewUsage.class::isInstance)
                                .map(ViewUsage.class::cast)
                                .map(ViewUsage::getViewDefinition)
                                .map(viewDefinition -> new AbstractMap.SimpleEntry<>(metadata, viewDefinition))
                )
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (existing, replacement) -> existing
                ));

        Map<ViewDefinition, List<RepresentationMetadata>> viewDefinitionToMetadataMap = allMetadata.stream()
                .collect(Collectors.groupingBy(metadataToViewDefinitionMap::get));

        return viewDefinitionToMetadataMap.entrySet().stream()
                .map(entry -> {
                    ViewDefinition viewDefinition = entry.getKey();
                    String viewDefinitionName = viewDefinition.getDeclaredShortName();
                    RepresentationMetadata firstMetadata = entry.getValue().get(0);
                    String descriptionId = firstMetadata.getDescriptionId();
                    IRepresentationDescription representationDescription = allDescriptions.get(descriptionId);

                    return Optional.ofNullable(representationDescription)
                            .map(rd -> {
                                if (rd instanceof DiagramDescription) {
                                    return new RepresentationDescriptionType(viewDefinitionName, rd, entry.getValue());
                                }
                                return new RepresentationDescriptionType(descriptionId, rd, entry.getValue());
                            });
                })
                .flatMap(Optional::stream)
                .toList();
    }

    private List<RepresentationKind> groupByKind(List<RepresentationDescriptionType> descriptionTypes) {
        return descriptionTypes.stream()
                .collect(Collectors.groupingBy(descType -> descType.representationsMetadata().get(0).getKind()))
                .entrySet().stream()
                .map(entry -> {
                    var kindId = entry.getKey();
                    var kindName = this.urlParser.getParameterValues(kindId).get("type").get(0);
                    return new RepresentationKind(kindId, kindName, entry.getValue());
                })
                .toList();
    }
}
