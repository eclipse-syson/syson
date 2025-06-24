/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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

import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.syson.services.data.ItemsModelTest;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.util.ModelBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test class for FeatureChainComputer.
 *
 * @author Arthur Daussy
 */
public class FeatureChainComputerTest {

    private ItemsModelTest model;

    @BeforeEach
    public void setUp() {
        this.model = new ItemsModelTest();
        this.model.getResourceSet().eAdapters().add(new ECrossReferenceAdapter());
    }

    @Test
    @DisplayName("Given a source type and a direct feature, when computing the shortest feature chain, then the result if a direct access to the feature")
    public void directAccess() {
        this.assertFeatures(this.computeShortestPath(this.model.getA11(), this.model.getI11Test()), this.model.getI11Test());
        this.assertFeatures(this.computeShortestPath(this.model.getA1(), this.model.getI1()), this.model.getI1());
    }

    @Test
    @DisplayName("Given a source and a feature accessed via 1 intermediate feature, when computing the shortest feature chain, then the result is composed of two feature segments")
    public void featureChain2Segments() {
        this.assertFeatures(this.computeShortestPath(this.model.getA1(), this.model.getId1()), this.model.getI2(), this.model.getId1());
    }

    @Test
    @DisplayName("Given a source and a feature accessed via 2 intermediate features, when computing the shortest feature chain, then the result is composed of two feature segments")
    public void featureChain3Segments() {
        this.assertFeatures(this.computeShortestPath(this.model.getA1(), this.model.getId2()), this.model.getI2(), this.model.getId1(),
                this.model.getId2());
        this.assertFeatures(this.computeShortestPath(this.model.getA0(), this.model.getI11()), this.model.getA1(), this.model.getA11(), this.model.getI11());
    }

    @Test
    @DisplayName("Given a source with no access to a feature defined at root level, when computing the shortest feature chain, then the result is composed with a feature chain starting at root level")
    public void rootAccessFeature() {
        this.assertFeatures(this.computeShortestPath(this.model.getExternalAction(), this.model.getI11()), this.model.getA0(), this.model.getA1(),
                this.model.getA11(), this.model.getI11());
    }

    @Test
    @DisplayName("Given a source and a feature accessed via 2 intermediate feature with one inherited, when computing the shortest feature chain, then the result is composed with a feature chain starting at root level")
    public void inheritedFeature() {
        // Expected path i0.i3 (i3 is accessed using i0 which typed by ID4 which inherits from ID3)
        this.assertFeatures(this.computeShortestPath(this.model.getA0(), this.model.getId3()), this.model.getI0(), this.model.getId3());
    }

    @Test
    @DisplayName("Given a source and a feature via multiple path, when computing all feature path, then all matching path are returned")
    public void multiplePath() {
        ModelBuilder builder = new ModelBuilder();
        // Makes i1 typed by ID4
        builder.setType(this.model.getI1(), this.model.getIdef4());
        // Makes i11Test typed by ID3
        builder.setType(this.model.getI11Test(), this.model.getIdef3());
        // Compute all path
        this.assertAllPathFeatures(this.computeAllPath(this.model.getA0(), this.model.getId3()), List.of(
                List.of(this.model.getI0(), this.model.getId3()),
                List.of(this.model.getA1(), this.model.getI1(), this.model.getId3()),
                List.of(this.model.getA1(), this.model.getA11(), this.model.getI11Test(), this.model.getId3())));
        // Compute shortest path
        this.assertFeatures(this.computeShortestPath(this.model.getA0(), this.model.getId3()), this.model.getI0(), this.model.getId3());
    }

    // Check against infinite loop
    @Test
    @DisplayName("Given a source and a feature using type infinit loop, when computing the shortest feature chain, then no stackoverflow is thrown")
    public void checkAgainstInfiniteLoop() {
        ModelBuilder builder = new ModelBuilder();
        builder.setType(this.model.getId2(), this.model.getIdef1());
        this.assertFeatures(this.computeShortestPath(this.model.getA1(), this.model.getId2()), this.model.getI2(), this.model.getId1(), this.model.getId2());
        builder.setType(this.model.getId1(), this.model.getIdef1());
        this.assertFeatures(this.computeShortestPath(this.model.getA1(), this.model.getId1()), this.model.getI2(), this.model.getId1());

    }

    private Optional<List<Feature>> computeShortestPath(Type source, Feature target) {
        return new FeatureChainComputer().computeShortestPath(source, target);
    }

    private List<List<Feature>> computeAllPath(Type source, Feature target) {
        return new FeatureChainComputer().computeAllPath(source, target);
    }

    private void assertFeatures(Optional<List<Feature>> features, Feature... expected) {
        assertTrue(features.isPresent());
        List<Feature> expecteds = List.of(expected);
        assertThat(features.get())
                .as("\nExpected :\n" + expecteds.stream().map(f -> f.getName()).collect(joining(",")) + "\n but was : \n" + features.get().stream().map(f -> f.getName()).collect(joining(",")))
                .isEqualTo(expecteds);
    }

    private String featuresToString(List<Feature> features) {
        return features.stream().map(f -> f.getName()).collect(joining(","));
    }

    private String pathToStringToString(List<List<Feature>> path) {
        return path.stream().map(this::featuresToString).collect(joining(",\n"));
    }

    private void assertAllPathFeatures(List<List<Feature>> path, List<List<Feature>> expectedPaths) {
        assertThat(path)
                .as("Expecting :\n" + this.pathToStringToString(expectedPaths) + "\n but was :\n " + this.pathToStringToString(path))
                .containsExactlyInAnyOrderElementsOf(expectedPaths);
    }

}
