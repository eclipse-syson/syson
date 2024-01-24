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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.syson.sysml.FeatureTyping;
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
        Resource resource = loadResourcesFrom(this.resourceSet, "testImport.xmi");
        assertNotNull(resource);
        EObject usage1 = resource.getEObject("3ebdee69-3032-46a9-8fbf-f337034829c9");
        assertNotNull(usage1);
        assertInstanceOf(PartUsage.class, usage1);
        EObject p1Def1 = resource.getEObject("5408ae27-f62d-41c9-813d-f35f8f4d732f");
        assertNotNull(p1Def1);
        assertInstanceOf(PartDefinition.class, p1Def1);
        EObject package1 = resource.getEObject("9a7349aa-57ef-4683-b867-2aaedf6de885");
        assertNotNull(package1);
        EObject package2 = resource.getEObject("70e52d0a-58b3-474a-a0fd-b95d1a859665");
        assertNotNull(package2);
        assertInstanceOf(org.eclipse.syson.sysml.Package.class, package2);

        EList<Import> ownedImport = ((org.eclipse.syson.sysml.Package) package2).getOwnedImport();
        assertEquals(0, ownedImport.size());

        this.importService.handleImport((PartUsage) usage1, (PartDefinition) p1Def1);

        ownedImport = ((org.eclipse.syson.sysml.Package) package2).getOwnedImport();
        assertEquals(1, ownedImport.size());
        Import namespaceImport = ownedImport.get(0);
        assertInstanceOf(NamespaceImport.class, namespaceImport);
        assertEquals(package1, ((NamespaceImport) namespaceImport).getImportedNamespace());
        assertEquals(package2, namespaceImport.getImportOwningNamespace());

        // If an import already handle the definition that is outside the scope, then a new call to handleImport should
        // do nothing.
        this.importService.handleImport((PartUsage) usage1, (PartDefinition) p1Def1);
        ownedImport = ((org.eclipse.syson.sysml.Package) package2).getOwnedImport();
        assertEquals(1, ownedImport.size());
    }

    @DisplayName("A Usage with a type that is a Definition from the same Package")
    @Test
    void testHandleImportForTypeFromSamePackage() {
        Resource resource = loadResourcesFrom(this.resourceSet, "testImport.xmi");
        assertNotNull(resource);
        EObject usage2 = resource.getEObject("28e306bc-1329-4be0-be10-d2b4f792c383");
        assertNotNull(usage2);
        assertInstanceOf(PartUsage.class, usage2);
        EObject p2Def1 = resource.getEObject("1dfe41cb-f833-4b6f-a9ca-df367428cf09");
        assertNotNull(p2Def1);
        assertInstanceOf(PartDefinition.class, p2Def1);
        EObject package1 = resource.getEObject("9a7349aa-57ef-4683-b867-2aaedf6de885");
        assertNotNull(package1);
        EObject package2 = resource.getEObject("70e52d0a-58b3-474a-a0fd-b95d1a859665");
        assertNotNull(package2);
        assertInstanceOf(org.eclipse.syson.sysml.Package.class, package2);

        EList<Import> ownedImport = ((org.eclipse.syson.sysml.Package) package2).getOwnedImport();
        assertEquals(0, ownedImport.size());

        this.importService.handleImport((PartUsage) usage2, (PartDefinition) p2Def1);

        ownedImport = ((org.eclipse.syson.sysml.Package) package2).getOwnedImport();
        assertEquals(0, ownedImport.size());
    }

    @DisplayName("A Usage with a type that is a Definition from another Package, but a recursive import already exists")
    @Test
    void testHandleRecursiveImportForTypeFromAnotherPackage() {
        Resource resource = loadResourcesFrom(this.resourceSet, "testImport.xmi");
        assertNotNull(resource);
        EObject usage1 = resource.getEObject("3ebdee69-3032-46a9-8fbf-f337034829c9");
        assertNotNull(usage1);
        assertInstanceOf(PartUsage.class, usage1);
        EObject p1Def1 = resource.getEObject("5408ae27-f62d-41c9-813d-f35f8f4d732f");
        assertNotNull(p1Def1);
        assertInstanceOf(PartDefinition.class, p1Def1);
        EObject p111Def1 = resource.getEObject("7390a6e5-f063-464d-94de-eba98c5ef85a");
        assertNotNull(p111Def1);
        assertInstanceOf(PartDefinition.class, p111Def1);
        EObject package1 = resource.getEObject("9a7349aa-57ef-4683-b867-2aaedf6de885");
        assertNotNull(package1);
        EObject package2 = resource.getEObject("70e52d0a-58b3-474a-a0fd-b95d1a859665");
        assertNotNull(package2);
        assertInstanceOf(org.eclipse.syson.sysml.Package.class, package2);

        EList<Import> ownedImport = ((org.eclipse.syson.sysml.Package) package2).getOwnedImport();
        assertEquals(0, ownedImport.size());

        this.importService.handleImport((PartUsage) usage1, (PartDefinition) p1Def1);

        ownedImport = ((org.eclipse.syson.sysml.Package) package2).getOwnedImport();
        assertEquals(1, ownedImport.size());
        Import namespaceImport = ownedImport.get(0);
        assertInstanceOf(NamespaceImport.class, namespaceImport);
        assertEquals(package1, ((NamespaceImport) namespaceImport).getImportedNamespace());
        assertEquals(package2, namespaceImport.getImportOwningNamespace());

        // Set the import to recursive
        namespaceImport.setIsRecursive(true);
        // Change the type of usage1 from P1Def1 to P111Def1
        FeatureTyping featureTyping = ((PartUsage) usage1).getOwnedTyping().get(0);
        featureTyping.setType((PartDefinition) p111Def1);
        
        // No new import because the existing recursive import should handle the new type
        this.importService.handleImport((PartUsage) usage1, (PartDefinition) p1Def1);
        ownedImport = ((org.eclipse.syson.sysml.Package) package2).getOwnedImport();
        assertEquals(1, ownedImport.size());
        namespaceImport = ownedImport.get(0);
        assertInstanceOf(NamespaceImport.class, namespaceImport);
        assertEquals(package1, ((NamespaceImport) namespaceImport).getImportedNamespace());
        assertEquals(package2, namespaceImport.getImportOwningNamespace());
    }
}
