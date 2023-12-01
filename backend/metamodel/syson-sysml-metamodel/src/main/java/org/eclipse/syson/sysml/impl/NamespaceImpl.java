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
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Import;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.VisibilityKind;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Namespace</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.impl.NamespaceImpl#getImportedMembership <em>Imported Membership</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.NamespaceImpl#getMember <em>Member</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.NamespaceImpl#getMembership <em>Membership</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.NamespaceImpl#getOwnedImport <em>Owned Import</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.NamespaceImpl#getOwnedMember <em>Owned Member</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.NamespaceImpl#getOwnedMembership <em>Owned Membership</em>}</li>
 * </ul>
 *
 * @generated
 */
public class NamespaceImpl extends ElementImpl implements Namespace {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected NamespaceImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getNamespace();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<Membership> getImportedMembership() {
        List<Membership> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getNamespace_ImportedMembership(), data.size(), data.toArray());

    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<Element> getMember() {
        List<Element> members = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getNamespace_Member(), members.size(), members.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<Membership> getMembership() {
        List<Element> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getNamespace_Membership(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<Import> getOwnedImport() {
        List<Element> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getNamespace_OwnedImport(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<Element> getOwnedMember() {
        List<Element> ownedMembers = new ArrayList<>();
        this.getOwnedRelationship().stream()
            .filter(Membership.class::isInstance)
            .map(Membership.class::cast)
            .flatMap(m -> m.getOwnedRelatedElement().stream())
            .forEach(ownedMembers::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getNamespace_OwnedMember(), ownedMembers.size(), ownedMembers.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<Membership> getOwnedMembership() {
        List<Membership> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getNamespace_OwnedMembership(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<Membership> importedMemberships(EList<Namespace> excluded) {
        // TODO: implement this method
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<String> namesOf(Element element) {
        // TODO: implement this method
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String qualificationOf(String qualifiedName) {
        // TODO: implement this method
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Membership resolve(String qualifiedName) {
        // TODO: implement this method
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Membership resolveGlobal(String qualifiedName) {
        // TODO: implement this method
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Membership resolveLocal(String name) {
        // TODO: implement this method
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Membership resolveVisible(String name) {
        // TODO: implement this method
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String unqualifiedNameOf(String qualifiedName) {
        // TODO: implement this method
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public VisibilityKind visibilityOf(Membership mem) {
        // TODO: implement this method
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<Membership> visibleMemberships(EList<Namespace> excluded, boolean isRecursive, boolean includeAll) {
        // TODO: implement this method
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
            case SysmlPackage.NAMESPACE__IMPORTED_MEMBERSHIP:
                return getImportedMembership();
            case SysmlPackage.NAMESPACE__MEMBER:
                return getMember();
            case SysmlPackage.NAMESPACE__MEMBERSHIP:
                return getMembership();
            case SysmlPackage.NAMESPACE__OWNED_IMPORT:
                return getOwnedImport();
            case SysmlPackage.NAMESPACE__OWNED_MEMBER:
                return getOwnedMember();
            case SysmlPackage.NAMESPACE__OWNED_MEMBERSHIP:
                return getOwnedMembership();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case SysmlPackage.NAMESPACE__IMPORTED_MEMBERSHIP:
                return !getImportedMembership().isEmpty();
            case SysmlPackage.NAMESPACE__MEMBER:
                return !getMember().isEmpty();
            case SysmlPackage.NAMESPACE__MEMBERSHIP:
                return !getMembership().isEmpty();
            case SysmlPackage.NAMESPACE__OWNED_IMPORT:
                return !getOwnedImport().isEmpty();
            case SysmlPackage.NAMESPACE__OWNED_MEMBER:
                return !getOwnedMember().isEmpty();
            case SysmlPackage.NAMESPACE__OWNED_MEMBERSHIP:
                return !getOwnedMembership().isEmpty();
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    @SuppressWarnings("unchecked")
    public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
        switch (operationID) {
            case SysmlPackage.NAMESPACE___IMPORTED_MEMBERSHIPS__ELIST:
                return importedMemberships((EList<Namespace>)arguments.get(0));
            case SysmlPackage.NAMESPACE___NAMES_OF__ELEMENT:
                return namesOf((Element)arguments.get(0));
            case SysmlPackage.NAMESPACE___QUALIFICATION_OF__STRING:
                return qualificationOf((String)arguments.get(0));
            case SysmlPackage.NAMESPACE___RESOLVE__STRING:
                return resolve((String)arguments.get(0));
            case SysmlPackage.NAMESPACE___RESOLVE_GLOBAL__STRING:
                return resolveGlobal((String)arguments.get(0));
            case SysmlPackage.NAMESPACE___RESOLVE_LOCAL__STRING:
                return resolveLocal((String)arguments.get(0));
            case SysmlPackage.NAMESPACE___RESOLVE_VISIBLE__STRING:
                return resolveVisible((String)arguments.get(0));
            case SysmlPackage.NAMESPACE___UNQUALIFIED_NAME_OF__STRING:
                return unqualifiedNameOf((String)arguments.get(0));
            case SysmlPackage.NAMESPACE___VISIBILITY_OF__MEMBERSHIP:
                return visibilityOf((Membership)arguments.get(0));
            case SysmlPackage.NAMESPACE___VISIBLE_MEMBERSHIPS__ELIST_BOOLEAN_BOOLEAN:
                return visibleMemberships((EList<Namespace>)arguments.get(0), (Boolean)arguments.get(1), (Boolean)arguments.get(2));
        }
        return super.eInvoke(operationID, arguments);
    }

} //NamespaceImpl
