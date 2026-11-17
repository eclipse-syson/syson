//
// Product Line Engineering Modeling Language (PLEML)
//
// The library provides an extension for SysML v2 to model PLE features.
//
// Version: v0.5
// License: MIT
// Maintainer: Tim Weilkiens, Contact: tim@mbse4u.com, Marco Forlingieri, Vince Molnar
// Topic: MBSE, MBPLE, PLE, Methodology
//
// MIT License
//
// Copyright (c) 2025 Tim Weilkiens, Marco Forlingieri, Vince Molnar
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.

library package <PLEML> 'Product Line Engineering Modeling Language' {
    doc /*
         * Definition of the Product Line Engineering Modeling Language (PLEML)
         *
         * PLEML is an extension for SysML v2 to enable the modeling of PLE Features.
         *
         *   PPPP   L     EEEEE  M   M  L
         *   P   P  L     E      MM MM  L
         *   PPPP   L     EEEE   M M M  L
         *   P      L     E      M   M  L
         *   P      LLLLL EEEEE  M   M  LLLLL
         *
         * Copyright 2025 Tim Weilkiens, Marco Forlingieri, Vince Molnar
         *
         */
    private import SequenceFunctions::*;

    occurrence def FeatureModel {
        occurrence featureTrees[*] : FeatureTree;
        occurrence featureConfigurations[*] : FeatureConfiguration;
    }
    occurrence featureModels[*] : FeatureModel;

    occurrence def FeatureConfiguration :> Feature {
        assert constraint :>> featureConstraints[*] {
            doc /* Assign the feature configuration to all feature constraints in the tree to enable their verification. */
            in occurrence :>> featureConfiguration = that;
        }
    }
    occurrence featureConfigurations[*] : FeatureConfiguration;

    occurrence def FeatureTree :> Feature {
        doc /* A Feature Tree is a tree structure that represents the features of a product line. */
    }
    occurrence featureTrees[*] : FeatureTree;

    occurrence def Feature {
        doc /*
            * A Feature is a characteristic of a product in a product line
            * that distinguishes it from other products in the product line.
            */
        attribute bindingTime : LifeCycleKind default LifeCycleKind::Development {
            doc /* The binding time of the feature with default Development. */
        }
        assert constraint featureConstraints[*] : FeatureConstraint {
            doc /* Set of all feature constraints owned by the feature. */
            in occurrence :>> featureConfiguration;
            in occurrence :>> owningFeature = that;
            in occurrence :>> constrainedFeatures;
        }
        assert constraint requiresFeatures[*] : RequiresConstraint :> featureConstraints {
            doc /* Set of REQUIRES constraints */
        }
        assert constraint xorFeatures[*] : XORConstraint :> featureConstraints {
            doc /* Set of XOR constraints (aka conflict constraints) */
        }
    }
    occurrence features[0..*] : Feature;

    enum def LifeCycleKind :> ScalarValues::Natural {
        doc /* Definition of feature binding times. The numbers provides an order of the life cycles. */
        enum Development = 1;
        enum Production = 2;
        enum Operation = 3;
    }

    abstract constraint def FeatureConstraint {
        doc /* Common abstract definition of a feature constraint. */
        in occurrence featureConfiguration: FeatureConfiguration;
        in occurrence owningFeature : Feature;
        in occurrence constrainedFeatures[0..*]: Feature;
    }

    constraint def RequiresConstraint :> FeatureConstraint {
        doc /* Definition of a REQUIRES constraint.
             *
             * Without a valid feature configuration,
             * no statement can be made and the constraint returns true.
             * With a valid feature configuration, it checks if the owning feature (=that)
             * is part of the feature configuration which implies that all requires features
             * must also be part of the feature configuration.
             */
        in occurrence requiredFeatures :>> constrainedFeatures;
        in occurrence owningFeature :>> owningFeature;
        in occurrence featureConfiguration :>> featureConfiguration;
        if featureConfiguration != null ?
            featureConfiguration.suboccurrences->includes(owningFeature)
                implies featureConfiguration.suboccurrences->includes(requiredFeatures)
        else
            true
    }

    constraint def XORConstraint :> FeatureConstraint {
        doc /* Definition of a XOR constraint.
                *
                * Without a valid feature configuration, no statement can be made,
                * and the constraint returns true. With a valid feature configuration,
                * it checks if the owning feature (=that) is part of the feature configuration,
                * which implies that all excluded features shall not be part of the feature configuration.
                */
        in occurrence featureConfiguration :>> featureConfiguration;
        in occurrence owningFeature :>> owningFeature;
        in occurrence excluded :>> constrainedFeatures;

        if featureConfiguration != null ?
            featureConfiguration.suboccurrences->includes(owningFeature)
                implies (1..size(excluded)-1)->ControlFunctions::forAll {
                    in f; not featureConfiguration.suboccurrences->includes(f)
                }
        else
            true
    }

    connection def FeatureBinding {
        doc /* Binds a feature to a shared asset. */
        end feature : Feature;
        end sharedAsset : Base::Anything;
    }
    abstract connection featureBindings[*] : FeatureBinding;

    //
    // Semantic Metadata for user-defined keywords
    //
    metadata def <featureModel> FeatureModelMetadata :> Metaobjects::SemanticMetadata {
        :>> baseType = featureModels meta SysML::OccurrenceUsage;
    }
    metadata def <featureTree> FeatureTreeMetadata :> Metaobjects::SemanticMetadata {
        :>> baseType = featureTrees meta SysML::OccurrenceUsage;
    }
    metadata def <feature> FeatureMetadata :> Metaobjects::SemanticMetadata {
        :>> baseType = features meta SysML::OccurrenceUsage;
    }
    metadata def <featureConfiguration> FeatureConfigurationMetadata :> Metaobjects::SemanticMetadata {
        :>> baseType = featureConfigurations meta SysML::OccurrenceUsage;
    }
    metadata def <featureBinding> FeatureBindingMetadata :> Metaobjects::SemanticMetadata {
        :>> baseType = featureBindings meta SysML::ConnectionUsage;
    }
}