/*******************************************************************************
 * Copyright (c) 2023 Obeo.
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

import org.eclipse.emf.common.util.EList;
import org.eclipse.syson.sysml.AttributeDefinition;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.Classifier;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.EnumerationDefinition;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.InterfaceDefinition;
import org.eclipse.syson.sysml.InterfaceUsage;
import org.eclipse.syson.sysml.ItemDefinition;
import org.eclipse.syson.sysml.ItemUsage;
import org.eclipse.syson.sysml.MetadataDefinition;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.PortDefinition;
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.Subclassification;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.util.SysmlSwitch;
import org.eclipse.syson.util.LabelConstants;

/**
 * Switch returning the label of some container nodes in the diagrams.
 *
 * @author arichard
 */
public class MultiLineLabelSwitch extends SysmlSwitch<String> {

    @Override
    public String caseElement(Element object) {
        String declaredName = object.getDeclaredName();
        if (declaredName == null) {
            return "";
        }
        return declaredName;
    }

    @Override
    public String caseAttributeDefinition(AttributeDefinition object) {
        StringBuilder label = new StringBuilder();
        label
            .append(this.abstractType(object))
            .append(LabelConstants.OPEN_QUOTE)
            .append("attribute def")
            .append(LabelConstants.CLOSE_QUOTE)
            .append(LabelConstants.CR)
            .append(this.caseElement(object))
            .append(this.subclassification(object));
        return label.toString();
    }

    @Override
    public String caseAttributeUsage(AttributeUsage object) {
        StringBuilder label = new StringBuilder();
        label
            .append(this.abstractType(object))
            .append(LabelConstants.OPEN_QUOTE)
            .append("attribute")
            .append(LabelConstants.CLOSE_QUOTE)
            .append(LabelConstants.CR)
            .append(this.caseElement(object))
            .append(this.featureTyping(object))
            .append(this.redefinition(object));
        return label.toString();
    }

    @Override
    public String caseEnumerationDefinition(EnumerationDefinition object) {
        StringBuilder label = new StringBuilder();
        label
            .append(this.abstractType(object))
            .append(LabelConstants.OPEN_QUOTE)
            .append("enumeration def")
            .append(LabelConstants.CLOSE_QUOTE)
            .append(LabelConstants.CR)
            .append(this.caseElement(object))
            .append(this.subclassification(object));
        return label.toString();
    }

    @Override
    public String caseInterfaceDefinition(InterfaceDefinition object) {
        StringBuilder label = new StringBuilder();
        label
            .append(this.abstractType(object))
            .append(LabelConstants.OPEN_QUOTE)
            .append("interface def")
            .append(LabelConstants.CLOSE_QUOTE)
            .append(LabelConstants.CR)
            .append(this.caseElement(object))
            .append(this.subclassification(object));
        return label.toString();
    }

    @Override
    public String caseInterfaceUsage(InterfaceUsage object) {
        StringBuilder label = new StringBuilder();
        label
            .append(this.abstractType(object))
            .append(LabelConstants.OPEN_QUOTE)
            .append("interface")
            .append(LabelConstants.CLOSE_QUOTE)
            .append(LabelConstants.CR)
            .append(this.caseElement(object))
            .append(this.featureTyping(object))
            .append(this.redefinition(object));
        return label.toString();
    }

    @Override
    public String caseItemDefinition(ItemDefinition object) {
        StringBuilder label = new StringBuilder();
        label
            .append(this.abstractType(object))
            .append(LabelConstants.OPEN_QUOTE)
            .append("item def")
            .append(LabelConstants.CLOSE_QUOTE)
            .append(LabelConstants.CR)
            .append(this.caseElement(object))
            .append(this.subclassification(object));
        return label.toString();
    }

    @Override
    public String caseItemUsage(ItemUsage object) {
        StringBuilder label = new StringBuilder();
        label
            .append(this.abstractType(object))
            .append(LabelConstants.OPEN_QUOTE)
            .append("item")
            .append(LabelConstants.CLOSE_QUOTE)
            .append(LabelConstants.CR)
            .append(this.caseElement(object))
            .append(this.featureTyping(object))
            .append(this.redefinition(object));
        return label.toString();
    }

    @Override
    public String caseMetadataDefinition(MetadataDefinition object) {
        StringBuilder label = new StringBuilder();
        label
            .append(this.abstractType(object))
            .append(LabelConstants.OPEN_QUOTE)
            .append("metadata def")
            .append(LabelConstants.CLOSE_QUOTE)
            .append(LabelConstants.CR)
            .append(this.caseElement(object))
            .append(this.subclassification(object));
        return label.toString();
    }

    @Override
    public String casePackage(Package object) {
        StringBuilder label = new StringBuilder();
        label
            .append(LabelConstants.OPEN_QUOTE)
            .append("package")
            .append(LabelConstants.CLOSE_QUOTE)
            .append(LabelConstants.CR)
            .append(this.caseElement(object));
        return label.toString();
    }

    @Override
    public String casePartDefinition(PartDefinition object) {
        StringBuilder label = new StringBuilder();
        label
            .append(this.abstractType(object))
            .append(LabelConstants.OPEN_QUOTE)
            .append("part def")
            .append(LabelConstants.CLOSE_QUOTE)
            .append(LabelConstants.CR)
            .append(this.caseElement(object))
            .append(this.subclassification(object));
        return label.toString();
    }

    @Override
    public String casePartUsage(PartUsage object) {
        StringBuilder label = new StringBuilder();
        label
            .append(this.abstractType(object))
            .append(LabelConstants.OPEN_QUOTE)
            .append("part")
            .append(LabelConstants.CLOSE_QUOTE)
            .append(LabelConstants.CR)
            .append(this.caseElement(object))
            .append(this.featureTyping(object))
            .append(this.redefinition(object));
        return label.toString();
    }

    @Override
    public String casePortDefinition(PortDefinition object) {
        StringBuilder label = new StringBuilder();
        label
            .append(this.abstractType(object))
            .append(LabelConstants.OPEN_QUOTE)
            .append("port def")
            .append(LabelConstants.CLOSE_QUOTE)
            .append(LabelConstants.CR)
            .append(this.caseElement(object))
            .append(this.subclassification(object));
        return label.toString();
    }

    @Override
    public String casePortUsage(PortUsage object) {
        StringBuilder label = new StringBuilder();
        label
            .append(this.abstractType(object))
            .append(LabelConstants.OPEN_QUOTE)
            .append("port")
            .append(LabelConstants.CLOSE_QUOTE)
            .append(LabelConstants.CR)
            .append(this.caseElement(object))
            .append(this.featureTyping(object))
            .append(this.redefinition(object));
        return label.toString();
    }

    private String abstractType(Type type) {
        StringBuilder label = new StringBuilder();
        if (type.isIsAbstract()) {
            label
                .append(LabelConstants.OPEN_QUOTE)
                .append("abstract")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR);
        }
        return label.toString();
    }

    private String featureTyping(Usage usage) {
        StringBuilder label = new StringBuilder();
        EList<Classifier> definitions = usage.getDefinition();
        if (!definitions.isEmpty()) {
            Classifier definition = definitions.get(0);
            label
                .append(LabelConstants.SPACE)
                .append(LabelConstants.COLON)
                .append(LabelConstants.SPACE)
                .append(definition.getDeclaredName());
        }
        return label.toString();
    }

    private String redefinition(Usage usage) {
        StringBuilder label = new StringBuilder();
        EList<Redefinition> redefinitions = usage.getOwnedRedefinition();
        if (!redefinitions.isEmpty()) {
            Redefinition redefinition = redefinitions.get(0);
            Feature redefinedFeature = redefinition.getRedefinedFeature();
            String redefinedFeatureName = null;
            if (redefinedFeature != null) {
                redefinedFeatureName = redefinedFeature.getDeclaredName();
            }
            label
                .append(LabelConstants.SPACE)
                .append(LabelConstants.REDEFINITION)
                .append(LabelConstants.SPACE)
                .append(redefinedFeatureName);
        }
        return label.toString();
    }

    private String subclassification(Definition definition) {
        StringBuilder label = new StringBuilder();
        EList<Subclassification> subclassifications = definition.getOwnedSubclassification();
        if (!subclassifications.isEmpty()) {
            Subclassification subclassification = subclassifications.get(0);
            Classifier superclassifier = subclassification.getSuperclassifier();
            String superclassifierName = null;
            if (superclassifier != null) {
                superclassifierName = superclassifier.getDeclaredName();
            }
            label
                .append(LabelConstants.SPACE)
                .append(LabelConstants.SUBCLASSIFICATION)
                .append(LabelConstants.SPACE)
                .append(superclassifierName);
        }
        return label.toString();
    }
}
