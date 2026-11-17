//
// Probability and Sampling library
//
// Library for modeling probabilities and sampling. Is used by MDAO concepts but can be usefull in another context.
//
// This library has been developed as part of the ODE4HERA project: https://www.ode4hera.eu/
//
// Version: v2.0
// License: MIT
// Maintainer: DLR Institute of System Architectures in Aeronautics
// Contact: digital.engineering@dlr.de
// Topics: MDAO, Multidisciplinary Design Analysis and Optimization
//
// MIT License
//
// Copyright: (c) 2026, The Contributors to the ODE4HERA SysML v2 Libraries.
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

library package ProbabilityAndSampling {
    private import ScalarValues::*;

    action def StatisticsDefinition {
        doc /* Definition of the statistics of interest and how they are estimated
        from the uncertain variables and model outputs. */

        attribute statisticType:StatisticType {
                doc /* The type of statistic of interest, e.g. mean, variance,
                probability of failure, etc. */
        }

        attribute estimatorTechnique : StatisticEstimatorTechnique {
            doc /* The high-level technique used to estimate the statistic,
                e.g. sampling-based Monte Carlo, multilevel sampling,
                Taylor approximation, or surrogate-based. */
        }

        attribute confidenceLevel[0..1] : ScalarValues::Real {
            doc /* Optional confidence level for the statistic estimator,
                e.g. 0.95 for a 95% confidence interval. */
        }

        attribute useControlVariates[0..1] : ScalarValues::Boolean default false {
            doc /* Flag indicating whether control variates are used as a
                variance reduction technique during statistic estimation. */
        }

        part estimationSettings[0..1] : StatisticEstimationSettings {
            doc /* Technique-specific settings for statistic estimation.
                For example: SurrogateEstimationSettings,
                Sampling-based settings (if needed), etc. */
        }

        /*assert constraint {
            (estimatorTechnique == StatisticEstimatorTechnique::SurrogateBased) implies estimationSettings.isKindOf(SurrogateEstimationSettings)
        }

        assert constraint {
            (estimatorTechnique == StatisticEstimatorTechnique::Sampling) implies estimationSettings.isKindOf(SamplingEstimationSettings)
        }*/

    }

     part def StatisticEstimationSettings {
        doc /* Abstract base for settings controlling how a statistic is
        estimated (sampling, surrogate, Taylor polynomial, etc.). */
    }

    part def SamplingEstimationSettings :> StatisticEstimationSettings {
        doc /* Settings for sampling-based estimation of statistics.
            Represents estimator-related parameters, not the sampling
            experiment itself. */

        attribute sampleSize[0..1] : ScalarValues::Integer {
            doc /* Optional batch size for sample grouping or incremental
                estimation updates. Useful for sequential or streaming
                estimators. */
        }
    }

    part def SurrogateEstimationSettings :> StatisticEstimationSettings {
        doc /* Settings for surrogate-based estimation of statistics.
        Equivalent to Surrogate_Settings(n_samples=...). */

        attribute nTrainingSamples[1] : ScalarValues::Integer {
            doc /* Number of model evaluations used to train/fit the surrogate
                per optimization iteration. Corresponds to GEMSEO
                Surrogate_Settings(n_samples=N). */
        }
    }

    action def SamplingDefinition {
        doc /* Definition of a sampling-based uncertainty propagation experiment.
            A sampling definition specifies how many samples are used and
            which sampling method generates realizations of the uncertain
            variables. */

        attribute method : SamplingMethod {
            doc /* The sampling method used to draw realizations of the
                uncertain variables, e.g. Monte Carlo, Latin hypercube,
                low-discrepancy sequences, multilevel Monte Carlo, etc. */
        }

        attribute nSamples[1] : ScalarValues::Integer {
            doc /* Nominal number of samples used for the estimation of
                statistics (per iteration or in total, depending on the
                UMDO formulation). */
        }

        attribute isSequential[0..1] : ScalarValues::Boolean default false {
            doc /* True if the sampling is sequential/adaptive, i.e. the
                sample size is increased iteratively based on convergence
                criteria rather than fixed a priori. */
        }

        attribute maxSamples[0..1] : ScalarValues::Integer {
            doc /* Optional upper bound on the total number of samples in a
                sequential sampling scheme. */
        }
    }
    
    attribute ProbabilityDistribution : Base::DataValue {
        doc /* Base class defining common properties for all probability distributions.
        Individual distribution types refine this with their specific parameters. */

        attribute parameters[*] : Quantities::ScalarQuantityValue {
            doc /* Generic dictionary of parameters used by external tools (optional).
                This attribute captures untyped parameter values imported from 
                third-party UQ libraries such as GEMSEO or OpenTURNS. */
        }

        attribute lowerBound[0..1] : ScalarValues::Real {
            doc /* Optional lower bound truncation of the distribution.
                If specified, probability density outside this bound is removed 
                and the distribution renormalized. */
        }

        attribute upperBound[0..1] : ScalarValues::Real {
            doc /* Optional upper bound truncation of the distribution.
                If specified, probability density outside this bound is removed 
                and the distribution renormalized. */
        }

        assert constraint {lowerBound < upperBound }
    }

    attribute BetaDistribution :> ProbabilityDistribution {
        doc /* Beta(α, β) distribution defined over the interval (0, 1). 
            It is a flexible family used for modeling random variables with bounded support. */

        attribute alpha[1] : ScalarValues::Real {
            doc /* First shape parameter α > 0, controlling left-tail behavior. */
        }

        attribute beta[1] : ScalarValues::Real {
            doc /* Second shape parameter β > 0, controlling right-tail behavior. */
        }

        assert constraint { alpha > 0 and beta > 0 }
    }

    attribute DiracDistribution :> ProbabilityDistribution {
        doc /* Dirac distribution concentrated at a single point.
            Represents a purely deterministic variable expressed in a probabilistic context. */

        attribute value[1] : ScalarValues::Real {
            doc /* Location of the Dirac mass. The distribution assigns probability 1 to this value. */
        }

        assert constraint { lowerBound <= value and upperBound >= value }
    }

    attribute ExponentialDistribution :> ProbabilityDistribution {
        doc /* Exponential(λ) distribution defined over (0, +∞); 
            commonly used for lifetime and failure-time modeling. */

        attribute lambda[1] : ScalarValues::Real {
            doc /* Rate parameter λ > 0. Expected value = 1/λ; variance = 1/λ². */
        }

        assert constraint { lambda > 0 }

        assert constraint { lowerBound >= 0.0 }
    }

    attribute LogNormalDistribution :> ProbabilityDistribution {
        doc /* LogNormal(μ_log, σ_log) distribution; 
            X = exp(Normal(μ_log, σ_log)).
            Models positive-valued uncertainties with multiplicative effects. */

        attribute meanLog[1] : ScalarValues::Real {
            doc /* μ_log: mean of the associated normal distribution in log-space. */
        }

        attribute stdDevLog[1] : ScalarValues::Real {
            doc /* σ_log: standard deviation of the associated normal distribution in log-space. */
        }

        assert constraint { stdDevLog > 0 }

        assert constraint { lowerBound > 0.0 }
    }

    attribute NormalDistribution :> ProbabilityDistribution {
        doc /* Normal(μ, σ) distribution, also called the Gaussian distribution.
            Models symmetric, unbounded uncertainties around a mean value. */

        attribute mean : ScalarValues::Real {
            doc /* Mean (location parameter) of the distribution. */
        }

        attribute stdDev : ScalarValues::Real {
            doc /* Standard deviation (scale parameter) σ > 0. */
        }

        assert constraint { stdDev > 0 }
    }

    attribute TriangularDistribution :> ProbabilityDistribution {
        doc /* Triangular(a, m, b) distribution defined by minimum (min), 
            mode (mode), and maximum (max). Useful when expert knowledge 
            provides three key points rather than a full distribution. */

        attribute min[1] : Real {
            doc /* Lower limit of support. */
        }

        attribute mode[1] : Real {
            doc /* Most likely value (mode). */
        }

        attribute max[1] : Real {
            doc /* Upper limit of support. */
        }

        assert constraint { min < mode and mode < max }
    }

    attribute UniformDistribution :> ProbabilityDistribution {
        doc /* Uniform(a, b) distribution with constant density over [a, b]. */

        attribute a[1] : Real {
            doc /* Lower bound a of the uniform support. */
        }

        attribute b[1] : Real {
            doc /* Upper bound b of the uniform support. */
        }

        assert constraint { a < b }
    }

        attribute WeibullDistribution :> ProbabilityDistribution {
        doc /* Weibull(k, λ) distribution defined over (0, +∞); 
            widely used in reliability engineering for failure modeling. */

        attribute shape[1] : Real {
            doc /* Shape parameter k > 0 controlling tail heaviness and failure rate behavior. */
        }

        attribute scale[1] : Real {
            doc /* Scale parameter λ > 0 stretching the distribution along the x-axis. */
        }

        assert constraint { shape > 0 and scale > 0 }

        assert constraint { lowerBound >= 0.0 }
    }

    attribute JointDistribution {
        doc /* Models a multivariate probability distribution composed of marginal
            distributions and an optional dependence structure (correlation or copula). */

        attribute marginals[2..*] :> ProbabilityDistribution {
            doc /* List of marginal probability distributions, one per variable. */
        }

        attribute correlationMatrix[0..1] : Quantities::TensorQuantityValue {
            doc /* Optional correlation matrix defining linear dependence between variables.
                Must be square, symmetric, and positive semi-definite. It is a qquare matrix 
                of correlation coefficients between marginals. Represented as a TensorQuantityValue 
                because correlation matrices are rank-2 numerical tensors. Dimensions must be (n,n), 
                symmetric, and contain values in [-1,1]. */
        }
    }

    enum def StatisticType {
        Mean;
        Variance;
        StandardDeviation;
        Margin;
        Probability;
    }

    enum def StatisticEstimatorTechnique {
        Sampling;          // generic Monte Carlo / quasi-MC
        ControlVariate;
        MultiLevelSampling;
        TaylorApproximation;
        SurrogateBased;    // e.g. PCE, meta-model based
    }

    enum def SamplingMethod {
        MonteCarlo;
        QuasiMonteCarlo;
        LatinHypercube;
        LowDiscrepancySequence;
        MultiLevelMonteCarlo;
        SequentialSampling;
    }
}