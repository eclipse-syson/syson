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
import org.eclipse.syson.sysml.ExhibitStateUsage;
import org.eclipse.syson.sysml.OccurrenceUsage;
import org.eclipse.syson.sysml.PerformActionUsage;
import org.eclipse.syson.sysml.StateUsage;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Exhibit State Usage</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.impl.ExhibitStateUsageImpl#getEventOccurrence <em>Event Occurrence</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.ExhibitStateUsageImpl#getPerformedAction <em>Performed Action</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.ExhibitStateUsageImpl#getExhibitedState <em>Exhibited State</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ExhibitStateUsageImpl extends StateUsageImpl implements ExhibitStateUsage {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ExhibitStateUsageImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getExhibitStateUsage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public OccurrenceUsage getEventOccurrence() {
        OccurrenceUsage eventOccurrence = basicGetEventOccurrence();
        return eventOccurrence != null && eventOccurrence.eIsProxy() ? (OccurrenceUsage)eResolveProxy((InternalEObject)eventOccurrence) : eventOccurrence;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OccurrenceUsage basicGetEventOccurrence() {
        // TODO: implement this method to return the 'Event Occurrence' reference
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
    public ActionUsage getPerformedAction() {
        ActionUsage performedAction = basicGetPerformedAction();
        return performedAction != null && performedAction.eIsProxy() ? (ActionUsage)eResolveProxy((InternalEObject)performedAction) : performedAction;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ActionUsage basicGetPerformedAction() {
        // TODO: implement this method to return the 'Performed Action' reference
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
    public StateUsage getExhibitedState() {
        StateUsage exhibitedState = basicGetExhibitedState();
        return exhibitedState != null && exhibitedState.eIsProxy() ? (StateUsage)eResolveProxy((InternalEObject)exhibitedState) : exhibitedState;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public StateUsage basicGetExhibitedState() {
        // TODO: implement this method to return the 'Exhibited State' reference
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
            case SysmlPackage.EXHIBIT_STATE_USAGE__EVENT_OCCURRENCE:
                if (resolve) return getEventOccurrence();
                return basicGetEventOccurrence();
            case SysmlPackage.EXHIBIT_STATE_USAGE__PERFORMED_ACTION:
                if (resolve) return getPerformedAction();
                return basicGetPerformedAction();
            case SysmlPackage.EXHIBIT_STATE_USAGE__EXHIBITED_STATE:
                if (resolve) return getExhibitedState();
                return basicGetExhibitedState();
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
            case SysmlPackage.EXHIBIT_STATE_USAGE__EVENT_OCCURRENCE:
                return basicGetEventOccurrence() != null;
            case SysmlPackage.EXHIBIT_STATE_USAGE__PERFORMED_ACTION:
                return basicGetPerformedAction() != null;
            case SysmlPackage.EXHIBIT_STATE_USAGE__EXHIBITED_STATE:
                return basicGetExhibitedState() != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
        if (baseClass == EventOccurrenceUsage.class) {
            switch (derivedFeatureID) {
                case SysmlPackage.EXHIBIT_STATE_USAGE__EVENT_OCCURRENCE: return SysmlPackage.EVENT_OCCURRENCE_USAGE__EVENT_OCCURRENCE;
                default: return -1;
            }
        }
        if (baseClass == PerformActionUsage.class) {
            switch (derivedFeatureID) {
                case SysmlPackage.EXHIBIT_STATE_USAGE__PERFORMED_ACTION: return SysmlPackage.PERFORM_ACTION_USAGE__PERFORMED_ACTION;
                default: return -1;
            }
        }
        return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
        if (baseClass == EventOccurrenceUsage.class) {
            switch (baseFeatureID) {
                case SysmlPackage.EVENT_OCCURRENCE_USAGE__EVENT_OCCURRENCE: return SysmlPackage.EXHIBIT_STATE_USAGE__EVENT_OCCURRENCE;
                default: return -1;
            }
        }
        if (baseClass == PerformActionUsage.class) {
            switch (baseFeatureID) {
                case SysmlPackage.PERFORM_ACTION_USAGE__PERFORMED_ACTION: return SysmlPackage.EXHIBIT_STATE_USAGE__PERFORMED_ACTION;
                default: return -1;
            }
        }
        return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
    }

} //ExhibitStateUsageImpl
