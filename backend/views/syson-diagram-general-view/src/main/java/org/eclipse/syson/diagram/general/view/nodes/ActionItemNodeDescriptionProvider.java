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
package org.eclipse.syson.diagram.general.view.nodes;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.syson.diagram.common.view.nodes.CompartmentItemNodeDescriptionProvider;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Used to create an Action item node description for a part.
 * <p>
 * Action items in part have a particular semantic candidate expression to ensure their content is not duplicated from
 * other compartments (like the "states" one).
 * </p>
 *
 * @author gdaniel
 */
public class ActionItemNodeDescriptionProvider extends CompartmentItemNodeDescriptionProvider {

    public ActionItemNodeDescriptionProvider(EClass eClass, EReference eReference, IColorProvider colorProvider, IDescriptionNameGenerator nameGenerator) {
        super(eClass, eReference, colorProvider, nameGenerator);
    }

    @Override
    protected String getSemanticCandidateExpression() {
        return AQLConstants.AQL_SELF + "." + this.eReference.getName() + "->select(a | not a.oclIsKindOf(" + SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getStateUsage()) + "))";
    }
}
