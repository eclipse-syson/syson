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
package org.eclipse.syson.sysmlcustomnodes.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.syson.sysmlcustomnodes.SysMLCustomnodesFactory;
import org.eclipse.syson.sysmlcustomnodes.SysMLCustomnodesPackage;
import org.eclipse.syson.sysmlcustomnodes.SysMLNoteNodeStyleDescription;
import org.eclipse.syson.sysmlcustomnodes.SysMLPackageNodeStyleDescription;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Factory</b>. <!-- end-user-doc -->
 *
 * @generated
 */
public class SysMLCustomnodesFactoryImpl extends EFactoryImpl implements SysMLCustomnodesFactory {
    /**
     * Creates the default factory implementation. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public static SysMLCustomnodesFactory init() {
        try {
            SysMLCustomnodesFactory theSysMLCustomnodesFactory = (SysMLCustomnodesFactory) EPackage.Registry.INSTANCE.getEFactory(SysMLCustomnodesPackage.eNS_URI);
            if (theSysMLCustomnodesFactory != null) {
                return theSysMLCustomnodesFactory;
            }
        } catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new SysMLCustomnodesFactoryImpl();
    }

    /**
     * Creates an instance of the factory. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public SysMLCustomnodesFactoryImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EObject create(EClass eClass) {
        switch (eClass.getClassifierID()) {
            case SysMLCustomnodesPackage.SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION:
                return this.createSysMLPackageNodeStyleDescription();
            case SysMLCustomnodesPackage.SYS_ML_NOTE_NODE_STYLE_DESCRIPTION:
                return this.createSysMLNoteNodeStyleDescription();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public SysMLPackageNodeStyleDescription createSysMLPackageNodeStyleDescription() {
        SysMLPackageNodeStyleDescriptionImpl sysMLPackageNodeStyleDescription = new SysMLPackageNodeStyleDescriptionImpl();
        return sysMLPackageNodeStyleDescription;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public SysMLNoteNodeStyleDescription createSysMLNoteNodeStyleDescription() {
        SysMLNoteNodeStyleDescriptionImpl sysMLNoteNodeStyleDescription = new SysMLNoteNodeStyleDescriptionImpl();
        return sysMLNoteNodeStyleDescription;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public SysMLCustomnodesPackage getSysMLCustomnodesPackage() {
        return (SysMLCustomnodesPackage) this.getEPackage();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @deprecated
     * @generated
     */
    @Deprecated
    public static SysMLCustomnodesPackage getPackage() {
        return SysMLCustomnodesPackage.eINSTANCE;
    }

} // SysMLCustomnodesFactoryImpl
