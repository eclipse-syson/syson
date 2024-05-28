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
 * <!-- begin-user-doc --> A representation of the literals of the enumeration '<em><b>State Subaction Kind</b></em>',
 * and utility methods for working with them. <!-- end-user-doc -->
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getStateSubactionKind()
 * @model
 * @generated
 */
public enum StateSubactionKind implements Enumerator {
    /**
     * The '<em><b>Do</b></em>' literal object. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #DO_VALUE
     * @generated
     * @ordered
     */
    DO(0, "do", "do"),

    /**
     * The '<em><b>Entry</b></em>' literal object. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #ENTRY_VALUE
     * @generated
     * @ordered
     */
    ENTRY(1, "entry", "entry"),

    /**
     * The '<em><b>Exit</b></em>' literal object. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #EXIT_VALUE
     * @generated
     * @ordered
     */
    EXIT(2, "exit", "exit");

    /**
     * The '<em><b>Do</b></em>' literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #DO
     * @model name="do"
     * @generated
     * @ordered
     */
    public static final int DO_VALUE = 0;

    /**
     * The '<em><b>Entry</b></em>' literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #ENTRY
     * @model name="entry"
     * @generated
     * @ordered
     */
    public static final int ENTRY_VALUE = 1;

    /**
     * The '<em><b>Exit</b></em>' literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #EXIT
     * @model name="exit"
     * @generated
     * @ordered
     */
    public static final int EXIT_VALUE = 2;

    /**
     * An array of all the '<em><b>State Subaction Kind</b></em>' enumerators. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @generated
     */
    private static final StateSubactionKind[] VALUES_ARRAY = new StateSubactionKind[] {
            DO,
            ENTRY,
            EXIT,
    };

    /**
     * A public read-only list of all the '<em><b>State Subaction Kind</b></em>' enumerators. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    public static final List<StateSubactionKind> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>State Subaction Kind</b></em>' literal with the specified literal value. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @param literal
     *            the literal.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static StateSubactionKind get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            StateSubactionKind result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>State Subaction Kind</b></em>' literal with the specified name. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @param name
     *            the name.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static StateSubactionKind getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            StateSubactionKind result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>State Subaction Kind</b></em>' literal with the specified integer value. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @param value
     *            the integer value.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static StateSubactionKind get(int value) {
        switch (value) {
            case DO_VALUE:
                return DO;
            case ENTRY_VALUE:
                return ENTRY;
            case EXIT_VALUE:
                return EXIT;
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
    private StateSubactionKind(int value, String name, String literal) {
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

} // StateSubactionKind
