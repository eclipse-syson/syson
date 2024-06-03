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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.syson.sysml.Annotation;
import org.eclipse.syson.sysml.Documentation;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.TextualRepresentation;
import org.eclipse.syson.sysml.helper.NameHelper;
import org.eclipse.syson.sysml.util.ElementUtil;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Element</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.ElementImpl#getAliasIds <em>Alias Ids</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ElementImpl#getDeclaredName <em>Declared Name</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ElementImpl#getDeclaredShortName <em>Declared Short Name</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ElementImpl#getElementId <em>Element Id</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ElementImpl#isIsImpliedIncluded <em>Is Implied Included</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ElementImpl#isIsLibraryElement <em>Is Library Element</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ElementImpl#getName <em>Name</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ElementImpl#getQualifiedName <em>Qualified Name</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ElementImpl#getShortName <em>Short Name</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ElementImpl#getDocumentation <em>Documentation</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ElementImpl#getOwnedAnnotation <em>Owned Annotation</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ElementImpl#getOwnedElement <em>Owned Element</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ElementImpl#getOwnedRelationship <em>Owned Relationship</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ElementImpl#getOwner <em>Owner</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ElementImpl#getOwningMembership <em>Owning Membership</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ElementImpl#getOwningNamespace <em>Owning Namespace</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ElementImpl#getOwningRelationship <em>Owning Relationship</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ElementImpl#getTextualRepresentation <em>Textual Representation</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class ElementImpl extends MinimalEObjectImpl.Container implements Element {
    /**
     * The cached value of the '{@link #getAliasIds() <em>Alias Ids</em>}' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getAliasIds()
     * @generated
     * @ordered
     */
    protected EList<String> aliasIds;

    /**
     * The default value of the '{@link #getDeclaredName() <em>Declared Name</em>}' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getDeclaredName()
     * @generated
     * @ordered
     */
    protected static final String DECLARED_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getDeclaredName() <em>Declared Name</em>}' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getDeclaredName()
     * @generated
     * @ordered
     */
    protected String declaredName = DECLARED_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getDeclaredShortName() <em>Declared Short Name</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getDeclaredShortName()
     * @generated
     * @ordered
     */
    protected static final String DECLARED_SHORT_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getDeclaredShortName() <em>Declared Short Name</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getDeclaredShortName()
     * @generated
     * @ordered
     */
    protected String declaredShortName = DECLARED_SHORT_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getElementId() <em>Element Id</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getElementId()
     * @generated
     * @ordered
     */
    protected static final String ELEMENT_ID_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getElementId() <em>Element Id</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getElementId()
     * @generated
     * @ordered
     */
    protected String elementId = ELEMENT_ID_EDEFAULT;

    /**
     * The default value of the '{@link #isIsImpliedIncluded() <em>Is Implied Included</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #isIsImpliedIncluded()
     * @generated
     * @ordered
     */
    protected static final boolean IS_IMPLIED_INCLUDED_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsImpliedIncluded() <em>Is Implied Included</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #isIsImpliedIncluded()
     * @generated
     * @ordered
     */
    protected boolean isImpliedIncluded = IS_IMPLIED_INCLUDED_EDEFAULT;

    /**
     * The default value of the '{@link #isIsLibraryElement() <em>Is Library Element</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #isIsLibraryElement()
     * @generated
     * @ordered
     */
    protected static final boolean IS_LIBRARY_ELEMENT_EDEFAULT = false;

    /**
     * The default value of the '{@link #getName() <em>Name</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @see #getName()
     * @generated
     * @ordered
     */
    protected static final String NAME_EDEFAULT = null;

    /**
     * The default value of the '{@link #getQualifiedName() <em>Qualified Name</em>}' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getQualifiedName()
     * @generated
     * @ordered
     */
    protected static final String QUALIFIED_NAME_EDEFAULT = null;

    /**
     * The default value of the '{@link #getShortName() <em>Short Name</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getShortName()
     * @generated
     * @ordered
     */
    protected static final String SHORT_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getOwnedRelationship() <em>Owned Relationship</em>}' containment reference list.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getOwnedRelationship()
     * @generated
     * @ordered
     */
    protected EList<Relationship> ownedRelationship;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected ElementImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getElement();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<String> getAliasIds() {
        if (this.aliasIds == null) {
            this.aliasIds = new EDataTypeUniqueEList<>(String.class, this, SysmlPackage.ELEMENT__ALIAS_IDS);
        }
        return this.aliasIds;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getDeclaredName() {
        return this.declaredName;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setDeclaredName(String newDeclaredName) {
        String oldDeclaredName = this.declaredName;
        this.declaredName = newDeclaredName;
        if (this.eNotificationRequired())
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.ELEMENT__DECLARED_NAME, oldDeclaredName, this.declaredName));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getDeclaredShortName() {
        return this.declaredShortName;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setDeclaredShortName(String newDeclaredShortName) {
        String oldDeclaredShortName = this.declaredShortName;
        this.declaredShortName = newDeclaredShortName;
        if (this.eNotificationRequired())
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.ELEMENT__DECLARED_SHORT_NAME, oldDeclaredShortName, this.declaredShortName));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Documentation> getDocumentation() {
        List<Documentation> documentation = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getElement_Documentation(), documentation.size(), documentation.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getElementId() {
        return this.elementId;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setElementId(String newElementId) {
        String oldElementId = this.elementId;
        this.elementId = newElementId;
        if (this.eNotificationRequired())
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.ELEMENT__ELEMENT_ID, oldElementId, this.elementId));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public boolean isIsImpliedIncluded() {
        return this.isImpliedIncluded;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setIsImpliedIncluded(boolean newIsImpliedIncluded) {
        boolean oldIsImpliedIncluded = this.isImpliedIncluded;
        this.isImpliedIncluded = newIsImpliedIncluded;
        if (this.eNotificationRequired())
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.ELEMENT__IS_IMPLIED_INCLUDED, oldIsImpliedIncluded, this.isImpliedIncluded));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public boolean isIsLibraryElement() {
        return ElementUtil.isFromLibrary(this, false);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public String getName() {
        // Return an effective name for this Element. By default this is the same as its declaredName.
        return this.effectiveName();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Annotation> getOwnedAnnotation() {
        List<Annotation> ownedAnnotation = new ArrayList<>();
        this.getOwnedRelationship().stream()
                .filter(Annotation.class::isInstance)
                .map(Annotation.class::cast)
                .filter(annotation -> this.equals(annotation.getAnnotatedElement()))
                .forEach(ownedAnnotation::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getElement_OwnedAnnotation(), ownedAnnotation.size(), ownedAnnotation.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Element> getOwnedElement() {
        List<Element> ownedElement = new ArrayList<>();
        this.getOwnedRelationship().stream()
                .flatMap(fm -> fm.getOwnedRelatedElement().stream())
                .forEach(ownedElement::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getElement_OwnedElement(), ownedElement.size(), ownedElement.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<Relationship> getOwnedRelationship() {
        if (this.ownedRelationship == null) {
            this.ownedRelationship = new EObjectContainmentWithInverseEList<>(Relationship.class, this, SysmlPackage.ELEMENT__OWNED_RELATIONSHIP,
                    SysmlPackage.RELATIONSHIP__OWNING_RELATED_ELEMENT);
        }
        return this.ownedRelationship;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Element getOwner() {
        Element owner = this.basicGetOwner();
        return owner != null && owner.eIsProxy() ? (Element) this.eResolveProxy((InternalEObject) owner) : owner;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public Element basicGetOwner() {
        Element basicGetOwner = null;
        Relationship owningRelationship = this.getOwningRelationship();
        if (owningRelationship != null) {
            basicGetOwner = owningRelationship.getOwningRelatedElement();
        }
        return basicGetOwner;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public OwningMembership getOwningMembership() {
        OwningMembership owningMembership = this.basicGetOwningMembership();
        return owningMembership != null && owningMembership.eIsProxy() ? (OwningMembership) this.eResolveProxy((InternalEObject) owningMembership) : owningMembership;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public OwningMembership basicGetOwningMembership() {
        OwningMembership basicGetOwningMembership = null;
        Relationship owningRelationship = this.getOwningRelationship();
        if (owningRelationship instanceof OwningMembership membership) {
            basicGetOwningMembership = membership;
        }
        return basicGetOwningMembership;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Namespace getOwningNamespace() {
        Namespace owningNamespace = this.basicGetOwningNamespace();
        return owningNamespace != null && owningNamespace.eIsProxy() ? (Namespace) this.eResolveProxy((InternalEObject) owningNamespace) : owningNamespace;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public Namespace basicGetOwningNamespace() {
        Namespace basicGetOwningNamespace = null;
        OwningMembership owningMembership = this.getOwningMembership();
        if (owningMembership != null) {
            basicGetOwningNamespace = owningMembership.getMembershipOwningNamespace();
        }
        return basicGetOwningNamespace;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Relationship getOwningRelationship() {
        if (this.eContainerFeatureID() != SysmlPackage.ELEMENT__OWNING_RELATIONSHIP)
            return null;
        return (Relationship) this.eInternalContainer();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetOwningRelationship(Relationship newOwningRelationship, NotificationChain msgs) {
        msgs = this.eBasicSetContainer((InternalEObject) newOwningRelationship, SysmlPackage.ELEMENT__OWNING_RELATIONSHIP, msgs);
        return msgs;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setOwningRelationship(Relationship newOwningRelationship) {
        if (newOwningRelationship != this.eInternalContainer() || (this.eContainerFeatureID() != SysmlPackage.ELEMENT__OWNING_RELATIONSHIP && newOwningRelationship != null)) {
            if (EcoreUtil.isAncestor(this, newOwningRelationship))
                throw new IllegalArgumentException("Recursive containment not allowed for " + this.toString());
            NotificationChain msgs = null;
            if (this.eInternalContainer() != null)
                msgs = this.eBasicRemoveFromContainer(msgs);
            if (newOwningRelationship != null)
                msgs = ((InternalEObject) newOwningRelationship).eInverseAdd(this, SysmlPackage.RELATIONSHIP__OWNED_RELATED_ELEMENT, Relationship.class, msgs);
            msgs = this.basicSetOwningRelationship(newOwningRelationship, msgs);
            if (msgs != null)
                msgs.dispatch();
        } else if (this.eNotificationRequired())
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.ELEMENT__OWNING_RELATIONSHIP, newOwningRelationship, newOwningRelationship));
    }

    /**
     * <!-- begin-user-doc --> The full ownership-qualified name of this Element, represented in a form that is valid
     * according to the KerML textual concrete syntax for qualified names (including use of unrestricted name notation
     * and escaped characters, as necessary). The qualifiedName is null if this Element has no owningNamespace or if
     * there is not a complete ownership chain of named Namespaces from a root Namespace to this Element. <!--
     * end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public String getQualifiedName() {
        String selfName = NameHelper.toPrintableName(this.getName());
        if (selfName.isBlank()) {
            return null;
        }

        StringBuilder qualifiedNameBuilder = new StringBuilder();
        Element container = this.getOwner();
        if (container != null && container instanceof Membership membership) {
            Element membershipContainer = membership.getOwner();
            if (membershipContainer != null) {
                String elementQN = membershipContainer.getQualifiedName();
                if (elementQN != null && !elementQN.isBlank()) {
                    qualifiedNameBuilder.append(elementQN);
                    qualifiedNameBuilder.append("::");
                }
            }
        } else if (container != null) {
            String elementQN = container.getQualifiedName();
            if (elementQN != null && !elementQN.isBlank()) {
                qualifiedNameBuilder.append(elementQN);
                qualifiedNameBuilder.append("::");
            }
        }

        qualifiedNameBuilder.append(selfName);
        return qualifiedNameBuilder.toString();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public String getShortName() {
        // Return an effective shortName for this Element. By default this is the same as its declaredShortName.
        return this.effectiveShortName();
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

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public String effectiveName() {
        return this.getDeclaredName();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public String effectiveShortName() {
        return this.getDeclaredShortName();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public String escapedName() {
        String escapedName = null;
        String name = this.getName();
        if (name == null) {
            escapedName = this.getShortName();
        } else {
            escapedName = this.getName();
        }
        if (escapedName != null && escapedName.contains("\\S+")) {
            escapedName = "'" + escapedName + "'";
        }
        return escapedName;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public Namespace libraryNamespace() {
        Relationship owningRelationship = this.getOwningRelationship();
        if (owningRelationship != null) {
            return owningRelationship.libraryNamespace();
        }
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
            case SysmlPackage.ELEMENT__OWNED_RELATIONSHIP:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) this.getOwnedRelationship()).basicAdd(otherEnd, msgs);
            case SysmlPackage.ELEMENT__OWNING_RELATIONSHIP:
                if (this.eInternalContainer() != null)
                    msgs = this.eBasicRemoveFromContainer(msgs);
                return this.basicSetOwningRelationship((Relationship) otherEnd, msgs);
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
            case SysmlPackage.ELEMENT__OWNED_RELATIONSHIP:
                return ((InternalEList<?>) this.getOwnedRelationship()).basicRemove(otherEnd, msgs);
            case SysmlPackage.ELEMENT__OWNING_RELATIONSHIP:
                return this.basicSetOwningRelationship(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
        switch (this.eContainerFeatureID()) {
            case SysmlPackage.ELEMENT__OWNING_RELATIONSHIP:
                return this.eInternalContainer().eInverseRemove(this, SysmlPackage.RELATIONSHIP__OWNED_RELATED_ELEMENT, Relationship.class, msgs);
        }
        return super.eBasicRemoveFromContainerFeature(msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.ELEMENT__ALIAS_IDS:
                return this.getAliasIds();
            case SysmlPackage.ELEMENT__DECLARED_NAME:
                return this.getDeclaredName();
            case SysmlPackage.ELEMENT__DECLARED_SHORT_NAME:
                return this.getDeclaredShortName();
            case SysmlPackage.ELEMENT__ELEMENT_ID:
                return this.getElementId();
            case SysmlPackage.ELEMENT__IS_IMPLIED_INCLUDED:
                return this.isIsImpliedIncluded();
            case SysmlPackage.ELEMENT__IS_LIBRARY_ELEMENT:
                return this.isIsLibraryElement();
            case SysmlPackage.ELEMENT__NAME:
                return this.getName();
            case SysmlPackage.ELEMENT__QUALIFIED_NAME:
                return this.getQualifiedName();
            case SysmlPackage.ELEMENT__SHORT_NAME:
                return this.getShortName();
            case SysmlPackage.ELEMENT__DOCUMENTATION:
                return this.getDocumentation();
            case SysmlPackage.ELEMENT__OWNED_ANNOTATION:
                return this.getOwnedAnnotation();
            case SysmlPackage.ELEMENT__OWNED_ELEMENT:
                return this.getOwnedElement();
            case SysmlPackage.ELEMENT__OWNED_RELATIONSHIP:
                return this.getOwnedRelationship();
            case SysmlPackage.ELEMENT__OWNER:
                if (resolve)
                    return this.getOwner();
                return this.basicGetOwner();
            case SysmlPackage.ELEMENT__OWNING_MEMBERSHIP:
                if (resolve)
                    return this.getOwningMembership();
                return this.basicGetOwningMembership();
            case SysmlPackage.ELEMENT__OWNING_NAMESPACE:
                if (resolve)
                    return this.getOwningNamespace();
                return this.basicGetOwningNamespace();
            case SysmlPackage.ELEMENT__OWNING_RELATIONSHIP:
                return this.getOwningRelationship();
            case SysmlPackage.ELEMENT__TEXTUAL_REPRESENTATION:
                return this.getTextualRepresentation();
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
            case SysmlPackage.ELEMENT__ALIAS_IDS:
                this.getAliasIds().clear();
                this.getAliasIds().addAll((Collection<? extends String>) newValue);
                return;
            case SysmlPackage.ELEMENT__DECLARED_NAME:
                this.setDeclaredName((String) newValue);
                return;
            case SysmlPackage.ELEMENT__DECLARED_SHORT_NAME:
                this.setDeclaredShortName((String) newValue);
                return;
            case SysmlPackage.ELEMENT__ELEMENT_ID:
                this.setElementId((String) newValue);
                return;
            case SysmlPackage.ELEMENT__IS_IMPLIED_INCLUDED:
                this.setIsImpliedIncluded((Boolean) newValue);
                return;
            case SysmlPackage.ELEMENT__OWNED_RELATIONSHIP:
                this.getOwnedRelationship().clear();
                this.getOwnedRelationship().addAll((Collection<? extends Relationship>) newValue);
                return;
            case SysmlPackage.ELEMENT__OWNING_RELATIONSHIP:
                this.setOwningRelationship((Relationship) newValue);
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
            case SysmlPackage.ELEMENT__ALIAS_IDS:
                this.getAliasIds().clear();
                return;
            case SysmlPackage.ELEMENT__DECLARED_NAME:
                this.setDeclaredName(DECLARED_NAME_EDEFAULT);
                return;
            case SysmlPackage.ELEMENT__DECLARED_SHORT_NAME:
                this.setDeclaredShortName(DECLARED_SHORT_NAME_EDEFAULT);
                return;
            case SysmlPackage.ELEMENT__ELEMENT_ID:
                this.setElementId(ELEMENT_ID_EDEFAULT);
                return;
            case SysmlPackage.ELEMENT__IS_IMPLIED_INCLUDED:
                this.setIsImpliedIncluded(IS_IMPLIED_INCLUDED_EDEFAULT);
                return;
            case SysmlPackage.ELEMENT__OWNED_RELATIONSHIP:
                this.getOwnedRelationship().clear();
                return;
            case SysmlPackage.ELEMENT__OWNING_RELATIONSHIP:
                this.setOwningRelationship((Relationship) null);
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
            case SysmlPackage.ELEMENT__ALIAS_IDS:
                return this.aliasIds != null && !this.aliasIds.isEmpty();
            case SysmlPackage.ELEMENT__DECLARED_NAME:
                return DECLARED_NAME_EDEFAULT == null ? this.declaredName != null : !DECLARED_NAME_EDEFAULT.equals(this.declaredName);
            case SysmlPackage.ELEMENT__DECLARED_SHORT_NAME:
                return DECLARED_SHORT_NAME_EDEFAULT == null ? this.declaredShortName != null : !DECLARED_SHORT_NAME_EDEFAULT.equals(this.declaredShortName);
            case SysmlPackage.ELEMENT__ELEMENT_ID:
                return ELEMENT_ID_EDEFAULT == null ? this.elementId != null : !ELEMENT_ID_EDEFAULT.equals(this.elementId);
            case SysmlPackage.ELEMENT__IS_IMPLIED_INCLUDED:
                return this.isImpliedIncluded != IS_IMPLIED_INCLUDED_EDEFAULT;
            case SysmlPackage.ELEMENT__IS_LIBRARY_ELEMENT:
                return this.isIsLibraryElement() != IS_LIBRARY_ELEMENT_EDEFAULT;
            case SysmlPackage.ELEMENT__NAME:
                return NAME_EDEFAULT == null ? this.getName() != null : !NAME_EDEFAULT.equals(this.getName());
            case SysmlPackage.ELEMENT__QUALIFIED_NAME:
                return QUALIFIED_NAME_EDEFAULT == null ? this.getQualifiedName() != null : !QUALIFIED_NAME_EDEFAULT.equals(this.getQualifiedName());
            case SysmlPackage.ELEMENT__SHORT_NAME:
                return SHORT_NAME_EDEFAULT == null ? this.getShortName() != null : !SHORT_NAME_EDEFAULT.equals(this.getShortName());
            case SysmlPackage.ELEMENT__DOCUMENTATION:
                return !this.getDocumentation().isEmpty();
            case SysmlPackage.ELEMENT__OWNED_ANNOTATION:
                return !this.getOwnedAnnotation().isEmpty();
            case SysmlPackage.ELEMENT__OWNED_ELEMENT:
                return !this.getOwnedElement().isEmpty();
            case SysmlPackage.ELEMENT__OWNED_RELATIONSHIP:
                return this.ownedRelationship != null && !this.ownedRelationship.isEmpty();
            case SysmlPackage.ELEMENT__OWNER:
                return this.basicGetOwner() != null;
            case SysmlPackage.ELEMENT__OWNING_MEMBERSHIP:
                return this.basicGetOwningMembership() != null;
            case SysmlPackage.ELEMENT__OWNING_NAMESPACE:
                return this.basicGetOwningNamespace() != null;
            case SysmlPackage.ELEMENT__OWNING_RELATIONSHIP:
                return this.getOwningRelationship() != null;
            case SysmlPackage.ELEMENT__TEXTUAL_REPRESENTATION:
                return !this.getTextualRepresentation().isEmpty();
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
            case SysmlPackage.ELEMENT___EFFECTIVE_NAME:
                return this.effectiveName();
            case SysmlPackage.ELEMENT___EFFECTIVE_SHORT_NAME:
                return this.effectiveShortName();
            case SysmlPackage.ELEMENT___ESCAPED_NAME:
                return this.escapedName();
            case SysmlPackage.ELEMENT___LIBRARY_NAMESPACE:
                return this.libraryNamespace();
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
        if (this.eIsProxy())
            return super.toString();

        StringBuilder result = new StringBuilder(super.toString());
        result.append(" (aliasIds: ");
        result.append(this.aliasIds);
        result.append(", declaredName: ");
        result.append(this.declaredName);
        result.append(", declaredShortName: ");
        result.append(this.declaredShortName);
        result.append(", elementId: ");
        result.append(this.elementId);
        result.append(", isImpliedIncluded: ");
        result.append(this.isImpliedIncluded);
        result.append(')');
        return result.toString();
    }

} // ElementImpl
