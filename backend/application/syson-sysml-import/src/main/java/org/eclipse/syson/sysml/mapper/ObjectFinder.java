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
package org.eclipse.syson.sysml.mapper;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Pattern;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.syson.sysml.AstConstant;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.LogBook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Facilitates the discovery and retrieval of EObjects based on identifiers or patterns.
 *
 * @author gescande
 */
public class ObjectFinder {

    private final Logger logger = LoggerFactory.getLogger(ObjectFinder.class);

    private final SortedMap<String, List<EObject>> objectList = Collections.synchronizedSortedMap(new TreeMap<>());

    private final SortedMap<String, List<EObject>> objectListShortName = Collections.synchronizedSortedMap(new TreeMap<>());

    private final List<String> importRegexp = Collections.synchronizedList(new ArrayList<>());

    public void addImportMember(final String importString) {
        this.logger.debug("addImportMember " + importString);
        this.importRegexp.add(importString);
    }

    public void addImportNamespace(final String importString) {
        this.logger.debug("addImportNamespace " + importString);
        this.importRegexp.add(importString + "::(.*::)*");
    }

    public void putEObject(final EObject eObject) {
        var qualifiedNameFeature = eObject.eClass().getEStructuralFeature("qualifiedName");
        if (qualifiedNameFeature != null && eObject.eGet(qualifiedNameFeature) != null && !qualifiedNameFeature.equals("null")) {
            String qualifiedName = eObject.eGet(qualifiedNameFeature).toString();
            this.putEObjectKey(eObject, qualifiedName);
        }

        var declaredShortNameFeature = eObject.eClass().getEStructuralFeature("declaredShortName");
        if (declaredShortNameFeature != null && eObject.eGet(declaredShortNameFeature) != null && !declaredShortNameFeature.equals("null")) {
            String declaredShortName = eObject.eGet(declaredShortNameFeature).toString();
            this.putEObjectKey(eObject, declaredShortName);
        }
    }

    public void putElement(final MappingElement mapping) {
        EObject target = mapping.getSelf();

        String identifier = AstConstant.getIdentifier(mapping.getMainNode());
        if (identifier != null) {
            this.logger.debug("putElement " + identifier + " " + mapping.getSelf());

            this.putEObjectKey(target, identifier);
        }

        String qualifiedName = AstConstant.getQualifiedName(mapping.getMainNode());
        if (qualifiedName != null) {
            this.logger.debug("putElement " + qualifiedName + " " + mapping.getSelf());

            this.putEObjectKey(target, qualifiedName);
        }
    }

    public void putEObjectKey(final EObject eObject, String key) {
        this.logger.debug("putEObject " + key + " " + eObject);
        List<EObject> eObjects = this.objectList.getOrDefault(key, new LinkedList<>());
        eObjects.add(eObject);
        this.objectList.put(key, eObjects);
    }

    public EObject findObject(final MappingElement mapping, final JsonNode jsonNode) {
        return this.findObject(mapping, jsonNode, null);
    }

    public EObject findObject(final MappingElement mapping, final JsonNode jsonNode, final EClass type) {
        EObject result = null;

        String identifier = AstConstant.getIdentifier(jsonNode);
        if (identifier == null) {
            result = this.findReference(mapping, jsonNode, type);
        } else {
            result = this.findInstance(jsonNode, type);
        }

        return result;

    }

    private EObject findInstance(final JsonNode jsonNode, final EClass type) {
        String identifier = AstConstant.getIdentifier(jsonNode);
        EObject result = null;

        this.logger.debug("findInstance " + identifier);

        result = this.findDirectSearch(identifier, type);

        if (result == null) {
            this.logger.debug("not found Instance for searchText = " + identifier + " and type " + type.getName());
        }

        return result;

    }

    private EObject findReference(final MappingElement mapping, final JsonNode jsonNode, final EClass type) {
        String searchText = null;

        if (jsonNode.has(AstConstant.REFERENCE_CONST) && !jsonNode.get(AstConstant.REFERENCE_CONST).isNull() && !jsonNode.get(AstConstant.REFERENCE_CONST).asText().isBlank()) {
            searchText = AstConstant.asCleanedText(jsonNode.get(AstConstant.REFERENCE_CONST));
        } else if (jsonNode.has(AstConstant.TEXT_CONST) && !jsonNode.get(AstConstant.TEXT_CONST).isNull() && !jsonNode.get(AstConstant.TEXT_CONST).asText().isBlank()) {
            searchText = AstConstant.asCleanedText(jsonNode.get(AstConstant.TEXT_CONST));
        }

        this.logger.debug("findReference " + searchText);

        EObject result = null;

        if (searchText != null) {
            result = this.findDirectSearch(searchText, type);

            if (result == null) {
                result = this.findStaticImport(searchText, type);
            }

            if (result == null) {
                result = this.findDynamicImport(searchText, type);
            }

            if (result == null) {
                result = this.findSimpleName(searchText, type);
            }

            if (result == null) {
                result = this.findDeclaredShortName(searchText, type);
            }

            if (result == null) {
                this.logger.warn("not found Reference for searchText = " + searchText + " and type " + type.getName());
                LogBook.addEvent("2", (Element) mapping.getSelf(), searchText);
            }
        } else {
            this.logger.error("Reference without searchText for node " + jsonNode);
            LogBook.addEvent("3", mapping.getSelf());
        }

        return result;
    }

    private EObject findDeclaredShortName(String searchText, EClass type) {
        EObject result = null;
        for (EObject directResult : this.objectListShortName.getOrDefault(searchText, List.of())) {
            if (type != null && !type.isSuperTypeOf(directResult.eClass())) {
                this.logger.warn("findObject declaredShortName with bad type - searchText = " + searchText + " - directResult = " + directResult + " - requested type : " + type.getName());
            } else {
                result = directResult;
                this.logger.debug("findObject declaredShortName searchText = " + searchText + " - result = " + result);
                break;
            }
        }
        return result;
    }

    private EObject findDirectSearch(final String searchText, final EClass type) {
        EObject result = null;
        for (EObject directResult : this.objectList.getOrDefault(searchText, List.of())) {
            if (type != null && !type.isSuperTypeOf(directResult.eClass())) {
                this.logger.warn("findObject direct with bad type - searchText = " + searchText + " - directResult = " + directResult + " - requested type : " + type.getName());
            } else {
                result = directResult;
                this.logger.debug("findObject direct searchText = " + searchText + " - result = " + result);
                break;
            }
        }
        return result;
    }

    private EObject findStaticImport(final String searchText, final EClass type) {
        EObject result = null;
        List<String> directImportResult = this.importRegexp.parallelStream().filter(t -> t.endsWith("::" + searchText)).toList();
        if (!directImportResult.isEmpty()) {
            for (String textValue : directImportResult) {
                for (EObject staticResult : this.objectList.getOrDefault(textValue, List.of())) {
                    if (type != null && !type.isSuperTypeOf(staticResult.eClass())) {
                        this.logger.warn("findObject static import with bad type - staticResult = " + staticResult + " - requested type : " + type.getName());
                    } else {
                        result = staticResult;
                        this.logger.debug("findObject static import textValue = " + textValue + " - result = " + result);
                        break;
                    }
                }
                if (result != null) {
                    break;
                }
            }
        }
        return result;
    }

    private EObject findDynamicImport(final String searchText, final EClass type) {
        EObject result = null;
        for (String importString : this.importRegexp) {
            if (importString.contains("*")) {

                String regexp = "^" + importString + Pattern.quote(searchText) + "$";

                for (Entry<String, List<EObject>> entry : this.objectList.entrySet()) {
                    if (Pattern.compile(regexp).matcher(entry.getKey()).find()) {
                        for (EObject dynamicResult : entry.getValue()) {

                            if (type != null && !type.isSuperTypeOf(dynamicResult.eClass())) {
                                this.logger.warn("findObject dynamic import with bad type - dynamicResult = " + dynamicResult + " - requested type : " + type.getName());
                            } else {
                                result = dynamicResult;
                                this.logger.debug("findObject dynamic import regexp = " + regexp + " - key = " + entry.getKey() + " - result = " + result);
                                break;
                            }
                        }
                        if (result != null) {
                            break;
                        }
                    }
                }
                if (result != null) {
                    break;
                }
            }
        }
        return result;
    }

    private EObject findSimpleName(final String searchText, final EClass type) {
        EObject result = null;
        String regexp = ".*::" + Pattern.quote(searchText) + "$";
        for (Entry<String, List<EObject>> entry : this.objectList.entrySet()) {
            if (Pattern.compile(regexp).matcher(entry.getKey()).find()) {
                for (EObject simpleNameResult : entry.getValue()) {

                    if (type != null && !type.isSuperTypeOf(simpleNameResult.eClass())) {
                        this.logger.warn("findObject simpleName with bad type - simpleNameResult = " + simpleNameResult + " - requested type : " + type.getName());
                    } else {
                        result = simpleNameResult;
                        this.logger.debug("findObject simpleName regexp = " + regexp + " - key = " + entry.getKey() + " - result = " + result);
                        break;
                    }
                }
                if (result != null) {
                    break;
                }
            }
        }
        return result;
    }
}
