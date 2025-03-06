/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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
package org.eclipse.syson.sysml.util;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.stream.Stream;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.Type;

/**
 * Utils class for tests.
 *
 * @author Arthur Daussy
 */
public class TestUtils {

    public static void assertContentEquals(List<? extends Element> actual, Element... expected) {
        assertEquals(Stream.of(expected).collect(toSet()), actual.stream().collect(toSet()), "Expecting :\n" + getLabel(Stream.of(expected)) + "\n but was \n" + getLabel(actual.stream()) + "\n");
        assertEquals(Stream.of(expected).count(), actual.size(), "Elements expecting :\n" + getLabel(Stream.of(expected)) + "\n but was \n" + getLabel(actual.stream()) + "\n");
    }

    public static void testInheritedFeature(Type toTest, Feature... expected) {
        assertContentEquals(toTest.getInheritedMembership(), Stream.of(expected).map(Feature::getOwningMembership).toArray(Element[]::new));
        assertContentEquals(toTest.inheritedMemberships(new BasicEList<>(), new BasicEList<>(), false), Stream.of(expected).map(Feature::getOwningMembership).toArray(Element[]::new));
        assertContentEquals(toTest.getInheritedFeature(), expected);
    }

    private static String getLabel(Stream<? extends Element> m) {
        return m.map(TestUtils::getLabel).collect(joining("\n"));
    }

    private static String getLabel(Element element) {
        final String label;
        if (element instanceof Membership membership) {
            label = getMembershipLabel(membership);
        } else {
            label = element.getDeclaredName();
        }
        return label;
    }

    private static String getMembershipLabel(Membership m) {
        if (m == null) {
            return null;
        }
        return m.eClass().getName() + " " + m.getMemberElement().getName();
    }

}
