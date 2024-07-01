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
import org.eclipse.syson.sysml.parser.AstTreeParser;
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

    public Resource convertResource(InputStream input, ResourceSet editingDomainResourceSet) {

        Resource result = new JSONResourceFactory().createResource(new JSONResourceFactory().createResourceURI(null));
        editingDomainResourceSet.getResources().add(result);
        if (input != null) {
            JsonNode astJson = readAst(input);
            if (astJson != null) {
                logger.info("Create the Root eObject containment structure");
                List<EObject> rootSysmlObjects = AstTreeParser.parseAst(astJson);
                result.getContents().addAll(rootSysmlObjects);
                logger.info("End of create the Root eObject containment structure");
                logger.info("Try to resolve Imports");
                AstTreeParser.resolveAllImport(result);
                logger.info("End of import resolving");
                logger.info("Try to resolve all references");
                AstTreeParser.resolveAllReference(result);
                logger.info("End of references resolving");
            }

        }
        return result;
    }

    private JsonNode readAst(InputStream input) {
        ObjectMapper objectMapper = new ObjectMapper();

        // Read JSON file and map to JSON Object
        try {
            return objectMapper.readTree(input);

        } catch (IOException e) {
            this.logger.error(e.getMessage());
            return null;
        }
    }
}