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
package org.eclipse.syson.sysml.validation;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IValidationService;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.syson.services.api.ISysONResourceService;
import org.eclipse.syson.sysml.util.ElementUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * {@link IValidationService} implementation that relies on the {@link EValidator} implementations registered into the
 * injected {@link EValidator.Registry}.
 *
 * @author arichard
 */
@Service
public class SysONSysMLValidationService implements IValidationService {

    private final Logger logger = LoggerFactory.getLogger(SysONSysMLValidationService.class);

    private final EValidator.Registry eValidatorRegistry;

    private final ISysONResourceService sysONResourceService;

    public SysONSysMLValidationService(EValidator.Registry eValidatorRegistry, final ISysONResourceService sysONResourceService) {
        this.eValidatorRegistry = Objects.requireNonNull(eValidatorRegistry);
        this.sysONResourceService = Objects.requireNonNull(sysONResourceService);
    }

    @Override
    public List<Object> validate(Object object, Object feature) {
        this.logger.debug("Called SysONSysMLValidationService.validate(%s, %s)".formatted(object.toString(), feature.toString()));
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

    protected List<Object> validate(IEMFEditingContext emfEditingContext) {
        final Instant start = Instant.now();

        final List<Object> validationResults = this.doValidation(emfEditingContext);

        final Instant finish = Instant.now();
        final long duration = Duration.between(start, finish).toMillis();
        if (duration > 200) {
            this.logger.debug("Validating EditingContext '%s' took %dms".formatted(emfEditingContext.getId(), duration));
        }

        return validationResults;
    }

    protected List<Object> doValidation(IEMFEditingContext emfEditingContext) {
        final List<EObject> objectsToValidate = this.getElementsToRunValidationOn(emfEditingContext);
        final Diagnostician diagnostician = this.createDiagnostician();
        final Map<Object, Object> diagnosticianOptions = this.getDiagnosticianOptions();

        final List<Diagnostic> diagnostics = objectsToValidate.stream()
                .map(eObject -> diagnostician.validate(eObject, diagnosticianOptions))
                .toList();

        final List<Object> validationResults = this.getValidationResultsFrom(diagnostics);
        return validationResults;
    }

    /**
     * Provides the elements to run the validation on. Note how this only returns the root elements of the resources
     * because {@link #getDiagnosticianOptions()} makes the validation run recursively.
     *
     * @param emfEditingContext
     *            the (non-{@code null}) {@link IEMFEditingContext} being validated.
     * @return a (non-{@code null}) {@link List} of {@link EObject} from {@code emfEditingContext}.
     */
    protected List<EObject> getElementsToRunValidationOn(final IEMFEditingContext emfEditingContext) {
        final List<Resource> resourcesToValidate = emfEditingContext.getDomain().getResourceSet().getResources().stream()
                .filter(resource ->
                // Do not validate the standard libraries.
                !ElementUtil.isStandardLibraryResource(resource)
                        // Do not validate the referenced libraries: it is the responsibility of the library project
                        && !this.sysONResourceService.isFromReferencedLibrary(emfEditingContext, resource))
                .toList();
        return resourcesToValidate.stream()
                .map(Resource::getContents)
                .flatMap(Collection::stream)
                .toList();
    }

    /**
     * Creates the {@link Diagnostician} used to run the validation.
     *
     * @return a (non-{@code null}) {@link Diagnostician}.
     */
    protected Diagnostician createDiagnostician() {
        return new Diagnostician(this.eValidatorRegistry);
    }

    /**
     * Creates the {@link Map} of options passed to the {@link Diagnostician#validate(EObject, Map)} upon validation.
     *
     * @return a (non-{@code null}) {@link Map}.
     */
    protected Map<Object, Object> getDiagnosticianOptions() {
        return Map.ofEntries(
                Map.entry(Diagnostician.VALIDATE_RECURSIVELY, true));
    }

    /**
     * Transforms the results from the {@link Diagnostician} into arbitrary objects understood by the consumers of
     * {@link IValidationService}.
     *
     * @param diagnostics
     *            the (non-{@code null}) {@link List} of {@link Diagnostic} to transform.
     * @return a (non-{@code null}) {@link List} of {@link Object}.
     */
    protected List<Object> getValidationResultsFrom(final List<Diagnostic> diagnostics) {
        return diagnostics.stream()
                .map(Diagnostic::getChildren)
                .flatMap(Collection::stream)
                // We sort the validation results based on the containing document and the qualified name of the
                // validated element.
                .sorted(new SysONDiagnosticComparator())
                .map(Object.class::cast)
                .toList();
    }
}
