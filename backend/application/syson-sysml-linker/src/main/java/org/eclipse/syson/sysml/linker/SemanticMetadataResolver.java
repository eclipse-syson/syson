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
package org.eclipse.syson.sysml.linker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Feature;
import org.omg.sysml.lang.sysml.MetadataAccessExpression;
import org.omg.sysml.lang.sysml.MetadataFeature;
import org.omg.sysml.lang.sysml.OperatorExpression;
import org.omg.sysml.lang.sysml.Specialization;
import org.omg.sysml.lang.sysml.Type;
import org.omg.sysml.util.ElementUtil;
import org.omg.sysml.util.FeatureUtil;
import org.omg.sysml.util.SysMLLibraryUtil;
import org.omg.sysml.util.TypeUtil;

/**
 * Resolves the dependencies needed to apply the implicit specializations introduced by semantic metadata.
 *
 * @author Arthur Daussy
 */
final class SemanticMetadataResolver {

    private final Map<EObject, Map<EReference, Set<QualifiedName>>> pendingLinks;

    private final BiFunction<EObject, EReference, Set<Element>> linkResolver;

    /**
     * Creates a resolver for the pending links and link-resolution operation.
     *
     * @param pendingLinks
     *            pending links
     * @param linkResolver
     *            resolved links
     */
    SemanticMetadataResolver(Map<EObject, Map<EReference, Set<QualifiedName>>> pendingLinks, BiFunction<EObject, EReference, Set<Element>> linkResolver) {
        this.pendingLinks = pendingLinks;
        this.linkResolver = linkResolver;
    }

    /**
     * Resolves metadata dependencies and applies their resulting implicit general types.
     *
     * @return <code>true</code> if at least one pending link was resolved, <code>false</code> otherwise
     */
    boolean resolveDependencies() {
        boolean resolved = false;
        for (Type annotatedType : this.getAnnotatedTypes()) {
            for (MetadataFeature metadataFeature : ElementUtil.getAllMetadataFeaturesOf(annotatedType)) {
                resolved = resolved | this.resolvePendingLinksIn(metadataFeature);
                // FeatureTyping determines the derived metaclass of a MetadataFeature.
                ElementUtil.clearCachesOf(metadataFeature);
                Type metadataDefinition = metadataFeature.getMetaclass();
                if (metadataDefinition != null) {
                    resolved = resolved | this.resolveDefinitionHierarchy(metadataDefinition, new HashSet<>());
                    ElementUtil.clearCachesOf(metadataFeature);
                    if (metadataFeature.isSemantic()) {
                        resolved = resolved | this.refreshImplicitGeneralTypes(annotatedType, metadataFeature, metadataDefinition);
                    }
                }
            }
        }
        return resolved;
    }

    /**
     * Gets the types in resources containing unresolved links that have metadata features.
     *
     * @return the {@link List} of annotated {@link Type}
     */
    private List<Type> getAnnotatedTypes() {
        Set<Resource> resources = new LinkedHashSet<>();
        this.pendingLinks.keySet().stream().map(EObject::eResource).filter(resource -> resource != null).forEach(resources::add);

        List<Type> result = new ArrayList<>();
        for (Resource resource : resources) {
            for (EObject root : resource.getContents()) {
                this.addAnnotatedType(root, result);
                TreeIterator<EObject> contents = root.eAllContents();
                while (contents.hasNext()) {
                    this.addAnnotatedType(contents.next(), result);
                }
            }
        }
        return result;
    }

    /**
     * Adds an object to the annotated types when it is a type with metadata features.
     *
     * @param object
     *            the {@link EObject}
     * @param annotatedTypes
     *            the {@link List} of annotated {@link Type}
     */
    private void addAnnotatedType(EObject object, List<Type> annotatedTypes) {
        if (object instanceof Type type && !ElementUtil.getAllMetadataFeaturesOf(type).isEmpty()) {
            annotatedTypes.add(type);
        }
    }

    /**
     * Resolves pending links in a metadata definition and its general definitions.
     *
     * @param metadataDefinition
     *            the metada definition {@link Type}
     * @param visited
     *            the {@link Set} of visited {@link Type}
     * @return <code>true</code> if at least one pending link was resolved, <code>false</code> otherwise
     */
    private boolean resolveDefinitionHierarchy(Type metadataDefinition, Set<Type> visited) {
        if (!visited.add(metadataDefinition)) {
            return false;
        }

        boolean resolved = this.resolvePendingLinksIn(metadataDefinition);
        for (Specialization specialization : metadataDefinition.getOwnedSpecialization()) {
            Type general = specialization.getGeneral();
            if (general != null) {
                resolved = resolved | this.resolveDefinitionHierarchy(general, visited);
            }
        }
        // A newly resolved general can change isSemantic() and the effective redefinition of baseType.
        this.invalidateAdapterCachesIn(metadataDefinition);
        return resolved;
    }

    /**
     * Refreshes the implicit general types of an annotated type from its semantic metadata.
     *
     * @param annotatedType
     *            the annotated {@link Type}
     * @param metadataFeature
     *            the {@link MetadataFeature}
     * @param metadataDefinition
     *            the metadata definition {@link Type}
     * @return <code>true</code> if at least one pending link was resolved, <code>false</code> otherwise
     */
    private boolean refreshImplicitGeneralTypes(Type annotatedType, MetadataFeature metadataFeature, Type metadataDefinition) {
        // Metadata dependency resolution may have populated the pilot's implicit-specialization cache too early.
        // Invalidate it without removing the cache and its dependency tracking.
        ElementUtil.clearCachesOf(annotatedType);
        TypeUtil.invalidateImplicitSpecializations(annotatedType);

        boolean resolved = false;
        for (Type baseType : this.getBaseTypes(metadataFeature, metadataDefinition)) {
            resolved = resolved | this.resolvePendingLinksIn(baseType);
            ElementUtil.clearCachesOf(baseType);
        }
        return resolved;
    }

    /**
     * Gets the base types selected by the semantic metadata definition.
     *
     * @param metadataFeature
     *            the {@link MetadataFeature}
     * @param metadataDefinition
     *            the metadata definition {@link Type}
     * @return the {@link List} of selected base {@link Type}
     */
    private List<Type> getBaseTypes(MetadataFeature metadataFeature, Type metadataDefinition) {
        Type baseType = SysMLLibraryUtil.getLibraryType(metadataFeature, "Metaobjects::SemanticMetadata::baseType");
        if (!(baseType instanceof Feature semanticMetadataBaseType)) {
            return Collections.emptyList();
        }

        return Stream.concat(metadataDefinition.getOwnedFeature().stream(), metadataDefinition.getFeature().stream())
                .distinct()
                .filter(feature -> TypeUtil.specializes(feature, semanticMetadataBaseType))
                .map(FeatureUtil::getValueExpressionFor)
                .filter(OperatorExpression.class::isInstance)
                .map(OperatorExpression.class::cast)
                .filter(expression -> "meta".equals(expression.getOperator()))
                .flatMap(expression -> expression.getParameter().stream())
                .map(FeatureUtil::getValueExpressionFor)
                .filter(MetadataAccessExpression.class::isInstance)
                .map(MetadataAccessExpression.class::cast)
                .map(MetadataAccessExpression::getReferencedElement)
                .filter(Type.class::isInstance)
                .map(Type.class::cast)
                .toList();
    }

    private void invalidateAdapterCachesIn(Element root) {
        ElementUtil.clearCachesOf(root);
        TreeIterator<EObject> contents = root.eAllContents();
        while (contents.hasNext()) {
            EObject content = contents.next();
            if (content instanceof Element element) {
                ElementUtil.clearCachesOf(element);
            }
        }
    }

    /**
     * Resolves pending links owned by an object or one of its contents.
     *
     * @param root
     *            the root {@link Element}
     *
     * @return <code>true</code> if at least one pending link was resolved, <code>false</code> otherwise
     */
    private boolean resolvePendingLinksIn(EObject root) {
        Map<EObject, Set<EReference>> referencesToResolve = new LinkedHashMap<>();
        for (Entry<EObject, Map<EReference, Set<QualifiedName>>> entry : new LinkedHashSet<>(this.pendingLinks.entrySet())) {
            EObject context = entry.getKey();
            if (context == root || EcoreUtil.isAncestor(root, context)) {
                referencesToResolve.put(context, new LinkedHashSet<>(entry.getValue().keySet()));
            }
        }

        boolean resolved = false;
        for (Entry<EObject, Set<EReference>> entry : referencesToResolve.entrySet()) {
            for (EReference reference : entry.getValue()) {
                Map<EReference, Set<QualifiedName>> pendingReferences = this.pendingLinks.get(entry.getKey());
                if (pendingReferences != null && pendingReferences.containsKey(reference)) {
                    resolved = resolved | !this.linkResolver.apply(entry.getKey(), reference).isEmpty();
                }
            }
        }
        return resolved;
    }
}
