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
package org.eclipse.syson.services;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.sirius.components.core.api.IDefaultIdentityService;
import org.eclipse.sirius.components.core.api.IDefaultLabelService;
import org.eclipse.sirius.components.core.api.IDefaultObjectSearchService;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IIdentityServiceDelegate;
import org.eclipse.sirius.components.core.api.ILabelServiceDelegate;
import org.eclipse.sirius.components.core.api.IObjectSearchServiceDelegate;
import org.eclipse.sirius.components.core.api.labels.StyledString;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.Type;
import org.springframework.stereotype.Service;

/**
 * Identifies, labels and resolves virtual feature chains without storing them in the model.
 * Identifiers encode the context identifier first, followed by the feature identifiers in traversal order.
 *
 * @author Jerome Gout
 */
@Service
public class ContextualFeatureChainObjectService implements IIdentityServiceDelegate, ILabelServiceDelegate, IObjectSearchServiceDelegate {

    private static final String ID_PREFIX = "contextual-feature-chain:";

    private final IDefaultIdentityService identityService;

    private final IDefaultObjectSearchService objectSearchService;

    private final IDefaultLabelService labelService;

    public ContextualFeatureChainObjectService(IDefaultIdentityService identityService, IDefaultObjectSearchService objectSearchService, IDefaultLabelService labelService) {
        this.identityService = Objects.requireNonNull(identityService);
        this.objectSearchService = Objects.requireNonNull(objectSearchService);
        this.labelService = Objects.requireNonNull(labelService);
    }

    @Override
    public boolean canHandle(Object object) {
        return object instanceof ContextualFeatureChain;
    }

    @Override
    public boolean canHandle(IEditingContext editingContext, String objectId) {
        return objectId != null && objectId.startsWith(ID_PREFIX);
    }

    @Override
    public String getId(Object object) {
        String id = null;
        if (object instanceof ContextualFeatureChain chain) {
            // URL-safe Base64 uses only letters, digits, '-' and '_' without padding, so '.' safely separates segments.
            id = ID_PREFIX + Stream.concat(Stream.of(chain.context()), chain.features().stream())
                    .map(this.identityService::getId)
                    .map(elementId -> Base64.getUrlEncoder().withoutPadding().encodeToString(elementId.getBytes(StandardCharsets.UTF_8)))
                    .collect(Collectors.joining("."));
        }
        return id;
    }

    @Override
    public String getKind(Object object) {
        return ContextualFeatureChain.class.getSimpleName();
    }

    @Override
    public StyledString getStyledLabel(Object object) {
        String label = "";
        if (object instanceof ContextualFeatureChain chain) {
            label = chain.features().stream()
                    .map(Feature::escapedName)
                    .map(name -> {
                        if (name.isEmpty()) {
                            return "<unnamed>";
                        }
                        return name;
                    })
                    .collect(Collectors.joining("."));
        }
        return StyledString.of(label);
    }

    @Override
    public List<String> getImagePaths(Object object) {
        if (object instanceof ContextualFeatureChain chain) {
            return this.labelService.getImagePaths(chain.features().getLast());
        }
        return List.of();
    }

    @Override
    public Optional<Object> getObject(IEditingContext editingContext, String objectId) {
        Optional<Object> result = Optional.empty();
        if (this.canHandle(editingContext, objectId)) {
            try {
                var segments = objectId.substring(ID_PREFIX.length()).split("\\.", -1);
                var context = this.resolveSegment(editingContext, segments[0]).filter(Type.class::isInstance).map(Type.class::cast);
                if (context.isPresent() && segments.length > 1) {
                    var features = new ArrayList<Feature>();
                    Type accessingContext = context.get();
                    for (int index = 1; index < segments.length; index++) {
                        var object = this.resolveSegment(editingContext, segments[index]).orElse(null);
                        if (object instanceof Feature feature && accessingContext.getFeatureMembership().stream()
                                .anyMatch(membership -> membership.getOwnedMemberFeature() == feature)) {
                            features.add(feature);
                            accessingContext = feature;
                        } else {
                            break;
                        }
                    }
                    if (features.size() == segments.length - 1) {
                        // Every feature still belongs to the effective memberships of its preceding context.
                        result = Optional.of(new ContextualFeatureChain(context.get(), features));
                    }
                }
            } catch (IllegalArgumentException exception) {
                // A malformed identifier cannot be decoded.
            }
        }
        return result;
    }

    private Optional<Object> resolveSegment(IEditingContext editingContext, String segment) {
        String elementId = new String(Base64.getUrlDecoder().decode(segment), StandardCharsets.UTF_8);
        return this.objectSearchService.getObject(editingContext, elementId);
    }
}
