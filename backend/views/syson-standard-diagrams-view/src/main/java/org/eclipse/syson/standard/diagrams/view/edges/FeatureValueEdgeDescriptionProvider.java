/*******************************************************************************
 * Copyright (c) 2023, 2025 Obeo.
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
package org.eclipse.syson.standard.diagrams.view.edges;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.syson.diagram.common.view.edges.AbstractFeatureValueEdgeDescriptionProvider;
import org.eclipse.syson.sysml.FeatureValue;
import org.eclipse.syson.sysml.ItemUsage;
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Used to describe a {@link FeatureValue} edge between two {@link PortUsage}, {@link ItemUsage} or
 * {@link ReferenceUsage}.
 *
 * @author Arthur Daussy
 */
public class FeatureValueEdgeDescriptionProvider extends AbstractFeatureValueEdgeDescriptionProvider {

    private final IDescriptionNameGenerator descriptionNameGenerator;

    public FeatureValueEdgeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(colorProvider);
        this.descriptionNameGenerator = Objects.requireNonNull(descriptionNameGenerator);
    }

    @Override
    public String getName() {
        return this.descriptionNameGenerator.getEdgeName(SysmlPackage.eINSTANCE.getFeatureValue());
    }

    @Override
    protected List<NodeDescription> getSourceNodes(IViewDiagramElementFinder cache) {
        List<NodeDescription> nodes = new ArrayList<>();
        cache.getNodeDescription(this.descriptionNameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getItemUsage())).ifPresent(nodes::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getPortUsage(), SysmlPackage.eINSTANCE.getUsage_NestedPort())).ifPresent(nodes::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getPortUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedPort())).ifPresent(nodes::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getInheritedBorderNodeName(SysmlPackage.eINSTANCE.getPortUsage(), SysmlPackage.eINSTANCE.getUsage_NestedPort())).ifPresent(nodes::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getInheritedBorderNodeName(SysmlPackage.eINSTANCE.getPortUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedPort()))
                .ifPresent(nodes::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getReferenceUsage())).ifPresent(nodes::add);
        return nodes;
    }

    @Override
    protected List<NodeDescription> getTargetNodes(IViewDiagramElementFinder cache) {
        List<NodeDescription> nodes = new ArrayList<>();
        cache.getNodeDescription(this.descriptionNameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getItemUsage())).ifPresent(nodes::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getPortUsage(), SysmlPackage.eINSTANCE.getUsage_NestedPort())).ifPresent(nodes::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getPortUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedPort())).ifPresent(nodes::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getInheritedBorderNodeName(SysmlPackage.eINSTANCE.getPortUsage(), SysmlPackage.eINSTANCE.getUsage_NestedPort())).ifPresent(nodes::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getInheritedBorderNodeName(SysmlPackage.eINSTANCE.getPortUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedPort()))
                .ifPresent(nodes::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getReferenceUsage())).ifPresent(nodes::add);
        return nodes;
    }

}
