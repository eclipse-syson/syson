/**
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
 */
package org.eclipse.syson.sysml.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectEList;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Import;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.VisibilityKind;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Import</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.ImportImpl#isIsImportAll <em>Is Import All</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ImportImpl#isIsRecursive <em>Is Recursive</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ImportImpl#getVisibility <em>Visibility</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ImportImpl#getImportedElement <em>Imported Element</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ImportImpl#getImportOwningNamespace <em>Import Owning Namespace</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class ImportImpl extends RelationshipImpl implements Import {
    /**
     * The default value of the '{@link #isIsImportAll() <em>Is Import All</em>}' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #isIsImportAll()
     * @generated
     * @ordered
     */
    protected static final boolean IS_IMPORT_ALL_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsImportAll() <em>Is Import All</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #isIsImportAll()
     * @generated
     * @ordered
     */
    protected boolean isImportAll = IS_IMPORT_ALL_EDEFAULT;

    /**
     * The default value of the '{@link #isIsRecursive() <em>Is Recursive</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #isIsRecursive()
     * @generated
     * @ordered
     */
    protected static final boolean IS_RECURSIVE_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsRecursive() <em>Is Recursive</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #isIsRecursive()
     * @generated
     * @ordered
     */
    protected boolean isRecursive = IS_RECURSIVE_EDEFAULT;

    /**
     * The default value of the '{@link #getVisibility() <em>Visibility</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getVisibility()
     * @generated
     * @ordered
     */
    protected static final VisibilityKind VISIBILITY_EDEFAULT = VisibilityKind.PRIVATE;

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
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected ImportImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getImport();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Element getImportedElement() {
        Element importedElement = this.basicGetImportedElement();
        return importedElement != null && importedElement.eIsProxy() ? (Element) this.eResolveProxy((InternalEObject) importedElement) : importedElement;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Element basicGetImportedElement() {
        // TODO: implement this method to return the 'Imported Element' reference
        // -> do not perform proxy resolution
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Namespace getImportOwningNamespace() {
        Namespace importOwningNamespace = this.basicGetImportOwningNamespace();
        return importOwningNamespace != null && importOwningNamespace.eIsProxy() ? (Namespace) this.eResolveProxy((InternalEObject) importOwningNamespace) : importOwningNamespace;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public Namespace basicGetImportOwningNamespace() {
        Element owningRelatedElement = this.getOwningRelatedElement();
        if (owningRelatedElement instanceof Namespace ns) {
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
    public boolean isIsImportAll() {
        return this.isImportAll;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setIsImportAll(boolean newIsImportAll) {
        boolean oldIsImportAll = this.isImportAll;
        this.isImportAll = newIsImportAll;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.IMPORT__IS_IMPORT_ALL, oldIsImportAll, this.isImportAll));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public boolean isIsRecursive() {
        return this.isRecursive;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setIsRecursive(boolean newIsRecursive) {
        boolean oldIsRecursive = this.isRecursive;
        this.isRecursive = newIsRecursive;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.IMPORT__IS_RECURSIVE, oldIsRecursive, this.isRecursive));
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
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.IMPORT__VISIBILITY, oldVisibility, this.visibility));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<Membership> importedMemberships(EList<Namespace> excluded) {
        // TODO: implement this method
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.IMPORT__IS_IMPORT_ALL:
                return this.isIsImportAll();
            case SysmlPackage.IMPORT__IS_RECURSIVE:
                return this.isIsRecursive();
            case SysmlPackage.IMPORT__VISIBILITY:
                return this.getVisibility();
            case SysmlPackage.IMPORT__IMPORTED_ELEMENT:
                if (resolve) {
                    return this.getImportedElement();
                }
                return this.basicGetImportedElement();
            case SysmlPackage.IMPORT__IMPORT_OWNING_NAMESPACE:
                if (resolve) {
                    return this.getImportOwningNamespace();
                }
                return this.basicGetImportOwningNamespace();
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
            case SysmlPackage.IMPORT__IS_IMPORT_ALL:
                this.setIsImportAll((Boolean) newValue);
                return;
            case SysmlPackage.IMPORT__IS_RECURSIVE:
                this.setIsRecursive((Boolean) newValue);
                return;
            case SysmlPackage.IMPORT__VISIBILITY:
                this.setVisibility((VisibilityKind) newValue);
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
            case SysmlPackage.IMPORT__IS_IMPORT_ALL:
                this.setIsImportAll(IS_IMPORT_ALL_EDEFAULT);
                return;
            case SysmlPackage.IMPORT__IS_RECURSIVE:
                this.setIsRecursive(IS_RECURSIVE_EDEFAULT);
                return;
            case SysmlPackage.IMPORT__VISIBILITY:
                this.setVisibility(VISIBILITY_EDEFAULT);
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
            case SysmlPackage.IMPORT__IS_IMPORT_ALL:
                return this.isImportAll != IS_IMPORT_ALL_EDEFAULT;
            case SysmlPackage.IMPORT__IS_RECURSIVE:
                return this.isRecursive != IS_RECURSIVE_EDEFAULT;
            case SysmlPackage.IMPORT__VISIBILITY:
                return this.visibility != VISIBILITY_EDEFAULT;
            case SysmlPackage.IMPORT__IMPORTED_ELEMENT:
                return this.basicGetImportedElement() != null;
            case SysmlPackage.IMPORT__IMPORT_OWNING_NAMESPACE:
                return this.basicGetImportOwningNamespace() != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    @SuppressWarnings("unchecked")
    public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
        switch (operationID) {
            case SysmlPackage.IMPORT___IMPORTED_MEMBERSHIPS__ELIST:
                return this.importedMemberships((EList<Namespace>) arguments.get(0));
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
        result.append(" (isImportAll: ");
        result.append(this.isImportAll);
        result.append(", isRecursive: ");
        result.append(this.isRecursive);
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
    public EList<Element> getSource() {
        EList<Element> sources = new EObjectEList.Unsettable<>(Element.class, this, SysmlPackage.IMPORT__SOURCE) {
            @Override
            public boolean addAll(Collection<? extends Element> collection) {
                return false;
            }

            @Override
            protected void dispatchNotification(Notification notification) {
            }
        };
        Namespace importOwningNamespace = this.getImportOwningNamespace();
        if (importOwningNamespace != null) {
            sources.add(importOwningNamespace);
        }
        return sources;
    }

} // ImportImpl
