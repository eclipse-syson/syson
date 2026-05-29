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
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Used to create a {@link org.eclipse.syson.sysml.ConstraintUsage} compartment item node description.
 * <p>
 * {@link org.eclipse.syson.sysml.ConstraintUsage} compartment items have to be contained in a {@link org.eclipse.syson.sysml.RequirementConstraintMembership} with {@link org.eclipse.syson.sysml.RequirementConstraintKind#REQUIREMENT}.
 * </p>
 * @author gcoutable
 */
public class RequireConstraintCompartmentItemNodeDescription extends CompartmentItemNodeDescriptionProvider {
    public static final String COMPARTMENT_ITEM_NAME = " require constraint";

    public RequireConstraintCompartmentItemNodeDescription(EClass eClass, EReference eReference, IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(eClass, eReference, colorProvider, descriptionNameGenerator);
    }

    @Override
    protected String getDomainType() {
        return SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getElement());
    }

    @Override
    protected String getName() {
        return super.getName() + COMPARTMENT_ITEM_NAME;
    }
}
