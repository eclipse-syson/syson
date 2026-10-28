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
package org.eclipse.syson.standard.diagrams.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.sirius.components.collaborative.api.ChangeDescription;
import org.eclipse.sirius.components.collaborative.api.ChangeKind;
import org.eclipse.sirius.components.collaborative.api.IRepresentationEventProcessor;
import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.InvokeSingleClickOnTwoDiagramElementsToolInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.InvokeSingleClickOnTwoDiagramElementsToolSuccessPayload;
import org.eclipse.sirius.components.collaborative.diagrams.handlers.InvokeSingleClickOnTwoDiagramElementsToolEventHandler;
import org.eclipse.sirius.components.collaborative.editingcontext.RepresentationEventProcessorEntry;
import org.eclipse.sirius.components.collaborative.representations.api.IRepresentationEventProcessorRegistry;
import org.eclipse.sirius.components.core.api.ErrorPayload;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IPayload;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.interpreter.AQLInterpreter;
import org.eclipse.sirius.components.representations.Failure;
import org.eclipse.sirius.components.representations.IRepresentation;
import org.eclipse.sirius.components.representations.IStatus;
import org.eclipse.sirius.components.representations.Success;
import org.eclipse.sirius.components.representations.VariableManager;
import org.eclipse.sirius.components.representations.WorkbenchSelection;
import org.eclipse.sirius.components.view.View;
import org.eclipse.sirius.components.view.builder.generated.diagram.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.view.ViewBuilders;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.emf.api.IViewAQLInterpreterFactory;
import org.eclipse.sirius.components.view.emf.diagram.api.IViewToolFinder;
import org.eclipse.sirius.components.view.emf.diagram.tools.api.ISingleClickOnTwoDiagramElementsVariableManagerProvider;
import org.eclipse.sirius.components.view.emf.diagram.tools.api.IToolExecutor;
import org.eclipse.syson.diagram.services.DiagramQueryElementService;
import org.eclipse.syson.standard.diagrams.view.services.SDVConnectorToolEventHandler;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.util.StandardDiagramsConstants;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import reactor.core.publisher.Sinks;

/**
 * Tests routing, execution costs, and fallback of the standard connector tool compatibility handler.
 *
 * @author arichard
 */
public class SDVConnectorToolEventHandlerTests {

    private final IRepresentationEventProcessorRegistry registry = mock(IRepresentationEventProcessorRegistry.class);

    private final InvokeSingleClickOnTwoDiagramElementsToolEventHandler defaultHandler = mock(InvokeSingleClickOnTwoDiagramElementsToolEventHandler.class);

    private final DiagramQueryElementService diagramQueryElementService = mock(DiagramQueryElementService.class);

    private final IViewToolFinder toolFinder = mock(IViewToolFinder.class);

    private final IViewAQLInterpreterFactory interpreterFactory = mock(IViewAQLInterpreterFactory.class);

    private final ISingleClickOnTwoDiagramElementsVariableManagerProvider variableManagerProvider = mock(ISingleClickOnTwoDiagramElementsVariableManagerProvider.class);

    private final IToolExecutor toolExecutor = mock(IToolExecutor.class);

    private final SDVConnectorToolEventHandler handler = new SDVConnectorToolEventHandler(this.registry, this.defaultHandler,
            this.diagramQueryElementService, this.toolFinder, this.interpreterFactory, this.variableManagerProvider, this.toolExecutor);

    private final IEditingContext editingContext = mock(IEditingContext.class);

    private final Diagram diagram = mock(Diagram.class);

    private final Node source = mock(Node.class);

    private final DiagramContext diagramContext = new DiagramContext(this.diagram);

    private final InvokeSingleClickOnTwoDiagramElementsToolInput input = new InvokeSingleClickOnTwoDiagramElementsToolInput(
            UUID.randomUUID(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), "source", "target", 0, 0, 0, 0, "tool", List.of());

    private final View view = new ViewBuilders().newView().build();

    private final NodeTool tool = new DiagramBuilders().newNodeTool().build();

    private final VariableManager variables = new VariableManager();

    private final AQLInterpreter interpreter = mock(AQLInterpreter.class);

    private final Sinks.One<IPayload> payloadSink = Sinks.one();

    private final Sinks.Many<ChangeDescription> changeSink = Sinks.many().unicast().onBackpressureBuffer();

    /**
     * Provides a valid source node and a connector tool attached to its owning View.
     */
    @BeforeEach
    public void setUp() {
        when(this.editingContext.getId()).thenReturn(this.input.editingContextId());
        when(this.diagram.getDescriptionId()).thenReturn(SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        when(this.diagram.getNodes()).thenReturn(List.of(this.source));
        when(this.source.getId()).thenReturn(this.input.diagramSourceElementId());
        when(this.source.getDescriptionId()).thenReturn("source-description");
        var builders = new DiagramBuilders();
        var palette = builders.newNodePalette().nodeTools(this.tool).build();
        var nodeDescription = builders.newNodeDescription().palette(palette).build();
        this.view.getDescriptions().add(builders.newDiagramDescription().nodeDescriptions(nodeDescription).build());
    }

    /**
     * Checks active representations without loading metadata or claiming other representation kinds.
     */
    @Test
    public void handlesOnlyActiveStandardDiagrams() {
        var entry = mock(RepresentationEventProcessorEntry.class);
        var processor = mock(IRepresentationEventProcessor.class);
        when(this.registry.get(this.editingContext.getId(), this.input.representationId())).thenReturn(entry);
        when(entry.getRepresentationEventProcessor()).thenReturn(processor);
        when(processor.getRepresentation()).thenReturn(this.diagram);
        assertThat(this.handler.canHandle(this.editingContext, this.input)).isTrue();

        when(this.diagram.getDescriptionId()).thenReturn("another-diagram-description");
        assertThat(this.handler.canHandle(this.editingContext, this.input)).isFalse();

        when(processor.getRepresentation()).thenReturn(new IRepresentation.NoOp());
        assertThat(this.handler.canHandle(this.editingContext, this.input)).isFalse();

        when(this.registry.get(this.editingContext.getId(), this.input.representationId())).thenReturn(null);
        assertThat(this.handler.canHandle(this.editingContext, this.input)).isFalse();
    }

    /**
     * Checks that unrelated events do not even access the active representation registry.
     */
    @Test
    public void ignoresOtherInputs() {
        assertThat(this.handler.canHandle(this.editingContext, mock(IDiagramInput.class))).isFalse();
        verifyNoInteractions(this.registry);
    }

    /**
     * Checks that non-IV connectors delegate before any execution machinery is prepared.
     *
     * @param qualifiedName
     *            the containing view definition's qualified name
     */
    @ParameterizedTest
    @ValueSource(strings = { StandardDiagramsConstants.GV_QN, StandardDiagramsConstants.AFV_QN, StandardDiagramsConstants.STV_QN, "unknown-view" })
    public void delegatesOtherViewKindsWithoutPreparingExecution(String qualifiedName) {
        this.givenContainingView(qualifiedName);
        this.handle();

        this.checkDelegation();
        verifyNoInteractions(this.toolFinder, this.variableManagerProvider, this.interpreterFactory, this.toolExecutor);
    }

    /**
     * Checks that a missing source falls back without preparing execution.
     */
    @Test
    public void delegatesMissingSource() {
        when(this.diagram.getNodes()).thenReturn(List.of());
        this.handle();
        this.checkDelegation();
        verifyNoInteractions(this.diagramQueryElementService, this.toolFinder, this.variableManagerProvider, this.interpreterFactory, this.toolExecutor);
    }

    /**
     * Checks that a source without a containing view uses the default connector implementation.
     */
    @Test
    public void delegatesMissingContainingView() {
        this.handle();
        this.checkDelegation();
        verifyNoInteractions(this.toolFinder, this.variableManagerProvider, this.interpreterFactory, this.toolExecutor);
    }

    /**
     * Checks that an unsupported connector does not prepare variables or create an interpreter.
     */
    @Test
    public void delegatesUnsupportedTool() {
        this.givenContainingView(StandardDiagramsConstants.IV_QN);
        this.handle();
        this.checkDelegation();
        verifyNoInteractions(this.variableManagerProvider, this.interpreterFactory, this.toolExecutor);
    }

    /**
     * Checks that unavailable connector variables do not create an interpreter.
     */
    @Test
    public void delegatesMissingVariables() {
        this.givenExecutableTool(new Success());
        when(this.variableManagerProvider.getVariableManager(this.editingContext, this.diagramContext,
                this.input.diagramSourceElementId(), this.input.diagramTargetElementId(), this.input.variables())).thenReturn(Optional.empty());
        this.handle();
        this.checkDelegation();
        verifyNoInteractions(this.interpreterFactory, this.toolExecutor);
    }

    /**
     * Checks one-time preparation and execution with the original diagram context and the tool's owning View.
     */
    @Test
    public void executesInterconnectionToolOnceWithSharedContext() {
        var selection = new WorkbenchSelection();
        this.givenExecutableTool(new Success(ChangeKind.SEMANTIC_CHANGE, Map.of(Success.NEW_SELECTION, selection)));
        this.handle();

        verify(this.diagramQueryElementService).getViewUsage(this.editingContext, this.diagramContext, this.source);
        verify(this.variableManagerProvider).getVariableManager(this.editingContext, this.diagramContext,
                this.input.diagramSourceElementId(), this.input.diagramTargetElementId(), this.input.variables());
        verify(this.interpreterFactory).createInterpreter(this.editingContext, this.view);
        verify(this.toolExecutor).executeTool(eq(this.tool), eq(this.interpreter), any(VariableManager.class));
        verifyNoInteractions(this.defaultHandler);
        assertThat(this.payloadSink.asMono().block(Duration.ofSeconds(1)))
                .isInstanceOf(InvokeSingleClickOnTwoDiagramElementsToolSuccessPayload.class);
        assertThat(this.changeSink.asFlux().next().block(Duration.ofSeconds(1)).getKind()).isEqualTo(ChangeKind.SEMANTIC_CHANGE);
    }

    /**
     * Checks that finding the first node avoids visiting the rest of a large diagram.
     */
    @Test
    public void stopsSearchingAfterFindingSource() {
        var tail = mock(Node.class);
        var nodes = new ArrayList<>(Collections.nCopies(10_000, tail));
        nodes.addFirst(this.source);
        when(this.diagram.getNodes()).thenReturn(nodes);
        this.givenContainingView(StandardDiagramsConstants.GV_QN);
        this.handle();

        verifyNoInteractions(tail);
        this.checkDelegation();
    }

    /**
     * Checks that tool failures retain their messages and do not request a semantic refresh.
     */
    @Test
    public void preservesToolFailures() {
        var failure = new Failure("tool failed");
        this.givenExecutableTool(failure);
        this.handle();

        assertThat(this.payloadSink.asMono().block(Duration.ofSeconds(1))).isEqualTo(new ErrorPayload(this.input.id(), failure.getMessages()));
        assertThat(this.changeSink.asFlux().next().block(Duration.ofSeconds(1)).getKind()).isEqualTo(ChangeKind.NOTHING);
        verifyNoInteractions(this.defaultHandler);
    }

    /**
     * Provides the nearest containing view returned by the existing graphical view lookup service.
     *
     * @param qualifiedName
     *            the containing view definition's qualified name
     */
    private void givenContainingView(String qualifiedName) {
        var viewUsage = mock(ViewUsage.class);
        var type = mock(Type.class);
        when(type.getQualifiedName()).thenReturn(qualifiedName);
        when(viewUsage.getType()).thenReturn(new BasicEList<>(List.of(type)));
        when(this.diagramQueryElementService.getViewUsage(this.editingContext, this.diagramContext, this.source)).thenReturn(viewUsage);
    }

    /**
     * Provides the executable IV tool and its execution collaborators.
     *
     * @param status
     *            the status returned by the tool executor
     */
    private void givenExecutableTool(IStatus status) {
        this.givenContainingView(StandardDiagramsConstants.IV_QN);
        when(this.toolFinder.findConnectorNodeTool(this.editingContext, this.source.getDescriptionId(), this.input.toolId())).thenReturn(Optional.of(this.tool));
        when(this.variableManagerProvider.getVariableManager(this.editingContext, this.diagramContext,
                this.input.diagramSourceElementId(), this.input.diagramTargetElementId(), this.input.variables())).thenReturn(Optional.of(this.variables));
        when(this.interpreterFactory.createInterpreter(this.editingContext, this.view)).thenReturn(this.interpreter);
        when(this.toolExecutor.executeTool(eq(this.tool), eq(this.interpreter), any(VariableManager.class))).thenReturn(status);
    }

    /**
     * Executes the connector handler with the test's original diagram context.
     */
    private void handle() {
        this.handler.handle(this.payloadSink, this.changeSink, this.editingContext, this.diagramContext, this.input);
    }

    /**
     * Checks that fallback is invoked exactly once with the original input and context.
     */
    private void checkDelegation() {
        verify(this.defaultHandler).handle(this.payloadSink, this.changeSink, this.editingContext, this.diagramContext, this.input);
    }
}
