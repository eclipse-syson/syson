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
package org.eclipse.syson.diagram.general.view.nodes;

import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.builder.providers.INodeDescriptionProvider;
import org.eclipse.syson.sysml.AllocationDefinition;
import org.eclipse.syson.sysml.AllocationUsage;
import org.eclipse.syson.sysml.AttributeDefinition;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.ConstraintDefinition;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.EnumerationDefinition;
import org.eclipse.syson.sysml.EnumerationUsage;
import org.eclipse.syson.sysml.InterfaceDefinition;
import org.eclipse.syson.sysml.InterfaceUsage;
import org.eclipse.syson.sysml.ItemDefinition;
import org.eclipse.syson.sysml.ItemUsage;
import org.eclipse.syson.sysml.MetadataDefinition;
import org.eclipse.syson.sysml.MetadataUsage;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.PortDefinition;
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.SysmlEClassSwitch;

/**
 * Switch retrieving the list of NodeDescriptionProvider for each SysMLv2 concept represented in the General View diagram.
 * 
 * @author Jerome Gout
 */
public class GeneralViewNodeDescriptionProviderSwitch extends SysmlEClassSwitch<INodeDescriptionProvider> {
    
    private final IColorProvider colorProvider;

    public GeneralViewNodeDescriptionProviderSwitch(IColorProvider colorProvider) {
        this.colorProvider = colorProvider;
    }
    
    @Override
    public INodeDescriptionProvider caseAllocationDefinition(AllocationDefinition object) {
        return new DefinitionNodeDescriptionProvider(SysmlPackage.eINSTANCE.getAllocationDefinition(), colorProvider);
    }
    
    @Override
    public INodeDescriptionProvider caseAllocationUsage(AllocationUsage object) {
        return new UsageNodeDescriptionProvider(SysmlPackage.eINSTANCE.getAllocationUsage(), colorProvider);
    }
    
    @Override
    public INodeDescriptionProvider caseAttributeDefinition(AttributeDefinition object) {
        return new DefinitionNodeDescriptionProvider(SysmlPackage.eINSTANCE.getAttributeDefinition(), colorProvider);
    }
    
    @Override
    public INodeDescriptionProvider caseAttributeUsage(AttributeUsage object) {
        return new UsageNodeDescriptionProvider(SysmlPackage.eINSTANCE.getAttributeUsage(), colorProvider);
    }
    
    @Override
    public INodeDescriptionProvider caseConstraintDefinition(ConstraintDefinition object) {
        return new DefinitionNodeDescriptionProvider(SysmlPackage.eINSTANCE.getConstraintDefinition(), colorProvider);
    }
    
    @Override
    public INodeDescriptionProvider caseConstraintUsage(ConstraintUsage object) {
        return new UsageNodeDescriptionProvider(SysmlPackage.eINSTANCE.getConstraintUsage(), colorProvider);
    }
    
    @Override
    public INodeDescriptionProvider caseEnumerationDefinition(EnumerationDefinition object) {
        return new DefinitionNodeDescriptionProvider(SysmlPackage.eINSTANCE.getEnumerationDefinition(), colorProvider);
    }
    
    @Override
    public INodeDescriptionProvider caseEnumerationUsage(EnumerationUsage object) {
        return new UsageNodeDescriptionProvider(SysmlPackage.eINSTANCE.getEnumerationUsage(), colorProvider);
    }
    
    @Override
    public INodeDescriptionProvider caseInterfaceDefinition(InterfaceDefinition object) {
        return new DefinitionNodeDescriptionProvider(SysmlPackage.eINSTANCE.getInterfaceDefinition(), colorProvider);
    }
    
    @Override
    public INodeDescriptionProvider caseInterfaceUsage(InterfaceUsage object) {
        return new UsageNodeDescriptionProvider(SysmlPackage.eINSTANCE.getInterfaceUsage(), colorProvider);
    }
    
    @Override
    public INodeDescriptionProvider caseItemDefinition(ItemDefinition object) {
        return new DefinitionNodeDescriptionProvider(SysmlPackage.eINSTANCE.getItemDefinition(), colorProvider);
    }
    
    @Override
    public INodeDescriptionProvider caseItemUsage(ItemUsage object) {
        return new UsageNodeDescriptionProvider(SysmlPackage.eINSTANCE.getItemUsage(), colorProvider);
    }
    
    @Override
    public INodeDescriptionProvider caseMetadataDefinition(MetadataDefinition object) {
        return new DefinitionNodeDescriptionProvider(SysmlPackage.eINSTANCE.getMetadataDefinition(), colorProvider);
    }
    
    @Override
    public INodeDescriptionProvider caseMetadataUsage(MetadataUsage object) {
        return new UsageNodeDescriptionProvider(SysmlPackage.eINSTANCE.getMetadataUsage(), colorProvider);
    }
    
    @Override
    public INodeDescriptionProvider casePackage(Package object) {
        return new PackageNodeDescriptionProvider(colorProvider);
    }
    
    @Override
    public INodeDescriptionProvider casePartDefinition(PartDefinition object) {
        return new DefinitionNodeDescriptionProvider(SysmlPackage.eINSTANCE.getPartDefinition(), colorProvider);
    }
    
    @Override
    public INodeDescriptionProvider casePartUsage(PartUsage object) {
        return new UsageNodeDescriptionProvider(SysmlPackage.eINSTANCE.getPartUsage(), colorProvider);
    }
    
    @Override
    public INodeDescriptionProvider casePortDefinition(PortDefinition object) {
        return new DefinitionNodeDescriptionProvider(SysmlPackage.eINSTANCE.getPortDefinition(), colorProvider);
    }
    
    @Override
    public INodeDescriptionProvider casePortUsage(PortUsage object) {
        return new UsageNodeDescriptionProvider(SysmlPackage.eINSTANCE.getPortUsage(), colorProvider);
    }

}
