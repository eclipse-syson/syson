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
package org.eclipse.syson.diagram.common.view.tools;

import java.util.Objects;

import org.eclipse.syson.diagram.common.view.services.ViewCreateService;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.ServiceMethod;

/**
 * Node tool provider for Parameters compartment in the element that need such compartment.
 *
 * @author arichard
 */
public class ParameterCompartmentNodeToolProvider extends AbstractCompartmentNodeToolProvider {

    private final FeatureDirectionKind direction;

    public ParameterCompartmentNodeToolProvider(FeatureDirectionKind direction) {
        this.direction = Objects.requireNonNull(direction);
    }

    @Override
    protected String getServiceCallExpression() {
        return ServiceMethod.of1(ViewCreateService::createActionParameter).aqlSelf(AQLUtils.aqlString(this.direction.getName()));
    }

    @Override
    protected String getNodeToolName() {
        String toolName = "New Parameter";
        if (FeatureDirectionKind.IN == this.direction) {
            toolName += " In";
        } else if (FeatureDirectionKind.INOUT == this.direction) {
            toolName += " Inout";
        } else if (FeatureDirectionKind.OUT == this.direction) {
            toolName += " Out";
        }
        return toolName;
    }

    @Override
    protected boolean revealOnCreate() {
        return false;
    }

    @Override
    protected String getNodeToolIconURLsExpression() {
        StringBuilder iconURLsExpression = new StringBuilder("/icons/full/obj16/ReferenceUsage");
        if (FeatureDirectionKind.IN == this.direction) {
            iconURLsExpression.append("In");
        } else if (FeatureDirectionKind.INOUT == this.direction) {
            iconURLsExpression.append("Inout");
        } else if (FeatureDirectionKind.OUT == this.direction) {
            iconURLsExpression.append("Out");
        }
        iconURLsExpression.append(".svg");
        return iconURLsExpression.toString();
    }

}
