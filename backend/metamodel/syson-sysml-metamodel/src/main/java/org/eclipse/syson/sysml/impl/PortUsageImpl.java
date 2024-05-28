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
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.PortDefinition;
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.TextualRepresentation;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Port Usage</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.PortUsageImpl#getPortDefinition <em>Port Definition</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PortUsageImpl extends OccurrenceUsageImpl implements PortUsage {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected PortUsageImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getPortUsage();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<PortDefinition> getPortDefinition() {
        List<PortDefinition> portDefinitions = new ArrayList<>();
        this.getOwnedRelationship().stream()
                .filter(FeatureTyping.class::isInstance)
                .map(FeatureTyping.class::cast)
                .map(ft -> ft.getType())
                .filter(PortDefinition.class::isInstance)
                .map(PortDefinition.class::cast)
                .forEach(portDefinitions::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getPortUsage_PortDefinition(), portDefinitions.size(), portDefinitions.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.PORT_USAGE__PORT_DEFINITION:
                return this.getPortDefinition();
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
            case SysmlPackage.PORT_USAGE__PORT_DEFINITION:
                return !this.getPortDefinition().isEmpty();
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

} // PortUsageImpl
