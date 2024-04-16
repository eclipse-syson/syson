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

import org.eclipse.syson.sysml.AstConstant;
import org.eclipse.syson.sysml.EventOccurrenceUsage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements mapping logic specific to EventOccurenceUsage in SysML models from AST node.
 *
 * @author wldblm
 */
public class MapperEventOccurrenceUsage extends MapperVisitorInterface {

    private final Logger logger = LoggerFactory.getLogger(MapperEventOccurrenceUsage.class);

    public MapperEventOccurrenceUsage(final ObjectFinder objectFinder, final MappingState mappingState) {
        super(objectFinder, mappingState);
    }

    @Override
    public boolean canVisit(final MappingElement mapping) {
        return mapping.getSelf() instanceof EventOccurrenceUsage;
    }

    @Override
    public void mappingVisit(final MappingElement mapping) {
        this.logger.debug("Add EventOccurenceUsage to map for p  = " + mapping.getSelf());

        EventOccurrenceUsage eObject = (EventOccurrenceUsage) mapping.getSelf();

        if (mapping.getMainNode().has(AstConstant.TARGET_REF_CONST) && mapping.getMainNode().get(AstConstant.TARGET_REF_CONST).has(AstConstant.TEXT_CONST)) {
            eObject.setDeclaredName(AstConstant.asCleanedText(mapping.getMainNode().get(AstConstant.TARGET_REF_CONST).get(AstConstant.TEXT_CONST)));
        }
        this.mappingState.toResolve().add(mapping);
    }

    @Override
    public void referenceVisit(final MappingElement mapping) {
    }
}
