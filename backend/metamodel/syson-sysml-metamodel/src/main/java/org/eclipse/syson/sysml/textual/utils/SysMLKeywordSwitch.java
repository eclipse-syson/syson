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
package org.eclipse.syson.sysml.textual.utils;


import org.eclipse.emf.ecore.EObject;
import org.eclipse.syson.sysml.ActionDefinition;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.ActorMembership;
import org.eclipse.syson.sysml.AssertConstraintUsage;
import org.eclipse.syson.sysml.AttributeDefinition;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.ConcernDefinition;
import org.eclipse.syson.sysml.ConcernUsage;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.EnumerationDefinition;
import org.eclipse.syson.sysml.EnumerationUsage;
import org.eclipse.syson.sysml.InterfaceDefinition;
import org.eclipse.syson.sysml.InterfaceUsage;
import org.eclipse.syson.sysml.ItemDefinition;
import org.eclipse.syson.sysml.ItemUsage;
import org.eclipse.syson.sysml.OccurrenceDefinition;
import org.eclipse.syson.sysml.OccurrenceUsage;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.PerformActionUsage;
import org.eclipse.syson.sysml.PortDefinition;
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.RequirementDefinition;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.SubjectMembership;
import org.eclipse.syson.sysml.util.SysmlSwitch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Keyword provider for SysML textual representation.
 *
 * @author Arthur Daussy
 */
public class SysMLKeywordSwitch extends SysmlSwitch<String> {

    private static final String CONCERN_KEYWORD = "concern";

    private static final String PERFORM = "perform";

    private static final String ACTION = "action";

    private static final Logger LOGGER = LoggerFactory.getLogger(SysMLKeywordSwitch.class);

    private static final String PART_KEYWORD = "part";

    private static final String PORT_KEYWORD = "port";

    private static final String INTERFACE_KEYWORD = "interface";

    private static final String ITEM_KEYWORD = "item";

    private static final String OCCURRENCE_KEYWORD = "occurrence";

    private static final String ENUM_KEYWORD = "enum";

    private static final String ATTRIBUTE_KEYWORD = "attribute";

    private static final String ACTOR_KEYWORD = "actor";

    private static final String SUBJECT_KEYWORD = "subject";

    private static final String ASSERT_KEYWORD = "assert";

    private static final String NOT_KEYWORD = "not";

    private static final String CONSTRAINT_KEYWORD = "constraint";

    private static final String REQUIREMENT_KEYWORD = "requirement";

    @Override
    public String defaultCase(EObject object) {
        if (object != null) {
            String msg = "Unable to get keyword for " + object.eClass().getName();
            LOGGER.warn(msg);
        }
        return super.defaultCase(object);
    }

    @Override
    public String caseConcernDefinition(ConcernDefinition object) {
        return CONCERN_KEYWORD;
    }

    @Override
    public String caseRequirementDefinition(RequirementDefinition object) {
        return REQUIREMENT_KEYWORD;
    }

    @Override
    public String caseConcernUsage(ConcernUsage object) {
        return CONCERN_KEYWORD;
    }

    @Override
    public String caseAttributeDefinition(AttributeDefinition object) {
        return ATTRIBUTE_KEYWORD;
    }

    @Override
    public String caseAttributeUsage(AttributeUsage object) {
        return ATTRIBUTE_KEYWORD;
    }

    @Override
    public String caseActionDefinition(ActionDefinition object) {
        return ACTION;
    }

    @Override
    public String caseActionUsage(ActionUsage object) {
        return ACTION;
    }

    @Override
    public String caseEnumerationUsage(EnumerationUsage object) {
        if (object.getOwningDefinition() instanceof EnumerationDefinition && !this.isNullOrEmpty(object.getName())) {
            // Inside a enumeration definition the keyword enum is optional
            return "";
        }
        return ENUM_KEYWORD;
    }

    @Override
    public String casePerformActionUsage(PerformActionUsage object) {
        return PERFORM + " " + ACTION;
    }

    @Override
    public String caseEnumerationDefinition(EnumerationDefinition object) {
        return ENUM_KEYWORD;
    }

    @Override
    public String caseItemDefinition(ItemDefinition object) {
        return ITEM_KEYWORD;
    }

    @Override
    public String caseItemUsage(ItemUsage object) {
        return ITEM_KEYWORD;
    }

    @Override
    public String caseOccurrenceDefinition(OccurrenceDefinition object) {
        return OCCURRENCE_KEYWORD;
    }

    @Override
    public String caseOccurrenceUsage(OccurrenceUsage object) {
        return OCCURRENCE_KEYWORD;
    }

    @Override
    public String caseInterfaceDefinition(InterfaceDefinition object) {
        return INTERFACE_KEYWORD;
    }

    @Override
    public String caseInterfaceUsage(InterfaceUsage object) {
        return INTERFACE_KEYWORD;
    }

    @Override
    public String casePortUsage(PortUsage object) {
        return PORT_KEYWORD;
    }

    @Override
    public String casePortDefinition(PortDefinition object) {
        return PORT_KEYWORD;
    }

    @Override
    public String casePartDefinition(PartDefinition object) {
        return PART_KEYWORD;
    }

    @Override
    public String casePartUsage(PartUsage object) {
        if (object.getOwningMembership() instanceof ActorMembership) {
            return ACTOR_KEYWORD;
        }
        return PART_KEYWORD;
    }

    @Override
    public String caseReferenceUsage(ReferenceUsage object) {
        if (object.getOwningMembership() instanceof SubjectMembership) {
            return SUBJECT_KEYWORD;
        }
        return "";
    }

    @Override
    public String caseAssertConstraintUsage(AssertConstraintUsage object) {
        if (object.isIsNegated()) {
            return ASSERT_KEYWORD + " " + NOT_KEYWORD;
        }
        return ASSERT_KEYWORD;
    }

    @Override
    public String caseConstraintUsage(ConstraintUsage object) {
        return CONSTRAINT_KEYWORD;
    }

    @Override
    public String caseRequirementUsage(RequirementUsage object) {
        return REQUIREMENT_KEYWORD;
    }

    private boolean isNullOrEmpty(String target) {
        return target == null || target.isEmpty();
    }
}
