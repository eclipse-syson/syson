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
package org.eclipse.syson.util;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.diagrams.description.NodeDescription;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * Name generator for all SysON description providers.
 *
 * @author arichard
 */
public class DescriptionNameGenerator implements IDescriptionNameGenerator {

    protected static final String SPACE = " ";

    private static final Pattern WORD_FINDER = Pattern.compile("(([A-Z]?[a-z]+)|([A-Z]))");

    private final String diagramPrefix;

    public DescriptionNameGenerator(String diagramPrefix) {
        this.diagramPrefix = diagramPrefix;
    }

    protected String getName(String prefix, String descType, String type) {
        StringBuilder name = new StringBuilder();
        name.append(prefix).append(SPACE).append(descType).append(SPACE).append(type);
        return name.toString();
    }

    protected String getNodeName(String prefix, String type) {
        return this.getName(prefix, "Node", type);
    }

    protected String getBorderNodeName(String prefix, String type) {
        return this.getName(prefix, "BorderNode", type);
    }

    protected String getInheritedBorderNodeName(String prefix, String type) {
        return this.getName(prefix, "InheritedBorderNode", type);
    }

    protected String getCompartmentName(String prefix, String suffix) {
        return this.getName(prefix, "Compartment", suffix);
    }

    protected String getCompartmentItemName(String prefix, String suffix) {
        return this.getName(prefix, "CompartmentItem", suffix);
    }

    protected String getInheritedCompartmentItemName(String prefix, String suffix) {
        return this.getName(prefix, "InheritedCompartmentItem", suffix);
    }

    protected String getEdgeName(String prefix, String type) {
        return this.getName(prefix, "Edge", type);
    }

    /**
     * Returns the name of the creation tool of the given {@link EClassifier} with a specified prefix.
     *
     * @param pattern
     *            the pattern string use to form the name of the tool, see {@link MessageFormat}.<br>
     *            If the pattern does not contain {0}, it is just prepend to the name of the given {@link EClassifier}.
     * @param eClassifier
     *            the {@link EClassifier} the creation tool is in charge of.
     * @return a string starting with the given prefix and followed by the name of the given {@link EClassifier}.<br>
     *         If the given classifier is a usage, the word "Usage" is removed from the name of the classifier.
     */
    @Override
    public String getCreationToolName(String pattern, EClassifier eClassifier) {
        String nameToParse = eClassifier.getName();
        if (eClassifier instanceof EClass eClass) {
            if (SysmlPackage.eINSTANCE.getUsage().isSuperTypeOf(eClass)
                    && !SysmlPackage.eINSTANCE.getConnectorAsUsage().equals(eClass)
                    && !SysmlPackage.eINSTANCE.getBindingConnectorAsUsage().equals(eClass)
                    && !SysmlPackage.eINSTANCE.getSuccessionAsUsage().equals(eClass)) {
                if (eClass.getName().endsWith("Usage")) {
                    nameToParse = eClass.getName().substring(0, eClass.getName().length() - 5);
                }
            }
        }
        if (!pattern.contains("{0}")) {
            return pattern + this.findWordsInMixedCase(nameToParse).stream().collect(Collectors.joining(SPACE));
        } else {
            return MessageFormat.format(pattern, this.findWordsInMixedCase(nameToParse).stream().collect(Collectors.joining(SPACE)));
        }
    }

    private List<String> findWordsInMixedCase(String text) {
        Matcher matcher = WORD_FINDER.matcher(text);
        List<String> words = new ArrayList<>();
        while (matcher.find()) {
            words.add(matcher.group(0));
        }
        return words;
    }

    /**
     * Returns the name of a creation tool of the given {@link EClassifier}.
     *
     * @param eClassifier
     *            the {@link EClassifier} the creation tool is in charge of.
     * @return a string starting with the word {@code NEW} and followed by the name of the given {@link EClassifier}.
     */
    @Override
    public String getCreationToolName(EClassifier eClassifier) {
        return this.getCreationToolName("New ", eClassifier);
    }

    /**
     * Returns the name of a {@link NodeDescription} starting with the diagram prefix and followed by the given string.
     *
     * @param type
     *            a string to form the name of the node description.
     * @return a string starting with the diagram prefix and followed by the given string.
     */
    @Override
    public String getNodeName(String type) {
        return this.getNodeName(this.getDiagramPrefix(), type);
    }

    /**
     * Returns the name of a {@link NodeDescription} starting with the diagram prefix and followed by the name of the
     * given {@link EClass}.
     *
     * @param eClass
     *            the {@link EClass} used to compute the name of the {@link NodeDescription}.
     * @return a string starting with the diagram prefix and followed by the name of the given {@link EClass}
     */
    @Override
    public String getNodeName(EClass eClass) {
        return this.getNodeName(this.getDiagramPrefix(), eClass.getName());
    }

    /**
     * Returns the name of a border {@link NodeDescription} starting with the diagram prefix and followed by the given
     * string.
     *
     * @param type
     *            a string to form the name of the border node description.
     * @return a string starting with the diagram prefix and followed by the given string.
     */
    @Override
    public String getBorderNodeName(String type) {
        return this.getBorderNodeName(this.getDiagramPrefix(), type);
    }

    /**
     * Returns the name of a border {@link NodeDescription} starting with the diagram prefix and followed by the name of
     * the given {@link EClass}.
     *
     * @param eClass
     *            the {@link EClass} used to compute the name of the border {@link NodeDescription}.
     * @return a string starting with the diagram prefix and followed by the name of the given {@link EClass}
     */
    @Override
    public String getBorderNodeName(EClass eClass) {
        return this.getBorderNodeName(this.getDiagramPrefix(), eClass.getName());
    }

    /**
     * Returns the name of an inherited border {@link NodeDescription} starting with the diagram prefix and followed by the name of
     * the given {@link EClass}.
     *
     * @param eClass
     *            the {@link EClass} used to compute the name of the border {@link NodeDescription}.
     * @return a string starting with the diagram prefix and followed by the name of the given {@link EClass}
     */
    @Override
    public String getInheritedBorderNodeName(EClass eClass) {
        return this.getInheritedBorderNodeName(this.getDiagramPrefix(), eClass.getName());
    }

    /**
     * Returns the name of a compartment {@link NodeDescription} starting with the diagram prefix, followed by the name
     * of the given {@link EClass} and the name of the given {@link EReference}.
     *
     * @param eClass
     *            the {@link EClass} used to compute the name of the {@link NodeDescription}.
     * @param eReference
     *            the {@link EReference} that the compartment is containing.
     * @return a string starting with the diagram prefix, followed by the name of the given {@link EClass} and the name
     *         of the given {@link EReference}
     */
    @Override
    public String getCompartmentName(EClass eClass, EReference eReference) {
        return this.getCompartmentName(this.getDiagramPrefix(), eClass.getName() + SPACE + eReference.getName());
    }

    /**
     * Returns the name of a free form compartment {@link NodeDescription} using the given {@link EClass} and
     * {@link EReference}.
     *
     * @param eClass
     *            the {@link EClass} used to compute the name of the {@link NodeDescription}.
     * @param eReference
     *            the {@link EReference} that the compartment is containing.
     *
     * @return a string used to name a compartment {@link NodeDescription}.
     */
    @Override
    public String getFreeFormCompartmentName(EClass eClass, EReference eReference) {
        return this.getCompartmentName(this.getDiagramPrefix(), eClass.getName() + SPACE + eReference.getName() + " FreeForm");
    }

    /**
     * Returns the name of a free form compartment {@link NodeDescription} based on the given name.
     *
     * @param name
     *            the name used to compute the name of the {@link NodeDescription}.
     *
     * @return a string used to name a compartment {@link NodeDescription}.
     */
    @Override
    public String getFreeFormCompartmentName(String name) {
        return this.getCompartmentName(this.getDiagramPrefix(), name + " FreeForm");
    }

    /**
     * Returns the name of a compartment items {@link NodeDescription} starting with the diagram prefix, followed by the
     * name of the given {@link EClass} and the name of the given {@link EReference}.
     *
     * @param eClass
     *            the {@link EClass} used to compute the name of the {@link NodeDescription}.
     * @param eReference
     *            the {@link EReference} that the compartment is containing.
     * @return a string starting with the diagram prefix, followed by the name of the given {@link EClass} and the name
     *         of the given {@link EReference}
     */
    @Override
    public String getCompartmentItemName(EClass eClass, EReference eReference) {
        return this.getCompartmentItemName(this.getDiagramPrefix(), eClass.getName() + SPACE + eReference.getName());
    }

    /**
     * Returns the name of the inherited compartment item {@link NodeDescription} starting with the diagram prefix,
     * followed by the name of the given {@link EClass} and the name of the given {@link EReference}.
     *
     * @param eClass
     *            the {@link EClass} used to compute the name of the {@link NodeDescription}.
     * @param eReference
     *            the {@link EReference} that the compartment is containing.
     * @return a string starting with the diagram prefix, followed by the name of the given {@link EClass} and the name
     *         of the given {@link EReference}
     */
    @Override
    public String getInheritedCompartmentItemName(EClass eClass, EReference eReference) {
        return this.getInheritedCompartmentItemName(this.getDiagramPrefix(), eClass.getName() + SPACE + eReference.getName());
    }

    /**
     * Returns the name of a domain based {@link EdgeDescription} starting with the diagram prefix and followed by the
     * given the given {@link EClass}.
     *
     * @param eClass
     *            the {@link EClass} used to compute the name of the domain based {@link NodeDescription}.
     * @return a string starting with the diagram prefix and followed by the name of the given {@link EClass}.
     */
    @Override
    public String getEdgeName(EClass eClass) {
        return this.getEdgeName(this.getDiagramPrefix(), eClass.getName());
    }

    /**
     * Returns the name of an {@link EdgeDescription} starting with the diagram prefix and followed by the given edge
     * type.
     *
     * @param type
     *            a string representing the name of the edge.
     * @return a string starting with the diagram prefix and followed by the given edge type.
     */
    @Override
    public String getEdgeName(String type) {
        return this.getEdgeName(this.getDiagramPrefix(), type);
    }

    @Override
    public String getDiagramPrefix() {
        return this.diagramPrefix;
    }
}
