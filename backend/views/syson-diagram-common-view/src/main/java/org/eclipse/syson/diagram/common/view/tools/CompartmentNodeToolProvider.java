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

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Node tool provider for elements inside compartments.
 *
 * @author Jerome Gout
 */
public class CompartmentNodeToolProvider extends AbstractCompartmentNodeToolProvider {

    private final EReference eReference;

    private final IDescriptionNameGenerator nameGenerator;

    private final FeatureDirectionKind featureDirectionKind;

    public CompartmentNodeToolProvider(EReference eReference, IDescriptionNameGenerator nameGenerator) {
        this(eReference, nameGenerator, null);
    }

    public CompartmentNodeToolProvider(EReference eReference, IDescriptionNameGenerator nameGenerator, FeatureDirectionKind featureDirectionKind) {
        super();
        this.eReference = eReference;
        this.nameGenerator = nameGenerator;
        this.featureDirectionKind = featureDirectionKind;
    }


    @Override
    protected String getServiceCallExpression() {
        if (this.featureDirectionKind != null) {
            List<String> params = List.of(AQLUtils.aqlString(this.eReference.getName()), AQLUtils.aqlString(this.featureDirectionKind.getLiteral()));
            return AQLUtils.getSelfServiceCallExpression("createCompartmentItemWithDirection", params);
        } else {
            return AQLUtils.getSelfServiceCallExpression("createCompartmentItem", AQLUtils.aqlString(this.eReference.getName()));
        }
    }

    @Override
    protected String getNodeToolName() {
        var toolLabel = this.nameGenerator.getCreationToolName(this.eReference);
        if (this.featureDirectionKind != null) {
            toolLabel += ' ' + StringUtils.capitalize(this.featureDirectionKind.getLiteral());
        }
        return toolLabel;
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
