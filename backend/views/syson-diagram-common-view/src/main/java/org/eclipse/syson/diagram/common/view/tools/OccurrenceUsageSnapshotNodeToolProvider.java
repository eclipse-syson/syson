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
package org.eclipse.syson.diagram.common.view.tools;

import java.util.Objects;

import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.diagram.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.view.ViewBuilders;
import org.eclipse.sirius.components.view.builder.providers.INodeToolProvider;
import org.eclipse.sirius.components.view.diagram.DialogDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.emf.diagram.ViewDiagramDescriptionConverter;
import org.eclipse.syson.diagram.common.view.services.ViewCreateService;
import org.eclipse.syson.diagram.services.aql.DiagramMutationAQLService;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.sysml.PortionKind;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.ServiceMethod;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Used to create a snapshot from any subclass of {@link org.eclipse.syson.sysml.OccurrenceUsage}.
 *
 * @author gcoutable
 */
public class OccurrenceUsageSnapshotNodeToolProvider implements INodeToolProvider {

    private final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    private final ViewBuilders viewBuilderHelper = new ViewBuilders();

    private final IDescriptionNameGenerator descriptionNameGenerator;

    public OccurrenceUsageSnapshotNodeToolProvider(IDescriptionNameGenerator descriptionNameGenerator) {
        this.descriptionNameGenerator = Objects.requireNonNull(descriptionNameGenerator);
    }

    @Override
    public NodeTool create(IViewDiagramElementFinder cache) {

        var setIsPortion = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getFeature_IsPortion().getName())
                .valueExpression("true");

        var setSnapshot = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getOccurrenceUsage_PortionKind().getName())
                .valueExpression(PortionKind.SNAPSHOT.getLiteral())
                .children(setIsPortion.build());

        var updateExposedElements = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of4(DiagramMutationAQLService::expose).aqlSelf(IEditingContext.EDITING_CONTEXT, DiagramContext.DIAGRAM_CONTEXT, Node.SELECTED_NODE,
                        ViewDiagramDescriptionConverter.CONVERTED_NODES_VARIABLE));

        var changeContextNewInstance = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of0(ViewCreateService::elementInitializer).aql("newInstance"))
                .children(
                    updateExposedElements.build(),
                    setSnapshot.build()
                );

        var createEClassInstance = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(eClass))
                .referenceName(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement().getName())
                .variableName("newInstance")
                .children(changeContextNewInstance.build());

        var changeContextMembership = this.viewBuilderHelper.newChangeContext()
                .expression("aql:newOwningMembership")
                .children(createEClassInstance.build());

        var createMembership = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getOwningMembership()))
                .referenceName(SysmlPackage.eINSTANCE.getElement_OwnedRelationship().getName())
                .variableName("newOwningMembership")
                .children(changeContextMembership.build());

        var changeContextViewUsageOwner = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of0(UtilService::getViewUsageOwner).aqlSelf())
                .children(createMembership.build());

        // TODO: Use IDescriptionNameGenerator instead ??
        String toolName = "Snapshot " + this.descriptionNameGenerator.getCreationToolName(this.eClass);
        if (this.eClass.equals(SysmlPackage.eINSTANCE.getOccurrenceUsage())) {
            toolName = "Snapshot";
        }

        return this.diagramBuilderHelper.newNodeTool()
                .name(toolName)
                .iconURLsExpression("/icons/full/obj16/" + this.eClass.getName() + ".svg")
                .body(changeContextViewUsageOwner.build())
                .dialogDescription(this.getDescriptionDialog())
                .build();
    }

    private DialogDescription getDescriptionDialog() {

        return this.diagramBuilderHelper.newSelectionDialogDescription()
                // TODO: what is needed to have a selection dialog description that support an empty selection and a single selection.
                //   The candidates should be all subclasses of OccurrenceUsage, OccurrenceUsage included
                .build();
    }
}
