/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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
package org.eclipse.syson.sysml.textual.models;

import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureReferenceExpression;
import org.eclipse.syson.sysml.FeatureValue;
import org.eclipse.syson.sysml.LiteralInteger;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.OperatorExpression;
import org.eclipse.syson.sysml.ParameterMembership;
import org.eclipse.syson.sysml.util.ModelBuilder;

/**
 * Test model for AttributeUsage with BracketOperatorExpression.
 *
 * <pre>
 * attribute attribute1 = 80 [millimetre];
 * </pre>
 *
 * @author wbilem
 */
public class AttributeUsageWithBracketOperatorExpressionTestModel {

    private final ModelBuilder builder = new ModelBuilder();

    private final AttributeUsage attributeUsage;

    private final AttributeUsage mm;

    public AttributeUsageWithBracketOperatorExpressionTestModel() {
        this.attributeUsage = this.builder.createWithName(AttributeUsage.class, "attribute1");
        this.mm = this.builder.createWithName(AttributeUsage.class, "millimetre");

        this.attributeUsageWithBracketOperatorExpression();
    }

    public AttributeUsage getAttributeUsage() {
        return this.attributeUsage;
    }

    private void attributeUsageWithBracketOperatorExpression() {
        OperatorExpression bracketExpression = this.builder.create(OperatorExpression.class);
        bracketExpression.setOperator("[");

        FeatureValue featureValue = this.builder.createIn(FeatureValue.class, this.attributeUsage);
        featureValue.getOwnedRelatedElement().add(bracketExpression);

        ParameterMembership literalParam1 = this.builder.createIn(ParameterMembership.class, bracketExpression);
        Feature feature1 = this.builder.create(Feature.class);
        literalParam1.getOwnedRelatedElement().add(feature1);

        FeatureValue featureValue2 = this.builder.createIn(FeatureValue.class, feature1);
        LiteralInteger literal = this.builder.create(LiteralInteger.class);
        literal.setValue(80);

        featureValue2.getOwnedRelatedElement().add(literal);

        ParameterMembership param2 = this.builder.createIn(ParameterMembership.class, bracketExpression);
        Feature feature2 = this.builder.create(Feature.class);
        param2.getOwnedRelatedElement().add(feature2);
        this.createFeatureReference(feature2, this.mm);
    }

    private void createFeatureReference(Feature feature, AttributeUsage attr) {
        FeatureValue featureValue = this.builder.createIn(FeatureValue.class, feature);
        FeatureReferenceExpression featureReferenceExpression = this.builder.create(FeatureReferenceExpression.class);
        featureValue.getOwnedRelatedElement().add(featureReferenceExpression);
        Membership membership = this.builder.createIn(Membership.class, featureReferenceExpression);
        membership.setMemberElement(attr);
    }
}
