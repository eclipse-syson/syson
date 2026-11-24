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
package org.eclipse.syson.sysml.serializer.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.syson.sysml.parser.SysMLModelParser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Namespace;
import org.omg.sysml.lang.sysml.SysMLFactory;
import org.omg.sysml.logic.SysMLLogicStandaloneSetup;

/**
 * Resolves reference spellings through owned memberships as name resolution does, shadowing included.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
class ContainmentNameLookupTest {

    private static final String SHADOWED_PATH = "Q::A";

    private static final String SHADOWING_MODEL = """
            package P {
                package Q {
                    part def A;
                }
                package Inner {
                    package Q;
                    part x : Q::A;
                }
            }
            """;

    @BeforeAll
    static void setUpModel() {
        SysMLLogicStandaloneSetup.doSetup();
    }

    /** An inner namespace binding the qualifier shadows the outer one: the lookup must not retry the outer path. */
    @Test
    void stopsAtTheInnermostBindingOfTheQualifier() throws IOException {
        final Namespace root = parse(SHADOWING_MODEL);
        final Element x = named(root, "x");

        final ContainmentNameLookup.Lookup shadowed = new ContainmentNameLookup().resolve(x, SHADOWED_PATH);
        final ContainmentNameLookup.Lookup qualified = new ContainmentNameLookup().resolve(x, "P::Q::A");

        assertNull(shadowed.membership());
        assertTrue(shadowed.definite());
        assertSame(named(root, "A"), StoredModel.memberElement(qualified.membership()));
    }

    /** A qualifier bound to a type cannot decide a missing member, which may be inherited. */
    @Test
    void cannotDecideAMemberMissingFromAType() throws IOException {
        final Namespace root = parse("""
                package P {
                    part def Q;
                    part x : Q::A;
                }
                """);

        final ContainmentNameLookup.Lookup lookup = new ContainmentNameLookup().resolve(named(root, "x"), SHADOWED_PATH);

        assertNull(lookup.membership());
        assertFalse(lookup.definite());
    }

    /** A missing context or a detached element must give an undecided lookup instead of failing. */
    @Test
    void toleratesMissingContexts() {
        final ContainmentNameLookup lookup = new ContainmentNameLookup();

        assertEquals(ContainmentNameLookup.Lookup.UNKNOWN, lookup.resolve(null, "$::A"));
        assertEquals(ContainmentNameLookup.Lookup.UNKNOWN, lookup.resolve(null, SHADOWED_PATH));
        assertEquals(ContainmentNameLookup.Lookup.UNKNOWN, lookup.resolve(SysMLFactory.eINSTANCE.createPartUsage(), "$::A"));
    }

    private static Namespace parse(final String source) throws IOException {
        return new SysMLModelParser().parseRootNamespace(new ByteArrayInputStream(source.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8).model();
    }

    private static Element named(final Namespace root, final String name) {
        final var contents = root.eAllContents();
        while (contents.hasNext()) {
            final EObject next = contents.next();
            if (next instanceof final Element element && name.equals(element.getDeclaredName())) {
                return element;
            }
        }
        return fail("Missing " + name);
    }
}
