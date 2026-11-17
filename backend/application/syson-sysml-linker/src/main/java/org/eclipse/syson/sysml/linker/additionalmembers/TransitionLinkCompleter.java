/*******************************************************************************
 * Copyright (c) 2026 Obeo.
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
package org.eclipse.syson.sysml.linker.additionalmembers;

import java.util.Collections;
import java.util.List;

import org.omg.sysml.lang.sysml.ActionUsage;
import org.omg.sysml.lang.sysml.BindingConnector;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Feature;
import org.omg.sysml.lang.sysml.FeatureMembership;
import org.omg.sysml.lang.sysml.Membership;
import org.omg.sysml.lang.sysml.ParameterMembership;
import org.omg.sysml.lang.sysml.Relationship;
import org.omg.sysml.lang.sysml.Succession;
import org.omg.sysml.lang.sysml.SysMLFactory;
import org.omg.sysml.lang.sysml.SysMLPackage;
import org.omg.sysml.lang.sysml.TransitionFeatureMembership;
import org.omg.sysml.lang.sysml.TransitionUsage;
import org.omg.sysml.lang.sysml.Type;
import org.omg.sysml.util.ConnectorUtil;
import org.omg.sysml.util.FeatureUtil;
import org.omg.sysml.util.TypeUtil;

/**
 * Completes the transition-link feature and its binding connectors.
 * <p>
 * This completer must run after reference resolution and after {@link TransitionSourceCompleter}. In particular, the transition succession, source and the context references used while transforming
 * binding connectors must be resolved. It must also run before the inheritance caches are cleared and recomputed. Completion is detected from the transition-link feature inserted in the model, so the
 * completer does not retain lifecycle state.
 * </p>
 *
 * @author Arthur Daussy
 */
public final class TransitionLinkCompleter {

    private TransitionLinkCompleter() {
        // Utility class.
    }

    /**
     * Completes the transition-link feature and binding connectors if absent.
     */
    public static void completeTransitionLinks(TransitionUsage transition) {
        if (getTransitionLinkFeature(transition) != null) {
            return;
        }

        Succession succession = getOwnedSuccession(transition);
        if (succession != null) {
            Feature transitionLink = SysMLFactory.eINSTANCE.createReferenceUsage();
            TypeUtil.addOwnedFeatureTo(transition, transitionLink);
            addBindingConnector(transition, succession, transitionLink);
        }

        List<Feature> parameters = TypeUtil.getOwnedParametersOf(transition);
        Feature source = getSourceWithoutDeriving(transition);
        if (!parameters.isEmpty() && source != null
                && !hasImplicitBindingConnector(transition, source, parameters.get(0))) {
            addBindingConnector(transition, source, parameters.get(0));
        }
    }

    private static BindingConnector addBindingConnector(Type type, Feature source, Feature target) {
        BindingConnector connector = ConnectorUtil.createBindingConnector(source, target);
        ConnectorUtil.transformBindingConnector(connector);
        Type contextType = ConnectorUtil.getContextTypeFor(connector);
        if (contextType != null && contextType != type) {
            FeatureUtil.addFeaturingTypesTo(connector, Collections.singleton(contextType));
        }

        return connector;
    }

    private static boolean hasImplicitBindingConnector(TransitionUsage transition, Feature source, Feature target) {
        boolean[] found = { false };
        TypeUtil.forEachImplicitBindingConnectorOf(transition, (connector, membershipKind) -> {
            List<Feature> relatedFeatures = connector.getRelatedFeature();
            if (relatedFeatures.size() >= 2
                    && relatedFeatures.get(0) == source
                    && relatedFeatures.get(1) == target) {
                found[0] = true;
            }
        });
        return found[0];
    }

    private static Succession getOwnedSuccession(TransitionUsage transition) {
        return transition.getOwnedRelationship().stream().map(Relationship::getOwnedRelatedElement)
                .flatMap(List::stream).filter(Succession.class::isInstance).map(Succession.class::cast)
                .findFirst().orElse(null);
    }

    private static Feature getTransitionLinkFeature(TransitionUsage transition) {
        for (Relationship relationship : transition.getOwnedRelationship()) {
            if (relationship instanceof FeatureMembership
                    && !(relationship instanceof TransitionFeatureMembership
                    || relationship instanceof ParameterMembership)) {
                for (Element element : relationship.getOwnedRelatedElement()) {
                    if (element instanceof Feature feature) {
                        return feature;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Reads the source membership directly instead of calling {@link TransitionUsage#getSource()}. The derived getter triggers additional-member completion and would therefore re-enter this
     * completer.
     */
    private static Feature getSourceWithoutDeriving(TransitionUsage transition) {
        for (Relationship relationship : transition.getOwnedRelationship()) {
            if (relationship instanceof Membership membership
                    && !(membership instanceof FeatureMembership)) {
                Object member = membership.eGet(SysMLPackage.Literals.MEMBERSHIP__MEMBER_ELEMENT, false);
                if (member instanceof Feature feature
                        && FeatureUtil.getBasicFeatureOf(feature) instanceof ActionUsage) {
                    return feature;
                }
            }
        }
        return null;
    }
}
