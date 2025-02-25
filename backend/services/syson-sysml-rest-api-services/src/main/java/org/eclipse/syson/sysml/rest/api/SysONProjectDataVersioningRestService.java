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
import org.eclipse.sirius.components.core.api.IIdentityService;
import org.eclipse.sirius.web.application.editingcontext.services.api.IEditingContextApplicationService;
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

    private final IIdentityService identityService;

    private final SysONObjectRestService objectRestService;

    private final IEditingContextApplicationService editingContextApplicationService;

    public SysONProjectDataVersioningRestService(IDefaultProjectDataVersioningRestService defaultProjectDataVersioningRestService, IIdentityService identityService,
            SysONObjectRestService objectRestService, IEditingContextApplicationService editingContextApplicationService) {
        this.defaultProjectDataVersioningRestService = Objects.requireNonNull(defaultProjectDataVersioningRestService);
        this.identityService = Objects.requireNonNull(identityService);
        this.objectRestService = Objects.requireNonNull(objectRestService);
        this.editingContextApplicationService = Objects.requireNonNull(editingContextApplicationService);
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
        var projectId = this.editingContextApplicationService.getProjectId(editingContext.getId());
        var changeTypesAllowed = changeTypes == null || changeTypes.isEmpty();
        if (commitId != null && projectId.isPresent() && Objects.equals(commitId.toString(), projectId.get()) && changeTypesAllowed) {
            var elements = this.objectRestService.getElements(editingContext);
            for (var element : elements) {
                var elementId = this.identityService.getId(element);
                var changeId = UUID.nameUUIDFromBytes((commitId + elementId).getBytes());
                var dataVersion = new RestDataVersion(changeId, new RestDataIdentity(UUID.fromString(elementId)), element);
                dataVersions.add(dataVersion);
            }
        }
        return dataVersions;
    }

    @Override
    public RestDataVersion getCommitChangeById(IEditingContext editingContext, UUID commitId, UUID changeId) {
        RestDataVersion dataVersion = null;
        var projectId = this.editingContextApplicationService.getProjectId(editingContext.getId());
        if (changeId != null && projectId.isPresent() && Objects.equals(commitId.toString(), projectId.get())) {
            var elements = this.objectRestService.getElements(editingContext);
            for (var element : elements) {
                var elementId = this.identityService.getId(element);
                if (elementId != null) {
                    var computedChangeId = UUID.nameUUIDFromBytes((commitId + elementId).getBytes());
                    if (Objects.equals(changeId.toString(), computedChangeId.toString())) {
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
