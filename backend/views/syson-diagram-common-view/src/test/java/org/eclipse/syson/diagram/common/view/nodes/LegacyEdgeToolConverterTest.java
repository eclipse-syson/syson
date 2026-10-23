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
package org.eclipse.syson.diagram.common.view.nodes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramQueryService;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.interpreter.AQLInterpreter;
import org.eclipse.sirius.components.view.builder.generated.diagram.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.view.ViewBuilders;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.components.view.emf.diagram.api.IViewDiagramDescriptionSearchService;
import org.eclipse.syson.diagram.services.DiagramQueryElementService;
import org.eclipse.syson.model.services.ModelQueryElementService;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.DescriptionNameGenerator;
import org.junit.jupiter.api.Test;

/**
 * Tests connector palette conversion and evaluates its target and action preconditions with AQL.
 *
 * @author mcharfadi
 */
public class LegacyEdgeToolConverterTest {

    private final DiagramBuilders builders = new DiagramBuilders();

    private final IEditingContext editingContext = mock(IEditingContext.class);

    private final IViewDiagramDescriptionSearchService descriptionSearchService = mock(IViewDiagramDescriptionSearchService.class);

    @Test
    public void preservesActionsAndDeduplicatesParentTargets() {
        var firstTarget = this.builders.newNodeDescription().name("Port").build();
        var secondTarget = this.builders.newNodeDescription().name("Inherited port").build();
        var body = new ViewBuilders().newChangeContext().expression("aql:self").build();
        var dialog = this.builders.newSelectionDialogDescription().build();
        var firstAction = this.builders.newEdgeTool().name("First").iconURLsExpression("icon.svg")
                .body(body).dialogDescription(dialog).elementsToSelectExpression("aql:self")
                .targetElementDescriptions(firstTarget).build();
        var secondAction = this.builders.newEdgeTool().name("Second").targetElementDescriptions(firstTarget, secondTarget).build();

        var converted = new LegacyEdgeToolConverter().convertToEdgeToolsWithPalette(List.of(firstAction, secondAction));

        assertThat(converted).hasSize(1);
        assertThat(converted.getFirst().getTargetElementDescriptions()).containsExactly(firstTarget, secondTarget);
        assertThat(converted.getFirst().getPalette().getNodeTools()).extracting(NodeTool::getName).containsExactly("First", "Second");
        var convertedAction = converted.getFirst().getPalette().getNodeTools().getFirst();
        assertThat(convertedAction.getBody()).containsExactly(body);
        assertThat(convertedAction.getDialogDescription()).isSameAs(dialog);
        assertThat(convertedAction.getIconURLsExpression()).isEqualTo("icon.svg");
        assertThat(convertedAction.getElementsToSelectExpression()).isEqualTo("aql:self");
    }

    @Test
    public void distinguishesVisualDescriptionsAndPreservesOriginalPreconditions() {
        var ordinaryDescription = this.builders.newNodeDescription().name("Port").domainType("sysml::PortUsage").build();
        var inheritedDescription = this.builders.newNodeDescription().name("Inherited port").domainType("sysml::PortUsage").build();
        when(this.descriptionSearchService.findViewNodeDescriptionById(this.editingContext, "ordinary")).thenReturn(Optional.of(ordinaryDescription));
        when(this.descriptionSearchService.findViewNodeDescriptionById(this.editingContext, "inherited")).thenReturn(Optional.of(inheritedDescription));
        var ordinaryAction = this.builders.newEdgeTool().name("Ordinary").targetElementDescriptions(ordinaryDescription).preconditionExpression("aql:enabled").build();
        var inheritedAction = this.builders.newEdgeTool().name("Inherited").targetElementDescriptions(inheritedDescription).build();
        var actions = new LegacyEdgeToolConverter().convertToEdgeToolsWithPalette(List.of(ordinaryAction, inheritedAction)).getFirst().getPalette().getNodeTools();
        var target = mock(Node.class);
        when(target.getDescriptionId()).thenReturn("ordinary");

        assertThat(this.isAvailable(actions.getFirst(), target, true)).isTrue();
        assertThat(this.isAvailable(actions.getFirst(), target, false)).isFalse();
        assertThat(this.isAvailable(actions.get(1), target, true)).isFalse();

        when(target.getDescriptionId()).thenReturn("inherited");
        assertThat(this.isAvailable(actions.getFirst(), target, true)).isFalse();
        assertThat(this.isAvailable(actions.get(1), target, true)).isTrue();
    }

    @Test
    public void handlesEmptyToolsEmptyTargetsAndLiteralPreconditions() {
        var helper = new LegacyEdgeToolConverter();
        assertThat(helper.convertToEdgeToolsWithPalette(List.of())).isEmpty();
        var noTargets = this.builders.newEdgeTool().name("No targets").build();
        var noTargetsAction = helper.convertToEdgeToolsWithPalette(List.of(noTargets)).getFirst().getPalette().getNodeTools().getFirst();
        assertThat(this.isAvailable(noTargetsAction, mock(Node.class), true)).isFalse();

        var description = this.builders.newNodeDescription().name("Port").build();
        when(this.descriptionSearchService.findViewNodeDescriptionById(this.editingContext, "port")).thenReturn(Optional.of(description));
        var disabled = this.builders.newEdgeTool().targetElementDescriptions(description).preconditionExpression("false").build();
        var disabledAction = helper.convertToEdgeToolsWithPalette(List.of(disabled)).getFirst().getPalette().getNodeTools().getFirst();
        var target = mock(Node.class);
        when(target.getDescriptionId()).thenReturn("port");
        assertThat(this.isAvailable(disabledAction, target, true)).isFalse();
    }

    @Test
    public void supportsEdgeTargetsEscapedNamesAndMissingDescriptions() {
        var descriptionName = new DescriptionNameGenerator("GV").getBorderNodeName(SysmlPackage.eINSTANCE.getPortUsage());
        var description = this.builders.newNodeDescription().name(descriptionName).build();
        when(this.descriptionSearchService.findViewNodeDescriptionById(this.editingContext, "port")).thenReturn(Optional.of(description));
        var action = this.builders.newEdgeTool().targetElementDescriptions(description).preconditionExpression(" ").build();
        var converted = new LegacyEdgeToolConverter().convertToEdgeToolsWithPalette(List.of(action)).getFirst().getPalette().getNodeTools().getFirst();
        var target = mock(Node.class);
        when(target.getDescriptionId()).thenReturn("port");
        assertThat(this.isAvailable(converted, target, true)).isTrue();

        when(target.getDescriptionId()).thenReturn("missing");
        assertThat(this.isAvailable(converted, target, true)).isFalse();
    }

    private boolean isAvailable(NodeTool action, Object target, boolean enabled) {
        var service = new DiagramQueryElementService(mock(IObjectSearchService.class), this.descriptionSearchService, mock(IDiagramIdProvider.class),
                mock(IFeedbackMessageService.class), mock(IDiagramQueryService.class), mock(ModelQueryElementService.class));
        var interpreter = new AQLInterpreter(List.of(), List.of(service), List.of());
        var result = interpreter.evaluateExpression(Map.of("edgeTarget", target, "editingContext", this.editingContext, "enabled", enabled), action.getPreconditionExpression());
        assertThat(result.asBoolean()).as(action.getPreconditionExpression()).isPresent();
        return result.asBoolean().orElseThrow();
    }
}
