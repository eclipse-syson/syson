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
package org.eclipse.syson.customnodes.metamodel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.sirius.components.view.FixedColor;
import org.eclipse.sirius.components.view.ViewFactory;
import org.eclipse.syson.sysmlcustomnodes.SysMLCustomnodesFactory;
import org.eclipse.syson.sysmlcustomnodes.SysMLCustomnodesPackage;
import org.eclipse.syson.sysmlcustomnodes.SysMLImportedPackageNodeStyleDescription;
import org.eclipse.syson.sysmlcustomnodes.SysMLNoteNodeStyleDescription;
import org.eclipse.syson.sysmlcustomnodes.SysMLPackageNodeStyleDescription;
import org.eclipse.syson.sysmlcustomnodes.SysMLViewFrameNodeStyleDescription;
import org.eclipse.syson.sysmlcustomnodes.util.SysMLCustomnodesAdapterFactory;
import org.eclipse.syson.sysmlcustomnodes.util.SysMLCustomnodesSwitch;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the generated custom-node switch and adapter factory.
 * <p>
 * The tests verify that each concrete custom-node style is dispatched to its dedicated switch case and accepted by
 * the model adapter factory. This protects the EMF classifier-to-type wiring used when Sirius Web reads custom node
 * style descriptions, while providing high-value coverage without constructing a representation or starting Spring.
 * </p>
 * <p>
 * They do not verify adapter implementations because the generated factory intentionally returns no adapters. They
 * also do not validate the visual rendering of the styles, which is the responsibility of the diagram runtime.
 * </p>
 *
 * @author arichard
 */
class SysMLCustomnodesSwitchTest {

    /**
     * Verifies dispatch to each custom-node style case.
     */
    @DisplayName("GIVEN each custom-node style, WHEN switching on it, THEN its dedicated case is selected")
    @Test
    void testSwitchDispatchesConcreteCustomNodeStyles() {
        SysMLCustomnodesSwitch<String> styleSwitch = new SysMLCustomnodesSwitch<>() {
            @Override
            public String caseSysMLPackageNodeStyleDescription(SysMLPackageNodeStyleDescription object) {
                return "package";
            }

            @Override
            public String caseSysMLNoteNodeStyleDescription(SysMLNoteNodeStyleDescription object) {
                return "note";
            }

            @Override
            public String caseSysMLImportedPackageNodeStyleDescription(SysMLImportedPackageNodeStyleDescription object) {
                return "imported-package";
            }

            @Override
            public String caseSysMLViewFrameNodeStyleDescription(SysMLViewFrameNodeStyleDescription object) {
                return "view-frame";
            }
        };

        assertThat(styleSwitch.doSwitch(SysMLCustomnodesFactory.eINSTANCE.createSysMLPackageNodeStyleDescription())).isEqualTo("package");
        assertThat(styleSwitch.doSwitch(SysMLCustomnodesFactory.eINSTANCE.createSysMLNoteNodeStyleDescription())).isEqualTo("note");
        assertThat(styleSwitch.doSwitch(SysMLCustomnodesFactory.eINSTANCE.createSysMLImportedPackageNodeStyleDescription())).isEqualTo("imported-package");
        assertThat(styleSwitch.doSwitch(SysMLCustomnodesFactory.eINSTANCE.createSysMLViewFrameNodeStyleDescription())).isEqualTo("view-frame");
    }

    /**
     * Verifies the model adapter factory applicability and default adapter creation path.
     */
    @DisplayName("GIVEN custom-node styles, WHEN adapting them, THEN the generated factory accepts every model object")
    @Test
    void testAdapterFactoryAcceptsAndAdaptsCustomNodeStyles() {
        SysMLCustomnodesAdapterFactory adapterFactory = new SysMLCustomnodesAdapterFactory();

        assertThat(adapterFactory.isFactoryForType(SysMLCustomnodesPackage.eINSTANCE)).isTrue();
        assertThat(adapterFactory.isFactoryForType(SysMLCustomnodesFactory.eINSTANCE.createSysMLPackageNodeStyleDescription())).isTrue();
        assertThat(adapterFactory.isFactoryForType(new Object())).isFalse();

        assertThat(adapterFactory.createAdapter(SysMLCustomnodesFactory.eINSTANCE.createSysMLPackageNodeStyleDescription())).isNull();
        assertThat(adapterFactory.createAdapter(SysMLCustomnodesFactory.eINSTANCE.createSysMLNoteNodeStyleDescription())).isNull();
        assertThat(adapterFactory.createAdapter(SysMLCustomnodesFactory.eINSTANCE.createSysMLImportedPackageNodeStyleDescription())).isNull();
        assertThat(adapterFactory.createAdapter(SysMLCustomnodesFactory.eINSTANCE.createSysMLViewFrameNodeStyleDescription())).isNull();
    }

    /**
     * Verifies classifier-based creation for every custom-node style and rejection of foreign EClasses.
     */
    @DisplayName("GIVEN custom-node EClasses, WHEN creating from their classifier, THEN the matching style is created")
    @Test
    void testFactoryCreatesCustomNodeStylesFromEClass() {
        assertThat(SysMLCustomnodesFactory.eINSTANCE.create(SysMLCustomnodesPackage.eINSTANCE.getSysMLPackageNodeStyleDescription()))
                .isInstanceOf(SysMLPackageNodeStyleDescription.class);
        assertThat(SysMLCustomnodesFactory.eINSTANCE.create(SysMLCustomnodesPackage.eINSTANCE.getSysMLNoteNodeStyleDescription()))
                .isInstanceOf(SysMLNoteNodeStyleDescription.class);
        assertThat(SysMLCustomnodesFactory.eINSTANCE.create(SysMLCustomnodesPackage.eINSTANCE.getSysMLImportedPackageNodeStyleDescription()))
                .isInstanceOf(SysMLImportedPackageNodeStyleDescription.class);
        assertThat(SysMLCustomnodesFactory.eINSTANCE.create(SysMLCustomnodesPackage.eINSTANCE.getSysMLViewFrameNodeStyleDescription()))
                .isInstanceOf(SysMLViewFrameNodeStyleDescription.class);
        assertThatIllegalArgumentException().isThrownBy(() -> SysMLCustomnodesFactory.eINSTANCE.create(EcoreFactory.eINSTANCE.createEClass()));
    }

    /**
     * Verifies that every custom style retains its configured background color.
     */
    @DisplayName("GIVEN custom-node styles, WHEN assigning their backgrounds, THEN each style retains its own color")
    @Test
    void testCustomNodeStylesRetainBackgroundColors() {
        FixedColor packageBackground = this.fixedColor("#112233");
        FixedColor noteBackground = this.fixedColor("#445566");
        FixedColor viewFrameBackground = this.fixedColor("#778899");
        SysMLPackageNodeStyleDescription packageStyle = SysMLCustomnodesFactory.eINSTANCE.createSysMLPackageNodeStyleDescription();
        SysMLNoteNodeStyleDescription noteStyle = SysMLCustomnodesFactory.eINSTANCE.createSysMLNoteNodeStyleDescription();
        SysMLViewFrameNodeStyleDescription viewFrameStyle = SysMLCustomnodesFactory.eINSTANCE.createSysMLViewFrameNodeStyleDescription();

        packageStyle.setBackground(packageBackground);
        noteStyle.setBackground(noteBackground);
        viewFrameStyle.setBackground(viewFrameBackground);

        assertThat(packageStyle.getBackground()).isSameAs(packageBackground);
        assertThat(noteStyle.getBackground()).isSameAs(noteBackground);
        assertThat(viewFrameStyle.getBackground()).isSameAs(viewFrameBackground);
    }

    /**
     * Creates a fixed color used by a custom-node style test.
     *
     * @param value
     *            the hexadecimal color value
     * @return the configured fixed color
     */
    private FixedColor fixedColor(String value) {
        FixedColor color = ViewFactory.eINSTANCE.createFixedColor();
        color.setValue(value);
        return color;
    }
}
