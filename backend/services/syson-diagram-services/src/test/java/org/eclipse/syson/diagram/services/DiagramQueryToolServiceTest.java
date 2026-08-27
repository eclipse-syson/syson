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
package org.eclipse.syson.diagram.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Edge;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.syson.sysml.AcceptActionUsage;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.StateSubactionMembership;
import org.eclipse.syson.sysml.StateUsage;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.UseCaseUsage;
import org.eclipse.syson.sysml.ViewDefinition;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.util.StandardDiagramsConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link DiagramQueryToolService}'s diagram-tool visibility and compartment-query decisions.
 * <p>
 * These tests exercise the service with SysML model objects and mocked Sirius Web lookup services. They protect the
 * expected availability of control-node tools and the interpretation of subject, state-subaction, receiver, payload,
 * and inherited-member compartments without requiring a running Sirius Web application.
 * </p>
 * <p>
 * They do not validate the AQL expressions that invoke this service, Spring dependency injection, or the rendering of
 * the resulting diagram tools and compartments. Those concerns belong to view-definition and integration tests.
 * </p>
 *
 * @author arichard
 */
class DiagramQueryToolServiceTest {

    @DisplayName("GIVEN selected nodes, WHEN checking a control-node action, THEN only action usages and definitions are accepted")
    @Test
    void testIsControlNodeActionCreationToolInAction() {
        IObjectSearchService objectSearchService = mock(IObjectSearchService.class);
        IEditingContext editingContext = mock(IEditingContext.class);
        Node selectedNode = mock(Node.class);
        when(selectedNode.getTargetObjectId()).thenReturn("selected-node");
        DiagramQueryToolService service = this.createService(objectSearchService);

        when(objectSearchService.getObject(editingContext, "selected-node")).thenReturn(Optional.of(SysmlFactory.eINSTANCE.createActionUsage()));
        assertThat(service.isControlNodeActionCreationToolInAction(editingContext, selectedNode)).isTrue();

        when(objectSearchService.getObject(editingContext, "selected-node")).thenReturn(Optional.of(SysmlFactory.eINSTANCE.createActionDefinition()));
        assertThat(service.isControlNodeActionCreationToolInAction(editingContext, selectedNode)).isTrue();

        when(objectSearchService.getObject(editingContext, "selected-node")).thenReturn(Optional.of(SysmlFactory.eINSTANCE.createPartUsage()));
        assertThat(service.isControlNodeActionCreationToolInAction(editingContext, selectedNode)).isFalse();

        when(objectSearchService.getObject(editingContext, "selected-node")).thenReturn(Optional.empty());
        assertThat(service.isControlNodeActionCreationToolInAction(editingContext, selectedNode)).isFalse();
    }

    /**
     * Verifies that connector palettes check their target node while ordinary node palettes keep edge tools available.
     */
    @DisplayName("GIVEN an edge-tool precondition, WHEN it is evaluated in connector and node palettes, THEN it checks only a connector target node")
    @Test
    void testIsTargetNodeOfType() {
        IObjectSearchService objectSearchService = mock(IObjectSearchService.class);
        IEditingContext editingContext = mock(IEditingContext.class);
        Node targetNode = mock(Node.class);
        when(targetNode.getTargetObjectId()).thenReturn("target-node");
        DiagramQueryToolService service = this.createService(objectSearchService);

        when(objectSearchService.getObject(editingContext, "target-node")).thenReturn(Optional.of(SysmlFactory.eINSTANCE.createPartUsage()));
        assertThat(service.isTargetNodeOfType(targetNode, editingContext, "PartUsage")).isTrue();
        assertThat(service.isTargetNodeOfType(targetNode, editingContext, "ActionUsage")).isFalse();
        assertThat(service.isTargetNodeOfType(SysmlFactory.eINSTANCE.createPartUsage(), editingContext, "ActionUsage")).isTrue();
        assertThat(service.isTargetNodeOfType(mock(Edge.class), editingContext, "PartUsage")).isFalse();
    }

    @DisplayName("GIVEN a requirement, WHEN checking its subject compartment, THEN its subject membership determines whether it is empty")
    @Test
    void testIsEmptySubjectCompartment() {
        DiagramQueryToolService service = this.createService(mock(IObjectSearchService.class));
        RequirementUsage requirementUsage = SysmlFactory.eINSTANCE.createRequirementUsage();

        assertThat(service.isEmptySubjectCompartment(requirementUsage)).isTrue();

        requirementUsage.getOwnedRelationship().add(SysmlFactory.eINSTANCE.createSubjectMembership());
        assertThat(service.isEmptySubjectCompartment(requirementUsage)).isFalse();
        assertThat(service.isEmptySubjectCompartment(SysmlFactory.eINSTANCE.createPartUsage())).isTrue();
    }

    @DisplayName("GIVEN a state, WHEN checking a subaction compartment, THEN a matching action kind makes it non-empty")
    @Test
    void testIsEmptyOfActionKindCompartment() {
        DiagramQueryToolService service = this.createService(mock(IObjectSearchService.class));
        StateUsage stateUsage = SysmlFactory.eINSTANCE.createStateUsage();

        assertThat(service.isEmptyOfActionKindCompartment(stateUsage, "entry")).isTrue();

        StateSubactionMembership membership = SysmlFactory.eINSTANCE.createStateSubactionMembership();
        membership.setKind(org.eclipse.syson.sysml.StateSubactionKind.ENTRY);
        stateUsage.getOwnedRelationship().add(membership);
        assertThat(service.isEmptyOfActionKindCompartment(stateUsage, "ENTRY")).isFalse();
        assertThat(service.isEmptyOfActionKindCompartment(SysmlFactory.eINSTANCE.createPartUsage(), "entry")).isTrue();
    }

    @DisplayName("GIVEN a use case, WHEN checking its objective compartment, THEN its objective membership determines whether it is empty")
    @Test
    void testIsEmptyObjectiveRequirementCompartment() {
        DiagramQueryToolService service = this.createService(mock(IObjectSearchService.class));
        UseCaseUsage useCaseUsage = SysmlFactory.eINSTANCE.createUseCaseUsage();

        assertThat(service.isEmptyObjectiveRequirementCompartment(useCaseUsage)).isTrue();

        useCaseUsage.getOwnedRelationship().add(SysmlFactory.eINSTANCE.createObjectiveMembership());
        assertThat(service.isEmptyObjectiveRequirementCompartment(useCaseUsage)).isFalse();
    }

    @DisplayName("GIVEN an accept action, WHEN checking receiver and payload compartments, THEN memberships and typings determine whether they are empty")
    @Test
    void testIsEmptyAcceptActionUsageCompartments() {
        DiagramQueryToolService service = this.createService(mock(IObjectSearchService.class));
        AcceptActionUsage acceptActionUsage = mock(AcceptActionUsage.class);

        assertThat(service.isEmptyAcceptActionUsageReceiver(SysmlFactory.eINSTANCE.createAcceptActionUsage())).isTrue();
        assertThat(service.isEmptyAcceptActionUsagePayload(SysmlFactory.eINSTANCE.createPartUsage())).isTrue();

        Expression receiver = mock(Expression.class);
        Membership receiverMembership = SysmlFactory.eINSTANCE.createMembership();
        when(acceptActionUsage.getReceiverArgument()).thenReturn(receiver);
        when(receiver.getOwnedRelationship()).thenReturn(new BasicEList<Relationship>(List.of(receiverMembership)));
        assertThat(service.isEmptyAcceptActionUsageReceiver(acceptActionUsage)).isTrue();
        receiverMembership.setMemberElement(SysmlFactory.eINSTANCE.createPartUsage());
        assertThat(service.isEmptyAcceptActionUsageReceiver(acceptActionUsage)).isFalse();

        ReferenceUsage payloadParameter = mock(ReferenceUsage.class);
        FeatureTyping featureTyping = mock(FeatureTyping.class);
        when(acceptActionUsage.getPayloadParameter()).thenReturn(payloadParameter);
        when(payloadParameter.getOwnedRelationship()).thenReturn(new BasicEList<Relationship>(List.of(featureTyping)));
        assertThat(service.isEmptyAcceptActionUsagePayload(acceptActionUsage)).isTrue();
        when(featureTyping.getType()).thenReturn(SysmlFactory.eINSTANCE.createPartDefinition());
        assertThat(service.isEmptyAcceptActionUsagePayload(acceptActionUsage)).isFalse();
    }

    @DisplayName("GIVEN inherited-members options, WHEN they are disabled, THEN no inherited feature is returned")
    @Test
    void testGetInheritedCompartmentItemsWithDisabledOptions() {
        IObjectSearchService objectSearchService = mock(IObjectSearchService.class);
        ShowDiagramsInheritedMembersService inheritedMembersService = new ShowDiagramsInheritedMembersService();
        inheritedMembersService.setShowInheritedMembers(false);
        inheritedMembersService.setShowInheritedMembersFromStandardLibraries(false);
        DiagramQueryToolService service = new DiagramQueryToolService(objectSearchService, inheritedMembersService);
        Type type = SysmlFactory.eINSTANCE.createPartDefinition();

        assertThat(service.getInheritedCompartmentItems(type, "ownedFeature")).isEmpty();
    }

    @DisplayName("GIVEN standard view kinds, WHEN checking tool availability, THEN each view enforces its semantic context rules")
    @Test
    void testToolShouldBeAvailableForStandardViews() {
        DiagramQueryToolService service = this.createService(mock(IObjectSearchService.class));

        assertThat(service.toolShouldBeAvailable(SysmlFactory.eINSTANCE.createPackage(), mock(IEditingContext.class), mock(DiagramContext.class),
                org.eclipse.syson.sysml.SysmlPackage.eINSTANCE.getPartUsage())).isTrue();
        assertThat(service.toolShouldBeAvailable(this.viewUsage(StandardDiagramsConstants.IV_QN, SysmlFactory.eINSTANCE.createPackage()), mock(IEditingContext.class),
                mock(DiagramContext.class), org.eclipse.syson.sysml.SysmlPackage.eINSTANCE.getAttributeUsage())).isFalse();
        assertThat(service.toolShouldBeAvailable(this.viewUsage(StandardDiagramsConstants.IV_QN, SysmlFactory.eINSTANCE.createPackage()), mock(IEditingContext.class),
                mock(DiagramContext.class), org.eclipse.syson.sysml.SysmlPackage.eINSTANCE.getPartUsage())).isTrue();
        assertThat(service.toolShouldBeAvailable(this.viewUsage(StandardDiagramsConstants.AFV_QN, SysmlFactory.eINSTANCE.createActionUsage()), mock(IEditingContext.class),
                mock(DiagramContext.class), org.eclipse.syson.sysml.SysmlPackage.eINSTANCE.getPartUsage())).isTrue();
        assertThat(service.toolShouldBeAvailable(this.viewUsage(StandardDiagramsConstants.AFV_QN, SysmlFactory.eINSTANCE.createActionDefinition()), mock(IEditingContext.class),
                mock(DiagramContext.class), org.eclipse.syson.sysml.SysmlPackage.eINSTANCE.getPartUsage())).isTrue();
        assertThat(service.toolShouldBeAvailable(this.viewUsage(StandardDiagramsConstants.AFV_QN, SysmlFactory.eINSTANCE.createPartUsage()), mock(IEditingContext.class),
                mock(DiagramContext.class), org.eclipse.syson.sysml.SysmlPackage.eINSTANCE.getActionDefinition())).isTrue();
        assertThat(service.toolShouldBeAvailable(this.viewUsage(StandardDiagramsConstants.STV_QN, SysmlFactory.eINSTANCE.createStateUsage()), mock(IEditingContext.class),
                mock(DiagramContext.class), org.eclipse.syson.sysml.SysmlPackage.eINSTANCE.getPartUsage())).isTrue();
        assertThat(service.toolShouldBeAvailable(this.viewUsage(StandardDiagramsConstants.STV_QN, SysmlFactory.eINSTANCE.createStateDefinition()), mock(IEditingContext.class),
                mock(DiagramContext.class), org.eclipse.syson.sysml.SysmlPackage.eINSTANCE.getPartUsage())).isTrue();
        assertThat(service.toolShouldBeAvailable(this.viewUsage(StandardDiagramsConstants.STV_QN, SysmlFactory.eINSTANCE.createPartUsage()), mock(IEditingContext.class),
                mock(DiagramContext.class), org.eclipse.syson.sysml.SysmlPackage.eINSTANCE.getStateDefinition())).isTrue();
    }

    @DisplayName("GIVEN an Action Flow View diagram, WHEN checking a background control-node tool, THEN its ViewUsage owner determines availability")
    @Test
    void testIsControlNodeActionCreationToolInsideActionOnAFV() {
        IObjectSearchService objectSearchService = mock(IObjectSearchService.class);
        IEditingContext editingContext = mock(IEditingContext.class);
        Diagram diagram = mock(Diagram.class);
        DiagramContext diagramContext = mock(DiagramContext.class);
        when(diagramContext.diagram()).thenReturn(diagram);
        when(diagram.getTargetObjectId()).thenReturn("diagram-target");
        ViewUsage actionFlowViewUsage = this.actionFlowViewUsage();
        when(objectSearchService.getObject(editingContext, "diagram-target")).thenReturn(Optional.of(actionFlowViewUsage));
        DiagramQueryToolService service = this.createService(objectSearchService);

        assertThat(service.isControlNodeActionCreationToolInsideActionOnAFV(this.viewUsage(StandardDiagramsConstants.AFV_QN, SysmlFactory.eINSTANCE.createActionUsage()),
                editingContext, diagramContext)).isTrue();
        assertThat(service.isControlNodeActionCreationToolInsideActionOnAFV(this.viewUsage(StandardDiagramsConstants.AFV_QN, SysmlFactory.eINSTANCE.createPartUsage()),
                editingContext, diagramContext)).isFalse();
        when(objectSearchService.getObject(editingContext, "diagram-target")).thenReturn(Optional.empty());
        assertThat(service.isControlNodeActionCreationToolInsideActionOnAFV(SysmlFactory.eINSTANCE.createActionUsage(), editingContext, diagramContext)).isFalse();
    }

    private DiagramQueryToolService createService(IObjectSearchService objectSearchService) {
        return new DiagramQueryToolService(objectSearchService, new ShowDiagramsInheritedMembersService());
    }

    private ViewUsage actionFlowViewUsage() {
        ViewDefinition viewDefinition = mock(ViewDefinition.class);
        when(viewDefinition.getQualifiedName()).thenReturn(StandardDiagramsConstants.AFV_QN);
        ViewUsage viewUsage = mock(ViewUsage.class);
        when(viewUsage.getType()).thenReturn(new BasicEList<Type>(List.of(viewDefinition)));
        return viewUsage;
    }

    private ViewUsage viewUsage(String qualifiedName, org.eclipse.syson.sysml.Element owner) {
        Type type = mock(Type.class);
        when(type.getQualifiedName()).thenReturn(qualifiedName);
        ViewUsage viewUsage = mock(ViewUsage.class);
        when(viewUsage.getType()).thenReturn(new BasicEList<Type>(List.of(type)));
        when(viewUsage.getOwner()).thenReturn(owner);
        return viewUsage;
    }
}
