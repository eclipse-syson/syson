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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.syson.sysml.AnnotatingElement;
import org.eclipse.syson.sysml.Annotation;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.Metaclass;
import org.eclipse.syson.sysml.MetadataFeature;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Usage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Metadata Feature</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.impl.MetadataFeatureImpl#getAnnotatedElement <em>Annotated Element</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.MetadataFeatureImpl#getAnnotation <em>Annotation</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.MetadataFeatureImpl#getMetaclass <em>Metaclass</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MetadataFeatureImpl extends FeatureImpl implements MetadataFeature {
    /**
     * The cached value of the '{@link #getAnnotation() <em>Annotation</em>}' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAnnotation()
     * @generated
     * @ordered
     */
    protected EList<Annotation> annotation;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected MetadataFeatureImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getMetadataFeature();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<Element> getAnnotatedElement() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getAnnotatingElement_AnnotatedElement(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<Annotation> getAnnotation() {
        if (annotation == null) {
            annotation = new EObjectWithInverseResolvingEList<Annotation>(Annotation.class, this, SysmlPackage.METADATA_FEATURE__ANNOTATION, SysmlPackage.ANNOTATION__ANNOTATING_ELEMENT);
        }
        return annotation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Metaclass getMetaclass() {
        Metaclass metaclass = basicGetMetaclass();
        return metaclass != null && metaclass.eIsProxy() ? (Metaclass)eResolveProxy((InternalEObject)metaclass) : metaclass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Metaclass basicGetMetaclass() {
        // TODO: implement this method to return the 'Metaclass' reference
        // -> do not perform proxy resolution
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<Element> evaluateFeature(Feature baseFeature) {
        // TODO: implement this method
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public boolean isSemantic() {
        return false;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public boolean isSyntactic() {
        return false;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Element syntaxElement() {
        // TODO: implement this method
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case SysmlPackage.METADATA_FEATURE__ANNOTATION:
                return ((InternalEList<InternalEObject>)(InternalEList<?>)getAnnotation()).basicAdd(otherEnd, msgs);
        }
        return super.eInverseAdd(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case SysmlPackage.METADATA_FEATURE__ANNOTATION:
                return ((InternalEList<?>)getAnnotation()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.METADATA_FEATURE__ANNOTATED_ELEMENT:
                return getAnnotatedElement();
            case SysmlPackage.METADATA_FEATURE__ANNOTATION:
                return getAnnotation();
            case SysmlPackage.METADATA_FEATURE__METACLASS:
                if (resolve) return getMetaclass();
                return basicGetMetaclass();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case SysmlPackage.METADATA_FEATURE__ANNOTATION:
                getAnnotation().clear();
                getAnnotation().addAll((Collection<? extends Annotation>)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case SysmlPackage.METADATA_FEATURE__ANNOTATION:
                getAnnotation().clear();
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case SysmlPackage.METADATA_FEATURE__ANNOTATED_ELEMENT:
                return !getAnnotatedElement().isEmpty();
            case SysmlPackage.METADATA_FEATURE__ANNOTATION:
                return annotation != null && !annotation.isEmpty();
            case SysmlPackage.METADATA_FEATURE__METACLASS:
                return basicGetMetaclass() != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
        if (baseClass == AnnotatingElement.class) {
            switch (derivedFeatureID) {
                case SysmlPackage.METADATA_FEATURE__ANNOTATED_ELEMENT: return SysmlPackage.ANNOTATING_ELEMENT__ANNOTATED_ELEMENT;
                case SysmlPackage.METADATA_FEATURE__ANNOTATION: return SysmlPackage.ANNOTATING_ELEMENT__ANNOTATION;
                default: return -1;
            }
        }
        return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
        if (baseClass == AnnotatingElement.class) {
            switch (baseFeatureID) {
                case SysmlPackage.ANNOTATING_ELEMENT__ANNOTATED_ELEMENT: return SysmlPackage.METADATA_FEATURE__ANNOTATED_ELEMENT;
                case SysmlPackage.ANNOTATING_ELEMENT__ANNOTATION: return SysmlPackage.METADATA_FEATURE__ANNOTATION;
                default: return -1;
            }
        }
        return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
        switch (operationID) {
            case SysmlPackage.METADATA_FEATURE___EVALUATE_FEATURE__FEATURE:
                return evaluateFeature((Feature)arguments.get(0));
            case SysmlPackage.METADATA_FEATURE___IS_SEMANTIC:
                return isSemantic();
            case SysmlPackage.METADATA_FEATURE___IS_SYNTACTIC:
                return isSyntactic();
            case SysmlPackage.METADATA_FEATURE___SYNTAX_ELEMENT:
                return syntaxElement();
        }
        return super.eInvoke(operationID, arguments);
    }

} //MetadataFeatureImpl
