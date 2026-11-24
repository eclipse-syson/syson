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
package org.eclipse.syson.sysml.serializer.rendering.actions;

import org.eclipse.syson.sysml.serializer.SerializationContext;
import org.eclipse.syson.sysml.serializer.model.NestedTargetSearch;
import org.eclipse.syson.sysml.serializer.references.ReferenceResolver;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.ReferenceSubsetting;
import org.omg.sysml.lang.sysml.SysMLPackage;

/**
 * Resolves the behavior or state named by a {@code perform} or {@code exhibit} usage written in its shorthand form, as
 * in {@code perform a.b;} or {@code exhibit s;}, where the usage reference-subsets the target instead of declaring a
 * name.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class PerformedActionTargetResolver {

    private final ReferenceResolver referenceResolver;

    private final SerializationContext context;

    private final NestedTargetSearch nestedTargets;

    /**
     * Creates the resolver.
     *
     * @param referenceResolver
     *            resolves reference names
     * @param context
     *            the serialization context
     */
    public PerformedActionTargetResolver(final ReferenceResolver referenceResolver, final SerializationContext context) {
        this.referenceResolver = referenceResolver;
        this.context = context;
        this.nestedTargets = context.modelQueries().nestedTargets();
    }

    /**
     * Resolves the target reference text of a {@code perform}/{@code exhibit} usage: the feature chain owned by the
     * {@code OwnedReferenceSubsetting} ({@code perform a.b.c}), which is the path the user wrote, else the direct
     * reference subsetting target. Only the reference subsetting is searched: the feature chains of the usage's body
     * belong to its members, not to its target.
     *
     * @param usage
     *            the perform or exhibit usage
     * @return the target reference text, or an empty string when the usage has no target
     */
    public String resolve(final Element usage) {
        String targetName = this.nestedTargets.firstChainedFeatureName(usage, ReferenceSubsetting.class,
                this.context.danglingReferenceIndex());
        if (targetName.isEmpty()) {
            targetName = this.referenceResolver.firstDirectRelationshipTarget(usage,
                    SysMLPackage.eINSTANCE.getReferenceSubsetting());
        }
        String target = "";
        if (!targetName.isBlank()) {
            target = this.referenceResolver.referenceName(targetName);
        }
        return target;
    }
}
