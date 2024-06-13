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
import org.eclipse.emf.common.util.BasicEList;
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
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.Metaclass;
import org.eclipse.syson.sysml.MetadataFeature;
import org.eclipse.syson.sysml.MetadataUsage;
import org.eclipse.syson.sysml.Structure;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Usage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Metadata Usage</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.MetadataUsageImpl#getAnnotatedElement <em>Annotated Element</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.MetadataUsageImpl#getAnnotation <em>Annotation</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.MetadataUsageImpl#getMetaclass <em>Metaclass</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.MetadataUsageImpl#getMetadataDefinition <em>Metadata Definition</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MetadataUsageImpl extends ItemUsageImpl implements MetadataUsage {
    /**
     * The cached value of the '{@link #getAnnotation() <em>Annotation</em>}' reference list. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getAnnotation()
     * @generated
     * @ordered
     */
    protected EList<Annotation> annotation;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected MetadataUsageImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getMetadataUsage();
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
        if (this.annotation == null) {
            this.annotation = new EObjectWithInverseResolvingEList<>(Annotation.class, this, SysmlPackage.METADATA_USAGE__ANNOTATION, SysmlPackage.ANNOTATION__ANNOTATING_ELEMENT);
        }
        return this.annotation;
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
     * @generated NOT
     */
    public Metaclass basicGetMetaclass() {
        return this.getMetadataDefinition();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Metaclass getMetadataDefinition() {
        Metaclass metadataDefinition = this.basicGetMetadataDefinition();
        return metadataDefinition != null && metadataDefinition.eIsProxy() ? (Metaclass) this.eResolveProxy((InternalEObject) metadataDefinition) : metadataDefinition;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public Metaclass basicGetMetadataDefinition() {

        Metaclass metaClass = (Metaclass) this.getOwnedRelationship().stream()
                .flatMap(rel -> rel.getOwnedRelatedElement().stream())
                .filter(FeatureTyping.class::isInstance)
                .map(FeatureTyping.class::cast)
                .filter(ft -> ft.getType() instanceof Metaclass)
                .map(FeatureTyping::getType)
                .findFirst().orElse(null);

        return metaClass;
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
    @SuppressWarnings("unchecked")
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case SysmlPackage.METADATA_USAGE__ANNOTATION:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) this.getAnnotation()).basicAdd(otherEnd, msgs);
        }
        return super.eInverseAdd(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case SysmlPackage.METADATA_USAGE__ANNOTATION:
                return ((InternalEList<?>) this.getAnnotation()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.METADATA_USAGE__ANNOTATED_ELEMENT:
                return this.getAnnotatedElement();
            case SysmlPackage.METADATA_USAGE__ANNOTATION:
                return this.getAnnotation();
            case SysmlPackage.METADATA_USAGE__METACLASS:
                if (resolve) {
                    return this.getMetaclass();
                }
                return this.basicGetMetaclass();
            case SysmlPackage.METADATA_USAGE__METADATA_DEFINITION:
                if (resolve) {
                    return this.getMetadataDefinition();
                }
                return this.basicGetMetadataDefinition();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case SysmlPackage.METADATA_USAGE__ANNOTATION:
                this.getAnnotation().clear();
                this.getAnnotation().addAll((Collection<? extends Annotation>) newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case SysmlPackage.METADATA_USAGE__ANNOTATION:
                this.getAnnotation().clear();
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case SysmlPackage.METADATA_USAGE__ANNOTATED_ELEMENT:
                return !this.getAnnotatedElement().isEmpty();
            case SysmlPackage.METADATA_USAGE__ANNOTATION:
                return this.annotation != null && !this.annotation.isEmpty();
            case SysmlPackage.METADATA_USAGE__METACLASS:
                return this.basicGetMetaclass() != null;
            case SysmlPackage.METADATA_USAGE__METADATA_DEFINITION:
                return this.basicGetMetadataDefinition() != null;
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
                case SysmlPackage.METADATA_USAGE__ANNOTATED_ELEMENT:
                    return SysmlPackage.ANNOTATING_ELEMENT__ANNOTATED_ELEMENT;
                case SysmlPackage.METADATA_USAGE__ANNOTATION:
                    return SysmlPackage.ANNOTATING_ELEMENT__ANNOTATION;
                default:
                    return -1;
            }
        }
        if (baseClass == MetadataFeature.class) {
            switch (derivedFeatureID) {
                case SysmlPackage.METADATA_USAGE__METACLASS:
                    return SysmlPackage.METADATA_FEATURE__METACLASS;
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
                    return SysmlPackage.METADATA_USAGE__ANNOTATED_ELEMENT;
                case SysmlPackage.ANNOTATING_ELEMENT__ANNOTATION:
                    return SysmlPackage.METADATA_USAGE__ANNOTATION;
                default:
                    return -1;
            }
        }
        if (baseClass == MetadataFeature.class) {
            switch (baseFeatureID) {
                case SysmlPackage.METADATA_FEATURE__METACLASS:
                    return SysmlPackage.METADATA_USAGE__METACLASS;
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
    public int eDerivedOperationID(int baseOperationID, Class<?> baseClass) {
        if (baseClass == AnnotatingElement.class) {
            switch (baseOperationID) {
                default:
                    return -1;
            }
        }
        if (baseClass == MetadataFeature.class) {
            switch (baseOperationID) {
                case SysmlPackage.METADATA_FEATURE___EVALUATE_FEATURE__FEATURE:
                    return SysmlPackage.METADATA_USAGE___EVALUATE_FEATURE__FEATURE;
                case SysmlPackage.METADATA_FEATURE___IS_SEMANTIC:
                    return SysmlPackage.METADATA_USAGE___IS_SEMANTIC;
                case SysmlPackage.METADATA_FEATURE___IS_SYNTACTIC:
                    return SysmlPackage.METADATA_USAGE___IS_SYNTACTIC;
                case SysmlPackage.METADATA_FEATURE___SYNTAX_ELEMENT:
                    return SysmlPackage.METADATA_USAGE___SYNTAX_ELEMENT;
                default:
                    return -1;
            }
        }
        return super.eDerivedOperationID(baseOperationID, baseClass);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
        switch (operationID) {
            case SysmlPackage.METADATA_USAGE___EVALUATE_FEATURE__FEATURE:
                return this.evaluateFeature((Feature) arguments.get(0));
            case SysmlPackage.METADATA_USAGE___IS_SEMANTIC:
                return this.isSemantic();
            case SysmlPackage.METADATA_USAGE___IS_SYNTACTIC:
                return this.isSyntactic();
            case SysmlPackage.METADATA_USAGE___SYNTAX_ELEMENT:
                return this.syntaxElement();
        }
        return super.eInvoke(operationID, arguments);
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Structure> getItemDefinition() {
        EList<Structure> itemDefinitions = new BasicEList<>();
        Metaclass metadataDefinition = this.getMetadataDefinition();
        if (metadataDefinition != null) {
            itemDefinitions.add(metadataDefinition);
        }
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getItemUsage_ItemDefinition(), itemDefinitions.size(), itemDefinitions.toArray());
    }

} // MetadataUsageImpl
