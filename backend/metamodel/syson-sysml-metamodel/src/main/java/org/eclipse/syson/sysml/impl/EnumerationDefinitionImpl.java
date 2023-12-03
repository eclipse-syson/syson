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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.EnumerationDefinition;
import org.eclipse.syson.sysml.EnumerationUsage;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.VariantMembership;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Enumeration Definition</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.impl.EnumerationDefinitionImpl#getEnumeratedValue <em>Enumerated Value</em>}</li>
 * </ul>
 *
 * @generated
 */
public class EnumerationDefinitionImpl extends AttributeDefinitionImpl implements EnumerationDefinition {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EnumerationDefinitionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getEnumerationDefinition();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<EnumerationUsage> getEnumeratedValue() {
        List<EnumerationUsage> enumeratedValues = new ArrayList<>();
        this.getOwnedRelationship().stream()
            .filter(VariantMembership.class::isInstance)
            .map(VariantMembership.class::cast)
            .flatMap(vm -> vm.getOwnedRelatedElement().stream())
            .filter(EnumerationUsage.class::isInstance)
            .map(EnumerationUsage.class::cast)
            .forEach(enumeratedValues::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getEnumerationDefinition_EnumeratedValue(), enumeratedValues.size(), enumeratedValues.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.ENUMERATION_DEFINITION__ENUMERATED_VALUE:
                return getEnumeratedValue();
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
            case SysmlPackage.ENUMERATION_DEFINITION__ENUMERATED_VALUE:
                return !getEnumeratedValue().isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //EnumerationDefinitionImpl
