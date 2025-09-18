/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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
package org.eclipse.syson.table.requirements.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.tables.descriptions.TableDescription;
import org.eclipse.sirius.components.view.RepresentationDescription;
import org.eclipse.sirius.components.view.builder.generated.table.TableBuilders;
import org.eclipse.sirius.components.view.builder.generated.view.ViewBuilders;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.builder.providers.IRepresentationDescriptionProvider;
import org.eclipse.sirius.components.view.table.CellDescription;
import org.eclipse.sirius.components.view.table.ColumnDescription;
import org.eclipse.sirius.components.view.table.RowContextMenuEntry;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Description of the Non-Standard Requirements Table Views using the ViewBuilder API from Sirius Web.
 *
 * @author arichard
 */
public class RTVTableDescriptionProvider implements IRepresentationDescriptionProvider {

    public static final String DESCRIPTION_NAME = "Requirements Table View";

    private final TableBuilders tableBuilders = new TableBuilders();

    private final ViewBuilders viewBuilders = new ViewBuilders();

    @Override
    public RepresentationDescription create(IColorProvider colorProvider) {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getNamespace());

        var reqRowDescription = this.tableBuilders.newRowDescription()
                .name("RTV-RowDescription-Requirement")
                .semanticCandidatesExpression(
                        "aql:self.getExposedRequirements()->sortAndFilterRequirements(columnSort, columnFilters, globalFilterData)->toPaginatedData(cursor,direction,size)")
                .contextMenuEntries(this.createContextMenuEntries().toArray(RowContextMenuEntry[]::new))
                .depthLevelExpression("0")
                .headerIconExpression("/icons/full/obj16/RequirementUsage.svg")
                .headerLabelExpression("")
                .initialHeightExpression("-1")
                .isResizableExpression(AQLConstants.AQL_FALSE)
                .build();

        return this.tableBuilders.newTableDescription()
                .name(DESCRIPTION_NAME)
                .titleExpression("aql:'view'+ Sequence{self.existingViewUsagesCountForRepresentationCreation(), 1}->sum()")
                .domainType(domainType)
                .columnDescriptions(this.createColumns().toArray(ColumnDescription[]::new))
                .cellDescriptions(this.createCells().toArray(CellDescription[]::new))
                .rowDescription(reqRowDescription)
                .pageSizeOptionsExpression("aql:Sequence{10,20,50}")
                .useStripedRowsExpression("aql:true")
                .build();
    }

    private List<ColumnDescription> createColumns() {
        List<ColumnDescription> columns = new ArrayList<>();

        var declaredNameColumnDescription = this.tableBuilders.newColumnDescription()
                .name("RTV-ColumnDescription-DeclaredName")
                .semanticCandidatesExpression("aql:'DeclaredName'")
                .headerLabelExpression("DeclaredName")
                .initialWidthExpression("250")
                .isResizableExpression(AQLConstants.AQL_TRUE)
                .isSortableExpression(AQLConstants.AQL_TRUE)
                .build();

        var reqIdColumnDescription = this.tableBuilders.newColumnDescription()
                .name("RTV-ColumnDescription-ReqId")
                .semanticCandidatesExpression("aql:'ReqId'")
                .headerLabelExpression("ReqId")
                .initialWidthExpression("150")
                .isResizableExpression(AQLConstants.AQL_TRUE)
                .isSortableExpression(AQLConstants.AQL_TRUE)
                .build();

        var docColumnDescription = this.tableBuilders.newColumnDescription()
                .name("RTV-ColumnDescription-Documentation")
                .semanticCandidatesExpression("aql:'Documentation'")
                .headerLabelExpression("Documentation")
                .initialWidthExpression("400")
                .isResizableExpression(AQLConstants.AQL_TRUE)
                .isSortableExpression(AQLConstants.AQL_TRUE)
                .build();

        columns.add(declaredNameColumnDescription);
        columns.add(reqIdColumnDescription);
        columns.add(docColumnDescription);

        return columns;
    }

    private List<CellDescription> createCells() {
        List<CellDescription> cells = new ArrayList<>();

        var declaredNameCellDescription = this.tableBuilders.newCellDescription()
                .name("RTV-CellDescription-DeclaredName")
                .preconditionExpression("aql:columnTargetObject == 'DeclaredName'")
                .valueExpression("aql:self.declaredName")
                .cellWidgetDescription(this.tableBuilders.newCellTextfieldWidgetDescription()
                        .body(this.viewBuilders.newSetValue()
                                .featureName("declaredName")
                                .valueExpression("aql:newValue")
                                .build())
                        .build())
                .build();

        var reqIdCellDescription = this.tableBuilders.newCellDescription()
                .name("RTV-CellDescription-ReqId")
                .preconditionExpression("aql:columnTargetObject == 'ReqId'")
                .valueExpression("aql:self.reqId")
                .cellWidgetDescription(this.tableBuilders.newCellTextfieldWidgetDescription()
                        .body(this.viewBuilders.newSetValue()
                                .featureName("reqId")
                                .valueExpression("aql:newValue")
                                .build())
                        .build())
                .build();

        var docCellDescription = this.tableBuilders.newCellDescription()
                .name("RTV-CellDescription-Documentation")
                .preconditionExpression("aql:columnTargetObject == 'Documentation'")
                .valueExpression("aql:self.getDocumentationBody()")
                .cellWidgetDescription(this.tableBuilders.newCellTextareaWidgetDescription()
                        .body(this.viewBuilders.newChangeContext()
                                .expression(AQLUtils.getSelfServiceCallExpression("editDocumentation", "newValue"))
                                .build())
                        .build())
                .build();

        cells.add(declaredNameCellDescription);
        cells.add(reqIdCellDescription);
        cells.add(docCellDescription);

        return cells;
    }

    private List<RowContextMenuEntry> createContextMenuEntries() {
        List<RowContextMenuEntry> entries = new ArrayList<>();

        var removeContextMenuEntry = this.tableBuilders.newRowContextMenuEntry()
                .name("RTV-ContextMenuEntry-RemoveRequirement")
                .labelExpression("Delete from table")
                .iconURLExpression("/images/graphicalDelete.svg")
                .body(this.viewBuilders.newChangeContext()
                        .expression(AQLUtils.getSelfServiceCallExpression("removeFromExposedElements", List.of(IEditingContext.EDITING_CONTEXT, TableDescription.TABLE)))
                        .build())
                .build();

        var deleteContextMenuEntry = this.tableBuilders.newRowContextMenuEntry()
                .name("RTV-ContextMenuEntry-DeleteRequirement")
                .labelExpression("Delete from model")
                .iconURLExpression("/images/semanticDelete.svg")
                .body(this.viewBuilders.newChangeContext()
                        .expression(AQLUtils.getSelfServiceCallExpression("deleteFromModel"))
                        .build())
                .build();

        entries.add(removeContextMenuEntry);
        entries.add(deleteContextMenuEntry);

        return entries;
    }
}
