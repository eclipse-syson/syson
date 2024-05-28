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

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.TextualRepresentation;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.VariantMembership;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Variant Membership</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.VariantMembershipImpl#getOwnedVariantUsage <em>Owned Variant Usage</em>}</li>
 * </ul>
 *
 * @generated
 */
public class VariantMembershipImpl extends OwningMembershipImpl implements VariantMembership {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected VariantMembershipImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getVariantMembership();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Usage getOwnedVariantUsage() {
        Usage ownedVariantUsage = this.basicGetOwnedVariantUsage();
        return ownedVariantUsage != null && ownedVariantUsage.eIsProxy() ? (Usage) this.eResolveProxy((InternalEObject) ownedVariantUsage) : ownedVariantUsage;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Usage basicGetOwnedVariantUsage() {
        // TODO: implement this method to return the 'Owned Variant Usage' reference
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
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.VARIANT_MEMBERSHIP__OWNED_VARIANT_USAGE:
                if (resolve)
                    return this.getOwnedVariantUsage();
                return this.basicGetOwnedVariantUsage();
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
            case SysmlPackage.VARIANT_MEMBERSHIP__OWNED_VARIANT_USAGE:
                return this.basicGetOwnedVariantUsage() != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<TextualRepresentation> getTextualRepresentation() {
        List<TextualRepresentation> textualRepresentation = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getElement_TextualRepresentation(), textualRepresentation.size(), textualRepresentation.toArray());
    }

} // VariantMembershipImpl
