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

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.syson.sysml.serializer.names.NameEscaper;
import org.eclipse.syson.sysml.serializer.tokens.ReferenceNameSyntax;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Import;
import org.omg.sysml.lang.sysml.Membership;
import org.omg.sysml.lang.sysml.Namespace;
import org.omg.sysml.lang.sysml.Relationship;
import org.omg.sysml.lang.sysml.Type;

/**
 * Finds what a reference spelling designates by following owned memberships only.
 * <p>
 * This replaces {@code Namespace.resolve}, which the SysML logic computes through imports and specializations and
 * which therefore resolves proxies and attaches the logic's caches. A spelling that is only visible through an import
 * or an inherited membership is not found here; callers treat "not found" as "cannot tell" and fall back to comparing
 * the spelling with the target's qualified name, or to the caller's {@code ReferencePathResolver}.
 * </p>
 * <p>
 * The memberships of each namespace are indexed by name the first time a lookup visits it, so that a lookup costs one
 * map access per enclosing namespace instead of a scan of its members; the SysML logic keeps a similar index for
 * {@code Namespace.resolve}. An instance belongs to one serialization run, during which the model does not change.
 * </p>
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class ContainmentNameLookup {

    /** The first explicit membership of each name and short name, per visited namespace. */
    private final Map<Namespace, Map<String, Membership>> membershipsByName = new IdentityHashMap<>();

    /**
     * Resolves a qualified spelling as name resolution does, but through owned memberships only. Its first segment is
     * bound in the innermost namespace enclosing {@code context} that owns a member with that name, and the rest of
     * the path is followed from that binding only: an outer namespace with the same name is never retried, since the
     * inner one shadows it. A leading {@code $} starts from the root namespace.
     *
     * @param context
     *            the element where the spelling is written, or {@code null}
     * @param spelling
     *            the reference spelling, possibly quoted and qualified
     * @return the designated membership, a definite failure, or a lookup that cannot tell
     */
    public Lookup resolve(final Element context, final String spelling) {
        Lookup lookup = Lookup.UNKNOWN;
        if (context != null && spelling != null) {
            final List<String> segments = ReferenceNameSyntax.splitOutsideQuotes(spelling, SysMLTokens.NAMESPACE_SEPARATOR);
            if (segments.stream().noneMatch(String::isBlank)) {
                lookup = this.resolveSegments(context, segments);
            }
        }
        return lookup;
    }

    /** Resolves non-blank segments from the root namespace after {@code $}, else from the qualifier's binding. */
    private Lookup resolveSegments(final Element context, final List<String> segments) {
        final List<String> names = segments.stream().map(ContainmentNameLookup::unquoted).toList();
        Lookup lookup = Lookup.UNKNOWN;
        if (!SysMLTokens.ROOT_NAMESPACE_MARKER.equals(segments.get(0))) {
            final Namespace binding = this.innermostBinding(context, names.get(0));
            if (binding != null) {
                lookup = this.resolvePath(binding, names);
            }
        } else if (root(context) instanceof final Namespace rootNamespace && segments.size() > 1) {
            lookup = this.resolvePath(rootNamespace, names.subList(1, names.size()));
        }
        return lookup;
    }

    /** Returns the innermost namespace enclosing {@code context} that owns a member named {@code name}, or null. */
    private Namespace innermostBinding(final Element context, final String name) {
        Namespace binding = null;
        for (EObject scope = context; binding == null && scope != null; scope = scope.eContainer()) {
            if (scope instanceof final Namespace namespace && this.ownedMembership(namespace, name) != null) {
                binding = namespace;
            }
        }
        return binding;
    }

    /**
     * Follows a path whose first segment is looked up in {@code namespace}. A segment missing from a namespace is a
     * definite failure only when owned memberships are all that namespace shows: a type also shows inherited members
     * and an import adds imported ones, which this lookup does not follow.
     */
    private Lookup resolvePath(final Namespace namespace, final List<String> names) {
        Namespace current = namespace;
        Lookup lookup = null;
        int index = 0;
        while (lookup == null) {
            final Membership membership = this.ownedMembership(current, names.get(index));
            if (membership == null) {
                lookup = Lookup.notFound(this.showsOwnedMembershipsOnly(current));
            } else if (index == names.size() - 1) {
                lookup = Lookup.found(membership);
            } else if (StoredModel.memberElement(membership) instanceof final Namespace next && !next.eIsProxy()) {
                current = next;
                index++;
            } else {
                lookup = Lookup.UNKNOWN;
            }
        }
        return lookup;
    }

    /** Tells whether the members of a namespace are exactly its owned memberships: no import and no inheritance. */
    private boolean showsOwnedMembershipsOnly(final Namespace namespace) {
        return !(namespace instanceof Type) && namespace.getOwnedRelationship().stream().noneMatch(Import.class::isInstance);
    }

    /** Returns the first explicit membership of the namespace whose member name or short name is {@code name}. */
    private Membership ownedMembership(final Namespace namespace, final String name) {
        return this.membershipsByName.computeIfAbsent(namespace, ContainmentNameLookup::indexMemberships).get(name);
    }

    /** Indexes the explicit memberships of a namespace by member name and short name, keeping the first of each. */
    private static Map<String, Membership> indexMemberships(final Namespace namespace) {
        final Map<String, Membership> memberships = new HashMap<>();
        for (final Relationship relationship : namespace.getOwnedRelationship()) {
            if (!relationship.isImplied() && relationship instanceof final Membership membership) {
                final String name = StoredModel.memberName(membership);
                if (name != null) {
                    memberships.putIfAbsent(name, membership);
                }
                final String shortName = StoredModel.memberShortName(membership);
                if (shortName != null) {
                    memberships.putIfAbsent(shortName, membership);
                }
            }
        }
        return memberships;
    }

    /**
     * Returns the name a segment spells: the segment itself, or the content of an unrestricted name with its escape
     * sequences decoded. Decoding the segment once lets every member name be compared as is.
     */
    private static String unquoted(final String segment) {
        final String delimiter = SysMLTokens.UNRESTRICTED_NAME_DELIMITER;
        if (segment.length() < 2 || !segment.startsWith(delimiter) || !segment.endsWith(delimiter)) {
            return segment;
        }
        final StringBuilder name = new StringBuilder(segment.length());
        final int end = segment.length() - 1;
        int index = 1;
        while (index < end) {
            final char character = segment.charAt(index);
            if (character == '\\' && index + 1 < end) {
                name.append(NameEscaper.unescapedCharacter(segment.charAt(index + 1)));
                index += 2;
            } else {
                name.append(character);
                index++;
            }
        }
        return name.toString();
    }

    private static EObject root(final Element element) {
        EObject root = element;
        while (root != null && root.eContainer() != null) {
            root = root.eContainer();
        }
        return root;
    }

    /**
     * The outcome of a lookup: the designated membership; or none, either definitely, when the path fails in a
     * namespace whose members are all owned, or because containment alone cannot tell.
     *
     * @param membership
     *            the designated membership, or {@code null}
     * @param definite
     *            whether a missing membership proves that the spelling designates nothing from its context
     */
    public record Lookup(Membership membership, boolean definite) {

        /** A lookup that containment alone cannot decide, for instance through an import or an inherited member. */
        public static final Lookup UNKNOWN = new Lookup(null, false);

        static Lookup found(final Membership membership) {
            return new Lookup(membership, true);
        }

        static Lookup notFound(final boolean definite) {
            return new Lookup(null, definite);
        }
    }
}
