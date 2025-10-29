/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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
package org.eclipse.syson.diagram.services.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.syson.diagram.services.DiagramQueryLabelService;
import org.eclipse.syson.diagram.services.api.IDiagramLabelService;
import org.eclipse.syson.sysml.AcceptActionUsage;
import org.eclipse.syson.sysml.AnnotatingElement;
import org.eclipse.syson.sysml.AssignmentActionUsage;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.helper.LabelConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Multiline Label services tests.
 *
 * @author jmallet
 */
public class MultilineLabelSwitchTest {

    private static final SysmlPackage SYSML = SysmlPackage.eINSTANCE;

    private static final String REF_ATTRIBUTE_LABEL = LabelConstants.REF + LabelConstants.SPACE;

    private static final String DEFAULT_ACTION_LABEL = LabelConstants.OPEN_QUOTE + REF_ATTRIBUTE_LABEL + "action" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR;

    private static final String DEFAULT_ACTION_DEF_LABEL = LabelConstants.OPEN_QUOTE + "action def" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR;

    private static final String DEFAULT_ACCEPT_ACTION_LABEL = LabelConstants.OPEN_QUOTE + REF_ATTRIBUTE_LABEL + "accept action" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR;

    private static final String DEFAULT_ALLOCATION_DEFINITION_LABEL = LabelConstants.OPEN_QUOTE + "allocation def" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR;

    private static final String DEFAULT_ALLOCATION_USAGE_LABEL = LabelConstants.OPEN_QUOTE + REF_ATTRIBUTE_LABEL + "allocation" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR;

    private static final String DEFAULT_ATTRIBUTE_DEFINITION_LABEL = LabelConstants.OPEN_QUOTE + "attribute def" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR;

    private static final String DEFAULT_ATTRIBUTE_USAGE_LABEL = LabelConstants.OPEN_QUOTE + "attribute" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR;

    private static final String DEFAULT_ASSIGNMENT_ACTION_USAGE_LABEL = LabelConstants.OPEN_QUOTE + REF_ATTRIBUTE_LABEL + "assign" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR;

    private static final String DEFAULT_COMMENT_LABEL = LabelConstants.OPEN_QUOTE + "comment" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR;

    private static final String DEFAULT_CONCERN_DEFINITION_LABEL = LabelConstants.OPEN_QUOTE + "concern def" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR;

    private static final String DEFAULT_CONCERN_USAGE_LABEL = LabelConstants.OPEN_QUOTE + REF_ATTRIBUTE_LABEL + "concern" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR;

    private static final String DEFAULT_CONSTRAINT_DEFINITION_LABEL = LabelConstants.OPEN_QUOTE + "constraint def" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR;

    private static final String DEFAULT_CONSTRAINT_USAGE_LABEL = LabelConstants.OPEN_QUOTE + REF_ATTRIBUTE_LABEL + "constraint" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR;

    private static final String DEFAULT_DOCUMENTATION_LABEL = LabelConstants.OPEN_QUOTE + "doc" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR;

    private static final String DEFAULT_ENUMERATION_DEFINITION_LABEL = LabelConstants.OPEN_QUOTE + "enumeration def" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR;

    private static final String DEFAULT_EXHIBIT_STATE_USAGE_LABEL = LabelConstants.OPEN_QUOTE + REF_ATTRIBUTE_LABEL + "exhibit state" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR;

    private static final String DEFAULT_INTERFACE_DEFINITION_LABEL = LabelConstants.OPEN_QUOTE + "interface def" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR;

    private static final String DEFAULT_INTERFACE_USAGE_LABEL = LabelConstants.OPEN_QUOTE + REF_ATTRIBUTE_LABEL + "interface" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR;

    private static final String DEFAULT_ITEM_DEFINITION_LABEL = LabelConstants.OPEN_QUOTE + "item def" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR;

    private static final String DEFAULT_ITEM_USAGE_LABEL = LabelConstants.OPEN_QUOTE + REF_ATTRIBUTE_LABEL + "item" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR;

    private static final String DEFAULT_METADATA_DEFINITION_LABEL = LabelConstants.OPEN_QUOTE + "metadata def" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR;

    private static final String DEFAULT_OCCURRENCE_DEFINITION_LABEL = LabelConstants.OPEN_QUOTE + "occurrence def" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR;

    private static final String DEFAULT_OCCURRENCE_USAGE_LABEL = LabelConstants.OPEN_QUOTE + REF_ATTRIBUTE_LABEL + "occurrence" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR;

    private static final String DEFAULT_PART_DEFINITION_LABEL = LabelConstants.OPEN_QUOTE + "part def" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR;

    private static final String DEFAULT_PART_USAGE_LABEL = LabelConstants.OPEN_QUOTE + REF_ATTRIBUTE_LABEL + "part" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR;

    private static final String DEFAULT_PERFORM_ACTION_USAGE_LABEL = LabelConstants.OPEN_QUOTE + REF_ATTRIBUTE_LABEL + "perform action" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR;

    private static final String DEFAULT_PORT_DEFINITION_LABEL = LabelConstants.OPEN_QUOTE + "port def" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR;

    private static final String DEFAULT_PORT_USAGE_LABEL = LabelConstants.OPEN_QUOTE + REF_ATTRIBUTE_LABEL + "port" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR;

    private static final String DEFAULT_REQUIREMENT_DEFINITION_LABEL = LabelConstants.OPEN_QUOTE + "requirement def" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR;

    private static final String DEFAULT_REQUIREMENT_USAGE_LABEL = LabelConstants.OPEN_QUOTE + REF_ATTRIBUTE_LABEL + "requirement" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR;

    private static final String DEFAULT_SATISFY_REQUIREMENT_USAGE_LABEL = LabelConstants.OPEN_QUOTE + REF_ATTRIBUTE_LABEL + "satisfy requirement" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR;

    private static final String DEFAULT_USE_CASE_DEFINITION_LABEL = LabelConstants.OPEN_QUOTE + "use case def" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR;

    private static final String DEFAULT_USE_CASE_USAGE_LABEL = LabelConstants.OPEN_QUOTE + REF_ATTRIBUTE_LABEL + "use case" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR;

    private static final String DEFAULT_STATE_DEFINITION_LABEL = LabelConstants.OPEN_QUOTE + "state def" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR;

    private static final String DEFAULT_STATE_USAGE_LABEL = LabelConstants.OPEN_QUOTE + REF_ATTRIBUTE_LABEL + "state" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR;

    private MultiLineLabelSwitch multiLineLabelSwitch;

    @BeforeEach
    void beforeEach() {
        IDiagramLabelService labelService = new DiagramQueryLabelService();
        this.multiLineLabelSwitch = new MultiLineLabelSwitch(labelService);
    }

    private static Stream<Arguments> defaultParameterProvider() {
        return Stream.of(
                Arguments.of(SYSML.getActionUsage(), DEFAULT_ACTION_LABEL),
                Arguments.of(SYSML.getActionDefinition(), DEFAULT_ACTION_DEF_LABEL),
                Arguments.of(SYSML.getAcceptActionUsage(), DEFAULT_ACCEPT_ACTION_LABEL),
                Arguments.of(SYSML.getAllocationDefinition(), DEFAULT_ALLOCATION_DEFINITION_LABEL),
                Arguments.of(SYSML.getAllocationUsage(), DEFAULT_ALLOCATION_USAGE_LABEL),
                Arguments.of(SYSML.getAttributeDefinition(), DEFAULT_ATTRIBUTE_DEFINITION_LABEL),
                Arguments.of(SYSML.getAttributeUsage(), DEFAULT_ATTRIBUTE_USAGE_LABEL),
                Arguments.of(SYSML.getAssignmentActionUsage(), DEFAULT_ASSIGNMENT_ACTION_USAGE_LABEL),
                Arguments.of(SYSML.getComment(), DEFAULT_COMMENT_LABEL),
                Arguments.of(SYSML.getConcernDefinition(), DEFAULT_CONCERN_DEFINITION_LABEL),
                Arguments.of(SYSML.getConcernUsage(), DEFAULT_CONCERN_USAGE_LABEL),
                Arguments.of(SYSML.getConstraintDefinition(), DEFAULT_CONSTRAINT_DEFINITION_LABEL),
                Arguments.of(SYSML.getConstraintUsage(), DEFAULT_CONSTRAINT_USAGE_LABEL),
                Arguments.of(SYSML.getDocumentation(), DEFAULT_DOCUMENTATION_LABEL),
                Arguments.of(SYSML.getEnumerationDefinition(), DEFAULT_ENUMERATION_DEFINITION_LABEL),
                Arguments.of(SYSML.getExhibitStateUsage(), DEFAULT_EXHIBIT_STATE_USAGE_LABEL),
                Arguments.of(SYSML.getInterfaceDefinition(), DEFAULT_INTERFACE_DEFINITION_LABEL),
                Arguments.of(SYSML.getInterfaceUsage(), DEFAULT_INTERFACE_USAGE_LABEL),
                Arguments.of(SYSML.getItemDefinition(), DEFAULT_ITEM_DEFINITION_LABEL),
                Arguments.of(SYSML.getItemUsage(), DEFAULT_ITEM_USAGE_LABEL),
                Arguments.of(SYSML.getMetadataDefinition(), DEFAULT_METADATA_DEFINITION_LABEL),
                Arguments.of(SYSML.getOccurrenceUsage(), DEFAULT_OCCURRENCE_USAGE_LABEL),
                Arguments.of(SYSML.getOccurrenceDefinition(), DEFAULT_OCCURRENCE_DEFINITION_LABEL),
                Arguments.of(SYSML.getPartUsage(), DEFAULT_PART_USAGE_LABEL),
                Arguments.of(SYSML.getPartDefinition(), DEFAULT_PART_DEFINITION_LABEL),
                Arguments.of(SYSML.getPerformActionUsage(), DEFAULT_PERFORM_ACTION_USAGE_LABEL),
                Arguments.of(SYSML.getPortDefinition(), DEFAULT_PORT_DEFINITION_LABEL),
                Arguments.of(SYSML.getPortUsage(), DEFAULT_PORT_USAGE_LABEL),
                Arguments.of(SYSML.getSatisfyRequirementUsage(), DEFAULT_SATISFY_REQUIREMENT_USAGE_LABEL),
                Arguments.of(SYSML.getRequirementDefinition(), DEFAULT_REQUIREMENT_DEFINITION_LABEL),
                Arguments.of(SYSML.getRequirementUsage(), DEFAULT_REQUIREMENT_USAGE_LABEL),
                Arguments.of(SYSML.getUseCaseDefinition(), DEFAULT_USE_CASE_DEFINITION_LABEL),
                Arguments.of(SYSML.getUseCaseUsage(), DEFAULT_USE_CASE_USAGE_LABEL),
                Arguments.of(SYSML.getStateDefinition(), DEFAULT_STATE_DEFINITION_LABEL),
                Arguments.of(SYSML.getStateUsage(), DEFAULT_STATE_USAGE_LABEL)
        );
    }

    private static Stream<Arguments> prefixParameterProvider() {
        return Stream.of(
                Arguments.of(SYSML.getActionUsage(), DEFAULT_ACTION_LABEL),
                Arguments.of(SYSML.getActionDefinition(), DEFAULT_ACTION_DEF_LABEL),
                Arguments.of(SYSML.getAcceptActionUsage(), DEFAULT_ACCEPT_ACTION_LABEL),
                Arguments.of(SYSML.getAllocationDefinition(), DEFAULT_ALLOCATION_DEFINITION_LABEL),
                Arguments.of(SYSML.getAllocationUsage(), DEFAULT_ALLOCATION_USAGE_LABEL),
                Arguments.of(SYSML.getAttributeDefinition(), DEFAULT_ATTRIBUTE_DEFINITION_LABEL),
                Arguments.of(SYSML.getAttributeUsage(), DEFAULT_ATTRIBUTE_USAGE_LABEL),
                Arguments.of(SYSML.getAssignmentActionUsage(), DEFAULT_ASSIGNMENT_ACTION_USAGE_LABEL),
                Arguments.of(SYSML.getConcernDefinition(), DEFAULT_CONCERN_DEFINITION_LABEL),
                Arguments.of(SYSML.getConcernUsage(), DEFAULT_CONCERN_USAGE_LABEL),
                Arguments.of(SYSML.getConstraintDefinition(), DEFAULT_CONSTRAINT_DEFINITION_LABEL),
                Arguments.of(SYSML.getConstraintUsage(), DEFAULT_CONSTRAINT_USAGE_LABEL),
                Arguments.of(SYSML.getEnumerationDefinition(), DEFAULT_ENUMERATION_DEFINITION_LABEL),
                Arguments.of(SYSML.getExhibitStateUsage(), DEFAULT_EXHIBIT_STATE_USAGE_LABEL),
                Arguments.of(SYSML.getInterfaceDefinition(), DEFAULT_INTERFACE_DEFINITION_LABEL),
                Arguments.of(SYSML.getInterfaceUsage(), DEFAULT_INTERFACE_USAGE_LABEL),
                Arguments.of(SYSML.getItemDefinition(), DEFAULT_ITEM_DEFINITION_LABEL),
                Arguments.of(SYSML.getItemUsage(), DEFAULT_ITEM_USAGE_LABEL),
                Arguments.of(SYSML.getMetadataDefinition(), DEFAULT_METADATA_DEFINITION_LABEL),
                Arguments.of(SYSML.getOccurrenceUsage(), DEFAULT_OCCURRENCE_USAGE_LABEL),
                Arguments.of(SYSML.getOccurrenceDefinition(), DEFAULT_OCCURRENCE_DEFINITION_LABEL),
                Arguments.of(SYSML.getPartUsage(), DEFAULT_PART_USAGE_LABEL),
                Arguments.of(SYSML.getPartDefinition(), DEFAULT_PART_DEFINITION_LABEL),
                Arguments.of(SYSML.getPerformActionUsage(), DEFAULT_PERFORM_ACTION_USAGE_LABEL),
                Arguments.of(SYSML.getPortDefinition(), DEFAULT_PORT_DEFINITION_LABEL),
                Arguments.of(SYSML.getPortUsage(), DEFAULT_PORT_USAGE_LABEL),
                Arguments.of(SYSML.getRequirementDefinition(), DEFAULT_REQUIREMENT_DEFINITION_LABEL),
                Arguments.of(SYSML.getRequirementUsage(), DEFAULT_REQUIREMENT_USAGE_LABEL),
                Arguments.of(SYSML.getSatisfyRequirementUsage(), DEFAULT_SATISFY_REQUIREMENT_USAGE_LABEL),
                Arguments.of(SYSML.getUseCaseDefinition(), DEFAULT_USE_CASE_DEFINITION_LABEL),
                Arguments.of(SYSML.getUseCaseUsage(), DEFAULT_USE_CASE_USAGE_LABEL),
                Arguments.of(SYSML.getStateDefinition(), DEFAULT_STATE_DEFINITION_LABEL),
                Arguments.of(SYSML.getStateUsage(), DEFAULT_STATE_USAGE_LABEL)
        );
    }

    @ParameterizedTest(name = "[{index}] Check default prefix in {0} element label")
    @MethodSource("defaultParameterProvider")
    public void testDefaultLabel(EClass elementType, String defaultLabel) {
        EObject newElement = SysmlFactory.eINSTANCE.create(elementType);
        if (newElement instanceof AcceptActionUsage) {
            assertEquals(defaultLabel + LabelConstants.CR,
                    this.multiLineLabelSwitch.doSwitch(newElement));
        } else {
            assertEquals(defaultLabel,
                    this.multiLineLabelSwitch.doSwitch(newElement));
        }
    }

    @ParameterizedTest(name = "[{index}] Check default prefix in {0} element label")
    @MethodSource("defaultParameterProvider")
    public void testDefaultLabelWithNamedElement(EClass elementType, String defaultLabel) {
        EObject newElement = SysmlFactory.eINSTANCE.create(elementType);
        if (newElement instanceof Element elt) {
            String declaredName = "myElt";
            elt.setDeclaredName(declaredName);
            if (newElement instanceof AcceptActionUsage || newElement instanceof AnnotatingElement) {
                assertEquals(defaultLabel + declaredName + LabelConstants.CR,
                        this.multiLineLabelSwitch.doSwitch(newElement));
            } else if (elt instanceof AssignmentActionUsage) {
                assertEquals(defaultLabel,
                        this.multiLineLabelSwitch.doSwitch(newElement));
            } else {
                assertEquals(defaultLabel + declaredName,
                        this.multiLineLabelSwitch.doSwitch(newElement));
            }
        }
    }

    @ParameterizedTest(name = "[{index}] Check Abstract prefix in {0} element label")
    @MethodSource("prefixParameterProvider")
    public void testPrefixLabelWithAbstractProperty(EClass elementType, String defaultLabel) {
        Element newElement = (Element) SysmlFactory.eINSTANCE.create(elementType);
        if (newElement instanceof Type type) {
            type.setIsAbstract(true);
            String abstractPrefix = LabelConstants.OPEN_QUOTE + LabelConstants.ABSTRACT + LabelConstants.CLOSE_QUOTE + LabelConstants.CR;
            if (newElement instanceof AcceptActionUsage) {
                assertEquals(abstractPrefix + defaultLabel + LabelConstants.CR,
                        this.multiLineLabelSwitch.doSwitch(newElement));
            } else {
                assertEquals(abstractPrefix + defaultLabel,
                        this.multiLineLabelSwitch.doSwitch(newElement));
            }
        }
    }

    @ParameterizedTest(name = "[{index}] Check Variation prefix in {0} element label")
    @MethodSource("prefixParameterProvider")
    public void testPrefixLabelWithVariationProperty(EClass elementType, String defaultLabel) {
        Element newElement = (Element) SysmlFactory.eINSTANCE.create(elementType);
        if (newElement instanceof Usage) {
            ((Usage) newElement).setIsVariation(true);
        }
        if (newElement instanceof Definition) {
            ((Definition) newElement).setIsVariation(true);
        }
        String variationPrefix = LabelConstants.OPEN_QUOTE + LabelConstants.VARIATION + LabelConstants.CLOSE_QUOTE + LabelConstants.CR;
        if (newElement instanceof AcceptActionUsage) {
            assertEquals(variationPrefix + defaultLabel + LabelConstants.CR,
                    this.multiLineLabelSwitch.doSwitch(newElement));
        } else {
            assertEquals(variationPrefix + defaultLabel,
                    this.multiLineLabelSwitch.doSwitch(newElement));
        }
    }

    @ParameterizedTest(name = "[{index}] Check Variant prefix in {0} element label")
    @MethodSource("prefixParameterProvider")
    public void testPrefixLabelWithVariantProperty(EClass elementType, String defaultLabel) {
        Element parentElement = (Element) SysmlFactory.eINSTANCE.create(elementType);
        if (parentElement instanceof Usage) {
            ((Usage) parentElement).setIsVariation(true);
        }
        if (parentElement instanceof Definition) {
            ((Definition) parentElement).setIsVariation(true);
        }
        Element newElement = this.createNestedElementIn(parentElement, elementType);
        String variantPrefix = LabelConstants.OPEN_QUOTE + LabelConstants.VARIANT + LabelConstants.CLOSE_QUOTE + LabelConstants.CR;
        if (newElement instanceof AcceptActionUsage) {
            assertEquals(variantPrefix + defaultLabel + LabelConstants.CR,
                    this.multiLineLabelSwitch.doSwitch(newElement));
        } else {
            assertEquals(variantPrefix + defaultLabel,
                    this.multiLineLabelSwitch.doSwitch(newElement));
        }
    }

    private Element createNestedElementIn(Element parentElement, EClass elementType) {
        Element newElement = (Element) SysmlFactory.eINSTANCE.create(elementType);
        FeatureMembership membership = SysmlFactory.eINSTANCE.createFeatureMembership();
        parentElement.getOwnedRelationship().add(membership);
        membership.getOwnedRelatedElement().add(newElement);
        return newElement;
    }

}
