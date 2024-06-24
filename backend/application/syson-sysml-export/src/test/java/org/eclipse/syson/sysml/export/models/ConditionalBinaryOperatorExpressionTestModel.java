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

import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.FeatureReferenceExpression;
import org.eclipse.syson.sysml.FeatureValue;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.OperatorExpression;
import org.eclipse.syson.sysml.ParameterMembership;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.util.ModelBuilder;

/**
 * Test model for ConditionalBinaryOperatorExpression.
 *
 * <pre>
 *  fuel == diesel and motor == 4cyl;
 * </pre>
 *
 * @author wbilem
 */
public class ConditionalBinaryOperatorExpressionTestModel {

    private final ModelBuilder builder = new ModelBuilder();

    private final PartUsage fuel;

    private final PartUsage diesel;

    private final PartUsage motor;

    private final PartUsage model;

    private final OperatorExpression binary;

    public ConditionalBinaryOperatorExpressionTestModel() {
        this.model = this.builder.createWithName(PartUsage.class, "cyl");
        this.motor = this.builder.createWithName(PartUsage.class, "motor");
        this.diesel = this.builder.createWithName(PartUsage.class, "diesel");
        this.fuel = this.builder.createWithName(PartUsage.class, "fuel");
        this.binary = this.builder.create(OperatorExpression.class);

        this.conditionalBinaryOperatorExpressionTestModel();
    }

    public OperatorExpression getOperatorExpression() {
        return this.binary;
    }

    private void conditionalBinaryOperatorExpressionTestModel() {
        this.binary.setOperator("and");

        ParameterMembership parameterMembership = this.builder.createIn(ParameterMembership.class, this.binary);
        Feature feature = this.builder.create(Feature.class);
        FeatureValue featureValue = this.builder.createIn(FeatureValue.class, feature);
        parameterMembership.getOwnedRelatedElement().add(feature);

        OperatorExpression binary2 = this.builder.create(OperatorExpression.class);
        binary2.setOperator("==");
        featureValue.getOwnedRelatedElement().add(binary2);

        this.createFeatureReference(binary2, this.fuel);
        this.createFeatureReference(binary2, this.diesel);

        ParameterMembership parameterMembership2 = this.builder.createIn(ParameterMembership.class, this.binary);
        Feature feature2 = this.builder.create(Feature.class);
        FeatureValue featureValue2 = this.builder.createIn(FeatureValue.class, feature2);
        parameterMembership2.getOwnedRelatedElement().add(feature2);

        FeatureReferenceExpression featureReferenceExpression = this.builder.create(FeatureReferenceExpression.class);
        featureValue2.getOwnedRelatedElement().add(featureReferenceExpression);
        FeatureMembership featureMembership = this.builder.createIn(ParameterMembership.class, featureReferenceExpression);

        OperatorExpression binary3 = this.builder.create(OperatorExpression.class);
        binary3.setOperator("==");
        featureMembership.getOwnedRelatedElement().add(binary3);

        this.createFeatureReference(binary3, this.motor);
        this.createFeatureReference(binary3, this.model);
    }

    private void createFeatureReference(Expression expression, Element memberElement) {
        ParameterMembership parameterMembership = this.builder.createIn(ParameterMembership.class, expression);
        Feature feature = this.builder.create(Feature.class);
        parameterMembership.getOwnedRelatedElement().add(feature);
        FeatureValue featureValue = this.builder.createIn(FeatureValue.class, feature);

        FeatureReferenceExpression featureReferenceExpression = this.builder.create(FeatureReferenceExpression.class);
        featureValue.getOwnedRelatedElement().add(featureReferenceExpression);
        Membership membership = this.builder.createIn(Membership.class, featureReferenceExpression);
        membership.setMemberElement(memberElement);
    }
}
