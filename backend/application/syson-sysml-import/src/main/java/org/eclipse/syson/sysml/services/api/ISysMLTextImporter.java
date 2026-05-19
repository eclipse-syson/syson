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
package org.eclipse.syson.sysml.services.api;

import java.util.List;

import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.syson.sysml.Element;

/**
 * Use to import a SySMLv2 text fragment as new objects inside an existing element.
 *
 * @author pcdavid
 */
public interface ISysMLTextImporter {
    /**
     * Import a SysMLv2 text fragment inside an existing element.
     *
     * @param emfEditingContext
     *            the editing context.
     * @param parentElement
     *            the parent element in which the elements in the imported text will be added.
     * @param textualContent
     *            the SysMLv2 text fragment to import. It may contain multiple elements.
     * @param messages
     *            a list where the import process may add diagnostic messages to report any issues during the import
     *            process.
     * @return the list of elements created from the parsed text fragment and added inside {@code parentElement}.
     */
    List<Element> importSysMLText(IEMFEditingContext emfEditingContext, Element parentElement, String textualContent, List<Message> messages);
}
