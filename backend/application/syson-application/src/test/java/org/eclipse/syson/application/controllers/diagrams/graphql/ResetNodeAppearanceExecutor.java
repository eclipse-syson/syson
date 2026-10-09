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
package org.eclipse.syson.application.controllers.diagrams.graphql;

import java.util.Objects;

import org.eclipse.sirius.components.collaborative.diagrams.dto.ResetNodeAppearanceInput;
import org.eclipse.sirius.components.diagrams.tests.graphql.ResetNodeAppearanceMutationRunner;
import org.springframework.stereotype.Service;
import org.springframework.test.context.transaction.TestTransaction;

/**
 * Executes the existing node appearance reset mutation and exposes its result assertions.
 *
 * @author gcoutable
 */
@Service
public class ResetNodeAppearanceExecutor {

    private final ResetNodeAppearanceMutationRunner resetNodeAppearanceMutationRunner;

    public ResetNodeAppearanceExecutor(ResetNodeAppearanceMutationRunner resetNodeAppearanceMutationRunner) {
        this.resetNodeAppearanceMutationRunner = Objects.requireNonNull(resetNodeAppearanceMutationRunner);
    }

    public ResetNodeAppearanceAssert execute(ResetNodeAppearanceInput input) {
        var result = this.resetNodeAppearanceMutationRunner.run(input);
        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();
        return new ResetNodeAppearanceAssert(result.data());
    }
}
