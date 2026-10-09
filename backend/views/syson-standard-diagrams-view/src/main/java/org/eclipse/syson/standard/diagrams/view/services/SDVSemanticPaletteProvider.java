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
        var sysml = SysmlPackage.eINSTANCE;
        this.registerFamily(palettes, new SDVSemanticPalette("#DCD9F5", "#FCFAFF", "#8275BC"), List.of(
                sysml.getPartDefinition(), sysml.getPartUsage(),
                sysml.getItemDefinition(), sysml.getItemUsage(),
                sysml.getAttributeDefinition(), sysml.getAttributeUsage(),
                sysml.getPortDefinition(), sysml.getPortUsage(),
                sysml.getInterfaceDefinition(), sysml.getInterfaceUsage(),
                sysml.getConnectionDefinition(), sysml.getConnectionUsage(), sysml.getReferenceUsage()));
    }

    private void registerBehavior(Map<String, SDVSemanticPalette> palettes) {
        var sysml = SysmlPackage.eINSTANCE;
        var palette = new SDVSemanticPalette("#DCF5C4", "#FAFEFC", "#186A3B");
        this.registerFamily(palettes, palette, List.of(
                sysml.getActionDefinition(), sysml.getActionUsage(), sysml.getAcceptActionUsage(),
                sysml.getAssignmentActionUsage(), sysml.getPerformActionUsage(), sysml.getSendActionUsage(),
                sysml.getTerminateActionUsage(), sysml.getIfActionUsage(), sysml.getLoopActionUsage(),
                sysml.getForLoopActionUsage(), sysml.getWhileLoopActionUsage(),
                sysml.getAllocationDefinition(), sysml.getAllocationUsage(),
                sysml.getFlowDefinition(), sysml.getFlowUsage(), sysml.getFlow(), sysml.getSuccessionFlowUsage(), sysml.getTransitionUsage()));
    }

    private void registerStates(Map<String, SDVSemanticPalette> palettes) {
        var sysml = SysmlPackage.eINSTANCE;
        this.registerFamily(palettes, new SDVSemanticPalette("#D5F5E3", "#FAFEFC", "#186A3B"), List.of(
                sysml.getStateDefinition(), sysml.getStateUsage(), sysml.getExhibitStateUsage()));
    }

    private void registerRequirements(Map<String, SDVSemanticPalette> palettes) {
        var sysml = SysmlPackage.eINSTANCE;
        this.registerFamily(palettes, new SDVSemanticPalette("#E5DDE9", "#FFFBFD", "#AA6C86"), List.of(
                sysml.getRequirementDefinition(), sysml.getRequirementUsage(),
                sysml.getConcernDefinition(), sysml.getConcernUsage(),
                sysml.getConstraintDefinition(), sysml.getConstraintUsage(), sysml.getAssertConstraintUsage(), sysml.getSatisfyRequirementUsage()));
    }

    private void registerAnalysis(Map<String, SDVSemanticPalette> palettes) {
        var sysml = SysmlPackage.eINSTANCE;
        this.registerFamily(palettes, new SDVSemanticPalette("#F2DFC3", "#FFFEF9", "#A1843B"), List.of(
                sysml.getCaseDefinition(), sysml.getCaseUsage(),
                sysml.getUseCaseDefinition(), sysml.getUseCaseUsage(),
                sysml.getAnalysisCaseDefinition(), sysml.getAnalysisCaseUsage(),
                sysml.getVerificationCaseDefinition(), sysml.getVerificationCaseUsage(),
                sysml.getCalculationDefinition(), sysml.getCalculationUsage()));
    }

    private void registerOccurrences(Map<String, SDVSemanticPalette> palettes) {
        var sysml = SysmlPackage.eINSTANCE;
        this.registerFamily(palettes, new SDVSemanticPalette("#CEF2FF", "#FAFDFF", "#3F8DB8"), List.of(
                sysml.getOccurrenceDefinition(), sysml.getOccurrenceUsage(), sysml.getEventOccurrenceUsage()));
    }

    private void registerOrganization(Map<String, SDVSemanticPalette> palettes) {
        var sysml = SysmlPackage.eINSTANCE;
        this.registerFamily(palettes, new SDVSemanticPalette("#E2E5ED", "#FDFDFE", "#818896"), List.of(
                sysml.getPackage(), sysml.getNamespaceImport(),
                sysml.getViewDefinition(), sysml.getViewUsage(), sysml.getViewpointDefinition(), sysml.getViewpointUsage(),
                sysml.getMetadataDefinition(), sysml.getMetadataUsage(), sysml.getComment(), sysml.getDocumentation()));
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
