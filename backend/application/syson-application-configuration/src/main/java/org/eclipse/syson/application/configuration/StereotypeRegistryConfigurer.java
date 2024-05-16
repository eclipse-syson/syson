/*******************************************************************************
 * Copyright (c) 2023, 2024 Obeo.
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
import org.eclipse.sirius.web.services.api.document.IStereotypeRegistry;
import org.eclipse.sirius.web.services.api.document.IStereotypeRegistryConfigurer;
import org.eclipse.sirius.web.services.api.document.Stereotype;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.util.ElementUtil;
import org.springframework.context.annotation.Configuration;

import io.micrometer.core.instrument.MeterRegistry;

/**
 * Configuration used to register new stereotype descriptions.
 *
 * @author sbegaudeau
 */
@Configuration
public class StereotypeRegistryConfigurer implements IStereotypeRegistryConfigurer {

    public static final UUID EMPTY_ID = UUID.nameUUIDFromBytes("empty".getBytes());

    public static final String EMPTY_LABEL = "Others...";

    public static final UUID EMPTY_SYSMLV2_ID = UUID.nameUUIDFromBytes("empty_sysmlv2".getBytes());

    public static final String EMPTY_SYSMLV2_LABEL = "SysMLv2";

    private static final String TIMER_NAME = "siriusweb_stereotype_load";

    private final StereotypeBuilder stereotypeBuilder;

    public StereotypeRegistryConfigurer(MeterRegistry meterRegistry) {
        this.stereotypeBuilder = new StereotypeBuilder(TIMER_NAME, meterRegistry);
    }

    @Override
    public void addStereotypes(IStereotypeRegistry registry) {
        registry.add(new Stereotype(EMPTY_SYSMLV2_ID, EMPTY_SYSMLV2_LABEL, this::getEmptySysMLv2StereotypeBody));
        registry.add(new Stereotype(EMPTY_ID, EMPTY_LABEL, this::getEmptyContent));
    }

    private String getEmptyContent() {
        return this.stereotypeBuilder.getStereotypeBody(List.of());
    }

    private String getEmptySysMLv2StereotypeBody() {
        return this.stereotypeBuilder.getStereotypeBody(getEmptySysMLv2Content());
    }

    public static List<EObject> getEmptySysMLv2Content() {
        var rootNamespace = SysmlFactory.eINSTANCE.createNamespace();
        var rootMembership = SysmlFactory.eINSTANCE.createOwningMembership();
        var package1 = SysmlFactory.eINSTANCE.createPackage();
        rootNamespace.getOwnedRelationship().add(rootMembership);
        rootMembership.getOwnedRelatedElement().add(package1);
        package1.setDeclaredName("Package 1");
        package1.setElementId(ElementUtil.generateUUID(package1).toString());
        return List.of(rootNamespace);
    }
}
