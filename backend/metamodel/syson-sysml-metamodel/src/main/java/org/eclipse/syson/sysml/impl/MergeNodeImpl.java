/*******************************************************************************
* Copyright (c) 2023, 2024 Obeo.
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
package org.eclipse.syson.sysml.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.syson.sysml.MergeNode;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Merge Node</b></em>'. <!-- end-user-doc -->
 *
 * @generated
 */
public class MergeNodeImpl extends ControlNodeImpl implements MergeNode {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected MergeNodeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getMergeNode();
    }

} // MergeNodeImpl
