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
package org.eclipse.syson.sysml;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.syson.application.configuration.SysONDefaultLibrariesConfiguration;
import org.eclipse.syson.application.configuration.SysONLoadDefaultLibrariesOnApplicationStartConfiguration;
import org.eclipse.syson.sysml.dto.ExpressionPropertiesInput;
import org.eclipse.syson.sysml.dto.FeatureValueExpressionPropertiesInput;
import org.eclipse.syson.sysml.metamodel.services.textual.SysONTextSerializer;
import org.eclipse.syson.sysml.services.SysMLExpressionEditor;
import org.eclipse.syson.sysml.services.SysMLMetamodelCopier;
import org.eclipse.syson.sysml.services.SysMLParsingResult;
import org.eclipse.syson.sysml.services.SysMLParsingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.omg.sysml.lang.sysml.SysMLPackage;

/**
 * Exercises metamodel conversion, contextual resolution, and isolated import failures without Node.
 *
 * @author arichard
 */
public class SysMLParsingServiceTests {

    private final SysMLParsingService parsingService = new SysMLParsingService();

    /**
     * Persisted classifier and feature names must remain compatible as either generated metamodel evolves.
     */
    @DisplayName("GIVEN both SysML metamodels, WHEN comparing persisted features, THEN classifier names, feature types, and enum literals are compatible")
    @Test
    public void verifiesPersistedMetamodelCompatibility() {
        assertThat(SysmlPackage.eINSTANCE.getEClassifiers()).extracting(classifier -> classifier.getName())
                .containsExactlyInAnyOrderElementsOf(SysMLPackage.eINSTANCE.getEClassifiers().stream().map(classifier -> classifier.getName()).toList());
        for (var classifier : SysmlPackage.eINSTANCE.getEClassifiers()) {
            var target = SysMLPackage.eINSTANCE.getEClassifier(classifier.getName());
            if (classifier instanceof EClass sourceClass) {
                assertThat(target).isInstanceOf(EClass.class);
                for (var feature : sourceClass.getEAllStructuralFeatures()) {
                    if (!feature.isDerived() && !feature.isTransient() && feature != EcorePackage.Literals.EMODEL_ELEMENT__EANNOTATIONS) {
                        var targetFeature = ((EClass) target).getEStructuralFeature(feature.getName());
                        assertThat(targetFeature).as(sourceClass.getName() + "." + feature.getName()).isNotNull();
                        assertThat(targetFeature.getEType().getName()).isEqualTo(feature.getEType().getName());
                        assertThat(targetFeature.isMany()).isEqualTo(feature.isMany());
                    }
                }
            } else if (classifier instanceof EEnum sourceEnum) {
                assertThat(target).isInstanceOf(EEnum.class);
                assertThat(((EEnum) target).getELiterals()).extracting(literal -> literal.getLiteral())
                        .containsExactlyInAnyOrderElementsOf(sourceEnum.getELiterals().stream().map(literal -> literal.getLiteral()).toList());
            }
        }
    }

    /**
     * Existing package and type references must resolve to the actual SysON objects, including after serialization.
     */
    @DisplayName("GIVEN an existing annotated package, WHEN importing and reloading a reference to it, THEN original object identities and annotations are preserved")
    @Test
    public void resolvesExistingPackageWithoutCopyingItBack() throws Exception {
        ResourceSet context = this.context();
        Namespace existing = (Namespace) context.getResources().getFirst().getContents().getFirst();
        Package p = SysmlFactory.eINSTANCE.createPackage();
        p.setDeclaredName("P");
        this.own(existing, p);
        PartDefinition definition = SysmlFactory.eINSTANCE.createPartDefinition();
        definition.setDeclaredName("T");
        this.own(p, definition);
        var annotation = EcoreFactory.eINSTANCE.createEAnnotation();
        annotation.setSource("test.annotation");
        p.getEAnnotations().add(annotation);
        String originalId = definition.getElementId();

        var result = this.parse(context, null, "package Q { import P::*; part t : T; }");

        assertThat(result.messages()).isEmpty();
        assertThat(this.all(result.root(), NamespaceImport.class)).singleElement().satisfies(imp -> assertThat(imp.getImportedNamespace()).isSameAs(p));
        assertThat(this.all(result.root(), FeatureTyping.class)).singleElement().satisfies(typing -> assertThat(typing.getType()).isSameAs(definition));
        assertThat(p.getOwnedMember()).containsExactly(definition);
        assertThat(p.getEAnnotations()).containsExactly(annotation);
        assertThat(definition.getElementId()).isEqualTo(originalId);
        assertThat(context.getResources()).hasSize(1);

        Resource imported = new XMIResourceImpl(URI.createURI("memory:/imported.xmi"));
        context.getResources().add(imported);
        imported.getContents().add(result.root());
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        imported.save(bytes, Map.of());
        context.getResources().remove(imported);
        Resource reloaded = new XMIResourceImpl(imported.getURI());
        context.getResources().add(reloaded);
        reloaded.load(new ByteArrayInputStream(bytes.toByteArray()), Map.of());
        assertThat(this.all(reloaded.getContents().getFirst(), FeatureTyping.class)).singleElement()
                .satisfies(typing -> assertThat(typing.getType()).isSameAs(definition));
    }

    /**
     * The insertion parent must participate in name resolution before any content is committed.
     */
    @DisplayName("GIVEN a contextual type, WHEN inserting a fragment without a semicolon, THEN its local members resolve without modifying the parent")
    @Test
    public void resolvesMembersOfTheInsertionParent() {
        ResourceSet context = this.context();
        Namespace root = (Namespace) context.getResources().getFirst().getContents().getFirst();
        PartDefinition parent = SysmlFactory.eINSTANCE.createPartDefinition();
        parent.setDeclaredName("Parent");
        this.own(root, parent);
        PartDefinition localType = SysmlFactory.eINSTANCE.createPartDefinition();
        localType.setDeclaredName("Local");
        this.own(parent, localType);

        var result = this.parse(context, parent, "part child : Local");

        assertThat(result.messages()).isEmpty();
        assertThat(this.all(result.root(), FeatureTyping.class)).singleElement().satisfies(typing -> assertThat(typing.getType()).isSameAs(localType));
        assertThat(result.root().getOwnedRelationship()).singleElement().isInstanceOf(FeatureMembership.class);
        assertThat(parent.getOwnedMember()).containsExactly(localType);
    }

    /**
     * Contextual fragments must see inherited members, aliases, and imports from the original parent.
     */
    @DisplayName("GIVEN a parent with inheritance, aliases, and imports, WHEN inserting text, THEN references resolve to original context members")
    @Test
    public void resolvesInheritedAndImportedContextMembers() {
        ResourceSet context = this.context();
        var initial = this.parse(context, null, """
                package Existing {
                    part def Base { part sibling; }
                    package More { part def Other; }
                    part def Parent :> Base {
                        part def Local;
                        alias Alias for Local;
                        import More::*;
                    }
                }
                """);
        assertThat(initial.messages()).isEmpty();
        context.getResources().getFirst().getContents().clear();
        context.getResources().getFirst().getContents().add(initial.root());
        PartDefinition parent = this.all(initial.root(), PartDefinition.class).stream().filter(type -> "Parent".equals(type.getDeclaredName())).findFirst().orElseThrow();
        var result = this.parse(context, parent, "part a : Alias; part b : Other; part c subsets sibling;");

        assertThat(result.messages()).isEmpty();
        assertThat(this.all(result.root(), FeatureTyping.class)).extracting(FeatureTyping::getType)
                .hasSize(2).allSatisfy(type -> assertThat(type.eResource()).isSameAs(parent.eResource()));
        assertThat(this.all(result.root(), Subsetting.class)).singleElement()
                .satisfies(subsetting -> assertThat(subsetting.getSubsettedFeature()).isSameAs(this.all(initial.root(), PartUsage.class).getFirst()));
    }

    /**
     * Different enum ordinals/defaults and unset attributes must survive the metamodel boundary.
     */
    @DisplayName("GIVEN enum defaults and unset attributes, WHEN copying between metamodels, THEN literal values and unset states are preserved")
    @Test
    public void preservesEnumLiteralsAndUnsetAttributes() {
        var forward = new SysMLMetamodelCopier(SysmlPackage.eINSTANCE, SysMLPackage.eINSTANCE, Map.of());
        StateSubactionMembership membership = SysmlFactory.eINSTANCE.createStateSubactionMembership();
        var mirrored = (org.omg.sysml.lang.sysml.StateSubactionMembership) forward.copy(membership);
        assertThat(mirrored.getKind().getLiteral()).isEqualTo(membership.getKind().getLiteral());

        PartUsage usage = SysmlFactory.eINSTANCE.createPartUsage();
        var backward = new SysMLMetamodelCopier(SysMLPackage.eINSTANCE, SysmlPackage.eINSTANCE, Map.of());
        PartUsage copy = (PartUsage) backward.copy(forward.copy(usage));
        assertThat(copy.isSetDirection()).isFalse();
        assertThat(copy.isSetPortionKind()).isFalse();
        usage.setDirection(FeatureDirectionKind.INOUT);
        usage.setPortionKind(PortionKind.TIMESLICE);
        copy = (PartUsage) backward.copy(forward.copy(usage));
        assertThat(copy.isSetDirection()).isTrue();
        assertThat(copy.getDirection()).isEqualTo(FeatureDirectionKind.INOUT);
        assertThat(copy.isSetPortionKind()).isTrue();
        assertThat(copy.getPortionKind()).isEqualTo(PortionKind.TIMESLICE);
    }

    /**
     * Recovery must retain usable content and report unsupported or unparsed source instead of silently ignoring it.
     */
    @DisplayName("GIVEN invalid SysML input, WHEN importing text, THEN recoverable content and diagnostics are returned without dangling references")
    @Test
    public void reportsSyntaxAndUnresolvedNames() {
        var unresolved = this.parse(this.context(), null, "package P { part p : Missing; }");
        assertThat(unresolved.root()).isNotNull();
        assertThat(unresolved.messages()).anySatisfy(message -> {
            assertThat(message.body()).contains("Missing");
            assertThat(message.level()).isEqualTo(MessageLevel.WARNING);
        });
        assertThat(this.all(unresolved.root(), FeatureTyping.class)).singleElement().satisfies(typing -> assertThat(typing.getType()).isNull());
        var unsupported = this.parse(this.context(), null, "package P; class C;");
        assertThat(unsupported.messages()).anySatisfy(message -> {
            assertThat(message.body()).contains("line 1:");
            assertThat(message.level()).isEqualTo(MessageLevel.ERROR);
        });
        var malformed = this.parse(this.context(), null, "package P { part a; @@@ }");
        assertThat(malformed.messages()).anyMatch(message -> message.level() == MessageLevel.ERROR);
        var recovered = this.parse(this.context(), null, "package Recovered { part a;; part b; }");
        assertThat(recovered.messages()).anyMatch(message -> message.level() == MessageLevel.ERROR);
        assertThat(recovered.root()).isNotNull();
        assertThat(this.all(recovered.root(), PartUsage.class)).extracting(PartUsage::getDeclaredName).containsExactly("a", "b");
    }

    /**
     * A target succession following explicit successions is invalid and must not be silently accepted.
     */
    @DisplayName("GIVEN a misplaced then after explicit successions, WHEN importing an action, THEN recovery reports a syntax diagnostic")
    @Test
    public void reportsMisplacedJoinTargetSuccession() {
        var result = this.parse(this.context(), null, "action A { join j; first start then j; then done; }");

        assertThat(result.root()).isNotNull();
        assertThat(result.messages()).anySatisfy(message -> {
            assertThat(message.body()).contains("line 1:", "then");
            assertThat(message.level()).isEqualTo(MessageLevel.ERROR);
        });
    }

    @DisplayName("GIVEN a then succession without a semicolon, WHEN importing an action definition, THEN the succession target still resolves")
    @Test
    public void resolvesSuccessionTargetAfterMissingSemicolon() {
        var result = this.parse(this.context(), null, """
                package P {
                  action def A {
                    action x;
                    then x
                    action z;
                  }
                }
                """);

        assertThat(result.messages()).singleElement().satisfies(message -> {
            assertThat(message.body()).contains("line 5:4:", "expecting {';', '{'}");
            assertThat(message.level()).isEqualTo(MessageLevel.ERROR);
        });
        var definition = this.all(result.root(), ActionDefinition.class).getFirst();
        var target = this.all(definition, ActionUsage.class).stream().filter(action -> "x".equals(action.getDeclaredName())).findFirst().orElseThrow();
        assertThat(this.all(definition, SuccessionAsUsage.class)).singleElement().satisfies(succession -> {
            var ends = succession.getOwnedRelationship().stream().filter(EndFeatureMembership.class::isInstance).toList();
            assertThat(ends).hasSize(2);
            assertThat(this.all(ends.getLast(), ReferenceSubsetting.class)).singleElement()
                    .satisfies(subsetting -> assertThat(subsetting.getReferencedFeature()).isSameAs(target));
        });
    }

    /**
     * Exported transition names must reparse to the same original standard-library targets.
     */
    @DisplayName("GIVEN a transition using this and done, WHEN importing, exporting, and reimporting, THEN the original library targets are preserved")
    @Test
    public void preservesTransitionLibraryTargetsAcrossTextualRoundTrip() {
        ResourceSet context = new SysONDefaultLibrariesConfiguration(new SysONLoadDefaultLibrariesOnApplicationStartConfiguration()).getLibrariesResourceSet();
        var imported = this.parse(context, null, """
                package P {
                    attribute def Sig;
                    part p;
                    state def S {
                        state S1;
                        transition S1 accept s : Sig do send s via this to p then done;
                    }
                }
                """);
        assertThat(imported.messages()).isEmpty();
        var resource = new XMIResourceImpl(URI.createURI("memory:/roundtrip.xmi"));
        context.getResources().add(resource);
        resource.getContents().add(imported.root());
        String text = new SysONTextSerializer().serialize(imported.root());
        assertThat(text).contains("then done;");

        var reimported = this.parse(context, null, text);

        assertThat(reimported.messages()).isEmpty();
        var originalThis = this.all(imported.root(), Membership.class).stream().map(Membership::getMemberElement)
                .filter(element -> element != null && "this".equals(element.getName())).findFirst().orElseThrow();
        assertThat(originalThis.getQualifiedName()).isEqualTo("Actions::Action::subactions::this");
        assertThat(this.all(reimported.root(), Membership.class)).anySatisfy(membership -> assertThat(membership.getMemberElement()).isSameAs(originalThis));
        var originalDone = this.all(imported.root(), ReferenceSubsetting.class).stream().map(ReferenceSubsetting::getReferencedFeature)
                .filter(element -> element != null && "done".equals(element.getName())).findFirst().orElseThrow();
        assertThat(originalDone.getQualifiedName()).isEqualTo("States::StateAction::done");
        assertThat(this.all(reimported.root(), ReferenceSubsetting.class)).anySatisfy(subsetting -> assertThat(subsetting.getReferencedFeature()).isSameAs(originalDone));
    }

    /**
     * Bare imports need a default visibility without modifying explicit modifiers or text inside comments and strings.
     */
    @Test
    @DisplayName("GIVEN bare and explicit imports, WHEN importing text, THEN bare visibility is public and explicit visibility is preserved")
    public void suppliesDefaultImportVisibility() {
        var result = this.parse(this.context(), null, """
                package P { part def T; }
                package Q { part def U; }
                package R { part def V; }
                package Uses {
                    /* import is documentation */
                    import P::*;
                    private import Q::*;
                    protected import R::*;
                    public import P::T;
                    attribute label = "import P::*;";
                }
                """);

        assertThat(result.messages()).isEmpty();
        assertThat(this.all(result.root(), NamespaceImport.class)).extracting(NamespaceImport::getVisibility)
                .containsExactly(VisibilityKind.PUBLIC, VisibilityKind.PRIVATE, VisibilityKind.PROTECTED);
        assertThat(this.all(result.root(), MembershipImport.class)).singleElement()
                .satisfies(membership -> assertThat(membership.getVisibility()).isEqualTo(VisibilityKind.PUBLIC));
        assertThat(this.all(result.root(), LiteralString.class)).singleElement()
                .satisfies(literal -> assertThat(literal.getValue()).isEqualTo("import P::*;"));
    }

    /**
     * Synthetic visibility tokens must not shift diagnostics or permit silently ignored source after the root.
     */
    @Test
    @DisplayName("GIVEN bare imports followed by unsupported syntax, WHEN importing text, THEN trailing input is reported at its original location")
    public void reportsTrailingInputAtOriginalLocation() {
        var result = this.parse(this.context(), null, "import Missing::*;\nimport Other::*;\n}");

        assertThat(result.messages()).anySatisfy(message -> {
            assertThat(message.body()).contains("line 3:0: Unexpected trailing input '}'");
            assertThat(message.level()).isEqualTo(MessageLevel.ERROR);
        });
    }

    /**
     * Concurrent workspaces must never resolve names against another project's copy.
     */
    @DisplayName("GIVEN concurrent editing contexts, WHEN importing identical names, THEN each reference targets its own project")
    @Test
    public void keepsConcurrentProjectsIsolated() {
        List<CompletableFuture<SysMLParsingResult>> results = new ArrayList<>();
        for (int index = 0; index < 4; index++) {
            ResourceSet context = this.context();
            Namespace root = (Namespace) context.getResources().getFirst().getContents().getFirst();
            PartDefinition type = SysmlFactory.eINSTANCE.createPartDefinition();
            type.setDeclaredName("Local");
            this.own(root, type);
            results.add(CompletableFuture.supplyAsync(() -> {
                var result = this.parse(context, null, "part p : Local;");
                assertThat(result.messages()).isEmpty();
                assertThat(this.all(result.root(), FeatureTyping.class)).singleElement().satisfies(typing -> assertThat(typing.getType()).isSameAs(type));
                return result;
            }));
        }
        results.forEach(CompletableFuture::join);
    }

    /**
     * Invalid expression edits must not create ownerships or modify existing values and feature-value properties.
     */
    @DisplayName("GIVEN an existing expression and properties, WHEN an edit cannot resolve a name, THEN containment, expression, and properties remain unchanged")
    @Test
    public void leavesFailedExpressionEditsUntouched() {
        ResourceSet context = this.context();
        Namespace root = (Namespace) context.getResources().getFirst().getContents().getFirst();
        AttributeUsage attribute = SysmlFactory.eINSTANCE.createAttributeUsage();
        this.own(root, attribute);
        FeatureValue value = SysmlFactory.eINSTANCE.createFeatureValue();
        value.setIsDefault(true);
        value.setIsInitial(true);
        LiteralInteger expression = SysmlFactory.eINSTANCE.createLiteralInteger();
        value.getOwnedRelatedElement().add(expression);
        attribute.getOwnedRelationship().add(value);
        IEMFEditingContext editingContext = mock(IEMFEditingContext.class);
        when(editingContext.getDomain()).thenReturn(new AdapterFactoryEditingDomain(new ComposedAdapterFactory(), new BasicCommandStack(), context));

        var properties = new ExpressionPropertiesInput(new FeatureValueExpressionPropertiesInput(false, false));
        var result = new SysMLExpressionEditor(this.parsingService).editExpression(editingContext, attribute, expression, "unknownName", properties);

        assertThat(result.createdExpression()).isNull();
        assertThat(result.messages()).anySatisfy(message -> {
            assertThat(message.body()).contains("unknownName");
            assertThat(message.level()).isEqualTo(MessageLevel.WARNING);
        });
        assertThat(attribute.getOwnedRelationship()).containsExactly(value);
        assertThat(value.getOwnedRelatedElement()).containsExactly(expression);
        assertThat(value.isIsDefault()).isTrue();
        assertThat(value.isIsInitial()).isTrue();
    }

    /**
     * Creates a minimal context with stable resource URIs for reference serialization.
     *
     * @return the project context
     */
    private ResourceSet context() {
        ResourceSet context = new ResourceSetImpl();
        context.getPackageRegistry().put(SysmlPackage.eNS_URI, SysmlPackage.eINSTANCE);
        Resource resource = new XMIResourceImpl(URI.createURI("memory:/project.xmi"));
        resource.getContents().add(SysmlFactory.eINSTANCE.createNamespace());
        context.getResources().add(resource);
        return context;
    }

    /**
     * Adds a named member to a namespace.
     *
     * @param parent the owning namespace
     * @param child the member
     */
    private void own(Namespace parent, Element child) {
        OwningMembership membership = SysmlFactory.eINSTANCE.createOwningMembership();
        membership.getOwnedRelatedElement().add(child);
        parent.getOwnedRelationship().add(membership);
    }

    /**
     * Parses text as either a full document or a contextual fragment.
     *
     * @param context the project resources
     * @param parent the optional insertion parent
     * @param text the textual input
     * @return the converted model and messages
     */
    private SysMLParsingResult parse(ResourceSet context, Element parent, String text) {
        return this.parsingService.parseAndLink(context, parent, new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * Finds a kind of element without depending on derived metamodel navigation.
     *
     * @param <T> the expected type
     * @param root the model root
     * @param type the expected type
     * @return the matching objects
     */
    private <T> List<T> all(EObject root, java.lang.Class<T> type) {
        List<T> matches = new ArrayList<>();
        root.eAllContents().forEachRemaining(object -> {
            if (type.isInstance(object)) {
                matches.add(type.cast(object));
            }
        });
        return matches;
    }
}
