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
package org.eclipse.syson.sysml.helper;

import org.eclipse.syson.sysml.Conjugation;
import org.eclipse.syson.sysml.Connector;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureChainExpression;
import org.eclipse.syson.sysml.FeatureChaining;
import org.eclipse.syson.sysml.FeatureReferenceExpression;
import org.eclipse.syson.sysml.Import;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.ReferenceSubsetting;
import org.eclipse.syson.sysml.Specialization;
import org.eclipse.syson.sysml.Type;

/**
 * Provides a {@link Namespace} that needs to be used to resolve/deresolve a name from an Element.
 *
 * @author Arthur Daussy
 */
public class DeresolvingNamespaceProvider {

    public Namespace getDeresolvingNamespace(Element element) {
        // See 8.2.3.5.2 Local and Global Namespaces
        final Namespace result;
        if (element instanceof Import aImport) {
            result = aImport.getImportOwningNamespace();
        } else if (element instanceof Membership membership) {
            result = this.getDeresolvingNamespaceForMembership(membership);
        } else if (element instanceof Specialization specialization) {
            result = this.getDeresolvingNamespaceForSpecialization(specialization);
        } else if (element instanceof Conjugation conjugation) {
            result = this.getDeresolvingNamespaceForConjugation(element, conjugation);
        } else if (element instanceof FeatureChaining featureChaining) {
            result = this.getDeresolvingNamespaceFeatureChaining(element, featureChaining);
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

    private Namespace getDeresolvingNamespaceForSpecialization(Specialization specialization) {
        // If the Specialization is a ReferenceSubsetting (see 8.3.3.3.9), and its referencingFeature is
        // an end Feature whose owningType is a Connector, then the local Namespace is the
        // owningNamespace of the Connector.
        if (specialization instanceof ReferenceSubsetting refSubsetting) {
            Feature feature = refSubsetting.getReferencingFeature();
            if (feature != null && feature.isIsEnd() && feature.getOwningType() instanceof Connector connector) {
                return connector.getOwningNamespace();
            }
        }
        // If the Specialization is a FeatureTyping (see 8.3.3.3.6), and its owningFeature is an
        // InvocationExpression, then the local Namespace is the non-invocation Namespace for the
        // owningFeature (determined as for a FeatureReferenceExpression under Membership above).
        // Otherwise, if the owningType is not null, then the local Namespace is the owningNamespace of the
        // owningType.
        // Otherwise, the local Namespace is the owningNamespace of the Specialization.

        return specialization.getOwningNamespace();
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
                result = this.getDeresolvingNamespace(featureChained.getOwningRelationship());
            } else {
                result = featureChained.getOwnedFeatureChaining().get(indexOf - 1).getChainingFeature();
            }
        } else {
            result = element.getOwningNamespace();
        }

        return result;
    }

}
