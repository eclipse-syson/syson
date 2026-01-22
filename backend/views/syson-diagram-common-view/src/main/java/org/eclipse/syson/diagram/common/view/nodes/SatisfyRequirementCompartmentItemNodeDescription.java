/*******************************************************************************
 * Copyright (c) 2025, 2026 Obeo.
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
 * Used to create an SatisfyRequirementUsage compartment item node description.
 * <p>
 * SatisfyRequirementUsage compartment items have a particular semantic candidate expression to ensure their content is
 * not duplicated from other compartments (like the "requirements" one).
 * </p>
 *
 * @author arichard
 */
public class SatisfyRequirementCompartmentItemNodeDescription extends CompartmentItemNodeDescriptionProvider {

    public static final String COMPARTMENT_ITEM_NAME = " SatisfyRequirement";

    public SatisfyRequirementCompartmentItemNodeDescription(EClass eClass, EReference eReference, IColorProvider colorProvider, IDescriptionNameGenerator nameGenerator) {
        super(eClass, eReference, colorProvider, nameGenerator);
    }

    @Override
    public NodeDescription create() {
        NodeDescription nodeDescription = super.create();
        nodeDescription.setPreconditionExpression("true");
        return nodeDescription;
    }

    @Override
    protected String getDomainType() {
        return SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getSatisfyRequirementUsage());
    }

    @Override
    protected String getName() {
        return super.getName() + COMPARTMENT_ITEM_NAME;
    }

    @Override
    protected String getSemanticCandidateExpression() {
        return AQLConstants.AQL_SELF + "." + this.eReference.getName() + "->select(x | x.oclIsTypeOf("
                + SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getSatisfyRequirementUsage()) + "))";
    }
}
