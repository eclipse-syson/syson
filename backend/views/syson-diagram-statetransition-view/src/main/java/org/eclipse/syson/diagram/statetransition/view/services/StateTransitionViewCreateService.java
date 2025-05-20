/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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
package org.eclipse.syson.diagram.statetransition.view.services;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.view.emf.diagram.api.IViewDiagramDescriptionSearchService;
import org.eclipse.syson.diagram.common.view.services.ShowDiagramsInheritedMembersService;
import org.eclipse.syson.diagram.common.view.services.ViewCreateService;
import org.eclipse.syson.diagram.statetransition.view.StateTransitionViewDiagramDescriptionProvider;
import org.eclipse.syson.services.api.ISysMLReadOnlyService;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * Creation-related Java services used by the {@link StateTransitionViewDiagramDescriptionProvider}.
 *
 * @author adieumegard
 */
public class StateTransitionViewCreateService extends ViewCreateService {

    public StateTransitionViewCreateService(IViewDiagramDescriptionSearchService viewDiagramDescriptionSearchService, IObjectSearchService objectSearchService, ISysMLReadOnlyService readOnlyService,
            ShowDiagramsInheritedMembersService showDiagramsInheritedMembersService, IFeedbackMessageService msgService) {
        super(viewDiagramDescriptionSearchService, objectSearchService, readOnlyService, showDiagramsInheritedMembersService, msgService);
    }

    @Override
    public boolean canCreateDiagram(Element element) {
        List<EClass> acceptedRootTypes = List.of(
                SysmlPackage.eINSTANCE.getPackage(),
                SysmlPackage.eINSTANCE.getPartUsage(),
                SysmlPackage.eINSTANCE.getPartDefinition(),
                SysmlPackage.eINSTANCE.getStateUsage(),
                SysmlPackage.eINSTANCE.getStateDefinition());
        // Use strict equality here and not EClass#isSuperTypeOf, we want to precisely select which element
        // types can be used as root.
        return super.canCreateDiagram(element) && acceptedRootTypes.contains(element.eClass());
    }
}
