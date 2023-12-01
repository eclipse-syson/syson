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
package org.eclipse.syson.sysml;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Package</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.Package#getFilterCondition <em>Filter Condition</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getPackage()
 * @model
 * @generated
 */
public interface Package extends Namespace {
    /**
     * Returns the value of the '<em><b>Filter Condition</b></em>' reference list.
     * The list contents are of type {@link org.eclipse.syson.sysml.Expression}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Filter Condition</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getPackage_FilterCondition()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<Expression> getFilterCondition();

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model required="true" ordered="false" elementRequired="true" elementOrdered="false"
     * @generated
     */
    boolean includeAsMember(Element element);

} // Package
