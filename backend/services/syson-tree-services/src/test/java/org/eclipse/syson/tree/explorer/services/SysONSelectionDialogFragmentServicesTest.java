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
package org.eclipse.syson.tree.explorer.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.RepresentationMetadata;
import org.eclipse.syson.tree.explorer.services.api.ISysONExplorerFragment;
import org.eclipse.syson.tree.selectiondialog.services.SysONSelectionDialogFragmentIdentityService;
import org.eclipse.syson.tree.selectiondialog.services.SysONSelectionDialogFragmentLabelService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for selection dialog services handling {@link ISysONExplorerFragment}.
 *
 * @author arichard
 */
@SuppressWarnings("checkstyle:MultipleStringLiterals")
public class SysONSelectionDialogFragmentServicesTest {

    @Test
    @DisplayName("GIVEN a selection dialog fragment identity service, WHEN the input is a fragment or not, THEN ids and kinds are resolved with the expected fallback")
    public void identityServiceHandlesFragmentsAndFallsBackForOtherObjects() {
        var service = new SysONSelectionDialogFragmentIdentityService();
        var fragment = new TestFragment("fragment-id", "fragment-kind", "Fragment", List.of("icons/LibraryResource.svg"));

        assertThat(service.canHandle(fragment)).isTrue();
        assertThat(service.canHandle("not-a-fragment")).isFalse();
        assertThat(service.getId(fragment)).isEqualTo("fragment-id");
        assertThat(service.getKind(fragment)).isEqualTo("fragment-kind");
        assertThat(service.getId("not-a-fragment")).isNull();
        assertThat(service.getKind("not-a-fragment")).isNull();
    }

    @Test
    @DisplayName("GIVEN a selection dialog fragment label service, WHEN the input is a fragment or not, THEN labels and icons are resolved with the expected fallback")
    public void labelServiceHandlesFragmentsAndFallsBackForOtherObjects() {
        var service = new SysONSelectionDialogFragmentLabelService();
        var fragment = new TestFragment("fragment-id", "fragment-kind", "Fragment", List.of("icons/LibraryResource.svg"));

        assertThat(service.canHandle(fragment)).isTrue();
        assertThat(service.canHandle("not-a-fragment")).isFalse();
        assertThat(service.getStyledLabel(fragment).toString()).isEqualTo("Fragment");
        assertThat(service.getImagePaths(fragment)).containsExactly("icons/LibraryResource.svg");
        assertThat(service.getStyledLabel("not-a-fragment").toString()).isEmpty();
        assertThat(service.getImagePaths("not-a-fragment")).isEmpty();
    }

    /**
     * Simple {@link ISysONExplorerFragment} stub used to exercise selection dialog delegates.
     *
     * @author arichard
     */
    private static final class TestFragment implements ISysONExplorerFragment {

        private final String id;

        private final String kind;

        private final String label;

        private final List<String> iconURL;

        private TestFragment(String id, String kind, String label, List<String> iconURL) {
            this.id = id;
            this.kind = kind;
            this.label = label;
            this.iconURL = iconURL;
        }

        @Override
        public String getId() {
            return this.id;
        }

        @Override
        public String getTooltip() {
            return this.label;
        }

        @Override
        public String getLabel() {
            return this.label;
        }

        @Override
        public String getKind() {
            return this.kind;
        }

        @Override
        public Object getParent() {
            return null;
        }

        @Override
        public List<String> getIconURL() {
            return this.iconURL;
        }

        @Override
        public boolean hasChildren(IEditingContext editingContext, List<RepresentationMetadata> existingRepresentations, List<String> expandedIds, List<String> activeFilterIds) {
            return false;
        }

        @Override
        public List<Object> getChildren(IEditingContext editingContext, List<RepresentationMetadata> existingRepresentations, List<String> expandedIds, List<String> activeFilterIds) {
            return List.of();
        }

        @Override
        public boolean isEditable() {
            return false;
        }

        @Override
        public boolean isDeletable() {
            return false;
        }

        @Override
        public boolean isSelectable() {
            return true;
        }
    }
}
