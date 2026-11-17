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
package org.eclipse.syson.sysml.linker;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Membership;
import org.omg.sysml.lang.sysml.Namespace;
import org.omg.sysml.lang.sysml.VisibilityKind;

/**
 * Builds and manages diagnostics produced while linking a SysML model.
 * <p>
 * An instance owns its diagnostic state and can be used without a linking orchestrator. It is mutable and is not
 * thread-safe.
 * </p>
 *
 * @author Arthur Daussy
 */
public final class DiagnosticBuilder {

    private static final String CANT_RESOLVE_MSG = "Can't resolve %s's reference %s to %s.";

    private static final String CANT_RESOLVE_MSG_VIA_FEATURE_NOT_IN_CONTAINING_TYPE = "Can't resolve %s's reference %s to %s via feature not in containing type.";

    private static final String CANT_FIND_START_CONTEXT_MSG = "The start context to resolve %s's reference %s while resolving %s can't be found.";

    private static final String CANT_BE_ASSIGNED_MSG = "The value %s (type %s) can't be assigned to %s's reference %s (type %s).";

    private static final String NO_OTHER_GLOBAL_NAMESPACES_MSG = "No other global namespaces from namespace %s while resolving %s.";

    private static final String IMPORT_NOT_RESOLVED_MSG = "Imported element not resolved to a membership nor a namespace in namespace %s, it resolved to %s (type %s) (resolving %s).";

    private static final String IMPORT_NO_NAMESPACE_TO_RESOLVE_REMAINING_SEGMENTS_MSG = "Imported element %s from namespace %s is not a namespace (type %s) but %s remains to resolve.";

    private static final String RECURSINVE_IMPORT_NOT_NAMESPACE_MSG = "Recursive import %s (type %s) in namespace %s is not a namespace (resolving %s).";

    private static final String SPECIALIZED_NOT_NAMESPACE_MSG = "Namespace %s specialize %s (type %s) which is not a namespace (resolving %s).";

    private static final String LOCAL_NO_NAMESPACE_TO_RESOLVE_REMAINING_SEGMENTS_MSG = "Local element %s from namespace %s is not a namespace (type %s) but %s remains to resolve.";

    private static final String MEMBERSHIP_REDEFINED_MSG = "The membership %s in namespace %s has been redefined (resolving %s).";

    private static final String NOT_RESOLVED_LOCALLY_MSG = "Namespace %s can't resolve %s locally.";

    private static final String NOT_RESOLVED_SPECIALIZATION_MSG = "Namespace %s can't resolved %s via specializations.";

    private static final String NOT_RESOLVED_IMPORT_MSG = "Namespace %s can't resolved %s via imports.";

    private final List<Diagnostic> diagnostics = new ArrayList<>();

    private final Deque<List<Diagnostic>> diagnosticStack = new ArrayDeque<>();

    /**
     * Creates an empty diagnostic builder.
     */
    public DiagnosticBuilder() {
        // Nothing to initialize.
    }

    /**
     * Starts a diagnostic context for one resolution attempt.
     */
    public void startDiagnosticContext() {
        this.diagnosticStack.addLast(new ArrayList<>());
    }

    /**
     * Ends the current diagnostic context.
     */
    public void endDiagnosticContext() {
        this.diagnosticStack.removeLast();
    }

    /**
     * Clears all diagnostics and diagnostic contexts.
     */
    public void clear() {
        this.diagnostics.clear();
        this.diagnosticStack.clear();
    }

    /**
     * Builds the root {@link Diagnostic}.
     *
     * @return the root {@link Diagnostic}
     */
    public Diagnostic buildDiagnostic() {
        return new BasicDiagnostic(null, 0, this.diagnostics, null, null);
    }

    /**
     * Adds a diagnostic for a missing start context.
     *
     * @param owner
     *            the reference owner
     * @param reference
     *            the reference
     * @param qualifiedName
     *            the name being resolved
     */
    public void addMissingContext(EObject owner, EReference reference, QualifiedName qualifiedName) {
        QualifiedName contextName = this.getDiagnosticQualifiedName(owner);
        String message = String.format(CANT_FIND_START_CONTEXT_MSG, contextName, reference.getName(), qualifiedName);
        this.addRootDiagnostic(Diagnostic.ERROR, message, owner, reference, qualifiedName);
    }

    /**
     * Adds a {@link Diagnostic} for an unresolved reference, including the diagnostics collected in the current
     * context.
     *
     * @param owner
     *            the reference owner
     * @param reference
     *            the reference
     * @param qualifiedName
     *            the unresolved name
     */
    public void addUnresolvedReference(EObject owner, EReference reference, QualifiedName qualifiedName) {
        QualifiedName contextName = this.getDiagnosticQualifiedName(owner);
        String message = String.format(CANT_RESOLVE_MSG, contextName, reference.getName(), qualifiedName);
        Object[] data = new Object[] { owner, reference, qualifiedName };
        this.getDiagnosticContext().add(new BasicDiagnostic(Diagnostic.ERROR, null, 0, message, data));
        this.diagnostics.add(new BasicDiagnostic(null, 0, this.getDiagnosticContext(), message, data));
    }

    /**
     * Adds a {@link Diagnostic} for a value that cannot be assigned to a reference.
     *
     * @param owner
     *            the reference owner
     * @param reference
     *            the reference
     * @param value
     *            the rejected value
     */
    public void addUnassignableValue(EObject owner, EReference reference, Element value) {
        QualifiedName valueName = this.getDiagnosticQualifiedName(value);
        QualifiedName contextName = this.getDiagnosticQualifiedName(owner);
        String message = String.format(CANT_BE_ASSIGNED_MSG, valueName, this.getType(value), contextName, reference.getName(), reference.getEType().getName());
        this.addRootDiagnostic(Diagnostic.ERROR, message, owner, reference, value);
    }

    /**
     * Adds a {@link Diagnostic} when a local element cannot qualify the remaining name segments.
     *
     * @param namespace
     *            the resolution namespace
     * @param localElement
     *            the local element
     * @param qualifiedName
     *            the name being resolved
     */
    public void addLocalElementNotNamespace(Namespace namespace, Element localElement, QualifiedName qualifiedName) {
        QualifiedName localName = this.getDiagnosticQualifiedName(localElement);
        QualifiedName contextName = this.getDiagnosticQualifiedName(namespace);
        String message = String.format(LOCAL_NO_NAMESPACE_TO_RESOLVE_REMAINING_SEGMENTS_MSG, localName, contextName, this.getType(localElement), qualifiedName);
        this.addContextDiagnostic(Diagnostic.WARNING, message, namespace, localElement, qualifiedName);
    }

    /**
     * Adds a {@link Diagnostic} when no other global namespace can be searched.
     *
     * @param namespace
     *            the resolution namespace
     * @param qualifiedName
     *            the name being resolved
     * @param visibilities
     *            the accepted visibilities
     * @param redefined
     *            the redefined memberships
     */
    public void addNoOtherGlobalNamespaces(Namespace namespace, QualifiedName qualifiedName, Set<VisibilityKind> visibilities, Set<Membership> redefined) {
        QualifiedName contextName = this.getDiagnosticQualifiedName(namespace);
        String message = String.format(NO_OTHER_GLOBAL_NAMESPACES_MSG, contextName, qualifiedName);
        this.addContextDiagnostic(Diagnostic.WARNING, message, namespace, qualifiedName, visibilities, redefined);
    }

    /**
     * Adds an informational {@link Diagnostic} for a failed local resolution.
     *
     * @param namespace
     *            the resolution namespace
     * @param qualifiedName
     *            the name being resolved
     */
    public void addNotResolvedLocally(Namespace namespace, QualifiedName qualifiedName) {
        this.addNotResolvedInfo(NOT_RESOLVED_LOCALLY_MSG, namespace, qualifiedName);
    }

    /**
     * Adds an informational {@link Diagnostic} for a failed resolution through specializations.
     *
     * @param namespace
     *            the resolution namespace
     * @param qualifiedName
     *            the name being resolved
     */
    public void addNotResolvedViaSpecializations(Namespace namespace, QualifiedName qualifiedName) {
        this.addNotResolvedInfo(NOT_RESOLVED_SPECIALIZATION_MSG, namespace, qualifiedName);
    }

    /**
     * Adds an informational diagnostic for a failed resolution through imports.
     *
     * @param namespace
     *            the resolution namespace
     * @param qualifiedName
     *            the name being resolved
     */
    public void addNotResolvedViaImports(Namespace namespace, QualifiedName qualifiedName) {
        this.addNotResolvedInfo(NOT_RESOLVED_IMPORT_MSG, namespace, qualifiedName);
    }

    /**
     * Adds a {@link Diagnostic} when an imported element is neither a membership nor a namespace.
     *
     * @param namespace
     *            the importing namespace
     * @param importedElement
     *            the imported element
     * @param qualifiedName
     *            the name being resolved
     * @param visibilities
     *            the accepted visibilities
     */
    public void addImportedElementNotResolved(Namespace namespace, Element importedElement, QualifiedName qualifiedName, Set<VisibilityKind> visibilities) {
        QualifiedName contextName = this.getDiagnosticQualifiedName(namespace);
        QualifiedName importedName = this.getDiagnosticQualifiedName(importedElement);
        String message = String.format(IMPORT_NOT_RESOLVED_MSG, contextName, importedName, this.getType(importedElement), qualifiedName);
        this.addContextAndRootDiagnostic(Diagnostic.ERROR, message, namespace, importedElement, qualifiedName, visibilities);
    }

    /**
     * Adds a {@link Diagnostic} when an imported membership cannot qualify the remaining name segments.
     *
     * @param namespace
     *            the importing namespace
     * @param importedMembership
     *            the imported membership
     * @param qualifiedName
     *            the name being resolved
     */
    public void addImportedElementNotNamespace(Namespace namespace, Membership importedMembership, QualifiedName qualifiedName) {
        QualifiedName importedName = this.getDiagnosticQualifiedName(importedMembership);
        QualifiedName contextName = this.getDiagnosticQualifiedName(namespace);
        String message = String.format(IMPORT_NO_NAMESPACE_TO_RESOLVE_REMAINING_SEGMENTS_MSG, importedName, contextName, this.getType(importedMembership), qualifiedName);
        this.addContextDiagnostic(Diagnostic.WARNING, message, namespace, importedMembership, qualifiedName);
    }

    /**
     * Adds a {@link Diagnostic} when a recursive import targets something other than a namespace.
     *
     * @param namespace
     *            the importing namespace
     * @param importedMembership
     *            the imported membership
     * @param qualifiedName
     *            the name being resolved
     */
    public void addRecursiveImportNotNamespace(Namespace namespace, Membership importedMembership, QualifiedName qualifiedName) {
        QualifiedName importedName = this.getDiagnosticQualifiedName(importedMembership);
        QualifiedName contextName = this.getDiagnosticQualifiedName(namespace);
        String message = String.format(RECURSINVE_IMPORT_NOT_NAMESPACE_MSG, importedName, this.getType(importedMembership), contextName, qualifiedName);
        this.addContextAndRootDiagnostic(Diagnostic.ERROR, message, namespace, importedMembership, qualifiedName);
    }

    /**
     * Adds a {@link Diagnostic} when a specialization target is not a namespace.
     *
     * @param namespace
     *            the specializing namespace
     * @param specializedElement
     *            the specialization target
     * @param qualifiedName
     *            the name being resolved
     */
    public void addSpecializedElementNotNamespace(Namespace namespace, Element specializedElement, QualifiedName qualifiedName) {
        QualifiedName contextName = this.getDiagnosticQualifiedName(namespace);
        String message = String.format(SPECIALIZED_NOT_NAMESPACE_MSG, contextName, specializedElement, this.getType(specializedElement), qualifiedName);
        this.addContextAndRootDiagnostic(Diagnostic.ERROR, message, namespace, specializedElement, qualifiedName);
    }

    /**
     * Adds a {@link Diagnostic} when a local membership has already been redefined.
     *
     * @param namespace
     *            the resolution namespace
     * @param localMembership
     *            the redefined membership
     * @param qualifiedName
     *            the name being resolved
     */
    public void addMembershipRedefined(Namespace namespace, Membership localMembership, QualifiedName qualifiedName) {
        QualifiedName localName = this.getDiagnosticQualifiedName(localMembership);
        QualifiedName contextName = this.getDiagnosticQualifiedName(namespace);
        String message = String.format(MEMBERSHIP_REDEFINED_MSG, localName, contextName, qualifiedName);
        this.addContextDiagnostic(Diagnostic.WARNING, message, namespace, localMembership, qualifiedName);
    }

    /**
     * Adds a {@link Diagnostic} when a redefinition cannot be resolved through a feature in a containing namespace.
     *
     * @param owner
     *            the redefinition
     * @param reference
     *            the redefined-feature reference
     * @param qualifiedName
     *            the unresolved name
     */
    public void addUnresolvedRedefinitionViaContainingFeature(EObject owner, EReference reference, QualifiedName qualifiedName) {
        QualifiedName contextName = this.getDiagnosticQualifiedName(owner);
        String message = String.format(CANT_RESOLVE_MSG_VIA_FEATURE_NOT_IN_CONTAINING_TYPE, contextName, reference.getName(), qualifiedName);
        this.addRootDiagnostic(Diagnostic.ERROR, message, owner, reference, qualifiedName);
    }

    /**
     * Adds a warning {@link Diagnostic} when the global context has an unsupported kind.
     *
     * @param globalContext
     *            the unsupported global context
     */
    public void addUnknownGlobalContextKind(Notifier globalContext) {
        final String message = "Unknown global context kind " + globalContext;
        this.addRootDiagnostic(Diagnostic.WARNING, message);
    }

    /**
     * Adds an informational {@link Diagnostic} for an unsuccessful resolution step.
     *
     * @param messageFormat
     *            the message format
     * @param namespace
     *            the context {@link Namespace}
     * @param qualifiedName
     *            the {@link QualifiedName}
     */
    private void addNotResolvedInfo(String messageFormat, Namespace namespace, QualifiedName qualifiedName) {
        QualifiedName contextName = this.getDiagnosticQualifiedName(namespace);
        String message = String.format(messageFormat, contextName, qualifiedName);
        this.addContextDiagnostic(Diagnostic.INFO, message, namespace, qualifiedName);
    }

    /**
     * Adds a {@link Diagnostic} to the root diagnostic collection.
     *
     * @param severity
     *            the severity of the {@link Diagnostic}
     * @param message
     *            the message
     * @param data
     *            any data that needs to be attached
     */
    private void addRootDiagnostic(int severity, String message, Object... data) {
        this.diagnostics.add(new BasicDiagnostic(severity, null, 0, message, data));
    }

    /**
     * Adds a {@link Diagnostic} to the current diagnostic context.
     *
     * @param severity
     *            the severity of the {@link Diagnostic}
     * @param message
     *            the message
     * @param data
     *            any data that needs to be attached
     */
    private void addContextDiagnostic(int severity, String message, Object... data) {
        this.getDiagnosticContext().add(new BasicDiagnostic(severity, null, 0, message, data));
    }

    /**
     * Adds the same diagnostic to the current context and the root collection.
     *
     * @param severity
     *            the severity of the {@link Diagnostic}
     * @param message
     *            the message
     * @param data
     *            any data that needs to be attached
     */
    private void addContextAndRootDiagnostic(int severity, String message, Object... data) {
        Diagnostic diagnostic = new BasicDiagnostic(severity, null, 0, message, data);
        this.getDiagnosticContext().add(diagnostic);
        this.diagnostics.add(diagnostic);
    }

    /**
     * Gets the active diagnostic context.
     *
     * @return the active diagnostic context
     */
    private List<Diagnostic> getDiagnosticContext() {
        return this.diagnosticStack.getLast();
    }

    /**
     * Gets the qualified name used to identify an object in diagnostics.
     *
     * @param object
     *            the {@link EObject}
     * @return the qualified name used to identify an object in diagnostics
     */
    private QualifiedName getDiagnosticQualifiedName(EObject object) {
        return QualifiedName.getQualifiedNameForDiagnostics(this.getContaining(Membership.class, object));
    }

    /**
     * Gets the metaclass name of an object.
     *
     * @param object
     *            the {@link EObject}
     * @return the metaclass name, or { null}
     */
    private String getType(EObject object) {
        String result = null;
        if (object != null) {
            result = object.eClass().getName();
        }
        return result;
    }

    /**
     * Gets the containing object of the given type.
     *
     */
    /**
     * @param <T>
     *            the kind of container
     * @param type
     *            the Class of the container
     * @param object
     *            the {@link EObject}
     * @return the containing object of the given type if any, <code>null</code> otherwise
     */
    private <T> T getContaining(Class<T> type, EObject object) {
        EObject current = object;
        while (current != null && !type.isInstance(current)) {
            current = current.eContainer();
        }
        return type.cast(current);
    }
}
