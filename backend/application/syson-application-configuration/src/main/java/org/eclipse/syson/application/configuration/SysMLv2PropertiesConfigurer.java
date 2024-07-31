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

import org.apache.logging.log4j.util.Strings;
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
import org.eclipse.sirius.components.view.form.FormElementDescription;
import org.eclipse.sirius.components.view.form.FormElementFor;
import org.eclipse.sirius.components.view.form.FormElementIf;
import org.eclipse.sirius.components.view.form.FormFactory;
import org.eclipse.sirius.components.view.form.GroupDescription;
import org.eclipse.sirius.components.view.form.GroupDisplayMode;
import org.eclipse.sirius.components.view.form.LabelDescription;
import org.eclipse.sirius.components.view.form.PageDescription;
import org.eclipse.sirius.components.view.form.RadioDescription;
import org.eclipse.sirius.components.view.form.TextAreaDescription;
import org.eclipse.sirius.components.view.form.TextfieldDescription;
import org.eclipse.sirius.components.view.form.WidgetDescription;
import org.eclipse.sirius.components.view.widget.reference.ReferenceFactory;
import org.eclipse.sirius.components.view.widget.reference.ReferenceWidgetDescription;
import org.eclipse.syson.application.services.DetailsViewService;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.helper.LabelConstants;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.AQLUtils;
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

    private static final String STATESUBACTIONKIND_PROPERTIES = "Statesubaction Properties";

    private static final String TRANSITION_SOURCETARGET_PROPERTIES = "Transition Source and Target Properties";

    private static final String SUBSETTING_PROPERTIES = "Subsetting Properties";

    private static final String TYPING_PROPERTIES = "Typing Properties";

    private static final String MEMBERSHIP_PROPERTIES = "Membership Properties";

    private static final String REQUIREMENT_CONSTRAINT_KIND_PROPERTIES = "Kind Properties";

    private static final String ACCEPT_ACTION_USAGE_PROPERTIES = "Accept Action Usage Properties";

    private static final String AQL_NOT_SELF_IS_READ_ONLY = "aql:not(self.isReadOnly())";

    private static final String AQL_NOT_SELF_IS_READ_ONLY_E_STRUCTURAL_FEATURE = "aql:not(self.isReadOnly(eStructuralFeature))";

    private static final String E_STRUCTURAL_FEATURE = "eStructuralFeature";

    private static final String CLOSING_QUOTE_CLOSING_PARENTHESIS = "')";

    private final ComposedAdapterFactory composedAdapterFactory;

    private final ViewFormDescriptionConverter converter;

    private final IFeedbackMessageService feedbackMessageService;

    private final ILabelService labelService;

    private final UtilService utilService;

    public SysMLv2PropertiesConfigurer(ComposedAdapterFactory composedAdapterFactory, ViewFormDescriptionConverter converter, IFeedbackMessageService feedbackMessageService,
            ILabelService labelService) {
        this.composedAdapterFactory = Objects.requireNonNull(composedAdapterFactory);
        this.converter = Objects.requireNonNull(converter);
        this.feedbackMessageService = Objects.requireNonNull(feedbackMessageService);
        this.labelService = Objects.requireNonNull(labelService);
        this.utilService = new UtilService();
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

        // Convert the View-based FormDescription and register the result into the system
        AQLInterpreter interpreter = new AQLInterpreter(List.of(), List.of(new DetailsViewService(this.composedAdapterFactory, this.feedbackMessageService), this.labelService, this.utilService),
                List.of(SysmlPackage.eINSTANCE));
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
        pageCore.setName("SysON-DetailsView-Core");
        pageCore.setDomainType(domainType);
        pageCore.setPreconditionExpression("");
        pageCore.setLabelExpression("Core");
        pageCore.getGroups().add(this.createCorePropertiesGroup());
        pageCore.getGroups().add(this.createExtraMembershipPropertiesGroup());
        pageCore.getGroups().add(this.createExtraRedefinitionPropertiesGroup());
        pageCore.getGroups().add(this.createExtraStatesubactionMembershipKindPropertiesGroup());
        pageCore.getGroups().add(this.createExtraSubclassificationPropertiesGroup());
        pageCore.getGroups().add(this.createExtraSubsettingPropertiesGroup());
        pageCore.getGroups().add(this.createExtraFeatureTypingPropertiesGroup());
        pageCore.getGroups().add(this.createExtraRequirementConstraintMembershipPropertiesGroup());
        pageCore.getGroups().add(this.createExtraAcceptActionUsagePropertiesGroup());
        pageCore.getGroups().add(this.createExtraTransitionSourceTargetPropertiesGroup());

        PageDescription pageAdvanced = FormFactory.eINSTANCE.createPageDescription();
        pageAdvanced.setName("SysON-DetailsView-Advanced");
        pageAdvanced.setDomainType(domainType);
        pageAdvanced.setPreconditionExpression("");
        pageAdvanced.setLabelExpression("Advanced");
        pageAdvanced.getGroups().add(this.createAdvancedPropertiesGroup());

        form.getPages().add(pageCore);
        form.getPages().add(pageAdvanced);

        return form;
    }

    private GroupDescription createCorePropertiesGroup() {
        GroupDescription group = FormFactory.eINSTANCE.createGroupDescription();
        group.setDisplayMode(GroupDisplayMode.LIST);
        group.setName(CORE_PROPERTIES);
        group.setLabelExpression("aql:self.eClass().getLabel() + ' Properties'");
        group.setSemanticCandidatesExpression(AQLConstants.AQL_SELF);

        group.getChildren().add(this.createCoreWidgets());
        group.getChildren().add(this.createDocumentationWidget());

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
        refWidget.setIsEnabledExpression(AQL_NOT_SELF_IS_READ_ONLY);
        ChangeContext setRefWidget = ViewFactory.eINSTANCE.createChangeContext();
        setRefWidget.setExpression("aql:self.handleReferenceWidgetNewValue('" + SysmlPackage.eINSTANCE.getRedefinition_RedefinedFeature().getName() + "', " + ViewFormDescriptionConverter.NEW_VALUE
                + LabelConstants.CLOSE_PARENTHESIS);
        refWidget.getBody().add(setRefWidget);

        group.getChildren().add(refWidget);

        return group;
    }

    private GroupDescription createExtraStatesubactionMembershipKindPropertiesGroup() {
        GroupDescription group = FormFactory.eINSTANCE.createGroupDescription();
        group.setDisplayMode(GroupDisplayMode.LIST);
        group.setName(STATESUBACTIONKIND_PROPERTIES);
        group.setLabelExpression("");
        group.setSemanticCandidatesExpression("aql:self.eContainer()->filter(sysml::StateSubactionMembership)");

        RadioDescription radio = FormFactory.eINSTANCE.createRadioDescription();
        radio.setName("ExtraRadioKindWidget");
        radio.setLabelExpression("Kind");
        radio.setCandidatesExpression(AQLUtils.getSelfServiceCallExpression("getEnumCandidates", Strings.quote(SysmlPackage.eINSTANCE.getStateSubactionMembership_Kind().getName())));
        radio.setCandidateLabelExpression("aql:candidate.name");
        radio.setValueExpression(AQLUtils.getSelfServiceCallExpression("getEnumValue", Strings.quote(SysmlPackage.eINSTANCE.getStateSubactionMembership_Kind().getName())));
        radio.setIsEnabledExpression(AQL_NOT_SELF_IS_READ_ONLY);
        ChangeContext setNewValueOperation = ViewFactory.eINSTANCE.createChangeContext();
        setNewValueOperation.setExpression(AQLUtils.getSelfServiceCallExpression("setNewValue", List.of(Strings.quote(SysmlPackage.eINSTANCE.getStateSubactionMembership_Kind().getName()), "newValue.instance")));
        radio.getBody().add(setNewValueOperation);

        group.getChildren().add(radio);

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
        refWidget.setIsEnabledExpression(AQL_NOT_SELF_IS_READ_ONLY);
        ChangeContext setRefWidget = ViewFactory.eINSTANCE.createChangeContext();
        setRefWidget.setExpression("aql:self.handleReferenceWidgetNewValue('" + SysmlPackage.eINSTANCE.getSubclassification_Superclassifier().getName() + "', " + ViewFormDescriptionConverter.NEW_VALUE
                + LabelConstants.CLOSE_PARENTHESIS);
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
        refWidget.setIsEnabledExpression(AQL_NOT_SELF_IS_READ_ONLY);
        ChangeContext setRefWidget = ViewFactory.eINSTANCE.createChangeContext();
        setRefWidget.setExpression("aql:self.handleReferenceWidgetNewValue('" + SysmlPackage.eINSTANCE.getSubsetting_SubsettedFeature().getName() + "', " + ViewFormDescriptionConverter.NEW_VALUE
                + LabelConstants.CLOSE_PARENTHESIS);
        refWidget.getBody().add(setRefWidget);

        group.getChildren().add(refWidget);

        return group;
    }

    private GroupDescription createExtraFeatureTypingPropertiesGroup() {
        GroupDescription group = FormFactory.eINSTANCE.createGroupDescription();
        group.setDisplayMode(GroupDisplayMode.LIST);
        group.setName(TYPING_PROPERTIES);
        group.setLabelExpression("");
        group.setSemanticCandidatesExpression("aql:self->filter(sysml::Feature)");

        ReferenceWidgetDescription refWidget = ReferenceFactory.eINSTANCE.createReferenceWidgetDescription();
        refWidget.setName("ExtraReferenceWidget");
        refWidget.setLabelExpression("Typed by");
        refWidget.setReferenceNameExpression(SysmlPackage.eINSTANCE.getFeature_Type().getName());
        refWidget.setReferenceOwnerExpression(AQLUtils.getSelfServiceCallExpression("getFeatureTypingOwnerExpression"));
        refWidget.setIsEnabledExpression(AQL_NOT_SELF_IS_READ_ONLY);
        ChangeContext setRefWidget = ViewFactory.eINSTANCE.createChangeContext();
        setRefWidget.setExpression(AQLUtils.getSelfServiceCallExpression("handleFeatureTypingNewValue", ViewFormDescriptionConverter.NEW_VALUE));
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
        radio.setCandidatesExpression("aql:self.getEnumCandidates('" + SysmlPackage.eINSTANCE.getRequirementConstraintMembership_Kind().getName() + CLOSING_QUOTE_CLOSING_PARENTHESIS);
        radio.setCandidateLabelExpression("aql:candidate.name");
        radio.setValueExpression("aql:self.getEnumValue('" + SysmlPackage.eINSTANCE.getRequirementConstraintMembership_Kind().getName() + CLOSING_QUOTE_CLOSING_PARENTHESIS);
        radio.setIsEnabledExpression(AQL_NOT_SELF_IS_READ_ONLY);
        ChangeContext setNewValueOperation = ViewFactory.eINSTANCE.createChangeContext();
        setNewValueOperation.setExpression("aql:self.setNewValue('" + SysmlPackage.eINSTANCE.getRequirementConstraintMembership_Kind().getName() + "', newValue.instance)");
        radio.getBody().add(setNewValueOperation);

        group.getChildren().add(radio);

        return group;
    }

    private GroupDescription createExtraMembershipPropertiesGroup() {
        GroupDescription group = FormFactory.eINSTANCE.createGroupDescription();
        group.setDisplayMode(GroupDisplayMode.LIST);
        group.setName(MEMBERSHIP_PROPERTIES);
        group.setLabelExpression("");
        group.setSemanticCandidatesExpression("aql:self.eContainer()->filter(sysml::Membership)");

        RadioDescription radio = FormFactory.eINSTANCE.createRadioDescription();
        radio.setName("ExtraRadioVisibilityWidget");
        radio.setLabelExpression("Visibility");
        radio.setCandidatesExpression("aql:self.getEnumCandidates('" + SysmlPackage.eINSTANCE.getMembership_Visibility().getName() + CLOSING_QUOTE_CLOSING_PARENTHESIS);
        radio.setCandidateLabelExpression("aql:candidate.name");
        radio.setValueExpression("aql:self.getEnumValue('" + SysmlPackage.eINSTANCE.getMembership_Visibility().getName() + CLOSING_QUOTE_CLOSING_PARENTHESIS);
        radio.setIsEnabledExpression(AQL_NOT_SELF_IS_READ_ONLY);
        ChangeContext setNewValueOperation = ViewFactory.eINSTANCE.createChangeContext();
        setNewValueOperation.setExpression("aql:self.setNewValue('" + SysmlPackage.eINSTANCE.getMembership_Visibility().getName() + "', newValue.instance)");
        radio.getBody().add(setNewValueOperation);

        group.getChildren().add(radio);

        return group;
    }

    private GroupDescription createExtraAcceptActionUsagePropertiesGroup() {
        GroupDescription group = FormFactory.eINSTANCE.createGroupDescription();
        group.setDisplayMode(GroupDisplayMode.LIST);
        group.setName(ACCEPT_ACTION_USAGE_PROPERTIES);
        group.setLabelExpression("");
        group.setSemanticCandidatesExpression(AQLUtils.getSelfServiceCallExpression("getAcceptActionUsage"));

        ReferenceWidgetDescription payloadRefWidget = ReferenceFactory.eINSTANCE.createReferenceWidgetDescription();
        payloadRefWidget.setName("ExtraPayloadWidget");
        payloadRefWidget.setLabelExpression("Payload");
        payloadRefWidget.setReferenceNameExpression(SysmlPackage.eINSTANCE.getFeatureTyping_Type().getName());
        payloadRefWidget.setReferenceOwnerExpression(AQLUtils.getSelfServiceCallExpression("getAcceptActionUsagePayloadFeatureTyping"));
        payloadRefWidget.setIsEnabledExpression(AQL_NOT_SELF_IS_READ_ONLY);
        ChangeContext setPayloadRefWidget = ViewFactory.eINSTANCE.createChangeContext();
        setPayloadRefWidget.setExpression(AQLUtils.getSelfServiceCallExpression("setAcceptActionUsagePayloadParameter", ViewFormDescriptionConverter.NEW_VALUE));
        payloadRefWidget.getBody().add(setPayloadRefWidget);

        ReferenceWidgetDescription receiverRefWidget = ReferenceFactory.eINSTANCE.createReferenceWidgetDescription();
        receiverRefWidget.setName("ExtraReceiverWidget");
        receiverRefWidget.setLabelExpression("Receiver");
        receiverRefWidget.setReferenceNameExpression(SysmlPackage.eINSTANCE.getMembership_MemberElement().getName());
        receiverRefWidget.setReferenceOwnerExpression(AQLUtils.getSelfServiceCallExpression("getAcceptActionUsageReceiverMembership"));
        receiverRefWidget.setIsEnabledExpression(AQL_NOT_SELF_IS_READ_ONLY);
        ChangeContext setReceiverRefWidget = ViewFactory.eINSTANCE.createChangeContext();
        setReceiverRefWidget.setExpression(AQLUtils.getSelfServiceCallExpression("setAcceptActionUsageReceiverArgument", ViewFormDescriptionConverter.NEW_VALUE));
        receiverRefWidget.getBody().add(setReceiverRefWidget);

        group.getChildren().add(payloadRefWidget);
        group.getChildren().add(receiverRefWidget);

        return group;
    }

    private GroupDescription createExtraTransitionSourceTargetPropertiesGroup() {
        GroupDescription group = FormFactory.eINSTANCE.createGroupDescription();
        group.setDisplayMode(GroupDisplayMode.LIST);
        group.setName(TRANSITION_SOURCETARGET_PROPERTIES);
        group.setLabelExpression("");
        group.setSemanticCandidatesExpression(AQLUtils.getSelfServiceCallExpression("getTransitionUsage"));

        ReferenceWidgetDescription sourceRefWidget = ReferenceFactory.eINSTANCE.createReferenceWidgetDescription();
        sourceRefWidget.setName("ExtraSourceWidget");
        sourceRefWidget.setLabelExpression("Source");
        sourceRefWidget.setReferenceNameExpression(SysmlPackage.eINSTANCE.getTransitionUsage_Source().getName());
        sourceRefWidget.setReferenceOwnerExpression(AQLConstants.AQL_SELF);
        sourceRefWidget.setIsEnabledExpression(AQL_NOT_SELF_IS_READ_ONLY);
        ChangeContext setSourceRefWidget = ViewFactory.eINSTANCE.createChangeContext();
        setSourceRefWidget.setExpression(AQLUtils.getSelfServiceCallExpression("setTransitionSourceParameter", ViewFormDescriptionConverter.NEW_VALUE));
        sourceRefWidget.getBody().add(setSourceRefWidget);

        ReferenceWidgetDescription targetRefWidget = ReferenceFactory.eINSTANCE.createReferenceWidgetDescription();
        targetRefWidget.setName("ExtraTargetWidget");
        targetRefWidget.setLabelExpression("Target");
        targetRefWidget.setReferenceNameExpression(SysmlPackage.eINSTANCE.getTransitionUsage_Target().getName());
        targetRefWidget.setReferenceOwnerExpression(AQLConstants.AQL_SELF);
        targetRefWidget.setIsEnabledExpression(AQL_NOT_SELF_IS_READ_ONLY);
        ChangeContext setTargetRefWidget = ViewFactory.eINSTANCE.createChangeContext();
        setTargetRefWidget.setExpression(AQLUtils.getSelfServiceCallExpression("setTransitionTargetParameter", ViewFormDescriptionConverter.NEW_VALUE));
        targetRefWidget.getBody().add(setTargetRefWidget);

        group.getChildren().add(sourceRefWidget);
        group.getChildren().add(targetRefWidget);

        return group;
    }

    private FormElementFor createCoreWidgets() {
        FormElementFor forElt = FormFactory.eINSTANCE.createFormElementFor();
        forElt.setName("Widgets for Core Group");
        forElt.setIterator(E_STRUCTURAL_FEATURE);
        forElt.setIterableExpression(AQLUtils.getSelfServiceCallExpression("getCoreFeatures"));
        forElt.getChildren().addAll(this.createWidgets());
        return forElt;
    }

    private FormElementFor createAdvancedWidgets() {
        FormElementFor forElt = FormFactory.eINSTANCE.createFormElementFor();
        forElt.setName("Widgets for Advanced Group");
        forElt.setIterator(E_STRUCTURAL_FEATURE);
        forElt.setIterableExpression(AQLUtils.getSelfServiceCallExpression("getAdvancedFeatures"));
        forElt.getChildren().addAll(this.createWidgets());
        return forElt;
    }

    private List<FormElementIf> createWidgets() {
        List<FormElementIf> widgets = new ArrayList<>();

        FormElementIf label = FormFactory.eINSTANCE.createFormElementIf();
        label.setName("Read-only String Attributes");
        label.setPredicateExpression(AQLUtils.getSelfServiceCallExpression("isReadOnlyStringAttribute", E_STRUCTURAL_FEATURE));
        label.getChildren().add(this.createLabelWidget());
        widgets.add(label);

        FormElementIf textfield = FormFactory.eINSTANCE.createFormElementIf();
        textfield.setName("String Attributes");
        textfield.setPredicateExpression(AQLUtils.getSelfServiceCallExpression("isStringAttribute", E_STRUCTURAL_FEATURE));
        textfield.getChildren().add(this.createTextfieldWidget());
        widgets.add(textfield);

        FormElementIf checkbox = FormFactory.eINSTANCE.createFormElementIf();
        checkbox.setName("Boolean Attributes");
        checkbox.setPredicateExpression(AQLUtils.getServiceCallExpression(E_STRUCTURAL_FEATURE, "isBooleanAttribute"));
        checkbox.getChildren().add(this.createCheckboxWidget());
        widgets.add(checkbox);

        FormElementIf radio = FormFactory.eINSTANCE.createFormElementIf();
        radio.setName("Radio Attributes");
        radio.setPredicateExpression(AQLUtils.getServiceCallExpression(E_STRUCTURAL_FEATURE, "isEnumAttribute"));
        radio.getChildren().add(this.createRadioWidget());
        widgets.add(radio);

        FormElementIf refWidget = FormFactory.eINSTANCE.createFormElementIf();
        refWidget.setName("ReferenceWidget References");
        refWidget.setPredicateExpression(AQLUtils.getServiceCallExpression(E_STRUCTURAL_FEATURE, "isReference"));
        refWidget.getChildren().add(this.createReferenceWidget());
        widgets.add(refWidget);

        FormElementIf number = FormFactory.eINSTANCE.createFormElementIf();
        number.setName("Number Attributes");
        number.setPredicateExpression(AQLUtils.getServiceCallExpression(E_STRUCTURAL_FEATURE, "isNumberAttribute"));
        number.getChildren().add(this.createTextfieldWidget());
        widgets.add(number);

        return widgets;
    }

    private WidgetDescription createLabelWidget() {
        LabelDescription label = FormFactory.eINSTANCE.createLabelDescription();
        label.setName("LabelWidget");
        label.setLabelExpression(AQLUtils.getSelfServiceCallExpression("getDetailsViewLabel", E_STRUCTURAL_FEATURE));
        label.setValueExpression(AQLUtils.getSelfServiceCallExpression("eGet", E_STRUCTURAL_FEATURE));
        return label;
    }

    private WidgetDescription createTextfieldWidget() {
        TextfieldDescription textfield = FormFactory.eINSTANCE.createTextfieldDescription();
        textfield.setName("TextfieldWidget");
        textfield.setLabelExpression(AQLUtils.getSelfServiceCallExpression("getDetailsViewLabel", E_STRUCTURAL_FEATURE));
        textfield.setValueExpression(AQLUtils.getSelfServiceCallExpression("eGet", E_STRUCTURAL_FEATURE));
        textfield.setIsEnabledExpression(AQL_NOT_SELF_IS_READ_ONLY_E_STRUCTURAL_FEATURE);
        ChangeContext setNewValueOperation = ViewFactory.eINSTANCE.createChangeContext();
        setNewValueOperation.setExpression(AQLUtils.getSelfServiceCallExpression("setNewValue", List.of(E_STRUCTURAL_FEATURE, ViewFormDescriptionConverter.NEW_VALUE)));
        textfield.getBody().add(setNewValueOperation);
        return textfield;
    }

    private WidgetDescription createCheckboxWidget() {
        CheckboxDescription checkbox = FormFactory.eINSTANCE.createCheckboxDescription();
        checkbox.setName("CheckboxWidget");
        checkbox.setLabelExpression(AQLUtils.getSelfServiceCallExpression("getDetailsViewLabel", E_STRUCTURAL_FEATURE));
        checkbox.setValueExpression(AQLUtils.getSelfServiceCallExpression("eGet", E_STRUCTURAL_FEATURE));
        checkbox.setIsEnabledExpression(AQL_NOT_SELF_IS_READ_ONLY_E_STRUCTURAL_FEATURE);
        ChangeContext setNewValueOperation = ViewFactory.eINSTANCE.createChangeContext();
        setNewValueOperation.setExpression(AQLUtils.getSelfServiceCallExpression("setNewValue", List.of(E_STRUCTURAL_FEATURE, ViewFormDescriptionConverter.NEW_VALUE)));
        checkbox.getBody().add(setNewValueOperation);
        return checkbox;
    }

    private WidgetDescription createRadioWidget() {
        RadioDescription radio = FormFactory.eINSTANCE.createRadioDescription();
        radio.setName("RadioWidget");
        radio.setLabelExpression(AQLUtils.getSelfServiceCallExpression("getDetailsViewLabel", E_STRUCTURAL_FEATURE));
        radio.setCandidatesExpression(AQLUtils.getSelfServiceCallExpression("getEnumCandidates", E_STRUCTURAL_FEATURE));
        radio.setCandidateLabelExpression("aql:candidate.name");
        radio.setValueExpression(AQLUtils.getSelfServiceCallExpression("getEnumValue", E_STRUCTURAL_FEATURE));
        radio.setIsEnabledExpression(AQL_NOT_SELF_IS_READ_ONLY_E_STRUCTURAL_FEATURE);
        ChangeContext setNewValueOperation = ViewFactory.eINSTANCE.createChangeContext();
        setNewValueOperation.setExpression(AQLUtils.getSelfServiceCallExpression("setNewValue", List.of(E_STRUCTURAL_FEATURE, "newValue.instance")));
        radio.getBody().add(setNewValueOperation);
        return radio;
    }

    private WidgetDescription createReferenceWidget() {
        ReferenceWidgetDescription refWidget = ReferenceFactory.eINSTANCE.createReferenceWidgetDescription();
        refWidget.setName("ReferenceWidget");
        refWidget.setLabelExpression(AQLUtils.getSelfServiceCallExpression("getDetailsViewLabel", E_STRUCTURAL_FEATURE));
        refWidget.setReferenceNameExpression(AQLConstants.AQL + E_STRUCTURAL_FEATURE + ".name");
        refWidget.setReferenceOwnerExpression(AQLConstants.AQL_SELF);
        refWidget.setIsEnabledExpression(AQL_NOT_SELF_IS_READ_ONLY_E_STRUCTURAL_FEATURE);
        ChangeContext setRefWidget = ViewFactory.eINSTANCE.createChangeContext();
        setRefWidget.setExpression(AQLUtils.getSelfServiceCallExpression("handleReferenceWidgetNewValue", List.of(E_STRUCTURAL_FEATURE + ".name", ViewFormDescriptionConverter.NEW_VALUE)));
        refWidget.getBody().add(setRefWidget);
        return refWidget;
    }

    private FormElementDescription createDocumentationWidget() {
        TextAreaDescription textarea = FormFactory.eINSTANCE.createTextAreaDescription();
        textarea.setName("DocumentationWidget");
        textarea.setLabelExpression("Documentation");
        textarea.setValueExpression(AQLUtils.getSelfServiceCallExpression("getDocumentation"));
        textarea.setHelpExpression("Use 'shift + enter' to add new lines");
        textarea.setIsEnabledExpression("aql:not(self.isReadOnly())");
        ChangeContext setNewValueOperation = ViewFactory.eINSTANCE.createChangeContext();
        setNewValueOperation.setExpression(AQLUtils.getSelfServiceCallExpression("setNewDocumentationValue", ViewFormDescriptionConverter.NEW_VALUE));
        textarea.getBody().add(setNewValueOperation);
        FormElementIf precondition = FormFactory.eINSTANCE.createFormElementIf();
        precondition.getChildren().add(textarea);
        precondition.setName("DocumentationWidget_Precondition");
        precondition.setPredicateExpression("aql:not(self.oclIsKindOf(sysml::Documentation))");
        return precondition;
    }
}
