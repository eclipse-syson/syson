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
package org.eclipse.syson.tree.services;

import java.util.List;
import java.util.Objects;

import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.syson.services.ContextualFeatureChain;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.metamodel.services.MetamodelMutationElementService;
import org.eclipse.syson.sysml.metamodel.services.redefine.FeatureChainRedefinitionResult;
import org.springframework.stereotype.Service;

/**
 * Selection dialog services doing tree mutations.
 *
 * @author Jerome Gout
 */
@Service
public class TreeMutationSelectionDialogService {

    private final IFeedbackMessageService feedbackMessageService;

    /**
     * Creates a selection dialog mutation service.
     *
     * @param feedbackMessageService
     *            the service used to report feature chain redefinition errors
     */
    public TreeMutationSelectionDialogService(IFeedbackMessageService feedbackMessageService) {
        this.feedbackMessageService = Objects.requireNonNull(feedbackMessageService);
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
        List<Feature> features = null;
        if (chain != null) {
            features = chain.features();
        }
        FeatureChainRedefinitionResult result = new MetamodelMutationElementService().redefineFeatureChain(context, features);
        result.messages().forEach(message -> this.feedbackMessageService.addFeedbackMessage(new Message(message, MessageLevel.ERROR)));
        List<Feature> localChain = null;
        if (result.isSuccess()) {
            localChain = result.chain();
        }
        return localChain;
    }
}
