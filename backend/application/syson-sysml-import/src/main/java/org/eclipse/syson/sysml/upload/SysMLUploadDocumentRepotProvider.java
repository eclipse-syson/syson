/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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
package org.eclipse.syson.sysml.upload;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.web.services.documents.api.IUploadDocumentReportProvider;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.LogBook;
import org.springframework.stereotype.Service;

/**
 * Specific {@link IUploadDocumentReportProvider} allowing to get report when uploading SysML textual resources.
 *
 * @author arichard
 */
@Service
public class SysMLUploadDocumentRepotProvider implements IUploadDocumentReportProvider {

    @Override
    public boolean canHandle(Resource resource) {
        return resource != null && resource.getContents().stream().anyMatch(Element.class::isInstance);
    }

    @Override
    public String createReport(Resource resource) {
        return LogBook.getReport(null);
    }

}
