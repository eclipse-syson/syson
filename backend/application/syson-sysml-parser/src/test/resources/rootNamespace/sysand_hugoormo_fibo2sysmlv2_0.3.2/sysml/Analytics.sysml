// ===================================================================
// Analytics.sysml
// Source-parity extracted from: FiBO_FND_Utilities.sysml
// Primary source module: Analytics.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Foundations FND/Utilities
// ===================================================================

package Analytics {
  doc/* This ontology provides mathematical abstractions for use in other ontologies, including for example the basic components of formulae, parameters and values. (source: dct:abstract, Analytics.rdf) */
  comment license /*
  * Copyright (c) 2013-2025 EDM Council, Inc.
  * Copyright (c) 2013-2025 Object Management Group, Inc.
  *
  * Permission is hereby granted, free of charge, to any person obtaining a copy
  * of this software and associated documentation files (the 'Software'), to deal
  * in the Software without restriction, including without limitation the rights
  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  * copies of the Software, and to permit persons to whom the Software is
  * furnished to do so, subject to the following conditions:
  *
  * The above copyright notice and this permission notice shall be included in all
  * copies or substantial portions of the Software.
  *
  * THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
  * THE SOFTWARE.
  *
  * See https://opensource.org/licenses/MIT. */

  item def <'annualized standard deviation'> AnnualizedStandardDeviation specializes StandardDeviation {
  	doc /* standard deviation for some measure over a specific reference period */
  	// skos:definition xml:lang="en" (Analytics.rdf)
  	// rdfs:label xml:lang="en" (Analytics.rdf)
  	// RDF: fibo-fnd-utl-alx:AnnualizedStandardDeviation
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-utl-alx;StandardDeviation"/>
  }
  item def <'arithmetic mean'> ArithmeticMean specializes Mean {
  	doc /* sum of a collection of numbers divided by the number of numbers in the collection */
  	// skos:definition xml:lang="en" (Analytics.rdf)
  	// rdfs:label xml:lang="en" (Analytics.rdf)
  	// RDF: fibo-fnd-utl-alx:ArithmeticMean
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-utl-alx;Mean"/>
  }
  item def <'average absolute deviation'> AverageAbsoluteDeviation specializes Dispersion {
  	doc /* average of the absolute deviations from a central point */
  	// skos:definition xml:lang="en" (Analytics.rdf)
  	// rdfs:label xml:lang="en" (Analytics.rdf)
  	// RDF: fibo-fnd-utl-alx:AverageAbsoluteDeviation
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-utl-alx;Dispersion"/>
  }
  item def <'difference'> Difference specializes StatisticalMeasure, Expression {
  	doc /* quantity by which amounts differ; the remainder left after subtraction of one value from another */
  	// skos:definition xml:lang="en" (Analytics.rdf)
  	// rdfs:label xml:lang="en" (Analytics.rdf)
  	// RDF: fibo-fnd-utl-alx:Difference
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-qtu;Expression"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-utl-alx;StatisticalMeasure"/>
  }
  item def <'dispersion'> Dispersion specializes StatisticalMeasure, Expression {
  	doc /* degree of scatter or variability shown by observations */
  	// skos:definition xml:lang="en" (Analytics.rdf)
  	// rdfs:label xml:lang="en" (Analytics.rdf)
  	// RDF: fibo-fnd-utl-alx:Dispersion
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-qtu;Expression"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-utl-alx;StatisticalMeasure"/>
  }
  item def <'finite population'> FinitePopulation specializes FiBO_FND_ExternalStubs::Collection {
  	doc /* population for which it is possible to count its units */
  	// skos:definition xml:lang="en" (Analytics.rdf)
  	// rdfs:label xml:lang="en" (Analytics.rdf)
  	// RDF: fibo-fnd-utl-alx:FinitePopulation
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-col;Collection"/>
  }
  item def Expression specializes FiBO_FND_ExternalStubs::Specification { // RDF: fibo-fnd-utl-alx:Expression
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-doc;Specification"/>
      // RDF: fibo-fnd-utl-alx:Expression
  }
  item def <'formula'> Formula specializes FiBO_FND_ExternalStubs::Specification {
  	doc /* rule expressed in letters and symbols that consists of at least one expression */
  	// skos:definition xml:lang="en" (Analytics.rdf)
  	// rdfs:label xml:lang="en" (Analytics.rdf)
  	// RDF: fibo-fnd-utl-alx:Formula
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-doc;Specification"/>
  }
  item def <'geometric mean'> GeometricMean specializes Mean {
  	doc /* mean that indicates the central tendency or typical value of a set of numbers by using the product of their values (as opposed to the arithmetic mean which uses their sum) */
  	// skos:definition xml:lang="en" (Analytics.rdf)
  	// rdfs:label xml:lang="en" (Analytics.rdf)
  	// RDF: fibo-fnd-utl-alx:GeometricMean
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-utl-alx;Mean"/>
  }
  item def <'key performance indicator'> KeyPerformanceIndicator specializes QualifiedMeasure {
  	doc /* measurable target that indicates how an individual or business is performing in terms of meeting its goals */
  	// skos:definition xml:lang="en" (Analytics.rdf)
  	// rdfs:label xml:lang="en" (Analytics.rdf)
  	// RDF: fibo-fnd-utl-alx:KeyPerformanceIndicator
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-utl-alx;QualifiedMeasure"/>
  }
  item def <'mean'> Mean specializes StatisticalMeasure, Expression {
  	doc /* average of the absolute deviations from a central point */
  	// skos:definition xml:lang="en" (Analytics.rdf)
  	// rdfs:label xml:lang="en" (Analytics.rdf)
  	// RDF: fibo-fnd-utl-alx:Mean
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-qtu;Expression"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-utl-alx;StatisticalMeasure"/>
  }
  item def <'median'> Median specializes StatisticalMeasure, Expression {
  	doc /* average of the absolute deviations from a central point */
  	// skos:definition xml:lang="en" (Analytics.rdf)
  	// rdfs:label xml:lang="en" (Analytics.rdf)
  	// RDF: fibo-fnd-utl-alx:Median
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-qtu;Expression"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-utl-alx;StatisticalMeasure"/>
  }
  item def <'median absolute deviation'> MedianAbsoluteDeviation specializes Dispersion {
  	doc /* median of the absolute deviations of observations from the average which may be the arithmetic mean, the median or the mode */
  	// skos:definition xml:lang="en" (Analytics.rdf)
  	// rdfs:label xml:lang="en" (Analytics.rdf)
  	// RDF: fibo-fnd-utl-alx:MedianAbsoluteDeviation
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-utl-alx;Dispersion"/>
  }
  item def <'numeric index value'> NumericIndexValue specializes FiBO_FND_ExternalStubs::ScalarQuantityValue {
  	doc /* numeric value of some aggregate relative to the value of that aggregate as of some date */
  	// skos:definition xml:lang="en" (Analytics.rdf)
  	// rdfs:label xml:lang="en" (Analytics.rdf)
  	// RDF: fibo-fnd-utl-alx:NumericIndexValue
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-qtu;ScalarQuantityValue"/>
  }
  item def QualifiedMeasure specializes StatisticalMeasure {
  	doc /* statistical measure that is constrained by features, quantity kinds or units that refine how it is calculated */
  	// skos:definition xml:lang="en" (Analytics.rdf)
    // rdfs:label xml:lang="en" (Analytics.rdf)
  	// RDF: fibo-fnd-utl-alx:QualifiedMeasure
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-utl-alx;StatisticalMeasure"/>
  }
  item def <'sampling variance'> SamplingVariance specializes Variance {
  	doc /* measure of the extent to which the estimate of a characteristic from different possible samples of the same size and the same design differ from one another */
  	// skos:definition xml:lang="en" (Analytics.rdf)
  	// rdfs:label xml:lang="en" (Analytics.rdf)
  	// RDF: fibo-fnd-utl-alx:SamplingVariance
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-utl-alx;Variance"/>
  }
  item def <'scoped measure'> ScopedMeasure specializes QualifiedMeasure {
  	doc /* qualified measure that is constrained by filters on the statistical population to which it applies */
  	// skos:definition xml:lang="en" (Analytics.rdf)
  	// rdfs:label xml:lang="en" (Analytics.rdf)
  	// RDF: fibo-fnd-utl-alx:ScopedMeasure
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-utl-alx;QualifiedMeasure"/>
  }
  item def <'standard deviation'> StandardDeviation specializes Dispersion {
  	doc /* square root of variance that measures the spread or dispersion around the mean of a data set */
  	// skos:definition xml:lang="en" (Analytics.rdf)
  	// rdfs:label xml:lang="en" (Analytics.rdf)
  	// RDF: fibo-fnd-utl-alx:StandardDeviation
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-utl-alx;Dispersion"/>
  }
  item def <'statistical area'> StatisticalArea specializes FiBO_FND_ExternalStubs::GeographicRegion {
  	doc /* physical location that is defined per some program for designating geographic regions for the purposes of tabulating and presenting statistical data */
  	// skos:definition xml:lang="en" (Analytics.rdf)
  	// rdfs:label xml:lang="en" (Analytics.rdf)
  	// RDF: fibo-fnd-utl-alx:StatisticalArea
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-loc;GeographicRegion"/>
  }
  item def <'statistical area identifier'> StatisticalAreaIdentifier specializes FiBO_FND_ExternalStubs::Identifier, FiBO_FND_ExternalStubs::GeographicRegionIdentifier {
  	doc /* identifier for a physical location that is defined per a nationally consistent program for designating geographic regions for the purposes of tabulating and presenting statistical data */
  	// skos:definition xml:lang="en" (Analytics.rdf)
  	// rdfs:label xml:lang="en" (Analytics.rdf)
  	// RDF: fibo-fnd-utl-alx:StatisticalAreaIdentifier
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-loc;GeographicRegionIdentifier"/>
  }
  item def <'statistical measure'> StatisticalMeasure specializes FiBO_FND_ExternalStubs::Measure {
  	doc /* summary (means, mode, total, index, etc.) of the individual quantitative variable values for the statistical units in a specific group (study domain) */
  	// skos:definition xml:lang="en" (Analytics.rdf)
  	// rdfs:label xml:lang="en" (Analytics.rdf)
  	// RDF: fibo-fnd-utl-alx:StatisticalMeasure
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-qtu;Measure"/>
  }
  item def <'statistical population'> StatisticalPopulation specializes FinitePopulation, StatisticalUniverse {
  	doc /* statistical universe filtered by time and region */
  	// skos:definition xml:lang="en" (Analytics.rdf)
  	// rdfs:label xml:lang="en" (Analytics.rdf)
  	// RDF: fibo-fnd-utl-alx:StatisticalPopulation
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-utl-alx;FinitePopulation"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-utl-alx;StatisticalUniverse"/>
  }
  item def <'statistical program'> StatisticalProgram specializes Objectives::Program {
  	doc /* program that presents a detailed investigation and analysis of a subject or situation involving one or more studies or surveys */
  	// skos:definition xml:lang="en" (Analytics.rdf)
  	// rdfs:label xml:lang="en" (Analytics.rdf)
  	// RDF: fibo-fnd-utl-alx:StatisticalProgram
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-gao-obj;Program"/>
  }
  item def <'statistical universe'> StatisticalUniverse specializes FiBO_FND_ExternalStubs::Collection {
  	doc /* collection representing the total membership, or 'universe', of people, resources, products, services, events, or entities of interest for some question, experiment, survey or statistical program */
  	// skos:definition xml:lang="en" (Analytics.rdf)
  	// rdfs:label xml:lang="en" (Analytics.rdf)
  	// RDF: fibo-fnd-utl-alx:StatisticalUniverse
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-col;Collection"/>
  }
  item def <'variance'> Variance specializes Dispersion {
  	doc /* square root of variance that measures the spread or dispersion around the mean of a data set */
  	// skos:definition xml:lang="en" (Analytics.rdf)
  	// rdfs:label xml:lang="en" (Analytics.rdf)
  	// RDF: fibo-fnd-utl-alx:Variance
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-utl-alx;Dispersion"/>
  }
  item def <'weighting function'> WeightingFunction specializes StatisticalMeasure, Expression {
  	doc /* expression or function that determines the relative importance or influence of a given element of a set with respect to the whole */
  	// skos:definition xml:lang="en" (Analytics.rdf)
  	// rdfs:label xml:lang="en" (Analytics.rdf)
  	// RDF: fibo-fnd-utl-alx:WeightingFunction
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-qtu;Expression"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-utl-alx;StatisticalMeasure"/>
  }
}
