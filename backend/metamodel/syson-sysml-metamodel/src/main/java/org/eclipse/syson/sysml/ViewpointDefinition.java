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
 * A representation of the model object '<em><b>Viewpoint Definition</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.ViewpointDefinition#getViewpointStakeholder <em>Viewpoint Stakeholder</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getViewpointDefinition()
 * @model
 * @generated
 */
public interface ViewpointDefinition extends RequirementDefinition {
    /**
     * Returns the value of the '<em><b>Viewpoint Stakeholder</b></em>' reference list.
     * The list contents are of type {@link org.eclipse.syson.sysml.PartUsage}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Viewpoint Stakeholder</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getViewpointDefinition_ViewpointStakeholder()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<PartUsage> getViewpointStakeholder();

} // ViewpointDefinition
