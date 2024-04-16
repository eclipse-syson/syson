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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.components.emf.services.JSONResourceFactory;
import org.eclipse.syson.sysml.mapper.CachedObjectFinder;
import org.eclipse.syson.sysml.mapper.MapperArray;
import org.eclipse.syson.sysml.mapper.MapperComment;
import org.eclipse.syson.sysml.mapper.MapperConjugatedPortTyping;
import org.eclipse.syson.sysml.mapper.MapperConnectionUsage;
import org.eclipse.syson.sysml.mapper.MapperCreateKnownType;
import org.eclipse.syson.sysml.mapper.MapperDependency;
import org.eclipse.syson.sysml.mapper.MapperEventOccurrenceUsage;
import org.eclipse.syson.sysml.mapper.MapperFeatureChaining;
import org.eclipse.syson.sysml.mapper.MapperFeatureReferenceExpression;
import org.eclipse.syson.sysml.mapper.MapperFeatureTyping;
import org.eclipse.syson.sysml.mapper.MapperFlowConnectionUsage;
import org.eclipse.syson.sysml.mapper.MapperGenericAttributes;
import org.eclipse.syson.sysml.mapper.MapperGenericReferences;
import org.eclipse.syson.sysml.mapper.MapperLiteralInteger;
import org.eclipse.syson.sysml.mapper.MapperLiteralRational;
import org.eclipse.syson.sysml.mapper.MapperLiteralString;
import org.eclipse.syson.sysml.mapper.MapperMembership;
import org.eclipse.syson.sysml.mapper.MapperMembershipImport;
import org.eclipse.syson.sysml.mapper.MapperMembershipReference;
import org.eclipse.syson.sysml.mapper.MapperNamespace;
import org.eclipse.syson.sysml.mapper.MapperNamespaceImport;
import org.eclipse.syson.sysml.mapper.MapperOperatorExpression;
import org.eclipse.syson.sysml.mapper.MapperRedefinition;
import org.eclipse.syson.sysml.mapper.MapperReferenceSubsetting;
import org.eclipse.syson.sysml.mapper.MapperSpecialization;
import org.eclipse.syson.sysml.mapper.MapperSubclassification;
import org.eclipse.syson.sysml.mapper.MapperSubsetting;
import org.eclipse.syson.sysml.mapper.MapperUsage;
import org.eclipse.syson.sysml.mapper.MapperVisitorInterface;
import org.eclipse.syson.sysml.mapper.MappingElement;
import org.eclipse.syson.sysml.mapper.MappingState;
import org.eclipse.syson.sysml.mapper.ObjectFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Transforms AST data using defined mappings and updates resources accordingly.
 *
 * @author gescande.
 */
public class ASTTransformer {

    private static final String CHILDREN_CONST = "children";

    private static final String TARGET_CONST = "target";

    private final ObjectFinder objectFinder = new CachedObjectFinder();

    private final MappingState mappingStates = new MappingState(Collections.synchronizedCollection(new LinkedHashSet<MappingElement>()),
            Collections.synchronizedCollection(new LinkedHashSet<MappingElement>()), Collections.synchronizedCollection(new LinkedHashSet<MappingElement>()));

    private final List<MapperVisitorInterface> mappers = List.of(
            new MapperArray(this.objectFinder, this.mappingStates),
            new MapperCreateKnownType(this.objectFinder, this.mappingStates),
            new MapperGenericAttributes(this.objectFinder, this.mappingStates),
            new MapperGenericReferences(this.objectFinder, this.mappingStates),
            new MapperFeatureTyping(this.objectFinder, this.mappingStates),
            new MapperFlowConnectionUsage(this.objectFinder, this.mappingStates),
            new MapperMembership(this.objectFinder, this.mappingStates),
            new MapperMembershipImport(this.objectFinder, this.mappingStates),
            new MapperNamespace(this.objectFinder, this.mappingStates),
            new MapperNamespaceImport(this.objectFinder, this.mappingStates),
            new MapperRedefinition(this.objectFinder, this.mappingStates),
            new MapperSubclassification(this.objectFinder, this.mappingStates),
            new MapperFeatureChaining(this.objectFinder, this.mappingStates),
            new MapperSubsetting(this.objectFinder, this.mappingStates),
            new MapperUsage(this.objectFinder, this.mappingStates),
            new MapperOperatorExpression(this.objectFinder, this.mappingStates),
            new MapperLiteralInteger(this.objectFinder, this.mappingStates),
            new MapperLiteralRational(this.objectFinder, this.mappingStates),
            new MapperFeatureReferenceExpression(this.objectFinder, this.mappingStates),
            new MapperConnectionUsage(this.objectFinder, this.mappingStates),
            new MapperEventOccurrenceUsage(this.objectFinder, this.mappingStates),
            new MapperDependency(this.objectFinder, this.mappingStates),
            new MapperSpecialization(this.objectFinder, this.mappingStates),
            new MapperMembershipReference(this.objectFinder, this.mappingStates),
            new MapperLiteralString(this.objectFinder, this.mappingStates),
            new MapperReferenceSubsetting(this.objectFinder, this.mappingStates),
            new MapperConjugatedPortTyping(this.objectFinder, this.mappingStates),
            new MapperComment(this.objectFinder, this.mappingStates)
            );

    private final Logger logger = LoggerFactory.getLogger(ASTTransformer.class);

    public Resource convertResource(InputStream input, List<EObject> list) {
        list.parallelStream().forEach((t) -> {
            if (t != null) {
                this.objectFinder.putEObject(t);
            }
        });

        Resource result = new JSONResourceFactory().createResource(new JSONResourceFactory().createResourceURI("test"));

        ObjectMapper objectMapper = new ObjectMapper();

        // Read JSON file and map to JSON Object
        try {
            JsonNode astJson = objectMapper.readTree(input);

            if (astJson.get(CHILDREN_CONST) != null) {
                astJson.get(CHILDREN_CONST).forEach(t -> this.mappingStates.toMap().add(new MappingElement(t.get(TARGET_CONST), null)));
    
                List<MappingElement> rootElements = List.copyOf(this.mappingStates.toMap());
    
                // Static Mapping
                while (!this.mappingStates.toMap().isEmpty()) {
                    this.logger.info("Start Mapping loop with " + this.mappingStates.toMap().size() + " elements");
                    LinkedHashSet<MappingElement> toOperate = new LinkedHashSet<>(this.mappingStates.toMap());
                    this.mappingStates.toMap().clear();
                    toOperate.parallelStream().forEach(mappingState -> {
                        this.mappers.forEach(t -> {
                            if (t.canVisit(mappingState)) {
                                try {
                                    t.mappingVisit(mappingState);
                                } catch (ClassCastException | IndexOutOfBoundsException e) {
                                    this.logger.error("Error during mapping of element " + mappingState + " : " + e.getMessage());
                                }
                            }
                        });
                    });
                }
                // Reference Mapping
                while (!this.mappingStates.toResolve().isEmpty()) {
                    this.logger.info("Start Resolving loop with " + this.mappingStates.toResolve().size() + " elements");
                    LinkedHashSet<MappingElement> toOperate = new LinkedHashSet<>(this.mappingStates.toResolve());
                    this.mappingStates.toResolve().clear();
                    toOperate.parallelStream().forEach(mappingState -> {
                        this.mappers.forEach(t -> {
                            if (t.canVisit(mappingState)) {
                                try {
                                    t.referenceVisit(mappingState);
                                } catch (ClassCastException | IndexOutOfBoundsException e) {
                                    this.logger.error("Error during referenceVisit of element " + mappingState + " : " + e.getMessage());
                                }
                            }
                        });
                    });
                }
    
                this.logger.info("End complete mapping loop");
                rootElements.forEach(t -> result.getContents().add(t.getSelf()));
            }

        } catch (IOException e) {
            this.logger.error(e.getMessage());
        }
        return result;
    }
}
