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

import com.fasterxml.jackson.databind.JsonNode;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.parser.translation.EClassifierTranslator;
import org.eclipse.syson.sysml.parser.translation.EReferenceComputer;
import org.eclipse.syson.sysml.utils.LogNameProvider;
import org.eclipse.syson.sysml.utils.MessageReporter;

/**
 * Handler of containment references.
 *
 * @author Arthur Daussy.
 */
public class ContainmentReferenceHandler {

    private final EClassifierTranslator typeBuilder = new EClassifierTranslator();

    private final LogNameProvider logNameProvider = new LogNameProvider();

    private final EReferenceComputer referenceTranslator = new EReferenceComputer();

    private final MessageReporter messageReporter;

    public ContainmentReferenceHandler(MessageReporter messageReporter) {
        this.messageReporter = messageReporter;
    }

    public boolean isContainmentReference(final EObject eObject, String referenceName, final JsonNode astValue) {
        if (astValue.isContainerNode()) {
            JsonNode referenceTypeNode = astValue.get("$type");
            return referenceTypeNode != null && referenceTypeNode.isTextual() && !astValue.has("reference");
        }
        return false;
    }

    public EObject handleContainmentReference(final EObject eObject, String referenceName, final JsonNode astValue) {
        final EObject newObject;
        final EObject refrenceTypeInstance = this.typeBuilder.createObject(astValue);

        if (refrenceTypeInstance != null) {
            newObject = this.addChild(eObject, referenceName, astValue, refrenceTypeInstance);
        } else {
            String msg = MessageFormat.format("Unexpected child type in {0} with reference {1} for holding {2}", this.logNameProvider.getName(eObject), referenceName, astValue);
            this.messageReporter.error(msg);
            newObject = null;
        }
        return newObject;
    }

    private EObject addChild(final EObject eObject, String referenceName, final JsonNode astValue, final EObject refrenceTypeInstance) {
        final EObject newObject;
        this.addChildIn(eObject, refrenceTypeInstance, referenceName);
        if (refrenceTypeInstance.eContainer() != null) {
            newObject = refrenceTypeInstance;
        } else {
            String msg = MessageFormat.format("Unable to find containment reference in {0} with reference {1} for holding {2}", this.logNameProvider.getName(eObject), referenceName,
                    astValue);
            this.messageReporter.error(msg);
            newObject = null;
        }
        return newObject;
    }

    private void setValue(final EObject eObject, EReference ref, EObject value) {
        if (ref.isMany()) {
            ((List<Object>) eObject.eGet(ref)).add(value);
        } else {
            eObject.eSet(ref, value);
        }
    }

    private boolean addChildIn(final EObject owner, final EObject owned, String referenceName) {
        Optional<EReference> optContainementReference = this.referenceTranslator.getContainmentReference(owner, owned.eClass(), referenceName);
        if (optContainementReference.isPresent()) {
            this.setValue(owner, optContainementReference.get(), owned);
        }

        // Some relationship should set their source feature set with their container value. For the moment only the one
        // under an "heritage" JSNode seems to be required
        if (owned instanceof Relationship && "heritage".equals(referenceName)) {
            Optional<EReference> sourceFeature = this.referenceTranslator.getNonContainmentReference(owned.eClass(), owner.eClass(), "source");
            if (sourceFeature.isPresent() && !sourceFeature.get().isContainer()) {
                this.setValue(owned, sourceFeature.get(), owner);
            }
        }
        return true;
    }

}
