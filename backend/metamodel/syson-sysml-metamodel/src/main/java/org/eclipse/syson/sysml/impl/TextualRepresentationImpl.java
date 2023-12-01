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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.TextualRepresentation;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Textual Representation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.impl.TextualRepresentationImpl#getBody <em>Body</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.TextualRepresentationImpl#getLanguage <em>Language</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.TextualRepresentationImpl#getRepresentedElement <em>Represented Element</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TextualRepresentationImpl extends AnnotatingElementImpl implements TextualRepresentation {
    /**
     * The default value of the '{@link #getBody() <em>Body</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBody()
     * @generated
     * @ordered
     */
    protected static final String BODY_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getBody() <em>Body</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBody()
     * @generated
     * @ordered
     */
    protected String body = BODY_EDEFAULT;

    /**
     * The default value of the '{@link #getLanguage() <em>Language</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLanguage()
     * @generated
     * @ordered
     */
    protected static final String LANGUAGE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getLanguage() <em>Language</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLanguage()
     * @generated
     * @ordered
     */
    protected String language = LANGUAGE_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected TextualRepresentationImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getTextualRepresentation();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getBody() {
        return body;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setBody(String newBody) {
        String oldBody = body;
        body = newBody;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.TEXTUAL_REPRESENTATION__BODY, oldBody, body));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getLanguage() {
        return language;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setLanguage(String newLanguage) {
        String oldLanguage = language;
        language = newLanguage;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.TEXTUAL_REPRESENTATION__LANGUAGE, oldLanguage, language));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Element getRepresentedElement() {
        Element representedElement = basicGetRepresentedElement();
        return representedElement != null && representedElement.eIsProxy() ? (Element)eResolveProxy((InternalEObject)representedElement) : representedElement;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Element basicGetRepresentedElement() {
        // TODO: implement this method to return the 'Represented Element' reference
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
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.TEXTUAL_REPRESENTATION__BODY:
                return getBody();
            case SysmlPackage.TEXTUAL_REPRESENTATION__LANGUAGE:
                return getLanguage();
            case SysmlPackage.TEXTUAL_REPRESENTATION__REPRESENTED_ELEMENT:
                if (resolve) return getRepresentedElement();
                return basicGetRepresentedElement();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case SysmlPackage.TEXTUAL_REPRESENTATION__BODY:
                setBody((String)newValue);
                return;
            case SysmlPackage.TEXTUAL_REPRESENTATION__LANGUAGE:
                setLanguage((String)newValue);
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
            case SysmlPackage.TEXTUAL_REPRESENTATION__BODY:
                setBody(BODY_EDEFAULT);
                return;
            case SysmlPackage.TEXTUAL_REPRESENTATION__LANGUAGE:
                setLanguage(LANGUAGE_EDEFAULT);
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
            case SysmlPackage.TEXTUAL_REPRESENTATION__BODY:
                return BODY_EDEFAULT == null ? body != null : !BODY_EDEFAULT.equals(body);
            case SysmlPackage.TEXTUAL_REPRESENTATION__LANGUAGE:
                return LANGUAGE_EDEFAULT == null ? language != null : !LANGUAGE_EDEFAULT.equals(language);
            case SysmlPackage.TEXTUAL_REPRESENTATION__REPRESENTED_ELEMENT:
                return basicGetRepresentedElement() != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuilder result = new StringBuilder(super.toString());
        result.append(" (body: ");
        result.append(body);
        result.append(", language: ");
        result.append(language);
        result.append(')');
        return result.toString();
    }

} //TextualRepresentationImpl
