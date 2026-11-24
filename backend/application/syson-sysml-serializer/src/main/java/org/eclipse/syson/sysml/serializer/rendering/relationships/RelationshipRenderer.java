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
package org.eclipse.syson.sysml.serializer.rendering.relationships;

import java.util.List;

import org.eclipse.syson.sysml.serializer.model.ElementSyntaxQueries;
import org.eclipse.syson.sysml.serializer.model.ModelNavigation;
import org.eclipse.syson.sysml.serializer.rendering.declarations.DeclarationRenderer;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.Expose;
import org.omg.sysml.lang.sysml.Import;
import org.omg.sysml.lang.sysml.Membership;
import org.omg.sysml.lang.sysml.Relationship;
import org.omg.sysml.lang.sysml.RequirementConstraintMembership;
import org.omg.sysml.lang.sysml.StateSubactionMembership;
import org.omg.sysml.lang.sysml.SubjectMembership;
import org.omg.sysml.lang.sysml.SysMLPackage;

/**
 * Entry point for body relationships: picks the syntax a relationship's metaclass implies (import, annotation, a
 * keyword-introduced membership such as {@code subject} or {@code variant}, or a plain membership) and delegates to the
 * renderer that owns it.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class RelationshipRenderer {

    private final DeclarationRenderer declarations;

    private final ElementSyntaxQueries syntax;

    private final MembershipSyntaxRenderer memberships;

    private final RequirementRenderer requirementRenderer;

    private final ImportRenderer importRenderer;

    private final MembershipRenderer membershipRenderer;

    private final ModelNavigation navigation;

    /**
     * Creates the dispatcher over the relationship renderers of this package.
     *
     * @param declarations
     *            writes promoted user keywords
     * @param syntax
     *            detects promoted keyword metadata
     * @param memberships
     *            writes keyword-introduced memberships and annotations
     * @param requirementRenderer
     *            writes {@code verify}, {@code require} and {@code assume} memberships
     * @param importRenderer
     *            writes imports and exposes
     * @param membershipRenderer
     *            writes plain memberships and aliases
     * @param navigation
     *            navigates owned elements
     */
    public RelationshipRenderer(final DeclarationRenderer declarations, final ElementSyntaxQueries syntax,
            final MembershipSyntaxRenderer memberships, final RequirementRenderer requirementRenderer,
            final ImportRenderer importRenderer, final MembershipRenderer membershipRenderer,
            final ModelNavigation navigation) {
        this.declarations = declarations;
        this.syntax = syntax;
        this.memberships = memberships;
        this.requirementRenderer = requirementRenderer;
        this.importRenderer = importRenderer;
        this.membershipRenderer = membershipRenderer;
        this.navigation = navigation;
    }

    /**
     * Writes one body relationship with the syntax its metaclass implies, falling back to plain membership rendering.
     * User keywords promoted from the relationship are written first; a relationship whose member is itself such a
     * keyword produces nothing more. Non-membership relationships with no specific syntax produce no text.
     * {@code siblings} and {@code relationshipIndex} give the original body order, which membership rendering uses to
     * look at neighbouring members.
     *
     * @param relationship
     *            the body relationship
     * @param siblings
     *            the body relationships of the owner, in order
     * @param relationshipIndex
     *            the index of {@code relationship} in {@code siblings}
     */
    public void serializeRelationship(final Relationship relationship, final List<Relationship> siblings,
            final int relationshipIndex) {
        if (!(relationship instanceof Expose)) {
            this.declarations.serializeUserKeywords(relationship);
        }
        final boolean handled = this.ownsPromotedKeywordMetadata(relationship)
                || this.serializeNonMembershipRelationship(relationship)
                || this.serializeCaseOrRequirementMembership(relationship)
                || this.serializeOtherKeywordMembership(relationship);
        if (!handled && relationship instanceof final Membership membership) {
            this.membershipRenderer.serializeMembership(membership, siblings, relationshipIndex);
        }
    }

    /** Promoted metadata was written as a {@code #Keyword} prefix, not as a separate body declaration. */
    private boolean ownsPromotedKeywordMetadata(final Relationship relationship) {
        return this.navigation.ownedRelatedElements(relationship).stream()
                .anyMatch(this.syntax::isPromotedUserKeywordMetadata);
    }

    /** Writes imports, exposes and annotations; returns false with nothing written for other relationships. */
    private boolean serializeNonMembershipRelationship(final Relationship relationship) {
        boolean handled = true;
        if (relationship instanceof final Import importRelationship) {
            this.importRenderer.serializeImport(importRelationship);
        } else if (this.navigation.isMetaclass(relationship, SysMLPackage.eINSTANCE.getAnnotation())) {
            this.memberships.serializeAnnotation(relationship);
        } else {
            handled = false;
        }
        return handled;
    }

    /**
     * Writes the keyword-introduced members of cases, requirements and viewpoints ({@code subject}, {@code actor},
     * {@code stakeholder}, {@code objective}, {@code verify}, {@code frame}, {@code require}/{@code assume}); returns
     * false with nothing written for other relationships.
     */
    private boolean serializeCaseOrRequirementMembership(final Relationship relationship) {
        boolean handled = true;
        if (relationship instanceof final SubjectMembership subjectMembership) {
            this.memberships.serializeCaseFeatureMembership(SysMLTokens.SUBJECT_KEYWORD, subjectMembership);
        } else if (this.navigation.isMetaclass(relationship, SysMLPackage.eINSTANCE.getActorMembership())) {
            this.memberships.serializeCaseFeatureMembership(SysMLTokens.ACTOR_KEYWORD, relationship);
        } else if (this.navigation.isMetaclass(relationship, SysMLPackage.eINSTANCE.getStakeholderMembership())) {
            this.memberships.serializeCaseFeatureMembership(SysMLTokens.STAKEHOLDER_KEYWORD, relationship);
        } else if (this.navigation.isMetaclass(relationship, SysMLPackage.eINSTANCE.getObjectiveMembership())) {
            this.memberships.serializeCaseFeatureMembership(SysMLTokens.OBJECTIVE_KEYWORD, relationship);
        } else if (this.navigation.isMetaclass(relationship,
                SysMLPackage.eINSTANCE.getRequirementVerificationMembership())) {
            this.requirementRenderer.serializeRequirementVerificationMembership(relationship);
        } else if (this.navigation.isMetaclass(relationship, SysMLPackage.eINSTANCE.getFramedConcernMembership())) {
            this.memberships.serializeFramedConcernMembership(relationship);
        } else if (this.navigation.isMetaclass(relationship,
                SysMLPackage.eINSTANCE.getRequirementConstraintMembership())) {
            this.requirementRenderer.serializeRequirementConstraintMembership(
                    (RequirementConstraintMembership) relationship);
        } else {
            handled = false;
        }
        return handled;
    }

    /**
     * Writes the remaining keyword-introduced members: state subactions ({@code entry}/{@code do}/{@code exit}),
     * {@code variant}, {@code filter}, {@code render} and {@code return}; returns false with nothing written for other
     * relationships.
     */
    private boolean serializeOtherKeywordMembership(final Relationship relationship) {
        boolean handled = true;
        if (relationship instanceof final StateSubactionMembership stateSubactionMembership) {
            this.memberships.serializeStateSubactionMembership(stateSubactionMembership);
        } else if (this.navigation.isMetaclass(relationship, SysMLPackage.eINSTANCE.getVariantMembership())) {
            this.memberships.serializeVariantMembership(relationship);
        } else if (this.navigation.isMetaclass(relationship, SysMLPackage.eINSTANCE.getElementFilterMembership())) {
            this.memberships.serializeElementFilterMembership(relationship);
        } else if (this.navigation.isMetaclass(relationship, SysMLPackage.eINSTANCE.getViewRenderingMembership())) {
            this.memberships.serializeViewRenderingMembership(relationship);
        } else if (this.navigation.isMetaclass(relationship, SysMLPackage.eINSTANCE.getReturnParameterMembership())) {
            this.memberships.serializeReturnParameterMembership(relationship);
        } else {
            handled = false;
        }
        return handled;
    }

    /**
     * Writes an annotation reached directly by element dispatch rather than as a body relationship.
     *
     * @param annotation
     *            the annotation relationship
     */
    public void serializeAnnotation(final Relationship annotation) {
        this.memberships.serializeAnnotation(annotation);
    }
}
