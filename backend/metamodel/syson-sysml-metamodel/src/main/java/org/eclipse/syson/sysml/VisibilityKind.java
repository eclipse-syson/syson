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
package org.eclipse.syson.sysml;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc --> A representation of the literals of the enumeration '<em><b>Visibility Kind</b></em>', and
 * utility methods for working with them. <!-- end-user-doc -->
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getVisibilityKind()
 * @model
 * @generated
 */
public enum VisibilityKind implements Enumerator {
    /**
     * The '<em><b>Private</b></em>' literal object. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #PRIVATE_VALUE
     * @generated
     * @ordered
     */
    PRIVATE(0, "private", "private"),

    /**
     * The '<em><b>Protected</b></em>' literal object. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #PROTECTED_VALUE
     * @generated
     * @ordered
     */
    PROTECTED(1, "protected", "protected"),

    /**
     * The '<em><b>Public</b></em>' literal object. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #PUBLIC_VALUE
     * @generated
     * @ordered
     */
    PUBLIC(2, "public", "public");

    /**
     * The '<em><b>Private</b></em>' literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #PRIVATE
     * @model name="private"
     * @generated
     * @ordered
     */
    public static final int PRIVATE_VALUE = 0;

    /**
     * The '<em><b>Protected</b></em>' literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #PROTECTED
     * @model name="protected"
     * @generated
     * @ordered
     */
    public static final int PROTECTED_VALUE = 1;

    /**
     * The '<em><b>Public</b></em>' literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #PUBLIC
     * @model name="public"
     * @generated
     * @ordered
     */
    public static final int PUBLIC_VALUE = 2;

    /**
     * An array of all the '<em><b>Visibility Kind</b></em>' enumerators. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private static final VisibilityKind[] VALUES_ARRAY = new VisibilityKind[] {
            PRIVATE,
            PROTECTED,
            PUBLIC,
    };

    /**
     * A public read-only list of all the '<em><b>Visibility Kind</b></em>' enumerators. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     */
    public static final List<VisibilityKind> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Visibility Kind</b></em>' literal with the specified literal value. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param literal
     *            the literal.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static VisibilityKind get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            VisibilityKind result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Visibility Kind</b></em>' literal with the specified name. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @param name
     *            the name.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static VisibilityKind getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            VisibilityKind result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Visibility Kind</b></em>' literal with the specified integer value. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param value
     *            the integer value.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static VisibilityKind get(int value) {
        switch (value) {
            case PRIVATE_VALUE:
                return PRIVATE;
            case PROTECTED_VALUE:
                return PROTECTED;
            case PUBLIC_VALUE:
                return PUBLIC;
        }
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private final int value;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private final String name;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private final String literal;

    /**
     * Only this class can construct instances. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private VisibilityKind(int value, String name, String literal) {
        this.value = value;
        this.name = name;
        this.literal = literal;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public int getValue() {
        return this.value;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getLiteral() {
        return this.literal;
    }

    /**
     * Returns the literal value of the enumerator, which is its string representation. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     */
    @Override
    public String toString() {
        return this.literal;
    }

} // VisibilityKind
