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
package org.eclipse.syson.sysml.serializer.rendering;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.syson.sysml.serializer.ElementDispatch;
import org.eclipse.syson.sysml.serializer.SerializationContext;
import org.eclipse.syson.sysml.serializer.model.ElementSyntaxQueries;
import org.eclipse.syson.sysml.serializer.model.ModelFlags;
import org.eclipse.syson.sysml.serializer.model.ModelNavigation;
import org.eclipse.syson.sysml.serializer.output.ExpressionAndReferenceWriter;
import org.eclipse.syson.sysml.serializer.references.ReferenceResolver;
import org.eclipse.syson.sysml.serializer.rendering.actions.ActionRenderer;
import org.eclipse.syson.sysml.serializer.rendering.actions.ExhibitStateRenderer;
import org.eclipse.syson.sysml.serializer.rendering.actions.PerformedActionTargetResolver;
import org.eclipse.syson.sysml.serializer.rendering.actions.SuccessionEndpointRenderer;
import org.eclipse.syson.sysml.serializer.rendering.actions.SuccessionRenderer;
import org.eclipse.syson.sysml.serializer.rendering.actions.TransitionRenderer;
import org.eclipse.syson.sysml.serializer.rendering.connections.ConnectorEndRenderer;
import org.eclipse.syson.sysml.serializer.rendering.connections.ConnectorRenderer;
import org.eclipse.syson.sysml.serializer.rendering.connections.EventOccurrenceRenderer;
import org.eclipse.syson.sysml.serializer.rendering.connections.FlowRenderer;
import org.eclipse.syson.sysml.serializer.rendering.connections.InterfaceRenderer;
import org.eclipse.syson.sysml.serializer.rendering.connections.PayloadReferenceRenderer;
import org.eclipse.syson.sysml.serializer.rendering.declarations.DeclarationRenderer;
import org.eclipse.syson.sysml.serializer.rendering.declarations.DefinitionAndUsageRenderer;
import org.eclipse.syson.sysml.serializer.rendering.declarations.IncludeUseCaseRenderer;
import org.eclipse.syson.sysml.serializer.rendering.declarations.MetadataRenderer;
import org.eclipse.syson.sysml.serializer.rendering.declarations.PackageRenderer;
import org.eclipse.syson.sysml.serializer.rendering.fragments.CompactParserFragmentRenderer;
import org.eclipse.syson.sysml.serializer.rendering.fragments.StructuredParserFragmentRenderer;
import org.eclipse.syson.sysml.serializer.rendering.relationships.DependencyRenderer;
import org.eclipse.syson.sysml.serializer.rendering.relationships.ImportRenderer;
import org.eclipse.syson.sysml.serializer.rendering.relationships.MembershipRenderer;
import org.eclipse.syson.sysml.serializer.rendering.relationships.MembershipSyntaxRenderer;
import org.eclipse.syson.sysml.serializer.rendering.relationships.RelationshipRenderer;
import org.eclipse.syson.sysml.serializer.rendering.relationships.RequirementRenderer;
import org.eclipse.syson.sysml.serializer.syntax.CommentRenderer;
import org.eclipse.syson.sysml.serializer.syntax.ParserFragments;
import org.omg.sysml.lang.sysml.AssignmentActionUsage;
import org.omg.sysml.lang.sysml.BindingConnectorAsUsage;
import org.omg.sysml.lang.sysml.Comment;
import org.omg.sysml.lang.sysml.ConnectionUsage;
import org.omg.sysml.lang.sysml.Definition;
import org.omg.sysml.lang.sysml.Dependency;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Expression;
import org.omg.sysml.lang.sysml.Feature;
import org.omg.sysml.lang.sysml.FlowUsage;
import org.omg.sysml.lang.sysml.InterfaceUsage;
import org.omg.sysml.lang.sysml.Package;
import org.omg.sysml.lang.sysml.ReferenceUsage;
import org.omg.sysml.lang.sysml.Relationship;
import org.omg.sysml.lang.sysml.SatisfyRequirementUsage;
import org.omg.sysml.lang.sysml.SuccessionAsUsage;
import org.omg.sysml.lang.sysml.SysMLPackage;
import org.omg.sysml.lang.sysml.TextualRepresentation;
import org.omg.sysml.lang.sysml.TransitionUsage;

/**
 * Maps SysML element types to their domain renderer. Registrations are ordered: the first compatible renderer wins.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class SysMLTypeRendererRegistry {

    private final ElementDispatch dispatch;

    private final ExpressionAndReferenceWriter referenceWriter;

    private final SerializationContext context;

    private final ModelNavigation navigation;

    private final ModelFlags flags;

    private final ConnectorRenderer connectorRenderer;

    private final FlowRenderer flowRenderer;

    private final InterfaceRenderer interfaceRenderer;

    private final RelationshipRenderer relationshipRenderer;

    private final DependencyRenderer dependencyRenderer;

    private final CommentRenderer commentRenderer;

    private final MetadataRenderer metadataRenderer;

    private final ActionRenderer actionRenderer;

    private final ExhibitStateRenderer exhibitStateRenderer;

    private final EventOccurrenceRenderer eventOccurrenceRenderer;

    private final IncludeUseCaseRenderer includeUseCaseRenderer;

    private final PayloadReferenceRenderer payloadReferenceRenderer;

    private final TransitionRenderer transitionRenderer;

    private final SuccessionRenderer successionRenderer;

    private final RequirementRenderer requirementRenderer;

    private final StructuredParserFragmentRenderer structuredFragmentRenderer;

    private final CompactParserFragmentRenderer compactFragmentRenderer;

    private final PackageRenderer packageRenderer;

    private final DefinitionAndUsageRenderer definitionAndUsageRenderer;

    private final List<ElementRenderer> elementRenderers;

    private final Map<EClass, List<ElementRenderer>> compatibleRendererCache = new IdentityHashMap<>();

    /**
     * Builds the ordered element and relationship rules and their domain renderers for one serialization run.
     *
     * @param context
     *            supplies the run's output, options, names and model queries
     * @param dispatch
     *            recursively renders contained elements and relationships through the session's scopes
     * @param names
     *            supplies declared names and reference spellings
     * @param referenceWriter
     *            writes expressions and references with their model-target metadata
     * @param syntax
     *            identifies explicit bodies and parser-generated elements when selecting syntax
     * @param declarations
     *            writes shared declaration prefixes, clauses and bodies
     * @param comments
     *            writes documentation and source notes
     */
    public SysMLTypeRendererRegistry(final SerializationContext context, final ElementDispatch dispatch,
            final ReferenceResolver names, final ExpressionAndReferenceWriter referenceWriter,
            final ElementSyntaxQueries syntax, final DeclarationRenderer declarations, final CommentRenderer comments) {
        this.context = context;
        this.navigation = context.modelQueries().navigation();
        this.flags = context.modelQueries().flags();
        this.dispatch = dispatch;
        this.referenceWriter = referenceWriter;
        this.commentRenderer = comments;
        final ConnectorEndRenderer connectorEndRenderer = new ConnectorEndRenderer(names, referenceWriter, context);
        this.connectorRenderer = new ConnectorRenderer(declarations, names, referenceWriter, syntax, context, connectorEndRenderer);
        this.flowRenderer = new FlowRenderer(declarations, names, referenceWriter, context);
        this.interfaceRenderer = new InterfaceRenderer(declarations, names, context, connectorEndRenderer);
        this.requirementRenderer = new RequirementRenderer(declarations, dispatch, names, referenceWriter, syntax, context);
        final ImportRenderer importRenderer = new ImportRenderer(declarations, dispatch, names, referenceWriter, context);
        final MembershipRenderer membershipRenderer = new MembershipRenderer(declarations, dispatch, names, referenceWriter, syntax, context);
        this.dependencyRenderer = new DependencyRenderer(declarations, names, referenceWriter, context);
        this.metadataRenderer = new MetadataRenderer(declarations, dispatch, names, referenceWriter, syntax, context);
        final PerformedActionTargetResolver performedActionTargetResolver = new PerformedActionTargetResolver(names, context);
        this.actionRenderer = new ActionRenderer(declarations, dispatch, names, referenceWriter, syntax, context, performedActionTargetResolver);
        this.exhibitStateRenderer = new ExhibitStateRenderer(declarations, names, referenceWriter, context, performedActionTargetResolver);
        this.eventOccurrenceRenderer = new EventOccurrenceRenderer(declarations, names, referenceWriter, context);
        this.includeUseCaseRenderer = new IncludeUseCaseRenderer(declarations, names, referenceWriter, context);
        this.payloadReferenceRenderer = new PayloadReferenceRenderer(names, referenceWriter, context);
        final SuccessionEndpointRenderer successionEndpointRenderer = new SuccessionEndpointRenderer(names, referenceWriter, syntax, context);
        this.successionRenderer = new SuccessionRenderer(declarations, dispatch, names, referenceWriter, syntax, context,
                successionEndpointRenderer);
        this.transitionRenderer = new TransitionRenderer(declarations, dispatch, names, referenceWriter, syntax, context, this.actionRenderer);
        final MembershipSyntaxRenderer membershipSyntaxRenderer = new MembershipSyntaxRenderer(this.actionRenderer, declarations, dispatch, names, referenceWriter, syntax, context);
        this.structuredFragmentRenderer = new StructuredParserFragmentRenderer(this.transitionRenderer, declarations, dispatch, names, referenceWriter, context, connectorEndRenderer);
        this.compactFragmentRenderer = new CompactParserFragmentRenderer(declarations, names, referenceWriter, context);
        this.packageRenderer = new PackageRenderer(declarations, names, syntax, context);
        this.definitionAndUsageRenderer = new DefinitionAndUsageRenderer(declarations, names, referenceWriter, syntax, context);
        this.relationshipRenderer = new RelationshipRenderer(declarations, syntax, membershipSyntaxRenderer,
                this.requirementRenderer, importRenderer, membershipRenderer, this.navigation);
        this.elementRenderers = this.createElementRenderers();
    }

    /**
     * Renders {@code element} with the first enabled rule whose type filter and instance predicate both accept it. The
     * catch-all generic declaration rule means every element is handled; the exception only signals a broken rule list.
     */
    public void render(final Element element) {
        for (final ElementRenderer elementRenderer : this
                .compatibleRenderers(element)) {
            if (elementRenderer.supports().test(element)) {
                elementRenderer.render().accept(element);
                return;
            }
        }
        throw new IllegalStateException(
                "No renderer registered for " + element.eClass().getName());
    }

    /**
     * Returns the rules whose type filter accepts the element's metaclass, in registration order. Type filters depend
     * only on the metaclass, so the result is cached per metaclass for the run; instance predicates still run for each
     * element.
     */
    private List<ElementRenderer> compatibleRenderers(final Element element) {
        return this.compatibleRendererCache.computeIfAbsent(element.eClass(),
                key -> this.elementRenderers.stream()
                        .filter(elementRenderer -> elementRenderer
                                .type().test(element))
                        .toList());
    }

    /**
     * Builds the ordered dispatch list, without the parser-only rules when a whole document is serialized. The list is
     * an ordered dispatch, not a set of independent handlers: parser fragments precede the model renderers that would
     * otherwise claim their root, and the generic declaration comes last.
     */
    private List<ElementRenderer> createElementRenderers() {
        return Stream
                .of(this.parserEntryPointRenderers(),
                        this.explicitDeclarationRenderers(),
                        this.coreElementRenderers(),
                        this.expressionAndActionRenderers(),
                        this.specializedFallbackRenderers(),
                        this.genericDeclarationRenderers())
                .flatMap(List::stream)
                .filter(ElementRenderer::enabled)
                .toList();
    }

    /** Packages and definitions, whose syntax depends only on their metaclass and never on a more specific rule. */
    private List<ElementRenderer> explicitDeclarationRenderers() {
        return List.of(this.renderer(Package.class,
                this.packageRenderer::serializePackage),
                this.renderer(Definition.class,
                        this.definitionAndUsageRenderer::serializeDeclaration));
    }

    /**
     * Rules for parse roots of fragment parser rules; empty when a whole document is serialized. They come first
     * because the root of a fragment parse has the metaclass of an ordinary element, and would otherwise get its full
     * declaration instead of the fragment's syntax.
     */
    private List<ElementRenderer> parserEntryPointRenderers() {
        return List.of(this.parserOnly(this.renderer(SysMLPackage.eINSTANCE.getAcceptActionUsage(),
                element -> ParserFragments.ACCEPT_PARAMETER_PART.equals(this.context.options().parserEntryPoint()),
                element -> this.context.output().raw(this.actionRenderer.acceptParameterText(element)))),
                this.parserOnly(this.renderer(
                        this.compactFragmentRenderer::canSerializeSimpleFragment,
                        this.compactFragmentRenderer::serializeSimpleFragment)),
                this.parserOnly(this.renderer(
                        this.structuredFragmentRenderer::canSerializeMessageEvent,
                        this.structuredFragmentRenderer::serializeMessageEvent)),
                this.parserOnly(this.renderer(
                        this.structuredFragmentRenderer::canSerializeDefaultInterfaceEnd,
                        this.structuredFragmentRenderer::serializeDefaultInterfaceEnd)));
    }

    /**
     * Annotations, comments, textual representations, connectors, flows, requirement forms and metadata, plus parse
     * roots that must be caught before the expression rule.
     */
    private List<ElementRenderer> coreElementRenderers() {
        return List.of(this.renderer(SysMLPackage.eINSTANCE.getAnnotation(),
                element -> this.relationshipRenderer.serializeAnnotation(
                        (Relationship) element)),
                this.renderer(Comment.class,
                        this.commentRenderer::serializeComment),
                this.renderer(TextualRepresentation.class,
                        this.commentRenderer::serializeTextualRepresentation),
                this.renderer(BindingConnectorAsUsage.class,
                        this.connectorRenderer::hasExactlyTwoReferenceEnds,
                        this.connectorRenderer::serializeBindingConnector),
                // Messages are FlowUsages too: test the {@code message} form before the generic {@code flow} form.
                this.renderer(FlowUsage.class,
                        this.flowRenderer::canSerializeMessageUsage,
                        this.flowRenderer::serializeMessageUsage),
                this.renderer(FlowUsage.class,
                        this.flowRenderer::canSerializeFlowUsage,
                        this.flowRenderer::serializeFlowUsage),
                // InterfaceUsage and AllocationUsage specialize ConnectionUsage, and canSerializeConnectionUsage does
                // not exclude them: both must precede the generic connection rule.
                this.renderer(InterfaceUsage.class,
                        this.interfaceRenderer::canSerializeInterfaceUsage,
                        this.interfaceRenderer::serializeInterfaceUsage),
                this.renderer(ConnectionUsage.class,
                        element -> this.navigation.isMetaclass(element,
                                SysMLPackage.eINSTANCE.getAllocationUsage())
                                && this.connectorRenderer
                                        .canSerializeAllocationUsage(element),
                        this.connectorRenderer::serializeAllocationUsage),
                this.renderer(ConnectionUsage.class,
                        this.connectorRenderer::canSerializeConnectionUsage,
                        this.connectorRenderer::serializeConnectionUsage),
                // Satisfy, requirement and assert usages are ConstraintUsages: they must precede the contextual
                // requirement rule in specializedFallbackRenderers, which matches any unnamed constraint with a
                // reference prefix.
                this.renderer(SatisfyRequirementUsage.class,
                        this.requirementRenderer::serializeSatisfyRequirement),
                this.renderer(Dependency.class,
                        this.dependencyRenderer::serializeDependency),
                this.renderer(SysMLPackage.eINSTANCE.getRequirementUsage(),
                        this.requirementRenderer::canSerializeRequirementReferenceUsage,
                        this.requirementRenderer::serializeRequirementReferenceUsage),
                this.renderer(SysMLPackage.eINSTANCE.getAssertConstraintUsage(),
                        this.requirementRenderer::canSerializeReferencedAssertConstraint,
                        this.requirementRenderer::serializeReferencedAssertConstraint),
                this.renderer(SysMLPackage.eINSTANCE.getMetadataUsage(),
                        this.metadataRenderer::serializeMetadataUsage),
                // Parse roots that are expressions or memberships: before the Expression rule below and the root
                // relationship fallbacks, which would otherwise write them as plain expressions or relationship
                // targets.
                this.parserOnly(this.renderer(
                        this.structuredFragmentRenderer::canSerializeFeatureValue,
                        this.structuredFragmentRenderer::serializeFeatureValue)),
                this.parserOnly(this.renderer(
                        this.structuredFragmentRenderer::canSerializeBodyExpression,
                        this.structuredFragmentRenderer::serializeBodyExpression)),
                this.parserOnly(this.renderer(
                        this.structuredFragmentRenderer::canSerializeExpressionBody,
                        this.structuredFragmentRenderer::serializeExpressionBody)));
    }

    /**
     * Syntax-level expressions and actions with dedicated syntax (accept, send, perform, control nodes, assignments).
     */
    private List<ElementRenderer> expressionAndActionRenderers() {
        return List.of(this.renderer(Expression.class,
                this.flags::isExpressionSyntaxElement,
                this::serializeExpression),
                this.parserOnly(this.renderer(
                        this.structuredFragmentRenderer::canSerializeEffectBehavior,
                        this.structuredFragmentRenderer::serializeEffectBehavior)),
                this.renderer(SysMLPackage.eINSTANCE.getAcceptActionUsage(),
                        this.actionRenderer::serializeStandaloneAcceptActionUsage),
                this.renderer(SysMLPackage.eINSTANCE.getSendActionUsage(),
                        this.actionRenderer::serializeStandaloneSendActionUsage),
                this.renderer(SysMLPackage.eINSTANCE.getEventOccurrenceUsage(),
                        this.eventOccurrenceRenderer::serializeEventOccurrenceUsage),
                this.renderer(SysMLPackage.eINSTANCE.getExhibitStateUsage(),
                        this.exhibitStateRenderer::serializeExhibitStateUsage),
                this.renderer(SysMLPackage.eINSTANCE.getPerformActionUsage(),
                        this.actionRenderer::serializePerformActionUsage),
                this.renderer(SysMLPackage.eINSTANCE.getIncludeUseCaseUsage(),
                        this.includeUseCaseRenderer::serializeIncludeUseCaseUsage),
                this.renderer(SysMLPackage.eINSTANCE.getTerminateActionUsage(),
                        this.actionRenderer::serializeTerminateActionUsage),
                this.renderer(SysMLPackage.eINSTANCE.getWhileLoopActionUsage(),
                        this.actionRenderer::serializeWhileLoopActionUsage),
                this.renderer(SysMLPackage.eINSTANCE.getIfActionUsage(),
                        this.actionRenderer::serializeIfActionUsage),
                this.renderer(SysMLPackage.eINSTANCE.getForLoopActionUsage(),
                        this.actionRenderer::serializeForLoopActionUsage),
                this.renderer(AssignmentActionUsage.class,
                        this.actionRenderer::serializeAssignment));
    }

    /**
     * Shorthand forms that apply to otherwise generic usages (payloads, metadata body fields, contextual requirements,
     * successions, transitions) and the remaining fragment roots. Everything here must precede the generic
     * declarations.
     */
    private List<ElementRenderer> specializedFallbackRenderers() {
        return List.of(this.renderer(ReferenceUsage.class,
                this.payloadReferenceRenderer::canSerialize,
                this.payloadReferenceRenderer::serialize),
                // After the payload rule: a {@code :>> payload : T} usage also has the shape of a metadata body field.
                this.renderer(SysMLPackage.eINSTANCE.getReferenceUsage(),
                        this.metadataRenderer::canSerializeMetadataBodyUsage,
                        this.metadataRenderer::serializeMetadataBodyUsage),
                this.renderer(
                        this.requirementRenderer::canSerializeContextualRequirementUsage,
                        this.requirementRenderer::serializeContextualRequirementUsage),
                this.renderer(SuccessionAsUsage.class,
                        this.successionRenderer::serializeSuccessionAsUsage),
                // Full transition syntax first; the simple form is the fallback for unnamed transitions that own a
                // succession.
                this.renderer(TransitionUsage.class,
                        this.transitionRenderer::canSerializeTransitionUsage,
                        this.transitionRenderer::serializeTransitionUsage),
                this.renderer(TransitionUsage.class,
                        this.transitionRenderer::canSerializeSimpleTransitionUsage,
                        this.transitionRenderer::serializeSimpleTransitionUsage),
                this.parserOnly(this.renderer(
                        this.structuredFragmentRenderer::canSerializePayload,
                        this.structuredFragmentRenderer::serializePayload)),
                this.parserOnly(this.renderer(
                        this.structuredFragmentRenderer::canSerializeConstructorResult,
                        this.structuredFragmentRenderer::serializeConstructorResult)),
                this.parserOnly(this.renderer(
                        this.structuredFragmentRenderer::canSerializeInterfaceEnd,
                        this.structuredFragmentRenderer::serializeInterfaceEnd)),
                this.parserOnly(this.renderer(
                        this.compactFragmentRenderer::canSerializeVariantReference,
                        this.compactFragmentRenderer::serializeVariantReference)),
                this.parserOnly(this.renderer(
                        this.compactFragmentRenderer::canSerializeTargetParameter,
                        this.compactFragmentRenderer::serializeTargetParameter)),
                this.parserOnly(this.renderer(
                        this.compactFragmentRenderer::canSerializeForVariableDeclaration,
                        this.compactFragmentRenderer::serializeForVariableDeclaration)),
                // Connectors and successions are both Features and Relationships: try the feature chain form before the
                // relationship target form.
                this.parserOnly(this.renderer(Feature.class,
                        this.compactFragmentRenderer::canSerializeRootFeatureChain,
                        this.compactFragmentRenderer::serializeRootFeatureChain)),
                // A detached relationship whose text is only its target, before the membership fallback below.
                this.parserOnly(this.renderer(Relationship.class,
                        this.compactFragmentRenderer::canSerializeRootRelationshipTarget,
                        this.compactFragmentRenderer::serializeRootRelationshipTarget)),
                // Direct parser-rule memberships reach this fallback before declarations.
                this.parserOnly(this.renderer(Relationship.class,
                        relationship -> relationship.eContainer() == null
                                && !(relationship instanceof Feature),
                        this.dispatch::serializeRelationship)));
    }

    /** The catch-all generic declaration, always last: every element without a more specific rule gets it. */
    private List<ElementRenderer> genericDeclarationRenderers() {
        return List.of(this.renderer(element -> true,
                this.definitionAndUsageRenderer::serializeDeclaration));
    }

    /** Creates a rule matching instances of {@code elementType} and its subtypes. */
    private <T extends Element> ElementRenderer renderer(
            final Class<T> elementType, final Consumer<T> render) {
        return this.renderer(elementType, element -> true, render);
    }

    /**
     * Creates a rule matching instances of {@code elementType} and its subtypes that also satisfy {@code supports}.
     */
    private <T extends Element> ElementRenderer renderer(
            final Class<T> elementType, final Predicate<T> supports,
            final Consumer<T> render) {
        return new ElementRenderer(elementType::isInstance,
                element -> supports.test(elementType.cast(element)),
                element -> render.accept(elementType.cast(element)), true);
    }

    /** Creates a rule with no type filter, so it is a candidate for every metaclass. */
    private ElementRenderer renderer(final Predicate<Element> supports,
            final Consumer<Element> render) {
        return new ElementRenderer(element -> true, supports, render, true);
    }

    /**
     * Creates a rule matching exactly {@code elementType}, not its subtypes; use it when subclasses have their own
     * syntax.
     */
    private ElementRenderer renderer(final EClass elementType,
            final Consumer<Element> render) {
        return this.renderer(elementType, element -> true, render);
    }

    /** Creates a rule matching exactly {@code elementType}, not its subtypes, that also satisfies {@code supports}. */
    private ElementRenderer renderer(final EClass elementType,
            final Predicate<Element> supports,
            final Consumer<Element> render) {
        return new ElementRenderer(element -> element.eClass() == elementType,
                supports, render, true);
    }

    /**
     * Restricts a rule to fragment parses: {@link #createElementRenderers()} drops it when a whole document is
     * serialized, which never needs nor pays for it.
     */
    private ElementRenderer parserOnly(final ElementRenderer rule) {
        return new ElementRenderer(rule.type(), rule.supports(),
                rule.render(), !this.context.options().parserEntryPoint().isBlank());
    }

    /**
     * An ordered rendering rule: {@code type} is a metaclass-only filter (cacheable), {@code supports} the per-instance
     * predicate, and {@code enabled} is false for parser-only rules outside fragment parses.
     */
    private record ElementRenderer(Predicate<Element> type,
            Predicate<Element> supports, Consumer<Element> render,
            boolean enabled) {
    }

    /**
     * Writes the generic declaration of {@code element}, bypassing the rule list; used when a specialized renderer
     * wants the full declaration form.
     */
    public void serializeDeclaration(final Element element) {
        this.definitionAndUsageRenderer.serializeDeclaration(element);
    }

    /**
     * Writes a body relationship in its membership syntax; {@code siblings} and {@code relationshipIndex} give its
     * position in the owner's body.
     */
    public void serializeRelationship(final Relationship relationship,
            final List<Relationship> siblings, final int relationshipIndex) {
        this.relationshipRenderer.serializeRelationship(relationship, siblings, relationshipIndex);
    }

    /**
     * Writes an expression parsed or serialized as a standalone element on its own line; blank text writes nothing.
     * Element dispatch already wrote the comments attached before it.
     */
    private void serializeExpression(final Expression expression) {
        final String text = this.referenceWriter.standaloneExpressionText(expression);
        if (!text.isBlank()) {
            this.referenceWriter.writeExpression(this.context.output(), expression, text).newline();
        }
    }
}
