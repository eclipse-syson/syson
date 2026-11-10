/*******************************************************************************
 * Copyright (c) 2024, 2026 Obeo.
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
package org.eclipse.syson.sysml.metamodel.helper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Membership;

/**
 * Filter in charge of checking if element has a conflicting name or short name with a set a previously checked
 * elements.
 *
 * <p>
 * This filter only checks for elements that has at least an {@link Element#effectiveName()} or an
 * {@link Element#effectiveShortName()}. If an element has neither, it matches the given element since it can not create
 * any name conflict.
 * </p>
 *
 * @author Arthur Daussy
 */
public class NameConflictingFilter implements Predicate<Membership> {

    private final Set<String> usedNames = new HashSet<>();

    /**
     * Checks the membership's alias or effective names, reading its name and member element only once.
     *
     * @param member
     *            the membership to check, which may be {@code null}
     * @return whether the membership has no conflicting name
     */
    @Override
    public boolean test(Membership member) {
        boolean test = true;
        if (member != null) {
            String memberName = member.getMemberName();
            Element memberElement = member.getMemberElement();
            if (memberName != null && memberElement != null && memberName != memberElement.getName()) {
                test = this.checkConflictingNames(memberName);
            } else {
                if (memberElement != null) {
                    test = this.checkConflictingElement(memberElement);
                }
            }
        }

        return test;
    }

    public boolean checkConflictingElement(Element memberElement) {
        return this.checkConflictingNames(memberElement.effectiveName()) && this.checkConflictingNames(memberElement.effectiveShortName());
    }

    public boolean checkConflictingNames(String name) {

        boolean hasName = name != null;
        if (hasName) {
            boolean hasConflictingName = hasName && this.usedNames.contains(name);

            boolean noConflict = !hasConflictingName;
            if (noConflict) {
                // Only add to forbidden names if the element is not conflicting on both the shortname and the
                // name
                this.usedNames.add(name);
            }
            return noConflict;
        } // Else if the element has neither a name or a short name it can not create name conflict
        return true;
    }

    public void fillUsedNames(List<Membership> existingMembers) {
        for (Membership member : existingMembers) {
            Element memberElement = member.getMemberElement();
            if (memberElement != null) {
                String name = memberElement.effectiveName();
                String shortName = memberElement.effectiveShortName();
                if (name != null) {
                    this.usedNames.add(name);
                }
                if (shortName != null) {
                    this.usedNames.add(shortName);
                }
            }
        }
    }

}
