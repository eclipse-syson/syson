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

import org.eclipse.syson.sysml.ConnectionDefinition;
import org.eclipse.syson.sysml.Connector;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.util.ModelBuilder;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link ConnectorImpl}.
 *
 * @author Arthur Daussy
 */
public class ConnectorImplTest {

    private final ModelBuilder builder = new ModelBuilder();

    @Test
    public void checkGetAssociationMethod() {
        Connector connector = SysmlFactory.eINSTANCE.createConnector();
        assertThat(connector.getAssociation()).isEmpty();

        ConnectionDefinition connectionDefinition = SysmlFactory.eINSTANCE.createConnectionDefinition();

        this.builder.setType(connector, connectionDefinition);
        assertThat(connector.getAssociation()).hasSize(1).allMatch(a -> a == connectionDefinition);
    }

}
