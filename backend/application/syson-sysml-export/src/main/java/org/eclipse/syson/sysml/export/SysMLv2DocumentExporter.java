/*******************************************************************************
 * Copyright (c) 2024, 2026 Obeo.
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
package org.eclipse.syson.sysml.export;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.sirius.web.application.document.services.api.IDocumentExporter;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.impl.MembershipCacheAdapter;
import org.eclipse.syson.sysml.textual.SysMLElementSerializer;
import org.eclipse.syson.sysml.textual.utils.FileNameDeresolver;
import org.eclipse.syson.sysml.textual.utils.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

/**
 * Used to export SysMlv2 Resource.
 *
 * @author gcoutable
 */
@Service
public class SysMLv2DocumentExporter implements IDocumentExporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SysMLv2DocumentExporter.class);

    @Override
    public boolean canHandle(Resource resource, String mediaType) {
        boolean canHandle = false;
        if (MediaType.TEXT_HTML.equals(MediaType.valueOf(mediaType))) {
            canHandle = !resource.getContents().isEmpty() && resource.getContents().get(0) instanceof Element;
        }
        return canHandle;
    }

    @Override
    public Optional<byte[]> getBytes(Resource resource, String mediaType) {
        if (!resource.getContents().isEmpty() && resource.getContents().get(0) instanceof Element element) {
            MembershipCacheAdapter membershipCacheAdapter = new MembershipCacheAdapter();

            this.installCacheAdapter(resource, membershipCacheAdapter);
            try {
                List<Status> status = new ArrayList<>();
                String textualForm = new SysMLElementSerializer(System.lineSeparator(), "\t", new FileNameDeresolver(), status::add).doSwitch(element);
                if (textualForm == null) {
                    textualForm = "";
                }
                for (Status s : status) {
                    s.log(LOGGER);
                }
                return Optional.of(textualForm.getBytes());
            } finally {
                this.removeCacheAdapter(resource, membershipCacheAdapter);
            }
        }
        return Optional.empty();
    }

    private void removeCacheAdapter(Resource resource, MembershipCacheAdapter membershipCacheAdapter) {
        ResourceSet resourceSet = resource.getResourceSet();
        if (resourceSet != null) {
            resourceSet.eAdapters().remove(membershipCacheAdapter);
        } else {
            resource.eAdapters().remove(membershipCacheAdapter);
        }
    }

    private ResourceSet installCacheAdapter(Resource resource, MembershipCacheAdapter membershipCacheAdapter) {
        ResourceSet resourceSet = resource.getResourceSet();
        if (resourceSet != null) {
            resourceSet.eAdapters().add(membershipCacheAdapter);
        } else {
            resource.eAdapters().add(membershipCacheAdapter);
        }
        return resourceSet;
    }
}
