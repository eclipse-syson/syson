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
package org.eclipse.syson.services;

import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Dependency;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.EnumerationDefinition;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.Specialization;
import org.eclipse.syson.sysml.Subclassification;
import org.eclipse.syson.sysml.Subsetting;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.util.SysmlSwitch;

/**
 * Switch called when a new element is created. Allows to set various attributes/references.
 *
 * @author arichard
 */
public class ElementInitializerSwitch extends SysmlSwitch<Element> {

    @Override
    public Element caseElement(Element object) {
        return object;
    }

    @Override
    public Element caseDependency(Dependency object) {
        object.setDeclaredName("dependency");
        return object;
    }

    @Override
    public Element caseDefinition(Definition object) {
        object.setDeclaredName(object.eClass().getName());
        return object;
    }

    @Override
    public Element caseEnumerationDefinition(EnumerationDefinition object) {
        object.setIsVariation(true);
        return object;
    }

    @Override
    public Element caseFeatureTyping(FeatureTyping object) {
        object.setDeclaredName("typed by");
        return object;
    }

    @Override
    public Element caseRedefinition(Redefinition object) {
        object.setDeclaredName("redefines");
        return object;
    }

    @Override
    public Element caseSpecialization(Specialization object) {
        object.setDeclaredName("specializes");
        return object;
    }

    @Override
    public Element caseSubclassification(Subclassification object) {
        object.setDeclaredName("specializes");
        return object;
    }

    @Override
    public Element caseSubsetting(Subsetting object) {
        object.setDeclaredName("subsets");
        return object;
    }

    @Override
    public Element caseUsage(Usage object) {
        char[] charArray = object.eClass().getName().toCharArray();
        charArray[0] = Character.toLowerCase(charArray[0]);
        String defaultName = new String(charArray);
        if (defaultName.endsWith("Usage")) {
            defaultName = defaultName.substring(0, defaultName.length() - 5);
        }
        object.setDeclaredName(defaultName);
        return object;
    }
}
