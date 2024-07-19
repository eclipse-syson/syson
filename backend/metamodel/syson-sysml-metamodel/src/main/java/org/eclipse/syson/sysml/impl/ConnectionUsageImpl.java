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
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.syson.sysml.Association;
import org.eclipse.syson.sysml.AssociationStructure;
import org.eclipse.syson.sysml.ConnectionUsage;
import org.eclipse.syson.sysml.ItemUsage;
import org.eclipse.syson.sysml.OccurrenceDefinition;
import org.eclipse.syson.sysml.OccurrenceUsage;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.PortionKind;
import org.eclipse.syson.sysml.Structure;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Connection Usage</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.ConnectionUsageImpl#isIsIndividual <em>Is Individual</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ConnectionUsageImpl#getPortionKind <em>Portion Kind</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ConnectionUsageImpl#getIndividualDefinition <em>Individual
 * Definition</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ConnectionUsageImpl#getOccurrenceDefinition <em>Occurrence
 * Definition</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ConnectionUsageImpl#getItemDefinition <em>Item Definition</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ConnectionUsageImpl#getPartDefinition <em>Part Definition</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ConnectionUsageImpl#getConnectionDefinition <em>Connection
 * Definition</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ConnectionUsageImpl extends ConnectorAsUsageImpl implements ConnectionUsage {
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
     * @generated
     * @ordered
     */
    protected static final PortionKind PORTION_KIND_EDEFAULT = PortionKind.SNAPSHOT;

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
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected ConnectionUsageImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getConnectionUsage();
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
     * @generated
     */
    public OccurrenceDefinition basicGetIndividualDefinition() {
        // TODO: implement this method to return the 'Individual Definition' reference
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
        if (this.eNotificationRequired())
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.CONNECTION_USAGE__IS_INDIVIDUAL, oldIsIndividual, this.isIndividual));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<org.eclipse.syson.sysml.Class> getOccurrenceDefinition() {
        List<org.eclipse.syson.sysml.Class> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getOccurrenceUsage_OccurrenceDefinition(), data.size(), data.toArray());
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
        if (this.eNotificationRequired())
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.CONNECTION_USAGE__PORTION_KIND, oldPortionKind, this.portionKind));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Structure> getItemDefinition() {
        List<Structure> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getItemUsage_ItemDefinition(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<PartDefinition> getPartDefinition() {
        List<PartDefinition> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getPartUsage_PartDefinition(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<AssociationStructure> getConnectionDefinition() {
        List<AssociationStructure> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getConnectionUsage_ConnectionDefinition(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.CONNECTION_USAGE__IS_INDIVIDUAL:
                return this.isIsIndividual();
            case SysmlPackage.CONNECTION_USAGE__PORTION_KIND:
                return this.getPortionKind();
            case SysmlPackage.CONNECTION_USAGE__INDIVIDUAL_DEFINITION:
                if (resolve)
                    return this.getIndividualDefinition();
                return this.basicGetIndividualDefinition();
            case SysmlPackage.CONNECTION_USAGE__OCCURRENCE_DEFINITION:
                return this.getOccurrenceDefinition();
            case SysmlPackage.CONNECTION_USAGE__ITEM_DEFINITION:
                return this.getItemDefinition();
            case SysmlPackage.CONNECTION_USAGE__PART_DEFINITION:
                return this.getPartDefinition();
            case SysmlPackage.CONNECTION_USAGE__CONNECTION_DEFINITION:
                return this.getConnectionDefinition();
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
            case SysmlPackage.CONNECTION_USAGE__IS_INDIVIDUAL:
                this.setIsIndividual((Boolean) newValue);
                return;
            case SysmlPackage.CONNECTION_USAGE__PORTION_KIND:
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
            case SysmlPackage.CONNECTION_USAGE__IS_INDIVIDUAL:
                this.setIsIndividual(IS_INDIVIDUAL_EDEFAULT);
                return;
            case SysmlPackage.CONNECTION_USAGE__PORTION_KIND:
                this.setPortionKind(PORTION_KIND_EDEFAULT);
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
            case SysmlPackage.CONNECTION_USAGE__IS_INDIVIDUAL:
                return this.isIndividual != IS_INDIVIDUAL_EDEFAULT;
            case SysmlPackage.CONNECTION_USAGE__PORTION_KIND:
                return this.portionKind != PORTION_KIND_EDEFAULT;
            case SysmlPackage.CONNECTION_USAGE__INDIVIDUAL_DEFINITION:
                return this.basicGetIndividualDefinition() != null;
            case SysmlPackage.CONNECTION_USAGE__OCCURRENCE_DEFINITION:
                return !this.getOccurrenceDefinition().isEmpty();
            case SysmlPackage.CONNECTION_USAGE__ITEM_DEFINITION:
                return !this.getItemDefinition().isEmpty();
            case SysmlPackage.CONNECTION_USAGE__PART_DEFINITION:
                return !this.getPartDefinition().isEmpty();
            case SysmlPackage.CONNECTION_USAGE__CONNECTION_DEFINITION:
                return !this.getConnectionDefinition().isEmpty();
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
        if (baseClass == OccurrenceUsage.class) {
            switch (derivedFeatureID) {
                case SysmlPackage.CONNECTION_USAGE__IS_INDIVIDUAL:
                    return SysmlPackage.OCCURRENCE_USAGE__IS_INDIVIDUAL;
                case SysmlPackage.CONNECTION_USAGE__PORTION_KIND:
                    return SysmlPackage.OCCURRENCE_USAGE__PORTION_KIND;
                case SysmlPackage.CONNECTION_USAGE__INDIVIDUAL_DEFINITION:
                    return SysmlPackage.OCCURRENCE_USAGE__INDIVIDUAL_DEFINITION;
                case SysmlPackage.CONNECTION_USAGE__OCCURRENCE_DEFINITION:
                    return SysmlPackage.OCCURRENCE_USAGE__OCCURRENCE_DEFINITION;
                default:
                    return -1;
            }
        }
        if (baseClass == ItemUsage.class) {
            switch (derivedFeatureID) {
                case SysmlPackage.CONNECTION_USAGE__ITEM_DEFINITION:
                    return SysmlPackage.ITEM_USAGE__ITEM_DEFINITION;
                default:
                    return -1;
            }
        }
        if (baseClass == PartUsage.class) {
            switch (derivedFeatureID) {
                case SysmlPackage.CONNECTION_USAGE__PART_DEFINITION:
                    return SysmlPackage.PART_USAGE__PART_DEFINITION;
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
        if (baseClass == OccurrenceUsage.class) {
            switch (baseFeatureID) {
                case SysmlPackage.OCCURRENCE_USAGE__IS_INDIVIDUAL:
                    return SysmlPackage.CONNECTION_USAGE__IS_INDIVIDUAL;
                case SysmlPackage.OCCURRENCE_USAGE__PORTION_KIND:
                    return SysmlPackage.CONNECTION_USAGE__PORTION_KIND;
                case SysmlPackage.OCCURRENCE_USAGE__INDIVIDUAL_DEFINITION:
                    return SysmlPackage.CONNECTION_USAGE__INDIVIDUAL_DEFINITION;
                case SysmlPackage.OCCURRENCE_USAGE__OCCURRENCE_DEFINITION:
                    return SysmlPackage.CONNECTION_USAGE__OCCURRENCE_DEFINITION;
                default:
                    return -1;
            }
        }
        if (baseClass == ItemUsage.class) {
            switch (baseFeatureID) {
                case SysmlPackage.ITEM_USAGE__ITEM_DEFINITION:
                    return SysmlPackage.CONNECTION_USAGE__ITEM_DEFINITION;
                default:
                    return -1;
            }
        }
        if (baseClass == PartUsage.class) {
            switch (baseFeatureID) {
                case SysmlPackage.PART_USAGE__PART_DEFINITION:
                    return SysmlPackage.CONNECTION_USAGE__PART_DEFINITION;
                default:
                    return -1;
            }
        }
        return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
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
        result.append(" (isIndividual: ");
        result.append(this.isIndividual);
        result.append(", portionKind: ");
        result.append(this.portionKind);
        result.append(')');
        return result.toString();
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Association> getAssociation() {
        EList<Association> associations = new BasicEList<>();
        EList<AssociationStructure> connectionDefinition = this.getConnectionDefinition();
        if (connectionDefinition != null) {
            associations.addAll(connectionDefinition);
        }
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getConnector_Association(), associations.size(), associations.toArray());
    }

} // ConnectionUsageImpl
