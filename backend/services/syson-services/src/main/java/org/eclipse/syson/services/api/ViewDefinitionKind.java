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
package org.eclipse.syson.services.api;

/**
 * The list of Standard ViewDefintions handled in SysON.
 *
 * @author arichard
 */
public enum ViewDefinitionKind {
    GENERAL_VIEW, INTERCONNECTION_VIEW, ACTION_FLOW_VIEW, STATE_TRANSITION_VIEW;

    public static ViewDefinitionKind getKind(String viewDefinitionName) {
        ViewDefinitionKind kind = null;
        if ("StandardViewDefinitions::GeneralView".equals(viewDefinitionName)) {
            kind = GENERAL_VIEW;
        } else if ("StandardViewDefinitions::InterconnectionView".equals(viewDefinitionName)) {
            kind = INTERCONNECTION_VIEW;
        } else if ("StandardViewDefinitions::ActionFlowView".equals(viewDefinitionName)) {
            kind = ACTION_FLOW_VIEW;
        } else if ("StandardViewDefinitions::StateTransitionView".equals(viewDefinitionName)) {
            kind = STATE_TRANSITION_VIEW;
        }
        return kind;
    }

    public static boolean isGeneralView(ViewDefinitionKind kind) {
        return kind == GENERAL_VIEW;
    }

    public static boolean isInterconnectionView(ViewDefinitionKind kind) {
        return kind == INTERCONNECTION_VIEW;
    }

    public static boolean isActionFlowView(ViewDefinitionKind kind) {
        return kind == ACTION_FLOW_VIEW;
    }

    public static boolean isStateTransitionView(ViewDefinitionKind kind) {
        return kind == STATE_TRANSITION_VIEW;
    }
}
