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

import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.NamespaceImport;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.PerformActionUsage;
import org.eclipse.syson.sysml.util.ModelBuilder;

/**
 * Implementation with our meta model of the PictureTaking from the examples provided by the OMG (examples/Camera
 * Example/PictureTaking.sysml).
 *
 * <b>Warning the "flow focus.xrsl to shoot.xsf;" is not implemented yet </b>
 *
 * <pre>
 * part def Camera {
        import PictureTaking::*;

        perform action takePicture[*] :> PictureTaking::takePicture;

        part focusingSubsystem {
            perform takePicture.focus;
        }

        part imagingSubsystem {
            perform takePicture.shoot;
        }

    }
 * </pre>
 *
 * The content of UseCaseTest.sysml that have been copied below is under LGPL-3.0-only license. The LGPL-3.0-only
 * license is accessible at the root of this repository, in the LICENSE-LGPL file.
 *
 * @see https://github.com/Systems-Modeling/SysML-v2-Release/blob/master/sysml/src/examples/Camera%20Example/PictureTaking.sysml
 * @author Arthur Daussy
 */
public class CameraModel {

    private final ModelBuilder builder;

    private final PictureTakingModel pictureTakingModel;

    private PartDefinition camera;

    private PerformActionUsage takePicture;

    private PartUsage focusingSubsystem;

    private PerformActionUsage focusingSubsystemPerformAction;

    private PartUsage imagingSubsystem;

    private PerformActionUsage imagingSubsystemPerformAction;

    public CameraModel(ModelBuilder builder, PictureTakingModel pictureTakingModel) {
        this.builder = builder;
        this.pictureTakingModel = pictureTakingModel;
        this.build();
    }

    public PictureTakingModel getPictureTakingModel() {
        return this.pictureTakingModel;
    }

    public PartDefinition getCamera() {
        return this.camera;
    }

    public PerformActionUsage getTakePicture() {
        return this.takePicture;
    }

    public PartUsage getFocusingSubsystem() {
        return this.focusingSubsystem;
    }

    public PerformActionUsage getFocusingSubsystemPerformAction() {
        return this.focusingSubsystemPerformAction;
    }

    public PartUsage getImagingSubsystem() {
        return this.imagingSubsystem;
    }

    public PerformActionUsage getImagingSubsystemPerformAction() {
        return this.imagingSubsystemPerformAction;
    }

    private void build() {
        this.camera = this.builder.createWithName(PartDefinition.class, "Camera");

        this.builder.createIn(NamespaceImport.class, this.camera).setImportedNamespace(this.pictureTakingModel.getPictureTaking());

        this.takePicture = this.builder.createInWithName(PerformActionUsage.class, this.camera, "takePicture");

        this.builder.addInfinityMutiplicityRange(this.takePicture);

        this.builder.addSubsetting(this.takePicture, this.pictureTakingModel.getTakePicture());

        this.focusingSubsystem = this.builder.createInWithName(PartUsage.class, this.camera, "focusingSubsystem");

        this.focusingSubsystemPerformAction = this.builder.createIn(PerformActionUsage.class, this.focusingSubsystem);

        Feature focusFeatureChain = this.builder.createFeatureChaining(this.takePicture, this.pictureTakingModel.getFocusUsage());

        this.builder.addReferenceSubsetting(this.focusingSubsystemPerformAction, focusFeatureChain);

        this.imagingSubsystem = this.builder.createInWithName(PartUsage.class, this.camera, "imagingSubsystem");

        this.imagingSubsystemPerformAction = this.builder.createIn(PerformActionUsage.class, this.imagingSubsystem);

        Feature shootFeatureChain = this.builder.createFeatureChaining(this.takePicture, this.pictureTakingModel.getShootUsage());

        this.builder.addReferenceSubsetting(this.imagingSubsystemPerformAction, shootFeatureChain);
    }
}
