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
import org.eclipse.syson.sysml.Dependency;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.SysmlPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements mapping logic specific to Dependency in SysML models from AST node.
 *
 * @author wldblm.
 */
public class MapperDependency extends MapperVisitorInterface {

    private final Logger logger = LoggerFactory.getLogger(MapperDependency.class);

    public MapperDependency(final ObjectFinder objectFinder, final MappingState mappingState) {
        super(objectFinder, mappingState);
    }

    @Override
    public boolean canVisit(final MappingElement mapping) {
        return mapping.getSelf() != null && SysmlPackage.eINSTANCE.getDependency().isSuperTypeOf(mapping.getSelf().eClass());
    }

    @Override
    public void mappingVisit(final MappingElement mapping) {
        this.logger.debug("Add Dependency to map for p  = " + mapping.getSelf());
        this.mappingState.toResolve().add(mapping);
    }

    @Override
    public void referenceVisit(final MappingElement mapping) {
        Dependency eObject = (Dependency) mapping.getSelf();

        mapping.getMainNode().get(AstConstant.SUPPLIER_CONST).forEach(subElement -> {

            EObject referencedObject = this.objectFinder.findObject(mapping, subElement, SysmlPackage.eINSTANCE.getElement());
            Element target = (Element) referencedObject;

            if (target != null) {
                this.logger.debug("Map Dependency object " + mapping.getSelf() + " supplier to object " + referencedObject);
                eObject.getSupplier().add(target);
            } else {
                this.logger.warn("Reference Dependency not found " + subElement);
            }
        });

        mapping.getMainNode().get(AstConstant.CLIENT_CONST).forEach(subElement -> {

            EObject referencedObject = this.objectFinder.findObject(mapping, subElement, SysmlPackage.eINSTANCE.getElement());
            Element target = (Element) referencedObject;

            if (target != null) {
                this.logger.debug("Map Dependency object " + mapping.getSelf() + " client to object " + referencedObject);
                eObject.getClient().add(target);
            } else {
                this.logger.warn("Reference Dependency not found " + subElement);
            }
        });

    }
}
