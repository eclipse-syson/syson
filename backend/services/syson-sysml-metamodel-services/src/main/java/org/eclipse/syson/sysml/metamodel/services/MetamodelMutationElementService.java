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
import org.eclipse.syson.sysml.Connector;
import org.eclipse.syson.sysml.Documentation;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.EndFeatureMembership;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureChaining;
import org.eclipse.syson.sysml.Flow;
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
        this.addConnectorEnd(connector, source, sourceContainer, newConnectorContainer);
        this.addConnectorEnd(connector, target, targetContainer, newConnectorContainer);
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
     * @param sourcePort
     *            the source of the interface
     * @param targetPort
     *            the target of the interface
     * @param sourceContainer
     *            the semantic element corresponding to the graphical container of the source
     * @param targetContainer
     *            the semantic element corresponding to the graphical container of the target
     * @param interfaceContainer
     *            the container of the new {@link InterfaceUsage}
     * @return a new {@link InterfaceUsage}
     */
    public InterfaceUsage createInterfaceUsage(PortUsage source, PortUsage target, Element sourceContainer, Element targetContainer, Namespace newConnectionContainer) {
        return (InterfaceUsage) this.createConnector(source, target, sourceContainer, targetContainer, newConnectionContainer, SysmlFactory.eINSTANCE.createInterfaceUsage());
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

    private Feature addConnectorEnd(Connector connector, Feature end, Element endContainer, Element connectorContainer) {
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
            var secondEndFeatureMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
            end.getOwnedRelationship().add(secondEndFeatureMembership);
            var endReferenceUsage = SysmlFactory.eINSTANCE.createReferenceUsage();
            secondEndFeatureMembership.getOwnedRelatedElement().add(endReferenceUsage);
            this.elementInitializerSwitch.doSwitch(endReferenceUsage);

            var flowFeatureMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
            endFeature.getOwnedRelationship().add(flowFeatureMembership);
            var flowReferenceUsage = SysmlFactory.eINSTANCE.createReferenceUsage();
            flowFeatureMembership.getOwnedRelatedElement().add(flowReferenceUsage);
            this.elementInitializerSwitch.doSwitch(flowReferenceUsage);
            flowReferenceUsage.setDeclaredName(null);
            var redefinition = SysmlFactory.eINSTANCE.createRedefinition();
            flowReferenceUsage.getOwnedRelationship().add(redefinition);
            redefinition.setRedefiningFeature(connector);
            redefinition.setRedefinedFeature(end);
            this.elementInitializerSwitch.doSwitch(redefinition);
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
