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
package org.eclipse.syson.util;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.util.SysmlSwitch;

/**
 * Specific SysmlSwitch with new doSwitch method.
 * 
 * @param <T>
 *            The type returned by the switch.
 * @author arichard
 */
public class SysmlEClassSwitch<T> extends SysmlSwitch<T> {

    public T doSwitch(EClass eClass) {
        // Create a dummy instance of eClass to let switch cases access the switched EClass.
        return doSwitch(eClass, SysmlFactory.eINSTANCE.create(eClass));
    }
}
