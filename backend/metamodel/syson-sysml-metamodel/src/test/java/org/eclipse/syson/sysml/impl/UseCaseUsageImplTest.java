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
package org.eclipse.syson.sysml.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.syson.sysml.IncludeUseCaseUsage;
import org.eclipse.syson.sysml.UseCaseUsage;
import org.eclipse.syson.sysml.util.ModelBuilder;
import org.junit.Test;

/**
 * {@link UseCaseUsageImpl} related tests.
 *
 * @author gcoutable
 */
public class UseCaseUsageImplTest {

    private final ModelBuilder builder = new ModelBuilder();

    @Test
    public void testIncludedUseCase() {
        var useCaseUsage = this.builder.create(UseCaseUsage.class);
        var includedUseCase = useCaseUsage.getIncludedUseCase();
        assertEquals(0, includedUseCase.size());

        var anotherUseCaseUsage = this.builder.create(UseCaseUsage.class);
        var includeUseCaseUsage = this.builder.createIn(IncludeUseCaseUsage.class, useCaseUsage);
        this.builder.addReferenceSubsetting(includeUseCaseUsage, anotherUseCaseUsage);

        includedUseCase = useCaseUsage.getIncludedUseCase();
        assertEquals(1, includedUseCase.size());
        assertEquals(anotherUseCaseUsage, includedUseCase.get(0));
    }
}
