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
package org.eclipse.syson.sysml.serializer.names;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.syson.sysml.parser.DanglingLink;
import org.eclipse.syson.sysml.parser.ISysMLParseResult;
import org.eclipse.syson.sysml.serializer.SerializationRequest;
import org.eclipse.syson.sysml.serializer.SerializerRoundTripTestSupport;
import org.eclipse.syson.sysml.serializer.SysMLSerializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Feature;
import org.omg.sysml.lang.sysml.FeatureTyping;
import org.omg.sysml.lang.sysml.Membership;
import org.omg.sysml.lang.sysml.Namespace;
import org.omg.sysml.lang.sysml.OwningMembership;
import org.omg.sysml.lang.sysml.Redefinition;
import org.omg.sysml.lang.sysml.Relationship;
import org.omg.sysml.lang.sysml.SysMLFactory;
import org.omg.sysml.lang.sysml.SysMLPackage;
import org.omg.sysml.lang.sysml.impl.FeatureImpl;
import org.omg.sysml.logic.SysMLLogicStandaloneSetup;
import org.omg.sysml.util.ElementUtil;

/**
 * Tests reference-name rendering without invoking costly derived collections.
 *
 * @author Obeo
 */
class NameRendererTest {

    private final NameRenderer renderer = new NameRenderer();

    @BeforeAll
    static void setUpModel() {
        SysMLLogicStandaloneSetup.doSetup();
        SysMLPackage.eINSTANCE.getName();
    }

    /**
     * For the two same-named members in this scenario, the first must use the expected qualified name and the second
     * must use the expected simple-name fallback.
     */
    @Test
    void rendersNestedQualifiedNamesAndKeepsDuplicateNamesUnqualified() {
        final Namespace root = SysMLFactory.eINSTANCE.createPackage();
        final Namespace nested = namedPackage("Nested");
        addOwnedMember(root, nested);
        final Namespace first = namedPackage("duplicate");
        final Namespace second = namedPackage("duplicate");
        addOwnedMember(nested, first);
        addOwnedMember(nested, second);

        assertEquals("Nested::duplicate",
                this.renderer.referenceName(first));
        assertEquals("duplicate", this.renderer.referenceName(second));
    }

    /**
     * An incomplete feature whose name computation fails must not prevent another member from being named; its own
     * rendered name must be empty.
     */
    @Test
    void rendersANamedMemberBesideAnIncompleteFeature() {
        final Namespace root = SysMLFactory.eINSTANCE.createPackage();
        final RuntimeException missingName = new NullPointerException("Incomplete feature");
        final Feature incomplete = new FeatureImpl() {
            @Override
            public String getName() {
                throw missingName;
            }
        };
        addOwnedMember(root, incomplete);
        final Namespace target = namedPackage("Target");
        addOwnedMember(root, target);

        assertEquals("Target", this.renderer.referenceName(target));
        assertEquals("", this.renderer.referenceName(incomplete));
    }

    /**
     * All 10,000 name requests for the last member of a 10,000-member namespace must return the expected name within
     * two seconds.
     */
    @Test
    void qualifiedNameScalesWithLargeNamespaces() {
        final Namespace root = SysMLFactory.eINSTANCE.createPackage();
        Element target = null;
        for (int index = 0; index < 10_000; index++) {
            target = namedPackage("Member" + index);
            addOwnedMember(root, target);
        }
        final Element lastMember = target;

        assertTimeout(Duration.ofSeconds(2), () -> {
            for (int iteration = 0; iteration < 10_000; iteration++) {
                assertEquals("Member9999",
                        this.renderer.referenceName(lastMember));
            }
        });
    }

    /**
     * A feature without a declared name must use the effective name inherited through its redefinition.
     */
    @Test
    void rendersAQualifiedEffectiveNameInheritedByRedefinition() {
        final Namespace root = SysMLFactory.eINSTANCE.createPackage();
        final Feature original = SysMLFactory.eINSTANCE.createFeature();
        original.setDeclaredName("inheritedName");
        final Feature redefining = SysMLFactory.eINSTANCE.createFeature();
        final Redefinition redefinition = SysMLFactory.eINSTANCE
                .createRedefinition();
        redefinition.setRedefiningFeature(redefining);
        redefinition.setRedefinedFeature(original);
        redefining.getOwnedRelationship().add(redefinition);
        addOwnedMember(root, redefining);

        assertEquals("inheritedName",
                this.renderer.referenceName(redefining));
    }

    /**
     * The contextual name provider must receive the first relationship referencing the target in the current subtree.
     */
    @Test
    void contextualNameUsesTheFirstReferencingRelationship() {
        final Namespace root = SysMLFactory.eINSTANCE.createPackage();
        final Namespace nested = namedPackage("Nested");
        addOwnedMember(root, nested);
        final Element target = namedPackage("Target");
        final Membership reference = SysMLFactory.eINSTANCE
                .createMembership();
        reference.setMemberElement(target);
        nested.getOwnedRelationship().add(reference);
        final AtomicReference<Element> observedReferenceContext =
                new AtomicReference<>();
        final NameRenderer contextualRenderer = new NameRenderer(
                (element, context) -> {
                    observedReferenceContext.set(context);
                    return "contextual";
                });

        contextualRenderer.beginContext(root);
        try {
            assertEquals("contextual",
                    contextualRenderer.referenceName(target));
        } finally {
            contextualRenderer.endContext();
        }

        assertSame(reference, observedReferenceContext.get());
    }

    /**
     * Implied relationships are never written, so neither they nor their subtrees are reference sites: the contextual
     * name resolver must receive the explicit typing of {@code p}, not the alias of the omitted helper declared before
     * it, and the name valid inside that helper must not be written.
     */
    @Test
    void contextualNameSkipsImpliedSubtrees() throws IOException {
        final ISysMLParseResult<Namespace> parseResult = SerializerRoundTripTestSupport.parseTestCase("edited/implied-subtree-reference.sysml");
        final Element pkg = member(parseResult.model(), 0);
        final Element target = member(pkg, 0);
        final Element part = member(pkg, 1);
        parseResult.danglingLinks().forEach(link -> link.context().eSet(link.reference(), target));
        final Relationship helperMembership = part.getOwnedRelationship().get(1);
        final Element helper = member(part, 1);
        helperMembership.setIsImplied(true);
        part.getOwnedRelationship().move(0, helperMembership);

        final String text = new SysMLSerializer().serialize(SerializationRequest.forElement(parseResult.model())
                .withContextualNames((element, context) -> {
                    String name = "P::T";
                    if (context.eContainer() == helper) {
                        name = "LocalT";
                    }
                    return name;
                }));

        assertTrue(text.contains("part p : P::T"), text);
    }

    /**
     * Reference-context lookup must recognize distinct objects that carry the same elementId.
     */
    @Test
    void contextualNameMatchesEquivalentElementIds() {
        final Namespace root = SysMLFactory.eINSTANCE.createPackage();
        final Element referenced = namedPackage("Referenced");
        final Element equivalent = namedPackage("Equivalent");
        ElementUtil.setElementIdOf(referenced, "shared-id");
        ElementUtil.setElementIdOf(equivalent, "shared-id");
        final Membership reference = SysMLFactory.eINSTANCE
                .createMembership();
        reference.setMemberElement(referenced);
        root.getOwnedRelationship().add(reference);
        final AtomicReference<Element> observedReferenceContext =
                new AtomicReference<>();
        final NameRenderer contextualRenderer = new NameRenderer(
                (element, context) -> {
                    observedReferenceContext.set(context);
                    return "contextual";
                });

        contextualRenderer.beginContext(root);
        try {
            contextualRenderer.referenceName(equivalent);
        } finally {
            contextualRenderer.endContext();
        }

        assertSame(reference, observedReferenceContext.get());
    }

    /**
     * All 10,000 lookups in the large context must return the expected names within two seconds, both with default
     * rendering and with a contextual name provider.
     */
    @Test
    void referenceLookupScalesWithLargeContexts() {
        final Namespace root = SysMLFactory.eINSTANCE.createPackage();
        for (int index = 0; index < 10_000; index++) {
            addOwnedMember(root, namedPackage("Member" + index));
        }
        final Element target = namedPackage("Target");
        final Membership reference = SysMLFactory.eINSTANCE
                .createMembership();
        reference.setMemberElement(target);
        root.getOwnedRelationship().add(reference);
        final NameRenderer defaultRenderer = new NameRenderer();
        final NameRenderer contextualRenderer = new NameRenderer(
                (element, context) -> "contextual");

        defaultRenderer.beginContext(root);
        try {
            assertTimeout(Duration.ofSeconds(2), () -> {
                for (int iteration = 0; iteration < 10_000; iteration++) {
                    assertEquals("Target",
                            defaultRenderer.referenceName(target));
                }
            });
        } finally {
            defaultRenderer.endContext();
        }
        contextualRenderer.beginContext(root);
        try {
            assertTimeout(Duration.ofSeconds(2), () -> {
                for (int iteration = 0; iteration < 10_000; iteration++) {
                    assertEquals("contextual",
                            contextualRenderer.referenceName(target));
                }
            });
        } finally {
            contextualRenderer.endContext();
        }
    }

    /** References must use short names when neither the target nor its namespace has a long name. */
    @Test
    void rendersQualifiedShortNames() {
        final Namespace root = SysMLFactory.eINSTANCE.createNamespace();
        final Namespace nested = SysMLFactory.eINSTANCE.createPackage();
        nested.setDeclaredShortName("P");
        addOwnedMember(root, nested);
        final Feature target = SysMLFactory.eINSTANCE.createFeature();
        target.setDeclaredShortName("short name");
        addOwnedMember(nested, target);

        assertEquals("P::'short name'", this.renderer.referenceName(target));
    }

    /**
     * Name lookup matches both names and short names, aliases included: a target whose name is already exposed by an
     * earlier member, as its short name or as an alias name, must be qualified by its own short name instead.
     */
    @Test
    void qualifiesByShortNameWhenTheNameIsShadowed() throws IOException {
        final Namespace root = SerializerRoundTripTestSupport.parseTestCase("edited/short-name-shadowing.sysml").model();

        assertEquals("P::S", this.renderer.referenceName(member(member(root, 0), 1)));
        assertEquals("P2::S", this.renderer.referenceName(member(member(root, 1), 2)));
    }

    /**
     * The URI fragment that names an unresolved proxy is a reference spelling like any other: a keyword or punctuation
     * must be quoted, while qualification is kept.
     */
    @Test
    void escapesProxyFragments() throws IOException {
        final ISysMLParseResult<Namespace> parseResult = SerializerRoundTripTestSupport.parseTestCase("edited/proxy-fragment-names.sysml");
        final Map<String, String> fragments = Map.of("'part'", "part", "'array-membership'", "array-membership", "P::'part'", "P::part");
        for (final DanglingLink link : parseResult.danglingLinks()) {
            link.context().eSet(link.reference(), proxy(fragments.get(link.targets().get(0))));
        }
        final List<Element> types = parseResult.model().getOwnedRelationship().subList(3, 6).stream()
                .map(membership -> ((FeatureTyping) membership.getOwnedRelatedElement().get(0).getOwnedRelationship().get(0)).getType())
                .map(Element.class::cast)
                .toList();

        assertEquals(List.of("'part'", "'array-membership'", "P::'part'"), types.stream().map(this.renderer::referenceName).toList());
    }

    /** Detached targets still have a usable short name, including names inherited through redefinition. */
    @Test
    void rendersDetachedAndInheritedShortNames() {
        final Feature target = SysMLFactory.eINSTANCE.createFeature();
        target.setDeclaredShortName("short name");
        final Feature redefining = SysMLFactory.eINSTANCE.createFeature();
        final Redefinition redefinition = SysMLFactory.eINSTANCE.createRedefinition();
        redefinition.setRedefiningFeature(redefining);
        redefinition.setRedefinedFeature(target);
        redefining.getOwnedRelationship().add(redefinition);

        assertEquals("'short name'", this.renderer.referenceName(target));
        assertEquals("'short name'", this.renderer.referenceName(redefining));
    }

    private static Namespace namedPackage(final String name) {
        final Namespace namespace = SysMLFactory.eINSTANCE.createPackage();
        namespace.setDeclaredName(name);
        return namespace;
    }

    /** Returns the element owned through the owned relationship at {@code index} of {@code owner}. */
    private static Element member(final Element owner, final int index) {
        return owner.getOwnedRelationship().get(index).getOwnedRelatedElement().get(0);
    }

    private static Element proxy(final String fragment) {
        final Element proxy = SysMLFactory.eINSTANCE.createPartDefinition();
        ((InternalEObject) proxy).eSetProxyURI(URI.createURI("unloaded:/library.sysml#" + fragment));
        return proxy;
    }

    private static void addOwnedMember(final Namespace owner,
            final Element member) {
        final OwningMembership membership = SysMLFactory.eINSTANCE
                .createOwningMembership();
        membership.setOwnedMemberElement(member);
        owner.getOwnedRelationship().add(membership);
    }
}
