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
package org.eclipse.syson.sysml;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.sirius.components.emf.services.JSONResourceFactory;
import org.eclipse.syson.sysml.parser.AstContainmentReferenceParser;
import org.eclipse.syson.sysml.parser.AstObjectParser;
import org.eclipse.syson.sysml.parser.AstTreeParser;
import org.eclipse.syson.sysml.parser.AstWeakReferenceParser;
import org.eclipse.syson.sysml.parser.ProxyResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Transforms AST data using defined mappings and updates resources accordingly.
 *
 * @author gescande.
 */
public class ASTTransformer {

    private final Logger logger = LoggerFactory.getLogger(ASTTransformer.class);

    private final AstTreeParser astTreeParser;

    public ASTTransformer() {
        final ProxyResolver proxyResolver = new ProxyResolver();
        final AstObjectParser astObjectParser = new AstObjectParser();
        final AstContainmentReferenceParser astContainmentReferenceParser = new AstContainmentReferenceParser();
        final AstWeakReferenceParser astWeakReferenceParser = new AstWeakReferenceParser(astObjectParser);
        this.astTreeParser = new AstTreeParser(astContainmentReferenceParser, astWeakReferenceParser, proxyResolver, astObjectParser);
        astContainmentReferenceParser.setAstTreeParser(this.astTreeParser);
    }

    public Resource convertResource(final InputStream input, final ResourceSet resourceSet) {

        final Resource result = new JSONResourceFactory().createResource(new JSONResourceFactory().createResourceURI(null));
        if (input != null) {
            final JsonNode astJson = readAst(input);
            if (astJson != null) {
                this.logger.info("Create the Root eObject containment structure");
                final List<EObject> rootSysmlObjects = this.astTreeParser.parseAst(astJson);
                resourceSet.getResources().add(result);
                result.getContents().addAll(rootSysmlObjects);
                this.logger.info("End of create the Root eObject containment structure");
                this.logger.info("Try to resolve Imports");
                this.astTreeParser.resolveAllImport(result);
                this.logger.info("End of import resolving");
                this.logger.info("Try to resolve all references");
                this.astTreeParser.resolveAllReference(result);
                this.logger.info("End of references resolving");
            }

        }
        return result;
    }

    private JsonNode readAst(final InputStream input) {
        final ObjectMapper objectMapper = new ObjectMapper();

        // Read JSON file and map to JSON Object
        try {
            return objectMapper.readTree(input);

        } catch (final IOException e) {
            this.logger.error(e.getMessage());
            return null;
        }
    }
}
