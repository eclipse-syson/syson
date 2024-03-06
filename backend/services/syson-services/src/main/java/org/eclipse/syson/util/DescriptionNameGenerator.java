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
package org.eclipse.syson.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * Name generator for all SysON description providers.
 * 
 * @author arichard
 */
public class DescriptionNameGenerator {

    private static final Pattern WORD_FINDER = Pattern.compile("(([A-Z]?[a-z]+)|([A-Z]))");

    protected static String getName(String prefix, String descType, String type) {
        StringBuilder name = new StringBuilder();
        name.append(prefix)
            .append(" ")
            .append(descType)
            .append(" ")
            .append(type);
        return name.toString();
    }

    protected static String getNodeName(String prefix, String type) {
        return getName(prefix, "Node", type);
    }

    protected static String getCompartmentName(String prefix, String type) {
        return getName(prefix, "Compartment", type);
    }

    protected static String getCompartmentItemName(String prefix, String type) {
        return getName(prefix, "CompartmentItem", type);
    }

    protected static String getEdgeName(String prefix, String type) {
        return getName(prefix, "Edge", type);
    }

    public static String getCreationToolName(EClassifier eClassifier) {
        return getCreationToolName("New ", eClassifier);
    }

    public static String getCreationToolName(String prefix, EClassifier eClassifier) {
        String nameToParse = eClassifier.getName();
        if (eClassifier.getName().endsWith("Usage")) {
            String baseClassifierName = eClassifier.getName().substring(0, eClassifier.getName().length() - 5);
            EClassifier definitionEClassifier = SysmlPackage.eINSTANCE.getEClassifier(baseClassifierName + "Definition");
            if (definitionEClassifier != null) {
                nameToParse = baseClassifierName;
            }
        }
        return prefix + findWordsInMixedCase(nameToParse).stream().collect(Collectors.joining(" "));
    }

    private static List<String> findWordsInMixedCase(String text) {
        Matcher matcher = WORD_FINDER.matcher(text);
        List<String> words = new ArrayList<>();
        while (matcher.find()) {
            words.add(matcher.group(0));
        }
        return words;
    }
}
