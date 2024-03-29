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

import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.MembershipImport;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.TextualRepresentation;
import org.eclipse.syson.sysml.VisibilityKind;
import org.eclipse.syson.sysml.helper.PrettyPrinter;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Membership Import</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.impl.MembershipImportImpl#getImportedMembership <em>Imported Membership</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MembershipImportImpl extends ImportImpl implements MembershipImport {
    /**
     * The cached value of the '{@link #getImportedMembership() <em>Imported Membership</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getImportedMembership()
     * @generated
     * @ordered
     */
    protected Membership importedMembership;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected MembershipImportImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getMembershipImport();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Membership getImportedMembership() {
        if (importedMembership != null && importedMembership.eIsProxy()) {
            InternalEObject oldImportedMembership = (InternalEObject)importedMembership;
            importedMembership = (Membership)eResolveProxy(oldImportedMembership);
            if (importedMembership != oldImportedMembership) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.MEMBERSHIP_IMPORT__IMPORTED_MEMBERSHIP, oldImportedMembership, importedMembership));
            }
        }
        return importedMembership;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Membership basicGetImportedMembership() {
        return importedMembership;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setImportedMembership(Membership newImportedMembership) {
        Membership oldImportedMembership = importedMembership;
        importedMembership = newImportedMembership;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.MEMBERSHIP_IMPORT__IMPORTED_MEMBERSHIP, oldImportedMembership, importedMembership));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.MEMBERSHIP_IMPORT__IMPORTED_MEMBERSHIP:
                if (resolve) return getImportedMembership();
                return basicGetImportedMembership();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case SysmlPackage.MEMBERSHIP_IMPORT__IMPORTED_MEMBERSHIP:
                setImportedMembership((Membership)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case SysmlPackage.MEMBERSHIP_IMPORT__IMPORTED_MEMBERSHIP:
                setImportedMembership((Membership)null);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case SysmlPackage.MEMBERSHIP_IMPORT__IMPORTED_MEMBERSHIP:
                return importedMembership != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<TextualRepresentation> getTextualRepresentation() {
        TextualRepresentation repr = getOrCreateTextualRepresentation();
        StringBuilder builder = new StringBuilder();
        if( ! getVisibility().equals(VisibilityKind.PUBLIC) ){
        builder.append(getVisibility() + " ");
        }
        builder.append("import ");
        if( getImportedMembership() != null && getImportedMembership().getMemberElement() != null){
            builder.append(PrettyPrinter.prettyPrintName(getImportedMembership().getMemberElement().getQualifiedName()));
        } else {
            builder.append(PrettyPrinter.prettyPrintName(getDeclaredName()));
        }
        if( isIsRecursive() ){
            builder.append("::**");
        }
        builder.append(";");
        repr.setBody(builder.toString());
        List<TextualRepresentation> textualRepresentation = List.of(repr);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getElement_TextualRepresentation(), textualRepresentation.size(), textualRepresentation.toArray());
    }

} //MembershipImportImpl
