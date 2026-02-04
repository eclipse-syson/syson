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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import org.eclipse.syson.sysml.ActorMembership;
import org.eclipse.syson.sysml.ConnectorAsUsage;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.SubjectMembership;

/**
 * Element-related services doing queries. This class should not depend on sirius-web services or other spring services.
 *
 * @author arichard
 */
public class MetamodelQueryElementService {

    /**
     * Returns {@code true} if the provided {@code element} is an actor, {@code false} otherwise.
     * <p>
     * An actor (typically of a UseCase or Requirement) is a kind of parameter stored in an {@link ActorMembership}.
     * </p>
     *
     * @param element
     *            the element to check
     * @return {@code true} if the provided {@code element} is an actor, {@code false} otherwise
     */
    public boolean isActor(Element element) {
        return element instanceof PartUsage && element.getOwningMembership() instanceof ActorMembership;
    }

    /**
     * Returns {@code true} if the provided {@code element} is a subject, {@code false} otherwise.
     * <p>
     * A subject (typically of a UseCase or Requirement) is a kind of parameter stored in an {@link SubjectMembership}.
     * </p>
     *
     * @param element
     *            the element to check
     * @return {@code true} if the provided {@code element} is a subject, {@code false} otherwise
     */
    public boolean isSubject(Element element) {
        return element instanceof ReferenceUsage && element.getOwningMembership() instanceof SubjectMembership;
    }

    /**
     * Gets the target of a {@link ConnectorAsUsage}.
     *
     * @param connector
     *         a {@link ConnectorAsUsage}
     * @return a list of targets
     */
    public List<Feature> getTarget(ConnectorAsUsage connector) {
        return connector.getTargetFeature().stream()
                .filter(Objects::nonNull)
                .map(Feature::getFeatureTarget)
                .toList();
    }

    /**
     * Gets the source of a {@link ConnectorAsUsage}.
     *
     * @param connectorAsUsage
     *         a {@link ConnectorAsUsage}
     * @return the source feature
     */
    public Feature getSource(ConnectorAsUsage connectorAsUsage) {
        Feature sourceFeature = connectorAsUsage.getSourceFeature();
        if (sourceFeature != null) {
            return sourceFeature.getFeatureTarget();
        }
        return null;
    }

    /**
     * Gets the closest common owner for both given {@link Element}.
     *
     * @param e1
     *         an Element
     * @param e2
     *         another Element
     * @return an optional common owning ancestor with the expected type
     */
    public <T extends Element> Optional<T> getCommonOwnerAncestor(Element e1, Element e2, Class<T> expectedType, Predicate<T> test) {

        // Collect all owners of e1;
        List<T> e1Owners = new ArrayList<>();
        Element currentE1Owner = e1;
        while (currentE1Owner != null) {
            if (expectedType.isInstance(currentE1Owner) && (test == null || test.test(expectedType.cast(currentE1Owner)))) {
                e1Owners.add((T) currentE1Owner);
            }
            currentE1Owner = currentE1Owner.getOwner();
        }

        // Navigate on e2 owners
        Element currentE2Owner = e2;
        while (currentE2Owner != null) {
            if (e1Owners.contains(currentE2Owner)) {
                return Optional.of(expectedType.cast(currentE2Owner));
            }
            currentE2Owner = currentE2Owner.getOwner();
        }

        return Optional.empty();
    }
}
