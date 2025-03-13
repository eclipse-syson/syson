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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.syson.sysml.Classifier;
import org.eclipse.syson.sysml.OccurrenceDefinition;
import org.eclipse.syson.sysml.OccurrenceUsage;
import org.eclipse.syson.sysml.PortionKind;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Occurrence Usage</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.OccurrenceUsageImpl#isIsIndividual <em>Is Individual</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.OccurrenceUsageImpl#getPortionKind <em>Portion Kind</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.OccurrenceUsageImpl#getIndividualDefinition <em>Individual
 * Definition</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.OccurrenceUsageImpl#getOccurrenceDefinition <em>Occurrence
 * Definition</em>}</li>
 * </ul>
 *
 * @generated
 */
public class OccurrenceUsageImpl extends UsageImpl implements OccurrenceUsage {
    /**
     * The default value of the '{@link #isIsIndividual() <em>Is Individual</em>}' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #isIsIndividual()
     * @generated
     * @ordered
     */
    protected static final boolean IS_INDIVIDUAL_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsIndividual() <em>Is Individual</em>}' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #isIsIndividual()
     * @generated
     * @ordered
     */
    protected boolean isIndividual = IS_INDIVIDUAL_EDEFAULT;

    /**
     * The default value of the '{@link #getPortionKind() <em>Portion Kind</em>}' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getPortionKind()
     * @generated NOT
     * @ordered
     */
    protected static final PortionKind PORTION_KIND_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getPortionKind() <em>Portion Kind</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getPortionKind()
     * @generated
     * @ordered
     */
    protected PortionKind portionKind = PORTION_KIND_EDEFAULT;

    /**
     * This is true if the Portion Kind attribute has been set. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    protected boolean portionKindESet;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected OccurrenceUsageImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getOccurrenceUsage();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public OccurrenceDefinition getIndividualDefinition() {
        OccurrenceDefinition individualDefinition = this.basicGetIndividualDefinition();
        return individualDefinition != null && individualDefinition.eIsProxy() ? (OccurrenceDefinition) this.eResolveProxy((InternalEObject) individualDefinition) : individualDefinition;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public OccurrenceDefinition basicGetIndividualDefinition() {
        return this.getOccurrenceDefinition().stream()
                .filter(OccurrenceDefinition.class::isInstance)
                .map(OccurrenceDefinition.class::cast)
                .filter(OccurrenceDefinition::isIsIndividual)
                .findFirst()
                .orElse(null);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public boolean isIsIndividual() {
        return this.isIndividual;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setIsIndividual(boolean newIsIndividual) {
        boolean oldIsIndividual = this.isIndividual;
        this.isIndividual = newIsIndividual;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.OCCURRENCE_USAGE__IS_INDIVIDUAL, oldIsIndividual, this.isIndividual));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<org.eclipse.syson.sysml.Class> getOccurrenceDefinition() {
        List<org.eclipse.syson.sysml.Class> occurrenceDefinitions = new ArrayList<>();
        this.getType().stream()
                .filter(org.eclipse.syson.sysml.Class.class::isInstance)
                .map(org.eclipse.syson.sysml.Class.class::cast)
                .forEach(occurrenceDefinitions::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getOccurrenceUsage_OccurrenceDefinition(), occurrenceDefinitions.size(), occurrenceDefinitions.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public PortionKind getPortionKind() {
        return this.portionKind;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setPortionKind(PortionKind newPortionKind) {
        PortionKind oldPortionKind = this.portionKind;
        this.portionKind = newPortionKind == null ? PORTION_KIND_EDEFAULT : newPortionKind;
        boolean oldPortionKindESet = this.portionKindESet;
        this.portionKindESet = true;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.OCCURRENCE_USAGE__PORTION_KIND, oldPortionKind, this.portionKind, !oldPortionKindESet));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void unsetPortionKind() {
        PortionKind oldPortionKind = this.portionKind;
        boolean oldPortionKindESet = this.portionKindESet;
        this.portionKind = PORTION_KIND_EDEFAULT;
        this.portionKindESet = false;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.UNSET, SysmlPackage.OCCURRENCE_USAGE__PORTION_KIND, oldPortionKind, PORTION_KIND_EDEFAULT, oldPortionKindESet));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public boolean isSetPortionKind() {
        return this.portionKindESet;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.OCCURRENCE_USAGE__IS_INDIVIDUAL:
                return this.isIsIndividual();
            case SysmlPackage.OCCURRENCE_USAGE__PORTION_KIND:
                return this.getPortionKind();
            case SysmlPackage.OCCURRENCE_USAGE__INDIVIDUAL_DEFINITION:
                if (resolve) {
                    return this.getIndividualDefinition();
                }
                return this.basicGetIndividualDefinition();
            case SysmlPackage.OCCURRENCE_USAGE__OCCURRENCE_DEFINITION:
                return this.getOccurrenceDefinition();
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
            case SysmlPackage.OCCURRENCE_USAGE__IS_INDIVIDUAL:
                this.setIsIndividual((Boolean) newValue);
                return;
            case SysmlPackage.OCCURRENCE_USAGE__PORTION_KIND:
                this.setPortionKind((PortionKind) newValue);
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
            case SysmlPackage.OCCURRENCE_USAGE__IS_INDIVIDUAL:
                this.setIsIndividual(IS_INDIVIDUAL_EDEFAULT);
                return;
            case SysmlPackage.OCCURRENCE_USAGE__PORTION_KIND:
                this.unsetPortionKind();
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
            case SysmlPackage.OCCURRENCE_USAGE__IS_INDIVIDUAL:
                return this.isIndividual != IS_INDIVIDUAL_EDEFAULT;
            case SysmlPackage.OCCURRENCE_USAGE__PORTION_KIND:
                return this.isSetPortionKind();
            case SysmlPackage.OCCURRENCE_USAGE__INDIVIDUAL_DEFINITION:
                return this.basicGetIndividualDefinition() != null;
            case SysmlPackage.OCCURRENCE_USAGE__OCCURRENCE_DEFINITION:
                return !this.getOccurrenceDefinition().isEmpty();
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
        if (this.eIsProxy()) {
            return super.toString();
        }

        StringBuilder result = new StringBuilder(super.toString());
        result.append(" (isIndividual: ");
        result.append(this.isIndividual);
        result.append(", portionKind: ");
        if (this.portionKindESet) {
            result.append(this.portionKind);
        } else {
            result.append("<unset>");
        }
        result.append(')');
        return result.toString();
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Classifier> getDefinition() {
        EList<Classifier> definitions = new BasicEList<>();
        EList<org.eclipse.syson.sysml.Class> occurrenceDefinition = this.getOccurrenceDefinition();
        if (occurrenceDefinition != null) {
            definitions.addAll(occurrenceDefinition);
        }
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getUsage_Definition(), definitions.size(), definitions.toArray());
    }

} // OccurrenceUsageImpl
