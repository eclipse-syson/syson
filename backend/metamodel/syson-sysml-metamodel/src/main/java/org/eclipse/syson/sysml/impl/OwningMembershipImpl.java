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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.TextualRepresentation;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Owning Membership</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.OwningMembershipImpl#getOwnedMemberElementId <em>Owned Member Element
 * Id</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.OwningMembershipImpl#getOwnedMemberName <em>Owned Member Name</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.OwningMembershipImpl#getOwnedMemberShortName <em>Owned Member Short
 * Name</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.OwningMembershipImpl#getOwnedMemberElement <em>Owned Member
 * Element</em>}</li>
 * </ul>
 *
 * @generated
 */
public class OwningMembershipImpl extends MembershipImpl implements OwningMembership {
    /**
     * The default value of the '{@link #getOwnedMemberElementId() <em>Owned Member Element Id</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getOwnedMemberElementId()
     * @generated
     * @ordered
     */
    protected static final String OWNED_MEMBER_ELEMENT_ID_EDEFAULT = null;

    /**
     * The default value of the '{@link #getOwnedMemberName() <em>Owned Member Name</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getOwnedMemberName()
     * @generated
     * @ordered
     */
    protected static final String OWNED_MEMBER_NAME_EDEFAULT = null;

    /**
     * The default value of the '{@link #getOwnedMemberShortName() <em>Owned Member Short Name</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getOwnedMemberShortName()
     * @generated
     * @ordered
     */
    protected static final String OWNED_MEMBER_SHORT_NAME_EDEFAULT = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected OwningMembershipImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getOwningMembership();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Element getOwnedMemberElement() {
        Element ownedMemberElement = this.basicGetOwnedMemberElement();
        return ownedMemberElement != null && ownedMemberElement.eIsProxy() ? (Element) this.eResolveProxy((InternalEObject) ownedMemberElement) : ownedMemberElement;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public Element basicGetOwnedMemberElement() {
        EList<Element> ownedMembers = this.getOwnedRelatedElement();
        if (ownedMembers != null && !ownedMembers.isEmpty()) {
            return ownedMembers.get(0);
        }
        return null;
    }
    
    /**
     * @generated NOT
     */
    @Override
    public Element getMemberElement() {
        return getOwnedMemberElement();
    }
    

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public String getOwnedMemberElementId() {
        Element ownedMemberElement = this.getOwnedMemberElement();
        if (ownedMemberElement != null) {
            return ownedMemberElement.getElementId();
        }
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public String getOwnedMemberName() {
        Element ownedMemberElement = this.getOwnedMemberElement();
        if (ownedMemberElement != null) {
            return ownedMemberElement.getName();
        }
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public String getOwnedMemberShortName() {
        Element ownedMemberElement = this.getOwnedMemberElement();
        if (ownedMemberElement != null) {
            return ownedMemberElement.getShortName();
        }
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public String getMemberName() {
        return this.getOwnedMemberName();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public String getMemberShortName() {
        return this.getOwnedMemberShortName();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.OWNING_MEMBERSHIP__OWNED_MEMBER_ELEMENT_ID:
                return this.getOwnedMemberElementId();
            case SysmlPackage.OWNING_MEMBERSHIP__OWNED_MEMBER_NAME:
                return this.getOwnedMemberName();
            case SysmlPackage.OWNING_MEMBERSHIP__OWNED_MEMBER_SHORT_NAME:
                return this.getOwnedMemberShortName();
            case SysmlPackage.OWNING_MEMBERSHIP__OWNED_MEMBER_ELEMENT:
                if (resolve)
                    return this.getOwnedMemberElement();
                return this.basicGetOwnedMemberElement();
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
            case SysmlPackage.OWNING_MEMBERSHIP__OWNED_MEMBER_ELEMENT_ID:
                return OWNED_MEMBER_ELEMENT_ID_EDEFAULT == null ? this.getOwnedMemberElementId() != null : !OWNED_MEMBER_ELEMENT_ID_EDEFAULT.equals(this.getOwnedMemberElementId());
            case SysmlPackage.OWNING_MEMBERSHIP__OWNED_MEMBER_NAME:
                return OWNED_MEMBER_NAME_EDEFAULT == null ? this.getOwnedMemberName() != null : !OWNED_MEMBER_NAME_EDEFAULT.equals(this.getOwnedMemberName());
            case SysmlPackage.OWNING_MEMBERSHIP__OWNED_MEMBER_SHORT_NAME:
                return OWNED_MEMBER_SHORT_NAME_EDEFAULT == null ? this.getOwnedMemberShortName() != null : !OWNED_MEMBER_SHORT_NAME_EDEFAULT.equals(this.getOwnedMemberShortName());
            case SysmlPackage.OWNING_MEMBERSHIP__OWNED_MEMBER_ELEMENT:
                return this.basicGetOwnedMemberElement() != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<TextualRepresentation> getTextualRepresentation() {
        List<TextualRepresentation> textualRepresentation = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getElement_TextualRepresentation(), textualRepresentation.size(), textualRepresentation.toArray());
    }

} // OwningMembershipImpl
