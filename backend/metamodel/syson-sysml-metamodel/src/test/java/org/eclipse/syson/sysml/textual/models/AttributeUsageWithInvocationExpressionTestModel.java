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
import org.eclipse.syson.sysml.InvocationExpression;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.ParameterMembership;
import org.eclipse.syson.sysml.util.ModelBuilder;

/**
 * Test model for AttributeUsage with SequenceExpression.
 *
 * <pre>
 * attribute attribute1 = fuel(front);
 * </pre>
 *
 * @author wbilem
 */
public class AttributeUsageWithInvocationExpressionTestModel {

    private final ModelBuilder builder = new ModelBuilder();

    private final AttributeUsage attributeUsage;

    private final AttributeUsage fuel;

    private final AttributeUsage front;

    public AttributeUsageWithInvocationExpressionTestModel() {
        this.attributeUsage = this.builder.createWithName(AttributeUsage.class, "attribute1");
        this.fuel = this.builder.createWithName(AttributeUsage.class, "fuel");
        this.front = this.builder.createWithName(AttributeUsage.class, "front");

        this.attributeUsageWithInvocationExpression();
    }

    public AttributeUsage getAttributeUsage() {
        return this.attributeUsage;
    }

    private void attributeUsageWithInvocationExpression() {
        FeatureValue featureValue = this.builder.createIn(FeatureValue.class, this.attributeUsage);

        InvocationExpression invocationExpression = this.builder.create(InvocationExpression.class);
        featureValue.getOwnedRelatedElement().add(invocationExpression);
        this.builder.setType(invocationExpression, this.fuel);

        ParameterMembership param1 = this.builder.createIn(ParameterMembership.class, invocationExpression);
        Feature feature1 = this.builder.create(Feature.class);
        this.createFeatureReference(feature1, this.front);
        param1.getOwnedRelatedElement().add(feature1);
    }

    private void createFeatureReference(Feature feature, AttributeUsage attr) {
        FeatureValue featureValue = this.builder.createIn(FeatureValue.class, feature);
        FeatureReferenceExpression featureReferenceExpression = this.builder.create(FeatureReferenceExpression.class);
        featureValue.getOwnedRelatedElement().add(featureReferenceExpression);
        Membership membership = this.builder.createIn(Membership.class, featureReferenceExpression);
        membership.setMemberElement(attr);
    }
}
