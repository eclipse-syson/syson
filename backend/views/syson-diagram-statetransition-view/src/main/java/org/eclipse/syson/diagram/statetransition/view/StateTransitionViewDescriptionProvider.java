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
package org.eclipse.syson.diagram.statetransition.view;

import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.sirius.components.view.builder.providers.IRepresentationDescriptionProvider;
import org.eclipse.sirius.components.view.emf.IViewConverter;
import org.eclipse.sirius.web.services.api.representations.IInMemoryViewRegistry;
import org.eclipse.syson.diagram.common.view.AbstractViewDescriptionProvider;
import org.springframework.context.annotation.Configuration;

/**
 * Allows to register the State Transition View diagram in the application.
 *
 * @author adieumegard
 */
@Configuration
public class StateTransitionViewDescriptionProvider extends AbstractViewDescriptionProvider {

    public StateTransitionViewDescriptionProvider(IViewConverter viewConverter, Registry ePackagesRegistry, IInMemoryViewRegistry inMemoryViewRegistry) {
        super(viewConverter, ePackagesRegistry, inMemoryViewRegistry);
    }

    @Override
    protected String getViewDiagramId() {
        return "StateTransitionViewDiagram";
    }

    @Override
    protected IRepresentationDescriptionProvider getRepresentationDescriptionProvider() {
        return new StateTransitionViewDiagramDescriptionProvider();
    }
}
