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
package org.eclipse.syson.sysml.linker.tests.util;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Stabilizes the {@code xmi:id}, {@code elementId}, and ID-based cross references of linker test XMI fixtures.
 *
 * @author Arthur Daussy
 */
public final class StableLinkerTestIdsApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(StableLinkerTestIdsApplication.class);

    private static final String MODEL_EXPECTED_SUFFIX = "-model-expected.txt";

    private static final String XMI_ID_ATTRIBUTE = "xmi:id";

    private static final String ELEMENT_ID_ATTRIBUTE = "elementId";

    private static final IdStabilizer ID_STABILIZER = new IdStabilizer();

    private StableLinkerTestIdsApplication() {
        // Prevent instantiation.
    }

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        if (args.length < 1) {
            LOGGER.error("Usage: StableLinkerTestIdsApplication <linker-test-resource-root> [extra-text-root...]");
            return;
        }

        Path root = Path.of(args[0]).toAbsolutePath().normalize();
        List<Path> modelFiles = findModelFiles(root);
        List<Path> rewriteFiles = new ArrayList<>(findRewriteFiles(root));
        for (int index = 1; index < args.length; index++) {
            rewriteFiles.addAll(findJavaFiles(Path.of(args[index]).toAbsolutePath().normalize()));
        }
        Map<String, String> idMapping = new LinkedHashMap<>();

        for (Path modelFile : modelFiles) {
            collectIDMapping(modelFile, idMapping);
        }

        int updatedFiles = 0;
        for (Path rewriteFile : rewriteFiles.stream().distinct().sorted().toList()) {
            if (applyIDMapping(rewriteFile, idMapping)) {
                updatedFiles++;
            }
        }

        System.out.println("Stable ID mappings: " + idMapping.size());
        System.out.println("Updated files: " + updatedFiles);
    }

    private static List<Path> findModelFiles(Path root) throws IOException {
        try (var paths = Files.walk(root)) {
            return paths.filter(Files::isRegularFile)
                    .filter(StableLinkerTestIdsApplication::isModelFile)
                    .sorted()
                    .toList();
        }
    }

    private static boolean isModelFile(Path path) {
        String fileName = path.getFileName().toString();
        return fileName.endsWith(".xmi") || fileName.endsWith(MODEL_EXPECTED_SUFFIX);
    }

    private static List<Path> findRewriteFiles(Path root) throws IOException {
        try (var paths = Files.walk(root)) {
            return paths.filter(Files::isRegularFile)
                    .filter(StableLinkerTestIdsApplication::isRewriteFile)
                    .sorted()
                    .toList();
        }
    }

    private static boolean isRewriteFile(Path path) {
        String fileName = path.getFileName().toString();
        return fileName.endsWith(".xmi") || fileName.endsWith(".txt");
    }

    private static List<Path> findJavaFiles(Path root) throws IOException {
        try (var paths = Files.walk(root)) {
            return paths.filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(".java"))
                    .sorted()
                    .toList();
        }
    }

    private static void collectIDMapping(Path modelFile, Map<String, String> idMapping) throws IOException, ParserConfigurationException, SAXException {
        String stableSysMLPath = ID_STABILIZER.getStableSysMLPath(modelFile);
        Document document = parse(modelFile);
        collectIDMapping(document.getDocumentElement(), "/", stableSysMLPath, idMapping);
    }

    private static Document parse(Path modelFile) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        String content = Files.readString(modelFile, StandardCharsets.UTF_8);
        return factory.newDocumentBuilder().parse(new InputSource(new StringReader(content)));
    }

    private static void collectIDMapping(Element element, String fragment, String stableSysMLPath, Map<String, String> idMapping) {
        String stableID = ID_STABILIZER.getStableID(stableSysMLPath, fragment, getEClassName(element));
        addIDMapping(idMapping, element.getAttribute(XMI_ID_ATTRIBUTE), stableID);
        addIDMapping(idMapping, element.getAttribute(ELEMENT_ID_ATTRIBUTE), stableID);

        Map<String, Integer> indexes = new HashMap<>();
        for (Element child : childElements(element)) {
            String featureName = getLocalName(child);
            int index = indexes.getOrDefault(featureName, 0);
            indexes.put(featureName, index + 1);
            collectIDMapping(child, appendFragment(fragment, featureName, index), stableSysMLPath, idMapping);
        }
    }

    private static void addIDMapping(Map<String, String> idMapping, String oldID, String stableID) {
        if (oldID != null && !oldID.isBlank()) {
            String existingStableID = idMapping.putIfAbsent(oldID, stableID);
            if (existingStableID != null && !existingStableID.equals(stableID)) {
                LOGGER.error("Conflicting stable ID mapping for {}: {} / {}", oldID, existingStableID, stableID);
                return;
            }
        }
    }

    private static List<Element> childElements(Element element) {
        List<Element> result = new ArrayList<>();

        for (Node child = element.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child instanceof Element childElement) {
                result.add(childElement);
            }
        }

        return result;
    }

    private static String appendFragment(String parentFragment, String featureName, int index) {
        String segment = "@" + featureName + "." + index;
        if ("/".equals(parentFragment)) {
            return "/" + segment;
        }
        return parentFragment + "/" + segment;
    }

    private static String getEClassName(Element element) {
        String type = element.getAttributeNS(XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI, "type");
        if (type == null || type.isBlank()) {
            type = element.getAttribute("xsi:type");
        }

        String result;
        if (type != null && !type.isBlank()) {
            result = stripPrefix(type);
        } else {
            result = getLocalName(element);
        }

        return result;
    }

    private static String getLocalName(Element element) {
        String localName = element.getLocalName();
        if (localName == null || localName.isBlank()) {
            localName = stripPrefix(element.getNodeName());
        }
        return localName;
    }

    private static String stripPrefix(String value) {
        int separatorIndex = value.indexOf(':');
        if (separatorIndex >= 0) {
            return value.substring(separatorIndex + 1);
        }
        return value;
    }

    private static boolean applyIDMapping(Path modelFile, Map<String, String> idMapping) throws IOException {
        String content = Files.readString(modelFile, StandardCharsets.UTF_8);
        String updatedContent = content;
        for (Map.Entry<String, String> entry : idMapping.entrySet()) {
            updatedContent = updatedContent.replace(entry.getKey(), entry.getValue());
        }
        if (!content.equals(updatedContent)) {
            Files.writeString(modelFile, updatedContent, StandardCharsets.UTF_8);
            return true;
        }
        return false;
    }
}
