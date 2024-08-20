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
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.Behavior;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.StateDefinition;
import org.eclipse.syson.sysml.StateSubactionKind;
import org.eclipse.syson.sysml.StateSubactionMembership;
import org.eclipse.syson.sysml.StateUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>State Usage</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.StateUsageImpl#isIsParallel <em>Is Parallel</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.StateUsageImpl#getDoAction <em>Do Action</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.StateUsageImpl#getEntryAction <em>Entry Action</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.StateUsageImpl#getExitAction <em>Exit Action</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.StateUsageImpl#getStateDefinition <em>State Definition</em>}</li>
 * </ul>
 *
 * @generated
 */
public class StateUsageImpl extends ActionUsageImpl implements StateUsage {
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
    protected StateUsageImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getStateUsage();
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
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.STATE_USAGE__IS_PARALLEL, oldIsParallel, this.isParallel));
        }
    }

    /**
     * <!-- begin-user-doc --> The Behaviors that are the types of this StateUsage. Nominally, these would be
     * StateDefinitions, but kernel Behaviors are also allowed, to permit use of Behaviors from the Kernel Model
     * Libraries. <!-- end-user-doc -->
     *
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
     * <!-- begin-user-doc --> Check if this StateUsage is composite and has an owningType that is an StateDefinition or
     * StateUsage with the given value of isParallel, but is not an entryAction or exitAction. If so, then it represents
     * a StateAction that is a substate or exclusiveState (for isParallel = false) of another StateAction.
     *
     * <pre>
     * {@code body:
     *  isComposite and owningType <> null and (
     *      owningType.oclIsKindOf(StateDefinition) and owningType.oclAsType(StateDefinition).isParallel = isParallel
     *      or
     *      owningType.oclIsKindOf(StateUsage) and owningType.oclAsType(StateUsage).isParallel = isParallel
     *  )
     *  and not owningFeatureMembership.oclIsKindOf(StateSubactionMembership)}
     * </pre>
     *
     * <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public boolean isSubstateUsage(boolean isParallel) {
        Type owningType = this.getOwningType();
        if (this.isIsComposite() && owningType != null) {
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
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.STATE_USAGE__IS_PARALLEL:
                return this.isIsParallel();
            case SysmlPackage.STATE_USAGE__DO_ACTION:
                if (resolve) {
                    return this.getDoAction();
                }
                return this.basicGetDoAction();
            case SysmlPackage.STATE_USAGE__ENTRY_ACTION:
                if (resolve) {
                    return this.getEntryAction();
                }
                return this.basicGetEntryAction();
            case SysmlPackage.STATE_USAGE__EXIT_ACTION:
                if (resolve) {
                    return this.getExitAction();
                }
                return this.basicGetExitAction();
            case SysmlPackage.STATE_USAGE__STATE_DEFINITION:
                return this.getStateDefinition();
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
            case SysmlPackage.STATE_USAGE__IS_PARALLEL:
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
            case SysmlPackage.STATE_USAGE__IS_PARALLEL:
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
            case SysmlPackage.STATE_USAGE__IS_PARALLEL:
                return this.isParallel != IS_PARALLEL_EDEFAULT;
            case SysmlPackage.STATE_USAGE__DO_ACTION:
                return this.basicGetDoAction() != null;
            case SysmlPackage.STATE_USAGE__ENTRY_ACTION:
                return this.basicGetEntryAction() != null;
            case SysmlPackage.STATE_USAGE__EXIT_ACTION:
                return this.basicGetExitAction() != null;
            case SysmlPackage.STATE_USAGE__STATE_DEFINITION:
                return !this.getStateDefinition().isEmpty();
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
        switch (operationID) {
            case SysmlPackage.STATE_USAGE___IS_SUBSTATE_USAGE__BOOLEAN:
                return this.isSubstateUsage((Boolean) arguments.get(0));
        }
        return super.eInvoke(operationID, arguments);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String toString() {
        if (this.eIsProxy()) {
            return super.toString();
        }

        StringBuilder result = new StringBuilder(super.toString());
        result.append(" (isParallel: ");
        result.append(this.isParallel);
        result.append(')');
        return result.toString();
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Behavior> getActionDefinition() {
        EList<Behavior> actionDefinitions = new BasicEList<>();
        EList<Behavior> stateDefinition = this.getStateDefinition();
        if (stateDefinition != null) {
            actionDefinitions.addAll(stateDefinition);
        }
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getActionUsage_ActionDefinition(), actionDefinitions.size(), actionDefinitions.toArray());
    }

} // StateUsageImpl
