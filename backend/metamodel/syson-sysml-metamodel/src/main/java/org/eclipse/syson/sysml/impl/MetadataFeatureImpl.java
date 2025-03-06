/*******************************************************************************
* Copyright (c) 2023, 2025 Obeo.
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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.syson.sysml.AnnotatingElement;
import org.eclipse.syson.sysml.Annotation;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.Metaclass;
import org.eclipse.syson.sysml.MetadataFeature;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Usage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Metadata Feature</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.MetadataFeatureImpl#getAnnotatedElement <em>Annotated Element</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.MetadataFeatureImpl#getAnnotation <em>Annotation</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.MetadataFeatureImpl#getMetaclass <em>Metaclass</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MetadataFeatureImpl extends FeatureImpl implements MetadataFeature {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected MetadataFeatureImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getMetadataFeature();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Element> getAnnotatedElement() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getAnnotatingElement_AnnotatedElement(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<Annotation> getAnnotation() {
        // TODO: implement this method to return the 'Annotation' reference list
        // Ensure that you remove @generated or mark it @generated NOT
        // The list is expected to implement org.eclipse.emf.ecore.util.InternalEList and
        // org.eclipse.emf.ecore.EStructuralFeature.Setting
        // so it's likely that an appropriate subclass of org.eclipse.emf.ecore.util.EcoreEList should be used.
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Annotation> getOwnedAnnotatingRelationship() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getAnnotatingElement_OwnedAnnotatingRelationship(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Annotation getOwningAnnotatingRelationship() {
        Annotation owningAnnotatingRelationship = this.basicGetOwningAnnotatingRelationship();
        return owningAnnotatingRelationship != null && owningAnnotatingRelationship.eIsProxy() ? (Annotation) this.eResolveProxy((InternalEObject) owningAnnotatingRelationship)
                : owningAnnotatingRelationship;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Annotation basicGetOwningAnnotatingRelationship() {
        // TODO: implement this method to return the 'Owning Annotating Relationship' reference
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
    public Metaclass getMetaclass() {
        Metaclass metaclass = this.basicGetMetaclass();
        return metaclass != null && metaclass.eIsProxy() ? (Metaclass) this.eResolveProxy((InternalEObject) metaclass) : metaclass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Metaclass basicGetMetaclass() {
        // TODO: implement this method to return the 'Metaclass' reference
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
    public EList<Element> evaluateFeature(Feature baseFeature) {
        // TODO: implement this method
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public boolean isSemantic() {
        return false;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public boolean isSyntactic() {
        return false;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Element syntaxElement() {
        // TODO: implement this method
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
            case SysmlPackage.METADATA_FEATURE__ANNOTATED_ELEMENT:
                return this.getAnnotatedElement();
            case SysmlPackage.METADATA_FEATURE__ANNOTATION:
                return this.getAnnotation();
            case SysmlPackage.METADATA_FEATURE__OWNED_ANNOTATING_RELATIONSHIP:
                return this.getOwnedAnnotatingRelationship();
            case SysmlPackage.METADATA_FEATURE__OWNING_ANNOTATING_RELATIONSHIP:
                if (resolve) {
                    return this.getOwningAnnotatingRelationship();
                }
                return this.basicGetOwningAnnotatingRelationship();
            case SysmlPackage.METADATA_FEATURE__METACLASS:
                if (resolve) {
                    return this.getMetaclass();
                }
                return this.basicGetMetaclass();
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
            case SysmlPackage.METADATA_FEATURE__ANNOTATED_ELEMENT:
                return !this.getAnnotatedElement().isEmpty();
            case SysmlPackage.METADATA_FEATURE__ANNOTATION:
                return !this.getAnnotation().isEmpty();
            case SysmlPackage.METADATA_FEATURE__OWNED_ANNOTATING_RELATIONSHIP:
                return !this.getOwnedAnnotatingRelationship().isEmpty();
            case SysmlPackage.METADATA_FEATURE__OWNING_ANNOTATING_RELATIONSHIP:
                return this.basicGetOwningAnnotatingRelationship() != null;
            case SysmlPackage.METADATA_FEATURE__METACLASS:
                return this.basicGetMetaclass() != null;
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
        if (baseClass == AnnotatingElement.class) {
            switch (derivedFeatureID) {
                case SysmlPackage.METADATA_FEATURE__ANNOTATED_ELEMENT:
                    return SysmlPackage.ANNOTATING_ELEMENT__ANNOTATED_ELEMENT;
                case SysmlPackage.METADATA_FEATURE__ANNOTATION:
                    return SysmlPackage.ANNOTATING_ELEMENT__ANNOTATION;
                case SysmlPackage.METADATA_FEATURE__OWNED_ANNOTATING_RELATIONSHIP:
                    return SysmlPackage.ANNOTATING_ELEMENT__OWNED_ANNOTATING_RELATIONSHIP;
                case SysmlPackage.METADATA_FEATURE__OWNING_ANNOTATING_RELATIONSHIP:
                    return SysmlPackage.ANNOTATING_ELEMENT__OWNING_ANNOTATING_RELATIONSHIP;
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
        if (baseClass == AnnotatingElement.class) {
            switch (baseFeatureID) {
                case SysmlPackage.ANNOTATING_ELEMENT__ANNOTATED_ELEMENT:
                    return SysmlPackage.METADATA_FEATURE__ANNOTATED_ELEMENT;
                case SysmlPackage.ANNOTATING_ELEMENT__ANNOTATION:
                    return SysmlPackage.METADATA_FEATURE__ANNOTATION;
                case SysmlPackage.ANNOTATING_ELEMENT__OWNED_ANNOTATING_RELATIONSHIP:
                    return SysmlPackage.METADATA_FEATURE__OWNED_ANNOTATING_RELATIONSHIP;
                case SysmlPackage.ANNOTATING_ELEMENT__OWNING_ANNOTATING_RELATIONSHIP:
                    return SysmlPackage.METADATA_FEATURE__OWNING_ANNOTATING_RELATIONSHIP;
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
    public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
        switch (operationID) {
            case SysmlPackage.METADATA_FEATURE___EVALUATE_FEATURE__FEATURE:
                return this.evaluateFeature((Feature) arguments.get(0));
            case SysmlPackage.METADATA_FEATURE___IS_SEMANTIC:
                return this.isSemantic();
            case SysmlPackage.METADATA_FEATURE___IS_SYNTACTIC:
                return this.isSyntactic();
            case SysmlPackage.METADATA_FEATURE___SYNTAX_ELEMENT:
                return this.syntaxElement();
        }
        return super.eInvoke(operationID, arguments);
    }

} // MetadataFeatureImpl
