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
package org.eclipse.syson.sysml.serializer.rendering.actions;

import java.util.function.Function;

import org.eclipse.syson.sysml.parser.adapter.ITextAdapter;
import org.eclipse.syson.sysml.serializer.SerializationContext;
import org.eclipse.syson.sysml.serializer.SerializationMode;
import org.eclipse.syson.sysml.serializer.model.DanglingReferenceIndex;
import org.eclipse.syson.sysml.serializer.model.ModelFlags;
import org.eclipse.syson.sysml.serializer.model.ParserAdapterQueries;
import org.eclipse.syson.sysml.serializer.model.SpecializationTargetNames;
import org.eclipse.syson.sysml.serializer.output.TextOutput;
import org.eclipse.syson.sysml.serializer.references.ReferenceResolver;
import org.eclipse.syson.sysml.serializer.rendering.declarations.DeclarationRenderer;
import org.eclipse.syson.sysml.serializer.syntax.SourceSyntax;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.Element;

/**
 * Writes the {@code ActionNodePrefix} shared by the action nodes of an action body ({@code OccurrenceUsagePrefix}
 * followed by an optional {@code ActionNodeUsageDeclaration}), and the {@code action} usage declaration of an
 * {@code ActionBodyParameter}.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
final class ActionNodePrefixRenderer {

    private final DeclarationRenderer declarations;

    private final ReferenceResolver referenceResolver;

    private final SerializationContext context;

    private final ParserAdapterQueries adapters;

    private final ModelFlags flags;

    private final SpecializationTargetNames specializationTargets;

    /**
     * Creates the renderer.
     *
     * @param declarations
     *            writes prefix modifiers, user keywords, identification and specializations
     * @param referenceResolver
     *            resolves declared names
     * @param context
     *            the serialization context
     */
    ActionNodePrefixRenderer(final DeclarationRenderer declarations, final ReferenceResolver referenceResolver,
            final SerializationContext context) {
        this.declarations = declarations;
        this.referenceResolver = referenceResolver;
        this.context = context;
        this.adapters = context.modelQueries().adapters();
        this.flags = context.modelQueries().flags();
        this.specializationTargets = context.modelQueries().specializationTargets();
    }

    /**
     * Writes the {@code ActionNodePrefix} of an action node (control node, {@code terminate}, accept, send or
     * assignment): its {@code OccurrenceUsagePrefix}
     * (direction, {@code derived}, {@code abstract}/{@code variation}, {@code constant}, {@code ref},
     * {@code individual}, portion kind and user keywords), then its {@code ActionNodeUsageDeclaration}
     * ({@code action <short> name : T :> s [m]}) when it has one.
     *
     * @param actionNode
     *            the action node whose prefix is written
     */
    void writeActionNodePrefix(final Element actionNode) {
        this.writeOccurrenceUsagePrefix(actionNode);
        if (this.hasActionUsageDeclaration(actionNode)) {
            this.writeActionUsageDeclaration(actionNode);
            this.context.output().space();
        }
    }

    /**
     * Writes the {@code OccurrenceUsagePrefix} keywords in grammar order. {@code ref} is kept when the source wrote it;
     * without parser syntax, a non-composite node gets it in normalized mode so that the reparsed node is non-composite
     * too.
     */
    private void writeOccurrenceUsagePrefix(final Element node) {
        final TextOutput output = this.context.output();
        final String direction = this.flags.direction(node);
        if (!direction.isBlank()) {
            output.token(direction).space();
        }
        this.declarations.serializePrefixModifiers(node);
        if (this.flags.hasExplicitRefPrefix(node)
                || this.context.options().mode() == SerializationMode.NORMALIZED && !SourceSyntax.hasTextAdapter(node)
                        && !this.flags.isComposite(node)) {
            output.token(SysMLTokens.REF_KEYWORD).space();
        }
        if (this.flags.isIndividual(node)) {
            output.token(SysMLTokens.INDIVIDUAL_KEYWORD).space();
        }
        final String portionKind = this.flags.portionKind(node);
        if (!portionKind.isBlank()) {
            output.token(portionKind).space();
        }
        this.declarations.serializeUserKeywords(node);
    }

    /**
     * Tells whether an action node or body parameter has an {@code action} usage declaration to write: the source
     * spelled the keyword, or it has a name, a short name or a typing, subsetting or redefinition, which can only be
     * written after the keyword.
     */
    boolean hasActionUsageDeclaration(final Element action) {
        final boolean identified = !this.referenceResolver.declaredName(action).isBlank()
                || !this.context.nameRenderer().declaredShortName(action).isBlank();
        return this.adapters.hasTextAdapter(action, ITextAdapter.EXPLICIT_ACTION_NODE_KEYWORD)
                || identified || this.hasSpecialization(action);
    }

    /** Tells whether an action has a typing, subsetting or redefinition to write in its usage declaration. */
    private boolean hasSpecialization(final Element action) {
        final DanglingReferenceIndex danglingReferences = this.context.danglingReferenceIndex();
        final Function<Element, String> referenceName = this.context.nameRenderer()::referenceName;
        return !this.specializationTargets.featureTypeNames(action, danglingReferences, referenceName).isEmpty()
                || !this.specializationTargets.subsettedFeatureNames(action, danglingReferences, referenceName).isEmpty()
                || !this.specializationTargets.redefinedFeatureNames(action, danglingReferences, referenceName).isEmpty();
    }

    /**
     * Returns the {@code ActionNodeUsageDeclaration} of an action node written inline, as a state subaction or a
     * transition effect ({@code PerformedActionUsage}): {@code action <shortName> name } for an identified node,
     * {@code action } when the source spelled the keyword, otherwise an empty string.
     *
     * @param actionNode
     *            the send, accept or assignment node
     * @return the declaration text, ending with a space when not empty
     */
    String inlineActionNodeDeclarationText(final Element actionNode) {
        final StringBuilder identification = new StringBuilder();
        final String shortName = this.context.nameRenderer().declaredShortName(actionNode);
        if (!shortName.isBlank()) {
            identification.append(SysMLTokens.SHORT_NAME_START).append(this.referenceResolver.renderedShortName(shortName))
                    .append(SysMLTokens.SHORT_NAME_END).append(SysMLTokens.SPACE);
        }
        final String name = this.referenceResolver.declaredName(actionNode);
        if (!name.isBlank()) {
            identification.append(name).append(SysMLTokens.SPACE);
        }
        String text = "";
        if (!identification.isEmpty() || this.adapters.hasTextAdapter(actionNode, ITextAdapter.EXPLICIT_ACTION_NODE_KEYWORD)) {
            text = SysMLTokens.ACTION_PREFIX + identification;
        }
        return text;
    }

    /** Writes {@code action} followed by the {@code UsageDeclaration}: identification and specializations. */
    void writeActionUsageDeclaration(final Element action) {
        this.context.output().token(SysMLTokens.ACTION_KEYWORD);
        this.declarations.renderOptionalIdentification(action, this.referenceResolver.declaredName(action));
        this.declarations.serializeSpecializations(action, true);
    }
}
