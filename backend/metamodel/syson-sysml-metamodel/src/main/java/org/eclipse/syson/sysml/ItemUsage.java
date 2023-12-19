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
package org.eclipse.syson.sysml;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Item Usage</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.ItemUsage#getItemDefinition <em>Item Definition</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getItemUsage()
 * @model
 * @generated
 */
public interface ItemUsage extends OccurrenceUsage {
    /**
     * Returns the value of the '<em><b>Item Definition</b></em>' reference list.
     * The list contents are of type {@link org.eclipse.syson.sysml.Structure}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Item Definition</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getItemUsage_ItemDefinition()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<Structure> getItemDefinition();

} // ItemUsage
