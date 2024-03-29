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
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.LiteralInteger;
import org.eclipse.syson.sysml.PortDefinition;
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.Subsetting;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.TextualRepresentation;
import org.eclipse.syson.sysml.helper.PrettyPrinter;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Port Usage</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.impl.PortUsageImpl#getPortDefinition <em>Port Definition</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PortUsageImpl extends OccurrenceUsageImpl implements PortUsage {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected PortUsageImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getPortUsage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
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
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.PORT_USAGE__PORT_DEFINITION:
                return getPortDefinition();
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
            case SysmlPackage.PORT_USAGE__PORT_DEFINITION:
                return !getPortDefinition().isEmpty();
        }
        return super.eIsSet(featureID);
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
        if(isIsEnd()){
            builder.append("end ");
        } else {
            builder.append("port ");
        }
        builder.append(PrettyPrinter.prettyPrintName(getDeclaredName()));

        // Type
        Optional<FeatureTyping> featureTyping = getOwnedRelationship().stream().filter(t -> SysmlPackage.eINSTANCE.getFeatureTyping().isSuperTypeOf(t.eClass())).map(t -> (FeatureTyping) t).findFirst();
        if(featureTyping.isPresent()){
            builder.append(": ");
            if(isIsConjugated()){
                builder.append("~");
            }
            if( featureTyping.get().getType() != null ){
                if(SysmlPackage.eINSTANCE.getConjugatedPortTyping().isSuperTypeOf(featureTyping.get().eClass())){
                    builder.append("~");
                }
                builder.append(PrettyPrinter.prettyPrintName(featureTyping.get().getType().getQualifiedName()));
            } else {
                builder.append(PrettyPrinter.prettyPrintName(featureTyping.get().getDeclaredName()));
            }
        }


        //Multiplicity
        if(getMultiplicity() != null){
            builder.append("[");
            Feature literal = getMultiplicity().getOwnedFeature().get(0);
            if(SysmlPackage.eINSTANCE.getLiteralInfinity().isSuperTypeOf(literal.eClass())){
                builder.append("*");
            }
            if(SysmlPackage.eINSTANCE.getLiteralInteger().isSuperTypeOf(literal.eClass())){
                builder.append(((LiteralInteger) literal).getValue());
            }
            builder.append("]");
        }

        // Redefinition
        Optional<Redefinition> redefinition = getOwnedRelationship().stream().filter(t -> SysmlPackage.eINSTANCE.getRedefinition().isSuperTypeOf(t.eClass())).map(t -> (Redefinition) t).findFirst();
        if(redefinition.isPresent()){
            builder.append(":>> ");

            if( redefinition.get().getRedefinedFeature() != null ){
                builder.append(PrettyPrinter.prettyPrintName(redefinition.get().getRedefinedFeature().getQualifiedName()));
            } else {
                builder.append(PrettyPrinter.prettyPrintName(redefinition.get().getDeclaredName()));
            }
        } else {
            // Subsetting
            Optional<Subsetting> subsetting = getOwnedRelationship().stream().filter(t -> SysmlPackage.eINSTANCE.getSubsetting().isSuperTypeOf(t.eClass())).map(t -> (Subsetting) t).findFirst();
            if(subsetting.isPresent()){
                builder.append(":> ");

                if( subsetting.get().getSubsettedFeature() != null ){
                    builder.append(PrettyPrinter.prettyPrintName(subsetting.get().getSubsettedFeature().getQualifiedName()));
                } else {
                    builder.append(PrettyPrinter.prettyPrintName(subsetting.get().getDeclaredName()));
                }
            }
        }

        List<FeatureMembership> featureMemberships = getOwnedFeatureMembership().stream().filter(t -> SysmlPackage.eINSTANCE.getFeatureMembership().isSuperTypeOf(t.eClass())).map(t -> (FeatureMembership) t).toList();

        if(! featureMemberships.isEmpty()){
            builder.append("{");
            for(FeatureMembership featureMembership: featureMemberships){
                for(var textualRepr: featureMembership.getTextualRepresentation()){
                    builder.append("\n");
                    builder.append(textualRepr.getBody());
                }
            }
            builder.append("\n}");
        } else {
            builder.append(";");
        }
    
        repr.setBody(builder.toString());
        List<TextualRepresentation> textualRepresentation = List.of(repr);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getElement_TextualRepresentation(), textualRepresentation.size(), textualRepresentation.toArray());
    }

} //PortUsageImpl
