/*******************************************************************************
 * Copyright (c) 2024, 2026 Obeo.
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link GetChildCreationSwitch}.
 *
 * @author gdaniel
 */
public class GetChildCreationSwitchTest {

    @DisplayName("Check the list of children that can be created from a Package")
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

    @DisplayName("Check the list of children that can be created from a Namespace")
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

    @DisplayName("Check the list of children that can be created from a ViewUsage")
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

    @DisplayName("Check the list of children that can be created from a RequirementDefinition")
    @Test
    public void testRequirementDefinitionChildren() {
        List<EClass> children = new GetChildCreationSwitch().doSwitch(SysmlFactory.eINSTANCE.createRequirementDefinition());
        List<EClass> expectedChildren = List.of(
                SysmlPackage.eINSTANCE.getSubclassification(),
                SysmlPackage.eINSTANCE.getDocumentation(),
                SysmlPackage.eINSTANCE.getComment(),
                SysmlPackage.eINSTANCE.getTextualRepresentation(),
                SysmlPackage.eINSTANCE.getSubjectMembership(),
                SysmlPackage.eINSTANCE.getFramedConcernMembership(),
                SysmlPackage.eINSTANCE.getConcernUsage()
        );
        assertThat(children).containsAll(expectedChildren);
        children.removeAll(expectedChildren);
        assertThat(children).allMatch(eClass -> {
            boolean authorizedClasses = SysmlPackage.eINSTANCE.getUsage().isSuperTypeOf(eClass) || SysmlPackage.eINSTANCE.getImport().isSuperTypeOf(eClass);
            return !eClass.isAbstract() && !eClass.isInterface() && authorizedClasses;
        });
    }

    @DisplayName("Check the list of children that can be created from a RequirementUsage")
    @Test
    public void testRequirementUsageChildren() {
        List<EClass> children = new GetChildCreationSwitch().doSwitch(SysmlFactory.eINSTANCE.createRequirementUsage());
        List<EClass> expectedChildren = List.of(
                SysmlPackage.eINSTANCE.getAttributeUsage(),
                SysmlPackage.eINSTANCE.getFeatureTyping(),
                SysmlPackage.eINSTANCE.getSubsetting(),
                SysmlPackage.eINSTANCE.getRedefinition(),
                SysmlPackage.eINSTANCE.getReferenceSubsetting(),
                SysmlPackage.eINSTANCE.getLiteralBoolean(),
                SysmlPackage.eINSTANCE.getLiteralInfinity(),
                SysmlPackage.eINSTANCE.getLiteralInteger(),
                SysmlPackage.eINSTANCE.getLiteralRational(),
                SysmlPackage.eINSTANCE.getLiteralString(),
                SysmlPackage.eINSTANCE.getFeatureMembership(),
                SysmlPackage.eINSTANCE.getDocumentation(),
                SysmlPackage.eINSTANCE.getComment(),
                SysmlPackage.eINSTANCE.getTextualRepresentation(),
                SysmlPackage.eINSTANCE.getSubjectMembership(),
                SysmlPackage.eINSTANCE.getFramedConcernMembership(),
                SysmlPackage.eINSTANCE.getConcernUsage()
        );
        assertThat(children).containsAll(expectedChildren);
        children.removeAll(expectedChildren);
        assertThat(children).isEmpty();
    }

    @DisplayName("Check the list of children that can be created from a FramedConcernMembership")
    @Test
    public void testFramedConcernMembership() {
        List<EClass> children = new GetChildCreationSwitch().doSwitch(SysmlFactory.eINSTANCE.createFramedConcernMembership());
        List<EClass> expectedChildren = List.of(SysmlPackage.eINSTANCE.getConcernUsage());
        assertThat(children).containsAll(expectedChildren);
        children.removeAll(expectedChildren);
        assertThat(children).isEmpty();
    }
}
