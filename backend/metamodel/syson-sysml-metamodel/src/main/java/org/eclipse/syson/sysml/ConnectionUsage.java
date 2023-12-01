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
 * A representation of the model object '<em><b>Connection Usage</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.ConnectionUsage#getConnectionDefinition <em>Connection Definition</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getConnectionUsage()
 * @model
 * @generated
 */
public interface ConnectionUsage extends ConnectorAsUsage, PartUsage {
    /**
     * Returns the value of the '<em><b>Connection Definition</b></em>' reference list.
     * The list contents are of type {@link org.eclipse.syson.sysml.AssociationStructure}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Connection Definition</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getConnectionUsage_ConnectionDefinition()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<AssociationStructure> getConnectionDefinition();

} // ConnectionUsage
