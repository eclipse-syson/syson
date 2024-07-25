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
package org.eclipse.syson;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.acceleo.query.ast.Call;
import org.eclipse.acceleo.query.parser.AstResult;
import org.eclipse.acceleo.query.runtime.IQueryEnvironment;
import org.eclipse.acceleo.query.runtime.IService;
import org.eclipse.acceleo.query.runtime.Query;
import org.eclipse.acceleo.query.runtime.QueryParsing;
import org.eclipse.sirius.components.interpreter.SimpleCrossReferenceProvider;
import org.eclipse.sirius.components.view.ChangeContext;
import org.eclipse.sirius.components.view.Operation;
import org.eclipse.sirius.components.view.RepresentationDescription;
import org.eclipse.sirius.components.view.View;
import org.eclipse.sirius.components.view.diagram.ConditionalNodeStyle;
import org.eclipse.sirius.components.view.diagram.CreateView;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.EdgeDescription;
import org.eclipse.sirius.components.view.diagram.InsideLabelDescription;
import org.eclipse.sirius.components.view.diagram.InsideLabelStyle;
import org.eclipse.sirius.components.view.diagram.LabelEditTool;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.OutsideLabelDescription;
import org.eclipse.sirius.components.view.diagram.Tool;
import org.eclipse.syson.sysml.helper.EMFUtils;
import org.eclipse.syson.util.AQLConstants;

/**
 * Checks that a {@link Method} is called by at least one AQL expression in the given {@link View}.
 *
 * @author gdaniel
 */
public class JavaServiceIsCalledChecker {

    private final Set<String> aqlServiceNames;

    public JavaServiceIsCalledChecker(View view) {
        IQueryEnvironment environment = Query.newEnvironmentWithDefaultServices(new SimpleCrossReferenceProvider());
        Set<String> aqlDefaultServiceNames = environment.getLookupEngine().getRegisteredServices().stream()
                .map(IService::getName)
                .collect(Collectors.toSet());


        this.aqlServiceNames = new HashSet<>();
        Collection<String> aqlExpressions = this.collectAQLExpressionsInView(view);

        List<String> aqlBodyExpressions = aqlExpressions.stream()
                .filter(expression -> expression != null && !expression.isBlank())
                .filter(expression -> expression.startsWith(AQLConstants.AQL))
                .map(expression -> expression.substring(AQLConstants.AQL.length()))
                .toList();

        for (String aqlBodyExpression : aqlBodyExpressions) {
            AstResult astResult = QueryParsing.newBuilder().build(aqlBodyExpression);
            assertThat(astResult.getErrors()).isEmpty();
            List<String> calledServices = EMFUtils.allContainedObjectOfType(astResult.getAst(), Call.class)
                    .map(Call::getServiceName)
                    .filter(name -> name != null && !name.isBlank())
                    // Exclude AQL default services, they cannot be associated to a service method.
                    .filter(name -> !aqlDefaultServiceNames.contains(name))
                    .toList();

            this.aqlServiceNames.addAll(calledServices);
        }
    }

    public void check(Method service) {
        assertThat(this.aqlServiceNames).as("The service method %s.%s should be called at least once in an AQL expression", service.getDeclaringClass().getSimpleName(), service.getName())
                .contains(service.getName());
    }

    private Collection<String> collectAQLExpressionsInView(View view) {
        Set<String> expressions = new HashSet<>();
        for (RepresentationDescription description : view.getDescriptions()) {
            if (description instanceof DiagramDescription diagramDescription) {
                expressions.addAll(this.collectAQLExpressionsInDiagramDescription(diagramDescription));
            }
        }
        return expressions;
    }

    private Collection<String> collectAQLExpressionsInDiagramDescription(DiagramDescription diagramDescription) {
        Set<String> expressions = new HashSet<>();

        expressions.add(diagramDescription.getPreconditionExpression());

        List<Tool> diagramTools = Optional.ofNullable(diagramDescription.getPalette()).stream()
                .flatMap(palette -> EMFUtils.allContainedObjectOfType(palette, Tool.class))
                .toList();

        for (Tool diagramTool : diagramTools) {
            expressions.add(diagramTool.getPreconditionExpression());
            for (Operation bodyOperation : diagramTool.getBody()) {
                EMFUtils.allContainedObjectOfType(bodyOperation, ChangeContext.class)
                        .map(ChangeContext::getExpression)
                        .forEach(expressions::add);
            }
        }

        EMFUtils.allContainedObjectOfType(diagramDescription, NodeDescription.class)
                .map(this::collectAQLExpressionsInNodeDescription)
                .forEach(expressions::addAll);
        EMFUtils.allContainedObjectOfType(diagramDescription, EdgeDescription.class)
                .map(this::collectAQLExpressionsInEdgeDescription)
                .forEach(expressions::addAll);

        EMFUtils.allContainedObjectOfType(diagramDescription, ConditionalNodeStyle.class)
                .map(ConditionalNodeStyle::getCondition)
                .forEach(expressions::add);

        return expressions;
    }

    private Collection<String> collectAQLExpressionsInNodeDescription(NodeDescription nodeDescription) {
        Set<String> expressions = new HashSet<>();
        Optional.ofNullable(nodeDescription.getSemanticCandidatesExpression())
            .map(expressions::add);

        Optional.ofNullable(nodeDescription.getInsideLabel()).map(InsideLabelDescription::getLabelExpression)
                .map(expressions::add);

        Optional.ofNullable(nodeDescription.getInsideLabel()).map(InsideLabelDescription::getStyle)
                .map(InsideLabelStyle::getShowIconExpression)
                .map(expressions::add);

        Optional.ofNullable(nodeDescription.getOutsideLabels())
                .ifPresent(outsideLabelDescriptions -> outsideLabelDescriptions.stream().map(OutsideLabelDescription::getLabelExpression).forEach(expressions::add));

        Optional.ofNullable(nodeDescription.getIsHiddenByDefaultExpression())
                .map(expressions::add);

        Optional.ofNullable(nodeDescription.getPreconditionExpression())
                .map(expressions::add);


        List<Tool> nodeTools = Optional.ofNullable(nodeDescription.getPalette()).stream()
                .flatMap(palette -> EMFUtils.allContainedObjectOfType(palette, Tool.class))
                .toList();

        for (Tool nodeTool : nodeTools) {
            expressions.add(nodeTool.getPreconditionExpression());
            if (nodeTool instanceof LabelEditTool labelEditTool) {
                expressions.add(labelEditTool.getInitialDirectEditLabelExpression());
            }
            for (Operation bodyOperation : nodeTool.getBody()) {
                EMFUtils.allContainedObjectOfType(bodyOperation, ChangeContext.class)
                        .forEach(changeContext -> expressions.add(changeContext.getExpression()));
                EMFUtils.allContainedObjectOfType(bodyOperation, CreateView.class)
                        .forEach(changeContext -> expressions.add(changeContext.getParentViewExpression()));
            }
        }
        return expressions;
    }

    private Collection<String> collectAQLExpressionsInEdgeDescription(EdgeDescription edgeDescription) {
        Set<String> expressions = new HashSet<>();
        Optional.ofNullable(edgeDescription.getSemanticCandidatesExpression())
                .map(expressions::add);

        Optional.ofNullable(edgeDescription.getBeginLabelExpression())
                .map(expressions::add);

        Optional.ofNullable(edgeDescription.getCenterLabelExpression())
                .map(expressions::add);

        Optional.ofNullable(edgeDescription.getEndLabelExpression())
                .map(expressions::add);

        Optional.ofNullable(edgeDescription.getSourceNodesExpression())
                .map(expressions::add);

        Optional.ofNullable(edgeDescription.getTargetNodesExpression())
                .map(expressions::add);

        Optional.ofNullable(edgeDescription.getPreconditionExpression())
                .map(expressions::add);

        List<Tool> edgeTools = Optional.ofNullable(edgeDescription.getPalette()).stream()
                .flatMap(palette -> EMFUtils.allContainedObjectOfType(palette, Tool.class))
                .toList();

        for (Tool edgeTool : edgeTools) {
            expressions.add(edgeTool.getPreconditionExpression());
            if (edgeTool instanceof LabelEditTool labelEditTool) {
                expressions.add(labelEditTool.getInitialDirectEditLabelExpression());
            }
            for (Operation bodyOperation : edgeTool.getBody()) {
                EMFUtils.allContainedObjectOfType(bodyOperation, ChangeContext.class)
                        .forEach(changeContext -> expressions.add(changeContext.getExpression()));
                EMFUtils.allContainedObjectOfType(bodyOperation, CreateView.class)
                        .forEach(changeContext -> expressions.add(changeContext.getParentViewExpression()));
            }
        }
        return expressions;
    }

}
