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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.syson.sysml.Comment;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Comment</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.CommentImpl#getBody <em>Body</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.CommentImpl#getLocale <em>Locale</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CommentImpl extends AnnotatingElementImpl implements Comment {
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
     * The default value of the '{@link #getLocale() <em>Locale</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getLocale()
     * @generated
     * @ordered
     */
    protected static final String LOCALE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getLocale() <em>Locale</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getLocale()
     * @generated
     * @ordered
     */
    protected String locale = LOCALE_EDEFAULT;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected CommentImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getComment();
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
        if (this.eNotificationRequired())
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.COMMENT__BODY, oldBody, this.body));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getLocale() {
        return this.locale;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setLocale(String newLocale) {
        String oldLocale = this.locale;
        this.locale = newLocale;
        if (this.eNotificationRequired())
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.COMMENT__LOCALE, oldLocale, this.locale));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.COMMENT__BODY:
                return this.getBody();
            case SysmlPackage.COMMENT__LOCALE:
                return this.getLocale();
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
            case SysmlPackage.COMMENT__BODY:
                this.setBody((String) newValue);
                return;
            case SysmlPackage.COMMENT__LOCALE:
                this.setLocale((String) newValue);
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
            case SysmlPackage.COMMENT__BODY:
                this.setBody(BODY_EDEFAULT);
                return;
            case SysmlPackage.COMMENT__LOCALE:
                this.setLocale(LOCALE_EDEFAULT);
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
            case SysmlPackage.COMMENT__BODY:
                return BODY_EDEFAULT == null ? this.body != null : !BODY_EDEFAULT.equals(this.body);
            case SysmlPackage.COMMENT__LOCALE:
                return LOCALE_EDEFAULT == null ? this.locale != null : !LOCALE_EDEFAULT.equals(this.locale);
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
        if (this.eIsProxy())
            return super.toString();

        StringBuilder result = new StringBuilder(super.toString());
        result.append(" (body: ");
        result.append(this.body);
        result.append(", locale: ");
        result.append(this.locale);
        result.append(')');
        return result.toString();
    }

} // CommentImpl
