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
package org.eclipse.syson.standard.diagrams.view;

import org.eclipse.sirius.components.view.RepresentationDescription;
import org.eclipse.sirius.components.view.builder.generated.diagram.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.builder.providers.IRepresentationDescriptionProvider;
import org.eclipse.sirius.components.view.diagram.ArrangeLayoutDirection;
import org.eclipse.syson.common.view.api.IViewDescriptionProvider;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.springframework.stereotype.Service;

/**
 * Allows to register the Action Flow View diagram in the application. It is only useful to have the Action Flow View
 * entry in the "New Representation" menu item in the Explorer view. The actual diagram created is the SDV one.
 *
 * @author arichard
 */
@Service
public class ActionFlowViewDiagramDescriptionProvider implements IViewDescriptionProvider {

    public static final String DESCRIPTION_NAME = "Action Flow View";

    @Override
    public String getViewId() {
        return "ActionFlowViewDiagram";
    }

    @Override
    public IRepresentationDescriptionProvider getRepresentationDescriptionProvider() {
        return new IRepresentationDescriptionProvider() {
            @Override
            public RepresentationDescription create(IColorProvider colorProvider) {
                return new DiagramBuilders().newDiagramDescription()
                        .arrangeLayoutDirection(ArrangeLayoutDirection.DOWN)
                        .autoLayout(false)
                        .domainType(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getNamespace()))
                        .preconditionExpression(AQLUtils.getSelfServiceCallExpression("canCreateDiagram"))
                        .name(DESCRIPTION_NAME)
                        .titleExpression("aql:'view'+ Sequence{self.existingViewUsagesCountForRepresentationCreation(), 1}->sum()")
                        .build();
            }
        };
    }
}
