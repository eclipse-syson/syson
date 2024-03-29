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
import java.util.UUID;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.BasicInternalEList;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.syson.sysml.Annotation;
import org.eclipse.syson.sysml.Documentation;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.LibraryPackage;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.MetadataUsage;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.TextualRepresentation;
import org.eclipse.syson.sysml.util.ElementUtil;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.impl.ElementImpl#getAliasIds <em>Alias Ids</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.ElementImpl#getDeclaredName <em>Declared Name</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.ElementImpl#getDeclaredShortName <em>Declared Short Name</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.ElementImpl#getElementId <em>Element Id</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.ElementImpl#isIsImpliedIncluded <em>Is Implied Included</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.ElementImpl#isIsLibraryElement <em>Is Library Element</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.ElementImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.ElementImpl#getQualifiedName <em>Qualified Name</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.ElementImpl#getShortName <em>Short Name</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.ElementImpl#getDocumentation <em>Documentation</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.ElementImpl#getOwnedAnnotation <em>Owned Annotation</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.ElementImpl#getOwnedElement <em>Owned Element</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.ElementImpl#getOwnedRelationship <em>Owned Relationship</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.ElementImpl#getOwner <em>Owner</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.ElementImpl#getOwningMembership <em>Owning Membership</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.ElementImpl#getOwningNamespace <em>Owning Namespace</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.ElementImpl#getOwningRelationship <em>Owning Relationship</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.ElementImpl#getTextualRepresentation <em>Textual Representation</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class ElementImpl extends MinimalEObjectImpl.Container implements Element {
    /**
     * The cached value of the '{@link #getAliasIds() <em>Alias Ids</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAliasIds()
     * @generated
     * @ordered
     */
    protected EList<String> aliasIds;

    /**
     * The default value of the '{@link #getDeclaredName() <em>Declared Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDeclaredName()
     * @generated
     * @ordered
     */
    protected static final String DECLARED_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getDeclaredName() <em>Declared Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDeclaredName()
     * @generated
     * @ordered
     */
    protected String declaredName = DECLARED_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getDeclaredShortName() <em>Declared Short Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDeclaredShortName()
     * @generated
     * @ordered
     */
    protected static final String DECLARED_SHORT_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getDeclaredShortName() <em>Declared Short Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDeclaredShortName()
     * @generated
     * @ordered
     */
    protected String declaredShortName = DECLARED_SHORT_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getElementId() <em>Element Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getElementId()
     * @generated
     * @ordered
     */
    protected static final String ELEMENT_ID_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getElementId() <em>Element Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getElementId()
     * @generated
     * @ordered
     */
    protected String elementId = ELEMENT_ID_EDEFAULT;

    /**
     * The default value of the '{@link #isIsImpliedIncluded() <em>Is Implied Included</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsImpliedIncluded()
     * @generated
     * @ordered
     */
    protected static final boolean IS_IMPLIED_INCLUDED_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsImpliedIncluded() <em>Is Implied Included</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsImpliedIncluded()
     * @generated
     * @ordered
     */
    protected boolean isImpliedIncluded = IS_IMPLIED_INCLUDED_EDEFAULT;

    /**
     * The default value of the '{@link #isIsLibraryElement() <em>Is Library Element</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsLibraryElement()
     * @generated
     * @ordered
     */
    protected static final boolean IS_LIBRARY_ELEMENT_EDEFAULT = false;

    /**
     * The default value of the '{@link #getName() <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getName()
     * @generated
     * @ordered
     */
    protected static final String NAME_EDEFAULT = null;

    /**
     * The default value of the '{@link #getQualifiedName() <em>Qualified Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getQualifiedName()
     * @generated
     * @ordered
     */
    protected static final String QUALIFIED_NAME_EDEFAULT = null;

    /**
     * The default value of the '{@link #getShortName() <em>Short Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getShortName()
     * @generated
     * @ordered
     */
    protected static final String SHORT_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getOwnedRelationship() <em>Owned Relationship</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOwnedRelationship()
     * @generated
     * @ordered
     */
    protected EList<Relationship> ownedRelationship;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ElementImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getElement();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<String> getAliasIds() {
        if (aliasIds == null) {
            aliasIds = new EDataTypeUniqueEList<String>(String.class, this, SysmlPackage.ELEMENT__ALIAS_IDS);
        }
        return aliasIds;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getDeclaredName() {
        return declaredName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setDeclaredName(String newDeclaredName) {
        String oldDeclaredName = declaredName;
        declaredName = newDeclaredName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.ELEMENT__DECLARED_NAME, oldDeclaredName, declaredName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getDeclaredShortName() {
        return declaredShortName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setDeclaredShortName(String newDeclaredShortName) {
        String oldDeclaredShortName = declaredShortName;
        declaredShortName = newDeclaredShortName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.ELEMENT__DECLARED_SHORT_NAME, oldDeclaredShortName, declaredShortName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<Documentation> getDocumentation() {
        List<Documentation> documentation = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getElement_Documentation(), documentation.size(), documentation.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getElementId() {
        return elementId;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setElementId(String newElementId) {
        String oldElementId = elementId;
        elementId = newElementId;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.ELEMENT__ELEMENT_ID, oldElementId, elementId));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean isIsImpliedIncluded() {
        return isImpliedIncluded;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setIsImpliedIncluded(boolean newIsImpliedIncluded) {
        boolean oldIsImpliedIncluded = isImpliedIncluded;
        isImpliedIncluded = newIsImpliedIncluded;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.ELEMENT__IS_IMPLIED_INCLUDED, oldIsImpliedIncluded, isImpliedIncluded));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public boolean isIsLibraryElement() {
        return ElementUtil.isFromLibrary(this, false);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public String getName() {
        return this.getDeclaredName();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
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
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
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
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<Relationship> getOwnedRelationship() {
        if (ownedRelationship == null) {
            ownedRelationship = new EObjectContainmentWithInverseEList<Relationship>(Relationship.class, this, SysmlPackage.ELEMENT__OWNED_RELATIONSHIP, SysmlPackage.RELATIONSHIP__OWNING_RELATED_ELEMENT);
        }
        return ownedRelationship;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Element getOwner() {
        Element owner = basicGetOwner();
        return owner != null && owner.eIsProxy() ? (Element)eResolveProxy((InternalEObject)owner) : owner;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
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
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public OwningMembership getOwningMembership() {
        OwningMembership owningMembership = basicGetOwningMembership();
        return owningMembership != null && owningMembership.eIsProxy() ? (OwningMembership)eResolveProxy((InternalEObject)owningMembership) : owningMembership;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
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
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Namespace getOwningNamespace() {
        Namespace owningNamespace = basicGetOwningNamespace();
        return owningNamespace != null && owningNamespace.eIsProxy() ? (Namespace)eResolveProxy((InternalEObject)owningNamespace) : owningNamespace;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
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
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Relationship getOwningRelationship() {
        if (eContainerFeatureID() != SysmlPackage.ELEMENT__OWNING_RELATIONSHIP) return null;
        return (Relationship)eInternalContainer();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetOwningRelationship(Relationship newOwningRelationship, NotificationChain msgs) {
        msgs = eBasicSetContainer((InternalEObject)newOwningRelationship, SysmlPackage.ELEMENT__OWNING_RELATIONSHIP, msgs);
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setOwningRelationship(Relationship newOwningRelationship) {
        if (newOwningRelationship != eInternalContainer() || (eContainerFeatureID() != SysmlPackage.ELEMENT__OWNING_RELATIONSHIP && newOwningRelationship != null)) {
            if (EcoreUtil.isAncestor(this, newOwningRelationship))
                throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
            NotificationChain msgs = null;
            if (eInternalContainer() != null)
                msgs = eBasicRemoveFromContainer(msgs);
            if (newOwningRelationship != null)
                msgs = ((InternalEObject)newOwningRelationship).eInverseAdd(this, SysmlPackage.RELATIONSHIP__OWNED_RELATED_ELEMENT, Relationship.class, msgs);
            msgs = basicSetOwningRelationship(newOwningRelationship, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.ELEMENT__OWNING_RELATIONSHIP, newOwningRelationship, newOwningRelationship));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public String getQualifiedName() {
        boolean qualifiedNameContainsNull = false;
        StringBuilder qualifiedNameBuilder = new StringBuilder();
        EObject container = this.eContainer();
        if (container instanceof Membership membership) {
            EObject membershipContainer = membership.eContainer();
            if (membershipContainer instanceof Element element) {
                String elementQN = element.getQualifiedName();
                if (elementQN == null) {
                    qualifiedNameContainsNull = true;
                } else {
                    qualifiedNameBuilder.append(elementQN);
                    qualifiedNameBuilder.append("::");
                }
            }
        } else if (container instanceof Element element) {
            String elementQN = element.getQualifiedName();
            if (elementQN == null) {
                qualifiedNameContainsNull = true;
            } else {
                qualifiedNameBuilder.append(elementQN);
                qualifiedNameBuilder.append("::");
            }
        }
        String name = this.getName();
        if (name == null || name.isBlank()) {
            qualifiedNameContainsNull = true;
        } else if (name.contains("\s")) {
            name = "'" + name + "'";
        }
        qualifiedNameBuilder.append(name);
        if (qualifiedNameContainsNull) {
            return null;
        }
        return qualifiedNameBuilder.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public String getShortName() {
        return this.getDeclaredShortName();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<TextualRepresentation> getTextualRepresentation() {
        List<TextualRepresentation> textualRepresentation = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getElement_TextualRepresentation(), textualRepresentation.size(), textualRepresentation.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public String effectiveName() {
        return this.getDeclaredName();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public String effectiveShortName() {
        return this.getDeclaredShortName();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
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
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
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
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case SysmlPackage.ELEMENT__OWNED_RELATIONSHIP:
                return ((InternalEList<InternalEObject>)(InternalEList<?>)getOwnedRelationship()).basicAdd(otherEnd, msgs);
            case SysmlPackage.ELEMENT__OWNING_RELATIONSHIP:
                if (eInternalContainer() != null)
                    msgs = eBasicRemoveFromContainer(msgs);
                return basicSetOwningRelationship((Relationship)otherEnd, msgs);
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
            case SysmlPackage.ELEMENT__OWNED_RELATIONSHIP:
                return ((InternalEList<?>)getOwnedRelationship()).basicRemove(otherEnd, msgs);
            case SysmlPackage.ELEMENT__OWNING_RELATIONSHIP:
                return basicSetOwningRelationship(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
        switch (eContainerFeatureID()) {
            case SysmlPackage.ELEMENT__OWNING_RELATIONSHIP:
                return eInternalContainer().eInverseRemove(this, SysmlPackage.RELATIONSHIP__OWNED_RELATED_ELEMENT, Relationship.class, msgs);
        }
        return super.eBasicRemoveFromContainerFeature(msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.ELEMENT__ALIAS_IDS:
                return getAliasIds();
            case SysmlPackage.ELEMENT__DECLARED_NAME:
                return getDeclaredName();
            case SysmlPackage.ELEMENT__DECLARED_SHORT_NAME:
                return getDeclaredShortName();
            case SysmlPackage.ELEMENT__ELEMENT_ID:
                return getElementId();
            case SysmlPackage.ELEMENT__IS_IMPLIED_INCLUDED:
                return isIsImpliedIncluded();
            case SysmlPackage.ELEMENT__IS_LIBRARY_ELEMENT:
                return isIsLibraryElement();
            case SysmlPackage.ELEMENT__NAME:
                return getName();
            case SysmlPackage.ELEMENT__QUALIFIED_NAME:
                return getQualifiedName();
            case SysmlPackage.ELEMENT__SHORT_NAME:
                return getShortName();
            case SysmlPackage.ELEMENT__DOCUMENTATION:
                return getDocumentation();
            case SysmlPackage.ELEMENT__OWNED_ANNOTATION:
                return getOwnedAnnotation();
            case SysmlPackage.ELEMENT__OWNED_ELEMENT:
                return getOwnedElement();
            case SysmlPackage.ELEMENT__OWNED_RELATIONSHIP:
                return getOwnedRelationship();
            case SysmlPackage.ELEMENT__OWNER:
                if (resolve) return getOwner();
                return basicGetOwner();
            case SysmlPackage.ELEMENT__OWNING_MEMBERSHIP:
                if (resolve) return getOwningMembership();
                return basicGetOwningMembership();
            case SysmlPackage.ELEMENT__OWNING_NAMESPACE:
                if (resolve) return getOwningNamespace();
                return basicGetOwningNamespace();
            case SysmlPackage.ELEMENT__OWNING_RELATIONSHIP:
                return getOwningRelationship();
            case SysmlPackage.ELEMENT__TEXTUAL_REPRESENTATION:
                return getTextualRepresentation();
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
            case SysmlPackage.ELEMENT__ALIAS_IDS:
                getAliasIds().clear();
                getAliasIds().addAll((Collection<? extends String>)newValue);
                return;
            case SysmlPackage.ELEMENT__DECLARED_NAME:
                setDeclaredName((String)newValue);
                return;
            case SysmlPackage.ELEMENT__DECLARED_SHORT_NAME:
                setDeclaredShortName((String)newValue);
                return;
            case SysmlPackage.ELEMENT__ELEMENT_ID:
                setElementId((String)newValue);
                return;
            case SysmlPackage.ELEMENT__IS_IMPLIED_INCLUDED:
                setIsImpliedIncluded((Boolean)newValue);
                return;
            case SysmlPackage.ELEMENT__OWNED_RELATIONSHIP:
                getOwnedRelationship().clear();
                getOwnedRelationship().addAll((Collection<? extends Relationship>)newValue);
                return;
            case SysmlPackage.ELEMENT__OWNING_RELATIONSHIP:
                setOwningRelationship((Relationship)newValue);
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
            case SysmlPackage.ELEMENT__ALIAS_IDS:
                getAliasIds().clear();
                return;
            case SysmlPackage.ELEMENT__DECLARED_NAME:
                setDeclaredName(DECLARED_NAME_EDEFAULT);
                return;
            case SysmlPackage.ELEMENT__DECLARED_SHORT_NAME:
                setDeclaredShortName(DECLARED_SHORT_NAME_EDEFAULT);
                return;
            case SysmlPackage.ELEMENT__ELEMENT_ID:
                setElementId(ELEMENT_ID_EDEFAULT);
                return;
            case SysmlPackage.ELEMENT__IS_IMPLIED_INCLUDED:
                setIsImpliedIncluded(IS_IMPLIED_INCLUDED_EDEFAULT);
                return;
            case SysmlPackage.ELEMENT__OWNED_RELATIONSHIP:
                getOwnedRelationship().clear();
                return;
            case SysmlPackage.ELEMENT__OWNING_RELATIONSHIP:
                setOwningRelationship((Relationship)null);
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
            case SysmlPackage.ELEMENT__ALIAS_IDS:
                return aliasIds != null && !aliasIds.isEmpty();
            case SysmlPackage.ELEMENT__DECLARED_NAME:
                return DECLARED_NAME_EDEFAULT == null ? declaredName != null : !DECLARED_NAME_EDEFAULT.equals(declaredName);
            case SysmlPackage.ELEMENT__DECLARED_SHORT_NAME:
                return DECLARED_SHORT_NAME_EDEFAULT == null ? declaredShortName != null : !DECLARED_SHORT_NAME_EDEFAULT.equals(declaredShortName);
            case SysmlPackage.ELEMENT__ELEMENT_ID:
                return ELEMENT_ID_EDEFAULT == null ? elementId != null : !ELEMENT_ID_EDEFAULT.equals(elementId);
            case SysmlPackage.ELEMENT__IS_IMPLIED_INCLUDED:
                return isImpliedIncluded != IS_IMPLIED_INCLUDED_EDEFAULT;
            case SysmlPackage.ELEMENT__IS_LIBRARY_ELEMENT:
                return isIsLibraryElement() != IS_LIBRARY_ELEMENT_EDEFAULT;
            case SysmlPackage.ELEMENT__NAME:
                return NAME_EDEFAULT == null ? getName() != null : !NAME_EDEFAULT.equals(getName());
            case SysmlPackage.ELEMENT__QUALIFIED_NAME:
                return QUALIFIED_NAME_EDEFAULT == null ? getQualifiedName() != null : !QUALIFIED_NAME_EDEFAULT.equals(getQualifiedName());
            case SysmlPackage.ELEMENT__SHORT_NAME:
                return SHORT_NAME_EDEFAULT == null ? getShortName() != null : !SHORT_NAME_EDEFAULT.equals(getShortName());
            case SysmlPackage.ELEMENT__DOCUMENTATION:
                return !getDocumentation().isEmpty();
            case SysmlPackage.ELEMENT__OWNED_ANNOTATION:
                return !getOwnedAnnotation().isEmpty();
            case SysmlPackage.ELEMENT__OWNED_ELEMENT:
                return !getOwnedElement().isEmpty();
            case SysmlPackage.ELEMENT__OWNED_RELATIONSHIP:
                return ownedRelationship != null && !ownedRelationship.isEmpty();
            case SysmlPackage.ELEMENT__OWNER:
                return basicGetOwner() != null;
            case SysmlPackage.ELEMENT__OWNING_MEMBERSHIP:
                return basicGetOwningMembership() != null;
            case SysmlPackage.ELEMENT__OWNING_NAMESPACE:
                return basicGetOwningNamespace() != null;
            case SysmlPackage.ELEMENT__OWNING_RELATIONSHIP:
                return getOwningRelationship() != null;
            case SysmlPackage.ELEMENT__TEXTUAL_REPRESENTATION:
                return !getTextualRepresentation().isEmpty();
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
            case SysmlPackage.ELEMENT___EFFECTIVE_NAME:
                return effectiveName();
            case SysmlPackage.ELEMENT___EFFECTIVE_SHORT_NAME:
                return effectiveShortName();
            case SysmlPackage.ELEMENT___ESCAPED_NAME:
                return escapedName();
            case SysmlPackage.ELEMENT___LIBRARY_NAMESPACE:
                return libraryNamespace();
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
        result.append(" (aliasIds: ");
        result.append(aliasIds);
        result.append(", declaredName: ");
        result.append(declaredName);
        result.append(", declaredShortName: ");
        result.append(declaredShortName);
        result.append(", elementId: ");
        result.append(elementId);
        result.append(", isImpliedIncluded: ");
        result.append(isImpliedIncluded);
        result.append(')');
        return result.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * We want to hide TextualRepresentations elements in our model trees representations.
     * The result must be an org.eclipse.emf.ecore.util.InternalEList.
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<EObject> eContents() {
        EList<EObject> eContents = super.eContents();
        EList<EObject> eContentsWithoutTextualRepresentations= new BasicInternalEList<EObject>(EObject.class);
        eContentsWithoutTextualRepresentations.addAll(eContents);
        eContents.stream().forEach(elt -> {
            if (elt instanceof Membership membership && membership.getOwnedRelatedElement().stream().anyMatch(TextualRepresentation.class::isInstance)) {
                eContentsWithoutTextualRepresentations.remove(membership);
            }
        });
        return eContentsWithoutTextualRepresentations;
    }

    /**
     * @generated NOT
     */
    @Override
    public TextualRepresentation getOrCreateTextualRepresentation() {
        return this.getOwnedRelationship().stream()
            .filter(OwningMembership.class::isInstance)
            .map(OwningMembership.class::cast)
            .flatMap(om -> om.getOwnedRelatedElement().stream())
            .filter(TextualRepresentation.class::isInstance)
            .map(TextualRepresentation.class::cast)
            .findFirst()
            .orElseGet(() -> {
                OwningMembership owningMembership = SysmlFactory.eINSTANCE.createOwningMembership();
                this.getOwnedRelationship().add(owningMembership);
                TextualRepresentation textualRepreentation = SysmlFactory.eINSTANCE.createTextualRepresentation();
                owningMembership.getOwnedRelatedElement().add(textualRepreentation);
                return textualRepreentation;
            });
    }
} //ElementImpl
