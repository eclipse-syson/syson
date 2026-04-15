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

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.components.collaborative.api.IEditingContextEventProcessor;
import org.eclipse.sirius.components.collaborative.api.IEditingContextEventProcessorRegistry;
import org.eclipse.sirius.components.core.api.ErrorPayload;
import org.eclipse.sirius.components.core.api.SuccessPayload;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionInput;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.sirius.web.application.editingcontext.EditingContext;
import org.eclipse.syson.sysml.util.ElementUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.stereotype.Service;

/**
 * Caches editing context content between tests.
 * <p>
 * This class is necessary because Sirius Web unloads all the resources of an editing context when disposing it (see <a href="https://github.com/eclipse-sirius/sirius-web/issues/4137">this issue</a>
 * for more information). Since unloading clears the content of the resource, we first need to move the standard libraries resources out of the resource set to ensure they don't get cleared by the
 * process. If Sirius Web evolves to support resource unloading as an opt-in solution, then we could get rid of this class, and cache standard library resources in
 * {@link CachedStandardLibrariesLoader} as soon as they get loaded.
 * </p>
 *
 * @author gdaniel
 */
@Service
@ConditionalOnBooleanProperty("org.eclipse.syson.test.cacheStandardLibraries")
public class EditingContextCachingService implements IEditingContextCachingService {

    private final Logger logger = LoggerFactory.getLogger(EditingContextCachingService.class);

    private final IEditingContextEventProcessorRegistry editingContextEventProcessorRegistry;

    private final CachedStandardLibrariesLoader cachedStandardLibrariesLoader;

    public EditingContextCachingService(IEditingContextEventProcessorRegistry editingContextEventProcessorRegistry, CachedStandardLibrariesLoader cachedStandardLibrariesLoader) {
        this.editingContextEventProcessorRegistry = Objects.requireNonNull(editingContextEventProcessorRegistry);
        this.cachedStandardLibrariesLoader = Objects.requireNonNull(cachedStandardLibrariesLoader);
    }

    @Override
    public void cache() {
        List<String> activeEditingContextIds = this.editingContextEventProcessorRegistry.getEditingContextEventProcessors().stream()
                .map(IEditingContextEventProcessor::getEditingContextId)
                .toList();

        for (String editingContextId : activeEditingContextIds) {
            ExecuteEditingContextFunctionInput executeEditingContextFunctionInput = new ExecuteEditingContextFunctionInput(UUID.randomUUID(), editingContextId, (editingContext, input) -> {
                if (editingContext instanceof EditingContext emfEditingContext) {
                    List<Resource> standardLibraryResources = emfEditingContext.getDomain().getResourceSet().getResources().stream()
                            .filter(ElementUtil::isStandardLibraryResource)
                            .toList();
                    if (standardLibraryResources.isEmpty()) {
                        this.logger.atError()
                                .setMessage("""
                                                The editing context {} does not contain any standard libraries to cache, and won't contain any standard library the next time it is loaded.
                                                This may indicate that the editing context has been opened multiple times during the same test (potentially a previous test).
                                                Make sure you didn't manually called IEditingContextSearchService#findById in a test, and used ExecuteEditingContextFunctionRunner instead.
                                                """)
                                .addArgument(editingContext::getId)
                                .log();
                    } else {
                        this.logger.atDebug()
                                .setMessage("Cached {} standard libraries from editing context {}")
                                .addArgument(standardLibraryResources::size)
                                .addArgument(editingContext::getId)
                                .log();
                    }
                    emfEditingContext.getChangeRecorder().dispose();
                    this.cachedStandardLibrariesLoader.cache(editingContextId, standardLibraryResources);
                    emfEditingContext.getDomain().getResourceSet().getResources().removeAll(standardLibraryResources);
                    return new SuccessPayload(input.id());
                }
                return new ErrorPayload(input.id(), List.of(new Message("", MessageLevel.ERROR)));
            });

            var payload = this.editingContextEventProcessorRegistry.dispatchEvent(editingContextId, executeEditingContextFunctionInput).block();
            if (!(payload instanceof SuccessPayload)) {
                this.logger.atError()
                        .setMessage("An unexpected error occurred while caching editing context {}")
                        .addArgument(editingContextId)
                        .log();
            }
        }
    }

    @Override
    public void invalidate() {
        this.cachedStandardLibrariesLoader.invalidateCache();
    }
}
