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

package org.eclipse.syson.customnodes.metamodel;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.sirius.components.view.FixedColor;
import org.eclipse.sirius.components.view.ViewFactory;
import org.eclipse.sirius.components.view.diagram.LineStyle;
import org.eclipse.syson.sysmlcustomnodes.SysMLCustomnodesFactory;
import org.junit.jupiter.api.Test;

/**
 * Unit test for the imported package custom node style.
 * 
 * @author jgout
 */
public class SysMLImportedPackageNodeStyleDescriptionTests {

    @Test
    public void testImportedPackageNodeStyle() {
        FixedColor bgColor = ViewFactory.eINSTANCE.createFixedColor();
        bgColor.setValue("#123456");
        FixedColor borderColor = ViewFactory.eINSTANCE.createFixedColor();
        bgColor.setValue("#abcdef");

        var importedPackageNodeStyleDescription = SysMLCustomnodesFactory.eINSTANCE.createSysMLImportedPackageNodeStyleDescription();

        importedPackageNodeStyleDescription.setBackground(bgColor);
        importedPackageNodeStyleDescription.setBorderColor(borderColor);
        importedPackageNodeStyleDescription.setBorderLineStyle(LineStyle.DOT);
        importedPackageNodeStyleDescription.setBorderRadius(10);
        importedPackageNodeStyleDescription.setBorderSize(2);

        assertThat(importedPackageNodeStyleDescription.getBackground()).isEqualTo(bgColor);
        assertThat(importedPackageNodeStyleDescription.getBorderColor()).isEqualTo(borderColor);
        assertThat(importedPackageNodeStyleDescription.getBorderLineStyle()).isEqualTo(LineStyle.DOT);
        assertThat(importedPackageNodeStyleDescription.getBorderRadius()).isEqualTo(10);
        assertThat(importedPackageNodeStyleDescription.getBorderSize()).isEqualTo(2);
    }
}
