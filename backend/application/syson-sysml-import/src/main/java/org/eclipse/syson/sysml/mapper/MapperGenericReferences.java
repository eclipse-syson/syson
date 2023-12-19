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
package org.eclipse.syson.sysml.mapper;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.syson.sysml.AstConstant;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.SysmlPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Maps generic references within SysML models to AST elements, handling various node types.
 *
 * @author gescande
 */
public class MapperGenericReferences extends MapperVisitorInterface {

    private final Logger logger = LoggerFactory.getLogger(MapperGenericReferences.class);

    private final List<String> nodes = Arrays.asList(AstConstant.TARGET_CONST, AstConstant.TARGET_CHAIN_CONST, AstConstant.VALUE, AstConstant.RESULT_CONST, AstConstant.SOURCE_CONST,
            AstConstant.THEN_CONST, AstConstant.ACCEPTER_CONST, AstConstant.GUARD_CONST, AstConstant.EFFECT_CONST, AstConstant.RECEIVER_CONST, AstConstant.PAYLOAD_CONST,
            AstConstant.MULTIPLICITY_CONST, AstConstant.RANGE_CONST, AstConstant.ABOUT_CONST, AstConstant.SENDER_CONST, AstConstant.TRANSITION_LINK_SOURCE_CONST);

    private final List<String> arrayNodes = Arrays.asList(AstConstant.CHILDREN_CONST, AstConstant.HERITAGE_CONST, AstConstant.TYPE_RELATIONSHIPS_CONST, AstConstant.ENDS_CONST,
            AstConstant.PREFIXES_CONST);

    public MapperGenericReferences(final ObjectFinder objectFinder, final MappingState mappingState) {
        super(objectFinder, mappingState);
    }

    @Override
    public boolean canVisit(final MappingElement mapping) {
        return mapping.getMainNode().has(AstConstant.TYPE_CONST) && mapping.getSelf() != null && !mapping.getSelf().eClass().getEAllReferences().isEmpty();
    }

    @Override
    public void mappingVisit(final MappingElement mapping) {
        Stream.concat(this.nodes.stream(), this.arrayNodes.stream()).filter(key -> mapping.getMainNode().has(key)).forEach(key -> {
            this.logger.debug("add " + key + " to map for p = " + mapping.getSelf());
            this.mappingState.toMap().add(new MappingElement(mapping.getMainNode().get(key), mapping.getSelf()));
        });

        this.mappingState.toResolve().add(mapping);
    }

    @Override
    public void referenceVisit(final MappingElement mapping) {
        this.logger.debug("referenceVisit of object " + mapping.getSelf());
        if (SysmlPackage.eINSTANCE.getRelationship().isSuperTypeOf(mapping.getSelf().eClass())) {
            this.nodes.stream().filter(node -> mapping.getMainNode().has(node) && !mapping.getMainNode().get(node).isEmpty()).forEach(node -> {
                this.mapRelationship(mapping.getMainNode().get(node), node, mapping);
            });
            this.arrayNodes.stream().filter(node -> mapping.getMainNode().has(node) && !mapping.getMainNode().get(node).isEmpty()).forEach(node -> {
                this.mapRelationshipArray(mapping.getMainNode(), node, mapping);
            });
        } else if (SysmlPackage.eINSTANCE.getElement().isSuperTypeOf(mapping.getSelf().eClass())) {
            this.nodes.stream().filter(node -> mapping.getMainNode().has(node) && !mapping.getMainNode().get(node).isEmpty()).forEach(node -> {
                this.mapElement(mapping.getMainNode().get(node), node, mapping);
            });
            this.arrayNodes.stream().filter(node -> mapping.getMainNode().has(node) && !mapping.getMainNode().get(node).isEmpty()).forEach(node -> {
                this.mapElementArray(mapping.getMainNode(), node, mapping);
            });
        }

        this.mappingState.done().add(mapping);
    }

    private void mapRelationshipArray(final JsonNode node, final String jsonPath, final MappingElement mapping) {
        node.get(jsonPath).forEach(subElement -> {
            if (!subElement.isEmpty() || subElement.size() > 0) {
                this.mapRelationship(subElement, jsonPath, mapping);
            }
        });

    }

    private void mapRelationship(final JsonNode subElement, final String jsonPath, final MappingElement mapping) {

        EObject referencedObject = this.objectFinder.findObject(mapping, subElement);

        if (referencedObject != null) {
            this.logger.debug("Map Relationship object " + mapping.getSelf() + " " + jsonPath + "  " + " to object " + referencedObject);

            this.logger.debug("Map object " + mapping.getSelf() + " to target " + referencedObject);
            ((Relationship) mapping.getSelf()).getTarget().add((Element) referencedObject);

            this.logger.debug("Map object " + mapping.getSelf() + " to source " + mapping.getParent());
            ((Relationship) mapping.getSelf()).getSource().add((Element) mapping.getParent());

            if (SysmlPackage.eINSTANCE.getRelationship().isSuperTypeOf(referencedObject.eClass())) {
                ((Relationship) referencedObject).setOwningRelationship((Relationship) mapping.getSelf());
            }
            if (SysmlPackage.eINSTANCE.getElement().isSuperTypeOf(referencedObject.eClass())) {
                ((Element) referencedObject).setOwningRelationship((Relationship) mapping.getSelf());
            }
        } else {
            this.logger.warn("Referenced  eObject not found " + subElement);
        }
    }

    private void mapElementArray(final JsonNode node, final String jsonPath, final MappingElement mapping) {
        node.get(jsonPath).forEach(subElement -> {
            if (!subElement.isEmpty() || subElement.size() > 0) {
                this.mapElement(subElement, jsonPath, mapping);
            }
        });
    }

    private void mapElement(final JsonNode subElement, final String jsonPath, final MappingElement mapping) {
        EObject referencedObject = this.objectFinder.findObject(mapping, subElement);
        if (referencedObject != null) {
            if (SysmlPackage.eINSTANCE.getRelationship().isSuperTypeOf(referencedObject.eClass())) {
                this.logger.debug("Map Element object " + mapping.getSelf() + " " + jsonPath + "  " + " to object " + referencedObject);
                ((Relationship) referencedObject).setOwningRelatedElement((Element) mapping.getSelf());
            }
        } else {
            this.logger.warn("Referenced  eObject not found " + subElement);
        }
    }
}
