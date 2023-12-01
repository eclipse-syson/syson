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
package org.eclipse.syson.sysml.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.RequirementConstraintKind;
import org.eclipse.syson.sysml.RequirementConstraintMembership;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Requirement Constraint Membership</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.impl.RequirementConstraintMembershipImpl#getKind <em>Kind</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.RequirementConstraintMembershipImpl#getOwnedConstraint <em>Owned Constraint</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.RequirementConstraintMembershipImpl#getReferencedConstraint <em>Referenced Constraint</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RequirementConstraintMembershipImpl extends FeatureMembershipImpl implements RequirementConstraintMembership {
    /**
     * The default value of the '{@link #getKind() <em>Kind</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getKind()
     * @generated
     * @ordered
     */
    protected static final RequirementConstraintKind KIND_EDEFAULT = RequirementConstraintKind.ASSUMPTION;

    /**
     * The cached value of the '{@link #getKind() <em>Kind</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getKind()
     * @generated
     * @ordered
     */
    protected RequirementConstraintKind kind = KIND_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected RequirementConstraintMembershipImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getRequirementConstraintMembership();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public RequirementConstraintKind getKind() {
        return kind;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setKind(RequirementConstraintKind newKind) {
        RequirementConstraintKind oldKind = kind;
        kind = newKind == null ? KIND_EDEFAULT : newKind;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.REQUIREMENT_CONSTRAINT_MEMBERSHIP__KIND, oldKind, kind));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ConstraintUsage getOwnedConstraint() {
        ConstraintUsage ownedConstraint = basicGetOwnedConstraint();
        return ownedConstraint != null && ownedConstraint.eIsProxy() ? (ConstraintUsage)eResolveProxy((InternalEObject)ownedConstraint) : ownedConstraint;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ConstraintUsage basicGetOwnedConstraint() {
        // TODO: implement this method to return the 'Owned Constraint' reference
        // -> do not perform proxy resolution
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ConstraintUsage getReferencedConstraint() {
        ConstraintUsage referencedConstraint = basicGetReferencedConstraint();
        return referencedConstraint != null && referencedConstraint.eIsProxy() ? (ConstraintUsage)eResolveProxy((InternalEObject)referencedConstraint) : referencedConstraint;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ConstraintUsage basicGetReferencedConstraint() {
        // TODO: implement this method to return the 'Referenced Constraint' reference
        // -> do not perform proxy resolution
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.REQUIREMENT_CONSTRAINT_MEMBERSHIP__KIND:
                return getKind();
            case SysmlPackage.REQUIREMENT_CONSTRAINT_MEMBERSHIP__OWNED_CONSTRAINT:
                if (resolve) return getOwnedConstraint();
                return basicGetOwnedConstraint();
            case SysmlPackage.REQUIREMENT_CONSTRAINT_MEMBERSHIP__REFERENCED_CONSTRAINT:
                if (resolve) return getReferencedConstraint();
                return basicGetReferencedConstraint();
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
            case SysmlPackage.REQUIREMENT_CONSTRAINT_MEMBERSHIP__KIND:
                setKind((RequirementConstraintKind)newValue);
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
            case SysmlPackage.REQUIREMENT_CONSTRAINT_MEMBERSHIP__KIND:
                setKind(KIND_EDEFAULT);
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
            case SysmlPackage.REQUIREMENT_CONSTRAINT_MEMBERSHIP__KIND:
                return kind != KIND_EDEFAULT;
            case SysmlPackage.REQUIREMENT_CONSTRAINT_MEMBERSHIP__OWNED_CONSTRAINT:
                return basicGetOwnedConstraint() != null;
            case SysmlPackage.REQUIREMENT_CONSTRAINT_MEMBERSHIP__REFERENCED_CONSTRAINT:
                return basicGetReferencedConstraint() != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuilder result = new StringBuilder(super.toString());
        result.append(" (kind: ");
        result.append(kind);
        result.append(')');
        return result.toString();
    }

} //RequirementConstraintMembershipImpl
