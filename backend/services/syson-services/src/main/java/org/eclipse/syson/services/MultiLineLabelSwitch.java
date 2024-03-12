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
import org.eclipse.syson.sysml.ConstraintDefinition;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.EnumerationDefinition;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.InterfaceDefinition;
import org.eclipse.syson.sysml.InterfaceUsage;
import org.eclipse.syson.sysml.ItemDefinition;
import org.eclipse.syson.sysml.ItemUsage;
import org.eclipse.syson.sysml.LiteralBoolean;
import org.eclipse.syson.sysml.LiteralExpression;
import org.eclipse.syson.sysml.LiteralInfinity;
import org.eclipse.syson.sysml.LiteralInteger;
import org.eclipse.syson.sysml.LiteralRational;
import org.eclipse.syson.sysml.LiteralString;
import org.eclipse.syson.sysml.MetadataDefinition;
import org.eclipse.syson.sysml.MultiplicityRange;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.PortDefinition;
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.Subclassification;
import org.eclipse.syson.sysml.Subsetting;
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
            .append(this.multiplicityRange(object))
            .append(this.featureTyping(object))
            .append(this.redefinition(object))
            .append(this.subsetting(object));
        return label.toString();
    }
    
    @Override
    public String caseConstraintDefinition(ConstraintDefinition object) {
        StringBuilder label = new StringBuilder();
        label
            .append(this.abstractType(object))
            .append(LabelConstants.OPEN_QUOTE)
            .append("constraint def")
            .append(LabelConstants.CLOSE_QUOTE)
            .append(LabelConstants.CR)
            .append(this.caseElement(object))
            .append(this.subclassification(object));
        return label.toString();
    }
    
    @Override
    public String caseConstraintUsage(ConstraintUsage object) {
        StringBuilder label = new StringBuilder();
        label
            .append(this.abstractType(object))
            .append(LabelConstants.OPEN_QUOTE)
            .append("constraint")
            .append(LabelConstants.CLOSE_QUOTE)
            .append(LabelConstants.CR)
            .append(this.caseElement(object))
            .append(this.multiplicityRange(object))
            .append(this.featureTyping(object))
            .append(this.redefinition(object))
            .append(this.subsetting(object));
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
            .append(this.multiplicityRange(object))
            .append(this.featureTyping(object))
            .append(this.redefinition(object))
            .append(this.subsetting(object));
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
            .append(this.multiplicityRange(object))
            .append(this.featureTyping(object))
            .append(this.redefinition(object))
            .append(this.subsetting(object));
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
            .append(this.multiplicityRange(object))
            .append(this.featureTyping(object))
            .append(this.redefinition(object))
            .append(this.subsetting(object));
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
            .append(this.multiplicityRange(object))
            .append(this.featureTyping(object))
            .append(this.featureTyping(object))
            .append(this.redefinition(object))
            .append(this.subsetting(object));
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

    private String multiplicityRange(Usage usage) {
        StringBuilder label = new StringBuilder();
        var optMultiplicityRange = usage.getOwnedRelationship().stream()
                .filter(OwningMembership.class::isInstance)
                .map(OwningMembership.class::cast)
                .flatMap(m -> m.getOwnedRelatedElement().stream())
                .filter(MultiplicityRange.class::isInstance)
                .map(MultiplicityRange.class::cast)
                .findFirst();
        if (optMultiplicityRange.isPresent()) {
            var range = optMultiplicityRange.get();
            String firstBound = null;
            String secondBound = null;
            var bounds = range.getOwnedRelationship().stream()
                    .filter(OwningMembership.class::isInstance)
                    .map(OwningMembership.class::cast)
                    .flatMap(m -> m.getOwnedRelatedElement().stream())
                    .filter(LiteralExpression.class::isInstance)
                    .map(LiteralExpression.class::cast)
                    .toList();
            if (bounds.size() == 1) {
                firstBound = this.getValue(bounds.get(0));
            } else if (bounds.size() == 2) {
                firstBound = this.getValue(bounds.get(0));
                secondBound = this.getValue(bounds.get(1));
            }
            label.append(LabelConstants.OPEN_BRACKET);
            if (firstBound != null) {
                label.append(firstBound);
            }
            if (secondBound != null) {
                label.append("..");
                label.append(secondBound);
            }
            label.append(LabelConstants.CLOSE_BRACKET);
            label.append(LabelConstants.SPACE);
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

    private String subsetting(Element element) {
        StringBuilder label = new StringBuilder();
        var subsetting = element.getOwnedRelationship().stream()
                .filter(r -> r instanceof Subsetting && !(r instanceof Redefinition))
                .map(Subsetting.class::cast)
                .findFirst();
        if (subsetting.isPresent()) {
            var subsettedFeature = subsetting.get().getSubsettedFeature();
            String subsettedFeatureName = null;
            if (subsettedFeature != null) {
                subsettedFeatureName = subsettedFeature.getDeclaredName();
            }
            label.append(LabelConstants.SPACE)
                .append(LabelConstants.SUBSETTING)
                .append(LabelConstants.SPACE)
                .append(subsettedFeatureName);
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

    private String getValue(Expression literalExpression) {
        String value = null;
        if (literalExpression instanceof LiteralInteger literal) {
            value = String.valueOf(literal.getValue());
        } else if (literalExpression instanceof LiteralRational literal) {
            value = String.valueOf(literal.getValue());
        } else if (literalExpression instanceof LiteralBoolean literal) {
            value = String.valueOf(literal.isValue());
        } else if (literalExpression instanceof LiteralString literal) {
            value = String.valueOf(literal.getValue());
        } else if (literalExpression instanceof LiteralInfinity literal) {
            value = "*";
        }
        return value;
    }
}
