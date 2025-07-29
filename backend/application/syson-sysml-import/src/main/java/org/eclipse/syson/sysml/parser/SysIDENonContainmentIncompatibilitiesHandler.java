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
package org.eclipse.syson.sysml.parser;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Objects;

import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.MetadataAccessExpression;
import org.eclipse.syson.sysml.SysmlFactory;

/**
 * In charge of handling non containment reference incompatibilities between the AST provided by SysIDE and the current
 * version of SysON SysML metamodel.
 *
 * @technical-debt
 *
 * @author Arthur Daussy
 */
public class SysIDENonContainmentIncompatibilitiesHandler {

    private final NonContainmentReferenceHandler nonContainmentHandler;

    public SysIDENonContainmentIncompatibilitiesHandler(NonContainmentReferenceHandler nonContainmentHandler) {
        this.nonContainmentHandler = Objects.requireNonNull(nonContainmentHandler);
    }

    /**
     * Tries to handle a special case of non containment reference that would normally result in a proxy creation.
     *
     * @param owner
     *            owned of non containment reference
     * @param referenceName
     *            the name of the non containment reference
     * @param astValue
     *            the {@link JsonNode} representing the reference
     * @return <code>true</code> if a special case has been handled
     */
    public boolean handleNonContainmentReference(final Element owner, String referenceName, final JsonNode astValue) {
        final boolean hasHandledSomething;
        if (this.isMetadataAccessExpressionCase(owner, referenceName, astValue)) {

            /**
             * <p>
             * Handle special case of MetadataAccessExpression.ReferencedElement that have changed between 2024-12 and
             * 2025-04 versions of the SySML V2 specification.
             * </p>
             * <p>
             * In 2024-12, this is a non-derived EReference from MetadataAccessExpression to an element. In 2025-04,
             * this a derived EReference computed from an ownedMembership. As a consequence the current version of
             * SysIDE import that information as a reference and not has SysML structure with membership. This special
             * case, aims to provide a workaround by creating the required Membership inside the
             * MetadataAccessExpression and setting the proxy from MetadataAccessExpression.ReferencedElement to
             * Membership.memberElement.
             * </p>
             * <p>
             * This code needs to be removed/reworked when the current version of SysIDE will be at least 2025-04.
             * <p>
             */
            this.handleMetadataAccessExpression(owner, referenceName, astValue);
            hasHandledSomething = true;
        } else {
            hasHandledSomething = false;
        }

        return hasHandledSomething;
    }

    private boolean isMetadataAccessExpressionCase(final Element owner, String referenceName, final JsonNode astValue) {
        return owner instanceof MetadataAccessExpression && "reference".equals(referenceName);
    }

    private void handleMetadataAccessExpression(final Element owner, String referenceName, final JsonNode astValue) {
        Membership membership = SysmlFactory.eINSTANCE.createMembership();
        owner.getOwnedRelationship().add(membership);
        this.nonContainmentHandler.createProxy(membership, "memberElement", astValue);
    }
}
