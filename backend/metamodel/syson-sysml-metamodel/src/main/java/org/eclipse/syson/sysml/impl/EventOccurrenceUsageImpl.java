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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.syson.sysml.EventOccurrenceUsage;
import org.eclipse.syson.sysml.OccurrenceUsage;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Event Occurrence Usage</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.impl.EventOccurrenceUsageImpl#getEventOccurrence <em>Event Occurrence</em>}</li>
 * </ul>
 *
 * @generated
 */
public class EventOccurrenceUsageImpl extends OccurrenceUsageImpl implements EventOccurrenceUsage {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EventOccurrenceUsageImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getEventOccurrenceUsage();
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
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.EVENT_OCCURRENCE_USAGE__EVENT_OCCURRENCE:
                if (resolve) return getEventOccurrence();
                return basicGetEventOccurrence();
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
            case SysmlPackage.EVENT_OCCURRENCE_USAGE__EVENT_OCCURRENCE:
                return basicGetEventOccurrence() != null;
        }
        return super.eIsSet(featureID);
    }

} //EventOccurrenceUsageImpl
