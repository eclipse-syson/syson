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
package org.eclipse.syson.sysml.parser.internal;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.misc.Interval;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.syson.sysml.parser.DanglingLink;
import org.eclipse.syson.sysml.parser.ParserUtils;
import org.eclipse.syson.sysml.parser.adapter.CommentAdapter;
import org.eclipse.syson.sysml.parser.adapter.CommentBodyAdapter;
import org.eclipse.syson.sysml.parser.adapter.CommentKind;
import org.eclipse.syson.sysml.parser.adapter.ICommentAdapter;
import org.eclipse.syson.sysml.parser.adapter.ITextAdapter;
import org.eclipse.syson.sysml.parser.adapter.NumericLiteralFormatAdapter;
import org.eclipse.syson.sysml.parser.adapter.ParsedComment;
import org.eclipse.syson.sysml.parser.adapter.StringLiteralFormatAdapter;
import org.omg.sysml.lang.sysml.CollectExpression;
import org.omg.sysml.lang.sysml.Comment;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Expression;
import org.omg.sysml.lang.sysml.Feature;
import org.omg.sysml.lang.sysml.FeatureChainExpression;
import org.omg.sysml.lang.sysml.FeatureDirectionKind;
import org.omg.sysml.lang.sysml.FeatureValue;
import org.omg.sysml.lang.sysml.IndexExpression;
import org.omg.sysml.lang.sysml.InvocationExpression;
import org.omg.sysml.lang.sysml.OperatorExpression;
import org.omg.sysml.lang.sysml.OwningMembership;
import org.omg.sysml.lang.sysml.ParameterMembership;
import org.omg.sysml.lang.sysml.PortionKind;
import org.omg.sysml.lang.sysml.RequirementConstraintKind;
import org.omg.sysml.lang.sysml.SelectExpression;
import org.omg.sysml.lang.sysml.StateSubactionKind;
import org.omg.sysml.lang.sysml.SysMLFactory;
import org.omg.sysml.lang.sysml.SysMLPackage;
import org.omg.sysml.lang.sysml.TransitionFeatureKind;
import org.omg.sysml.lang.sysml.TriggerKind;
import org.omg.sysml.lang.sysml.VisibilityKind;

/**
 * ANTLR parser base class that builds SysON EMF objects directly from grammar actions.
 * <p>
 * Temporary values are held on an operand stack while {@link IEObjectContext object contexts} delimit stable model
 * construction boundaries. Text adapters preserve syntax needed for serialization, whereas cross-reference targets are
 * recorded as {@link DanglingLink dangling links} for the separate linking pass.
 * </p>
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public abstract class AbstractSysMLEMFParser extends Parser {

    /**
     * Converts ANTLR syntax errors into EMF diagnostics returned with the parse result.
     */
    private static final class DiagnosticErrorListener extends BaseErrorListener {

        /**
         * The {@link List} of {@link Diagnostic} to populate while parsing.
         */
        private final List<Diagnostic> diagnostics;

        /**
         * Constructor.
         *
         * @param diagnostics
         *            the {@link List} of {@link Diagnostic} to populate while parsing
         */
        private DiagnosticErrorListener(List<Diagnostic> diagnostics) {
            this.diagnostics = diagnostics;
        }

        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
            Diagnostic child = new BasicDiagnostic(Diagnostic.ERROR, null, 0, msg, new Object[] { line, charPositionInLine });
            this.diagnostics.add(child);
        }

    }

    /**
     * The stack used to hold temporary results.
     *
     */
    private final Deque<Object> operandStack = new ArrayDeque<>();

    /**
     * The {@link List} of dangling {@link EReference}.
     */
    private final List<DanglingLink> danglingLinks = new ArrayList<>();

    /**
     * The {@link Set} of {@link EObject} with {@link ITextAdapter}.
     */
    private final Set<EObject> textAdapted = new LinkedHashSet<>();

    /**
     * Hidden comment token indexes already attached to an {@link EObject}.
     */
    private final Set<Integer> attachedCommentTokenIndexes = new HashSet<>();

    /**
     * The stack used to hold {@link IEObjectContext} to be affected.
     */
    private final Deque<IEObjectContext> contextStack = new ArrayDeque<>();

    /** Recovery results with no parsed assignments, and their otherwise empty owning memberships. */
    private final Set<EObject> emptyRecoveryResults = new HashSet<>();

    /** Object contexts whose nested grammar rules reported syntax errors. */
    private final Set<IEObjectContext> contextsWithSyntaxErrors = new HashSet<>();

    /**
     * The factory used to create SysML objects with textual defaults.
     */
    private final SysMLFactory sysMLFactory = new SysMLParsingFactoryImpl();

    /**
     * The internal {@link Diagnostic} {@link List}.
     */
    private List<Diagnostic> internalDiagnostics;

    /**
     * Constructor.
     *
     * @param input
     *            the input {@link TokenStream}
     *
     */
    public AbstractSysMLEMFParser(TokenStream input) {
        super(input);
    }

    protected void attachSpecSymb() {
        this.attach(ITextAdapter.SPECIALIZES_SYMBOL);
    }

    protected void attachSpec() {
        this.attach(ITextAdapter.SPECIALIZES);
    }

    protected void attachSubsetSymb() {
        this.attach(ITextAdapter.SUBSETS_SYMBOL);
    }

    protected void attachSubset() {
        this.attach(ITextAdapter.SUBSETS);
    }

    protected void attachRefSymb() {
        this.attach(ITextAdapter.REFERENCES_SYMBOL);
    }

    protected void attachRef() {
        this.attach(ITextAdapter.REFERENCES);
    }

    protected void attachCrossSymb() {
        this.attach(ITextAdapter.CROSSES_SYMBOL);
    }

    protected void attachCross() {
        this.attach(ITextAdapter.CROSSES);
    }

    protected void attachRedefSymb() {
        this.attach(ITextAdapter.REDEFINES_SYMBOL);
    }

    protected void attachRedef() {
        this.attach(ITextAdapter.REDEFINES);
    }

    protected void attachDefinedBy() {
        this.attach(ITextAdapter.DEFINED_BY);
    }

    protected void attachTypingClauseStart() {
        final Token previous = this.previousDefaultToken(
                this.getCurrentToken().getTokenIndex());
        if (previous != null && (":".equals(previous.getText())
                || "by".equals(previous.getText()))) {
            this.attach(ITextAdapter.TYPING_CLAUSE_START);
            if ("by".equals(previous.getText())) {
                this.attach(ITextAdapter.DEFINED_BY);
            }
        }
    }

    protected void attachExplicitStateUsageKeyword() {
        this.attach(ITextAdapter.EXPLICIT_STATE_USAGE_KEYWORD);
    }

    protected void attachDeclarationKeywordOmitted() {
        this.attach(ITextAdapter.DECLARATION_KEYWORD_OMITTED);
    }

    protected void attachExplicitRepKeyword() {
        this.attach(ITextAdapter.EXPLICIT_REP_KEYWORD);
    }

    protected void attachExplicitInterfaceConnectKeyword() {
        this.attach(ITextAdapter.EXPLICIT_INTERFACE_CONNECT_KEYWORD);
    }

    protected void attachExplicitDeclarationKeyword() {
        this.attach(ITextAdapter.EXPLICIT_DECLARATION_KEYWORD);
    }

    protected void attachEmptyParenthesizedExpression() {
        this.attach(ITextAdapter.EMPTY_PARENTHESIZED_EXPRESSION);
        this.attach(ITextAdapter.PARENTHESIZED_EXPRESSION);
    }

    protected void attachNonUniqueAfterOrdered() {
        this.attach(ITextAdapter.NONUNIQUE_AFTER_ORDERED);
    }

    protected void attachBodyBlock() {
        this.attach(ITextAdapter.BODY_BLOCK);
    }

    protected void attachInterfaceConnectorDeclaration() {
        this.attach(ITextAdapter.INTERFACE_CONNECTOR_DECLARATION);
    }

    protected void attachMessageUsage() {
        this.attach(ITextAdapter.MESSAGE_USAGE);
    }

    protected void attachParenthesizedFeatureValueExpression(ParserRuleContext expression) {
        if (this.isParenthesized(expression)) {
            this.attach(ITextAdapter.PARENTHESIZED_FEATURE_VALUE_EXPRESSION);
        }
    }

    protected void attachFirstMembership() {
        this.attach(ITextAdapter.FIRST_MEMBERSHIP);
    }

    protected void attachCommentKeyword() {
        this.attach(ITextAdapter.COMMENT_KEYWORD);
    }

    protected void attachFeatureValueDefault() {
        this.attach(ITextAdapter.FEATURE_VALUE_DEFAULT);
    }

    protected void attachFeatureValueDefaultEquals() {
        this.attach(ITextAdapter.FEATURE_VALUE_DEFAULT_EQUALS);
    }

    protected void attachFeatureValueDefaultInitial() {
        this.attach(ITextAdapter.FEATURE_VALUE_DEFAULT_INITIAL);
    }

    protected void attachExplicitRefPrefix() {
        this.attach(ITextAdapter.EXPLICIT_REF_PREFIX);
    }

    protected void attachEndFeatureKeyword() {
        this.attach(ITextAdapter.END_FEATURE_KEYWORD);
    }

    protected void attachExplicitPublicVisibility() {
        this.attach(ITextAdapter.EXPLICIT_PUBLIC_VISIBILITY);
    }

    protected void attachQuotedAliasTarget() {
        this.attach(ITextAdapter.QUOTED_ALIAS_TARGET);
    }

    protected void attachSatisfyAssert() {
        this.attach(ITextAdapter.SATISFY_ASSERT);
    }

    protected void attachRequirementConstraintKeyword() {
        this.attach(ITextAdapter.REQUIREMENT_CONSTRAINT_KEYWORD);
    }

    protected void attachFlowFrom() {
        this.attach(ITextAdapter.FLOW_FROM);
    }

    protected void attachCompactIndividualDefinition() {
        this.attach(ITextAdapter.COMPACT_INDIVIDUAL_DEFINITION);
    }

    protected void attachCompactIndividualUsage() {
        this.attach(ITextAdapter.COMPACT_INDIVIDUAL_USAGE);
    }

    protected void attachMetadataAtSign() {
        this.attach(ITextAdapter.METADATA_AT_SIGN);
    }

    protected void attachMetadataColon() {
        this.attach(ITextAdapter.METADATA_COLON);
    }

    protected void attachConnectionKeyword() {
        if (this.getCurrentToken() != null
                && "connect".equals(this.getCurrentToken().getText())) {
            this.attach(ITextAdapter.CONNECTION_KEYWORD);
        }
    }

    protected void attachPrefixMetadata() {
        this.attach(ITextAdapter.PREFIX_METADATA);
    }

    protected void attachQuotedName() {
        this.attach(ITextAdapter.QUOTED_NAME);
    }

    protected void attachQuotedShortName() {
        this.attach(ITextAdapter.QUOTED_SHORT_NAME);
    }

    protected void attachLateNonUnique() {
        this.attach(ITextAdapter.LATE_NONUNIQUE);
    }

    protected void attachEnumKeywordOmitted() {
        this.attach(ITextAdapter.ENUM_KEYWORD_OMITTED);
    }

    protected void attachDependencyFromOmitted() {
        this.attach(ITextAdapter.DEPENDENCY_FROM_OMITTED);
    }

    protected void attachExplicitTransitionKeyword() {
        this.attach(ITextAdapter.EXPLICIT_TRANSITION_KEYWORD);
    }

    protected void attachTransitionFirstKeywordOmitted() {
        this.attach(ITextAdapter.TRANSITION_FIRST_KEYWORD_OMITTED);
    }

    protected void attachExplicitActionNodeKeyword() {
        this.attach(ITextAdapter.EXPLICIT_ACTION_NODE_KEYWORD);
    }

    protected void attachDefaultTargetSuccession() {
        this.attach(ITextAdapter.DEFAULT_TARGET_SUCCESSION);
    }

    protected void attachSuccessionKeyword() {
        this.attach(ITextAdapter.SUCCESSION_KEYWORD);
    }

    /**
     * Marks the current expression operand as originally enclosed in parentheses.
     */
    protected void attachParenthesizedExpression() {
        if (!this.operandStack.isEmpty() && this.operandStack.getLast() instanceof EObject eObject) {
            eObject.eAdapters().add(ITextAdapter.PARENTHESIZED_EXPRESSION);
            this.textAdapted.add(eObject);
        }
    }

    protected void attachParenthesizedLambdaInvocation(ParserRuleContext ctx) {
        if (ctx == null || ctx.getStart() == null || ctx.getStop() == null
                || this.contextStack.isEmpty()
                || !"(".equals(ctx.getStart().getText())
                || !")".equals(ctx.getStop().getText())) {
            return;
        }
        boolean hasBody = false;
        for (int index = ctx.getStart().getTokenIndex(); index <= ctx.getStop().getTokenIndex(); index++) {
            if ("{".equals(this.getInputStream().get(index).getText())) {
                hasBody = true;
                break;
            }
        }
        if (hasBody) {
            this.attach(ITextAdapter.PARENTHESIZED_LAMBDA_ARGUMENT);
        }
    }

    protected void attachInterfaceMultiplicityBeforeType(
            ParserRuleContext ctx) {
        if (ctx == null || ctx.getStart() == null) {
            return;
        }
        int endIndex = ctx.getStop() == null ? this.getInputStream().index()
                : ctx.getStop().getTokenIndex();
        int multiplicityIndex = -1;
        int typeIndex = -1;
        for (int index = ctx.getStart().getTokenIndex(); index <= endIndex; index++) {
            String token = this.getInputStream().get(index).getText();
            if (multiplicityIndex < 0 && "[".equals(token)) {
                multiplicityIndex = index;
            } else if (typeIndex < 0 && ":".equals(token)) {
                typeIndex = index;
            }
        }
        if (multiplicityIndex >= 0 && typeIndex >= 0
                && multiplicityIndex < typeIndex) {
            this.attach(ITextAdapter.INTERFACE_MULTIPLICITY_BEFORE_TYPE);
        }
    }

    protected void attachTrailingComma(ParserRuleContext ctx) {
        if (ctx == null || ctx.getStart() == null) {
            return;
        }
        int endIndex = ctx.getStop() == null ? this.getInputStream().index() - 1
                : ctx.getStop().getTokenIndex();
        if (endIndex > ctx.getStart().getTokenIndex()
                && ",".equals(this.getInputStream().get(endIndex - 1).getText())) {
            if (!this.operandStack.isEmpty()
                    && this.operandStack.getLast() instanceof EObject eObject) {
                eObject.eAdapters().add(ITextAdapter.TRAILING_COMMA);
                this.textAdapted.add(eObject);
            } else {
                this.attach(ITextAdapter.TRAILING_COMMA);
            }
        }
    }

    /**
     * Attaches the given {@link ITextAdapter}.
     *
     * @param adapter
     *            the {@link ITextAdapter} to attach
     */
    private void attach(ITextAdapter adapter) {
        this.contextStack.getLast().attach(adapter);
    }

    private boolean isParenthesized(ParserRuleContext context) {
        if (context == null || context.getStart() == null || context.getStop() == null) {
            return false;
        }
        Token start = context.getStart();
        Token stop = context.getStop();
        if ("(".equals(start.getText()) && ")".equals(stop.getText())) {
            return true;
        }
        Token previous = this.previousDefaultToken(start.getTokenIndex());
        Token next = this.nextDefaultToken(stop.getTokenIndex());
        return previous != null && next != null && "(".equals(previous.getText()) && ")".equals(next.getText());
    }

    private Token previousDefaultToken(int tokenIndex) {
        for (int index = tokenIndex - 1; index >= 0; index--) {
            Token token = this.getInputStream().get(index);
            if (token.getChannel() == Token.DEFAULT_CHANNEL) {
                return token;
            }
        }
        return null;
    }

    private Token nextDefaultToken(int tokenIndex) {
        for (int index = tokenIndex + 1; index < this.getInputStream().size(); index++) {
            Token token = this.getInputStream().get(index);
            if (token.getChannel() == Token.DEFAULT_CHANNEL) {
                return token;
            }
        }
        return null;
    }

    /**
     * Sets the internal {@link Diagnostic} {@link List}.
     *
     * @param internalDiagnostics
     *            the {@link Diagnostic} {@link List}
     */
    public void setInternalDiagnostics(List<Diagnostic> internalDiagnostics) {
        this.internalDiagnostics = internalDiagnostics;
    }

    /**
     * Gets the internal {@link Diagnostic} {@link List}.
     *
     * @return the internal {@link Diagnostic} {@link List}
     */
    public List<Diagnostic> getInternalDiagnostics() {
        return this.internalDiagnostics;
    }

    /**
     * Gets the {@link ANTLRErrorListener} that will populate the given {@link List} of {@link Diagnostic} with syntax
     * errors.
     *
     * @param diagnostics
     *            the {@link List} of {@link Diagnostic} to populate while parsing
     * @return an error listener that appends syntax errors to {@code diagnostics}
     */
    public ANTLRErrorListener getErrorListener(List<Diagnostic> diagnostics) {
        return new DiagnosticErrorListener(diagnostics);
    }

    /**
     * Gets the {@link List} of {@link DanglingLink}.
     *
     * @return the {@link List} of {@link DanglingLink}
     */
    public List<DanglingLink> getDanglingLinks() {
        return this.danglingLinks;
    }

    /**
     * Gets the {@link Set} of {@link EObject} that have a {@link ITextAdapter}.
     *
     * @return the {@link Set} of {@link EObject} that have a {@link ITextAdapter}
     */
    public Set<EObject> getTextAdapted() {
        return this.textAdapted;
    }

    /**
     * Tells if there is a result.
     *
     * @return <code>true</code> if there is a result, <code>false</code> otherwise
     */
    public boolean hasResult() {
        return !this.operandStack.isEmpty();
    }

    /**
     * Ends the current {@link EObject} context, creates the {@link EObject} and {@linkplain #pushValue(Object) pushes}
     * it as an operand.
     *
     * @param ctx
     *            the completed parser-rule context
     */
    public void end(ParserRuleContext ctx) {
        if (this.contextStack.isEmpty() || !(this.contextStack.getLast() instanceof AbstractContext objectContext)
                || objectContext.getRuleContext() != ctx) {
            this.internalDiagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, null, 0,
                    "Object context does not belong to the completed parser rule", new Object[] { ctx }));
            return;
        }
        // free memory
        ctx.children = null;

        boolean emptyFailedResult = this.contextsWithSyntaxErrors.remove(objectContext) && !objectContext.hasDeferredOperations();
        EObject eObject = this.contextStack.removeLast().createEObject();
        if (emptyFailedResult) {
            this.emptyRecoveryResults.add(eObject);
        }
        if (eObject instanceof Element element) {
            element.getOwnedRelationship().removeIf(this.emptyRecoveryResults::remove);
        }
        if (eObject instanceof OwningMembership membership) {
            boolean removed = membership.getOwnedRelatedElement().removeIf(this.emptyRecoveryResults::remove);
            if (removed && membership.getOwnedRelatedElement().isEmpty()) {
                this.emptyRecoveryResults.add(membership);
            }
        }
        this.attachLeadingComments(ctx, eObject);
        this.attachTrailingComments(ctx, eObject);
        this.attachBodyTrailingComments(ctx, eObject);
        this.attachExpressionSyntax(ctx, eObject);
        this.attachRawCommentBody(ctx, eObject);
        this.attachNumericLiteralFormat(ctx, eObject);
        this.attachStringLiteralFormat(ctx, eObject);
        this.operandStack.addLast(eObject);
    }

    /**
     * Completes partial objects before control returns to the caller after ANTLR recovery skips a rule's end action.
     * The caller can then attach the partial result to its own object context.
     */
    @Override
    public void exitRule() {
        ParserRuleContext ruleContext = this.getContext();
        if (ruleContext.exception != null && !this.contextStack.isEmpty()) {
            // ANTLR can suppress a repeated error notification while it is already recovering.
            this.contextsWithSyntaxErrors.add(this.contextStack.getLast());
        }
        try {
            while (!this.contextStack.isEmpty() && this.contextStack.getLast() instanceof AbstractContext objectContext
                    && objectContext.getRuleContext() == ruleContext) {
                this.end(ruleContext);
            }
        } finally {
            super.exitRule();
        }
    }

    /** Tracks errors in helper rules that do not themselves create an object context. */
    @Override
    public void notifyErrorListeners(Token offendingToken, String message, RecognitionException exception) {
        if (!this.contextStack.isEmpty()) {
            this.contextsWithSyntaxErrors.add(this.contextStack.getLast());
        }
        super.notifyErrorListeners(offendingToken, message, exception);
    }

    protected void attachSameLineFollowingComments(ParserRuleContext ctx) {
        if (ctx == null || this.contextStack.isEmpty()
                || !(this.getTokenStream() instanceof CommonTokenStream commonTokenStream)) {
            return;
        }
        Token stop = ctx.getStop();
        if (stop == null) {
            stop = this.getTokenStream().LT(-1);
        }
        if (stop == null) {
            return;
        }
        List<Token> hiddenTokens = commonTokenStream.getHiddenTokensToRight(stop.getTokenIndex());
        if (hiddenTokens == null || hiddenTokens.isEmpty()) {
            return;
        }
        CommentAdapter adapter = null;
        for (Token token : hiddenTokens) {
            ParsedComment comment = this.toParsedComment(token);
            if (comment != null && token.getLine() == stop.getLine()
                    && this.attachedCommentTokenIndexes.add(token.getTokenIndex())) {
                if (adapter == null) {
                    adapter = new CommentAdapter();
                }
                adapter.addTrailingComment(comment);
            }
        }
        if (adapter != null) {
            this.contextStack.getLast().attachCommentAdapter(adapter);
        }
    }

    private void attachBodyTrailingComments(ParserRuleContext ctx, EObject eObject) {
        if (!(eObject instanceof Element) || ctx == null
                || !(this.getTokenStream() instanceof CommonTokenStream commonTokenStream)) {
            return;
        }
        Token closingBrace = ctx.getStop();
        if (closingBrace == null && this.getCurrentToken() != null) {
            closingBrace = this.previousDefaultToken(this.getCurrentToken().getTokenIndex());
        }
        if (closingBrace == null || !"}".equals(closingBrace.getText())) {
            return;
        }
        List<Token> hiddenTokens = commonTokenStream.getHiddenTokensToLeft(closingBrace.getTokenIndex());
        if (hiddenTokens == null || hiddenTokens.isEmpty()) {
            return;
        }
        CommentAdapter adapter = null;
        for (Token token : hiddenTokens) {
            ParsedComment comment = this.toParsedComment(token);
            if (comment != null && this.attachedCommentTokenIndexes.add(token.getTokenIndex())) {
                if (adapter == null) {
                    adapter = this.commentAdapter(eObject);
                }
                adapter.addTrailingComment(comment);
            }
        }
    }

    private void attachExpressionSyntax(ParserRuleContext ctx, EObject eObject) {
        if (!(eObject instanceof Expression)) {
            return;
        }
        if (this.isParenthesized(ctx)) {
            eObject.eAdapters().add(ITextAdapter.PARENTHESIZED_EXPRESSION);
            this.textAdapted.add(eObject);
        }
        if (!this.isNumericLiteral(eObject)) {
            this.attachFollowingComments(ctx, eObject);
            this.attachCommentsInsideExpression(ctx, eObject);
        }
    }

    private void attachCommentsInsideExpression(ParserRuleContext ctx, EObject eObject) {
        if (ctx == null || ctx.getStart() == null
                || !(this.getTokenStream() instanceof CommonTokenStream commonTokenStream)) {
            return;
        }
        // end() runs before ANTLR assigns ctx.stop, so recover the closing default-channel token
        // to preserve comments placed between the parentheses of an empty expression.
        Token stop = ctx.getStop();
        if (stop == null && this.getCurrentToken() != null) {
            stop = this.previousDefaultToken(this.getCurrentToken().getTokenIndex());
        }
        if ((stop == null) || !"NullExpression".equals(eObject.eClass().getName()) || !"(".equals(ctx.getStart().getText())
                || !")".equals(stop.getText())) {
            return;
        }
        CommentAdapter adapter = null;
        for (Token token : commonTokenStream.getTokens(ctx.getStart().getTokenIndex(), stop.getTokenIndex())) {
            ParsedComment comment = this.toParsedComment(token);
            if (comment != null) {
                if (adapter == null) {
                    adapter = this.commentAdapter(eObject);
                }
                if (!adapter.getLeadingComments().contains(comment) && !adapter.getTrailingComments().contains(comment)) {
                    adapter.addTrailingComment(comment);
                }
            }
        }
    }

    private void attachNumericLiteralFormat(ParserRuleContext ctx, EObject eObject) {
        if (!this.isNumericLiteral(eObject)) {
            return;
        }
        String literalText = this.numericLiteralText(ctx);
        if (literalText.isBlank()) {
            return;
        }
        try {
            eObject.eAdapters().add(NumericLiteralFormatAdapter.parse(literalText));
            this.textAdapted.add(eObject);
        } catch (IllegalArgumentException exception) {
            // Numeric format preservation must not affect semantic parsing.
        }
    }

    private void attachStringLiteralFormat(ParserRuleContext ctx, EObject eObject) {
        if (!(eObject instanceof org.omg.sysml.lang.sysml.LiteralString literal) || ctx == null || ctx.getStart() == null) {
            return;
        }
        String literalText = ctx.getStart().getText();
        if (literalText != null && literalText.length() >= 2 && literalText.startsWith("\"")
                && literalText.endsWith("\"")) {
            StringLiteralFormatAdapter adapter = new StringLiteralFormatAdapter(literalText);
            if (!adapter.isCanonicalFor(literal.getValue())) {
                eObject.eAdapters().add(adapter);
                this.textAdapted.add(eObject);
            }
        }
    }

    private String numericLiteralText(ParserRuleContext ctx) {
        if (ctx == null || ctx.getStart() == null) {
            return "";
        }
        TokenStream tokenStream = this.getTokenStream();
        if (tokenStream == null) {
            return "";
        }
        if (ctx.getStop() != null) {
            return tokenStream.getText(ctx);
        }
        int startIndex = ctx.getStart().getTokenIndex();
        int stopIndex = this.getCurrentToken().getTokenIndex() - 1;
        if (startIndex < 0 || stopIndex < startIndex) {
            return "";
        }
        return tokenStream.getText(Interval.of(startIndex, stopIndex));
    }

    private boolean isNumericLiteral(EObject eObject) {
        if (eObject == null || eObject.eClass() == null) {
            return false;
        }
        String className = eObject.eClass().getName();
        return "LiteralInteger".equals(className) || "LiteralRational".equals(className);
    }

    private void attachRawCommentBody(ParserRuleContext ctx, EObject eObject) {
        if (!(eObject instanceof Comment) || ctx == null) {
            return;
        }
        String rawBody = this.rawBlockCommentText(ctx);
        if (!rawBody.isBlank()) {
            eObject.eAdapters().add(new CommentBodyAdapter(rawBody));
        }
    }

    private String rawBlockCommentText(ParserRuleContext ctx) {
        if (!(this.getTokenStream() instanceof CommonTokenStream commonTokenStream) || ctx.getStart() == null
                || ctx.getStop() == null) {
            return "";
        }
        for (Token token : commonTokenStream.getTokens(ctx.getStart().getTokenIndex(), ctx.getStop().getTokenIndex())) {
            String text = token.getText();
            if (text != null && text.startsWith("/*") && text.endsWith("*/")) {
                return text;
            }
        }
        return "";
    }

    private void attachLeadingComments(ParserRuleContext ctx, EObject eObject) {
        if (ctx == null || ctx.getStart() == null || eObject == null || !(this.getTokenStream() instanceof CommonTokenStream commonTokenStream)) {
            return;
        }
        List<Token> hiddenTokens = commonTokenStream.getHiddenTokensToLeft(ctx.getStart().getTokenIndex());
        if (hiddenTokens == null || hiddenTokens.isEmpty()) {
            return;
        }
        CommentAdapter adapter = null;
        for (Token token : hiddenTokens) {
            ParsedComment comment = this.toParsedComment(token);
            if (comment != null && this.attachedCommentTokenIndexes.add(token.getTokenIndex())) {
                if (adapter == null) {
                    adapter = this.commentAdapter(eObject);
                }
                adapter.addLeadingComment(comment);
            }
        }
    }

    private void attachTrailingComments(ParserRuleContext ctx, EObject eObject) {
        if (ctx == null || ctx.getStart() == null || ctx.getStop() == null || ctx.getStart() == ctx.getStop()
                || eObject == null) {
            return;
        }
        if (!(this.getTokenStream() instanceof CommonTokenStream commonTokenStream)) {
            return;
        }
        List<Token> hiddenTokens = commonTokenStream.getHiddenTokensToLeft(ctx.getStop().getTokenIndex());
        if (hiddenTokens == null || hiddenTokens.isEmpty()) {
            return;
        }
        CommentAdapter adapter = null;
        for (Token token : hiddenTokens) {
            ParsedComment comment = this.toParsedComment(token);
            if (comment != null && this.attachedCommentTokenIndexes.add(token.getTokenIndex())) {
                if (adapter == null) {
                    adapter = this.commentAdapter(eObject);
                }
                adapter.addTrailingComment(comment);
            }
        }
    }

    protected void attachFollowingComments(ParserRuleContext ctx) {
        this.attachFollowingComments(ctx, null);
    }

    private void attachFollowingComments(ParserRuleContext ctx, EObject eObject) {
        if (ctx == null || ctx.getStop() == null || eObject == null && this.contextStack.isEmpty()) {
            return;
        }
        if (!(this.getTokenStream() instanceof CommonTokenStream commonTokenStream)) {
            return;
        }
        List<Token> hiddenTokens = commonTokenStream.getHiddenTokensToRight(ctx.getStop().getTokenIndex());
        if (hiddenTokens == null || hiddenTokens.isEmpty()) {
            return;
        }
        CommentAdapter adapter = null;
        for (Token token : hiddenTokens) {
            ParsedComment comment = this.toParsedComment(token);
            if (comment != null && this.attachedCommentTokenIndexes.add(token.getTokenIndex())) {
                if (adapter == null) {
                    adapter = new CommentAdapter();
                }
                adapter.addTrailingComment(comment);
            }
        }
        if (adapter != null) {
            if (eObject == null) {
                this.contextStack.getLast().attachCommentAdapter(adapter);
            } else {
                eObject.eAdapters().add(adapter);
            }
        }
    }

    private ParsedComment toParsedComment(Token token) {
        if (token.getType() == SysMLParser.SL_NOTE) {
            return new ParsedComment(CommentKind.LINE, trimLineTerminator(token.getText()));
        }
        if (token.getType() == SysMLParser.ML_NOTE) {
            return new ParsedComment(CommentKind.BLOCK, token.getText());
        }
        return null;
    }

    protected void attachTrailingComments() {
        if (!(this.getTokenStream() instanceof CommonTokenStream commonTokenStream)) {
            return;
        }
        List<Token> hiddenTokens = commonTokenStream.getHiddenTokensToLeft(this.getCurrentToken().getTokenIndex());
        if (hiddenTokens == null || hiddenTokens.isEmpty()) {
            return;
        }
        CommentAdapter adapter = null;
        for (Token token : hiddenTokens) {
            ParsedComment comment = this.toParsedComment(token);
            if (comment != null && this.attachedCommentTokenIndexes.add(token.getTokenIndex())) {
                if (adapter == null) {
                    adapter = new CommentAdapter();
                }
                adapter.addTrailingComment(comment);
            }
        }
        if (adapter != null) {
            this.contextStack.getLast().attachCommentAdapter(adapter);
        }
    }

    private CommentAdapter commentAdapter(EObject eObject) {
        return eObject.eAdapters().stream().filter(adapter -> adapter.isAdapterForType(ICommentAdapter.class))
                .filter(CommentAdapter.class::isInstance).map(CommentAdapter.class::cast).findFirst().orElseGet(() -> {
                    CommentAdapter adapter = new CommentAdapter();
                    eObject.eAdapters().add(adapter);
                    return adapter;
                });
    }

    private static String trimLineTerminator(String text) {
        if (text == null) {
            return "";
        }
        return text.replaceFirst("\\R$", "");
    }

    /**
     * Peeks the current value from the operand stack.
     *
     * @param <T>
     *            the kind of value
     * @return the current value from the operand stack
     */
    public <T> T peekValue() {
        return (T) this.operandStack.getLast();
    }

    /**
     * Pushes the given value to the operand stack.
     *
     * @param value
     *            the value to push
     */
    protected void pushValue(Object value) {
        this.operandStack.addLast(value);
    }

    /**
     * Pushes the text of the token most recently consumed by the parser.
     */
    protected void pushPreviousTokenText() {
        this.pushValue(this.previousTokenText());
    }

    /**
     * Gets the text of the token most recently consumed by the parser.
     *
     * @return the consumed token text
     */
    protected String previousTokenText() {
        return this.getTokenStream().LT(-1).getText();
    }

    /**
     * Gets the source text consumed since entering the given parser rule context without requiring ANTLR to build a
     * parse tree. This can be used from an embedded action before ANTLR has assigned the context's stop token.
     *
     * @param ctx
     *            the parser rule context
     * @return the source text consumed in the context so far
     */
    protected String currentContextText(ParserRuleContext ctx) {
        int startIndex = ctx.getStart().getTokenIndex();
        int stopIndex = this.getTokenStream().LT(-1).getTokenIndex();
        return this.getTokenStream().getText(Interval.of(startIndex, stopIndex));
    }

    /**
     * Pops the current value from operand stack.
     *
     * @param <T>
     *            the expected value type
     * @return the current value from operand stack
     */
    protected <T> T popValue() {
        return (T) this.operandStack.removeLast();
    }

    /**
     * Starts an {@link IEObjectContext} for the given {@link EClass}.
     *
     * @param ruleContext
     *            the current {@link ParserRuleContext}.
     * @param eCls
     *            the {@link EClass}
     */
    protected void start(ParserRuleContext ruleContext, EClass eCls) {
        this.contextStack.addLast(new StartContext(ruleContext, eCls, this.operandStack, this.danglingLinks, this.textAdapted, this.getInternalDiagnostics(), this.sysMLFactory));
    }

    /**
     * Restarts an {@link IEObjectContext} for the current {@link EObject} operand.
     *
     * @param ruleContext
     *            the current {@link ParserRuleContext}.
     */
    protected void restart(ParserRuleContext ruleContext) {
        try {
            this.contextStack.addLast(new RestartContext(ruleContext, this.operandStack, this.danglingLinks, this.textAdapted, this.getInternalDiagnostics()));
        } catch (Exception e) {
            BasicDiagnostic diagnostic = new BasicDiagnostic(Diagnostic.ERROR, null, 0, e.getMessage(), new Object[] { e });
            this.getInternalDiagnostics().add(diagnostic);
        }
    }

    /**
     * Starts a {@link OperatorExpression} context and sets its first operand.
     */
    protected void startOperatorExpression() {
        try {
            this.start(this.getContext(), this.operatorExpression__());

            Expression operande = (Expression) this.operandStack.removeLast();

            ParameterMembership parameterMembership = this.sysMLFactory.createParameterMembership();
            parameterMembership.setVisibility(VisibilityKind.PRIVATE);

            Feature feature = this.sysMLFactory.createFeature();
            feature.setDirection(FeatureDirectionKind.IN);
            parameterMembership.getOwnedRelatedElement().add(feature);

            FeatureValue featureValue = this.sysMLFactory.createFeatureValue();
            feature.getOwnedRelationship().add(featureValue);
            featureValue.getOwnedRelatedElement().add(operande);

            this.add(this.element_OwnedRelationship(), parameterMembership);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts a {@link FeatureChainExpression} context and sets its first operand.
     */
    protected void startFeatureChainExpression() {
        try {
            this.start(this.getContext(), this.featureChainExpression__());

            Expression operande = (Expression) this.operandStack.removeLast();

            ParameterMembership parameterMembership = this.sysMLFactory.createParameterMembership();
            parameterMembership.setVisibility(VisibilityKind.PRIVATE);

            Feature feature = this.sysMLFactory.createFeature();
            feature.setDirection(FeatureDirectionKind.IN);
            parameterMembership.getOwnedRelatedElement().add(feature);

            FeatureValue featureValue = this.sysMLFactory.createFeatureValue();
            feature.getOwnedRelationship().add(featureValue);
            featureValue.getOwnedRelatedElement().add(operande);

            this.add(this.element_OwnedRelationship(), parameterMembership);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts a {@link IndexExpression} context and sets its first operand.
     */
    protected void startIndexExpression() {
        try {
            this.start(this.getContext(), this.indexExpression__());

            Expression operande = (Expression) this.operandStack.removeLast();

            ParameterMembership parameterMembership = this.sysMLFactory.createParameterMembership();
            parameterMembership.setVisibility(VisibilityKind.PRIVATE);

            Feature feature = this.sysMLFactory.createFeature();
            feature.setDirection(FeatureDirectionKind.IN);
            parameterMembership.getOwnedRelatedElement().add(feature);

            FeatureValue featureValue = this.sysMLFactory.createFeatureValue();
            feature.getOwnedRelationship().add(featureValue);
            featureValue.getOwnedRelatedElement().add(operande);

            this.add(this.element_OwnedRelationship(), parameterMembership);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts a {@link InvocationExpression} context and sets its first operand.
     */
    protected void startInvocationExpression() {
        try {
            this.start(this.getContext(), this.invocationExpression__());

            Expression operande = (Expression) this.operandStack.removeLast();

            ParameterMembership parameterMembership = this.sysMLFactory.createParameterMembership();
            parameterMembership.setVisibility(VisibilityKind.PRIVATE);

            Feature feature = this.sysMLFactory.createFeature();
            feature.setDirection(FeatureDirectionKind.IN);
            parameterMembership.getOwnedRelatedElement().add(feature);

            FeatureValue featureValue = this.sysMLFactory.createFeatureValue();
            feature.getOwnedRelationship().add(featureValue);
            featureValue.getOwnedRelatedElement().add(operande);

            this.add(this.element_OwnedRelationship(), parameterMembership);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts a {@link CollectExpression} context and sets its first operand.
     */
    protected void startCollectExpression() {
        try {
            this.start(this.getContext(), this.collectExpression__());

            Expression operande = (Expression) this.operandStack.removeLast();

            ParameterMembership parameterMembership = this.sysMLFactory.createParameterMembership();
            parameterMembership.setVisibility(VisibilityKind.PRIVATE);

            Feature feature = this.sysMLFactory.createFeature();
            feature.setDirection(FeatureDirectionKind.IN);
            parameterMembership.getOwnedRelatedElement().add(feature);

            FeatureValue featureValue = this.sysMLFactory.createFeatureValue();
            feature.getOwnedRelationship().add(featureValue);
            featureValue.getOwnedRelatedElement().add(operande);

            this.add(this.element_OwnedRelationship(), parameterMembership);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts a {@link SelectExpression} context and sets its first operand.
     */
    protected void startSelectExpression() {
        try {
            this.start(this.getContext(), this.selectExpression__());

            Expression operande = (Expression) this.operandStack.removeLast();

            ParameterMembership parameterMembership = this.sysMLFactory.createParameterMembership();
            parameterMembership.setVisibility(VisibilityKind.PRIVATE);

            Feature feature = this.sysMLFactory.createFeature();
            feature.setDirection(FeatureDirectionKind.IN);
            parameterMembership.getOwnedRelatedElement().add(feature);

            FeatureValue featureValue = this.sysMLFactory.createFeatureValue();
            feature.getOwnedRelationship().add(featureValue);
            featureValue.getOwnedRelatedElement().add(operande);

            this.add(this.element_OwnedRelationship(), parameterMembership);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts a {@link OperatorExpression} context and sets its first operand.
     */
    protected void addOperand() {
        try {
            Expression operande = (Expression) this.operandStack.removeLast();

            ParameterMembership parameterMembership = this.sysMLFactory.createParameterMembership();
            parameterMembership.setVisibility(VisibilityKind.PRIVATE);

            Feature feature = this.sysMLFactory.createFeature();
            parameterMembership.getOwnedRelatedElement().add(feature);

            FeatureValue featureValue = this.sysMLFactory.createFeatureValue();
            feature.getOwnedRelationship().add(featureValue);
            featureValue.getOwnedRelatedElement().add(operande);

            this.add(this.element_OwnedRelationship(), parameterMembership);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the given {@link EStructuralFeature} with the operand at the top of the operand stack.
     *
     * @param feature
     *            the {@link EStructuralFeature}
     */
    protected void set(EStructuralFeature feature) {
        this.contextStack.getLast().set(feature);
    }

    /**
     * Sets the given value on the given {@link EStructuralFeature}.
     *
     * @param feature
     *            the {@link EStructuralFeature}
     * @param value
     *            the value to set
     */
    protected void set(EStructuralFeature feature, Object value) {
        this.contextStack.getLast().set(feature, value);
    }

    /**
     * Adds the given {@link Token} value to the given {@link EAttribute}.
     *
     * @param attribute
     *            the {@link EAttribute}
     * @param token
     *            the {@link Token}
     */
    protected void set(EAttribute attribute, Token token) {
        if (token.getTokenIndex() != -1) {
            Object value = EcoreUtil.createFromString((EDataType) attribute.getEType(), token.getText());
            this.set(attribute, value);
        }
    }

    /**
     * Sets the given {@link Token} string value to the given {@link EAttribute}.
     *
     * @param attribute
     *            the {@link EAttribute}
     * @param token
     *            the {@link Token}
     */
    protected void setStringValue(EAttribute attribute, Token token) {
        if (token.getTokenIndex() != -1) {
            this.set(attribute, ParserUtils.getStringValue(token.getText()));
        }
    }

    /**
     * Gets the {@link String} value from the given escaped value.
     *
     * @param value
     *            the escaped value
     * @return the {@link String} value from the given escaped value
     */
    protected String stringValue(String value) {
        return ParserUtils.getStringValue(value);
    }

    /**
     * Gets the EDouble {@link EDataType}.
     *
     * @return the EDouble {@link EDataType}
     */
    protected EDataType eDouble() {
        return EcorePackage.eINSTANCE.getEDouble();
    }

    /**
     * Gets the given {@link EDataType} value from the given {@link String}.
     *
     * @param type
     *            the {@link EDataType}
     * @param value
     *            the {@link String} value
     * @return the given {@link EDataType} value from the given {@link String}
     */
    protected Object fromString(EDataType type, String value) {
        return EcoreUtil.createFromString(type, value);
    }

    /**
     * Adds the given {@link Token} comment body value to the given {@link EAttribute}.
     *
     * @param attribute
     *            the {@link EAttribute}
     * @param token
     *            the {@link Token}
     */
    protected void setCommentBody(EAttribute attribute, Token token) {
        if (token.getTokenIndex() != -1) {
            this.contextStack.getLast().attach(new CommentBodyAdapter(token.getText()));
            this.set(attribute, ParserUtils.processCommentBody(token.getText()));
        }
    }

    /**
     * Sets <code>true</code> to the given {@link EAttribute}.
     *
     * @param attribute
     *            the {@link EAttribute}
     */
    protected void setTrue(EAttribute attribute) {
        this.contextStack.getLast().set(attribute, true);
    }

    /**
     * Sets <code>false</code> to the given {@link EAttribute}.
     *
     * @param attribute
     *            the {@link EAttribute}
     */
    protected void setFalse(EAttribute attribute) {
        this.contextStack.getLast().set(attribute, false);
    }

    /**
     * Adds the operand at the top of the stack to the given {@link EStructuralFeature}.
     *
     * @param feature
     *            the {@link EStructuralFeature}
     */
    protected void add(EStructuralFeature feature) {
        this.contextStack.getLast().add(feature);
    }

    /**
     * Adds the given value to the given {@link EStructuralFeature}.
     *
     * @param feature
     *            the {@link EStructuralFeature}
     * @param value
     *            the value to add
     */
    protected void add(EStructuralFeature feature, Object value) {
        this.contextStack.getLast().add(feature, value);
    }

    /**
     * Sets a {@link DanglingLink} for the given {@link EReference} with the operand at the top of the stack.
     *
     * @param reference
     *            the {@link EReference}
     * @param eClass
     *            the targeted {@link EClass}
     */
    protected void setLink(EReference reference, EClass eClass) {
        this.contextStack.getLast().setLink(reference, eClass);
    }

    /**
     * Sets a {@link DanglingLink} for the given {@link EReference} with the given value.
     *
     * @param reference
     *            the {@link EReference}
     * @param eClass
     *            the targeted {@link EClass}
     * @param value
     *            the value
     */
    protected void setLink(EReference reference, EClass eClass, String value) {
        this.contextStack.getLast().setLink(reference, eClass, value);
    }

    /**
     * Adds a {@link DanglingLink} for the operand at the top of the stack to the given {@link EReference}.
     *
     * @param reference
     *            the {@link EReference}
     * @param eClass
     *            the targeted {@link EClass}
     */
    protected void addLink(EReference reference, EClass eClass) {
        this.contextStack.getLast().addLink(reference, eClass);
    }

    /**
     * Adds a {@link DanglingLink} for the given value to the given {@link EReference}.
     *
     * @param reference
     *            the {@link EReference}
     * @param eClass
     *            the targeted {@link EClass}
     * @param value
     *            the value
     */
    protected void addLink(EReference reference, EClass eClass, String value) {
        this.contextStack.getLast().addLink(reference, eClass, value);
    }

    protected void pushQualifiedName() {
        this.pushValue(new QualifiedName());
    }

    protected void popQualifiedName() {
        QualifiedName qualifiedName = this.popValue();
        this.pushValue(qualifiedName.toString());
    }

    protected void setGlobalQualifiedName() {
        ((QualifiedName) this.peekValue()).setGlobal(true);
    }

    protected void addQualifiedNameSegment() {
        String segment = this.popValue();
        ((QualifiedName) this.peekValue()).addSegment(segment);
    }

    protected void conjugateQualifiedName() {
        String qualifiedName = this.popValue();
        this.pushValue(QualifiedName.CONJUGATED + qualifiedName);
    }

    /**
     * Gets the EAttribute {@link EClassifier}.
     *
     * @return the EAttribute {@link EClassifier}
     *
     */
    protected EClass eAttribute__() {
        return EcorePackage.eINSTANCE.getEAttribute();
    }

    /**
     * Gets the EAnnotation {@link EClassifier}.
     *
     * @return the EAnnotation {@link EClassifier}
     *
     */
    protected EClass eAnnotation__() {
        return EcorePackage.eINSTANCE.getEAnnotation();
    }

    /**
     * Gets the EClass {@link EClassifier}.
     *
     * @return the EClass {@link EClassifier}
     *
     */
    protected EClass eClass__() {
        return EcorePackage.eINSTANCE.getEClass();
    }

    /**
     * Gets the EClassifier {@link EClassifier}.
     *
     * @return the EClassifier {@link EClassifier}
     *
     */
    protected EClass eClassifier__() {
        return EcorePackage.eINSTANCE.getEClassifier();
    }

    /**
     * Gets the EDataType {@link EClassifier}.
     *
     * @return the EDataType {@link EClassifier}
     *
     */
    protected EClass eDataType__() {
        return EcorePackage.eINSTANCE.getEDataType();
    }

    /**
     * Gets the EEnum {@link EClassifier}.
     *
     * @return the EEnum {@link EClassifier}
     *
     */
    protected EClass eEnum__() {
        return EcorePackage.eINSTANCE.getEEnum();
    }

    /**
     * Gets the EEnumLiteral {@link EClassifier}.
     *
     * @return the EEnumLiteral {@link EClassifier}
     *
     */
    protected EClass eEnumLiteral__() {
        return EcorePackage.eINSTANCE.getEEnumLiteral();
    }

    /**
     * Gets the EFactory {@link EClassifier}.
     *
     * @return the EFactory {@link EClassifier}
     *
     */
    protected EClass eFactory__() {
        return EcorePackage.eINSTANCE.getEFactory();
    }

    /**
     * Gets the EModelElement {@link EClassifier}.
     *
     * @return the EModelElement {@link EClassifier}
     *
     */
    protected EClass eModelElement__() {
        return EcorePackage.eINSTANCE.getEModelElement();
    }

    /**
     * Gets the ENamedElement {@link EClassifier}.
     *
     * @return the ENamedElement {@link EClassifier}
     *
     */
    protected EClass eNamedElement__() {
        return EcorePackage.eINSTANCE.getENamedElement();
    }

    /**
     * Gets the EObject {@link EClassifier}.
     *
     * @return the EObject {@link EClassifier}
     *
     */
    protected EClass eObject__() {
        return EcorePackage.eINSTANCE.getEObject();
    }

    /**
     * Gets the EOperation {@link EClassifier}.
     *
     * @return the EOperation {@link EClassifier}
     *
     */
    protected EClass eOperation__() {
        return EcorePackage.eINSTANCE.getEOperation();
    }

    /**
     * Gets the EPackage {@link EClassifier}.
     *
     * @return the EPackage {@link EClassifier}
     *
     */
    protected EClass ePackage__() {
        return EcorePackage.eINSTANCE.getEPackage();
    }

    /**
     * Gets the EParameter {@link EClassifier}.
     *
     * @return the EParameter {@link EClassifier}
     *
     */
    protected EClass eParameter__() {
        return EcorePackage.eINSTANCE.getEParameter();
    }

    /**
     * Gets the EReference {@link EClassifier}.
     *
     * @return the EReference {@link EClassifier}
     *
     */
    protected EClass eReference__() {
        return EcorePackage.eINSTANCE.getEReference();
    }

    /**
     * Gets the EStructuralFeature {@link EClassifier}.
     *
     * @return the EStructuralFeature {@link EClassifier}
     *
     */
    protected EClass eStructuralFeature__() {
        return EcorePackage.eINSTANCE.getEStructuralFeature();
    }

    /**
     * Gets the ETypedElement {@link EClassifier}.
     *
     * @return the ETypedElement {@link EClassifier}
     *
     */
    protected EClass eTypedElement__() {
        return EcorePackage.eINSTANCE.getETypedElement();
    }

    /**
     * Gets the EStringToStringMapEntry {@link EClassifier}.
     *
     * @return the EStringToStringMapEntry {@link EClassifier}
     *
     */
    protected EClass eStringToStringMapEntry__() {
        return EcorePackage.eINSTANCE.getEStringToStringMapEntry();
    }

    /**
     * Gets the EGenericType {@link EClassifier}.
     *
     * @return the EGenericType {@link EClassifier}
     *
     */
    protected EClass eGenericType__() {
        return EcorePackage.eINSTANCE.getEGenericType();
    }

    /**
     * Gets the ETypeParameter {@link EClassifier}.
     *
     * @return the ETypeParameter {@link EClassifier}
     *
     */
    protected EClass eTypeParameter__() {
        return EcorePackage.eINSTANCE.getETypeParameter();
    }

    /**
     * Gets the EBigDecimal {@link EClassifier}.
     *
     * @return the EBigDecimal {@link EClassifier}
     *
     */
    protected EDataType eBigDecimal__() {
        return EcorePackage.eINSTANCE.getEBigDecimal();
    }

    /**
     * Gets the EBigInteger {@link EClassifier}.
     *
     * @return the EBigInteger {@link EClassifier}
     *
     */
    protected EDataType eBigInteger__() {
        return EcorePackage.eINSTANCE.getEBigInteger();
    }

    /**
     * Gets the EBoolean {@link EClassifier}.
     *
     * @return the EBoolean {@link EClassifier}
     *
     */
    protected EDataType eBoolean__() {
        return EcorePackage.eINSTANCE.getEBoolean();
    }

    /**
     * Gets the EBooleanObject {@link EClassifier}.
     *
     * @return the EBooleanObject {@link EClassifier}
     *
     */
    protected EDataType eBooleanObject__() {
        return EcorePackage.eINSTANCE.getEBooleanObject();
    }

    /**
     * Gets the EByte {@link EClassifier}.
     *
     * @return the EByte {@link EClassifier}
     *
     */
    protected EDataType eByte__() {
        return EcorePackage.eINSTANCE.getEByte();
    }

    /**
     * Gets the EByteArray {@link EClassifier}.
     *
     * @return the EByteArray {@link EClassifier}
     *
     */
    protected EDataType eByteArray__() {
        return EcorePackage.eINSTANCE.getEByteArray();
    }

    /**
     * Gets the EByteObject {@link EClassifier}.
     *
     * @return the EByteObject {@link EClassifier}
     *
     */
    protected EDataType eByteObject__() {
        return EcorePackage.eINSTANCE.getEByteObject();
    }

    /**
     * Gets the EChar {@link EClassifier}.
     *
     * @return the EChar {@link EClassifier}
     *
     */
    protected EDataType eChar__() {
        return EcorePackage.eINSTANCE.getEChar();
    }

    /**
     * Gets the ECharacterObject {@link EClassifier}.
     *
     * @return the ECharacterObject {@link EClassifier}
     *
     */
    protected EDataType eCharacterObject__() {
        return EcorePackage.eINSTANCE.getECharacterObject();
    }

    /**
     * Gets the EDate {@link EClassifier}.
     *
     * @return the EDate {@link EClassifier}
     *
     */
    protected EDataType eDate__() {
        return EcorePackage.eINSTANCE.getEDate();
    }

    /**
     * Gets the EDiagnosticChain {@link EClassifier}.
     *
     * @return the EDiagnosticChain {@link EClassifier}
     *
     */
    protected EDataType eDiagnosticChain__() {
        return EcorePackage.eINSTANCE.getEDiagnosticChain();
    }

    /**
     * Gets the EDouble {@link EClassifier}.
     *
     * @return the EDouble {@link EClassifier}
     *
     */
    protected EDataType eDouble__() {
        return EcorePackage.eINSTANCE.getEDouble();
    }

    /**
     * Gets the EDoubleObject {@link EClassifier}.
     *
     * @return the EDoubleObject {@link EClassifier}
     *
     */
    protected EDataType eDoubleObject__() {
        return EcorePackage.eINSTANCE.getEDoubleObject();
    }

    /**
     * Gets the EEList {@link EClassifier}.
     *
     * @return the EEList {@link EClassifier}
     *
     */
    protected EDataType eEList__() {
        return EcorePackage.eINSTANCE.getEEList();
    }

    /**
     * Gets the EEnumerator {@link EClassifier}.
     *
     * @return the EEnumerator {@link EClassifier}
     *
     */
    protected EDataType eEnumerator__() {
        return EcorePackage.eINSTANCE.getEEnumerator();
    }

    /**
     * Gets the EFeatureMap {@link EClassifier}.
     *
     * @return the EFeatureMap {@link EClassifier}
     *
     */
    protected EDataType eFeatureMap__() {
        return EcorePackage.eINSTANCE.getEFeatureMap();
    }

    /**
     * Gets the EFeatureMapEntry {@link EClassifier}.
     *
     * @return the EFeatureMapEntry {@link EClassifier}
     *
     */
    protected EDataType eFeatureMapEntry__() {
        return EcorePackage.eINSTANCE.getEFeatureMapEntry();
    }

    /**
     * Gets the EFloat {@link EClassifier}.
     *
     * @return the EFloat {@link EClassifier}
     *
     */
    protected EDataType eFloat__() {
        return EcorePackage.eINSTANCE.getEFloat();
    }

    /**
     * Gets the EFloatObject {@link EClassifier}.
     *
     * @return the EFloatObject {@link EClassifier}
     *
     */
    protected EDataType eFloatObject__() {
        return EcorePackage.eINSTANCE.getEFloatObject();
    }

    /**
     * Gets the EInt {@link EClassifier}.
     *
     * @return the EInt {@link EClassifier}
     *
     */
    protected EDataType eInt__() {
        return EcorePackage.eINSTANCE.getEInt();
    }

    /**
     * Gets the EIntegerObject {@link EClassifier}.
     *
     * @return the EIntegerObject {@link EClassifier}
     *
     */
    protected EDataType eIntegerObject__() {
        return EcorePackage.eINSTANCE.getEIntegerObject();
    }

    /**
     * Gets the EJavaClass {@link EClassifier}.
     *
     * @return the EJavaClass {@link EClassifier}
     *
     */
    protected EDataType eJavaClass__() {
        return EcorePackage.eINSTANCE.getEJavaClass();
    }

    /**
     * Gets the EJavaObject {@link EClassifier}.
     *
     * @return the EJavaObject {@link EClassifier}
     *
     */
    protected EDataType eJavaObject__() {
        return EcorePackage.eINSTANCE.getEJavaObject();
    }

    /**
     * Gets the ELong {@link EClassifier}.
     *
     * @return the ELong {@link EClassifier}
     *
     */
    protected EDataType eLong__() {
        return EcorePackage.eINSTANCE.getELong();
    }

    /**
     * Gets the ELongObject {@link EClassifier}.
     *
     * @return the ELongObject {@link EClassifier}
     *
     */
    protected EDataType eLongObject__() {
        return EcorePackage.eINSTANCE.getELongObject();
    }

    /**
     * Gets the EMap {@link EClassifier}.
     *
     * @return the EMap {@link EClassifier}
     *
     */
    protected EDataType eMap__() {
        return EcorePackage.eINSTANCE.getEMap();
    }

    /**
     * Gets the EResource {@link EClassifier}.
     *
     * @return the EResource {@link EClassifier}
     *
     */
    protected EDataType eResource__() {
        return EcorePackage.eINSTANCE.getEResource();
    }

    /**
     * Gets the EResourceSet {@link EClassifier}.
     *
     * @return the EResourceSet {@link EClassifier}
     *
     */
    protected EDataType eResourceSet__() {
        return EcorePackage.eINSTANCE.getEResourceSet();
    }

    /**
     * Gets the EShort {@link EClassifier}.
     *
     * @return the EShort {@link EClassifier}
     *
     */
    protected EDataType eShort__() {
        return EcorePackage.eINSTANCE.getEShort();
    }

    /**
     * Gets the EShortObject {@link EClassifier}.
     *
     * @return the EShortObject {@link EClassifier}
     *
     */
    protected EDataType eShortObject__() {
        return EcorePackage.eINSTANCE.getEShortObject();
    }

    /**
     * Gets the EString {@link EClassifier}.
     *
     * @return the EString {@link EClassifier}
     *
     */
    protected EDataType eString__() {
        return EcorePackage.eINSTANCE.getEString();
    }

    /**
     * Gets the ETreeIterator {@link EClassifier}.
     *
     * @return the ETreeIterator {@link EClassifier}
     *
     */
    protected EDataType eTreeIterator__() {
        return EcorePackage.eINSTANCE.getETreeIterator();
    }

    /**
     * Gets the EInvocationTargetException {@link EClassifier}.
     *
     * @return the EInvocationTargetException {@link EClassifier}
     *
     */
    protected EDataType eInvocationTargetException__() {
        return EcorePackage.eINSTANCE.getEInvocationTargetException();
    }

    /**
     * Gets the SelectExpression {@link EClassifier}.
     *
     * @return the SelectExpression {@link EClassifier}
     *
     */
    protected EClass selectExpression__() {
        return SysMLPackage.eINSTANCE.getSelectExpression();
    }

    /**
     * Gets the OperatorExpression {@link EClassifier}.
     *
     * @return the OperatorExpression {@link EClassifier}
     *
     */
    protected EClass operatorExpression__() {
        return SysMLPackage.eINSTANCE.getOperatorExpression();
    }

    /**
     * Gets the InvocationExpression {@link EClassifier}.
     *
     * @return the InvocationExpression {@link EClassifier}
     *
     */
    protected EClass invocationExpression__() {
        return SysMLPackage.eINSTANCE.getInvocationExpression();
    }

    /**
     * Gets the InstantiationExpression {@link EClassifier}.
     *
     * @return the InstantiationExpression {@link EClassifier}
     *
     */
    protected EClass instantiationExpression__() {
        return SysMLPackage.eINSTANCE.getInstantiationExpression();
    }

    /**
     * Gets the Expression {@link EClassifier}.
     *
     * @return the Expression {@link EClassifier}
     *
     */
    protected EClass expression__() {
        return SysMLPackage.eINSTANCE.getExpression();
    }

    /**
     * Gets the Step {@link EClassifier}.
     *
     * @return the Step {@link EClassifier}
     *
     */
    protected EClass step__() {
        return SysMLPackage.eINSTANCE.getStep();
    }

    /**
     * Gets the Feature {@link EClassifier}.
     *
     * @return the Feature {@link EClassifier}
     *
     */
    protected EClass feature__() {
        return SysMLPackage.eINSTANCE.getFeature();
    }

    /**
     * Gets the Type {@link EClassifier}.
     *
     * @return the Type {@link EClassifier}
     *
     */
    protected EClass type__() {
        return SysMLPackage.eINSTANCE.getType();
    }

    /**
     * Gets the Namespace {@link EClassifier}.
     *
     * @return the Namespace {@link EClassifier}
     *
     */
    protected EClass namespace__() {
        return SysMLPackage.eINSTANCE.getNamespace();
    }

    /**
     * Gets the Element {@link EClassifier}.
     *
     * @return the Element {@link EClassifier}
     *
     */
    protected EClass element__() {
        return SysMLPackage.eINSTANCE.getElement();
    }

    /**
     * Gets the OwningMembership {@link EClassifier}.
     *
     * @return the OwningMembership {@link EClassifier}
     *
     */
    protected EClass owningMembership__() {
        return SysMLPackage.eINSTANCE.getOwningMembership();
    }

    /**
     * Gets the Membership {@link EClassifier}.
     *
     * @return the Membership {@link EClassifier}
     *
     */
    protected EClass membership__() {
        return SysMLPackage.eINSTANCE.getMembership();
    }

    /**
     * Gets the Relationship {@link EClassifier}.
     *
     * @return the Relationship {@link EClassifier}
     *
     */
    protected EClass relationship__() {
        return SysMLPackage.eINSTANCE.getRelationship();
    }

    /**
     * Gets the Documentation {@link EClassifier}.
     *
     * @return the Documentation {@link EClassifier}
     *
     */
    protected EClass documentation__() {
        return SysMLPackage.eINSTANCE.getDocumentation();
    }

    /**
     * Gets the Comment {@link EClassifier}.
     *
     * @return the Comment {@link EClassifier}
     *
     */
    protected EClass comment__() {
        return SysMLPackage.eINSTANCE.getComment();
    }

    /**
     * Gets the AnnotatingElement {@link EClassifier}.
     *
     * @return the AnnotatingElement {@link EClassifier}
     *
     */
    protected EClass annotatingElement__() {
        return SysMLPackage.eINSTANCE.getAnnotatingElement();
    }

    /**
     * Gets the Annotation {@link EClassifier}.
     *
     * @return the Annotation {@link EClassifier}
     *
     */
    protected EClass annotation__() {
        return SysMLPackage.eINSTANCE.getAnnotation();
    }

    /**
     * Gets the TextualRepresentation {@link EClassifier}.
     *
     * @return the TextualRepresentation {@link EClassifier}
     *
     */
    protected EClass textualRepresentation__() {
        return SysMLPackage.eINSTANCE.getTextualRepresentation();
    }

    /**
     * Gets the Import {@link EClassifier}.
     *
     * @return the Import {@link EClassifier}
     *
     */
    protected EClass import__() {
        return SysMLPackage.eINSTANCE.getImport();
    }

    /**
     * Gets the Specialization {@link EClassifier}.
     *
     * @return the Specialization {@link EClassifier}
     *
     */
    protected EClass specialization__() {
        return SysMLPackage.eINSTANCE.getSpecialization();
    }

    /**
     * Gets the FeatureMembership {@link EClassifier}.
     *
     * @return the FeatureMembership {@link EClassifier}
     *
     */
    protected EClass featureMembership__() {
        return SysMLPackage.eINSTANCE.getFeatureMembership();
    }

    /**
     * Gets the Conjugation {@link EClassifier}.
     *
     * @return the Conjugation {@link EClassifier}
     *
     */
    protected EClass conjugation__() {
        return SysMLPackage.eINSTANCE.getConjugation();
    }

    /**
     * Gets the Multiplicity {@link EClassifier}.
     *
     * @return the Multiplicity {@link EClassifier}
     *
     */
    protected EClass multiplicity__() {
        return SysMLPackage.eINSTANCE.getMultiplicity();
    }

    /**
     * Gets the Intersecting {@link EClassifier}.
     *
     * @return the Intersecting {@link EClassifier}
     *
     */
    protected EClass intersecting__() {
        return SysMLPackage.eINSTANCE.getIntersecting();
    }

    /**
     * Gets the Unioning {@link EClassifier}.
     *
     * @return the Unioning {@link EClassifier}
     *
     */
    protected EClass unioning__() {
        return SysMLPackage.eINSTANCE.getUnioning();
    }

    /**
     * Gets the Disjoining {@link EClassifier}.
     *
     * @return the Disjoining {@link EClassifier}
     *
     */
    protected EClass disjoining__() {
        return SysMLPackage.eINSTANCE.getDisjoining();
    }

    /**
     * Gets the Differencing {@link EClassifier}.
     *
     * @return the Differencing {@link EClassifier}
     *
     */
    protected EClass differencing__() {
        return SysMLPackage.eINSTANCE.getDifferencing();
    }

    /**
     * Gets the Redefinition {@link EClassifier}.
     *
     * @return the Redefinition {@link EClassifier}
     *
     */
    protected EClass redefinition__() {
        return SysMLPackage.eINSTANCE.getRedefinition();
    }

    /**
     * Gets the Subsetting {@link EClassifier}.
     *
     * @return the Subsetting {@link EClassifier}
     *
     */
    protected EClass subsetting__() {
        return SysMLPackage.eINSTANCE.getSubsetting();
    }

    /**
     * Gets the FeatureTyping {@link EClassifier}.
     *
     * @return the FeatureTyping {@link EClassifier}
     *
     */
    protected EClass featureTyping__() {
        return SysMLPackage.eINSTANCE.getFeatureTyping();
    }

    /**
     * Gets the TypeFeaturing {@link EClassifier}.
     *
     * @return the TypeFeaturing {@link EClassifier}
     *
     */
    protected EClass typeFeaturing__() {
        return SysMLPackage.eINSTANCE.getTypeFeaturing();
    }

    /**
     * Gets the FeatureInverting {@link EClassifier}.
     *
     * @return the FeatureInverting {@link EClassifier}
     *
     */
    protected EClass featureInverting__() {
        return SysMLPackage.eINSTANCE.getFeatureInverting();
    }

    /**
     * Gets the FeatureChaining {@link EClassifier}.
     *
     * @return the FeatureChaining {@link EClassifier}
     *
     */
    protected EClass featureChaining__() {
        return SysMLPackage.eINSTANCE.getFeatureChaining();
    }

    /**
     * Gets the ReferenceSubsetting {@link EClassifier}.
     *
     * @return the ReferenceSubsetting {@link EClassifier}
     *
     */
    protected EClass referenceSubsetting__() {
        return SysMLPackage.eINSTANCE.getReferenceSubsetting();
    }

    /**
     * Gets the CrossSubsetting {@link EClassifier}.
     *
     * @return the CrossSubsetting {@link EClassifier}
     *
     */
    protected EClass crossSubsetting__() {
        return SysMLPackage.eINSTANCE.getCrossSubsetting();
    }

    /**
     * Gets the Behavior {@link EClassifier}.
     *
     * @return the Behavior {@link EClassifier}
     *
     */
    protected EClass behavior__() {
        return SysMLPackage.eINSTANCE.getBehavior();
    }

    /**
     * Gets the Class {@link EClassifier}.
     *
     * @return the Class {@link EClassifier}
     *
     */
    protected EClass class__() {
        return SysMLPackage.eINSTANCE.getClass_();
    }

    /**
     * Gets the Classifier {@link EClassifier}.
     *
     * @return the Classifier {@link EClassifier}
     *
     */
    protected EClass classifier__() {
        return SysMLPackage.eINSTANCE.getClassifier();
    }

    /**
     * Gets the Subclassification {@link EClassifier}.
     *
     * @return the Subclassification {@link EClassifier}
     *
     */
    protected EClass subclassification__() {
        return SysMLPackage.eINSTANCE.getSubclassification();
    }

    /**
     * Gets the Function {@link EClassifier}.
     *
     * @return the Function {@link EClassifier}
     *
     */
    protected EClass function__() {
        return SysMLPackage.eINSTANCE.getFunction();
    }

    /**
     * Gets the ConstructorExpression {@link EClassifier}.
     *
     * @return the ConstructorExpression {@link EClassifier}
     *
     */
    protected EClass constructorExpression__() {
        return SysMLPackage.eINSTANCE.getConstructorExpression();
    }

    /**
     * Gets the NullExpression {@link EClassifier}.
     *
     * @return the NullExpression {@link EClassifier}
     *
     */
    protected EClass nullExpression__() {
        return SysMLPackage.eINSTANCE.getNullExpression();
    }

    /**
     * Gets the IndexExpression {@link EClassifier}.
     *
     * @return the IndexExpression {@link EClassifier}
     *
     */
    protected EClass indexExpression__() {
        return SysMLPackage.eINSTANCE.getIndexExpression();
    }

    /**
     * Gets the CollectExpression {@link EClassifier}.
     *
     * @return the CollectExpression {@link EClassifier}
     *
     */
    protected EClass collectExpression__() {
        return SysMLPackage.eINSTANCE.getCollectExpression();
    }

    /**
     * Gets the LiteralBoolean {@link EClassifier}.
     *
     * @return the LiteralBoolean {@link EClassifier}
     *
     */
    protected EClass literalBoolean__() {
        return SysMLPackage.eINSTANCE.getLiteralBoolean();
    }

    /**
     * Gets the LiteralExpression {@link EClassifier}.
     *
     * @return the LiteralExpression {@link EClassifier}
     *
     */
    protected EClass literalExpression__() {
        return SysMLPackage.eINSTANCE.getLiteralExpression();
    }

    /**
     * Gets the FeatureReferenceExpression {@link EClassifier}.
     *
     * @return the FeatureReferenceExpression {@link EClassifier}
     *
     */
    protected EClass featureReferenceExpression__() {
        return SysMLPackage.eINSTANCE.getFeatureReferenceExpression();
    }

    /**
     * Gets the MetadataAccessExpression {@link EClassifier}.
     *
     * @return the MetadataAccessExpression {@link EClassifier}
     *
     */
    protected EClass metadataAccessExpression__() {
        return SysMLPackage.eINSTANCE.getMetadataAccessExpression();
    }

    /**
     * Gets the MetadataFeature {@link EClassifier}.
     *
     * @return the MetadataFeature {@link EClassifier}
     *
     */
    protected EClass metadataFeature__() {
        return SysMLPackage.eINSTANCE.getMetadataFeature();
    }

    /**
     * Gets the Metaclass {@link EClassifier}.
     *
     * @return the Metaclass {@link EClassifier}
     *
     */
    protected EClass metaclass__() {
        return SysMLPackage.eINSTANCE.getMetaclass();
    }

    /**
     * Gets the Structure {@link EClassifier}.
     *
     * @return the Structure {@link EClassifier}
     *
     */
    protected EClass structure__() {
        return SysMLPackage.eINSTANCE.getStructure();
    }

    /**
     * Gets the LiteralRational {@link EClassifier}.
     *
     * @return the LiteralRational {@link EClassifier}
     *
     */
    protected EClass literalRational__() {
        return SysMLPackage.eINSTANCE.getLiteralRational();
    }

    /**
     * Gets the LiteralInteger {@link EClassifier}.
     *
     * @return the LiteralInteger {@link EClassifier}
     *
     */
    protected EClass literalInteger__() {
        return SysMLPackage.eINSTANCE.getLiteralInteger();
    }

    /**
     * Gets the LiteralString {@link EClassifier}.
     *
     * @return the LiteralString {@link EClassifier}
     *
     */
    protected EClass literalString__() {
        return SysMLPackage.eINSTANCE.getLiteralString();
    }

    /**
     * Gets the FeatureChainExpression {@link EClassifier}.
     *
     * @return the FeatureChainExpression {@link EClassifier}
     *
     */
    protected EClass featureChainExpression__() {
        return SysMLPackage.eINSTANCE.getFeatureChainExpression();
    }

    /**
     * Gets the LiteralInfinity {@link EClassifier}.
     *
     * @return the LiteralInfinity {@link EClassifier}
     *
     */
    protected EClass literalInfinity__() {
        return SysMLPackage.eINSTANCE.getLiteralInfinity();
    }

    /**
     * Gets the BooleanExpression {@link EClassifier}.
     *
     * @return the BooleanExpression {@link EClassifier}
     *
     */
    protected EClass booleanExpression__() {
        return SysMLPackage.eINSTANCE.getBooleanExpression();
    }

    /**
     * Gets the Predicate {@link EClassifier}.
     *
     * @return the Predicate {@link EClassifier}
     *
     */
    protected EClass predicate__() {
        return SysMLPackage.eINSTANCE.getPredicate();
    }

    /**
     * Gets the ReturnParameterMembership {@link EClassifier}.
     *
     * @return the ReturnParameterMembership {@link EClassifier}
     *
     */
    protected EClass returnParameterMembership__() {
        return SysMLPackage.eINSTANCE.getReturnParameterMembership();
    }

    /**
     * Gets the ParameterMembership {@link EClassifier}.
     *
     * @return the ParameterMembership {@link EClassifier}
     *
     */
    protected EClass parameterMembership__() {
        return SysMLPackage.eINSTANCE.getParameterMembership();
    }

    /**
     * Gets the Invariant {@link EClassifier}.
     *
     * @return the Invariant {@link EClassifier}
     *
     */
    protected EClass invariant__() {
        return SysMLPackage.eINSTANCE.getInvariant();
    }

    /**
     * Gets the ResultExpressionMembership {@link EClassifier}.
     *
     * @return the ResultExpressionMembership {@link EClassifier}
     *
     */
    protected EClass resultExpressionMembership__() {
        return SysMLPackage.eINSTANCE.getResultExpressionMembership();
    }

    /**
     * Gets the MultiplicityRange {@link EClassifier}.
     *
     * @return the MultiplicityRange {@link EClassifier}
     *
     */
    protected EClass multiplicityRange__() {
        return SysMLPackage.eINSTANCE.getMultiplicityRange();
    }

    /**
     * Gets the FeatureValue {@link EClassifier}.
     *
     * @return the FeatureValue {@link EClassifier}
     *
     */
    protected EClass featureValue__() {
        return SysMLPackage.eINSTANCE.getFeatureValue();
    }

    /**
     * Gets the DataType {@link EClassifier}.
     *
     * @return the DataType {@link EClassifier}
     *
     */
    protected EClass dataType__() {
        return SysMLPackage.eINSTANCE.getDataType();
    }

    /**
     * Gets the BindingConnector {@link EClassifier}.
     *
     * @return the BindingConnector {@link EClassifier}
     *
     */
    protected EClass bindingConnector__() {
        return SysMLPackage.eINSTANCE.getBindingConnector();
    }

    /**
     * Gets the Connector {@link EClassifier}.
     *
     * @return the Connector {@link EClassifier}
     *
     */
    protected EClass connector__() {
        return SysMLPackage.eINSTANCE.getConnector();
    }

    /**
     * Gets the Association {@link EClassifier}.
     *
     * @return the Association {@link EClassifier}
     *
     */
    protected EClass association__() {
        return SysMLPackage.eINSTANCE.getAssociation();
    }

    /**
     * Gets the Succession {@link EClassifier}.
     *
     * @return the Succession {@link EClassifier}
     *
     */
    protected EClass succession__() {
        return SysMLPackage.eINSTANCE.getSuccession();
    }

    /**
     * Gets the AssociationStructure {@link EClassifier}.
     *
     * @return the AssociationStructure {@link EClassifier}
     *
     */
    protected EClass associationStructure__() {
        return SysMLPackage.eINSTANCE.getAssociationStructure();
    }

    /**
     * Gets the Package {@link EClassifier}.
     *
     * @return the Package {@link EClassifier}
     *
     */
    protected EClass package__() {
        return SysMLPackage.eINSTANCE.getPackage();
    }

    /**
     * Gets the LibraryPackage {@link EClassifier}.
     *
     * @return the LibraryPackage {@link EClassifier}
     *
     */
    protected EClass libraryPackage__() {
        return SysMLPackage.eINSTANCE.getLibraryPackage();
    }

    /**
     * Gets the ElementFilterMembership {@link EClassifier}.
     *
     * @return the ElementFilterMembership {@link EClassifier}
     *
     */
    protected EClass elementFilterMembership__() {
        return SysMLPackage.eINSTANCE.getElementFilterMembership();
    }

    /**
     * Gets the Flow {@link EClassifier}.
     *
     * @return the Flow {@link EClassifier}
     *
     */
    protected EClass flow__() {
        return SysMLPackage.eINSTANCE.getFlow();
    }

    /**
     * Gets the FlowEnd {@link EClassifier}.
     *
     * @return the FlowEnd {@link EClassifier}
     *
     */
    protected EClass flowEnd__() {
        return SysMLPackage.eINSTANCE.getFlowEnd();
    }

    /**
     * Gets the PayloadFeature {@link EClassifier}.
     *
     * @return the PayloadFeature {@link EClassifier}
     *
     */
    protected EClass payloadFeature__() {
        return SysMLPackage.eINSTANCE.getPayloadFeature();
    }

    /**
     * Gets the Interaction {@link EClassifier}.
     *
     * @return the Interaction {@link EClassifier}
     *
     */
    protected EClass interaction__() {
        return SysMLPackage.eINSTANCE.getInteraction();
    }

    /**
     * Gets the SuccessionFlow {@link EClassifier}.
     *
     * @return the SuccessionFlow {@link EClassifier}
     *
     */
    protected EClass successionFlow__() {
        return SysMLPackage.eINSTANCE.getSuccessionFlow();
    }

    /**
     * Gets the EndFeatureMembership {@link EClassifier}.
     *
     * @return the EndFeatureMembership {@link EClassifier}
     *
     */
    protected EClass endFeatureMembership__() {
        return SysMLPackage.eINSTANCE.getEndFeatureMembership();
    }

    /**
     * Gets the MembershipImport {@link EClassifier}.
     *
     * @return the MembershipImport {@link EClassifier}
     *
     */
    protected EClass membershipImport__() {
        return SysMLPackage.eINSTANCE.getMembershipImport();
    }

    /**
     * Gets the NamespaceImport {@link EClassifier}.
     *
     * @return the NamespaceImport {@link EClassifier}
     *
     */
    protected EClass namespaceImport__() {
        return SysMLPackage.eINSTANCE.getNamespaceImport();
    }

    /**
     * Gets the Dependency {@link EClassifier}.
     *
     * @return the Dependency {@link EClassifier}
     *
     */
    protected EClass dependency__() {
        return SysMLPackage.eINSTANCE.getDependency();
    }

    /**
     * Gets the VerificationCaseUsage {@link EClassifier}.
     *
     * @return the VerificationCaseUsage {@link EClassifier}
     *
     */
    protected EClass verificationCaseUsage__() {
        return SysMLPackage.eINSTANCE.getVerificationCaseUsage();
    }

    /**
     * Gets the CaseUsage {@link EClassifier}.
     *
     * @return the CaseUsage {@link EClassifier}
     *
     */
    protected EClass caseUsage__() {
        return SysMLPackage.eINSTANCE.getCaseUsage();
    }

    /**
     * Gets the CalculationUsage {@link EClassifier}.
     *
     * @return the CalculationUsage {@link EClassifier}
     *
     */
    protected EClass calculationUsage__() {
        return SysMLPackage.eINSTANCE.getCalculationUsage();
    }

    /**
     * Gets the ActionUsage {@link EClassifier}.
     *
     * @return the ActionUsage {@link EClassifier}
     *
     */
    protected EClass actionUsage__() {
        return SysMLPackage.eINSTANCE.getActionUsage();
    }

    /**
     * Gets the OccurrenceUsage {@link EClassifier}.
     *
     * @return the OccurrenceUsage {@link EClassifier}
     *
     */
    protected EClass occurrenceUsage__() {
        return SysMLPackage.eINSTANCE.getOccurrenceUsage();
    }

    /**
     * Gets the Usage {@link EClassifier}.
     *
     * @return the Usage {@link EClassifier}
     *
     */
    protected EClass usage__() {
        return SysMLPackage.eINSTANCE.getUsage();
    }

    /**
     * Gets the VariantMembership {@link EClassifier}.
     *
     * @return the VariantMembership {@link EClassifier}
     *
     */
    protected EClass variantMembership__() {
        return SysMLPackage.eINSTANCE.getVariantMembership();
    }

    /**
     * Gets the Definition {@link EClassifier}.
     *
     * @return the Definition {@link EClassifier}
     *
     */
    protected EClass definition__() {
        return SysMLPackage.eINSTANCE.getDefinition();
    }

    /**
     * Gets the ReferenceUsage {@link EClassifier}.
     *
     * @return the ReferenceUsage {@link EClassifier}
     *
     */
    protected EClass referenceUsage__() {
        return SysMLPackage.eINSTANCE.getReferenceUsage();
    }

    /**
     * Gets the AttributeUsage {@link EClassifier}.
     *
     * @return the AttributeUsage {@link EClassifier}
     *
     */
    protected EClass attributeUsage__() {
        return SysMLPackage.eINSTANCE.getAttributeUsage();
    }

    /**
     * Gets the EnumerationUsage {@link EClassifier}.
     *
     * @return the EnumerationUsage {@link EClassifier}
     *
     */
    protected EClass enumerationUsage__() {
        return SysMLPackage.eINSTANCE.getEnumerationUsage();
    }

    /**
     * Gets the EnumerationDefinition {@link EClassifier}.
     *
     * @return the EnumerationDefinition {@link EClassifier}
     *
     */
    protected EClass enumerationDefinition__() {
        return SysMLPackage.eINSTANCE.getEnumerationDefinition();
    }

    /**
     * Gets the AttributeDefinition {@link EClassifier}.
     *
     * @return the AttributeDefinition {@link EClassifier}
     *
     */
    protected EClass attributeDefinition__() {
        return SysMLPackage.eINSTANCE.getAttributeDefinition();
    }

    /**
     * Gets the ItemUsage {@link EClassifier}.
     *
     * @return the ItemUsage {@link EClassifier}
     *
     */
    protected EClass itemUsage__() {
        return SysMLPackage.eINSTANCE.getItemUsage();
    }

    /**
     * Gets the PartUsage {@link EClassifier}.
     *
     * @return the PartUsage {@link EClassifier}
     *
     */
    protected EClass partUsage__() {
        return SysMLPackage.eINSTANCE.getPartUsage();
    }

    /**
     * Gets the PartDefinition {@link EClassifier}.
     *
     * @return the PartDefinition {@link EClassifier}
     *
     */
    protected EClass partDefinition__() {
        return SysMLPackage.eINSTANCE.getPartDefinition();
    }

    /**
     * Gets the ItemDefinition {@link EClassifier}.
     *
     * @return the ItemDefinition {@link EClassifier}
     *
     */
    protected EClass itemDefinition__() {
        return SysMLPackage.eINSTANCE.getItemDefinition();
    }

    /**
     * Gets the OccurrenceDefinition {@link EClassifier}.
     *
     * @return the OccurrenceDefinition {@link EClassifier}
     *
     */
    protected EClass occurrenceDefinition__() {
        return SysMLPackage.eINSTANCE.getOccurrenceDefinition();
    }

    /**
     * Gets the PortUsage {@link EClassifier}.
     *
     * @return the PortUsage {@link EClassifier}
     *
     */
    protected EClass portUsage__() {
        return SysMLPackage.eINSTANCE.getPortUsage();
    }

    /**
     * Gets the PortDefinition {@link EClassifier}.
     *
     * @return the PortDefinition {@link EClassifier}
     *
     */
    protected EClass portDefinition__() {
        return SysMLPackage.eINSTANCE.getPortDefinition();
    }

    /**
     * Gets the ConjugatedPortDefinition {@link EClassifier}.
     *
     * @return the ConjugatedPortDefinition {@link EClassifier}
     *
     */
    protected EClass conjugatedPortDefinition__() {
        return SysMLPackage.eINSTANCE.getConjugatedPortDefinition();
    }

    /**
     * Gets the PortConjugation {@link EClassifier}.
     *
     * @return the PortConjugation {@link EClassifier}
     *
     */
    protected EClass portConjugation__() {
        return SysMLPackage.eINSTANCE.getPortConjugation();
    }

    /**
     * Gets the ConnectorAsUsage {@link EClassifier}.
     *
     * @return the ConnectorAsUsage {@link EClassifier}
     *
     */
    protected EClass connectorAsUsage__() {
        return SysMLPackage.eINSTANCE.getConnectorAsUsage();
    }

    /**
     * Gets the FlowUsage {@link EClassifier}.
     *
     * @return the FlowUsage {@link EClassifier}
     *
     */
    protected EClass flowUsage__() {
        return SysMLPackage.eINSTANCE.getFlowUsage();
    }

    /**
     * Gets the InterfaceUsage {@link EClassifier}.
     *
     * @return the InterfaceUsage {@link EClassifier}
     *
     */
    protected EClass interfaceUsage__() {
        return SysMLPackage.eINSTANCE.getInterfaceUsage();
    }

    /**
     * Gets the ConnectionUsage {@link EClassifier}.
     *
     * @return the ConnectionUsage {@link EClassifier}
     *
     */
    protected EClass connectionUsage__() {
        return SysMLPackage.eINSTANCE.getConnectionUsage();
    }

    /**
     * Gets the InterfaceDefinition {@link EClassifier}.
     *
     * @return the InterfaceDefinition {@link EClassifier}
     *
     */
    protected EClass interfaceDefinition__() {
        return SysMLPackage.eINSTANCE.getInterfaceDefinition();
    }

    /**
     * Gets the ConnectionDefinition {@link EClassifier}.
     *
     * @return the ConnectionDefinition {@link EClassifier}
     *
     */
    protected EClass connectionDefinition__() {
        return SysMLPackage.eINSTANCE.getConnectionDefinition();
    }

    /**
     * Gets the AllocationUsage {@link EClassifier}.
     *
     * @return the AllocationUsage {@link EClassifier}
     *
     */
    protected EClass allocationUsage__() {
        return SysMLPackage.eINSTANCE.getAllocationUsage();
    }

    /**
     * Gets the AllocationDefinition {@link EClassifier}.
     *
     * @return the AllocationDefinition {@link EClassifier}
     *
     */
    protected EClass allocationDefinition__() {
        return SysMLPackage.eINSTANCE.getAllocationDefinition();
    }

    /**
     * Gets the StateUsage {@link EClassifier}.
     *
     * @return the StateUsage {@link EClassifier}
     *
     */
    protected EClass stateUsage__() {
        return SysMLPackage.eINSTANCE.getStateUsage();
    }

    /**
     * Gets the TransitionUsage {@link EClassifier}.
     *
     * @return the TransitionUsage {@link EClassifier}
     *
     */
    protected EClass transitionUsage__() {
        return SysMLPackage.eINSTANCE.getTransitionUsage();
    }

    /**
     * Gets the AcceptActionUsage {@link EClassifier}.
     *
     * @return the AcceptActionUsage {@link EClassifier}
     *
     */
    protected EClass acceptActionUsage__() {
        return SysMLPackage.eINSTANCE.getAcceptActionUsage();
    }

    /**
     * Gets the ConstraintUsage {@link EClassifier}.
     *
     * @return the ConstraintUsage {@link EClassifier}
     *
     */
    protected EClass constraintUsage__() {
        return SysMLPackage.eINSTANCE.getConstraintUsage();
    }

    /**
     * Gets the RequirementUsage {@link EClassifier}.
     *
     * @return the RequirementUsage {@link EClassifier}
     *
     */
    protected EClass requirementUsage__() {
        return SysMLPackage.eINSTANCE.getRequirementUsage();
    }

    /**
     * Gets the RequirementDefinition {@link EClassifier}.
     *
     * @return the RequirementDefinition {@link EClassifier}
     *
     */
    protected EClass requirementDefinition__() {
        return SysMLPackage.eINSTANCE.getRequirementDefinition();
    }

    /**
     * Gets the ConstraintDefinition {@link EClassifier}.
     *
     * @return the ConstraintDefinition {@link EClassifier}
     *
     */
    protected EClass constraintDefinition__() {
        return SysMLPackage.eINSTANCE.getConstraintDefinition();
    }

    /**
     * Gets the ConcernUsage {@link EClassifier}.
     *
     * @return the ConcernUsage {@link EClassifier}
     *
     */
    protected EClass concernUsage__() {
        return SysMLPackage.eINSTANCE.getConcernUsage();
    }

    /**
     * Gets the ConcernDefinition {@link EClassifier}.
     *
     * @return the ConcernDefinition {@link EClassifier}
     *
     */
    protected EClass concernDefinition__() {
        return SysMLPackage.eINSTANCE.getConcernDefinition();
    }

    /**
     * Gets the AnalysisCaseUsage {@link EClassifier}.
     *
     * @return the AnalysisCaseUsage {@link EClassifier}
     *
     */
    protected EClass analysisCaseUsage__() {
        return SysMLPackage.eINSTANCE.getAnalysisCaseUsage();
    }

    /**
     * Gets the AnalysisCaseDefinition {@link EClassifier}.
     *
     * @return the AnalysisCaseDefinition {@link EClassifier}
     *
     */
    protected EClass analysisCaseDefinition__() {
        return SysMLPackage.eINSTANCE.getAnalysisCaseDefinition();
    }

    /**
     * Gets the CaseDefinition {@link EClassifier}.
     *
     * @return the CaseDefinition {@link EClassifier}
     *
     */
    protected EClass caseDefinition__() {
        return SysMLPackage.eINSTANCE.getCaseDefinition();
    }

    /**
     * Gets the CalculationDefinition {@link EClassifier}.
     *
     * @return the CalculationDefinition {@link EClassifier}
     *
     */
    protected EClass calculationDefinition__() {
        return SysMLPackage.eINSTANCE.getCalculationDefinition();
    }

    /**
     * Gets the ActionDefinition {@link EClassifier}.
     *
     * @return the ActionDefinition {@link EClassifier}
     *
     */
    protected EClass actionDefinition__() {
        return SysMLPackage.eINSTANCE.getActionDefinition();
    }

    /**
     * Gets the UseCaseUsage {@link EClassifier}.
     *
     * @return the UseCaseUsage {@link EClassifier}
     *
     */
    protected EClass useCaseUsage__() {
        return SysMLPackage.eINSTANCE.getUseCaseUsage();
    }

    /**
     * Gets the UseCaseDefinition {@link EClassifier}.
     *
     * @return the UseCaseDefinition {@link EClassifier}
     *
     */
    protected EClass useCaseDefinition__() {
        return SysMLPackage.eINSTANCE.getUseCaseDefinition();
    }

    /**
     * Gets the ViewUsage {@link EClassifier}.
     *
     * @return the ViewUsage {@link EClassifier}
     *
     */
    protected EClass viewUsage__() {
        return SysMLPackage.eINSTANCE.getViewUsage();
    }

    /**
     * Gets the ViewDefinition {@link EClassifier}.
     *
     * @return the ViewDefinition {@link EClassifier}
     *
     */
    protected EClass viewDefinition__() {
        return SysMLPackage.eINSTANCE.getViewDefinition();
    }

    /**
     * Gets the ViewpointUsage {@link EClassifier}.
     *
     * @return the ViewpointUsage {@link EClassifier}
     *
     */
    protected EClass viewpointUsage__() {
        return SysMLPackage.eINSTANCE.getViewpointUsage();
    }

    /**
     * Gets the ViewpointDefinition {@link EClassifier}.
     *
     * @return the ViewpointDefinition {@link EClassifier}
     *
     */
    protected EClass viewpointDefinition__() {
        return SysMLPackage.eINSTANCE.getViewpointDefinition();
    }

    /**
     * Gets the RenderingUsage {@link EClassifier}.
     *
     * @return the RenderingUsage {@link EClassifier}
     *
     */
    protected EClass renderingUsage__() {
        return SysMLPackage.eINSTANCE.getRenderingUsage();
    }

    /**
     * Gets the RenderingDefinition {@link EClassifier}.
     *
     * @return the RenderingDefinition {@link EClassifier}
     *
     */
    protected EClass renderingDefinition__() {
        return SysMLPackage.eINSTANCE.getRenderingDefinition();
    }

    /**
     * Gets the MetadataUsage {@link EClassifier}.
     *
     * @return the MetadataUsage {@link EClassifier}
     *
     */
    protected EClass metadataUsage__() {
        return SysMLPackage.eINSTANCE.getMetadataUsage();
    }

    /**
     * Gets the VerificationCaseDefinition {@link EClassifier}.
     *
     * @return the VerificationCaseDefinition {@link EClassifier}
     *
     */
    protected EClass verificationCaseDefinition__() {
        return SysMLPackage.eINSTANCE.getVerificationCaseDefinition();
    }

    /**
     * Gets the RequirementVerificationMembership {@link EClassifier}.
     *
     * @return the RequirementVerificationMembership {@link EClassifier}
     *
     */
    protected EClass requirementVerificationMembership__() {
        return SysMLPackage.eINSTANCE.getRequirementVerificationMembership();
    }

    /**
     * Gets the RequirementConstraintMembership {@link EClassifier}.
     *
     * @return the RequirementConstraintMembership {@link EClassifier}
     *
     */
    protected EClass requirementConstraintMembership__() {
        return SysMLPackage.eINSTANCE.getRequirementConstraintMembership();
    }

    /**
     * Gets the MetadataDefinition {@link EClassifier}.
     *
     * @return the MetadataDefinition {@link EClassifier}
     *
     */
    protected EClass metadataDefinition__() {
        return SysMLPackage.eINSTANCE.getMetadataDefinition();
    }

    /**
     * Gets the EventOccurrenceUsage {@link EClassifier}.
     *
     * @return the EventOccurrenceUsage {@link EClassifier}
     *
     */
    protected EClass eventOccurrenceUsage__() {
        return SysMLPackage.eINSTANCE.getEventOccurrenceUsage();
    }

    /**
     * Gets the AssignmentActionUsage {@link EClassifier}.
     *
     * @return the AssignmentActionUsage {@link EClassifier}
     *
     */
    protected EClass assignmentActionUsage__() {
        return SysMLPackage.eINSTANCE.getAssignmentActionUsage();
    }

    /**
     * Gets the TriggerInvocationExpression {@link EClassifier}.
     *
     * @return the TriggerInvocationExpression {@link EClassifier}
     *
     */
    protected EClass triggerInvocationExpression__() {
        return SysMLPackage.eINSTANCE.getTriggerInvocationExpression();
    }

    /**
     * Gets the SendActionUsage {@link EClassifier}.
     *
     * @return the SendActionUsage {@link EClassifier}
     *
     */
    protected EClass sendActionUsage__() {
        return SysMLPackage.eINSTANCE.getSendActionUsage();
    }

    /**
     * Gets the WhileLoopActionUsage {@link EClassifier}.
     *
     * @return the WhileLoopActionUsage {@link EClassifier}
     *
     */
    protected EClass whileLoopActionUsage__() {
        return SysMLPackage.eINSTANCE.getWhileLoopActionUsage();
    }

    /**
     * Gets the LoopActionUsage {@link EClassifier}.
     *
     * @return the LoopActionUsage {@link EClassifier}
     *
     */
    protected EClass loopActionUsage__() {
        return SysMLPackage.eINSTANCE.getLoopActionUsage();
    }

    /**
     * Gets the PerformActionUsage {@link EClassifier}.
     *
     * @return the PerformActionUsage {@link EClassifier}
     *
     */
    protected EClass performActionUsage__() {
        return SysMLPackage.eINSTANCE.getPerformActionUsage();
    }

    /**
     * Gets the ForLoopActionUsage {@link EClassifier}.
     *
     * @return the ForLoopActionUsage {@link EClassifier}
     *
     */
    protected EClass forLoopActionUsage__() {
        return SysMLPackage.eINSTANCE.getForLoopActionUsage();
    }

    /**
     * Gets the TerminateActionUsage {@link EClassifier}.
     *
     * @return the TerminateActionUsage {@link EClassifier}
     *
     */
    protected EClass terminateActionUsage__() {
        return SysMLPackage.eINSTANCE.getTerminateActionUsage();
    }

    /**
     * Gets the DecisionNode {@link EClassifier}.
     *
     * @return the DecisionNode {@link EClassifier}
     *
     */
    protected EClass decisionNode__() {
        return SysMLPackage.eINSTANCE.getDecisionNode();
    }

    /**
     * Gets the ControlNode {@link EClassifier}.
     *
     * @return the ControlNode {@link EClassifier}
     *
     */
    protected EClass controlNode__() {
        return SysMLPackage.eINSTANCE.getControlNode();
    }

    /**
     * Gets the IfActionUsage {@link EClassifier}.
     *
     * @return the IfActionUsage {@link EClassifier}
     *
     */
    protected EClass ifActionUsage__() {
        return SysMLPackage.eINSTANCE.getIfActionUsage();
    }

    /**
     * Gets the MergeNode {@link EClassifier}.
     *
     * @return the MergeNode {@link EClassifier}
     *
     */
    protected EClass mergeNode__() {
        return SysMLPackage.eINSTANCE.getMergeNode();
    }

    /**
     * Gets the JoinNode {@link EClassifier}.
     *
     * @return the JoinNode {@link EClassifier}
     *
     */
    protected EClass joinNode__() {
        return SysMLPackage.eINSTANCE.getJoinNode();
    }

    /**
     * Gets the ForkNode {@link EClassifier}.
     *
     * @return the ForkNode {@link EClassifier}
     *
     */
    protected EClass forkNode__() {
        return SysMLPackage.eINSTANCE.getForkNode();
    }

    /**
     * Gets the StateSubactionMembership {@link EClassifier}.
     *
     * @return the StateSubactionMembership {@link EClassifier}
     *
     */
    protected EClass stateSubactionMembership__() {
        return SysMLPackage.eINSTANCE.getStateSubactionMembership();
    }

    /**
     * Gets the TransitionFeatureMembership {@link EClassifier}.
     *
     * @return the TransitionFeatureMembership {@link EClassifier}
     *
     */
    protected EClass transitionFeatureMembership__() {
        return SysMLPackage.eINSTANCE.getTransitionFeatureMembership();
    }

    /**
     * Gets the StateDefinition {@link EClassifier}.
     *
     * @return the StateDefinition {@link EClassifier}
     *
     */
    protected EClass stateDefinition__() {
        return SysMLPackage.eINSTANCE.getStateDefinition();
    }

    /**
     * Gets the ExhibitStateUsage {@link EClassifier}.
     *
     * @return the ExhibitStateUsage {@link EClassifier}
     *
     */
    protected EClass exhibitStateUsage__() {
        return SysMLPackage.eINSTANCE.getExhibitStateUsage();
    }

    /**
     * Gets the ObjectiveMembership {@link EClassifier}.
     *
     * @return the ObjectiveMembership {@link EClassifier}
     *
     */
    protected EClass objectiveMembership__() {
        return SysMLPackage.eINSTANCE.getObjectiveMembership();
    }

    /**
     * Gets the ActorMembership {@link EClassifier}.
     *
     * @return the ActorMembership {@link EClassifier}
     *
     */
    protected EClass actorMembership__() {
        return SysMLPackage.eINSTANCE.getActorMembership();
    }

    /**
     * Gets the SubjectMembership {@link EClassifier}.
     *
     * @return the SubjectMembership {@link EClassifier}
     *
     */
    protected EClass subjectMembership__() {
        return SysMLPackage.eINSTANCE.getSubjectMembership();
    }

    /**
     * Gets the StakeholderMembership {@link EClassifier}.
     *
     * @return the StakeholderMembership {@link EClassifier}
     *
     */
    protected EClass stakeholderMembership__() {
        return SysMLPackage.eINSTANCE.getStakeholderMembership();
    }

    /**
     * Gets the FramedConcernMembership {@link EClassifier}.
     *
     * @return the FramedConcernMembership {@link EClassifier}
     *
     */
    protected EClass framedConcernMembership__() {
        return SysMLPackage.eINSTANCE.getFramedConcernMembership();
    }

    /**
     * Gets the SatisfyRequirementUsage {@link EClassifier}.
     *
     * @return the SatisfyRequirementUsage {@link EClassifier}
     *
     */
    protected EClass satisfyRequirementUsage__() {
        return SysMLPackage.eINSTANCE.getSatisfyRequirementUsage();
    }

    /**
     * Gets the AssertConstraintUsage {@link EClassifier}.
     *
     * @return the AssertConstraintUsage {@link EClassifier}
     *
     */
    protected EClass assertConstraintUsage__() {
        return SysMLPackage.eINSTANCE.getAssertConstraintUsage();
    }

    /**
     * Gets the MembershipExpose {@link EClassifier}.
     *
     * @return the MembershipExpose {@link EClassifier}
     *
     */
    protected EClass membershipExpose__() {
        return SysMLPackage.eINSTANCE.getMembershipExpose();
    }

    /**
     * Gets the Expose {@link EClassifier}.
     *
     * @return the Expose {@link EClassifier}
     *
     */
    protected EClass expose__() {
        return SysMLPackage.eINSTANCE.getExpose();
    }

    /**
     * Gets the NamespaceExpose {@link EClassifier}.
     *
     * @return the NamespaceExpose {@link EClassifier}
     *
     */
    protected EClass namespaceExpose__() {
        return SysMLPackage.eINSTANCE.getNamespaceExpose();
    }

    /**
     * Gets the ViewRenderingMembership {@link EClassifier}.
     *
     * @return the ViewRenderingMembership {@link EClassifier}
     *
     */
    protected EClass viewRenderingMembership__() {
        return SysMLPackage.eINSTANCE.getViewRenderingMembership();
    }

    /**
     * Gets the BindingConnectorAsUsage {@link EClassifier}.
     *
     * @return the BindingConnectorAsUsage {@link EClassifier}
     *
     */
    protected EClass bindingConnectorAsUsage__() {
        return SysMLPackage.eINSTANCE.getBindingConnectorAsUsage();
    }

    /**
     * Gets the SuccessionAsUsage {@link EClassifier}.
     *
     * @return the SuccessionAsUsage {@link EClassifier}
     *
     */
    protected EClass successionAsUsage__() {
        return SysMLPackage.eINSTANCE.getSuccessionAsUsage();
    }

    /**
     * Gets the ConjugatedPortTyping {@link EClassifier}.
     *
     * @return the ConjugatedPortTyping {@link EClassifier}
     *
     */
    protected EClass conjugatedPortTyping__() {
        return SysMLPackage.eINSTANCE.getConjugatedPortTyping();
    }

    /**
     * Gets the SuccessionFlowUsage {@link EClassifier}.
     *
     * @return the SuccessionFlowUsage {@link EClassifier}
     *
     */
    protected EClass successionFlowUsage__() {
        return SysMLPackage.eINSTANCE.getSuccessionFlowUsage();
    }

    /**
     * Gets the FlowDefinition {@link EClassifier}.
     *
     * @return the FlowDefinition {@link EClassifier}
     *
     */
    protected EClass flowDefinition__() {
        return SysMLPackage.eINSTANCE.getFlowDefinition();
    }

    /**
     * Gets the IncludeUseCaseUsage {@link EClassifier}.
     *
     * @return the IncludeUseCaseUsage {@link EClassifier}
     *
     */
    protected EClass includeUseCaseUsage__() {
        return SysMLPackage.eINSTANCE.getIncludeUseCaseUsage();
    }

    /**
     * Gets the VisibilityKind {@link EClassifier}.
     *
     * @return the VisibilityKind {@link EClassifier}
     *
     */
    protected EEnum visibilityKind__() {
        return SysMLPackage.eINSTANCE.getVisibilityKind();
    }

    /**
     * Gets the FeatureDirectionKind {@link EClassifier}.
     *
     * @return the FeatureDirectionKind {@link EClassifier}
     *
     */
    protected EEnum featureDirectionKind__() {
        return SysMLPackage.eINSTANCE.getFeatureDirectionKind();
    }

    /**
     * Gets the PortionKind {@link EClassifier}.
     *
     * @return the PortionKind {@link EClassifier}
     *
     */
    protected EEnum portionKind__() {
        return SysMLPackage.eINSTANCE.getPortionKind();
    }

    /**
     * Gets the RequirementConstraintKind {@link EClassifier}.
     *
     * @return the RequirementConstraintKind {@link EClassifier}
     *
     */
    protected EEnum requirementConstraintKind__() {
        return SysMLPackage.eINSTANCE.getRequirementConstraintKind();
    }

    /**
     * Gets the TriggerKind {@link EClassifier}.
     *
     * @return the TriggerKind {@link EClassifier}
     *
     */
    protected EEnum triggerKind__() {
        return SysMLPackage.eINSTANCE.getTriggerKind();
    }

    /**
     * Gets the StateSubactionKind {@link EClassifier}.
     *
     * @return the StateSubactionKind {@link EClassifier}
     *
     */
    protected EEnum stateSubactionKind__() {
        return SysMLPackage.eINSTANCE.getStateSubactionKind();
    }

    /**
     * Gets the TransitionFeatureKind {@link EClassifier}.
     *
     * @return the TransitionFeatureKind {@link EClassifier}
     *
     */
    protected EEnum transitionFeatureKind__() {
        return SysMLPackage.eINSTANCE.getTransitionFeatureKind();
    }

    /**
     * Gets the EAttribute.iD {@link EStructuralFeature}.
     *
     * @return the EAttribute.iD {@link EStructuralFeature}
     *
     */
    protected EAttribute eAttribute_ID() {
        return EcorePackage.eINSTANCE.getEAttribute_ID();
    }

    /**
     * Gets the EAttribute.eAttributeType {@link EStructuralFeature}.
     *
     * @return the EAttribute.eAttributeType {@link EStructuralFeature}
     *
     */
    protected EReference eAttribute_EAttributeType() {
        return EcorePackage.eINSTANCE.getEAttribute_EAttributeType();
    }

    /**
     * Gets the EAnnotation.source {@link EStructuralFeature}.
     *
     * @return the EAnnotation.source {@link EStructuralFeature}
     *
     */
    protected EAttribute eAnnotation_Source() {
        return EcorePackage.eINSTANCE.getEAnnotation_Source();
    }

    /**
     * Gets the EAnnotation.details {@link EStructuralFeature}.
     *
     * @return the EAnnotation.details {@link EStructuralFeature}
     *
     */
    protected EReference eAnnotation_Details() {
        return EcorePackage.eINSTANCE.getEAnnotation_Details();
    }

    /**
     * Gets the EAnnotation.eModelElement {@link EStructuralFeature}.
     *
     * @return the EAnnotation.eModelElement {@link EStructuralFeature}
     *
     */
    protected EReference eAnnotation_EModelElement() {
        return EcorePackage.eINSTANCE.getEAnnotation_EModelElement();
    }

    /**
     * Gets the EAnnotation.contents {@link EStructuralFeature}.
     *
     * @return the EAnnotation.contents {@link EStructuralFeature}
     *
     */
    protected EReference eAnnotation_Contents() {
        return EcorePackage.eINSTANCE.getEAnnotation_Contents();
    }

    /**
     * Gets the EAnnotation.references {@link EStructuralFeature}.
     *
     * @return the EAnnotation.references {@link EStructuralFeature}
     *
     */
    protected EReference eAnnotation_References() {
        return EcorePackage.eINSTANCE.getEAnnotation_References();
    }

    /**
     * Gets the EClass.abstract {@link EStructuralFeature}.
     *
     * @return the EClass.abstract {@link EStructuralFeature}
     *
     */
    protected EAttribute eClass_Abstract() {
        return EcorePackage.eINSTANCE.getEClass_Abstract();
    }

    /**
     * Gets the EClass.interface {@link EStructuralFeature}.
     *
     * @return the EClass.interface {@link EStructuralFeature}
     *
     */
    protected EAttribute eClass_Interface() {
        return EcorePackage.eINSTANCE.getEClass_Interface();
    }

    /**
     * Gets the EClass.eSuperTypes {@link EStructuralFeature}.
     *
     * @return the EClass.eSuperTypes {@link EStructuralFeature}
     *
     */
    protected EReference eClass_ESuperTypes() {
        return EcorePackage.eINSTANCE.getEClass_ESuperTypes();
    }

    /**
     * Gets the EClass.eOperations {@link EStructuralFeature}.
     *
     * @return the EClass.eOperations {@link EStructuralFeature}
     *
     */
    protected EReference eClass_EOperations() {
        return EcorePackage.eINSTANCE.getEClass_EOperations();
    }

    /**
     * Gets the EClass.eAllAttributes {@link EStructuralFeature}.
     *
     * @return the EClass.eAllAttributes {@link EStructuralFeature}
     *
     */
    protected EReference eClass_EAllAttributes() {
        return EcorePackage.eINSTANCE.getEClass_EAllAttributes();
    }

    /**
     * Gets the EClass.eAllReferences {@link EStructuralFeature}.
     *
     * @return the EClass.eAllReferences {@link EStructuralFeature}
     *
     */
    protected EReference eClass_EAllReferences() {
        return EcorePackage.eINSTANCE.getEClass_EAllReferences();
    }

    /**
     * Gets the EClass.eReferences {@link EStructuralFeature}.
     *
     * @return the EClass.eReferences {@link EStructuralFeature}
     *
     */
    protected EReference eClass_EReferences() {
        return EcorePackage.eINSTANCE.getEClass_EReferences();
    }

    /**
     * Gets the EClass.eAttributes {@link EStructuralFeature}.
     *
     * @return the EClass.eAttributes {@link EStructuralFeature}
     *
     */
    protected EReference eClass_EAttributes() {
        return EcorePackage.eINSTANCE.getEClass_EAttributes();
    }

    /**
     * Gets the EClass.eAllContainments {@link EStructuralFeature}.
     *
     * @return the EClass.eAllContainments {@link EStructuralFeature}
     *
     */
    protected EReference eClass_EAllContainments() {
        return EcorePackage.eINSTANCE.getEClass_EAllContainments();
    }

    /**
     * Gets the EClass.eAllOperations {@link EStructuralFeature}.
     *
     * @return the EClass.eAllOperations {@link EStructuralFeature}
     *
     */
    protected EReference eClass_EAllOperations() {
        return EcorePackage.eINSTANCE.getEClass_EAllOperations();
    }

    /**
     * Gets the EClass.eAllStructuralFeatures {@link EStructuralFeature}.
     *
     * @return the EClass.eAllStructuralFeatures {@link EStructuralFeature}
     *
     */
    protected EReference eClass_EAllStructuralFeatures() {
        return EcorePackage.eINSTANCE.getEClass_EAllStructuralFeatures();
    }

    /**
     * Gets the EClass.eAllSuperTypes {@link EStructuralFeature}.
     *
     * @return the EClass.eAllSuperTypes {@link EStructuralFeature}
     *
     */
    protected EReference eClass_EAllSuperTypes() {
        return EcorePackage.eINSTANCE.getEClass_EAllSuperTypes();
    }

    /**
     * Gets the EClass.eIDAttribute {@link EStructuralFeature}.
     *
     * @return the EClass.eIDAttribute {@link EStructuralFeature}
     *
     */
    protected EReference eClass_EIDAttribute() {
        return EcorePackage.eINSTANCE.getEClass_EIDAttribute();
    }

    /**
     * Gets the EClass.eStructuralFeatures {@link EStructuralFeature}.
     *
     * @return the EClass.eStructuralFeatures {@link EStructuralFeature}
     *
     */
    protected EReference eClass_EStructuralFeatures() {
        return EcorePackage.eINSTANCE.getEClass_EStructuralFeatures();
    }

    /**
     * Gets the EClass.eGenericSuperTypes {@link EStructuralFeature}.
     *
     * @return the EClass.eGenericSuperTypes {@link EStructuralFeature}
     *
     */
    protected EReference eClass_EGenericSuperTypes() {
        return EcorePackage.eINSTANCE.getEClass_EGenericSuperTypes();
    }

    /**
     * Gets the EClass.eAllGenericSuperTypes {@link EStructuralFeature}.
     *
     * @return the EClass.eAllGenericSuperTypes {@link EStructuralFeature}
     *
     */
    protected EReference eClass_EAllGenericSuperTypes() {
        return EcorePackage.eINSTANCE.getEClass_EAllGenericSuperTypes();
    }

    /**
     * Gets the EClassifier.instanceClassName {@link EStructuralFeature}.
     *
     * @return the EClassifier.instanceClassName {@link EStructuralFeature}
     *
     */
    protected EAttribute eClassifier_InstanceClassName() {
        return EcorePackage.eINSTANCE.getEClassifier_InstanceClassName();
    }

    /**
     * Gets the EClassifier.instanceClass {@link EStructuralFeature}.
     *
     * @return the EClassifier.instanceClass {@link EStructuralFeature}
     *
     */
    protected EAttribute eClassifier_InstanceClass() {
        return EcorePackage.eINSTANCE.getEClassifier_InstanceClass();
    }

    /**
     * Gets the EClassifier.defaultValue {@link EStructuralFeature}.
     *
     * @return the EClassifier.defaultValue {@link EStructuralFeature}
     *
     */
    protected EAttribute eClassifier_DefaultValue() {
        return EcorePackage.eINSTANCE.getEClassifier_DefaultValue();
    }

    /**
     * Gets the EClassifier.instanceTypeName {@link EStructuralFeature}.
     *
     * @return the EClassifier.instanceTypeName {@link EStructuralFeature}
     *
     */
    protected EAttribute eClassifier_InstanceTypeName() {
        return EcorePackage.eINSTANCE.getEClassifier_InstanceTypeName();
    }

    /**
     * Gets the EClassifier.ePackage {@link EStructuralFeature}.
     *
     * @return the EClassifier.ePackage {@link EStructuralFeature}
     *
     */
    protected EReference eClassifier_EPackage() {
        return EcorePackage.eINSTANCE.getEClassifier_EPackage();
    }

    /**
     * Gets the EClassifier.eTypeParameters {@link EStructuralFeature}.
     *
     * @return the EClassifier.eTypeParameters {@link EStructuralFeature}
     *
     */
    protected EReference eClassifier_ETypeParameters() {
        return EcorePackage.eINSTANCE.getEClassifier_ETypeParameters();
    }

    /**
     * Gets the EDataType.serializable {@link EStructuralFeature}.
     *
     * @return the EDataType.serializable {@link EStructuralFeature}
     *
     */
    protected EAttribute eDataType_Serializable() {
        return EcorePackage.eINSTANCE.getEDataType_Serializable();
    }

    /**
     * Gets the EEnum.eLiterals {@link EStructuralFeature}.
     *
     * @return the EEnum.eLiterals {@link EStructuralFeature}
     *
     */
    protected EReference eEnum_ELiterals() {
        return EcorePackage.eINSTANCE.getEEnum_ELiterals();
    }

    /**
     * Gets the EEnumLiteral.value {@link EStructuralFeature}.
     *
     * @return the EEnumLiteral.value {@link EStructuralFeature}
     *
     */
    protected EAttribute eEnumLiteral_Value() {
        return EcorePackage.eINSTANCE.getEEnumLiteral_Value();
    }

    /**
     * Gets the EEnumLiteral.instance {@link EStructuralFeature}.
     *
     * @return the EEnumLiteral.instance {@link EStructuralFeature}
     *
     */
    protected EAttribute eEnumLiteral_Instance() {
        return EcorePackage.eINSTANCE.getEEnumLiteral_Instance();
    }

    /**
     * Gets the EEnumLiteral.literal {@link EStructuralFeature}.
     *
     * @return the EEnumLiteral.literal {@link EStructuralFeature}
     *
     */
    protected EAttribute eEnumLiteral_Literal() {
        return EcorePackage.eINSTANCE.getEEnumLiteral_Literal();
    }

    /**
     * Gets the EEnumLiteral.eEnum {@link EStructuralFeature}.
     *
     * @return the EEnumLiteral.eEnum {@link EStructuralFeature}
     *
     */
    protected EReference eEnumLiteral_EEnum() {
        return EcorePackage.eINSTANCE.getEEnumLiteral_EEnum();
    }

    /**
     * Gets the EFactory.ePackage {@link EStructuralFeature}.
     *
     * @return the EFactory.ePackage {@link EStructuralFeature}
     *
     */
    protected EReference eFactory_EPackage() {
        return EcorePackage.eINSTANCE.getEFactory_EPackage();
    }

    /**
     * Gets the EModelElement.eAnnotations {@link EStructuralFeature}.
     *
     * @return the EModelElement.eAnnotations {@link EStructuralFeature}
     *
     */
    protected EReference eModelElement_EAnnotations() {
        return EcorePackage.eINSTANCE.getEModelElement_EAnnotations();
    }

    /**
     * Gets the ENamedElement.name {@link EStructuralFeature}.
     *
     * @return the ENamedElement.name {@link EStructuralFeature}
     *
     */
    protected EAttribute eNamedElement_Name() {
        return EcorePackage.eINSTANCE.getENamedElement_Name();
    }

    /**
     * Gets the EOperation.eContainingClass {@link EStructuralFeature}.
     *
     * @return the EOperation.eContainingClass {@link EStructuralFeature}
     *
     */
    protected EReference eOperation_EContainingClass() {
        return EcorePackage.eINSTANCE.getEOperation_EContainingClass();
    }

    /**
     * Gets the EOperation.eTypeParameters {@link EStructuralFeature}.
     *
     * @return the EOperation.eTypeParameters {@link EStructuralFeature}
     *
     */
    protected EReference eOperation_ETypeParameters() {
        return EcorePackage.eINSTANCE.getEOperation_ETypeParameters();
    }

    /**
     * Gets the EOperation.eParameters {@link EStructuralFeature}.
     *
     * @return the EOperation.eParameters {@link EStructuralFeature}
     *
     */
    protected EReference eOperation_EParameters() {
        return EcorePackage.eINSTANCE.getEOperation_EParameters();
    }

    /**
     * Gets the EOperation.eExceptions {@link EStructuralFeature}.
     *
     * @return the EOperation.eExceptions {@link EStructuralFeature}
     *
     */
    protected EReference eOperation_EExceptions() {
        return EcorePackage.eINSTANCE.getEOperation_EExceptions();
    }

    /**
     * Gets the EOperation.eGenericExceptions {@link EStructuralFeature}.
     *
     * @return the EOperation.eGenericExceptions {@link EStructuralFeature}
     *
     */
    protected EReference eOperation_EGenericExceptions() {
        return EcorePackage.eINSTANCE.getEOperation_EGenericExceptions();
    }

    /**
     * Gets the EPackage.nsURI {@link EStructuralFeature}.
     *
     * @return the EPackage.nsURI {@link EStructuralFeature}
     *
     */
    protected EAttribute ePackage_NsURI() {
        return EcorePackage.eINSTANCE.getEPackage_NsURI();
    }

    /**
     * Gets the EPackage.nsPrefix {@link EStructuralFeature}.
     *
     * @return the EPackage.nsPrefix {@link EStructuralFeature}
     *
     */
    protected EAttribute ePackage_NsPrefix() {
        return EcorePackage.eINSTANCE.getEPackage_NsPrefix();
    }

    /**
     * Gets the EPackage.eFactoryInstance {@link EStructuralFeature}.
     *
     * @return the EPackage.eFactoryInstance {@link EStructuralFeature}
     *
     */
    protected EReference ePackage_EFactoryInstance() {
        return EcorePackage.eINSTANCE.getEPackage_EFactoryInstance();
    }

    /**
     * Gets the EPackage.eClassifiers {@link EStructuralFeature}.
     *
     * @return the EPackage.eClassifiers {@link EStructuralFeature}
     *
     */
    protected EReference ePackage_EClassifiers() {
        return EcorePackage.eINSTANCE.getEPackage_EClassifiers();
    }

    /**
     * Gets the EPackage.eSubpackages {@link EStructuralFeature}.
     *
     * @return the EPackage.eSubpackages {@link EStructuralFeature}
     *
     */
    protected EReference ePackage_ESubpackages() {
        return EcorePackage.eINSTANCE.getEPackage_ESubpackages();
    }

    /**
     * Gets the EPackage.eSuperPackage {@link EStructuralFeature}.
     *
     * @return the EPackage.eSuperPackage {@link EStructuralFeature}
     *
     */
    protected EReference ePackage_ESuperPackage() {
        return EcorePackage.eINSTANCE.getEPackage_ESuperPackage();
    }

    /**
     * Gets the EParameter.eOperation {@link EStructuralFeature}.
     *
     * @return the EParameter.eOperation {@link EStructuralFeature}
     *
     */
    protected EReference eParameter_EOperation() {
        return EcorePackage.eINSTANCE.getEParameter_EOperation();
    }

    /**
     * Gets the EReference.containment {@link EStructuralFeature}.
     *
     * @return the EReference.containment {@link EStructuralFeature}
     *
     */
    protected EAttribute eReference_Containment() {
        return EcorePackage.eINSTANCE.getEReference_Containment();
    }

    /**
     * Gets the EReference.container {@link EStructuralFeature}.
     *
     * @return the EReference.container {@link EStructuralFeature}
     *
     */
    protected EAttribute eReference_Container() {
        return EcorePackage.eINSTANCE.getEReference_Container();
    }

    /**
     * Gets the EReference.resolveProxies {@link EStructuralFeature}.
     *
     * @return the EReference.resolveProxies {@link EStructuralFeature}
     *
     */
    protected EAttribute eReference_ResolveProxies() {
        return EcorePackage.eINSTANCE.getEReference_ResolveProxies();
    }

    /**
     * Gets the EReference.eOpposite {@link EStructuralFeature}.
     *
     * @return the EReference.eOpposite {@link EStructuralFeature}
     *
     */
    protected EReference eReference_EOpposite() {
        return EcorePackage.eINSTANCE.getEReference_EOpposite();
    }

    /**
     * Gets the EReference.eReferenceType {@link EStructuralFeature}.
     *
     * @return the EReference.eReferenceType {@link EStructuralFeature}
     *
     */
    protected EReference eReference_EReferenceType() {
        return EcorePackage.eINSTANCE.getEReference_EReferenceType();
    }

    /**
     * Gets the EReference.eKeys {@link EStructuralFeature}.
     *
     * @return the EReference.eKeys {@link EStructuralFeature}
     *
     */
    protected EReference eReference_EKeys() {
        return EcorePackage.eINSTANCE.getEReference_EKeys();
    }

    /**
     * Gets the EStructuralFeature.changeable {@link EStructuralFeature}.
     *
     * @return the EStructuralFeature.changeable {@link EStructuralFeature}
     *
     */
    protected EAttribute eStructuralFeature_Changeable() {
        return EcorePackage.eINSTANCE.getEStructuralFeature_Changeable();
    }

    /**
     * Gets the EStructuralFeature.volatile {@link EStructuralFeature}.
     *
     * @return the EStructuralFeature.volatile {@link EStructuralFeature}
     *
     */
    protected EAttribute eStructuralFeature_Volatile() {
        return EcorePackage.eINSTANCE.getEStructuralFeature_Volatile();
    }

    /**
     * Gets the EStructuralFeature.transient {@link EStructuralFeature}.
     *
     * @return the EStructuralFeature.transient {@link EStructuralFeature}
     *
     */
    protected EAttribute eStructuralFeature_Transient() {
        return EcorePackage.eINSTANCE.getEStructuralFeature_Transient();
    }

    /**
     * Gets the EStructuralFeature.defaultValueLiteral {@link EStructuralFeature}.
     *
     * @return the EStructuralFeature.defaultValueLiteral {@link EStructuralFeature}
     *
     */
    protected EAttribute eStructuralFeature_DefaultValueLiteral() {
        return EcorePackage.eINSTANCE.getEStructuralFeature_DefaultValueLiteral();
    }

    /**
     * Gets the EStructuralFeature.defaultValue {@link EStructuralFeature}.
     *
     * @return the EStructuralFeature.defaultValue {@link EStructuralFeature}
     *
     */
    protected EAttribute eStructuralFeature_DefaultValue() {
        return EcorePackage.eINSTANCE.getEStructuralFeature_DefaultValue();
    }

    /**
     * Gets the EStructuralFeature.unsettable {@link EStructuralFeature}.
     *
     * @return the EStructuralFeature.unsettable {@link EStructuralFeature}
     *
     */
    protected EAttribute eStructuralFeature_Unsettable() {
        return EcorePackage.eINSTANCE.getEStructuralFeature_Unsettable();
    }

    /**
     * Gets the EStructuralFeature.derived {@link EStructuralFeature}.
     *
     * @return the EStructuralFeature.derived {@link EStructuralFeature}
     *
     */
    protected EAttribute eStructuralFeature_Derived() {
        return EcorePackage.eINSTANCE.getEStructuralFeature_Derived();
    }

    /**
     * Gets the EStructuralFeature.eContainingClass {@link EStructuralFeature}.
     *
     * @return the EStructuralFeature.eContainingClass {@link EStructuralFeature}
     *
     */
    protected EReference eStructuralFeature_EContainingClass() {
        return EcorePackage.eINSTANCE.getEStructuralFeature_EContainingClass();
    }

    /**
     * Gets the ETypedElement.ordered {@link EStructuralFeature}.
     *
     * @return the ETypedElement.ordered {@link EStructuralFeature}
     *
     */
    protected EAttribute eTypedElement_Ordered() {
        return EcorePackage.eINSTANCE.getETypedElement_Ordered();
    }

    /**
     * Gets the ETypedElement.unique {@link EStructuralFeature}.
     *
     * @return the ETypedElement.unique {@link EStructuralFeature}
     *
     */
    protected EAttribute eTypedElement_Unique() {
        return EcorePackage.eINSTANCE.getETypedElement_Unique();
    }

    /**
     * Gets the ETypedElement.lowerBound {@link EStructuralFeature}.
     *
     * @return the ETypedElement.lowerBound {@link EStructuralFeature}
     *
     */
    protected EAttribute eTypedElement_LowerBound() {
        return EcorePackage.eINSTANCE.getETypedElement_LowerBound();
    }

    /**
     * Gets the ETypedElement.upperBound {@link EStructuralFeature}.
     *
     * @return the ETypedElement.upperBound {@link EStructuralFeature}
     *
     */
    protected EAttribute eTypedElement_UpperBound() {
        return EcorePackage.eINSTANCE.getETypedElement_UpperBound();
    }

    /**
     * Gets the ETypedElement.many {@link EStructuralFeature}.
     *
     * @return the ETypedElement.many {@link EStructuralFeature}
     *
     */
    protected EAttribute eTypedElement_Many() {
        return EcorePackage.eINSTANCE.getETypedElement_Many();
    }

    /**
     * Gets the ETypedElement.required {@link EStructuralFeature}.
     *
     * @return the ETypedElement.required {@link EStructuralFeature}
     *
     */
    protected EAttribute eTypedElement_Required() {
        return EcorePackage.eINSTANCE.getETypedElement_Required();
    }

    /**
     * Gets the ETypedElement.eType {@link EStructuralFeature}.
     *
     * @return the ETypedElement.eType {@link EStructuralFeature}
     *
     */
    protected EReference eTypedElement_EType() {
        return EcorePackage.eINSTANCE.getETypedElement_EType();
    }

    /**
     * Gets the ETypedElement.eGenericType {@link EStructuralFeature}.
     *
     * @return the ETypedElement.eGenericType {@link EStructuralFeature}
     *
     */
    protected EReference eTypedElement_EGenericType() {
        return EcorePackage.eINSTANCE.getETypedElement_EGenericType();
    }

    /**
     * Gets the EStringToStringMapEntry.key {@link EStructuralFeature}.
     *
     * @return the EStringToStringMapEntry.key {@link EStructuralFeature}
     *
     */
    protected EAttribute eStringToStringMapEntry_Key() {
        return EcorePackage.eINSTANCE.getEStringToStringMapEntry_Key();
    }

    /**
     * Gets the EStringToStringMapEntry.value {@link EStructuralFeature}.
     *
     * @return the EStringToStringMapEntry.value {@link EStructuralFeature}
     *
     */
    protected EAttribute eStringToStringMapEntry_Value() {
        return EcorePackage.eINSTANCE.getEStringToStringMapEntry_Value();
    }

    /**
     * Gets the EGenericType.eUpperBound {@link EStructuralFeature}.
     *
     * @return the EGenericType.eUpperBound {@link EStructuralFeature}
     *
     */
    protected EReference eGenericType_EUpperBound() {
        return EcorePackage.eINSTANCE.getEGenericType_EUpperBound();
    }

    /**
     * Gets the EGenericType.eTypeArguments {@link EStructuralFeature}.
     *
     * @return the EGenericType.eTypeArguments {@link EStructuralFeature}
     *
     */
    protected EReference eGenericType_ETypeArguments() {
        return EcorePackage.eINSTANCE.getEGenericType_ETypeArguments();
    }

    /**
     * Gets the EGenericType.eRawType {@link EStructuralFeature}.
     *
     * @return the EGenericType.eRawType {@link EStructuralFeature}
     *
     */
    protected EReference eGenericType_ERawType() {
        return EcorePackage.eINSTANCE.getEGenericType_ERawType();
    }

    /**
     * Gets the EGenericType.eLowerBound {@link EStructuralFeature}.
     *
     * @return the EGenericType.eLowerBound {@link EStructuralFeature}
     *
     */
    protected EReference eGenericType_ELowerBound() {
        return EcorePackage.eINSTANCE.getEGenericType_ELowerBound();
    }

    /**
     * Gets the EGenericType.eTypeParameter {@link EStructuralFeature}.
     *
     * @return the EGenericType.eTypeParameter {@link EStructuralFeature}
     *
     */
    protected EReference eGenericType_ETypeParameter() {
        return EcorePackage.eINSTANCE.getEGenericType_ETypeParameter();
    }

    /**
     * Gets the EGenericType.eClassifier {@link EStructuralFeature}.
     *
     * @return the EGenericType.eClassifier {@link EStructuralFeature}
     *
     */
    protected EReference eGenericType_EClassifier() {
        return EcorePackage.eINSTANCE.getEGenericType_EClassifier();
    }

    /**
     * Gets the ETypeParameter.eBounds {@link EStructuralFeature}.
     *
     * @return the ETypeParameter.eBounds {@link EStructuralFeature}
     *
     */
    protected EReference eTypeParameter_EBounds() {
        return EcorePackage.eINSTANCE.getETypeParameter_EBounds();
    }

    /**
     * Gets the OperatorExpression.operator {@link EStructuralFeature}.
     *
     * @return the OperatorExpression.operator {@link EStructuralFeature}
     *
     */
    protected EAttribute operatorExpression_Operator() {
        return SysMLPackage.eINSTANCE.getOperatorExpression_Operator();
    }

    /**
     * Gets the InstantiationExpression.argument {@link EStructuralFeature}.
     *
     * @return the InstantiationExpression.argument {@link EStructuralFeature}
     *
     */
    protected EReference instantiationExpression_Argument() {
        return SysMLPackage.eINSTANCE.getInstantiationExpression_Argument();
    }

    /**
     * Gets the InstantiationExpression.instantiatedType {@link EStructuralFeature}.
     *
     * @return the InstantiationExpression.instantiatedType {@link EStructuralFeature}
     *
     */
    protected EReference instantiationExpression_InstantiatedType() {
        return SysMLPackage.eINSTANCE.getInstantiationExpression_InstantiatedType();
    }

    /**
     * Gets the Expression.function {@link EStructuralFeature}.
     *
     * @return the Expression.function {@link EStructuralFeature}
     *
     */
    protected EReference expression_Function() {
        return SysMLPackage.eINSTANCE.getExpression_Function();
    }

    /**
     * Gets the Expression.result {@link EStructuralFeature}.
     *
     * @return the Expression.result {@link EStructuralFeature}
     *
     */
    protected EReference expression_Result() {
        return SysMLPackage.eINSTANCE.getExpression_Result();
    }

    /**
     * Gets the Expression.isModelLevelEvaluable {@link EStructuralFeature}.
     *
     * @return the Expression.isModelLevelEvaluable {@link EStructuralFeature}
     *
     */
    protected EAttribute expression_IsModelLevelEvaluable() {
        return SysMLPackage.eINSTANCE.getExpression_IsModelLevelEvaluable();
    }

    /**
     * Gets the Step.behavior {@link EStructuralFeature}.
     *
     * @return the Step.behavior {@link EStructuralFeature}
     *
     */
    protected EReference step_Behavior() {
        return SysMLPackage.eINSTANCE.getStep_Behavior();
    }

    /**
     * Gets the Step.parameter {@link EStructuralFeature}.
     *
     * @return the Step.parameter {@link EStructuralFeature}
     *
     */
    protected EReference step_Parameter() {
        return SysMLPackage.eINSTANCE.getStep_Parameter();
    }

    /**
     * Gets the Feature.owningFeatureMembership {@link EStructuralFeature}.
     *
     * @return the Feature.owningFeatureMembership {@link EStructuralFeature}
     *
     */
    protected EReference feature_OwningFeatureMembership() {
        return SysMLPackage.eINSTANCE.getFeature_OwningFeatureMembership();
    }

    /**
     * Gets the Feature.owningType {@link EStructuralFeature}.
     *
     * @return the Feature.owningType {@link EStructuralFeature}
     *
     */
    protected EReference feature_OwningType() {
        return SysMLPackage.eINSTANCE.getFeature_OwningType();
    }

    /**
     * Gets the Feature.endOwningType {@link EStructuralFeature}.
     *
     * @return the Feature.endOwningType {@link EStructuralFeature}
     *
     */
    protected EReference feature_EndOwningType() {
        return SysMLPackage.eINSTANCE.getFeature_EndOwningType();
    }

    /**
     * Gets the Feature.isUnique {@link EStructuralFeature}.
     *
     * @return the Feature.isUnique {@link EStructuralFeature}
     *
     */
    protected EAttribute feature_IsUnique() {
        return SysMLPackage.eINSTANCE.getFeature_IsUnique();
    }

    /**
     * Gets the Feature.isOrdered {@link EStructuralFeature}.
     *
     * @return the Feature.isOrdered {@link EStructuralFeature}
     *
     */
    protected EAttribute feature_IsOrdered() {
        return SysMLPackage.eINSTANCE.getFeature_IsOrdered();
    }

    /**
     * Gets the Feature.type {@link EStructuralFeature}.
     *
     * @return the Feature.type {@link EStructuralFeature}
     *
     */
    protected EReference feature_Type() {
        return SysMLPackage.eINSTANCE.getFeature_Type();
    }

    /**
     * Gets the Feature.ownedRedefinition {@link EStructuralFeature}.
     *
     * @return the Feature.ownedRedefinition {@link EStructuralFeature}
     *
     */
    protected EReference feature_OwnedRedefinition() {
        return SysMLPackage.eINSTANCE.getFeature_OwnedRedefinition();
    }

    /**
     * Gets the Feature.ownedSubsetting {@link EStructuralFeature}.
     *
     * @return the Feature.ownedSubsetting {@link EStructuralFeature}
     *
     */
    protected EReference feature_OwnedSubsetting() {
        return SysMLPackage.eINSTANCE.getFeature_OwnedSubsetting();
    }

    /**
     * Gets the Feature.isComposite {@link EStructuralFeature}.
     *
     * @return the Feature.isComposite {@link EStructuralFeature}
     *
     */
    protected EAttribute feature_IsComposite() {
        return SysMLPackage.eINSTANCE.getFeature_IsComposite();
    }

    /**
     * Gets the Feature.isEnd {@link EStructuralFeature}.
     *
     * @return the Feature.isEnd {@link EStructuralFeature}
     *
     */
    protected EAttribute feature_IsEnd() {
        return SysMLPackage.eINSTANCE.getFeature_IsEnd();
    }

    /**
     * Gets the Feature.ownedTyping {@link EStructuralFeature}.
     *
     * @return the Feature.ownedTyping {@link EStructuralFeature}
     *
     */
    protected EReference feature_OwnedTyping() {
        return SysMLPackage.eINSTANCE.getFeature_OwnedTyping();
    }

    /**
     * Gets the Feature.featuringType {@link EStructuralFeature}.
     *
     * @return the Feature.featuringType {@link EStructuralFeature}
     *
     */
    protected EReference feature_FeaturingType() {
        return SysMLPackage.eINSTANCE.getFeature_FeaturingType();
    }

    /**
     * Gets the Feature.ownedTypeFeaturing {@link EStructuralFeature}.
     *
     * @return the Feature.ownedTypeFeaturing {@link EStructuralFeature}
     *
     */
    protected EReference feature_OwnedTypeFeaturing() {
        return SysMLPackage.eINSTANCE.getFeature_OwnedTypeFeaturing();
    }

    /**
     * Gets the Feature.isDerived {@link EStructuralFeature}.
     *
     * @return the Feature.isDerived {@link EStructuralFeature}
     *
     */
    protected EAttribute feature_IsDerived() {
        return SysMLPackage.eINSTANCE.getFeature_IsDerived();
    }

    /**
     * Gets the Feature.chainingFeature {@link EStructuralFeature}.
     *
     * @return the Feature.chainingFeature {@link EStructuralFeature}
     *
     */
    protected EReference feature_ChainingFeature() {
        return SysMLPackage.eINSTANCE.getFeature_ChainingFeature();
    }

    /**
     * Gets the Feature.ownedFeatureInverting {@link EStructuralFeature}.
     *
     * @return the Feature.ownedFeatureInverting {@link EStructuralFeature}
     *
     */
    protected EReference feature_OwnedFeatureInverting() {
        return SysMLPackage.eINSTANCE.getFeature_OwnedFeatureInverting();
    }

    /**
     * Gets the Feature.ownedFeatureChaining {@link EStructuralFeature}.
     *
     * @return the Feature.ownedFeatureChaining {@link EStructuralFeature}
     *
     */
    protected EReference feature_OwnedFeatureChaining() {
        return SysMLPackage.eINSTANCE.getFeature_OwnedFeatureChaining();
    }

    /**
     * Gets the Feature.isPortion {@link EStructuralFeature}.
     *
     * @return the Feature.isPortion {@link EStructuralFeature}
     *
     */
    protected EAttribute feature_IsPortion() {
        return SysMLPackage.eINSTANCE.getFeature_IsPortion();
    }

    /**
     * Gets the Feature.isVariable {@link EStructuralFeature}.
     *
     * @return the Feature.isVariable {@link EStructuralFeature}
     *
     */
    protected EAttribute feature_IsVariable() {
        return SysMLPackage.eINSTANCE.getFeature_IsVariable();
    }

    /**
     * Gets the Feature.isConstant {@link EStructuralFeature}.
     *
     * @return the Feature.isConstant {@link EStructuralFeature}
     *
     */
    protected EAttribute feature_IsConstant() {
        return SysMLPackage.eINSTANCE.getFeature_IsConstant();
    }

    /**
     * Gets the Feature.ownedReferenceSubsetting {@link EStructuralFeature}.
     *
     * @return the Feature.ownedReferenceSubsetting {@link EStructuralFeature}
     *
     */
    protected EReference feature_OwnedReferenceSubsetting() {
        return SysMLPackage.eINSTANCE.getFeature_OwnedReferenceSubsetting();
    }

    /**
     * Gets the Feature.featureTarget {@link EStructuralFeature}.
     *
     * @return the Feature.featureTarget {@link EStructuralFeature}
     *
     */
    protected EReference feature_FeatureTarget() {
        return SysMLPackage.eINSTANCE.getFeature_FeatureTarget();
    }

    /**
     * Gets the Feature.crossFeature {@link EStructuralFeature}.
     *
     * @return the Feature.crossFeature {@link EStructuralFeature}
     *
     */
    protected EReference feature_CrossFeature() {
        return SysMLPackage.eINSTANCE.getFeature_CrossFeature();
    }

    /**
     * Gets the Feature.direction {@link EStructuralFeature}.
     *
     * @return the Feature.direction {@link EStructuralFeature}
     *
     */
    protected EAttribute feature_Direction() {
        return SysMLPackage.eINSTANCE.getFeature_Direction();
    }

    /**
     * Gets the Feature.ownedCrossSubsetting {@link EStructuralFeature}.
     *
     * @return the Feature.ownedCrossSubsetting {@link EStructuralFeature}
     *
     */
    protected EReference feature_OwnedCrossSubsetting() {
        return SysMLPackage.eINSTANCE.getFeature_OwnedCrossSubsetting();
    }

    /**
     * Gets the Type.ownedSpecialization {@link EStructuralFeature}.
     *
     * @return the Type.ownedSpecialization {@link EStructuralFeature}
     *
     */
    protected EReference type_OwnedSpecialization() {
        return SysMLPackage.eINSTANCE.getType_OwnedSpecialization();
    }

    /**
     * Gets the Type.ownedFeatureMembership {@link EStructuralFeature}.
     *
     * @return the Type.ownedFeatureMembership {@link EStructuralFeature}
     *
     */
    protected EReference type_OwnedFeatureMembership() {
        return SysMLPackage.eINSTANCE.getType_OwnedFeatureMembership();
    }

    /**
     * Gets the Type.feature {@link EStructuralFeature}.
     *
     * @return the Type.feature {@link EStructuralFeature}
     *
     */
    protected EReference type_Feature() {
        return SysMLPackage.eINSTANCE.getType_Feature();
    }

    /**
     * Gets the Type.ownedFeature {@link EStructuralFeature}.
     *
     * @return the Type.ownedFeature {@link EStructuralFeature}
     *
     */
    protected EReference type_OwnedFeature() {
        return SysMLPackage.eINSTANCE.getType_OwnedFeature();
    }

    /**
     * Gets the Type.input {@link EStructuralFeature}.
     *
     * @return the Type.input {@link EStructuralFeature}
     *
     */
    protected EReference type_Input() {
        return SysMLPackage.eINSTANCE.getType_Input();
    }

    /**
     * Gets the Type.output {@link EStructuralFeature}.
     *
     * @return the Type.output {@link EStructuralFeature}
     *
     */
    protected EReference type_Output() {
        return SysMLPackage.eINSTANCE.getType_Output();
    }

    /**
     * Gets the Type.isAbstract {@link EStructuralFeature}.
     *
     * @return the Type.isAbstract {@link EStructuralFeature}
     *
     */
    protected EAttribute type_IsAbstract() {
        return SysMLPackage.eINSTANCE.getType_IsAbstract();
    }

    /**
     * Gets the Type.inheritedMembership {@link EStructuralFeature}.
     *
     * @return the Type.inheritedMembership {@link EStructuralFeature}
     *
     */
    protected EReference type_InheritedMembership() {
        return SysMLPackage.eINSTANCE.getType_InheritedMembership();
    }

    /**
     * Gets the Type.endFeature {@link EStructuralFeature}.
     *
     * @return the Type.endFeature {@link EStructuralFeature}
     *
     */
    protected EReference type_EndFeature() {
        return SysMLPackage.eINSTANCE.getType_EndFeature();
    }

    /**
     * Gets the Type.ownedEndFeature {@link EStructuralFeature}.
     *
     * @return the Type.ownedEndFeature {@link EStructuralFeature}
     *
     */
    protected EReference type_OwnedEndFeature() {
        return SysMLPackage.eINSTANCE.getType_OwnedEndFeature();
    }

    /**
     * Gets the Type.isSufficient {@link EStructuralFeature}.
     *
     * @return the Type.isSufficient {@link EStructuralFeature}
     *
     */
    protected EAttribute type_IsSufficient() {
        return SysMLPackage.eINSTANCE.getType_IsSufficient();
    }

    /**
     * Gets the Type.ownedConjugator {@link EStructuralFeature}.
     *
     * @return the Type.ownedConjugator {@link EStructuralFeature}
     *
     */
    protected EReference type_OwnedConjugator() {
        return SysMLPackage.eINSTANCE.getType_OwnedConjugator();
    }

    /**
     * Gets the Type.isConjugated {@link EStructuralFeature}.
     *
     * @return the Type.isConjugated {@link EStructuralFeature}
     *
     */
    protected EAttribute type_IsConjugated() {
        return SysMLPackage.eINSTANCE.getType_IsConjugated();
    }

    /**
     * Gets the Type.inheritedFeature {@link EStructuralFeature}.
     *
     * @return the Type.inheritedFeature {@link EStructuralFeature}
     *
     */
    protected EReference type_InheritedFeature() {
        return SysMLPackage.eINSTANCE.getType_InheritedFeature();
    }

    /**
     * Gets the Type.multiplicity {@link EStructuralFeature}.
     *
     * @return the Type.multiplicity {@link EStructuralFeature}
     *
     */
    protected EReference type_Multiplicity() {
        return SysMLPackage.eINSTANCE.getType_Multiplicity();
    }

    /**
     * Gets the Type.unioningType {@link EStructuralFeature}.
     *
     * @return the Type.unioningType {@link EStructuralFeature}
     *
     */
    protected EReference type_UnioningType() {
        return SysMLPackage.eINSTANCE.getType_UnioningType();
    }

    /**
     * Gets the Type.ownedIntersecting {@link EStructuralFeature}.
     *
     * @return the Type.ownedIntersecting {@link EStructuralFeature}
     *
     */
    protected EReference type_OwnedIntersecting() {
        return SysMLPackage.eINSTANCE.getType_OwnedIntersecting();
    }

    /**
     * Gets the Type.intersectingType {@link EStructuralFeature}.
     *
     * @return the Type.intersectingType {@link EStructuralFeature}
     *
     */
    protected EReference type_IntersectingType() {
        return SysMLPackage.eINSTANCE.getType_IntersectingType();
    }

    /**
     * Gets the Type.ownedUnioning {@link EStructuralFeature}.
     *
     * @return the Type.ownedUnioning {@link EStructuralFeature}
     *
     */
    protected EReference type_OwnedUnioning() {
        return SysMLPackage.eINSTANCE.getType_OwnedUnioning();
    }

    /**
     * Gets the Type.ownedDisjoining {@link EStructuralFeature}.
     *
     * @return the Type.ownedDisjoining {@link EStructuralFeature}
     *
     */
    protected EReference type_OwnedDisjoining() {
        return SysMLPackage.eINSTANCE.getType_OwnedDisjoining();
    }

    /**
     * Gets the Type.featureMembership {@link EStructuralFeature}.
     *
     * @return the Type.featureMembership {@link EStructuralFeature}
     *
     */
    protected EReference type_FeatureMembership() {
        return SysMLPackage.eINSTANCE.getType_FeatureMembership();
    }

    /**
     * Gets the Type.differencingType {@link EStructuralFeature}.
     *
     * @return the Type.differencingType {@link EStructuralFeature}
     *
     */
    protected EReference type_DifferencingType() {
        return SysMLPackage.eINSTANCE.getType_DifferencingType();
    }

    /**
     * Gets the Type.ownedDifferencing {@link EStructuralFeature}.
     *
     * @return the Type.ownedDifferencing {@link EStructuralFeature}
     *
     */
    protected EReference type_OwnedDifferencing() {
        return SysMLPackage.eINSTANCE.getType_OwnedDifferencing();
    }

    /**
     * Gets the Type.directedFeature {@link EStructuralFeature}.
     *
     * @return the Type.directedFeature {@link EStructuralFeature}
     *
     */
    protected EReference type_DirectedFeature() {
        return SysMLPackage.eINSTANCE.getType_DirectedFeature();
    }

    /**
     * Gets the Namespace.ownedMembership {@link EStructuralFeature}.
     *
     * @return the Namespace.ownedMembership {@link EStructuralFeature}
     *
     */
    protected EReference namespace_OwnedMembership() {
        return SysMLPackage.eINSTANCE.getNamespace_OwnedMembership();
    }

    /**
     * Gets the Namespace.ownedMember {@link EStructuralFeature}.
     *
     * @return the Namespace.ownedMember {@link EStructuralFeature}
     *
     */
    protected EReference namespace_OwnedMember() {
        return SysMLPackage.eINSTANCE.getNamespace_OwnedMember();
    }

    /**
     * Gets the Namespace.membership {@link EStructuralFeature}.
     *
     * @return the Namespace.membership {@link EStructuralFeature}
     *
     */
    protected EReference namespace_Membership() {
        return SysMLPackage.eINSTANCE.getNamespace_Membership();
    }

    /**
     * Gets the Namespace.ownedImport {@link EStructuralFeature}.
     *
     * @return the Namespace.ownedImport {@link EStructuralFeature}
     *
     */
    protected EReference namespace_OwnedImport() {
        return SysMLPackage.eINSTANCE.getNamespace_OwnedImport();
    }

    /**
     * Gets the Namespace.member {@link EStructuralFeature}.
     *
     * @return the Namespace.member {@link EStructuralFeature}
     *
     */
    protected EReference namespace_Member() {
        return SysMLPackage.eINSTANCE.getNamespace_Member();
    }

    /**
     * Gets the Namespace.importedMembership {@link EStructuralFeature}.
     *
     * @return the Namespace.importedMembership {@link EStructuralFeature}
     *
     */
    protected EReference namespace_ImportedMembership() {
        return SysMLPackage.eINSTANCE.getNamespace_ImportedMembership();
    }

    /**
     * Gets the Element.owningMembership {@link EStructuralFeature}.
     *
     * @return the Element.owningMembership {@link EStructuralFeature}
     *
     */
    protected EReference element_OwningMembership() {
        return SysMLPackage.eINSTANCE.getElement_OwningMembership();
    }

    /**
     * Gets the Element.ownedRelationship {@link EStructuralFeature}.
     *
     * @return the Element.ownedRelationship {@link EStructuralFeature}
     *
     */
    protected EReference element_OwnedRelationship() {
        return SysMLPackage.eINSTANCE.getElement_OwnedRelationship();
    }

    /**
     * Gets the Element.owningRelationship {@link EStructuralFeature}.
     *
     * @return the Element.owningRelationship {@link EStructuralFeature}
     *
     */
    protected EReference element_OwningRelationship() {
        return SysMLPackage.eINSTANCE.getElement_OwningRelationship();
    }

    /**
     * Gets the Element.owningNamespace {@link EStructuralFeature}.
     *
     * @return the Element.owningNamespace {@link EStructuralFeature}
     *
     */
    protected EReference element_OwningNamespace() {
        return SysMLPackage.eINSTANCE.getElement_OwningNamespace();
    }

    /**
     * Gets the Element.elementId {@link EStructuralFeature}.
     *
     * @return the Element.elementId {@link EStructuralFeature}
     *
     */
    protected EAttribute element_ElementId() {
        return SysMLPackage.eINSTANCE.getElement_ElementId();
    }

    /**
     * Gets the Element.owner {@link EStructuralFeature}.
     *
     * @return the Element.owner {@link EStructuralFeature}
     *
     */
    protected EReference element_Owner() {
        return SysMLPackage.eINSTANCE.getElement_Owner();
    }

    /**
     * Gets the Element.ownedElement {@link EStructuralFeature}.
     *
     * @return the Element.ownedElement {@link EStructuralFeature}
     *
     */
    protected EReference element_OwnedElement() {
        return SysMLPackage.eINSTANCE.getElement_OwnedElement();
    }

    /**
     * Gets the Element.documentation {@link EStructuralFeature}.
     *
     * @return the Element.documentation {@link EStructuralFeature}
     *
     */
    protected EReference element_Documentation() {
        return SysMLPackage.eINSTANCE.getElement_Documentation();
    }

    /**
     * Gets the Element.ownedAnnotation {@link EStructuralFeature}.
     *
     * @return the Element.ownedAnnotation {@link EStructuralFeature}
     *
     */
    protected EReference element_OwnedAnnotation() {
        return SysMLPackage.eINSTANCE.getElement_OwnedAnnotation();
    }

    /**
     * Gets the Element.textualRepresentation {@link EStructuralFeature}.
     *
     * @return the Element.textualRepresentation {@link EStructuralFeature}
     *
     */
    protected EReference element_TextualRepresentation() {
        return SysMLPackage.eINSTANCE.getElement_TextualRepresentation();
    }

    /**
     * Gets the Element.aliasIds {@link EStructuralFeature}.
     *
     * @return the Element.aliasIds {@link EStructuralFeature}
     *
     */
    protected EAttribute element_AliasIds() {
        return SysMLPackage.eINSTANCE.getElement_AliasIds();
    }

    /**
     * Gets the Element.declaredShortName {@link EStructuralFeature}.
     *
     * @return the Element.declaredShortName {@link EStructuralFeature}
     *
     */
    protected EAttribute element_DeclaredShortName() {
        return SysMLPackage.eINSTANCE.getElement_DeclaredShortName();
    }

    /**
     * Gets the Element.declaredName {@link EStructuralFeature}.
     *
     * @return the Element.declaredName {@link EStructuralFeature}
     *
     */
    protected EAttribute element_DeclaredName() {
        return SysMLPackage.eINSTANCE.getElement_DeclaredName();
    }

    /**
     * Gets the Element.shortName {@link EStructuralFeature}.
     *
     * @return the Element.shortName {@link EStructuralFeature}
     *
     */
    protected EAttribute element_ShortName() {
        return SysMLPackage.eINSTANCE.getElement_ShortName();
    }

    /**
     * Gets the Element.name {@link EStructuralFeature}.
     *
     * @return the Element.name {@link EStructuralFeature}
     *
     */
    protected EAttribute element_Name() {
        return SysMLPackage.eINSTANCE.getElement_Name();
    }

    /**
     * Gets the Element.qualifiedName {@link EStructuralFeature}.
     *
     * @return the Element.qualifiedName {@link EStructuralFeature}
     *
     */
    protected EAttribute element_QualifiedName() {
        return SysMLPackage.eINSTANCE.getElement_QualifiedName();
    }

    /**
     * Gets the Element.isImpliedIncluded {@link EStructuralFeature}.
     *
     * @return the Element.isImpliedIncluded {@link EStructuralFeature}
     *
     */
    protected EAttribute element_IsImpliedIncluded() {
        return SysMLPackage.eINSTANCE.getElement_IsImpliedIncluded();
    }

    /**
     * Gets the Element.isLibraryElement {@link EStructuralFeature}.
     *
     * @return the Element.isLibraryElement {@link EStructuralFeature}
     *
     */
    protected EAttribute element_IsLibraryElement() {
        return SysMLPackage.eINSTANCE.getElement_IsLibraryElement();
    }

    /**
     * Gets the OwningMembership.ownedMemberElementId {@link EStructuralFeature}.
     *
     * @return the OwningMembership.ownedMemberElementId {@link EStructuralFeature}
     *
     */
    protected EAttribute owningMembership_OwnedMemberElementId() {
        return SysMLPackage.eINSTANCE.getOwningMembership_OwnedMemberElementId();
    }

    /**
     * Gets the OwningMembership.ownedMemberShortName {@link EStructuralFeature}.
     *
     * @return the OwningMembership.ownedMemberShortName {@link EStructuralFeature}
     *
     */
    protected EAttribute owningMembership_OwnedMemberShortName() {
        return SysMLPackage.eINSTANCE.getOwningMembership_OwnedMemberShortName();
    }

    /**
     * Gets the OwningMembership.ownedMemberName {@link EStructuralFeature}.
     *
     * @return the OwningMembership.ownedMemberName {@link EStructuralFeature}
     *
     */
    protected EAttribute owningMembership_OwnedMemberName() {
        return SysMLPackage.eINSTANCE.getOwningMembership_OwnedMemberName();
    }

    /**
     * Gets the OwningMembership.ownedMemberElement {@link EStructuralFeature}.
     *
     * @return the OwningMembership.ownedMemberElement {@link EStructuralFeature}
     *
     */
    protected EReference owningMembership_OwnedMemberElement() {
        return SysMLPackage.eINSTANCE.getOwningMembership_OwnedMemberElement();
    }

    /**
     * Gets the Membership.memberElementId {@link EStructuralFeature}.
     *
     * @return the Membership.memberElementId {@link EStructuralFeature}
     *
     */
    protected EAttribute membership_MemberElementId() {
        return SysMLPackage.eINSTANCE.getMembership_MemberElementId();
    }

    /**
     * Gets the Membership.membershipOwningNamespace {@link EStructuralFeature}.
     *
     * @return the Membership.membershipOwningNamespace {@link EStructuralFeature}
     *
     */
    protected EReference membership_MembershipOwningNamespace() {
        return SysMLPackage.eINSTANCE.getMembership_MembershipOwningNamespace();
    }

    /**
     * Gets the Membership.memberShortName {@link EStructuralFeature}.
     *
     * @return the Membership.memberShortName {@link EStructuralFeature}
     *
     */
    protected EAttribute membership_MemberShortName() {
        return SysMLPackage.eINSTANCE.getMembership_MemberShortName();
    }

    /**
     * Gets the Membership.memberElement {@link EStructuralFeature}.
     *
     * @return the Membership.memberElement {@link EStructuralFeature}
     *
     */
    protected EReference membership_MemberElement() {
        return SysMLPackage.eINSTANCE.getMembership_MemberElement();
    }

    /**
     * Gets the Membership.memberName {@link EStructuralFeature}.
     *
     * @return the Membership.memberName {@link EStructuralFeature}
     *
     */
    protected EAttribute membership_MemberName() {
        return SysMLPackage.eINSTANCE.getMembership_MemberName();
    }

    /**
     * Gets the Membership.visibility {@link EStructuralFeature}.
     *
     * @return the Membership.visibility {@link EStructuralFeature}
     *
     */
    protected EAttribute membership_Visibility() {
        return SysMLPackage.eINSTANCE.getMembership_Visibility();
    }

    /**
     * Gets the Relationship.relatedElement {@link EStructuralFeature}.
     *
     * @return the Relationship.relatedElement {@link EStructuralFeature}
     *
     */
    protected EReference relationship_RelatedElement() {
        return SysMLPackage.eINSTANCE.getRelationship_RelatedElement();
    }

    /**
     * Gets the Relationship.target {@link EStructuralFeature}.
     *
     * @return the Relationship.target {@link EStructuralFeature}
     *
     */
    protected EReference relationship_Target() {
        return SysMLPackage.eINSTANCE.getRelationship_Target();
    }

    /**
     * Gets the Relationship.source {@link EStructuralFeature}.
     *
     * @return the Relationship.source {@link EStructuralFeature}
     *
     */
    protected EReference relationship_Source() {
        return SysMLPackage.eINSTANCE.getRelationship_Source();
    }

    /**
     * Gets the Relationship.owningRelatedElement {@link EStructuralFeature}.
     *
     * @return the Relationship.owningRelatedElement {@link EStructuralFeature}
     *
     */
    protected EReference relationship_OwningRelatedElement() {
        return SysMLPackage.eINSTANCE.getRelationship_OwningRelatedElement();
    }

    /**
     * Gets the Relationship.ownedRelatedElement {@link EStructuralFeature}.
     *
     * @return the Relationship.ownedRelatedElement {@link EStructuralFeature}
     *
     */
    protected EReference relationship_OwnedRelatedElement() {
        return SysMLPackage.eINSTANCE.getRelationship_OwnedRelatedElement();
    }

    /**
     * Gets the Relationship.isImplied {@link EStructuralFeature}.
     *
     * @return the Relationship.isImplied {@link EStructuralFeature}
     *
     */
    protected EAttribute relationship_IsImplied() {
        return SysMLPackage.eINSTANCE.getRelationship_IsImplied();
    }

    /**
     * Gets the Documentation.documentedElement {@link EStructuralFeature}.
     *
     * @return the Documentation.documentedElement {@link EStructuralFeature}
     *
     */
    protected EReference documentation_DocumentedElement() {
        return SysMLPackage.eINSTANCE.getDocumentation_DocumentedElement();
    }

    /**
     * Gets the Comment.locale {@link EStructuralFeature}.
     *
     * @return the Comment.locale {@link EStructuralFeature}
     *
     */
    protected EAttribute comment_Locale() {
        return SysMLPackage.eINSTANCE.getComment_Locale();
    }

    /**
     * Gets the Comment.body {@link EStructuralFeature}.
     *
     * @return the Comment.body {@link EStructuralFeature}
     *
     */
    protected EAttribute comment_Body() {
        return SysMLPackage.eINSTANCE.getComment_Body();
    }

    /**
     * Gets the AnnotatingElement.annotatedElement {@link EStructuralFeature}.
     *
     * @return the AnnotatingElement.annotatedElement {@link EStructuralFeature}
     *
     */
    protected EReference annotatingElement_AnnotatedElement() {
        return SysMLPackage.eINSTANCE.getAnnotatingElement_AnnotatedElement();
    }

    /**
     * Gets the AnnotatingElement.ownedAnnotatingRelationship {@link EStructuralFeature}.
     *
     * @return the AnnotatingElement.ownedAnnotatingRelationship {@link EStructuralFeature}
     *
     */
    protected EReference annotatingElement_OwnedAnnotatingRelationship() {
        return SysMLPackage.eINSTANCE.getAnnotatingElement_OwnedAnnotatingRelationship();
    }

    /**
     * Gets the AnnotatingElement.annotation {@link EStructuralFeature}.
     *
     * @return the AnnotatingElement.annotation {@link EStructuralFeature}
     *
     */
    protected EReference annotatingElement_Annotation() {
        return SysMLPackage.eINSTANCE.getAnnotatingElement_Annotation();
    }

    /**
     * Gets the AnnotatingElement.owningAnnotatingRelationship {@link EStructuralFeature}.
     *
     * @return the AnnotatingElement.owningAnnotatingRelationship {@link EStructuralFeature}
     *
     */
    protected EReference annotatingElement_OwningAnnotatingRelationship() {
        return SysMLPackage.eINSTANCE.getAnnotatingElement_OwningAnnotatingRelationship();
    }

    /**
     * Gets the Annotation.annotatingElement {@link EStructuralFeature}.
     *
     * @return the Annotation.annotatingElement {@link EStructuralFeature}
     *
     */
    protected EReference annotation_AnnotatingElement() {
        return SysMLPackage.eINSTANCE.getAnnotation_AnnotatingElement();
    }

    /**
     * Gets the Annotation.annotatedElement {@link EStructuralFeature}.
     *
     * @return the Annotation.annotatedElement {@link EStructuralFeature}
     *
     */
    protected EReference annotation_AnnotatedElement() {
        return SysMLPackage.eINSTANCE.getAnnotation_AnnotatedElement();
    }

    /**
     * Gets the Annotation.owningAnnotatedElement {@link EStructuralFeature}.
     *
     * @return the Annotation.owningAnnotatedElement {@link EStructuralFeature}
     *
     */
    protected EReference annotation_OwningAnnotatedElement() {
        return SysMLPackage.eINSTANCE.getAnnotation_OwningAnnotatedElement();
    }

    /**
     * Gets the Annotation.ownedAnnotatingElement {@link EStructuralFeature}.
     *
     * @return the Annotation.ownedAnnotatingElement {@link EStructuralFeature}
     *
     */
    protected EReference annotation_OwnedAnnotatingElement() {
        return SysMLPackage.eINSTANCE.getAnnotation_OwnedAnnotatingElement();
    }

    /**
     * Gets the Annotation.owningAnnotatingElement {@link EStructuralFeature}.
     *
     * @return the Annotation.owningAnnotatingElement {@link EStructuralFeature}
     *
     */
    protected EReference annotation_OwningAnnotatingElement() {
        return SysMLPackage.eINSTANCE.getAnnotation_OwningAnnotatingElement();
    }

    /**
     * Gets the TextualRepresentation.language {@link EStructuralFeature}.
     *
     * @return the TextualRepresentation.language {@link EStructuralFeature}
     *
     */
    protected EAttribute textualRepresentation_Language() {
        return SysMLPackage.eINSTANCE.getTextualRepresentation_Language();
    }

    /**
     * Gets the TextualRepresentation.body {@link EStructuralFeature}.
     *
     * @return the TextualRepresentation.body {@link EStructuralFeature}
     *
     */
    protected EAttribute textualRepresentation_Body() {
        return SysMLPackage.eINSTANCE.getTextualRepresentation_Body();
    }

    /**
     * Gets the TextualRepresentation.representedElement {@link EStructuralFeature}.
     *
     * @return the TextualRepresentation.representedElement {@link EStructuralFeature}
     *
     */
    protected EReference textualRepresentation_RepresentedElement() {
        return SysMLPackage.eINSTANCE.getTextualRepresentation_RepresentedElement();
    }

    /**
     * Gets the Import.visibility {@link EStructuralFeature}.
     *
     * @return the Import.visibility {@link EStructuralFeature}
     *
     */
    protected EAttribute import_Visibility() {
        return SysMLPackage.eINSTANCE.getImport_Visibility();
    }

    /**
     * Gets the Import.isRecursive {@link EStructuralFeature}.
     *
     * @return the Import.isRecursive {@link EStructuralFeature}
     *
     */
    protected EAttribute import_IsRecursive() {
        return SysMLPackage.eINSTANCE.getImport_IsRecursive();
    }

    /**
     * Gets the Import.isImportAll {@link EStructuralFeature}.
     *
     * @return the Import.isImportAll {@link EStructuralFeature}
     *
     */
    protected EAttribute import_IsImportAll() {
        return SysMLPackage.eINSTANCE.getImport_IsImportAll();
    }

    /**
     * Gets the Import.importedElement {@link EStructuralFeature}.
     *
     * @return the Import.importedElement {@link EStructuralFeature}
     *
     */
    protected EReference import_ImportedElement() {
        return SysMLPackage.eINSTANCE.getImport_ImportedElement();
    }

    /**
     * Gets the Import.importOwningNamespace {@link EStructuralFeature}.
     *
     * @return the Import.importOwningNamespace {@link EStructuralFeature}
     *
     */
    protected EReference import_ImportOwningNamespace() {
        return SysMLPackage.eINSTANCE.getImport_ImportOwningNamespace();
    }

    /**
     * Gets the Specialization.general {@link EStructuralFeature}.
     *
     * @return the Specialization.general {@link EStructuralFeature}
     *
     */
    protected EReference specialization_General() {
        return SysMLPackage.eINSTANCE.getSpecialization_General();
    }

    /**
     * Gets the Specialization.specific {@link EStructuralFeature}.
     *
     * @return the Specialization.specific {@link EStructuralFeature}
     *
     */
    protected EReference specialization_Specific() {
        return SysMLPackage.eINSTANCE.getSpecialization_Specific();
    }

    /**
     * Gets the Specialization.owningType {@link EStructuralFeature}.
     *
     * @return the Specialization.owningType {@link EStructuralFeature}
     *
     */
    protected EReference specialization_OwningType() {
        return SysMLPackage.eINSTANCE.getSpecialization_OwningType();
    }

    /**
     * Gets the FeatureMembership.ownedMemberFeature {@link EStructuralFeature}.
     *
     * @return the FeatureMembership.ownedMemberFeature {@link EStructuralFeature}
     *
     */
    protected EReference featureMembership_OwnedMemberFeature() {
        return SysMLPackage.eINSTANCE.getFeatureMembership_OwnedMemberFeature();
    }

    /**
     * Gets the FeatureMembership.owningType {@link EStructuralFeature}.
     *
     * @return the FeatureMembership.owningType {@link EStructuralFeature}
     *
     */
    protected EReference featureMembership_OwningType() {
        return SysMLPackage.eINSTANCE.getFeatureMembership_OwningType();
    }

    /**
     * Gets the Conjugation.originalType {@link EStructuralFeature}.
     *
     * @return the Conjugation.originalType {@link EStructuralFeature}
     *
     */
    protected EReference conjugation_OriginalType() {
        return SysMLPackage.eINSTANCE.getConjugation_OriginalType();
    }

    /**
     * Gets the Conjugation.conjugatedType {@link EStructuralFeature}.
     *
     * @return the Conjugation.conjugatedType {@link EStructuralFeature}
     *
     */
    protected EReference conjugation_ConjugatedType() {
        return SysMLPackage.eINSTANCE.getConjugation_ConjugatedType();
    }

    /**
     * Gets the Conjugation.owningType {@link EStructuralFeature}.
     *
     * @return the Conjugation.owningType {@link EStructuralFeature}
     *
     */
    protected EReference conjugation_OwningType() {
        return SysMLPackage.eINSTANCE.getConjugation_OwningType();
    }

    /**
     * Gets the Intersecting.intersectingType {@link EStructuralFeature}.
     *
     * @return the Intersecting.intersectingType {@link EStructuralFeature}
     *
     */
    protected EReference intersecting_IntersectingType() {
        return SysMLPackage.eINSTANCE.getIntersecting_IntersectingType();
    }

    /**
     * Gets the Intersecting.typeIntersected {@link EStructuralFeature}.
     *
     * @return the Intersecting.typeIntersected {@link EStructuralFeature}
     *
     */
    protected EReference intersecting_TypeIntersected() {
        return SysMLPackage.eINSTANCE.getIntersecting_TypeIntersected();
    }

    /**
     * Gets the Unioning.unioningType {@link EStructuralFeature}.
     *
     * @return the Unioning.unioningType {@link EStructuralFeature}
     *
     */
    protected EReference unioning_UnioningType() {
        return SysMLPackage.eINSTANCE.getUnioning_UnioningType();
    }

    /**
     * Gets the Unioning.typeUnioned {@link EStructuralFeature}.
     *
     * @return the Unioning.typeUnioned {@link EStructuralFeature}
     *
     */
    protected EReference unioning_TypeUnioned() {
        return SysMLPackage.eINSTANCE.getUnioning_TypeUnioned();
    }

    /**
     * Gets the Disjoining.typeDisjoined {@link EStructuralFeature}.
     *
     * @return the Disjoining.typeDisjoined {@link EStructuralFeature}
     *
     */
    protected EReference disjoining_TypeDisjoined() {
        return SysMLPackage.eINSTANCE.getDisjoining_TypeDisjoined();
    }

    /**
     * Gets the Disjoining.disjoiningType {@link EStructuralFeature}.
     *
     * @return the Disjoining.disjoiningType {@link EStructuralFeature}
     *
     */
    protected EReference disjoining_DisjoiningType() {
        return SysMLPackage.eINSTANCE.getDisjoining_DisjoiningType();
    }

    /**
     * Gets the Disjoining.owningType {@link EStructuralFeature}.
     *
     * @return the Disjoining.owningType {@link EStructuralFeature}
     *
     */
    protected EReference disjoining_OwningType() {
        return SysMLPackage.eINSTANCE.getDisjoining_OwningType();
    }

    /**
     * Gets the Differencing.differencingType {@link EStructuralFeature}.
     *
     * @return the Differencing.differencingType {@link EStructuralFeature}
     *
     */
    protected EReference differencing_DifferencingType() {
        return SysMLPackage.eINSTANCE.getDifferencing_DifferencingType();
    }

    /**
     * Gets the Differencing.typeDifferenced {@link EStructuralFeature}.
     *
     * @return the Differencing.typeDifferenced {@link EStructuralFeature}
     *
     */
    protected EReference differencing_TypeDifferenced() {
        return SysMLPackage.eINSTANCE.getDifferencing_TypeDifferenced();
    }

    /**
     * Gets the Redefinition.redefiningFeature {@link EStructuralFeature}.
     *
     * @return the Redefinition.redefiningFeature {@link EStructuralFeature}
     *
     */
    protected EReference redefinition_RedefiningFeature() {
        return SysMLPackage.eINSTANCE.getRedefinition_RedefiningFeature();
    }

    /**
     * Gets the Redefinition.redefinedFeature {@link EStructuralFeature}.
     *
     * @return the Redefinition.redefinedFeature {@link EStructuralFeature}
     *
     */
    protected EReference redefinition_RedefinedFeature() {
        return SysMLPackage.eINSTANCE.getRedefinition_RedefinedFeature();
    }

    /**
     * Gets the Subsetting.subsettedFeature {@link EStructuralFeature}.
     *
     * @return the Subsetting.subsettedFeature {@link EStructuralFeature}
     *
     */
    protected EReference subsetting_SubsettedFeature() {
        return SysMLPackage.eINSTANCE.getSubsetting_SubsettedFeature();
    }

    /**
     * Gets the Subsetting.subsettingFeature {@link EStructuralFeature}.
     *
     * @return the Subsetting.subsettingFeature {@link EStructuralFeature}
     *
     */
    protected EReference subsetting_SubsettingFeature() {
        return SysMLPackage.eINSTANCE.getSubsetting_SubsettingFeature();
    }

    /**
     * Gets the Subsetting.owningFeature {@link EStructuralFeature}.
     *
     * @return the Subsetting.owningFeature {@link EStructuralFeature}
     *
     */
    protected EReference subsetting_OwningFeature() {
        return SysMLPackage.eINSTANCE.getSubsetting_OwningFeature();
    }

    /**
     * Gets the FeatureTyping.typedFeature {@link EStructuralFeature}.
     *
     * @return the FeatureTyping.typedFeature {@link EStructuralFeature}
     *
     */
    protected EReference featureTyping_TypedFeature() {
        return SysMLPackage.eINSTANCE.getFeatureTyping_TypedFeature();
    }

    /**
     * Gets the FeatureTyping.type {@link EStructuralFeature}.
     *
     * @return the FeatureTyping.type {@link EStructuralFeature}
     *
     */
    protected EReference featureTyping_Type() {
        return SysMLPackage.eINSTANCE.getFeatureTyping_Type();
    }

    /**
     * Gets the FeatureTyping.owningFeature {@link EStructuralFeature}.
     *
     * @return the FeatureTyping.owningFeature {@link EStructuralFeature}
     *
     */
    protected EReference featureTyping_OwningFeature() {
        return SysMLPackage.eINSTANCE.getFeatureTyping_OwningFeature();
    }

    /**
     * Gets the TypeFeaturing.featureOfType {@link EStructuralFeature}.
     *
     * @return the TypeFeaturing.featureOfType {@link EStructuralFeature}
     *
     */
    protected EReference typeFeaturing_FeatureOfType() {
        return SysMLPackage.eINSTANCE.getTypeFeaturing_FeatureOfType();
    }

    /**
     * Gets the TypeFeaturing.featuringType {@link EStructuralFeature}.
     *
     * @return the TypeFeaturing.featuringType {@link EStructuralFeature}
     *
     */
    protected EReference typeFeaturing_FeaturingType() {
        return SysMLPackage.eINSTANCE.getTypeFeaturing_FeaturingType();
    }

    /**
     * Gets the TypeFeaturing.owningFeatureOfType {@link EStructuralFeature}.
     *
     * @return the TypeFeaturing.owningFeatureOfType {@link EStructuralFeature}
     *
     */
    protected EReference typeFeaturing_OwningFeatureOfType() {
        return SysMLPackage.eINSTANCE.getTypeFeaturing_OwningFeatureOfType();
    }

    /**
     * Gets the FeatureInverting.featureInverted {@link EStructuralFeature}.
     *
     * @return the FeatureInverting.featureInverted {@link EStructuralFeature}
     *
     */
    protected EReference featureInverting_FeatureInverted() {
        return SysMLPackage.eINSTANCE.getFeatureInverting_FeatureInverted();
    }

    /**
     * Gets the FeatureInverting.invertingFeature {@link EStructuralFeature}.
     *
     * @return the FeatureInverting.invertingFeature {@link EStructuralFeature}
     *
     */
    protected EReference featureInverting_InvertingFeature() {
        return SysMLPackage.eINSTANCE.getFeatureInverting_InvertingFeature();
    }

    /**
     * Gets the FeatureInverting.owningFeature {@link EStructuralFeature}.
     *
     * @return the FeatureInverting.owningFeature {@link EStructuralFeature}
     *
     */
    protected EReference featureInverting_OwningFeature() {
        return SysMLPackage.eINSTANCE.getFeatureInverting_OwningFeature();
    }

    /**
     * Gets the FeatureChaining.chainingFeature {@link EStructuralFeature}.
     *
     * @return the FeatureChaining.chainingFeature {@link EStructuralFeature}
     *
     */
    protected EReference featureChaining_ChainingFeature() {
        return SysMLPackage.eINSTANCE.getFeatureChaining_ChainingFeature();
    }

    /**
     * Gets the FeatureChaining.featureChained {@link EStructuralFeature}.
     *
     * @return the FeatureChaining.featureChained {@link EStructuralFeature}
     *
     */
    protected EReference featureChaining_FeatureChained() {
        return SysMLPackage.eINSTANCE.getFeatureChaining_FeatureChained();
    }

    /**
     * Gets the ReferenceSubsetting.referencedFeature {@link EStructuralFeature}.
     *
     * @return the ReferenceSubsetting.referencedFeature {@link EStructuralFeature}
     *
     */
    protected EReference referenceSubsetting_ReferencedFeature() {
        return SysMLPackage.eINSTANCE.getReferenceSubsetting_ReferencedFeature();
    }

    /**
     * Gets the ReferenceSubsetting.referencingFeature {@link EStructuralFeature}.
     *
     * @return the ReferenceSubsetting.referencingFeature {@link EStructuralFeature}
     *
     */
    protected EReference referenceSubsetting_ReferencingFeature() {
        return SysMLPackage.eINSTANCE.getReferenceSubsetting_ReferencingFeature();
    }

    /**
     * Gets the CrossSubsetting.crossedFeature {@link EStructuralFeature}.
     *
     * @return the CrossSubsetting.crossedFeature {@link EStructuralFeature}
     *
     */
    protected EReference crossSubsetting_CrossedFeature() {
        return SysMLPackage.eINSTANCE.getCrossSubsetting_CrossedFeature();
    }

    /**
     * Gets the CrossSubsetting.crossingFeature {@link EStructuralFeature}.
     *
     * @return the CrossSubsetting.crossingFeature {@link EStructuralFeature}
     *
     */
    protected EReference crossSubsetting_CrossingFeature() {
        return SysMLPackage.eINSTANCE.getCrossSubsetting_CrossingFeature();
    }

    /**
     * Gets the Behavior.step {@link EStructuralFeature}.
     *
     * @return the Behavior.step {@link EStructuralFeature}
     *
     */
    protected EReference behavior_Step() {
        return SysMLPackage.eINSTANCE.getBehavior_Step();
    }

    /**
     * Gets the Behavior.parameter {@link EStructuralFeature}.
     *
     * @return the Behavior.parameter {@link EStructuralFeature}
     *
     */
    protected EReference behavior_Parameter() {
        return SysMLPackage.eINSTANCE.getBehavior_Parameter();
    }

    /**
     * Gets the Classifier.ownedSubclassification {@link EStructuralFeature}.
     *
     * @return the Classifier.ownedSubclassification {@link EStructuralFeature}
     *
     */
    protected EReference classifier_OwnedSubclassification() {
        return SysMLPackage.eINSTANCE.getClassifier_OwnedSubclassification();
    }

    /**
     * Gets the Subclassification.superclassifier {@link EStructuralFeature}.
     *
     * @return the Subclassification.superclassifier {@link EStructuralFeature}
     *
     */
    protected EReference subclassification_Superclassifier() {
        return SysMLPackage.eINSTANCE.getSubclassification_Superclassifier();
    }

    /**
     * Gets the Subclassification.subclassifier {@link EStructuralFeature}.
     *
     * @return the Subclassification.subclassifier {@link EStructuralFeature}
     *
     */
    protected EReference subclassification_Subclassifier() {
        return SysMLPackage.eINSTANCE.getSubclassification_Subclassifier();
    }

    /**
     * Gets the Subclassification.owningClassifier {@link EStructuralFeature}.
     *
     * @return the Subclassification.owningClassifier {@link EStructuralFeature}
     *
     */
    protected EReference subclassification_OwningClassifier() {
        return SysMLPackage.eINSTANCE.getSubclassification_OwningClassifier();
    }

    /**
     * Gets the Function.expression {@link EStructuralFeature}.
     *
     * @return the Function.expression {@link EStructuralFeature}
     *
     */
    protected EReference function_Expression() {
        return SysMLPackage.eINSTANCE.getFunction_Expression();
    }

    /**
     * Gets the Function.result {@link EStructuralFeature}.
     *
     * @return the Function.result {@link EStructuralFeature}
     *
     */
    protected EReference function_Result() {
        return SysMLPackage.eINSTANCE.getFunction_Result();
    }

    /**
     * Gets the Function.isModelLevelEvaluable {@link EStructuralFeature}.
     *
     * @return the Function.isModelLevelEvaluable {@link EStructuralFeature}
     *
     */
    protected EAttribute function_IsModelLevelEvaluable() {
        return SysMLPackage.eINSTANCE.getFunction_IsModelLevelEvaluable();
    }

    /**
     * Gets the LiteralBoolean.value {@link EStructuralFeature}.
     *
     * @return the LiteralBoolean.value {@link EStructuralFeature}
     *
     */
    protected EAttribute literalBoolean_Value() {
        return SysMLPackage.eINSTANCE.getLiteralBoolean_Value();
    }

    /**
     * Gets the FeatureReferenceExpression.referent {@link EStructuralFeature}.
     *
     * @return the FeatureReferenceExpression.referent {@link EStructuralFeature}
     *
     */
    protected EReference featureReferenceExpression_Referent() {
        return SysMLPackage.eINSTANCE.getFeatureReferenceExpression_Referent();
    }

    /**
     * Gets the MetadataAccessExpression.referencedElement {@link EStructuralFeature}.
     *
     * @return the MetadataAccessExpression.referencedElement {@link EStructuralFeature}
     *
     */
    protected EReference metadataAccessExpression_ReferencedElement() {
        return SysMLPackage.eINSTANCE.getMetadataAccessExpression_ReferencedElement();
    }

    /**
     * Gets the MetadataFeature.metaclass {@link EStructuralFeature}.
     *
     * @return the MetadataFeature.metaclass {@link EStructuralFeature}
     *
     */
    protected EReference metadataFeature_Metaclass() {
        return SysMLPackage.eINSTANCE.getMetadataFeature_Metaclass();
    }

    /**
     * Gets the LiteralRational.value {@link EStructuralFeature}.
     *
     * @return the LiteralRational.value {@link EStructuralFeature}
     *
     */
    protected EAttribute literalRational_Value() {
        return SysMLPackage.eINSTANCE.getLiteralRational_Value();
    }

    /**
     * Gets the LiteralInteger.value {@link EStructuralFeature}.
     *
     * @return the LiteralInteger.value {@link EStructuralFeature}
     *
     */
    protected EAttribute literalInteger_Value() {
        return SysMLPackage.eINSTANCE.getLiteralInteger_Value();
    }

    /**
     * Gets the LiteralString.value {@link EStructuralFeature}.
     *
     * @return the LiteralString.value {@link EStructuralFeature}
     *
     */
    protected EAttribute literalString_Value() {
        return SysMLPackage.eINSTANCE.getLiteralString_Value();
    }

    /**
     * Gets the FeatureChainExpression.targetFeature {@link EStructuralFeature}.
     *
     * @return the FeatureChainExpression.targetFeature {@link EStructuralFeature}
     *
     */
    protected EReference featureChainExpression_TargetFeature() {
        return SysMLPackage.eINSTANCE.getFeatureChainExpression_TargetFeature();
    }

    /**
     * Gets the BooleanExpression.predicate {@link EStructuralFeature}.
     *
     * @return the BooleanExpression.predicate {@link EStructuralFeature}
     *
     */
    protected EReference booleanExpression_Predicate() {
        return SysMLPackage.eINSTANCE.getBooleanExpression_Predicate();
    }

    /**
     * Gets the ParameterMembership.ownedMemberParameter {@link EStructuralFeature}.
     *
     * @return the ParameterMembership.ownedMemberParameter {@link EStructuralFeature}
     *
     */
    protected EReference parameterMembership_OwnedMemberParameter() {
        return SysMLPackage.eINSTANCE.getParameterMembership_OwnedMemberParameter();
    }

    /**
     * Gets the Invariant.isNegated {@link EStructuralFeature}.
     *
     * @return the Invariant.isNegated {@link EStructuralFeature}
     *
     */
    protected EAttribute invariant_IsNegated() {
        return SysMLPackage.eINSTANCE.getInvariant_IsNegated();
    }

    /**
     * Gets the ResultExpressionMembership.ownedResultExpression {@link EStructuralFeature}.
     *
     * @return the ResultExpressionMembership.ownedResultExpression {@link EStructuralFeature}
     *
     */
    protected EReference resultExpressionMembership_OwnedResultExpression() {
        return SysMLPackage.eINSTANCE.getResultExpressionMembership_OwnedResultExpression();
    }

    /**
     * Gets the MultiplicityRange.lowerBound {@link EStructuralFeature}.
     *
     * @return the MultiplicityRange.lowerBound {@link EStructuralFeature}
     *
     */
    protected EReference multiplicityRange_LowerBound() {
        return SysMLPackage.eINSTANCE.getMultiplicityRange_LowerBound();
    }

    /**
     * Gets the MultiplicityRange.upperBound {@link EStructuralFeature}.
     *
     * @return the MultiplicityRange.upperBound {@link EStructuralFeature}
     *
     */
    protected EReference multiplicityRange_UpperBound() {
        return SysMLPackage.eINSTANCE.getMultiplicityRange_UpperBound();
    }

    /**
     * Gets the MultiplicityRange.bound {@link EStructuralFeature}.
     *
     * @return the MultiplicityRange.bound {@link EStructuralFeature}
     *
     */
    protected EReference multiplicityRange_Bound() {
        return SysMLPackage.eINSTANCE.getMultiplicityRange_Bound();
    }

    /**
     * Gets the FeatureValue.featureWithValue {@link EStructuralFeature}.
     *
     * @return the FeatureValue.featureWithValue {@link EStructuralFeature}
     *
     */
    protected EReference featureValue_FeatureWithValue() {
        return SysMLPackage.eINSTANCE.getFeatureValue_FeatureWithValue();
    }

    /**
     * Gets the FeatureValue.value {@link EStructuralFeature}.
     *
     * @return the FeatureValue.value {@link EStructuralFeature}
     *
     */
    protected EReference featureValue_Value() {
        return SysMLPackage.eINSTANCE.getFeatureValue_Value();
    }

    /**
     * Gets the FeatureValue.isInitial {@link EStructuralFeature}.
     *
     * @return the FeatureValue.isInitial {@link EStructuralFeature}
     *
     */
    protected EAttribute featureValue_IsInitial() {
        return SysMLPackage.eINSTANCE.getFeatureValue_IsInitial();
    }

    /**
     * Gets the FeatureValue.isDefault {@link EStructuralFeature}.
     *
     * @return the FeatureValue.isDefault {@link EStructuralFeature}
     *
     */
    protected EAttribute featureValue_IsDefault() {
        return SysMLPackage.eINSTANCE.getFeatureValue_IsDefault();
    }

    /**
     * Gets the Connector.relatedFeature {@link EStructuralFeature}.
     *
     * @return the Connector.relatedFeature {@link EStructuralFeature}
     *
     */
    protected EReference connector_RelatedFeature() {
        return SysMLPackage.eINSTANCE.getConnector_RelatedFeature();
    }

    /**
     * Gets the Connector.association {@link EStructuralFeature}.
     *
     * @return the Connector.association {@link EStructuralFeature}
     *
     */
    protected EReference connector_Association() {
        return SysMLPackage.eINSTANCE.getConnector_Association();
    }

    /**
     * Gets the Connector.connectorEnd {@link EStructuralFeature}.
     *
     * @return the Connector.connectorEnd {@link EStructuralFeature}
     *
     */
    protected EReference connector_ConnectorEnd() {
        return SysMLPackage.eINSTANCE.getConnector_ConnectorEnd();
    }

    /**
     * Gets the Connector.sourceFeature {@link EStructuralFeature}.
     *
     * @return the Connector.sourceFeature {@link EStructuralFeature}
     *
     */
    protected EReference connector_SourceFeature() {
        return SysMLPackage.eINSTANCE.getConnector_SourceFeature();
    }

    /**
     * Gets the Connector.targetFeature {@link EStructuralFeature}.
     *
     * @return the Connector.targetFeature {@link EStructuralFeature}
     *
     */
    protected EReference connector_TargetFeature() {
        return SysMLPackage.eINSTANCE.getConnector_TargetFeature();
    }

    /**
     * Gets the Connector.defaultFeaturingType {@link EStructuralFeature}.
     *
     * @return the Connector.defaultFeaturingType {@link EStructuralFeature}
     *
     */
    protected EReference connector_DefaultFeaturingType() {
        return SysMLPackage.eINSTANCE.getConnector_DefaultFeaturingType();
    }

    /**
     * Gets the Association.relatedType {@link EStructuralFeature}.
     *
     * @return the Association.relatedType {@link EStructuralFeature}
     *
     */
    protected EReference association_RelatedType() {
        return SysMLPackage.eINSTANCE.getAssociation_RelatedType();
    }

    /**
     * Gets the Association.sourceType {@link EStructuralFeature}.
     *
     * @return the Association.sourceType {@link EStructuralFeature}
     *
     */
    protected EReference association_SourceType() {
        return SysMLPackage.eINSTANCE.getAssociation_SourceType();
    }

    /**
     * Gets the Association.targetType {@link EStructuralFeature}.
     *
     * @return the Association.targetType {@link EStructuralFeature}
     *
     */
    protected EReference association_TargetType() {
        return SysMLPackage.eINSTANCE.getAssociation_TargetType();
    }

    /**
     * Gets the Association.associationEnd {@link EStructuralFeature}.
     *
     * @return the Association.associationEnd {@link EStructuralFeature}
     *
     */
    protected EReference association_AssociationEnd() {
        return SysMLPackage.eINSTANCE.getAssociation_AssociationEnd();
    }

    /**
     * Gets the Package.filterCondition {@link EStructuralFeature}.
     *
     * @return the Package.filterCondition {@link EStructuralFeature}
     *
     */
    protected EReference package_FilterCondition() {
        return SysMLPackage.eINSTANCE.getPackage_FilterCondition();
    }

    /**
     * Gets the LibraryPackage.isStandard {@link EStructuralFeature}.
     *
     * @return the LibraryPackage.isStandard {@link EStructuralFeature}
     *
     */
    protected EAttribute libraryPackage_IsStandard() {
        return SysMLPackage.eINSTANCE.getLibraryPackage_IsStandard();
    }

    /**
     * Gets the ElementFilterMembership.condition {@link EStructuralFeature}.
     *
     * @return the ElementFilterMembership.condition {@link EStructuralFeature}
     *
     */
    protected EReference elementFilterMembership_Condition() {
        return SysMLPackage.eINSTANCE.getElementFilterMembership_Condition();
    }

    /**
     * Gets the Flow.payloadType {@link EStructuralFeature}.
     *
     * @return the Flow.payloadType {@link EStructuralFeature}
     *
     */
    protected EReference flow_PayloadType() {
        return SysMLPackage.eINSTANCE.getFlow_PayloadType();
    }

    /**
     * Gets the Flow.targetInputFeature {@link EStructuralFeature}.
     *
     * @return the Flow.targetInputFeature {@link EStructuralFeature}
     *
     */
    protected EReference flow_TargetInputFeature() {
        return SysMLPackage.eINSTANCE.getFlow_TargetInputFeature();
    }

    /**
     * Gets the Flow.sourceOutputFeature {@link EStructuralFeature}.
     *
     * @return the Flow.sourceOutputFeature {@link EStructuralFeature}
     *
     */
    protected EReference flow_SourceOutputFeature() {
        return SysMLPackage.eINSTANCE.getFlow_SourceOutputFeature();
    }

    /**
     * Gets the Flow.flowEnd {@link EStructuralFeature}.
     *
     * @return the Flow.flowEnd {@link EStructuralFeature}
     *
     */
    protected EReference flow_FlowEnd() {
        return SysMLPackage.eINSTANCE.getFlow_FlowEnd();
    }

    /**
     * Gets the Flow.payloadFeature {@link EStructuralFeature}.
     *
     * @return the Flow.payloadFeature {@link EStructuralFeature}
     *
     */
    protected EReference flow_PayloadFeature() {
        return SysMLPackage.eINSTANCE.getFlow_PayloadFeature();
    }

    /**
     * Gets the Flow.interaction {@link EStructuralFeature}.
     *
     * @return the Flow.interaction {@link EStructuralFeature}
     *
     */
    protected EReference flow_Interaction() {
        return SysMLPackage.eINSTANCE.getFlow_Interaction();
    }

    /**
     * Gets the MembershipImport.importedMembership {@link EStructuralFeature}.
     *
     * @return the MembershipImport.importedMembership {@link EStructuralFeature}
     *
     */
    protected EReference membershipImport_ImportedMembership() {
        return SysMLPackage.eINSTANCE.getMembershipImport_ImportedMembership();
    }

    /**
     * Gets the NamespaceImport.importedNamespace {@link EStructuralFeature}.
     *
     * @return the NamespaceImport.importedNamespace {@link EStructuralFeature}
     *
     */
    protected EReference namespaceImport_ImportedNamespace() {
        return SysMLPackage.eINSTANCE.getNamespaceImport_ImportedNamespace();
    }

    /**
     * Gets the Dependency.client {@link EStructuralFeature}.
     *
     * @return the Dependency.client {@link EStructuralFeature}
     *
     */
    protected EReference dependency_Client() {
        return SysMLPackage.eINSTANCE.getDependency_Client();
    }

    /**
     * Gets the Dependency.supplier {@link EStructuralFeature}.
     *
     * @return the Dependency.supplier {@link EStructuralFeature}
     *
     */
    protected EReference dependency_Supplier() {
        return SysMLPackage.eINSTANCE.getDependency_Supplier();
    }

    /**
     * Gets the VerificationCaseUsage.verificationCaseDefinition {@link EStructuralFeature}.
     *
     * @return the VerificationCaseUsage.verificationCaseDefinition {@link EStructuralFeature}
     *
     */
    protected EReference verificationCaseUsage_VerificationCaseDefinition() {
        return SysMLPackage.eINSTANCE.getVerificationCaseUsage_VerificationCaseDefinition();
    }

    /**
     * Gets the VerificationCaseUsage.verifiedRequirement {@link EStructuralFeature}.
     *
     * @return the VerificationCaseUsage.verifiedRequirement {@link EStructuralFeature}
     *
     */
    protected EReference verificationCaseUsage_VerifiedRequirement() {
        return SysMLPackage.eINSTANCE.getVerificationCaseUsage_VerifiedRequirement();
    }

    /**
     * Gets the CaseUsage.objectiveRequirement {@link EStructuralFeature}.
     *
     * @return the CaseUsage.objectiveRequirement {@link EStructuralFeature}
     *
     */
    protected EReference caseUsage_ObjectiveRequirement() {
        return SysMLPackage.eINSTANCE.getCaseUsage_ObjectiveRequirement();
    }

    /**
     * Gets the CaseUsage.caseDefinition {@link EStructuralFeature}.
     *
     * @return the CaseUsage.caseDefinition {@link EStructuralFeature}
     *
     */
    protected EReference caseUsage_CaseDefinition() {
        return SysMLPackage.eINSTANCE.getCaseUsage_CaseDefinition();
    }

    /**
     * Gets the CaseUsage.subjectParameter {@link EStructuralFeature}.
     *
     * @return the CaseUsage.subjectParameter {@link EStructuralFeature}
     *
     */
    protected EReference caseUsage_SubjectParameter() {
        return SysMLPackage.eINSTANCE.getCaseUsage_SubjectParameter();
    }

    /**
     * Gets the CaseUsage.actorParameter {@link EStructuralFeature}.
     *
     * @return the CaseUsage.actorParameter {@link EStructuralFeature}
     *
     */
    protected EReference caseUsage_ActorParameter() {
        return SysMLPackage.eINSTANCE.getCaseUsage_ActorParameter();
    }

    /**
     * Gets the CalculationUsage.calculationDefinition {@link EStructuralFeature}.
     *
     * @return the CalculationUsage.calculationDefinition {@link EStructuralFeature}
     *
     */
    protected EReference calculationUsage_CalculationDefinition() {
        return SysMLPackage.eINSTANCE.getCalculationUsage_CalculationDefinition();
    }

    /**
     * Gets the ActionUsage.actionDefinition {@link EStructuralFeature}.
     *
     * @return the ActionUsage.actionDefinition {@link EStructuralFeature}
     *
     */
    protected EReference actionUsage_ActionDefinition() {
        return SysMLPackage.eINSTANCE.getActionUsage_ActionDefinition();
    }

    /**
     * Gets the OccurrenceUsage.occurrenceDefinition {@link EStructuralFeature}.
     *
     * @return the OccurrenceUsage.occurrenceDefinition {@link EStructuralFeature}
     *
     */
    protected EReference occurrenceUsage_OccurrenceDefinition() {
        return SysMLPackage.eINSTANCE.getOccurrenceUsage_OccurrenceDefinition();
    }

    /**
     * Gets the OccurrenceUsage.individualDefinition {@link EStructuralFeature}.
     *
     * @return the OccurrenceUsage.individualDefinition {@link EStructuralFeature}
     *
     */
    protected EReference occurrenceUsage_IndividualDefinition() {
        return SysMLPackage.eINSTANCE.getOccurrenceUsage_IndividualDefinition();
    }

    /**
     * Gets the OccurrenceUsage.isIndividual {@link EStructuralFeature}.
     *
     * @return the OccurrenceUsage.isIndividual {@link EStructuralFeature}
     *
     */
    protected EAttribute occurrenceUsage_IsIndividual() {
        return SysMLPackage.eINSTANCE.getOccurrenceUsage_IsIndividual();
    }

    /**
     * Gets the OccurrenceUsage.portionKind {@link EStructuralFeature}.
     *
     * @return the OccurrenceUsage.portionKind {@link EStructuralFeature}
     *
     */
    protected EAttribute occurrenceUsage_PortionKind() {
        return SysMLPackage.eINSTANCE.getOccurrenceUsage_PortionKind();
    }

    /**
     * Gets the Usage.mayTimeVary {@link EStructuralFeature}.
     *
     * @return the Usage.mayTimeVary {@link EStructuralFeature}
     *
     */
    protected EAttribute usage_MayTimeVary() {
        return SysMLPackage.eINSTANCE.getUsage_MayTimeVary();
    }

    /**
     * Gets the Usage.isReference {@link EStructuralFeature}.
     *
     * @return the Usage.isReference {@link EStructuralFeature}
     *
     */
    protected EAttribute usage_IsReference() {
        return SysMLPackage.eINSTANCE.getUsage_IsReference();
    }

    /**
     * Gets the Usage.variant {@link EStructuralFeature}.
     *
     * @return the Usage.variant {@link EStructuralFeature}
     *
     */
    protected EReference usage_Variant() {
        return SysMLPackage.eINSTANCE.getUsage_Variant();
    }

    /**
     * Gets the Usage.variantMembership {@link EStructuralFeature}.
     *
     * @return the Usage.variantMembership {@link EStructuralFeature}
     *
     */
    protected EReference usage_VariantMembership() {
        return SysMLPackage.eINSTANCE.getUsage_VariantMembership();
    }

    /**
     * Gets the Usage.owningDefinition {@link EStructuralFeature}.
     *
     * @return the Usage.owningDefinition {@link EStructuralFeature}
     *
     */
    protected EReference usage_OwningDefinition() {
        return SysMLPackage.eINSTANCE.getUsage_OwningDefinition();
    }

    /**
     * Gets the Usage.owningUsage {@link EStructuralFeature}.
     *
     * @return the Usage.owningUsage {@link EStructuralFeature}
     *
     */
    protected EReference usage_OwningUsage() {
        return SysMLPackage.eINSTANCE.getUsage_OwningUsage();
    }

    /**
     * Gets the Usage.nestedUsage {@link EStructuralFeature}.
     *
     * @return the Usage.nestedUsage {@link EStructuralFeature}
     *
     */
    protected EReference usage_NestedUsage() {
        return SysMLPackage.eINSTANCE.getUsage_NestedUsage();
    }

    /**
     * Gets the Usage.definition {@link EStructuralFeature}.
     *
     * @return the Usage.definition {@link EStructuralFeature}
     *
     */
    protected EReference usage_Definition() {
        return SysMLPackage.eINSTANCE.getUsage_Definition();
    }

    /**
     * Gets the Usage.usage {@link EStructuralFeature}.
     *
     * @return the Usage.usage {@link EStructuralFeature}
     *
     */
    protected EReference usage_Usage() {
        return SysMLPackage.eINSTANCE.getUsage_Usage();
    }

    /**
     * Gets the Usage.directedUsage {@link EStructuralFeature}.
     *
     * @return the Usage.directedUsage {@link EStructuralFeature}
     *
     */
    protected EReference usage_DirectedUsage() {
        return SysMLPackage.eINSTANCE.getUsage_DirectedUsage();
    }

    /**
     * Gets the Usage.nestedReference {@link EStructuralFeature}.
     *
     * @return the Usage.nestedReference {@link EStructuralFeature}
     *
     */
    protected EReference usage_NestedReference() {
        return SysMLPackage.eINSTANCE.getUsage_NestedReference();
    }

    /**
     * Gets the Usage.nestedAttribute {@link EStructuralFeature}.
     *
     * @return the Usage.nestedAttribute {@link EStructuralFeature}
     *
     */
    protected EReference usage_NestedAttribute() {
        return SysMLPackage.eINSTANCE.getUsage_NestedAttribute();
    }

    /**
     * Gets the Usage.nestedEnumeration {@link EStructuralFeature}.
     *
     * @return the Usage.nestedEnumeration {@link EStructuralFeature}
     *
     */
    protected EReference usage_NestedEnumeration() {
        return SysMLPackage.eINSTANCE.getUsage_NestedEnumeration();
    }

    /**
     * Gets the Usage.nestedOccurrence {@link EStructuralFeature}.
     *
     * @return the Usage.nestedOccurrence {@link EStructuralFeature}
     *
     */
    protected EReference usage_NestedOccurrence() {
        return SysMLPackage.eINSTANCE.getUsage_NestedOccurrence();
    }

    /**
     * Gets the Usage.nestedItem {@link EStructuralFeature}.
     *
     * @return the Usage.nestedItem {@link EStructuralFeature}
     *
     */
    protected EReference usage_NestedItem() {
        return SysMLPackage.eINSTANCE.getUsage_NestedItem();
    }

    /**
     * Gets the Usage.nestedPart {@link EStructuralFeature}.
     *
     * @return the Usage.nestedPart {@link EStructuralFeature}
     *
     */
    protected EReference usage_NestedPart() {
        return SysMLPackage.eINSTANCE.getUsage_NestedPart();
    }

    /**
     * Gets the Usage.nestedPort {@link EStructuralFeature}.
     *
     * @return the Usage.nestedPort {@link EStructuralFeature}
     *
     */
    protected EReference usage_NestedPort() {
        return SysMLPackage.eINSTANCE.getUsage_NestedPort();
    }

    /**
     * Gets the Usage.nestedConnection {@link EStructuralFeature}.
     *
     * @return the Usage.nestedConnection {@link EStructuralFeature}
     *
     */
    protected EReference usage_NestedConnection() {
        return SysMLPackage.eINSTANCE.getUsage_NestedConnection();
    }

    /**
     * Gets the Usage.nestedFlow {@link EStructuralFeature}.
     *
     * @return the Usage.nestedFlow {@link EStructuralFeature}
     *
     */
    protected EReference usage_NestedFlow() {
        return SysMLPackage.eINSTANCE.getUsage_NestedFlow();
    }

    /**
     * Gets the Usage.nestedInterface {@link EStructuralFeature}.
     *
     * @return the Usage.nestedInterface {@link EStructuralFeature}
     *
     */
    protected EReference usage_NestedInterface() {
        return SysMLPackage.eINSTANCE.getUsage_NestedInterface();
    }

    /**
     * Gets the Usage.nestedAllocation {@link EStructuralFeature}.
     *
     * @return the Usage.nestedAllocation {@link EStructuralFeature}
     *
     */
    protected EReference usage_NestedAllocation() {
        return SysMLPackage.eINSTANCE.getUsage_NestedAllocation();
    }

    /**
     * Gets the Usage.nestedAction {@link EStructuralFeature}.
     *
     * @return the Usage.nestedAction {@link EStructuralFeature}
     *
     */
    protected EReference usage_NestedAction() {
        return SysMLPackage.eINSTANCE.getUsage_NestedAction();
    }

    /**
     * Gets the Usage.nestedState {@link EStructuralFeature}.
     *
     * @return the Usage.nestedState {@link EStructuralFeature}
     *
     */
    protected EReference usage_NestedState() {
        return SysMLPackage.eINSTANCE.getUsage_NestedState();
    }

    /**
     * Gets the Usage.nestedTransition {@link EStructuralFeature}.
     *
     * @return the Usage.nestedTransition {@link EStructuralFeature}
     *
     */
    protected EReference usage_NestedTransition() {
        return SysMLPackage.eINSTANCE.getUsage_NestedTransition();
    }

    /**
     * Gets the Usage.nestedCalculation {@link EStructuralFeature}.
     *
     * @return the Usage.nestedCalculation {@link EStructuralFeature}
     *
     */
    protected EReference usage_NestedCalculation() {
        return SysMLPackage.eINSTANCE.getUsage_NestedCalculation();
    }

    /**
     * Gets the Usage.nestedConstraint {@link EStructuralFeature}.
     *
     * @return the Usage.nestedConstraint {@link EStructuralFeature}
     *
     */
    protected EReference usage_NestedConstraint() {
        return SysMLPackage.eINSTANCE.getUsage_NestedConstraint();
    }

    /**
     * Gets the Usage.nestedRequirement {@link EStructuralFeature}.
     *
     * @return the Usage.nestedRequirement {@link EStructuralFeature}
     *
     */
    protected EReference usage_NestedRequirement() {
        return SysMLPackage.eINSTANCE.getUsage_NestedRequirement();
    }

    /**
     * Gets the Usage.nestedConcern {@link EStructuralFeature}.
     *
     * @return the Usage.nestedConcern {@link EStructuralFeature}
     *
     */
    protected EReference usage_NestedConcern() {
        return SysMLPackage.eINSTANCE.getUsage_NestedConcern();
    }

    /**
     * Gets the Usage.nestedCase {@link EStructuralFeature}.
     *
     * @return the Usage.nestedCase {@link EStructuralFeature}
     *
     */
    protected EReference usage_NestedCase() {
        return SysMLPackage.eINSTANCE.getUsage_NestedCase();
    }

    /**
     * Gets the Usage.nestedAnalysisCase {@link EStructuralFeature}.
     *
     * @return the Usage.nestedAnalysisCase {@link EStructuralFeature}
     *
     */
    protected EReference usage_NestedAnalysisCase() {
        return SysMLPackage.eINSTANCE.getUsage_NestedAnalysisCase();
    }

    /**
     * Gets the Usage.nestedVerificationCase {@link EStructuralFeature}.
     *
     * @return the Usage.nestedVerificationCase {@link EStructuralFeature}
     *
     */
    protected EReference usage_NestedVerificationCase() {
        return SysMLPackage.eINSTANCE.getUsage_NestedVerificationCase();
    }

    /**
     * Gets the Usage.nestedUseCase {@link EStructuralFeature}.
     *
     * @return the Usage.nestedUseCase {@link EStructuralFeature}
     *
     */
    protected EReference usage_NestedUseCase() {
        return SysMLPackage.eINSTANCE.getUsage_NestedUseCase();
    }

    /**
     * Gets the Usage.nestedView {@link EStructuralFeature}.
     *
     * @return the Usage.nestedView {@link EStructuralFeature}
     *
     */
    protected EReference usage_NestedView() {
        return SysMLPackage.eINSTANCE.getUsage_NestedView();
    }

    /**
     * Gets the Usage.nestedViewpoint {@link EStructuralFeature}.
     *
     * @return the Usage.nestedViewpoint {@link EStructuralFeature}
     *
     */
    protected EReference usage_NestedViewpoint() {
        return SysMLPackage.eINSTANCE.getUsage_NestedViewpoint();
    }

    /**
     * Gets the Usage.nestedRendering {@link EStructuralFeature}.
     *
     * @return the Usage.nestedRendering {@link EStructuralFeature}
     *
     */
    protected EReference usage_NestedRendering() {
        return SysMLPackage.eINSTANCE.getUsage_NestedRendering();
    }

    /**
     * Gets the Usage.nestedMetadata {@link EStructuralFeature}.
     *
     * @return the Usage.nestedMetadata {@link EStructuralFeature}
     *
     */
    protected EReference usage_NestedMetadata() {
        return SysMLPackage.eINSTANCE.getUsage_NestedMetadata();
    }

    /**
     * Gets the Usage.isVariation {@link EStructuralFeature}.
     *
     * @return the Usage.isVariation {@link EStructuralFeature}
     *
     */
    protected EAttribute usage_IsVariation() {
        return SysMLPackage.eINSTANCE.getUsage_IsVariation();
    }

    /**
     * Gets the VariantMembership.ownedVariantUsage {@link EStructuralFeature}.
     *
     * @return the VariantMembership.ownedVariantUsage {@link EStructuralFeature}
     *
     */
    protected EReference variantMembership_OwnedVariantUsage() {
        return SysMLPackage.eINSTANCE.getVariantMembership_OwnedVariantUsage();
    }

    /**
     * Gets the Definition.isVariation {@link EStructuralFeature}.
     *
     * @return the Definition.isVariation {@link EStructuralFeature}
     *
     */
    protected EAttribute definition_IsVariation() {
        return SysMLPackage.eINSTANCE.getDefinition_IsVariation();
    }

    /**
     * Gets the Definition.variant {@link EStructuralFeature}.
     *
     * @return the Definition.variant {@link EStructuralFeature}
     *
     */
    protected EReference definition_Variant() {
        return SysMLPackage.eINSTANCE.getDefinition_Variant();
    }

    /**
     * Gets the Definition.variantMembership {@link EStructuralFeature}.
     *
     * @return the Definition.variantMembership {@link EStructuralFeature}
     *
     */
    protected EReference definition_VariantMembership() {
        return SysMLPackage.eINSTANCE.getDefinition_VariantMembership();
    }

    /**
     * Gets the Definition.usage {@link EStructuralFeature}.
     *
     * @return the Definition.usage {@link EStructuralFeature}
     *
     */
    protected EReference definition_Usage() {
        return SysMLPackage.eINSTANCE.getDefinition_Usage();
    }

    /**
     * Gets the Definition.directedUsage {@link EStructuralFeature}.
     *
     * @return the Definition.directedUsage {@link EStructuralFeature}
     *
     */
    protected EReference definition_DirectedUsage() {
        return SysMLPackage.eINSTANCE.getDefinition_DirectedUsage();
    }

    /**
     * Gets the Definition.ownedReference {@link EStructuralFeature}.
     *
     * @return the Definition.ownedReference {@link EStructuralFeature}
     *
     */
    protected EReference definition_OwnedReference() {
        return SysMLPackage.eINSTANCE.getDefinition_OwnedReference();
    }

    /**
     * Gets the Definition.ownedAttribute {@link EStructuralFeature}.
     *
     * @return the Definition.ownedAttribute {@link EStructuralFeature}
     *
     */
    protected EReference definition_OwnedAttribute() {
        return SysMLPackage.eINSTANCE.getDefinition_OwnedAttribute();
    }

    /**
     * Gets the Definition.ownedEnumeration {@link EStructuralFeature}.
     *
     * @return the Definition.ownedEnumeration {@link EStructuralFeature}
     *
     */
    protected EReference definition_OwnedEnumeration() {
        return SysMLPackage.eINSTANCE.getDefinition_OwnedEnumeration();
    }

    /**
     * Gets the Definition.ownedOccurrence {@link EStructuralFeature}.
     *
     * @return the Definition.ownedOccurrence {@link EStructuralFeature}
     *
     */
    protected EReference definition_OwnedOccurrence() {
        return SysMLPackage.eINSTANCE.getDefinition_OwnedOccurrence();
    }

    /**
     * Gets the Definition.ownedItem {@link EStructuralFeature}.
     *
     * @return the Definition.ownedItem {@link EStructuralFeature}
     *
     */
    protected EReference definition_OwnedItem() {
        return SysMLPackage.eINSTANCE.getDefinition_OwnedItem();
    }

    /**
     * Gets the Definition.ownedPart {@link EStructuralFeature}.
     *
     * @return the Definition.ownedPart {@link EStructuralFeature}
     *
     */
    protected EReference definition_OwnedPart() {
        return SysMLPackage.eINSTANCE.getDefinition_OwnedPart();
    }

    /**
     * Gets the Definition.ownedPort {@link EStructuralFeature}.
     *
     * @return the Definition.ownedPort {@link EStructuralFeature}
     *
     */
    protected EReference definition_OwnedPort() {
        return SysMLPackage.eINSTANCE.getDefinition_OwnedPort();
    }

    /**
     * Gets the Definition.ownedConnection {@link EStructuralFeature}.
     *
     * @return the Definition.ownedConnection {@link EStructuralFeature}
     *
     */
    protected EReference definition_OwnedConnection() {
        return SysMLPackage.eINSTANCE.getDefinition_OwnedConnection();
    }

    /**
     * Gets the Definition.ownedFlow {@link EStructuralFeature}.
     *
     * @return the Definition.ownedFlow {@link EStructuralFeature}
     *
     */
    protected EReference definition_OwnedFlow() {
        return SysMLPackage.eINSTANCE.getDefinition_OwnedFlow();
    }

    /**
     * Gets the Definition.ownedInterface {@link EStructuralFeature}.
     *
     * @return the Definition.ownedInterface {@link EStructuralFeature}
     *
     */
    protected EReference definition_OwnedInterface() {
        return SysMLPackage.eINSTANCE.getDefinition_OwnedInterface();
    }

    /**
     * Gets the Definition.ownedAllocation {@link EStructuralFeature}.
     *
     * @return the Definition.ownedAllocation {@link EStructuralFeature}
     *
     */
    protected EReference definition_OwnedAllocation() {
        return SysMLPackage.eINSTANCE.getDefinition_OwnedAllocation();
    }

    /**
     * Gets the Definition.ownedAction {@link EStructuralFeature}.
     *
     * @return the Definition.ownedAction {@link EStructuralFeature}
     *
     */
    protected EReference definition_OwnedAction() {
        return SysMLPackage.eINSTANCE.getDefinition_OwnedAction();
    }

    /**
     * Gets the Definition.ownedState {@link EStructuralFeature}.
     *
     * @return the Definition.ownedState {@link EStructuralFeature}
     *
     */
    protected EReference definition_OwnedState() {
        return SysMLPackage.eINSTANCE.getDefinition_OwnedState();
    }

    /**
     * Gets the Definition.ownedTransition {@link EStructuralFeature}.
     *
     * @return the Definition.ownedTransition {@link EStructuralFeature}
     *
     */
    protected EReference definition_OwnedTransition() {
        return SysMLPackage.eINSTANCE.getDefinition_OwnedTransition();
    }

    /**
     * Gets the Definition.ownedCalculation {@link EStructuralFeature}.
     *
     * @return the Definition.ownedCalculation {@link EStructuralFeature}
     *
     */
    protected EReference definition_OwnedCalculation() {
        return SysMLPackage.eINSTANCE.getDefinition_OwnedCalculation();
    }

    /**
     * Gets the Definition.ownedConstraint {@link EStructuralFeature}.
     *
     * @return the Definition.ownedConstraint {@link EStructuralFeature}
     *
     */
    protected EReference definition_OwnedConstraint() {
        return SysMLPackage.eINSTANCE.getDefinition_OwnedConstraint();
    }

    /**
     * Gets the Definition.ownedRequirement {@link EStructuralFeature}.
     *
     * @return the Definition.ownedRequirement {@link EStructuralFeature}
     *
     */
    protected EReference definition_OwnedRequirement() {
        return SysMLPackage.eINSTANCE.getDefinition_OwnedRequirement();
    }

    /**
     * Gets the Definition.ownedConcern {@link EStructuralFeature}.
     *
     * @return the Definition.ownedConcern {@link EStructuralFeature}
     *
     */
    protected EReference definition_OwnedConcern() {
        return SysMLPackage.eINSTANCE.getDefinition_OwnedConcern();
    }

    /**
     * Gets the Definition.ownedCase {@link EStructuralFeature}.
     *
     * @return the Definition.ownedCase {@link EStructuralFeature}
     *
     */
    protected EReference definition_OwnedCase() {
        return SysMLPackage.eINSTANCE.getDefinition_OwnedCase();
    }

    /**
     * Gets the Definition.ownedAnalysisCase {@link EStructuralFeature}.
     *
     * @return the Definition.ownedAnalysisCase {@link EStructuralFeature}
     *
     */
    protected EReference definition_OwnedAnalysisCase() {
        return SysMLPackage.eINSTANCE.getDefinition_OwnedAnalysisCase();
    }

    /**
     * Gets the Definition.ownedVerificationCase {@link EStructuralFeature}.
     *
     * @return the Definition.ownedVerificationCase {@link EStructuralFeature}
     *
     */
    protected EReference definition_OwnedVerificationCase() {
        return SysMLPackage.eINSTANCE.getDefinition_OwnedVerificationCase();
    }

    /**
     * Gets the Definition.ownedUseCase {@link EStructuralFeature}.
     *
     * @return the Definition.ownedUseCase {@link EStructuralFeature}
     *
     */
    protected EReference definition_OwnedUseCase() {
        return SysMLPackage.eINSTANCE.getDefinition_OwnedUseCase();
    }

    /**
     * Gets the Definition.ownedView {@link EStructuralFeature}.
     *
     * @return the Definition.ownedView {@link EStructuralFeature}
     *
     */
    protected EReference definition_OwnedView() {
        return SysMLPackage.eINSTANCE.getDefinition_OwnedView();
    }

    /**
     * Gets the Definition.ownedViewpoint {@link EStructuralFeature}.
     *
     * @return the Definition.ownedViewpoint {@link EStructuralFeature}
     *
     */
    protected EReference definition_OwnedViewpoint() {
        return SysMLPackage.eINSTANCE.getDefinition_OwnedViewpoint();
    }

    /**
     * Gets the Definition.ownedRendering {@link EStructuralFeature}.
     *
     * @return the Definition.ownedRendering {@link EStructuralFeature}
     *
     */
    protected EReference definition_OwnedRendering() {
        return SysMLPackage.eINSTANCE.getDefinition_OwnedRendering();
    }

    /**
     * Gets the Definition.ownedMetadata {@link EStructuralFeature}.
     *
     * @return the Definition.ownedMetadata {@link EStructuralFeature}
     *
     */
    protected EReference definition_OwnedMetadata() {
        return SysMLPackage.eINSTANCE.getDefinition_OwnedMetadata();
    }

    /**
     * Gets the Definition.ownedUsage {@link EStructuralFeature}.
     *
     * @return the Definition.ownedUsage {@link EStructuralFeature}
     *
     */
    protected EReference definition_OwnedUsage() {
        return SysMLPackage.eINSTANCE.getDefinition_OwnedUsage();
    }

    /**
     * Gets the AttributeUsage.attributeDefinition {@link EStructuralFeature}.
     *
     * @return the AttributeUsage.attributeDefinition {@link EStructuralFeature}
     *
     */
    protected EReference attributeUsage_AttributeDefinition() {
        return SysMLPackage.eINSTANCE.getAttributeUsage_AttributeDefinition();
    }

    /**
     * Gets the EnumerationUsage.enumerationDefinition {@link EStructuralFeature}.
     *
     * @return the EnumerationUsage.enumerationDefinition {@link EStructuralFeature}
     *
     */
    protected EReference enumerationUsage_EnumerationDefinition() {
        return SysMLPackage.eINSTANCE.getEnumerationUsage_EnumerationDefinition();
    }

    /**
     * Gets the EnumerationDefinition.enumeratedValue {@link EStructuralFeature}.
     *
     * @return the EnumerationDefinition.enumeratedValue {@link EStructuralFeature}
     *
     */
    protected EReference enumerationDefinition_EnumeratedValue() {
        return SysMLPackage.eINSTANCE.getEnumerationDefinition_EnumeratedValue();
    }

    /**
     * Gets the ItemUsage.itemDefinition {@link EStructuralFeature}.
     *
     * @return the ItemUsage.itemDefinition {@link EStructuralFeature}
     *
     */
    protected EReference itemUsage_ItemDefinition() {
        return SysMLPackage.eINSTANCE.getItemUsage_ItemDefinition();
    }

    /**
     * Gets the PartUsage.partDefinition {@link EStructuralFeature}.
     *
     * @return the PartUsage.partDefinition {@link EStructuralFeature}
     *
     */
    protected EReference partUsage_PartDefinition() {
        return SysMLPackage.eINSTANCE.getPartUsage_PartDefinition();
    }

    /**
     * Gets the OccurrenceDefinition.isIndividual {@link EStructuralFeature}.
     *
     * @return the OccurrenceDefinition.isIndividual {@link EStructuralFeature}
     *
     */
    protected EAttribute occurrenceDefinition_IsIndividual() {
        return SysMLPackage.eINSTANCE.getOccurrenceDefinition_IsIndividual();
    }

    /**
     * Gets the PortUsage.portDefinition {@link EStructuralFeature}.
     *
     * @return the PortUsage.portDefinition {@link EStructuralFeature}
     *
     */
    protected EReference portUsage_PortDefinition() {
        return SysMLPackage.eINSTANCE.getPortUsage_PortDefinition();
    }

    /**
     * Gets the PortDefinition.conjugatedPortDefinition {@link EStructuralFeature}.
     *
     * @return the PortDefinition.conjugatedPortDefinition {@link EStructuralFeature}
     *
     */
    protected EReference portDefinition_ConjugatedPortDefinition() {
        return SysMLPackage.eINSTANCE.getPortDefinition_ConjugatedPortDefinition();
    }

    /**
     * Gets the ConjugatedPortDefinition.ownedPortConjugator {@link EStructuralFeature}.
     *
     * @return the ConjugatedPortDefinition.ownedPortConjugator {@link EStructuralFeature}
     *
     */
    protected EReference conjugatedPortDefinition_OwnedPortConjugator() {
        return SysMLPackage.eINSTANCE.getConjugatedPortDefinition_OwnedPortConjugator();
    }

    /**
     * Gets the ConjugatedPortDefinition.originalPortDefinition {@link EStructuralFeature}.
     *
     * @return the ConjugatedPortDefinition.originalPortDefinition {@link EStructuralFeature}
     *
     */
    protected EReference conjugatedPortDefinition_OriginalPortDefinition() {
        return SysMLPackage.eINSTANCE.getConjugatedPortDefinition_OriginalPortDefinition();
    }

    /**
     * Gets the PortConjugation.originalPortDefinition {@link EStructuralFeature}.
     *
     * @return the PortConjugation.originalPortDefinition {@link EStructuralFeature}
     *
     */
    protected EReference portConjugation_OriginalPortDefinition() {
        return SysMLPackage.eINSTANCE.getPortConjugation_OriginalPortDefinition();
    }

    /**
     * Gets the PortConjugation.conjugatedPortDefinition {@link EStructuralFeature}.
     *
     * @return the PortConjugation.conjugatedPortDefinition {@link EStructuralFeature}
     *
     */
    protected EReference portConjugation_ConjugatedPortDefinition() {
        return SysMLPackage.eINSTANCE.getPortConjugation_ConjugatedPortDefinition();
    }

    /**
     * Gets the FlowUsage.flowDefinition {@link EStructuralFeature}.
     *
     * @return the FlowUsage.flowDefinition {@link EStructuralFeature}
     *
     */
    protected EReference flowUsage_FlowDefinition() {
        return SysMLPackage.eINSTANCE.getFlowUsage_FlowDefinition();
    }

    /**
     * Gets the InterfaceUsage.interfaceDefinition {@link EStructuralFeature}.
     *
     * @return the InterfaceUsage.interfaceDefinition {@link EStructuralFeature}
     *
     */
    protected EReference interfaceUsage_InterfaceDefinition() {
        return SysMLPackage.eINSTANCE.getInterfaceUsage_InterfaceDefinition();
    }

    /**
     * Gets the ConnectionUsage.connectionDefinition {@link EStructuralFeature}.
     *
     * @return the ConnectionUsage.connectionDefinition {@link EStructuralFeature}
     *
     */
    protected EReference connectionUsage_ConnectionDefinition() {
        return SysMLPackage.eINSTANCE.getConnectionUsage_ConnectionDefinition();
    }

    /**
     * Gets the InterfaceDefinition.interfaceEnd {@link EStructuralFeature}.
     *
     * @return the InterfaceDefinition.interfaceEnd {@link EStructuralFeature}
     *
     */
    protected EReference interfaceDefinition_InterfaceEnd() {
        return SysMLPackage.eINSTANCE.getInterfaceDefinition_InterfaceEnd();
    }

    /**
     * Gets the ConnectionDefinition.connectionEnd {@link EStructuralFeature}.
     *
     * @return the ConnectionDefinition.connectionEnd {@link EStructuralFeature}
     *
     */
    protected EReference connectionDefinition_ConnectionEnd() {
        return SysMLPackage.eINSTANCE.getConnectionDefinition_ConnectionEnd();
    }

    /**
     * Gets the AllocationUsage.allocationDefinition {@link EStructuralFeature}.
     *
     * @return the AllocationUsage.allocationDefinition {@link EStructuralFeature}
     *
     */
    protected EReference allocationUsage_AllocationDefinition() {
        return SysMLPackage.eINSTANCE.getAllocationUsage_AllocationDefinition();
    }

    /**
     * Gets the AllocationDefinition.allocation {@link EStructuralFeature}.
     *
     * @return the AllocationDefinition.allocation {@link EStructuralFeature}
     *
     */
    protected EReference allocationDefinition_Allocation() {
        return SysMLPackage.eINSTANCE.getAllocationDefinition_Allocation();
    }

    /**
     * Gets the StateUsage.stateDefinition {@link EStructuralFeature}.
     *
     * @return the StateUsage.stateDefinition {@link EStructuralFeature}
     *
     */
    protected EReference stateUsage_StateDefinition() {
        return SysMLPackage.eINSTANCE.getStateUsage_StateDefinition();
    }

    /**
     * Gets the StateUsage.entryAction {@link EStructuralFeature}.
     *
     * @return the StateUsage.entryAction {@link EStructuralFeature}
     *
     */
    protected EReference stateUsage_EntryAction() {
        return SysMLPackage.eINSTANCE.getStateUsage_EntryAction();
    }

    /**
     * Gets the StateUsage.doAction {@link EStructuralFeature}.
     *
     * @return the StateUsage.doAction {@link EStructuralFeature}
     *
     */
    protected EReference stateUsage_DoAction() {
        return SysMLPackage.eINSTANCE.getStateUsage_DoAction();
    }

    /**
     * Gets the StateUsage.exitAction {@link EStructuralFeature}.
     *
     * @return the StateUsage.exitAction {@link EStructuralFeature}
     *
     */
    protected EReference stateUsage_ExitAction() {
        return SysMLPackage.eINSTANCE.getStateUsage_ExitAction();
    }

    /**
     * Gets the StateUsage.isParallel {@link EStructuralFeature}.
     *
     * @return the StateUsage.isParallel {@link EStructuralFeature}
     *
     */
    protected EAttribute stateUsage_IsParallel() {
        return SysMLPackage.eINSTANCE.getStateUsage_IsParallel();
    }

    /**
     * Gets the TransitionUsage.source {@link EStructuralFeature}.
     *
     * @return the TransitionUsage.source {@link EStructuralFeature}
     *
     */
    protected EReference transitionUsage_Source() {
        return SysMLPackage.eINSTANCE.getTransitionUsage_Source();
    }

    /**
     * Gets the TransitionUsage.target {@link EStructuralFeature}.
     *
     * @return the TransitionUsage.target {@link EStructuralFeature}
     *
     */
    protected EReference transitionUsage_Target() {
        return SysMLPackage.eINSTANCE.getTransitionUsage_Target();
    }

    /**
     * Gets the TransitionUsage.triggerAction {@link EStructuralFeature}.
     *
     * @return the TransitionUsage.triggerAction {@link EStructuralFeature}
     *
     */
    protected EReference transitionUsage_TriggerAction() {
        return SysMLPackage.eINSTANCE.getTransitionUsage_TriggerAction();
    }

    /**
     * Gets the TransitionUsage.guardExpression {@link EStructuralFeature}.
     *
     * @return the TransitionUsage.guardExpression {@link EStructuralFeature}
     *
     */
    protected EReference transitionUsage_GuardExpression() {
        return SysMLPackage.eINSTANCE.getTransitionUsage_GuardExpression();
    }

    /**
     * Gets the TransitionUsage.effectAction {@link EStructuralFeature}.
     *
     * @return the TransitionUsage.effectAction {@link EStructuralFeature}
     *
     */
    protected EReference transitionUsage_EffectAction() {
        return SysMLPackage.eINSTANCE.getTransitionUsage_EffectAction();
    }

    /**
     * Gets the TransitionUsage.succession {@link EStructuralFeature}.
     *
     * @return the TransitionUsage.succession {@link EStructuralFeature}
     *
     */
    protected EReference transitionUsage_Succession() {
        return SysMLPackage.eINSTANCE.getTransitionUsage_Succession();
    }

    /**
     * Gets the AcceptActionUsage.receiverArgument {@link EStructuralFeature}.
     *
     * @return the AcceptActionUsage.receiverArgument {@link EStructuralFeature}
     *
     */
    protected EReference acceptActionUsage_ReceiverArgument() {
        return SysMLPackage.eINSTANCE.getAcceptActionUsage_ReceiverArgument();
    }

    /**
     * Gets the AcceptActionUsage.payloadParameter {@link EStructuralFeature}.
     *
     * @return the AcceptActionUsage.payloadParameter {@link EStructuralFeature}
     *
     */
    protected EReference acceptActionUsage_PayloadParameter() {
        return SysMLPackage.eINSTANCE.getAcceptActionUsage_PayloadParameter();
    }

    /**
     * Gets the AcceptActionUsage.payloadArgument {@link EStructuralFeature}.
     *
     * @return the AcceptActionUsage.payloadArgument {@link EStructuralFeature}
     *
     */
    protected EReference acceptActionUsage_PayloadArgument() {
        return SysMLPackage.eINSTANCE.getAcceptActionUsage_PayloadArgument();
    }

    /**
     * Gets the ConstraintUsage.constraintDefinition {@link EStructuralFeature}.
     *
     * @return the ConstraintUsage.constraintDefinition {@link EStructuralFeature}
     *
     */
    protected EReference constraintUsage_ConstraintDefinition() {
        return SysMLPackage.eINSTANCE.getConstraintUsage_ConstraintDefinition();
    }

    /**
     * Gets the RequirementUsage.requirementDefinition {@link EStructuralFeature}.
     *
     * @return the RequirementUsage.requirementDefinition {@link EStructuralFeature}
     *
     */
    protected EReference requirementUsage_RequirementDefinition() {
        return SysMLPackage.eINSTANCE.getRequirementUsage_RequirementDefinition();
    }

    /**
     * Gets the RequirementUsage.reqId {@link EStructuralFeature}.
     *
     * @return the RequirementUsage.reqId {@link EStructuralFeature}
     *
     */
    protected EAttribute requirementUsage_ReqId() {
        return SysMLPackage.eINSTANCE.getRequirementUsage_ReqId();
    }

    /**
     * Gets the RequirementUsage.text {@link EStructuralFeature}.
     *
     * @return the RequirementUsage.text {@link EStructuralFeature}
     *
     */
    protected EAttribute requirementUsage_Text() {
        return SysMLPackage.eINSTANCE.getRequirementUsage_Text();
    }

    /**
     * Gets the RequirementUsage.requiredConstraint {@link EStructuralFeature}.
     *
     * @return the RequirementUsage.requiredConstraint {@link EStructuralFeature}
     *
     */
    protected EReference requirementUsage_RequiredConstraint() {
        return SysMLPackage.eINSTANCE.getRequirementUsage_RequiredConstraint();
    }

    /**
     * Gets the RequirementUsage.assumedConstraint {@link EStructuralFeature}.
     *
     * @return the RequirementUsage.assumedConstraint {@link EStructuralFeature}
     *
     */
    protected EReference requirementUsage_AssumedConstraint() {
        return SysMLPackage.eINSTANCE.getRequirementUsage_AssumedConstraint();
    }

    /**
     * Gets the RequirementUsage.subjectParameter {@link EStructuralFeature}.
     *
     * @return the RequirementUsage.subjectParameter {@link EStructuralFeature}
     *
     */
    protected EReference requirementUsage_SubjectParameter() {
        return SysMLPackage.eINSTANCE.getRequirementUsage_SubjectParameter();
    }

    /**
     * Gets the RequirementUsage.framedConcern {@link EStructuralFeature}.
     *
     * @return the RequirementUsage.framedConcern {@link EStructuralFeature}
     *
     */
    protected EReference requirementUsage_FramedConcern() {
        return SysMLPackage.eINSTANCE.getRequirementUsage_FramedConcern();
    }

    /**
     * Gets the RequirementUsage.actorParameter {@link EStructuralFeature}.
     *
     * @return the RequirementUsage.actorParameter {@link EStructuralFeature}
     *
     */
    protected EReference requirementUsage_ActorParameter() {
        return SysMLPackage.eINSTANCE.getRequirementUsage_ActorParameter();
    }

    /**
     * Gets the RequirementUsage.stakeholderParameter {@link EStructuralFeature}.
     *
     * @return the RequirementUsage.stakeholderParameter {@link EStructuralFeature}
     *
     */
    protected EReference requirementUsage_StakeholderParameter() {
        return SysMLPackage.eINSTANCE.getRequirementUsage_StakeholderParameter();
    }

    /**
     * Gets the RequirementDefinition.reqId {@link EStructuralFeature}.
     *
     * @return the RequirementDefinition.reqId {@link EStructuralFeature}
     *
     */
    protected EAttribute requirementDefinition_ReqId() {
        return SysMLPackage.eINSTANCE.getRequirementDefinition_ReqId();
    }

    /**
     * Gets the RequirementDefinition.text {@link EStructuralFeature}.
     *
     * @return the RequirementDefinition.text {@link EStructuralFeature}
     *
     */
    protected EAttribute requirementDefinition_Text() {
        return SysMLPackage.eINSTANCE.getRequirementDefinition_Text();
    }

    /**
     * Gets the RequirementDefinition.subjectParameter {@link EStructuralFeature}.
     *
     * @return the RequirementDefinition.subjectParameter {@link EStructuralFeature}
     *
     */
    protected EReference requirementDefinition_SubjectParameter() {
        return SysMLPackage.eINSTANCE.getRequirementDefinition_SubjectParameter();
    }

    /**
     * Gets the RequirementDefinition.actorParameter {@link EStructuralFeature}.
     *
     * @return the RequirementDefinition.actorParameter {@link EStructuralFeature}
     *
     */
    protected EReference requirementDefinition_ActorParameter() {
        return SysMLPackage.eINSTANCE.getRequirementDefinition_ActorParameter();
    }

    /**
     * Gets the RequirementDefinition.stakeholderParameter {@link EStructuralFeature}.
     *
     * @return the RequirementDefinition.stakeholderParameter {@link EStructuralFeature}
     *
     */
    protected EReference requirementDefinition_StakeholderParameter() {
        return SysMLPackage.eINSTANCE.getRequirementDefinition_StakeholderParameter();
    }

    /**
     * Gets the RequirementDefinition.assumedConstraint {@link EStructuralFeature}.
     *
     * @return the RequirementDefinition.assumedConstraint {@link EStructuralFeature}
     *
     */
    protected EReference requirementDefinition_AssumedConstraint() {
        return SysMLPackage.eINSTANCE.getRequirementDefinition_AssumedConstraint();
    }

    /**
     * Gets the RequirementDefinition.requiredConstraint {@link EStructuralFeature}.
     *
     * @return the RequirementDefinition.requiredConstraint {@link EStructuralFeature}
     *
     */
    protected EReference requirementDefinition_RequiredConstraint() {
        return SysMLPackage.eINSTANCE.getRequirementDefinition_RequiredConstraint();
    }

    /**
     * Gets the RequirementDefinition.framedConcern {@link EStructuralFeature}.
     *
     * @return the RequirementDefinition.framedConcern {@link EStructuralFeature}
     *
     */
    protected EReference requirementDefinition_FramedConcern() {
        return SysMLPackage.eINSTANCE.getRequirementDefinition_FramedConcern();
    }

    /**
     * Gets the ConcernUsage.concernDefinition {@link EStructuralFeature}.
     *
     * @return the ConcernUsage.concernDefinition {@link EStructuralFeature}
     *
     */
    protected EReference concernUsage_ConcernDefinition() {
        return SysMLPackage.eINSTANCE.getConcernUsage_ConcernDefinition();
    }

    /**
     * Gets the AnalysisCaseUsage.analysisCaseDefinition {@link EStructuralFeature}.
     *
     * @return the AnalysisCaseUsage.analysisCaseDefinition {@link EStructuralFeature}
     *
     */
    protected EReference analysisCaseUsage_AnalysisCaseDefinition() {
        return SysMLPackage.eINSTANCE.getAnalysisCaseUsage_AnalysisCaseDefinition();
    }

    /**
     * Gets the AnalysisCaseUsage.resultExpression {@link EStructuralFeature}.
     *
     * @return the AnalysisCaseUsage.resultExpression {@link EStructuralFeature}
     *
     */
    protected EReference analysisCaseUsage_ResultExpression() {
        return SysMLPackage.eINSTANCE.getAnalysisCaseUsage_ResultExpression();
    }

    /**
     * Gets the AnalysisCaseDefinition.resultExpression {@link EStructuralFeature}.
     *
     * @return the AnalysisCaseDefinition.resultExpression {@link EStructuralFeature}
     *
     */
    protected EReference analysisCaseDefinition_ResultExpression() {
        return SysMLPackage.eINSTANCE.getAnalysisCaseDefinition_ResultExpression();
    }

    /**
     * Gets the CaseDefinition.objectiveRequirement {@link EStructuralFeature}.
     *
     * @return the CaseDefinition.objectiveRequirement {@link EStructuralFeature}
     *
     */
    protected EReference caseDefinition_ObjectiveRequirement() {
        return SysMLPackage.eINSTANCE.getCaseDefinition_ObjectiveRequirement();
    }

    /**
     * Gets the CaseDefinition.subjectParameter {@link EStructuralFeature}.
     *
     * @return the CaseDefinition.subjectParameter {@link EStructuralFeature}
     *
     */
    protected EReference caseDefinition_SubjectParameter() {
        return SysMLPackage.eINSTANCE.getCaseDefinition_SubjectParameter();
    }

    /**
     * Gets the CaseDefinition.actorParameter {@link EStructuralFeature}.
     *
     * @return the CaseDefinition.actorParameter {@link EStructuralFeature}
     *
     */
    protected EReference caseDefinition_ActorParameter() {
        return SysMLPackage.eINSTANCE.getCaseDefinition_ActorParameter();
    }

    /**
     * Gets the CalculationDefinition.calculation {@link EStructuralFeature}.
     *
     * @return the CalculationDefinition.calculation {@link EStructuralFeature}
     *
     */
    protected EReference calculationDefinition_Calculation() {
        return SysMLPackage.eINSTANCE.getCalculationDefinition_Calculation();
    }

    /**
     * Gets the ActionDefinition.action {@link EStructuralFeature}.
     *
     * @return the ActionDefinition.action {@link EStructuralFeature}
     *
     */
    protected EReference actionDefinition_Action() {
        return SysMLPackage.eINSTANCE.getActionDefinition_Action();
    }

    /**
     * Gets the UseCaseUsage.useCaseDefinition {@link EStructuralFeature}.
     *
     * @return the UseCaseUsage.useCaseDefinition {@link EStructuralFeature}
     *
     */
    protected EReference useCaseUsage_UseCaseDefinition() {
        return SysMLPackage.eINSTANCE.getUseCaseUsage_UseCaseDefinition();
    }

    /**
     * Gets the UseCaseUsage.includedUseCase {@link EStructuralFeature}.
     *
     * @return the UseCaseUsage.includedUseCase {@link EStructuralFeature}
     *
     */
    protected EReference useCaseUsage_IncludedUseCase() {
        return SysMLPackage.eINSTANCE.getUseCaseUsage_IncludedUseCase();
    }

    /**
     * Gets the UseCaseDefinition.includedUseCase {@link EStructuralFeature}.
     *
     * @return the UseCaseDefinition.includedUseCase {@link EStructuralFeature}
     *
     */
    protected EReference useCaseDefinition_IncludedUseCase() {
        return SysMLPackage.eINSTANCE.getUseCaseDefinition_IncludedUseCase();
    }

    /**
     * Gets the ViewUsage.viewDefinition {@link EStructuralFeature}.
     *
     * @return the ViewUsage.viewDefinition {@link EStructuralFeature}
     *
     */
    protected EReference viewUsage_ViewDefinition() {
        return SysMLPackage.eINSTANCE.getViewUsage_ViewDefinition();
    }

    /**
     * Gets the ViewUsage.satisfiedViewpoint {@link EStructuralFeature}.
     *
     * @return the ViewUsage.satisfiedViewpoint {@link EStructuralFeature}
     *
     */
    protected EReference viewUsage_SatisfiedViewpoint() {
        return SysMLPackage.eINSTANCE.getViewUsage_SatisfiedViewpoint();
    }

    /**
     * Gets the ViewUsage.exposedElement {@link EStructuralFeature}.
     *
     * @return the ViewUsage.exposedElement {@link EStructuralFeature}
     *
     */
    protected EReference viewUsage_ExposedElement() {
        return SysMLPackage.eINSTANCE.getViewUsage_ExposedElement();
    }

    /**
     * Gets the ViewUsage.viewRendering {@link EStructuralFeature}.
     *
     * @return the ViewUsage.viewRendering {@link EStructuralFeature}
     *
     */
    protected EReference viewUsage_ViewRendering() {
        return SysMLPackage.eINSTANCE.getViewUsage_ViewRendering();
    }

    /**
     * Gets the ViewUsage.viewCondition {@link EStructuralFeature}.
     *
     * @return the ViewUsage.viewCondition {@link EStructuralFeature}
     *
     */
    protected EReference viewUsage_ViewCondition() {
        return SysMLPackage.eINSTANCE.getViewUsage_ViewCondition();
    }

    /**
     * Gets the ViewDefinition.view {@link EStructuralFeature}.
     *
     * @return the ViewDefinition.view {@link EStructuralFeature}
     *
     */
    protected EReference viewDefinition_View() {
        return SysMLPackage.eINSTANCE.getViewDefinition_View();
    }

    /**
     * Gets the ViewDefinition.satisfiedViewpoint {@link EStructuralFeature}.
     *
     * @return the ViewDefinition.satisfiedViewpoint {@link EStructuralFeature}
     *
     */
    protected EReference viewDefinition_SatisfiedViewpoint() {
        return SysMLPackage.eINSTANCE.getViewDefinition_SatisfiedViewpoint();
    }

    /**
     * Gets the ViewDefinition.viewRendering {@link EStructuralFeature}.
     *
     * @return the ViewDefinition.viewRendering {@link EStructuralFeature}
     *
     */
    protected EReference viewDefinition_ViewRendering() {
        return SysMLPackage.eINSTANCE.getViewDefinition_ViewRendering();
    }

    /**
     * Gets the ViewDefinition.viewCondition {@link EStructuralFeature}.
     *
     * @return the ViewDefinition.viewCondition {@link EStructuralFeature}
     *
     */
    protected EReference viewDefinition_ViewCondition() {
        return SysMLPackage.eINSTANCE.getViewDefinition_ViewCondition();
    }

    /**
     * Gets the ViewpointUsage.viewpointDefinition {@link EStructuralFeature}.
     *
     * @return the ViewpointUsage.viewpointDefinition {@link EStructuralFeature}
     *
     */
    protected EReference viewpointUsage_ViewpointDefinition() {
        return SysMLPackage.eINSTANCE.getViewpointUsage_ViewpointDefinition();
    }

    /**
     * Gets the ViewpointUsage.viewpointStakeholder {@link EStructuralFeature}.
     *
     * @return the ViewpointUsage.viewpointStakeholder {@link EStructuralFeature}
     *
     */
    protected EReference viewpointUsage_ViewpointStakeholder() {
        return SysMLPackage.eINSTANCE.getViewpointUsage_ViewpointStakeholder();
    }

    /**
     * Gets the ViewpointDefinition.viewpointStakeholder {@link EStructuralFeature}.
     *
     * @return the ViewpointDefinition.viewpointStakeholder {@link EStructuralFeature}
     *
     */
    protected EReference viewpointDefinition_ViewpointStakeholder() {
        return SysMLPackage.eINSTANCE.getViewpointDefinition_ViewpointStakeholder();
    }

    /**
     * Gets the RenderingUsage.renderingDefinition {@link EStructuralFeature}.
     *
     * @return the RenderingUsage.renderingDefinition {@link EStructuralFeature}
     *
     */
    protected EReference renderingUsage_RenderingDefinition() {
        return SysMLPackage.eINSTANCE.getRenderingUsage_RenderingDefinition();
    }

    /**
     * Gets the RenderingDefinition.rendering {@link EStructuralFeature}.
     *
     * @return the RenderingDefinition.rendering {@link EStructuralFeature}
     *
     */
    protected EReference renderingDefinition_Rendering() {
        return SysMLPackage.eINSTANCE.getRenderingDefinition_Rendering();
    }

    /**
     * Gets the MetadataUsage.metadataDefinition {@link EStructuralFeature}.
     *
     * @return the MetadataUsage.metadataDefinition {@link EStructuralFeature}
     *
     */
    protected EReference metadataUsage_MetadataDefinition() {
        return SysMLPackage.eINSTANCE.getMetadataUsage_MetadataDefinition();
    }

    /**
     * Gets the VerificationCaseDefinition.verifiedRequirement {@link EStructuralFeature}.
     *
     * @return the VerificationCaseDefinition.verifiedRequirement {@link EStructuralFeature}
     *
     */
    protected EReference verificationCaseDefinition_VerifiedRequirement() {
        return SysMLPackage.eINSTANCE.getVerificationCaseDefinition_VerifiedRequirement();
    }

    /**
     * Gets the RequirementVerificationMembership.ownedRequirement {@link EStructuralFeature}.
     *
     * @return the RequirementVerificationMembership.ownedRequirement {@link EStructuralFeature}
     *
     */
    protected EReference requirementVerificationMembership_OwnedRequirement() {
        return SysMLPackage.eINSTANCE.getRequirementVerificationMembership_OwnedRequirement();
    }

    /**
     * Gets the RequirementVerificationMembership.verifiedRequirement {@link EStructuralFeature}.
     *
     * @return the RequirementVerificationMembership.verifiedRequirement {@link EStructuralFeature}
     *
     */
    protected EReference requirementVerificationMembership_VerifiedRequirement() {
        return SysMLPackage.eINSTANCE.getRequirementVerificationMembership_VerifiedRequirement();
    }

    /**
     * Gets the RequirementConstraintMembership.kind {@link EStructuralFeature}.
     *
     * @return the RequirementConstraintMembership.kind {@link EStructuralFeature}
     *
     */
    protected EAttribute requirementConstraintMembership_Kind() {
        return SysMLPackage.eINSTANCE.getRequirementConstraintMembership_Kind();
    }

    /**
     * Gets the RequirementConstraintMembership.ownedConstraint {@link EStructuralFeature}.
     *
     * @return the RequirementConstraintMembership.ownedConstraint {@link EStructuralFeature}
     *
     */
    protected EReference requirementConstraintMembership_OwnedConstraint() {
        return SysMLPackage.eINSTANCE.getRequirementConstraintMembership_OwnedConstraint();
    }

    /**
     * Gets the RequirementConstraintMembership.referencedConstraint {@link EStructuralFeature}.
     *
     * @return the RequirementConstraintMembership.referencedConstraint {@link EStructuralFeature}
     *
     */
    protected EReference requirementConstraintMembership_ReferencedConstraint() {
        return SysMLPackage.eINSTANCE.getRequirementConstraintMembership_ReferencedConstraint();
    }

    /**
     * Gets the EventOccurrenceUsage.eventOccurrence {@link EStructuralFeature}.
     *
     * @return the EventOccurrenceUsage.eventOccurrence {@link EStructuralFeature}
     *
     */
    protected EReference eventOccurrenceUsage_EventOccurrence() {
        return SysMLPackage.eINSTANCE.getEventOccurrenceUsage_EventOccurrence();
    }

    /**
     * Gets the AssignmentActionUsage.targetArgument {@link EStructuralFeature}.
     *
     * @return the AssignmentActionUsage.targetArgument {@link EStructuralFeature}
     *
     */
    protected EReference assignmentActionUsage_TargetArgument() {
        return SysMLPackage.eINSTANCE.getAssignmentActionUsage_TargetArgument();
    }

    /**
     * Gets the AssignmentActionUsage.valueExpression {@link EStructuralFeature}.
     *
     * @return the AssignmentActionUsage.valueExpression {@link EStructuralFeature}
     *
     */
    protected EReference assignmentActionUsage_ValueExpression() {
        return SysMLPackage.eINSTANCE.getAssignmentActionUsage_ValueExpression();
    }

    /**
     * Gets the AssignmentActionUsage.referent {@link EStructuralFeature}.
     *
     * @return the AssignmentActionUsage.referent {@link EStructuralFeature}
     *
     */
    protected EReference assignmentActionUsage_Referent() {
        return SysMLPackage.eINSTANCE.getAssignmentActionUsage_Referent();
    }

    /**
     * Gets the TriggerInvocationExpression.kind {@link EStructuralFeature}.
     *
     * @return the TriggerInvocationExpression.kind {@link EStructuralFeature}
     *
     */
    protected EAttribute triggerInvocationExpression_Kind() {
        return SysMLPackage.eINSTANCE.getTriggerInvocationExpression_Kind();
    }

    /**
     * Gets the SendActionUsage.receiverArgument {@link EStructuralFeature}.
     *
     * @return the SendActionUsage.receiverArgument {@link EStructuralFeature}
     *
     */
    protected EReference sendActionUsage_ReceiverArgument() {
        return SysMLPackage.eINSTANCE.getSendActionUsage_ReceiverArgument();
    }

    /**
     * Gets the SendActionUsage.payloadArgument {@link EStructuralFeature}.
     *
     * @return the SendActionUsage.payloadArgument {@link EStructuralFeature}
     *
     */
    protected EReference sendActionUsage_PayloadArgument() {
        return SysMLPackage.eINSTANCE.getSendActionUsage_PayloadArgument();
    }

    /**
     * Gets the SendActionUsage.senderArgument {@link EStructuralFeature}.
     *
     * @return the SendActionUsage.senderArgument {@link EStructuralFeature}
     *
     */
    protected EReference sendActionUsage_SenderArgument() {
        return SysMLPackage.eINSTANCE.getSendActionUsage_SenderArgument();
    }

    /**
     * Gets the WhileLoopActionUsage.whileArgument {@link EStructuralFeature}.
     *
     * @return the WhileLoopActionUsage.whileArgument {@link EStructuralFeature}
     *
     */
    protected EReference whileLoopActionUsage_WhileArgument() {
        return SysMLPackage.eINSTANCE.getWhileLoopActionUsage_WhileArgument();
    }

    /**
     * Gets the WhileLoopActionUsage.untilArgument {@link EStructuralFeature}.
     *
     * @return the WhileLoopActionUsage.untilArgument {@link EStructuralFeature}
     *
     */
    protected EReference whileLoopActionUsage_UntilArgument() {
        return SysMLPackage.eINSTANCE.getWhileLoopActionUsage_UntilArgument();
    }

    /**
     * Gets the LoopActionUsage.bodyAction {@link EStructuralFeature}.
     *
     * @return the LoopActionUsage.bodyAction {@link EStructuralFeature}
     *
     */
    protected EReference loopActionUsage_BodyAction() {
        return SysMLPackage.eINSTANCE.getLoopActionUsage_BodyAction();
    }

    /**
     * Gets the PerformActionUsage.performedAction {@link EStructuralFeature}.
     *
     * @return the PerformActionUsage.performedAction {@link EStructuralFeature}
     *
     */
    protected EReference performActionUsage_PerformedAction() {
        return SysMLPackage.eINSTANCE.getPerformActionUsage_PerformedAction();
    }

    /**
     * Gets the ForLoopActionUsage.seqArgument {@link EStructuralFeature}.
     *
     * @return the ForLoopActionUsage.seqArgument {@link EStructuralFeature}
     *
     */
    protected EReference forLoopActionUsage_SeqArgument() {
        return SysMLPackage.eINSTANCE.getForLoopActionUsage_SeqArgument();
    }

    /**
     * Gets the ForLoopActionUsage.loopVariable {@link EStructuralFeature}.
     *
     * @return the ForLoopActionUsage.loopVariable {@link EStructuralFeature}
     *
     */
    protected EReference forLoopActionUsage_LoopVariable() {
        return SysMLPackage.eINSTANCE.getForLoopActionUsage_LoopVariable();
    }

    /**
     * Gets the TerminateActionUsage.terminatedOccurrenceArgument {@link EStructuralFeature}.
     *
     * @return the TerminateActionUsage.terminatedOccurrenceArgument {@link EStructuralFeature}
     *
     */
    protected EReference terminateActionUsage_TerminatedOccurrenceArgument() {
        return SysMLPackage.eINSTANCE.getTerminateActionUsage_TerminatedOccurrenceArgument();
    }

    /**
     * Gets the IfActionUsage.elseAction {@link EStructuralFeature}.
     *
     * @return the IfActionUsage.elseAction {@link EStructuralFeature}
     *
     */
    protected EReference ifActionUsage_ElseAction() {
        return SysMLPackage.eINSTANCE.getIfActionUsage_ElseAction();
    }

    /**
     * Gets the IfActionUsage.thenAction {@link EStructuralFeature}.
     *
     * @return the IfActionUsage.thenAction {@link EStructuralFeature}
     *
     */
    protected EReference ifActionUsage_ThenAction() {
        return SysMLPackage.eINSTANCE.getIfActionUsage_ThenAction();
    }

    /**
     * Gets the IfActionUsage.ifArgument {@link EStructuralFeature}.
     *
     * @return the IfActionUsage.ifArgument {@link EStructuralFeature}
     *
     */
    protected EReference ifActionUsage_IfArgument() {
        return SysMLPackage.eINSTANCE.getIfActionUsage_IfArgument();
    }

    /**
     * Gets the StateSubactionMembership.kind {@link EStructuralFeature}.
     *
     * @return the StateSubactionMembership.kind {@link EStructuralFeature}
     *
     */
    protected EAttribute stateSubactionMembership_Kind() {
        return SysMLPackage.eINSTANCE.getStateSubactionMembership_Kind();
    }

    /**
     * Gets the StateSubactionMembership.action {@link EStructuralFeature}.
     *
     * @return the StateSubactionMembership.action {@link EStructuralFeature}
     *
     */
    protected EReference stateSubactionMembership_Action() {
        return SysMLPackage.eINSTANCE.getStateSubactionMembership_Action();
    }

    /**
     * Gets the TransitionFeatureMembership.kind {@link EStructuralFeature}.
     *
     * @return the TransitionFeatureMembership.kind {@link EStructuralFeature}
     *
     */
    protected EAttribute transitionFeatureMembership_Kind() {
        return SysMLPackage.eINSTANCE.getTransitionFeatureMembership_Kind();
    }

    /**
     * Gets the TransitionFeatureMembership.transitionFeature {@link EStructuralFeature}.
     *
     * @return the TransitionFeatureMembership.transitionFeature {@link EStructuralFeature}
     *
     */
    protected EReference transitionFeatureMembership_TransitionFeature() {
        return SysMLPackage.eINSTANCE.getTransitionFeatureMembership_TransitionFeature();
    }

    /**
     * Gets the StateDefinition.state {@link EStructuralFeature}.
     *
     * @return the StateDefinition.state {@link EStructuralFeature}
     *
     */
    protected EReference stateDefinition_State() {
        return SysMLPackage.eINSTANCE.getStateDefinition_State();
    }

    /**
     * Gets the StateDefinition.entryAction {@link EStructuralFeature}.
     *
     * @return the StateDefinition.entryAction {@link EStructuralFeature}
     *
     */
    protected EReference stateDefinition_EntryAction() {
        return SysMLPackage.eINSTANCE.getStateDefinition_EntryAction();
    }

    /**
     * Gets the StateDefinition.doAction {@link EStructuralFeature}.
     *
     * @return the StateDefinition.doAction {@link EStructuralFeature}
     *
     */
    protected EReference stateDefinition_DoAction() {
        return SysMLPackage.eINSTANCE.getStateDefinition_DoAction();
    }

    /**
     * Gets the StateDefinition.exitAction {@link EStructuralFeature}.
     *
     * @return the StateDefinition.exitAction {@link EStructuralFeature}
     *
     */
    protected EReference stateDefinition_ExitAction() {
        return SysMLPackage.eINSTANCE.getStateDefinition_ExitAction();
    }

    /**
     * Gets the StateDefinition.isParallel {@link EStructuralFeature}.
     *
     * @return the StateDefinition.isParallel {@link EStructuralFeature}
     *
     */
    protected EAttribute stateDefinition_IsParallel() {
        return SysMLPackage.eINSTANCE.getStateDefinition_IsParallel();
    }

    /**
     * Gets the ExhibitStateUsage.exhibitedState {@link EStructuralFeature}.
     *
     * @return the ExhibitStateUsage.exhibitedState {@link EStructuralFeature}
     *
     */
    protected EReference exhibitStateUsage_ExhibitedState() {
        return SysMLPackage.eINSTANCE.getExhibitStateUsage_ExhibitedState();
    }

    /**
     * Gets the ObjectiveMembership.ownedObjectiveRequirement {@link EStructuralFeature}.
     *
     * @return the ObjectiveMembership.ownedObjectiveRequirement {@link EStructuralFeature}
     *
     */
    protected EReference objectiveMembership_OwnedObjectiveRequirement() {
        return SysMLPackage.eINSTANCE.getObjectiveMembership_OwnedObjectiveRequirement();
    }

    /**
     * Gets the ActorMembership.ownedActorParameter {@link EStructuralFeature}.
     *
     * @return the ActorMembership.ownedActorParameter {@link EStructuralFeature}
     *
     */
    protected EReference actorMembership_OwnedActorParameter() {
        return SysMLPackage.eINSTANCE.getActorMembership_OwnedActorParameter();
    }

    /**
     * Gets the SubjectMembership.ownedSubjectParameter {@link EStructuralFeature}.
     *
     * @return the SubjectMembership.ownedSubjectParameter {@link EStructuralFeature}
     *
     */
    protected EReference subjectMembership_OwnedSubjectParameter() {
        return SysMLPackage.eINSTANCE.getSubjectMembership_OwnedSubjectParameter();
    }

    /**
     * Gets the StakeholderMembership.ownedStakeholderParameter {@link EStructuralFeature}.
     *
     * @return the StakeholderMembership.ownedStakeholderParameter {@link EStructuralFeature}
     *
     */
    protected EReference stakeholderMembership_OwnedStakeholderParameter() {
        return SysMLPackage.eINSTANCE.getStakeholderMembership_OwnedStakeholderParameter();
    }

    /**
     * Gets the FramedConcernMembership.ownedConcern {@link EStructuralFeature}.
     *
     * @return the FramedConcernMembership.ownedConcern {@link EStructuralFeature}
     *
     */
    protected EReference framedConcernMembership_OwnedConcern() {
        return SysMLPackage.eINSTANCE.getFramedConcernMembership_OwnedConcern();
    }

    /**
     * Gets the FramedConcernMembership.referencedConcern {@link EStructuralFeature}.
     *
     * @return the FramedConcernMembership.referencedConcern {@link EStructuralFeature}
     *
     */
    protected EReference framedConcernMembership_ReferencedConcern() {
        return SysMLPackage.eINSTANCE.getFramedConcernMembership_ReferencedConcern();
    }

    /**
     * Gets the SatisfyRequirementUsage.satisfiedRequirement {@link EStructuralFeature}.
     *
     * @return the SatisfyRequirementUsage.satisfiedRequirement {@link EStructuralFeature}
     *
     */
    protected EReference satisfyRequirementUsage_SatisfiedRequirement() {
        return SysMLPackage.eINSTANCE.getSatisfyRequirementUsage_SatisfiedRequirement();
    }

    /**
     * Gets the SatisfyRequirementUsage.satisfyingFeature {@link EStructuralFeature}.
     *
     * @return the SatisfyRequirementUsage.satisfyingFeature {@link EStructuralFeature}
     *
     */
    protected EReference satisfyRequirementUsage_SatisfyingFeature() {
        return SysMLPackage.eINSTANCE.getSatisfyRequirementUsage_SatisfyingFeature();
    }

    /**
     * Gets the AssertConstraintUsage.assertedConstraint {@link EStructuralFeature}.
     *
     * @return the AssertConstraintUsage.assertedConstraint {@link EStructuralFeature}
     *
     */
    protected EReference assertConstraintUsage_AssertedConstraint() {
        return SysMLPackage.eINSTANCE.getAssertConstraintUsage_AssertedConstraint();
    }

    /**
     * Gets the ViewRenderingMembership.ownedRendering {@link EStructuralFeature}.
     *
     * @return the ViewRenderingMembership.ownedRendering {@link EStructuralFeature}
     *
     */
    protected EReference viewRenderingMembership_OwnedRendering() {
        return SysMLPackage.eINSTANCE.getViewRenderingMembership_OwnedRendering();
    }

    /**
     * Gets the ViewRenderingMembership.referencedRendering {@link EStructuralFeature}.
     *
     * @return the ViewRenderingMembership.referencedRendering {@link EStructuralFeature}
     *
     */
    protected EReference viewRenderingMembership_ReferencedRendering() {
        return SysMLPackage.eINSTANCE.getViewRenderingMembership_ReferencedRendering();
    }

    /**
     * Gets the ConjugatedPortTyping.portDefinition {@link EStructuralFeature}.
     *
     * @return the ConjugatedPortTyping.portDefinition {@link EStructuralFeature}
     *
     */
    protected EReference conjugatedPortTyping_PortDefinition() {
        return SysMLPackage.eINSTANCE.getConjugatedPortTyping_PortDefinition();
    }

    /**
     * Gets the ConjugatedPortTyping.conjugatedPortDefinition {@link EStructuralFeature}.
     *
     * @return the ConjugatedPortTyping.conjugatedPortDefinition {@link EStructuralFeature}
     *
     */
    protected EReference conjugatedPortTyping_ConjugatedPortDefinition() {
        return SysMLPackage.eINSTANCE.getConjugatedPortTyping_ConjugatedPortDefinition();
    }

    /**
     * Gets the FlowDefinition.flowEnd {@link EStructuralFeature}.
     *
     * @return the FlowDefinition.flowEnd {@link EStructuralFeature}
     *
     */
    protected EReference flowDefinition_FlowEnd() {
        return SysMLPackage.eINSTANCE.getFlowDefinition_FlowEnd();
    }

    /**
     * Gets the IncludeUseCaseUsage.useCaseIncluded {@link EStructuralFeature}.
     *
     * @return the IncludeUseCaseUsage.useCaseIncluded {@link EStructuralFeature}
     *
     */
    protected EReference includeUseCaseUsage_UseCaseIncluded() {
        return SysMLPackage.eINSTANCE.getIncludeUseCaseUsage_UseCaseIncluded();
    }

    /**
     * Gets the VisibilityKind.private {@link EEnumLiteral}.
     *
     * @return the VisibilityKind.private {@link EEnumLiteral}
     *
     */
    protected VisibilityKind visibilityKind_Private() {
        return VisibilityKind.PRIVATE;
    }

    /**
     * Gets the VisibilityKind.protected {@link EEnumLiteral}.
     *
     * @return the VisibilityKind.protected {@link EEnumLiteral}
     *
     */
    protected VisibilityKind visibilityKind_Protected() {
        return VisibilityKind.PROTECTED;
    }

    /**
     * Gets the VisibilityKind.public {@link EEnumLiteral}.
     *
     * @return the VisibilityKind.public {@link EEnumLiteral}
     *
     */
    protected VisibilityKind visibilityKind_Public() {
        return VisibilityKind.PUBLIC;
    }

    /**
     * Gets the FeatureDirectionKind.in {@link EEnumLiteral}.
     *
     * @return the FeatureDirectionKind.in {@link EEnumLiteral}
     *
     */
    protected FeatureDirectionKind featureDirectionKind_In() {
        return FeatureDirectionKind.IN;
    }

    /**
     * Gets the FeatureDirectionKind.inout {@link EEnumLiteral}.
     *
     * @return the FeatureDirectionKind.inout {@link EEnumLiteral}
     *
     */
    protected FeatureDirectionKind featureDirectionKind_Inout() {
        return FeatureDirectionKind.INOUT;
    }

    /**
     * Gets the FeatureDirectionKind.out {@link EEnumLiteral}.
     *
     * @return the FeatureDirectionKind.out {@link EEnumLiteral}
     *
     */
    protected FeatureDirectionKind featureDirectionKind_Out() {
        return FeatureDirectionKind.OUT;
    }

    /**
     * Gets the PortionKind.timeslice {@link EEnumLiteral}.
     *
     * @return the PortionKind.timeslice {@link EEnumLiteral}
     *
     */
    protected PortionKind portionKind_Timeslice() {
        return PortionKind.TIMESLICE;
    }

    /**
     * Gets the PortionKind.snapshot {@link EEnumLiteral}.
     *
     * @return the PortionKind.snapshot {@link EEnumLiteral}
     *
     */
    protected PortionKind portionKind_Snapshot() {
        return PortionKind.SNAPSHOT;
    }

    /**
     * Gets the RequirementConstraintKind.assumption {@link EEnumLiteral}.
     *
     * @return the RequirementConstraintKind.assumption {@link EEnumLiteral}
     *
     */
    protected RequirementConstraintKind requirementConstraintKind_Assumption() {
        return RequirementConstraintKind.ASSUMPTION;
    }

    /**
     * Gets the RequirementConstraintKind.requirement {@link EEnumLiteral}.
     *
     * @return the RequirementConstraintKind.requirement {@link EEnumLiteral}
     *
     */
    protected RequirementConstraintKind requirementConstraintKind_Requirement() {
        return RequirementConstraintKind.REQUIREMENT;
    }

    /**
     * Gets the TriggerKind.when {@link EEnumLiteral}.
     *
     * @return the TriggerKind.when {@link EEnumLiteral}
     *
     */
    protected TriggerKind triggerKind_When() {
        return TriggerKind.WHEN;
    }

    /**
     * Gets the TriggerKind.at {@link EEnumLiteral}.
     *
     * @return the TriggerKind.at {@link EEnumLiteral}
     *
     */
    protected TriggerKind triggerKind_At() {
        return TriggerKind.AT;
    }

    /**
     * Gets the TriggerKind.after {@link EEnumLiteral}.
     *
     * @return the TriggerKind.after {@link EEnumLiteral}
     *
     */
    protected TriggerKind triggerKind_After() {
        return TriggerKind.AFTER;
    }

    /**
     * Gets the StateSubactionKind.entry {@link EEnumLiteral}.
     *
     * @return the StateSubactionKind.entry {@link EEnumLiteral}
     *
     */
    protected StateSubactionKind stateSubactionKind_Entry() {
        return StateSubactionKind.ENTRY;
    }

    /**
     * Gets the StateSubactionKind.do {@link EEnumLiteral}.
     *
     * @return the StateSubactionKind.do {@link EEnumLiteral}
     *
     */
    protected StateSubactionKind stateSubactionKind_Do() {
        return StateSubactionKind.DO;
    }

    /**
     * Gets the StateSubactionKind.exit {@link EEnumLiteral}.
     *
     * @return the StateSubactionKind.exit {@link EEnumLiteral}
     *
     */
    protected StateSubactionKind stateSubactionKind_Exit() {
        return StateSubactionKind.EXIT;
    }

    /**
     * Gets the TransitionFeatureKind.trigger {@link EEnumLiteral}.
     *
     * @return the TransitionFeatureKind.trigger {@link EEnumLiteral}
     *
     */
    protected TransitionFeatureKind transitionFeatureKind_Trigger() {
        return TransitionFeatureKind.TRIGGER;
    }

    /**
     * Gets the TransitionFeatureKind.guard {@link EEnumLiteral}.
     *
     * @return the TransitionFeatureKind.guard {@link EEnumLiteral}
     *
     */
    protected TransitionFeatureKind transitionFeatureKind_Guard() {
        return TransitionFeatureKind.GUARD;
    }

    /**
     * Gets the TransitionFeatureKind.effect {@link EEnumLiteral}.
     *
     * @return the TransitionFeatureKind.effect {@link EEnumLiteral}
     *
     */
    protected TransitionFeatureKind transitionFeatureKind_Effect() {
        return TransitionFeatureKind.EFFECT;
    }
}
