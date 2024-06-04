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
import org.eclipse.syson.sysml.FeatureChainExpression;
import org.eclipse.syson.sysml.FeatureChaining;
import org.eclipse.syson.sysml.FeatureReferenceExpression;
import org.eclipse.syson.sysml.FeatureValue;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.OperatorExpression;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.ParameterMembership;
import org.eclipse.syson.sysml.util.ModelBuilder;

/**
 * Test model for AttributeUsage with FeatureChainExpression.
 *
 * <pre>
 * attribute attribute1 = (fuel.mass, front.mass, rear.drives.mass);
 * </pre>
 *
 * @author wbilem
 */
public class AttributeUsageWithFeatureChainExpressionTestModel {

    private final ModelBuilder builder = new ModelBuilder();

    private final AttributeUsage attributeUsage;

    private final AttributeUsage fuel;

    private final AttributeUsage front;

    private final AttributeUsage rear;

    private final AttributeUsage drives;

    private final AttributeUsage mass;

    public AttributeUsageWithFeatureChainExpressionTestModel() {
        this.attributeUsage = this.builder.createWithName(AttributeUsage.class, "attribute1");
        this.fuel = this.builder.createWithName(AttributeUsage.class, "fuel");
        this.front = this.builder.createWithName(AttributeUsage.class, "front");
        this.rear = this.builder.createWithName(AttributeUsage.class, "rear");
        this.drives = this.builder.createWithName(AttributeUsage.class, "drives");
        this.mass = this.builder.createWithName(AttributeUsage.class, "mass");

        this.attributeUsageWithFeatureChainExpression();
    }

    public AttributeUsage getAttributeUsage() {
        return this.attributeUsage;
    }

    private void attributeUsageWithFeatureChainExpression() {
        FeatureValue featureValue = this.builder.createIn(FeatureValue.class, this.attributeUsage);

        OperatorExpression sequenceExpression = this.createSequenceExpression(featureValue, this.fuel, this.mass);

        ParameterMembership param1 = this.builder.createIn(ParameterMembership.class, sequenceExpression);
        Feature feature1 = this.builder.create(Feature.class);
        FeatureValue featureValue1 = this.builder.createIn(FeatureValue.class, feature1);
        param1.getOwnedRelatedElement().add(feature1);

        this.createSequenceExpression(featureValue1, this.front, this.mass);

        ParameterMembership param3 = this.builder.createIn(ParameterMembership.class, sequenceExpression);
        Feature feature3 = this.builder.create(Feature.class);
        FeatureValue featureValue3 = this.builder.createIn(FeatureValue.class, feature3);
        param3.getOwnedRelatedElement().add(feature3);

        this.createFeatureChainExpressionWithFeatureChaining(featureValue3, this.rear, this.drives, this.mass);
    }

    private void createFeatureReference(Feature feature, AttributeUsage attr) {
        FeatureValue featureValue = this.builder.createIn(FeatureValue.class, feature);
        FeatureReferenceExpression featureReferenceExpression = this.builder.create(FeatureReferenceExpression.class);
        featureValue.getOwnedRelatedElement().add(featureReferenceExpression);
        Membership membership = this.builder.createIn(Membership.class, featureReferenceExpression);
        membership.setMemberElement(attr);
    }

    private OperatorExpression createSequenceExpression(FeatureValue container, AttributeUsage attr1, AttributeUsage attr2) {
        OperatorExpression sequenceExpression = this.builder.create(OperatorExpression.class);
        sequenceExpression.setOperator(",");

        container.getOwnedRelatedElement().add(sequenceExpression);

        ParameterMembership param = this.builder.createIn(ParameterMembership.class, sequenceExpression);
        Feature feature = this.builder.create(Feature.class);
        FeatureValue featureValue = this.builder.createIn(FeatureValue.class, feature);
        param.getOwnedRelatedElement().add(feature);

        this.createFeatureChainExpression(featureValue, attr1, attr2);

        return sequenceExpression;
    }

    private void createFeatureChainExpression(FeatureValue container, AttributeUsage attr1, AttributeUsage attr2) {
        FeatureChainExpression chainExpression = this.builder.create(FeatureChainExpression.class);
        container.getOwnedRelatedElement().add(chainExpression);

        ParameterMembership param2 = this.builder.createIn(ParameterMembership.class, chainExpression);
        Feature feature2 = this.builder.create(Feature.class);
        param2.getOwnedRelatedElement().add(feature2);
        this.createFeatureReference(feature2, attr1);

        Membership membership = this.builder.createIn(Membership.class, chainExpression);
        membership.setMemberElement(attr2);
    }

    private void createFeatureChainExpressionWithFeatureChaining(FeatureValue container, AttributeUsage attr1, AttributeUsage attr2, AttributeUsage attr3) {
        FeatureChainExpression chainExpression = this.builder.create(FeatureChainExpression.class);
        container.getOwnedRelatedElement().add(chainExpression);

        ParameterMembership param2 = this.builder.createIn(ParameterMembership.class, chainExpression);
        Feature feature2 = this.builder.create(Feature.class);
        param2.getOwnedRelatedElement().add(feature2);
        this.createFeatureReference(feature2, attr1);

        OwningMembership membership = this.builder.createIn(OwningMembership.class, chainExpression);
        Feature feature3 = this.builder.create(Feature.class);
        membership.getOwnedRelatedElement().add(feature3);

        FeatureChaining chaining1 = this.builder.createIn(FeatureChaining.class, feature3);
        chaining1.setChainingFeature(attr2);

        FeatureChaining chaining2 = this.builder.createIn(FeatureChaining.class, feature3);
        chaining2.setChainingFeature(attr3);
    }
}
