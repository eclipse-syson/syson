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
package org.eclipse.syson.standard.diagrams.view.nodes;

import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.syson.diagram.services.aql.DiagramQueryAQLService;
import org.eclipse.syson.model.services.aql.ModelQueryAQLService;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.ServiceMethod;

/**
 * Node description provider for stakeholder {@link PartUsage} in the General View diagram.
 *
 * @author pcdavid
 */
public class StakeholderNodeDescriptionProvider extends UsageNodeDescriptionProvider {

    public static final String NAME = SDVDescriptionNameGenerator.PREFIX + " Node Stakeholder";

    public StakeholderNodeDescriptionProvider(IColorProvider colorProvider) {
        super(SysmlPackage.eINSTANCE.getPartUsage(), colorProvider);
    }

    @Override
    protected String createPreconditionExpression() {
        return ServiceMethod.of0(ModelQueryAQLService::isStakeholder).aqlSelf();
    }

    @Override
    protected String getNodeDescriptionName() {
        return NAME;
    }

    @Override
    protected String getSemanticCandidatesExpression(String domainType) {
        return ServiceMethod.of4(DiagramQueryAQLService::getExposedStakeholders).aqlSelf(domainType, org.eclipse.sirius.components.diagrams.description.NodeDescription.ANCESTORS,
                IEditingContext.EDITING_CONTEXT,
                DiagramContext.DIAGRAM_CONTEXT);
    }
}
