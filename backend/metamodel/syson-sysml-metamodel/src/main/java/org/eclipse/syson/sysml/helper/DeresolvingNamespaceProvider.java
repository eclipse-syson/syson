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
package org.eclipse.syson.sysml.helper;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.syson.sysml.Conjugation;
import org.eclipse.syson.sysml.Connector;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureChainExpression;
import org.eclipse.syson.sysml.FeatureChaining;
import org.eclipse.syson.sysml.FeatureReferenceExpression;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.Import;
import org.eclipse.syson.sysml.InvocationExpression;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.ReferenceSubsetting;
import org.eclipse.syson.sysml.Specialization;
import org.eclipse.syson.sysml.Type;

/**
 * Provides a {@link Namespace} that needs to be used to resolve/deresolve a name from an Element.
 *
 * @author Arthur Daussy
 */
public class DeresolvingNamespaceProvider {

    public List<Namespace> getDeresolvingNamespaces(Element element) {
        // See 8.2.3.5.2 Local and Global Namespaces
        List<Namespace> result = new ArrayList<>();
        if (element instanceof Import aImport) {
            result.add(aImport.getImportOwningNamespace());
        } else if (element instanceof Membership membership) {
            result.add(this.getDeresolvingNamespaceForMembership(membership));
        } else if (element instanceof Redefinition redefinition) {
            result.addAll(this.getDeresolvingNamespaceForRedefinition(redefinition));
        } else if (element instanceof Specialization specialization) {
            result.add(this.getDeresolvingNamespaceForSpecialization(specialization));
        } else if (element instanceof Conjugation conjugation) {
            result.add(this.getDeresolvingNamespaceForConjugation(element, conjugation));
        } else if (element instanceof FeatureChaining featureChaining) {
            result.addAll(this.getDeresolvingNamespaceFeatureChaining(element, featureChaining));
        } else {
            result.add(element.getOwningNamespace());
        }

        result = result.stream().filter(Objects::nonNull).collect(toList());

        if (result.isEmpty() && element.getOwner() instanceof Namespace ownerNamespace) {
            result.add(ownerNamespace);
        }
        if (result.isEmpty() && element.eContainer() instanceof Namespace containerNamespace) {
            result.add(containerNamespace);
        }

        return result;
    }

    private List<Namespace> getDeresolvingNamespaceForRedefinition(Redefinition redefinition) {
        /*
         * In general, the resolution of a qualified name begins with the namespace in which the name appears and
         * proceeds outwards from there to containing namespaces (see 8.2.3.5). However, the resolution of the qualified
         * names of redefined features of owned redefinitions follow special rules. In particular, the local namespace
         * of the owning type of the redefining feature is not included in the name resolution of the redefined
         * features, with resolution beginning instead with the direct supertypes of the owning type. Since redefined
         * features are not inherited, they would not be included in the local namespace of the owning type and,
         * therefore, could not be referenced by an unqualified name.
         */
        List<Namespace> result = new ArrayList<>();
        Feature redefiningFeature = redefinition.getRedefiningFeature();
        if (redefiningFeature != null) {
            Type owningType = redefiningFeature.getOwningType();
            if (owningType != null) {
                owningType.allSupertypes().stream()
                        .filter(sp -> sp != null)
                        .forEach(result::add);
                result.remove(owningType);
                // Always look into the outer scope
                result.add(owningType.getOwningNamespace());

            }
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
            ns = EMFUtils.getAncestors(Namespace.class, ns, e -> this.isLocalNamespaceForFeatureReferenceExpression(e)).stream()
                    .findFirst()
                    .orElse(null);
            if (ns != null) {
                ns = ns.getOwningNamespace();
            }
        } else if (ns instanceof FeatureChainExpression featureChainExpression) {
            // If the membershipOwningNamespace is a FeatureChainExpression see 8.3.4.8.3, then the local
            // Namespace is the result parameter of the argument Expression of the FeatureChainExpression.
            ns = featureChainExpression.getArgument().stream()
                    .map(Expression::getResult)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);
        }
        return ns;
    }

    private boolean isLocalNamespaceForFeatureReferenceExpression(EObject n) {
        return n != null
                && !(n instanceof FeatureReferenceExpression)
                && !(n instanceof InvocationExpression)
                && !this.isOwnedFeatureOfInvocationExpression(n);
    }

    private boolean isOwnedFeatureOfInvocationExpression(EObject n) {
        if (n instanceof Feature feature) {
            return ((Feature) n).getOwningType() instanceof InvocationExpression;
        }
        return false;
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
        Namespace result = null;
        if (specialization instanceof ReferenceSubsetting refSubsetting) {
            Feature feature = refSubsetting.getReferencingFeature();
            if (feature != null && feature.isIsEnd() && feature.getOwningType() instanceof Connector connector) {
                result = connector.getOwningNamespace();
            }
        }

        if (result == null && specialization instanceof FeatureTyping featureTyping && featureTyping.getOwningFeature() instanceof InvocationExpression) {
            // If the Specialization is a FeatureTyping (see 8.3.3.3.6), and its owningFeature is an
            // InvocationExpression, then the local Namespace is the non-invocation Namespace for the
            // owningFeature (determined as for a FeatureReferenceExpression under Membership above).
            result = EMFUtils.getAncestors(Namespace.class, featureTyping.getOwningFeature(), e -> !this.isLocalNamespaceForFeatureReferenceExpression(e)).stream()
                    .findFirst()
                    .orElse(null);

        }
        if (result == null && specialization.getOwningType() != null) {
            // Otherwise, if the owningType is not null, then the local Namespace is the owningNamespace of the
            // owningType.
            result = specialization.getOwningType().getOwningNamespace();
        }
        if (result == null) {
            // Otherwise, the local Namespace is the owningNamespace of the Specialization.
            result = specialization.getOwningNamespace();
        }
        return result;
    }

    private List<Namespace> getDeresolvingNamespaceFeatureChaining(Element element, FeatureChaining featureChaining) {
        final List<Namespace> result;
        // If the FeatureChaining is the first ownedFeatureChaining of its featureChained, then the local
        // Namespace is determined as if the owningRelationship of the featureChained (which will be a
        // Membership, Subsetting or Conjugation) was the context Relationship (see above).
        // Otherwise, the local Namespace is the chainingFeature of the previous FeatureChaining in the
        // ownedFeatureChaining list.
        var featureChained = featureChaining.getFeatureChained();
        var ownedFeatureChaining = featureChained.getOwnedFeatureChaining();
        int indexOf = ownedFeatureChaining.indexOf(featureChaining);
        if (indexOf != -1) {
            if (indexOf == 0) {
                result = this.getDeresolvingNamespaces(featureChained.getOwningRelationship());
            } else {
                var chainingFeature = ownedFeatureChaining.get(indexOf - 1).getChainingFeature();
                if (chainingFeature != null) {
                    result = List.of(chainingFeature);
                } else {
                    result = List.of();
                }
            }
        } else {
            result = List.of(element.getOwningNamespace());
        }
        return result;
    }
}
