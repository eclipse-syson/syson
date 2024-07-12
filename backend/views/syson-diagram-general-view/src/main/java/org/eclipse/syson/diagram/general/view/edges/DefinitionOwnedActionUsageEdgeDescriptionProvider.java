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
package org.eclipse.syson.diagram.general.view.edges;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.syson.diagram.common.view.edges.AbstractDefinitionOwnedUsageEdgeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.GeneralViewDiagramDescriptionProvider;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Used to create the edge description between definitions and their owned action usages in the General View diagram.
 *
 * @author Jerome Gout
 */
public class DefinitionOwnedActionUsageEdgeDescriptionProvider extends AbstractDefinitionOwnedUsageEdgeDescriptionProvider {

    public DefinitionOwnedActionUsageEdgeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator nameGenerator) {
        super(SysmlPackage.eINSTANCE.getActionUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedAction(), colorProvider, nameGenerator);
    }

    @Override
    protected List<EClass> getEdgeSources() {
        return GeneralViewDiagramDescriptionProvider.DEFINITIONS;
    }

}
