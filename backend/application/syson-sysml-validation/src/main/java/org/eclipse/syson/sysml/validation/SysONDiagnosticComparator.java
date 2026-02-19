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
package org.eclipse.syson.sysml.validation;

import java.util.Comparator;
import java.util.List;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.syson.sysml.Element;

/**
 * {@link Comparator} of {@link Diagnostic} such that the natural order it defines is the most practical for displaying
 * diagnostics to end users.
 * <p>
 * <b>Assumptions</b>: This implementation expects that, in general, for every diagnostic being compared:
 * <ul>
 * <li>The {@link Diagnostic#getSource() source} is the name of the validation rule that produced the diagnostic.</li>
 * <li>The first {@link Diagnostic#getData() data} is a reference to the {@link Element SysML element} targeted by the
 * rule that produced the diagnostic.</li>
 * </ul>
 * </p>
 * <p>
 * <b>Sorting order</b>:
 * <ul>
 * <li>This implementation first sorts diagnostics by the lexicographical order of their {@link Diagnostic#getSource()
 * source}.</li>
 * <li>When two diagnostics have the same source, then they are sorted according to their target {@link Element} as
 * follows:</li>
 * <ul>
 * <li>If both are from the same resource, then they are sorted according to their {@link Element#getQualifiedName()
 * qualified name} (if possible), otherwise we fallback to their {@link EcoreUtil#getID(EObject) ID}.</li>
 * <li>Otherwise, they are sorted according to the order of their containing resource in the containing
 * {@link ResourceSet}.</li>
 * </ul>
 * <li>Diagnostics without a {@link Diagnostic#getSource() source} that is a {@link String}, or without an
 * {@link Element} as its first {@link Diagnostic#getData() data}, or for which the {@link Element#getQualifiedName()
 * qualified name} is {@code null}, are superior (placed last in the natural order defined by {@code this}).</li>
 * </ul>
 * </p>
 *
 * @author flatombe
 */
public class SysONDiagnosticComparator implements Comparator<Diagnostic> {

    @Override
    public int compare(final Diagnostic left, final Diagnostic right) {
        final int result;

        final Object leftSource = left.getSource();
        final Object rightSource = right.getSource();

        // In SysMLValidator, the source is a non-null String that is the name of the rule.
        if (!(leftSource instanceof String)) {
            if (!(rightSource instanceof String)) {
                // Fallback to comparing by their data.
                result = this.compareDiagnosticsByTheirData(left, right);
            } else {
                // The 'left' diagnostic source is missing or not a String, while the 'right' one is.
                // We want the 'right' diagnostic to appear first in the natural order.
                result = 1; // left > right
            }
        } else if (!(rightSource instanceof String)) {
            // The 'left' diagnostic has a source String while the 'right' one does not.
            // We want the 'right' diagnostic to appear last in the natural order.
            result = -1; // left < right
        } else {
            final String leftRuleName = (String) leftSource;
            final String rightRuleName = (String) rightSource;
            if (leftRuleName.equals(rightRuleName)) {
                // For each rule that has produced several diagnostics, always sort in the same manner.
                result = this.compareDiagnosticsByTheirData(left, right);
            } else {
                result = leftRuleName.compareTo(rightRuleName);
            }
        }

        return result;
    }

    private int compareDiagnosticsByTheirData(final Diagnostic left, final Diagnostic right) {
        final int result;
        final List<?> leftData = left.getData();
        final List<?> rightData = right.getData();
        if (leftData.isEmpty()) {
            if (rightData.isEmpty()) {
                // Both diagnostics are missing the expected data.
                // Fallback to sorting message lexicographically.
                result = left.getMessage().compareTo(right.getMessage());
            } else {
                // The 'left' diagnostic does not have the expected data while the 'right' one does.
                // We want the 'right' diagnostic to appear first in the natural order.
                result = 1; // left > right
            }
        } else if (rightData.isEmpty()) {
            // The 'left' diagnostic has the expected data while the 'right' one does not.
            // We want the 'left' diagnostic to appear first in the natural order.
            result = -1; // left < right
        } else {
            final Object leftObject = leftData.get(0);
            final Object rightObject = rightData.get(0);
            if (!(leftObject instanceof EObject)) {
                if (!(rightObject instanceof EObject)) {
                    // Both diagnostics have the data but it is not of the expected type.
                    // Fallback to sorting message lexicographically.
                    result = left.getMessage().compareTo(right.getMessage());
                } else {
                    // The 'left' diagnostic data is not of the expected type, while the 'right' one is.
                    // We want the 'right' diagnostic to appear first in the natural order
                    result = 1; // left > right
                }
            } else if (!(rightObject instanceof EObject)) {
                // The 'left' diagnostic has the data of the expected type while the 'right' one does not.
                // We want the 'left' diagnostic to appear first in the natural order.
                result = -1; // left < right
            } else {
                final EObject leftEObject = (EObject) leftObject;
                final EObject rightEObject = (EObject) rightObject;
                final Resource leftResource = leftEObject.eResource();
                final Resource rightResource = rightEObject.eResource();
                if (leftResource != rightResource) {
                    // Sort by the order, in the ResourceSet, of the containing resources.
                    final ResourceSet resourceSet = leftResource.getResourceSet();
                    result = Integer.valueOf(resourceSet.getResources().indexOf(leftResource)).compareTo(Integer.valueOf(resourceSet.getResources().indexOf(rightResource)));
                } else {
                    result = this.compareEObjectsOfSameResource(leftEObject, rightEObject);
                }
            }
        }

        return result;
    }

    protected int compareEObjectsOfSameResource(final EObject leftEObject, final EObject rightEObject) {
        final int result;

        if (!(leftEObject instanceof Element)) {
            if (!(rightEObject instanceof Element)) {
                // Fallback to the EObject ID.
                result = EcoreUtil.getID(leftEObject).compareTo(EcoreUtil.getID(rightEObject));
            } else {
                // The 'left' is not from SysML while the 'right' is.
                // We want non-SysML elements to appear last in the natural order.
                result = 1; // left > right
            }
        } else if (!(rightEObject instanceof Element)) {
            // The 'left' is from SysML while the 'right' is not.
            // We want non-SysML elements to appear last in the natural order.
            result = -1; // left < right
        } else {
            final Element leftElement = (Element) leftEObject;
            final Element rightElement = (Element) rightEObject;

            // For some elements, the qualified name returned by SysON is null so we have to watch out for it during
            // the comparison.
            final String leftQualifiedName = leftElement.getQualifiedName();
            final String rightQualifiedName = rightElement.getQualifiedName();

            if (leftQualifiedName == null) {
                if (rightQualifiedName == null) {
                    // Both elements did not have qualified name
                    // Fallback to their ID
                    result = EcoreUtil.getID(leftElement).compareTo(EcoreUtil.getID(rightElement));
                } else {
                    // The 'left' qualified name is null while the 'right' one is not.
                    // We want null qualified names to appear last in the natural order.
                    result = 1; // left > right
                }
            } else if (rightQualifiedName == null) {
                // The 'left' qualified name is not null while the 'right' one is.
                // We want null qualified names to appear last in the natural order.
                result = -1; // left < right
            } else {
                // General case: inside a resource, SysML elements are sorted by the lexicographical
                // order of their qualified name.
                result = leftQualifiedName.compareTo(rightQualifiedName);
            }
        }
        return result;
    }
}
