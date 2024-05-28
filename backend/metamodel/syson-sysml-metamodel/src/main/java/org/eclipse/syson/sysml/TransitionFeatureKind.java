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
 * <!-- begin-user-doc --> A representation of the literals of the enumeration '<em><b>Transition Feature
 * Kind</b></em>', and utility methods for working with them. <!-- end-user-doc -->
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getTransitionFeatureKind()
 * @model
 * @generated
 */
public enum TransitionFeatureKind implements Enumerator {
    /**
     * The '<em><b>Effect</b></em>' literal object. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #EFFECT_VALUE
     * @generated
     * @ordered
     */
    EFFECT(0, "effect", "effect"),

    /**
     * The '<em><b>Guard</b></em>' literal object. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #GUARD_VALUE
     * @generated
     * @ordered
     */
    GUARD(1, "guard", "guard"),

    /**
     * The '<em><b>Trigger</b></em>' literal object. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #TRIGGER_VALUE
     * @generated
     * @ordered
     */
    TRIGGER(2, "trigger", "trigger");

    /**
     * The '<em><b>Effect</b></em>' literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #EFFECT
     * @model name="effect"
     * @generated
     * @ordered
     */
    public static final int EFFECT_VALUE = 0;

    /**
     * The '<em><b>Guard</b></em>' literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #GUARD
     * @model name="guard"
     * @generated
     * @ordered
     */
    public static final int GUARD_VALUE = 1;

    /**
     * The '<em><b>Trigger</b></em>' literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #TRIGGER
     * @model name="trigger"
     * @generated
     * @ordered
     */
    public static final int TRIGGER_VALUE = 2;

    /**
     * An array of all the '<em><b>Transition Feature Kind</b></em>' enumerators. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     */
    private static final TransitionFeatureKind[] VALUES_ARRAY = new TransitionFeatureKind[] {
            EFFECT,
            GUARD,
            TRIGGER,
    };

    /**
     * A public read-only list of all the '<em><b>Transition Feature Kind</b></em>' enumerators. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    public static final List<TransitionFeatureKind> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Transition Feature Kind</b></em>' literal with the specified literal value. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @param literal
     *            the literal.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static TransitionFeatureKind get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            TransitionFeatureKind result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Transition Feature Kind</b></em>' literal with the specified name. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param name
     *            the name.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static TransitionFeatureKind getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            TransitionFeatureKind result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Transition Feature Kind</b></em>' literal with the specified integer value. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the integer value.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static TransitionFeatureKind get(int value) {
        switch (value) {
            case EFFECT_VALUE:
                return EFFECT;
            case GUARD_VALUE:
                return GUARD;
            case TRIGGER_VALUE:
                return TRIGGER;
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
    private TransitionFeatureKind(int value, String name, String literal) {
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

} // TransitionFeatureKind
