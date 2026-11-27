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

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.sirius.web.application.document.services.LoadingReport;
import org.eclipse.syson.sysml.dto.ExpressionPropertiesInput;
import org.eclipse.syson.sysml.dto.FeatureValueExpressionPropertiesInput;
import org.eclipse.syson.sysml.services.SysMLExpressionEditor;
import org.eclipse.syson.sysml.services.SysMLParsingResult;
import org.eclipse.syson.sysml.services.SysMLParsingService;
import org.eclipse.syson.sysml.services.SysMLTextImporter;
import org.eclipse.syson.sysml.upload.SysMLExternalResourceLoaderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Verifies SI fallback resolution and import commits across the isolated metamodel boundary.
 *
 * @author arichard
 */
public class SysMLSIImportTests {

    private final SysMLParsingService service = new SysMLParsingService();

    private ResourceSet context;

    private Namespace root;

    private LibraryPackage library;

    /**
     * Creates a project and a small, separate SI library with stable resource URIs.
     */
    @BeforeEach
    public void setUp() {
        this.context = new ResourceSetImpl();
        this.context.getPackageRegistry().put(SysmlPackage.eNS_URI, SysmlPackage.eINSTANCE);
        var project = new XMIResourceImpl(URI.createURI("memory:/project.xmi"));
        this.root = SysmlFactory.eINSTANCE.createNamespace();
        project.getContents().add(this.root);
        this.context.getResources().add(project);
        var libraries = new XMIResourceImpl(URI.createURI("memory:/SI.xmi"));
        var libraryRoot = SysmlFactory.eINSTANCE.createNamespace();
        libraries.getContents().add(libraryRoot);
        this.context.getResources().add(libraries);
        this.library = SysmlFactory.eINSTANCE.createLibraryPackage();
        this.library.setDeclaredName("SI");
        this.own(libraryRoot, this.library);
        for (String name : List.of("kg", "g")) {
            var unit = SysmlFactory.eINSTANCE.createAttributeUsage();
            unit.setDeclaredName(name);
            this.own(this.library, unit);
        }
    }

    /**
     * Imports belong to the nearest new package, or the document root when no package exists.
     *
     * @param nested whether the aliases are declared in nested packages
     */
    @DisplayName("GIVEN unresolved SI members, WHEN importing a document, THEN one import is added at the nearest package or root")
    @ParameterizedTest
    @ValueSource(booleans = { false, true })
    public void importsSIOnceAtTheCorrectScope(boolean nested) {
        String text = "alias mass for kg; alias smallMass for g; alias otherMass for kg;";
        if (nested) {
            text = "package outer { package inner { " + text + " } }";
        }

        var result = this.parse(null, text);

        assertThat(result.messages()).isEmpty();
        assertThat(result.importsToAdd()).isEmpty();
        assertThat(this.all(result.root(), NamespaceImport.class)).singleElement().satisfies(namespaceImport -> {
            assertThat(namespaceImport.getImportedNamespace()).isSameAs(this.library);
            if (nested) {
                assertThat(((Namespace) namespaceImport.eContainer()).getDeclaredName()).isEqualTo("inner");
            } else {
                assertThat(namespaceImport.eContainer()).isSameAs(result.root());
            }
        });
        assertThat(this.aliasTarget(result.root(), "mass")).isSameAs(this.library.getOwnedMember().getFirst());
        assertThat(this.aliasTarget(result.root(), "otherMass")).isSameAs(this.aliasTarget(result.root(), "mass"));
        assertThat(this.root.getOwnedRelationship()).isEmpty();
        assertThat(this.library.getOwnedMember()).hasSize(2);
    }

    /**
     * Qualified names and explicit imports use normal linking without additional imports.
     *
     * @param explicitImport whether the document already imports SI
     */
    @DisplayName("GIVEN qualified SI references or an explicit import, WHEN linking, THEN no duplicate import is introduced")
    @ParameterizedTest
    @ValueSource(booleans = { false, true })
    public void reusesNormalResolution(boolean explicitImport) {
        String text = "package model { alias mass for SI::kg; }";
        if (explicitImport) {
            text = "package model { private import SI::*; alias mass for kg; alias smallMass for g; }";
        }
        var result = this.parse(null, text);

        assertThat(result.messages()).isEmpty();
        if (explicitImport) {
            assertThat(this.all(result.root(), NamespaceImport.class)).hasSize(1);
        } else {
            assertThat(this.all(result.root(), NamespaceImport.class)).isEmpty();
        }
        assertThat(this.aliasTarget(result.root(), "mass")).isSameAs(this.library.getOwnedMember().getFirst());
    }

    /**
     * SI fallback must not replace names already resolved locally or through explicit imports.
     */
    @DisplayName("GIVEN local and explicitly imported unit names, WHEN another name needs SI fallback, THEN normal resolution keeps precedence")
    @Test
    public void preservesLocalAndExplicitImportPrecedence() {
        var metre = SysmlFactory.eINSTANCE.createAttributeUsage();
        metre.setDeclaredName("metre");
        this.own(this.library, metre);
        var result = this.parse(null, """
                package Units { attribute kg; }
                package model {
                    private import Units::*;
                    attribute g;
                    alias mass for kg;
                    alias smallMass for g;
                    alias fallback for metre;
                }
                """);
        assertThat(result.messages()).isEmpty();
        assertThat(this.aliasTarget(result.root(), "mass").getOwningNamespace().getDeclaredName()).isEqualTo("Units");
        assertThat(this.aliasTarget(result.root(), "smallMass").getOwningNamespace().getDeclaredName()).isEqualTo("model");
        assertThat(this.all(result.root(), NamespaceImport.class)).hasSize(2);

        var fallback = this.parse(null, "package model { attribute kg; alias mass for kg; alias smallMass for g; }");
        assertThat(fallback.messages()).isEmpty();
        assertThat(this.aliasTarget(fallback.root(), "mass").getOwningNamespace().getDeclaredName()).isEqualTo("model");
        assertThat(this.aliasTarget(fallback.root(), "smallMass")).isSameAs(this.library.resolve("g").getMemberElement());
    }

    /**
     * Reference type checks apply to SI fallback, and explicit global names never receive the SI prefix.
     */
    @DisplayName("GIVEN an incompatible or global unresolved target, WHEN linking, THEN no SI import is added and the target stays unset")
    @Test
    public void rejectsIncompatibleAndGlobalTargets() {
        var incompatible = this.parse(null, "package model { part def Invalid :> kg; }");
        assertThat(incompatible.messages()).anySatisfy(message -> {
            assertThat(message.body()).contains("kg");
            assertThat(message.level()).isEqualTo(MessageLevel.WARNING);
        });
        assertThat(this.all(incompatible.root(), NamespaceImport.class)).isEmpty();
        assertThat(this.all(incompatible.root(), Subclassification.class)).singleElement()
                .satisfies(specialization -> assertThat(specialization.getSuperclassifier()).isNull());

        var global = this.parse(null, "package model { alias mass for $::kg; }");
        assertThat(global.messages()).anySatisfy(message -> {
            assertThat(message.body()).contains("kg");
            assertThat(message.level()).isEqualTo(MessageLevel.WARNING);
        });
        assertThat(this.all(global.root(), NamespaceImport.class)).isEmpty();
    }

    /**
     * Membership imports require the membership itself rather than its member element.
     */
    @DisplayName("GIVEN an unresolved membership import, WHEN its target is in SI, THEN the original membership is imported")
    @Test
    public void resolvesMembershipImport() {
        var result = this.parse(null, "package model { private import kg; }");

        assertThat(result.messages()).isEmpty();
        assertThat(this.all(result.root(), MembershipImport.class)).singleElement()
                .satisfies(namespaceImport -> assertThat(namespaceImport.getImportedMembership()).isSameAs(this.library.getOwnedMembership().getFirst()));
        assertThat(this.all(result.root(), NamespaceImport.class)).hasSize(1);
    }

    /**
     * Parsing must not mutate existing scopes, and a repeated commit must not duplicate imports.
     *
     * @param packaged whether the existing attribute is owned by a package rather than the root namespace
     */
    @DisplayName("GIVEN an existing package or root scope, WHEN parsing then committing SI references, THEN imports are deferred and deduplicated")
    @ParameterizedTest
    @ValueSource(booleans = { false, true })
    public void defersImportsUntilCommit(boolean packaged) {
        var parent = this.parent();
        if (!packaged) {
            this.root.getOwnedRelationship().add(parent.getOwningMembership());
        }
        var first = this.parse(parent, "attribute value = 1 [g];");
        var second = this.parse(parent, "attribute value = 2 [kg];");

        assertThat(first.messages()).isEmpty();
        assertThat(second.messages()).isEmpty();
        assertThat(this.all(this.root, NamespaceImport.class)).isEmpty();
        assertThat(first.importsToAdd()).containsOnlyKeys((Namespace) parent.getOwner());
        assertThat(first.importsToAdd().values()).singleElement().satisfies(libraries -> assertThat(libraries).containsExactly(this.library));
        first.applyImports();
        second.applyImports();
        assertThat(this.all(this.root, NamespaceImport.class)).singleElement()
                .satisfies(namespaceImport -> assertThat(namespaceImport.getImportedNamespace()).isSameAs(this.library));
    }

    /**
     * Successful expression edits commit their SI import and keep references to the original library after reload.
     */
    @DisplayName("GIVEN an expression without an SI import, WHEN editing with an SI unit, THEN its import and original library references survive reload")
    @Test
    public void commitsExpressionImportAndPreservesIdentityOnReload() throws Exception {
        var parent = this.parent();
        var value = this.value(parent);
        var result = new SysMLExpressionEditor(this.service).editExpression(this.editingContext(), parent, value.getValue(), "45 [kg]", null);

        assertThat(result.messages()).isEmpty();
        assertThat(result.createdExpression()).isNotNull();
        assertThat(this.all(this.root, NamespaceImport.class)).singleElement()
                .satisfies(namespaceImport -> assertThat(namespaceImport.getImportedNamespace()).isSameAs(this.library));
        assertThat(this.all(result.createdExpression(), Membership.class)).anySatisfy(membership ->
                assertThat(membership.getMemberElement()).isSameAs(this.library.getOwnedMember().getFirst()));
        var resource = this.root.eResource();
        var bytes = new ByteArrayOutputStream();
        resource.save(bytes, Map.of());
        this.context.getResources().remove(resource);
        var reloaded = new XMIResourceImpl(resource.getURI());
        this.context.getResources().add(reloaded);
        reloaded.load(new ByteArrayInputStream(bytes.toByteArray()), Map.of());
        assertThat(this.all(reloaded.getContents().getFirst(), NamespaceImport.class)).singleElement()
                .satisfies(namespaceImport -> assertThat(namespaceImport.getImportedNamespace()).isSameAs(this.library));
        assertThat(this.all(reloaded.getContents().getFirst(), Membership.class)).anySatisfy(membership ->
                assertThat(membership.getMemberElement()).isSameAs(this.library.getOwnedMember().getFirst()));
    }

    /**
     * Failure after SI fallback must preserve the existing expression, properties, and imports.
     *
     * @param existingImport whether the parent package already imports SI
     */
    @DisplayName("GIVEN an existing expression, WHEN an SI edit also contains an unknown name, THEN its value, properties, and imports are unchanged")
    @ParameterizedTest
    @ValueSource(booleans = { false, true })
    public void failedExpressionPreservesContentAndImports(boolean existingImport) {
        var parent = this.parent();
        var value = this.value(parent);
        var expression = value.getValue();
        if (existingImport) {
            var namespaceImport = SysmlFactory.eINSTANCE.createNamespaceImport();
            namespaceImport.setImportedNamespace(this.library);
            parent.getOwner().getOwnedRelationship().add(namespaceImport);
        }
        var imports = this.all(this.root, NamespaceImport.class);
        var properties = new ExpressionPropertiesInput(new FeatureValueExpressionPropertiesInput(false, false));

        var result = new SysMLExpressionEditor(this.service).editExpression(this.editingContext(), parent, expression, "50 [g] * unknownUnit", properties);

        assertThat(result.createdExpression()).isNull();
        assertThat(result.messages()).anySatisfy(message -> {
            assertThat(message.body()).contains("unknownUnit");
            assertThat(message.level()).isEqualTo(MessageLevel.WARNING);
        });
        assertThat(result.messages()).noneMatch(message -> message.body().contains("resolve name 'g'"));
        assertThat(parent.getOwnedRelationship()).containsExactly(value);
        assertThat(value.getValue()).isSameAs(expression);
        assertThat(value.isIsDefault()).isTrue();
        assertThat(value.isIsInitial()).isTrue();
        assertThat(this.all(this.root, NamespaceImport.class)).containsExactlyElementsOf(imports);
    }

    /**
     * Recoverable insertion commits its useful SI import while reporting the remaining unresolved name.
     */
    @DisplayName("GIVEN a recoverable textual insertion, WHEN one name resolves through SI, THEN the import is committed and only unresolved names are reported")
    @Test
    public void commitsImportsForRecoverableInsertion() {
        var parent = this.parent();
        List<Message> messages = new ArrayList<>();

        var elements = new SysMLTextImporter(this.service).importSysMLText(this.editingContext(), parent, "attribute mass = 1 [g]; attribute other = unknownUnit;", messages);

        assertThat(elements).hasSize(2);
        assertThat(messages).anySatisfy(message -> {
            assertThat(message.body()).contains("unknownUnit");
            assertThat(message.level()).isEqualTo(MessageLevel.WARNING);
        });
        assertThat(messages).noneMatch(message -> message.body().contains("resolve name 'g'"));
        assertThat(this.all(this.root, NamespaceImport.class)).singleElement()
                .satisfies(namespaceImport -> assertThat(namespaceImport.getImportedNamespace()).isSameAs(this.library));
    }

    /**
     * Recoverable uploads retain required imports in their new content without modifying the existing project.
     */
    @DisplayName("GIVEN a recoverable upload using SI, WHEN loading the resource, THEN its import targets the original library and existing content stays unchanged")
    @Test
    public void retainsImportsForRecoverableUpload() {
        var loader = new SysMLExternalResourceLoaderService(this.service);
        var input = new ByteArrayInputStream("package imported { attribute mass = 1 [g]; attribute other = unknownUnit; }".getBytes(StandardCharsets.UTF_8));

        var loaded = loader.getResource(input, URI.createURI("memory:/uploaded.sysml"), this.context, false).orElseThrow();

        assertThat(loaded.loadingReport()).isInstanceOfSatisfying(LoadingReport.class, report ->
                assertThat(report.content()).anySatisfy(message -> assertThat(message).startsWith("WARNING: ").contains("unknownUnit")));
        assertThat(this.all(loaded.resource().getContents().getFirst(), NamespaceImport.class)).singleElement()
                .satisfies(namespaceImport -> assertThat(namespaceImport.getImportedNamespace()).isSameAs(this.library));
        assertThat(this.root.getOwnedRelationship()).isEmpty();
        assertThat(this.library.getOwnedMember()).hasSize(2);
    }

    /**
     * Creates an existing attribute in a package for contextual expression operations.
     *
     * @return the existing attribute
     */
    private AttributeUsage parent() {
        var owner = SysmlFactory.eINSTANCE.createPackage();
        owner.setDeclaredName("Existing");
        this.own(this.root, owner);
        var parent = SysmlFactory.eINSTANCE.createAttributeUsage();
        parent.setDeclaredName("x");
        this.own(owner, parent);
        return parent;
    }

    /**
     * Installs an existing value with properties that a failed edit must preserve.
     *
     * @param parent the attribute owning the value
     * @return the existing feature value
     */
    private FeatureValue value(AttributeUsage parent) {
        var value = SysmlFactory.eINSTANCE.createFeatureValue();
        value.setIsDefault(true);
        value.setIsInitial(true);
        value.getOwnedRelatedElement().add(SysmlFactory.eINSTANCE.createLiteralInteger());
        parent.getOwnedRelationship().add(value);
        return value;
    }

    /**
     * Exposes the test resources through an editing context.
     *
     * @return the editing context
     */
    private IEMFEditingContext editingContext() {
        var editingContext = mock(IEMFEditingContext.class);
        when(editingContext.getDomain()).thenReturn(new AdapterFactoryEditingDomain(new ComposedAdapterFactory(), new BasicCommandStack(), this.context));
        return editingContext;
    }

    /**
     * Parses text without committing the returned content or pending imports.
     *
     * @param parent the optional insertion context
     * @param text the input text
     * @return the detached result
     */
    private SysMLParsingResult parse(Element parent, String text) {
        return this.service.parseAndLink(this.context, parent, new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * Adds an owning membership to a namespace.
     *
     * @param owner the owning namespace
     * @param child the owned element
     */
    private void own(Namespace owner, Element child) {
        var membership = SysmlFactory.eINSTANCE.createOwningMembership();
        membership.getOwnedRelatedElement().add(child);
        owner.getOwnedRelationship().add(membership);
    }

    /**
     * Collects contained objects of a semantic type.
     *
     * @param <T> the expected type
     * @param owner the containment root
     * @param type the expected class
     * @return the contained matches
     */
    private <T> List<T> all(EObject owner, java.lang.Class<T> type) {
        List<T> result = new ArrayList<>();
        owner.eAllContents().forEachRemaining(object -> {
            if (type.isInstance(object)) {
                result.add(type.cast(object));
            }
        });
        return result;
    }

    /**
     * Gets the target of a named alias.
     *
     * @param owner the parsed containment root
     * @param name the alias name
     * @return its resolved target
     */
    private Element aliasTarget(EObject owner, String name) {
        return this.all(owner, Membership.class).stream().filter(membership -> name.equals(membership.getMemberName()))
                .findFirst().orElseThrow().getMemberElement();
    }
}
