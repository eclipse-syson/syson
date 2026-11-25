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
package org.eclipse.syson.application.export;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.syson.application.configuration.SysONDefaultLibrariesConfiguration;
import org.eclipse.syson.application.configuration.SysONLoadDefaultLibrariesOnApplicationStartConfiguration;
import org.eclipse.syson.diagram.common.view.services.ShowDiagramsIconsService;
import org.eclipse.syson.diagram.common.view.services.ViewLabelService;
import org.eclipse.syson.diagram.services.DiagramQueryLabelService;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.Dependency;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.FeatureValue;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.MembershipImport;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.TransitionUsage;
import org.eclipse.syson.sysml.export.SysMLv2DocumentExporter;
import org.eclipse.syson.sysml.metamodel.helper.EMFUtils;
import org.eclipse.syson.sysml.metamodel.services.textual.SysONTextSerializer;
import org.eclipse.syson.sysml.services.SysMLParsingService;
import org.eclipse.syson.sysml.upload.SysMLExternalResourceLoaderService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

/**
 * Exercises imported reference spellings across both metamodel conversions and the document download.
 *
 * @author arichard
 */
class ImportedReferenceExportTests {

    /** Unresolved names and UTF-8 identifiers survive export while the imported objects are loaded. */
    @Test
    void retainsUnresolvedType() {
        var resource = this.upload("package P { part def Known; part 'pompe été' : MissingType; }");
        String unresolved = this.download(resource);
        assertThat(unresolved).contains("part 'pompe été' : MissingType;");
        assertThat(this.typing(this.upload(unresolved)).getType()).isNull();
        var known = EMFUtils.allContainedObjectOfType(resource, PartDefinition.class).findFirst().orElseThrow();
        this.typing(resource).setType(known);
        String resolved = this.download(resource);
        assertThat(resolved).contains("part 'pompe été' : Known;").doesNotContain("MissingType");
        assertThat(this.typing(this.upload(resolved)).getType().getQualifiedName()).isEqualTo("P::Known");
    }

    /** Edits to a target or its namespace supersede imported names, including a subsequently cleared target. */
    @Test
    void exportsCurrentTarget() {
        var resource = this.upload("package P { part def A; } package Q { part def A; } part car : P::A;");
        var typing = this.typing(resource);
        PartDefinition replacement = EMFUtils.allContainedObjectOfType(resource, PartDefinition.class)
                .filter(definition -> "Q".equals(definition.getOwningNamespace().getName())).findFirst().orElseThrow();
        assertThat(this.download(resource)).contains("part car : P::A;");
        typing.setType(replacement);
        var reloaded = this.upload(this.download(resource));
        assertThat(this.typing(reloaded).getType().getQualifiedName()).isEqualTo("Q::A");
        replacement.getOwningNamespace().setDeclaredName("Renamed");
        assertThat(this.download(resource)).contains("part car : Renamed::A;");
        typing.setType(null);
        assertThat(this.download(resource)).doesNotContain("part car : P::A", "part car : Renamed::A");
    }

    /** An alias of a private namespace remains accessible from a client with a conflicting local name. */
    @Test
    void exportsVisibleAlias() {
        var resource = this.upload("""
                package Library { private package Internal { public part def Component; } public alias API for Internal; }
                package Client { package API; part a : Library::API::Component; }
                """);
        var typing = this.typing(resource);
        var target = typing.getType();
        assertThat(target).isNotNull();
        typing.setType(null);
        typing.setType(target);
        String text = this.download(resource);
        assertThat(text).contains("part a : Library::API::Component;");
        assertThat(this.typing(this.upload(text)).getType().getQualifiedName()).isEqualTo(typing.getType().getQualifiedName());
    }

    /** An import of an alias introduces that particular membership even when other aliases name the same target. */
    @Test
    void importsTheChosenAlias() {
        var document = this.upload("""
                package Library { package Internal { part def Component; } alias API for Internal; alias PublicAPI for Internal; }
                package Client { private import Library::PublicAPI; part c : PublicAPI::Component; }
                """);
        var text = this.download(document);
        assertThat(text).contains("private import Library::PublicAPI;");
        var reimported = this.upload(text);
        var declaration = EMFUtils.allContainedObjectOfType(reimported, MembershipImport.class).findFirst().orElseThrow();
        assertThat(declaration.getImportedMembership().getMemberName()).isEqualTo("PublicAPI");
        assertThat(this.typing(reimported).getType().getQualifiedName()).isEqualTo("Library::Internal::Component");
    }

    /** Stored SysON data survives reload, while a spelling without a semantic target lives only in memory. */
    @Test
    void unresolvedSpellingExpiresOnReload() throws IOException {
        var imported = this.upload("package P { part pump : MissingType; }");
        assertThat(this.download(this.reload(imported))).contains("part pump").doesNotContain("MissingType");
    }

    /** A persisted decision still renders its default branch after all parser markers have been discarded. */
    @Test
    void rendersDefaultBranchAfterReload() throws IOException {
        var imported = this.upload("action def A { action a; decide d; if true then a; else a; }");
        String text = this.download(this.reload(imported));
        assertThat(text).contains("if true then a;", "else a;").doesNotContain("in ref ;");
        assertThat(this.download(this.upload(text))).isEqualTo(text);
    }

    /** Loads only the persisted features in a fresh resource. */
    private Resource reload(Resource imported) throws IOException {
        var saved = new XMIResourceImpl(URI.createURI("memory:/saved.xmi"));
        saved.getContents().add(imported.getContents().getFirst());
        var bytes = new ByteArrayOutputStream();
        saved.save(bytes, Map.of());
        var reloaded = new XMIResourceImpl(saved.getURI());
        reloaded.load(new ByteArrayInputStream(bytes.toByteArray()), Map.of());
        return reloaded;
    }

    /** A reference to a renamed package in another document uses its current qualified name. */
    @Test
    void updatesExternalTargetName() {
        var library = this.upload("package Library { part def T; }");
        var resources = library.getResourceSet();
        library.setURI(URI.createURI("memory:/library.sysml"));
        var client = this.upload(resources, "package Client { part part1 : Library::T; }");
        var definition = EMFUtils.allContainedObjectOfType(library, PartDefinition.class).findFirst().orElseThrow();
        assertThat(this.typing(client).getType()).isSameAs(definition);
        definition.getOwningNamespace().setDeclaredName("RenamedLibrary");
        var text = this.download(client);
        assertThat(text).contains("part1 : RenamedLibrary::T;");
        resources.getResources().remove(client);
        assertThat(this.typing(this.upload(resources, text)).getType()).isSameAs(definition);
    }

    /** Every endpoint of a dependency follows its external target when that namespace is renamed. */
    @Test
    void updatesExternalDependencyNames() {
        var library = this.upload("package Lib { part def A; part def B; }");
        var resources = library.getResourceSet();
        library.setURI(URI.createURI("memory:/library.sysml"));
        var document = this.upload(resources, "part x; dependency from x to Lib::A, Lib::B;");
        var dependency = EMFUtils.allContainedObjectOfType(document, Dependency.class).findFirst().orElseThrow();
        assertThat(dependency.getSupplier()).hasSize(2);
        dependency.getSupplier().getFirst().getOwningNamespace().setDeclaredName("Renamed");
        String text = this.download(document);
        assertThat(text).contains("Renamed::A", "Renamed::B").doesNotContain("Lib::");
        resources.getResources().remove(document);
        var reparsed = EMFUtils.allContainedObjectOfType(this.upload(resources, text), Dependency.class).findFirst().orElseThrow();
        assertThat(reparsed.getSupplier()).containsExactlyElementsOf(dependency.getSupplier());
    }

    /** The imported standard-library type is the identical library object after text export and reimport. */
    @Test
    void resolvesStandardLibraryImport() {
        var libraries = new SysONDefaultLibrariesConfiguration(new SysONLoadDefaultLibrariesOnApplicationStartConfiguration()).getLibrariesResourceSet();
        var resource = this.upload(libraries, "private import ScalarValues::Integer; attribute x : Integer;");
        var type = this.typing(resource).getType();
        assertThat(type.getQualifiedName()).isEqualTo("ScalarValues::Integer");
        String text = this.download(resource);
        assertThat(text).contains("private import ScalarValues::Integer;", "x : Integer;");
        libraries.getResources().remove(resource);
        assertThat(this.typing(this.upload(libraries, text)).getType()).isSameAs(type);
    }

    /** The label shows the unit symbol and the editor receives a reference it can reparse. */
    @Test
    void rendersUnitForDisplayAndEditing() {
        String declarations = "package Units { attribute <u> 'a unit'; } ";
        var document = this.upload(declarations + "attribute n = 2 [Units::'a unit'];");
        var value = EMFUtils.allContainedObjectOfType(document, FeatureValue.class).findFirst().orElseThrow();
        var labels = new DiagramQueryLabelService();
        assertThat(labels.getSysmlTextualRepresentation(value.getValue(), false)).isEqualTo("2 [u]");
        String edit = labels.getSysmlTextualRepresentation(value.getValue(), true);
        var reimported = this.upload(declarations + "attribute n = " + edit + ";");
        var unit = EMFUtils.allContainedObjectOfType(reimported, AttributeUsage.class).filter(a -> "u".equals(a.getDeclaredShortName())).findFirst().orElseThrow();
        assertThat(EMFUtils.allContainedObjectOfType(reimported, Membership.class))
                .anyMatch(m -> m.eClass() == SysmlPackage.eINSTANCE.getMembership() && m.getMemberElement() == unit);
    }

    /** Transition labels supply the guard brackets, and the editable trigger can be submitted again. */
    @Test
    void rendersTriggerFragment() {
        String prefix = "package Sm { attribute def Signal; part receiver; state def S { state a; state b; transition first a accept ";
        String suffix = " if true then b; } }";
        var resource = this.upload(prefix + "payload : Signal via receiver" + suffix);
        var transition = EMFUtils.allContainedObjectOfType(resource, TransitionUsage.class).findFirst().orElseThrow();
        var labels = new DiagramQueryLabelService();
        assertThat(labels.getTransitionLabel(transition)).isEqualTo("payload : Signal via receiver [true]");
        var trigger = new SysONTextSerializer().acceptParameterPart(transition.getTriggerAction().getFirst());
        assertThat(trigger).isEqualTo("payload : Signal via receiver");
        var reimported = EMFUtils.allContainedObjectOfType(this.upload(prefix + trigger + suffix), TransitionUsage.class).findFirst().orElseThrow();
        assertThat(labels.getTransitionLabel(reimported)).isEqualTo(labels.getTransitionLabel(transition));
    }

    /** The feedback template supplies the equals sign and keeps its severity and original semantic element. */
    @Test
    void reportsFeatureValue() {
        var document = this.upload("attribute n = 42;");
        var value = EMFUtils.allContainedObjectOfType(document, FeatureValue.class).findFirst().orElseThrow();
        var feedback = mock(IFeedbackMessageService.class);
        var labels = new ViewLabelService(feedback, mock(ShowDiagramsIconsService.class));
        assertThat(labels.sendMessageWithTextualRepresentation("Unable to reconnect (= {0})", "WARNING", value)).isSameAs(value);
        verify(feedback).addFeedbackMessage(new Message("Unable to reconnect (= 42)", MessageLevel.WARNING));
    }

    /** Returns the explicit typing under test. */
    private FeatureTyping typing(Resource resource) {
        return EMFUtils.allContainedObjectOfType(resource, FeatureTyping.class).filter(t -> !t.isIsImplied()).findFirst().orElseThrow();
    }

    /** Loads the given text in a fresh SysON editing context, so reimport cannot bind to the original document. */
    private Resource upload(String text) {
        var resources = new ResourceSetImpl();
        resources.eAdapters().add(new ECrossReferenceAdapter());
        return this.upload(resources, text);
    }

    /** Imports text into an existing editing context that may contain referenced documents. */
    private Resource upload(ResourceSet resources, String text) {
        var loader = new SysMLExternalResourceLoaderService(new SysMLParsingService());
        var bytes = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
        return loader.getResource(bytes, URI.createURI("memory:/test.sysml"), resources, false).orElseThrow().resource();
    }

    /** Runs the downloader with the same media type as the application. */
    private String download(Resource resource) {
        var exporter = new SysMLv2DocumentExporter();
        assertThat(exporter.canHandle(resource, MediaType.TEXT_HTML_VALUE)).isTrue();
        return new String(exporter.getBytes(resource, MediaType.TEXT_HTML_VALUE).orElseThrow(), StandardCharsets.UTF_8);
    }
}
