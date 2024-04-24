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

import java.util.Optional;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.Step;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.TransitionFeatureKind;
import org.eclipse.syson.sysml.TransitionFeatureMembership;

/**
 * <!-- begin-user-doc -->
 * A TransitionFeatureMembership is a FeatureMembership for a trigger, guard or effect of 
 * a TransitionUsage, whose transitionFeature is a AcceptActionUsage, Boolean-valued 
 * Expression or ActionUsage, depending on its kind.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.impl.TransitionFeatureMembershipImpl#getKind <em>Kind</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.TransitionFeatureMembershipImpl#getTransitionFeature <em>Transition Feature</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TransitionFeatureMembershipImpl extends FeatureMembershipImpl implements TransitionFeatureMembership {
    /**
     * The default value of the '{@link #getKind() <em>Kind</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getKind()
     * @generated
     * @ordered
     */
    protected static final TransitionFeatureKind KIND_EDEFAULT = TransitionFeatureKind.EFFECT;

    /**
     * The cached value of the '{@link #getKind() <em>Kind</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getKind()
     * @generated
     * @ordered
     */
    protected TransitionFeatureKind kind = KIND_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected TransitionFeatureMembershipImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getTransitionFeatureMembership();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public TransitionFeatureKind getKind() {
        return kind;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setKind(TransitionFeatureKind newKind) {
        TransitionFeatureKind oldKind = kind;
        kind = newKind == null ? KIND_EDEFAULT : newKind;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.TRANSITION_FEATURE_MEMBERSHIP__KIND, oldKind, kind));
    }

    /**
     * <!-- begin-user-doc -->
     * The Step that is the ownedMemberFeature of this TransitionFeatureMembership.
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Step getTransitionFeature() {
        Step transitionFeature = basicGetTransitionFeature();
        return transitionFeature != null && transitionFeature.eIsProxy() ? (Step)eResolveProxy((InternalEObject)transitionFeature) : transitionFeature;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    public Step basicGetTransitionFeature() {
        Feature memberFeature = this.getFeature();
        if (memberFeature instanceof Step step) {
            return step;
        }
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
            case SysmlPackage.TRANSITION_FEATURE_MEMBERSHIP__KIND:
                return getKind();
            case SysmlPackage.TRANSITION_FEATURE_MEMBERSHIP__TRANSITION_FEATURE:
                if (resolve) return getTransitionFeature();
                return basicGetTransitionFeature();
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
            case SysmlPackage.TRANSITION_FEATURE_MEMBERSHIP__KIND:
                setKind((TransitionFeatureKind)newValue);
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
            case SysmlPackage.TRANSITION_FEATURE_MEMBERSHIP__KIND:
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
            case SysmlPackage.TRANSITION_FEATURE_MEMBERSHIP__KIND:
                return kind != KIND_EDEFAULT;
            case SysmlPackage.TRANSITION_FEATURE_MEMBERSHIP__TRANSITION_FEATURE:
                return basicGetTransitionFeature() != null;
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

} //TransitionFeatureMembershipImpl
