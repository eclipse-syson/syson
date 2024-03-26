/*******************************************************************************
 * Copyright (c) 2023, 2024 Obeo.
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
package org.eclipse.syson.application.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.sirius.components.collaborative.forms.services.api.IPropertiesDescriptionRegistry;
import org.eclipse.sirius.components.collaborative.forms.services.api.IPropertiesDescriptionRegistryConfigurer;
import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.sirius.components.core.api.ILabelService;
import org.eclipse.sirius.components.emf.services.IDAdapter;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.components.interpreter.AQLInterpreter;
import org.eclipse.sirius.components.representations.IRepresentationDescription;
import org.eclipse.sirius.components.view.ChangeContext;
import org.eclipse.sirius.components.view.View;
import org.eclipse.sirius.components.view.ViewFactory;
import org.eclipse.sirius.components.view.emf.form.ViewFormDescriptionConverter;
import org.eclipse.sirius.components.view.form.CheckboxDescription;
import org.eclipse.sirius.components.view.form.FormDescription;
import org.eclipse.sirius.components.view.form.FormElementFor;
import org.eclipse.sirius.components.view.form.FormElementIf;
import org.eclipse.sirius.components.view.form.FormFactory;
import org.eclipse.sirius.components.view.form.GroupDescription;
import org.eclipse.sirius.components.view.form.GroupDisplayMode;
import org.eclipse.sirius.components.view.form.LabelDescription;
import org.eclipse.sirius.components.view.form.PageDescription;
import org.eclipse.sirius.components.view.form.RadioDescription;
import org.eclipse.sirius.components.view.form.TextfieldDescription;
import org.eclipse.sirius.components.view.form.WidgetDescription;
import org.eclipse.sirius.components.view.widget.reference.ReferenceFactory;
import org.eclipse.sirius.components.view.widget.reference.ReferenceWidgetDescription;
import org.eclipse.sirius.web.services.api.representations.IInMemoryViewRegistry;
import org.eclipse.syson.application.services.DetailsViewService;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.LabelConstants;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.springframework.context.annotation.Configuration;

/**
 * Provides custom Details view for SysML elements.
 *
 * @author arichard
 */
@Configuration
public class SysMLv2PropertiesConfigurer implements IPropertiesDescriptionRegistryConfigurer {

    private static final String CORE_PROPERTIES = "Core Properties";

    private static final String ADVANCED_PROPERTIES = "Advanced Properties";

    private static final String REDEFINITION_PROPERTIES = "Redefinition Properties";

    private static final String SUBCLASSIFICATION_PROPERTIES = "Subclassification Properties";

    private static final String SUBSETTING_PROPERTIES = "Subsetting Properties";

    private static final String TYPING_PROPERTIES = "Typing Properties";

    private static final String REQUIREMENT_CONSTRAINT_KIND_PROPERTIES = "Kind Properties";

    private final ComposedAdapterFactory composedAdapterFactory;

    private final ViewFormDescriptionConverter converter;

    private final IFeedbackMessageService feedbackMessageService;

    private final IInMemoryViewRegistry viewRegistry;

    private final ILabelService labelService;

    public SysMLv2PropertiesConfigurer(ComposedAdapterFactory composedAdapterFactory, ViewFormDescriptionConverter converter, IFeedbackMessageService feedbackMessageService,
            IInMemoryViewRegistry viewRegistry, ILabelService labelService) {
        this.composedAdapterFactory = Objects.requireNonNull(composedAdapterFactory);
        this.viewRegistry = Objects.requireNonNull(viewRegistry);
        this.converter = Objects.requireNonNull(converter);
        this.feedbackMessageService = Objects.requireNonNull(feedbackMessageService);
        this.labelService = Objects.requireNonNull(labelService);
    }

    @Override
    public void addPropertiesDescriptions(IPropertiesDescriptionRegistry registry) {
        // Build the actual FormDescription that will be used in Detail view.
        FormDescription viewFormDescription = this.createDetailsView();

        // The FormDescription must be part of View inside a proper EMF Resource to be correctly handled
        URI uri = URI.createURI(IEMFEditingContext.RESOURCE_SCHEME + ":///" + UUID.nameUUIDFromBytes(SysMLv2PropertiesConfigurer.class.getCanonicalName().getBytes()));
        Resource resource = new XMIResourceImpl(uri);
        View view = org.eclipse.sirius.components.view.ViewFactory.eINSTANCE.createView();
        resource.getContents().add(view);
        view.getDescriptions().add(viewFormDescription);

        view.eAllContents().forEachRemaining(eObject -> {
            eObject.eAdapters().add(new IDAdapter(UUID.nameUUIDFromBytes(EcoreUtil.getURI(eObject).toString().getBytes())));
        });

        this.viewRegistry.register(view);

        // Convert the View-based FormDescription and register the result into the system
        AQLInterpreter interpreter = new AQLInterpreter(List.of(),
                List.of(new DetailsViewService(this.composedAdapterFactory, this.feedbackMessageService), this.labelService), List.of(SysmlPackage.eINSTANCE));
        IRepresentationDescription converted = this.converter.convert(viewFormDescription, List.of(), interpreter);
        if (converted instanceof org.eclipse.sirius.components.forms.description.FormDescription formDescription) {
            formDescription.getPageDescriptions().forEach(registry::add);
        }
    }

    private FormDescription createDetailsView() {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getElement());
        FormDescription form = FormFactory.eINSTANCE.createFormDescription();
        form.setName("SysON Details View");
        form.setDomainType(domainType);
        form.setTitleExpression("SysON Details View");

        PageDescription pageCore = FormFactory.eINSTANCE.createPageDescription();
        pageCore.setDomainType(domainType);
        pageCore.setPreconditionExpression("");
        pageCore.setLabelExpression("Core");
        pageCore.getGroups().add(this.createCorePropertiesGroup());
        pageCore.getGroups().add(this.createExtraRedefinitionPropertiesGroup());
        pageCore.getGroups().add(this.createExtraSubclassificationPropertiesGroup());
        pageCore.getGroups().add(this.createExtraSubsettingPropertiesGroup());
        pageCore.getGroups().add(this.createExtraFeatureTypingPropertiesGroup());
        pageCore.getGroups().add(this.createExtraRequirementConstraintMembershipPropertiesGroup());

        PageDescription pageAdvanced = FormFactory.eINSTANCE.createPageDescription();
        pageAdvanced.setDomainType(domainType);
        pageAdvanced.setPreconditionExpression("");
        pageAdvanced.setLabelExpression("Advanced");
        pageAdvanced.getGroups().add(this.createAdvancedPropertiesGroup());

        form.getPages().add(pageAdvanced);
        form.getPages().add(pageCore);

        return form;
    }

    private GroupDescription createCorePropertiesGroup() {
        GroupDescription group = FormFactory.eINSTANCE.createGroupDescription();
        group.setDisplayMode(GroupDisplayMode.LIST);
        group.setName(CORE_PROPERTIES);
        group.setLabelExpression("aql:self.eClass().getLabel() + ' Properties'");
        group.setSemanticCandidatesExpression(AQLConstants.AQL_SELF);

        group.getChildren().add(this.createCoreWidgets());

        return group;
    }

    private GroupDescription createAdvancedPropertiesGroup() {
        GroupDescription group = FormFactory.eINSTANCE.createGroupDescription();
        group.setDisplayMode(GroupDisplayMode.LIST);
        group.setName(ADVANCED_PROPERTIES);
        group.setLabelExpression("aql:self.eClass().getLabel() + ' Properties'");
        group.setSemanticCandidatesExpression(AQLConstants.AQL_SELF);

        group.getChildren().add(this.createAdvancedWidgets());

        return group;
    }

    private GroupDescription createExtraRedefinitionPropertiesGroup() {
        GroupDescription group = FormFactory.eINSTANCE.createGroupDescription();
        group.setDisplayMode(GroupDisplayMode.LIST);
        group.setName(REDEFINITION_PROPERTIES);
        group.setLabelExpression("");
        group.setSemanticCandidatesExpression("aql:self.ownedRelationship->filter(sysml::Redefinition)");

        ReferenceWidgetDescription refWidget = ReferenceFactory.eINSTANCE.createReferenceWidgetDescription();
        refWidget.setName("ExtraReferenceWidget");
        refWidget.setLabelExpression("Redefines");
        refWidget.setReferenceNameExpression(SysmlPackage.eINSTANCE.getRedefinition_RedefinedFeature().getName());
        refWidget.setReferenceOwnerExpression(AQLConstants.AQL_SELF);
        refWidget.setIsEnabledExpression("aql:not(self.isReadOnly())");
        ChangeContext setRefWidget = ViewFactory.eINSTANCE.createChangeContext();
        setRefWidget.setExpression("aql:self.handleReferenceWidgetNewValue('" + SysmlPackage.eINSTANCE.getRedefinition_RedefinedFeature().getName() + "', " + ViewFormDescriptionConverter.NEW_VALUE + LabelConstants.CLOSE_PARENTHESIS);
        refWidget.getBody().add(setRefWidget);

        group.getChildren().add(refWidget);

        return group;
    }

    private GroupDescription createExtraSubclassificationPropertiesGroup() {
        GroupDescription group = FormFactory.eINSTANCE.createGroupDescription();
        group.setDisplayMode(GroupDisplayMode.LIST);
        group.setName(SUBCLASSIFICATION_PROPERTIES);
        group.setLabelExpression("");
        group.setSemanticCandidatesExpression("aql:self.ownedRelationship->filter(sysml::Subclassification)");

        ReferenceWidgetDescription refWidget = ReferenceFactory.eINSTANCE.createReferenceWidgetDescription();
        refWidget.setName("ExtraReferenceWidget");
        refWidget.setLabelExpression("Specializes");
        refWidget.setReferenceNameExpression(SysmlPackage.eINSTANCE.getSubclassification_Superclassifier().getName());
        refWidget.setReferenceOwnerExpression(AQLConstants.AQL_SELF);
        refWidget.setIsEnabledExpression("aql:not(self.isReadOnly())");
        ChangeContext setRefWidget = ViewFactory.eINSTANCE.createChangeContext();
        setRefWidget.setExpression("aql:self.handleReferenceWidgetNewValue('" + SysmlPackage.eINSTANCE.getSubclassification_Superclassifier().getName() + "', " + ViewFormDescriptionConverter.NEW_VALUE + LabelConstants.CLOSE_PARENTHESIS);
        refWidget.getBody().add(setRefWidget);

        group.getChildren().add(refWidget);

        return group;
    }

    private GroupDescription createExtraSubsettingPropertiesGroup() {
        GroupDescription group = FormFactory.eINSTANCE.createGroupDescription();
        group.setDisplayMode(GroupDisplayMode.LIST);
        group.setName(SUBSETTING_PROPERTIES);
        group.setLabelExpression("");
        group.setSemanticCandidatesExpression("aql:self.ownedRelationship->select(r | r.oclIsTypeOf(sysml::Subsetting))");

        ReferenceWidgetDescription refWidget = ReferenceFactory.eINSTANCE.createReferenceWidgetDescription();
        refWidget.setName("ExtraReferenceWidget");
        refWidget.setLabelExpression("Subsets");
        refWidget.setReferenceNameExpression(SysmlPackage.eINSTANCE.getSubsetting_SubsettedFeature().getName());
        refWidget.setReferenceOwnerExpression(AQLConstants.AQL_SELF);
        refWidget.setIsEnabledExpression("aql:not(self.isReadOnly())");
        ChangeContext setRefWidget = ViewFactory.eINSTANCE.createChangeContext();
        setRefWidget.setExpression("aql:self.handleReferenceWidgetNewValue('" + SysmlPackage.eINSTANCE.getSubsetting_SubsettedFeature().getName() + "', " + ViewFormDescriptionConverter.NEW_VALUE + LabelConstants.CLOSE_PARENTHESIS);
        refWidget.getBody().add(setRefWidget);

        group.getChildren().add(refWidget);

        return group;
    }

    private GroupDescription createExtraFeatureTypingPropertiesGroup() {
        GroupDescription group = FormFactory.eINSTANCE.createGroupDescription();
        group.setDisplayMode(GroupDisplayMode.LIST);
        group.setName(TYPING_PROPERTIES);
        group.setLabelExpression("");
        group.setSemanticCandidatesExpression("aql:self.ownedRelationship->filter(sysml::FeatureTyping)");

        ReferenceWidgetDescription refWidget = ReferenceFactory.eINSTANCE.createReferenceWidgetDescription();
        refWidget.setName("ExtraReferenceWidget");
        refWidget.setLabelExpression("Typed by");
        refWidget.setReferenceNameExpression(SysmlPackage.eINSTANCE.getFeatureTyping_Type().getName());
        refWidget.setReferenceOwnerExpression(AQLConstants.AQL_SELF);
        refWidget.setIsEnabledExpression("aql:not(self.isReadOnly())");
        ChangeContext setRefWidget = ViewFactory.eINSTANCE.createChangeContext();
        setRefWidget.setExpression("aql:self.handleReferenceWidgetNewValue('" + SysmlPackage.eINSTANCE.getFeatureTyping_Type().getName() + "', " + ViewFormDescriptionConverter.NEW_VALUE + LabelConstants.CLOSE_PARENTHESIS);
        refWidget.getBody().add(setRefWidget);

        group.getChildren().add(refWidget);

        return group;
    }

    private GroupDescription createExtraRequirementConstraintMembershipPropertiesGroup() {
        GroupDescription group = FormFactory.eINSTANCE.createGroupDescription();
        group.setDisplayMode(GroupDisplayMode.LIST);
        group.setName(REQUIREMENT_CONSTRAINT_KIND_PROPERTIES);
        group.setLabelExpression("");
        group.setSemanticCandidatesExpression("aql:self.eContainer()->filter(sysml::RequirementConstraintMembership)");

        RadioDescription radio = FormFactory.eINSTANCE.createRadioDescription();
        radio.setName("ExtraRadioKindWidget");
        radio.setLabelExpression("Kind");
        radio.setCandidatesExpression("aql:self.getEnumCandidates('" + SysmlPackage.eINSTANCE.getRequirementConstraintMembership_Kind().getName() + "')");
        radio.setCandidateLabelExpression("aql:candidate.name");
        radio.setValueExpression("aql:self.getEnumValue('" + SysmlPackage.eINSTANCE.getRequirementConstraintMembership_Kind().getName() + "')");
        ChangeContext setNewValueOperation = ViewFactory.eINSTANCE.createChangeContext();
        setNewValueOperation.setExpression("aql:self.setNewValue('" + SysmlPackage.eINSTANCE.getRequirementConstraintMembership_Kind().getName() + "', newValue.instance)");
        radio.getBody().add(setNewValueOperation);

        group.getChildren().add(radio);

        return group;
    }

    private FormElementFor createCoreWidgets() {
        FormElementFor forElt = FormFactory.eINSTANCE.createFormElementFor();
        forElt.setName("Widgets for Core Group");
        forElt.setIterator("eStructuralFeature");
        forElt.setIterableExpression("aql:self.getCoreFeatures()");
        forElt.getChildren().addAll(this.createWidgets());
        return forElt;
    }

    private FormElementFor createAdvancedWidgets() {
        FormElementFor forElt = FormFactory.eINSTANCE.createFormElementFor();
        forElt.setName("Widgets for Advanced Group");
        forElt.setIterator("eStructuralFeature");
        forElt.setIterableExpression("aql:self.getAdvancedFeatures()");
        forElt.getChildren().addAll(this.createWidgets());
        return forElt;
    }

    private List<FormElementIf> createWidgets() {
        List<FormElementIf> widgets = new ArrayList<>();

        FormElementIf label = FormFactory.eINSTANCE.createFormElementIf();
        label.setName("Read-only String Attributes");
        label.setPredicateExpression("aql:eStructuralFeature.isReadOnlyStringAttribute()");
        label.getChildren().add(this.createLabelWidget());
        widgets.add(label);

        FormElementIf textfield = FormFactory.eINSTANCE.createFormElementIf();
        textfield.setName("String Attributes");
        textfield.setPredicateExpression("aql:eStructuralFeature.isStringAttribute()");
        textfield.getChildren().add(this.createTextfieldWidget());
        widgets.add(textfield);

        FormElementIf checkbox = FormFactory.eINSTANCE.createFormElementIf();
        checkbox.setName("Boolean Attributes");
        checkbox.setPredicateExpression("aql:eStructuralFeature.isBooleanAttribute()");
        checkbox.getChildren().add(this.createCheckboxWidget());
        widgets.add(checkbox);

        FormElementIf radio = FormFactory.eINSTANCE.createFormElementIf();
        radio.setName("Radio Attributes");
        radio.setPredicateExpression("aql:eStructuralFeature.isEnumAttribute()");
        radio.getChildren().add(this.createRadioWidget());
        widgets.add(radio);

        FormElementIf refWidget = FormFactory.eINSTANCE.createFormElementIf();
        refWidget.setName("ReferenceWidget References");
        refWidget.setPredicateExpression("aql:eStructuralFeature.isReference()");
        refWidget.getChildren().add(this.createReferenceWidget());
        widgets.add(refWidget);

        FormElementIf number = FormFactory.eINSTANCE.createFormElementIf();
        number.setName("Number Attributes");
        number.setPredicateExpression("aql:eStructuralFeature.isNumberAttribute()");
        number.getChildren().add(this.createTextfieldWidget());
        widgets.add(number);

        return widgets;
    }

    private WidgetDescription createLabelWidget() {
        LabelDescription label = FormFactory.eINSTANCE.createLabelDescription();
        label.setName("LabelWidget");
        label.setLabelExpression("aql:self.getDetailsViewLabel(eStructuralFeature)");
        label.setValueExpression("aql:self.eGet(eStructuralFeature)");
        return label;
    }

    private WidgetDescription createTextfieldWidget() {
        TextfieldDescription textfield = FormFactory.eINSTANCE.createTextfieldDescription();
        textfield.setName("TextfieldWidget");
        textfield.setLabelExpression("aql:self.getDetailsViewLabel(eStructuralFeature)");
        textfield.setValueExpression("aql:self.eGet(eStructuralFeature)");
        textfield.setIsEnabledExpression("aql:not(eStructuralFeature.isReadOnly() or self.isReadOnly())");
        ChangeContext setNewValueOperation = ViewFactory.eINSTANCE.createChangeContext();
        setNewValueOperation.setExpression("aql:self.setNewValue(eStructuralFeature, " + ViewFormDescriptionConverter.NEW_VALUE + LabelConstants.CLOSE_PARENTHESIS);
        textfield.getBody().add(setNewValueOperation);
        return textfield;
    }

    private WidgetDescription createCheckboxWidget() {
        CheckboxDescription checkbox = FormFactory.eINSTANCE.createCheckboxDescription();
        checkbox.setName("CheckboxWidget");
        checkbox.setLabelExpression("aql:self.getDetailsViewLabel(eStructuralFeature)");
        checkbox.setValueExpression("aql:self.eGet(eStructuralFeature)");
        checkbox.setIsEnabledExpression("aql:not(eStructuralFeature.isReadOnly() or self.isReadOnly())");
        ChangeContext setNewValueOperation = ViewFactory.eINSTANCE.createChangeContext();
        setNewValueOperation.setExpression("aql:self.setNewValue(eStructuralFeature, " + ViewFormDescriptionConverter.NEW_VALUE + LabelConstants.CLOSE_PARENTHESIS);
        checkbox.getBody().add(setNewValueOperation);
        return checkbox;
    }

    private WidgetDescription createRadioWidget() {
        RadioDescription radio = FormFactory.eINSTANCE.createRadioDescription();
        radio.setName("RadioWidget");
        radio.setLabelExpression("aql:self.getDetailsViewLabel(eStructuralFeature)");
        radio.setCandidatesExpression("aql:self.getEnumCandidates(eStructuralFeature)");
        radio.setCandidateLabelExpression("aql:candidate.name");
        radio.setValueExpression("aql:self.getEnumValue(eStructuralFeature)");
        radio.setIsEnabledExpression("aql:not(eStructuralFeature.isReadOnly() or self.isReadOnly())");
        ChangeContext setNewValueOperation = ViewFactory.eINSTANCE.createChangeContext();
        setNewValueOperation.setExpression("aql:self.setNewValue(eStructuralFeature, newValue.instance)");
        radio.getBody().add(setNewValueOperation);
        return radio;
    }

    private WidgetDescription createReferenceWidget() {
        ReferenceWidgetDescription refWidget = ReferenceFactory.eINSTANCE.createReferenceWidgetDescription();
        refWidget.setName("ReferenceWidget");
        refWidget.setLabelExpression("aql:self.getDetailsViewLabel(eStructuralFeature)");
        refWidget.setReferenceNameExpression("aql:eStructuralFeature.name");
        refWidget.setReferenceOwnerExpression("aql:self");
        refWidget.setIsEnabledExpression("aql:not(eStructuralFeature.isReadOnly() or self.isReadOnly())");
        ChangeContext setRefWidget = ViewFactory.eINSTANCE.createChangeContext();
        setRefWidget.setExpression("aql:self.handleReferenceWidgetNewValue(eStructuralFeature.name, " + ViewFormDescriptionConverter.NEW_VALUE + LabelConstants.CLOSE_PARENTHESIS);
        refWidget.getBody().add(setRefWidget);
        return refWidget;
    }
}
