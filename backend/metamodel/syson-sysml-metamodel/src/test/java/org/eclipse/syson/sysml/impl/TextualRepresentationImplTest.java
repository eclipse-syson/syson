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
package org.eclipse.syson.sysml.impl;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.TextualRepresentation;
import org.eclipse.syson.sysml.util.ModelBuilder;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link TextualRepresentationImpl}.
 *
 * @author Arthur Daussy
 */
public class TextualRepresentationImplTest {

    private final ModelBuilder builder = new ModelBuilder();

    @Test
    public void checkGetAnnotedElement() {
        PartDefinition partDef = this.builder.create(PartDefinition.class);
        TextualRepresentation textualRep = this.builder.createIn(TextualRepresentation.class, partDef);

        assertThat(textualRep.getAnnotatedElement())
                .hasSize(1)
                .allMatch(e -> e == partDef);
    }

}
