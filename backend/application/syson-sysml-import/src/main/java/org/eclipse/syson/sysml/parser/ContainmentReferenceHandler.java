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
package org.eclipse.syson.sysml.parser;

import com.fasterxml.jackson.databind.JsonNode;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.FeatureValue;
import org.eclipse.syson.sysml.InvocationExpression;
import org.eclipse.syson.sysml.ParameterMembership;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.parser.translation.EClassifierTranslator;
import org.eclipse.syson.sysml.parser.translation.EReferenceComputer;
import org.eclipse.syson.sysml.utils.LogNameProvider;
import org.eclipse.syson.sysml.utils.MessageReporter;

/**
 * Handler of containment references.
 *
 * @author Arthur Daussy.
 */
public class ContainmentReferenceHandler {

    private final EClassifierTranslator typeBuilder = new EClassifierTranslator();

    private final LogNameProvider logNameProvider = new LogNameProvider();

    private final EReferenceComputer referenceTranslator = new EReferenceComputer();

    private final ReferenceNodeTester referenceNodeTester = new ReferenceNodeTester();

    private final MessageReporter messageReporter;

    public ContainmentReferenceHandler(MessageReporter messageReporter) {
        this.messageReporter = Objects.requireNonNull(messageReporter);
    }

    public boolean isContainmentReference(final EObject eObject, String referenceName, final JsonNode astValue) {
        if (astValue.isContainerNode()) {
            JsonNode referenceTypeNode = astValue.get("$type");
            return referenceTypeNode != null && referenceTypeNode.isTextual() && !this.referenceNodeTester.test(astValue);
        }
        return false;
    }

    public EObject handleContainmentReference(final EObject eObject, String referenceName, final JsonNode astValue) {
        final EObject newObject;
        final EObject referenceTypeInstance = this.typeBuilder.createObject(astValue);

        if (referenceTypeInstance != null) {
            newObject = this.addChild(eObject, referenceName, astValue, referenceTypeInstance);
        } else {
            String msg = MessageFormat.format("Unexpected child type in {0} with reference {1} for holding {2}", this.logNameProvider.getName(eObject), referenceName, astValue);
            this.messageReporter.error(msg);
            newObject = null;
        }
        return newObject;
    }

    private EObject addChild(final EObject eObject, String referenceName, final JsonNode astValue, final EObject refrenceTypeInstance) {
        final EObject newObject;
        this.addChildIn(eObject, refrenceTypeInstance, referenceName);
        if (refrenceTypeInstance.eContainer() != null) {
            newObject = refrenceTypeInstance;
        } else {
            String msg = MessageFormat.format("Unable to find containment reference in {0} with reference {1} for holding {2}", this.logNameProvider.getName(eObject), referenceName,
                    astValue);
            this.messageReporter.error(msg);
            newObject = null;
        }
        return newObject;
    }

    private void setValue(final EObject eObject, EReference ref, EObject value) {
        if (ref.isMany()) {
            ((List<Object>) eObject.eGet(ref)).add(value);
        } else {
            eObject.eSet(ref, value);
        }
    }

    private boolean addChildIn(final EObject owner, final EObject owned, String referenceName) {

        if ("operands".equals(referenceName) && owned instanceof Expression ownedExpression && owner instanceof InvocationExpression invocationExpression) {
            // In this special case, we are handling parameters of an InvocationExpression
            // The current implementation of sysIDe returns in the "operands" list directly the ValueExpression of the
            // parameter. The specification requires here a ParameterMembership holding a Feature with a FeatureValue
            // holding the given expression.
            ParameterMembership paramMembership = SysmlFactory.eINSTANCE.createParameterMembership();
            Feature feature = SysmlFactory.eINSTANCE.createFeature();
            feature.setDirection(FeatureDirectionKind.IN);
            paramMembership.getOwnedRelatedElement().add(feature);
            FeatureValue featureValue = SysmlFactory.eINSTANCE.createFeatureValue();
            feature.getOwnedRelationship().add(featureValue);
            featureValue.getOwnedRelatedElement().add(ownedExpression);
            invocationExpression.getOwnedRelationship().add(paramMembership);
        } else {

            Optional<EReference> optContainementReference = this.referenceTranslator.getContainmentReference(owner, owned.eClass(), referenceName);
            if (optContainementReference.isPresent()) {
                this.setValue(owner, optContainementReference.get(), owned);
            }
        }

        // Some relationship should set their source feature set with their container value. For the moment only the one
        // under an "heritage" JSNode seems to be required
        if (owned instanceof Relationship && "heritage".equals(referenceName)) {
            Optional<EReference> sourceFeature = this.referenceTranslator.getNonContainmentReference(owned.eClass(), owner.eClass(), "source");
            if (sourceFeature.isPresent() && !sourceFeature.get().isContainer()) {
                this.setValue(owned, sourceFeature.get(), owner);
            }
        }
        return true;
    }
}
