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

/**
 * Node tool provider for Subject compartment in the element that need such compartment.
 *
 * @author Jerome Gout
 */
public class SubjectCompartmentNodeToolProvider extends AbstractCompartmentNodeToolProvider {

    public SubjectCompartmentNodeToolProvider() {
        super();
    }

    @Override
    protected String getServiceCallExpression() {
        return "aql:self.createPartUsageAsSubject(self.eContainer().eContainer())";
    }

    @Override
    protected String getNodeToolName() {
        return "New Subject";
    }

    @Override
    protected String getNodeToolIconURLsExpression() {
        return "/icons/full/obj16/Subject.svg";
    }

    @Override
    protected String getPreconditionExpression() {
        return "aql:self.isEmptySubjectCompartment()";
    }

    @Override
    protected boolean expandOnCreate() {
        return true;
    }
}
