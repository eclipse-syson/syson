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
package org.eclipse.syson.sysml.finder;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.syson.sysml.AstConstant;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.LogBook;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.mapper.MappingElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Facilitates the discovery and retrieval of EObjects based on identifiers or patterns.
 *
 * @author gescande
 */
public class ObjectFinder {

    private final Logger logger = LoggerFactory.getLogger(ObjectFinder.class);

    private final ConcurrentHashMap<String, List<EObject>> objectList = new ConcurrentHashMap<String, List<EObject>>();

    private final ConcurrentHashMap<String, List<EObject>> objectListShortName = new ConcurrentHashMap<String, List<EObject>>();

    private final Set<String> importSet = Collections.synchronizedSet(new HashSet<>());

    private final ConcurrentHashMap<String, Set<String>> aliases = new ConcurrentHashMap<String, Set<String>>();

    private long statFindObject;
    private long statFindInstance;
    private long statFindReference;
    private long statFindDirectSearch;
    private long statFindStaticImport;
    private long statFindDynamicImport;
    private long statFindSimpleName;
    private long statFindDeclaredShortName;
    private long statFindAliasImport;
    private long statNotFoundReference;

    public void addImportMember(final String importString) {
        this.logger.debug("addImportMember " + importString);
        synchronized (this.importSet) {
            this.importSet.add(importString);
        }
    }

    public void addImportAlias(final String initial, final String target) {
        this.logger.debug("addImportAlias " + initial + " -> " + target);
        Set<String> thisAliases = this.aliases.getOrDefault(initial, new TreeSet<>());
        thisAliases.add(target);
        this.aliases.put(initial, thisAliases);
    }

    public void addImportNamespace(final String importString) {
        this.logger.debug("addImportNamespace " + importString);
        synchronized (this.importSet) {
            this.importSet.add(importString + "::");
        }
    }

    public void putEObject(final EObject eObject) {
        var qualifiedNameFeature = eObject.eClass().getEStructuralFeature("qualifiedName");
        if (qualifiedNameFeature != null && eObject.eGet(qualifiedNameFeature) != null && !qualifiedNameFeature.equals("null")) {
            String qualifiedName = eObject.eGet(qualifiedNameFeature).toString();
            this.putEObjectKey(eObject, qualifiedName);

            this.logger.debug("putEObject " + qualifiedName);

            if (eObject instanceof Membership membership) {
                EAttribute orElse = membership.eClass().getEAttributes().stream().filter(a -> a.getName().equals("isAlias")).findFirst().orElse(null);
                if (Boolean.TRUE.equals(orElse)) {
                    Element referencedObject = membership.getMemberElement();
                    this.addImportAlias(qualifiedName, referencedObject.getQualifiedName());
                }
            }
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
        List<EObject> eObjects = this.objectList.getOrDefault(key, Collections.synchronizedList(new LinkedList<>()));
        if (eObject != null) {
            eObjects.add(eObject);
        }
        this.objectList.put(key, eObjects);
    }

    public EObject findObject(final MappingElement mapping, final JsonNode jsonNode) {
        return this.findObject(mapping, jsonNode, null);
    }

    public EObject findObject(final MappingElement mapping, final JsonNode jsonNode, final EClass type) {
        statFindObject++;
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
        EObject result = null;
        String identifier = AstConstant.getIdentifier(jsonNode);
        result = this.findDirectSearch(identifier, type);
        if (result == null) {
            this.logger.debug("not found Instance for searchText = " + identifier + " and type " + type.getName());
        } else {
            statFindInstance++;
        }
        return result;
    }


    private EObject findReference(final MappingElement mapping, final JsonNode jsonNode, final EClass type) {
        String searchText = AstConstant.getSearchText(jsonNode);

        this.logger.trace("findReference " + searchText);

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
                result = this.findAliasImport(searchText, type);
            }

            if (result == null) {
                statNotFoundReference++;
                this.logger.warn("not found Reference for searchText = " + searchText + " and type " + type.getName());
                LogBook.addEvent("2", (Element) mapping.getSelf(), searchText);
            }
        } else {
            this.logger.error("Reference without searchText for node " + jsonNode);
            LogBook.addEvent("3", mapping.getSelf());
        }
        if (result != null) {
            statFindReference++;
        }

        return result;
    }

    protected EObject findReferenceBySearchText(String searchText, EClass type) {
        EObject result = this.findDirectSearch(searchText, type);
        if (result == null) {
            result = this.findStaticImport(searchText, type);
        }
        if (result == null) {
            result = this.findDynamicImport(searchText, type);
        }
        if (result == null) {
            result = this.findAliasImport(searchText, type);
        }
        return result;
    }

    private EObject findDeclaredShortName(String searchText, EClass type) {
        EObject result = null;
        for (EObject directResult : this.objectListShortName.getOrDefault(searchText, List.of())) {
            if (type != null && !type.isSuperTypeOf(directResult.eClass())) {
                this.logger.debug("findObject declaredShortName with bad type - searchText = " + searchText + " - directResult = " + directResult + " - requested type : " + type.getName());
            } else {
                result = directResult;
                statFindDeclaredShortName++;
                this.logger.trace("findObject declaredShortName searchText = " + searchText + " - result = " + result);
                break;
            }
        }
        return result;
    }

    private EObject findDirectSearch(final String searchText, final EClass type) {
        EObject result = null;
        for (EObject directResult : this.objectList.getOrDefault(searchText, List.of())) {
            if (type != null && !type.isSuperTypeOf(directResult.eClass())) {
                this.logger.debug("findObject direct with bad type - searchText = " + searchText + " - directResult = " + directResult + " - requested type : " + type.getName());
            } else {
                result = directResult;
                statFindDirectSearch++;
                this.logger.trace("findObject direct searchText = " + searchText + " - result = " + result);
                break;
            }
        }
        return result;
    }

    private EObject findStaticImport(final String searchText, final EClass type) {
        this.logger.trace("findStaticImport searchText = " + searchText);
        EObject result = null;
        List<String> directImportResult = null;
        synchronized (this.importSet) {
            directImportResult = this.importSet.stream().filter(t -> t.endsWith("::" + searchText)).toList();
        }
        if (!directImportResult.isEmpty()) {
            for (String textValue : directImportResult) {
                for (EObject staticResult : this.objectList.getOrDefault(textValue, List.of())) {
                    if (type != null && !type.isSuperTypeOf(staticResult.eClass())) {
                        this.logger.debug("findObject static import with bad type - searchText = " + searchText + " - staticResult = " + staticResult + " - requested type : " + type.getName());
                    } else {
                        result = staticResult;
                        statFindStaticImport++;
                        this.logger.trace("findObject static import textValue = " + textValue + " - result = " + result);
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

        Set<String> copySet = null;
        synchronized (this.importSet) {
            copySet = new HashSet<>(this.importSet);
        }
        
        for (String importString : copySet) {
            if (importString.endsWith("::")) {

                for (Entry<String, List<EObject>> entry : this.objectList.entrySet()) {
                    String key = entry.getKey();
                    if (key.startsWith(importString) && key.endsWith("::" + searchText)) {
                        for (EObject dynamicResult : entry.getValue()) {

                            if (type != null && !type.isSuperTypeOf(dynamicResult.eClass())) {
                                this.logger.debug("findObject dynamic import with bad type - searchText = " + searchText + " - dynamicResult = " + dynamicResult + " - requested type : " + type.getName());
                            } else {
                                result = dynamicResult;
                                statFindDynamicImport++;
                                this.logger.trace("findObject dynamic import searchText = " + searchText + " - importString = " + importString + " - key = " + key + " - result = " + result);
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
        for (Entry<String, List<EObject>> entry : this.objectList.entrySet()) {
            String key = entry.getKey();
            if (key.endsWith("::" + searchText)) {
                for (EObject simpleNameResult : entry.getValue()) {

                    if (type != null && !type.isSuperTypeOf(simpleNameResult.eClass())) {
                        this.logger.debug("findObject simpleName with bad type - searchText = " + searchText + " - simpleNameResult = " + simpleNameResult + " - requested type : " + type.getName());
                    } else {
                        result = simpleNameResult;
                        statFindSimpleName++;
                        this.logger.trace("findObject simpleName searchText = " + searchText + " - key = " + key + " - result = " + result);
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

    private EObject findAliasImport(final String searchText, final EClass type) {
        EObject result = null;
        this.logger.trace("findAliasImport AliasImport searchText = " + searchText);
        for (Entry<String, Set<String>> alias : this.aliases.entrySet()) {
            String aliasKey = alias.getKey();
            Pattern matcher = Pattern.compile(aliasKey + ".*");
            this.logger.trace("searchText = " + searchText + " - aliasKey = " + aliasKey);
            if (matcher.matcher(searchText).matches()) {
                this.logger.trace("Matches : " + matcher.matcher(searchText).matches() + " getValue = " + alias.getValue());
                for (String aliasTarget : alias.getValue()) {
                    this.logger.trace("searchText = " + searchText + " - aliasTarget = " + aliasTarget);
                    String aliasSearchText = searchText.replaceAll(aliasKey, aliasTarget);
                    result = this.findReferenceBySearchText(aliasSearchText, type);
                    statFindAliasImport++;
                    if (result != null) {
                        this.logger.debug("findObject AliasImport textValue = " + searchText + " - res = " + result);
                        break;
                    }
                }
            }
            if (result != null) {
                break;
            }
        }
        return result;
    }

    public void logStat() {
        this.logger.info("ObjectFinder Stat - statFindObject = " + statFindObject);
        this.logger.info("ObjectFinder Stat - statFindInstance = " + statFindInstance);
        this.logger.info("ObjectFinder Stat - statFindReference = " + statFindReference);
        this.logger.info("ObjectFinder Stat - statFindDirectSearch = " + statFindDirectSearch);
        this.logger.info("ObjectFinder Stat - statFindStaticImport = " + statFindStaticImport);
        this.logger.info("ObjectFinder Stat - statFindDynamicImport = " + statFindDynamicImport);
        this.logger.info("ObjectFinder Stat - statFindSimpleName = " + statFindSimpleName);
        this.logger.info("ObjectFinder Stat - statFindDeclaredShortName = " + statFindDeclaredShortName);
        this.logger.info("ObjectFinder Stat - statFindAliasImport = " + statFindAliasImport);
        this.logger.info("ObjectFinder Stat - statNotFoundReference = " + statNotFoundReference);
    }

    public long getStatFindObject() {
        return statFindObject;
    }

    public long getStatFindInstance() {
        return statFindInstance;
    }

    public long getStatFindReference() {
        return statFindReference;
    }

    public long getStatFindDirectSearch() {
        return statFindDirectSearch;
    }

    public long getStatFindStaticImport() {
        return statFindStaticImport;
    }

    public long getStatFindDynamicImport() {
        return statFindDynamicImport;
    }

    public long getStatFindSimpleName() {
        return statFindSimpleName;
    }

    public long getStatFindDeclaredShortName() {
        return statFindDeclaredShortName;
    }

    public long getStatFindAliasImport() {
        return statFindAliasImport;
    }

    public long getStatNotFoundReference() {
        return statNotFoundReference;
    }
}