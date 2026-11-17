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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

import org.eclipse.emf.ecore.EObject;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Membership;

/**
 * A qualified name.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
public class QualifiedName {

    /**
     * The separator (::) constant.
     */
    public static final String SEPARATOR = "::";

    /**
     * The global ($) constant.
     */
    public static final String GLOBAL = "$";

    /**
     * The conjugated (~) constant.
     */
    public static final String CONJUGATED = "~";

    /**
     * Segments of the qualified name.
     */
    private final String[] segments;

    /**
     * Tells if the qualified name is conjugated (starts with <code>"~"</code>). If <code>true</code> the
     * <code>"~"</code> is not in the {@link #segments}.
     */
    private final boolean conjugated;

    /**
     * Tells if the qualified name is global (starts with <code>"$::"</code>). If <code>true</code> the
     * <code>"$::"</code> is not in the {@link #segments}.
     */
    private final boolean global;

    /**
     * Constructor.
     *
     * @param qualifiedName
     *            the {@link String} representation of the qualified name
     */
    public QualifiedName(String qualifiedName) {
        String[] tmpSegments = qualifiedName.split(SEPARATOR);

        if (tmpSegments[0].startsWith(CONJUGATED)) {
            this.conjugated = true;
            tmpSegments[0] = tmpSegments[0].substring(CONJUGATED.length());
        } else {
            this.conjugated = false;
        }

        if (GLOBAL.equals(tmpSegments[0])) {
            this.global = true;
            this.segments = Arrays.copyOfRange(tmpSegments, 1, tmpSegments.length);
        } else {
            this.global = false;
            this.segments = tmpSegments;
        }
        // un-escape each segment to match model names
        for (int i = 0; i < this.segments.length; i++) {
            if (this.segments[i].startsWith("'") && this.segments[i].endsWith("'")) {
                this.segments[i] = ParserUtils.getStringValue(this.segments[i]);
            }
        }
    }

    /**
     * Constructor.
     *
     * @param segments
     *            the segments of the qualified name
     */
    private QualifiedName(String[] segments, boolean conjugated, boolean global) {
        this.segments = segments;
        this.conjugated = conjugated;
        this.global = global;
    }

    /**
     * Tells if the qualified name is conjugated (starts with <code>"~"</code>). If <code>true</code> the
     * <code>"~"</code> is not in the {@link #segments}.
     *
     * @return <code>true</code> if the qualified name is global (starts with <code>"$::"</code>), <code>false</code>
     *         otherwise
     */
    public boolean isConjugated() {
        return this.conjugated;
    }

    /**
     * Tells if the qualified name is global (starts with <code>"$::"</code>). If <code>true</code> the
     * <code>"$::"</code> is not in the {@link #segments}.
     *
     * @return <code>true</code> if the qualified name is global (starts with <code>"$::"</code>), <code>false</code>
     *         otherwise
     */
    public boolean isGlobal() {
        return this.global;
    }

    /**
     * Tells if this qualified name reference a local {@link Element}.
     *
     * @return <code>true</code> if this qualified name reference a local {@link Element}, <code>false</code> otherwise
     */
    public boolean isLocal() {
        return this.segments.length == 1;
    }

    /**
     * Tells if this qualified name is empty (has no segment).
     *
     * @return <code>true</code> if this qualified name is empty (has no segment), <code>false</code> otherwise
     */
    public boolean isEmpty() {
        return this.segments.length == 0;
    }

    /**
     * Gets the first segment of the qualified name.
     *
     * @return the first segment of the qualified name
     */
    public String getFirstSegment() {
        return this.segments[0];
    }

    /**
     * Gets the last segment of the qualified name.
     *
     * @return the last segment of the qualified name
     */
    public String getLastSegment() {
        return this.segments[this.segments.length - 1];
    }

    /**
     * Gets the long notation.
     *
     * <ul>
     * <li>P -> P</li>
     * <li>~P -> P::~P</li>
     * <li>~N::P -> N::P::~P</li>
     * <li>~N::P::~P -> N::P</li>
     * <li>$::P -> $::P</li>
     * <li>~$::P -> $::P::~P</li>
     * <li>~$::N::P -> $::N::P::~P</li>
     * <li>~$::N::P::~P -> $::N::P</li>
     * </ul>
     *
     * @return the long notation
     */
    public QualifiedName getLongNotation() {
        QualifiedName res;

        if (this.isConjugated()) {
            String lastSegment = this.segments[this.segments.length - 1];
            if (lastSegment.startsWith(CONJUGATED)) {
                String[] tmpSegments = Arrays.copyOfRange(this.segments, 0, this.segments.length - 1);
                res = new QualifiedName(tmpSegments, false, this.isGlobal());
            } else {
                String[] tmpSegments = Arrays.copyOfRange(this.segments, 0, this.segments.length + 1);
                tmpSegments[tmpSegments.length - 1] = CONJUGATED + tmpSegments[tmpSegments.length - 2];
                res = new QualifiedName(tmpSegments, false, this.isGlobal());
            }
        } else {
            res = this;
        }

        return res;
    }

    /**
     * Gets the {@link QualifiedName} representing this {@link QualifiedName} without the first segment.
     *
     * @return the {@link QualifiedName} representing this {@link QualifiedName} without the first segment
     */
    public QualifiedName removeFirstSegment() {
        QualifiedName res;

        String[] localSegments;
        if (this.isConjugated()) {
            localSegments = this.getLongNotation().segments;
        } else {
            localSegments = this.segments;
        }
        String[] tmpSegments = Arrays.copyOfRange(localSegments, 1, localSegments.length);
        if (tmpSegments.length != 0) {
            res = new QualifiedName(tmpSegments, false, false);
        } else {
            res = null;
        }

        return res;
    }

    @Override
    public int hashCode() {
        int res = 0;

        for (String name : this.segments) {
            res ^= name.hashCode();
        }

        return res;
    }

    @Override
    public boolean equals(Object obj) {
        boolean res = true;

        if (obj instanceof QualifiedName) {
            QualifiedName other = (QualifiedName) obj;
            if (this.segments.length == other.segments.length) {
                for (int i = 0; i < this.segments.length; i++) {
                    if (!this.segments[i].equals(other.segments[i])) {
                        res = false;
                        break;
                    }
                }
            } else {
                res = false;
            }
        } else {
            res = false;
        }

        return res;
    }

    @Override
    public String toString() {
        StringJoiner res;

        if (this.isConjugated()) {
            res = new StringJoiner(SEPARATOR, CONJUGATED, "");
        } else {
            res = new StringJoiner(SEPARATOR);
        }

        if (this.isGlobal()) {
            res.add(GLOBAL);
        }
        for (String name : this.segments) {
            res.add(name);
        }

        return res.toString();
    }

    /**
     * Gets the containment global {@link QualifiedName} for the given {@link Membership}.
     *
     * @param membership
     *            the {@link Membership}
     * @return the containment global {@link QualifiedName} for the given {@link Membership}
     */
    public static QualifiedName getQualifiedNameForDiagnostics(Membership membership) {
        List<String> segments = new ArrayList<>();

        Membership currentMembership = membership;
        while (currentMembership != null) {
            if (currentMembership.getMemberName() != null) {
                segments.add(currentMembership.getMemberName());
            } else if (currentMembership.getMemberShortName() != null) {
                segments.add(currentMembership.getMemberShortName());
            } else {
                segments.add("<" + currentMembership.getElementId() + ">");
            }

            if (currentMembership.eContainer() != null) {
                EObject grandParent = currentMembership.eContainer().eContainer();
                if (grandParent instanceof Membership grandParentMembership) {
                    currentMembership = grandParentMembership;
                } else {
                    currentMembership = null;
                }

            } else {
                currentMembership = null;
            }
        }
        Collections.reverse(segments);

        return new QualifiedName(segments.toArray(new String[segments.size()]), false, true);
    }

}
