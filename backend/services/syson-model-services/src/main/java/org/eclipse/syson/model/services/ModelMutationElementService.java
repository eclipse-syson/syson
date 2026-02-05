/*******************************************************************************
 * Copyright (c) 2025, 2026 Obeo.
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
package org.eclipse.syson.model.services;

import java.util.Optional;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.sirius.components.emf.utils.SiriusEMFCopier;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.SatisfyRequirementUsage;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.ViewDefinition;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.sysml.helper.EMFUtils;
import org.eclipse.syson.sysml.metamodel.services.MetamodelMutationElementService;
import org.eclipse.syson.sysml.util.ElementUtil;
import org.eclipse.syson.util.GetIntermediateContainerCreationSwitch;
import org.springframework.stereotype.Service;

/**
 * Element-related services doing mutations in models.
 * This class own service methods that modify the semantic model and either requires other Spring services or combine several atomic operation such as creations, deletions, moves, etc...
 * For methods doing atomic SysML Operation have a look {@link MetamodelMutationElementService} .
 *
 * @author arichard
 */
@Service
public class ModelMutationElementService {

    private final ElementUtil elementUtil;

    private final MetamodelMutationElementService metamodelMutationElementService;

    public ModelMutationElementService() {
        this.metamodelMutationElementService = new MetamodelMutationElementService();
        this.elementUtil = new ElementUtil();
    }

    /**
     * Set a new {@link ViewDefinition} for the given {@link ViewUsage}.
     *
     * @param viewUsage
     *         the given {@link ViewUsage}.
     * @param newViewDefinition
     *         the new {@link ViewDefinition} to set, through its qualified name (for example,
     *         StandardDiagramsConstants.GV).
     * @return the given {@link ViewUsage}.
     */
    public Element setAsView(ViewUsage viewUsage, String newViewDefinition) {
        var types = viewUsage.getType();
        var generalViewViewDef = this.elementUtil.findByNameAndType(viewUsage, newViewDefinition, ViewDefinition.class);
        if (types == null || types.isEmpty()) {
            var featureTyping = SysmlFactory.eINSTANCE.createFeatureTyping();
            viewUsage.getOwnedRelationship().add(featureTyping);
            featureTyping.setType(generalViewViewDef);
            featureTyping.setTypedFeature(viewUsage);
        } else {
            Relationship relationship = viewUsage.getOwnedRelationship().get(0);
            if (relationship instanceof FeatureTyping featureTyping) {
                featureTyping.setType(generalViewViewDef);
            }
        }
        return viewUsage;
    }

    /**
     * Create a sibling {@link PartUsage} and then connect self and the newly created {@link PartUsage} through an {@link org.eclipse.syson.sysml.BindingConnectorAsUsage} connected to 2 new
     * {@link org.eclipse.syson.sysml.PortUsage}.
     *
     * @param self
     *         a {@link PartUsage}
     * @return the newly create {@link PartUsage}
     */
    public Element createPartUsageAndBindingConnectorAsUsage(PartUsage self) {
        var parent = self.getOwner();
        if (parent instanceof Namespace parentNamespace) {
            // create a new port on given part usage
            var newSelfPort = SysmlFactory.eINSTANCE.createPortUsage();
            this.metamodelMutationElementService.addChildInParent(self, newSelfPort);
            this.metamodelMutationElementService.initialize(newSelfPort);
            // create a new part usage as a self sibling
            var newPartUsage = SysmlFactory.eINSTANCE.createPartUsage();
            this.metamodelMutationElementService.addChildInParent(parent, newPartUsage);
            this.metamodelMutationElementService.initialize(newPartUsage);
            // create a new port on the new part usage
            var newPartUsagePort = SysmlFactory.eINSTANCE.createPortUsage();
            this.metamodelMutationElementService.addChildInParent(newPartUsage, newPartUsagePort);
            this.metamodelMutationElementService.initialize(newPartUsagePort);
            // create binding connector as usage edge between both new ports
            this.metamodelMutationElementService.createBindingConnectorAsUsage(newSelfPort, newPartUsagePort, parentNamespace);
            return newPartUsage;
        }
        return self;
    }

    /**
     * Create a sibling {@link PartUsage} and then connect self and the newly created {@link PartUsage} through an {@link org.eclipse.syson.sysml.FlowUsage} connected to 2 new
     * {@link org.eclipse.syson.sysml.PortUsage}.
     *
     * @param self
     *         a {@link PartUsage}
     * @return the newly create {@link PartUsage}
     */
    public PartUsage createPartUsageAndFlowConnection(PartUsage self) {
        var parent = self.getOwner();
        if (parent instanceof Namespace parentNamespace) {
            // create a new port on given part usage
            var newSelfPort = SysmlFactory.eINSTANCE.createPortUsage();
            this.metamodelMutationElementService.addChildInParent(self, newSelfPort);
            this.metamodelMutationElementService.initialize(newSelfPort);
            // create a new part usage as a self sibling
            var newPartUsage = SysmlFactory.eINSTANCE.createPartUsage();
            this.metamodelMutationElementService.addChildInParent(parent, newPartUsage);
            this.metamodelMutationElementService.initialize(newPartUsage);
            // create a new port on the new part usage
            var newPartUsagePort = SysmlFactory.eINSTANCE.createPortUsage();
            this.metamodelMutationElementService.addChildInParent(newPartUsage, newPartUsagePort);
            this.metamodelMutationElementService.initialize(newPartUsagePort);
            // create flow connection edge between both new ports
            this.metamodelMutationElementService.createFlowUsage(newSelfPort, newPartUsagePort, parentNamespace);
            return newPartUsage;
        }
        return self;
    }

    /**
     * Create a sibling {@link PartUsage} and then connect self and the newly created {@link PartUsage} through an {@link org.eclipse.syson.sysml.InterfaceUsage} connected to 2 new
     * {@link org.eclipse.syson.sysml.PortUsage}.
     *
     * @param self
     *         a {@link PartUsage}
     * @return the newly create {@link PartUsage}
     */
    public PartUsage createPartUsageAndInterface(PartUsage self) {
        var parent = self.getOwner();
        if (parent instanceof Namespace parentNamespace) {
            // create a new port on given part usage
            var newSelfPort = SysmlFactory.eINSTANCE.createPortUsage();
            this.metamodelMutationElementService.addChildInParent(self, newSelfPort);
            this.metamodelMutationElementService.initialize(newSelfPort);
            // create a new part usage as a self sibling
            var newPartUsage = SysmlFactory.eINSTANCE.createPartUsage();
            this.metamodelMutationElementService.addChildInParent(parent, newPartUsage);
            this.metamodelMutationElementService.initialize(newPartUsage);
            // create a new port on the new part usage
            var newPartUsagePort = SysmlFactory.eINSTANCE.createPortUsage();
            this.metamodelMutationElementService.addChildInParent(newPartUsage, newPartUsagePort);
            this.metamodelMutationElementService.initialize(newPartUsagePort);
            // create interface edge between both new ports
            this.metamodelMutationElementService.createInterfaceUsage(newSelfPort, newPartUsagePort, parentNamespace);
            return newPartUsage;
        }
        return self;
    }

    /**
     * Create a SatisfyRequirement with the following syntax: "satisfy req by subj", where req is a ReferenceSubsetting
     * to an existing RequirementUsage and subj is a SubjectMembership containing a ReferenceUsage pointing to a
     * Feature. In this case the Feature is the given parent Element.
     *
     * @param element
     *            the parent {@link Element} of the new {@link SatisfyRequirementUsage}.
     * @param existingRequirement
     *            an existing {@link RequirementUsage}.
     * @return the new {@link SatisfyRequirementUsage}.
     */
    public SatisfyRequirementUsage createSatisfy(Element element, RequirementUsage existingRequirement) {
        // create a new SatisfyRequirementUsage as child of the given Element
        var newSatisfyRequirementUsage = SysmlFactory.eINSTANCE.createSatisfyRequirementUsage();
        this.metamodelMutationElementService.addChildInParent(element, newSatisfyRequirementUsage);
        // create a new ReferenceSubsetting as child of the new SatisfyRequirementUsage
        var newReferenceSubsetting = SysmlFactory.eINSTANCE.createReferenceSubsetting();
        newSatisfyRequirementUsage.getOwnedRelationship().add(newReferenceSubsetting);
        // link the new ReferenceSubsetting to the given existing RequirementUsage
        newReferenceSubsetting.setReferencedFeature(existingRequirement);
        // initialize the new SatisfyRequirementUsage, after added it in the model and after linked it to the existing
        // RequirementUsage
        this.metamodelMutationElementService.initialize(newSatisfyRequirementUsage);
        this.metamodelMutationElementService.initialize(newReferenceSubsetting);

        return newSatisfyRequirementUsage;
    }

    /**
     * Creates a {@link ViewUsage}.
     *
     * @param owningElement
     *            the element owning the {@link ViewUsage} to be created
     * @param viewName
     *            the name of the view to be created
     * @return an {@link Optional} of the newly created {@link ViewUsage} if the creation was successful, an empty
     *         {@link Optional} otherwise.
     */
    public Optional<ViewUsage> createViewUsage(Element owningElement, String viewName) {
        Optional<ViewUsage> viewUsageOpt = Optional.empty();

        Optional<EClass> membershipClassOpt = new GetIntermediateContainerCreationSwitch(owningElement).doSwitch(owningElement.eClass());
        if (membershipClassOpt.isPresent()) {
            EObject viewUsageMembership = SysmlFactory.eINSTANCE.create(membershipClassOpt.get());
            if (viewUsageMembership instanceof Relationship viewUsageRelationship) {
                ViewUsage viewUsage = SysmlFactory.eINSTANCE.createViewUsage();
                owningElement.getOwnedRelationship().add(viewUsageRelationship);
                viewUsageRelationship.getOwnedRelatedElement().add(viewUsage);
                viewUsage.setDeclaredName(viewName);
                viewUsageOpt = Optional.of(viewUsage);
            }
        }

        return viewUsageOpt;
    }

    /**
     * Feature types of {@link ViewUsage} with the {@link ViewDefinition} associated with the  standard diagram.
     *
     * @param viewUsage
     *         the view to feature type
     * @param diagramName
     *         the name of the {@link ViewDefinition} of the standard diagram
     */
    public void featureTypeViewUsage(ViewUsage viewUsage, String diagramName) {
        FeatureTyping featureTyping = SysmlFactory.eINSTANCE.createFeatureTyping();
        viewUsage.getOwnedRelationship().add(featureTyping);
        var viewDefinition = this.elementUtil.findByNameAndType(viewUsage, diagramName, ViewDefinition.class);
        featureTyping.setType(viewDefinition);
        featureTyping.setTypedFeature(viewUsage);
    }

    /**
     * Duplicates an Element.
     *
     * @param elementToDuplicate
     *         the element to duplicate
     * @param duplicateContent
     *         holds true if the content of the object to duplicate need to be duplicated
     * @param copyOutgoingReferences
     *         holds true if the outgoing references need to be copied of the object to duplicate need to be duplicated
     * @return an optional duplicated object
     */
    public Optional<Element> duplicateElement(Element elementToDuplicate, boolean duplicateContent, boolean copyOutgoingReferences) {
        Optional<EObject> duplicateObject;
        if (duplicateContent) {
            EcoreUtil.Copier copier = new EcoreUtil.Copier();
            EObject duplicatedObject = copier.copy(elementToDuplicate);
            if (copyOutgoingReferences) {
                copier.copyReferences();
            }
            duplicateObject = Optional.ofNullable(duplicatedObject);
        } else {
            SiriusEMFCopier copier = new SiriusEMFCopier();
            EObject duplicatedObject = copier.copyWithoutContent(elementToDuplicate);
            if (copyOutgoingReferences) {
                copier.copyReferences();
            }
            duplicateObject = Optional.ofNullable(duplicatedObject);
        }

        return duplicateObject.filter(Element.class::isInstance)
                .map(Element.class::cast)
                .map(duplicate -> {
                    // Change the name of element root element (not its content)
                    if (elementToDuplicate.getDeclaredName() != null && !elementToDuplicate.getDeclaredName().isBlank()) {
                        duplicate.setDeclaredName(elementToDuplicate.getDeclaredName() + "-copy");
                    } else if (elementToDuplicate.getShortName() != null && !elementToDuplicate.getShortName().isBlank()) {
                        duplicate.setDeclaredShortName(elementToDuplicate.getDeclaredShortName() + "-copy");
                    }
                    // Reset all ids
                    EMFUtils.allContainedObjectOfType(duplicate, Element.class).forEach(element -> element.setElementId(ElementUtil.generateUUID(element).toString()));

                    return duplicate;
                });
    }
}
