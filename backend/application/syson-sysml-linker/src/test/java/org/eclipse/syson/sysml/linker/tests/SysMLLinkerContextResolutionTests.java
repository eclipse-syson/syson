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
package org.eclipse.syson.sysml.linker.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.syson.sysml.linker.ContextNamespaceProvider;
import org.eclipse.syson.sysml.linker.MapBasedReferenceSetChecker;
import org.eclipse.syson.sysml.linker.QualifiedName;
import org.eclipse.syson.sysml.linker.SysMLLinker;
import org.eclipse.syson.sysml.linker.tests.adapter.LibraryLoader;
import org.eclipse.syson.sysml.parser.DanglingLink;
import org.eclipse.syson.sysml.parser.SysMLModelParser;
import org.eclipse.syson.sysml.parser.internal.StructuralModelCompletionService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.omg.sysml.lang.sysml.ConstructorExpression;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Feature;
import org.omg.sysml.lang.sysml.Namespace;
import org.omg.sysml.lang.sysml.ParameterMembership;
import org.omg.sysml.lang.sysml.ReturnParameterMembership;
import org.omg.sysml.lang.sysml.TransitionFeatureMembership;
import org.omg.sysml.lang.sysml.Type;
import org.omg.sysml.logic.SysMLLogicStandaloneSetup;
import org.omg.sysml.util.SysMLLibraryUtil;

/**
 * Checks expression and connector scopes, and implied send-action parameter names, using the SysML snippets in each test.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class SysMLLinkerContextResolutionTests {

    @BeforeClass
    public static void beforeAll() {
        SysMLLogicStandaloneSetup.doSetup();
        SysMLLibraryUtil.setModelLibraryDirectory(new LibraryLoader().getStandardLibraryPath().toString());
    }

    /**
     * The member after a function call belongs to its returned Counter, not to the holder referenced by an argument.
     * KerML 8.2.3.5.2 defines the local namespace as the argument expression's result parameter.
     */
    @Test
    public void invocationMemberUsesReturnType() throws IOException {
        var model = this.link("""
                package Test {
                    part def Counter { attribute count; }
                    part holder { part nested : Counter; }
                    calc def Increment { in c : Counter; return : Counter; }
                    attribute value = Increment(holder.nested).count;
                }
                """);
        this.assertTargets(model, "count", "Test::Counter::count", 1);
    }

    /**
     * The first g is resolved from the constructor result typed by A; subsequent segments follow the preceding g.
     * The three links must all target the same feature despite repeatedly visiting its type.
     */
    @Test
    public void constructorResultSupportsRepeatedFeaturePath() throws IOException {
        var model = this.link("""
                package Test {
                    part def A { part g : A; }
                    part value = new A().g.g.g;
                }
                """);
        this.assertTargets(model, "g", "Test::A::g", 3);
    }

    /**
     * An undirected feature with no explicit specialization inherits members from its value's result.
     * This reproduces the inferred intermediate types in FeaturePathTest (KerML 8.3.3.3.4, checkFeatureValuationSpecialization).
     */
    @Test
    public void featurePathFollowsValueResult() throws IOException {
        var model = this.link("""
                package Test {
                    part def F { part a : A; }
                    part f : F;
                    part def A { part g = f.a; }
                    part value = new A().g.g.g;
                }
                """);
        this.assertTargets(model, "g", "Test::A::g", 3);
    }

    /**
     * A cast's explicitly typed result supplies the member scope, even when its operand has no such member.
     * KerML 8.2.3.5.2 requires looking at the argument result, not the first type or reference inside the operand.
     */
    @Test
    public void castMemberUsesExplicitResultType() throws IOException {
        var model = this.link("""
                package Test {
                    part def A { attribute m; }
                    part source;
                    part a : A;
                    attribute byType = (source as A).m;
                    attribute byFeature = (source as a).m;
                }
                """);
        this.assertTargets(model, "m", "Test::A::m", 2);
    }

    /**
     * The local unnamed payload must acquire its name from the implied SendAction specialization, rather than
     * lookup selecting the library parameter itself (KerML 8.4.4.7.2; SysML 8.3.17.15).
     */
    @Test
    public void sendPayloadResolvesToOwnedParameter() throws IOException {
        var model = this.link("""
                package Test {
                    action a {
                        in s;
                        action snd send via this to this;
                        bind s = snd.payload;
                    }
                }
                """);
        Element send = model.linker().getMemberElement(model.linker().resolve(model.root(), new QualifiedName("Test::a::snd")).orElseThrow());
        var payload = send.getOwnedRelationship().stream()
                .filter(ParameterMembership.class::isInstance).map(ParameterMembership.class::cast)
                .map(ParameterMembership::getOwnedMemberParameter).findFirst().orElseThrow();
        this.assertTargets(model, "payload", payload, 1);
    }

    /**
     * In a composite send action, this must resolve to the specialized subaction self-reference rather than
     * Occurrence::this. The library redefinition is checked independently through its fully qualified name.
     */
    @Test
    public void sendThisUsesSubactionRedefinition() throws IOException {
        var model = this.link("""
                package Test {
                    action a {
                        action snd send via this to this;
                    }
                }
                """);
        this.assertTargets(model, "this", "Actions::Action::subactions::this", 2);
    }

    /**
     * A typed binding or succession must connect the surrounding parts, even if its definition declares ends with
     * the same names. KerML 8.2.3.5.2 starts an end ReferenceSubsetting in the connector's owning namespace.
     */
    @Test
    public void typedConnectorEndsUseOwningNamespace() throws IOException {
        var model = this.link("""
                package Test {
                    connection def AB { end a; end b; }
                    part def P {
                        part a { part x; }
                        part b;
                        binding bound : AB bind a = b;
                        succession ordered : AB first a then b;
                        binding nested : AB bind a.x = b;
                        succession nestedOrder : AB first a.x then b;
                    }
                }
                """);
        this.assertTargets(model, "a", "Test::P::a", 4);
        this.assertTargets(model, "b", "Test::P::b", 4);
        this.assertTargets(model, "x", "Test::P::a::x", 2);
    }

    /**
     * The generated succession for a transition must find the state's done redefinition, rather than the generic
     * action completion inherited by the transition itself (SysML 8.2.2.18.3 and 8.3.18.9).
     */
    @Test
    public void transitionTargetUsesEnclosingState() throws IOException {
        var model = this.link("""
                package Test {
                    state def S {
                        state active;
                        transition first active then done;
                    }
                }
                """);
        this.assertTargets(model, "done", "States::StateAction::done", 1);
    }

    /**
     * KerML 8.3.4.8.6 makes a sequence index result specialize its first argument's result. Members after the index
     * therefore belong to the selected Entry; the inferred value type must also remain available through a variable.
     */
    @Test
    public void indexedSequenceExposesElementMembersAndInferredValueType() throws IOException {
        var model = this.link("""
                package Test {
                    part def Frame { attribute offset; }
                    part def Entry { part coordinateFrame : Frame; }
                    part entries : Entry[3];
                    private attribute selectedFrame = entries#(1).coordinateFrame;
                    attribute offset = selectedFrame.offset;
                }
                """);
        this.assertTargets(model, "coordinateFrame", "Test::Entry::coordinateFrame", 1);
        this.assertTargets(model, "offset", "Test::Frame::offset", 1);
    }

    /**
     * Consecutive index expressions must carry the sequence element type through each result parameter rather than
     * fall back to an untyped generic evaluation result (KerML 8.3.4.8.6).
     */
    @Test
    public void repeatedSequenceIndexPreservesElementType() throws IOException {
        var model = this.link("""
                package Test {
                    part def Entry { attribute count; }
                    part entries : Entry[3];
                    attribute value = entries#(1)#(1).count;
                }
                """);
        this.assertTargets(model, "count", "Test::Entry::count", 1);
    }

    /**
     * KerML 8.3.4.8.6 explicitly excludes Collections::Array from sequence-result specialization, including indirect
     * specializations. Indexing Samples must not expose the array object's members as members of its elements.
     */
    @Test
    public void arrayIndexDoesNotInheritTheArrayObjectContext() throws IOException {
        var model = this.link("""
                package Test {
                    attribute def Samples :> Collections::Array;
                    attribute samples : Samples;
                    attribute value = samples#(1);
                }
                """);
        var value = model.linker().getMemberElement(model.linker().resolve(model.root(), new QualifiedName("Test::value")).orElseThrow());
        assertNull(new ContextNamespaceProvider(model.linker()).getResultContext((Feature) value));
    }

    /**
     * SysML 8.4.14.3 implicitly redefines the transition's accepter, guard and effect. These names must
     * identify the owned actions before implied relationships are materialized, so effect.sentMessage resolves.
     */
    @Test
    public void transitionNamesIdentifyOwnedActions() throws IOException {
        var model = this.link("""
                package Test {
                    item def Signal;
                    state s {
                        state waiting;
                        transition t first waiting
                            accept msg : Signal
                            if true
                            do send new Signal() to this
                            then waiting;
                    }
                    bind s.t.effect.sentMessage = s.t.accepter.acceptedMessage;
                    attribute condition = s.t.guard;
                }
                """);
        Namespace transition = (Namespace) model.linker().getMemberElement(model.linker().resolve(model.root(), new QualifiedName("Test::s::t")).orElseThrow());
        for (var relationship : transition.getOwnedRelationship()) {
            if (relationship instanceof TransitionFeatureMembership membership) {
                String name = switch (membership.getKind()) {
                    case TRIGGER -> "accepter";
                    case GUARD -> "guard";
                    case EFFECT -> "effect";
                };
                this.assertTargets(model, name, membership.getOwnedMemberFeature(), 1);
            }
        }
    }

    /**
     * SysML 8.3.16.3 requires Flows::flows for usages with ends. Its source and target redefine the
     * Transfers ends inherited through the explicit type, even when the flow also redefines a base flow.
     */
    @Test
    public void typedFlowEndsUseImplicitFlowSpecialization() throws IOException {
        var model = this.link("""
                package Test {
                    part def Base { flow publication; }
                    part p : Base {
                        part sender;
                        part receiver;
                        flow :>> publication : Transfers::MessageTransfer {
                            end :>> source ::> sender;
                            end :>> target ::> receiver;
                        }
                    }
                }
                """);
        this.assertTargets(model, "source", "Flows::flows::source", 1);
        this.assertTargets(model, "target", "Flows::flows::target", 1);
    }

    /**
     * KerML 8.3.3.3.4 checkFeatureEndRedefinition and 8.4.4.6.1 give unnamed connector ends the
     * corresponding association-end names. The chain must select the local end, not the definition's end.
     */
    @Test
    public void interfaceEndNamesIdentifyOwnedEnds() throws IOException {
        var model = this.link("""
                package Test {
                    port def P { in item data; }
                    interface def I {
                        end source : ~P;
                        end target : P;
                    }
                    part a { port p : ~P; }
                    part b { port p : P; }
                    interface c : I connect a.p to b.p {
                        flow transfer from c.source.data to c.target.data;
                    }
                }
                """);
        Type connector = (Type) model.linker().getMemberElement(model.linker().resolve(model.root(), new QualifiedName("Test::c")).orElseThrow());
        this.assertTargets(model, "source", connector.getOwnedEndFeature().get(0), 1);
        this.assertTargets(model, "target", connector.getOwnedEndFeature().get(1), 1);
    }

    /**
     * Constructor arguments implicitly redefine public features in order (KerML 8.4.4.9.4). Pilot interchange
     * models resolve their values relative to the constructed result, so a matching name identifies the local
     * argument feature. A different name must still resolve in the enclosing namespace.
     */
    @Test
    public void constructorArgumentUsesLocalImplicitNameOrEnclosingFeature() throws IOException {
        var model = this.link("""
                package Test {
                    item def Data { item content; }
                    item otherContent;
                    item value = new Data(content);
                    item otherValue = new Data(otherContent);
                }
                """);
        this.assertTargets(model, "otherContent", "Test::otherContent", 1);
        var contents = model.root().eAllContents();
        while (contents.hasNext()) {
            if (contents.next() instanceof ConstructorExpression constructor && constructor.getOwningNamespace().getDeclaredName().equals("value")) {
                Feature result = constructor.getOwnedRelationship().stream().filter(ReturnParameterMembership.class::isInstance)
                        .map(ReturnParameterMembership.class::cast).map(ReturnParameterMembership::getOwnedMemberParameter).findFirst().orElseThrow();
                this.assertTargets(model, "content", result.getOwnedFeature().get(0), 1);
                return;
            }
        }
        fail("Expected a constructor expression");
    }

    /**
     * A constructor used as a direct send argument keeps the surrounding send-parameter scope (SysML
     * 8.2.2.17.4; KerML 8.2.3.5.2). The implicitly named constructor argument must not shadow the action input,
     * as in Arrowhead's send new Return_AllItems(itms).
     */
    @Test
    public void sendConstructorArgumentKeepsEnclosingActionScope() throws IOException {
        var model = this.link("""
                package Test {
                    item def Payload { item content; }
                    action a {
                        in content;
                        send new Payload(content) to this;
                    }
                }
                """);
        this.assertTargets(model, "content", "Test::a::content", 1);
    }

    private void assertTargets(LinkedModel model, String name, String expectedName, int count) {
        var expected = model.linker().getMemberElement(model.linker().resolve(model.root(), new QualifiedName(expectedName)).orElseThrow());
        this.assertTargets(model, name, expected, count);
    }

    private void assertTargets(LinkedModel model, String name, Element expected, int count) {
        int actualCount = 0;
        for (DanglingLink link : model.links()) {
            if (link.targets().equals(java.util.List.of(name))) {
                assertSame(name, expected, link.context().eGet(link.reference()));
                actualCount++;
            }
        }
        assertEquals("Number of checked links", count, actualCount);
    }

    private LinkedModel link(String source) throws IOException {
        var resourceSet = new ResourceSetImpl();
        resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
        resourceSet.getResources().addAll(new LibraryLoader().getStdLibResources());
        var parsed = new SysMLModelParser().parseRootNamespace(new ByteArrayInputStream(source.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        assertTrue(parsed.diagnostic().toString(), parsed.diagnostic().getSeverity() < org.eclipse.emf.common.util.Diagnostic.ERROR);
        resourceSet.createResource(URI.createURI("test.sysml")).getContents().add(parsed.model());
        Map<EObject, Map<EReference, Set<QualifiedName>>> links = new LinkedHashMap<>();
        for (DanglingLink link : parsed.danglingLinks()) {
            for (String name : link.targets()) {
                links.computeIfAbsent(link.context(), key -> new LinkedHashMap<>())
                        .computeIfAbsent(link.reference(), key -> new LinkedHashSet<>()).add(new QualifiedName(name));
            }
        }
        var elements = new ArrayList<Element>();
        elements.add(parsed.model());
        parsed.model().eAllContents().forEachRemaining(content -> {
            if (content instanceof Element element) {
                elements.add(element);
            }
        });
        var completion = new StructuralModelCompletionService(new MapBasedReferenceSetChecker(links));
        elements.forEach(completion::complete);
        var linker = new SysMLLinker(resourceSet, links);
        linker.resolveAll();
        assertTrue("Unresolved references: " + linker.getNotResolved(), linker.getNotResolved().isEmpty());
        return new LinkedModel(parsed.model(), linker, parsed.danglingLinks());
    }

    /**
     * Parsed model and original links retained for checking their resolved targets.
     */
    private record LinkedModel(Namespace root, SysMLLinker linker, Set<DanglingLink> links) {
    }
}
