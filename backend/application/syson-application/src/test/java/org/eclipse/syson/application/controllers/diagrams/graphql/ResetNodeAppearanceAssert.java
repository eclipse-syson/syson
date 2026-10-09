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

import static org.assertj.core.api.Assertions.assertThat;

import com.jayway.jsonpath.JsonPath;

import java.util.Objects;

import org.assertj.core.api.AbstractAssert;
import org.eclipse.sirius.components.core.api.SuccessPayload;

/**
 * Assertions on the result of resetting a node appearance.
 *
 * @author gcoutable
 */
public class ResetNodeAppearanceAssert extends AbstractAssert<ResetNodeAppearanceAssert, String> {

    public ResetNodeAppearanceAssert(String result) {
        super(Objects.requireNonNull(result), ResetNodeAppearanceAssert.class);
    }

    public ResetNodeAppearanceAssert isSuccess() {
        String typename = JsonPath.read(this.actual, "$.data.resetNodeAppearance.__typename");
        assertThat(typename).isEqualTo(SuccessPayload.class.getSimpleName());
        return this;
    }
}
