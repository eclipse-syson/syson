/*******************************************************************************
 * Copyright (c) 2024, 2026 Obeo.
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
import org.eclipse.syson.sysml.ConstraintDefinition;
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
import org.eclipse.syson.sysml.textual.SysMLv2Keywords;
import org.eclipse.syson.sysml.util.SysmlSwitch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Keyword provider for SysML textual representation.
 *
 * @author Arthur Daussy
 */
public class SysMLKeywordSwitch extends SysmlSwitch<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SysMLKeywordSwitch.class);

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
        return SysMLv2Keywords.CONCERN;
    }

    @Override
    public String caseRequirementDefinition(RequirementDefinition object) {
        return SysMLv2Keywords.REQUIREMENT;
    }

    @Override
    public String caseConcernUsage(ConcernUsage object) {
        return SysMLv2Keywords.CONCERN;
    }

    @Override
    public String caseAttributeDefinition(AttributeDefinition object) {
        return SysMLv2Keywords.ATTRIBUTE;
    }

    @Override
    public String caseAttributeUsage(AttributeUsage object) {
        return SysMLv2Keywords.ATTRIBUTE;
    }

    @Override
    public String caseActionDefinition(ActionDefinition object) {
        return SysMLv2Keywords.ACTION;
    }

    @Override
    public String caseActionUsage(ActionUsage object) {
        return SysMLv2Keywords.ACTION;
    }

    @Override
    public String caseConstraintDefinition(ConstraintDefinition object) {
        return SysMLv2Keywords.CONSTRAINT;
    }

    @Override
    public String caseEnumerationUsage(EnumerationUsage object) {
        if (object.getOwningDefinition() instanceof EnumerationDefinition && !this.isNullOrEmpty(object.getName())) {
            // Inside a enumeration definition the keyword enum is optional
            return "";
        }
        return SysMLv2Keywords.ENUM;
    }

    @Override
    public String casePerformActionUsage(PerformActionUsage object) {
        return SysMLv2Keywords.PERFORM + " " + SysMLv2Keywords.ACTION;
    }

    @Override
    public String caseEnumerationDefinition(EnumerationDefinition object) {
        return SysMLv2Keywords.ENUM;
    }

    @Override
    public String caseItemDefinition(ItemDefinition object) {
        return SysMLv2Keywords.ITEM;
    }

    @Override
    public String caseItemUsage(ItemUsage object) {
        return SysMLv2Keywords.ITEM;
    }

    @Override
    public String caseOccurrenceDefinition(OccurrenceDefinition object) {
        return SysMLv2Keywords.OCCURRENCE;
    }

    @Override
    public String caseOccurrenceUsage(OccurrenceUsage object) {
        return SysMLv2Keywords.OCCURRENCE;
    }

    @Override
    public String caseInterfaceDefinition(InterfaceDefinition object) {
        return SysMLv2Keywords.INTERFACE;
    }

    @Override
    public String caseInterfaceUsage(InterfaceUsage object) {
        return SysMLv2Keywords.INTERFACE;
    }

    @Override
    public String casePortUsage(PortUsage object) {
        return SysMLv2Keywords.PORT;
    }

    @Override
    public String casePortDefinition(PortDefinition object) {
        return SysMLv2Keywords.PORT;
    }

    @Override
    public String casePartDefinition(PartDefinition object) {
        return SysMLv2Keywords.PART;
    }

    @Override
    public String casePartUsage(PartUsage object) {
        if (object.getOwningMembership() instanceof ActorMembership) {
            return SysMLv2Keywords.ACTOR;
        }
        return SysMLv2Keywords.PART;
    }

    @Override
    public String caseReferenceUsage(ReferenceUsage object) {
        if (object.getOwningMembership() instanceof SubjectMembership) {
            return SysMLv2Keywords.SUBJECT;
        }
        return "";
    }

    @Override
    public String caseAssertConstraintUsage(AssertConstraintUsage object) {
        if (object.isIsNegated()) {
            return SysMLv2Keywords.ASSERT + " " + SysMLv2Keywords.NOT;
        }
        return SysMLv2Keywords.ASSERT;
    }

    @Override
    public String caseConstraintUsage(ConstraintUsage object) {
        return SysMLv2Keywords.CONSTRAINT;
    }

    @Override
    public String caseRequirementUsage(RequirementUsage object) {
        return SysMLv2Keywords.REQUIREMENT;
    }

    private boolean isNullOrEmpty(String target) {
        return target == null || target.isEmpty();
    }
}
