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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.syson.sysml.AcceptActionUsage;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.Succession;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.TransitionUsage;
import org.eclipse.syson.sysml.Usage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Transition Usage</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.impl.TransitionUsageImpl#getEffectAction <em>Effect Action</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.TransitionUsageImpl#getGuardExpression <em>Guard Expression</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.TransitionUsageImpl#getSource <em>Source</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.TransitionUsageImpl#getSuccession <em>Succession</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.TransitionUsageImpl#getTarget <em>Target</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.TransitionUsageImpl#getTriggerAction <em>Trigger Action</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TransitionUsageImpl extends ActionUsageImpl implements TransitionUsage {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected TransitionUsageImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getTransitionUsage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<ActionUsage> getEffectAction() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getTransitionUsage_EffectAction(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<Expression> getGuardExpression() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getTransitionUsage_GuardExpression(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ActionUsage getSource() {
        ActionUsage source = basicGetSource();
        return source != null && source.eIsProxy() ? (ActionUsage)eResolveProxy((InternalEObject)source) : source;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ActionUsage basicGetSource() {
        // TODO: implement this method to return the 'Source' reference
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
    public Succession getSuccession() {
        Succession succession = basicGetSuccession();
        return succession != null && succession.eIsProxy() ? (Succession)eResolveProxy((InternalEObject)succession) : succession;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Succession basicGetSuccession() {
        // TODO: implement this method to return the 'Succession' reference
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
    public ActionUsage getTarget() {
        ActionUsage target = basicGetTarget();
        return target != null && target.eIsProxy() ? (ActionUsage)eResolveProxy((InternalEObject)target) : target;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ActionUsage basicGetTarget() {
        // TODO: implement this method to return the 'Target' reference
        // -> do not perform proxy resolution
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<AcceptActionUsage> getTriggerAction() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getTransitionUsage_TriggerAction(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ReferenceUsage triggerPayloadParameter() {
        // TODO: implement this method
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
            case SysmlPackage.TRANSITION_USAGE__EFFECT_ACTION:
                return getEffectAction();
            case SysmlPackage.TRANSITION_USAGE__GUARD_EXPRESSION:
                return getGuardExpression();
            case SysmlPackage.TRANSITION_USAGE__SOURCE:
                if (resolve) return getSource();
                return basicGetSource();
            case SysmlPackage.TRANSITION_USAGE__SUCCESSION:
                if (resolve) return getSuccession();
                return basicGetSuccession();
            case SysmlPackage.TRANSITION_USAGE__TARGET:
                if (resolve) return getTarget();
                return basicGetTarget();
            case SysmlPackage.TRANSITION_USAGE__TRIGGER_ACTION:
                return getTriggerAction();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case SysmlPackage.TRANSITION_USAGE__EFFECT_ACTION:
                return !getEffectAction().isEmpty();
            case SysmlPackage.TRANSITION_USAGE__GUARD_EXPRESSION:
                return !getGuardExpression().isEmpty();
            case SysmlPackage.TRANSITION_USAGE__SOURCE:
                return basicGetSource() != null;
            case SysmlPackage.TRANSITION_USAGE__SUCCESSION:
                return basicGetSuccession() != null;
            case SysmlPackage.TRANSITION_USAGE__TARGET:
                return basicGetTarget() != null;
            case SysmlPackage.TRANSITION_USAGE__TRIGGER_ACTION:
                return !getTriggerAction().isEmpty();
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
        switch (operationID) {
            case SysmlPackage.TRANSITION_USAGE___TRIGGER_PAYLOAD_PARAMETER:
                return triggerPayloadParameter();
        }
        return super.eInvoke(operationID, arguments);
    }

} //TransitionUsageImpl
