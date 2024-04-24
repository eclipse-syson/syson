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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.Behavior;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.StateDefinition;
import org.eclipse.syson.sysml.StateSubactionMembership;
import org.eclipse.syson.sysml.StateUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.Usage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>State Usage</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.impl.StateUsageImpl#isIsParallel <em>Is Parallel</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.StateUsageImpl#getDoAction <em>Do Action</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.StateUsageImpl#getEntryAction <em>Entry Action</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.StateUsageImpl#getExitAction <em>Exit Action</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.StateUsageImpl#getStateDefinition <em>State Definition</em>}</li>
 * </ul>
 *
 * @generated
 */
public class StateUsageImpl extends ActionUsageImpl implements StateUsage {
    /**
     * The default value of the '{@link #isIsParallel() <em>Is Parallel</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsParallel()
     * @generated
     * @ordered
     */
    protected static final boolean IS_PARALLEL_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsParallel() <em>Is Parallel</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsParallel()
     * @generated
     * @ordered
     */
    protected boolean isParallel = IS_PARALLEL_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected StateUsageImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getStateUsage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ActionUsage getDoAction() {
        ActionUsage doAction = basicGetDoAction();
        return doAction != null && doAction.eIsProxy() ? (ActionUsage)eResolveProxy((InternalEObject)doAction) : doAction;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ActionUsage basicGetDoAction() {
        // TODO: implement this method to return the 'Do Action' reference
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
    public ActionUsage getEntryAction() {
        ActionUsage entryAction = basicGetEntryAction();
        return entryAction != null && entryAction.eIsProxy() ? (ActionUsage)eResolveProxy((InternalEObject)entryAction) : entryAction;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ActionUsage basicGetEntryAction() {
        // TODO: implement this method to return the 'Entry Action' reference
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
    public ActionUsage getExitAction() {
        ActionUsage exitAction = basicGetExitAction();
        return exitAction != null && exitAction.eIsProxy() ? (ActionUsage)eResolveProxy((InternalEObject)exitAction) : exitAction;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ActionUsage basicGetExitAction() {
        // TODO: implement this method to return the 'Exit Action' reference
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
    public boolean isIsParallel() {
        return isParallel;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setIsParallel(boolean newIsParallel) {
        boolean oldIsParallel = isParallel;
        isParallel = newIsParallel;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.STATE_USAGE__IS_PARALLEL, oldIsParallel, isParallel));
    }

    /**
     * <!-- begin-user-doc -->
     * The Behaviors that are the types of this StateUsage. Nominally, these would be StateDefinitions, 
     * but kernel Behaviors are also allowed, to permit use of Behaviors from the Kernel Model Libraries.
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<Behavior> getStateDefinition() {
        List<Behavior> data = new ArrayList<>();
        this.getOwnedTyping().stream()
            .map(FeatureTyping::getRelatedElement)
            .filter(Behavior.class::isInstance)
            .map(Behavior.class::cast)
            .forEach(data::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getStateUsage_StateDefinition(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * Check if this StateUsage is composite and has an owningType that is an StateDefinition or StateUsage 
     * with the given value of isParallel, but is not an entryAction or exitAction. If so, then it represents 
     * a StateAction that is a substate or exclusiveState (for isParallel = false) of another StateAction.
     * <pre>
     * {@code body: 
     *  owningType <> null and ( 
     *      owningType.oclIsKindOf(StateDefinition) and owningType.oclAsType(StateDefinition).isParallel = isParallel 
     *      or 
     *      owningType.oclIsKindOf(StateUsage) and owningType.oclAsType(StateUsage).isParallel = isParallel
     *  )
     *  and not owningFeatureMembership.oclIsKindOf(StateSubactionMembership)}
     * </pre>
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public boolean isSubstateUsage(boolean isParallel) {
        Type owningType = getOwningType();
        if (owningType != null) {
            boolean inStateSubactionMembership = this.getOwningFeatureMembership() instanceof StateSubactionMembership;
            if (!inStateSubactionMembership) {
                boolean isSubstateUsageOfSD = owningType instanceof StateDefinition sd && sd.isIsParallel() == isParallel;
                boolean isSubstateUsageOfSU = owningType instanceof StateUsage sd && sd.isIsParallel() == isParallel;
                return isSubstateUsageOfSD || isSubstateUsageOfSU;
            }
        }
        return false;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.STATE_USAGE__IS_PARALLEL:
                return isIsParallel();
            case SysmlPackage.STATE_USAGE__DO_ACTION:
                if (resolve) return getDoAction();
                return basicGetDoAction();
            case SysmlPackage.STATE_USAGE__ENTRY_ACTION:
                if (resolve) return getEntryAction();
                return basicGetEntryAction();
            case SysmlPackage.STATE_USAGE__EXIT_ACTION:
                if (resolve) return getExitAction();
                return basicGetExitAction();
            case SysmlPackage.STATE_USAGE__STATE_DEFINITION:
                return getStateDefinition();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case SysmlPackage.STATE_USAGE__IS_PARALLEL:
                setIsParallel((Boolean)newValue);
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
            case SysmlPackage.STATE_USAGE__IS_PARALLEL:
                setIsParallel(IS_PARALLEL_EDEFAULT);
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
            case SysmlPackage.STATE_USAGE__IS_PARALLEL:
                return isParallel != IS_PARALLEL_EDEFAULT;
            case SysmlPackage.STATE_USAGE__DO_ACTION:
                return basicGetDoAction() != null;
            case SysmlPackage.STATE_USAGE__ENTRY_ACTION:
                return basicGetEntryAction() != null;
            case SysmlPackage.STATE_USAGE__EXIT_ACTION:
                return basicGetExitAction() != null;
            case SysmlPackage.STATE_USAGE__STATE_DEFINITION:
                return !getStateDefinition().isEmpty();
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
            case SysmlPackage.STATE_USAGE___IS_SUBSTATE_USAGE__BOOLEAN:
                return isSubstateUsage((Boolean)arguments.get(0));
        }
        return super.eInvoke(operationID, arguments);
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
        result.append(" (isParallel: ");
        result.append(isParallel);
        result.append(')');
        return result.toString();
    }

} //StateUsageImpl
