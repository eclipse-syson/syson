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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.EventOccurrenceUsage;
import org.eclipse.syson.sysml.IncludeUseCaseUsage;
import org.eclipse.syson.sysml.OccurrenceUsage;
import org.eclipse.syson.sysml.PerformActionUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.UseCaseUsage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Include Use Case Usage</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.IncludeUseCaseUsageImpl#getEventOccurrence <em>Event Occurrence</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.IncludeUseCaseUsageImpl#getPerformedAction <em>Performed Action</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.IncludeUseCaseUsageImpl#getUseCaseIncluded <em>Use Case Included</em>}</li>
 * </ul>
 *
 * @generated
 */
public class IncludeUseCaseUsageImpl extends UseCaseUsageImpl implements IncludeUseCaseUsage {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected IncludeUseCaseUsageImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getIncludeUseCaseUsage();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public OccurrenceUsage getEventOccurrence() {
        OccurrenceUsage eventOccurrence = this.basicGetEventOccurrence();
        return eventOccurrence != null && eventOccurrence.eIsProxy() ? (OccurrenceUsage) this.eResolveProxy((InternalEObject) eventOccurrence) : eventOccurrence;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public OccurrenceUsage basicGetEventOccurrence() {
        // TODO: implement this method to return the 'Event Occurrence' reference
        // -> do not perform proxy resolution
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ActionUsage getPerformedAction() {
        ActionUsage performedAction = this.basicGetPerformedAction();
        return performedAction != null && performedAction.eIsProxy() ? (ActionUsage) this.eResolveProxy((InternalEObject) performedAction) : performedAction;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public ActionUsage basicGetPerformedAction() {
        // TODO: implement this method to return the 'Performed Action' reference
        // -> do not perform proxy resolution
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public UseCaseUsage getUseCaseIncluded() {
        UseCaseUsage useCaseIncluded = this.basicGetUseCaseIncluded();
        return useCaseIncluded != null && useCaseIncluded.eIsProxy() ? (UseCaseUsage) this.eResolveProxy((InternalEObject) useCaseIncluded) : useCaseIncluded;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public UseCaseUsage basicGetUseCaseIncluded() {
        return this.getUseCaseIncluded();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.INCLUDE_USE_CASE_USAGE__EVENT_OCCURRENCE:
                if (resolve) {
                    return this.getEventOccurrence();
                }
                return this.basicGetEventOccurrence();
            case SysmlPackage.INCLUDE_USE_CASE_USAGE__PERFORMED_ACTION:
                if (resolve) {
                    return this.getPerformedAction();
                }
                return this.basicGetPerformedAction();
            case SysmlPackage.INCLUDE_USE_CASE_USAGE__USE_CASE_INCLUDED:
                if (resolve) {
                    return this.getUseCaseIncluded();
                }
                return this.basicGetUseCaseIncluded();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case SysmlPackage.INCLUDE_USE_CASE_USAGE__EVENT_OCCURRENCE:
                return this.basicGetEventOccurrence() != null;
            case SysmlPackage.INCLUDE_USE_CASE_USAGE__PERFORMED_ACTION:
                return this.basicGetPerformedAction() != null;
            case SysmlPackage.INCLUDE_USE_CASE_USAGE__USE_CASE_INCLUDED:
                return this.basicGetUseCaseIncluded() != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
        if (baseClass == EventOccurrenceUsage.class) {
            switch (derivedFeatureID) {
                case SysmlPackage.INCLUDE_USE_CASE_USAGE__EVENT_OCCURRENCE:
                    return SysmlPackage.EVENT_OCCURRENCE_USAGE__EVENT_OCCURRENCE;
                default:
                    return -1;
            }
        }
        if (baseClass == PerformActionUsage.class) {
            switch (derivedFeatureID) {
                case SysmlPackage.INCLUDE_USE_CASE_USAGE__PERFORMED_ACTION:
                    return SysmlPackage.PERFORM_ACTION_USAGE__PERFORMED_ACTION;
                default:
                    return -1;
            }
        }
        return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
        if (baseClass == EventOccurrenceUsage.class) {
            switch (baseFeatureID) {
                case SysmlPackage.EVENT_OCCURRENCE_USAGE__EVENT_OCCURRENCE:
                    return SysmlPackage.INCLUDE_USE_CASE_USAGE__EVENT_OCCURRENCE;
                default:
                    return -1;
            }
        }
        if (baseClass == PerformActionUsage.class) {
            switch (baseFeatureID) {
                case SysmlPackage.PERFORM_ACTION_USAGE__PERFORMED_ACTION:
                    return SysmlPackage.INCLUDE_USE_CASE_USAGE__PERFORMED_ACTION;
                default:
                    return -1;
            }
        }
        return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
    }

} // IncludeUseCaseUsageImpl
