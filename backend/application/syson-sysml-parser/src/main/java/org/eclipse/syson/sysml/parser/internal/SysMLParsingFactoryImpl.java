/*******************************************************************************
 * Copyright (c) 2026 Obeo.
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
package org.eclipse.syson.sysml.parser.internal;


import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.omg.sysml.lang.sysml.SysMLPackage;
import org.omg.sysml.lang.sysml.Usage;
import org.omg.sysml.lang.sysml.impl.SysMLFactoryImpl;

/**
 * A SysML factory that applies the defaults implied by the textual syntax.
 *
 * <p>This factory is intended for parser-created objects only. The regular
 * SysML factory remains authoritative for programmatic creation and XMI
 * deserialization.</p>
 *
 * @author Arthur Daussy
 */
public class SysMLParsingFactoryImpl extends SysMLFactoryImpl {

    private static final List<EClass> NON_COMPOSITE_USAGE_TYPES = List.of(
            SysMLPackage.Literals.ATTRIBUTE_USAGE,
            SysMLPackage.Literals.BINDING_CONNECTOR_AS_USAGE,
            SysMLPackage.Literals.EVENT_OCCURRENCE_USAGE,
            SysMLPackage.Literals.EXHIBIT_STATE_USAGE,
            SysMLPackage.Literals.INCLUDE_USE_CASE_USAGE,
            SysMLPackage.Literals.PERFORM_ACTION_USAGE,
            SysMLPackage.Literals.REFERENCE_USAGE,
            SysMLPackage.Literals.SUCCESSION_AS_USAGE);

    @Override
    public EObject create(EClass eClass) {
        EObject result = super.create(eClass);
        if (result instanceof Usage usage && isCompositeByDefault(eClass)) {
            usage.setIsComposite(true);
        }
        return result;
    }

    private static boolean isCompositeByDefault(EClass eClass) {
        return NON_COMPOSITE_USAGE_TYPES.stream().noneMatch(type -> type.isSuperTypeOf(eClass));
    }
}
