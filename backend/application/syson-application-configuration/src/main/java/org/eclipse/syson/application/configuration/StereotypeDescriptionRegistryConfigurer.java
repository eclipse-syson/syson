/*******************************************************************************
 * Copyright (c) 2023 Obeo.
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
package org.eclipse.syson.application.configuration;

import java.util.List;
import java.util.UUID;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.components.core.configuration.IStereotypeDescriptionRegistry;
import org.eclipse.sirius.components.core.configuration.IStereotypeDescriptionRegistryConfigurer;
import org.eclipse.sirius.components.core.configuration.StereotypeDescription;
import org.eclipse.syson.sysml.SysmlFactory;
import org.springframework.context.annotation.Configuration;

import io.micrometer.core.instrument.MeterRegistry;

/**
 * Configuration used to register new stereotype descriptions.
 *
 * @author sbegaudeau
 */
@Configuration
public class StereotypeDescriptionRegistryConfigurer implements IStereotypeDescriptionRegistryConfigurer {

    public static final UUID EMPTY_ID = UUID.nameUUIDFromBytes("empty".getBytes());

    public static final String EMPTY_LABEL = "Others...";

    public static final UUID EMPTY_SYSMLV2_ID = UUID.nameUUIDFromBytes("empty_sysmlv2".getBytes());

    public static final String EMPTY_SYSMLV2_LABEL = "SysMLv2";

    private static final String TIMER_NAME = "siriusweb_stereotype_load";

    private final StereotypeBuilder stereotypeBuilder;

    public StereotypeDescriptionRegistryConfigurer(MeterRegistry meterRegistry) {
        this.stereotypeBuilder = new StereotypeBuilder(TIMER_NAME, meterRegistry);
    }

    @Override
    public void addStereotypeDescriptions(IStereotypeDescriptionRegistry registry) {
        registry.add(new StereotypeDescription(EMPTY_SYSMLV2_ID, EMPTY_SYSMLV2_LABEL, this::getEmptySysMLv2StereotypeBody));
        registry.add(new StereotypeDescription(EMPTY_ID, EMPTY_LABEL, "New", this::getEmptyContent));
    }

    private String getEmptyContent() {
        return this.stereotypeBuilder.getStereotypeBody(List.of());
    }

    private String getEmptySysMLv2StereotypeBody() {
        return this.stereotypeBuilder.getStereotypeBody(getEmptySysMLv2Content());
    }

    public static List<EObject> getEmptySysMLv2Content() {
        var package1 = SysmlFactory.eINSTANCE.createPackage();
        package1.setDeclaredName("Package 1");
        return List.of(package1);
    }
}
