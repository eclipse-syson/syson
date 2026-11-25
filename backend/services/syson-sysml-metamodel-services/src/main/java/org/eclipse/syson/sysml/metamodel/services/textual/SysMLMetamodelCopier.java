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
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.parser.DanglingLink;
import org.eclipse.syson.sysml.parser.adapter.ICommentBodyAdapter;
import org.eclipse.syson.sysml.parser.adapter.KeywordAdapter;

/**
 * Copies stored SysML features between the SysON and OMG metamodels by name.
 *
 * @author arichard
 */
public class SysMLMetamodelCopier extends EcoreUtil.Copier {

    private static final long serialVersionUID = 1L;

    private final EPackage sourcePackage;

    private final EPackage targetPackage;

    private final Map<EObject, EObject> referenceTargets;

    /**
     * Creates a copier whose external reference targets are never copied or modified.
     *
     * @param sourcePackage the source metamodel
     * @param targetPackage the target metamodel
     * @param referenceTargets existing target objects indexed by their source counterparts
     */
    public SysMLMetamodelCopier(EPackage sourcePackage, EPackage targetPackage, Map<EObject, EObject> referenceTargets) {
        super(false, false);
        this.sourcePackage = sourcePackage;
        this.targetPackage = targetPackage;
        this.referenceTargets = new IdentityHashMap<>(referenceTargets);
    }

    /** Copies the source syntax markers shared by parser, linker and serializer. Keyword markers are stateless. */
    @Override
    public EObject copy(EObject source) {
        EObject target = super.copy(source);
        if (source != null) {
            source.eAdapters().stream().filter(KeywordAdapter.class::isInstance).forEach(target.eAdapters()::add);
        }
        return target;
    }

    /** Matches a classifier in the target package. */
    @Override
    protected EClass getTarget(EClass sourceClass) {
        EClass target = null;
        if (sourceClass.getEPackage() == this.sourcePackage) {
            target = (EClass) this.targetPackage.getEClassifier(sourceClass.getName());
        }
        return Objects.requireNonNull(target,
                () -> "Unsupported SysML classifier: " + sourceClass.getName());
    }

    /** Matches stored semantic features and omits annotations and derived values. */
    @Override
    protected EStructuralFeature getTarget(EStructuralFeature feature) {
        EStructuralFeature target = null;
        if (!feature.isTransient() && !feature.isDerived() && feature != EcorePackage.Literals.EMODEL_ELEMENT__EANNOTATIONS) {
            target = Objects.requireNonNull(this.getTarget(feature.getEContainingClass()).getEStructuralFeature(feature.getName()),
                    () -> "Unsupported SysML feature: " + feature.getEContainingClass().getName() + "." + feature.getName());
        }
        return target;
    }

    /** Translates enum literals and preserves stored comment formatting. */
    @Override
    protected void copyAttribute(EAttribute attribute, EObject source, EObject target) {
        EStructuralFeature targetFeature = this.getTarget(attribute);
        if (targetFeature != null) {
            if (attribute.getEAttributeType() instanceof EEnum) {
                // Enum defaults and their numeric values differ between the two metamodels.
                Object value = source.eGet(attribute, false);
                boolean unset = (attribute.isUnsettable() || targetFeature.isUnsettable()) && !source.eIsSet(attribute);
                if (value != null && !unset) {
                    String literal = this.sourcePackage.getEFactoryInstance().convertToString(attribute.getEAttributeType(), value);
                    EAttribute targetAttribute = (EAttribute) targetFeature;
                    target.eSet(targetAttribute, this.targetPackage.getEFactoryInstance().createFromString(targetAttribute.getEAttributeType(), literal));
                }
            } else {
                super.copyAttribute(attribute, source, target);
                if (this.targetPackage == SysmlPackage.eINSTANCE && "body".equals(attribute.getName())) {
                    // SysON keeps comment indentation; the OMG semantic body has already removed it.
                    source.eAdapters().stream().filter(ICommentBodyAdapter.class::isInstance).map(ICommentBodyAdapter.class::cast).findFirst()
                            .ifPresent(adapter -> target.eSet(targetFeature, adapter.getRawBody().substring(2, adapter.getRawBody().length() - 2).strip()));
                }
            }
        }
    }

    /** Wires stored references without resolving the source targets. */
    @Override
    protected void copyReference(EReference reference, EObject source, EObject target) {
        if (this.getTarget(reference) != null && source.eIsSet(reference)) {
            Object value = source.eGet(reference, false);
            if (reference.isMany()) {
                for (Object element : ((InternalEList<?>) value).basicList()) {
                    Objects.requireNonNull(this.get(element), () -> "Reference target outside the SysML import context: " + reference.getName());
                }
            } else if (value != null) {
                Objects.requireNonNull(this.get(value), () -> "Reference target outside the SysML import context: " + reference.getName());
            }
            super.copyReference(reference, source, target);
        }
    }

    /** Looks up a copied element, a supplied external target or a previously unseen proxy. */
    @Override
    public EObject get(Object key) {
        EObject result = super.get(key);
        if (result == null) {
            result = this.referenceTargets.get(key);
        }
        if (result == null && key instanceof EObject source && source.eIsProxy()) {
            result = this.externalProxy(source);
        }
        return result;
    }

    /** Captures imported spellings on converted owners after all references have been wired. */
    public void retainReferenceText(Set<DanglingLink> links) {
        for (DanglingLink link : links) {
            if (this.containsKey(link.context())) {
                EObject owner = this.get(link.context());
                owner.eAdapters().add(new ImportedReferenceText(owner, link));
            }
        }
    }

    /** Creates and remembers a typed proxy for an external reference without resolving the source. */
    protected EObject externalProxy(EObject source) {
        EObject proxy = EcoreUtil.create(this.getTarget(source.eClass()));
        ((InternalEObject) proxy).eSetProxyURI(EcoreUtil.getURI(source));
        this.referenceTargets.put(source, proxy);
        return proxy;
    }
}
