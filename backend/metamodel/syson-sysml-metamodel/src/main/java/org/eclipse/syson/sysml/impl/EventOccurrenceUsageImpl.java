/*******************************************************************************
* Copyright (c) 2023, 2025 Obeo.
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
import org.eclipse.syson.sysml.EventOccurrenceUsage;
import org.eclipse.syson.sysml.OccurrenceUsage;
import org.eclipse.syson.sysml.ReferenceSubsetting;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Event Occurrence Usage</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.EventOccurrenceUsageImpl#getEventOccurrence <em>Event Occurrence</em>}</li>
 * </ul>
 *
 * @generated
 */
public class EventOccurrenceUsageImpl extends OccurrenceUsageImpl implements EventOccurrenceUsage {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected EventOccurrenceUsageImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getEventOccurrenceUsage();
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
     * @generated NOT
     */
    public OccurrenceUsage basicGetEventOccurrence() {
        ReferenceSubsetting referenceSubSetting = this.getOwnedReferenceSubsetting();
        if (referenceSubSetting != null && referenceSubSetting.getReferencedFeature() instanceof OccurrenceUsage occurenceUsage) {
            return occurenceUsage;
        }
        return this;
    }

    /**
     * <!-- begin-user-doc --> Always true for an EventOccurrenceUsage. <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public boolean isIsReference() {
        return true;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.EVENT_OCCURRENCE_USAGE__EVENT_OCCURRENCE:
                if (resolve) {
                    return this.getEventOccurrence();
                }
                return this.basicGetEventOccurrence();
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
            case SysmlPackage.EVENT_OCCURRENCE_USAGE__EVENT_OCCURRENCE:
                return this.basicGetEventOccurrence() != null;
        }
        return super.eIsSet(featureID);
    }

} // EventOccurrenceUsageImpl
