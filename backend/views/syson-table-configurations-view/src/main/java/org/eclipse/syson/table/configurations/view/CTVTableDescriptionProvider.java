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
package org.eclipse.syson.table.configurations.view;

import org.eclipse.sirius.components.view.RepresentationDescription;
import org.eclipse.sirius.components.view.builder.generated.table.TableBuilders;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.builder.providers.IRepresentationDescriptionProvider;
import org.eclipse.sirius.components.view.table.RowDescription;
import org.eclipse.syson.services.ContextualFeatureChainObjectService;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.table.configurations.view.services.CTVQueryServices;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.ServiceMethod;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Description of the Configurations Table View using the ViewBuilder API from Sirius Web.
 *
 * @author Jerome Gout
 */
public class CTVTableDescriptionProvider implements IRepresentationDescriptionProvider {

    public static final String DESCRIPTION_NAME = "Configurations Table View";

    private final TableBuilders tableBuilders = new TableBuilders();

    /**
     * Creates the configurations table description with feature-chain rows and no columns.
     *
     * @param colorProvider
     *            the available view colors
     * @return the configurations table description
     */
    @Override
    public RepresentationDescription create(IColorProvider colorProvider) {
        RowDescription attributeRowDescription = this.tableBuilders.newRowDescription()
                .name("CTV-RowDescription-Feature")
                .semanticCandidatesExpression(ServiceMethod.of0(CTVQueryServices::getConfigurableFeatureChains).aqlSelf() + "->toPaginatedData(cursor,direction,size)")
                .depthLevelExpression("0")
                .hasChildrenExpression(AQLConstants.AQL_FALSE)
                .headerIconExpression(ServiceMethod.of0(ContextualFeatureChainObjectService::getImagePaths).aqlSelf())
                .headerLabelExpression(ServiceMethod.of0(CTVQueryServices::getFeatureChainLabel).aqlSelf())
                .initialHeightExpression("-1")
                .isResizableExpression(AQLConstants.AQL_FALSE)
                .build();

        return this.tableBuilders.newTableDescription()
                .name(DESCRIPTION_NAME)
                .titleExpression("aql:'view'+ Sequence{self.existingViewUsagesCountForRepresentationCreation(), 1}->sum()")
                .domainType(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getType()))
                .rowDescription(attributeRowDescription)
                .pageSizeOptionsExpression("aql:Sequence{10,20,50}")
                .useStripedRowsExpression(AQLConstants.AQL_TRUE)
                .enableSubRows(false)
                .build();
    }

}
