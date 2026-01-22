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
package org.eclipse.syson.standard.diagrams.view.nodes;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.syson.diagram.common.view.nodes.CompartmentItemNodeDescriptionProvider;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Used to display documentations inside the objective compartment.
 *
 * @author arichard
 */
public class ObjectiveDocumentationCompartmentItemNodeDescription extends CompartmentItemNodeDescriptionProvider {

    public static final String COMPARTMENT_ITEM_NAME = " objective documentation";

    public ObjectiveDocumentationCompartmentItemNodeDescription(EClass eClass, EReference eReference, IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(eClass, eReference, colorProvider, descriptionNameGenerator);
    }

    @Override
    public NodeDescription create() {
        NodeDescription nodeDescription = super.create();
        nodeDescription.setPreconditionExpression("true");
        return nodeDescription;
    }

    @Override
    protected String getName() {
        return super.getName() + COMPARTMENT_ITEM_NAME;
    }

    @Override
    protected String getSemanticCandidateExpression() {
        return AQLConstants.AQL_SELF + "." + this.getEReference().getName() + "." + SysmlPackage.eINSTANCE.getElement_Documentation().getName();
    }

    @Override
    protected String getDomainType() {
        return SysmlPackage.eINSTANCE.getDocumentation().getName();
    }
}
