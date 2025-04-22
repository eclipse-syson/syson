/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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
package org.eclipse.syson.sysmlcustomnodes;

import org.eclipse.sirius.components.view.UserColor;
import org.eclipse.sirius.components.view.diagram.NodeStyleDescription;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Sys ML View Frame Node Style
 * Description</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysmlcustomnodes.SysMLViewFrameNodeStyleDescription#getBackground
 * <em>Background</em>}</li>
 * </ul>
 *
 * @model
 * @generated
 * @see org.eclipse.syson.sysmlcustomnodes.SysMLCustomnodesPackage#getSysMLViewFrameNodeStyleDescription()
 */
public interface SysMLViewFrameNodeStyleDescription extends NodeStyleDescription {

    /**
     * Returns the value of the '<em><b>Background</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Background</em>' reference.
     * @model
     * @generated
     * @see #setBackground(UserColor)
     * @see org.eclipse.syson.sysmlcustomnodes.SysMLCustomnodesPackage#getSysMLViewFrameNodeStyleDescription_Background()
     */
    UserColor getBackground();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysmlcustomnodes.SysMLViewFrameNodeStyleDescription#getBackground
     * <em>Background</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *         the new value of the '<em>Background</em>' reference.
     * @generated
     * @see #getBackground()
     */
    void setBackground(UserColor value);

} // SysMLViewFrameNodeStyleDescription
