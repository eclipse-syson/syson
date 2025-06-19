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
package org.eclipse.syson.sysml.parser;

import com.fasterxml.jackson.databind.JsonNode;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.parser.translation.EClassifierTranslator;
import org.eclipse.syson.sysml.parser.translation.EReferenceComputer;
import org.eclipse.syson.sysml.utils.LogNameProvider;
import org.eclipse.syson.sysml.utils.MessageReporter;

/**
 * Class that handles non containment reference.
 *
 * @author gescande.
 */
public class NonContainmentReferenceHandler {

    private static final String QUALIFIED_CONST = "qualifiedName";

    private final EReferenceComputer referenceComputer = new EReferenceComputer();

    private final EClassifierTranslator typeBuilder = new EClassifierTranslator();

    private final LogNameProvider logNameProvider = new LogNameProvider();

    private final List<ProxiedReference> proxiesToResolve = new ArrayList<>();

    private final MessageReporter messageReporter;

    public NonContainmentReferenceHandler(MessageReporter messageReporter) {
        this.messageReporter = messageReporter;
    }

    public boolean isNonContainmentReference(final Element owner, String referenceName, final JsonNode astValue) {
        if (astValue.isContainerNode()) {
            JsonNode referenceTypeNode = astValue.get("$type");
            JsonNode textNode = astValue.get("text");
            return referenceTypeNode != null && astValue.has("reference") && textNode != null && textNode.isTextual();
        }
        return false;
    }

    public void createProxy(final Element owner, String referenceName, final JsonNode astValue) {
        JsonNode referenceTypeNode = astValue.get("$type");
        JsonNode textNode = astValue.get("text");
        String referenceType = referenceTypeNode.asText();
        final EObject refrenceTypeInstance = this.typeBuilder.createObject(astValue);

        if (refrenceTypeInstance instanceof InternalEObject internalTarget) {
            String qualifiedNameTarget = textNode.asText();
            internalTarget.eSetProxyURI(URI.createGenericURI("syson-import", QUALIFIED_CONST, qualifiedNameTarget));
            // It should be a reference
            Optional<EReference> optEReference = this.referenceComputer.getNonContainmentReference(owner.eClass(), refrenceTypeInstance.eClass(), referenceName);
            if (optEReference.isEmpty()) {
                this.messageReporter.error(MessageFormat.format("Unable to find a reference from {0} to {1} with reference name {2} on object {3}", owner.eClass().getName(),
                        refrenceTypeInstance.eClass().getName(), referenceType, this.logNameProvider.getName(owner)));
            } else {
                EReference ref = optEReference.get();
                this.doSetProxy(owner, internalTarget, ref);
            }
        } else {
            this.messageReporter.error(MessageFormat.format("Unable to create on {0} for reference {1} with value {2}", this.logNameProvider.getName(owner), referenceName, astValue));
        }
    }

    public List<ProxiedReference> getProxiesToResolve() {
        return this.proxiesToResolve;
    }

    private void doSetProxy(final Element owner, final InternalEObject refrenceTypeInstance, EReference ref) {
        if (ref.isMany()) {
            ((List<Object>) owner.eGet(ref)).add(refrenceTypeInstance);
        } else {
            owner.eSet(ref, refrenceTypeInstance);
        }
        this.proxiesToResolve.add(new ProxiedReference(owner, ref, refrenceTypeInstance));
    }

}
