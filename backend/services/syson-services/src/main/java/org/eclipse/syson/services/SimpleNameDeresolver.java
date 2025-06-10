/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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
package org.eclipse.syson.services;

import java.util.Objects;

import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.FeatureReferenceExpression;
import org.eclipse.syson.sysml.OperatorExpression;
import org.eclipse.syson.sysml.helper.EMFUtils;
import org.eclipse.syson.sysml.textual.utils.INameDeresolver;

/**
 * {@link INameDeresolver} implementation that uses {@link Element#getDeclaredName()} or {@link Element#getShortName()}
 * to represent a reference to an element without checking if that name is conflicting with other elements. This
 * implementation is fast and efficient.
 *
 * @author Arthur Daussy
 */
public class SimpleNameDeresolver implements INameDeresolver {

    @Override
    public String getDeresolvedName(Element element, Element context) {
        final String result;
        if (element == null) {
            result = null;
        } else {
            String shortName = element.getShortName();
            if (shortName != null && !shortName.isBlank() && this.preferUsingShortName(element, context)) {
                result = shortName;
            } else {
                result = element.getName();
            }
        }
        return result;
    }

    private boolean preferUsingShortName(Element element, Element context) {
        // Check if the reference if used as a unit
        if (context instanceof FeatureReferenceExpression featureRef) {
            return EMFUtils.getAncestors(OperatorExpression.class, featureRef,
                    ancestor -> ancestor instanceof OperatorExpression operatorExpression && Objects.equals(operatorExpression.getOperator(), "[")).size() > 0;
        }

        return false;
    }

}
