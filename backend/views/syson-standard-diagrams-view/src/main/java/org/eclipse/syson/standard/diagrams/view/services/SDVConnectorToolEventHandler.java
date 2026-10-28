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

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.sirius.components.collaborative.api.ChangeDescription;
import org.eclipse.sirius.components.collaborative.api.ChangeKind;
import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramEventHandler;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.InvokeSingleClickOnTwoDiagramElementsToolInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.InvokeSingleClickOnTwoDiagramElementsToolSuccessPayload;
import org.eclipse.sirius.components.collaborative.diagrams.handlers.InvokeSingleClickOnTwoDiagramElementsToolEventHandler;
import org.eclipse.sirius.components.collaborative.representations.api.IRepresentationEventProcessorRegistry;
import org.eclipse.sirius.components.core.api.ErrorPayload;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IPayload;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.representations.Failure;
import org.eclipse.sirius.components.representations.IStatus;
import org.eclipse.sirius.components.representations.Success;
import org.eclipse.sirius.components.representations.WorkbenchSelection;
import org.eclipse.sirius.components.view.View;
import org.eclipse.sirius.components.view.emf.api.IViewAQLInterpreterFactory;
import org.eclipse.sirius.components.view.emf.diagram.api.IViewToolFinder;
import org.eclipse.sirius.components.view.emf.diagram.tools.api.ISingleClickOnTwoDiagramElementsVariableManagerProvider;
import org.eclipse.sirius.components.view.emf.diagram.tools.api.IToolExecutor;
import org.eclipse.syson.diagram.services.DiagramQueryElementService;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.services.api.ViewDefinitionKind;
import org.eclipse.syson.util.NodeFinder;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Sinks.Many;
import reactor.core.publisher.Sinks.One;

/**
 * Executes Interconnection View connector palette tools with the event processor's diagram context so graphical changes
 * survive the subsequent refresh. This class should be removed when
 * https://github.com/eclipse-sirius/sirius-web/issues/7120 has been fixed.
 *
 * @author arichard
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Service
public class SDVConnectorToolEventHandler implements IDiagramEventHandler {

    private final IRepresentationEventProcessorRegistry representationEventProcessorRegistry;

    private final InvokeSingleClickOnTwoDiagramElementsToolEventHandler defaultHandler;

    private final DiagramQueryElementService diagramQueryElementService;

    private final UtilService utilService = new UtilService();

    private final IViewToolFinder viewToolFinder;

    private final IViewAQLInterpreterFactory interpreterFactory;

    private final ISingleClickOnTwoDiagramElementsVariableManagerProvider variableManagerProvider;

    private final IToolExecutor toolExecutor;

    /**
     * Creates the connector handler.
     *
     * @param representationEventProcessorRegistry
     *            the registry of active representations used to restrict this handler to standard diagrams
     * @param defaultHandler
     *            the fallback for other connector implementations
     * @param diagramQueryElementService
     *            the service locating the source node's containing view
     * @param viewToolFinder
     *            the connector palette tool finder
     * @param interpreterFactory
     *            the interpreter factory for the view's services
     * @param variableManagerProvider
     *            the provider of connector tool variables
     * @param toolExecutor
     *            the executor of view operations
     */
    public SDVConnectorToolEventHandler(IRepresentationEventProcessorRegistry representationEventProcessorRegistry,
            InvokeSingleClickOnTwoDiagramElementsToolEventHandler defaultHandler, DiagramQueryElementService diagramQueryElementService,
            IViewToolFinder viewToolFinder, IViewAQLInterpreterFactory interpreterFactory,
            ISingleClickOnTwoDiagramElementsVariableManagerProvider variableManagerProvider, IToolExecutor toolExecutor) {
        this.representationEventProcessorRegistry = Objects.requireNonNull(representationEventProcessorRegistry);
        this.defaultHandler = Objects.requireNonNull(defaultHandler);
        this.diagramQueryElementService = Objects.requireNonNull(diagramQueryElementService);
        this.viewToolFinder = Objects.requireNonNull(viewToolFinder);
        this.interpreterFactory = Objects.requireNonNull(interpreterFactory);
        this.variableManagerProvider = Objects.requireNonNull(variableManagerProvider);
        this.toolExecutor = Objects.requireNonNull(toolExecutor);
    }

    /**
     * Checks the active representation in memory without loading representation metadata.
     *
     * @param editingContext
     *            the editing context receiving the input
     * @param diagramInput
     *            the diagram event to dispatch
     * @return whether the input targets a standard diagram's connector tool
     */
    @Override
    public boolean canHandle(IEditingContext editingContext, IDiagramInput diagramInput) {
        if (diagramInput instanceof InvokeSingleClickOnTwoDiagramElementsToolInput input) {
            return Optional.ofNullable(this.representationEventProcessorRegistry.get(editingContext.getId(), input.representationId()))
                    .map(entry -> entry.getRepresentationEventProcessor().getRepresentation())
                    .filter(Diagram.class::isInstance)
                    .map(Diagram.class::cast)
                    .filter(diagram -> SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID.equals(diagram.getDescriptionId()))
                    .isPresent();
        }
        return false;
    }

    @Override
    public void handle(One<IPayload> payloadSink, Many<ChangeDescription> changeDescriptionSink, IEditingContext editingContext,
            DiagramContext diagramContext, IDiagramInput diagramInput) {
        if (diagramInput instanceof InvokeSingleClickOnTwoDiagramElementsToolInput input) {
            var optionalStatus = this.executeTool(editingContext, diagramContext, input);
            if (optionalStatus.isEmpty()) {
                this.defaultHandler.handle(payloadSink, changeDescriptionSink, editingContext, diagramContext, diagramInput);
                return;
            }
            var status = optionalStatus.get();
            IPayload payload = new ErrorPayload(input.id(), "Unable to execute the connector tool");
            String changeKind = ChangeKind.NOTHING;
            if (status instanceof Success success && success.getParameters().get(Success.NEW_SELECTION) instanceof WorkbenchSelection selection) {
                payload = new InvokeSingleClickOnTwoDiagramElementsToolSuccessPayload(input.id(), selection, success.getMessages());
                changeKind = ChangeKind.SEMANTIC_CHANGE;
            } else if (status instanceof Failure failure) {
                payload = new ErrorPayload(input.id(), failure.getMessages());
            }
            payloadSink.tryEmitValue(payload);
            changeDescriptionSink.tryEmitNext(new ChangeDescription(changeKind, input.representationId(), input));
        }
    }

    /**
     * Executes an Interconnection View connector palette tool using the shared diagram context.
     * Resolves the containing view before preparing tool variables or creating an interpreter.
     *
     * @param editingContext
     *            the editing context containing the model
     * @param diagramContext
     *            the diagram context retained by the event processor
     * @param input
     *            the connector tool input
     * @return the tool execution status, or empty if the default connector handler should execute the tool
     */
    private Optional<IStatus> executeTool(IEditingContext editingContext,
            DiagramContext diagramContext, InvokeSingleClickOnTwoDiagramElementsToolInput input) {
        Optional<IStatus> status = Optional.empty();
        var source = new NodeFinder(diagramContext.diagram()).getOneNodeMatching(node -> input.diagramSourceElementId().equals(node.getId()));
        var viewUsage = source.map(node -> this.diagramQueryElementService.getViewUsage(editingContext, diagramContext, node)).orElse(null);
        if (source.isPresent() && ViewDefinitionKind.isInterconnectionView(this.utilService.getViewDefinitionKind(viewUsage, List.of(), editingContext))) {
            var tool = this.viewToolFinder.findConnectorNodeTool(editingContext, source.get().getDescriptionId(), input.toolId());
            if (tool.isPresent() && EcoreUtil.getRootContainer(tool.get()) instanceof View view) {
                var variables = this.variableManagerProvider.getVariableManager(editingContext, diagramContext,
                        input.diagramSourceElementId(), input.diagramTargetElementId(), input.variables());
                if (variables.isPresent()) {
                    // shortcut: Sirius Web 2026.9.4 discards connector diagram events, remove when its executor shares the diagram context.
                    var interpreter = this.interpreterFactory.createInterpreter(editingContext, view);
                    status = Optional.of(this.toolExecutor.executeTool(tool.get(), interpreter, variables.get().createChild()));
                }
            }
        }
        return status;
    }
}
