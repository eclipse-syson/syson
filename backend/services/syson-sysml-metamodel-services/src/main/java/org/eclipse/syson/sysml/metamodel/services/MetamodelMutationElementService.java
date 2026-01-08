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

import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.syson.sysml.BindingConnectorAsUsage;
import org.eclipse.syson.sysml.ConnectionUsage;
import org.eclipse.syson.sysml.ConnectorAsUsage;
import org.eclipse.syson.sysml.Documentation;
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
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.Type;

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
            flowUsage.getOwnedRelationship().add(this.createFlowConnectionEnd(source));
            flowUsage.getOwnedRelationship().add(this.createFlowConnectionEnd(target));
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

    /**
     * Creates a new {@link FlowUsage} in the given container.
     *
     * @param source
     *         the source the flow
     * @param target
     *         the target the flow
     * @param flowContainer
     *         the container of the new {@link FlowUsage}
     * @return a new {@link FlowUsage}
     */
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

    /**
     * Creates a new {@link InterfaceUsage} in the given container.
     *
     * @param sourcePort
     *         the source the interface
     * @param targetPort
     *         the target the interface
     * @param interfaceContainer
     *         the container of the new interface
     * @return a new {@link InterfaceUsage}
     */
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

    /**
     * Creates a new {@link BindingConnectorAsUsage} in the given container.
     *
     * @param source
     *         the source the binding
     * @param target
     *         the target the binding
     * @param container
     *         the container of the new binding
     * @return a new {@link BindingConnectorAsUsage}
     */
    public BindingConnectorAsUsage createBindingConnectorAsUsage(Feature source, Feature target, Namespace container) {
        return (BindingConnectorAsUsage) this.createConnectorAsUsage(source, target, container, SysmlFactory.eINSTANCE.createBindingConnectorAsUsage());
    }

    /**
     * Creates a new {@link ConnectionUsage} in the given container.
     *
     * @param source
     *         the source the connection
     * @param target
     *         the target the connection
     * @param container
     *         the container of the new connection
     * @return a new {@link ConnectionUsage}
     */
    public ConnectionUsage createConnectionUsage(Feature source, Feature target, Namespace container) {
        return (ConnectionUsage) this.createConnectorAsUsage(source, target, container, SysmlFactory.eINSTANCE.createConnectionUsage());
    }

    /**
     * Initializes a new SysML {@link Element}.
     *
     * <p>The given element is expected to be added in its container before being given to this method.</p>
     *
     * @param toInit
     *         an {@link Element}
     * @param <T>
     *         the type of the {@link Element}
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
     * referenced element (i.e. a RequirementUsage inside an ObjectiveMembership) doesn't exists yet, it is also
     * created. This method only works for an Objective Documentation.
     *
     * @param element
     *            the given {@link Element}
     * @param reference
     *            the given {@link EReference}.
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

    private ConnectorAsUsage createConnectorAsUsage(Feature source, Feature target, Namespace owner, ConnectorAsUsage connectorInstance) {
        this.addChildInParent(owner, connectorInstance);
        this.elementInitializerSwitch.doSwitch(connectorInstance);

        this.setConnectorEnds(connectorInstance, source, target, owner);

        return connectorInstance;
    }

}
