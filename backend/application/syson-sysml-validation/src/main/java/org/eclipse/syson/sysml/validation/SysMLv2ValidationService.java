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
package org.eclipse.syson.sysml.validation;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IValidationService;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.web.application.library.services.LibraryMetadataAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Used to validate SysMLv2 elements in SysON.
 *
 * @author arichard
 */
@Service
public class SysMLv2ValidationService implements IValidationService {

    private final Logger logger = LoggerFactory.getLogger(SysMLv2ValidationService.class);

    private final EValidator.Registry eValidatorRegistry;

    public SysMLv2ValidationService(EValidator.Registry eValidatorRegistry) {
        this.eValidatorRegistry = Objects.requireNonNull(eValidatorRegistry);
    }

    @Override
    public List<Object> validate(Object object, Object feature) {
        return List.of();
    }

    @Override
    public List<Object> validate(IEditingContext editingContext) {
        return Optional.of(editingContext)
                .filter(IEMFEditingContext.class::isInstance)
                .map(IEMFEditingContext.class::cast)
                .map(this::validate)
                .orElseGet(List::of);
    }

    private List<Object> validate(IEMFEditingContext editingContext) {
        long start = System.currentTimeMillis();

        var domain = editingContext.getDomain();

        var options = new HashMap<>();
        options.put(Diagnostician.VALIDATE_RECURSIVELY, true);
        var diagnostician = this.getNewDiagnostician();

        var objects = domain.getResourceSet().getResources().stream()
                .filter(r -> r.getURI() != null
                        && r.getURI().toString().startsWith(IEMFEditingContext.RESOURCE_SCHEME)
                        // Do not validate referenced libraries: it is the responsibility of the library project
                        && r.eAdapters().stream().noneMatch(LibraryMetadataAdapter.class::isInstance))
                .map(Resource::getContents)
                .flatMap(Collection::stream)
                .map(eObject -> diagnostician.validate(eObject, options))
                .map(Diagnostic::getChildren)
                .flatMap(Collection::stream)
                .map(Object.class::cast)
                .toList();

        long end = System.currentTimeMillis();
        if (end - start > 200) {
            this.logger.atDebug()
                    .setMessage("EditingContext {}: {}ms to compute all validation rules.")
                    .addArgument(editingContext.getId())
                    .addArgument(() -> String.format("%1$6s", end - start))
                    .log();
        }
        return objects;
    }


    private Diagnostician getNewDiagnostician() {
        return new Diagnostician(this.eValidatorRegistry);
    }
}
