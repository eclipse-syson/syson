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
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.syson.sysml.linker.DiagnosticBuilder;
import org.eclipse.syson.sysml.linker.QualifiedName;
import org.junit.BeforeClass;
import org.junit.Test;
import org.omg.sysml.lang.sysml.Membership;
import org.omg.sysml.lang.sysml.Namespace;
import org.omg.sysml.lang.sysml.Redefinition;
import org.omg.sysml.lang.sysml.SysMLFactory;
import org.omg.sysml.lang.sysml.SysMLPackage;
import org.omg.sysml.logic.SysMLLogicStandaloneSetup;

/**
 * Tests {@link DiagnosticBuilder}.
 *
 * @author Arthur Daussy
 */
public class DiagnosticBuilderTests {

    private static final EReference REDEFINED_FEATURE = SysMLPackage.Literals.REDEFINITION__REDEFINED_FEATURE;

    /**
     * Installs OMG logic delegates before tests create SysML elements.
     */
    @BeforeClass
    public static void beforeClass() {
        SysMLLogicStandaloneSetup.doSetup();
    }

    @Test
    public void newBuilderProducesAnEmptyDiagnostic() {
        Diagnostic diagnostic = new DiagnosticBuilder().buildDiagnostic();

        assertEquals(Diagnostic.OK, diagnostic.getSeverity());
        assertTrue(diagnostic.getChildren().isEmpty());
    }

    @Test
    public void unresolvedReferenceRetainsContextInInsertionOrder() {
        DiagnosticBuilder builder = new DiagnosticBuilder();
        Namespace namespace = SysMLFactory.eINSTANCE.createNamespace();
        Redefinition owner = SysMLFactory.eINSTANCE.createRedefinition();
        QualifiedName qualifiedName = new QualifiedName("missing");

        builder.startDiagnosticContext();
        builder.addNotResolvedLocally(namespace, qualifiedName);
        builder.addNotResolvedViaSpecializations(namespace, qualifiedName);
        builder.addUnresolvedReference(owner, REDEFINED_FEATURE, qualifiedName);
        builder.endDiagnosticContext();

        Diagnostic root = builder.buildDiagnostic();
        assertEquals(Diagnostic.ERROR, root.getSeverity());
        assertEquals(1, root.getChildren().size());
        Diagnostic unresolved = root.getChildren().get(0);
        assertEquals("Can't resolve $'s reference redefinedFeature to missing.", unresolved.getMessage());
        assertEquals(3, unresolved.getChildren().size());
        assertEquals(Diagnostic.INFO, unresolved.getChildren().get(0).getSeverity());
        assertEquals("Namespace $ can't resolve missing locally.", unresolved.getChildren().get(0).getMessage());
        assertEquals(Diagnostic.INFO, unresolved.getChildren().get(1).getSeverity());
        assertEquals("Namespace $ can't resolved missing via specializations.", unresolved.getChildren().get(1).getMessage());
        assertEquals(Diagnostic.ERROR, unresolved.getChildren().get(2).getSeverity());
        assertEquals(unresolved.getMessage(), unresolved.getChildren().get(2).getMessage());
    }

    @Test
    public void endingAnUnmaterializedContextDiscardsItsDiagnostics() {
        DiagnosticBuilder builder = new DiagnosticBuilder();

        builder.startDiagnosticContext();
        builder.addNotResolvedLocally(SysMLFactory.eINSTANCE.createNamespace(), new QualifiedName("missing"));
        builder.endDiagnosticContext();

        assertTrue(builder.buildDiagnostic().getChildren().isEmpty());
    }

    @Test
    public void structuralErrorIsSharedByContextAndRoot() {
        DiagnosticBuilder builder = new DiagnosticBuilder();
        Namespace namespace = SysMLFactory.eINSTANCE.createNamespace();
        Membership importedMembership = SysMLFactory.eINSTANCE.createMembership();
        Redefinition owner = SysMLFactory.eINSTANCE.createRedefinition();
        QualifiedName qualifiedName = new QualifiedName("missing");

        builder.startDiagnosticContext();
        builder.addRecursiveImportNotNamespace(namespace, importedMembership, qualifiedName);
        builder.addUnresolvedReference(owner, REDEFINED_FEATURE, qualifiedName);
        builder.endDiagnosticContext();

        Diagnostic root = builder.buildDiagnostic();
        assertEquals(2, root.getChildren().size());
        Diagnostic structuralError = root.getChildren().get(0);
        Diagnostic unresolved = root.getChildren().get(1);
        assertSame(structuralError, unresolved.getChildren().get(0));
    }

    @Test
    public void clearResetsDiagnosticsAndContexts() {
        DiagnosticBuilder builder = new DiagnosticBuilder();
        builder.startDiagnosticContext();
        builder.addMissingContext(SysMLFactory.eINSTANCE.createRedefinition(), REDEFINED_FEATURE, new QualifiedName("missing"));

        builder.clear();

        assertTrue(builder.buildDiagnostic().getChildren().isEmpty());
        builder.startDiagnosticContext();
        builder.endDiagnosticContext();
    }

    @Test
    public void instancesHaveIndependentState() {
        DiagnosticBuilder first = new DiagnosticBuilder();
        DiagnosticBuilder second = new DiagnosticBuilder();
        first.addMissingContext(SysMLFactory.eINSTANCE.createRedefinition(), REDEFINED_FEATURE, new QualifiedName("missing"));

        Diagnostic firstDiagnostic = first.buildDiagnostic();
        Diagnostic secondDiagnostic = second.buildDiagnostic();
        assertNotSame(firstDiagnostic, secondDiagnostic);
        assertEquals(1, firstDiagnostic.getChildren().size());
        assertTrue(secondDiagnostic.getChildren().isEmpty());
    }
}
