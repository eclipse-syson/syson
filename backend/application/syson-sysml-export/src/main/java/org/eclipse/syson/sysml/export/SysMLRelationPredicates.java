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
package org.eclipse.syson.sysml.export;

import java.util.function.Predicate;
import java.util.stream.Stream;

import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Import;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.MetadataUsage;
import org.eclipse.syson.sysml.Relationship;

/**
 * List of predicates to select Relationships.
 * 
 * @author Arthur Daussy
 */
public class SysMLRelationPredicates {

    public static final Predicate<Relationship> IS_MEMBERSHIP = r -> r instanceof Membership;

    public static final Predicate<Relationship> IS_IMPORT = r -> r instanceof Import;

    public static final Predicate<Relationship> IS_METADATA_USAGE = m -> isMemberElementA(m, MetadataUsage.class);

    public static final Predicate<Relationship> IS_DEFINITION_BODY_ELEMENT = r -> !isMemberElementA(r, MetadataUsage.class);


    public static boolean isMemberElementA(Relationship r, Class<?>... types) {
        if (r instanceof Membership membership) {
            return isA(membership.getMemberElement(), types);
        }
        return false;
    }

    public static boolean isA(Element e, Class<?>... types) {
        if (e == null) {
            return false;
        } else {
            return Stream.of(types).filter(t -> t.isInstance(e)).findAny().isPresent();
        }
    }
}
