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
package org.eclipse.syson.sysml.metamodel.services;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.syson.sysml.AllocationUsage;
import org.eclipse.syson.sysml.BindingConnectorAsUsage;
import org.eclipse.syson.sysml.ConnectionUsage;
import org.eclipse.syson.sysml.Connector;
import org.eclipse.syson.sysml.Documentation;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.EndFeatureMembership;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureChaining;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.Flow;
import org.eclipse.syson.sysml.FlowEnd;
import org.eclipse.syson.sysml.FlowUsage;
import org.eclipse.syson.sysml.InterfaceUsage;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.Metaclass;
import org.eclipse.syson.sysml.MetadataUsage;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.OccurrenceDefinition;
import org.eclipse.syson.sysml.OccurrenceUsage;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.ReferenceSubsetting;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.Usage;

/**
 * Element-related services doing mutations. This class should not depend on sirius-web services or other spring
 * services.
 *
 * @author arichard
 */
public class MetamodelMutationElementService {

    private final ElementInitializerSwitch elementInitializerSwitch;

    public MetamodelMutationElementService() {
        this.elementInitializerSwitch = new ElementInitializerSwitch();
    }


    /**
     * Create the appropriate {@link Membership} child according to the given {@link Element}.
     *
     * @param element
     *            the given {@link Element}.
     * @return the newly created {@link Membership}.
     */
    public Membership createMembership(Element element) {
        Membership membership = null;
        if (element instanceof Package) {
            membership = SysmlFactory.eINSTANCE.createOwningMembership();
        } else {
            membership = SysmlFactory.eINSTANCE.createFeatureMembership();
        }
        element.getOwnedRelationship().add(membership);
        return membership;
    }

    /**
     * Sets the connector ends of a {@link Connector}.
     *
     * @param connector
     *            the {@link Connector} to configure
     * @param source
     *            the source of the {@link Connector}
     * @param target
     *            the target of the {@link Connector}
     * @param sourceContainer
     *            the semantic element corresponding to the graphical container of the source
     * @param targetContainer
     *            the semantic element corresponding to the graphical container of the target
     * @param newConnectorContainer
     *            the container of the Connector. If the container is a type, it will intend to compute a feature chain
     *            between the container and the ends.
     */
    public void setConnectorEnds(Connector connector, Feature source, Feature target, Element sourceContainer, Element targetContainer, Element newConnectorContainer) {
        this.addConnectorEnd(connector, source, sourceContainer, newConnectorContainer, FeatureDirectionKind.OUT);
        this.addConnectorEnd(connector, target, targetContainer, newConnectorContainer, FeatureDirectionKind.IN);
    }

    /**
     * Create a new {@link EndFeatureMembership} to be used as {@link FlowUsage} end.
     *
     * @param targetedFeature
     *            the targeted feature (either the source or target of the flow)
     * @return the new EndFeatureMembership
     * @technical-debt This method should be deleted at some point. We still need it public because of the old
     *                 architecture relying on ViewCreateService.
     */
    public EndFeatureMembership createFlowConnectionEnd(Feature targetedFeature) {
        EndFeatureMembership featureMembership = SysmlFactory.eINSTANCE.createEndFeatureMembership();

        FlowEnd flowEnd = SysmlFactory.eINSTANCE.createFlowEnd();
        featureMembership.getOwnedRelatedElement().add(flowEnd);

        Type owningType = targetedFeature.getOwningType();
        if (owningType instanceof Feature owningFeature) {
            var referenceSubSetting = SysmlFactory.eINSTANCE.createReferenceSubsetting();
            flowEnd.getOwnedRelationship().add(referenceSubSetting);
            referenceSubSetting.setReferencedFeature(owningFeature);
        }

        EndFeatureMembership target = SysmlFactory.eINSTANCE.createEndFeatureMembership();
        flowEnd.getOwnedRelationship().add(target);

        ReferenceUsage referenceUsage = SysmlFactory.eINSTANCE.createReferenceUsage();
        target.getOwnedRelatedElement().add(referenceUsage);

        Redefinition redefinition = SysmlFactory.eINSTANCE.createRedefinition();
        redefinition.setRedefinedFeature(targetedFeature);
        redefinition.setRedefiningFeature(referenceUsage);

        referenceUsage.getOwnedRelationship().add(redefinition);

        return featureMembership;
    }

    /**
     * Creates a new {@link BindingConnectorAsUsage} in the given container.
     *
     * @param source
     *            the source of the binding
     * @param target
     *            the target of the binding
     * @param sourceContainer
     *            the semantic element corresponding to the graphical container of the source
     * @param targetContainer
     *            the semantic element corresponding to the graphical container of the target
     * @param newConnectionContainer
     *            the container of the new {@link BindingConnectorAsUsage}
     * @return a new {@link BindingConnectorAsUsage}
     */
    public BindingConnectorAsUsage createBindingConnectorAsUsage(Feature source, Feature target, Element sourceContainer, Element targetContainer, Namespace newConnectionContainer) {
        return (BindingConnectorAsUsage) this.createConnector(source, target, sourceContainer, targetContainer, newConnectionContainer, SysmlFactory.eINSTANCE.createBindingConnectorAsUsage());
    }

    /**
     * Creates a new {@link ConnectionUsage} in the given container.
     *
     * @param source
     *            the source of the connection
     * @param target
     *            the target of the connection
     * @param sourceContainer
     *            the semantic element corresponding to the graphical container of the source
     * @param targetContainer
     *            the semantic element corresponding to the graphical container of the target
     * @param newConnectionContainer
     *            the container of the new {@link ConnectionUsage}
     * @return a new {@link ConnectionUsage}
     */
    public ConnectionUsage createConnectionUsage(Feature source, Feature target, Element sourceContainer, Element targetContainer, Namespace newConnectionContainer) {
        return (ConnectionUsage) this.createConnector(source, target, sourceContainer, targetContainer, newConnectionContainer, SysmlFactory.eINSTANCE.createConnectionUsage());
    }

    /**
     * Creates a new requirement derivation in the given container.
     * <p>
     * SysML v2 has no dedicated metaclass for requirement derivation: it is a {@link ConnectionUsage} annotated with
     * the {@code #derivation} metadata, whose ends are annotated with the {@code #original} and {@code #derive}
     * metadata. Those three metadata definitions come from the {@code RequirementDerivation} standard library and are
     * given as parameters, so that this service does not have to resolve them.
     * </p>
     * <p>
     * The ends are annotated rather than relying on their declaration order, so that the direction of the derivation
     * stays explicit.
     * </p>
     *
     * @param derived
     *            the requirement derived from {@code original}
     * @param original
     *            the requirement {@code derived} is derived from
     * @param newConnectionContainer
     *            the container of the new {@link ConnectionUsage}
     * @param derivationMetadata
     *            the {@code DerivationMetadata} definition, applied on the connection
     * @param originalEndMetadata
     *            the {@code OriginalRequirementMetadata} definition, applied on the end referencing {@code original}
     * @param derivedEndMetadata
     *            the {@code DerivedRequirementMetadata} definition, applied on the end referencing {@code derived}
     * @return the new requirement derivation
     */
    public ConnectionUsage createRequirementDerivation(RequirementUsage derived, RequirementUsage original, Namespace newConnectionContainer, Metaclass derivationMetadata,
            Metaclass originalEndMetadata, Metaclass derivedEndMetadata) {
        // The original requirement is the first end, so that a derivation whose ends are not annotated, which is what
        // the end order fallback of the display expects, is still oriented the same way.
        ConnectionUsage derivation = this.createConnectionUsage(original, derived, original.getOwner(), derived.getOwner(), newConnectionContainer);
        // A derivation is not referenced by its name, so the generated one is dropped, the way the generated name of a
        // connector end is. It keeps the derivation anonymous, as it is when written in text.
        derivation.setDeclaredName(null);
        this.applyPrefixMetadata(derivation, derivationMetadata);

        List<Feature> ends = derivation.getConnectorEnd();
        if (ends.size() == 2) {
            this.applyPrefixMetadata(ends.get(0), originalEndMetadata);
            this.applyPrefixMetadata(ends.get(1), derivedEndMetadata);
        }
        return derivation;
    }

    /**
     * Applies a prefix metadata, such as the {@code #derivation} of a requirement derivation, on the given element.
     *
     * @param annotatedElement
     *            the element to annotate
     * @param metadataDefinition
     *            the definition of the applied metadata
     * @return the new {@link MetadataUsage}, or {@code null} if no metadata definition was given
     */
    public MetadataUsage applyPrefixMetadata(Element annotatedElement, Metaclass metadataDefinition) {
        if (metadataDefinition == null) {
            return null;
        }
        MetadataUsage metadataUsage = SysmlFactory.eINSTANCE.createMetadataUsage();
        OwningMembership owningMembership = SysmlFactory.eINSTANCE.createOwningMembership();
        owningMembership.getOwnedRelatedElement().add(metadataUsage);
        annotatedElement.getOwnedRelationship().add(owningMembership);

        FeatureTyping featureTyping = SysmlFactory.eINSTANCE.createFeatureTyping();
        featureTyping.setType(metadataDefinition);
        featureTyping.setTypedFeature(metadataUsage);
        metadataUsage.getOwnedRelationship().add(featureTyping);

        this.elementInitializerSwitch.doSwitch(owningMembership);
        this.elementInitializerSwitch.doSwitch(metadataUsage);
        this.elementInitializerSwitch.doSwitch(featureTyping);
        // A prefix metadata has no name of its own, it is identified by the definition it is typed by.
        metadataUsage.setDeclaredName(null);
        return metadataUsage;
    }

    /**
     * Creates a new {@link FlowUsage} in the given container.
     *
     * @param source
     *            the source of the flow
     * @param target
     *            the target of the flow
     * @param sourceContainer
     *            the semantic element corresponding to the graphical container of the source
     * @param targetContainer
     *            the semantic element corresponding to the graphical container of the target
     * @param newConnectionContainer
     *            the container of the new {@link FlowUsage}
     * @return a new {@link FlowUsage}
     */
    public FlowUsage createFlowUsage(Feature source, Feature target, Element sourceContainer, Element targetContainer, Namespace newConnectionContainer) {
        return (FlowUsage) this.createConnector(source, target, sourceContainer, targetContainer, newConnectionContainer, SysmlFactory.eINSTANCE.createFlowUsage());
    }

    /**
     * Creates a new {@link InterfaceUsage} in the given container.
     *
     * @param source
     *            the source of the interface
     * @param target
     *            the target of the interface
     * @param sourceContainer
     *            the semantic element corresponding to the graphical container of the source
     * @param targetContainer
     *            the semantic element corresponding to the graphical container of the target
     * @param newConnectionContainer
     *            the container of the new {@link InterfaceUsage}
     * @return a new {@link InterfaceUsage}
     */
    public InterfaceUsage createInterfaceUsage(PortUsage source, PortUsage target, Element sourceContainer, Element targetContainer, Namespace newConnectionContainer) {
        return (InterfaceUsage) this.createConnector(source, target, sourceContainer, targetContainer, newConnectionContainer, SysmlFactory.eINSTANCE.createInterfaceUsage());
    }

    /**
     * Creates an instance of {@code eClass} in the given {@link Type} container if it is an {@link OccurrenceUsage} or an {@link OccurrenceDefinition}.
     * <p>It returns {@code null} if the {@code eClass} is not assignable to {@link OccurrenceUsage} or the {@code container} is not assignable to either @link OccurrenceUsage} or {@link OccurrenceDefinition}</p>
     *
     * @param container
     *            the {@link Type} container
     * @param eClass
     *            the {@link EClass} assignable to {@link OccurrenceUsage} to instantiate
     * @return a new {@link EClass} instantiated {@link EObject}, {@code null} if the {@code eClass} is not assignable to {@link OccurrenceUsage} and the {@code container} is not assignable to either {@link OccurrenceUsage} or {@link OccurrenceDefinition}
     */
    public EObject createOccurrenceInOccurrence(Type container, EClass eClass) {
        if ((container instanceof OccurrenceUsage || container instanceof OccurrenceDefinition) && SysmlPackage.eINSTANCE.getOccurrenceUsage().isSuperTypeOf(eClass)) {
            var membership = SysmlFactory.eINSTANCE.createFeatureMembership();
            var timeSlice = (OccurrenceUsage) SysmlFactory.eINSTANCE.create(eClass);
            membership.getOwnedRelatedElement().add(timeSlice);
            container.getOwnedRelationship().add(membership);
            return timeSlice;
        }
        return null;
    }

    /**
     * Initializes a new SysML {@link Element}.
     *
     * <p>
     * The given element is expected to be added in its container before being given to this method.
     * </p>
     *
     * @param toInit
     *            an {@link Element}
     * @param <T>
     *            the type of the {@link Element}
     * @return the given {@link Element} for convenience.
     */
    public <T extends Element> T initialize(T toInit) {
        this.elementInitializerSwitch.doSwitch(toInit);
        return toInit;
    }

    /**
     * Add an Element in a parent using the proper {@link Membership}.
     *
     * @param parent
     *         a parent element
     * @param child
     *         a child
     */
    public void addChildInParent(Element parent, Element child) {
        final Membership membership;
        if (child instanceof Feature && parent instanceof Type) {
            membership = SysmlFactory.eINSTANCE.createFeatureMembership();
        } else {
            membership = SysmlFactory.eINSTANCE.createOwningMembership();
        }
        membership.getOwnedRelatedElement().add(child);
        parent.getOwnedRelationship().add(membership);
    }

    /**
     * Create a {@link Documentation} element inside the element referenced by the given {@link EReference}. If the
     * referenced element (i.e. a RequirementUsage inside an ObjectiveMembership) doesn't exist yet, it is also
     * created. This method only works for an Objective Documentation.
     *
     * @param element
     *            the given {@link Element}
     * @param referenceName
     *            the given {@link EReference} name.
     * @return the newly created {@link Documentation}.
     */
    public Documentation createObjectiveDocumentation(Element element, String referenceName) {
        RequirementUsage objective = null;
        EStructuralFeature eStructuralFeature = element.eClass().getEStructuralFeature(referenceName);
        if (eStructuralFeature != null) {
            var existingObjective = element.eGet(eStructuralFeature);
            if (existingObjective instanceof RequirementUsage reqUsage) {
                objective = reqUsage;
            } else {
                var newObjectiveMembership = SysmlFactory.eINSTANCE.createObjectiveMembership();
                element.getOwnedRelationship().add(newObjectiveMembership);
                var newObjective = SysmlFactory.eINSTANCE.createRequirementUsage();
                newObjectiveMembership.getOwnedRelatedElement().add(newObjective);
                objective = newObjective;
            }
            var documentation = SysmlFactory.eINSTANCE.createDocumentation();
            documentation.setBody("add objective doc here");
            var owningMembership = SysmlFactory.eINSTANCE.createOwningMembership();
            objective.getOwnedRelationship().add(owningMembership);
            owningMembership.getOwnedRelatedElement().add(documentation);
            return documentation;
        }
        return null;
    }

    /**
     * Creates an allocate edge between the given source and target.
     *
     * @param source
     *            the source of the allocate edge
     * @param target
     *            the target of the allocate edge
     * @return the created {@link AllocationUsage}
     */
    public AllocationUsage createAllocateEdge(Element source, Element target) {
        var owner = source.getOwner();
        var ownerMembership = SysmlFactory.eINSTANCE.createOwningMembership();
        owner.getOwnedRelationship().add(ownerMembership);
        var allocation = SysmlFactory.eINSTANCE.createAllocationUsage();
        ownerMembership.getOwnedRelatedElement().add(allocation);
        this.addEndToAllocateEdge(allocation, source);
        this.addEndToAllocateEdge(allocation, target);
        return allocation;
    }

    private void addEndToAllocateEdge(AllocationUsage edge, Element end) {
        if (end instanceof Usage usage) {
            var featureMembership = SysmlFactory.eINSTANCE.createEndFeatureMembership();
            edge.getOwnedRelationship().add(featureMembership);
            var feature = SysmlFactory.eINSTANCE.createFeature();
            featureMembership.getOwnedRelatedElement().add(feature);
            var reference = SysmlFactory.eINSTANCE.createReferenceSubsetting();
            feature.getOwnedRelationship().add(reference);
            reference.setReferencedFeature(usage);
        }
    }

    private Feature addConnectorEnd(Connector connector, Feature end, Element endContainer, Element connectorContainer, FeatureDirectionKind defaultDirection) {
        List<Feature> sourceFeaturePath = List.of();
        // This code will not work to connect inherited non redefined feature.
        FeatureChainComputer cmp = new FeatureChainComputer();
        sourceFeaturePath = cmp.computeShortestPath(connectorContainer, end, endContainer).orElse(List.of());

        EndFeatureMembership endFeatureMembership = SysmlFactory.eINSTANCE.createEndFeatureMembership();
        connector.getOwnedRelationship().add(endFeatureMembership);

        final Feature endFeature;
        if (connector instanceof Flow) {
            endFeature = SysmlFactory.eINSTANCE.createFlowEnd();
        } else if (connector instanceof InterfaceUsage) {
            endFeature = SysmlFactory.eINSTANCE.createPortUsage();
        } else {
            endFeature = SysmlFactory.eINSTANCE.createReferenceUsage();
        }
        endFeature.setIsEnd(true);

        endFeatureMembership.getOwnedRelatedElement().add(endFeature);
        this.elementInitializerSwitch.doSwitch(endFeature);
        // For this use case, the generated name is not required
        endFeature.setDeclaredName(null);
        ReferenceSubsetting sourceReferenceSubsetting = SysmlFactory.eINSTANCE.createReferenceSubsetting();
        endFeature.getOwnedRelationship().add(sourceReferenceSubsetting);
        this.elementInitializerSwitch.doSwitch(sourceReferenceSubsetting);
        if (sourceFeaturePath.isEmpty() || sourceFeaturePath.size() == 1) {
            // If no path found create a direct reference. The model may not be valid but keep track of the desire
            // target
            sourceReferenceSubsetting.setReferencedFeature(end);
        } else {
            // We need to create a feature chain here
            Feature sourceFeatureChain = SysmlFactory.eINSTANCE.createFeature();
            for (Feature chainedFeature : sourceFeaturePath) {
                FeatureChaining featureChaining = SysmlFactory.eINSTANCE.createFeatureChaining();
                sourceFeatureChain.getOwnedRelationship().add(featureChaining);
                featureChaining.setChainingFeature(chainedFeature);
            }
            sourceReferenceSubsetting.setReferencedFeature(sourceFeatureChain);
            sourceReferenceSubsetting.getOwnedRelatedElement().add(sourceFeatureChain);
        }

        if (connector instanceof Flow) {
            var flowFeatureMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
            endFeature.getOwnedRelationship().add(flowFeatureMembership);
            var flowReferenceUsage = SysmlFactory.eINSTANCE.createReferenceUsage();
            flowFeatureMembership.getOwnedRelatedElement().add(flowReferenceUsage);
            this.elementInitializerSwitch.doSwitch(flowReferenceUsage);
            flowReferenceUsage.setDeclaredName(null);

            if (end.isSetDirection()) {
                // Since the 'end' already has a direction, use the 'end' for the redefinition.
                var redefinition = SysmlFactory.eINSTANCE.createRedefinition();
                flowReferenceUsage.getOwnedRelationship().add(redefinition);
                redefinition.setRedefiningFeature(connector);
                redefinition.setRedefinedFeature(end);
                this.elementInitializerSwitch.doSwitch(redefinition);
            } else {
                // Since the 'end' has no direction, the 'end' needs to contain a ReferenceUsage with a direction for the redefinition.
                var secondEndFeatureMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
                end.getOwnedRelationship().add(secondEndFeatureMembership);
                var endReferenceUsage = SysmlFactory.eINSTANCE.createReferenceUsage();
                endReferenceUsage.setDirection(defaultDirection);
                secondEndFeatureMembership.getOwnedRelatedElement().add(endReferenceUsage);
                this.elementInitializerSwitch.doSwitch(endReferenceUsage);

                var redefinition = SysmlFactory.eINSTANCE.createRedefinition();
                flowReferenceUsage.getOwnedRelationship().add(redefinition);
                redefinition.setRedefiningFeature(connector);
                redefinition.setRedefinedFeature(endReferenceUsage);
                this.elementInitializerSwitch.doSwitch(redefinition);
            }
        }
        return endFeature;
    }

    private Connector createConnector(Feature source, Feature target, Element sourceContainer, Element targetContainer, Namespace newConnectorContainer, Connector connectorInstance) {
        this.addChildInParent(newConnectorContainer, connectorInstance);
        this.elementInitializerSwitch.doSwitch(connectorInstance);
        this.setConnectorEnds(connectorInstance, source, target, sourceContainer, targetContainer, newConnectorContainer);
        return connectorInstance;
    }
}
