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
package org.eclipse.syson.diagram.common.view;

import java.util.List;
import java.util.UUID;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.sirius.components.emf.ResourceMetadataAdapter;
import org.eclipse.sirius.components.emf.services.IDAdapter;
import org.eclipse.sirius.components.emf.services.JSONResourceFactory;
import org.eclipse.sirius.components.view.RepresentationDescription;
import org.eclipse.sirius.components.view.View;
import org.eclipse.sirius.components.view.builder.generated.ViewBuilder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.builder.providers.IRepresentationDescriptionProvider;
import org.eclipse.sirius.emfjson.resource.JsonResource;
import org.eclipse.syson.services.ColorProvider;

/**
 * Abstract class used to register View diagram in the application.
 *
 * @author Jerome Gout
 */
public interface IViewDescriptionProvider {

    /**
     * Implementers should provide the ID of the view diagram this description provider is for.
     *
     * @return the Id of the view diagram
     */
    String getViewDiagramId();

    /**
     * Implementers should provide the {@link IRepresentationDescriptionProvider} of the view diagram this description
     * provider is for.
     *
     * @return the representation description provider of the view diagram
     */
    IRepresentationDescriptionProvider getRepresentationDescriptionProvider();

    default List<View> getRepresentationDescriptions() {
        // Create org.eclipse.sirius.components.view.View
        ViewBuilder viewBuilder = new ViewBuilder();
        View view = viewBuilder.build();
        IColorProvider colorProvider = new ColorProvider(view);

        // Create org.eclipse.sirius.components.view.RepresentationDescription
        IRepresentationDescriptionProvider viewDiagramDescriptionProvider = this.getRepresentationDescriptionProvider();
        RepresentationDescription viewRepresentationDescription = viewDiagramDescriptionProvider.create(colorProvider);
        view.getDescriptions().add(viewRepresentationDescription);

        // Add an ID to all view elements
        view.eAllContents().forEachRemaining(eObject -> {
            eObject.eAdapters().add(new IDAdapter(UUID.nameUUIDFromBytes(EcoreUtil.getURI(eObject).toString().getBytes())));
        });

        // All programmatic Views need to be stored in a Resource and registered in IInMemoryViewRegistry
        String resourcePath = UUID.nameUUIDFromBytes(this.getViewDiagramId().getBytes()).toString();
        JsonResource resource = new JSONResourceFactory().createResourceFromPath(resourcePath);
        resource.eAdapters().add(new ResourceMetadataAdapter(this.getViewDiagramId()));
        resource.getContents().add(view);

        return List.of(view);
    }
}
