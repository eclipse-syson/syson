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
import org.eclipse.syson.standard.diagrams.view.services.api.ISDVSemanticPaletteProvider;
import org.eclipse.syson.standard.diagrams.view.services.api.SDVSemanticPalette;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.springframework.stereotype.Service;

/**
 * Defines all standard diagram semantic colors and their description associations in one place.
 *
 * @author gcoutable
 */
@Service
public class SDVSemanticPaletteProvider implements ISDVSemanticPaletteProvider {

    private final IDescriptionNameGenerator descriptionNameGenerator = new SDVDescriptionNameGenerator();

    private final Map<String, SDVSemanticPalette> palettesByDescriptionName;

    public SDVSemanticPaletteProvider() {
        var palettes = new HashMap<String, SDVSemanticPalette>();
        this.registerStructure(palettes);
        this.registerBehavior(palettes);
        this.registerStates(palettes);
        this.registerRequirements(palettes);
        this.registerAnalysis(palettes);
        this.registerOccurrences(palettes);
        this.registerOrganization(palettes);
        this.palettesByDescriptionName = Map.copyOf(palettes);
    }

    @Override
    public Optional<SDVSemanticPalette> findPalette(String descriptionName) {
        Objects.requireNonNull(descriptionName);
        return Optional.ofNullable(this.palettesByDescriptionName.get(descriptionName))
                .or(() -> this.palettesByDescriptionName.entrySet().stream()
                        .filter(entry -> entry.getKey().endsWith(" ") && descriptionName.startsWith(entry.getKey()))
                        .map(Map.Entry::getValue)
                        .findFirst());
    }

    private void registerStructure(Map<String, SDVSemanticPalette> palettes) {
        this.registerFamily(palettes, new SDVSemanticPalette("#DCD9F5", "#FCFAFF", "#8275BC"), List.of(
                SysmlPackage.eINSTANCE.getPartDefinition(), SysmlPackage.eINSTANCE.getPartUsage(),
                SysmlPackage.eINSTANCE.getItemDefinition(), SysmlPackage.eINSTANCE.getItemUsage(),
                SysmlPackage.eINSTANCE.getAttributeDefinition(), SysmlPackage.eINSTANCE.getAttributeUsage(),
                SysmlPackage.eINSTANCE.getPortDefinition(), SysmlPackage.eINSTANCE.getPortUsage(),
                SysmlPackage.eINSTANCE.getInterfaceDefinition(), SysmlPackage.eINSTANCE.getInterfaceUsage(),
                SysmlPackage.eINSTANCE.getConnectionDefinition(), SysmlPackage.eINSTANCE.getConnectionUsage(),
                SysmlPackage.eINSTANCE.getReferenceUsage()));
    }

    private void registerBehavior(Map<String, SDVSemanticPalette> palettes) {
        var palette = new SDVSemanticPalette("#DCF5C4", "#FAFEFC", "#186A3B");
        this.registerFamily(palettes, palette, List.of(
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

    private void registerStates(Map<String, SDVSemanticPalette> palettes) {
        this.registerFamily(palettes, new SDVSemanticPalette("#D5F5E3", "#FAFEFC", "#186A3B"), List.of(
                SysmlPackage.eINSTANCE.getStateDefinition(), SysmlPackage.eINSTANCE.getStateUsage(),
                SysmlPackage.eINSTANCE.getExhibitStateUsage()));
    }

    private void registerRequirements(Map<String, SDVSemanticPalette> palettes) {
        this.registerFamily(palettes, new SDVSemanticPalette("#E5DDE9", "#FFFBFD", "#AA6C86"), List.of(
                SysmlPackage.eINSTANCE.getRequirementDefinition(), SysmlPackage.eINSTANCE.getRequirementUsage(),
                SysmlPackage.eINSTANCE.getConcernDefinition(), SysmlPackage.eINSTANCE.getConcernUsage(),
                SysmlPackage.eINSTANCE.getConstraintDefinition(), SysmlPackage.eINSTANCE.getConstraintUsage(),
                SysmlPackage.eINSTANCE.getAssertConstraintUsage(), SysmlPackage.eINSTANCE.getSatisfyRequirementUsage()));
    }

    private void registerAnalysis(Map<String, SDVSemanticPalette> palettes) {
        this.registerFamily(palettes, new SDVSemanticPalette("#F2DFC3", "#FFFEF9", "#A1843B"), List.of(
                SysmlPackage.eINSTANCE.getCaseDefinition(), SysmlPackage.eINSTANCE.getCaseUsage(),
                SysmlPackage.eINSTANCE.getUseCaseDefinition(), SysmlPackage.eINSTANCE.getUseCaseUsage(),
                SysmlPackage.eINSTANCE.getAnalysisCaseDefinition(), SysmlPackage.eINSTANCE.getAnalysisCaseUsage(),
                SysmlPackage.eINSTANCE.getVerificationCaseDefinition(), SysmlPackage.eINSTANCE.getVerificationCaseUsage(),
                SysmlPackage.eINSTANCE.getCalculationDefinition(), SysmlPackage.eINSTANCE.getCalculationUsage()));
    }

    private void registerOccurrences(Map<String, SDVSemanticPalette> palettes) {
        this.registerFamily(palettes, new SDVSemanticPalette("#CEF2FF", "#FAFDFF", "#3F8DB8"), List.of(
                SysmlPackage.eINSTANCE.getOccurrenceDefinition(), SysmlPackage.eINSTANCE.getOccurrenceUsage(),
                SysmlPackage.eINSTANCE.getEventOccurrenceUsage()));
    }

    private void registerOrganization(Map<String, SDVSemanticPalette> palettes) {
        this.registerFamily(palettes, new SDVSemanticPalette("#E2E5ED", "#FDFDFE", "#818896"), List.of(
                SysmlPackage.eINSTANCE.getPackage(), SysmlPackage.eINSTANCE.getNamespaceImport(),
                SysmlPackage.eINSTANCE.getViewDefinition(), SysmlPackage.eINSTANCE.getViewUsage(),
                SysmlPackage.eINSTANCE.getViewpointDefinition(), SysmlPackage.eINSTANCE.getViewpointUsage(),
                SysmlPackage.eINSTANCE.getMetadataDefinition(), SysmlPackage.eINSTANCE.getMetadataUsage(),
                SysmlPackage.eINSTANCE.getComment(), SysmlPackage.eINSTANCE.getDocumentation()));
    }

    private void registerFamily(Map<String, SDVSemanticPalette> palettes, SDVSemanticPalette palette, List<EClass> types) {
        var prefix = this.descriptionNameGenerator.getDiagramPrefix();
        for (var type : types) {
            var ownerPrefix = type.getName() + " ";
            palettes.put(this.descriptionNameGenerator.getNodeName(type), palette);
            palettes.put(this.descriptionNameGenerator.getCompartmentName(prefix, ownerPrefix), palette);
            palettes.put(this.descriptionNameGenerator.getCompartmentItemName(prefix, ownerPrefix), palette);
            palettes.put(this.descriptionNameGenerator.getInheritedCompartmentItemName(prefix, ownerPrefix), palette);
        }
    }
}
