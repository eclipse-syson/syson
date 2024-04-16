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
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.Relationship;
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
        EObject self = mapping.getSelf();
        JsonNode mainNode = mapping.getMainNode();
        this.logger.debug("referenceVisit of object " + self);
        if (self instanceof Relationship) {
            this.nodes.stream().filter(node -> mainNode.has(node) && !mainNode.get(node).isEmpty()).forEach(node -> {
                this.mapRelationship(mapping.getMainNode().get(node), node, mapping);
            });
            this.arrayNodes.stream().filter(node -> mainNode.has(node) && !mainNode.get(node).isEmpty()).forEach(node -> {
                this.mapRelationshipArray(mainNode, node, mapping);
            });
        } else if (self instanceof Element) {
            this.nodes.stream().filter(node -> mainNode.has(node) && !mainNode.get(node).isEmpty()).forEach(node -> {
                this.mapElement(mainNode.get(node), node, mapping);
            });
            this.arrayNodes.stream().filter(node -> mainNode.has(node) && !mainNode.get(node).isEmpty()).forEach(node -> {
                this.mapElementArray(mainNode, node, mapping);
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

        if (referencedObject instanceof Element target && mapping.getSelf() instanceof Relationship relationship) {
            this.logger.debug("Map Relationship object " + relationship + " " + jsonPath + "  " + " to object " + target);

            this.logger.debug("Map object " + mapping.getSelf() + " to target " + target);
            relationship.getTarget().add(target);

            this.logger.debug("Map object " + mapping.getSelf() + " to source " + mapping.getParent());
            relationship.getSource().add((Element) mapping.getParent());

            if (target instanceof Membership membership) {
                membership.setOwningRelatedElement(relationship);
            } else {
                target.setOwningRelationship(relationship);
            }
        } else {
            this.logger.warn("Referenced seObject not found " + subElement);
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
        if (referencedObject instanceof Relationship target) {
            this.logger.debug("Map Element object " + mapping.getSelf() + " " + jsonPath + "  " + " to object " + target);
            target.setOwningRelatedElement((Element) mapping.getSelf());
        } else {
            this.logger.warn("Referenced eObject not found " + subElement);
        }
    }
}
