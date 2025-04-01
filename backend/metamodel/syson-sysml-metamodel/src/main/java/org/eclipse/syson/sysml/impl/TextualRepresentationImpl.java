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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.TextualRepresentation;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Textual Representation</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.TextualRepresentationImpl#getBody <em>Body</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.TextualRepresentationImpl#getLanguage <em>Language</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.TextualRepresentationImpl#getRepresentedElement <em>Represented
 * Element</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TextualRepresentationImpl extends AnnotatingElementImpl implements TextualRepresentation {
    /**
     * The default value of the '{@link #getBody() <em>Body</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @see #getBody()
     * @generated
     * @ordered
     */
    protected static final String BODY_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getBody() <em>Body</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @see #getBody()
     * @generated
     * @ordered
     */
    protected String body = BODY_EDEFAULT;

    /**
     * The default value of the '{@link #getLanguage() <em>Language</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getLanguage()
     * @generated
     * @ordered
     */
    protected static final String LANGUAGE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getLanguage() <em>Language</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getLanguage()
     * @generated
     * @ordered
     */
    protected String language = LANGUAGE_EDEFAULT;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected TextualRepresentationImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getTextualRepresentation();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getBody() {
        return this.body;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setBody(String newBody) {
        String oldBody = this.body;
        this.body = newBody;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.TEXTUAL_REPRESENTATION__BODY, oldBody, this.body));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getLanguage() {
        return this.language;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setLanguage(String newLanguage) {
        String oldLanguage = this.language;
        this.language = newLanguage;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.TEXTUAL_REPRESENTATION__LANGUAGE, oldLanguage, this.language));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Element getRepresentedElement() {
        Element representedElement = this.basicGetRepresentedElement();
        return representedElement != null && representedElement.eIsProxy() ? (Element) this.eResolveProxy((InternalEObject) representedElement) : representedElement;
    }

    /**
     * <!-- begin-user-doc -->
     *
     * <pre>
     * /representedElement : Element {subsets owner, redefines annotatedElement}
     * The Element that is represented by this TextualRepresentation.
     * </pre>
     * 
     * <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public Element basicGetRepresentedElement() {
        return this.getOwner();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.TEXTUAL_REPRESENTATION__BODY:
                return this.getBody();
            case SysmlPackage.TEXTUAL_REPRESENTATION__LANGUAGE:
                return this.getLanguage();
            case SysmlPackage.TEXTUAL_REPRESENTATION__REPRESENTED_ELEMENT:
                if (resolve) {
                    return this.getRepresentedElement();
                }
                return this.basicGetRepresentedElement();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case SysmlPackage.TEXTUAL_REPRESENTATION__BODY:
                this.setBody((String) newValue);
                return;
            case SysmlPackage.TEXTUAL_REPRESENTATION__LANGUAGE:
                this.setLanguage((String) newValue);
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
            case SysmlPackage.TEXTUAL_REPRESENTATION__BODY:
                this.setBody(BODY_EDEFAULT);
                return;
            case SysmlPackage.TEXTUAL_REPRESENTATION__LANGUAGE:
                this.setLanguage(LANGUAGE_EDEFAULT);
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
            case SysmlPackage.TEXTUAL_REPRESENTATION__BODY:
                return BODY_EDEFAULT == null ? this.body != null : !BODY_EDEFAULT.equals(this.body);
            case SysmlPackage.TEXTUAL_REPRESENTATION__LANGUAGE:
                return LANGUAGE_EDEFAULT == null ? this.language != null : !LANGUAGE_EDEFAULT.equals(this.language);
            case SysmlPackage.TEXTUAL_REPRESENTATION__REPRESENTED_ELEMENT:
                return this.basicGetRepresentedElement() != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String toString() {
        if (this.eIsProxy()) {
            return super.toString();
        }

        StringBuilder result = new StringBuilder(super.toString());
        result.append(" (body: ");
        result.append(this.body);
        result.append(", language: ");
        result.append(this.language);
        result.append(')');
        return result.toString();
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Element> getAnnotatedElement() {
        EList<Element> annotatedElements = new BasicEList<>();
        Element representedElement = this.getRepresentedElement();
        if (representedElement != null) {
            annotatedElements.add(representedElement);
        }
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getAnnotatingElement_AnnotatedElement(), annotatedElements.size(), annotatedElements.toArray());
    }

} // TextualRepresentationImpl
