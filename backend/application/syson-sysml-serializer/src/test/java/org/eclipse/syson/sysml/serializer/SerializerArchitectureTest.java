/*******************************************************************************
 * Copyright (c) 2026 Obeo.
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
package org.eclipse.syson.sysml.serializer;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.eclipse.syson.sysml.serializer.model.ElementSyntaxQueries;
import org.eclipse.syson.sysml.serializer.output.ExpressionAndReferenceWriter;
import org.junit.jupiter.api.Test;

/**
 * Keeps rendering implementation package dependencies acyclic and the token vocabulary independent.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
class SerializerArchitectureTest {

    private static final String SERIALIZER_PACKAGE = "org.eclipse.syson.sysml.serializer";

    // Shared API and support types are outside the rendering implementation graph.
    // These packages also contain the four rendering components listed below.
    private static final Set<String> SUPPORT_PACKAGES = Set.of(SERIALIZER_PACKAGE,
            SERIALIZER_PACKAGE + ".model", SERIALIZER_PACKAGE + ".names", SERIALIZER_PACKAGE + ".output");

    private static final Set<String> RENDERING_SUPPORT_TYPES = Set.of(
            ElementDispatch.class.getName(), SerializationContext.class.getName(),
            ElementSyntaxQueries.class.getName(), ExpressionAndReferenceWriter.class.getName());

    private static final Pattern PACKAGE = Pattern.compile("(?m)^package ([\\w.]+);");

    private static final Pattern IMPORT = Pattern.compile("(?m)^import (?:static )?([\\w.]+);");

    /**
     * SysMLTokens.java must not contain any imports.
     */
    @Test
    void tokenVocabularyDoesNotImportOtherTypes() throws IOException {
        final Path vocabulary = Path.of("src/main/java",
                SERIALIZER_PACKAGE.replace('.', '/'), "tokens/SysMLTokens.java");
        assertFalse(IMPORT.matcher(Files.readString(vocabulary)).find(),
                "The token vocabulary must remain independent of other types");
    }

    /**
     * Imports between rendering implementation packages must not form cycles. Shared API, naming, model-navigation,
     * and output-support types are outside this graph.
     */
    @Test
    void renderingPackagesHaveNoDependencyCycles() throws IOException {
        final Map<String, Set<String>> dependencies = this.readPackageDependencies();
        final Set<String> visitedPackages = new HashSet<>();
        for (final String packageName : dependencies.keySet()) {
            this.assertAcyclicFrom(packageName, dependencies, visitedPackages, new ArrayList<>());
        }
    }

    private Map<String, Set<String>> readPackageDependencies() throws IOException {
        final Path sources = Path.of("src/main/java", SERIALIZER_PACKAGE.replace('.', '/'));
        final Map<String, Set<String>> dependencies = new HashMap<>();
        final Map<String, String> packageByType = new HashMap<>();
        final Map<String, List<String>> importsByPackage = new HashMap<>();
        try (var files = Files.walk(sources)) {
            for (final Path file : files.filter(path -> path.toString().endsWith(".java")).toList()) {
                final String source = Files.readString(file);
                final String packageName = PACKAGE.matcher(source).results().findFirst().orElseThrow().group(1);
                final String typeName = file.getFileName().toString().replace(".java", "");
                final String qualifiedTypeName = packageName + "." + typeName;
                if (!this.participatesInRenderingGraph(packageName, qualifiedTypeName)) {
                    continue;
                }
                packageByType.put(qualifiedTypeName, packageName);
                dependencies.computeIfAbsent(packageName, key -> new HashSet<>());
                importsByPackage.computeIfAbsent(packageName, key -> new ArrayList<>())
                        .addAll(IMPORT.matcher(source).results().map(match -> match.group(1)).toList());
            }
        }
        for (final var entry : importsByPackage.entrySet()) {
            for (final String importedType : entry.getValue()) {
                this.addDependency(entry.getKey(), importedType, packageByType, dependencies);
            }
        }
        return dependencies;
    }

    private boolean participatesInRenderingGraph(final String packageName, final String qualifiedTypeName) {
        return !SUPPORT_PACKAGES.contains(packageName) || RENDERING_SUPPORT_TYPES.contains(qualifiedTypeName);
    }

    private void addDependency(final String packageName, final String importedType,
            final Map<String, String> packageByType, final Map<String, Set<String>> dependencies) {
        // Nested-type and static-member imports resolve through their enclosing source type.
        String type = importedType;
        while (type.startsWith(SERIALIZER_PACKAGE) && !packageByType.containsKey(type)) {
            type = type.substring(0, type.lastIndexOf('.'));
        }
        final String dependency = packageByType.get(type);
        if (dependency != null && !packageName.equals(dependency)) {
            dependencies.get(packageName).add(dependency);
        }
    }

    private void assertAcyclicFrom(final String packageName, final Map<String, Set<String>> dependencies,
            final Set<String> visitedPackages, final List<String> activePackagePath) {
        assertFalse(activePackagePath.contains(packageName), () -> "Package dependency cycle: " + activePackagePath + " -> " + packageName);
        if (visitedPackages.add(packageName)) {
            activePackagePath.add(packageName);
            for (final String dependency : dependencies.get(packageName)) {
                this.assertAcyclicFrom(dependency, dependencies, visitedPackages, activePackagePath);
            }
            activePackagePath.removeLast();
        }
    }
}
