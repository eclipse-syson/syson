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

import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureReferenceExpression;
import org.eclipse.syson.sysml.FeatureValue;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.OperatorExpression;
import org.eclipse.syson.sysml.ParameterMembership;
import org.eclipse.syson.sysml.util.ModelBuilder;

/**
 * Test model for AttributeUsage with BinaryOperatorExpression.
 *
 * <pre>
 * attribute attribute1 = bestFuel + idlingFuel * fuel;
 * </pre>
 *
 * @author wbilem
 */
public class AttributeUsageWithBinaryOperatorExpressionTestModel {

    private final ModelBuilder builder = new ModelBuilder();

    private final AttributeUsage attributeUsage;

    private final AttributeUsage bestFuelConsumption;

    private final AttributeUsage idlingFuelConsumption;

    private final AttributeUsage tpdAvg;

    public AttributeUsageWithBinaryOperatorExpressionTestModel() {
        this.attributeUsage = this.builder.createWithName(AttributeUsage.class, "attribute1");
        this.bestFuelConsumption = this.builder.createWithName(AttributeUsage.class, "bestFuel");
        this.idlingFuelConsumption = this.builder.createWithName(AttributeUsage.class, "idlingFuel");
        this.tpdAvg = this.builder.createWithName(AttributeUsage.class, "fuel");

        this.attributeUsageWithBinaryOperatorExpression();
    }
    
    public AttributeUsage getAttributeUsage() {
        return this.attributeUsage;
    }

    private void attributeUsageWithBinaryOperatorExpression() {
        OperatorExpression plusExpression = this.builder.create(OperatorExpression.class);
        plusExpression.setOperator("+");

        FeatureValue featureValue = this.builder.createIn(FeatureValue.class, this.attributeUsage);
        featureValue.getOwnedRelatedElement().add(plusExpression);

        ParameterMembership plusParam1 = this.builder.createIn(ParameterMembership.class, plusExpression);
        Feature plusFeature1 = this.builder.create(Feature.class);
        plusParam1.getOwnedRelatedElement().add(plusFeature1);
        this.createFeatureReference(plusFeature1, this.bestFuelConsumption);

        ParameterMembership plusParam2 = this.builder.createIn(ParameterMembership.class, plusExpression);
        Feature plusFeature2 = this.builder.create(Feature.class);
        plusParam2.getOwnedRelatedElement().add(plusFeature2);

        OperatorExpression multiplyExpression = this.builder.create(OperatorExpression.class);
        multiplyExpression.setOperator("*");

        FeatureValue featureValue2 = this.builder.createIn(FeatureValue.class, plusFeature2);
        featureValue2.getOwnedRelatedElement().add(multiplyExpression);

        plusFeature2.getOwnedRelationship().add(this.builder.createIn(ParameterMembership.class, multiplyExpression));

        ParameterMembership multiplyParam1 = this.builder.createIn(ParameterMembership.class, multiplyExpression);
        Feature multiplyFeature1 = this.builder.create(Feature.class);
        multiplyParam1.getOwnedRelatedElement().add(multiplyFeature1);
        this.createFeatureReference(multiplyFeature1, this.idlingFuelConsumption);

        ParameterMembership multiplyParam2 = this.builder.createIn(ParameterMembership.class, multiplyExpression);
        Feature multiplyFeature2 = this.builder.create(Feature.class);
        multiplyParam2.getOwnedRelatedElement().add(multiplyFeature2);
        this.createFeatureReference(multiplyFeature2, this.tpdAvg);
    }

    private void createFeatureReference(Feature feature, AttributeUsage attr) {
        FeatureValue featureValue = this.builder.createIn(FeatureValue.class, feature);
        FeatureReferenceExpression featureReferenceExpression = this.builder.create(FeatureReferenceExpression.class);
        featureValue.getOwnedRelatedElement().add(featureReferenceExpression);
        Membership membership = this.builder.createIn(Membership.class, featureReferenceExpression);
        membership.setMemberElement(attr);
    }
}
