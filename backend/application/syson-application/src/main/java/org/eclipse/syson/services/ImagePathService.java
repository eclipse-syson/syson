/*******************************************************************************
 * Copyright (c) 2023 Obeo.
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
package org.eclipse.syson.services;

import java.util.List;

import org.eclipse.sirius.components.core.api.IImagePathService;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link IImagePathService} for the Flow domain.
 *
 * @author lfasani
 */
@Service
public class ImagePathService implements IImagePathService {

    private static final List<String> IMAGES_PATHS = List.of("/img", "/images", "/icons", "/icons2");

    @Override
    public List<String> getPaths() {
        return IMAGES_PATHS;
    }

}
