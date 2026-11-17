/*******************************************************************************
 * Copyright (c) 2026 Obeo.
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
package org.eclipse.syson.sysml.linker.tests.adapter;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringJoiner;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.BeforeClass;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Feature;
import org.omg.sysml.lang.sysml.Membership;
import org.omg.sysml.lang.sysml.Namespace;
import org.omg.sysml.lang.sysml.SysMLPackage;
import org.omg.sysml.lang.sysml.VisibilityKind;
import org.omg.sysml.logic.SysMLLogicStandaloneSetup;

/**
 * Tests the {@link Membership} index.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public abstract class AbstractSysMLIndexAdapterTests {

    /**
     * The root {@link Namespace} used for testing.
     */
    protected Namespace namespace;

    @BeforeClass
    public static void beforeAll() {
        SysMLLogicStandaloneSetup.doSetup();
        SysMLPackage.eINSTANCE.getName(); // make sure SysmlPackage is initialized
    }

    protected Namespace getNamespace() {
        return EcoreUtil.copy(this.namespace);
    }

    /**
     * Gets the {@link String} representation of the given namespaceIndex.
     *
     * @param namespaceIndex
     *            the namespaceIndex
     * @return the {@link String} representation of the given namespaceIndex
     */
    protected String toStringMembershipIndex(Map<Namespace, Map<VisibilityKind, Map<String, Set<Membership>>>> namespaceIndex) {
        StringBuilder builder = new StringBuilder();

        for (Entry<Namespace, Map<VisibilityKind, Map<String, Set<Membership>>>> nsEntry : namespaceIndex.entrySet()) {
            builder.append("<");
            builder.append(nsEntry.getKey().getDeclaredShortName());
            builder.append(">");
            builder.append(nsEntry.getKey().getDeclaredName());
            builder.append(":\n");
            for (Entry<VisibilityKind, Map<String, Set<Membership>>> visibilityEntry : nsEntry.getValue().entrySet()) {
                builder.append("  ");
                builder.append(visibilityEntry.getKey().getName());
                builder.append(":\n");
                for (Entry<String, Set<Membership>> nameEntry : visibilityEntry.getValue().entrySet()) {
                    builder.append("    ");
                    builder.append(nameEntry.getKey());
                    builder.append(": ");
                    StringJoiner joinder = new StringJoiner(", ", "", "\n");
                    for (Membership membership : nameEntry.getValue()) {
                        joinder.add(membership.getElementId());
                    }
                    builder.append(joinder.toString());
                }
            }
        }

        return builder.toString();
    }

    /**
     * Gets the {@link String} representation of the given namingFeaturesIndex.
     *
     * @param namingFeaturesIndex
     *            the namingFeaturesIndex
     * @return the {@link String} representation of the given namingFeaturesIndex
     */
    protected String toStringNamingFeaturesIndex(Map<Feature, Set<Membership>> namingFeaturesIndex) {
        StringBuilder builder = new StringBuilder();

        for (Entry<Feature, Set<Membership>> entry : namingFeaturesIndex.entrySet()) {
            builder.append("<");
            builder.append(entry.getKey().getDeclaredShortName());
            builder.append(">");
            builder.append(entry.getKey().getDeclaredName());
            builder.append(" (");
            builder.append(entry.getKey().getElementId());
            builder.append(")");
            builder.append(": ");
            StringJoiner joinder = new StringJoiner(", ", "", "\n");
            for (Element element : entry.getValue()) {
                joinder.add(element.getElementId());
            }
            builder.append(joinder.toString());
        }

        return builder.toString();
    }

    /**
     * Gets the {@link String} representation of the given namedMembershipIndex.
     *
     * @param map
     *            the namedMembershipIndex
     * @return the {@link String} representation of the given namedMembershipIndex
     */
    protected String toStringNamedMembershipIndex(Map<Membership, Feature> map) {
        StringBuilder builder = new StringBuilder();

        for (Entry<Membership, Feature> entry : map.entrySet()) {
            builder.append("<");
            builder.append(entry.getKey().getDeclaredShortName());
            builder.append(">");
            builder.append(entry.getKey().getDeclaredName());
            builder.append(" (");
            builder.append(entry.getKey().getElementId());
            builder.append(")");
            builder.append(": ");
            builder.append(entry.getValue().getElementId());
            builder.append("\n");
        }

        return builder.toString();
    }

    protected String toStringRedefinedIndex(Map<Namespace, Set<Membership>> redefinedIndex) {
        StringBuilder builder = new StringBuilder();

        for (Entry<Namespace, Set<Membership>> nsEntry : redefinedIndex.entrySet()) {
            builder.append(nsEntry.getKey().getDeclaredName());
            builder.append(": ");
            StringJoiner joinder = new StringJoiner(", ", "", "\n");
            for (Membership membership : nsEntry.getValue()) {
                joinder.add(membership.getElementId());
            }
            builder.append(joinder.toString());
        }

        return builder.toString();
    }

}
