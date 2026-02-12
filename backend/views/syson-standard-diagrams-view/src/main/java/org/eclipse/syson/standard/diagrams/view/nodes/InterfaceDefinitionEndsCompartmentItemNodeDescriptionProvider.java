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

import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.syson.diagram.common.view.nodes.CompartmentItemNodeDescriptionProvider;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Used to create a Interface Definition ends compartment item node description.
 * <p>
 * The interfaceEnds reference of connection definition returns inherited elements from standard libraries, we don't
 * want that.
 * </p>
 *
 * @author pcdavid
 */
public class InterfaceDefinitionEndsCompartmentItemNodeDescriptionProvider extends CompartmentItemNodeDescriptionProvider {

    public InterfaceDefinitionEndsCompartmentItemNodeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator nameGenerator) {
        super(SysmlPackage.eINSTANCE.getInterfaceDefinition(), SysmlPackage.eINSTANCE.getInterfaceDefinition_InterfaceEnd(), colorProvider, nameGenerator);
    }

    @Override
    protected String getSemanticCandidateExpression() {
        return AQLConstants.AQL_SELF + "." + this.eReference.getName() + "->select(endElt | not endElt.isFromStandardLibrary())";
    }
}
