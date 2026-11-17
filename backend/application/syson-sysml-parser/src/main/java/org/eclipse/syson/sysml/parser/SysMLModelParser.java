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
package org.eclipse.syson.sysml.parser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.DefaultErrorStrategy;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.syson.sysml.parser.internal.SysMLLexer;
import org.eclipse.syson.sysml.parser.internal.SysMLParser;
import org.omg.sysml.lang.sysml.*;
import org.omg.sysml.lang.sysml.Package;

/**
 * Entry point for parsing SysML v2 text directly into OMG SysML model objects.
 * <p>
 * The generated ANTLR parser builds the EMF model while applying a selected grammar rule and records unresolved names
 * as {@link DanglingLink dangling links}. Cross-reference resolution is deliberately left to the linker after the
 * complete model has been parsed. The returned {@link ISysMLParseResult} keeps syntax diagnostics separate from
 * internal model-building diagnostics.
 * </p>
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class SysMLModelParser {

    /**
     * Parse operation used to delegate parsing to a given rule.
     *
     * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
     */
    private interface ParseOperation {
        void parse(SysMLParser parser);
    }

    /**
     * Parses the given {@link InputStream} with the given {@link Charset} and delegating to the given
     * {@link ParseOperation}. The {@link ParseOperation} is used to delegate to a given parser rule.
     *
     * @param <T>
     *            the kind of {@link ISysMLParseResult}
     * @param is
     *            the {@link InputStream}
     * @param charset
     *            the {@link Charset} to use for reading the {@link InputStream}
     * @param parseOperation
     *            the {@link ParseOperation} that delegates to a parser rule
     * @return the {@link ISysMLParseResult}
     * @throws IOException
     *             if the {@link InputStream} can't be read
     */
    private <T> ISysMLParseResult<T> parse(InputStream is, Charset charset, ParseOperation parseOperation) throws IOException {
        return this.parse(new SysMLLexer(CharStreams.fromStream(is, charset)), parseOperation);
    }

    /**
     * Parses tokens from the supplied lexer, retaining its source positions and syntax diagnostics.
     *
     * @param <T> the model type produced by the selected rule
     * @param lexer the SysML lexer, optionally adapting tokens at an application boundary
     * @param parseOperation the selected grammar rule
     * @return the parsed model and diagnostics
     */
    private <T> ISysMLParseResult<T> parse(SysMLLexer lexer, ParseOperation parseOperation) {
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SysMLParser parser = new SysMLParser(tokens);
        parser.setBuildParseTree(false);
        parser.getInterpreter().setPredictionMode(PredictionMode.SLL);

        // configure diagnostic listener
        List<Diagnostic> diagnostics = new ArrayList<>();
        List<Diagnostic> internalDiagnostics = new ArrayList<>();
        parser.setInternalDiagnostics(internalDiagnostics);
        ANTLRErrorListener errorListener = parser.getErrorListener(diagnostics);
        lexer.removeErrorListeners();
        lexer.addErrorListener(errorListener);
        parser.removeErrorListeners();
        parser.addErrorListener(errorListener);

        try {
            parseOperation.parse(parser);
        } catch (IllegalArgumentException | IllegalStateException | IndexOutOfBoundsException | NoSuchElementException | NullPointerException | ClassCastException e) {
            tokens.seek(0);
            // ANTLR reset does not clear the EMF operand and context stacks from the failed model construction.
            parser = new SysMLParser(tokens);
            parser.setBuildParseTree(false);
            diagnostics = new ArrayList<>();
            internalDiagnostics = new ArrayList<>();
            parser.setInternalDiagnostics(internalDiagnostics);
            errorListener = parser.getErrorListener(diagnostics);
            lexer.removeErrorListeners();
            lexer.addErrorListener(errorListener);
            parser.removeErrorListeners();
            parser.addErrorListener(errorListener);
            parser.getInterpreter().setPredictionMode(PredictionMode.LL);
            parser.setErrorHandler(new DefaultErrorStrategy());
            try {
                // Changing the prediction mode alone does not run the LL fallback.
                parseOperation.parse(parser);
            } catch (IllegalArgumentException | IllegalStateException | IndexOutOfBoundsException | NoSuchElementException | NullPointerException | ClassCastException recoveryFailure) {
                diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, null, 0, "Unable to recover the SysML model: " + recoveryFailure.getMessage(), null));
                return new SysMLParseResult<>(null, Set.of(), Set.of(), new BasicDiagnostic(null, 0, diagnostics, null, null),
                        new BasicDiagnostic(null, 0, internalDiagnostics, null, null));
            }
        }

        Diagnostic diagnostic = new BasicDiagnostic(null, 0, diagnostics, null, null);
        Diagnostic internalDiagnostic = new BasicDiagnostic(null, 0, internalDiagnostics, null, null);
        Set<DanglingLink> danglingLinks = new LinkedHashSet<>(parser.getDanglingLinks());
        Set<EObject> textAdapted = parser.getTextAdapted();
        T model;
        if (diagnostic.getSeverity() == Diagnostic.ERROR) {
            if (parser.hasResult()) {
                model = parser.peekValue();
            } else {
                model = null;
            }
        } else {
            model = parser.peekValue();
        }

        return new SysMLParseResult<>(model, danglingLinks, textAdapted, diagnostic, internalDiagnostic);
    }

    /**
     * Parses the complete input using the <code>rootNamespace</code> rule, returning no root if recovery produced an incompatible result.
     *
     * @param is the textual input
     * @param charset the source character set
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Namespace> parseRootNamespace(InputStream is, Charset charset) throws IOException {
        return this.parseRootNamespace(new SysMLLexer(CharStreams.fromStream(is, charset)));
    }

    /**
     * Parses a complete document from a supplied lexer without changing the generated grammar.
     *
     * @param lexer the SysML lexer, optionally supplying application compatibility tokens
     * @return the root namespace and diagnostics, including any unconsumed input
     */
    public ISysMLParseResult<Namespace> parseRootNamespace(SysMLLexer lexer) {
        ISysMLParseResult<?> result = this.parse(lexer, parser -> {
            parser.rootNamespace();
            if (parser.getCurrentToken().getType() != Token.EOF) {
                parser.notifyErrorListeners("Unexpected trailing input '" + parser.getCurrentToken().getText() + "'");
            }
        });
        Namespace root = result.model() instanceof Namespace namespace ? namespace : null;
        return new SysMLParseResult<>(root, result.danglingLinks(), result.textAdapted(), result.diagnostic(), ((SysMLParseResult<?>) result).internalDiagnostic());
    }

    /**
     * Parses using the <code>identification</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Element> parseIdentification(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.identification());
    }

    /**
     * Parses using the <code>relationshipBody</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Relationship> parseRelationshipBody(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.relationshipBody());
    }

    /**
     * Parses using the <code>dependency</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Dependency> parseDependency(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.dependency());
    }

    /**
     * Parses using the <code>annotation</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Annotation> parseAnnotation(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.annotation());
    }

    /**
     * Parses using the <code>ownedAnnotation</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Annotation> parseOwnedAnnotation(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.ownedAnnotation());
    }

    /**
     * Parses using the <code>annotatingMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<OwningMembership> parseAnnotatingMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.annotatingMember());
    }

    /**
     * Parses using the <code>annotatingElement</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<AnnotatingElement> parseAnnotatingElement(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.annotatingElement());
    }

    /**
     * Parses using the <code>comment</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Comment> parseComment(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.comment());
    }

    /**
     * Parses using the <code>documentation</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Documentation> parseDocumentation(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.documentation());
    }

    /**
     * Parses using the <code>textualRepresentation</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<TextualRepresentation> parseTextualRepresentation(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.textualRepresentation());
    }

    /**
     * Parses using the <code>metadataKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseMetadataKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.metadataKeyword());
    }

    /**
     * Parses using the <code>metadataDefKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseMetadataDefKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.metadataDefKeyword());
    }

    /**
     * Parses using the <code>metadataUsageKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseMetadataUsageKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.metadataUsageKeyword());
    }

    /**
     * Parses using the <code>metadataDefinition</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<MetadataDefinition> parseMetadataDefinition(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.metadataDefinition());
    }

    /**
     * Parses using the <code>prefixMetadataAnnotation</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Annotation> parsePrefixMetadataAnnotation(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.prefixMetadataAnnotation());
    }

    /**
     * Parses using the <code>prefixMetadataMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<OwningMembership> parsePrefixMetadataMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.prefixMetadataMember());
    }

    /**
     * Parses using the <code>prefixMetadataUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<MetadataUsage> parsePrefixMetadataUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.prefixMetadataUsage());
    }

    /**
     * Parses using the <code>metadataUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<MetadataUsage> parseMetadataUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.metadataUsage());
    }

    /**
     * Parses using the <code>metadataUsageDeclaration</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<MetadataUsage> parseMetadataUsageDeclaration(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.metadataUsageDeclaration());
    }

    /**
     * Parses using the <code>metadataTyping</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FeatureTyping> parseMetadataTyping(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.metadataTyping());
    }

    /**
     * Parses using the <code>metadataBody</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Usage> parseMetadataBody(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.metadataBody());
    }

    /**
     * Parses using the <code>metadataBodyUsageMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FeatureMembership> parseMetadataBodyUsageMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.metadataBodyUsageMember());
    }

    /**
     * Parses using the <code>metadataBodyUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ReferenceUsage> parseMetadataBodyUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.metadataBodyUsage());
    }

    /**
     * Parses using the <code>package_</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Package> parsePackage_(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.package_());
    }

    /**
     * Parses using the <code>libraryPackage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<LibraryPackage> parseLibraryPackage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.libraryPackage());
    }

    /**
     * Parses using the <code>packageDeclaration</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Package> parsePackageDeclaration(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.packageDeclaration());
    }

    /**
     * Parses using the <code>packageBody</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Package> parsePackageBody(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.packageBody());
    }

    /**
     * Parses using the <code>packageBodyElement</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Namespace> parsePackageBodyElement(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.packageBodyElement());
    }

    /**
     * Parses using the <code>memberPrefix</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Membership> parseMemberPrefix(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.memberPrefix());
    }

    /**
     * Parses using the <code>packageMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<OwningMembership> parsePackageMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.packageMember());
    }

    /**
     * Parses using the <code>elementFilterMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ElementFilterMembership> parseElementFilterMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.elementFilterMember());
    }

    /**
     * Parses using the <code>aliasMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Membership> parseAliasMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.aliasMember());
    }

    /**
     * Parses using the <code>importPrefix</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Import> parseImportPrefix(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.importPrefix());
    }

    /**
     * Parses using the <code>import_</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Import> parseImport_(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.import_());
    }

    /**
     * Parses using the <code>membershipImport</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<MembershipImport> parseMembershipImport(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.membershipImport());
    }

    /**
     * Parses using the <code>importedMembership</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<MembershipImport> parseImportedMembership(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.importedMembership());
    }

    /**
     * Parses using the <code>namespaceImport</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<NamespaceImport> parseNamespaceImport(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.namespaceImport());
    }

    /**
     * Parses using the <code>importedNamespace</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<NamespaceImport> parseImportedNamespace(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.importedNamespace());
    }

    /**
     * Parses using the <code>filterPackage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Package> parseFilterPackage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.filterPackage());
    }

    /**
     * Parses using the <code>filterPackageImport</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Import> parseFilterPackageImport(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.filterPackageImport());
    }

    /**
     * Parses using the <code>filterPackageMembershipImport</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<MembershipImport> parseFilterPackageMembershipImport(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.filterPackageMembershipImport());
    }

    /**
     * Parses using the <code>filterPackageNamespaceImport</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<NamespaceImport> parseFilterPackageNamespaceImport(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.filterPackageNamespaceImport());
    }

    /**
     * Parses using the <code>filterPackageMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ElementFilterMembership> parseFilterPackageMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.filterPackageMember());
    }

    /**
     * Parses using the <code>filterPackageMemberVisibility</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<VisibilityKind> parseFilterPackageMemberVisibility(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.filterPackageMemberVisibility());
    }

    /**
     * Parses using the <code>visibilityIndicator</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<VisibilityKind> parseVisibilityIndicator(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.visibilityIndicator());
    }

    /**
     * Parses using the <code>definitionElement</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Element> parseDefinitionElement(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.definitionElement());
    }

    /**
     * Parses using the <code>usageElement</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Usage> parseUsageElement(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.usageElement());
    }

    /**
     * Parses using the <code>subclassificationPart</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Classifier> parseSubclassificationPart(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.subclassificationPart());
    }

    /**
     * Parses using the <code>specializesKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseSpecializesKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.specializesKeyword());
    }

    /**
     * Parses using the <code>ownedSubclassification</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Subclassification> parseOwnedSubclassification(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.ownedSubclassification());
    }

    /**
     * Parses using the <code>featureDeclaration</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Feature> parseFeatureDeclaration(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.featureDeclaration());
    }

    /**
     * Parses using the <code>featureSpecializationPart</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Feature> parseFeatureSpecializationPart(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.featureSpecializationPart());
    }

    /**
     * Parses using the <code>multiplicityPart</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Feature> parseMultiplicityPart(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.multiplicityPart());
    }

    /**
     * Parses using the <code>featureSpecialization</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Feature> parseFeatureSpecialization(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.featureSpecialization());
    }

    /**
     * Parses using the <code>typings</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Feature> parseTypings(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.typings());
    }

    /**
     * Parses using the <code>typedBy</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Feature> parseTypedBy(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.typedBy());
    }

    /**
     * Parses using the <code>definedByKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseDefinedByKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.definedByKeyword());
    }

    /**
     * Parses using the <code>subsettings</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Feature> parseSubsettings(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.subsettings());
    }

    /**
     * Parses using the <code>subsets</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Feature> parseSubsets(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.subsets());
    }

    /**
     * Parses using the <code>subsetsKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseSubsetsKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.subsetsKeyword());
    }

    /**
     * Parses using the <code>references</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Feature> parseReferences(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.references());
    }

    /**
     * Parses using the <code>referencesKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseReferencesKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.referencesKeyword());
    }

    /**
     * Parses using the <code>crosses</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Feature> parseCrosses(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.crosses());
    }

    /**
     * Parses using the <code>crossesKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseCrossesKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.crossesKeyword());
    }

    /**
     * Parses using the <code>redefines</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Feature> parseRedefines(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.redefines());
    }

    /**
     * Parses using the <code>redefinesKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseRedefinesKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.redefinesKeyword());
    }

    /**
     * Parses using the <code>featureTyping</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FeatureTyping> parseFeatureTyping(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.featureTyping());
    }

    /**
     * Parses using the <code>ownedFeatureTyping</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FeatureTyping> parseOwnedFeatureTyping(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.ownedFeatureTyping());
    }

    /**
     * Parses using the <code>ownedSubsetting</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Subsetting> parseOwnedSubsetting(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.ownedSubsetting());
    }

    /**
     * Parses using the <code>ownedReferenceSubsetting</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ReferenceSubsetting> parseOwnedReferenceSubsetting(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.ownedReferenceSubsetting());
    }

    /**
     * Parses using the <code>ownedCrossSubsetting</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<CrossSubsetting> parseOwnedCrossSubsetting(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.ownedCrossSubsetting());
    }

    /**
     * Parses using the <code>redefinitions</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Feature> parseRedefinitions(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.redefinitions());
    }

    /**
     * Parses using the <code>ownedRedefinition</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Redefinition> parseOwnedRedefinition(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.ownedRedefinition());
    }

    /**
     * Parses using the <code>ownedMultiplicity</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<OwningMembership> parseOwnedMultiplicity(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.ownedMultiplicity());
    }

    /**
     * Parses using the <code>multiplicityRange</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<MultiplicityRange> parseMultiplicityRange(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.multiplicityRange());
    }

    /**
     * Parses using the <code>multiplicityExpressionMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<OwningMembership> parseMultiplicityExpressionMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.multiplicityExpressionMember());
    }

    /**
     * Parses using the <code>basicDefinitionPrefix</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Definition> parseBasicDefinitionPrefix(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.basicDefinitionPrefix());
    }

    /**
     * Parses using the <code>definitionExtensionKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Definition> parseDefinitionExtensionKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.definitionExtensionKeyword());
    }

    /**
     * Parses using the <code>definitionPrefix</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Definition> parseDefinitionPrefix(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.definitionPrefix());
    }

    /**
     * Parses using the <code>definition</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Definition> parseDefinition(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.definition());
    }

    /**
     * Parses using the <code>definitionDeclaration</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Definition> parseDefinitionDeclaration(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.definitionDeclaration());
    }

    /**
     * Parses using the <code>definitionBody</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Type> parseDefinitionBody(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.definitionBody());
    }

    /**
     * Parses using the <code>definitionBodyItem</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Type> parseDefinitionBodyItem(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.definitionBodyItem());
    }

    /**
     * Parses using the <code>definitionMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<OwningMembership> parseDefinitionMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.definitionMember());
    }

    /**
     * Parses using the <code>variantUsageMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<VariantMembership> parseVariantUsageMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.variantUsageMember());
    }

    /**
     * Parses using the <code>nonOccurrenceUsageMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FeatureMembership> parseNonOccurrenceUsageMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.nonOccurrenceUsageMember());
    }

    /**
     * Parses using the <code>occurrenceUsageMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FeatureMembership> parseOccurrenceUsageMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.occurrenceUsageMember());
    }

    /**
     * Parses using the <code>structureUsageMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FeatureMembership> parseStructureUsageMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.structureUsageMember());
    }

    /**
     * Parses using the <code>behaviorUsageMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FeatureMembership> parseBehaviorUsageMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.behaviorUsageMember());
    }

    /**
     * Parses using the <code>featureDirection</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FeatureDirectionKind> parseFeatureDirection(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.featureDirection());
    }

    /**
     * Parses using the <code>refPrefix</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Usage> parseRefPrefix(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.refPrefix());
    }

    /**
     * Parses using the <code>basicUsagePrefix</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Usage> parseBasicUsagePrefix(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.basicUsagePrefix());
    }

    /**
     * Parses using the <code>endUsagePrefix</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Usage> parseEndUsagePrefix(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.endUsagePrefix());
    }

    /**
     * Parses using the <code>unextendedUsagePrefix</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Usage> parseUnextendedUsagePrefix(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.unextendedUsagePrefix());
    }

    /**
     * Parses using the <code>usageExtensionKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Usage> parseUsageExtensionKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.usageExtensionKeyword());
    }

    /**
     * Parses using the <code>usagePrefix</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Usage> parseUsagePrefix(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.usagePrefix());
    }

    /**
     * Parses using the <code>ownedCrossFeatureMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<OwningMembership> parseOwnedCrossFeatureMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.ownedCrossFeatureMember());
    }

    /**
     * Parses using the <code>ownedCrossFeature</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ReferenceUsage> parseOwnedCrossFeature(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.ownedCrossFeature());
    }

    /**
     * Parses using the <code>usage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Usage> parseUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.usage());
    }

    /**
     * Parses using the <code>usageDeclaration</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Usage> parseUsageDeclaration(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.usageDeclaration());
    }

    /**
     * Parses using the <code>usageCompletion</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Usage> parseUsageCompletion(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.usageCompletion());
    }

    /**
     * Parses using the <code>usageBody</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Usage> parseUsageBody(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.usageBody());
    }

    /**
     * Parses using the <code>valuePart</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Feature> parseValuePart(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.valuePart());
    }

    /**
     * Parses using the <code>featureValue</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FeatureValue> parseFeatureValue(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.featureValue());
    }

    /**
     * Parses using the <code>referenceKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseReferenceKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.referenceKeyword());
    }

    /**
     * Parses using the <code>referenceUsageKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseReferenceUsageKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.referenceUsageKeyword());
    }

    /**
     * Parses using the <code>defaultReferenceUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ReferenceUsage> parseDefaultReferenceUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.defaultReferenceUsage());
    }

    /**
     * Parses using the <code>referenceUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ReferenceUsage> parseReferenceUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.referenceUsage());
    }

    /**
     * Parses using the <code>variantReference</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ReferenceUsage> parseVariantReference(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.variantReference());
    }

    /**
     * Parses using the <code>nonOccurrenceUsageElement</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Usage> parseNonOccurrenceUsageElement(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.nonOccurrenceUsageElement());
    }

    /**
     * Parses using the <code>occurrenceUsageElement</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Usage> parseOccurrenceUsageElement(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.occurrenceUsageElement());
    }

    /**
     * Parses using the <code>structureUsageElement</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Usage> parseStructureUsageElement(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.structureUsageElement());
    }

    /**
     * Parses using the <code>behaviorUsageElement</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Usage> parseBehaviorUsageElement(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.behaviorUsageElement());
    }

    /**
     * Parses using the <code>variantUsageElement</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Usage> parseVariantUsageElement(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.variantUsageElement());
    }

    /**
     * Parses using the <code>extendedDefinition</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Definition> parseExtendedDefinition(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.extendedDefinition());
    }

    /**
     * Parses using the <code>extendedUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Usage> parseExtendedUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.extendedUsage());
    }

    /**
     * Parses using the <code>attributeKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseAttributeKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.attributeKeyword());
    }

    /**
     * Parses using the <code>attributeDefKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseAttributeDefKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.attributeDefKeyword());
    }

    /**
     * Parses using the <code>attributeUsageKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseAttributeUsageKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.attributeUsageKeyword());
    }

    /**
     * Parses using the <code>attributeDefinition</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<AttributeDefinition> parseAttributeDefinition(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.attributeDefinition());
    }

    /**
     * Parses using the <code>attributeUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<AttributeUsage> parseAttributeUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.attributeUsage());
    }

    /**
     * Parses using the <code>enumerationKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseEnumerationKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.enumerationKeyword());
    }

    /**
     * Parses using the <code>enumerationDefKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseEnumerationDefKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.enumerationDefKeyword());
    }

    /**
     * Parses using the <code>enumerationUsageKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseEnumerationUsageKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.enumerationUsageKeyword());
    }

    /**
     * Parses using the <code>enumerationDefinition</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<EnumerationDefinition> parseEnumerationDefinition(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.enumerationDefinition());
    }

    /**
     * Parses using the <code>enumerationBody</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<EnumerationDefinition> parseEnumerationBody(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.enumerationBody());
    }

    /**
     * Parses using the <code>enumerationUsageMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<VariantMembership> parseEnumerationUsageMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.enumerationUsageMember());
    }

    /**
     * Parses using the <code>enumeratedValue</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<EnumerationUsage> parseEnumeratedValue(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.enumeratedValue());
    }

    /**
     * Parses using the <code>enumerationUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<EnumerationUsage> parseEnumerationUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.enumerationUsage());
    }

    /**
     * Parses using the <code>occurrenceKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseOccurrenceKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.occurrenceKeyword());
    }

    /**
     * Parses using the <code>occurrenceDefKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseOccurrenceDefKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.occurrenceDefKeyword());
    }

    /**
     * Parses using the <code>occurrenceDefinitionPrefix</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<OccurrenceDefinition> parseOccurrenceDefinitionPrefix(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.occurrenceDefinitionPrefix());
    }

    /**
     * Parses using the <code>occurrenceDefinition</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<OccurrenceDefinition> parseOccurrenceDefinition(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.occurrenceDefinition());
    }

    /**
     * Parses using the <code>individualDefinition</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<OccurrenceDefinition> parseIndividualDefinition(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.individualDefinition());
    }

    /**
     * Parses using the <code>emptyMultiplicityMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<OwningMembership> parseEmptyMultiplicityMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.emptyMultiplicityMember());
    }

    /**
     * Parses using the <code>emptyMultiplicity</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Multiplicity> parseEmptyMultiplicity(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.emptyMultiplicity());
    }

    /**
     * Parses using the <code>occurrenceUsageKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseOccurrenceUsageKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.occurrenceUsageKeyword());
    }

    /**
     * Parses using the <code>occurrenceUsagePrefix</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<OccurrenceUsage> parseOccurrenceUsagePrefix(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.occurrenceUsagePrefix());
    }

    /**
     * Parses using the <code>occurrenceUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<OccurrenceUsage> parseOccurrenceUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.occurrenceUsage());
    }

    /**
     * Parses using the <code>individualUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<OccurrenceUsage> parseIndividualUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.individualUsage());
    }

    /**
     * Parses using the <code>portionUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<OccurrenceUsage> parsePortionUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.portionUsage());
    }

    /**
     * Parses using the <code>portionKind</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<PortionKind> parsePortionKind(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.portionKind());
    }

    /**
     * Parses using the <code>eventOccurrenceUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<EventOccurrenceUsage> parseEventOccurrenceUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.eventOccurrenceUsage());
    }

    /**
     * Parses using the <code>emptySuccessionMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FeatureMembership> parseEmptySuccessionMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.emptySuccessionMember());
    }

    /**
     * Parses using the <code>emptySuccession</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<SuccessionAsUsage> parseEmptySuccession(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.emptySuccession());
    }

    /**
     * Parses using the <code>multiplicitySourceEndMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<EndFeatureMembership> parseMultiplicitySourceEndMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.multiplicitySourceEndMember());
    }

    /**
     * Parses using the <code>multiplicitySourceEnd</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ReferenceUsage> parseMultiplicitySourceEnd(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.multiplicitySourceEnd());
    }

    /**
     * Parses using the <code>emptyTargetEndMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<EndFeatureMembership> parseEmptyTargetEndMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.emptyTargetEndMember());
    }

    /**
     * Parses using the <code>emptyTargetEnd</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ReferenceUsage> parseEmptyTargetEnd(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.emptyTargetEnd());
    }

    /**
     * Parses using the <code>itemKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseItemKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.itemKeyword());
    }

    /**
     * Parses using the <code>itemDefKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseItemDefKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.itemDefKeyword());
    }

    /**
     * Parses using the <code>itemUsageKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseItemUsageKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.itemUsageKeyword());
    }

    /**
     * Parses using the <code>itemDefinition</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ItemDefinition> parseItemDefinition(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.itemDefinition());
    }

    /**
     * Parses using the <code>itemUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ItemUsage> parseItemUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.itemUsage());
    }

    /**
     * Parses using the <code>partKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parsePartKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.partKeyword());
    }

    /**
     * Parses using the <code>partDefKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parsePartDefKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.partDefKeyword());
    }

    /**
     * Parses using the <code>partUsageKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parsePartUsageKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.partUsageKeyword());
    }

    /**
     * Parses using the <code>partDefinition</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<PartDefinition> parsePartDefinition(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.partDefinition());
    }

    /**
     * Parses using the <code>partUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<PartUsage> parsePartUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.partUsage());
    }

    /**
     * Parses using the <code>portKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parsePortKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.portKeyword());
    }

    /**
     * Parses using the <code>portDefKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parsePortDefKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.portDefKeyword());
    }

    /**
     * Parses using the <code>portDefinition</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<PortDefinition> parsePortDefinition(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.portDefinition());
    }

    /**
     * Parses using the <code>conjugatedPortDefinitionMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<OwningMembership> parseConjugatedPortDefinitionMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.conjugatedPortDefinitionMember());
    }

    /**
     * Parses using the <code>conjugatedPortDefinition</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ConjugatedPortDefinition> parseConjugatedPortDefinition(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.conjugatedPortDefinition());
    }

    /**
     * Parses using the <code>portConjugation</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<PortConjugation> parsePortConjugation(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.portConjugation());
    }

    /**
     * Parses using the <code>conjugatedPortTyping</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ConjugatedPortTyping> parseConjugatedPortTyping(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.conjugatedPortTyping());
    }

    /**
     * Parses using the <code>conjugatedQualifiedName</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseConjugatedQualifiedName(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.conjugatedQualifiedName());
    }

    /**
     * Parses using the <code>portUsageKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parsePortUsageKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.portUsageKeyword());
    }

    /**
     * Parses using the <code>portUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<PortUsage> parsePortUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.portUsage());
    }

    /**
     * Parses using the <code>connectorEndMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<EndFeatureMembership> parseConnectorEndMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.connectorEndMember());
    }

    /**
     * Parses using the <code>connectorEnd</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ReferenceUsage> parseConnectorEnd(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.connectorEnd());
    }

    /**
     * Parses using the <code>ownedCrossMultiplicityMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<OwningMembership> parseOwnedCrossMultiplicityMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.ownedCrossMultiplicityMember());
    }

    /**
     * Parses using the <code>ownedCrossMultiplicity</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Feature> parseOwnedCrossMultiplicity(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.ownedCrossMultiplicity());
    }

    /**
     * Parses using the <code>bindingKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseBindingKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.bindingKeyword());
    }

    /**
     * Parses using the <code>bindingConnectorAsUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<BindingConnectorAsUsage> parseBindingConnectorAsUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.bindingConnectorAsUsage());
    }

    /**
     * Parses using the <code>successionKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseSuccessionKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.successionKeyword());
    }

    /**
     * Parses using the <code>successionAsUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<SuccessionAsUsage> parseSuccessionAsUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.successionAsUsage());
    }

    /**
     * Parses using the <code>connectionKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseConnectionKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.connectionKeyword());
    }

    /**
     * Parses using the <code>connectionDefKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseConnectionDefKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.connectionDefKeyword());
    }

    /**
     * Parses using the <code>connectionDefinition</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ConnectionDefinition> parseConnectionDefinition(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.connectionDefinition());
    }

    /**
     * Parses using the <code>connectorKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseConnectorKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.connectorKeyword());
    }

    /**
     * Parses using the <code>connectionUsageKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseConnectionUsageKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.connectionUsageKeyword());
    }

    /**
     * Parses using the <code>connectionUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ConnectionUsage> parseConnectionUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.connectionUsage());
    }

    /**
     * Parses using the <code>connectorPart</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ConnectionUsage> parseConnectorPart(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.connectorPart());
    }

    /**
     * Parses using the <code>binaryConnectorPart</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Connector> parseBinaryConnectorPart(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.binaryConnectorPart());
    }

    /**
     * Parses using the <code>naryConnectorPart</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Connector> parseNaryConnectorPart(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.naryConnectorPart());
    }

    /**
     * Parses using the <code>emptySourceEndMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<EndFeatureMembership> parseEmptySourceEndMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.emptySourceEndMember());
    }

    /**
     * Parses using the <code>emptySourceEnd</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ReferenceUsage> parseEmptySourceEnd(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.emptySourceEnd());
    }

    /**
     * Parses using the <code>interfaceKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseInterfaceKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.interfaceKeyword());
    }

    /**
     * Parses using the <code>interfaceDefKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseInterfaceDefKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.interfaceDefKeyword());
    }

    /**
     * Parses using the <code>interfaceDefinition</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<InterfaceDefinition> parseInterfaceDefinition(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.interfaceDefinition());
    }

    /**
     * Parses using the <code>interfaceBody</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Type> parseInterfaceBody(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.interfaceBody());
    }

    /**
     * Parses using the <code>interfaceBodyItem</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Type> parseInterfaceBodyItem(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.interfaceBodyItem());
    }

    /**
     * Parses using the <code>interfaceNonOccurrenceUsageMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FeatureMembership> parseInterfaceNonOccurrenceUsageMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.interfaceNonOccurrenceUsageMember());
    }

    /**
     * Parses using the <code>interfaceNonOccurrenceUsageElement</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Usage> parseInterfaceNonOccurrenceUsageElement(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.interfaceNonOccurrenceUsageElement());
    }

    /**
     * Parses using the <code>interfaceOccurrenceUsageMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FeatureMembership> parseInterfaceOccurrenceUsageMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.interfaceOccurrenceUsageMember());
    }

    /**
     * Parses using the <code>interfaceOccurrenceUsageElement</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Usage> parseInterfaceOccurrenceUsageElement(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.interfaceOccurrenceUsageElement());
    }

    /**
     * Parses using the <code>defaultInterfaceEnd</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<PortUsage> parseDefaultInterfaceEnd(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.defaultInterfaceEnd());
    }

    /**
     * Parses using the <code>interfaceUsageKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseInterfaceUsageKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.interfaceUsageKeyword());
    }

    /**
     * Parses using the <code>interfaceUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<InterfaceUsage> parseInterfaceUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.interfaceUsage());
    }

    /**
     * Parses using the <code>interfaceUsageDeclaration</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<InterfaceUsage> parseInterfaceUsageDeclaration(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.interfaceUsageDeclaration());
    }

    /**
     * Parses using the <code>interfacePart</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ConnectionUsage> parseInterfacePart(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.interfacePart());
    }

    /**
     * Parses using the <code>binaryInterfacePart</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Connector> parseBinaryInterfacePart(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.binaryInterfacePart());
    }

    /**
     * Parses using the <code>naryInterfacePart</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Connector> parseNaryInterfacePart(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.naryInterfacePart());
    }

    /**
     * Parses using the <code>interfaceEndMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<EndFeatureMembership> parseInterfaceEndMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.interfaceEndMember());
    }

    /**
     * Parses using the <code>interfaceEnd</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<PortUsage> parseInterfaceEnd(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.interfaceEnd());
    }

    /**
     * Parses using the <code>allocationKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseAllocationKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.allocationKeyword());
    }

    /**
     * Parses using the <code>allocationDefKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseAllocationDefKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.allocationDefKeyword());
    }

    /**
     * Parses using the <code>allocationDefinition</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<AllocationDefinition> parseAllocationDefinition(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.allocationDefinition());
    }

    /**
     * Parses using the <code>allocationUsageKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseAllocationUsageKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.allocationUsageKeyword());
    }

    /**
     * Parses using the <code>allocateKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseAllocateKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.allocateKeyword());
    }

    /**
     * Parses using the <code>allocationUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<AllocationUsage> parseAllocationUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.allocationUsage());
    }

    /**
     * Parses using the <code>allocationUsageDeclaration</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<AllocationUsage> parseAllocationUsageDeclaration(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.allocationUsageDeclaration());
    }

    /**
     * Parses using the <code>flowKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseFlowKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.flowKeyword());
    }

    /**
     * Parses using the <code>flowDefKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseFlowDefKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.flowDefKeyword());
    }

    /**
     * Parses using the <code>flowDefinition</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FlowDefinition> parseFlowDefinition(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.flowDefinition());
    }

    /**
     * Parses using the <code>messageKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseMessageKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.messageKeyword());
    }

    /**
     * Parses using the <code>message</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FlowUsage> parseMessage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.message());
    }

    /**
     * Parses using the <code>messageDeclaration</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FlowUsage> parseMessageDeclaration(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.messageDeclaration());
    }

    /**
     * Parses using the <code>messageEventMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ParameterMembership> parseMessageEventMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.messageEventMember());
    }

    /**
     * Parses using the <code>messageEvent</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<EventOccurrenceUsage> parseMessageEvent(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.messageEvent());
    }

    /**
     * Parses using the <code>flowUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FlowUsage> parseFlowUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.flowUsage());
    }

    /**
     * Parses using the <code>successionFlowKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseSuccessionFlowKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.successionFlowKeyword());
    }

    /**
     * Parses using the <code>successionFlowUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<SuccessionFlowUsage> parseSuccessionFlowUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.successionFlowUsage());
    }

    /**
     * Parses using the <code>flowDeclaration</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FlowUsage> parseFlowDeclaration(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.flowDeclaration());
    }

    /**
     * Parses using the <code>payloadFeatureMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FeatureMembership> parsePayloadFeatureMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.payloadFeatureMember());
    }

    /**
     * Parses using the <code>payloadFeature</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<PayloadFeature> parsePayloadFeature(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.payloadFeature());
    }

    /**
     * Parses using the <code>payload</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Feature> parsePayload(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.payload());
    }

    /**
     * Parses using the <code>payloadFeatureSpecializationPart</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Feature> parsePayloadFeatureSpecializationPart(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.payloadFeatureSpecializationPart());
    }

    /**
     * Parses using the <code>flowEndMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<EndFeatureMembership> parseFlowEndMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.flowEndMember());
    }

    /**
     * Parses using the <code>flowEnd</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FlowEnd> parseFlowEnd(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.flowEnd());
    }

    /**
     * Parses using the <code>flowEndSubsetting</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ReferenceSubsetting> parseFlowEndSubsetting(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.flowEndSubsetting());
    }

    /**
     * Parses using the <code>featureChainPrefix</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Feature> parseFeatureChainPrefix(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.featureChainPrefix());
    }

    /**
     * Parses using the <code>flowFeatureMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FeatureMembership> parseFlowFeatureMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.flowFeatureMember());
    }

    /**
     * Parses using the <code>flowFeature</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ReferenceUsage> parseFlowFeature(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.flowFeature());
    }

    /**
     * Parses using the <code>flowRedefinition</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Redefinition> parseFlowRedefinition(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.flowRedefinition());
    }

    /**
     * Parses using the <code>actionKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseActionKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.actionKeyword());
    }

    /**
     * Parses using the <code>actionDefKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseActionDefKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.actionDefKeyword());
    }

    /**
     * Parses using the <code>actionDefinition</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ActionDefinition> parseActionDefinition(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.actionDefinition());
    }

    /**
     * Parses using the <code>actionBody</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Type> parseActionBody(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.actionBody());
    }

    /**
     * Parses using the <code>actionBodyItem</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Type> parseActionBodyItem(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.actionBodyItem());
    }

    /**
     * Parses using the <code>initialNodeMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Membership> parseInitialNodeMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.initialNodeMember());
    }

    /**
     * Parses using the <code>actionNodeMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FeatureMembership> parseActionNodeMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.actionNodeMember());
    }

    /**
     * Parses using the <code>targetSuccessionMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FeatureMembership> parseTargetSuccessionMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.targetSuccessionMember());
    }

    /**
     * Parses using the <code>guardedSuccessionMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FeatureMembership> parseGuardedSuccessionMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.guardedSuccessionMember());
    }

    /**
     * Parses using the <code>actionUsageKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseActionUsageKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.actionUsageKeyword());
    }

    /**
     * Parses using the <code>actionUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ActionUsage> parseActionUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.actionUsage());
    }

    /**
     * Parses using the <code>performActionUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<PerformActionUsage> parsePerformActionUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.performActionUsage());
    }

    /**
     * Parses using the <code>performActionUsageDeclaration</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ActionUsage> parsePerformActionUsageDeclaration(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.performActionUsageDeclaration());
    }

    /**
     * Parses using the <code>actionUsageDeclaration</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ActionUsage> parseActionUsageDeclaration(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.actionUsageDeclaration());
    }

    /**
     * Parses using the <code>actionNode</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ActionUsage> parseActionNode(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.actionNode());
    }

    /**
     * Parses using the <code>actionNodeUsageDeclaration</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ActionUsage> parseActionNodeUsageDeclaration(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.actionNodeUsageDeclaration());
    }

    /**
     * Parses using the <code>actionNodePrefix</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ActionUsage> parseActionNodePrefix(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.actionNodePrefix());
    }

    /**
     * Parses using the <code>acceptNode</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<AcceptActionUsage> parseAcceptNode(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.acceptNode());
    }

    /**
     * Parses using the <code>acceptNodeDeclaration</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ActionUsage> parseAcceptNodeDeclaration(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.acceptNodeDeclaration());
    }

    /**
     * Parses using the <code>acceptParameterPart</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ActionUsage> parseAcceptParameterPart(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.acceptParameterPart());
    }

    /**
     * Parses using the <code>payloadParameterMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ParameterMembership> parsePayloadParameterMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.payloadParameterMember());
    }

    /**
     * Parses using the <code>payloadParameter</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ReferenceUsage> parsePayloadParameter(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.payloadParameter());
    }

    /**
     * Parses using the <code>triggerValuePart</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Feature> parseTriggerValuePart(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.triggerValuePart());
    }

    /**
     * Parses using the <code>triggerFeatureValue</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FeatureValue> parseTriggerFeatureValue(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.triggerFeatureValue());
    }

    /**
     * Parses using the <code>triggerExpression</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<TriggerInvocationExpression> parseTriggerExpression(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.triggerExpression());
    }

    /**
     * Parses using the <code>timeTriggerKind</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<TriggerKind> parseTimeTriggerKind(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.timeTriggerKind());
    }

    /**
     * Parses using the <code>changeTriggerKind</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<TriggerKind> parseChangeTriggerKind(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.changeTriggerKind());
    }

    /**
     * Parses using the <code>argumentExpressionMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ParameterMembership> parseArgumentExpressionMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.argumentExpressionMember());
    }

    /**
     * Parses using the <code>argumentExpression</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Feature> parseArgumentExpression(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.argumentExpression());
    }

    /**
     * Parses using the <code>argumentExpressionValue</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FeatureValue> parseArgumentExpressionValue(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.argumentExpressionValue());
    }

    /**
     * Parses using the <code>sendNode</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<SendActionUsage> parseSendNode(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.sendNode());
    }

    /**
     * Parses using the <code>sendNodeDeclaration</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ActionUsage> parseSendNodeDeclaration(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.sendNodeDeclaration());
    }

    /**
     * Parses using the <code>senderReceiverPart</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ActionUsage> parseSenderReceiverPart(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.senderReceiverPart());
    }

    /**
     * Parses using the <code>nodeParameterMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ParameterMembership> parseNodeParameterMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.nodeParameterMember());
    }

    /**
     * Parses using the <code>nodeParameter</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ReferenceUsage> parseNodeParameter(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.nodeParameter());
    }

    /**
     * Parses using the <code>featureBinding</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FeatureValue> parseFeatureBinding(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.featureBinding());
    }

    /**
     * Parses using the <code>assignmentNode</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<AssignmentActionUsage> parseAssignmentNode(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.assignmentNode());
    }

    /**
     * Parses using the <code>assignmentNodeDeclaration</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ActionUsage> parseAssignmentNodeDeclaration(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.assignmentNodeDeclaration());
    }

    /**
     * Parses using the <code>assignmentTargetMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ParameterMembership> parseAssignmentTargetMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.assignmentTargetMember());
    }

    /**
     * Parses using the <code>targetParameter</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ReferenceUsage> parseTargetParameter(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.targetParameter());
    }

    /**
     * Parses using the <code>targetFeatureMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FeatureMembership> parseTargetFeatureMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.targetFeatureMember());
    }

    /**
     * Parses using the <code>targetFeature</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ReferenceUsage> parseTargetFeature(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.targetFeature());
    }

    /**
     * Parses using the <code>targetAccessedFeatureMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FeatureMembership> parseTargetAccessedFeatureMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.targetAccessedFeatureMember());
    }

    /**
     * Parses using the <code>targetBinding</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FeatureValue> parseTargetBinding(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.targetBinding());
    }

    /**
     * Parses using the <code>targetExpression</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Expression> parseTargetExpression(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.targetExpression());
    }

    /**
     * Parses using the <code>expressionParameterMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ParameterMembership> parseExpressionParameterMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.expressionParameterMember());
    }

    /**
     * Parses using the <code>ifNode</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<IfActionUsage> parseIfNode(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.ifNode());
    }

    /**
     * Parses using the <code>actionBodyParameterMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ParameterMembership> parseActionBodyParameterMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.actionBodyParameterMember());
    }

    /**
     * Parses using the <code>actionBodyParameter</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ActionUsage> parseActionBodyParameter(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.actionBodyParameter());
    }

    /**
     * Parses using the <code>ifNodeParameterMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ParameterMembership> parseIfNodeParameterMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.ifNodeParameterMember());
    }

    /**
     * Parses using the <code>whileLoopNode</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<WhileLoopActionUsage> parseWhileLoopNode(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.whileLoopNode());
    }

    /**
     * Parses using the <code>forLoopNode</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ForLoopActionUsage> parseForLoopNode(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.forLoopNode());
    }

    /**
     * Parses using the <code>forVariableDeclarationMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FeatureMembership> parseForVariableDeclarationMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.forVariableDeclarationMember());
    }

    /**
     * Parses using the <code>forVariableDeclaration</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ReferenceUsage> parseForVariableDeclaration(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.forVariableDeclaration());
    }

    /**
     * Parses using the <code>terminateNode</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<TerminateActionUsage> parseTerminateNode(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.terminateNode());
    }

    /**
     * Parses using the <code>controlNode</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ControlNode> parseControlNode(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.controlNode());
    }

    /**
     * Parses using the <code>controlNodePrefix</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<OccurrenceUsage> parseControlNodePrefix(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.controlNodePrefix());
    }

    /**
     * Parses using the <code>mergeNode</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<MergeNode> parseMergeNode(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.mergeNode());
    }

    /**
     * Parses using the <code>decisionNode</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<DecisionNode> parseDecisionNode(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.decisionNode());
    }

    /**
     * Parses using the <code>joinNode</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<JoinNode> parseJoinNode(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.joinNode());
    }

    /**
     * Parses using the <code>forkNode</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ForkNode> parseForkNode(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.forkNode());
    }

    /**
     * Parses using the <code>emptyParameterMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ParameterMembership> parseEmptyParameterMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.emptyParameterMember());
    }

    /**
     * Parses using the <code>emptyUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ReferenceUsage> parseEmptyUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.emptyUsage());
    }

    /**
     * Parses using the <code>actionTargetSuccession</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Usage> parseActionTargetSuccession(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.actionTargetSuccession());
    }

    /**
     * Parses using the <code>targetSuccession</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<SuccessionAsUsage> parseTargetSuccession(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.targetSuccession());
    }

    /**
     * Parses using the <code>guardedTargetSuccession</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<TransitionUsage> parseGuardedTargetSuccession(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.guardedTargetSuccession());
    }

    /**
     * Parses using the <code>defaultTargetSuccession</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<TransitionUsage> parseDefaultTargetSuccession(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.defaultTargetSuccession());
    }

    /**
     * Parses using the <code>guardedSuccession</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<TransitionUsage> parseGuardedSuccession(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.guardedSuccession());
    }

    /**
     * Parses using the <code>stateKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseStateKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.stateKeyword());
    }

    /**
     * Parses using the <code>stateDefKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseStateDefKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.stateDefKeyword());
    }

    /**
     * Parses using the <code>stateDefinition</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<StateDefinition> parseStateDefinition(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.stateDefinition());
    }

    /**
     * Parses using the <code>stateDefBody</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<StateDefinition> parseStateDefBody(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.stateDefBody());
    }

    /**
     * Parses using the <code>stateBodyPart</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Type> parseStateBodyPart(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.stateBodyPart());
    }

    /**
     * Parses using the <code>stateBodyItem</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Type> parseStateBodyItem(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.stateBodyItem());
    }

    /**
     * Parses using the <code>entryActionMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<StateSubactionMembership> parseEntryActionMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.entryActionMember());
    }

    /**
     * Parses using the <code>entryActionKind</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<StateSubactionKind> parseEntryActionKind(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.entryActionKind());
    }

    /**
     * Parses using the <code>doActionMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<StateSubactionMembership> parseDoActionMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.doActionMember());
    }

    /**
     * Parses using the <code>doActionKind</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<StateSubactionKind> parseDoActionKind(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.doActionKind());
    }

    /**
     * Parses using the <code>exitActionMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<StateSubactionMembership> parseExitActionMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.exitActionMember());
    }

    /**
     * Parses using the <code>exitActionKind</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<StateSubactionKind> parseExitActionKind(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.exitActionKind());
    }

    /**
     * Parses using the <code>entryTransitionMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FeatureMembership> parseEntryTransitionMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.entryTransitionMember());
    }

    /**
     * Parses using the <code>stateActionUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ActionUsage> parseStateActionUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.stateActionUsage());
    }

    /**
     * Parses using the <code>emptyActionUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ActionUsage> parseEmptyActionUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.emptyActionUsage());
    }

    /**
     * Parses using the <code>performedActionUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ActionUsage> parsePerformedActionUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.performedActionUsage());
    }

    /**
     * Parses using the <code>transitionUsageMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FeatureMembership> parseTransitionUsageMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.transitionUsageMember());
    }

    /**
     * Parses using the <code>targetTransitionUsageMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FeatureMembership> parseTargetTransitionUsageMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.targetTransitionUsageMember());
    }

    /**
     * Parses using the <code>stateUsageKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseStateUsageKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.stateUsageKeyword());
    }

    /**
     * Parses using the <code>stateUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<StateUsage> parseStateUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.stateUsage());
    }

    /**
     * Parses using the <code>stateUsageBody</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<StateUsage> parseStateUsageBody(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.stateUsageBody());
    }

    /**
     * Parses using the <code>exhibitStateUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ExhibitStateUsage> parseExhibitStateUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.exhibitStateUsage());
    }

    /**
     * Parses using the <code>transitionUsageKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseTransitionUsageKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.transitionUsageKeyword());
    }

    /**
     * Parses using the <code>transitionUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<TransitionUsage> parseTransitionUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.transitionUsage());
    }

    /**
     * Parses using the <code>targetTransitionUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<TransitionUsage> parseTargetTransitionUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.targetTransitionUsage());
    }

    /**
     * Parses using the <code>transitionSourceMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Membership> parseTransitionSourceMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.transitionSourceMember());
    }

    /**
     * Parses using the <code>triggerActionMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<TransitionFeatureMembership> parseTriggerActionMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.triggerActionMember());
    }

    /**
     * Parses using the <code>triggerFeatureKind</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<TransitionFeatureKind> parseTriggerFeatureKind(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.triggerFeatureKind());
    }

    /**
     * Parses using the <code>triggerAction</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<AcceptActionUsage> parseTriggerAction(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.triggerAction());
    }

    /**
     * Parses using the <code>guardExpressionMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<TransitionFeatureMembership> parseGuardExpressionMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.guardExpressionMember());
    }

    /**
     * Parses using the <code>guardFeatureKind</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<TransitionFeatureKind> parseGuardFeatureKind(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.guardFeatureKind());
    }

    /**
     * Parses using the <code>effectBehaviorMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<TransitionFeatureMembership> parseEffectBehaviorMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.effectBehaviorMember());
    }

    /**
     * Parses using the <code>effectFeatureKind</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<TransitionFeatureKind> parseEffectFeatureKind(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.effectFeatureKind());
    }

    /**
     * Parses using the <code>effectBehaviorUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ActionUsage> parseEffectBehaviorUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.effectBehaviorUsage());
    }

    /**
     * Parses using the <code>transitionSuccessionMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<OwningMembership> parseTransitionSuccessionMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.transitionSuccessionMember());
    }

    /**
     * Parses using the <code>transitionSuccession</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<SuccessionAsUsage> parseTransitionSuccession(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.transitionSuccession());
    }

    /**
     * Parses using the <code>calculationKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseCalculationKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.calculationKeyword());
    }

    /**
     * Parses using the <code>calculationDefKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseCalculationDefKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.calculationDefKeyword());
    }

    /**
     * Parses using the <code>calculationDefinition</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<CalculationDefinition> parseCalculationDefinition(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.calculationDefinition());
    }

    /**
     * Parses using the <code>calculationBody</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Type> parseCalculationBody(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.calculationBody());
    }

    /**
     * Parses using the <code>calculationBodyPart</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Type> parseCalculationBodyPart(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.calculationBodyPart());
    }

    /**
     * Parses using the <code>calculationBodyItem</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Type> parseCalculationBodyItem(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.calculationBodyItem());
    }

    /**
     * Parses using the <code>returnParameterMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ReturnParameterMembership> parseReturnParameterMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.returnParameterMember());
    }

    /**
     * Parses using the <code>calculationUsageKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseCalculationUsageKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.calculationUsageKeyword());
    }

    /**
     * Parses using the <code>calculationUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<CalculationUsage> parseCalculationUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.calculationUsage());
    }

    /**
     * Parses using the <code>constraintKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseConstraintKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.constraintKeyword());
    }

    /**
     * Parses using the <code>constraintDefKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseConstraintDefKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.constraintDefKeyword());
    }

    /**
     * Parses using the <code>constraintDefinition</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ConstraintDefinition> parseConstraintDefinition(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.constraintDefinition());
    }

    /**
     * Parses using the <code>constraintUsageKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseConstraintUsageKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.constraintUsageKeyword());
    }

    /**
     * Parses using the <code>constraintUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ConstraintUsage> parseConstraintUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.constraintUsage());
    }

    /**
     * Parses using the <code>assertConstraintUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<AssertConstraintUsage> parseAssertConstraintUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.assertConstraintUsage());
    }

    /**
     * Parses using the <code>constraintUsageDeclaration</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ConstraintUsage> parseConstraintUsageDeclaration(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.constraintUsageDeclaration());
    }

    /**
     * Parses using the <code>requirementKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseRequirementKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.requirementKeyword());
    }

    /**
     * Parses using the <code>requirementDefKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseRequirementDefKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.requirementDefKeyword());
    }

    /**
     * Parses using the <code>requirementDefinition</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<RequirementDefinition> parseRequirementDefinition(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.requirementDefinition());
    }

    /**
     * Parses using the <code>requirementBody</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Type> parseRequirementBody(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.requirementBody());
    }

    /**
     * Parses using the <code>requirementBodyItem</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Type> parseRequirementBodyItem(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.requirementBodyItem());
    }

    /**
     * Parses using the <code>subjectMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<SubjectMembership> parseSubjectMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.subjectMember());
    }

    /**
     * Parses using the <code>subjectUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ReferenceUsage> parseSubjectUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.subjectUsage());
    }

    /**
     * Parses using the <code>requirementConstraintMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<RequirementConstraintMembership> parseRequirementConstraintMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.requirementConstraintMember());
    }

    /**
     * Parses using the <code>requirementConstraintKind</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<RequirementConstraintKind> parseRequirementConstraintKind(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.requirementConstraintKind());
    }

    /**
     * Parses using the <code>requirementConstraintUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ConstraintUsage> parseRequirementConstraintUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.requirementConstraintUsage());
    }

    /**
     * Parses using the <code>framedConcernMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FramedConcernMembership> parseFramedConcernMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.framedConcernMember());
    }

    /**
     * Parses using the <code>framedConcernKind</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<RequirementConstraintKind> parseFramedConcernKind(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.framedConcernKind());
    }

    /**
     * Parses using the <code>framedConcernUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ConcernUsage> parseFramedConcernUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.framedConcernUsage());
    }

    /**
     * Parses using the <code>actorMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ActorMembership> parseActorMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.actorMember());
    }

    /**
     * Parses using the <code>actorUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<PartUsage> parseActorUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.actorUsage());
    }

    /**
     * Parses using the <code>stakeholderMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<StakeholderMembership> parseStakeholderMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.stakeholderMember());
    }

    /**
     * Parses using the <code>stakeholderUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<PartUsage> parseStakeholderUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.stakeholderUsage());
    }

    /**
     * Parses using the <code>requirementUsageKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseRequirementUsageKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.requirementUsageKeyword());
    }

    /**
     * Parses using the <code>requirementUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<RequirementUsage> parseRequirementUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.requirementUsage());
    }

    /**
     * Parses using the <code>satisfyRequirementUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<SatisfyRequirementUsage> parseSatisfyRequirementUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.satisfyRequirementUsage());
    }

    /**
     * Parses using the <code>satisfactionSubjectMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<SubjectMembership> parseSatisfactionSubjectMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.satisfactionSubjectMember());
    }

    /**
     * Parses using the <code>satisfactionParameter</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ReferenceUsage> parseSatisfactionParameter(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.satisfactionParameter());
    }

    /**
     * Parses using the <code>satisfactionFeatureValue</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FeatureValue> parseSatisfactionFeatureValue(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.satisfactionFeatureValue());
    }

    /**
     * Parses using the <code>satisfactionReferenceExpression</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FeatureReferenceExpression> parseSatisfactionReferenceExpression(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.satisfactionReferenceExpression());
    }

    /**
     * Parses using the <code>concernKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseConcernKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.concernKeyword());
    }

    /**
     * Parses using the <code>concernDefKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseConcernDefKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.concernDefKeyword());
    }

    /**
     * Parses using the <code>concernDefinition</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ConcernDefinition> parseConcernDefinition(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.concernDefinition());
    }

    /**
     * Parses using the <code>concernUsageKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseConcernUsageKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.concernUsageKeyword());
    }

    /**
     * Parses using the <code>concernUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ConcernUsage> parseConcernUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.concernUsage());
    }

    /**
     * Parses using the <code>caseKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseCaseKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.caseKeyword());
    }

    /**
     * Parses using the <code>caseDefKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseCaseDefKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.caseDefKeyword());
    }

    /**
     * Parses using the <code>caseDefinition</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<CaseDefinition> parseCaseDefinition(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.caseDefinition());
    }

    /**
     * Parses using the <code>caseBody</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Type> parseCaseBody(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.caseBody());
    }

    /**
     * Parses using the <code>caseBodyItem</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Type> parseCaseBodyItem(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.caseBodyItem());
    }

    /**
     * Parses using the <code>objectiveMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ObjectiveMembership> parseObjectiveMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.objectiveMember());
    }

    /**
     * Parses using the <code>objectiveRequirementUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<RequirementUsage> parseObjectiveRequirementUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.objectiveRequirementUsage());
    }

    /**
     * Parses using the <code>caseUsageKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseCaseUsageKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.caseUsageKeyword());
    }

    /**
     * Parses using the <code>caseUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<CaseUsage> parseCaseUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.caseUsage());
    }

    /**
     * Parses using the <code>analysisCaseKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseAnalysisCaseKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.analysisCaseKeyword());
    }

    /**
     * Parses using the <code>analysisCaseDefKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseAnalysisCaseDefKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.analysisCaseDefKeyword());
    }

    /**
     * Parses using the <code>analysisCaseUsageKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseAnalysisCaseUsageKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.analysisCaseUsageKeyword());
    }

    /**
     * Parses using the <code>analysisCaseDefinition</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<AnalysisCaseDefinition> parseAnalysisCaseDefinition(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.analysisCaseDefinition());
    }

    /**
     * Parses using the <code>analysisCaseUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<AnalysisCaseUsage> parseAnalysisCaseUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.analysisCaseUsage());
    }

    /**
     * Parses using the <code>verificationCaseKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseVerificationCaseKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.verificationCaseKeyword());
    }

    /**
     * Parses using the <code>verificationCaseDefKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseVerificationCaseDefKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.verificationCaseDefKeyword());
    }

    /**
     * Parses using the <code>verificationCaseUsageKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseVerificationCaseUsageKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.verificationCaseUsageKeyword());
    }

    /**
     * Parses using the <code>verificationCaseDefinition</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<VerificationCaseDefinition> parseVerificationCaseDefinition(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.verificationCaseDefinition());
    }

    /**
     * Parses using the <code>verificationCaseUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<VerificationCaseUsage> parseVerificationCaseUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.verificationCaseUsage());
    }

    /**
     * Parses using the <code>requirementVerificationMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<RequirementVerificationMembership> parseRequirementVerificationMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.requirementVerificationMember());
    }

    /**
     * Parses using the <code>requirementVerificationKind</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<RequirementConstraintKind> parseRequirementVerificationKind(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.requirementVerificationKind());
    }

    /**
     * Parses using the <code>requirementVerificationUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<RequirementUsage> parseRequirementVerificationUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.requirementVerificationUsage());
    }

    /**
     * Parses using the <code>useCaseKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseUseCaseKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.useCaseKeyword());
    }

    /**
     * Parses using the <code>useCaseDefKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseUseCaseDefKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.useCaseDefKeyword());
    }

    /**
     * Parses using the <code>useCaseUsageKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseUseCaseUsageKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.useCaseUsageKeyword());
    }

    /**
     * Parses using the <code>useCaseDefinition</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<UseCaseDefinition> parseUseCaseDefinition(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.useCaseDefinition());
    }

    /**
     * Parses using the <code>useCaseUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<UseCaseUsage> parseUseCaseUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.useCaseUsage());
    }

    /**
     * Parses using the <code>includeUseCaseUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<IncludeUseCaseUsage> parseIncludeUseCaseUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.includeUseCaseUsage());
    }

    /**
     * Parses using the <code>viewKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseViewKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.viewKeyword());
    }

    /**
     * Parses using the <code>viewDefKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseViewDefKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.viewDefKeyword());
    }

    /**
     * Parses using the <code>viewDefinition</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ViewDefinition> parseViewDefinition(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.viewDefinition());
    }

    /**
     * Parses using the <code>viewDefinitionBody</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ViewDefinition> parseViewDefinitionBody(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.viewDefinitionBody());
    }

    /**
     * Parses using the <code>viewDefinitionBodyItem</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ViewDefinition> parseViewDefinitionBodyItem(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.viewDefinitionBodyItem());
    }

    /**
     * Parses using the <code>viewRenderingMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ViewRenderingMembership> parseViewRenderingMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.viewRenderingMember());
    }

    /**
     * Parses using the <code>viewRenderingUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<RenderingUsage> parseViewRenderingUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.viewRenderingUsage());
    }

    /**
     * Parses using the <code>viewUsageKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseViewUsageKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.viewUsageKeyword());
    }

    /**
     * Parses using the <code>viewUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ViewUsage> parseViewUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.viewUsage());
    }

    /**
     * Parses using the <code>viewBody</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ViewUsage> parseViewBody(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.viewBody());
    }

    /**
     * Parses using the <code>viewBodyItem</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ViewUsage> parseViewBodyItem(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.viewBodyItem());
    }

    /**
     * Parses using the <code>exposePrefix</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Expose> parseExposePrefix(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.exposePrefix());
    }

    /**
     * Parses using the <code>exposeVisibilityKind</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<VisibilityKind> parseExposeVisibilityKind(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.exposeVisibilityKind());
    }

    /**
     * Parses using the <code>expose</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Expose> parseExpose(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.expose());
    }

    /**
     * Parses using the <code>membershipExpose</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<MembershipExpose> parseMembershipExpose(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.membershipExpose());
    }

    /**
     * Parses using the <code>namespaceExpose</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<NamespaceExpose> parseNamespaceExpose(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.namespaceExpose());
    }

    /**
     * Parses using the <code>viewpointKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseViewpointKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.viewpointKeyword());
    }

    /**
     * Parses using the <code>viewpointDefKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseViewpointDefKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.viewpointDefKeyword());
    }

    /**
     * Parses using the <code>viewpointUsageKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseViewpointUsageKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.viewpointUsageKeyword());
    }

    /**
     * Parses using the <code>viewpointDefinition</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ViewpointDefinition> parseViewpointDefinition(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.viewpointDefinition());
    }

    /**
     * Parses using the <code>viewpointUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ViewpointUsage> parseViewpointUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.viewpointUsage());
    }

    /**
     * Parses using the <code>renderingKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseRenderingKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.renderingKeyword());
    }

    /**
     * Parses using the <code>renderingDefKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseRenderingDefKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.renderingDefKeyword());
    }

    /**
     * Parses using the <code>renderingDefinition</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<RenderingDefinition> parseRenderingDefinition(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.renderingDefinition());
    }

    /**
     * Parses using the <code>renderingUsageKeyword</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseRenderingUsageKeyword(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.renderingUsageKeyword());
    }

    /**
     * Parses using the <code>renderingUsage</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<RenderingUsage> parseRenderingUsage(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.renderingUsage());
    }

    /**
     * Parses using the <code>ownedExpressionMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FeatureMembership> parseOwnedExpressionMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.ownedExpressionMember());
    }

    /**
     * Parses using the <code>ownedExpression</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Expression> parseOwnedExpression(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.ownedExpression());
    }

    /**
     * Parses using the <code>ownedExpressionReference</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FeatureReferenceExpression> parseOwnedExpressionReference(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.ownedExpressionReference());
    }

    /**
     * Parses using the <code>conditionalExpression</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Expression> parseConditionalExpression(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.conditionalExpression());
    }

    /**
     * Parses using the <code>conditionalOperator</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseConditionalOperator(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.conditionalOperator());
    }

    /**
     * Parses using the <code>nullCoalescingExpression</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Expression> parseNullCoalescingExpression(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.nullCoalescingExpression());
    }

    /**
     * Parses using the <code>nullCoalescingOperator</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseNullCoalescingOperator(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.nullCoalescingOperator());
    }

    /**
     * Parses using the <code>impliesExpressionReference</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FeatureReferenceExpression> parseImpliesExpressionReference(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.impliesExpressionReference());
    }

    /**
     * Parses using the <code>impliesExpressionMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FeatureMembership> parseImpliesExpressionMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.impliesExpressionMember());
    }

    /**
     * Parses using the <code>impliesExpression</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Expression> parseImpliesExpression(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.impliesExpression());
    }

    /**
     * Parses using the <code>impliesOperator</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseImpliesOperator(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.impliesOperator());
    }

    /**
     * Parses using the <code>orExpressionReference</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FeatureReferenceExpression> parseOrExpressionReference(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.orExpressionReference());
    }

    /**
     * Parses using the <code>orExpressionMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FeatureMembership> parseOrExpressionMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.orExpressionMember());
    }

    /**
     * Parses using the <code>orExpression</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Expression> parseOrExpression(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.orExpression());
    }

    /**
     * Parses using the <code>orOperator</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseOrOperator(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.orOperator());
    }

    /**
     * Parses using the <code>conditionalOrOperator</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseConditionalOrOperator(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.conditionalOrOperator());
    }

    /**
     * Parses using the <code>xorExpressionReference</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FeatureReferenceExpression> parseXorExpressionReference(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.xorExpressionReference());
    }

    /**
     * Parses using the <code>xorExpressionMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FeatureMembership> parseXorExpressionMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.xorExpressionMember());
    }

    /**
     * Parses using the <code>xorExpression</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Expression> parseXorExpression(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.xorExpression());
    }

    /**
     * Parses using the <code>xorOperator</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseXorOperator(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.xorOperator());
    }

    /**
     * Parses using the <code>andExpression</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Expression> parseAndExpression(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.andExpression());
    }

    /**
     * Parses using the <code>andOperator</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseAndOperator(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.andOperator());
    }

    /**
     * Parses using the <code>conditionalAndOperator</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseConditionalAndOperator(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.conditionalAndOperator());
    }

    /**
     * Parses using the <code>equalityExpressionReference</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FeatureReferenceExpression> parseEqualityExpressionReference(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.equalityExpressionReference());
    }

    /**
     * Parses using the <code>equalityExpressionMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FeatureMembership> parseEqualityExpressionMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.equalityExpressionMember());
    }

    /**
     * Parses using the <code>equalityExpression</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Expression> parseEqualityExpression(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.equalityExpression());
    }

    /**
     * Parses using the <code>equalityOperator</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseEqualityOperator(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.equalityOperator());
    }

    /**
     * Parses using the <code>classificationExpression</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Expression> parseClassificationExpression(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.classificationExpression());
    }

    /**
     * Parses using the <code>classificationTestOperator</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseClassificationTestOperator(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.classificationTestOperator());
    }

    /**
     * Parses using the <code>metaClassificationTestOperator</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseMetaClassificationTestOperator(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.metaClassificationTestOperator());
    }

    /**
     * Parses using the <code>castOperator</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseCastOperator(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.castOperator());
    }

    /**
     * Parses using the <code>metaCastOperator</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseMetaCastOperator(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.metaCastOperator());
    }

    /**
     * Parses using the <code>metadataReference</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<MetadataAccessExpression> parseMetadataReference(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.metadataReference());
    }

    /**
     * Parses using the <code>typeReferenceMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ParameterMembership> parseTypeReferenceMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.typeReferenceMember());
    }

    /**
     * Parses using the <code>typeResultMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ReturnParameterMembership> parseTypeResultMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.typeResultMember());
    }

    /**
     * Parses using the <code>typeReference</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Feature> parseTypeReference(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.typeReference());
    }

    /**
     * Parses using the <code>referenceTyping</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FeatureTyping> parseReferenceTyping(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.referenceTyping());
    }

    /**
     * Parses using the <code>selfReferenceExpression</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FeatureReferenceExpression> parseSelfReferenceExpression(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.selfReferenceExpression());
    }

    /**
     * Parses using the <code>selfReferenceMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ReturnParameterMembership> parseSelfReferenceMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.selfReferenceMember());
    }

    /**
     * Parses using the <code>emptyFeature</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Feature> parseEmptyFeature(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.emptyFeature());
    }

    /**
     * Parses using the <code>relationalExpression</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Expression> parseRelationalExpression(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.relationalExpression());
    }

    /**
     * Parses using the <code>relationalOperator</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseRelationalOperator(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.relationalOperator());
    }

    /**
     * Parses using the <code>rangeExpression</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Expression> parseRangeExpression(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.rangeExpression());
    }

    /**
     * Parses using the <code>additiveExpression</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Expression> parseAdditiveExpression(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.additiveExpression());
    }

    /**
     * Parses using the <code>additiveOperator</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseAdditiveOperator(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.additiveOperator());
    }

    /**
     * Parses using the <code>multiplicativeExpression</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Expression> parseMultiplicativeExpression(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.multiplicativeExpression());
    }

    /**
     * Parses using the <code>multiplicativeOperator</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseMultiplicativeOperator(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.multiplicativeOperator());
    }

    /**
     * Parses using the <code>exponentiationExpression</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Expression> parseExponentiationExpression(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.exponentiationExpression());
    }

    /**
     * Parses using the <code>exponentiationOperator</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseExponentiationOperator(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.exponentiationOperator());
    }

    /**
     * Parses using the <code>unaryExpression</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Expression> parseUnaryExpression(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.unaryExpression());
    }

    /**
     * Parses using the <code>unaryOperator</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseUnaryOperator(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.unaryOperator());
    }

    /**
     * Parses using the <code>extentExpression</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Expression> parseExtentExpression(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.extentExpression());
    }

    /**
     * Parses using the <code>primaryExpression</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Expression> parsePrimaryExpression(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.primaryExpression());
    }

    /**
     * Parses using the <code>functionReferenceExpression</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FeatureReferenceExpression> parseFunctionReferenceExpression(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.functionReferenceExpression());
    }

    /**
     * Parses using the <code>functionReferenceMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FeatureMembership> parseFunctionReferenceMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.functionReferenceMember());
    }

    /**
     * Parses using the <code>functionReference</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Expression> parseFunctionReference(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.functionReference());
    }

    /**
     * Parses using the <code>featureChainMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Membership> parseFeatureChainMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.featureChainMember());
    }

    /**
     * Parses using the <code>ownedFeatureChain</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Feature> parseOwnedFeatureChain(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.ownedFeatureChain());
    }

    /**
     * Parses using the <code>baseExpression</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Expression> parseBaseExpression(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.baseExpression());
    }

    /**
     * Parses using the <code>bodyExpression</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FeatureReferenceExpression> parseBodyExpression(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.bodyExpression());
    }

    /**
     * Parses using the <code>expressionBodyMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FeatureMembership> parseExpressionBodyMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.expressionBodyMember());
    }

    /**
     * Parses using the <code>expressionBody</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Expression> parseExpressionBody(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.expressionBody());
    }

    /**
     * Parses using the <code>resultExpressionMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ResultExpressionMembership> parseResultExpressionMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.resultExpressionMember());
    }

    /**
     * Parses using the <code>bodyParameterMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ParameterMembership> parseBodyParameterMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.bodyParameterMember());
    }

    /**
     * Parses using the <code>bodyParameter</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Feature> parseBodyParameter(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.bodyParameter());
    }

    /**
     * Parses using the <code>sequenceExpression</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Expression> parseSequenceExpression(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.sequenceExpression());
    }

    /**
     * Parses using the <code>featureReferenceExpression</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FeatureReferenceExpression> parseFeatureReferenceExpression(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.featureReferenceExpression());
    }

    /**
     * Parses using the <code>featureReferenceMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Membership> parseFeatureReferenceMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.featureReferenceMember());
    }

    /**
     * Parses using the <code>metadataAccessExpression</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<MetadataAccessExpression> parseMetadataAccessExpression(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.metadataAccessExpression());
    }

    /**
     * Parses using the <code>elementReferenceMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Membership> parseElementReferenceMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.elementReferenceMember());
    }

    /**
     * Parses using the <code>invocationExpression</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<InvocationExpression> parseInvocationExpression(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.invocationExpression());
    }

    /**
     * Parses using the <code>constructorExpression</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ConstructorExpression> parseConstructorExpression(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.constructorExpression());
    }

    /**
     * Parses using the <code>constructorResultMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ReturnParameterMembership> parseConstructorResultMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.constructorResultMember());
    }

    /**
     * Parses using the <code>constructorResult</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Feature> parseConstructorResult(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.constructorResult());
    }

    /**
     * Parses using the <code>instantiatedTypeMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Membership> parseInstantiatedTypeMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.instantiatedTypeMember());
    }

    /**
     * Parses using the <code>featureChain</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Feature> parseFeatureChain(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.featureChain());
    }

    /**
     * Parses using the <code>ownedFeatureChaining</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FeatureChaining> parseOwnedFeatureChaining(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.ownedFeatureChaining());
    }

    /**
     * Parses using the <code>argumentList</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Feature> parseArgumentList(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.argumentList());
    }

    /**
     * Parses using the <code>positionalArgumentList</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Feature> parsePositionalArgumentList(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.positionalArgumentList());
    }

    /**
     * Parses using the <code>argumentMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ParameterMembership> parseArgumentMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.argumentMember());
    }

    /**
     * Parses using the <code>argument</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Feature> parseArgument(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.argument());
    }

    /**
     * Parses using the <code>namedArgumentList</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Feature> parseNamedArgumentList(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.namedArgumentList());
    }

    /**
     * Parses using the <code>namedArgumentMember</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<ParameterMembership> parseNamedArgumentMember(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.namedArgumentMember());
    }

    /**
     * Parses using the <code>namedArgument</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Feature> parseNamedArgument(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.namedArgument());
    }

    /**
     * Parses using the <code>parameterRedefinition</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Redefinition> parseParameterRedefinition(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.parameterRedefinition());
    }

    /**
     * Parses using the <code>argumentValue</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<FeatureValue> parseArgumentValue(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.argumentValue());
    }

    /**
     * Parses using the <code>nullExpression</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<NullExpression> parseNullExpression(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.nullExpression());
    }

    /**
     * Parses using the <code>literalExpression</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<LiteralExpression> parseLiteralExpression(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.literalExpression());
    }

    /**
     * Parses using the <code>literalBoolean</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<LiteralBoolean> parseLiteralBoolean(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.literalBoolean());
    }

    /**
     * Parses using the <code>booleanValue</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Boolean> parseBooleanValue(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.booleanValue());
    }

    /**
     * Parses using the <code>literalString</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<LiteralString> parseLiteralString(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.literalString());
    }

    /**
     * Parses using the <code>literalInteger</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<LiteralInteger> parseLiteralInteger(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.literalInteger());
    }

    /**
     * Parses using the <code>literalReal</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<LiteralRational> parseLiteralReal(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.literalReal());
    }

    /**
     * Parses using the <code>realValue</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<Double> parseRealValue(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.realValue());
    }

    /**
     * Parses using the <code>literalInfinity</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<LiteralInfinity> parseLiteralInfinity(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.literalInfinity());
    }

    /**
     * Parses using the <code>name</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseName(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.name());
    }

    /**
     * Parses using the <code>globalQualification</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseGlobalQualification(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.globalQualification());
    }

    /**
     * Parses using the <code>qualification</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseQualification(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.qualification());
    }

    /**
     * Parses using the <code>qualifiedName</code> rule as entry point.
     *
     * @return the {@link ISysMLParseResult}
     */
    public ISysMLParseResult<String> parseQualifiedName(InputStream is, Charset charset) throws IOException {
        return this.parse(is, charset, parser -> parser.qualifiedName());
    }

}
