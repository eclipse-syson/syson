/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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
package org.eclipse.syson.sysml.rest.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IObjectService;
import org.eclipse.sirius.web.application.project.data.versioning.dto.ChangeType;
import org.eclipse.sirius.web.application.project.data.versioning.dto.RestBranch;
import org.eclipse.sirius.web.application.project.data.versioning.dto.RestCommit;
import org.eclipse.sirius.web.application.project.data.versioning.dto.RestDataIdentity;
import org.eclipse.sirius.web.application.project.data.versioning.dto.RestDataVersion;
import org.eclipse.sirius.web.application.project.data.versioning.services.api.IDefaultProjectDataVersioningRestService;
import org.eclipse.sirius.web.application.project.data.versioning.services.api.IProjectDataVersioningRestServiceDelegate;
import org.springframework.stereotype.Service;

/**
 * SysON implementation of the Sirius Web delegate service used by project-data-versioning-related REST APIs.
 *
 * @author arichard
 */
@Service
public class SysONProjectDataVersioningRestService implements IProjectDataVersioningRestServiceDelegate {

    private final IDefaultProjectDataVersioningRestService defaultProjectDataVersioningRestService;

    private final IObjectService objectService;

    private final SysONObjectRestService objectRestService;

    public SysONProjectDataVersioningRestService(IDefaultProjectDataVersioningRestService defaultProjectDataVersioningRestService, IObjectService objectService,
            SysONObjectRestService objectRestService) {
        this.defaultProjectDataVersioningRestService = Objects.requireNonNull(defaultProjectDataVersioningRestService);
        this.objectService = Objects.requireNonNull(objectService);
        this.objectRestService = Objects.requireNonNull(objectRestService);
    }

    @Override
    public boolean canHandle(IEditingContext editingContext) {
        return true;
    }

    @Override
    public List<RestCommit> getCommits(IEditingContext editingContext) {
        return this.defaultProjectDataVersioningRestService.getCommits(editingContext);
    }

    @Override
    public RestCommit createCommit(IEditingContext editingContext, Optional<UUID> branchId) {
        return null;
    }

    @Override
    public RestCommit getCommitById(IEditingContext editingContext, UUID commitId) {
        return this.defaultProjectDataVersioningRestService.getCommitById(editingContext, commitId);
    }

    @Override
    public List<RestDataVersion> getCommitChange(IEditingContext editingContext, UUID commitId, List<ChangeType> changeTypes) {
        List<RestDataVersion> dataVersions = new ArrayList<>();
        var changeTypesAllowed = changeTypes == null || changeTypes.isEmpty();
        if (commitId != null && commitId.toString().equals(editingContext.getId()) && changeTypesAllowed) {
            var elements = this.objectRestService.getElements(editingContext);
            for (var element : elements) {
                var elementId = this.objectService.getId(element);
                var changeId = UUID.nameUUIDFromBytes((commitId.toString() + elementId).getBytes());
                var dataVersion = new RestDataVersion(changeId, new RestDataIdentity(UUID.fromString(elementId)), element);
                dataVersions.add(dataVersion);
            }
        }
        return dataVersions;
    }

    @Override
    public RestDataVersion getCommitChangeById(IEditingContext editingContext, UUID commitId, UUID changeId) {
        RestDataVersion dataVersion = null;
        if (changeId != null && commitId != null && commitId.toString().equals(editingContext.getId())) {
            var elements = this.objectRestService.getElements(editingContext);
            for (var element : elements) {
                var elementId = this.objectService.getId(element);
                if (elementId != null) {
                    var computedChangeId = UUID.nameUUIDFromBytes((commitId.toString() + elementId).getBytes());
                    if (changeId.toString().equals(computedChangeId.toString())) {
                        dataVersion = new RestDataVersion(changeId, new RestDataIdentity(UUID.fromString(elementId)), element);
                        break;
                    }
                }
            }
        }
        return dataVersion;
    }

    @Override
    public List<RestBranch> getBranches(IEditingContext editingContext) {
        return this.defaultProjectDataVersioningRestService.getBranches(editingContext);
    }

    @Override
    public RestBranch createBranch(IEditingContext editingContext, String branchName, UUID commitId) {
        return this.defaultProjectDataVersioningRestService.createBranch(editingContext, branchName, commitId);
    }

    @Override
    public RestBranch getBranchById(IEditingContext editingContext, UUID branchId) {
        return this.defaultProjectDataVersioningRestService.getBranchById(editingContext, branchId);
    }

    @Override
    public RestBranch deleteBranch(IEditingContext editingContext, UUID branchId) {
        return this.defaultProjectDataVersioningRestService.getBranchById(editingContext, branchId);
    }
}
