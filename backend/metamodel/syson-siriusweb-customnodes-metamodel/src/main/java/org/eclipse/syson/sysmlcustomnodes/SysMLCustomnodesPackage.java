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
package org.eclipse.syson.sysmlcustomnodes;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.view.diagram.DiagramPackage;

/**
 * <!-- begin-user-doc --> The <b>Package</b> for the model. It contains accessors for the meta objects to represent
 * <ul>
 * <li>each class,</li>
 * <li>each feature of each class,</li>
 * <li>each operation of each class,</li>
 * <li>each enum,</li>
 * <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 *
 * @see org.eclipse.syson.sysmlcustomnodes.SysMLCustomnodesFactory
 * @model kind="package"
 * @generated
 */
public interface SysMLCustomnodesPackage extends EPackage {
    /**
     * The package name. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    String eNAME = "sysmlcustomnodes";

    /**
     * The package namespace URI. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    String eNS_URI = "http://www.eclipse.org/syson/customnodes";

    /**
     * The package namespace name. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    String eNS_PREFIX = "sysmlcustomnodes";

    /**
     * The singleton instance of the package. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    SysMLCustomnodesPackage eINSTANCE = org.eclipse.syson.sysmlcustomnodes.impl.SysMLCustomnodesPackageImpl.init();

    /**
     * The meta object id for the '{@link org.eclipse.syson.sysmlcustomnodes.impl.SysMLPackageNodeStyleDescriptionImpl
     * <em>Sys ML Package Node Style Description</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.eclipse.syson.sysmlcustomnodes.impl.SysMLPackageNodeStyleDescriptionImpl
     * @see org.eclipse.syson.sysmlcustomnodes.impl.SysMLCustomnodesPackageImpl#getSysMLPackageNodeStyleDescription()
     * @generated
     */
    int SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION = 0;

    /**
     * The feature id for the '<em><b>Border Color</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION__BORDER_COLOR = DiagramPackage.NODE_STYLE_DESCRIPTION__BORDER_COLOR;

    /**
     * The feature id for the '<em><b>Border Radius</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION__BORDER_RADIUS = DiagramPackage.NODE_STYLE_DESCRIPTION__BORDER_RADIUS;

    /**
     * The feature id for the '<em><b>Border Size</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION__BORDER_SIZE = DiagramPackage.NODE_STYLE_DESCRIPTION__BORDER_SIZE;

    /**
     * The feature id for the '<em><b>Border Line Style</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @generated
     * @ordered
     */
    int SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION__BORDER_LINE_STYLE = DiagramPackage.NODE_STYLE_DESCRIPTION__BORDER_LINE_STYLE;

    /**
     * The feature id for the '<em><b>Children Layout Strategy</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION__CHILDREN_LAYOUT_STRATEGY = DiagramPackage.NODE_STYLE_DESCRIPTION__CHILDREN_LAYOUT_STRATEGY;

    /**
     * The feature id for the '<em><b>Background</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION__BACKGROUND = DiagramPackage.NODE_STYLE_DESCRIPTION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Sys ML Package Node Style Description</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION_FEATURE_COUNT = DiagramPackage.NODE_STYLE_DESCRIPTION_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Sys ML Package Node Style Description</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION_OPERATION_COUNT = DiagramPackage.NODE_STYLE_DESCRIPTION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.syson.sysmlcustomnodes.impl.SysMLNoteNodeStyleDescriptionImpl
     * <em>Sys ML Note Node Style Description</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.eclipse.syson.sysmlcustomnodes.impl.SysMLNoteNodeStyleDescriptionImpl
     * @see org.eclipse.syson.sysmlcustomnodes.impl.SysMLCustomnodesPackageImpl#getSysMLNoteNodeStyleDescription()
     * @generated
     */
    int SYS_ML_NOTE_NODE_STYLE_DESCRIPTION = 1;

    /**
     * The feature id for the '<em><b>Border Color</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SYS_ML_NOTE_NODE_STYLE_DESCRIPTION__BORDER_COLOR = DiagramPackage.NODE_STYLE_DESCRIPTION__BORDER_COLOR;

    /**
     * The feature id for the '<em><b>Border Radius</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SYS_ML_NOTE_NODE_STYLE_DESCRIPTION__BORDER_RADIUS = DiagramPackage.NODE_STYLE_DESCRIPTION__BORDER_RADIUS;

    /**
     * The feature id for the '<em><b>Border Size</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SYS_ML_NOTE_NODE_STYLE_DESCRIPTION__BORDER_SIZE = DiagramPackage.NODE_STYLE_DESCRIPTION__BORDER_SIZE;

    /**
     * The feature id for the '<em><b>Border Line Style</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @generated
     * @ordered
     */
    int SYS_ML_NOTE_NODE_STYLE_DESCRIPTION__BORDER_LINE_STYLE = DiagramPackage.NODE_STYLE_DESCRIPTION__BORDER_LINE_STYLE;

    /**
     * The feature id for the '<em><b>Children Layout Strategy</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SYS_ML_NOTE_NODE_STYLE_DESCRIPTION__CHILDREN_LAYOUT_STRATEGY = DiagramPackage.NODE_STYLE_DESCRIPTION__CHILDREN_LAYOUT_STRATEGY;

    /**
     * The feature id for the '<em><b>Background</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SYS_ML_NOTE_NODE_STYLE_DESCRIPTION__BACKGROUND = DiagramPackage.NODE_STYLE_DESCRIPTION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Sys ML Note Node Style Description</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SYS_ML_NOTE_NODE_STYLE_DESCRIPTION_FEATURE_COUNT = DiagramPackage.NODE_STYLE_DESCRIPTION_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Sys ML Note Node Style Description</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SYS_ML_NOTE_NODE_STYLE_DESCRIPTION_OPERATION_COUNT = DiagramPackage.NODE_STYLE_DESCRIPTION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the
     * '{@link org.eclipse.syson.sysmlcustomnodes.impl.SysMLImportedPackageNodeStyleDescriptionImpl <em>Sys ML Imported
     * Package Node Style Description</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.eclipse.syson.sysmlcustomnodes.impl.SysMLImportedPackageNodeStyleDescriptionImpl
     * @see org.eclipse.syson.sysmlcustomnodes.impl.SysMLCustomnodesPackageImpl#getSysMLImportedPackageNodeStyleDescription()
     * @generated
     */
    int SYS_ML_IMPORTED_PACKAGE_NODE_STYLE_DESCRIPTION = 2;

    /**
     * The feature id for the '<em><b>Border Color</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SYS_ML_IMPORTED_PACKAGE_NODE_STYLE_DESCRIPTION__BORDER_COLOR = DiagramPackage.NODE_STYLE_DESCRIPTION__BORDER_COLOR;

    /**
     * The feature id for the '<em><b>Border Radius</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SYS_ML_IMPORTED_PACKAGE_NODE_STYLE_DESCRIPTION__BORDER_RADIUS = DiagramPackage.NODE_STYLE_DESCRIPTION__BORDER_RADIUS;

    /**
     * The feature id for the '<em><b>Border Size</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SYS_ML_IMPORTED_PACKAGE_NODE_STYLE_DESCRIPTION__BORDER_SIZE = DiagramPackage.NODE_STYLE_DESCRIPTION__BORDER_SIZE;

    /**
     * The feature id for the '<em><b>Border Line Style</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @generated
     * @ordered
     */
    int SYS_ML_IMPORTED_PACKAGE_NODE_STYLE_DESCRIPTION__BORDER_LINE_STYLE = DiagramPackage.NODE_STYLE_DESCRIPTION__BORDER_LINE_STYLE;

    /**
     * The feature id for the '<em><b>Children Layout Strategy</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SYS_ML_IMPORTED_PACKAGE_NODE_STYLE_DESCRIPTION__CHILDREN_LAYOUT_STRATEGY = DiagramPackage.NODE_STYLE_DESCRIPTION__CHILDREN_LAYOUT_STRATEGY;

    /**
     * The feature id for the '<em><b>Background</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SYS_ML_IMPORTED_PACKAGE_NODE_STYLE_DESCRIPTION__BACKGROUND = DiagramPackage.NODE_STYLE_DESCRIPTION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Sys ML Imported Package Node Style Description</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SYS_ML_IMPORTED_PACKAGE_NODE_STYLE_DESCRIPTION_FEATURE_COUNT = DiagramPackage.NODE_STYLE_DESCRIPTION_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Sys ML Imported Package Node Style Description</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SYS_ML_IMPORTED_PACKAGE_NODE_STYLE_DESCRIPTION_OPERATION_COUNT = DiagramPackage.NODE_STYLE_DESCRIPTION_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.eclipse.syson.sysmlcustomnodes.impl.SysMLViewFrameNodeStyleDescriptionImpl
     * <em>Sys ML View Frame Node Style Description</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.eclipse.syson.sysmlcustomnodes.impl.SysMLViewFrameNodeStyleDescriptionImpl
     * @see org.eclipse.syson.sysmlcustomnodes.impl.SysMLCustomnodesPackageImpl#getSysMLViewFrameNodeStyleDescription()
     * @generated
     */
    int SYS_ML_VIEW_FRAME_NODE_STYLE_DESCRIPTION = 3;

    /**
     * The feature id for the '<em><b>Border Color</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SYS_ML_VIEW_FRAME_NODE_STYLE_DESCRIPTION__BORDER_COLOR = DiagramPackage.NODE_STYLE_DESCRIPTION__BORDER_COLOR;

    /**
     * The feature id for the '<em><b>Border Radius</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SYS_ML_VIEW_FRAME_NODE_STYLE_DESCRIPTION__BORDER_RADIUS = DiagramPackage.NODE_STYLE_DESCRIPTION__BORDER_RADIUS;

    /**
     * The feature id for the '<em><b>Border Size</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SYS_ML_VIEW_FRAME_NODE_STYLE_DESCRIPTION__BORDER_SIZE = DiagramPackage.NODE_STYLE_DESCRIPTION__BORDER_SIZE;

    /**
     * The feature id for the '<em><b>Border Line Style</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @generated
     * @ordered
     */
    int SYS_ML_VIEW_FRAME_NODE_STYLE_DESCRIPTION__BORDER_LINE_STYLE = DiagramPackage.NODE_STYLE_DESCRIPTION__BORDER_LINE_STYLE;

    /**
     * The feature id for the '<em><b>Children Layout Strategy</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SYS_ML_VIEW_FRAME_NODE_STYLE_DESCRIPTION__CHILDREN_LAYOUT_STRATEGY = DiagramPackage.NODE_STYLE_DESCRIPTION__CHILDREN_LAYOUT_STRATEGY;

    /**
     * The feature id for the '<em><b>Background</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SYS_ML_VIEW_FRAME_NODE_STYLE_DESCRIPTION__BACKGROUND = DiagramPackage.NODE_STYLE_DESCRIPTION_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Sys ML View Frame Node Style Description</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SYS_ML_VIEW_FRAME_NODE_STYLE_DESCRIPTION_FEATURE_COUNT = DiagramPackage.NODE_STYLE_DESCRIPTION_FEATURE_COUNT + 1;

    /**
     * The number of operations of the '<em>Sys ML View Frame Node Style Description</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SYS_ML_VIEW_FRAME_NODE_STYLE_DESCRIPTION_OPERATION_COUNT = DiagramPackage.NODE_STYLE_DESCRIPTION_OPERATION_COUNT + 0;

    /**
     * Returns the meta object for class '{@link org.eclipse.syson.sysmlcustomnodes.SysMLPackageNodeStyleDescription
     * <em>Sys ML Package Node Style Description</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Sys ML Package Node Style Description</em>'.
     * @see org.eclipse.syson.sysmlcustomnodes.SysMLPackageNodeStyleDescription
     * @generated
     */
    EClass getSysMLPackageNodeStyleDescription();

    /**
     * Returns the meta object for the reference
     * '{@link org.eclipse.syson.sysmlcustomnodes.SysMLPackageNodeStyleDescription#getBackground <em>Background</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the reference '<em>Background</em>'.
     * @see org.eclipse.syson.sysmlcustomnodes.SysMLPackageNodeStyleDescription#getBackground()
     * @see #getSysMLPackageNodeStyleDescription()
     * @generated
     */
    EReference getSysMLPackageNodeStyleDescription_Background();

    /**
     * Returns the meta object for class '{@link org.eclipse.syson.sysmlcustomnodes.SysMLNoteNodeStyleDescription
     * <em>Sys ML Note Node Style Description</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Sys ML Note Node Style Description</em>'.
     * @see org.eclipse.syson.sysmlcustomnodes.SysMLNoteNodeStyleDescription
     * @generated
     */
    EClass getSysMLNoteNodeStyleDescription();

    /**
     * Returns the meta object for the reference
     * '{@link org.eclipse.syson.sysmlcustomnodes.SysMLNoteNodeStyleDescription#getBackground <em>Background</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the reference '<em>Background</em>'.
     * @see org.eclipse.syson.sysmlcustomnodes.SysMLNoteNodeStyleDescription#getBackground()
     * @see #getSysMLNoteNodeStyleDescription()
     * @generated
     */
    EReference getSysMLNoteNodeStyleDescription_Background();

    /**
     * Returns the meta object for class
     * '{@link org.eclipse.syson.sysmlcustomnodes.SysMLImportedPackageNodeStyleDescription <em>Sys ML Imported Package
     * Node Style Description</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Sys ML Imported Package Node Style Description</em>'.
     * @see org.eclipse.syson.sysmlcustomnodes.SysMLImportedPackageNodeStyleDescription
     * @generated
     */
    EClass getSysMLImportedPackageNodeStyleDescription();

    /**
     * Returns the meta object for the reference
     * '{@link org.eclipse.syson.sysmlcustomnodes.SysMLImportedPackageNodeStyleDescription#getBackground
     * <em>Background</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the reference '<em>Background</em>'.
     * @see org.eclipse.syson.sysmlcustomnodes.SysMLImportedPackageNodeStyleDescription#getBackground()
     * @see #getSysMLImportedPackageNodeStyleDescription()
     * @generated
     */
    EReference getSysMLImportedPackageNodeStyleDescription_Background();

    /**
     * Returns the meta object for class '{@link org.eclipse.syson.sysmlcustomnodes.SysMLViewFrameNodeStyleDescription
     * <em>Sys ML View Frame Node Style Description</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Sys ML View Frame Node Style Description</em>'.
     * @see org.eclipse.syson.sysmlcustomnodes.SysMLViewFrameNodeStyleDescription
     * @generated
     */
    EClass getSysMLViewFrameNodeStyleDescription();

    /**
     * Returns the meta object for the reference
     * '{@link org.eclipse.syson.sysmlcustomnodes.SysMLViewFrameNodeStyleDescription#getBackground
     * <em>Background</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the reference '<em>Background</em>'.
     * @see org.eclipse.syson.sysmlcustomnodes.SysMLViewFrameNodeStyleDescription#getBackground()
     * @see #getSysMLViewFrameNodeStyleDescription()
     * @generated
     */
    EReference getSysMLViewFrameNodeStyleDescription_Background();

    /**
     * Returns the factory that creates the instances of the model. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the factory that creates the instances of the model.
     * @generated
     */
    SysMLCustomnodesFactory getSysMLCustomnodesFactory();

    /**
     * <!-- begin-user-doc --> Defines literals for the meta objects that represent
     * <ul>
     * <li>each class,</li>
     * <li>each feature of each class,</li>
     * <li>each operation of each class,</li>
     * <li>each enum,</li>
     * <li>and each data type</li>
     * </ul>
     * <!-- end-user-doc -->
     *
     * @generated
     */
    interface Literals {
        /**
         * The meta object literal for the
         * '{@link org.eclipse.syson.sysmlcustomnodes.impl.SysMLPackageNodeStyleDescriptionImpl <em>Sys ML Package Node
         * Style Description</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
         *
         * @see org.eclipse.syson.sysmlcustomnodes.impl.SysMLPackageNodeStyleDescriptionImpl
         * @see org.eclipse.syson.sysmlcustomnodes.impl.SysMLCustomnodesPackageImpl#getSysMLPackageNodeStyleDescription()
         * @generated
         */
        EClass SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION = eINSTANCE.getSysMLPackageNodeStyleDescription();

        /**
         * The meta object literal for the '<em><b>Background</b></em>' reference feature. <!-- begin-user-doc --> <!--
         * end-user-doc -->
         *
         * @generated
         */
        EReference SYS_ML_PACKAGE_NODE_STYLE_DESCRIPTION__BACKGROUND = eINSTANCE.getSysMLPackageNodeStyleDescription_Background();

        /**
         * The meta object literal for the
         * '{@link org.eclipse.syson.sysmlcustomnodes.impl.SysMLNoteNodeStyleDescriptionImpl <em>Sys ML Note Node Style
         * Description</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
         *
         * @see org.eclipse.syson.sysmlcustomnodes.impl.SysMLNoteNodeStyleDescriptionImpl
         * @see org.eclipse.syson.sysmlcustomnodes.impl.SysMLCustomnodesPackageImpl#getSysMLNoteNodeStyleDescription()
         * @generated
         */
        EClass SYS_ML_NOTE_NODE_STYLE_DESCRIPTION = eINSTANCE.getSysMLNoteNodeStyleDescription();

        /**
         * The meta object literal for the '<em><b>Background</b></em>' reference feature. <!-- begin-user-doc --> <!--
         * end-user-doc -->
         *
         * @generated
         */
        EReference SYS_ML_NOTE_NODE_STYLE_DESCRIPTION__BACKGROUND = eINSTANCE.getSysMLNoteNodeStyleDescription_Background();

        /**
         * The meta object literal for the
         * '{@link org.eclipse.syson.sysmlcustomnodes.impl.SysMLImportedPackageNodeStyleDescriptionImpl <em>Sys ML
         * Imported Package Node Style Description</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
         *
         * @see org.eclipse.syson.sysmlcustomnodes.impl.SysMLImportedPackageNodeStyleDescriptionImpl
         * @see org.eclipse.syson.sysmlcustomnodes.impl.SysMLCustomnodesPackageImpl#getSysMLImportedPackageNodeStyleDescription()
         * @generated
         */
        EClass SYS_ML_IMPORTED_PACKAGE_NODE_STYLE_DESCRIPTION = eINSTANCE.getSysMLImportedPackageNodeStyleDescription();

        /**
         * The meta object literal for the '<em><b>Background</b></em>' reference feature. <!-- begin-user-doc --> <!--
         * end-user-doc -->
         *
         * @generated
         */
        EReference SYS_ML_IMPORTED_PACKAGE_NODE_STYLE_DESCRIPTION__BACKGROUND = eINSTANCE.getSysMLImportedPackageNodeStyleDescription_Background();

        /**
         * The meta object literal for the
         * '{@link org.eclipse.syson.sysmlcustomnodes.impl.SysMLViewFrameNodeStyleDescriptionImpl <em>Sys ML View Frame
         * Node Style Description</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
         *
         * @see org.eclipse.syson.sysmlcustomnodes.impl.SysMLViewFrameNodeStyleDescriptionImpl
         * @see org.eclipse.syson.sysmlcustomnodes.impl.SysMLCustomnodesPackageImpl#getSysMLViewFrameNodeStyleDescription()
         * @generated
         */
        EClass SYS_ML_VIEW_FRAME_NODE_STYLE_DESCRIPTION = eINSTANCE.getSysMLViewFrameNodeStyleDescription();

        /**
         * The meta object literal for the '<em><b>Background</b></em>' reference feature. <!-- begin-user-doc --> <!--
         * end-user-doc -->
         *
         * @generated
         */
        EReference SYS_ML_VIEW_FRAME_NODE_STYLE_DESCRIPTION__BACKGROUND = eINSTANCE.getSysMLViewFrameNodeStyleDescription_Background();

    }

} // SysMLCustomnodesPackage
