/**
 * Copyright (c) 2023 Obeo.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.syson.sysml.impl;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.syson.sysml.ControlNode;
import org.eclipse.syson.sysml.Multiplicity;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Control Node</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public abstract class ControlNodeImpl extends ActionUsageImpl implements ControlNode {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ControlNodeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getControlNode();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public boolean multiplicityHasBounds(Multiplicity mult, int lower, int upper) {
        return false;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
        switch (operationID) {
            case SysmlPackage.CONTROL_NODE___MULTIPLICITY_HAS_BOUNDS__MULTIPLICITY_INT_INT:
                return multiplicityHasBounds((Multiplicity)arguments.get(0), (Integer)arguments.get(1), (Integer)arguments.get(2));
        }
        return super.eInvoke(operationID, arguments);
    }

} //ControlNodeImpl
