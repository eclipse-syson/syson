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
package org.eclipse.syson.diagram.common.view.tools;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Node tool provider for elements inside compartments.
 *
 * @author Jerome Gout
 */
public class CompartmentNodeToolProvider extends AbstractCompartmentNodeToolProvider {

    private final EReference eReference;

    private final IDescriptionNameGenerator nameGenerator;

    public CompartmentNodeToolProvider(EReference eReference, IDescriptionNameGenerator nameGenerator) {
        super();
        this.eReference = eReference;
        this.nameGenerator = nameGenerator;
    }

    @Override
    protected String getServiceCallExpression() {
        return "aql:self.createCompartmentItem('" + this.eReference.getName() + "')";
    }

    @Override
    protected String getNodeToolName() {
        return this.nameGenerator.getCreationToolName(this.eReference);
    }

    @Override
    protected String getNodeToolIconURLsExpression() {
        return "/icons/full/obj16/" + this.eReference.getEType().getName() + ".svg";
    }

    @Override
    protected boolean revealOnCreate() {
        return true;
    }
}
