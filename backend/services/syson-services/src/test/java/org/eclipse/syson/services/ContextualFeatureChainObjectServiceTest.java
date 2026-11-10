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
package org.eclipse.syson.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.sirius.components.core.api.IDefaultIdentityService;
import org.eclipse.sirius.components.core.api.IDefaultLabelService;
import org.eclipse.sirius.components.core.api.IDefaultObjectSearchService;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.labels.StyledString;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.Type;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests virtual feature chain identity, presentation and resolution.
 *
 * @author Jerome Gout
 */
public class ContextualFeatureChainObjectServiceTest {

    private static final String CONTEXT_ID = "context-id";

    private static final String FIRST_FEATURE_ID = "first-feature-id";

    private static final String SECOND_FEATURE_ID = "second-feature-id";

    private final IEditingContext editingContext = new IEditingContext.NoOp();

    private final TestIdentityService identityService = new TestIdentityService();

    private final TestObjectSearchService searchService = new TestObjectSearchService(this.editingContext);

    private final TestLabelService labelService = new TestLabelService();

    private final ContextualFeatureChainObjectService contextualFeatureChainObjectService = new ContextualFeatureChainObjectService(this.identityService, this.searchService, this.labelService);

    private final Type context = SysmlFactory.eINSTANCE.createPartDefinition();

    @Test
    @DisplayName("GIVEN feature lists, WHEN constructing chains, THEN features are copied and invalid contents are rejected")
    public void chainDefensivelyCopiesItsFeaturesAndRejectsInvalidContents() {
        var feature = SysmlFactory.eINSTANCE.createPartUsage();
        var features = new ArrayList<Feature>(List.of(feature));
        var chain = new ContextualFeatureChain(this.context, features);
        features.clear();

        assertThat(chain.context()).isSameAs(this.context);
        assertThat(chain.features()).containsExactly(feature);
        assertThatThrownBy(() -> chain.features().clear()).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> new ContextualFeatureChain(this.context, List.of())).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new ContextualFeatureChain(this.context, null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new ContextualFeatureChain(null, List.of(feature))).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new ContextualFeatureChain(this.context, Arrays.asList(feature, null))).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("GIVEN a feature chain, WHEN requesting its presentation, THEN the label follows chain order and icons come from the last feature")
    public void labelsFollowChainOrderAndIconsComeFromTheLastFeature() {
        var part = SysmlFactory.eINSTANCE.createPartUsage();
        part.setDeclaredName("part");
        var port = SysmlFactory.eINSTANCE.createPortUsage();
        port.setDeclaredName("port");
        var chain = new ContextualFeatureChain(this.context, List.of(part, port));
        var icons = List.of("/icons/custom-port.svg");
        this.labelService.imagePaths.put(port, icons);

        assertThat(this.contextualFeatureChainObjectService.getStyledLabel(chain).toString()).isEqualTo("'part'.'port'");
        assertThat(this.contextualFeatureChainObjectService.getImagePaths(chain)).isEqualTo(icons);
        assertThat(this.contextualFeatureChainObjectService.canHandle(chain)).isTrue();
        assertThat(this.contextualFeatureChainObjectService.getKind(chain)).isEqualTo("ContextualFeatureChain");
        assertThat(this.labelService.requestedObjects).containsExactly(port);
    }

    @ParameterizedTest
    @CsvSource(value = { "|wh|wh", "wheel.diameter||'wheel.diameter'", "diameter|dia|diameter", "||<unnamed>" }, delimiter = '|', quoteCharacter = '"')
    @DisplayName("GIVEN named or unnamed features, WHEN requesting a path label, THEN names are escaped with short names and an unnamed fallback")
    public void labelsUseEscapedNamesAndFallbacks(String name, String shortName, String expectedLabel) {
        var feature = SysmlFactory.eINSTANCE.createPartUsage();
        feature.setDeclaredName(name);
        feature.setDeclaredShortName(shortName);
        var chain = new ContextualFeatureChain(this.context, List.of(feature));

        var label = this.contextualFeatureChainObjectService.getStyledLabel(chain).toString();

        assertThat(label).isEqualTo(expectedLabel);
    }

    @Test
    @DisplayName("GIVEN a dotted name and a two-segment path, WHEN requesting their labels, THEN segment boundaries remain distinct")
    public void labelsDistinguishDottedNamesFromMultipleSegments() {
        var dottedFeature = SysmlFactory.eINSTANCE.createPartUsage();
        dottedFeature.setDeclaredName("wheel.diameter");
        var wheel = SysmlFactory.eINSTANCE.createPartUsage();
        wheel.setDeclaredName("wheel");
        var diameter = SysmlFactory.eINSTANCE.createAttributeUsage();
        diameter.setDeclaredName("diameter");
        var unnamed = SysmlFactory.eINSTANCE.createAttributeUsage();

        var dottedLabel = this.contextualFeatureChainObjectService.getStyledLabel(new ContextualFeatureChain(this.context, List.of(dottedFeature))).toString();
        var pathLabel = this.contextualFeatureChainObjectService.getStyledLabel(new ContextualFeatureChain(this.context, List.of(wheel, diameter))).toString();
        var unnamedLabel = this.contextualFeatureChainObjectService.getStyledLabel(new ContextualFeatureChain(this.context, List.of(wheel, unnamed))).toString();

        assertThat(dottedLabel).isEqualTo("'wheel.diameter'");
        assertThat(pathLabel).isEqualTo("wheel.diameter").isNotEqualTo(dottedLabel);
        assertThat(unnamedLabel).isEqualTo("wheel.<unnamed>");
    }

    @Test
    @DisplayName("GIVEN an unsupported object, WHEN requesting its identity or presentation, THEN no identity, label or icon is returned")
    public void unsupportedObjectsHaveNoIdentityLabelOrIcon() {
        var feature = SysmlFactory.eINSTANCE.createPartUsage();

        assertThat(this.contextualFeatureChainObjectService.canHandle(feature)).isFalse();
        assertThat(this.contextualFeatureChainObjectService.canHandle(null)).isFalse();
        assertThat(this.contextualFeatureChainObjectService.getId(feature)).isNull();
        assertThat(this.contextualFeatureChainObjectService.getStyledLabel(feature).toString()).isEmpty();
        assertThat(this.contextualFeatureChainObjectService.getImagePaths(feature)).isEmpty();
        assertThat(this.labelService.requestedObjects).isEmpty();
    }

    @Test
    @DisplayName("GIVEN same-named features, WHEN identifying and resolving chains, THEN context, feature identity and order are preserved")
    public void idsDistinguishSameNamedFeaturesAndPreserveChainOrderOnResolution() {
        var resource = new ResourceImpl(URI.createURI("test:/model"));
        var first = SysmlFactory.eINSTANCE.createPartUsage();
        var second = SysmlFactory.eINSTANCE.createPartUsage();
        first.setDeclaredName("sameName");
        second.setDeclaredName("sameName");
        resource.getContents().add(this.context);
        this.addFeature(this.context, first);
        this.addFeature(first, second);
        this.registerObject(CONTEXT_ID, this.context);
        this.registerObject(FIRST_FEATURE_ID, first);
        this.registerObject(SECOND_FEATURE_ID, second);
        var chain = new ContextualFeatureChain(this.context, List.of(first, second));
        var id = this.contextualFeatureChainObjectService.getId(chain);

        assertThat(id).startsWith("contextual-feature-chain:");
        assertThat(this.contextualFeatureChainObjectService.canHandle(this.editingContext, id)).isTrue();
        assertThat(this.contextualFeatureChainObjectService.getId(new ContextualFeatureChain(this.context, List.of(first, second)))).isEqualTo(id);
        assertThat(this.contextualFeatureChainObjectService.getId(new ContextualFeatureChain(this.context, List.of(second, first)))).isNotEqualTo(id);
        assertThat(this.contextualFeatureChainObjectService.getId(new ContextualFeatureChain(second, List.of(first, second)))).isNotEqualTo(id);
        assertThat(this.contextualFeatureChainObjectService.getId(new ContextualFeatureChain(this.context, List.of(first))))
                .isNotEqualTo(this.contextualFeatureChainObjectService.getId(new ContextualFeatureChain(this.context, List.of(second))));
        assertThat(this.contextualFeatureChainObjectService.getObject(this.editingContext, id)).contains(chain);
        assertThat(this.searchService.searchedIds).containsExactly(CONTEXT_ID, FIRST_FEATURE_ID, SECOND_FEATURE_ID);
        assertThat(resource.getContents()).containsExactly(this.context);
        assertThat(first.getOwningType()).isSameAs(this.context);
        assertThat(second.getOwningType()).isSameAs(first);
    }

    @ParameterizedTest
    @ValueSource(booleans = { false, true })
    @DisplayName("GIVEN a saved path, WHEN its first or nested feature moves to another owner, THEN resolution rejects the inaccessible path")
    public void movedFeaturesInvalidateSavedPaths(boolean moveFirstFeature) {
        // part def Context { part wheel { attribute diameter; } part engine; }
        var wheel = SysmlFactory.eINSTANCE.createPartUsage();
        var diameter = SysmlFactory.eINSTANCE.createAttributeUsage();
        var engine = SysmlFactory.eINSTANCE.createPartUsage();
        this.addFeature(this.context, wheel);
        this.addFeature(wheel, diameter);
        this.addFeature(this.context, engine);
        this.registerObject(CONTEXT_ID, this.context);
        this.registerObject(FIRST_FEATURE_ID, wheel);
        this.registerObject(SECOND_FEATURE_ID, diameter);
        var chain = new ContextualFeatureChain(this.context, List.of(wheel, diameter));
        var id = this.contextualFeatureChainObjectService.getId(chain);
        assertThat(this.contextualFeatureChainObjectService.getObject(this.editingContext, id)).contains(chain);
        this.searchService.searchedIds.clear();

        Feature movedFeature = diameter;
        if (moveFirstFeature) {
            movedFeature = wheel;
        }
        engine.getOwnedRelationship().add(movedFeature.getOwningMembership());
        var resolved = this.contextualFeatureChainObjectService.getObject(this.editingContext, id);

        assertThat(resolved).isEmpty();
        assertThat(movedFeature.getOwningType()).isSameAs(engine);
        assertThat(this.searchService.objects.get(FIRST_FEATURE_ID)).isSameAs(wheel);
        assertThat(this.searchService.objects.get(SECOND_FEATURE_ID)).isSameAs(diameter);
        if (moveFirstFeature) {
            assertThat(this.searchService.searchedIds).containsExactly(CONTEXT_ID, FIRST_FEATURE_ID);
        } else {
            assertThat(this.searchService.searchedIds).containsExactly(CONTEXT_ID, FIRST_FEATURE_ID, SECOND_FEATURE_ID);
        }
    }

    @Test
    @DisplayName("GIVEN a path inherited through feature typing at each segment, WHEN resolving its identifier, THEN the path remains accessible")
    public void inheritedPathsRemainAccessible() {
        // part def Wheel { attribute diameter; }
        // part def Vehicle { part wheel : Wheel; }
        // part vehicle : Vehicle;
        var vehicleDefinition = SysmlFactory.eINSTANCE.createPartDefinition();
        var wheelDefinition = SysmlFactory.eINSTANCE.createPartDefinition();
        var vehicle = SysmlFactory.eINSTANCE.createPartUsage();
        var wheel = SysmlFactory.eINSTANCE.createPartUsage();
        var diameter = SysmlFactory.eINSTANCE.createAttributeUsage();
        this.addFeature(vehicleDefinition, wheel);
        this.addFeature(wheelDefinition, diameter);
        this.addType(vehicle, vehicleDefinition);
        this.addType(wheel, wheelDefinition);
        this.registerObject(CONTEXT_ID, vehicle);
        this.registerObject(FIRST_FEATURE_ID, wheel);
        this.registerObject(SECOND_FEATURE_ID, diameter);
        var chain = new ContextualFeatureChain(vehicle, List.of(wheel, diameter));
        var id = this.contextualFeatureChainObjectService.getId(chain);

        var resolved = this.contextualFeatureChainObjectService.getObject(this.editingContext, id);

        assertThat(resolved).contains(chain);
        assertThat(wheel.getOwningType()).isSameAs(vehicleDefinition);
        assertThat(diameter.getOwningType()).isSameAs(wheelDefinition);
        assertThat(this.searchService.searchedIds).containsExactly(CONTEXT_ID, FIRST_FEATURE_ID, SECOND_FEATURE_ID);
    }

    @ParameterizedTest
    @DisplayName("GIVEN an invalid identifier, WHEN resolving a chain, THEN no object is returned")
    @NullAndEmptySource
    @ValueSource(strings = { "other-id", "contextual-feature-chain:", "contextual-feature-chain:!" })
    public void invalidIdsCannotBeResolved(String id) {
        assertThat(this.contextualFeatureChainObjectService.getObject(this.editingContext, id)).isEmpty();
    }

    @Test
    @DisplayName("GIVEN an unrelated identifier, WHEN resolving a chain, THEN the default search service is not invoked")
    public void unrelatedIdsDoNotInvokeTheDefaultSearchService() {
        assertThat(this.contextualFeatureChainObjectService.canHandle(this.editingContext, null)).isFalse();
        assertThat(this.contextualFeatureChainObjectService.canHandle(this.editingContext, "other-id")).isFalse();
        assertThat(this.contextualFeatureChainObjectService.getObject(this.editingContext, "other-id")).isEmpty();
        assertThat(this.searchService.searchedIds).isEmpty();
    }

    @Test
    @DisplayName("GIVEN a malformed segment after a valid prefix, WHEN resolving a chain, THEN no partial chain is returned")
    public void malformedSegmentAfterAResolvedPrefixDoesNotReturnAPartialChain() {
        var feature = SysmlFactory.eINSTANCE.createPartUsage();
        this.addFeature(this.context, feature);
        this.searchService.objects.put("context", this.context);
        this.searchService.objects.put("foo", feature);

        assertThat(this.contextualFeatureChainObjectService.getObject(this.editingContext, "contextual-feature-chain:Y29udGV4dA.Zm9v.!")).isEmpty();
        assertThat(this.searchService.searchedIds).containsExactly("context", "foo");
    }

    @ParameterizedTest
    @DisplayName("GIVEN a missing or non-Type context, WHEN resolving a chain, THEN feature resolution is skipped")
    @ValueSource(booleans = { false, true })
    public void unresolvedOrNonTypeContextPreventsFeatureResolution(boolean wrongType) {
        if (wrongType) {
            this.searchService.objects.put("context", SysmlFactory.eINSTANCE.createPackage());
        }
        this.searchService.objects.put("foo", SysmlFactory.eINSTANCE.createPartUsage());

        assertThat(this.contextualFeatureChainObjectService.getObject(this.editingContext, "contextual-feature-chain:Y29udGV4dA.Zm9v")).isEmpty();
        assertThat(this.searchService.searchedIds).containsExactly("context");
    }

    @ParameterizedTest
    @DisplayName("GIVEN a missing or non-Feature segment, WHEN resolving a chain, THEN resolution stops without returning a partial chain")
    @ValueSource(booleans = { false, true })
    public void unresolvedOrNonFeatureSegmentsStopResolutionWithoutReturningAPartialChain(boolean wrongType) {
        var resource = new ResourceImpl(URI.createURI("test:/model"));
        var first = SysmlFactory.eINSTANCE.createPartUsage();
        var second = SysmlFactory.eINSTANCE.createPartUsage();
        var third = SysmlFactory.eINSTANCE.createPartUsage();
        resource.getContents().addAll(List.of(this.context, first, second, third));
        this.addFeature(this.context, first);
        this.addFeature(first, second);
        this.addFeature(second, third);
        this.identityService.ids.put(this.context, CONTEXT_ID);
        this.identityService.ids.put(first, FIRST_FEATURE_ID);
        this.identityService.ids.put(second, SECOND_FEATURE_ID);
        this.identityService.ids.put(third, "third-feature-id");
        this.searchService.objects.put(CONTEXT_ID, this.context);
        this.searchService.objects.put(FIRST_FEATURE_ID, first);
        if (wrongType) {
            this.searchService.objects.put(SECOND_FEATURE_ID, SysmlFactory.eINSTANCE.createPackage());
        }
        var id = this.contextualFeatureChainObjectService.getId(new ContextualFeatureChain(this.context, List.of(first, second, third)));

        assertThat(this.contextualFeatureChainObjectService.getObject(this.editingContext, id)).isEmpty();
        assertThat(this.searchService.searchedIds).containsExactly(CONTEXT_ID, FIRST_FEATURE_ID, SECOND_FEATURE_ID);
    }

    private void addFeature(Type owner, Feature feature) {
        var membership = SysmlFactory.eINSTANCE.createFeatureMembership();
        owner.getOwnedRelationship().add(membership);
        membership.getOwnedRelatedElement().add(feature);
    }

    private void addType(Feature feature, Type type) {
        var typing = SysmlFactory.eINSTANCE.createFeatureTyping();
        feature.getOwnedRelationship().add(typing);
        typing.setType(type);
        typing.setTypedFeature(feature);
    }

    private void registerObject(String id, Object object) {
        this.identityService.ids.put(object, id);
        this.searchService.objects.put(id, object);
    }

    /**
     * Test identity service providing configured identifiers independently of EMF URIs.
     *
     * @author Jerome Gout
     */
    private static final class TestIdentityService extends IDefaultIdentityService.NoOp {

        private final Map<Object, String> ids = new HashMap<>();

        @Override
        public String getId(Object object) {
            return this.ids.get(object);
        }
    }

    /**
     * Test label service providing configured icons and recording the objects requested.
     *
     * @author Jerome Gout
     */
    private static final class TestLabelService implements IDefaultLabelService {

        private final Map<Object, List<String>> imagePaths = new HashMap<>();

        private final List<Object> requestedObjects = new ArrayList<>();

        @Override
        public StyledString getStyledLabel(Object object) {
            this.requestedObjects.add(object);
            return StyledString.of("");
        }

        @Override
        public List<String> getImagePaths(Object object) {
            this.requestedObjects.add(object);
            return this.imagePaths.getOrDefault(object, List.of());
        }
    }

    /**
     * Configurable NoOp search service checking the editing context and recording the requested identifiers.
     *
     * @author Jerome Gout
     */
    private static final class TestObjectSearchService extends IDefaultObjectSearchService.NoOp {

        private final IEditingContext expectedEditingContext;

        private final Map<String, Object> objects = new HashMap<>();

        private final List<String> searchedIds = new ArrayList<>();

        private TestObjectSearchService(IEditingContext expectedEditingContext) {
            this.expectedEditingContext = expectedEditingContext;
        }

        @Override
        public Optional<Object> getObject(IEditingContext editingContext, String objectId) {
            assertThat(editingContext).isSameAs(this.expectedEditingContext);
            this.searchedIds.add(objectId);
            return Optional.ofNullable(this.objects.get(objectId));
        }
    }
}
