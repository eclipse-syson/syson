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

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.VisibilityKind;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Membership</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.impl.MembershipImpl#getMemberElementId <em>Member Element Id</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.MembershipImpl#getMemberName <em>Member Name</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.MembershipImpl#getMemberShortName <em>Member Short Name</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.MembershipImpl#getVisibility <em>Visibility</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.MembershipImpl#getMemberElement <em>Member Element</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.MembershipImpl#getMembershipOwningNamespace <em>Membership Owning Namespace</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MembershipImpl extends RelationshipImpl implements Membership {
    /**
     * The default value of the '{@link #getMemberElementId() <em>Member Element Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMemberElementId()
     * @generated
     * @ordered
     */
    protected static final String MEMBER_ELEMENT_ID_EDEFAULT = null;

    /**
     * The default value of the '{@link #getMemberName() <em>Member Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMemberName()
     * @generated
     * @ordered
     */
    protected static final String MEMBER_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getMemberName() <em>Member Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMemberName()
     * @generated
     * @ordered
     */
    protected String memberName = MEMBER_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getMemberShortName() <em>Member Short Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMemberShortName()
     * @generated
     * @ordered
     */
    protected static final String MEMBER_SHORT_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getMemberShortName() <em>Member Short Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMemberShortName()
     * @generated
     * @ordered
     */
    protected String memberShortName = MEMBER_SHORT_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getVisibility() <em>Visibility</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getVisibility()
     * @generated
     * @ordered
     */
    protected static final VisibilityKind VISIBILITY_EDEFAULT = VisibilityKind.PUBLIC;

    /**
     * The cached value of the '{@link #getVisibility() <em>Visibility</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getVisibility()
     * @generated
     * @ordered
     */
    protected VisibilityKind visibility = VISIBILITY_EDEFAULT;

    /**
     * The cached value of the '{@link #getMemberElement() <em>Member Element</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMemberElement()
     * @generated
     * @ordered
     */
    protected Element memberElement;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected MembershipImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getMembership();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Element getMemberElement() {
        if (memberElement != null && memberElement.eIsProxy()) {
            InternalEObject oldMemberElement = (InternalEObject)memberElement;
            memberElement = (Element)eResolveProxy(oldMemberElement);
            if (memberElement != oldMemberElement) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.MEMBERSHIP__MEMBER_ELEMENT, oldMemberElement, memberElement));
            }
        }
        return memberElement;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Element basicGetMemberElement() {
        return memberElement;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setMemberElement(Element newMemberElement) {
        Element oldMemberElement = memberElement;
        memberElement = newMemberElement;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.MEMBERSHIP__MEMBER_ELEMENT, oldMemberElement, memberElement));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public String getMemberElementId() {
        // TODO: implement this method to return the 'Member Element Id' attribute
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getMemberName() {
        return memberName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setMemberName(String newMemberName) {
        String oldMemberName = memberName;
        memberName = newMemberName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.MEMBERSHIP__MEMBER_NAME, oldMemberName, memberName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Namespace getMembershipOwningNamespace() {
        Namespace membershipOwningNamespace = basicGetMembershipOwningNamespace();
        return membershipOwningNamespace != null && membershipOwningNamespace.eIsProxy() ? (Namespace)eResolveProxy((InternalEObject)membershipOwningNamespace) : membershipOwningNamespace;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Namespace basicGetMembershipOwningNamespace() {
        // TODO: implement this method to return the 'Membership Owning Namespace' reference
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
    public String getMemberShortName() {
        return memberShortName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setMemberShortName(String newMemberShortName) {
        String oldMemberShortName = memberShortName;
        memberShortName = newMemberShortName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.MEMBERSHIP__MEMBER_SHORT_NAME, oldMemberShortName, memberShortName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public VisibilityKind getVisibility() {
        return visibility;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setVisibility(VisibilityKind newVisibility) {
        VisibilityKind oldVisibility = visibility;
        visibility = newVisibility == null ? VISIBILITY_EDEFAULT : newVisibility;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.MEMBERSHIP__VISIBILITY, oldVisibility, visibility));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public boolean isDistinguishableFrom(Membership other) {
        return false;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.MEMBERSHIP__MEMBER_ELEMENT_ID:
                return getMemberElementId();
            case SysmlPackage.MEMBERSHIP__MEMBER_NAME:
                return getMemberName();
            case SysmlPackage.MEMBERSHIP__MEMBER_SHORT_NAME:
                return getMemberShortName();
            case SysmlPackage.MEMBERSHIP__VISIBILITY:
                return getVisibility();
            case SysmlPackage.MEMBERSHIP__MEMBER_ELEMENT:
                if (resolve) return getMemberElement();
                return basicGetMemberElement();
            case SysmlPackage.MEMBERSHIP__MEMBERSHIP_OWNING_NAMESPACE:
                if (resolve) return getMembershipOwningNamespace();
                return basicGetMembershipOwningNamespace();
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
            case SysmlPackage.MEMBERSHIP__MEMBER_NAME:
                setMemberName((String)newValue);
                return;
            case SysmlPackage.MEMBERSHIP__MEMBER_SHORT_NAME:
                setMemberShortName((String)newValue);
                return;
            case SysmlPackage.MEMBERSHIP__VISIBILITY:
                setVisibility((VisibilityKind)newValue);
                return;
            case SysmlPackage.MEMBERSHIP__MEMBER_ELEMENT:
                setMemberElement((Element)newValue);
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
            case SysmlPackage.MEMBERSHIP__MEMBER_NAME:
                setMemberName(MEMBER_NAME_EDEFAULT);
                return;
            case SysmlPackage.MEMBERSHIP__MEMBER_SHORT_NAME:
                setMemberShortName(MEMBER_SHORT_NAME_EDEFAULT);
                return;
            case SysmlPackage.MEMBERSHIP__VISIBILITY:
                setVisibility(VISIBILITY_EDEFAULT);
                return;
            case SysmlPackage.MEMBERSHIP__MEMBER_ELEMENT:
                setMemberElement((Element)null);
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
            case SysmlPackage.MEMBERSHIP__MEMBER_ELEMENT_ID:
                return MEMBER_ELEMENT_ID_EDEFAULT == null ? getMemberElementId() != null : !MEMBER_ELEMENT_ID_EDEFAULT.equals(getMemberElementId());
            case SysmlPackage.MEMBERSHIP__MEMBER_NAME:
                return MEMBER_NAME_EDEFAULT == null ? memberName != null : !MEMBER_NAME_EDEFAULT.equals(memberName);
            case SysmlPackage.MEMBERSHIP__MEMBER_SHORT_NAME:
                return MEMBER_SHORT_NAME_EDEFAULT == null ? memberShortName != null : !MEMBER_SHORT_NAME_EDEFAULT.equals(memberShortName);
            case SysmlPackage.MEMBERSHIP__VISIBILITY:
                return visibility != VISIBILITY_EDEFAULT;
            case SysmlPackage.MEMBERSHIP__MEMBER_ELEMENT:
                return memberElement != null;
            case SysmlPackage.MEMBERSHIP__MEMBERSHIP_OWNING_NAMESPACE:
                return basicGetMembershipOwningNamespace() != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
        switch (operationID) {
            case SysmlPackage.MEMBERSHIP___IS_DISTINGUISHABLE_FROM__MEMBERSHIP:
                return isDistinguishableFrom((Membership)arguments.get(0));
        }
        return super.eInvoke(operationID, arguments);
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
        result.append(" (memberName: ");
        result.append(memberName);
        result.append(", memberShortName: ");
        result.append(memberShortName);
        result.append(", visibility: ");
        result.append(visibility);
        result.append(')');
        return result.toString();
    }

} //MembershipImpl
