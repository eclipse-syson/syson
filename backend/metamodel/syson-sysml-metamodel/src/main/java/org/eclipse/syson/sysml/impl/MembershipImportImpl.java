/*******************************************************************************
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
*******************************************************************************/
package org.eclipse.syson.sysml.impl;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectEList;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.MembershipImport;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.helper.MembershipComputer;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Membership Import</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.MembershipImportImpl#getImportedMembership <em>Imported Membership</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MembershipImportImpl extends ImportImpl implements MembershipImport {
    /**
     * The cached value of the '{@link #getImportedMembership() <em>Imported Membership</em>}' reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getImportedMembership()
     * @generated
     * @ordered
     */
    protected Membership importedMembership;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected MembershipImportImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getMembershipImport();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Membership getImportedMembership() {
        if (this.importedMembership != null && this.importedMembership.eIsProxy()) {
            InternalEObject oldImportedMembership = (InternalEObject) this.importedMembership;
            this.importedMembership = (Membership) this.eResolveProxy(oldImportedMembership);
            if (this.importedMembership != oldImportedMembership) {
                if (this.eNotificationRequired()) {
                    this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.MEMBERSHIP_IMPORT__IMPORTED_MEMBERSHIP, oldImportedMembership, this.importedMembership));
                }
            }
        }
        return this.importedMembership;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Membership basicGetImportedMembership() {
        return this.importedMembership;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setImportedMembership(Membership newImportedMembership) {
        Membership oldImportedMembership = this.importedMembership;
        this.importedMembership = newImportedMembership;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.MEMBERSHIP_IMPORT__IMPORTED_MEMBERSHIP, oldImportedMembership, this.importedMembership));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.MEMBERSHIP_IMPORT__IMPORTED_MEMBERSHIP:
                if (resolve) {
                    return this.getImportedMembership();
                }
                return this.basicGetImportedMembership();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case SysmlPackage.MEMBERSHIP_IMPORT__IMPORTED_MEMBERSHIP:
                this.setImportedMembership((Membership) newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case SysmlPackage.MEMBERSHIP_IMPORT__IMPORTED_MEMBERSHIP:
                this.setImportedMembership((Membership) null);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case SysmlPackage.MEMBERSHIP_IMPORT__IMPORTED_MEMBERSHIP:
                return this.importedMembership != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * @generated NOT
     */
    @Override
    public EList<Membership> importedMemberships(EList<Namespace> excluded) {
        return new MembershipComputer(this, excluded).importedMemberships();
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Element> getTarget() {
        EList<Element> targets = new EObjectEList.Unsettable<>(Element.class, this, SysmlPackage.MEMBERSHIP_IMPORT__TARGET) {
            @Override
            public boolean addAll(Collection<? extends Element> collection) {
                if (collection != null) {
                    Iterator<? extends Element> iterator = collection.iterator();
                    if (iterator.hasNext()) {
                        Element next = iterator.next();
                        if (next instanceof Membership membership) {
                            MembershipImportImpl.this.setImportedMembership(membership);
                            return true;
                        }
                    }
                }
                return false;
            }

            @Override
            protected void dispatchNotification(Notification notification) {
            }
        };
        Membership importedMembership = this.getImportedMembership();
        if (importedMembership != null) {
            targets.add(importedMembership);
        }
        return targets;
    }

} // MembershipImportImpl
