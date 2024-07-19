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

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.syson.sysml.DataType;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.EnumerationDefinition;
import org.eclipse.syson.sysml.EnumerationUsage;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.TextualRepresentation;
import org.eclipse.syson.sysml.Type;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Enumeration Usage</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.EnumerationUsageImpl#getEnumerationDefinition <em>Enumeration
 * Definition</em>}</li>
 * </ul>
 *
 * @generated
 */
public class EnumerationUsageImpl extends AttributeUsageImpl implements EnumerationUsage {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected EnumerationUsageImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getEnumerationUsage();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EnumerationDefinition getEnumerationDefinition() {
        EnumerationDefinition enumerationDefinition = this.basicGetEnumerationDefinition();
        return enumerationDefinition != null && enumerationDefinition.eIsProxy() ? (EnumerationDefinition) this.eResolveProxy((InternalEObject) enumerationDefinition) : enumerationDefinition;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public EnumerationDefinition basicGetEnumerationDefinition() {
        EnumerationDefinition enumerationDefinition = null;
        EList<Relationship> relationships = this.getOwnedRelationship();
        for (Relationship relationship : relationships) {
            if (relationship instanceof FeatureTyping featureTyping) {
                Type type = featureTyping.getType();
                if (type instanceof EnumerationDefinition definition) {
                    enumerationDefinition = definition;
                    break;
                }
            }
        }
        if (enumerationDefinition == null) {
            Definition owningDefinition = this.getOwningDefinition();
            if (owningDefinition instanceof EnumerationDefinition definition) {
                enumerationDefinition = definition;
            }
        }
        return enumerationDefinition;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.ENUMERATION_USAGE__ENUMERATION_DEFINITION:
                if (resolve)
                    return this.getEnumerationDefinition();
                return this.basicGetEnumerationDefinition();
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
            case SysmlPackage.ENUMERATION_USAGE__ENUMERATION_DEFINITION:
                return this.basicGetEnumerationDefinition() != null;
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

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<DataType> getAttributeDefinition() {
        EList<DataType> attributeDefinitions = new BasicEList<>();
        EnumerationDefinition enumerationDefinition = this.getEnumerationDefinition();
        if (enumerationDefinition != null) {
            attributeDefinitions.add(enumerationDefinition);
        }
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getAttributeUsage_AttributeDefinition(), attributeDefinitions.size(), attributeDefinitions.toArray());
    }

} // EnumerationUsageImpl
