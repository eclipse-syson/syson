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
package org.eclipse.syson.diagram.common.view.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramService;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.diagrams.description.EdgeDescription;
import org.eclipse.sirius.components.view.builder.generated.diagram.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.view.ViewBuilders;
import org.eclipse.sirius.components.view.diagram.EdgeTool;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.syson.diagram.services.aql.DiagramQueryAQLService;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.ServiceMethod;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Service class for {@link ViewEdgeToolSwitch}.
 *
 * @author arichard
 */
public class ViewEdgeToolService {

    private static final String METAMODEL_ICONS_PATH = "/icons/full/obj16/";

    private static final String SVG = ".svg";

    private static final String NEW_INSTANCE = "newInstance";

    private final ViewBuilders viewBuilderHelper;

    private final DiagramBuilders diagramBuilderHelper;

    private final List<NodeDescription> allNodeDescriptions;

    private final IDescriptionNameGenerator nameGenerator;

    public ViewEdgeToolService(ViewBuilders viewBuilderHelper, DiagramBuilders diagramBuilderHelper, List<NodeDescription> allNodeDescriptions, IDescriptionNameGenerator nameGenerator) {
        this.viewBuilderHelper = Objects.requireNonNull(viewBuilderHelper);
        this.diagramBuilderHelper = Objects.requireNonNull(diagramBuilderHelper);
        this.allNodeDescriptions = Objects.requireNonNull(allNodeDescriptions);
        this.nameGenerator = Objects.requireNonNull(nameGenerator);
    }

    public Optional<NodeDescription> getNodeDescription(EClassifier classifier) {
        if (classifier == null) {
            return Optional.empty();
        }
        return this.allNodeDescriptions.stream()
                .filter(nd -> nd.getDomainType().equals(classifier.getEPackage().getNsPrefix() + "::" + classifier.getName()))
                .findFirst();
    }

    public EClassifier getDefinitionFromUsage(Usage object) {
        String definitionFromUsage = object.eClass().getName().replace("Usage", "Definition");
        return SysmlPackage.eINSTANCE.getEClassifier(definitionFromUsage);
    }

    public boolean isTheNodeDescriptionFor(NodeDescription nd, EClass eClass) {
        return this.nameGenerator.getNodeName(eClass).equals(nd.getName());
    }

    public EdgeTool createDependencyEdgeTool(List<NodeDescription> targetElementDescriptions) {
        var builder = this.diagramBuilderHelper.newEdgeTool();

        var callElementInitializerService = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of0(ViewCreateService::elementInitializer).aqlSelf());

        var setClient = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getDependency_Client().getName())
                .valueExpression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_SOURCE);

        var setSupplier = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getDependency_Supplier().getName())
                .valueExpression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_TARGET);

        var changeContextNewInstance = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL + NEW_INSTANCE)
                .children(callElementInitializerService.build(), setClient.build(), setSupplier.build());

        var createInstance = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getDependency()))
                .referenceName(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement().getName())
                .variableName(NEW_INSTANCE)
                .children(changeContextNewInstance.build());

        var changeContextMembership = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL + "newOwningMembership")
                .children(createInstance.build());

        var createMembership = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getOwningMembership()))
                .referenceName(SysmlPackage.eINSTANCE.getElement_OwnedRelationship().getName())
                .variableName("newOwningMembership")
                .children(changeContextMembership.build());

        var body = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_SOURCE + ".getContainerPackage()")
                .children(createMembership.build());

        return builder
                .name(this.nameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getDependency()))
                .iconURLsExpression(METAMODEL_ICONS_PATH + SysmlPackage.eINSTANCE.getDependency().getName() + SVG)
                .body(body.build())
                .targetElementDescriptions(targetElementDescriptions.toArray(NodeDescription[]::new))
                .build();
    }

    public EdgeTool createSubclassificationEdgeTool(List<NodeDescription> targetElementDescriptions) {
        var builder = this.diagramBuilderHelper.newEdgeTool();

        var callElementInitializerService = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of0(ViewCreateService::elementInitializer).aqlSelf());

        var setSubclassifier = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getSubclassification_Subclassifier().getName())
                .valueExpression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_SOURCE);

        var setSuperclassifier = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getSubclassification_Superclassifier().getName())
                .valueExpression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_TARGET);

        var setSpecific = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getSpecialization_Specific().getName())
                .valueExpression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_SOURCE);

        var setGeneral = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getSpecialization_General().getName())
                .valueExpression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_TARGET);

        var changeContextNewInstance = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL + NEW_INSTANCE)
                .children(callElementInitializerService.build(), setSubclassifier.build(), setSuperclassifier.build(), setSpecific.build(), setGeneral.build());

        var createInstance = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getSubclassification()))
                .referenceName(SysmlPackage.eINSTANCE.getElement_OwnedRelationship().getName())
                .variableName(NEW_INSTANCE)
                .children(changeContextNewInstance.build());

        String hasExistingSubclassification = "hasExistingSubclassification";

        var feedback = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of1(DiagramQueryAQLService::infoMessage).aqlSelf(AQLUtils.aqlString("A subclassification already exists between these elements.")))
                .build();

        var createIfPossible = this.viewBuilderHelper.newIf()
                .conditionExpression(AQLConstants.AQL + "not " + hasExistingSubclassification)
                .children(createInstance.build());

        var informOtherwise = this.viewBuilderHelper.newIf()
                .conditionExpression(AQLConstants.AQL + hasExistingSubclassification)
                .children(feedback);

        var letExistingSubsetting = this.viewBuilderHelper.newLet()
                .variableName(hasExistingSubclassification)
                .valueExpression(AQLConstants.AQL_SELF + ".eContents(sysml::Subclassification).superclassifier->includes(" + EdgeDescription.SEMANTIC_EDGE_TARGET + ")")
                .children(createIfPossible.build(), informOtherwise.build());

        var body = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_SOURCE)
                .children(letExistingSubsetting.build());

        return builder
                .name(this.nameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getSubclassification()))
                .iconURLsExpression(METAMODEL_ICONS_PATH + SysmlPackage.eINSTANCE.getSubclassification().getName() + SVG)
                .body(body.build())
                .targetElementDescriptions(targetElementDescriptions.toArray(NodeDescription[]::new))
                .build();
    }

    public EdgeTool createRedefinitionEdgeTool(List<NodeDescription> targetElementDescriptions) {
        var builder = this.diagramBuilderHelper.newEdgeTool();

        var callElementInitializerService = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of0(ViewCreateService::elementInitializer).aqlSelf());

        var setRedefiningFeature = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getRedefinition_RedefiningFeature().getName())
                .valueExpression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_SOURCE);

        var setRedefinedFeature = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getRedefinition_RedefinedFeature().getName())
                .valueExpression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_TARGET);

        var setSubsettingFeature = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getSubsetting_SubsettingFeature().getName())
                .valueExpression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_SOURCE);

        var setSubsettedFeature = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getSubsetting_SubsettedFeature().getName())
                .valueExpression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_TARGET);

        var setSpecific = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getSpecialization_Specific().getName())
                .valueExpression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_SOURCE);

        var setGeneral = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getSpecialization_General().getName())
                .valueExpression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_TARGET);

        var changeContextNewInstance = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL + NEW_INSTANCE)
                .children(callElementInitializerService.build(), setRedefiningFeature.build(), setRedefinedFeature.build(), setSubsettingFeature.build(), setSubsettedFeature.build(),
                        setSpecific.build(),
                        setGeneral.build());

        var createInstance = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getRedefinition()))
                .referenceName(SysmlPackage.eINSTANCE.getElement_OwnedRelationship().getName())
                .variableName(NEW_INSTANCE)
                .children(changeContextNewInstance.build());

        String hasExistingRedefinition = "hasExistingRedefinition";

        var feedback = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of1(DiagramQueryAQLService::infoMessage).aqlSelf(AQLUtils.aqlString("A redefinition already exists between these elements.")))
                .build();

        var createIfPossible = this.viewBuilderHelper.newIf()
                .conditionExpression(AQLConstants.AQL + "not " + hasExistingRedefinition)
                .children(createInstance.build());

        var informOtherwise = this.viewBuilderHelper.newIf()
                .conditionExpression(AQLConstants.AQL + hasExistingRedefinition)
                .children(feedback);

        var letExistingSubsetting = this.viewBuilderHelper.newLet()
                .variableName(hasExistingRedefinition)
                .valueExpression(AQLConstants.AQL_SELF + ".eContents(sysml::Redefinition).redefinedFeature->includes(" + EdgeDescription.SEMANTIC_EDGE_TARGET + ")")
                .children(createIfPossible.build(), informOtherwise.build());

        var body = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_SOURCE)
                .children(letExistingSubsetting.build());

        return builder
                .name(this.nameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getRedefinition()))
                .iconURLsExpression(METAMODEL_ICONS_PATH + SysmlPackage.eINSTANCE.getRedefinition().getName() + SVG)
                .body(body.build())
                .targetElementDescriptions(targetElementDescriptions.toArray(NodeDescription[]::new))
                .build();
    }

    public EdgeTool createSubsettingEdgeTool(List<NodeDescription> targetElementDescriptions) {
        var builder = this.diagramBuilderHelper.newEdgeTool();

        var callElementInitializerService = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of0(ViewCreateService::elementInitializer).aqlSelf());

        var setSubsettingFeature = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getSubsetting_SubsettingFeature().getName())
                .valueExpression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_SOURCE);

        var setSubsettedFeature = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getSubsetting_SubsettedFeature().getName())
                .valueExpression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_TARGET);

        var setSpecific = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getSpecialization_Specific().getName())
                .valueExpression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_SOURCE);

        var setGeneral = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getSpecialization_General().getName())
                .valueExpression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_TARGET);

        var changeContextNewInstance = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL + NEW_INSTANCE)
                .children(callElementInitializerService.build(), setSubsettingFeature.build(), setSubsettedFeature.build(), setSpecific.build(),
                        setGeneral.build());

        var createInstance = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getSubsetting()))
                .referenceName(SysmlPackage.eINSTANCE.getElement_OwnedRelationship().getName())
                .variableName(NEW_INSTANCE)
                .children(changeContextNewInstance.build());

        String hasExistingReferenceSubsetting = "hasExistingSubsetting";

        var feedback = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of1(DiagramQueryAQLService::infoMessage).aqlSelf(AQLUtils.aqlString("A subsetting already exists between these elements.")))
                .build();

        var createIfPossible = this.viewBuilderHelper.newIf()
                .conditionExpression(AQLConstants.AQL + "not " + hasExistingReferenceSubsetting)
                .children(createInstance.build());

        var informOtherwise = this.viewBuilderHelper.newIf()
                .conditionExpression(AQLConstants.AQL + hasExistingReferenceSubsetting)
                .children(feedback);

        var letExistingSubsetting = this.viewBuilderHelper.newLet()
                .variableName(hasExistingReferenceSubsetting)
                .valueExpression(AQLConstants.AQL_SELF + ".eContents(sysml::Subsetting).subsettedFeature->includes(" + EdgeDescription.SEMANTIC_EDGE_TARGET + ")")
                .children(createIfPossible.build(), informOtherwise.build());

        var body = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_SOURCE)
                .children(letExistingSubsetting.build());

        return builder
                .name(this.nameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getSubsetting()))
                .iconURLsExpression(METAMODEL_ICONS_PATH + SysmlPackage.eINSTANCE.getSubsetting().getName() + SVG)
                .body(body.build())
                .targetElementDescriptions(targetElementDescriptions.toArray(NodeDescription[]::new))
                .build();
    }

    public EdgeTool createReferenceSubsettingEdgeTool(List<NodeDescription> targetElementDescriptions) {
        var builder = this.diagramBuilderHelper.newEdgeTool();

        var callElementInitializerService = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of0(ViewCreateService::elementInitializer).aqlSelf());

        var setReferencedFeature = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getReferenceSubsetting_ReferencedFeature().getName())
                .valueExpression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_TARGET);

        var setSubsettingFeature = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getSubsetting_SubsettingFeature().getName())
                .valueExpression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_SOURCE);

        var setSubsettedFeature = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getSubsetting_SubsettedFeature().getName())
                .valueExpression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_TARGET);

        var setSpecific = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getSpecialization_Specific().getName())
                .valueExpression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_SOURCE);

        var setGeneral = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getSpecialization_General().getName())
                .valueExpression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_TARGET);

        var changeContextNewInstance = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL + NEW_INSTANCE)
                .children(callElementInitializerService.build(), setSubsettingFeature.build(), setReferencedFeature.build(), setSubsettedFeature.build(),
                        setSpecific.build(), setGeneral.build());

        var createInstance = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getReferenceSubsetting()))
                .referenceName(SysmlPackage.eINSTANCE.getElement_OwnedRelationship().getName())
                .variableName(NEW_INSTANCE)
                .children(changeContextNewInstance.build());

        String hasExistingReferenceSubsetting = "hasExistingReferenceSubsetting";

        var feedback = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of1(DiagramQueryAQLService::infoMessage).aqlSelf(AQLUtils.aqlString("A reference subsetting already exists between these elements.")))
                .build();

        var createIfPossible = this.viewBuilderHelper.newIf()
                .conditionExpression(AQLConstants.AQL + "not " + hasExistingReferenceSubsetting)
                .children(createInstance.build());

        var informOtherwise = this.viewBuilderHelper.newIf()
                .conditionExpression(AQLConstants.AQL + hasExistingReferenceSubsetting)
                .children(feedback);

        var letExistingSubsetting = this.viewBuilderHelper.newLet()
                .variableName(hasExistingReferenceSubsetting)
                .valueExpression(AQLConstants.AQL_SELF + ".eContents(sysml::ReferenceSubsetting).referencedFeature->includes(" + EdgeDescription.SEMANTIC_EDGE_TARGET + ")")
                .children(createIfPossible.build(), informOtherwise.build());

        var body = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_SOURCE)
                .children(letExistingSubsetting.build());

        return builder
                .name(this.nameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getReferenceSubsetting()))
                .iconURLsExpression(METAMODEL_ICONS_PATH + SysmlPackage.eINSTANCE.getReferenceSubsetting().getName() + SVG)
                .body(body.build())
                .targetElementDescriptions(targetElementDescriptions.toArray(NodeDescription[]::new))
                .build();
    }

    public EdgeTool createFeatureTypingEdgeTool(List<NodeDescription> targetElementDescriptions) {
        var builder = this.diagramBuilderHelper.newEdgeTool();

        var callElementInitializerService = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of0(ViewCreateService::elementInitializer).aqlSelf());

        var setTypedFeature = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getFeatureTyping_TypedFeature().getName())
                .valueExpression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_SOURCE);

        var setType = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getFeatureTyping_Type().getName())
                .valueExpression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_TARGET);

        var setSpecific = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getSpecialization_Specific().getName())
                .valueExpression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_SOURCE);

        var setGeneral = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getSpecialization_General().getName())
                .valueExpression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_TARGET);

        var changeContextNewInstance = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL + NEW_INSTANCE)
                .children(callElementInitializerService.build(), setTypedFeature.build(), setType.build(), setSpecific.build(), setGeneral.build());

        var createInstance = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getFeatureTyping()))
                .referenceName(SysmlPackage.eINSTANCE.getElement_OwnedRelationship().getName())
                .variableName(NEW_INSTANCE)
                .children(changeContextNewInstance.build());

        var feedback = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of1(DiagramQueryAQLService::infoMessage).aqlSelf(AQLUtils.aqlString("A feature typing already exists between these elements.")))
                .build();

        String hasExistingFeatureTyping = "hasExistingFeatureTyping";

        var createIfPossible = this.viewBuilderHelper.newIf()
                .conditionExpression(AQLConstants.AQL + "not " + hasExistingFeatureTyping)
                .children(createInstance.build());

        var informOtherwise = this.viewBuilderHelper.newIf()
                .conditionExpression(AQLConstants.AQL + hasExistingFeatureTyping)
                .children(feedback);

        var letExistingFeatureTyping = this.viewBuilderHelper.newLet()
                .variableName(hasExistingFeatureTyping)
                .valueExpression(AQLConstants.AQL_SELF + ".type->includes(" + EdgeDescription.SEMANTIC_EDGE_TARGET + ")")
                .children(createIfPossible.build(), informOtherwise.build());

        var body = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_SOURCE)
                .children(letExistingFeatureTyping.build());

        return builder
                .name(this.nameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getFeatureTyping()))
                .iconURLsExpression(METAMODEL_ICONS_PATH + SysmlPackage.eINSTANCE.getFeatureTyping().getName() + SVG)
                .body(body.build())
                .targetElementDescriptions(targetElementDescriptions.toArray(NodeDescription[]::new))
                .build();
    }

    public EdgeTool createAddAsNestedEdgeTool(NodeDescription targetElementDescription) {
        var builder = this.diagramBuilderHelper.newEdgeTool();

        var callService = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of1(ViewToolService::becomeNestedUsage).aql(EdgeDescription.SEMANTIC_EDGE_TARGET, EdgeDescription.SEMANTIC_EDGE_SOURCE));

        return builder
                .name(this.nameGenerator.getCreationToolName("Add as nested ", SysMLMetamodelHelper.toEClass(targetElementDescription.getDomainType())))
                .iconURLsExpression(METAMODEL_ICONS_PATH + SysmlPackage.eINSTANCE.getMembership().getName() + SVG)
                .body(callService.build())
                .targetElementDescriptions(targetElementDescription)
                .build();
    }

    public EdgeTool createBecomeNestedElementEdgeTool(EClass eClass, List<NodeDescription> targetElementDescriptions) {
        var builder = this.diagramBuilderHelper.newEdgeTool();

        var callService = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of1(ViewToolService::becomeNestedUsage).aql(EdgeDescription.SEMANTIC_EDGE_SOURCE, EdgeDescription.SEMANTIC_EDGE_TARGET));

        return builder
                .name(this.nameGenerator.getCreationToolName("Become nested ", eClass))
                .iconURLsExpression(METAMODEL_ICONS_PATH + SysmlPackage.eINSTANCE.getMembership().getName() + SVG)
                .body(callService.build())
                .targetElementDescriptions(targetElementDescriptions.toArray(NodeDescription[]::new))
                .build();
    }

    public EdgeTool createBecomeObjectiveRequirementEdgeTool(List<NodeDescription> targetElementDescriptions) {
        var builder = this.diagramBuilderHelper.newEdgeTool();

        var callService = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of1(ViewToolService::becomeObjectiveRequirement).aql(EdgeDescription.SEMANTIC_EDGE_SOURCE, EdgeDescription.SEMANTIC_EDGE_TARGET));

        return builder
                .name(this.nameGenerator.getCreationToolName("Become objective", SysmlPackage.eINSTANCE.getRequirementUsage()))
                .iconURLsExpression(METAMODEL_ICONS_PATH + "Objective" + SVG)
                .body(callService.build())
                .targetElementDescriptions(targetElementDescriptions.toArray(NodeDescription[]::new))
                .build();
    }

    public EdgeTool createTransitionUsageEdgeTool(EClass eClass, List<NodeDescription> targetElementDescriptions) {
        var builder = this.diagramBuilderHelper.newEdgeTool();

        var callService = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of5(ViewCreateService::createTransitionUsage).aql(EdgeDescription.SEMANTIC_EDGE_SOURCE, EdgeDescription.SEMANTIC_EDGE_TARGET, EdgeDescription.EDGE_SOURCE,
                        EdgeDescription.EDGE_TARGET, IDiagramService.DIAGRAM_SERVICES, IEditingContext.EDITING_CONTEXT));

        return builder
                .name(this.nameGenerator.getCreationToolName(eClass))
                .body(callService.build())
                .targetElementDescriptions(targetElementDescriptions.toArray(NodeDescription[]::new))
                .build();
    }

    public EdgeTool createIncludeUseCaseUsageTool(List<NodeDescription> targetElementDescriptions) {
        var builder = this.diagramBuilderHelper.newEdgeTool();
        String useCaseUsageNodeName = this.nameGenerator.getNodeName(SysmlPackage.eINSTANCE.getUseCaseUsage());
        var useCaseUsageNode = targetElementDescriptions.stream()
                .filter(nd -> useCaseUsageNodeName.equals(nd.getName()))
                .findFirst().get();

        var body = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of1(ViewCreateService::createIncludeUseCaseUsage).aql(EdgeDescription.SEMANTIC_EDGE_SOURCE, EdgeDescription.SEMANTIC_EDGE_TARGET));

        return builder.name(this.nameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getIncludeUseCaseUsage()))
                .iconURLsExpression(METAMODEL_ICONS_PATH + SysmlPackage.eINSTANCE.getIncludeUseCaseUsage().getName() + SVG)
                .body(body.build())
                .targetElementDescriptions(useCaseUsageNode)
                .build();
    }

    public EdgeTool createAllocateEdgeTool(List<NodeDescription> targetElementDescriptions) {
        var builder = this.diagramBuilderHelper.newEdgeTool();
        var onlyUsages = targetElementDescriptions.stream()
                .filter(nd -> nd.getName().endsWith("Usage"))
                .toArray(NodeDescription[]::new);

        var body = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of4(ViewCreateService::createAllocateEdge).aql(EdgeDescription.SEMANTIC_EDGE_SOURCE, EdgeDescription.SEMANTIC_EDGE_TARGET, EdgeDescription.EDGE_SOURCE,
                        IEditingContext.EDITING_CONTEXT, IDiagramService.DIAGRAM_SERVICES));

        return builder.name(this.nameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getAllocationUsage()))
                .iconURLsExpression(METAMODEL_ICONS_PATH + SysmlPackage.eINSTANCE.getDependency().getName() + SVG)
                .body(body.build())
                .targetElementDescriptions(onlyUsages)
                .build();
    }

    public EdgeTool createSuccessionEdgeTool(List<NodeDescription> targetElementDescriptions) {
        var builder = this.diagramBuilderHelper.newEdgeTool();
        var body = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of5(ViewCreateService::createSuccessionEdge).aql(EdgeDescription.SEMANTIC_EDGE_SOURCE, EdgeDescription.SEMANTIC_EDGE_TARGET, EdgeDescription.EDGE_SOURCE,
                        EdgeDescription.EDGE_TARGET, IEditingContext.EDITING_CONTEXT, IDiagramService.DIAGRAM_SERVICES));

        return builder.name(this.nameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getSuccession()))
                .iconURLsExpression(METAMODEL_ICONS_PATH + SysmlPackage.eINSTANCE.getSuccession().getName() + SVG)
                .body(body.build())
                .targetElementDescriptions(targetElementDescriptions.toArray(NodeDescription[]::new))
                .build();
    }
}
