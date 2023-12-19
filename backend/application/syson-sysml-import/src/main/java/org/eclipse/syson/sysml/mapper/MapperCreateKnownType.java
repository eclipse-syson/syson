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

import java.math.BigDecimal;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.impl.EClassImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.syson.sysml.AstConstant;
import org.eclipse.syson.sysml.SysmlPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Facilitates the creation of known SysML types from AST nodes during model transformation.
 *
 * @author gescande
 */
public class MapperCreateKnownType extends MapperVisitorInterface {

    private final Logger logger = LoggerFactory.getLogger(MapperCreateKnownType.class);

    public MapperCreateKnownType(final ObjectFinder objectFinder, final MappingState mappingState) {
        super(objectFinder, mappingState);
    }

    @Override
    public boolean canVisit(final MappingElement mapping) {
        return mapping.getMainNode().has(AstConstant.TYPE_CONST);
    }

    @Override
    public void mappingVisit(final MappingElement mapping) {
        if (mapping.getSelf() == null) {
            String type = mapping.getMainNode().findValue(AstConstant.TYPE_CONST).textValue();

            if (type.equals("MembershipReference")) {
                type = "Membership";
            }
            if (type.equals("SysMLFunction")) {
                type = "Function";
            }
            if ("LiteralNumber".equals(type)) {
                String literalValue = mapping.getMainNode().get(AstConstant.LITERAL).asText();
                if (literalValue != null) {
                    try {
                        BigDecimal bd = new BigDecimal(literalValue);
                        bd.intValueExact();
                        type = "LiteralInteger";
                    } catch (ArithmeticException e) {
                        type = "LiteralRational";
                    }
                }
            }
            this.logger.debug("mapping type = " + type);

            EClassifier classif = SysmlPackage.eINSTANCE.getEClassifier(type);
            EClassImpl eclassImpl = (EClassImpl) classif;

            this.logger.debug("mapping class = " + eclassImpl.getName());

            mapping.setSelf(EcoreUtil.create(eclassImpl));
            this.objectFinder.putElement(mapping);

            this.mappingState.done().add(mapping);
        } else {
            this.logger.debug("already mapped - self = " + mapping.getSelf());
        }
    }

    @Override
    public void referenceVisit(final MappingElement mapping) {
    }

}
