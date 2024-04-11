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
import java.util.Optional;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.syson.sysml.AttributeDefinition;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.Subclassification;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.TextualRepresentation;
import org.eclipse.syson.sysml.helper.PrettyPrinter;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Attribute Definition</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class AttributeDefinitionImpl extends DefinitionImpl implements AttributeDefinition {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected AttributeDefinitionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getAttributeDefinition();
    }


    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<TextualRepresentation> getTextualRepresentation() {
        List<TextualRepresentation> textualRepresentation = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getElement_TextualRepresentation(), textualRepresentation.size(), textualRepresentation.toArray());
    }

} //AttributeDefinitionImpl
