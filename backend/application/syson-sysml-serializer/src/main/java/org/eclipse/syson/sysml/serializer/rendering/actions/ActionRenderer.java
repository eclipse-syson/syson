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

import java.util.List;

import org.eclipse.syson.sysml.parser.adapter.ITextAdapter;
import org.eclipse.syson.sysml.serializer.ElementDispatch;
import org.eclipse.syson.sysml.serializer.SerializationContext;
import org.eclipse.syson.sysml.serializer.model.ElementSyntaxQueries;
import org.eclipse.syson.sysml.serializer.model.ModelFlags;
import org.eclipse.syson.sysml.serializer.model.ModelNavigation;
import org.eclipse.syson.sysml.serializer.model.MultiplicityText;
import org.eclipse.syson.sysml.serializer.model.NestedTargetSearch;
import org.eclipse.syson.sysml.serializer.model.ParserAdapterQueries;
import org.eclipse.syson.sysml.serializer.model.SpecializationTargetNames;
import org.eclipse.syson.sysml.serializer.output.ExpressionAndReferenceWriter;
import org.eclipse.syson.sysml.serializer.output.TextOutput;
import org.eclipse.syson.sysml.serializer.references.ReferenceResolver;
import org.eclipse.syson.sysml.serializer.rendering.declarations.DeclarationRenderer;
import org.eclipse.syson.sysml.serializer.syntax.ParserFragments;
import org.eclipse.syson.sysml.serializer.syntax.SourceSyntax;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.AssignmentActionUsage;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.ReferenceSubsetting;
import org.omg.sysml.lang.sysml.SysMLPackage;

/**
 * Entry point for action-specific syntax: {@code perform}, {@code terminate}, assignments, {@code send}/{@code accept},
 * control actions (if, while, for), and the compact action texts embedded in transitions and state subactions. Most
 * forms are delegated to dedicated renderers sharing one {@link ActionExpressionSupport}.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class ActionRenderer {

    private static final String TYPE_REFERENCE = "type";

    private final DeclarationRenderer declarations;

    private final ReferenceResolver referenceResolver;

    private final ExpressionAndReferenceWriter referenceWriter;

    private final ElementSyntaxQueries syntax;

    private final SerializationContext context;

    private final ModelFlags flags;

    private final ParserAdapterQueries adapters;

    private final MultiplicityText multiplicities;

    private final SpecializationTargetNames specializationTargets;

    private final NestedTargetSearch nestedTargets;

    private final PerformedActionTargetResolver performedActionTargetResolver;

    private final ActionExpressionSupport expressionSupport;

    private final AssignmentActionRenderer assignmentRenderer;

    private final MessageActionRenderer messageRenderer;

    private final ControlActionRenderer controlRenderer;

    private final CompactActionRenderer compactRenderer;

    private final ParameterActionBodyWriter bodyWriter;

    /**
     * Creates the renderer and its delegate renderers.
     *
     * @param declarations
     *            writes the generic parts of declarations
     * @param dispatch
     *            dispatches nested elements to their renderers
     * @param referenceResolver
     *            resolves declared and referenced names
     * @param referenceWriter
     *            writes expressions and references with traceability metadata
     * @param syntax
     *            syntax queries on elements
     * @param context
     *            the serialization context
     * @param performedActionTargetResolver
     *            resolves {@code perform}/{@code exhibit} targets
     */
    public ActionRenderer(final DeclarationRenderer declarations,
            final ElementDispatch dispatch,
            final ReferenceResolver referenceResolver,
            final ExpressionAndReferenceWriter referenceWriter,
            final ElementSyntaxQueries syntax,
            final SerializationContext context,
            final PerformedActionTargetResolver performedActionTargetResolver) {
        this.declarations = declarations;
        this.referenceResolver = referenceResolver;
        this.referenceWriter = referenceWriter;
        this.syntax = syntax;
        this.context = context;
        this.flags = context.modelQueries().flags();
        this.adapters = context.modelQueries().adapters();
        this.multiplicities = context.modelQueries().multiplicities();
        this.specializationTargets = context.modelQueries().specializationTargets();
        this.nestedTargets = context.modelQueries().nestedTargets();
        this.performedActionTargetResolver = performedActionTargetResolver;
        this.expressionSupport = new ActionExpressionSupport(referenceWriter, context.modelQueries().navigation());
        final ActionNodePrefixRenderer prefixRenderer = new ActionNodePrefixRenderer(declarations, referenceResolver, context);
        this.bodyWriter = new ParameterActionBodyWriter(declarations, syntax, context);
        this.assignmentRenderer = new AssignmentActionRenderer(referenceResolver, referenceWriter, context, this.expressionSupport,
                prefixRenderer, this.bodyWriter);
        this.messageRenderer = new MessageActionRenderer(declarations, dispatch, referenceResolver, referenceWriter, syntax, context, this.expressionSupport);
        this.controlRenderer = new ControlActionRenderer(declarations, referenceResolver, referenceWriter, context, this.expressionSupport,
                prefixRenderer);
        this.compactRenderer = new CompactActionRenderer(context.modelQueries().navigation(), this.assignmentRenderer, this.messageRenderer,
                prefixRenderer);
    }

    /**
     * Writes an assignment action statement ({@code assign x := v;}).
     *
     * @param assignmentActionUsage
     *            the assignment action
     */
    public void serializeAssignment(final AssignmentActionUsage assignmentActionUsage) {
        this.assignmentRenderer.serializeAssignment(assignmentActionUsage);
    }

    /**
     * Writes a perform action usage: {@code perform a.b;} or {@code perform action p : P;}. Standalone performed-action
     * fragments are written with {@code action} instead of {@code perform}, as their grammar requires.
     *
     * @param performActionUsage
     *            the perform action usage
     */
    public void serializePerformActionUsage(final Element performActionUsage) {
        final boolean performedActionFragment = SourceSyntax.isRootParserEntry(this.context.options(),
                performActionUsage, ParserFragments.PERFORMED_ACTION_USAGE);
        this.declarations.serializePrefixModifiers(performActionUsage);
        this.declarations.serializeUserKeywords(performActionUsage);
        final String name = this.referenceResolver.declaredName(performActionUsage);
        boolean shorthandTarget = false;
        if (!this.referenceResolver.hasIdentification(performActionUsage)) {
            String leadingKeyword = SysMLTokens.PERFORM_KEYWORD;
            if (performedActionFragment) {
                leadingKeyword = SysMLTokens.ACTION_KEYWORD;
            }
            shorthandTarget = this.writeAnonymousPerformDeclaration(performActionUsage, leadingKeyword);
        } else {
            String leadingKeyword = SysMLTokens.PERFORM_KEYWORD;
            if (performedActionFragment) {
                leadingKeyword = "";
            }
            this.writeNamedPerformDeclaration(performActionUsage, leadingKeyword, name);
        }
        this.writePerformStatementEnd(performActionUsage, shorthandTarget);
    }

    /**
     * Writes a perform action used as a {@code PerformedActionUsage}: the {@code PerformActionUsageDeclaration} of a
     * state subaction ({@code entry a.b { ... }}) or of a transition effect ({@code do action d : D { ... }}), without
     * {@code perform} keyword. Its body goes through the same body writer as a {@code perform} statement, so typed,
     * specialized or nested members are not reduced to a compact spelling.
     *
     * @param performActionUsage
     *            the perform action usage
     * @param statement
     *            {@code true} for a state subaction, whose {@code ActionBody} ends with {@code ;} when there is no
     *            block; {@code false} for a transition effect, which has no terminator
     */
    public void serializePerformedPerformAction(final Element performActionUsage, final boolean statement) {
        final String name = this.referenceResolver.declaredName(performActionUsage);
        boolean shorthandTarget = false;
        if (!this.referenceResolver.hasIdentification(performActionUsage)) {
            shorthandTarget = this.writeAnonymousPerformDeclaration(performActionUsage, "");
        } else {
            this.writeNamedPerformDeclaration(performActionUsage, "", name);
        }
        if (statement) {
            this.writePerformStatementEnd(performActionUsage, shorthandTarget);
        } else if (this.syntax.shouldSerializeBlockBody(performActionUsage)) {
            this.declarations.serializeBody(performActionUsage);
        }
    }

    /**
     * Writes an unnamed perform declaration after {@code leadingKeyword}: the shorthand {@code target :>> r : T [1]}
     * when a target can be resolved and {@code action} was not written explicitly, otherwise {@code action :>> r}.
     * Returns whether the shorthand target form was written.
     */
    private boolean writeAnonymousPerformDeclaration(final Element action, final String leadingKeyword) {
        final TextOutput output = this.context.output();
        final boolean explicitAction = this.adapters.hasTextAdapter(action,
                ITextAdapter.EXPLICIT_ACTION_NODE_KEYWORD);
        String target = "";
        if (!explicitAction) {
            target = this.performedActionTargetResolver.resolve(action);
        }
        if (!leadingKeyword.isEmpty()) {
            output.token(leadingKeyword).space();
        }
        if (target.isBlank()) {
            output.token(SysMLTokens.ACTION_KEYWORD);
            this.declarations.serializeSpecializations(action, true);
        } else {
            this.referenceWriter.writeReferenceText(output, target, action);
            this.writePerformedTargetSpecializations(action);
        }
        this.declarations.serializeFeatureValues(action);
        return !target.isBlank();
    }

    /**
     * Writes {@code action <shortName> name} after {@code leadingKeyword}, with its specializations and value; either
     * part of the identification may be absent.
     */
    private void writeNamedPerformDeclaration(final Element action, final String leadingKeyword, final String name) {
        final TextOutput output = this.context.output();
        if (!leadingKeyword.isEmpty()) {
            output.token(leadingKeyword).space();
        }
        output.token(SysMLTokens.ACTION_KEYWORD);
        this.declarations.renderOptionalIdentification(action, name);
        this.declarations.serializeSpecializations(action, true);
        this.declarations.serializeFeatureValues(action);
    }

    /**
     * Ends a perform declaration with its block body or {@code ;}. A shorthand target perform keeps only an explicit
     * body, without its implied plain features, so they are not exported; an {@code action} declaration keeps its
     * whole body, including {@code first start;} successions.
     */
    private void writePerformStatementEnd(final Element action, final boolean shorthandTarget) {
        if (shorthandTarget && this.syntax.hasExplicitActionBodyContent(action)) {
            this.declarations.serializeBodyExcludingImplicitFeatures(action);
            this.context.output().newline();
        } else if (!shorthandTarget && this.syntax.shouldSerializeBlockBody(action)) {
            this.declarations.serializeBody(action);
            this.context.output().newline();
        } else {
            this.declarations.serializeStatementEnd(action);
        }
    }

    /**
     * Writes the clauses allowed after a shorthand perform target ({@code FeatureSpecializationPart}): redefinition,
     * typing and multiplicity. The reference subsetting is the target itself and is not repeated.
     */
    private void writePerformedTargetSpecializations(final Element action) {
        final TextOutput output = this.context.output();
        final List<String> redefinedFeatures = this.specializationTargets.redefinedFeatureNames(
                action, this.context.danglingReferenceIndex(),
                this.context.nameRenderer()::referenceName);
        final List<String> featureTypes = this.specializationTargets.featureTypeNames(action,
                this.context.danglingReferenceIndex(),
                this.context.nameRenderer()::referenceName);
        if (!redefinedFeatures.isEmpty()) {
            this.declarations.writeRedefinition(output, action, redefinedFeatures);
        }
        if (!featureTypes.isEmpty()) {
            output.space().token(SysMLTokens.TYPE_SEPARATOR).space();
            this.referenceWriter.writeRelationshipReferences(output,
                    this.referenceResolver.referenceNames(featureTypes), action,
                    SysMLPackage.eINSTANCE.getFeatureTyping(), TYPE_REFERENCE);
        }
        this.writePerformedTargetMultiplicity(action);
    }

    /**
     * Writes the multiplicity, {@code ordered} and late {@code nonunique} of a shorthand perform; the multiplicity may
     * sit on a nested feature of the target chain, but never on a body member, which writes its own.
     */
    private void writePerformedTargetMultiplicity(final Element element) {
        String multiplicity = this.multiplicities.multiplicityText(element,
                this.referenceWriter::expressionText);
        if (multiplicity.isBlank()) {
            multiplicity = this.targetChainMultiplicityText(element);
        }
        if (!multiplicity.isBlank()) {
            this.referenceWriter.writeReferenceText(this.context.output(),
                    multiplicity, element);
        }
        if (this.flags.isOrdered(element)) {
            this.context.output().space().token(SysMLTokens.ORDERED_KEYWORD);
        }
        this.declarations.serializeLateNonUnique(element);
    }

    /** Returns the first multiplicity found in the target chain owned by the perform's reference subsetting. */
    private String targetChainMultiplicityText(final Element element) {
        final ModelNavigation navigation = this.context.modelQueries().navigation();
        return navigation.ownedRelationships(element).stream()
                .filter(ReferenceSubsetting.class::isInstance)
                .flatMap(relationship -> navigation.ownedRelatedElements(relationship).stream())
                .map(chain -> this.multiplicities.multiplicityText(chain, this.referenceWriter::expressionText))
                .filter(text -> !text.isBlank())
                .findFirst().orElse("");
    }

    /**
     * Writes {@code terminate;}, {@code terminate occ;} or {@code action name terminate occ;}, ended by its action body
     * ({@code ;} or a block). The occurrence comes from the first parameter value, else from the first feature chain in
     * the subtree.
     *
     * @param terminateActionUsage
     *            the terminate action usage
     */
    public void serializeTerminateActionUsage(final Element terminateActionUsage) {
        final TextOutput output = this.context.output();
        String argument = this.expressionSupport.firstParameterExpressionText(terminateActionUsage);
        if (argument.isBlank()) {
            argument = this.nestedTargets
                    .chainedFeatureNames(terminateActionUsage,
                            this.context.danglingReferenceIndex())
                    .stream().findFirst().orElse("");
        }
        this.controlRenderer.writeActionNodePrefix(terminateActionUsage);
        output.token(SysMLTokens.TERMINATE_KEYWORD);
        if (!argument.isBlank()) {
            this.referenceWriter.writeReferenceText(output.space(), argument,
                    terminateActionUsage);
        }
        this.bodyWriter.writeBodyOrStatementEnd(terminateActionUsage);
    }

    /**
     * Writes an accept action as a statement ({@code accept s : Signal via p;}).
     *
     * @param acceptActionUsage
     *            the accept action usage
     */
    public void serializeStandaloneAcceptActionUsage(final Element acceptActionUsage) {
        this.messageRenderer.serializeAccept(acceptActionUsage);
    }

    /**
     * Writes a send action as a statement ({@code send s via p to t;}).
     *
     * @param sendActionUsage
     *            the send action usage
     */
    public void serializeStandaloneSendActionUsage(final Element sendActionUsage) {
        this.messageRenderer.serializeSend(sendActionUsage);
    }

    /**
     * Writes a {@code while}/{@code loop} action with its body and optional {@code until} condition.
     *
     * @param whileLoopActionUsage
     *            the while loop action usage
     */
    public void serializeWhileLoopActionUsage(final Element whileLoopActionUsage) {
        this.controlRenderer.serializeWhileLoop(whileLoopActionUsage);
    }

    /**
     * Writes an {@code if} action with its {@code else} branch.
     *
     * @param ifActionUsage
     *            the if action usage
     */
    public void serializeIfActionUsage(final Element ifActionUsage) {
        this.controlRenderer.serializeIf(ifActionUsage);
    }

    /**
     * Writes a {@code for x in seq} action with its body.
     *
     * @param forLoopActionUsage
     *            the for loop action usage
     */
    public void serializeForLoopActionUsage(final Element forLoopActionUsage) {
        this.controlRenderer.serializeForLoop(forLoopActionUsage);
    }

    /**
     * Returns the inline text of a send, accept or assignment written as a state subaction ({@code entry},
     * {@code do}, {@code exit}) or a transition effect, with its {@code action name} declaration.
     *
     * @param action
     *            the subaction
     * @return the inline action text
     */
    public String stateSubactionText(final Element action) {
        return this.compactRenderer.stateSubactionText(action);
    }

    /**
     * Returns the inline text of an accept action used as a transition trigger, without statement terminator.
     *
     * @param acceptActionUsage
     *            the accept action usage
     * @return the accept text
     */
    public String acceptActionText(final Element acceptActionUsage) {
        return this.messageRenderer.acceptActionText(acceptActionUsage);
    }

    /**
     * Tells whether a send, accept or assignment written inline (as a state subaction or a transition effect) has
     * body members that must follow its inline text. Any other action has no inline body.
     *
     * @param action
     *            the inline action
     * @return {@code true} when a body must follow
     */
    public boolean hasInlineActionBody(final Element action) {
        final boolean hasBody;
        if (this.isSendAction(action)) {
            hasBody = this.messageRenderer.hasSendBody(action);
        } else {
            hasBody = this.isAcceptOrAssignmentAction(action) && this.bodyWriter.hasBody(action);
        }
        return hasBody;
    }

    /**
     * Writes the body block of a send, accept or assignment whose inline text was written by the caller, without
     * trailing newline.
     *
     * @param action
     *            the inline action
     */
    public void serializeInlineActionBody(final Element action) {
        if (this.isSendAction(action)) {
            this.messageRenderer.serializeSendBody(action);
        } else {
            this.bodyWriter.writeBody(action);
        }
    }

    private boolean isSendAction(final Element action) {
        return this.context.modelQueries().navigation().isMetaclass(action, SysMLPackage.eINSTANCE.getSendActionUsage());
    }

    /** Returns the payload and optional receiver for a standalone accept parameter part. */
    public String acceptParameterText(final Element action) {
        return this.messageRenderer.acceptParameterText(action);
    }

    private boolean isAcceptOrAssignmentAction(final Element action) {
        return this.context.modelQueries().navigation().isMetaclass(action, SysMLPackage.eINSTANCE.getAcceptActionUsage())
                || action instanceof AssignmentActionUsage;
    }

}
