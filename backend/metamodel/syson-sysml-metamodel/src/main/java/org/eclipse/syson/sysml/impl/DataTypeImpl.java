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

import java.util.List;
import java.util.Optional;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.syson.sysml.DataType;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.Specialization;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.TextualRepresentation;
import org.eclipse.syson.sysml.helper.PrettyPrinter;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Data Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class DataTypeImpl extends ClassifierImpl implements DataType {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DataTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getDataType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<TextualRepresentation> getTextualRepresentation() {
        TextualRepresentation repr = getOrCreateTextualRepresentation();
        StringBuilder builder = new StringBuilder();
        if(isIsAbstract()){
            builder.append("abstract ");
        }
        builder.append("datatype ");
        if( getDeclaredName() != null){
            builder.append(PrettyPrinter.prettyPrintName(getDeclaredName()));
        }
        
        // Specialize
        List<Specialization> specialisation = getOwnedRelationship().stream().filter(t -> SysmlPackage.eINSTANCE.getSpecialization().isSuperTypeOf(t.eClass())).map(t -> (Specialization) t).toList();
        if( !specialisation.isEmpty()){
            builder.append(":> ");
            builder.append(String.join(", ", specialisation.stream().filter(t -> t.getGeneral() != null).map(t -> PrettyPrinter.prettyPrintName(t.getGeneral().getQualifiedName())).toList()));
        }


        builder.append(";");
        List<TextualRepresentation> textualRepresentation = List.of(repr);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getElement_TextualRepresentation(), textualRepresentation.size(), textualRepresentation.toArray());
    }

} //DataTypeImpl
