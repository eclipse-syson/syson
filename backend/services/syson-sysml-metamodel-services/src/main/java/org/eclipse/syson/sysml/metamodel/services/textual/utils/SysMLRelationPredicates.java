/*******************************************************************************
 * Copyright (c) 2024, 2026 Obeo.
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
package org.eclipse.syson.sysml.metamodel.services.textual.utils;

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
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.VariantMembership;

/**
 * List of predicates to select Relationships.
 *
 * @author Arthur Daussy
 */
public class SysMLRelationPredicates {

    private final Predicate<Relationship> isNotUserDefineKeywordMember = r -> !this.isMemberElementA(r, MetadataUsage.class);

    private final Predicate<Relationship> isMembership = r -> r instanceof Membership;

    private final Predicate<Relationship> isImport = r -> r instanceof Import;

    private final Predicate<Relationship> isMetadataUsage = m -> this.isMemberElementA(m, MetadataUsage.class);

    private final Predicate<Relationship> isNonOccurrenceUsageMember = r -> this.isMemberElementA(r, Definition.class, ReferenceUsage.class,
            AttributeUsage.class, EnumerationUsage.class, BindingConnectorAsUsage.class, SuccessionAsUsage.class) || this.isExtendedUsage(r);


    // https://issues.omg.org/issues/KERML-307
    private final Predicate<Relationship> isAnnotatingElement = r -> this.isMemberElementA(r, AnnotatingElement.class)
            && !this.isMemberElementA(r, MetadataFeature.class);

    private final Predicate<Relationship> isDefinitionMember = r -> this.isMemberElementA(r, Definition.class, Package.class)
            || this.isAnnotatingElement.test(r) || this.isA(r, Dependency.class);

    private final Predicate<Relationship> isVariantUsageMember = r -> this.isA(r, VariantMembership.class);

    private final Predicate<Relationship> isStructureUsageElementMember = r -> this.isMemberElementA(r, OccurrenceUsage.class);

    private final Predicate<Relationship> isOccurrenceUsageMember = r -> this.isMemberElementA(r, OccurrenceUsage.class);

    private final Predicate<Relationship> isAliasMember = r -> r instanceof Membership membership && !(r instanceof OwningMembership)
            && (membership.getMemberName() != null || membership.getShortName() != null);

    private final Predicate<Relationship> isDefinitionBodyItemMember = this.isNotUserDefineKeywordMember.and(
            this.isDefinitionMember
                    .or(this.isVariantUsageMember)
                    .or(this.isNonOccurrenceUsageMember)
                    .or(this.isOccurrenceUsageMember)
                    .or(this.isAliasMember).or(this.isImport));

    public Predicate<Relationship> isNotUserDefineKeywordMember() {
        return this.isNotUserDefineKeywordMember;
    }

    public Predicate<Relationship> isMembership() {
        return this.isMembership;
    }

    public Predicate<Relationship> isImport() {
        return this.isImport;
    }

    public Predicate<Relationship> isMetadataUsage() {
        return this.isMetadataUsage;
    }

    public Predicate<Relationship> isNonOccurrenceUsageMember() {
        return this.isNonOccurrenceUsageMember;
    }

    public Predicate<Relationship> isAnnotatingElement() {
        return this.isAnnotatingElement;
    }

    public Predicate<Relationship> isDefinitionMember() {
        return this.isDefinitionMember;
    }

    public Predicate<Relationship> isVariantUsageMember() {
        return this.isVariantUsageMember;
    }

    public Predicate<Relationship> isStructureUsageElementMember() {
        return this.isStructureUsageElementMember;
    }

    public Predicate<Relationship> isOccurrenceUsageMember() {
        return this.isOccurrenceUsageMember;
    }

    public Predicate<Relationship> isAliasMember() {
        return this.isAliasMember;
    }

    public Predicate<Relationship> isDefinitionBodyItemMember() {
        return this.isDefinitionBodyItemMember;
    }

    private boolean isMemberElementA(Relationship r, Class<?>... types) {
        if (r instanceof Membership membership) {
            return this.isA(membership.getMemberElement(), types);
        }
        return false;
    }

    private boolean isA(Element e, Class<?>... types) {
        if (e == null) {
            return false;
        } else {
            return Stream.of(types).filter(t -> t.isInstance(e)).findAny().isPresent();
        }
    }

    private boolean isExtendedUsage(Relationship r) {
        return r instanceof Membership membership
                && membership.getMemberElement() != null
                && membership.getMemberElement().eClass() == SysmlPackage.eINSTANCE.getUsage();
    }
}
