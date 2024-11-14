/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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
package org.eclipse.syson.sysml.dto;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.components.collaborative.api.ChangeDescription;
import org.eclipse.sirius.components.collaborative.api.ChangeKind;
import org.eclipse.sirius.components.collaborative.api.IEditingContextEventHandler;
import org.eclipse.sirius.components.collaborative.api.Monitoring;
import org.eclipse.sirius.components.collaborative.messages.ICollaborativeMessageService;
import org.eclipse.sirius.components.core.api.ErrorPayload;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.sirius.components.core.api.IInput;
import org.eclipse.sirius.components.core.api.IObjectService;
import org.eclipse.sirius.components.core.api.IPayload;
import org.eclipse.sirius.components.core.api.SuccessPayload;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.syson.sysml.ASTTransformer;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.SysmlToAst;
import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import reactor.core.publisher.Sinks.Many;
import reactor.core.publisher.Sinks.One;

/**
 * Event handler for InsertTextualSysMLv2 mutation.
 *
 * @author arichard
 */
@Service
public class InsertTextualSysMLv2EventHandler implements IEditingContextEventHandler {

    private final IObjectService objectService;

    private final ICollaborativeMessageService messageService;

    private final IFeedbackMessageService feedbackMessageService;

    private final SysmlToAst sysmlToAst;

    private final Counter counter;

    public InsertTextualSysMLv2EventHandler(IObjectService objectService, ICollaborativeMessageService messageService, IFeedbackMessageService feedbackMessageService,
            SysmlToAst sysmlToAst, MeterRegistry meterRegistry) {
        this.objectService = Objects.requireNonNull(objectService);
        this.messageService = Objects.requireNonNull(messageService);
        this.feedbackMessageService = Objects.requireNonNull(feedbackMessageService);
        this.sysmlToAst = Objects.requireNonNull(sysmlToAst);
        this.counter = Counter.builder(Monitoring.EVENT_HANDLER)
                .tag(Monitoring.NAME, this.getClass().getSimpleName())
                .register(meterRegistry);
    }

    @Override
    public boolean canHandle(IEditingContext editingContext, IInput input) {
        return input instanceof InsertTextualSysMLv2Input;
    }

    @Override
    public void handle(One<IPayload> payloadSink, Many<ChangeDescription> changeDescriptionSink, IEditingContext editingContext, IInput input) {
        this.counter.increment();

        List<Message> messages = List.of(new Message(this.messageService.invalidInput(input.getClass().getSimpleName(), InsertTextualSysMLv2Input.class.getSimpleName()),
                MessageLevel.ERROR));
        ChangeDescription changeDescription = new ChangeDescription(ChangeKind.NOTHING, editingContext.getId(), input);

        IPayload payload = null;

        if (input instanceof InsertTextualSysMLv2Input insertTextualInput && editingContext instanceof IEMFEditingContext emfEditingContext) {
            var parentObjectId = insertTextualInput.objectId();
            var parentElement = this.getParentElement(parentObjectId, emfEditingContext);
            if (parentElement != null) {
                var tranformer = new ASTTransformer();
                var resource = this.convert(insertTextualInput, emfEditingContext, tranformer);
                if (resource != null && !resource.getContents().isEmpty()) {
                    // Workaround for https://github.com/eclipse-syson/syson/issues/860
                    tranformer.logTransformationMessages();
                    var rootElements = this.extractContent(resource);
                    rootElements.forEach(element -> {
                        var membership = this.createMembership(parentElement);
                        membership.getOwnedRelatedElement().add(element);
                    });
                    if (!rootElements.isEmpty()) {
                        payload = new SuccessPayload(input.id(), this.feedbackMessageService.getFeedbackMessages());
                        changeDescription = new ChangeDescription(ChangeKind.SEMANTIC_CHANGE, editingContext.getId(), input);
                    } else {
                        messages = List.of(new Message("Unable to convert the input into valid SysMLv2", MessageLevel.ERROR));
                    }
                    // We don't want the new resource to stay in the resource set and create a new document
                    var resourceSet = emfEditingContext.getDomain().getResourceSet();
                    resource.getContents().clear();
                    resourceSet.getResources().remove(resource);
                } else {
                    messages = List.of(new Message("Unable to convert the input into valid SysMLv2", MessageLevel.ERROR));
                }
            }
        }

        if (payload == null) {
            payload = new ErrorPayload(input.id(), messages);
        }

        payloadSink.tryEmitValue(payload);
        changeDescriptionSink.tryEmitNext(changeDescription);
    }

    private Element getParentElement(String parentObjectId, IEMFEditingContext emfEditingContext) {
        var parentObject = this.objectService.getObject(emfEditingContext, parentObjectId);
        if (parentObject.isPresent()) {
            var object = parentObject.get();
            if (object instanceof Element parentElement) {
                return parentElement;
            }
        }
        return null;
    }

    private Resource convert(InsertTextualSysMLv2Input insertTextualInput, IEMFEditingContext emfEditingContext, ASTTransformer tranformer) {
        var textualContent = insertTextualInput.textualContent();
        var resourceSet = emfEditingContext.getDomain().getResourceSet();
        var inputStream = new ByteArrayInputStream(textualContent.getBytes());
        var astStream = this.sysmlToAst.convert(inputStream, ".sysml");
        var resource = tranformer.convertResource(astStream, resourceSet);
        return resource;
    }

    private Membership createMembership(Element element) {
        Membership membership = null;
        if (element instanceof Package || SysmlPackage.eINSTANCE.getNamespace().equals(element.eClass())) {
            membership = SysmlFactory.eINSTANCE.createOwningMembership();
        } else {
            membership = SysmlFactory.eINSTANCE.createFeatureMembership();
        }
        element.getOwnedRelationship().add(membership);
        return membership;
    }

    private List<Element> extractContent(Resource resource) {
        var roots = resource.getContents().stream()
                .filter(Namespace.class::isInstance)
                .map(Namespace.class::cast)
                .flatMap(ns -> ns.getOwnedRelationship().stream())
                .flatMap(r -> this.getChildren(r).stream())
                .toList();
        return roots;
    }

    private List<Element> getChildren(Relationship relationship) {
        List<Element> children = new ArrayList<>();
        if (relationship instanceof OwningMembership) {
            children.addAll(relationship.getOwnedRelatedElement());
        } else {
            children.add(relationship);
        }
        return children;
    }
}
