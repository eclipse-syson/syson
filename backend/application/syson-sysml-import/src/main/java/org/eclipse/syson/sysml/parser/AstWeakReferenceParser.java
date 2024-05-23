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
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.syson.sysml.AstConstant;
import org.eclipse.syson.sysml.Classifier;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.Featuring;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.NamespaceImport;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.Specialization;
import org.eclipse.syson.sysml.Subclassification;
import org.eclipse.syson.sysml.Type;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * AstReferenceParser.
 *
 * @author gescande.
 */
public class AstWeakReferenceParser {

    private final AstObjectParser astObjectParser;

    public AstWeakReferenceParser(final AstObjectParser astObjectParser) {
        this.astObjectParser = astObjectParser;
    }

    public void proxyNonContainmentReference(final Element owner, final JsonNode astJson) {
        final Map<String, List<JsonNode>> notOwnedJson = getNotOwnedJsonNode(astJson);

        notOwnedJson.forEach((referenceType, referenceList) -> {

            referenceList.forEach(referenceAST -> {
                String qualifiedNameTarget = referenceAST.get(AstConstant.REFERENCE_CONST).asText();
                if (qualifiedNameTarget == null || qualifiedNameTarget.isBlank() || qualifiedNameTarget.equals("null")) {
                    qualifiedNameTarget = referenceAST.get(AstConstant.TEXT_CONST).asText();
                }

                final EObject reference = astObjectParser.createObject(referenceAST);
                
                if (reference instanceof final InternalEObject internalTarget) {

                    internalTarget.eSetProxyURI(URI.createGenericURI("syson-import", "qualifiedName", qualifiedNameTarget));

                    setReferenceSpecificAttributes(referenceType, reference, owner);
                }
            });
        });
    }

    private Map<String, List<JsonNode>> getNotOwnedJsonNode(final JsonNode astJson) {
        return ReferenceHelper.extractNotOwnedObjectByAttribute(astJson);
    }

    private void setReferenceSpecificAttributes(final String referenceType, final EObject reference, final Element owner) {
        // This switch case allow to manage distinct type of non containing references
        switch (referenceType) {
            case AstConstant.TARGET_REF_CONST :
                if (reference instanceof final Type referenceAsType && owner instanceof final Featuring ownerFeaturing) {
                    ownerFeaturing.setType(referenceAsType);
                } 
                if (reference instanceof final Type referenceAsType && owner instanceof final Specialization ownerSpecialization) {
                    ownerSpecialization.setGeneral(referenceAsType);
                } 
                if (reference instanceof final Type referenceAsType && owner instanceof final FeatureTyping ownerFeatureTyping) {
                    ownerFeatureTyping.setType(referenceAsType);
                } 
                if (reference instanceof final Feature referenceAsFeature && owner instanceof final Redefinition ownerRedefinition) {
                    ownerRedefinition.setRedefinedFeature(referenceAsFeature);
                } 
                if (reference instanceof final Classifier referenceAsClassifier && owner instanceof final Subclassification ownerAsSubclassification) {
                    ownerAsSubclassification.setSuperclassifier(referenceAsClassifier);
                } 

                if (reference instanceof final Namespace referenceAsNamespace && owner instanceof final NamespaceImport ownerAsNamespaceImport) {
                    ownerAsNamespaceImport.setImportedNamespace(referenceAsNamespace);
                } 
                if (reference instanceof final Element referenceAsElement && owner instanceof final Membership ownerMembership) {
                    ownerMembership.setMemberName(ownerMembership.getDeclaredName()); // To manage isAlias
                    ownerMembership.setMemberElement(referenceAsElement);
                } 
                if (reference instanceof final Element referenceAsElement && owner instanceof final Relationship ownerRelationship) {
                    ownerRelationship.getTarget().add(referenceAsElement);
                } 
                break;
            default:
                break;
        }
    }
    
}
