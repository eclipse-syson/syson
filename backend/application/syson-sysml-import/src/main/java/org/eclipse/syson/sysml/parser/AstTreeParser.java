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
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Import;
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
        List<EObject> rootElements;
        if (!astJson.isEmpty()) {
            rootElements = this.parseJsonNode(astJson);
        } else {
            rootElements = List.of();
        }

        return rootElements;
    }

    public List<EObject> parseJsonNode(final JsonNode astJson) {
        List<EObject> result = null;
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
        astJson.forEach(t -> {
            result.addAll(this.parseJsonNode(t));
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

    public void resolveAllImport(final Resource rootResource) {
        rootResource.getContents().forEach(content -> {
            this.resolveAllImport(content);
        });
    }

    public void resolveAllImport(final EObject parent) {

        if (parent instanceof Import parentImport) {
            LOGGER.debug("Resolve Import " + parentImport);
            this.proxyResolver.resolveAllProxy(parent);
        }

        parent.eContents().forEach(content -> {
            this.resolveAllImport(content);
        });
    }

    public void resolveAllReference(final Resource rootResource) {
        rootResource.getContents().forEach(content -> {
            this.resolveAllReference(content);
        });
    }

    public void resolveAllReference(final EObject parent) {
        this.proxyResolver.resolveAllProxy(parent);

        parent.eContents().forEach(content -> {
            this.resolveAllReference(content);
        });
    }

}
