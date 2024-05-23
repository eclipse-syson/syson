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

import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.syson.sysml.Classifier;
import org.eclipse.syson.sysml.ConjugatedPortTyping;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.Featuring;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.PortDefinition;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.Specialization;
import org.eclipse.syson.sysml.Subclassification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * AstReferenceParser.
 *
 * @author gescande.
 */
public class AstContainmentReferenceParser {


    private static final Logger LOGGER = LoggerFactory.getLogger(AstContainmentReferenceParser.class);

    private AstTreeParser astTreeParser;

    public void populateContainmentReference(final EObject eObject, final JsonNode astJson) {
        final List<JsonNode> ownedJson = getOwnedJsonNode(astJson);
        final List<EObject> ownedObject = ownedJson.stream().flatMap(t -> {
            return astTreeParser.parseJsonNode(t).stream();
        }).toList();

        ownedObject.stream().forEach(t -> ownObject(eObject, t));
        
    }

    public void ownObject(final EObject owner, final EObject owned) {
        
        LOGGER.trace("ownObject " + owner + " " + owned);

        if (owner instanceof final Relationship ownerRelationship && owned instanceof final Element ownedElement) {
            LOGGER.trace("ownerRelationship");
            ownedElement.setOwningRelationship(ownerRelationship);
        }
        
        if (owner instanceof final OwningMembership ownerOwningMembership) {
            LOGGER.trace("ownerOwningMembership");
            ownerOwningMembership.setMemberElement((Element) owned);
        }

        if (owned instanceof final OwningMembership ownedOwningMembership) {
            LOGGER.trace("ownedOwningMembership");
            if (owner instanceof final Element ownerElement) {
                ownedOwningMembership.setOwningRelatedElement(ownerElement);
            }
        }
      
        if (owner instanceof final Featuring ownerFeatureMembership && owned instanceof final Feature ownedFeature) {
            LOGGER.trace("ownedFeature");
            ownerFeatureMembership.setFeature(ownedFeature);
        }
        
        if (owner instanceof final Feature ownerFeature && owned instanceof final FeatureTyping ownedFeatureTyping) {
            LOGGER.trace("ownerFeature");
            ownedFeatureTyping.setTypedFeature(ownerFeature);
        }

        if (owner instanceof final Feature ownerFeature && owned instanceof final Specialization ownedSpecialization) {
            LOGGER.trace("ownerFeature");
            ownedSpecialization.setSpecific(ownerFeature);
        }

        if (owner instanceof final Feature ownerFeature && owned instanceof final Redefinition ownedRedefinition) {
            LOGGER.trace("ownerFeature");
            ownedRedefinition.setRedefiningFeature(ownerFeature);
        }
        
        if (owner instanceof final Classifier ownerClassifier && owned instanceof final Subclassification ownedSubclassification) {
            LOGGER.trace("ownedSubclassification");
            ownedSubclassification.setSubclassifier(ownerClassifier);
        }

        if (owner instanceof final ConjugatedPortTyping ownerConjugatedPortTyping && owned instanceof final PortDefinition ownedPortDefinition) {
            LOGGER.trace("ownerConjugatedPortDefinition");
            ownerConjugatedPortTyping.setConjugatedPortDefinition(ownedPortDefinition.getConjugatedPortDefinition());
        }

        if (owner instanceof final Element ownerElement && owned instanceof final Relationship ownedRelationship) {
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
