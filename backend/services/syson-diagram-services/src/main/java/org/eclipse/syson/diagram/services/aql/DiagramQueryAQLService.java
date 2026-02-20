/*******************************************************************************
 * Copyright (c) 2025, 2026 Obeo.
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
package org.eclipse.syson.diagram.services.aql;

import java.util.Objects;

import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.renderer.DiagramRenderingCache;
import org.eclipse.syson.diagram.services.DiagramQueryElementService;
import org.eclipse.syson.diagram.services.DiagramQueryLabelService;
import org.eclipse.syson.sysml.Comment;
import org.eclipse.syson.sysml.Connector;
import org.eclipse.syson.sysml.Dependency;
import org.eclipse.syson.sysml.Documentation;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.SatisfyRequirementUsage;
import org.eclipse.syson.sysml.TextualRepresentation;
import org.eclipse.syson.sysml.TransitionUsage;
import org.eclipse.syson.sysml.Usage;

/**
 * Entry point for all diagram-related services doing queries in diagrams and called by AQL expressions in diagram
 * descriptions. This class is not a @Service class but act as it was, because it is called by IJavaServiceProvider.
 *
 * @author arichard
 */
public class DiagramQueryAQLService {

    private final DiagramQueryElementService diagramQueryElementService;

    private final DiagramQueryLabelService diagramQueryLabelService;

    public DiagramQueryAQLService(DiagramQueryElementService diagramQueryElementService, DiagramQueryLabelService diagramQueryLabelService) {
        this.diagramQueryElementService = Objects.requireNonNull(diagramQueryElementService);
        this.diagramQueryLabelService = Objects.requireNonNull(diagramQueryLabelService);
    }

    /**
     * {@link DiagramQueryLabelService#getBorderNodeUsageLabel(Usage)}.
     */
    public String getBorderNodeUsageLabel(Usage usage) {
        return this.diagramQueryLabelService.getBorderNodeUsageLabel(usage);
    }

    /**
     * {@link DiagramQueryLabelService#getCompartmentItemLabel(Usage)}.
     * {@link DiagramQueryLabelService#getCompartmentItemLabel(Documentation)}.
     */
    public String getCompartmentItemLabel(Element element) {
        String compartmentItemLabel = "";
        if (element instanceof Documentation documentation) {
            compartmentItemLabel = this.diagramQueryLabelService.getCompartmentItemLabel(documentation);
        } else if (element instanceof Usage usage) {
            compartmentItemLabel = this.diagramQueryLabelService.getCompartmentItemLabel(usage);
        }
        return compartmentItemLabel;
    }

    /**
     * {@link DiagramQueryLabelService#getContainerLabel(Element)}.
     */
    public String getContainerLabel(Element element) {
        return this.diagramQueryLabelService.getContainerLabel(element);
    }

    /**
     * {@link DiagramQueryLabelService#getDefaultInitialDirectEditLabel(Comment)}.
     * {@link DiagramQueryLabelService#getDefaultInitialDirectEditLabel(Element)}.
     * {@link DiagramQueryLabelService#getDefaultInitialDirectEditLabel(TextualRepresentation)}.
     */
    public String getDefaultInitialDirectEditLabel(Element element) {
        String defaultInitialDirectEditLabel = "";
        if (element instanceof Comment comment) {
            defaultInitialDirectEditLabel = this.diagramQueryLabelService.getDefaultInitialDirectEditLabel(comment);
        } else if (element instanceof TextualRepresentation textualRepresentation) {
            defaultInitialDirectEditLabel = this.diagramQueryLabelService.getDefaultInitialDirectEditLabel(textualRepresentation);
        } else {
            defaultInitialDirectEditLabel = this.diagramQueryLabelService.getDefaultInitialDirectEditLabel(element);
        }
        return defaultInitialDirectEditLabel;
    }

    /**
     * {@link DiagramQueryLabelService#getDependencyLabel(Dependency)}.
     */
    public String getDependencyLabel(Dependency dependency) {
        return this.diagramQueryLabelService.getDependencyLabel(dependency);
    }

    /**
     * {@link DiagramQueryLabelService#getEdgeLabel(Element)}.
     */
    public String getEdgeLabel(Element element) {
        return this.diagramQueryLabelService.getEdgeLabel(element);
    }

    /**
     * {@link DiagramQueryLabelService#getInitialDirectEditListItemLabel(Documentation)}.
     * {@link DiagramQueryLabelService#getInitialDirectEditListItemLabel(Comment)}.
     * {@link DiagramQueryLabelService#getInitialDirectEditListItemLabel(Usage)}.
     */
    public String getInitialDirectEditListItemLabel(Element element) {
        String initialDirectEditListItemLabel = "";
        if (element instanceof Documentation documentation) {
            initialDirectEditListItemLabel = this.diagramQueryLabelService.getInitialDirectEditListItemLabel(documentation);
        } else if (element instanceof Comment comment) {
            initialDirectEditListItemLabel = this.diagramQueryLabelService.getInitialDirectEditListItemLabel(comment);
        } else if (element instanceof Usage usage) {
            initialDirectEditListItemLabel = this.diagramQueryLabelService.getInitialDirectEditListItemLabel(usage);
        }
        return initialDirectEditListItemLabel;
    }

    /**
     * {@link DiagramQueryLabelService#getMultiplicityLabel(Element)}.
     */
    public String getMultiplicityLabel(Element element) {
        return this.diagramQueryLabelService.getMultiplicityLabel(element);
    }

    /**
     * {@link DiagramQueryLabelService#getTransitionLabel(TransitionUsage)}.
     */
    public String getTransitionLabel(TransitionUsage transition) {
        return this.diagramQueryLabelService.getTransitionLabel(transition);
    }

    /**
     * {@link DiagramQueryLabelService#getSatisfyLabel(SatisfyRequirementUsage)}.
     */
    public String getSatisfyLabel(SatisfyRequirementUsage satisfyRequirementUsage) {
        return this.diagramQueryLabelService.getSatisfyLabel(satisfyRequirementUsage);
    }

    /**
     * {@link DiagramQueryElementService#infoMessage(Object, String)}.
     */
    public Object infoMessage(Object self, String message) {
        this.diagramQueryElementService.infoMessage(self, message);
        return self;
    }

    /**
     * {@link DiagramQueryElementService#isDiagramEmpty(editingContext, diagramContext, previousDiagram)}.
     */
    public boolean isDiagramEmpty(IEditingContext editingContext, DiagramContext diagramContext, Diagram previousDiagram, int exposedElements) {
        return this.diagramQueryElementService.isDiagramEmpty(editingContext, diagramContext, previousDiagram, exposedElements);
    }

    /**
     *
     * {@link DiagramQueryElementService#shouldRenderConnectorEdge(Connector, org.eclipse.sirius.components.representations.Element, org.eclipse.sirius.components.representations.Element, DiagramRenderingCache, IEditingContext)}.
     */
    public boolean shouldRenderConnectorEdge(Connector connector, org.eclipse.sirius.components.representations.Element sourceNode,
            org.eclipse.sirius.components.representations.Element targetNode, DiagramRenderingCache cache, IEditingContext editingContext) {
        return this.diagramQueryElementService.shouldRenderConnectorEdge(connector, sourceNode, targetNode, cache, editingContext);
    }
}
