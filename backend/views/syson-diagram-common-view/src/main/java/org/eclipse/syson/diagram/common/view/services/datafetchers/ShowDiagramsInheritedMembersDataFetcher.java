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
import org.eclipse.syson.diagram.common.view.services.ShowDiagramsInheritedMembersService;

import graphql.schema.DataFetchingEnvironment;

/**
 * Data fetcher for Viewer#showDiagramsInheritedMembers query.
 *
 * @author arichard
 */
@QueryDataFetcher(type = "Viewer", field = "showDiagramsInheritedMembersValue")
public class ShowDiagramsInheritedMembersDataFetcher implements IDataFetcherWithFieldCoordinates<Boolean> {

    private final ShowDiagramsInheritedMembersService showDiagramsInheritedMembersService;

    public ShowDiagramsInheritedMembersDataFetcher(ShowDiagramsInheritedMembersService showDiagramsInheritedMembersService) {
        this.showDiagramsInheritedMembersService = Objects.requireNonNull(showDiagramsInheritedMembersService);
    }

    @Override
    public Boolean get(DataFetchingEnvironment environment) throws Exception {
        return Boolean.valueOf(this.showDiagramsInheritedMembersService.getShowInheritedMembers());
    }
}
