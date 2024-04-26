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
package org.eclipse.syson.sysml.export.utils;

import java.util.Comparator;
import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.syson.sysml.Conjugation;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureChainExpression;
import org.eclipse.syson.sysml.FeatureChaining;
import org.eclipse.syson.sysml.FeatureReferenceExpression;
import org.eclipse.syson.sysml.Import;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.Specialization;
import org.eclipse.syson.sysml.Type;

/**
 * Object in charge of converting an Element to a resolvable qualified name depending of its context.
 * 
 * @author Arthur Daussy
 */
public class NameDeresolver {

    public String getDeresolvedName(Element element, Element context) {

        if (element == null) {
            return null;
        }

        Namespace deresolvingNamespace = getDeresolvingNamespace(context);

        final String qualifiedName;
        if (deresolvingNamespace == null) {
            qualifiedName = element.getQualifiedName();
        } else {
            qualifiedName = deresolve(element, deresolvingNamespace);
        }

        return qualifiedName;

    }

    /**
     * @param element
     * @param deresolvingNamespace
     * @return
     */
    private String deresolve(Element element, Namespace deresolvingNamespace) {

        final String qualifiedName;
        if (deresolvingNamespace == null) {
            qualifiedName = element.getQualifiedName();
        } // Shortcut in most case use the simple name
        else if (element.getOwningNamespace() == deresolvingNamespace) {
            qualifiedName = Appender.toPrintableName(element.getName());
        } else {
            List<Membership> importedContainer = deresolvingNamespace.visibleMemberships(new BasicEList<>(), false, false).stream()
                    .filter(m -> m.getMemberElement() != null)
                    .filter(m -> EcoreUtil.isAncestor(m.getMemberElement(), element))
                    .toList();

            if (!importedContainer.isEmpty()) {
                // found a match compute qualified name
                qualifiedName = getShortestQualifiedName(importedContainer, element, deresolvingNamespace);
            } else {
                qualifiedName = deresolve(element, deresolvingNamespace.getOwningNamespace());
            }

        }

        return qualifiedName;
    }

    private String getShortestQualifiedName(List<Membership> importedContainer, Element element, Namespace owningNamespace) {
        String elementQn = element.getQualifiedName();

        String shortQn = importedContainer.stream().map(m -> buildRelativeQualifiedName(elementQn, element, owningNamespace, m)).sorted(Comparator.comparing(String::length)).findFirst().get();
        return shortQn;
    }

    private String buildRelativeQualifiedName(String elementQn, Element element, Namespace owningNamespace, Membership m) {
        Element memberElement = m.getMemberElement();
        if (element == memberElement) {
            return Appender.toPrintableName(element.getDeclaredName());
        }
        return elementQn.substring(owningNamespace.getQualifiedName().length() + 2, elementQn.length());
    }

    private Namespace getDeresolvingNamespace(Element element) {
        // See 8.2.3.5.2 Local and Global Namespaces
        final Namespace result;
        if (element instanceof Import aImport) {
            result = aImport.getImportOwningNamespace();
        } else if (element instanceof Membership membership) {
            result = getDeresolvingNamespaceForMembership(membership);
        } else if (element instanceof Specialization specialization) {
            result = getDeresolvingNamespaceForSpecialization(specialization);
        } else if (element instanceof Conjugation conjugation) {
            result = getDeresolvingNamespaceForConjugation(element, conjugation);
        } else if (element instanceof FeatureChaining featureChaining) {
            result = getDeresolvingNamespaceFeatureChaining(element, featureChaining);
        } else {
            result = element.getOwningNamespace();
        }

        return result;
    }

    private Namespace getDeresolvingNamespaceForMembership(Membership membership) {
        Namespace ns = membership.getMembershipOwningNamespace();
        if (ns instanceof FeatureReferenceExpression featureRef) {
            // If the membershipOwningNamespace is a FeatureReferenceExpression (see 8.3.4.8.4), then the
            // local Namespace is the non-invocation Namespace for the membershipOwningNamespace, which is
            // defined to be the nearest containing Namespace that is none of the following:
            // FeatureReferenceExpression
            // InvocationExpression
            // ownedFeature of an InvocationExpression
        } else if (membership instanceof FeatureChainExpression featureChainExpression) {
            // If the membershipOwningNamespace is a FeatureChainExpression see 8.3.4.8.3, then the local
            // Namespace is the result parameter of the argument Expression of the FeatureChainExpression.
            Feature resultFeature = featureChainExpression.getResult();
            if (resultFeature != null) {
                ns = resultFeature;
            }
        }

        return ns;
    }

    private Namespace getDeresolvingNamespaceForConjugation(Element element, Conjugation conjugation) {
        final Namespace result;
        // If the owningType is not null, the local Namespace is the owningNamespace of the owningType.
        // Otherwise, the local Namespace is the owningNamespace of the Conjugation.
        Type type = conjugation.getOwningType();
        if (type != null) {
            result = type.getOwningNamespace();
        } else {
            result = element.getOwningNamespace();
        }
        return result;
    }

    private Namespace getDeresolvingNamespaceForSpecialization(Specialization element) {
        // If the Specialization is a ReferenceSubsetting (see 8.3.3.3.9), and its referencingFeature is
        // an end Feature whose owningType is a Connector, then the local Namespace is the
        // owningNamespace of the Connector.
        // If the Specialization is a FeatureTyping (see 8.3.3.3.6), and its owningFeature is an
        // InvocationExpression, then the local Namespace is the non-invocation Namespace for the
        // owningFeature (determined as for a FeatureReferenceExpression under Membership above).
        // Otherwise, if the owningType is not null, then the local Namespace is the owningNamespace of the
        // owningType.
        // Otherwise, the local Namespace is the owningNamespace of the Specialization.

        return element.getOwningNamespace();
    }

    private Namespace getDeresolvingNamespaceFeatureChaining(Element element, FeatureChaining featureChaining) {
        final Namespace result;
        // If the FeatureChaining is the first ownedFeatureChaining of its featureChained, then the local
        // Namespace is determined as if the owningRelationship of the featureChained (which will be a
        // Membership, Subsetting or Conjugation) was the context Relationship (see above).
        // Otherwise, the local Namespace is the chainingFeature of the previous FeatureChaining in the
        // ownedFeatureChaining list.
        Feature featureChained = featureChaining.getFeatureChained();
        int indexOf = featureChained.getOwnedFeatureChaining().indexOf(featureChaining);
        if (indexOf != -1) {

            if (indexOf == 0) {
                result = getDeresolvingNamespace(featureChained.getOwningRelationship());
            } else {
                result = getDeresolvingNamespace(featureChained.getOwnedFeatureChaining().get(indexOf - 1));
            }
        } else {
            result = element.getOwningNamespace();
        }

        return result;
    }

}
