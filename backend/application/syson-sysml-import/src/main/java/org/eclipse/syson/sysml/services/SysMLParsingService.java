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
package org.eclipse.syson.sysml.services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.linker.AdditionalMemberCompletionService;
import org.eclipse.syson.sysml.linker.MapBasedReferenceSetChecker;
import org.eclipse.syson.sysml.linker.QualifiedName;
import org.eclipse.syson.sysml.linker.SysMLLinker;
import org.eclipse.syson.sysml.metamodel.helper.EMFUtils;
import org.eclipse.syson.sysml.parser.ISysMLParseResult;
import org.eclipse.syson.sysml.parser.SysMLModelParser;
import org.eclipse.syson.sysml.parser.internal.StructuralModelCompletionService;
import org.eclipse.syson.sysml.parser.internal.SysMLLexer;
import org.omg.sysml.lang.sysml.Feature;
import org.omg.sysml.lang.sysml.Membership;
import org.omg.sysml.lang.sysml.NamespaceImport;
import org.omg.sysml.lang.sysml.Package;
import org.omg.sysml.lang.sysml.SysMLFactory;
import org.omg.sysml.lang.sysml.SysMLPackage;
import org.omg.sysml.lang.sysml.Type;
import org.omg.sysml.logic.SysMLLogicStandaloneSetup;
import org.omg.sysml.logic.implicit.specialization.ImplicitSpecializationService;
import org.omg.sysml.logic.implicit.specialization.ImplicitSpecializationServices;
import org.springframework.stereotype.Service;

/**
 * Parses and links text in an isolated OMG model, returning detached SysON content.
 *
 * @author arichard
 */
@Service
public class SysMLParsingService {

    static {
        SysMLLogicStandaloneSetup.doSetup();
    }

    /**
     * Parses UTF-8 input against the project's current resources without modifying existing content.
     * Required SI imports in existing scopes are returned for the caller to commit after accepting the result.
     *
     * @param context the current project resources, including its libraries
     * @param parent the insertion context, or null for a document upload
     * @param input the textual input
     * @return the converted content and diagnostics
     */
    public SysMLParsingResult parseAndLink(ResourceSet context, Element parent, InputStream input) {
        List<Message> messages = new ArrayList<>();
        Map<Namespace, Set<Namespace>> importsToAdd = new LinkedHashMap<>();
        Namespace result = null;
        try {
            String text = new String(input.readAllBytes(), StandardCharsets.UTF_8);
            if (parent != null && !this.isTerminated(text)) {
                text += "\n;";
            }
            ISysMLParseResult<?> parsed = new SysMLModelParser().parseRootNamespace(new SysMLImportLexer(CharStreams.fromString(text)));
            this.addDiagnostics(parsed.diagnostic(), messages);
            if (parsed.model() instanceof org.omg.sysml.lang.sysml.Namespace parsedRoot) {
                result = this.linkAndConvert(context, parent, parsedRoot, parsed, messages, importsToAdd);
            } else {
                messages.add(new Message("Unable to parse the input into a SysML v2 model.", MessageLevel.ERROR));
            }
        } catch (IOException | IllegalArgumentException | IllegalStateException | NullPointerException exception) {
            messages.add(new Message("Unable to import SysML v2: " + exception.getMessage(), MessageLevel.ERROR));
        }
        if (result == null) {
            importsToAdd.clear();
        }
        return new SysMLParsingResult(result, List.copyOf(messages), Map.copyOf(importsToAdd));
    }

    /**
     * Checks the last substantive token so optional editor semicolons remain outside trailing comments.
     *
     * @param text the textual fragment
     * @return whether the fragment is empty or already ends with a delimiter
     */
    private boolean isTerminated(String text) {
        var lexer = new SysMLLexer(CharStreams.fromString(text));
        lexer.removeErrorListeners();
        var tokens = lexer.getAllTokens().stream()
                .filter(token -> token.getChannel() == Token.DEFAULT_CHANNEL)
                .filter(token -> token.getType() != SysMLLexer.REGULAR_COMMENT && token.getType() != SysMLLexer.ML_NOTE && token.getType() != SysMLLexer.SL_NOTE)
                .toList();
        return tokens.isEmpty() || List.of(";", "}").contains(tokens.getLast().getText());
    }

    /**
     * Links newly parsed content against a copy of the complete editing context and converts only that new content.
     * SI imports added to copied context namespaces are returned separately instead of copying existing content back.
     *
     * @param context the current resources
     * @param parent the original insertion context
     * @param root the parsed namespace
     * @param parsed the parser result
     * @param messages the collected diagnostics
     * @param importsToAdd the imports to commit to original namespaces if the caller accepts the result
     * @return a detached SysON namespace containing the imported content
     */
    private Namespace linkAndConvert(ResourceSet context, Element parent, org.omg.sysml.lang.sysml.Namespace root,
            ISysMLParseResult<?> parsed, List<Message> messages, Map<Namespace, Set<Namespace>> importsToAdd) {
        ResourceSet workspace = new ResourceSetImpl();
        workspace.getPackageRegistry().put(SysMLPackage.eNS_URI, SysMLPackage.eINSTANCE);
        ImplicitSpecializationServices.install(workspace, new ImplicitSpecializationService());
        var forward = new SysMLMetamodelCopier(SysmlPackage.eINSTANCE, SysMLPackage.eINSTANCE, Map.of());
        // shortcut: copies the complete context per operation; add snapshot caching only if profiling justifies its invalidation complexity.
        for (Resource resource : List.copyOf(context.getResources())) {
            Resource copy = new XMIResourceImpl(resource.getURI());
            workspace.getResources().add(copy);
            resource.getContents().stream().filter(Element.class::isInstance).forEach(original -> copy.getContents().add(forward.copy(original)));
        }
        forward.copyReferences();
        forward.forEach((original, copy) -> {
            if (original.eResource() != null && copy.eResource() instanceof XMIResourceImpl resource) {
                resource.setID(copy, original.eResource().getURIFragment(original));
            }
        });
        Map<EObject, EObject> originals = new IdentityHashMap<>();
        forward.forEach((original, copy) -> originals.put(copy, original));

        Resource incoming = new XMIResourceImpl(URI.createURI("memory:/sysml-import.xmi"));
        workspace.getResources().add(incoming);
        incoming.getContents().add(root);
        List<org.omg.sysml.lang.sysml.Relationship> relationships = List.copyOf(root.getOwnedRelationship());
        if (parent != null) {
            var mirrorParent = (org.omg.sysml.lang.sysml.Element) forward.get(parent);
            relationships = this.attachToContext(mirrorParent, relationships);
        }

        Map<EObject, Map<EReference, Set<QualifiedName>>> toResolve = new LinkedHashMap<>();
        for (var link : parsed.danglingLinks()) {
            for (String name : link.targets()) {
                toResolve.computeIfAbsent(link.context(), key -> new LinkedHashMap<>())
                        .computeIfAbsent(link.reference(), key -> new LinkedHashSet<>()).add(new QualifiedName(name));
            }
        }
        var completion = new StructuralModelCompletionService(new MapBasedReferenceSetChecker(toResolve));
        List<org.omg.sysml.lang.sysml.Element> imported = this.getElements(relationships);
        imported.forEach(completion::complete);
        var linker = new SysMLLinker(workspace, toResolve);
        linker.resolveAll();
        List<NamespaceImport> addedImports = this.addSIImports(linker.getNotResolved(), workspace);
        if (!addedImports.isEmpty()) {
            linker = new SysMLLinker(workspace, linker.getNotResolved());
            linker.resolveAll();
        }
        if (linker.getNotResolved().isEmpty()) {
            this.addDiagnostics(linker.getDiagnostic(), messages);
        } else {
            linker.getNotResolved().forEach((owner, references) -> references.forEach((reference, names) -> names.forEach(name ->
                    messages.add(new Message("Unable to resolve name '" + name + "' for reference '" + reference.getName() + "' on element '["
                            + owner.eClass().getName() + "] " + this.getDisplayName(owner) + "'", MessageLevel.WARNING)))));
        }
        var additionalMembers = new AdditionalMemberCompletionService();
        this.getElements(relationships).forEach(additionalMembers::complete);

        var backward = new SysMLMetamodelCopier(SysMLPackage.eINSTANCE, SysmlPackage.eINSTANCE, originals);
        Namespace converted;
        if (parent == null) {
            converted = (Namespace) backward.copy(root);
        } else {
            converted = SysmlFactory.eINSTANCE.createNamespace();
            for (var relationship : relationships) {
                converted.getOwnedRelationship().add((org.eclipse.syson.sysml.Relationship) backward.copy(relationship));
            }
        }
        backward.copyReferences();
        backward.retainReferenceText(parsed.danglingLinks());
        for (NamespaceImport namespaceImport : addedImports) {
            if (originals.get(namespaceImport.eContainer()) instanceof Namespace owner
                    && originals.get(namespaceImport.getImportedNamespace()) instanceof Namespace library) {
                importsToAdd.computeIfAbsent(owner, key -> new LinkedHashSet<>()).add(library);
            }
        }
        importsToAdd.replaceAll((owner, libraries) -> Set.copyOf(libraries));
        return converted;
    }

    /**
     * Retries ordinary unresolved names against SI, adding imports only for compatible library targets.
     * All changes remain in the isolated workspace until the caller commits the converted result.
     *
     * @param unresolved the references left unresolved by ordinary linking
     * @param workspace the isolated resources containing the model and its library copies
     * @return the imports added to newly parsed or copied namespaces
     */
    private List<NamespaceImport> addSIImports(Map<EObject, Map<EReference, Set<QualifiedName>>> unresolved, ResourceSet workspace) {
        List<NamespaceImport> addedImports = new ArrayList<>();
        SysMLLinker lookup = new SysMLLinker(workspace, new LinkedHashMap<>());
        for (var entry : unresolved.entrySet()) {
            var namespaces = EMFUtils.getAncestors(org.omg.sysml.lang.sysml.Namespace.class, entry.getKey(), null);
            if (!namespaces.isEmpty()) {
                var owner = EMFUtils.getFirstAncestor(Package.class, entry.getKey(), null)
                        .map(org.omg.sysml.lang.sysml.Namespace.class::cast).orElse(namespaces.getLast());
                for (var referenceEntry : entry.getValue().entrySet()) {
                    for (QualifiedName name : referenceEntry.getValue()) {
                        if (!name.isGlobal()) {
                            var resolved = lookup.resolve(namespaces.getFirst(), new QualifiedName("SI::" + name));
                            if (resolved.isPresent()) {
                                Membership membership = resolved.get();
                                var target = lookup.getMemberElement(membership);
                                if (referenceEntry.getKey() == SysMLPackage.eINSTANCE.getMembershipImport_ImportedMembership()) {
                                    target = membership;
                                }
                                var library = membership.libraryNamespace();
                                if (library != null && referenceEntry.getKey().getEReferenceType().isInstance(target)) {
                                    this.addSIImport(owner, library, addedImports);
                                }
                            }
                        }
                    }
                }
            }
        }
        return addedImports;
    }

    /**
     * Adds one SI namespace import per owner without duplicating an existing import.
     *
     * @param owner the copied or newly parsed namespace receiving the import
     * @param library the library namespace found by SI lookup
     * @param addedImports the imports introduced by this operation
     */
    private void addSIImport(org.omg.sysml.lang.sysml.Namespace owner, org.omg.sysml.lang.sysml.Namespace library, List<NamespaceImport> addedImports) {
        boolean alreadyImported = owner.getOwnedRelationship().stream()
                .filter(NamespaceImport.class::isInstance).map(NamespaceImport.class::cast)
                .anyMatch(namespaceImport -> namespaceImport.getImportedNamespace() == library);
        if (!alreadyImported) {
            NamespaceImport namespaceImport = SysMLFactory.eINSTANCE.createNamespaceImport();
            namespaceImport.setImportedNamespace(library);
            owner.getOwnedRelationship().add(namespaceImport);
            addedImports.add(namespaceImport);
        }
    }

    /**
     * Builds a diagnostic name without exposing the temporary model's generated element IDs or wrapper short names.
     *
     * @param object the unresolved reference's owner
     * @return its user-facing qualified name
     */
    private String getDisplayName(EObject object) {
        List<String> segments = new ArrayList<>();
        EObject current = object;
        while (current != null) {
            if (current instanceof org.omg.sysml.lang.sysml.Element element && element.getDeclaredName() != null) {
                segments.addFirst(element.getDeclaredName());
            }
            current = current.eContainer();
        }
        if (object instanceof org.omg.sysml.lang.sysml.Element element && element.getDeclaredName() == null) {
            segments.add("<" + object.eClass().getName() + ">");
        }
        return String.join("::", segments);
    }

    /**
     * Establishes the same owning scope as the eventual insertion, without touching the real parent.
     *
     * @param parent the copied parent
     * @param relationships the parsed relationships
     * @return the relationships owned by the copied parent
     */
    private List<org.omg.sysml.lang.sysml.Relationship> attachToContext(org.omg.sysml.lang.sysml.Element parent,
            List<org.omg.sysml.lang.sysml.Relationship> relationships) {
        List<org.omg.sysml.lang.sysml.Relationship> attached = new ArrayList<>();
        for (var relationship : relationships) {
            var ownership = relationship;
            if (parent instanceof Type && relationship instanceof org.omg.sysml.lang.sysml.OwningMembership membership
                    && membership.getOwnedMemberElement() instanceof Feature && !(membership instanceof org.omg.sysml.lang.sysml.FeatureMembership)) {
                Membership featureMembership = SysMLFactory.eINSTANCE.createFeatureMembership();
                featureMembership.getOwnedRelatedElement().addAll(List.copyOf(membership.getOwnedRelatedElement()));
                ownership = featureMembership;
            }
            parent.getOwnedRelationship().add(ownership);
            attached.add(ownership);
        }
        return attached;
    }

    /**
     * Collects contained elements before completion, which may add further children.
     *
     * @param roots the roots to traverse
     * @return the contained elements
     */
    private List<org.omg.sysml.lang.sysml.Element> getElements(List<? extends EObject> roots) {
        List<org.omg.sysml.lang.sysml.Element> elements = new ArrayList<>();
        for (EObject root : roots) {
            if (root instanceof org.omg.sysml.lang.sysml.Element element) {
                elements.add(element);
            }
            root.eAllContents().forEachRemaining(object -> {
                if (object instanceof org.omg.sysml.lang.sysml.Element element) {
                    elements.add(element);
                }
            });
        }
        return elements;
    }

    /**
     * Reports user-facing diagnostics once, retaining available parser line and column information.
     *
     * @param diagnostic the parse or link diagnostic
     * @param messages the destination messages
     */
    private void addDiagnostics(Diagnostic diagnostic, List<Message> messages) {
        if (diagnostic.getChildren().isEmpty()) {
            if (diagnostic.getSeverity() >= Diagnostic.WARNING && diagnostic.getMessage() != null) {
                String location = "";
                if (diagnostic.getData().size() >= 2 && diagnostic.getData().get(0) instanceof Integer line && diagnostic.getData().get(1) instanceof Integer column) {
                    location = "line " + line + ":" + column + ": ";
                }
                Message message = new Message(location + diagnostic.getMessage(), this.getMessageLevel(diagnostic));
                if (!messages.contains(message)) {
                    messages.add(message);
                }
            }
        } else {
            diagnostic.getChildren().forEach(child -> this.addDiagnostics(child, messages));
        }
    }

    /**
     * Gets the {@link MessageLevel} for the given {@link Diagnostic}.
     *
     * @param diagnostic
     *            the {@link Diagnostic}
     * @return the {@link MessageLevel} for the given {@link Diagnostic}
     */
    private MessageLevel getMessageLevel(Diagnostic diagnostic) {
        MessageLevel result;

        switch (diagnostic.getSeverity()) {
            case Diagnostic.OK:
                result = MessageLevel.SUCCESS;
                break;

            case Diagnostic.INFO:
                result = MessageLevel.INFO;
                break;

            case Diagnostic.WARNING:
                result = MessageLevel.WARNING;
                break;

            case Diagnostic.ERROR:
                result = MessageLevel.ERROR;
                break;

            default:
                result = MessageLevel.ERROR;
        }

        return result;
    }
}
