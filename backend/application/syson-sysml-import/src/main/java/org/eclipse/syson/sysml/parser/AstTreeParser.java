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
package org.eclipse.syson.sysml.parser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.EClassImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.syson.sysml.AstConstant;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Import;
import org.eclipse.syson.sysml.SysmlPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * AstTreeParser.
 *
 * @author gescande.
 */
public class AstTreeParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(AstTreeParser.class);
    
    public static List<EObject> parseAst(JsonNode astJson) {
        List<EObject> rootElements;
        if (!astJson.isEmpty()) {
            rootElements = parseJsonNode(astJson);
        } else {
            rootElements = List.of();
        }

        return rootElements;
    }

    public static List<EObject> parseJsonNode(JsonNode astJson) {
        List<EObject> result = null;
        if (astJson == null) {
            result = List.of();
        } else {
            if (astJson.isArray()) {
                result = parseJsonArray(astJson);
            } else {
                result = parseJsonObject(astJson);
            }
        }
        return result;
    }

    public static List<EObject> parseJsonArray(JsonNode astJson) {
        List<EObject> result = new ArrayList<EObject>();
        astJson.forEach(t -> {
            result.addAll(parseJsonNode(t));
        });
        return result;
    }
    
    public static List<EObject> parseJsonObject(JsonNode astJson) {
        List<EObject> result = new ArrayList<EObject>();

        EObject eObject = createObject(astJson);
        if (eObject != null)  {
            AstObjectParser.setObjectAttribute(eObject, astJson);
            AstContainmentReferenceParser.populateContainmentReference(eObject, astJson);
            AstWeakReferenceParser.proxyNonContainmentReference((Element) eObject, astJson);
    
            result.add(eObject);
        } else {
            LOGGER.error("Error building the object " + astJson);
        }

        return result;
    }

    public static EObject createObject(JsonNode astJson) {
        String type = astJson.findValue(AstConstant.TYPE_CONST).textValue();


        if (type.equals("MembershipReference")) {
            type = "Membership";
        }
        if (type.equals("SysMLFunction")) {
            type = "Function";
        }
        if (type.equals("TypeReference")) {
            type = "Type";
        }
        if (type.equals("FeatureReference")) {
            type = "Feature";
        }
        if (type.equals("ConjugatedPortReference")) {
            type = "ConjugatedPortDefinition";
        }
        if (type.equals("ClassifierReference")) {
            type = "Classifier";
        }
        if (type.equals("ElementReference")) {
            type = "Type";
        }
        if (type.equals("MembershipReference")) {
            type = "Membership";
        }
        

        if (type.equals("NamespaceReference")) {
            type = "Namespace";
        }
        if ("LiteralNumber".equals(type)) {
            String literalValue = astJson.get(AstConstant.LITERAL_CONST).asText();
            if (literalValue != null) {
                try {
                    BigDecimal bd = new BigDecimal(literalValue);
                    bd.intValueExact();
                    type = "LiteralInteger";
                } catch (ArithmeticException e) {
                    type = "LiteralRational";
                }
            }
        }
        

        EClassifier classif = SysmlPackage.eINSTANCE.getEClassifier(type);
        EClassImpl eclassImpl = (EClassImpl) classif;

        if (classif == null) {
            return null;
        } else {
            return EcoreUtil.create(eclassImpl);
        }
    }

    public static void resolveAllImport(Resource rootResource) {
        rootResource.getContents().forEach(content -> {
            resolveAllImport(content);
        });
    }

    public static void resolveAllImport(EObject parent) {

        if (parent instanceof Import parentImport) {
            LOGGER.debug("Resolve Import " + parentImport);
            ProxyResolver.resolveAllProxy(parent);
        }

        parent.eContents().forEach(content -> {
            resolveAllImport(content);
        });
    }

    public static void resolveAllReference(Resource rootResource) {
        rootResource.getContents().forEach(content -> {
            resolveAllReference(content);
        });
    }

    public static void resolveAllReference(EObject parent) {
        ProxyResolver.resolveAllProxy(parent);

        parent.eContents().forEach(content -> {
            resolveAllReference(content);
        });
    }

}
