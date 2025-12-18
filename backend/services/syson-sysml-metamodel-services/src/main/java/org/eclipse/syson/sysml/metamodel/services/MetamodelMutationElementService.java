/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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
import java.util.Optional;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.sirius.components.emf.utils.SiriusEMFCopier;
import org.eclipse.syson.sysml.BindingConnectorAsUsage;
import org.eclipse.syson.sysml.ConnectionUsage;
import org.eclipse.syson.sysml.ConnectorAsUsage;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.EndFeatureMembership;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureChaining;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.FlowEnd;
import org.eclipse.syson.sysml.FlowUsage;
import org.eclipse.syson.sysml.InterfaceUsage;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.ReferenceSubsetting;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.helper.EMFUtils;
import org.eclipse.syson.sysml.util.ElementUtil;
import org.springframework.stereotype.Service;

/**
 * Element-related services doing mutations. This class should not depend on sirius-web services or other spring
 * services.
 *
 * @author arichard
 */
@Service
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
     * Sets the connector ends of a {@link ConnectorAsUsage}.
     *
     * @param connectorAsUsage
     *         The connector to configure
     * @param source
     *         the source of the connector
     * @param target
     *         the target of the connector
     * @param connectorContainer
     *         the container of the connector. If the container is a type will intend to compute a feature chain between the container and the ends.
     */
    public void setConnectorEnds(ConnectorAsUsage connectorAsUsage, Feature source, Feature target, Element connectorContainer) {
        if (connectorAsUsage instanceof FlowUsage flowUsage) {
            connectorAsUsage.getOwnedRelationship().add(this.createFlowConnectionEnd(source));
            connectorAsUsage.getOwnedRelationship().add(this.createFlowConnectionEnd(target));
        } else {
            this.addConnectorEnd(connectorAsUsage, source, connectorContainer);
            this.addConnectorEnd(connectorAsUsage, target, connectorContainer);
        }

    }

    /**
     * Create a new EndFeatureMembership to be used as {@link FlowUsage} end.
     *
     * @param targetedFeature
     *         the targeted feature (either the source or target of the flow)
     * @return the new EndFeatureMembership
     */
    // This method should be private at some point.
    // We still need it public because of the old architecture relying on ViewCreateService
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

    public FlowUsage createFlowUsage(Feature source, Feature target, Namespace flowContainer) {
        FeatureMembership featureMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
        flowContainer.getOwnedRelationship().add(featureMembership);

        FlowUsage flowUsage = SysmlFactory.eINSTANCE.createFlowUsage();
        this.addChildInParent(flowContainer, flowUsage);
        this.elementInitializerSwitch.doSwitch(flowUsage);

        flowUsage.getOwnedRelationship().add(this.createFlowConnectionEnd(source));
        flowUsage.getOwnedRelationship().add(this.createFlowConnectionEnd(target));
        return flowUsage;
    }

    public InterfaceUsage createInterfaceUsage(PortUsage sourcePort, PortUsage targetPort, Namespace interfaceContainer) {
        InterfaceUsage interfaceUsage = SysmlFactory.eINSTANCE.createInterfaceUsage();
        this.addChildInParent(interfaceContainer, interfaceUsage);
        this.elementInitializerSwitch.doSwitch(interfaceUsage);
        // Edges should have an empty default name. This is not the case when using the initializer, because
        // InterfaceUsage can be a node, which requires a default name.
        interfaceUsage.setDeclaredName("");

        EndFeatureMembership sourceEndFeatureMembership = SysmlFactory.eINSTANCE.createEndFeatureMembership();
        interfaceUsage.getOwnedRelationship().add(sourceEndFeatureMembership);
        Feature sourceFeature = SysmlFactory.eINSTANCE.createFeature();
        sourceFeature.setIsEnd(true);
        sourceEndFeatureMembership.getOwnedRelatedElement().add(sourceFeature);
        this.elementInitializerSwitch.doSwitch(sourceFeature);
        ReferenceSubsetting sourceReferenceSubsetting = SysmlFactory.eINSTANCE.createReferenceSubsetting();
        sourceFeature.getOwnedRelationship().add(sourceReferenceSubsetting);
        this.elementInitializerSwitch.doSwitch(sourceReferenceSubsetting);
        sourceReferenceSubsetting.setReferencedFeature(sourcePort);

        EndFeatureMembership targetEndFeatureMembership = SysmlFactory.eINSTANCE.createEndFeatureMembership();
        interfaceUsage.getOwnedRelationship().add(targetEndFeatureMembership);
        Feature targetFeature = SysmlFactory.eINSTANCE.createFeature();
        targetFeature.setIsEnd(true);
        targetEndFeatureMembership.getOwnedRelatedElement().add(targetFeature);
        this.elementInitializerSwitch.doSwitch(targetFeature);
        ReferenceSubsetting targetReferenceSubsetting = SysmlFactory.eINSTANCE.createReferenceSubsetting();
        targetFeature.getOwnedRelationship().add(targetReferenceSubsetting);
        this.elementInitializerSwitch.doSwitch(targetReferenceSubsetting);
        targetReferenceSubsetting.setReferencedFeature(targetPort);

        return interfaceUsage;
    }

    public BindingConnectorAsUsage createBindingConnectorAsUsage(Feature source, Feature target, Namespace container) {
        return (BindingConnectorAsUsage) this.createConnectorAsUsage(source, target, container, SysmlFactory.eINSTANCE.createBindingConnectorAsUsage());
    }


    public ConnectionUsage createConnectionUsage(Feature source, Feature target, Namespace owner) {
        return (ConnectionUsage) this.createConnectorAsUsage(source, target, owner, SysmlFactory.eINSTANCE.createConnectionUsage());
    }

    public <T extends Element> T initialize(T toInit) {
        this.elementInitializerSwitch.doSwitch(toInit);
        return toInit;
    }


    public void addChildInParent(Element parent, Element child) {
        // Parent could be a Package where children are stored
        // in OwingMembership and not in FeatureMembership.
        Membership membership = SysmlFactory.eINSTANCE.createFeatureMembership();
        if (parent instanceof Package) {
            membership = SysmlFactory.eINSTANCE.createOwningMembership();
        }
        membership.getOwnedRelatedElement().add(child);
        parent.getOwnedRelationship().add(membership);
    }

    private Feature addConnectorEnd(ConnectorAsUsage connectorAsUsage, Feature end, Element connectorContainer) {
        List<Feature> sourceFeaturePath = List.of();
        // If the container is a type try to compute the feature chain to access the feature ends from the container
        if (connectorContainer instanceof Type containerType) {
            // This code will not work to connect inherited non redefined feature. In such case we should
            FeatureChainComputer cmp = new FeatureChainComputer();
            sourceFeaturePath = cmp.computeShortestPath(containerType, end).orElse(List.of());
        }

        EndFeatureMembership sourceEndFeatureMembership = SysmlFactory.eINSTANCE.createEndFeatureMembership();
        connectorAsUsage.getOwnedRelationship().add(sourceEndFeatureMembership);
        Feature sourceFeature = SysmlFactory.eINSTANCE.createReferenceUsage();
        sourceFeature.setIsEnd(true);


        sourceEndFeatureMembership.getOwnedRelatedElement().add(sourceFeature);
        this.elementInitializerSwitch.doSwitch(sourceFeature);
        // For this use case, the generated name is not required
        sourceFeature.setDeclaredName(null);
        ReferenceSubsetting sourceReferenceSubsetting = SysmlFactory.eINSTANCE.createReferenceSubsetting();
        sourceFeature.getOwnedRelationship().add(sourceReferenceSubsetting);
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
        return sourceFeature;
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

    private ConnectorAsUsage createConnectorAsUsage(Feature source, Feature target, Namespace owner, ConnectorAsUsage connectorInstance) {
        this.addChildInParent(owner, connectorInstance);
        this.elementInitializerSwitch.doSwitch(connectorInstance);

        this.setConnectorEnds(connectorInstance, source, target, owner);

        return connectorInstance;
    }

}
