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
package org.eclipse.syson.sysml.serializer.model;

/**
 * Names of the SysML cross-references that the reference queries look up by name, both as EMF structural features and
 * as keys of {@link DanglingReferenceIndex}. Keep them here so the model and parser-link lookups use one spelling.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
final class EmfReferenceNames {

    static final String CHAINING_FEATURE = "chainingFeature";

    static final String CONJUGATED_PORT_DEFINITION = "conjugatedPortDefinition";

    static final String CROSSED_FEATURE = "crossedFeature";

    static final String GENERAL = "general";

    static final String IMPORTED_MEMBERSHIP = "importedMembership";

    static final String IMPORTED_NAMESPACE = "importedNamespace";

    static final String MEMBER_ELEMENT = "memberElement";

    static final String PORT_DEFINITION = "portDefinition";

    static final String REDEFINED_FEATURE = "redefinedFeature";

    static final String REFERENCED_FEATURE = "referencedFeature";

    static final String SUBSETTED_FEATURE = "subsettedFeature";

    static final String SUPERCLASSIFIER = "superclassifier";

    static final String TYPE = "type";

    private EmfReferenceNames() {
        // constants only
    }
}
