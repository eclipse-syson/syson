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
package org.eclipse.syson.sysml.parser;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.syson.sysml.Classifier;
import org.eclipse.syson.sysml.ConjugatedPortTyping;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.Featuring;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.PortDefinition;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.Specialization;
import org.eclipse.syson.sysml.Subclassification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AstReferenceParser.
 *
 * @author gescande.
 */
public class AstContainmentReferenceParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(AstContainmentReferenceParser.class);

    private AstTreeParser astTreeParser;

    public void populateContainmentReference(final EObject eObject, final JsonNode astJson) {
        final List<JsonNode> ownedJson = this.getOwnedJsonNode(astJson);
        final List<EObject> ownedObject = ownedJson.stream().flatMap(t -> {
            return this.astTreeParser.parseJsonNode(t).stream();
        }).toList();

        ownedObject.stream().forEach(t -> this.ownObject(eObject, t));

    }

    public void ownObject(final EObject owner, final EObject owned) {

        LOGGER.trace("ownObject " + owner + " " + owned);

        if (owner instanceof Relationship ownerRelationship && owned instanceof Element ownedElement) {
            LOGGER.trace("ownerRelationship");
            ownedElement.setOwningRelationship(ownerRelationship);
        }

        if (owner instanceof OwningMembership ownerOwningMembership) {
            LOGGER.trace("ownerOwningMembership");
            ownerOwningMembership.setMemberElement((Element) owned);
        }

        if (owned instanceof OwningMembership ownedOwningMembership) {
            LOGGER.trace("ownedOwningMembership");
            if (owner instanceof Element ownerElement) {
                ownedOwningMembership.setOwningRelatedElement(ownerElement);
            }
        }

        if (owner instanceof Featuring ownerFeatureMembership && owned instanceof Feature ownedFeature) {
            LOGGER.trace("ownedFeature");
            ownerFeatureMembership.setFeature(ownedFeature);
        }

        if (owner instanceof Feature ownerFeature && owned instanceof FeatureTyping ownedFeatureTyping) {
            LOGGER.trace("ownerFeature");
            ownedFeatureTyping.setTypedFeature(ownerFeature);
        }

        if (owner instanceof Feature ownerFeature && owned instanceof Specialization ownedSpecialization) {
            LOGGER.trace("ownerFeature");
            ownedSpecialization.setSpecific(ownerFeature);
        }

        if (owner instanceof Feature ownerFeature && owned instanceof Redefinition ownedRedefinition) {
            LOGGER.trace("ownerFeature");
            ownedRedefinition.setRedefiningFeature(ownerFeature);
        }

        if (owner instanceof Classifier ownerClassifier && owned instanceof Subclassification ownedSubclassification) {
            LOGGER.trace("ownedSubclassification");
            ownedSubclassification.setSubclassifier(ownerClassifier);
        }

        if (owner instanceof ConjugatedPortTyping ownerConjugatedPortTyping && owned instanceof PortDefinition ownedPortDefinition) {
            LOGGER.trace("ownerConjugatedPortDefinition");
            ownerConjugatedPortTyping.setConjugatedPortDefinition(ownedPortDefinition.getConjugatedPortDefinition());
        }

        if (owner instanceof Element ownerElement && owned instanceof Relationship ownedRelationship
                && !(owned instanceof Namespace)) {
            // The owned object is a Relationship and isn't a Namespace (e.g. Membership, Specialization). We have to
            // set its owning related element and source, and add it to the relationships of its owner. This is not the
            // case if the element is a Relationship and a Namespace: these elements (e.g. Allocation) are handled as
            // Element and not as Relationships and these references shouldn't be set here.
            LOGGER.trace("ownerElement");
            ownerElement.getOwnedRelationship().add(ownedRelationship);
            ownedRelationship.setOwningRelatedElement(ownerElement);
            ownedRelationship.getSource().add(ownerElement);
        }
    }

    public void setAstTreeParser(final AstTreeParser astTreeParser) {
        this.astTreeParser = astTreeParser;
    }

    private List<JsonNode> getOwnedJsonNode(final JsonNode astJson) {
        return ReferenceHelper.extractOwnedObject(astJson);
    }
}
