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

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.variables.CoreVariables;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.syson.diagram.services.DiagramQueryCache;
import org.eclipse.syson.diagram.services.aql.DiagramQueryAQLService;
import org.eclipse.syson.model.services.aql.ModelQueryAQLService;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.ServiceMethod;

/**
 * Node description provider for subject {@link PartUsage} in the General View diagram.
 *
 * @author arichard
 */
public class SubjectNodeDescriptionProvider extends UsageNodeDescriptionProvider {

    public static final String NAME = SDVDescriptionNameGenerator.PREFIX + " Node Subject";

    public SubjectNodeDescriptionProvider(IColorProvider colorProvider) {
        super(SysmlPackage.eINSTANCE.getReferenceUsage(), colorProvider);
    }

    @Override
    protected String createPreconditionExpression() {
        return ServiceMethod.of0(ModelQueryAQLService::isSubject).aqlSelf();
    }

    @Override
    protected String getNodeDescriptionName() {
        return NAME;
    }

    /**
     * Builds semantic candidates using the current render cache.
     */
    @Override
    protected String getSemanticCandidatesExpression(String domainType) {
        return ServiceMethod.of5(DiagramQueryAQLService.class, DiagramQueryAQLService::getExposedSubjects, Element.class, EClass.class, List.class,
                IEditingContext.class, DiagramContext.class, DiagramQueryCache.class)
                .aqlSelf(domainType, org.eclipse.sirius.components.diagrams.description.NodeDescription.ANCESTORS, CoreVariables.EDITING_CONTEXT.name(),
                DiagramContext.DIAGRAM_CONTEXT, DiagramQueryCache.VARIABLE_NAME);
    }
}
