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
 * The inherited exhibit state compartment list item node description provider.
 *
 * @author gcoutable
 */
public class ExhibitStateInheritedCompartmentItemNodeDescriptionProvider extends InheritedCompartmentItemNodeDescriptionProvider {

    public ExhibitStateInheritedCompartmentItemNodeDescriptionProvider(EClass eClass, EReference eReference, IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(eClass, eReference, colorProvider, descriptionNameGenerator);
    }

    @Override
    public NodeDescription create() {
        NodeDescription nd = super.create();
        var qualifiedName = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getExhibitStateUsage());
        nd.setName(this.descriptionNameGenerator.getInheritedCompartmentItemName(this.eClass, this.eReference) + StatesCompartmentNodeDescriptionProvider.EXHIBIT_STATES_NAME);
        nd.setDomainType(qualifiedName);
        nd.setPreconditionExpression(AQLConstants.AQL_SELF + ".oclIsTypeOf(" + qualifiedName + ")");
        return nd;
    }
}
