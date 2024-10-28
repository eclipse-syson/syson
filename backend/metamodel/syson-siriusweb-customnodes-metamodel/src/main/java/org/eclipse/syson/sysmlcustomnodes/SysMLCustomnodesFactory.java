/**
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
 */
package org.eclipse.syson.sysmlcustomnodes;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc --> The <b>Factory</b> for the model. It provides a create method for each non-abstract class of
 * the model. <!-- end-user-doc -->
 *
 * @see org.eclipse.syson.sysmlcustomnodes.SysMLCustomnodesPackage
 * @generated
 */
public interface SysMLCustomnodesFactory extends EFactory {
    /**
     * The singleton instance of the factory. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    SysMLCustomnodesFactory eINSTANCE = org.eclipse.syson.sysmlcustomnodes.impl.SysMLCustomnodesFactoryImpl.init();

    /**
     * Returns a new object of class '<em>Sys ML Package Node Style Description</em>'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return a new object of class '<em>Sys ML Package Node Style Description</em>'.
     * @generated
     */
    SysMLPackageNodeStyleDescription createSysMLPackageNodeStyleDescription();

    /**
     * Returns a new object of class '<em>Sys ML Note Node Style Description</em>'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return a new object of class '<em>Sys ML Note Node Style Description</em>'.
     * @generated
     */
    SysMLNoteNodeStyleDescription createSysMLNoteNodeStyleDescription();

    /**
     * Returns a new object of class '<em>Sys ML Imported Package Node Style Description</em>'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return a new object of class '<em>Sys ML Imported Package Node Style Description</em>'.
     * @generated
     */
    SysMLImportedPackageNodeStyleDescription createSysMLImportedPackageNodeStyleDescription();

    /**
     * Returns the package supported by this factory. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the package supported by this factory.
     * @generated
     */
    SysMLCustomnodesPackage getSysMLCustomnodesPackage();

} // SysMLCustomnodesFactory
