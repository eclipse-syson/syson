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
package org.eclipse.syson.sysml.parser.internal;

import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Feature;
import org.omg.sysml.lang.sysml.FeatureChainExpression;
import org.omg.sysml.lang.sysml.FeatureDirectionKind;
import org.omg.sysml.lang.sysml.FeatureMembership;
import org.omg.sysml.lang.sysml.Relationship;
import org.omg.sysml.lang.sysml.SysMLFactory;

/**
 * Prepares feature-chain source targets using only containment and stored direction. Ported from Pilot
 * ExpressionUtil.prepareSourceTargetFeature (ST6RI-981).
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public final class FeatureChainSourceTargetCompleter {

    private FeatureChainSourceTargetCompleter() {
        // Utility class.
    }

    /**
     * Prepares the first owned input, preserving existing nested features and their names. May be called again after an
     * input is added or replaced. No derived references or semantic queries are used, so preparation is safe before
     * linking.
     *
     * @param expression
     *            the {@link FeatureChainExpression} to prepare
     */
    public static void completeSourceTarget(FeatureChainExpression expression) {
        for (Relationship relationship : expression.getOwnedRelationship()) {
            Feature source = getOwnedFeature(relationship);
            if (source != null && source.getDirection() == FeatureDirectionKind.IN) {
                for (Relationship nested : source.getOwnedRelationship()) {
                    if (getOwnedFeature(nested) != null) {
                        return;
                    }
                }
                Feature target = SysMLFactory.eINSTANCE.createFeature();
                target.setDeclaredName(""); // Suppress effective naming.
                FeatureMembership membership = SysMLFactory.eINSTANCE.createFeatureMembership();
                membership.getOwnedRelatedElement().add(target);
                source.getOwnedRelationship().add(membership);
                return;
            }
        }
    }

    /**
     * Gets the owned feature of the given {@link Relationship}.
     *
     * @param relationship
     *            the {@link Relationship}
     * @return the owned feature of the given {@link Relationship}
     */
    private static Feature getOwnedFeature(Relationship relationship) {
        if (relationship instanceof FeatureMembership && !relationship.getOwnedRelatedElement().isEmpty()) {
            Element element = relationship.getOwnedRelatedElement().get(0);
            if (element instanceof Feature feature) {
                return feature;
            }
        }
        return null;
    }
}
