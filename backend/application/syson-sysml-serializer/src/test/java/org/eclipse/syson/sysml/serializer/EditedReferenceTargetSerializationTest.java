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
package org.eclipse.syson.sysml.serializer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.syson.sysml.parser.DanglingLink;
import org.eclipse.syson.sysml.parser.ISysMLParseResult;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.omg.sysml.lang.sysml.Annotation;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Feature;
import org.omg.sysml.lang.sysml.Namespace;
import org.omg.sysml.lang.sysml.NamespaceImport;
import org.omg.sysml.lang.sysml.Relationship;
import org.omg.sysml.lang.sysml.Subsetting;
import org.omg.sysml.lang.sysml.SysMLFactory;
import org.omg.sysml.lang.sysml.SysMLPackage;
import org.omg.sysml.logic.SysMLLogicStandaloneSetup;

/**
 * Verifies references whose target was linked, renamed or retargeted after parsing. The parser spelling of such a
 * reference is kept only while it still designates the installed target; otherwise the serialized text must name the
 * current target. Linking is simulated by installing the targets that the parser spellings designate.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
class EditedReferenceTargetSerializationTest {

    private static final String LIBRARY = "package P { part def A { part f; } action act; use case uc; } "
            + "package Q { part def A { part f; } action act; use case uc; } ";

    private static final String P_A = "P::A";

    private static final String WHITESPACE_REGEX = "\\s+";

    private static final SysMLPackage SYSML = SysMLPackage.eINSTANCE;

    static {
        SysMLLogicStandaloneSetup.doSetup();
    }

    /**
     * A linked membership import must keep its spelling; once retargeted, it must name the new imported member.
     */
    @ParameterizedTest
    @EnumSource(SerializationMode.class)
    void membershipImportNamesItsCurrentTarget(final SerializationMode mode) throws IOException {
        final var parseResult = parse("private import P::A;");
        link(parseResult, SYSML.getMembershipImport_ImportedMembership(), P_A, member(parseResult, P_A).getOwningMembership());
        assertBodyEquals("private import P::A;", parseResult, mode);

        link(parseResult, SYSML.getMembershipImport_ImportedMembership(), P_A, member(parseResult, "Q::A").getOwningMembership());

        assertBodyEquals("private import Q::A;", parseResult, mode);
    }

    /**
     * A namespace import must name the imported namespace after it was renamed.
     */
    @ParameterizedTest
    @EnumSource(SerializationMode.class)
    void namespaceImportNamesItsRenamedNamespace(final SerializationMode mode) throws IOException {
        final var parseResult = parse("private import P::*;");
        final Element namespace = member(parseResult, "P");
        link(parseResult, SYSML.getNamespaceImport_ImportedNamespace(), "P", namespace);
        assertBodyEquals("private import P::*;", parseResult, mode);

        namespace.setDeclaredName("Renamed");

        assertBodyEquals("private import Renamed::*;", parseResult, mode);
    }

    /**
     * An alias must keep its spelling while linked to it; once retargeted, it must name the new member.
     */
    @ParameterizedTest
    @EnumSource(SerializationMode.class)
    void aliasNamesItsCurrentMember(final SerializationMode mode) throws IOException {
        final var parseResult = parse("alias AL for P::A;");
        link(parseResult, SYSML.getMembership_MemberElement(), P_A, member(parseResult, P_A));
        assertBodyEquals("alias AL for P::A;", parseResult, mode);

        link(parseResult, SYSML.getMembership_MemberElement(), P_A, member(parseResult, "Q::A"));

        assertBodyEquals("alias AL for Q::A;", parseResult, mode);
    }

    /**
     * A conjugated port typing is linked to the conjugated definition that its port definition owns; it must name that
     * port definition, so that renaming it renames the typing, with or without parser spellings.
     */
    @ParameterizedTest
    @EnumSource(SerializationMode.class)
    void conjugatedPortTypingNamesItsRenamedPortDefinition(final SerializationMode mode) throws IOException {
        final var parseResult = inResource(SerializerRoundTripTestSupport.parseTestCase("edited/conjugated-port-typing-rename.sysml"));
        final Element definition = member(parseResult, "U::PD");
        link(parseResult, SYSML.getConjugatedPortTyping_ConjugatedPortDefinition(), "~PD",
                definition.getOwnedRelationship().get(0).getOwnedRelatedElement().get(0));
        assertBodyEquals("port def PD; ref port p : ~PD;", parseResult, mode);

        definition.setDeclaredName("Renamed");

        assertBodyEquals("port def Renamed; ref port p : ~U::Renamed;", parseResult, mode);
        assertTrue(new SysMLSerializer().serialize(parseResult.model(), SerializationOptions.defaults().withMode(mode))
                .replaceAll(WHITESPACE_REGEX, " ").contains("ref port p : ~U::Renamed;"));
    }

    /**
     * An alias spelled with an escape sequence ({@code 'a\\nb'}, a line break) no longer names its target once the
     * target is renamed to the characters of that spelling (a backslash and {@code n}): the new name must be written.
     */
    @ParameterizedTest
    @EnumSource(SerializationMode.class)
    void aliasNamesItsTargetRenamedToAnEscapeSpelling(final SerializationMode mode) throws IOException {
        final var parseResult = inResource(SerializerRoundTripTestSupport.parseTestCase("edited/alias-of-an-escaped-name.sysml"));
        final Element part = member(parseResult, "U::'a\\nb'");
        link(parseResult, SYSML.getMembership_MemberElement(), "'a\\nb'", part);
        assertBodyEquals("part 'a\\nb'; alias al for 'a\\nb';", parseResult, mode);

        part.setDeclaredName("a\\nb");

        assertBodyEquals("part 'a\\\\nb'; alias al for U::'a\\\\nb';", parseResult, mode);
    }

    /**
     * A redefinition written with an inherited simple name must keep it while linked to the inherited feature; once
     * retargeted to a feature that is not inherited, it must name the new feature.
     */
    @ParameterizedTest
    @EnumSource(SerializationMode.class)
    void redefinitionNamesItsCurrentRedefinedFeature(final SerializationMode mode) throws IOException {
        final var parseResult = parse("part z : P::A { part redefines f; }");
        link(parseResult, SYSML.getFeatureTyping_Type(), P_A, member(parseResult, P_A));
        link(parseResult, SYSML.getRedefinition_RedefinedFeature(), "f", member(parseResult, "P::A::f"));
        assertBodyEquals("part z : P::A { part redefines f; }", parseResult, mode);

        link(parseResult, SYSML.getRedefinition_RedefinedFeature(), "f", member(parseResult, "Q::A::f"));

        assertBodyEquals("part z : P::A { part redefines Q::A::f; }", parseResult, mode);
    }

    /**
     * A performed action reference must keep its spelling while linked to it; once retargeted, it must name the new
     * action.
     */
    @ParameterizedTest
    @EnumSource(SerializationMode.class)
    void performedActionNamesItsCurrentTarget(final SerializationMode mode) throws IOException {
        final var parseResult = parse("part def H { perform P::act; }");
        link(parseResult, SYSML.getReferenceSubsetting_ReferencedFeature(), "P::act", member(parseResult, "P::act"));
        assertBodyEquals("part def H { perform P::act; }", parseResult, mode);

        link(parseResult, SYSML.getReferenceSubsetting_ReferencedFeature(), "P::act", member(parseResult, "Q::act"));

        assertBodyEquals("part def H { perform Q::act; }", parseResult, mode);
    }

    /**
     * An included use case must keep its qualified spelling while linked to it, and must name the new use case once
     * retargeted; writing only the declared name of the target would not resolve from the including use case.
     */
    @ParameterizedTest
    @EnumSource(SerializationMode.class)
    void includedUseCaseNamesItsCurrentTarget(final SerializationMode mode) throws IOException {
        final var parseResult = parse("use case def M { include P::uc; }");
        final String prefix = this.includePrefix(mode);
        link(parseResult, SYSML.getReferenceSubsetting_ReferencedFeature(), "P::uc", member(parseResult, "P::uc"));
        assertBodyEquals("use case def M { " + prefix + "include P::uc; }", parseResult, mode);

        link(parseResult, SYSML.getReferenceSubsetting_ReferencedFeature(), "P::uc", member(parseResult, "Q::uc"));

        assertBodyEquals("use case def M { " + prefix + "include Q::uc; }", parseResult, mode);
    }

    /**
     * A renamed feature reached through a flow end ({@code a.c}) must be written as the new path segment, not as a
     * qualified name inside the feature path.
     */
    @ParameterizedTest
    @EnumSource(SerializationMode.class)
    void flowEndNamesItsRenamedNestedFeature(final SerializationMode mode) throws IOException {
        final var parseResult = parse("part def X { part a { part c; } part b { part c; } flow a.c to b.c; }");
        linkFlowEnd(parseResult, "a", "U::X::a::c");
        linkFlowEnd(parseResult, "b", "U::X::b::c");
        assertBodyEquals("part def X { part a { part c; } part b { part c; } flow a.c to b.c; }", parseResult, mode);

        member(parseResult, "U::X::a::c").setDeclaredName("d");

        assertBodyEquals("part def X { part a { part d; } part b { part c; } flow a.d to b.c; }", parseResult, mode);
    }

    /**
     * A transition source must name the state it currently designates after being retargeted.
     */
    @ParameterizedTest
    @EnumSource(SerializationMode.class)
    void transitionSourceNamesItsCurrentState(final SerializationMode mode) throws IOException {
        final var parseResult = parse("state def S { state s1; state s2; transition first s1 then s2; }");
        link(parseResult, SYSML.getMembership_MemberElement(), "s1", member(parseResult, "U::S::s1"));
        assertTrue(bodyText(parseResult, mode).contains("transition first s1 then s2;"));

        link(parseResult, SYSML.getMembership_MemberElement(), "s1", member(parseResult, "U::S::s2"));

        final String text = bodyText(parseResult, mode);
        assertTrue(text.matches(".*transition first (U::S::)?s2 then s2;.*"), text);
    }

    /**
     * A metadata {@code about} target must keep its spelling while linked to it; once retargeted, it must name the new
     * annotated element.
     */
    @ParameterizedTest
    @EnumSource(SerializationMode.class)
    void metadataAnnotationNamesItsCurrentAnnotatedElement(final SerializationMode mode) throws IOException {
        final var parseResult = parse("metadata def M; metadata M about P::A;");
        link(parseResult, SYSML.getAnnotation_AnnotatedElement(), P_A, member(parseResult, P_A));
        assertBodyEquals("metadata def M; metadata M about P::A;", parseResult, mode);

        link(parseResult, SYSML.getAnnotation_AnnotatedElement(), P_A, member(parseResult, "Q::A"));

        assertBodyEquals("metadata def M; metadata M about Q::A;", parseResult, mode);
    }

    /**
     * A metadata annotation created in the model, without any parser spelling, must still write its {@code about}
     * target.
     */
    @ParameterizedTest
    @EnumSource(SerializationMode.class)
    void modelCreatedMetadataAnnotationWritesItsAnnotatedElement(final SerializationMode mode) throws IOException {
        final var parseResult = parse("metadata def M; metadata m : M;");
        final Annotation annotation = SysMLFactory.eINSTANCE.createAnnotation();
        annotation.setAnnotatedElement(member(parseResult, P_A));
        member(parseResult, "U::m").getOwnedRelationship().add(annotation);

        assertBodyEquals("metadata def M; metadata m : M about P::A;", parseResult, mode);
    }

    /**
     * The segments of a feature chain expression, chaining features ({@code f.g.x}) and the member of a plain chain
     * ({@code f.g}), must name their renamed targets instead of keeping the stale parser spellings.
     */
    @ParameterizedTest
    @EnumSource(SerializationMode.class)
    void featureChainExpressionNamesItsRenamedSegments(final SerializationMode mode) throws IOException {
        final var parseResult = parse("part def X { part f { part g { attribute x; } } attribute y = f.g.x; attribute w = f.g; }");
        linkAll(parseResult, "f", member(parseResult, "U::X::f"));
        linkAll(parseResult, "g", member(parseResult, "U::X::f::g"));
        linkAll(parseResult, "x", member(parseResult, "U::X::f::g::x"));
        assertBodyEquals("part def X { part f { part g { attribute x; } } attribute y = f.g.x; attribute w = f.g; }", parseResult, mode);

        final Element g = member(parseResult, "U::X::f::g");
        member(parseResult, "U::X::f::g::x").setDeclaredName("z");
        g.setDeclaredName("h");

        assertBodyEquals("part def X { part f { part h { attribute z; } } attribute y = f.h.z; attribute w = f.h; }", parseResult, mode);
    }

    /**
     * A renamed segment of a specialization feature chain must be written as the new segment name; it must not be
     * written as a qualified name inside the path, nor rewrite another segment with the same spelling.
     */
    @ParameterizedTest
    @EnumSource(SerializationMode.class)
    void specializationFeatureChainNamesItsRenamedSegment(final SerializationMode mode) throws IOException {
        final var parseResult = parse("part def X { part x { attribute x; } attribute y :> x.x; }");
        final List<DanglingLink> chainLinks = parseResult.danglingLinks().stream()
                .filter(link -> link.reference() == SYSML.getFeatureChaining_ChainingFeature())
                .toList();
        chainLinks.get(0).context().eSet(SYSML.getFeatureChaining_ChainingFeature(), member(parseResult, "U::X::x"));
        chainLinks.get(1).context().eSet(SYSML.getFeatureChaining_ChainingFeature(), member(parseResult, "U::X::x::x"));
        assertBodyEquals("part def X { part x { attribute x; } attribute y :> x.x; }", parseResult, mode);

        member(parseResult, "U::X::x::x").setDeclaredName("z");

        assertBodyEquals("part def X { part x { attribute z; } attribute y :> x.z; }", parseResult, mode);
    }

    /**
     * The target of a standalone relationship fragment, such as an {@code ownedSubsetting} parse, must name the target
     * installed since parsing instead of the stale parser spelling.
     */
    @ParameterizedTest
    @EnumSource(SerializationMode.class)
    void standaloneRelationshipFragmentNamesItsCurrentTarget(final SerializationMode mode) throws IOException {
        final var parseResult = parseFragment("parseOwnedSubsetting", "ownedSubsetting/qualifiedName/sysml/qualifiedName.sysml");
        ((Subsetting) parseResult.model()).setSubsettedFeature(namedFeature("Renamed"));

        assertEquals("Renamed", fragmentText(parseResult, "parseOwnedSubsetting", mode));
    }

    /**
     * The imported namespace of a standalone filter package import fragment must name the namespace installed since
     * parsing, keeping the import suffix.
     */
    @ParameterizedTest
    @EnumSource(SerializationMode.class)
    void standaloneFilterImportFragmentNamesItsCurrentNamespace(final SerializationMode mode) throws IOException {
        final String fixture = "filterPackageImport/filterPackageNamespaceImport/sysml/filterPackageNamespaceImport.sysml";
        final var parseResult = parseFragment("parseFilterPackageImport", fixture);
        final Namespace namespace = SysMLFactory.eINSTANCE.createNamespace();
        namespace.setDeclaredName("Renamed");
        ((NamespaceImport) parseResult.model()).setImportedNamespace(namespace);

        assertEquals("Renamed::*::**", fragmentText(parseResult, "parseFilterPackageImport", mode));
    }

    /** A quoted name containing separators is not the qualified path of an external target. */
    @ParameterizedTest
    @EnumSource(SerializationMode.class)
    void externalImportRetargetingPreservesNameBoundaries(final SerializationMode mode) throws IOException {
        final var parseResult = parse("private import 'External::Target';");
        final var library = SerializerRoundTripTestSupport.parseDocument("package External { part def Target; }");
        final Element target = member(library, "External::Target");
        link(parseResult, SYSML.getMembershipImport_ImportedMembership(), "'External::Target'", target.getOwningMembership());

        assertBodyEquals("private import External::'Target';", parseResult, mode);
    }

    /** Normalized output makes the non-composite flag of an include explicit with {@code ref}. */
    private String includePrefix(final SerializationMode mode) {
        String prefix = "";
        if (mode == SerializationMode.NORMALIZED) {
            prefix = "ref ";
        }
        return prefix;
    }

    /** Parses the body in package {@code U}, in a ResourceSet so that names can be looked up as in an editing session. */
    /**
     * A spelling whose qualifier is shadowed by an inner namespace without the member must not be kept for the outer
     * member it no longer reaches: {@code Q::A} from {@code Inner} binds {@code Q} to {@code Inner::Q}.
     */
    @ParameterizedTest
    @EnumSource(SerializationMode.class)
    void shadowedQualifiedSpellingNamesItsTargetAgain(final SerializationMode mode) throws IOException {
        final String body = "package Q { part def A; } package Inner { package Q; part x : %s; }";
        final var parseResult = parse(body.formatted("Q::A"));
        link(parseResult, SYSML.getFeatureTyping_Type(), "Q::A", member(parseResult, "U::Q::A"));

        assertBodyEquals(body.formatted("U::Q::A"), parseResult, mode);
    }

    private static ISysMLParseResult<Namespace> parse(final String body) throws IOException {
        return inResource(SerializerRoundTripTestSupport.parseDocument(LIBRARY + "package U { " + body + " }"));
    }

    /** Puts the parsed model in a resource of its own, as an edited model is. */
    private static ISysMLParseResult<Namespace> inResource(final ISysMLParseResult<Namespace> parseResult) {
        final Resource resource = new ResourceImpl(URI.createURI("memory:/edited.sysml"));
        new ResourceSetImpl().getResources().add(resource);
        resource.getContents().add(parseResult.model());
        return parseResult;
    }

    /** Parses a parser test fixture with the given entry point. */
    private static ISysMLParseResult<? extends Element> parseFragment(final String parseMethod, final String fixture) throws IOException {
        return SerializerRoundTripTestSupport.parseAccepted(
                new SerializerRoundTripTestSupport.ParserRuleFixture(parseMethod, Path.of("../syson-sysml-parser/src/test/resources", fixture)));
    }

    private static String fragmentText(final ISysMLParseResult<? extends Element> parseResult, final String parseMethod,
            final SerializationMode mode) {
        return new SysMLSerializer().serialize(parseResult, SerializationOptions.defaults().withMode(mode).withParserEntryPoint(parseMethod))
                .trim();
    }

    private static Feature namedFeature(final String name) {
        final Feature feature = SysMLFactory.eINSTANCE.createFeature();
        feature.setDeclaredName(name);
        return feature;
    }

    /** Installs {@code target} in the reference that the parser left unresolved with the given spelling. */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static void link(final ISysMLParseResult<Namespace> parseResult, final EReference reference, final String spelling,
            final EObject target) {
        final DanglingLink link = parseResult.danglingLinks().stream()
                .filter(candidate -> candidate.reference() == reference && candidate.targets().contains(spelling))
                .findFirst()
                .orElseThrow(() -> new AssertionError("No parser link " + reference.getName() + " = " + spelling));
        if (reference.isMany()) {
            final List<EObject> values = (List<EObject>) link.context().eGet(reference, false);
            values.clear();
            values.add(target);
        } else {
            link.context().eSet(reference, target);
        }
    }

    /** Installs {@code target} in every single-valued reference that the parser left unresolved with the given spelling. */
    private static void linkAll(final ISysMLParseResult<Namespace> parseResult, final String spelling, final EObject target) {
        parseResult.danglingLinks().stream()
                .filter(link -> !link.reference().isMany() && link.targets().contains(spelling))
                .forEach(link -> link.context().eSet(link.reference(), target));
    }

    /**
     * Links the flow end written {@code <end>.<feature>}: its reference subsetting to {@code U::X::<end>} and the
     * redefinition of its nested flow feature to {@code featureName}.
     */
    private static void linkFlowEnd(final ISysMLParseResult<Namespace> parseResult, final String end, final String featureName) {
        final DanglingLink endLink = parseResult.danglingLinks().stream()
                .filter(link -> link.reference() == SYSML.getReferenceSubsetting_ReferencedFeature() && link.targets().contains(end))
                .findFirst()
                .orElseThrow();
        endLink.context().eSet(endLink.reference(), member(parseResult, "U::X::" + end));
        final EObject endFeature = ((Relationship) endLink.context()).getOwningRelatedElement();
        final DanglingLink featureLink = parseResult.danglingLinks().stream()
                .filter(link -> link.reference() == SYSML.getRedefinition_RedefinedFeature()
                        && EcoreUtil.isAncestor(endFeature, link.context()))
                .findFirst()
                .orElseThrow();
        featureLink.context().eSet(featureLink.reference(), member(parseResult, featureName));
    }

    private static Element member(final ISysMLParseResult<Namespace> parseResult, final String qualifiedName) {
        final var contents = EcoreUtil.<EObject> getAllContents(List.of(parseResult.model()));
        while (contents.hasNext()) {
            if (contents.next() instanceof final Element element && qualifiedName.equals(element.getQualifiedName())) {
                return element;
            }
        }
        return fail("No element " + qualifiedName);
    }

    private static void assertBodyEquals(final String expectedBody, final ISysMLParseResult<Namespace> parseResult, final SerializationMode mode) {
        assertEquals("package U { " + expectedBody + " }", bodyText(parseResult, mode), mode::name);
    }

    /** Serializes the model and returns the text of package {@code U} with whitespace runs collapsed. */
    private static String bodyText(final ISysMLParseResult<Namespace> parseResult, final SerializationMode mode) {
        final String text = new SysMLSerializer().serialize(parseResult, SerializationOptions.defaults().withMode(mode))
                .replaceAll(WHITESPACE_REGEX, " ").trim();
        return text.substring(text.indexOf("package U"));
    }
}
