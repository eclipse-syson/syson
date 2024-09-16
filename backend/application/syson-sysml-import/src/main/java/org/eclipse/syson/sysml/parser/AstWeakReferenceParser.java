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
import org.eclipse.syson.sysml.MembershipImport;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.NamespaceImport;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.Specialization;
import org.eclipse.syson.sysml.Subclassification;
import org.eclipse.syson.sysml.Type;

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
        final Map<String, List<JsonNode>> notOwnedJson = this.getNotOwnedJsonNode(astJson);
        notOwnedJson.forEach((referenceType, referenceList) -> {
            referenceList.forEach(referenceAST -> {
                String qualifiedNameTarget = referenceAST.get(AstConstant.TEXT_CONST).asText();
                if (qualifiedNameTarget == null || qualifiedNameTarget.isBlank() || qualifiedNameTarget.equals("null")) {
                    qualifiedNameTarget = referenceAST.get(AstConstant.REFERENCE_CONST).asText();
                }
                final EObject reference = this.astObjectParser.createObject(referenceAST);
                if (reference instanceof InternalEObject internalTarget) {
                    internalTarget.eSetProxyURI(URI.createGenericURI("syson-import", AstConstant.QUALIFIED_CONST, qualifiedNameTarget));
                    this.setReferenceSpecificAttributes(referenceType, reference, owner);
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
            case AstConstant.TARGET_REF_CONST:
                if (reference instanceof Type referenceAsType && owner instanceof Featuring ownerFeaturing) {
                    ownerFeaturing.setType(referenceAsType);
                }
                if (reference instanceof Type referenceAsType && owner instanceof Specialization ownerSpecialization) {
                    ownerSpecialization.setGeneral(referenceAsType);
                }
                if (reference instanceof Type referenceAsType && owner instanceof FeatureTyping ownerFeatureTyping) {
                    ownerFeatureTyping.setType(referenceAsType);
                }
                if (reference instanceof Feature referenceAsFeature && owner instanceof Redefinition ownerRedefinition) {
                    ownerRedefinition.setRedefinedFeature(referenceAsFeature);
                }
                if (reference instanceof Classifier referenceAsClassifier && owner instanceof Subclassification ownerAsSubclassification) {
                    ownerAsSubclassification.setSuperclassifier(referenceAsClassifier);
                }
                if (reference instanceof Namespace referenceAsNamespace && owner instanceof NamespaceImport ownerAsNamespaceImport) {
                    ownerAsNamespaceImport.setImportedNamespace(referenceAsNamespace);
                }
                if (reference instanceof Element referenceAsElement && owner instanceof Membership ownerMembership) {
                    ownerMembership.setMemberName(ownerMembership.getDeclaredName()); // To manage isAlias
                    ownerMembership.setMemberElement(referenceAsElement);
                }
                if (reference instanceof Element referenceAsElement && owner instanceof Relationship ownerRelationship) {
                    ownerRelationship.getTarget().add(referenceAsElement);
                }
                if (reference instanceof Membership referenceAsMembership && owner instanceof MembershipImport ownerMembershipImport) {
                    ownerMembershipImport.setImportedMembership(referenceAsMembership);
                }
                break;
            default:
                break;
        }
    }

}
