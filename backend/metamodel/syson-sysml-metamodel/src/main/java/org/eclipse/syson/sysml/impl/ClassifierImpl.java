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
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.syson.sysml.Classifier;
import org.eclipse.syson.sysml.Subclassification;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Classifier</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.ClassifierImpl#getOwnedSubclassification <em>Owned
 * Subclassification</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ClassifierImpl extends TypeImpl implements Classifier {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected ClassifierImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getClassifier();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Subclassification> getOwnedSubclassification() {
        List<Subclassification> ownedSubclassifications = new ArrayList<>();
        this.getOwnedRelationship().stream()
                .filter(Subclassification.class::isInstance)
                .map(Subclassification.class::cast)
                .forEach(ownedSubclassifications::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getClassifier_OwnedSubclassification(), ownedSubclassifications.size(), ownedSubclassifications.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.CLASSIFIER__OWNED_SUBCLASSIFICATION:
                return this.getOwnedSubclassification();
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
            case SysmlPackage.CLASSIFIER__OWNED_SUBCLASSIFICATION:
                return !this.getOwnedSubclassification().isEmpty();
        }
        return super.eIsSet(featureID);
    }

} // ClassifierImpl
