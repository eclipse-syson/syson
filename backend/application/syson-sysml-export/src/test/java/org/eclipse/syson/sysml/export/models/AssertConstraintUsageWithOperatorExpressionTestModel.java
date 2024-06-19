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
package org.eclipse.syson.sysml.export.models;

import org.eclipse.syson.sysml.AssertConstraintUsage;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureReferenceExpression;
import org.eclipse.syson.sysml.FeatureValue;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.NullExpression;
import org.eclipse.syson.sysml.OperatorExpression;
import org.eclipse.syson.sysml.ParameterMembership;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.ResultExpressionMembership;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.util.ModelBuilder;

/**
 * Test model for AssertConstraintUsage with BinaryOperatorExpression and NullExpression.
 *
 * <pre>
 * assert constraint {
                        runner != null xor walker != null
                    }
 * </pre>
 *
 * @author wbilem
 */
public class AssertConstraintUsageWithOperatorExpressionTestModel {

    private final ModelBuilder builder = new ModelBuilder();

    private final AssertConstraintUsage assertConstraintUsage;

    private final PartUsage runner;

    private final PartUsage walker;

    public AssertConstraintUsageWithOperatorExpressionTestModel() {
        this.assertConstraintUsage = this.builder.create(AssertConstraintUsage.class);
        this.runner = this.builder.createWithName(PartUsage.class, "runner");
        this.walker = this.builder.createWithName(PartUsage.class, "walker");

        this.attributeUsageWithBracketOperatorExpression();
    }

    public AssertConstraintUsage getAssertConstraintUsage() {
        return this.assertConstraintUsage;
    }

    private void attributeUsageWithBracketOperatorExpression() {
        ResultExpressionMembership resultExpressionMembership = this.builder.createIn(ResultExpressionMembership.class, this.assertConstraintUsage);

        OperatorExpression expression = this.builder.create(OperatorExpression.class);
        expression.setOperator("xor");
        resultExpressionMembership.getOwnedRelatedElement().add(expression);

        ParameterMembership parameterMembership = this.builder.createIn(ParameterMembership.class, expression);

        Feature feature = this.builder.create(Feature.class);
        parameterMembership.getOwnedRelatedElement().add(feature);

        FeatureValue featureValue = this.builder.createIn(FeatureValue.class, feature);

        OperatorExpression expression2 = this.builder.create(OperatorExpression.class);
        expression2.setOperator("!=");
        featureValue.getOwnedRelatedElement().add(expression2);

        ParameterMembership parameterMembership2 = this.builder.createIn(ParameterMembership.class, expression2);

        Feature feature2 = this.builder.create(Feature.class);
        parameterMembership2.getOwnedRelatedElement().add(feature2);

        this.createFeatureReference(feature2, this.runner);

        this.createNullExpression(expression2);

        ParameterMembership parameterMembership4 = this.builder.createIn(ParameterMembership.class, expression);

        Feature feature4 = this.builder.create(Feature.class);
        parameterMembership4.getOwnedRelatedElement().add(feature4);

        FeatureValue featureValue4 = this.builder.createIn(FeatureValue.class, feature4);

        OperatorExpression expression3 = this.builder.create(OperatorExpression.class);
        expression3.setOperator("!=");
        featureValue4.getOwnedRelatedElement().add(expression3);

        ParameterMembership parameterMembership5 = this.builder.createIn(ParameterMembership.class, expression3);

        Feature feature5 = this.builder.create(Feature.class);
        parameterMembership5.getOwnedRelatedElement().add(feature5);

        this.createFeatureReference(feature5, this.walker);

        this.createNullExpression(expression3);
    }

    private void createFeatureReference(Feature feature, Usage attr) {
        FeatureValue featureValue = this.builder.createIn(FeatureValue.class, feature);
        FeatureReferenceExpression featureReferenceExpression = this.builder.create(FeatureReferenceExpression.class);
        featureValue.getOwnedRelatedElement().add(featureReferenceExpression);
        Membership membership = this.builder.createIn(Membership.class, featureReferenceExpression);
        membership.setMemberElement(attr);
    }

    private void createNullExpression(Expression expression) {
        ParameterMembership parameterMembership = this.builder.createIn(ParameterMembership.class, expression);
        Feature feature = this.builder.create(Feature.class);
        parameterMembership.getOwnedRelatedElement().add(feature);

        FeatureValue featureValue = this.builder.createIn(FeatureValue.class, feature);
        NullExpression nullExpression = this.builder.create(NullExpression.class);
        featureValue.getOwnedRelatedElement().add(nullExpression);
    }
}
