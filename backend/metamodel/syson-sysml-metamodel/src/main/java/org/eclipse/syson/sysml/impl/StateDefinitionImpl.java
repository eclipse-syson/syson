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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.StateDefinition;
import org.eclipse.syson.sysml.StateSubactionKind;
import org.eclipse.syson.sysml.StateSubactionMembership;
import org.eclipse.syson.sysml.StateUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Usage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>State Definition</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.StateDefinitionImpl#isIsParallel <em>Is Parallel</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.StateDefinitionImpl#getDoAction <em>Do Action</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.StateDefinitionImpl#getEntryAction <em>Entry Action</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.StateDefinitionImpl#getExitAction <em>Exit Action</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.StateDefinitionImpl#getState <em>State</em>}</li>
 * </ul>
 *
 * @generated
 */
public class StateDefinitionImpl extends ActionDefinitionImpl implements StateDefinition {
    /**
     * The default value of the '{@link #isIsParallel() <em>Is Parallel</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #isIsParallel()
     * @generated
     * @ordered
     */
    protected static final boolean IS_PARALLEL_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsParallel() <em>Is Parallel</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #isIsParallel()
     * @generated
     * @ordered
     */
    protected boolean isParallel = IS_PARALLEL_EDEFAULT;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected StateDefinitionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getStateDefinition();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ActionUsage getDoAction() {
        ActionUsage doAction = this.basicGetDoAction();
        return doAction != null && doAction.eIsProxy() ? (ActionUsage) this.eResolveProxy((InternalEObject) doAction) : doAction;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public ActionUsage basicGetDoAction() {
        return this.getOwnedRelationship().stream()
                .filter(StateSubactionMembership.class::isInstance)
                .map(StateSubactionMembership.class::cast)
                .filter(ssm -> ssm.getKind().equals(StateSubactionKind.DO))
                .map(StateSubactionMembership::getAction)
                .filter(ActionUsage.class::isInstance)
                .map(ActionUsage.class::cast)
                .findFirst()
                .orElse(null);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ActionUsage getEntryAction() {
        ActionUsage entryAction = this.basicGetEntryAction();
        return entryAction != null && entryAction.eIsProxy() ? (ActionUsage) this.eResolveProxy((InternalEObject) entryAction) : entryAction;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public ActionUsage basicGetEntryAction() {
        return this.getOwnedRelationship().stream()
                .filter(StateSubactionMembership.class::isInstance)
                .map(StateSubactionMembership.class::cast)
                .filter(ssm -> ssm.getKind().equals(StateSubactionKind.ENTRY))
                .map(StateSubactionMembership::getAction)
                .filter(ActionUsage.class::isInstance)
                .map(ActionUsage.class::cast)
                .findFirst()
                .orElse(null);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ActionUsage getExitAction() {
        ActionUsage exitAction = this.basicGetExitAction();
        return exitAction != null && exitAction.eIsProxy() ? (ActionUsage) this.eResolveProxy((InternalEObject) exitAction) : exitAction;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public ActionUsage basicGetExitAction() {
        return this.getOwnedRelationship().stream()
                .filter(StateSubactionMembership.class::isInstance)
                .map(StateSubactionMembership.class::cast)
                .filter(ssm -> ssm.getKind().equals(StateSubactionKind.EXIT))
                .map(StateSubactionMembership::getAction)
                .filter(ActionUsage.class::isInstance)
                .map(ActionUsage.class::cast)
                .findFirst()
                .orElse(null);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public boolean isIsParallel() {
        return this.isParallel;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setIsParallel(boolean newIsParallel) {
        boolean oldIsParallel = this.isParallel;
        this.isParallel = newIsParallel;
        if (this.eNotificationRequired())
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.STATE_DEFINITION__IS_PARALLEL, oldIsParallel, this.isParallel));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<StateUsage> getState() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getStateDefinition_State(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.STATE_DEFINITION__IS_PARALLEL:
                return this.isIsParallel();
            case SysmlPackage.STATE_DEFINITION__DO_ACTION:
                if (resolve)
                    return this.getDoAction();
                return this.basicGetDoAction();
            case SysmlPackage.STATE_DEFINITION__ENTRY_ACTION:
                if (resolve)
                    return this.getEntryAction();
                return this.basicGetEntryAction();
            case SysmlPackage.STATE_DEFINITION__EXIT_ACTION:
                if (resolve)
                    return this.getExitAction();
                return this.basicGetExitAction();
            case SysmlPackage.STATE_DEFINITION__STATE:
                return this.getState();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case SysmlPackage.STATE_DEFINITION__IS_PARALLEL:
                this.setIsParallel((Boolean) newValue);
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
            case SysmlPackage.STATE_DEFINITION__IS_PARALLEL:
                this.setIsParallel(IS_PARALLEL_EDEFAULT);
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
            case SysmlPackage.STATE_DEFINITION__IS_PARALLEL:
                return this.isParallel != IS_PARALLEL_EDEFAULT;
            case SysmlPackage.STATE_DEFINITION__DO_ACTION:
                return this.basicGetDoAction() != null;
            case SysmlPackage.STATE_DEFINITION__ENTRY_ACTION:
                return this.basicGetEntryAction() != null;
            case SysmlPackage.STATE_DEFINITION__EXIT_ACTION:
                return this.basicGetExitAction() != null;
            case SysmlPackage.STATE_DEFINITION__STATE:
                return !this.getState().isEmpty();
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String toString() {
        if (this.eIsProxy())
            return super.toString();

        StringBuilder result = new StringBuilder(super.toString());
        result.append(" (isParallel: ");
        result.append(this.isParallel);
        result.append(')');
        return result.toString();
    }

} // StateDefinitionImpl
