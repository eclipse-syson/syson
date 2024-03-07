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
package org.eclipse.syson.diagram.general.view.tools;

import java.util.Objects;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.ViewBuilders;
import org.eclipse.sirius.components.view.builder.providers.INodeToolProvider;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.DescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Node tool provider for elements inside compartments.
 * 
 * @author gdaniel
 */
public class CompartmentNodeToolProvider implements INodeToolProvider {

    private DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    private ViewBuilders viewBuilderHelper = new ViewBuilders();

    private EClassifier eClassifier;

    public CompartmentNodeToolProvider(EClassifier eClassifier) {
        this.eClassifier = Objects.requireNonNull(eClassifier);
    }

    @Override
    public NodeTool create(IViewDiagramElementFinder cache) {
        var builder = this.diagramBuilderHelper.newNodeTool();

        var callElementInitializerService = this.viewBuilderHelper.newChangeContext()
                .expression("aql:self.elementInitializer()");
        
        var changeContextNewInstance = this.viewBuilderHelper.newChangeContext()
                .expression("aql:newInstance")
                .children(callElementInitializerService.build());

        var createInstance = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(eClassifier))
                .referenceName(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement().getName())
                .variableName("newInstance")
                .children(changeContextNewInstance.build());

        var changeContextMembership = this.viewBuilderHelper.newChangeContext()
                .expression("aql:newFeatureMembership")
                .children(createInstance.build());

        var createMembership = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(getMembership(eClassifier)))
                .referenceName(SysmlPackage.eINSTANCE.getElement_OwnedRelationship().getName())
                .variableName("newFeatureMembership")
                .children(changeContextMembership.build());

        return builder.name(DescriptionNameGenerator.getCreationToolName(eClassifier))
                .iconURLsExpression("/icons/full/obj16/" + eClassifier.getName() + ".svg")
                .body(createMembership.build())
                .build();
    }

    private EClass getMembership(EClassifier type) {
        EClass membershipClass = SysmlPackage.eINSTANCE.getFeatureMembership();
        if (type.equals(SysmlPackage.eINSTANCE.getEnumerationUsage())) {
            membershipClass = SysmlPackage.eINSTANCE.getVariantMembership();
        }
        return membershipClass;
    }
}
