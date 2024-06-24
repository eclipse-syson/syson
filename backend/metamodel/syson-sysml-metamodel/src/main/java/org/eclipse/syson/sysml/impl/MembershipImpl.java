/**
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
 */
package org.eclipse.syson.sysml.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectEList;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.VisibilityKind;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Membership</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.MembershipImpl#getMemberElementId <em>Member Element Id</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.MembershipImpl#getMemberName <em>Member Name</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.MembershipImpl#getMemberShortName <em>Member Short Name</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.MembershipImpl#getVisibility <em>Visibility</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.MembershipImpl#getMemberElement <em>Member Element</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.MembershipImpl#getMembershipOwningNamespace <em>Membership Owning
 * Namespace</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MembershipImpl extends RelationshipImpl implements Membership {
    /**
     * The default value of the '{@link #getMemberElementId() <em>Member Element Id</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getMemberElementId()
     * @generated
     * @ordered
     */
    protected static final String MEMBER_ELEMENT_ID_EDEFAULT = null;

    /**
     * The default value of the '{@link #getMemberName() <em>Member Name</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getMemberName()
     * @generated
     * @ordered
     */
    protected static final String MEMBER_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getMemberName() <em>Member Name</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getMemberName()
     * @generated
     * @ordered
     */
    protected String memberName = MEMBER_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getMemberShortName() <em>Member Short Name</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getMemberShortName()
     * @generated
     * @ordered
     */
    protected static final String MEMBER_SHORT_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getMemberShortName() <em>Member Short Name</em>}' attribute. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @see #getMemberShortName()
     * @generated
     * @ordered
     */
    protected String memberShortName = MEMBER_SHORT_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getVisibility() <em>Visibility</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getVisibility()
     * @generated
     * @ordered
     */
    protected static final VisibilityKind VISIBILITY_EDEFAULT = VisibilityKind.PUBLIC;

    /**
     * The cached value of the '{@link #getVisibility() <em>Visibility</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getVisibility()
     * @generated
     * @ordered
     */
    protected VisibilityKind visibility = VISIBILITY_EDEFAULT;

    /**
     * The cached value of the '{@link #getMemberElement() <em>Member Element</em>}' reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getMemberElement()
     * @generated
     * @ordered
     */
    protected Element memberElement;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected MembershipImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getMembership();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Element getMemberElement() {
        if (this.memberElement != null && this.memberElement.eIsProxy()) {
            InternalEObject oldMemberElement = (InternalEObject) this.memberElement;
            this.memberElement = (Element) this.eResolveProxy(oldMemberElement);
            if (this.memberElement != oldMemberElement) {
                if (this.eNotificationRequired()) {
                    this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.MEMBERSHIP__MEMBER_ELEMENT, oldMemberElement, this.memberElement));
                }
            }
        }
        return this.memberElement;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Element basicGetMemberElement() {
        return this.memberElement;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setMemberElement(Element newMemberElement) {
        Element oldMemberElement = this.memberElement;
        this.memberElement = newMemberElement;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.MEMBERSHIP__MEMBER_ELEMENT, oldMemberElement, this.memberElement));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public String getMemberElementId() {
        Element element = this.getMemberElement();
        if (element != null) {
            return element.getElementId();
        }
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getMemberName() {
        return this.memberName;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setMemberName(String newMemberName) {
        String oldMemberName = this.memberName;
        this.memberName = newMemberName;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.MEMBERSHIP__MEMBER_NAME, oldMemberName, this.memberName));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Namespace getMembershipOwningNamespace() {
        Namespace membershipOwningNamespace = this.basicGetMembershipOwningNamespace();
        return membershipOwningNamespace != null && membershipOwningNamespace.eIsProxy() ? (Namespace) this.eResolveProxy((InternalEObject) membershipOwningNamespace) : membershipOwningNamespace;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public Namespace basicGetMembershipOwningNamespace() {
        EObject container = this.eContainer();
        if (container instanceof Namespace ns) {
            return ns;
        }
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getMemberShortName() {
        return this.memberShortName;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setMemberShortName(String newMemberShortName) {
        String oldMemberShortName = this.memberShortName;
        this.memberShortName = newMemberShortName;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.MEMBERSHIP__MEMBER_SHORT_NAME, oldMemberShortName, this.memberShortName));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public VisibilityKind getVisibility() {
        return this.visibility;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setVisibility(VisibilityKind newVisibility) {
        VisibilityKind oldVisibility = this.visibility;
        this.visibility = newVisibility == null ? VISIBILITY_EDEFAULT : newVisibility;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.MEMBERSHIP__VISIBILITY, oldVisibility, this.visibility));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public boolean isDistinguishableFrom(Membership other) {
        return false;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.MEMBERSHIP__MEMBER_ELEMENT_ID:
                return this.getMemberElementId();
            case SysmlPackage.MEMBERSHIP__MEMBER_NAME:
                return this.getMemberName();
            case SysmlPackage.MEMBERSHIP__MEMBER_SHORT_NAME:
                return this.getMemberShortName();
            case SysmlPackage.MEMBERSHIP__VISIBILITY:
                return this.getVisibility();
            case SysmlPackage.MEMBERSHIP__MEMBER_ELEMENT:
                if (resolve) {
                    return this.getMemberElement();
                }
                return this.basicGetMemberElement();
            case SysmlPackage.MEMBERSHIP__MEMBERSHIP_OWNING_NAMESPACE:
                if (resolve) {
                    return this.getMembershipOwningNamespace();
                }
                return this.basicGetMembershipOwningNamespace();
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
            case SysmlPackage.MEMBERSHIP__MEMBER_NAME:
                this.setMemberName((String) newValue);
                return;
            case SysmlPackage.MEMBERSHIP__MEMBER_SHORT_NAME:
                this.setMemberShortName((String) newValue);
                return;
            case SysmlPackage.MEMBERSHIP__VISIBILITY:
                this.setVisibility((VisibilityKind) newValue);
                return;
            case SysmlPackage.MEMBERSHIP__MEMBER_ELEMENT:
                this.setMemberElement((Element) newValue);
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
            case SysmlPackage.MEMBERSHIP__MEMBER_NAME:
                this.setMemberName(MEMBER_NAME_EDEFAULT);
                return;
            case SysmlPackage.MEMBERSHIP__MEMBER_SHORT_NAME:
                this.setMemberShortName(MEMBER_SHORT_NAME_EDEFAULT);
                return;
            case SysmlPackage.MEMBERSHIP__VISIBILITY:
                this.setVisibility(VISIBILITY_EDEFAULT);
                return;
            case SysmlPackage.MEMBERSHIP__MEMBER_ELEMENT:
                this.setMemberElement((Element) null);
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
            case SysmlPackage.MEMBERSHIP__MEMBER_ELEMENT_ID:
                return MEMBER_ELEMENT_ID_EDEFAULT == null ? this.getMemberElementId() != null : !MEMBER_ELEMENT_ID_EDEFAULT.equals(this.getMemberElementId());
            case SysmlPackage.MEMBERSHIP__MEMBER_NAME:
                return MEMBER_NAME_EDEFAULT == null ? this.memberName != null : !MEMBER_NAME_EDEFAULT.equals(this.memberName);
            case SysmlPackage.MEMBERSHIP__MEMBER_SHORT_NAME:
                return MEMBER_SHORT_NAME_EDEFAULT == null ? this.memberShortName != null : !MEMBER_SHORT_NAME_EDEFAULT.equals(this.memberShortName);
            case SysmlPackage.MEMBERSHIP__VISIBILITY:
                return this.visibility != VISIBILITY_EDEFAULT;
            case SysmlPackage.MEMBERSHIP__MEMBER_ELEMENT:
                return this.memberElement != null;
            case SysmlPackage.MEMBERSHIP__MEMBERSHIP_OWNING_NAMESPACE:
                return this.basicGetMembershipOwningNamespace() != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
        switch (operationID) {
            case SysmlPackage.MEMBERSHIP___IS_DISTINGUISHABLE_FROM__MEMBERSHIP:
                return this.isDistinguishableFrom((Membership) arguments.get(0));
        }
        return super.eInvoke(operationID, arguments);
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
        result.append(" (memberName: ");
        result.append(this.memberName);
        result.append(", memberShortName: ");
        result.append(this.memberShortName);
        result.append(", visibility: ");
        result.append(this.visibility);
        result.append(')');
        return result.toString();
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Element> getTarget() {
        EList<Element> targets = new EObjectEList.Unsettable<>(Element.class, this, SysmlPackage.MEMBERSHIP__TARGET) {
            @Override
            public boolean addAll(Collection<? extends Element> collection) {
                if (collection != null) {
                    Iterator<? extends Element> iterator = collection.iterator();
                    if (iterator.hasNext()) {
                        Element next = iterator.next();
                        if (next instanceof Type type) {
                            MembershipImpl.this.setMemberElement(type);
                            return true;
                        }
                    }
                }
                return false;
            }

            @Override
            protected void dispatchNotification(Notification notification) {
            }
        };
        Element memberElement = this.getMemberElement();
        if (memberElement != null) {
            targets.add(memberElement);
        }
        return targets;
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Element> getSource() {
        EList<Element> sources = new EObjectEList.Unsettable<>(Element.class, this, SysmlPackage.MEMBERSHIP__SOURCE) {
            @Override
            public boolean addAll(Collection<? extends Element> collection) {
                return false;
            }

            @Override
            protected void dispatchNotification(Notification notification) {
            }
        };
        Namespace membershipOwningNamespace = this.getMembershipOwningNamespace();
        if (membershipOwningNamespace != null) {
            sources.add(membershipOwningNamespace);
        }
        return sources;
    }

} // MembershipImpl
