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
package org.eclipse.syson.sysmlcustomnodes.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.sirius.components.view.ViewPackage;
import org.eclipse.sirius.components.view.diagram.DiagramPackage;
import org.eclipse.syson.sysmlcustomnodes.SysMLCustomnodesFactory;
import org.eclipse.syson.sysmlcustomnodes.SysMLCustomnodesPackage;
import org.eclipse.syson.sysmlcustomnodes.SysMLPackageNodeStyleDescription;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Package</b>. <!-- end-user-doc -->
 *
 * @generated
 */
public class SysMLCustomnodesPackageImpl extends EPackageImpl implements SysMLCustomnodesPackage {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass sysMLPackageNodeStyleDescriptionEClass = null;

    /**
     * Creates an instance of the model <b>Package</b>, registered with {@link org.eclipse.emf.ecore.EPackage.Registry
     * EPackage.Registry} by the package package URI value.
     * <p>
     * Note: the correct way to create the package is via the static factory method {@link #init init()}, which also
     * performs initialization of the package, or returns the registered package, if one already exists. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.eclipse.emf.ecore.EPackage.Registry
     * @see org.eclipse.syson.sysmlcustomnodes.SysMLCustomnodesPackage#eNS_URI
     * @see #init()
     * @generated
     */
    private SysMLCustomnodesPackageImpl() {
        super(eNS_URI, SysMLCustomnodesFactory.eINSTANCE);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private static boolean isInited = false;

    /**
     * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
     *
     * <p>
     * This method is used to initialize {@link SysMLCustomnodesPackage#eINSTANCE} when that field is accessed. Clients
     * should not invoke it directly. Instead, they should simply access that field to obtain the package. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static SysMLCustomnodesPackage init() {
        if (isInited)
            return (SysMLCustomnodesPackage) EPackage.Registry.INSTANCE.getEPackage(SysMLCustomnodesPackage.eNS_URI);

        // Obtain or create and register package
        Object registeredSysMLCustomnodesPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
        SysMLCustomnodesPackageImpl theSysMLCustomnodesPackage = registeredSysMLCustomnodesPackage instanceof SysMLCustomnodesPackageImpl
                ? (SysMLCustomnodesPackageImpl) registeredSysMLCustomnodesPackage
                : new SysMLCustomnodesPackageImpl();

        isInited = true;

        // Initialize simple dependencies
        DiagramPackage.eINSTANCE.eClass();
        ViewPackage.eINSTANCE.eClass();

        // Create package meta-data objects
        theSysMLCustomnodesPackage.createPackageContents();

        // Initialize created meta-data
        theSysMLCustomnodesPackage.initializePackageContents();

        // Mark meta-data to indicate it can't be changed
        theSysMLCustomnodesPackage.freeze();

        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(SysMLCustomnodesPackage.eNS_URI, theSysMLCustomnodesPackage);
        return theSysMLCustomnodesPackage;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getSysMLPackageNodeStyleDescription() {
        return this.sysMLPackageNodeStyleDescriptionEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getSysMLPackageNodeStyleDescription_Background() {
        return (EReference) this.sysMLPackageNodeStyleDescriptionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public SysMLCustomnodesFactory getSysMLCustomnodesFactory() {
        return (SysMLCustomnodesFactory) this.getEFactoryInstance();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private boolean isCreated = false;

    /**
     * Creates the meta-model objects for the package. This method is guarded to have no affect on any invocation but
     * its first. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public void createPackageContents() {
        if (this.isCreated)
            return;
        this.isCreated = true;

        // Create classes and their features
        this.sysMLPackageNodeStyleDescriptionEClass = this.createEClass(SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION);
        this.createEReference(this.sysMLPackageNodeStyleDescriptionEClass, SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION__BACKGROUND);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private boolean isInitialized = false;

    /**
     * Complete the initialization of the package and its meta-model. This method is guarded to have no affect on any
     * invocation but its first. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public void initializePackageContents() {
        if (this.isInitialized)
            return;
        this.isInitialized = true;

        // Initialize package
        this.setName(eNAME);
        this.setNsPrefix(eNS_PREFIX);
        this.setNsURI(eNS_URI);

        // Obtain other dependent packages
        DiagramPackage theDiagramPackage = (DiagramPackage) EPackage.Registry.INSTANCE.getEPackage(DiagramPackage.eNS_URI);
        ViewPackage theViewPackage = (ViewPackage) EPackage.Registry.INSTANCE.getEPackage(ViewPackage.eNS_URI);

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes
        this.sysMLPackageNodeStyleDescriptionEClass.getESuperTypes().add(theDiagramPackage.getNodeStyleDescription());

        // Initialize classes, features, and operations; add parameters
        this.initEClass(this.sysMLPackageNodeStyleDescriptionEClass, SysMLPackageNodeStyleDescription.class, "SysMLPackageNodeStyleDescription", !IS_ABSTRACT, !IS_INTERFACE,
                IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getSysMLPackageNodeStyleDescription_Background(), theViewPackage.getUserColor(), null, "background", null, 0, 1, SysMLPackageNodeStyleDescription.class, !IS_TRANSIENT,
                !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        // Create resource
        this.createResource(eNS_URI);
    }

} // SysMLCustomnodesPackageImpl
