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
package org.eclipse.syson.sysml.serializer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.eclipse.syson.sysml.parser.DanglingLink;
import org.eclipse.syson.sysml.serializer.model.DanglingReferenceIndex;
import org.eclipse.syson.sysml.serializer.model.ModelReference;
import org.eclipse.syson.sysml.serializer.model.SerializerModelQueries;
import org.eclipse.syson.sysml.serializer.names.NameRenderer;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.omg.sysml.lang.sysml.Dependency;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Namespace;
import org.omg.sysml.lang.sysml.OwningMembership;
import org.omg.sysml.lang.sysml.PartDefinition;
import org.omg.sysml.lang.sysml.SysMLFactory;
import org.omg.sysml.lang.sysml.SysMLPackage;
import org.omg.sysml.logic.SysMLLogicStandaloneSetup;

/**
 * Preserves dependency endpoints when only some targets are resolved.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public class DependencyReferenceSerializationTest {

    private static final String KNOWN = "Known";

    private static final String WHITESPACE_REGEX = "\\s+";

    static {
        SysMLLogicStandaloneSetup.doSetup();
    }

    /**
     * Partially resolved dependencies must retain every client and supplier, including unresolved names and their
     * original order, in both modes.
     */
    @ParameterizedTest
    @EnumSource(SerializationMode.class)
    void preservesEveryEndpointOfPartiallyResolvedDependencies(final SerializationMode mode) {
        final var root = SysMLFactory.eINSTANCE.createNamespace();
        final var known = createPartDefinition(root, "Known");
        final var dependency = createDependency(root, List.of(known), List.of(known));
        final var references = DanglingReferenceIndex.from(Set.of(
                new DanglingLink(dependency, SysMLPackage.eINSTANCE.getDependency_Client(),
                        SysMLPackage.eINSTANCE.getElement(), List.of("Missing", KNOWN)),
                new DanglingLink(dependency, SysMLPackage.eINSTANCE.getDependency_Supplier(),
                        SysMLPackage.eINSTANCE.getElement(), List.of(KNOWN, "Missing"))));

        assertEquals("part def Known; dependency from Missing, Known to Known, Missing;", serialize(root, references, mode));
    }

    /**
     * A renamed target of a partially resolved dependency must be written with its current name, next to the
     * retained unresolved names.
     */
    @ParameterizedTest
    @EnumSource(SerializationMode.class)
    void namesTheRenamedTargetOfAPartiallyResolvedDependency(final SerializationMode mode) {
        final var root = SysMLFactory.eINSTANCE.createNamespace();
        final var known = createPartDefinition(root, KNOWN);
        final var dependency = createDependency(root, List.of(known), List.of(known));
        final var references = DanglingReferenceIndex.from(Set.of(
                new DanglingLink(dependency, SysMLPackage.eINSTANCE.getDependency_Client(),
                        SysMLPackage.eINSTANCE.getElement(), List.of("Missing", KNOWN)),
                new DanglingLink(dependency, SysMLPackage.eINSTANCE.getDependency_Supplier(),
                        SysMLPackage.eINSTANCE.getElement(), List.of(KNOWN))));
        known.setDeclaredName("Renamed");

        assertEquals("part def Renamed; dependency from Missing, Renamed to Renamed;", serialize(root, references, mode));
    }

    /**
     * Each installed target of a partially resolved dependency must keep the spelling that designates it, even when
     * several targets share the same last name segment.
     */
    @ParameterizedTest
    @EnumSource(SerializationMode.class)
    void pairsSameLeafTargetsWithTheirOwnSpelling(final SerializationMode mode) {
        final var root = SysMLFactory.eINSTANCE.createNamespace();
        final var first = SysMLFactory.eINSTANCE.createPackage();
        first.setDeclaredName("P");
        addOwnedMember(root, first);
        final var second = SysMLFactory.eINSTANCE.createPackage();
        second.setDeclaredName("Q");
        addOwnedMember(root, second);
        final var firstX = createPartDefinition(first, "X");
        final var secondX = createPartDefinition(second, "X");
        final var dependency = createDependency(root, List.of(firstX, secondX), List.of(firstX));
        final var references = DanglingReferenceIndex.from(Set.of(
                new DanglingLink(dependency, SysMLPackage.eINSTANCE.getDependency_Client(),
                        SysMLPackage.eINSTANCE.getElement(), List.of("Q::X", "Missing", "P::X")),
                new DanglingLink(dependency, SysMLPackage.eINSTANCE.getDependency_Supplier(),
                        SysMLPackage.eINSTANCE.getElement(), List.of("P::X"))));
        dependency.getClient().move(0, secondX);

        final List<ModelReference> clients = new SerializerModelQueries(mode == SerializationMode.ROUND_TRIP).referenceTargets()
                .referenceTargets(dependency, SysMLPackage.eINSTANCE.getDependency_Client().getName(), references,
                        new NameRenderer()::referenceName);

        assertEquals("package P { part def X; } package Q { part def X; } dependency from Q::X, Missing, P::X to P::X;",
                serialize(root, references, mode));
        assertEquals(Arrays.asList(secondX, null, firstX), clients.stream().map(ModelReference::element).toList());
    }

    private static String serialize(final Namespace root, final DanglingReferenceIndex references, final SerializationMode mode) {
        return new SysMLSerializer().serialize(SerializationRequest.forElement(root)
                .withOptions(SerializationOptions.defaults().withMode(mode))
                .withDanglingReferences(references))
                .replaceAll(WHITESPACE_REGEX, " ").trim();
    }

    private static PartDefinition createPartDefinition(final Namespace owner, final String name) {
        final var definition = SysMLFactory.eINSTANCE.createPartDefinition();
        definition.setDeclaredName(name);
        addOwnedMember(owner, definition);
        return definition;
    }

    private static Dependency createDependency(final Namespace owner, final List<Element> clients, final List<Element> suppliers) {
        final var dependency = SysMLFactory.eINSTANCE.createDependency();
        dependency.getClient().addAll(clients);
        dependency.getSupplier().addAll(suppliers);
        addOwnedMember(owner, dependency);
        return dependency;
    }

    private static OwningMembership addOwnedMember(final Namespace namespace,
            final Element member) {
        final OwningMembership membership =
                SysMLFactory.eINSTANCE.createOwningMembership();
        membership.setOwnedMemberElement(member);
        namespace.getOwnedRelationship().add(membership);
        return membership;
    }
}
