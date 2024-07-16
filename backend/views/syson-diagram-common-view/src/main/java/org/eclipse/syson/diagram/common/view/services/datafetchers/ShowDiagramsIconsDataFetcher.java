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
package org.eclipse.syson.diagram.common.view.services.datafetchers;

import java.util.Objects;

import org.eclipse.sirius.components.annotations.spring.graphql.QueryDataFetcher;
import org.eclipse.sirius.components.graphql.api.IDataFetcherWithFieldCoordinates;
import org.eclipse.syson.diagram.common.view.services.ShowDiagramsIconsService;

import graphql.schema.DataFetchingEnvironment;

/**
 * Data fetcher for Viewer#showDiagramsIcons query.
 *
 * @author arichard
 */
@QueryDataFetcher(type = "Viewer", field = "showDiagramsIconsValue")
public class ShowDiagramsIconsDataFetcher implements IDataFetcherWithFieldCoordinates<Boolean> {

    private final ShowDiagramsIconsService showDiagramsIconsService;

    public ShowDiagramsIconsDataFetcher(ShowDiagramsIconsService showDiagramsIconsService) {
        this.showDiagramsIconsService = Objects.requireNonNull(showDiagramsIconsService);
    }

    @Override
    public Boolean get(DataFetchingEnvironment environment) throws Exception {
        return Boolean.valueOf(this.showDiagramsIconsService.getShowIcons());
    }
}
