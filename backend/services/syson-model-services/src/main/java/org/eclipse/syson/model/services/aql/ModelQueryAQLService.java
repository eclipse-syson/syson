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
package org.eclipse.syson.model.services.aql;

import java.util.List;

import org.eclipse.syson.sysml.Connector;
import org.eclipse.syson.sysml.ConnectorAsUsage;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.metamodel.services.MetamodelQueryElementService;

/**
 * Entry point for all model-related services doing queries in models and called by AQL expressions in representation
 * descriptions.
 *
 * @author arichard
 */
public class ModelQueryAQLService {

    private final MetamodelQueryElementService metamodelQueryElementService;

    public ModelQueryAQLService() {
        this.metamodelQueryElementService = new MetamodelQueryElementService();
    }

    /**
     * {@link MetamodelQueryElementService#isActor(Element)}.
     */
    public boolean isActor(Element element) {
        return this.metamodelQueryElementService.isActor(element);
    }

    /**
     * {@link MetamodelQueryElementService#isSubject(Element)}.
     */
    public boolean isSubject(Element element) {
        return this.metamodelQueryElementService.isSubject(element);
    }

    /**
     * {@link MetamodelQueryElementService#getConnectorSource(Connector)}.
     */
    public Feature getConnectorSource(Connector connector) {
        return this.metamodelQueryElementService.getConnectorSource(connector);
    }

    /**
     * {@link MetamodelQueryElementService#getConnectorTarget(Connector)}.
     */
    public List<Feature> getConnectorTarget(ConnectorAsUsage connector) {
        return this.metamodelQueryElementService.getConnectorTarget(connector);
    }
}
