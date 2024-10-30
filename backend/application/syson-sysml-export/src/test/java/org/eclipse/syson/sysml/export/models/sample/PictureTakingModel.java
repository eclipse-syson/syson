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
package org.eclipse.syson.sysml.export.models.sample;

import org.eclipse.syson.sysml.ActionDefinition;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.util.ModelBuilder;

/**
 * Implementation with our meta model of the PictureTaking from the examples provided by the OMG (examples/Camera
 * Example/PictureTaking.sysml).
 *
 * <b>Warning the "flow focus.xrsl to shoot.xsf;" is not implemented yet </b>
 *
 * <pre>
 * package PictureTaking {
        part def Exposure;

        action def Focus { out xrsl: Exposure; }
        action def Shoot { in xsf: Exposure; }

        action takePicture {
            action focus: Focus[1];
            flow focus.xrsl to shoot.xsf;
            action shoot: Shoot[1];
        }
    }
 * </pre>
 *
 * The content of UseCaseTest.sysml that have been copied below is under LGPL-3.0-only license. The LGPL-3.0-only
 * license is accessible at the root of this repository, in the LICENSE-LGPL file.
 *
 * @see https://github.com/Systems-Modeling/SysML-v2-Release/blob/master/sysml/src/examples/Camera%20Example/Camera.sysml
 * @author Arthur Daussy
 */
public class PictureTakingModel {

    private final ModelBuilder builder;

    private Package pictureTaking;

    private PartDefinition exposure;

    private ActionDefinition focusDefinition;

    private ReferenceUsage xrsl;

    private ActionDefinition shootDefinition;

    private ReferenceUsage xsf;

    private ActionUsage takePicture;

    private ActionUsage focusUsage;

    private ActionUsage shootUsage;

    public PictureTakingModel(ModelBuilder builder) {
        this.builder = builder;
        this.build();
    }

    public Package getPictureTaking() {
        return this.pictureTaking;
    }

    public PartDefinition getExposure() {
        return this.exposure;
    }

    public ActionDefinition getFocusDefinition() {
        return this.focusDefinition;
    }

    public ReferenceUsage getXrsl() {
        return this.xrsl;
    }

    public ActionDefinition getShootDefinition() {
        return this.shootDefinition;
    }

    public ReferenceUsage getXsf() {
        return this.xsf;
    }

    public ActionUsage getTakePicture() {
        return this.takePicture;
    }

    public ActionUsage getFocusUsage() {
        return this.focusUsage;
    }

    public ActionUsage getShootUsage() {
        return this.shootUsage;
    }

    private void build() {
        this.pictureTaking = this.builder.createWithName(Package.class, "PictureTaking");

        this.exposure = this.builder.createInWithName(PartDefinition.class, this.pictureTaking, "Exposure");

        this.focusDefinition = this.builder.createInWithName(ActionDefinition.class, this.pictureTaking, "Focus");

        this.xrsl = this.builder.createInWithName(ReferenceUsage.class, this.focusDefinition, "xrsl");
        this.xrsl.setDirection(FeatureDirectionKind.OUT);

        this.builder.setType(this.xrsl, this.exposure);

        this.shootDefinition = this.builder.createInWithName(ActionDefinition.class, this.pictureTaking, "Shoot");

        this.xsf = this.builder.createInWithName(ReferenceUsage.class, this.shootDefinition, "xsf");
        this.xsf.setDirection(FeatureDirectionKind.IN);

        this.builder.setType(this.xsf, this.exposure);

        this.takePicture = this.builder.createInWithName(ActionUsage.class, this.pictureTaking, "takePicture");

        this.focusUsage = this.builder.createInWithName(ActionUsage.class, this.takePicture, "focus");

        this.builder.setType(this.focusUsage, this.focusDefinition);

        this.builder.addIntMutiplicityRange(this.focusUsage, 1);

        // Missing "flow focus.xrsl to shoot.xsf;"

        this.shootUsage = this.builder.createInWithName(ActionUsage.class, this.takePicture, "shoot");

        this.builder.setType(this.shootUsage, this.shootDefinition);

        this.builder.addIntMutiplicityRange(this.shootUsage, 1);
    }
}
