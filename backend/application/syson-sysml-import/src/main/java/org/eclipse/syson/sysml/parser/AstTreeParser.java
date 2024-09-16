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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.syson.sysml.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AstTreeParser.
 *
 * @author gescande.
 */
public class AstTreeParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(AstTreeParser.class);

    private final AstContainmentReferenceParser astContainmentReferenceParser;

    private final AstWeakReferenceParser astWeakReferenceParser;

    private final ProxyResolver proxyResolver;

    private final AstObjectParser astObjectParser;

    public AstTreeParser(final AstContainmentReferenceParser astContainmentReferenceParser, final AstWeakReferenceParser astWeakReferenceParser, final ProxyResolver proxyResolver,
            final AstObjectParser astObjectParser) {
        this.astContainmentReferenceParser = astContainmentReferenceParser;
        this.astWeakReferenceParser = astWeakReferenceParser;
        this.proxyResolver = proxyResolver;
        this.astObjectParser = astObjectParser;
    }

    public List<EObject> parseAst(final JsonNode astJson) {
        if (!astJson.isEmpty()) {
            return this.parseJsonNode(astJson);
        }
        return List.of();
    }

    public List<EObject> parseJsonNode(final JsonNode astJson) {
        List<EObject> result;
        if (astJson == null) {
            result = List.of();
        } else if (astJson.isArray()) {
            result = this.parseJsonArray(astJson);
        } else {
            result = this.parseJsonObject(astJson);
        }
        return result;
    }

    public List<EObject> parseJsonArray(final JsonNode astJson) {
        final List<EObject> result = new ArrayList<>();
        astJson.forEach(jsonNode -> {
            result.addAll(this.parseJsonNode(jsonNode));
        });
        return result;
    }

    public List<EObject> parseJsonObject(final JsonNode astJson) {
        final List<EObject> result = new ArrayList<>();

        final EObject eObject = this.astObjectParser.createObject(astJson);
        if (eObject != null) {
            this.astObjectParser.setObjectAttribute(eObject, astJson);
            this.astContainmentReferenceParser.populateContainmentReference(eObject, astJson);
            this.astWeakReferenceParser.proxyNonContainmentReference((Element) eObject, astJson);

            result.add(eObject);
        } else {
            LOGGER.error("Error building the object " + astJson);
        }

        return result;
    }

    public List<ProxiedReference> collectUnresolvedReferences(final Resource rootResource) {
        List<ProxiedReference> unresolvedReferences =  new ArrayList<>();
        rootResource.getAllContents().forEachRemaining(eObject -> {
            final List<EReference> allReferences = eObject.eClass().getEAllReferences().stream()
                    .filter(reference -> !reference.isContainment() && !reference.isDerived())
                    .toList();

            for (EReference reference : allReferences) {
                if (reference.isMany()) {
                    final Object referenceList = eObject.eGet(reference, false);
                    if (referenceList instanceof Collection referenceCollection) {
                        for (final Object target : referenceCollection) {
                            if (target instanceof InternalEObject eTarget && eTarget.eIsProxy()) {
                                unresolvedReferences.add(new ProxiedReference(eObject, reference, eTarget));
                            }
                        }
                    }
                } else {
                    final Object target = eObject.eGet(reference, false);
                    if (target instanceof InternalEObject eTarget && eTarget.eIsProxy()) {
                        unresolvedReferences.add(new ProxiedReference(eObject, reference, eTarget));
                    }
                }
            }
        });
        return unresolvedReferences;
    }

    public void resolveAllReference(List<ProxiedReference> unresolvedReferences) {
        List<ProxiedReference> unresolvedReferencesTmp = new ArrayList<>(unresolvedReferences);
        // There can be at first unsolvable references or aliases because they depend on another yet unresolved element.
        // Iterating over every unresolved reference until every single one is solved or no new resolution is possible.
        while (!unresolvedReferencesTmp.isEmpty()) {
            List<ProxiedReference> proxiedReferences = unresolvedReferencesTmp.stream()
                    .filter(proxiedReference -> !this.proxyResolver.resolveProxy(proxiedReference))
                    .toList();
            if (proxiedReferences.equals(unresolvedReferencesTmp)) {
                proxiedReferences.forEach(proxiedReference -> {
                    LOGGER.error("Unable to find object with qualifiedName {} in namespace.", proxiedReference.targetProxy().eProxyURI().fragment());
                });
                break;
            } else {
                unresolvedReferencesTmp = proxiedReferences;
            }
        }
    }
}
