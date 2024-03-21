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
        return this.kind;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setKind(RequirementConstraintKind newKind) {
        RequirementConstraintKind oldKind = this.kind;
        this.kind = newKind == null ? KIND_EDEFAULT : newKind;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.REQUIREMENT_CONSTRAINT_MEMBERSHIP__KIND, oldKind, this.kind));
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ConstraintUsage getOwnedConstraint() {
        ConstraintUsage ownedConstraint = this.basicGetOwnedConstraint();
        return ownedConstraint != null && ownedConstraint.eIsProxy() ? (ConstraintUsage)this.eResolveProxy((InternalEObject)ownedConstraint) : ownedConstraint;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    public ConstraintUsage basicGetOwnedConstraint() {
        return this.getOwnedRelatedElement().stream()
                .filter(ConstraintUsage.class::isInstance)
                .map(ConstraintUsage.class::cast)
                .findFirst()
                .orElse(null);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ConstraintUsage getReferencedConstraint() {
        ConstraintUsage referencedConstraint = this.basicGetReferencedConstraint();
        return referencedConstraint != null && referencedConstraint.eIsProxy() ? (ConstraintUsage)this.eResolveProxy((InternalEObject)referencedConstraint) : referencedConstraint;
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
                return this.getKind();
            case SysmlPackage.REQUIREMENT_CONSTRAINT_MEMBERSHIP__OWNED_CONSTRAINT:
                if (resolve) {
                    return this.getOwnedConstraint();
                }
                return this.basicGetOwnedConstraint();
            case SysmlPackage.REQUIREMENT_CONSTRAINT_MEMBERSHIP__REFERENCED_CONSTRAINT:
                if (resolve) {
                    return this.getReferencedConstraint();
                }
                return this.basicGetReferencedConstraint();
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
                this.setKind((RequirementConstraintKind)newValue);
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
                this.setKind(KIND_EDEFAULT);
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
                return this.kind != KIND_EDEFAULT;
            case SysmlPackage.REQUIREMENT_CONSTRAINT_MEMBERSHIP__OWNED_CONSTRAINT:
                return this.basicGetOwnedConstraint() != null;
            case SysmlPackage.REQUIREMENT_CONSTRAINT_MEMBERSHIP__REFERENCED_CONSTRAINT:
                return this.basicGetReferencedConstraint() != null;
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
        if (this.eIsProxy()) {
            return super.toString();
        }

        StringBuilder result = new StringBuilder(super.toString());
        result.append(" (kind: ");
        result.append(this.kind);
        result.append(')');
        return result.toString();
    }

} //RequirementConstraintMembershipImpl
