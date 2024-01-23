/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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
package org.eclipse.syson.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.syson.sysml.Import;
import org.eclipse.syson.sysml.NamespaceImport;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Import-related Java services tests.
 * 
 * @author arichard
 */
public class ImportServiceTest extends AbstractServiceTest {

    private ResourceSetImpl resourceSet;

    private ImportService importService;

    @BeforeEach
    void beforeEach() {
        this.resourceSet = new ResourceSetImpl();
        this.resourceSet.getPackageRegistry().put(SysmlPackage.eNS_URI, SysmlPackage.eINSTANCE);
        this.importService = new ImportService();
    }

    @DisplayName("A Usage with a type that is a Definition from another Package")
    @Test
    void testHandleImportForTypeFromAnotherPackage() {
        Resource resource = loadResourcesFrom(this.resourceSet, "testHandleImportForTypeFromAnotherPackage.xmi");
        assertNotNull(resource);
        EObject usage = resource.getEObject("3ebdee69-3032-46a9-8fbf-f337034829c9");
        assertNotNull(usage);
        assertInstanceOf(PartUsage.class, usage);
        EObject definition = resource.getEObject("5408ae27-f62d-41c9-813d-f35f8f4d732f");
        assertNotNull(definition);
        assertInstanceOf(PartDefinition.class, definition);
        EObject package1 = resource.getEObject("9a7349aa-57ef-4683-b867-2aaedf6de885");
        assertNotNull(package1);
        EObject package2 = resource.getEObject("70e52d0a-58b3-474a-a0fd-b95d1a859665");
        assertNotNull(package2);
        assertInstanceOf(org.eclipse.syson.sysml.Package.class, package2);
        EList<Import> ownedImport = ((org.eclipse.syson.sysml.Package) package2).getOwnedImport();
        assertEquals(0, ownedImport.size());

        this.importService.handleImport((PartUsage) usage, (PartDefinition) definition);

        ownedImport = ((org.eclipse.syson.sysml.Package) package2).getOwnedImport();
        assertEquals(1, ownedImport.size());
        Import namespaceImport = ownedImport.get(0);
        assertInstanceOf(NamespaceImport.class, namespaceImport);
        assertEquals(package1, ((NamespaceImport) namespaceImport).getImportedNamespace());
        assertEquals(package2, namespaceImport.getImportOwningNamespace());

        // If an import already handle the definition that is outside the scope, then a new call to handleImport should
        // do nothing.
        this.importService.handleImport((PartUsage) usage, (PartDefinition) definition);
        ownedImport = ((org.eclipse.syson.sysml.Package) package2).getOwnedImport();
        assertEquals(1, ownedImport.size());
    }
}
