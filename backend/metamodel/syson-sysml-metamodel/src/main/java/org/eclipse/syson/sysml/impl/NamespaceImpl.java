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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Import;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.VisibilityKind;
import org.eclipse.syson.sysml.helper.MembershipComputer;
import org.eclipse.syson.sysml.helper.NameConflictingFilter;
import org.eclipse.syson.sysml.helper.NameHelper;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Namespace</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.NamespaceImpl#getImportedMembership <em>Imported Membership</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.NamespaceImpl#getMember <em>Member</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.NamespaceImpl#getMembership <em>Membership</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.NamespaceImpl#getOwnedImport <em>Owned Import</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.NamespaceImpl#getOwnedMember <em>Owned Member</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.NamespaceImpl#getOwnedMembership <em>Owned Membership</em>}</li>
 * </ul>
 *
 * @generated
 */
public class NamespaceImpl extends ElementImpl implements Namespace {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected NamespaceImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getNamespace();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Membership> getImportedMembership() {
        return this.getImportedMembership(new BasicEList<>());
    }

    /**
     * @generated NOT
     */
    private EList<Membership> getImportedMembership(EList<Namespace> excluded) {
        return this.importedMemberships(new UniqueEList<>());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Element> getMember() {
        List<Element> members = new ArrayList<>();
        this.getMembership().stream()
                .map(membership -> membership.getMemberElement())
                .filter(Objects::nonNull)
                .forEach(members::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getNamespace_Member(), members.size(), members.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Membership> getMembership() {
        return this.getMembership(new BasicEList<>());
    }

    /**
     * @generated NOT
     */
    protected EList<Membership> getMembership(EList<Namespace> excluded) {
        List<Element> memberships = new ArrayList<>();
        NameConflictingFilter filter = new NameConflictingFilter();
        this.getOwnedMembership().stream().filter(filter).forEach(memberships::add);
        this.importedMemberships(excluded).stream().filter(filter).forEach(memberships::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getNamespace_Membership(), memberships.size(), memberships.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Import> getOwnedImport() {
        List<Element> ownedImports = new ArrayList<>();
        this.getOwnedRelationship().stream()
                .filter(Import.class::isInstance)
                .map(Import.class::cast)
                .filter(imprt -> this.equals(imprt.getImportOwningNamespace()))
                .forEach(ownedImports::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getNamespace_OwnedImport(), ownedImports.size(), ownedImports.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Element> getOwnedMember() {
        List<Element> ownedMembers = new ArrayList<>();
        this.getOwnedMembership().stream()
                .flatMap(m -> m.getOwnedRelatedElement().stream())
                .forEach(ownedMembers::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getNamespace_OwnedMember(), ownedMembers.size(), ownedMembers.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Membership> getOwnedMembership() {
        List<Membership> ownedMemberships = new ArrayList<>();
        this.getOwnedRelationship().stream()
                .filter(Membership.class::isInstance)
                .map(Membership.class::cast)
                .forEach(ownedMemberships::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getNamespace_OwnedMembership(), ownedMemberships.size(), ownedMemberships.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Membership> importedMemberships(EList<Namespace> excluded) {
        NameConflictingFilter nameConflictingFilter = new NameConflictingFilter();
        nameConflictingFilter.fillUsedNames(this.getOwnedMembership());
        List<Membership> importedMemberships = new ArrayList<>();
        EList<Namespace> excludedAndSelf = new BasicEList<>();
        excludedAndSelf.addAll(excluded);
        excludedAndSelf.add(this);
        this.getOwnedImport().stream()
                .map(imprt -> imprt.importedMemberships(excludedAndSelf))
                .flatMap(Collection::stream)
                .filter(nameConflictingFilter)
                .forEach(importedMemberships::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getNamespace_ImportedMembership(), importedMemberships.size(), importedMemberships.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<Membership> membershipsOfVisibility(VisibilityKind visibility, EList<Namespace> excluded) {
        // TODO: implement this method
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<String> namesOf(Element element) {
        EDataTypeUniqueEList<String> namesOfs = new EDataTypeUniqueEList<>(String.class, this, SysmlPackage.eINSTANCE.getNamespace__NamesOf__Element().getOperationID());
        List<Membership> elementMemberships = new ArrayList<>();
        this.getMembership().stream()
                .filter(membership -> element != null && element.equals(membership.getMemberElement()))
                .forEach(elementMemberships::add);
        elementMemberships.forEach(elt -> {
            String shortName = elt.getMemberShortName();
            String name = elt.getMemberName();
            if (shortName != null) {
                namesOfs.add(shortName);
            }
            if (name != null) {
                namesOfs.add(name);
            }
        });
        return namesOfs;
    }

    /**
     * <!-- begin-user-doc --> Return a string with valid KerML syntax representing the qualification part of a given
     * qualifiedName, that is, a qualified name with all the segment names of the given name except the last. If the
     * given qualifiedName has only one segment, then return null. <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public String qualificationOf(String qualifiedName) {
        List<String> segments = NameHelper.parseQualifiedName(qualifiedName);

        // If only 1 or member is present.
        if (segments.size() < 2) {
            return null;
        } else {
            String qualification = String.join("::", segments.subList(0, segments.size() - 1));
            return NameHelper.escapeString(qualification);
        }
    }

    /**
     * <!-- begin-user-doc --> Resolve the given qualified name to the named Membership (if any), starting with this
     * Namespace as the local scope. The qualified name string must conform to the concrete syntax of the KerML textual
     * notation. According to the KerML name resolution rules every qualified name will resolve to either a single
     * Membership, or to none. <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public Membership resolve(String qualifiedName) {

        String qualification = this.qualificationOf(qualifiedName);
        String name = this.unqualifiedNameOf(qualifiedName);

        Membership result = null;

        if (qualification == null) {
            result = this.resolveLocal(name);
        } else {
            Membership membership = this.resolve(qualification);
            if (membership != null) {
                Element memberElement = membership.getMemberElement();
                if (memberElement instanceof Namespace namespace) {
                    result = namespace.resolveVisible(name);
                }
            }
        }
        return result;
    }

    /**
     * <!-- begin-user-doc --> Resolve the given qualified name to the named Membership (if any) in the effective global
     * Namespace that is the outermost naming scope. The qualified name string must conform to the concrete syntax of
     * the KerML textual notation. <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public Membership resolveGlobal(String qualifiedName) {
        Membership result = null;

        Resource owningEcoreResource = this.eResource();
        if (owningEcoreResource != null) {
            ResourceSet owningResourceSet = owningEcoreResource.getResourceSet();
            if (owningResourceSet != null) {
                for (var resource : owningResourceSet.getResources()) {
                    if (!resource.getContents().isEmpty()) {
                        EObject root = resource.getContents().get(0);
                        if (root instanceof Namespace rootNamespace) {
                            result = rootNamespace.resolveVisible(qualifiedName);
                        }
                        if (result != null) {
                            break;
                        }
                    }
                }

            }
        }
        return result;
    }

    /**
     * <!-- begin-user-doc --> Resolve a simple name starting with this Namespace as the local scope, and continuing
     * with containing outer scopes as necessary. However, if this Namespace is a root Namespace, then the resolution is
     * done directly in global scope. <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public Membership resolveLocal(String name) {
        // Try to resolve the simple name in the current namespace
        var membership = this.getMembership().stream()
             .filter(m -> Objects.equals(m.getMemberShortName(), name) || Objects.equals(m.getMemberName(), name))
             .findFirst();

        if (membership.isEmpty()) {
            Namespace owningNamespace = this.getOwningNamespace();
            if (owningNamespace != null) {
                return owningNamespace.resolveLocal(name);
            } else {
                // If no parent namespace, global resolve
                return this.resolveGlobal(name);
            }
        } else {
            return membership.get();
        }
    }

    /**
     * <!-- begin-user-doc --> Resolve a simple name from the visible Memberships of this Namespace. <!-- end-user-doc
     * -->
     *
     * @generated NOT
     */
    @Override
    public Membership resolveVisible(String name) {
        EList<Membership> memberships = this.visibleMemberships(new BasicEList<>(), false, false);

        return memberships.stream()
                .filter(m -> Objects.equals(m.getMemberShortName(), name) || Objects.equals(m.getMemberName(), name))
                .findFirst()
                .orElse(null);
    }

    /**
     * <!-- begin-user-doc --> Return the simple name that is the last segment name of the given qualifiedName. If this
     * segment name has the form of a KerML unrestricted name, then "unescape" it by removing the surrounding single
     * quotes and replacing all escape sequences with the specified character. <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public String unqualifiedNameOf(String qualifiedName) {
        List<String> segments = NameHelper.parseQualifiedName(qualifiedName);

        if (segments.isEmpty()) {
            return null;
        } else {
            return NameHelper.unescapeString(segments.get(segments.size() - 1));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public VisibilityKind visibilityOf(Membership mem) {
        // TODO: implement this method
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Membership> visibleMemberships(EList<Namespace> excluded, boolean isRecursive, boolean includeAll) {
        return new MembershipComputer(this, excluded).visibleMemberships(isRecursive, includeAll, false);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.NAMESPACE__IMPORTED_MEMBERSHIP:
                return this.getImportedMembership();
            case SysmlPackage.NAMESPACE__MEMBER:
                return this.getMember();
            case SysmlPackage.NAMESPACE__MEMBERSHIP:
                return this.getMembership();
            case SysmlPackage.NAMESPACE__OWNED_IMPORT:
                return this.getOwnedImport();
            case SysmlPackage.NAMESPACE__OWNED_MEMBER:
                return this.getOwnedMember();
            case SysmlPackage.NAMESPACE__OWNED_MEMBERSHIP:
                return this.getOwnedMembership();
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
            case SysmlPackage.NAMESPACE__IMPORTED_MEMBERSHIP:
                return !this.getImportedMembership().isEmpty();
            case SysmlPackage.NAMESPACE__MEMBER:
                return !this.getMember().isEmpty();
            case SysmlPackage.NAMESPACE__MEMBERSHIP:
                return !this.getMembership().isEmpty();
            case SysmlPackage.NAMESPACE__OWNED_IMPORT:
                return !this.getOwnedImport().isEmpty();
            case SysmlPackage.NAMESPACE__OWNED_MEMBER:
                return !this.getOwnedMember().isEmpty();
            case SysmlPackage.NAMESPACE__OWNED_MEMBERSHIP:
                return !this.getOwnedMembership().isEmpty();
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
            case SysmlPackage.NAMESPACE___IMPORTED_MEMBERSHIPS__ELIST:
                return this.importedMemberships((EList<Namespace>) arguments.get(0));
            case SysmlPackage.NAMESPACE___MEMBERSHIPS_OF_VISIBILITY__VISIBILITYKIND_ELIST:
                return this.membershipsOfVisibility((VisibilityKind) arguments.get(0), (EList<Namespace>) arguments.get(1));
            case SysmlPackage.NAMESPACE___NAMES_OF__ELEMENT:
                return this.namesOf((Element) arguments.get(0));
            case SysmlPackage.NAMESPACE___QUALIFICATION_OF__STRING:
                return this.qualificationOf((String) arguments.get(0));
            case SysmlPackage.NAMESPACE___RESOLVE__STRING:
                return this.resolve((String) arguments.get(0));
            case SysmlPackage.NAMESPACE___RESOLVE_GLOBAL__STRING:
                return this.resolveGlobal((String) arguments.get(0));
            case SysmlPackage.NAMESPACE___RESOLVE_LOCAL__STRING:
                return this.resolveLocal((String) arguments.get(0));
            case SysmlPackage.NAMESPACE___RESOLVE_VISIBLE__STRING:
                return this.resolveVisible((String) arguments.get(0));
            case SysmlPackage.NAMESPACE___UNQUALIFIED_NAME_OF__STRING:
                return this.unqualifiedNameOf((String) arguments.get(0));
            case SysmlPackage.NAMESPACE___VISIBILITY_OF__MEMBERSHIP:
                return this.visibilityOf((Membership) arguments.get(0));
            case SysmlPackage.NAMESPACE___VISIBLE_MEMBERSHIPS__ELIST_BOOLEAN_BOOLEAN:
                return this.visibleMemberships((EList<Namespace>) arguments.get(0), (Boolean) arguments.get(1), (Boolean) arguments.get(2));
        }
        return super.eInvoke(operationID, arguments);
    }

} // NamespaceImpl
