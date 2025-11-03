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
package org.eclipse.syson.tree.explorer.view.menu.context;

import java.util.Objects;
import java.util.Optional;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.components.collaborative.api.ChangeDescription;
import org.eclipse.sirius.components.collaborative.api.IInputPostProcessor;
import org.eclipse.sirius.components.collaborative.api.IInputPreProcessor;
import org.eclipse.sirius.components.collaborative.dto.CreateRepresentationInput;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IEditingContextPersistenceService;
import org.eclipse.sirius.components.core.api.IInput;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.web.application.editingcontext.EditingContext;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.ViewDefinition;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.sysml.util.ElementUtil;
import org.eclipse.syson.util.GetIntermediateContainerCreationSwitch;
import org.eclipse.syson.util.StandardDiagramsConstants;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Sinks.Many;

/**
 * {@link IInputPreProcessor} and {@link IInputPostProcessor} allowing to create a ViewUsage element when creating a new
 * SysON SysMLv2 representation. The SysON SysMLv2 representation is then attached to this new ViewUsage.
 *
 * @author arichard
 */
@Service
public class CreateRepresentationInputProcessor implements IInputPreProcessor, IInputPostProcessor {

    private final IObjectSearchService objectSearchService;

    private final IEditingContextPersistenceService editingContextPersistenceService;

    private final ElementUtil elementUtil;

    public CreateRepresentationInputProcessor(IObjectSearchService objectSearchService, IEditingContextPersistenceService editingContextPersistenceService) {
        this.objectSearchService = Objects.requireNonNull(objectSearchService);
        this.editingContextPersistenceService = Objects.requireNonNull(editingContextPersistenceService);
        this.elementUtil = new ElementUtil();
    }

    @Override
    public IInput preProcess(IEditingContext editingContext, IInput input, Many<ChangeDescription> changeDescriptionSink) {
        if (editingContext instanceof EditingContext && input instanceof CreateRepresentationInput createRepresentationInput && this.canHandle(createRepresentationInput)) {
            var optElement = this.getObject(editingContext, createRepresentationInput);
            if (optElement.isPresent()) {
                Element containerElement = optElement.get();
                ViewUsage viewUsage = null;
                if (containerElement instanceof ViewUsage containerVU) {
                    // In case of a ViewUsage we want to follow the nominal case.
                    viewUsage = containerVU;
                } else {
                    // In other cases we want to create a new ViewUsage and associated the new Diagram to this new
                    // ViewUsage.
                    viewUsage = this.createViewUsage(input, containerElement, createRepresentationInput.representationName());
                }
                // In any case we want to create the representation, either the general view (which applies for all 4
                // standard diagrams) or the requirements table
                String representationDescriptionId = this.getRepresentationDescriptionId(createRepresentationInput.representationDescriptionId());
                return new CreateRepresentationInput(input.id(), createRepresentationInput.editingContextId(), representationDescriptionId,
                        viewUsage.getElementId(), createRepresentationInput.representationName());
            }
        }
        return input;
    }

    @Override
    public void postProcess(IEditingContext editingContext, IInput input, Many<ChangeDescription> changeDescriptionSink) {
        if (editingContext instanceof EditingContext && input instanceof CreateRepresentationInput createRepresentationInput && this.canHandle(createRepresentationInput)) {
            this.editingContextPersistenceService.persist(createRepresentationInput, editingContext);
        }
    }

    private boolean canHandle(CreateRepresentationInput createRepresentationInput) {
        boolean canHandle = false;
        var representationDescriptionId = createRepresentationInput.representationDescriptionId();
        if (Objects.equals(SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID, representationDescriptionId)) {
            canHandle = true;
        } else if (Objects.equals(SysONRepresentationDescriptionIdentifiers.INTERCONNECTION_VIEW_DIAGRAM_DESCRIPTION_ID, representationDescriptionId)) {
            canHandle = true;
        } else if (Objects.equals(SysONRepresentationDescriptionIdentifiers.ACTION_FLOW_VIEW_DIAGRAM_DESCRIPTION_ID, representationDescriptionId)) {
            canHandle = true;
        } else if (Objects.equals(SysONRepresentationDescriptionIdentifiers.STATE_TRANSITION_VIEW_DIAGRAM_DESCRIPTION_ID, representationDescriptionId)) {
            canHandle = true;
        } else if (Objects.equals(SysONRepresentationDescriptionIdentifiers.REQUIREMENTS_TABLE_VIEW_DESCRIPTION_ID, representationDescriptionId)) {
            canHandle = true;
        }
        return canHandle;
    }

    private String getRepresentationDescriptionId(String representationDescriptionId) {
        String id = "";
        if (Objects.equals(SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID, representationDescriptionId)) {
            id = SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID;
        } else if (Objects.equals(SysONRepresentationDescriptionIdentifiers.INTERCONNECTION_VIEW_DIAGRAM_DESCRIPTION_ID, representationDescriptionId)) {
            id = SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID;
        } else if (Objects.equals(SysONRepresentationDescriptionIdentifiers.ACTION_FLOW_VIEW_DIAGRAM_DESCRIPTION_ID, representationDescriptionId)) {
            id = SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID;
        } else if (Objects.equals(SysONRepresentationDescriptionIdentifiers.STATE_TRANSITION_VIEW_DIAGRAM_DESCRIPTION_ID, representationDescriptionId)) {
            id = SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID;
        } else if (Objects.equals(SysONRepresentationDescriptionIdentifiers.REQUIREMENTS_TABLE_VIEW_DESCRIPTION_ID, representationDescriptionId)) {
            id = SysONRepresentationDescriptionIdentifiers.REQUIREMENTS_TABLE_VIEW_DESCRIPTION_ID;
        }
        return id;
    }

    private Optional<Element> getObject(IEditingContext editingContext, CreateRepresentationInput input) {
        return this.objectSearchService.getObject(editingContext, input.objectId())
                .filter(Element.class::isInstance)
                .map(Element.class::cast);
    }

    private ViewUsage createViewUsage(IInput input, Element containerElement, String viewUsageName) {
        var getIntermediateContainerCreationSwitch = new GetIntermediateContainerCreationSwitch(containerElement);
        var intermediateContainerClass = getIntermediateContainerCreationSwitch.doSwitch(containerElement.eClass());
        if (intermediateContainerClass.isPresent()) {
            EObject intermediateContainerEObject = SysmlFactory.eINSTANCE.create(intermediateContainerClass.get());
            if (intermediateContainerEObject instanceof Relationship intermediateContainer) {
                var viewUsage = SysmlFactory.eINSTANCE.createViewUsage();
                viewUsage.setDeclaredName(viewUsageName);
                containerElement.getOwnedRelationship().add(intermediateContainer);
                intermediateContainer.getOwnedRelatedElement().add(viewUsage);
                this.setViewDefinition(containerElement, viewUsage, input);
                return viewUsage;
            }
        }
        return null;
    }

    private void setViewDefinition(Element containerElement, ViewUsage viewUsage, IInput input) {
        var viewDefinition = this.getViewDefinition(containerElement, (CreateRepresentationInput) input);
        if (viewDefinition.isPresent()) {
            var featureTyping = SysmlFactory.eINSTANCE.createFeatureTyping();
            viewUsage.getOwnedRelationship().add(featureTyping);
            featureTyping.setType(viewDefinition.get());
            featureTyping.setTypedFeature(viewUsage);
        }
    }

    private Optional<ViewDefinition> getViewDefinition(Element containerElement, CreateRepresentationInput input) {
        Optional<ViewDefinition> optViewDef = Optional.empty();
        var representationDescriptionId = input.representationDescriptionId();
        if (Objects.equals(SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID, representationDescriptionId)) {
            var generalViewViewDef = this.elementUtil.findByNameAndType(containerElement, StandardDiagramsConstants.GV, ViewDefinition.class);
            optViewDef = Optional.ofNullable(generalViewViewDef);
        } else if (Objects.equals(SysONRepresentationDescriptionIdentifiers.INTERCONNECTION_VIEW_DIAGRAM_DESCRIPTION_ID, representationDescriptionId)) {
            var generalViewViewDef = this.elementUtil.findByNameAndType(containerElement, StandardDiagramsConstants.IV, ViewDefinition.class);
            optViewDef = Optional.ofNullable(generalViewViewDef);
        } else if (Objects.equals(SysONRepresentationDescriptionIdentifiers.ACTION_FLOW_VIEW_DIAGRAM_DESCRIPTION_ID, representationDescriptionId)) {
            var generalViewViewDef = this.elementUtil.findByNameAndType(containerElement, StandardDiagramsConstants.AFV, ViewDefinition.class);
            optViewDef = Optional.ofNullable(generalViewViewDef);
        } else if (Objects.equals(SysONRepresentationDescriptionIdentifiers.STATE_TRANSITION_VIEW_DIAGRAM_DESCRIPTION_ID, representationDescriptionId)) {
            var generalViewViewDef = this.elementUtil.findByNameAndType(containerElement, StandardDiagramsConstants.STV, ViewDefinition.class);
            optViewDef = Optional.ofNullable(generalViewViewDef);
        } else if (Objects.equals(SysONRepresentationDescriptionIdentifiers.REQUIREMENTS_TABLE_VIEW_DESCRIPTION_ID, representationDescriptionId)) {
            optViewDef = Optional.empty();
        }
        return optViewDef;
    }
}
