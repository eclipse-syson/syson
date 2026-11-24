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
package org.eclipse.syson.sysml.serializer.rendering.actions;

import org.eclipse.syson.sysml.serializer.model.ModelNavigation;
import org.omg.sysml.lang.sysml.AssignmentActionUsage;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.SysMLPackage;

/**
 * Builds the one-line text of a send, accept or assignment state subaction ({@code entry}, {@code do}, {@code exit})
 * such as {@code entry send s to p;}. The text is returned rather than written because the caller adds the subaction
 * kind and terminator around it. Perform subactions are not compact: they are written by
 * {@link ActionRenderer#serializePerformedPerformAction(Element, boolean)} with their regular body.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
final class CompactActionRenderer {

    private final ModelNavigation navigation;

    private final AssignmentActionRenderer assignmentRenderer;

    private final MessageActionRenderer messageRenderer;

    private final ActionNodePrefixRenderer prefixRenderer;

    /**
     * Creates the renderer.
     *
     * @param navigation
     *            the model navigation
     * @param assignmentRenderer
     *            provides inline assignment texts
     * @param messageRenderer
     *            provides inline send and accept texts
     * @param prefixRenderer
     *            provides the inline {@code action name} declaration of accept and assignment nodes
     */
    CompactActionRenderer(final ModelNavigation navigation,
            final AssignmentActionRenderer assignmentRenderer,
            final MessageActionRenderer messageRenderer,
            final ActionNodePrefixRenderer prefixRenderer) {
        this.navigation = navigation;
        this.assignmentRenderer = assignmentRenderer;
        this.messageRenderer = messageRenderer;
        this.prefixRenderer = prefixRenderer;
    }

    /**
     * Returns the inline text of a send, accept or assignment state subaction or transition effect
     * ({@code PerformedActionUsage}), with its {@code action name} declaration, or an empty string for another action.
     * Any other action yields an empty string.
     *
     * @param action
     *            the subaction
     * @return the inline text, or an empty string when the action has no compact form
     */
    String stateSubactionText(final Element action) {
        final String text;
        if (this.navigation.isMetaclass(action, SysMLPackage.eINSTANCE.getSendActionUsage())) {
            text = this.messageRenderer.sendActionText(action);
        } else if (this.navigation.isMetaclass(action, SysMLPackage.eINSTANCE.getAcceptActionUsage())) {
            text = this.prefixRenderer.inlineActionNodeDeclarationText(action) + this.messageRenderer.acceptActionText(action);
        } else {
            text = this.assignmentText(action);
        }
        return text;
    }

    /** Returns the assignment text when the action itself is an assignment. */
    private String assignmentText(final Element action) {
        String text = "";
        if (action instanceof final AssignmentActionUsage assignmentActionUsage) {
            text = this.prefixRenderer.inlineActionNodeDeclarationText(action)
                    + this.assignmentRenderer.assignmentText(assignmentActionUsage);
        }
        return text;
    }
}
