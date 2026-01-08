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
package org.eclipse.syson.diagram.common.view.tools;

import java.util.Objects;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.syson.model.services.aql.ModelMutationAQLService;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.ServiceMethod;

/**
 * Used to create a documentation inside the objective compartment.
 *
 * @author arichard
 */
public class ObjectiveDocumentationNodeToolProvider extends AbstractCompartmentNodeToolProvider {

    private final EReference eReference;

    public ObjectiveDocumentationNodeToolProvider(EReference eReference) {
        this.eReference = Objects.requireNonNull(eReference);
    }

    @Override
    protected String getServiceCallExpression() {
        return ServiceMethod.of1(ModelMutationAQLService::createObjectiveDocumentation).aqlSelf(AQLUtils.aqlString(this.eReference.getName()));
    }

    @Override
    protected String getNodeToolName() {
        return "New Objective Documentation";
    }

    @Override
    protected boolean revealOnCreate() {
        return true;
    }

    @Override
    protected String getNodeToolIconURLsExpression() {
        return "/icons/full/obj16/Documentation.svg";
    }
}
