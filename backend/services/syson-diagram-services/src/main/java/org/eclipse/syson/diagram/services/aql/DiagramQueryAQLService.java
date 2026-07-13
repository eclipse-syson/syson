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

import java.util.List;
import java.util.Objects;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.renderer.DiagramRenderingCache;
import org.eclipse.syson.diagram.services.DiagramQueryAnnotatingService;
import org.eclipse.syson.diagram.services.DiagramQueryElementService;
import org.eclipse.syson.diagram.services.DiagramQueryExposeService;
import org.eclipse.syson.diagram.services.DiagramQueryGraphicalService;
import org.eclipse.syson.diagram.services.DiagramQueryLabelService;
import org.eclipse.syson.diagram.services.DiagramQueryToolService;
import org.eclipse.syson.diagram.services.DiagramQueryViewService;
import org.eclipse.syson.sysml.Comment;
import org.eclipse.syson.sysml.ConnectionUsage;
import org.eclipse.syson.sysml.Connector;
import org.eclipse.syson.sysml.ControlNode;
import org.eclipse.syson.sysml.Dependency;
import org.eclipse.syson.sysml.Documentation;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.SatisfyRequirementUsage;
import org.eclipse.syson.sysml.TextualRepresentation;
import org.eclipse.syson.sysml.TransitionUsage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.ViewDefinition;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.util.StandardDiagramsConstants;

/**
 * Entry point for all diagram-related services doing queries in diagrams and called by AQL expressions in diagram
 * descriptions. This class is not a @Service class but act as it was, because it is called by IJavaServiceProvider.
 *
 * @author arichard
 */
public class DiagramQueryAQLService {

    private final DiagramQueryElementService diagramQueryElementService;

    private final DiagramQueryLabelService diagramQueryLabelService;

    private final DiagramQueryExposeService diagramQueryExposeService;

    private final DiagramQueryGraphicalService diagramQueryGraphicalService;

    private final DiagramQueryViewService diagramQueryViewService;

    private final DiagramQueryAnnotatingService diagramQueryAnnotatingService;

    private final DiagramQueryToolService diagramQueryToolService;

    /**
     * Creates a new diagram query AQL service.
     *
     * @param diagramQueryElementService
     *            the diagram element query service
     * @param diagramQueryLabelService
     *            the diagram label query service
     * @param diagramQueryExposeService
     *            the diagram exposed element query service
     * @param diagramQueryGraphicalService
     *            the diagram graphical query service
     * @param diagramQueryViewService
     *            the diagram view query service
     * @param diagramQueryAnnotatingService
     *            the diagram annotating query service
     * @param diagramQueryToolService
     *            the diagram tool query service
     */
    public DiagramQueryAQLService(DiagramQueryElementService diagramQueryElementService, DiagramQueryLabelService diagramQueryLabelService,
            DiagramQueryExposeService diagramQueryExposeService, DiagramQueryGraphicalService diagramQueryGraphicalService, DiagramQueryViewService diagramQueryViewService,
            DiagramQueryAnnotatingService diagramQueryAnnotatingService, DiagramQueryToolService diagramQueryToolService) {
        this.diagramQueryElementService = Objects.requireNonNull(diagramQueryElementService);
        this.diagramQueryLabelService = Objects.requireNonNull(diagramQueryLabelService);
        this.diagramQueryExposeService = Objects.requireNonNull(diagramQueryExposeService);
        this.diagramQueryGraphicalService = Objects.requireNonNull(diagramQueryGraphicalService);
        this.diagramQueryViewService = Objects.requireNonNull(diagramQueryViewService);
        this.diagramQueryAnnotatingService = Objects.requireNonNull(diagramQueryAnnotatingService);
        this.diagramQueryToolService = Objects.requireNonNull(diagramQueryToolService);
    }

    /**
     * {@link DiagramQueryElementService#canCreateFlowUsage(ConnectionUsage)}.
     */
    public boolean canCreateFlowUsage(ConnectionUsage connection) {
        return this.diagramQueryElementService.canCreateFlowUsage(connection);
    }

    /**
     * {@link DiagramQueryViewService#canCreateDiagram(Element)}.
     */
    public boolean canCreateDiagram(Element element) {
        return this.diagramQueryViewService.canCreateDiagram(element);
    }

    /**
     * {@link DiagramQueryLabelService#getBeginEdgeLabel(Element)}.
     */
    public String getBeginEdgeLabel(Element element) {
        return this.diagramQueryLabelService.getBeginEdgeLabel(element);
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
     * {@link DiagramQueryLabelService#getConnectionUsageLabel(ConnectionUsage)}.
     */
    public String getConnectionUsageLabel(ConnectionUsage element) {
        return this.diagramQueryLabelService.getConnectionUsageLabel(element);
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
     * {@link DiagramQueryExposeService#getExposedActors(Element, EClass, List, IEditingContext, DiagramContext)}.
     */
    public List<PartUsage> getExposedActors(Element element, EClass domainType, List<Object> ancestors, IEditingContext editingContext, DiagramContext diagramContext) {
        return this.diagramQueryExposeService.getExposedActors(element, domainType, ancestors, editingContext, diagramContext);
    }

    /**
     * {@link DiagramQueryExposeService#getExposedElements(Element, EClass, List, IEditingContext, DiagramContext)}.
     */
    public List<Element> getExposedElements(Element element, EClass domainType, List<Object> ancestors, IEditingContext editingContext, DiagramContext diagramContext) {
        return this.diagramQueryExposeService.getExposedElements(element, domainType, ancestors, editingContext, diagramContext);
    }

    /**
     * {@link DiagramQueryExposeService#getExposedElements(Element, Element, EClass, List, IEditingContext, DiagramContext)}.
     */
    public List<Element> getExposedElements(Element element, Element parent, EClass domainType, List<Object> ancestors, IEditingContext editingContext, DiagramContext diagramContext) {
        return this.diagramQueryExposeService.getExposedElements(element, parent, domainType, ancestors, editingContext, diagramContext);
    }

    /**
     * {@link DiagramQueryToolService#getInheritedCompartmentItems(Type, String)}.
     */
    public List<Feature> getInheritedCompartmentItems(Type type, String eReferenceName) {
        return this.diagramQueryToolService.getInheritedCompartmentItems(type, eReferenceName);
    }

    /**
     * {@link DiagramQueryExposeService#getExposedStakeholders(Element, EClass, List, IEditingContext, DiagramContext)}.
     */
    public List<PartUsage> getExposedStakeholders(Element element, EClass domainType, List<Object> ancestors, IEditingContext editingContext, DiagramContext diagramContext) {
        return this.diagramQueryExposeService.getExposedStakeholders(element, domainType, ancestors, editingContext, diagramContext);
    }

    /**
     * {@link DiagramQueryExposeService#getExposedSubjects(Element, EClass, List, IEditingContext, DiagramContext)}.
     */
    public List<ReferenceUsage> getExposedSubjects(Element element, EClass domainType, List<Object> ancestors, IEditingContext editingContext, DiagramContext diagramContext) {
        return this.diagramQueryExposeService.getExposedSubjects(element, domainType, ancestors, editingContext, diagramContext);
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
     * {@link DiagramQueryLabelService#getSatisfyLabel(SatisfyRequirementUsage)}.
     */
    public String getSatisfyLabel(SatisfyRequirementUsage satisfyRequirementUsage) {
        return this.diagramQueryLabelService.getSatisfyLabel(satisfyRequirementUsage);
    }

    /**
     * {@link DiagramQueryLabelService#getTransitionLabel(TransitionUsage)}.
     */
    public String getTransitionLabel(TransitionUsage transition) {
        return this.diagramQueryLabelService.getTransitionLabel(transition);
    }

    /**
     * {@link DiagramQueryElementService#infoMessage(Object, String)}.
     */
    public Object infoMessage(Object self, String message) {
        this.diagramQueryElementService.infoMessage(self, message);
        return self;
    }

    /**
     * {@link DiagramQueryToolService#isControlNodeActionCreationToolInAction(IEditingContext, Node)}.
     */
    public boolean isControlNodeActionCreationToolInAction(IEditingContext editingContext, Node selectedNode) {
        return this.diagramQueryToolService.isControlNodeActionCreationToolInAction(editingContext, selectedNode);
    }

    /**
     * {@link DiagramQueryToolService#isControlNodeActionCreationToolInsideActionOnAFV(Element, IEditingContext, DiagramContext)}.
     */
    public boolean isControlNodeActionCreationToolInsideActionOnAFV(Element element, IEditingContext editingContext, DiagramContext diagramContext) {
        return this.diagramQueryToolService.isControlNodeActionCreationToolInsideActionOnAFV(element, editingContext, diagramContext);
    }

    /**
     * {@link DiagramQueryToolService#isEmptyAcceptActionUsagePayload(Element)}.
     */
    public boolean isEmptyAcceptActionUsagePayload(Element element) {
        return this.diagramQueryToolService.isEmptyAcceptActionUsagePayload(element);
    }

    /**
     * {@link DiagramQueryToolService#isEmptyAcceptActionUsageReceiver(Element)}.
     */
    public boolean isEmptyAcceptActionUsageReceiver(Element element) {
        return this.diagramQueryToolService.isEmptyAcceptActionUsageReceiver(element);
    }

    /**
     * {@link DiagramQueryToolService#isEmptyObjectiveRequirementCompartment(Element)}.
     */
    public boolean isEmptyObjectiveRequirementCompartment(Element self) {
        return this.diagramQueryToolService.isEmptyObjectiveRequirementCompartment(self);
    }

    /**
     * {@link DiagramQueryToolService#isEmptyOfActionKindCompartment(Element, String)}.
     */
    public boolean isEmptyOfActionKindCompartment(Element self, String kind) {
        return this.diagramQueryToolService.isEmptyOfActionKindCompartment(self, kind);
    }

    /**
     * {@link DiagramQueryToolService#isEmptySubjectCompartment(Element)}.
     */
    public boolean isEmptySubjectCompartment(Element self) {
        return this.diagramQueryToolService.isEmptySubjectCompartment(self);
    }

    /**
     * {@link DiagramQueryElementService#isDiagramEmpty(editingContext, diagramContext, previousDiagram)}.
     */
    public boolean isDiagramEmpty(IEditingContext editingContext, DiagramContext diagramContext, Diagram previousDiagram, int exposedElements) {
        return this.diagramQueryElementService.isDiagramEmpty(editingContext, diagramContext, previousDiagram, exposedElements);
    }

    /**
     * {@link DiagramQueryViewService#isHiddenByDefault(Element, String, List, IEditingContext, DiagramContext)}.
     */
    public boolean isHiddenByDefault(Element self, String compartmentName, List<Object> ancestors, IEditingContext editingContext, DiagramContext diagramContext) {
        return this.diagramQueryViewService.isHiddenByDefault(self, compartmentName, ancestors, editingContext, diagramContext);
    }

    /**
     * AQL Service to control the default visibility of control nodes (Decision, Fork, Join, Merge) in diagrams (GV and
     * AFV).
     *
     * @param element
     *            the control node to display or not
     * @param ancestors
     *            the semantic ancestor of the given element
     * @return <code>true</code> if this node should be hidden by default or <code>false</code> if it should be
     *         displayed.
     */
    public boolean isHiddenControlNodeByDefault(Element element, List<Object> ancestors) {
        // A control node on the top of the GV diagram should be hidden by default
        // A control node on the top of the AFV diagram should be hidden by default if it has not been created on the
        // AFV background
        boolean result = false;
        if (element instanceof ControlNode controlNode && !ancestors.isEmpty() && ancestors.getFirst() instanceof ViewUsage viewUsage) {
            if (this.isGVDiagram(viewUsage)) {
                result = true;
            } else if (this.isAFVDiagram(viewUsage)) {
                Element owner = controlNode.getOwner();
                Element viewUsageOwner = viewUsage.getOwner();
                if (!Objects.equals(owner, viewUsageOwner)) {
                    result = true;
                }
            }
        }
        return result;
    }

    /**
     * {@link DiagramQueryGraphicalService#isInSameGraphicalContainer(org.eclipse.sirius.components.representations.Element, org.eclipse.sirius.components.representations.Element, DiagramRenderingCache)}.
     */
    public boolean isInSameGraphicalContainer(org.eclipse.sirius.components.representations.Element source,
            org.eclipse.sirius.components.representations.Element target, DiagramRenderingCache cache) {
        return this.diagramQueryGraphicalService.isInSameGraphicalContainer(source, target, cache);
    }

    /**
     * {@link DiagramQueryGraphicalService#isNotAncestorOf(org.eclipse.sirius.components.representations.Element, org.eclipse.sirius.components.representations.Element, DiagramRenderingCache)}.
     */
    public boolean isNotAncestorOf(org.eclipse.sirius.components.representations.Element parentNodeElement,
            org.eclipse.sirius.components.representations.Element childNodeElement, DiagramRenderingCache cache) {
        return this.diagramQueryGraphicalService.isNotAncestorOf(parentNodeElement, childNodeElement, cache);
    }

    /**
     * {@link DiagramQueryViewService#isView(Element, String, List, IEditingContext, DiagramContext)}.
     */
    public boolean isView(Element element, String viewDefinition, List<Object> ancestors, IEditingContext editingContext, DiagramContext diagramContext) {
        return this.diagramQueryViewService.isView(element, viewDefinition, ancestors, editingContext, diagramContext);
    }

    /**
     * {@link DiagramQueryViewService#isView(Element, String, Node, IEditingContext, DiagramContext)}.
     */
    public boolean isView(Element element, String viewDefinition, Node selectedNode, IEditingContext editingContext, DiagramContext diagramContext) {
        return this.diagramQueryViewService.isView(element, viewDefinition, selectedNode, editingContext, diagramContext);
    }

    /**
     * {@link DiagramQueryElementService#shouldRenderConnectorEdge(Connector, org.eclipse.sirius.components.representations.Element, org.eclipse.sirius.components.representations.Element, DiagramRenderingCache, IEditingContext)}.
     */
    public boolean shouldRenderConnectorEdge(Connector connector, org.eclipse.sirius.components.representations.Element sourceNode,
            org.eclipse.sirius.components.representations.Element targetNode, DiagramRenderingCache cache, IEditingContext editingContext) {
        return this.diagramQueryElementService.shouldRenderConnectorEdge(connector, sourceNode, targetNode, cache, editingContext);
    }

    /**
     * {@link DiagramQueryAnnotatingService#showAnnotatingNode(Element, DiagramContext, IEditingContext)}.
     */
    public boolean showAnnotatingNode(Element element, DiagramContext diagramContext, IEditingContext editingContext) {
        return this.diagramQueryAnnotatingService.showAnnotatingNode(element, diagramContext, editingContext);
    }

    /**
     * {@link DiagramQueryToolService#toolShouldBeAvailable(Element, IEditingContext, DiagramContext, EClass)}.
     */
    public boolean toolShouldBeAvailable(Element element, IEditingContext editingContext, DiagramContext diagramContext, EClass newElementType) {
        return this.diagramQueryToolService.toolShouldBeAvailable(element, editingContext, diagramContext, newElementType);
    }

    private boolean isAFVDiagram(ViewUsage viewUsage) {
        return this.isDiagram(viewUsage, StandardDiagramsConstants.AFV_QN);
    }

    private boolean isDiagram(ViewUsage viewUsage, String diagramQualifiedName) {
        EList<Type> viewUsageType = viewUsage.getType();
        if (viewUsageType != null && !viewUsageType.isEmpty()) {
            Type type = viewUsageType.getFirst();
            return type instanceof ViewDefinition && diagramQualifiedName.equals(type.getQualifiedName());
        }
        return false;
    }

    private boolean isGVDiagram(ViewUsage viewUsage) {
        return this.isDiagram(viewUsage, StandardDiagramsConstants.GV_QN);
    }
}
