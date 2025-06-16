/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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
package org.eclipse.syson.application.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link GetChildCreationSwitch}.
 *
 * @author gdaniel
 */
public class GetChildCreationSwitchTest {

    @Test
    public void testPackageChildren() {
        List<EClass> children = new GetChildCreationSwitch().doSwitch(SysmlFactory.eINSTANCE.createPackage());
        List<EClass> expectedChildren = List.of(SysmlPackage.eINSTANCE.getDependency(),
                SysmlPackage.eINSTANCE.getDocumentation(),
                SysmlPackage.eINSTANCE.getComment(),
                SysmlPackage.eINSTANCE.getTextualRepresentation(),
                SysmlPackage.eINSTANCE.getOwningMembership(),
                SysmlPackage.eINSTANCE.getPackage());
        assertThat(children).containsAll(expectedChildren);
        assertThat(children).noneMatch(eClass -> eClass.isAbstract() || eClass.isInterface());
        children.removeAll(expectedChildren);
        // Ensure all the remaining types are subclasses of expected types.
        assertThat(children).allMatch(eClass -> SysmlPackage.eINSTANCE.getDefinition().isSuperTypeOf(eClass)
                || SysmlPackage.eINSTANCE.getUsage().isSuperTypeOf(eClass)
                || SysmlPackage.eINSTANCE.getImport().isSuperTypeOf(eClass)
                || SysmlPackage.eINSTANCE.getPackage().isSuperTypeOf(eClass));
    }

    @Test
    public void testNamespaceChildren() {
        List<EClass> children = new GetChildCreationSwitch().doSwitch(SysmlFactory.eINSTANCE.createNamespace());
        List<EClass> expectedChildren = List.of(SysmlPackage.eINSTANCE.getDependency(),
                SysmlPackage.eINSTANCE.getDocumentation(),
                SysmlPackage.eINSTANCE.getComment(),
                SysmlPackage.eINSTANCE.getTextualRepresentation(),
                SysmlPackage.eINSTANCE.getOwningMembership(),
                SysmlPackage.eINSTANCE.getPackage());
        assertThat(children).containsAll(expectedChildren);
        assertThat(children).noneMatch(eClass -> eClass.isAbstract() || eClass.isInterface());
        children.removeAll(expectedChildren);
        // Ensure all the remaining types are subclasses of expected types.
        assertThat(children).allMatch(eClass -> SysmlPackage.eINSTANCE.getDefinition().isSuperTypeOf(eClass)
                || SysmlPackage.eINSTANCE.getUsage().isSuperTypeOf(eClass)
                || SysmlPackage.eINSTANCE.getImport().isSuperTypeOf(eClass)
                || SysmlPackage.eINSTANCE.getPackage().isSuperTypeOf(eClass));
    }

    @Test
    public void testViewUsageChildren() {
        List<EClass> children = new GetChildCreationSwitch().doSwitch(SysmlFactory.eINSTANCE.createViewUsage());
        List<EClass> expectedChildren = List.of(SysmlPackage.eINSTANCE.getMembershipExpose(),
                SysmlPackage.eINSTANCE.getNamespaceExpose(),
                SysmlPackage.eINSTANCE.getDocumentation(),
                SysmlPackage.eINSTANCE.getComment(),
                SysmlPackage.eINSTANCE.getTextualRepresentation(),
                SysmlPackage.eINSTANCE.getViewUsage());
        assertThat(children).containsAll(expectedChildren);
        children.removeAll(expectedChildren);
        assertThat(children).isEmpty();
    }
}
