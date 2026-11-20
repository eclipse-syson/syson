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
package org.eclipse.syson.tree.services.aql;

import java.util.List;
import java.util.Objects;

import org.eclipse.syson.services.ContextualFeatureChain;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.tree.services.TreeMutationSelectionDialogService;

/**
 * Entry point for all tree-related services doing mutations in trees and called by AQL expressions in tree
 * descriptions.
 *
 * @author arichard
 */
public class TreeMutationAQLService {

    private final TreeMutationSelectionDialogService treeMutationSelectionDialogService;

    /**
     * Creates a tree mutation AQL service.
     *
     * @param treeMutationSelectionDialogService
     *            the selection dialog mutation service
     */
    public TreeMutationAQLService(TreeMutationSelectionDialogService treeMutationSelectionDialogService) {
        this.treeMutationSelectionDialogService = Objects.requireNonNull(treeMutationSelectionDialogService);
    }

    /**
     * Redefines the selected chain and reports failures without changing the model.
     *
     * @param context
     *            the type on which the tool was invoked
     * @param chain
     *            the feature chain selected in the dialog
     * @return the local redefined feature chain, or null on failure
     */
    public List<Feature> redefineFeatureChain(Type context, ContextualFeatureChain chain) {
        return this.treeMutationSelectionDialogService.redefineFeatureChain(context, chain);
    }
}
