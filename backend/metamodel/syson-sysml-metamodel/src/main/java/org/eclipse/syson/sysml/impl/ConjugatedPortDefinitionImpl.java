/**
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
 */
package org.eclipse.syson.sysml.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.syson.sysml.ConjugatedPortDefinition;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.PortConjugation;
import org.eclipse.syson.sysml.PortDefinition;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.helper.LabelConstants;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Conjugated Port Definition</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.ConjugatedPortDefinitionImpl#getOriginalPortDefinition <em>Original Port
 * Definition</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ConjugatedPortDefinitionImpl#getOwnedPortConjugator <em>Owned Port
 * Conjugator</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ConjugatedPortDefinitionImpl extends PortDefinitionImpl implements ConjugatedPortDefinition {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected ConjugatedPortDefinitionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getConjugatedPortDefinition();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public PortDefinition getOriginalPortDefinition() {
        PortDefinition originalPortDefinition = this.basicGetOriginalPortDefinition();
        return originalPortDefinition != null && originalPortDefinition.eIsProxy() ? (PortDefinition) this.eResolveProxy((InternalEObject) originalPortDefinition) : originalPortDefinition;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public PortDefinition basicGetOriginalPortDefinition() {
        Namespace owningNamespace = this.getOwningNamespace();
        if (owningNamespace instanceof PortDefinition portDefinition) {
            return portDefinition;
        }
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public PortConjugation getOwnedPortConjugator() {
        PortConjugation ownedPortConjugator = this.basicGetOwnedPortConjugator();
        return ownedPortConjugator != null && ownedPortConjugator.eIsProxy() ? (PortConjugation) this.eResolveProxy((InternalEObject) ownedPortConjugator) : ownedPortConjugator;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public PortConjugation basicGetOwnedPortConjugator() {
        return this.getOwnedRelationship().stream()
                .filter(PortConjugation.class::isInstance)
                .map(PortConjugation.class::cast)
                .findFirst()
                .orElse(null);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public void setDeclaredName(String newDeclaredName) {
        // Nothing to do here
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public String getDeclaredName() {
        PortDefinition originalPortDefinition = this.getOriginalPortDefinition();
        if (originalPortDefinition != null) {
            StringBuilder declaredName = new StringBuilder();
            declaredName.append(LabelConstants.CONJUGATED);
            declaredName.append(originalPortDefinition.getDeclaredName());
            return declaredName.toString();
        }
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
            case SysmlPackage.CONJUGATED_PORT_DEFINITION__ORIGINAL_PORT_DEFINITION:
                if (resolve)
                    return this.getOriginalPortDefinition();
                return this.basicGetOriginalPortDefinition();
            case SysmlPackage.CONJUGATED_PORT_DEFINITION__OWNED_PORT_CONJUGATOR:
                if (resolve)
                    return this.getOwnedPortConjugator();
                return this.basicGetOwnedPortConjugator();
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
            case SysmlPackage.CONJUGATED_PORT_DEFINITION__ORIGINAL_PORT_DEFINITION:
                return this.basicGetOriginalPortDefinition() != null;
            case SysmlPackage.CONJUGATED_PORT_DEFINITION__OWNED_PORT_CONJUGATOR:
                return this.basicGetOwnedPortConjugator() != null;
        }
        return super.eIsSet(featureID);
    }

    @Override
    public boolean isIsConjugated() {
        return true;
    }

} // ConjugatedPortDefinitionImpl
