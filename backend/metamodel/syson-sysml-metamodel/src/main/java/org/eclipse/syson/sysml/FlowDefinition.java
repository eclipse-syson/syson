/**
 * Copyright (c) 2023, 2025 Obeo.
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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Flow Definition</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.FlowDefinition#getFlowEnd <em>Flow End</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getFlowDefinition()
 * @model
 * @generated
 */
public interface FlowDefinition extends ActionDefinition, Interaction {
    /**
     * Returns the value of the '<em><b>Flow End</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Usage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Flow End</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getFlowDefinition_FlowEnd()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false" annotation="redefines"
     * @generated
     */
    EList<Usage> getFlowEnd();

} // FlowDefinition
