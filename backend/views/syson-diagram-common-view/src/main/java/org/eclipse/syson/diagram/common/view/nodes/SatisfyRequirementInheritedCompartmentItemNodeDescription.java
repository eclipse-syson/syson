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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * The inherited satisfy requirements compartment list item node description provider.
 * <p>
 *     Generic {@link InheritedCompartmentItemNodeDescriptionProvider} class can not be used directly because of the references used to own {@link org.eclipse.syson.sysml.SatisfyRequirementUsage}
 *     in the {@link org.eclipse.syson.sysml.PartDefinition} or in the {@link org.eclipse.syson.sysml.PartUsage}.
 *     The type of these references is {@link org.eclipse.syson.sysml.RequirementUsage} which does not match exactly the required type: {@link org.eclipse.syson.sysml.SatisfyRequirementUsage}.
 *     That way, it is necessary to provide an inherited class that customize the domain type properly.
 * </p>
 *
 * @author Jerome Gout
 */
public class SatisfyRequirementInheritedCompartmentItemNodeDescription extends InheritedCompartmentItemNodeDescriptionProvider {

    public SatisfyRequirementInheritedCompartmentItemNodeDescription(EClass eClass, EReference eReference, IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(eClass, eReference, colorProvider, descriptionNameGenerator);
    }

    @Override
    public NodeDescription create() {
        NodeDescription nd = super.create();
        var qualifiedName = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getSatisfyRequirementUsage());
        nd.setName(this.descriptionNameGenerator.getInheritedCompartmentItemName(this.eClass, this.eReference) + SatisfyRequirementCompartmentItemNodeDescription.COMPARTMENT_ITEM_NAME);
        nd.setDomainType(qualifiedName);
        nd.setPreconditionExpression(AQLConstants.AQL_SELF + ".oclIsTypeOf(" + qualifiedName + ")");
        return nd;
    }
}
