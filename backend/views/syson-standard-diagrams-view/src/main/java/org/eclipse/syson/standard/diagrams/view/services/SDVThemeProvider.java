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
package org.eclipse.syson.standard.diagrams.view.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.standard.diagrams.view.services.api.ISDVThemeProvider;
import org.eclipse.syson.standard.diagrams.view.services.api.SDVTheme;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.springframework.stereotype.Service;

/**
 * Provides themes for standard diagrams.
 *
 * @author gcoutable
 */
@Service
public class SDVThemeProvider implements ISDVThemeProvider {

    private static final String STRUCTURE_NODE_BACKGROUND_COLOR = "#DCD9F5";

    private static final String STRUCTURE_COMPARTMENT_BACKGROUND_COLOR = "#FCFAFF";

    private static final String STRUCTURE_BORDER_COLOR = "#8275BC";

    private static final String BEHAVIOR_NODE_BACKGROUND_COLOR = "#DCF5C4";

    private static final String BEHAVIOR_COMPARTMENT_BACKGROUND_COLOR = "#FAFEFC";

    private static final String BEHAVIOR_BORDER_COLOR = "#186A3B";

    private static final String STATES_NODE_BACKGROUND_COLOR = "#D5F5E3";

    private static final String STATES_COMPARTMENT_BACKGROUND_COLOR = "#FAFEFC";

    private static final String STATES_BORDER_COLOR = "#186A3B";

    private static final String REQUIREMENTS_NODE_BACKGROUND_COLOR = "#E5DDE9";

    private static final String REQUIREMENTS_COMPARTMENT_BACKGROUND_COLOR = "#FFFBFD";

    private static final String REQUIREMENTS_BORDER_COLOR = "#AA6C86";

    private static final String ANALYSIS_NODE_BACKGROUND_COLOR = "#F2DFC3";

    private static final String ANALYSIS_COMPARTMENT_BACKGROUND_COLOR = "#FFFEF9";

    private static final String ANALYSIS_BORDER_COLOR = "#A1843B";

    private static final String OCCURRENCES_NODE_BACKGROUND_COLOR = "#CEF2FF";

    private static final String OCCURRENCES_COMPARTMENT_BACKGROUND_COLOR = "#FAFDFF";

    private static final String OCCURRENCES_BORDER_COLOR = "#3F8DB8";

    private static final String ORGANIZATION_NODE_BACKGROUND_COLOR = "#E2E5ED";

    private static final String ORGANIZATION_COMPARTMENT_BACKGROUND_COLOR = "#FDFDFE";

    private static final String ORGANIZATION_BORDER_COLOR = "#818896";

    private final IDescriptionNameGenerator descriptionNameGenerator = new SDVDescriptionNameGenerator();

    private final Map<String, SDVTheme> themeByDescriptionName;

    public SDVThemeProvider() {
        var themes = new HashMap<String, SDVTheme>();
        this.registerStructure(themes);
        this.registerBehavior(themes);
        this.registerStates(themes);
        this.registerRequirements(themes);
        this.registerAnalysis(themes);
        this.registerOccurrences(themes);
        this.registerOrganization(themes);
        this.registerOrganizationCustomNode(themes);
        this.themeByDescriptionName = Map.copyOf(themes);
    }

    @Override
    public Optional<SDVTheme> findTheme(String descriptionName) {
        Objects.requireNonNull(descriptionName);
        return Optional.ofNullable(this.themeByDescriptionName.get(descriptionName))
                .or(() -> this.themeByDescriptionName.entrySet().stream()
                        .filter(entry -> entry.getKey().endsWith(" ") && descriptionName.startsWith(entry.getKey()))
                        .map(Map.Entry::getValue)
                        .findFirst());
    }

    private void registerStructure(Map<String, SDVTheme> themes) {
        var theme = new SDVTheme(STRUCTURE_NODE_BACKGROUND_COLOR, STRUCTURE_COMPARTMENT_BACKGROUND_COLOR, STRUCTURE_BORDER_COLOR);
        this.registerFamily(themes, theme, List.of(
                SysmlPackage.eINSTANCE.getPartDefinition(), SysmlPackage.eINSTANCE.getPartUsage(),
                SysmlPackage.eINSTANCE.getItemDefinition(), SysmlPackage.eINSTANCE.getItemUsage(),
                SysmlPackage.eINSTANCE.getAttributeDefinition(), SysmlPackage.eINSTANCE.getAttributeUsage(),
                SysmlPackage.eINSTANCE.getPortDefinition(), SysmlPackage.eINSTANCE.getPortUsage(),
                SysmlPackage.eINSTANCE.getInterfaceDefinition(), SysmlPackage.eINSTANCE.getInterfaceUsage(),
                SysmlPackage.eINSTANCE.getConnectionDefinition(), SysmlPackage.eINSTANCE.getConnectionUsage(),
                SysmlPackage.eINSTANCE.getReferenceUsage()));
    }

    private void registerBehavior(Map<String, SDVTheme> themes) {
        var theme = new SDVTheme(BEHAVIOR_NODE_BACKGROUND_COLOR, BEHAVIOR_COMPARTMENT_BACKGROUND_COLOR, BEHAVIOR_BORDER_COLOR);
        this.registerFamily(themes, theme, List.of(
                SysmlPackage.eINSTANCE.getActionDefinition(), SysmlPackage.eINSTANCE.getActionUsage(),
                SysmlPackage.eINSTANCE.getAcceptActionUsage(), SysmlPackage.eINSTANCE.getAssignmentActionUsage(),
                SysmlPackage.eINSTANCE.getPerformActionUsage(), SysmlPackage.eINSTANCE.getSendActionUsage(),
                SysmlPackage.eINSTANCE.getTerminateActionUsage(), SysmlPackage.eINSTANCE.getIfActionUsage(),
                SysmlPackage.eINSTANCE.getLoopActionUsage(), SysmlPackage.eINSTANCE.getForLoopActionUsage(),
                SysmlPackage.eINSTANCE.getWhileLoopActionUsage(), SysmlPackage.eINSTANCE.getAllocationDefinition(),
                SysmlPackage.eINSTANCE.getAllocationUsage(), SysmlPackage.eINSTANCE.getFlowDefinition(),
                SysmlPackage.eINSTANCE.getFlowUsage(), SysmlPackage.eINSTANCE.getFlow(),
                SysmlPackage.eINSTANCE.getSuccessionFlowUsage(), SysmlPackage.eINSTANCE.getTransitionUsage()));
    }

    private void registerStates(Map<String, SDVTheme> themes) {
        var theme = new SDVTheme(STATES_NODE_BACKGROUND_COLOR, STATES_COMPARTMENT_BACKGROUND_COLOR, STATES_BORDER_COLOR);
        this.registerFamily(themes, theme, List.of(
                SysmlPackage.eINSTANCE.getStateDefinition(), SysmlPackage.eINSTANCE.getStateUsage(),
                SysmlPackage.eINSTANCE.getExhibitStateUsage()));
    }

    private void registerRequirements(Map<String, SDVTheme> themes) {
        var theme = new SDVTheme(REQUIREMENTS_NODE_BACKGROUND_COLOR, REQUIREMENTS_COMPARTMENT_BACKGROUND_COLOR, REQUIREMENTS_BORDER_COLOR);
        this.registerFamily(themes, theme, List.of(
                SysmlPackage.eINSTANCE.getRequirementDefinition(), SysmlPackage.eINSTANCE.getRequirementUsage(),
                SysmlPackage.eINSTANCE.getConcernDefinition(), SysmlPackage.eINSTANCE.getConcernUsage(),
                SysmlPackage.eINSTANCE.getConstraintDefinition(), SysmlPackage.eINSTANCE.getConstraintUsage(),
                SysmlPackage.eINSTANCE.getAssertConstraintUsage(), SysmlPackage.eINSTANCE.getSatisfyRequirementUsage()));
    }

    private void registerAnalysis(Map<String, SDVTheme> themes) {
        var theme = new SDVTheme(ANALYSIS_NODE_BACKGROUND_COLOR, ANALYSIS_COMPARTMENT_BACKGROUND_COLOR, ANALYSIS_BORDER_COLOR);
        this.registerFamily(themes, theme, List.of(
                SysmlPackage.eINSTANCE.getCaseDefinition(), SysmlPackage.eINSTANCE.getCaseUsage(),
                SysmlPackage.eINSTANCE.getUseCaseDefinition(), SysmlPackage.eINSTANCE.getUseCaseUsage(),
                SysmlPackage.eINSTANCE.getAnalysisCaseDefinition(), SysmlPackage.eINSTANCE.getAnalysisCaseUsage(),
                SysmlPackage.eINSTANCE.getVerificationCaseDefinition(), SysmlPackage.eINSTANCE.getVerificationCaseUsage(),
                SysmlPackage.eINSTANCE.getCalculationDefinition(), SysmlPackage.eINSTANCE.getCalculationUsage()));
    }

    private void registerOccurrences(Map<String, SDVTheme> themes) {
        var theme = new SDVTheme(OCCURRENCES_NODE_BACKGROUND_COLOR, OCCURRENCES_COMPARTMENT_BACKGROUND_COLOR, OCCURRENCES_BORDER_COLOR);
        this.registerFamily(themes, theme, List.of(
                SysmlPackage.eINSTANCE.getOccurrenceDefinition(), SysmlPackage.eINSTANCE.getOccurrenceUsage(),
                SysmlPackage.eINSTANCE.getEventOccurrenceUsage()));
    }

    private void registerOrganization(Map<String, SDVTheme> themes) {
        var theme = new SDVTheme(ORGANIZATION_NODE_BACKGROUND_COLOR, ORGANIZATION_COMPARTMENT_BACKGROUND_COLOR, ORGANIZATION_BORDER_COLOR);
        this.registerFamily(themes, theme, List.of(
                SysmlPackage.eINSTANCE.getNamespaceImport(), SysmlPackage.eINSTANCE.getViewDefinition(),
                SysmlPackage.eINSTANCE.getViewpointDefinition(), SysmlPackage.eINSTANCE.getViewpointUsage(),
                SysmlPackage.eINSTANCE.getMetadataDefinition(), SysmlPackage.eINSTANCE.getMetadataUsage(),
                SysmlPackage.eINSTANCE.getComment(), SysmlPackage.eINSTANCE.getDocumentation()));
    }

    /**
     * @technical-debt Registers a lighter organization theme for custom container nodes (packages and views).
     * These nodes currently apply {@link SDVTheme#nodeBackgroundColor()} to their entire shape, including the area containing child nodes.
     * The intended rendering would use this color only for the header and {@link SDVTheme#compartmentBackgroundColor()} for the content area.
     * Until their renderers support separate header and content backgrounds, both colors use the light compartment background.
     * Once supported, these nodes can use the theme defined in {@link #registerOrganization(Map)}.
     */
    private void registerOrganizationCustomNode(Map<String, SDVTheme> themes) {
        var theme = new SDVTheme(ORGANIZATION_COMPARTMENT_BACKGROUND_COLOR, ORGANIZATION_COMPARTMENT_BACKGROUND_COLOR, ORGANIZATION_BORDER_COLOR);
        this.registerFamily(themes, theme, List.of(
                SysmlPackage.eINSTANCE.getPackage(), SysmlPackage.eINSTANCE.getViewUsage()
        ));
    }

    private void registerFamily(Map<String, SDVTheme> themes, SDVTheme theme, List<EClass> types) {
        var prefix = this.descriptionNameGenerator.getDiagramPrefix();
        for (var type : types) {
            var ownerPrefix = type.getName() + " ";
            themes.put(this.descriptionNameGenerator.getNodeName(type), theme);
            themes.put(this.descriptionNameGenerator.getCompartmentName(prefix, ownerPrefix), theme);
            themes.put(this.descriptionNameGenerator.getCompartmentItemName(prefix, ownerPrefix), theme);
            themes.put(this.descriptionNameGenerator.getInheritedCompartmentItemName(prefix, ownerPrefix), theme);
        }
    }
}
