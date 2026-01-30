/*******************************************************************************
 * Copyright (c) 2023, 2026 Obeo.
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
package org.eclipse.syson.diagram.common.view.edges;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.EdgeDescription;
import org.eclipse.sirius.components.view.diagram.EdgeStyle;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.sysml.Subsetting;
import org.eclipse.syson.util.ServiceMethod;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Used to create the {@link Subsetting} edge description.
 *
 * @author arichard
 */
public abstract class AbstractSubsettingEdgeDescriptionProvider extends AbstractEdgeDescriptionProvider {

    private final EClass eClass;

    public AbstractSubsettingEdgeDescriptionProvider(EClass eClass, IColorProvider colorProvider) {
        super(colorProvider);
        this.eClass = eClass;
    }

    /**
     * Implementers should provide the actual name of this {@link EdgeDescription}.
     *
     * @return the name of the edge description
     */
    protected abstract String getName();

    /**
     * Implementers should provide the source expression of this {@link EdgeDescription}.
     *
     * @param cache
     *            the cache used to retrieve node descriptions.
     * @return the source expression of this {@link EdgeDescription}.s
     */
    protected abstract String getSourceExpression();

    /**
     * Implementers should provide the target expression of this {@link EdgeDescription}.
     *
     * @param cache
     *            the cache used to retrieve node descriptions.
     * @return the target expression of this {@link EdgeDescription}.s
     */
    protected abstract String getTargetExpression();

    /**
     * Implementers should provide the list of {@link NodeDescription} that can be a source of this
     * {@link EdgeDescription}.
     *
     * @param cache
     *            the cache used to retrieve node descriptions.
     * @return the list of {@link NodeDescription} that can be a source of this {@link EdgeDescription}.
     */
    protected abstract List<NodeDescription> getSourceNodes(IViewDiagramElementFinder cache);

    /**
     * Implementers should provide the list of {@link NodeDescription} that can be a target of this
     * {@link EdgeDescription}.
     *
     * @param cache
     *            the cache used to retrieve node descriptions.
     * @return the list of {@link NodeDescription} that can be a target of this {@link EdgeDescription}.
     */
    protected abstract List<NodeDescription> getTargetNodes(IViewDiagramElementFinder cache);

    /**
     * Create the {@link EdgeStyle} for this {@link EdgeDescription}.
     *
     * @return a new {@link EdgeStyle}.
     */
    protected abstract EdgeStyle createEdgeStyle();

    @Override
    public EdgeDescription create() {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(this.eClass);
        return this.diagramBuilderHelper.newEdgeDescription()
                .domainType(domainType)
                .isDomainBasedEdge(true)
                .centerLabelExpression("")
                .name(this.getName())
                .preconditionExpression("aql:self.oclIsTypeOf(" + domainType + ")")
                .semanticCandidatesExpression(ServiceMethod.of1(UtilService::getAllReachable).aqlSelf(domainType))
                .sourceExpression(this.getSourceExpression())
                .style(this.createEdgeStyle())
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED)
                .targetExpression(this.getTargetExpression())
                .build();
    }


    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        cache.getEdgeDescription(this.getName()).ifPresent(edgeDescription -> {
            diagramDescription.getEdgeDescriptions().add(edgeDescription);
            edgeDescription.getSourceDescriptions().addAll(this.getSourceNodes(cache));
            edgeDescription.getTargetDescriptions().addAll(this.getTargetNodes(cache));
            edgeDescription.setPalette(this.createEdgePalette(cache));
        });
    }
}
