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

import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.NamespaceImport;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.TextualRepresentation;
import org.eclipse.syson.sysml.VisibilityKind;
import org.eclipse.syson.sysml.helper.PrettyPrinter;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Namespace Import</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.impl.NamespaceImportImpl#getImportedNamespace <em>Imported Namespace</em>}</li>
 * </ul>
 *
 * @generated
 */
public class NamespaceImportImpl extends ImportImpl implements NamespaceImport {
    /**
     * The cached value of the '{@link #getImportedNamespace() <em>Imported Namespace</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getImportedNamespace()
     * @generated
     * @ordered
     */
    protected Namespace importedNamespace;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected NamespaceImportImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getNamespaceImport();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Namespace getImportedNamespace() {
        if (importedNamespace != null && importedNamespace.eIsProxy()) {
            InternalEObject oldImportedNamespace = (InternalEObject)importedNamespace;
            importedNamespace = (Namespace)eResolveProxy(oldImportedNamespace);
            if (importedNamespace != oldImportedNamespace) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.NAMESPACE_IMPORT__IMPORTED_NAMESPACE, oldImportedNamespace, importedNamespace));
            }
        }
        return importedNamespace;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Namespace basicGetImportedNamespace() {
        return importedNamespace;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setImportedNamespace(Namespace newImportedNamespace) {
        Namespace oldImportedNamespace = importedNamespace;
        importedNamespace = newImportedNamespace;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.NAMESPACE_IMPORT__IMPORTED_NAMESPACE, oldImportedNamespace, importedNamespace));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.NAMESPACE_IMPORT__IMPORTED_NAMESPACE:
                if (resolve) return getImportedNamespace();
                return basicGetImportedNamespace();
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
            case SysmlPackage.NAMESPACE_IMPORT__IMPORTED_NAMESPACE:
                setImportedNamespace((Namespace)newValue);
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
            case SysmlPackage.NAMESPACE_IMPORT__IMPORTED_NAMESPACE:
                setImportedNamespace((Namespace)null);
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
            case SysmlPackage.NAMESPACE_IMPORT__IMPORTED_NAMESPACE:
                return importedNamespace != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<TextualRepresentation> getTextualRepresentation() {
        TextualRepresentation repr = getOrCreateTextualRepresentation();
        StringBuilder builder = new StringBuilder();
        if( ! getVisibility().equals(VisibilityKind.PUBLIC) ){
        builder.append(getVisibility() + " ");
        }
        builder.append("import ");
        if( getImportedNamespace() != null ){
            builder.append(PrettyPrinter.prettyPrintName(getImportedNamespace().getDeclaredName()));
        } else {
            builder.append(PrettyPrinter.prettyPrintName(getDeclaredName()));
        }
        builder.append("::*");
        builder.append(";");
        repr.setBody(builder.toString());
        List<TextualRepresentation> textualRepresentation = List.of(repr);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getElement_TextualRepresentation(), textualRepresentation.size(), textualRepresentation.toArray());
    }

} //NamespaceImportImpl
