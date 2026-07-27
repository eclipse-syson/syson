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
package org.eclipse.syson.sysml.metamodel.services;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.syson.sysml.FeatureValue;
import org.eclipse.syson.sysml.LiteralBoolean;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.SuccessionAsUsage;
import org.eclipse.syson.sysml.SysmlFactory;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link MetamodelQueryElementService}.
 *
 * @author arichard
 */
public class MetamodelQueryElementServiceTest {

    /**
     * Verifies that a feature value expression is found from its owning feature.
     */
    @Test
    public void findSingleExpressionDefinitionInFeatureValueOfSuccessionAsUsage() {
        var service = new MetamodelQueryElementService();
        SuccessionAsUsage successionAsUsage = SysmlFactory.eINSTANCE.createSuccessionAsUsage();
        FeatureValue featureValue = SysmlFactory.eINSTANCE.createFeatureValue();
        LiteralBoolean expression = SysmlFactory.eINSTANCE.createLiteralBoolean();

        successionAsUsage.getOwnedRelationship().add(featureValue);
        featureValue.getOwnedRelatedElement().add(expression);

        assertThat(service.findSingleExpressionDefinition(successionAsUsage)).containsSame(expression);
        assertThat(service.hasSingleExpressionDefinition(successionAsUsage)).isTrue();
    }

    /**
     * Verifies that an expression reached both directly and through a feature value is counted only once.
     */
    @Test
    public void findSingleExpressionDefinitionInFeatureValueOfPartUsage() {
        var service = new MetamodelQueryElementService();
        PartUsage partUsage = SysmlFactory.eINSTANCE.createPartUsage();
        FeatureValue featureValue = SysmlFactory.eINSTANCE.createFeatureValue();
        LiteralBoolean expression = SysmlFactory.eINSTANCE.createLiteralBoolean();

        partUsage.getOwnedRelationship().add(featureValue);
        featureValue.getOwnedRelatedElement().add(expression);

        assertThat(service.findSingleExpressionDefinition(partUsage)).containsSame(expression);
        assertThat(service.hasSingleExpressionDefinition(partUsage)).isTrue();
    }

    /**
     * Verifies that a directly owned expression is found.
     */
    @Test
    public void findSingleExpressionDefinitionDirectlyOwnedByFeatureValue() {
        var service = new MetamodelQueryElementService();
        FeatureValue featureValue = SysmlFactory.eINSTANCE.createFeatureValue();
        LiteralBoolean expression = SysmlFactory.eINSTANCE.createLiteralBoolean();

        featureValue.getOwnedRelatedElement().add(expression);

        assertThat(service.findSingleExpressionDefinition(featureValue)).containsSame(expression);
    }

    /**
     * Verifies that distinct feature value expressions remain ambiguous.
     */
    @Test
    public void findSingleExpressionDefinitionWithMultipleFeatureValueExpressions() {
        var service = new MetamodelQueryElementService();
        SuccessionAsUsage successionAsUsage = SysmlFactory.eINSTANCE.createSuccessionAsUsage();
        FeatureValue firstFeatureValue = SysmlFactory.eINSTANCE.createFeatureValue();
        FeatureValue secondFeatureValue = SysmlFactory.eINSTANCE.createFeatureValue();
        LiteralBoolean firstExpression = SysmlFactory.eINSTANCE.createLiteralBoolean();
        LiteralBoolean secondExpression = SysmlFactory.eINSTANCE.createLiteralBoolean();

        successionAsUsage.getOwnedRelationship().add(firstFeatureValue);
        successionAsUsage.getOwnedRelationship().add(secondFeatureValue);
        firstFeatureValue.getOwnedRelatedElement().add(firstExpression);
        secondFeatureValue.getOwnedRelatedElement().add(secondExpression);

        assertThat(service.findSingleExpressionDefinition(successionAsUsage)).isEmpty();
        assertThat(service.hasSingleExpressionDefinition(successionAsUsage)).isFalse();
    }
}
