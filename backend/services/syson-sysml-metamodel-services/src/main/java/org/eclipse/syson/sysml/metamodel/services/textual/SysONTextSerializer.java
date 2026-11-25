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
package org.eclipse.syson.sysml.metamodel.services.textual;

import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.syson.sysml.AcceptActionUsage;
import org.eclipse.syson.sysml.Comment;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.LiteralString;
import org.eclipse.syson.sysml.MembershipImport;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.TextualRepresentation;
import org.eclipse.syson.sysml.metamodel.services.textual.utils.FileNameDeresolver;
import org.eclipse.syson.sysml.metamodel.services.textual.utils.INameDeresolver;
import org.eclipse.syson.sysml.parser.DanglingLink;
import org.eclipse.syson.sysml.parser.adapter.CommentBodyAdapter;
import org.eclipse.syson.sysml.parser.adapter.ITextAdapter;
import org.eclipse.syson.sysml.parser.adapter.StringLiteralFormatAdapter;
import org.eclipse.syson.sysml.serializer.SerializationOptions;
import org.eclipse.syson.sysml.serializer.SerializationRequest;
import org.eclipse.syson.sysml.serializer.SysMLSerializer;
import org.eclipse.syson.sysml.serializer.model.DanglingReferenceIndex;
import org.eclipse.syson.sysml.serializer.output.FormattingOptions;
import org.eclipse.syson.sysml.serializer.syntax.ParserFragments;
import org.eclipse.syson.sysml.util.NonResolvingReads;
import org.omg.sysml.lang.sysml.SysMLPackage;
import org.omg.sysml.logic.SysMLLogicStandaloneSetup;

/**
 * Writes SysON elements using an isolated OMG copy of the requested containment subtree.
 *
 * @author arichard
 */
public final class SysONTextSerializer {

    static {
        SysMLLogicStandaloneSetup.doSetup();
    }

    private final Supplier<INameDeresolver> nameProvider;
    private final boolean display;

    /** Creates a renderer for normalized, resolvable document or expression text. */
    public SysONTextSerializer() {
        this(FileNameDeresolver::new, false);
    }

    /** Selects reference naming and whether string literals are displayed without escaping. */
    public SysONTextSerializer(Supplier<INameDeresolver> nameProvider, boolean display) {
        this.nameProvider = Objects.requireNonNull(nameProvider);
        this.display = display;
    }

    /** Serializes the current element without modifying it or its editing context. */
    public String serialize(Element element) {
        return NonResolvingReads.compute(() -> this.render(element, ""));
    }

    /** Writes the payload and optional receiver of a transition trigger, without statement syntax. */
    public String acceptParameterPart(AcceptActionUsage action) {
        return NonResolvingReads.compute(() -> this.render(action, ParserFragments.ACCEPT_PARAMETER_PART));
    }

    /** Builds the request and drops only the renderer's final line separator. */
    private String render(Element element, String entryPoint) {
        if (element == null) {
            return "";
        }
        Projection projection = new Projection();
        var root = (org.omg.sysml.lang.sysml.Element) projection.copy(element);
        projection.copyReferences();
        projection.forEach((original, copy) -> projection.originals.put(copy, (Element) original));
        Set<DanglingLink> links = new LinkedHashSet<>();
        projection.forEach((original, copy) -> {
            for (var adapter : original.eAdapters()) {
                if (adapter instanceof ImportedReferenceText text) {
                    var link = text.forCopy(copy);
                    if (this.needsSpelling(link, (Element) original, projection)) {
                        links.add(link);
                    }
                }
            }
            if (this.display) {
                copy.eAdapters().remove(ITextAdapter.QUOTED_NAME);
            }
            if (original instanceof Comment || original instanceof TextualRepresentation) {
                String body = Objects.toString(original.eGet(original.eClass().getEStructuralFeature("body")), "");
                copy.eAdapters().add(new CommentBodyAdapter("/* " + body.replace("*/", "* /") + " */"));
            }
            if (this.display && original instanceof LiteralString literal) {
                String value = literal.getValue();
                copy.eAdapters().add(new StringLiteralFormatAdapter("\"" + Objects.toString(value, "") + "\""));
            }
        });
        var formatting = FormattingOptions.defaults();
        if (this.display) {
            formatting = new FormattingOptions("\n", " ");
        }
        var names = this.nameProvider.get();
        var options = new SerializationOptions(SerializationOptions.defaults().mode(), formatting, entryPoint, true);
        var request = SerializationRequest.forElement(root).withOptions(options).withDanglingReferences(DanglingReferenceIndex.from(links))
                .withContextualNames((target, scope) -> {
                    Element context = projection.originals.getOrDefault(scope, element);
                    Element original = this.loadedTarget(projection.originals.get(target), context);
                    if (context instanceof MembershipImport && original instanceof OwningMembership membership) {
                        original = this.loadedTarget(membership.getMemberElement(), context);
                    }
                    return names.getDeresolvedName(original, context);
                });
        String text = new SysMLSerializer().serialize(request);
        if (text.endsWith(formatting.lineSeparator())) {
            text = text.substring(0, text.length() - formatting.lineSeparator().length());
        }
        return text;
    }

    /** Lets display names and loaded external targets supersede a stored spelling. */
    private boolean needsSpelling(DanglingLink link, Element owner, Projection projection) {
        if (!link.reference().isMany() && link.context().eGet(link.reference(), false) instanceof org.omg.sysml.lang.sysml.Element target) {
            boolean contextual = this.display || target.eIsProxy();
            return !contextual || this.loadedTarget(projection.originals.get(target), owner) == null;
        }
        return true;
    }

    /** Looks up a proxy in already loaded resources without replacing it in the live model. */
    private Element loadedTarget(Element target, Element context) {
        if (target == null || !target.eIsProxy()) {
            return target;
        }
        Element result = null;
        var resource = context.eResource();
        if (resource != null && resource.getResourceSet() != null) {
            URI uri = ((InternalEObject) target).eProxyURI();
            if (uri.isRelative() && resource.getURI() != null) {
                uri = uri.resolve(resource.getURI());
            }
            var loaded = resource.getResourceSet().getResource(uri.trimFragment(), false);
            if (loaded != null && uri.hasFragment() && loaded.getEObject(uri.fragment()) instanceof Element resolved && !resolved.eIsProxy()) {
                result = resolved;
            }
        }
        return result;
    }

    /** Represents references outside the requested subtree by typed proxies on the temporary model. */
    private static final class Projection extends SysMLMetamodelCopier {
        private static final long serialVersionUID = 1L;
        private final Map<EObject, Element> originals = new IdentityHashMap<>();

        /** Copies stored features from SysON to OMG. */
        private Projection() {
            super(SysmlPackage.eINSTANCE, SysMLPackage.eINSTANCE, Map.of());
        }

        /** Supplies an external proxy when the target was outside the copied subtree. */
        @Override
        public EObject get(Object key) {
            EObject copy = super.get(key);
            if (copy == null && key instanceof Element original) {
                copy = this.externalProxy(original);
            }
            return copy;
        }

        /** Records the original target for contextual naming of external references. */
        @Override
        protected EObject externalProxy(EObject source) {
            EObject proxy = super.externalProxy(source);
            this.originals.put(proxy, (Element) source);
            return proxy;
        }
    }
}
