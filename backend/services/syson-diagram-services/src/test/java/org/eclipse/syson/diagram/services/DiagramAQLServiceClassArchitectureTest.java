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
package org.eclipse.syson.diagram.services;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

import java.util.List;

import org.eclipse.syson.tests.architecture.SysONServiceDependencyPredicate;
import org.junit.jupiter.api.Test;

/**
 * Architecture test that checks dependencies between services classes.
 *
 * @author Arthur Daussy
 */
public class DiagramAQLServiceClassArchitectureTest {

    @Test
    void checkDiagramMutationAqlServiceClassDependencies() {
        JavaClasses imported = new ClassFileImporter()
                .importPackages("org.eclipse.syson.diagram.services.aql");

        ArchRule rule = ArchRuleDefinition.classes()
                .that().haveSimpleName("DiagramMutationAQLService")
                .should().onlyDependOnClassesThat(new SysONServiceDependencyPredicate(List.of("Diagram"), "Mutation"))
                .because("DiagramMutationAQLService should only depends on mutation diagram services");

        rule.check(imported);
    }

    @Test
    void checkDiagramQueryAqlServiceClassDependencies() {
        JavaClasses imported = new ClassFileImporter()
                .importPackages("org.eclipse.syson.diagram.services.aql");

        ArchRule rule = ArchRuleDefinition.classes()
                .that().haveSimpleName("DiagramQueryAQLService")
                .should().onlyDependOnClassesThat(new SysONServiceDependencyPredicate(List.of("Diagram"), "Query"))
                .because("DiagramQueryAQLService should only depends on query diagram services");

        rule.check(imported);
    }

}
