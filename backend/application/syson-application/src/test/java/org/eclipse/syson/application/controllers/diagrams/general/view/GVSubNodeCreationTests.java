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
package org.eclipse.syson.application.controllers.diagrams.general.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.sirius.components.diagrams.tests.assertions.DiagramAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.core.api.IObjectService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.controller.editingContext.checkers.CheckElementInParent;
import org.eclipse.syson.application.controller.editingContext.checkers.ISemanticChecker;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckChildNode;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckNodeInCompartment;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckNodeOnDiagram;
import org.eclipse.syson.application.controllers.diagrams.checkers.IDiagramChecker;
import org.eclipse.syson.application.controllers.diagrams.testers.NodeCreationTester;
import org.eclipse.syson.application.controllers.utils.TestNameGenerator;
import org.eclipse.syson.application.data.SysMLv2Identifiers;
import org.eclipse.syson.diagram.general.view.GVDescriptionNameGenerator;
import org.eclipse.syson.services.SemanticCheckerFactory;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramReference;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.sysml.AcceptActionUsage;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.helper.EMFUtils;
import org.eclipse.syson.sysml.helper.LabelConstants;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;

import reactor.test.StepVerifier;
import reactor.test.StepVerifier.Step;

/**
 * Tests the creation of sub nodes in the General View diagram.
 *
 * @author gdaniel
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVSubNodeCreationTests extends AbstractIntegrationTests {

    private static final String ATTRIBUTES_COMPARTMENT = "attributes";

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenDiagramReference givenDiagram;

    @Autowired
    private IGivenDiagramDescription givenDiagramDescription;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    @Autowired
    private IDiagramIdProvider diagramIdProvider;

    @Autowired
    private IObjectService objectService;

    @Autowired
    private NodeCreationTester nodeCreationTester;

    @Autowired
    private SemanticCheckerFactory semanticCheckerFactory;

    @Autowired
    private DiagramComparator diagramComparator;

    private DiagramDescriptionIdProvider diagramDescriptionIdProvider;

    private Step<DiagramRefreshedEventPayload> verifier;

    private AtomicReference<Diagram> diagram;

    private DiagramDescription diagramDescription;

    private final IDescriptionNameGenerator descriptionNameGenerator = new GVDescriptionNameGenerator();

    private static Stream<Arguments> attributeDefinitionSubNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getAttributeUsage(), ATTRIBUTES_COMPARTMENT, SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute()))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> attributeUsageSubNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getAttributeUsage(), ATTRIBUTES_COMPARTMENT, SysmlPackage.eINSTANCE.getUsage_NestedAttribute()),
                Arguments.of(SysmlPackage.eINSTANCE.getReferenceUsage(), "references", SysmlPackage.eINSTANCE.getUsage_NestedReference()))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> enumerationDefinitionSubNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getEnumerationUsage(), "enumerations", SysmlPackage.eINSTANCE.getEnumerationDefinition_EnumeratedValue()))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> itemDefinitionSiblingNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getItemUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedUsage(), 3))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> itemDefinitionSubNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getAttributeUsage(), ATTRIBUTES_COMPARTMENT, SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute()))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> itemUsageSiblingNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getItemUsage(), SysmlPackage.eINSTANCE.getUsage_NestedItem(), 3),
                Arguments.of(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedPart(), 3),
                Arguments.of(SysmlPackage.eINSTANCE.getPortUsage(), SysmlPackage.eINSTANCE.getUsage_NestedPort(), 3))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> itemUsageSubNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getAttributeUsage(), ATTRIBUTES_COMPARTMENT, SysmlPackage.eINSTANCE.getUsage_NestedAttribute()),
                Arguments.of(SysmlPackage.eINSTANCE.getReferenceUsage(), "references", SysmlPackage.eINSTANCE.getUsage_NestedReference()))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> packageSubNodeParameters() {
        EReference ownedMember = SysmlPackage.eINSTANCE.getNamespace_OwnedMember();
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getAttributeUsage(), ownedMember, 3),
                Arguments.of(SysmlPackage.eINSTANCE.getAttributeDefinition(), ownedMember, 2),
                Arguments.of(SysmlPackage.eINSTANCE.getEnumerationDefinition(), ownedMember, 2),
                Arguments.of(SysmlPackage.eINSTANCE.getItemUsage(), ownedMember, 3),
                Arguments.of(SysmlPackage.eINSTANCE.getItemDefinition(), ownedMember, 2),
                // A package doesn't have a compartment: it is handled as a custom node
                Arguments.of(SysmlPackage.eINSTANCE.getPackage(), ownedMember, 0),
                Arguments.of(SysmlPackage.eINSTANCE.getPartUsage(), ownedMember, 3),
                Arguments.of(SysmlPackage.eINSTANCE.getPartDefinition(), ownedMember, 3),
                Arguments.of(SysmlPackage.eINSTANCE.getAllocationUsage(), ownedMember, 2),
                Arguments.of(SysmlPackage.eINSTANCE.getAllocationDefinition(), ownedMember, 1),
                Arguments.of(SysmlPackage.eINSTANCE.getInterfaceUsage(), ownedMember, 3),
                Arguments.of(SysmlPackage.eINSTANCE.getInterfaceDefinition(), ownedMember, 4),
                Arguments.of(SysmlPackage.eINSTANCE.getPortUsage(), ownedMember, 3),
                Arguments.of(SysmlPackage.eINSTANCE.getPortDefinition(), ownedMember, 4),
                Arguments.of(SysmlPackage.eINSTANCE.getAcceptActionUsage(), ownedMember, 1),
                Arguments.of(SysmlPackage.eINSTANCE.getActionUsage(), ownedMember, 4),
                Arguments.of(SysmlPackage.eINSTANCE.getActionDefinition(), ownedMember, 3),
                Arguments.of(SysmlPackage.eINSTANCE.getAssignmentActionUsage(), ownedMember, 0),
                Arguments.of(SysmlPackage.eINSTANCE.getConstraintUsage(), ownedMember, 2),
                Arguments.of(SysmlPackage.eINSTANCE.getConstraintDefinition(), ownedMember, 2),
                Arguments.of(SysmlPackage.eINSTANCE.getRequirementUsage(), ownedMember, 5),
                Arguments.of(SysmlPackage.eINSTANCE.getRequirementDefinition(), ownedMember, 5),
                Arguments.of(SysmlPackage.eINSTANCE.getUseCaseUsage(), ownedMember, 2),
                Arguments.of(SysmlPackage.eINSTANCE.getUseCaseDefinition(), ownedMember, 2),
                Arguments.of(SysmlPackage.eINSTANCE.getOccurrenceUsage(), ownedMember, 2),
                Arguments.of(SysmlPackage.eINSTANCE.getOccurrenceDefinition(), ownedMember, 3),
                Arguments.of(SysmlPackage.eINSTANCE.getMetadataDefinition(), ownedMember, 3),
                Arguments.of(SysmlPackage.eINSTANCE.getStateUsage(), ownedMember, 3),
                Arguments.of(SysmlPackage.eINSTANCE.getStateDefinition(), ownedMember, 3))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> partDefinitionSubNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getAttributeUsage(), ATTRIBUTES_COMPARTMENT, SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute()),
                Arguments.of(SysmlPackage.eINSTANCE.getPortUsage(), "ports", SysmlPackage.eINSTANCE.getDefinition_OwnedPort()))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> partDefinitionSiblingNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getItemUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedItem(), 3),
                Arguments.of(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedPart(), 3))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> partUsageSubNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getAttributeUsage(), ATTRIBUTES_COMPARTMENT, SysmlPackage.eINSTANCE.getUsage_NestedAttribute()),
                Arguments.of(SysmlPackage.eINSTANCE.getPortUsage(), "ports", SysmlPackage.eINSTANCE.getUsage_NestedPort()))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> partUsageSiblingNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getItemUsage(), SysmlPackage.eINSTANCE.getUsage_NestedItem(), 3),
                Arguments.of(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedUsage(), 3))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> allocationUsageSiblingNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getItemUsage(), SysmlPackage.eINSTANCE.getUsage_NestedItem(), 3),
                Arguments.of(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedUsage(), 3))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> allocationUsageSubNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getAllocationUsage(), "allocations", SysmlPackage.eINSTANCE.getUsage_NestedAllocation()))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> allocationDefinitionSiblingNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getItemUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedItem(), 3),
                Arguments.of(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedPart(), 3))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> interfaceUsageSiblingNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getItemUsage(), SysmlPackage.eINSTANCE.getUsage_NestedItem(), 3),
                Arguments.of(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedUsage(), 3))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> interfaceUsageSubNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getAttributeUsage(), ATTRIBUTES_COMPARTMENT, SysmlPackage.eINSTANCE.getUsage_NestedAttribute()),
                Arguments.of(SysmlPackage.eINSTANCE.getPortUsage(), "ports", SysmlPackage.eINSTANCE.getUsage_NestedPort()))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> interfaceDefinitionSubNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getAttributeUsage(), ATTRIBUTES_COMPARTMENT, SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute()),
                Arguments.of(SysmlPackage.eINSTANCE.getInterfaceUsage(), "interfaces", SysmlPackage.eINSTANCE.getDefinition_OwnedInterface()),
                Arguments.of(SysmlPackage.eINSTANCE.getPortUsage(), "ports", SysmlPackage.eINSTANCE.getDefinition_OwnedPort()))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> portUsageSiblingNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getPortUsage(), SysmlPackage.eINSTANCE.getUsage_NestedPort(), 3))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> portUsageSubNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getAttributeUsage(), ATTRIBUTES_COMPARTMENT, SysmlPackage.eINSTANCE.getUsage_NestedAttribute()),
                Arguments.of(SysmlPackage.eINSTANCE.getReferenceUsage(), "references", SysmlPackage.eINSTANCE.getUsage_NestedReference()))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> portDefinitionSiblingNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedPart(), 3))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> portDefinitionSubNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getAttributeUsage(), ATTRIBUTES_COMPARTMENT, SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute()),
                Arguments.of(SysmlPackage.eINSTANCE.getPortUsage(), "ports", SysmlPackage.eINSTANCE.getDefinition_OwnedPort()),
                Arguments.of(SysmlPackage.eINSTANCE.getReferenceUsage(), "references", SysmlPackage.eINSTANCE.getDefinition_OwnedReference()))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> acceptActionUsagePayloadParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getPartDefinition()),
                Arguments.of(SysmlPackage.eINSTANCE.getItemDefinition()))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> actionUsageSiblingNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedPart(), 3),
                Arguments.of(SysmlPackage.eINSTANCE.getAttributeUsage(), SysmlPackage.eINSTANCE.getUsage_NestedAttribute(), 3),
                Arguments.of(SysmlPackage.eINSTANCE.getPortUsage(), SysmlPackage.eINSTANCE.getUsage_NestedPort(), 3))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> actionUsageListNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getItemUsage(), "items", SysmlPackage.eINSTANCE.getUsage_NestedItem()))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> actionUsageListAndFreeFormNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getAcceptActionUsage(), "actions", SysmlPackage.eINSTANCE.getUsage_NestedAction(), 1),
                Arguments.of(SysmlPackage.eINSTANCE.getActionUsage(), "actions", SysmlPackage.eINSTANCE.getUsage_NestedAction(), 4))
                // Uncomment when https://github.com/eclipse-sirius/sirius-web/issues/3650 is fixed
                // Arguments.of(SysmlPackage.eINSTANCE.getDecisionNode(), "actions",
                // SysmlPackage.eINSTANCE.getUsage_NestedAction(), 0),
                // Arguments.of(SysmlPackage.eINSTANCE.getForkNode(), "actions",
                // SysmlPackage.eINSTANCE.getUsage_NestedAction(), 0),
                // Arguments.of(SysmlPackage.eINSTANCE.getJoinNode(), "actions",
                // SysmlPackage.eINSTANCE.getUsage_NestedAction(), 0),
                // Arguments.of(SysmlPackage.eINSTANCE.getMergeNode(), "actions",
                // SysmlPackage.eINSTANCE.getUsage_NestedAction(), 0))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> actionDefinitionListAndFreeFormNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getAcceptActionUsage(), "actions", SysmlPackage.eINSTANCE.getDefinition_OwnedAction(), 1),
                Arguments.of(SysmlPackage.eINSTANCE.getActionUsage(), "actions", SysmlPackage.eINSTANCE.getDefinition_OwnedAction(), 4))
                // Uncomment when https://github.com/eclipse-sirius/sirius-web/issues/3650 is fixed
                // Arguments.of(SysmlPackage.eINSTANCE.getDecisionNode(), "actions",
                // SysmlPackage.eINSTANCE.getDefinition_OwnedAction(), 0),
                // Arguments.of(SysmlPackage.eINSTANCE.getForkNode(), "actions",
                // SysmlPackage.eINSTANCE.getDefinition_OwnedAction(), 0),
                // Arguments.of(SysmlPackage.eINSTANCE.getJoinNode(), "actions",
                // SysmlPackage.eINSTANCE.getDefinition_OwnedAction(), 0),
                // Arguments.of(SysmlPackage.eINSTANCE.getMergeNode(), "actions",
                // SysmlPackage.eINSTANCE.getDefinition_OwnedAction(), 0))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> constraintUsageSiblingNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getAttributeUsage(), SysmlPackage.eINSTANCE.getUsage_NestedAttribute(), 3),
                Arguments.of(SysmlPackage.eINSTANCE.getItemUsage(), SysmlPackage.eINSTANCE.getUsage_NestedItem(), 3),
                Arguments.of(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedPart(), 3),
                Arguments.of(SysmlPackage.eINSTANCE.getPortUsage(), SysmlPackage.eINSTANCE.getUsage_NestedPort(), 3))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> constraintUsageChildNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getConstraintUsage(), "constraints", SysmlPackage.eINSTANCE.getUsage_NestedConstraint()))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> constraintDefinitionSiblingNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedUsage(), 3))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> constraintDefinitionChildNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getConstraintUsage(), "constraints", SysmlPackage.eINSTANCE.getDefinition_OwnedConstraint()))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> requirementUsageSiblingNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getItemUsage(), SysmlPackage.eINSTANCE.getUsage_NestedItem(), 3),
                Arguments.of(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedPart(), 3),
                Arguments.of(SysmlPackage.eINSTANCE.getPortUsage(), SysmlPackage.eINSTANCE.getUsage_NestedPort(), 3),
                Arguments.of(SysmlPackage.eINSTANCE.getRequirementUsage(), SysmlPackage.eINSTANCE.getUsage_NestedRequirement(), 5))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> requirementUsageChildNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getAttributeUsage(), ATTRIBUTES_COMPARTMENT, SysmlPackage.eINSTANCE.getUsage_NestedAttribute()))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> requirementDefinitionChildNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getAttributeUsage(), ATTRIBUTES_COMPARTMENT, SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute()))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> useCaseUsageSiblingNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getAttributeUsage(), SysmlPackage.eINSTANCE.getUsage_NestedAttribute(), 3),
                Arguments.of(SysmlPackage.eINSTANCE.getItemUsage(), SysmlPackage.eINSTANCE.getUsage_NestedItem(), 3),
                Arguments.of(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedPart(), 3),
                Arguments.of(SysmlPackage.eINSTANCE.getPortUsage(), SysmlPackage.eINSTANCE.getUsage_NestedPort(), 3))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> occurrenceUsageChildNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getOccurrenceUsage(), "occurrences", SysmlPackage.eINSTANCE.getUsage_NestedOccurrence()))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> occurrenceDefinitionSiblingNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedPart(), 3))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> occurrenceDefinitionChildNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getAttributeUsage(), ATTRIBUTES_COMPARTMENT, SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute()),
                Arguments.of(SysmlPackage.eINSTANCE.getOccurrenceUsage(), "occurrences", SysmlPackage.eINSTANCE.getDefinition_OwnedOccurrence()))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> metadataDefinitionSiblingNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getItemUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedItem(), 3))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> metadataDefinitionChildNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getAttributeUsage(), ATTRIBUTES_COMPARTMENT, SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute()),
                Arguments.of(SysmlPackage.eINSTANCE.getReferenceUsage(), "references", SysmlPackage.eINSTANCE.getDefinition_OwnedReference()))
                .map(TestNameGenerator::namedArguments);
    }

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                SysMLv2Identifiers.GENERAL_VIEW_WITH_TOP_NODES_PROJECT,
                SysMLv2Identifiers.GENERAL_VIEW_WITH_TOP_NODES_DIAGRAM);
        var flux = this.givenDiagramSubscription.subscribe(diagramEventInput);
        this.verifier = StepVerifier.create(flux);
        this.diagram = this.givenDiagram.getDiagram(this.verifier);
        this.diagramDescription = this.givenDiagramDescription.getDiagramDescription(SysMLv2Identifiers.GENERAL_VIEW_WITH_TOP_NODES_PROJECT,
                SysMLv2Identifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        this.diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(this.diagramDescription, this.diagramIdProvider);
    }

    @AfterEach
    public void tearDown() {
        if (this.verifier != null) {
            this.verifier.thenCancel()
                    .verify(Duration.ofSeconds(10));
        }
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("attributeDefinitionSubNodeParameters")
    public void createAttributeDefinitionSubNodes(EClass childEClass, String compartmentName, EReference containmentReference) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getAttributeDefinition();
        String parentLabel = "AttributeDefinition";
        this.createNode(parentEClass, parentLabel, childEClass);
        this.checkDiagram(this.getCompartmentNodeGraphicalChecker(parentLabel, parentEClass, containmentReference, compartmentName));
        this.checkEditingContext(this.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("attributeUsageSubNodeParameters")
    public void createAttributeUsageSubNodes(EClass childEClass, String compartmentName, EReference containmentReference) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getAttributeUsage();
        String parentLabel = "attribute";
        this.createNode(parentEClass, parentLabel, childEClass);
        this.checkDiagram(this.getCompartmentNodeGraphicalChecker(parentLabel, parentEClass, containmentReference, compartmentName));
        this.checkEditingContext(this.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("enumerationDefinitionSubNodeParameters")
    public void createEnumerationDefinitionSubNodes(EClass childEClass, String compartmentName, EReference containmentReference) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getEnumerationDefinition();
        String parentLabel = "EnumerationDefinition";
        this.createNode(parentEClass, parentLabel, childEClass);
        this.checkDiagram(this.getCompartmentNodeGraphicalChecker(parentLabel, parentEClass, containmentReference, compartmentName));
        this.checkEditingContext(this.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("itemDefinitionSiblingNodeParameters")
    public void createItemDefinitionSiblingNodes(EClass childEClass, EReference containmentReference, int compartmentCount) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getItemDefinition();
        String parentLabel = "ItemDefinition";
        this.createNode(parentEClass, parentLabel, childEClass);
        this.checkDiagram(this.getSiblingNodeGraphicalChecker(childEClass, compartmentCount));
        this.checkEditingContext(this.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("itemDefinitionSubNodeParameters")
    public void createItemDefinitionSubNodes(EClass childEClass, String compartmentName, EReference containmentReference) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getItemDefinition();
        String parentLabel = "ItemDefinition";
        this.createNode(parentEClass, parentLabel, childEClass);
        this.checkDiagram(this.getCompartmentNodeGraphicalChecker(parentLabel, parentEClass, containmentReference, compartmentName));
        this.checkEditingContext(this.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("itemUsageSiblingNodeParameters")
    public void createItemUsageSiblingNodes(EClass childEClass, EReference containmentReference, int compartmentCount) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getItemUsage();
        String parentLabel = "item";
        this.createNode(parentEClass, parentLabel, childEClass);
        this.checkDiagram(this.getSiblingNodeGraphicalChecker(childEClass, compartmentCount));
        this.checkEditingContext(this.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("itemUsageSubNodeParameters")
    public void createItemUsageSubNodes(EClass childEClass, String compartmentName, EReference containmentReference) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getItemUsage();
        String parentLabel = "item";
        this.createNode(parentEClass, parentLabel, childEClass);
        this.checkDiagram(this.getCompartmentNodeGraphicalChecker(parentLabel, parentEClass, containmentReference, compartmentName));
        this.checkEditingContext(this.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("packageSubNodeParameters")
    public void createPackageSubNodes(EClass childEClass, EReference containmentReference, int compartmentCount) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getPackage();
        String parentLabel = "Package";
        this.createNode(parentEClass, parentLabel, childEClass);
        this.checkDiagram(this.getChildNodeGraphicalChecker(parentLabel, childEClass, compartmentCount));
        this.checkEditingContext(this.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("partDefinitionSubNodeParameters")
    public void createPartDefinitionSubNodes(EClass childEClass, String compartmentName, EReference containmentReference) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getPartDefinition();
        String parentLabel = "PartDefinition";
        this.createNode(parentEClass, parentLabel, childEClass);
        this.checkDiagram(this.getCompartmentNodeGraphicalChecker(parentLabel, parentEClass, containmentReference, compartmentName));
        this.checkEditingContext(this.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("partDefinitionSiblingNodeParameters")
    public void createPartDefinitionSiblingNodes(EClass childEClass, EReference containmentReference, int compartmentCount) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getPartDefinition();
        String parentLabel = "PartDefinition";
        this.createNode(parentEClass, parentLabel, childEClass);
        this.checkDiagram(this.getSiblingNodeGraphicalChecker(childEClass, compartmentCount));
        this.checkEditingContext(this.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("partUsageSubNodeParameters")
    public void createPartUsageSubNodes(EClass childEClass, String compartmentName, EReference containmentReference) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getPartUsage();
        String parentLabel = "part";
        this.createNode(parentEClass, parentLabel, childEClass);
        this.checkDiagram(this.getCompartmentNodeGraphicalChecker(parentLabel, parentEClass, containmentReference, compartmentName));
        this.checkEditingContext(this.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("partUsageSiblingNodeParameters")
    public void createPartUsageSiblingNodes(EClass childEClass, EReference containmentReference, int compartmentCount) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getPartUsage();
        String parentLabel = "part";
        this.createNode(parentEClass, parentLabel, childEClass);
        this.checkDiagram(this.getSiblingNodeGraphicalChecker(childEClass, compartmentCount));
        this.checkEditingContext(this.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("allocationUsageSiblingNodeParameters")
    public void createAllocationUsageSiblingNodes(EClass childEClass, EReference containmentReference, int compartmentCount) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getAllocationUsage();
        String parentLabel = "allocation";
        this.createNode(parentEClass, parentLabel, childEClass);
        this.checkDiagram(this.getSiblingNodeGraphicalChecker(childEClass, compartmentCount));
        this.checkEditingContext(this.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("allocationUsageSubNodeParameters")
    public void createAllocationUsageSubNodes(EClass childEClass, String compartmentName, EReference containmentReference) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getAllocationUsage();
        String parentLabel = "allocation";
        this.createNode(parentEClass, parentLabel, childEClass);
        this.checkDiagram(this.getCompartmentNodeGraphicalChecker(parentLabel, parentEClass, containmentReference, compartmentName));
        this.checkEditingContext(this.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("allocationDefinitionSiblingNodeParameters")
    public void createAllocationDefinitionSiblingNodes(EClass childEClass, EReference containmentReference, int compartmentCount) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getAllocationDefinition();
        String parentLabel = "AllocationDefinition";
        this.createNode(parentEClass, parentLabel, childEClass);
        this.checkDiagram(this.getSiblingNodeGraphicalChecker(childEClass, compartmentCount));
        this.checkEditingContext(this.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("interfaceUsageSiblingNodeParameters")
    public void createInterfaceUsageSiblingNodes(EClass childEClass, EReference containmentReference, int compartmentCount) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getInterfaceUsage();
        String parentLabel = "interface";
        this.createNode(parentEClass, parentLabel, childEClass);
        this.checkDiagram(this.getSiblingNodeGraphicalChecker(childEClass, compartmentCount));
        this.checkEditingContext(this.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("interfaceUsageSubNodeParameters")
    public void createInterfaceUsageSubNodes(EClass childEClass, String compartmentName, EReference containmentReference) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getInterfaceUsage();
        String parentLabel = "interface";
        this.createNode(parentEClass, parentLabel, childEClass);
        this.checkDiagram(this.getCompartmentNodeGraphicalChecker(parentLabel, parentEClass, containmentReference, compartmentName));
        this.checkEditingContext(this.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("interfaceDefinitionSubNodeParameters")
    public void createInterfaceDefinitionSubNodes(EClass childEClass, String compartmentName, EReference containmentReference) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getInterfaceDefinition();
        String parentLabel = "InterfaceDefinition";
        this.createNode(parentEClass, parentLabel, childEClass);
        this.checkDiagram(this.getCompartmentNodeGraphicalChecker(parentLabel, parentEClass, containmentReference, compartmentName));
        this.checkEditingContext(this.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("portUsageSiblingNodeParameters")
    public void createPortUsageSiblingNodes(EClass childEClass, EReference containmentReference, int compartmentCount) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getPortUsage();
        String parentLabel = "port";
        this.createNode(parentEClass, parentLabel, childEClass);
        this.checkDiagram(this.getSiblingNodeGraphicalChecker(childEClass, compartmentCount));
        this.checkEditingContext(this.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("portUsageSubNodeParameters")
    public void createPortUsageSubNodes(EClass childEClass, String compartmentName, EReference containmentReference) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getPortUsage();
        String parentLabel = "port";
        this.createNode(parentEClass, parentLabel, childEClass);
        this.checkDiagram(this.getCompartmentNodeGraphicalChecker(parentLabel, parentEClass, containmentReference, compartmentName));
        this.checkEditingContext(this.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("portDefinitionSiblingNodeParameters")
    public void createPortDefinitionSiblingNodes(EClass childEClass, EReference containmentReference, int compartmentCount) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getPortDefinition();
        String parentLabel = "PortDefinition";
        this.createNode(parentEClass, parentLabel, childEClass);
        this.checkDiagram(this.getSiblingNodeGraphicalChecker(childEClass, compartmentCount));
        this.checkEditingContext(this.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("portDefinitionSubNodeParameters")
    public void createPortDefinitionSubNodes(EClass childEClass, String compartmentName, EReference containmentReference) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getPortDefinition();
        String parentLabel = "PortDefinition";
        this.createNode(parentEClass, parentLabel, childEClass);
        this.checkDiagram(this.getCompartmentNodeGraphicalChecker(parentLabel, parentEClass, containmentReference, compartmentName));
        this.checkEditingContext(this.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("acceptActionUsagePayloadParameters")
    public void createAcceptActionUsagePayload(EClass eClass) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getAcceptActionUsage();
        String parentLabel = "acceptAction";

        this.createNode(parentEClass, parentLabel, this.descriptionNameGenerator.getCreationToolName("New {0} as Payload", eClass));

        IDiagramChecker diagramChecker = (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(0)
                    .hasNewEdgeCount(0)
                    .check(initialDiagram, newDiagram);

            DiagramNavigator diagramNavigator = new DiagramNavigator(newDiagram);
            Node updatedNode = diagramNavigator.nodeWithTargetObjectLabel(parentLabel).getNode();
            assertThat(updatedNode).as("The updated node should exist").isNotNull();
            assertThat(updatedNode.getInsideLabel()).as("The updated node label should exist").isNotNull();
            assertThat(updatedNode.getInsideLabel().getText())
                    .contains(LabelConstants.OPEN_QUOTE + "accept" + LabelConstants.CLOSE_QUOTE)
                    .contains("payload: acceptActionPayloadType");
        };

        this.checkDiagram(diagramChecker);

        ISemanticChecker semanticChecker = (editingContext) -> {
            Object semanticRootObject = this.objectService.getObject(editingContext, SysMLv2Identifiers.GENERAL_VIEW_WITH_TOP_NODES_DIAGRAM_OBJECT).orElse(null);
            assertThat(semanticRootObject).isInstanceOf(Element.class);
            Element semanticRootElement = (Element) semanticRootObject;
            Optional<Element> optParentElement = EMFUtils.allContainedObjectOfType(semanticRootElement, Element.class)
                    .filter(element -> Objects.equals(element.getName(), parentLabel))
                    .findFirst();
            assertThat(optParentElement).isPresent();
            AcceptActionUsage acceptActionUsage = (AcceptActionUsage) optParentElement.get();
            var referenceUsage = acceptActionUsage.getPayloadParameter();
            var relationship = referenceUsage.getOwnedRelationship().get(0);
            assertThat(relationship).isInstanceOf(FeatureTyping.class);
            FeatureTyping featureTyping = (FeatureTyping) relationship;
            assertThat(eClass.isInstance(featureTyping.getType()));
            assertThat(featureTyping.getType().getName()).isEqualTo("acceptActionPayloadType");
        };

        this.checkEditingContext(semanticChecker);
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void createAcceptActionUsageReceiver() {
        EClass parentEClass = SysmlPackage.eINSTANCE.getAcceptActionUsage();
        String parentLabel = "acceptAction";
        EClass eClass = SysmlPackage.eINSTANCE.getPortUsage();
        this.createNode(parentEClass, parentLabel, this.descriptionNameGenerator.getCreationToolName("New {0} as Receiver", eClass));

        IDiagramChecker diagramChecker = (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(0)
                    .hasNewEdgeCount(0)
                    .check(initialDiagram, newDiagram);

            DiagramNavigator diagramNavigator = new DiagramNavigator(newDiagram);
            Node updatedNode = diagramNavigator.nodeWithTargetObjectLabel(parentLabel).getNode();
            assertThat(updatedNode).as("The updated node should exist").isNotNull();
            assertThat(updatedNode.getInsideLabel()).as("The updated node label should exist").isNotNull();
            assertThat(updatedNode.getInsideLabel().getText())
                    .contains(LabelConstants.OPEN_QUOTE + "via" + LabelConstants.CLOSE_QUOTE)
                    .contains("acceptAction's receiver");
        };

        this.checkDiagram(diagramChecker);

        ISemanticChecker semanticChecker = (editingContext) -> {
            Object semanticRootObject = this.objectService.getObject(editingContext, SysMLv2Identifiers.GENERAL_VIEW_WITH_TOP_NODES_DIAGRAM_OBJECT).orElse(null);
            assertThat(semanticRootObject).isInstanceOf(Element.class);
            Element semanticRootElement = (Element) semanticRootObject;
            Optional<Element> optParentElement = EMFUtils.allContainedObjectOfType(semanticRootElement, Element.class)
                    .filter(element -> Objects.equals(element.getName(), parentLabel))
                    .findFirst();
            assertThat(optParentElement).isPresent();
            AcceptActionUsage acceptActionUsage = (AcceptActionUsage) optParentElement.get();
            var receiverExpression = acceptActionUsage.getReceiverArgument();
            var relationship = receiverExpression.getOwnedRelationship().get(0);
            assertThat(relationship).isInstanceOf(Membership.class);
            Membership membership = (Membership) relationship;
            assertThat(eClass.isInstance(membership.getMemberElement())).isTrue();
            assertThat(membership.getMemberElement().getName()).isEqualTo("acceptAction's receiver");
        };

        this.checkEditingContext(semanticChecker);
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("actionUsageSiblingNodeParameters")
    public void createActionUsageSiblingNodes(EClass childEClass, EReference containmentReference, int compartmentCount) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getActionUsage();
        String parentLabel = "action";
        this.createNode(parentEClass, parentLabel, childEClass);
        final IDiagramChecker diagramChecker;
        if (SysmlPackage.eINSTANCE.getPartUsage().equals(childEClass)) {
            diagramChecker = (initialDiagram, newDiagram) -> {
                int createdNodesExpectedCount = 2 + compartmentCount;
                new CheckDiagramElementCount(this.diagramComparator)
                        .hasNewNodeCount(createdNodesExpectedCount)
                        .hasNewEdgeCount(1)
                        .check(initialDiagram, newDiagram);

                String newNodeDescriptionName = this.descriptionNameGenerator.getNodeName(childEClass);
                new CheckNodeOnDiagram(this.diagramDescriptionIdProvider, this.diagramComparator)
                        .hasNodeDescriptionName(newNodeDescriptionName)
                        .hasCompartmentCount(compartmentCount)
                        .check(initialDiagram, newDiagram);

                String compartmentNodeDescriptionName = this.descriptionNameGenerator.getCompartmentItemName(parentEClass, SysmlPackage.eINSTANCE.getUsage_NestedItem());
                new CheckNodeInCompartment(this.diagramDescriptionIdProvider, this.diagramComparator)
                        .withParentLabel(parentLabel)
                        .withCompartmentName("items")
                        .hasNodeDescriptionName(compartmentNodeDescriptionName)
                        .check(initialDiagram, newDiagram);
            };
        } else {
            diagramChecker = this.getSiblingNodeGraphicalChecker(childEClass, compartmentCount);
        }
        this.checkDiagram(diagramChecker);
        this.checkEditingContext(this.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("actionUsageListNodeParameters")
    public void createActionUsageListChildNodes(EClass childEClass, String compartmentName, EReference containmentReference) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getActionUsage();
        String parentLabel = "action";
        this.createNode(parentEClass, parentLabel, childEClass);
        this.checkDiagram(this.getCompartmentNodeGraphicalChecker(parentLabel, parentEClass, containmentReference, compartmentName));
        this.checkEditingContext(this.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("actionUsageListAndFreeFormNodeParameters")
    public void createActionUsageListAndFreeFormChildNodes(EClass childEClass, String compartmentName, EReference containmentReference, int compartmentCount) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getActionUsage();
        String parentLabel = "action";

        String creationToolName = this.descriptionNameGenerator.getCreationToolName(childEClass);
        if (creationToolName.endsWith(" Node")) {
            creationToolName = creationToolName.substring(0, creationToolName.length() - 5);
        }

        this.createNode(parentEClass, parentLabel, creationToolName);
        IDiagramChecker diagramChecker = (initialDiagram, newDiagram) -> {
            int createdNodesExpectedCount = 2 + compartmentCount;
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(createdNodesExpectedCount)
                    .check(initialDiagram, newDiagram);
            String listNodeDescription = this.descriptionNameGenerator.getCompartmentItemName(parentEClass, containmentReference);
            new CheckNodeInCompartment(this.diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName(compartmentName)
                    .hasNodeDescriptionName(listNodeDescription)
                    .hasCompartmentCount(0)
                    .check(initialDiagram, newDiagram);
            String freeFormNodeDescription = this.descriptionNameGenerator.getNodeName(childEClass);
            new CheckNodeInCompartment(this.diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName("action flow")
                    .hasNodeDescriptionName(freeFormNodeDescription)
                    .hasCompartmentCount(compartmentCount)
                    .check(initialDiagram, newDiagram);
        };

        this.checkDiagram(diagramChecker);
        this.checkEditingContext(this.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("actionDefinitionListAndFreeFormNodeParameters")
    public void createActionDefinitionListAndFreeFormChildNodes(EClass childEClass, String compartmentName, EReference containmentReference, int compartmentCount) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getActionDefinition();
        String parentLabel = "ActionDefinition";

        String creationToolName = this.descriptionNameGenerator.getCreationToolName(childEClass);
        if (creationToolName.endsWith(" Node")) {
            creationToolName = creationToolName.substring(0, creationToolName.length() - 5);
        }

        this.createNode(parentEClass, parentLabel, creationToolName);

        IDiagramChecker diagramChecker = (initialDiagram, newDiagram) -> {
            int createdNodesExpectedCount = 2 + compartmentCount;
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(createdNodesExpectedCount)
                    .check(initialDiagram, newDiagram);
            String listNodeDescription = this.descriptionNameGenerator.getCompartmentItemName(parentEClass, containmentReference);
            new CheckNodeInCompartment(this.diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName(compartmentName)
                    .hasNodeDescriptionName(listNodeDescription)
                    .hasCompartmentCount(0)
                    .check(initialDiagram, newDiagram);
            String freeFormNodeDescription = this.descriptionNameGenerator.getNodeName(childEClass);
            new CheckNodeInCompartment(this.diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName("action flow")
                    .hasNodeDescriptionName(freeFormNodeDescription)
                    .hasCompartmentCount(compartmentCount)
                    .check(initialDiagram, newDiagram);
        };

        this.checkDiagram(diagramChecker);
        this.checkEditingContext(this.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("constraintUsageSiblingNodeParameters")
    public void createConstraintUsageSiblingNodes(EClass childEClass, EReference containmentReference, int compartmentCount) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getConstraintUsage();
        String parentLabel = "constraint";
        this.createNode(parentEClass, parentLabel, childEClass);
        this.checkDiagram(this.getSiblingNodeGraphicalChecker(childEClass, compartmentCount));
        this.checkEditingContext(this.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("constraintUsageChildNodeParameters")
    public void createConstraintUsageChildNodes(EClass childEClass, String compartmentName, EReference containmentReference) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getConstraintUsage();
        String parentLabel = "constraint";
        this.createNode(parentEClass, parentLabel, childEClass);
        this.checkDiagram(this.getCompartmentNodeGraphicalChecker(parentLabel, parentEClass, containmentReference, compartmentName));
        this.checkEditingContext(this.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("constraintDefinitionSiblingNodeParameters")
    public void createConstraintDefinitionSiblingNodes(EClass childEClass, EReference containmentReference, int compartmentCount) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getConstraintDefinition();
        String parentLabel = "ConstraintDefinition";
        this.createNode(parentEClass, parentLabel, childEClass);
        this.checkDiagram(this.getSiblingNodeGraphicalChecker(childEClass, compartmentCount));
        this.checkEditingContext(this.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("constraintDefinitionChildNodeParameters")
    public void createConstraintDefinitionChildNodes(EClass childEClass, String compartmentName, EReference containmentReference) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getConstraintDefinition();
        String parentLabel = "ConstraintDefinition";
        this.createNode(parentEClass, parentLabel, childEClass);
        this.checkDiagram(this.getCompartmentNodeGraphicalChecker(parentLabel, parentEClass, containmentReference, compartmentName));
        this.checkEditingContext(this.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("requirementUsageSiblingNodeParameters")
    public void createRequirementUsageSiblingNodes(EClass childEClass, EReference containmentReference, int compartmentCount) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getRequirementUsage();
        String parentLabel = "requirement";
        this.createNode(parentEClass, parentLabel, childEClass);
        this.checkDiagram(this.getSiblingNodeGraphicalChecker(childEClass, compartmentCount));
        this.checkEditingContext(this.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("requirementUsageChildNodeParameters")
    public void createRequirementUsageChildNodes(EClass childEClass, String compartmentName, EReference containmentReference) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getRequirementUsage();
        String parentLabel = "requirement";
        this.createNode(parentEClass, parentLabel, childEClass);
        this.checkDiagram(this.getCompartmentNodeGraphicalChecker(parentLabel, parentEClass, containmentReference, compartmentName));
        this.checkEditingContext(this.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("requirementDefinitionChildNodeParameters")
    public void createRequirementDefinitionChildNodes(EClass childEClass, String compartmentName, EReference containmentReference) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getRequirementDefinition();
        String parentLabel = "RequirementDefinition";
        this.createNode(parentEClass, parentLabel, childEClass);
        this.checkDiagram(this.getCompartmentNodeGraphicalChecker(parentLabel, parentEClass, containmentReference, compartmentName));
        this.checkEditingContext(this.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("useCaseUsageSiblingNodeParameters")
    public void createUseCaseUsageSiblingNodes(EClass childEClass, EReference containmentReference, int compartmentCount) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getUseCaseUsage();
        String parentLabel = "useCase";
        this.createNode(parentEClass, parentLabel, childEClass);
        this.checkDiagram(this.getSiblingNodeGraphicalChecker(childEClass, compartmentCount));
        this.checkEditingContext(this.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("occurrenceUsageChildNodeParameters")
    public void createOccurrenceUsageChildNodes(EClass childEClass, String compartmentName, EReference containmentReference) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getOccurrenceUsage();
        String parentLabel = "occurrence";
        this.createNode(parentEClass, parentLabel, childEClass);
        this.checkDiagram(this.getCompartmentNodeGraphicalChecker(parentLabel, parentEClass, containmentReference, compartmentName));
        this.checkEditingContext(this.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("occurrenceDefinitionSiblingNodeParameters")
    public void createOccurrenceDefinitionSiblingNodes(EClass childEClass, EReference containmentReference, int compartmentCount) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getOccurrenceDefinition();
        String parentLabel = "OccurrenceDefinition";
        final IDiagramChecker diagramChecker;
        if (SysmlPackage.eINSTANCE.getPartUsage().equals(childEClass)) {
            // A PartUsage appears both as a sibling and in the "occurrences" compartment.
            diagramChecker = (initialDiagram, newDiagram) -> {
                int createdNodesExpectedCount = 2 + compartmentCount;
                new CheckDiagramElementCount(this.diagramComparator)
                        .hasNewNodeCount(createdNodesExpectedCount)
                        .hasNewEdgeCount(1)
                        .check(initialDiagram, newDiagram);

                String newNodeDescriptionName = this.descriptionNameGenerator.getNodeName(childEClass);
                new CheckNodeOnDiagram(this.diagramDescriptionIdProvider, this.diagramComparator)
                        .hasNodeDescriptionName(newNodeDescriptionName)
                        .hasCompartmentCount(compartmentCount)
                        .check(initialDiagram, newDiagram);

                String compartmentNodeDescriptionName = this.descriptionNameGenerator.getCompartmentItemName(parentEClass, SysmlPackage.eINSTANCE.getDefinition_OwnedOccurrence());
                new CheckNodeInCompartment(this.diagramDescriptionIdProvider, this.diagramComparator)
                        .withParentLabel(parentLabel)
                        .withCompartmentName("occurrences")
                        .hasNodeDescriptionName(compartmentNodeDescriptionName)
                        .check(initialDiagram, newDiagram);
            };
        } else {
            diagramChecker = this.getSiblingNodeGraphicalChecker(childEClass, compartmentCount);
        }
        this.createNode(parentEClass, parentLabel, childEClass);
        this.checkDiagram(diagramChecker);
        this.checkEditingContext(this.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("occurrenceDefinitionChildNodeParameters")
    public void createOccurrenceDefinitionChildNodes(EClass childEClass, String compartmentName, EReference containmentReference) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getOccurrenceDefinition();
        String parentLabel = "OccurrenceDefinition";
        this.createNode(parentEClass, parentLabel, childEClass);
        this.checkDiagram(this.getCompartmentNodeGraphicalChecker(parentLabel, parentEClass, containmentReference, compartmentName));
        this.checkEditingContext(this.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("metadataDefinitionSiblingNodeParameters")
    public void createMetadataDefinitionSiblingNodes(EClass childEClass, EReference containmentReference, int compartmentCount) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getMetadataDefinition();
        String parentLabel = "MetadataDefinition";
        this.createNode(parentEClass, parentLabel, childEClass);
        this.checkDiagram(this.getSiblingNodeGraphicalChecker(childEClass, compartmentCount));
        this.checkEditingContext(this.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("metadataDefinitionChildNodeParameters")
    public void createMetadataDefinitionChildNodes(EClass childEClass, String compartmentName, EReference containmentReference) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getMetadataDefinition();
        String parentLabel = "MetadataDefinition";
        this.createNode(parentEClass, parentLabel, childEClass);
        this.checkDiagram(this.getCompartmentNodeGraphicalChecker(parentLabel, parentEClass, containmentReference, compartmentName));
        this.checkEditingContext(this.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));
    }

    private void createNode(EClass parentEClass, String parentLabel, EClass childEClass) {
        this.createNode(parentEClass, parentLabel, this.descriptionNameGenerator.getCreationToolName(childEClass));
    }

    private void createNode(EClass parentEClass, String parentLabel, String toolName) {
        String creationToolId = this.diagramDescriptionIdProvider.getNodeCreationToolId(this.descriptionNameGenerator.getNodeName(parentEClass), toolName);
        this.verifier.then(() -> this.nodeCreationTester.createNode(SysMLv2Identifiers.GENERAL_VIEW_WITH_TOP_NODES_PROJECT,
                this.diagram,
                parentLabel,
                creationToolId));
    }

    private IDiagramChecker getChildNodeGraphicalChecker(String parentLabel, EClass childEClass, int compartmentCount) {
        return (initialDiagram, newDiagram) -> {
            int createdNodesExpectedCount = 1 + compartmentCount;
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(createdNodesExpectedCount)
                    .hasNewEdgeCount(0)
                    .check(this.diagram.get(), newDiagram);

            String newNodeDescriptionName = this.descriptionNameGenerator.getNodeName(childEClass);
            new CheckChildNode(this.diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .hasNodeDescriptionName(newNodeDescriptionName)
                    .hasCompartmentCount(compartmentCount)
                    .check(this.diagram.get(), newDiagram);
        };
    }

    private IDiagramChecker getCompartmentNodeGraphicalChecker(String parentLabel, EClass parentEClass, EReference containmentReference, String compartmentName) {
        return (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(1)
                    .hasNewEdgeCount(0)
                    .check(this.diagram.get(), newDiagram);

            String newNodeDescriptionName = this.descriptionNameGenerator.getCompartmentItemName(parentEClass, containmentReference);
            new CheckNodeInCompartment(this.diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName(compartmentName)
                    .hasNodeDescriptionName(newNodeDescriptionName)
                    .hasCompartmentCount(0)
                    .check(this.diagram.get(), newDiagram);
        };
    }

    private IDiagramChecker getSiblingNodeGraphicalChecker(EClass childEClass, int compartmentCount) {
        return (initialDiagram, newDiagram) -> {
            int createdNodesExpectedCount = 1 + compartmentCount;
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(createdNodesExpectedCount)
                    .hasNewEdgeCount(1)
                    .check(this.diagram.get(), newDiagram);

            String newNodeDescriptionName = this.descriptionNameGenerator.getNodeName(childEClass);
            new CheckNodeOnDiagram(this.diagramDescriptionIdProvider, this.diagramComparator)
                    .hasNodeDescriptionName(newNodeDescriptionName)
                    .hasCompartmentCount(compartmentCount)
                    .check(this.diagram.get(), newDiagram);
        };
    }

    private void checkDiagram(IDiagramChecker diagramChecker) {
        Consumer<DiagramRefreshedEventPayload> diagramConsumer = payload -> Optional.of(payload)
                .map(DiagramRefreshedEventPayload::diagram)
                .ifPresentOrElse(newDiagram -> {
                    diagramChecker.check(this.diagram.get(), newDiagram);
                }, () -> fail("Missing diagram"));
        this.verifier.consumeNextWith(diagramConsumer);
    }

    private void checkEditingContext(ISemanticChecker semanticChecker) {
        Runnable runnableChecker = this.semanticCheckerFactory.createRunnableChecker(SysMLv2Identifiers.GENERAL_VIEW_WITH_TOP_NODES_PROJECT,
                editingContext -> {
                    semanticChecker.check(editingContext);
                    return true;
                });

        this.verifier.then(runnableChecker);
    }

    private ISemanticChecker getElementInParentSemanticChecker(String parentLabel, EReference containmentReference, EClass childEClass) {
        return new CheckElementInParent(this.objectService, SysMLv2Identifiers.GENERAL_VIEW_WITH_TOP_NODES_DIAGRAM_OBJECT)
                .withParentLabel(parentLabel)
                .withContainmentReference(containmentReference)
                .hasType(childEClass);
    }
}
