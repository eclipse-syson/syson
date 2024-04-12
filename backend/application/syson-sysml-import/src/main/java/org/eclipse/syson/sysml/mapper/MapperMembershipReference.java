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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.syson.sysml.AstConstant;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.LogBook;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.finder.ObjectFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements mapping logic specific to reference Membership in SysML models from AST node MembershipReference.
 *
 * @author wldblm
 */
public class MapperMembershipReference extends MapperVisitorInterface {

    private final Logger logger = LoggerFactory.getLogger(MapperMembershipReference.class);

    public MapperMembershipReference(final ObjectFinder objectFinder, final MappingState mappingState) {
        super(objectFinder, mappingState);
    }

    @Override
    public boolean canVisit(final MappingElement mapping) {
        return mapping.getSelf() instanceof Membership
                && mapping.getMainNode().findValue(AstConstant.TYPE_CONST).textValue().equals("MembershipReference");
    }

    @Override
    public void mappingVisit(final MappingElement mapping) {
        this.mappingState.toResolve().add(mapping);
    }

    @Override
    public void referenceVisit(final MappingElement mapping) {
        EObject referencedObject = this.objectFinder.findObject(mapping, mapping.getMainNode(), SysmlPackage.eINSTANCE.getElement());

        Membership membership = (Membership) mapping.getSelf();

        if (referencedObject instanceof Element target) {
            this.logger.debug("Add Membership target between " + membership + " to " + target);
            membership.setMemberElement(target);
        } else {
            this.logger.warn("Reference Membership not found " + mapping.getMainNode());
            LogBook.addEvent("4", membership);
        }
    }
}
