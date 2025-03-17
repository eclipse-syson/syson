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
package org.eclipse.syson.sysml.textual.utils;

import java.util.function.Predicate;
import java.util.stream.Stream;

import org.eclipse.syson.sysml.AnnotatingElement;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.BindingConnectorAsUsage;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Dependency;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.EnumerationUsage;
import org.eclipse.syson.sysml.Import;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.MetadataFeature;
import org.eclipse.syson.sysml.MetadataUsage;
import org.eclipse.syson.sysml.OccurrenceUsage;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.SuccessionAsUsage;
import org.eclipse.syson.sysml.VariantMembership;

/**
 * List of predicates to select Relationships.
 *
 * @author Arthur Daussy
 */
public class SysMLRelationPredicates {

    public static final Predicate<Relationship> IS_NOT_USER_DEFINE_KEYWORD_MEMBER = r -> !isMemberElementA(r, MetadataUsage.class);

    public static final Predicate<Relationship> IS_MEMBERSHIP = r -> r instanceof Membership;

    public static final Predicate<Relationship> IS_IMPORT = r -> r instanceof Import;

    public static final Predicate<Relationship> IS_METADATA_USAGE = m -> isMemberElementA(m, MetadataUsage.class);

    // ExtendedUsage defined in the BNF is not implemented yet here
    public static final Predicate<Relationship> IS_NON_OCCURENCE_USAGE_MEMBER = r -> isMemberElementA(r, Definition.class, ReferenceUsage.class, AttributeUsage.class, EnumerationUsage.class,
            BindingConnectorAsUsage.class, SuccessionAsUsage.class);

    // https://issues.omg.org/issues/KERML-307
    public static final Predicate<Relationship> IS_ANNOTATING_ELEMENT = r -> isMemberElementA(r, AnnotatingElement.class) && !isMemberElementA(r, MetadataFeature.class);

    public static final Predicate<Relationship> IS_DEFINITION_MEMBER = r -> isMemberElementA(r, Definition.class, Package.class) || IS_ANNOTATING_ELEMENT.test(r) || isA(r, Dependency.class);

    public static final Predicate<Relationship> IS_VARIANT_USAGE_MEMBER = r -> isA(r, VariantMembership.class);

    public static final Predicate<Relationship> IS_STRUCTURE_USAGE_ELEMENT_MEMBER = r -> isMemberElementA(r, OccurrenceUsage.class);

    public static final Predicate<Relationship> IS_OCCURRENCE_USAGE_MEMBER = r -> isMemberElementA(r, OccurrenceUsage.class);

    public static final Predicate<Relationship> IS_ALIAS_MEMBER = r -> r instanceof Membership membership && !(r instanceof OwningMembership)
            && (membership.getMemberName() != null || membership.getShortName() != null);

    public static final Predicate<Relationship> IS_DEFINITION_BODY_ITEM_MEMBER = IS_NOT_USER_DEFINE_KEYWORD_MEMBER.and(
            IS_DEFINITION_MEMBER
                    .or(IS_VARIANT_USAGE_MEMBER)
                    .or(IS_NON_OCCURENCE_USAGE_MEMBER)
                    .or(IS_OCCURRENCE_USAGE_MEMBER)
                    .or(IS_ALIAS_MEMBER).or(IS_IMPORT));

    public static boolean isMemberElementA(Relationship r, Class<?>... types) {
        if (r instanceof Membership membership) {
            return isA(membership.getMemberElement(), types);
        }
        return false;
    }

    public static boolean isA(Element e, Class<?>... types) {
        if (e == null) {
            return false;
        } else {
            return Stream.of(types).filter(t -> t.isInstance(e)).findAny().isPresent();
        }
    }
}
