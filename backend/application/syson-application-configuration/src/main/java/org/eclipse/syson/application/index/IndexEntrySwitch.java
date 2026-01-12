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
package org.eclipse.syson.application.index;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IIdentityService;
import org.eclipse.sirius.components.core.api.ILabelService;
import org.eclipse.sirius.web.application.index.services.api.IIndexEntry;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.util.SysmlSwitch;

/**
 * Provides {@link IIndexEntry} for SysML elements.
 *
 * <p>
 * {@link IIndexEntry} are top-level POJOs representing SysML elements that are stored in Elasticsearch indices. They can contain {@link INestedIndexEntry} to represent elements connected to the
 * element they represent.
 * </p>
 * <p>
 * SysON serializes SysML elements into entries that can contain nested entries with a maximum depth of:
 * <ul>
 * <li>1 for nested elements: for example, a package containing a part will define fields such as {@code name} (the name of the package), but also {@code ownedElement.name} (the name of the part)</li>
 * <li>2 for nested specializations: for example, a part typed by a part definition will define fields such as {@code name} (the name of the part}, but also {@code ownedSpecialization.general.name}
 * (the name of the part definition typing the part)</li>
 * </ul>
 * </p>
 *
 * @author gdaniel
 */
public class IndexEntrySwitch extends SysmlSwitch<Optional<IIndexEntry>> {

    private final IIdentityService identityService;

    private final ILabelService labelService;

    private final IEditingContext editingContext;

    private final NestedIndexEntrySwitch nestedIndexEntrySwitch;

    public IndexEntrySwitch(IIdentityService identityService, ILabelService labelService, IEditingContext editingContext) {
        this.identityService = Objects.requireNonNull(identityService);
        this.labelService = Objects.requireNonNull(labelService);
        this.editingContext = Objects.requireNonNull(editingContext);
        this.nestedIndexEntrySwitch = new NestedIndexEntrySwitch(identityService, labelService);
    }

    @Override
    public Optional<IIndexEntry> doSwitch(EObject eObject) {
        Optional<IIndexEntry> result = Optional.empty();
        if (eObject != null) {
            result = super.doSwitch(eObject);
        }
        return result;
    }

    @Override
    public Optional<IIndexEntry> defaultCase(EObject object) {
        return Optional.empty();
    }

    @Override
    public Optional<IIndexEntry> caseNamespace(Namespace namespace) {
        String objectId = this.identityService.getId(namespace);
        String type = this.getEClassifierName(namespace.eClass());
        String label = this.labelService.getStyledLabel(namespace).toString();
        List<String> iconURLs = this.labelService.getImagePaths(namespace);

        String name = namespace.getName();
        String shortName = namespace.getShortName();
        String qualifiedName = namespace.getQualifiedName();

        Optional<INestedIndexEntry> ownerNestedIndexEntry = this.nestedIndexEntrySwitch.doSwitch(namespace.getOwner());
        List<INestedIndexEntry> ownedElementNestedIndexEntries = namespace.getOwnedElement().stream()
                .map(this.nestedIndexEntrySwitch::doSwitch)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        return Optional.of(new NamespaceIndexEntry(this.editingContext.getId(), objectId, type, label, iconURLs, name, shortName, qualifiedName, ownerNestedIndexEntry.orElse(null),
                ownedElementNestedIndexEntries));
    }

    @Override
    public Optional<IIndexEntry> caseType(Type type) {
        String objectId = this.identityService.getId(type);
        String objectType = this.getEClassifierName(type.eClass());
        String label = this.labelService.getStyledLabel(type).toString();
        List<String> iconURLs = this.labelService.getImagePaths(type);

        String name = type.getName();
        String shortName = type.getShortName();
        String qualifiedName = type.getQualifiedName();

        Optional<INestedIndexEntry> ownerNestedIndexEntry = this.nestedIndexEntrySwitch.doSwitch(type.getOwner());
        List<INestedIndexEntry> ownedElementNestedIndexEntries = type.getOwnedElement().stream()
                .map(this.nestedIndexEntrySwitch::doSwitch)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        List<NestedSpecializationIndexEntry> ownedSpecializationNestedIndexEntries = type.getOwnedSpecialization().stream()
                .map(this.nestedIndexEntrySwitch::doSwitch)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(NestedSpecializationIndexEntry.class::isInstance)
                .map(NestedSpecializationIndexEntry.class::cast)
                .toList();

        return Optional.of(new TypeIndexEntry(this.editingContext.getId(), objectId, objectType, label, iconURLs, name, shortName, qualifiedName, ownerNestedIndexEntry.orElse(null),
                ownedSpecializationNestedIndexEntries, ownedElementNestedIndexEntries));
    }

    private String getEClassifierName(EClassifier eClassifier) {
        String name = eClassifier.getName();
        if (eClassifier instanceof EClass eClass) {
            if (SysmlPackage.eINSTANCE.getUsage().isSuperTypeOf(eClass)
                    && !SysmlPackage.eINSTANCE.getConnectorAsUsage().equals(eClass)
                    && !SysmlPackage.eINSTANCE.getBindingConnectorAsUsage().equals(eClass)
                    && !SysmlPackage.eINSTANCE.getSuccessionAsUsage().equals(eClass)) {
                if (eClass.getName().endsWith("Usage")) {
                    name = eClass.getName().substring(0, eClass.getName().length() - 5);
                }
            }
        }
        return name;
    }
}
