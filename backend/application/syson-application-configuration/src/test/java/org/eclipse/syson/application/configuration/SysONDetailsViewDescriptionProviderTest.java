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
package org.eclipse.syson.application.configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.sirius.components.collaborative.widget.reference.api.ReferenceWidgetVariables;
import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.sirius.components.core.api.ILabelService;
import org.eclipse.sirius.components.interpreter.AQLInterpreter;
import org.eclipse.sirius.components.representations.VariableManager;
import org.eclipse.sirius.components.view.ChangeContext;
import org.eclipse.sirius.components.view.View;
import org.eclipse.sirius.components.view.widget.reference.ReferenceWidgetDescription;
import org.eclipse.sirius.web.application.editingcontext.EditingContext;
import org.eclipse.syson.form.services.FormMutationElementService;
import org.eclipse.syson.form.services.FormQueryElementService;
import org.eclipse.syson.form.services.aql.FormMutationAQLService;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.junit.jupiter.api.Test;

/**
 * Tests the reference widget actions in the SysON details view.
 *
 * @author arichard
 */
public class SysONDetailsViewDescriptionProviderTest {

    /**
     * Checks that Core and Advanced reference actions use the selection handler variables without the form iterator.
     */
    @Test
    public void setSubsettedFeatureFromSelection() {
        var editingContext = mock(EditingContext.class);
        List<View> views = new ArrayList<>();
        when(editingContext.getViews()).thenReturn(views);
        new SysONDetailsViewDescriptionProvider().preProcess(editingContext);

        List<ReferenceWidgetDescription> widgets = new ArrayList<>();
        views.get(0).eAllContents().forEachRemaining(object -> {
            if (object instanceof ReferenceWidgetDescription widget && "ReferenceWidget".equals(widget.getName())) {
                widgets.add(widget);
            }
        });
        assertThat(widgets).hasSize(2);

        var queryService = new FormQueryElementService(List.of(), element -> false, List.of(), new ILabelService.NoOp());
        var mutationService = new FormMutationElementService(new IFeedbackMessageService.NoOp(), queryService);
        var interpreter = new AQLInterpreter(List.of(), List.of(new FormMutationAQLService(mutationService)), List.of(SysmlPackage.eINSTANCE, EcorePackage.eINSTANCE));
        for (var widget : widgets) {
            var subsetting = SysmlFactory.eINSTANCE.createSubsetting();
            subsetting.setIsImplied(true);
            var feature = SysmlFactory.eINSTANCE.createAttributeUsage();
            var variables = Map.<String, Object>of(
                    VariableManager.SELF, subsetting,
                    ReferenceWidgetVariables.REFERENCE_OWNER.name(), subsetting,
                    ReferenceWidgetVariables.REFERENCE.name(), SysmlPackage.eINSTANCE.getSubsetting_SubsettedFeature(),
                    ReferenceWidgetVariables.NEW_VALUE.name(), feature);
            var operation = (ChangeContext) widget.getSet().getBody().get(0);

            var result = interpreter.evaluateExpression(variables, operation.getExpression());

            assertThat(result.asObject()).contains(subsetting);
            assertThat(subsetting.getSubsettedFeature()).isSameAs(feature);
            assertThat(subsetting.isIsImplied()).isFalse();
        }
    }
}
