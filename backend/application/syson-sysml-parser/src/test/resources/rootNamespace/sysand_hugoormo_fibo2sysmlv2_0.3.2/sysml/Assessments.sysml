// ===================================================================
// Assessments.sysml
// Source-parity extracted from: FiBO_FND_Arrangements.sysml
// Primary source module: Assessments.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Foundations FND/Arrangements
// ===================================================================

package Assessments {
  doc/* This ontology defines abstract concepts for assessments, evaluations, and outcomes, as the basis for various analysis, such as for business performance, compliance and risk. (source: dct:abstract, Assessments.rdf) */
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

  item def <'appraisal'> Appraisal specializes Assessments::AssessmentReport {
  	doc /* method used to determine the present or expected worth of an asset */
  	// skos:definition xml:lang="en" (Assessments.rdf)
  	// rdfs:label xml:lang="en" (Assessments.rdf)
  	// RDF: fibo-fnd-arr-asmt:Appraisal
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-asmt;AssessmentReport"/>
  }

  item def <'appraised value'> AppraisedValue specializes MarketValue {
  	doc /* estimated value of some asset as of a given point in time */
  	// skos:definition xml:lang="en" (Assessments.rdf)
  	// rdfs:label xml:lang="en" (Assessments.rdf)
  	// RDF: fibo-fnd-arr-asmt:AppraisedValue
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-asmt;MarketValue"/>
  }

  item def <'appraiser'> Appraiser specializes Parties::PartyRole {
  	doc /* party that evaluates or estimates the nature, quality, ability, or value of someone or something */
  	// skos:definition xml:lang="en" (Assessments.rdf)
  	// rdfs:label xml:lang="en" (Assessments.rdf)
  	// RDF: fibo-fnd-arr-asmt:Appraiser
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;PartyRole"/>
  }

  item def <'assessment activity'> AssessmentActivity specializes FiBOOccurrences::OccurrenceKind {
  	doc /* activity involving the evaluation or estimation of the nature, quality, ability, or value of someone or something */
  	// skos:definition xml:lang="en" (Assessments.rdf)
  	// rdfs:label xml:lang="en" (Assessments.rdf)
  	// RDF: fibo-fnd-arr-asmt:AssessmentActivity
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-oc;OccurrenceKind"/>
  }

  item def AssessmentEvent specializes FiBOOccurrences::FiBOOccurrence {
  	doc /* event involving the evaluation or estimation of the nature, quality, or ability of someone or something */
  	// skos:definition xml:lang="en" (Assessments.rdf)
    // rdfs:label xml:lang="en" (Assessments.rdf)
  	// RDF: fibo-fnd-arr-asmt:AssessmentEvent
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-oc;FiBOOccurrence"/>
  }

  item def <'assessment report'> AssessmentReport specializes Reporting::Report {
  	doc /* report that includes an opinion, judgement, appraisal, or view about something and typically the methodology and raw inputs used to arrive at that opinion */
  	// skos:definition xml:lang="en" (Assessments.rdf)
  	// rdfs:label xml:lang="en" (Assessments.rdf)
  	// RDF: fibo-fnd-arr-asmt:AssessmentReport
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-rep;Report"/>
  }

  item def <'expected value'> ExpectedValue specializes Value {
  	doc /* theoretical value that is anticipated based on a model or hypothesis */
  	// skos:definition xml:lang="en" (Assessments.rdf)
  	// rdfs:label xml:lang="en" (Assessments.rdf)
  	// RDF: fibo-fnd-arr-asmt:ExpectedValue
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-asmt;Value"/>
  }

  item def <'fair value'> FairValue specializes QuantitativeValue, Currency_Amount::MonetaryAmount {
  	doc /* price that would be received to sell an asset, or paid to transfer a liability, in an orderly transaction between market participants at the measurement date */
  	// skos:definition xml:lang="en" (Assessments.rdf)
  	// rdfs:label xml:lang="en" (Assessments.rdf)
  	// RDF: fibo-fnd-arr-asmt:FairValue
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-cur;MonetaryAmount"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-asmt;QuantitativeValue"/>
  }

  item def <'market value'> MarketValue specializes QuantitativeValue, Currency_Amount::MonetaryAmount {
  	doc /* price an asset would sell for in the market */
  	// skos:definition xml:lang="en" (Assessments.rdf)
  	// rdfs:label xml:lang="en" (Assessments.rdf)
  	// RDF: fibo-fnd-arr-asmt:MarketValue
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-cur;MonetaryAmount"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-asmt;QuantitativeValue"/>
  }

  item def <'observed value'> ObservedValue specializes Value {
  	doc /* value that is an actual data point collected from an experiment, survey, or observation */
  	// skos:definition xml:lang="en" (Assessments.rdf)
  	// rdfs:label xml:lang="en" (Assessments.rdf)
  	// RDF: fibo-fnd-arr-asmt:ObservedValue
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-asmt;Value"/>
  }

  item def <'opinion'> Opinion { // RDF: fibo-fnd-arr-asmt:Opinion
		doc /* judgement, appraisal, or view about something */
		// skos:definition xml:lang="en" (Assessments.rdf)
		// rdfs:label xml:lang="en" (Assessments.rdf)
    // RDF: fibo-fnd-arr-asmt:Opinion
  }

  item def <'present value'> PresentValue specializes QuantitativeValue {
  	doc /* value of an asset today, which may be calculated from reference data and may based on its expected future value */
  	// skos:definition xml:lang="en" (Assessments.rdf)
  	// rdfs:label xml:lang="en" (Assessments.rdf)
  	// RDF: fibo-fnd-arr-asmt:PresentValue
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-asmt;QuantitativeValue"/>
  }

  item def <'qualitative value'> QualitativeValue specializes Value {
  	doc /* value that has less precision or accuracy than a value determined via quantitative methods and which is usually expressed in codes rather than actual numbers */
  	// skos:definition xml:lang="en" (Assessments.rdf)
  	// rdfs:label xml:lang="en" (Assessments.rdf)
  	// RDF: fibo-fnd-arr-asmt:QualitativeValue
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-asmt;Value"/>
  }

  item def <'quantitative value'> QuantitativeValue specializes Value, FiBO_FND_ExternalStubs::ScalarQuantityValue {
  	doc /* value determined via quantitative methods, expressed as a numerical value in appropriate units */
  	// skos:definition xml:lang="en" (Assessments.rdf)
  	// rdfs:label xml:lang="en" (Assessments.rdf)
  	// RDF: fibo-fnd-arr-asmt:QuantitativeValue
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-qtu;ScalarQuantityValue"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-asmt;Value"/>
  }

  item def <'reference value'> ReferenceValue specializes QuantitativeValue, FiBO_FND_ExternalStubs::Reference {
  	doc /* value for something discernible for which evidence can be obtained */
  	// skos:definition xml:lang="en" (Assessments.rdf)
  	// rdfs:label xml:lang="en" (Assessments.rdf)
  	// RDF: fibo-fnd-arr-asmt:ReferenceValue
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-doc;Reference"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-asmt;QuantitativeValue"/>
  }

  item def <'valuation method'> ValuationMethod specializes Objectives::Strategy {
  	doc /* method used to determine the present or expected worth of an asset */
  	// skos:definition xml:lang="en" (Assessments.rdf)
  	// rdfs:label xml:lang="en" (Assessments.rdf)
  	// RDF: fibo-fnd-arr-asmt:ValuationMethod
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-gao-obj;Strategy"/>
  }

  item def <'value'> Value specializes FiBO_FND_ExternalStubs::Aspect {
  	doc /* date on which an assessment process was completed */
  	// skos:definition xml:lang="en" (Assessments.rdf)
  	// rdfs:label xml:lang="en" (Assessments.rdf)
  	// RDF: fibo-fnd-arr-asmt:Value
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Aspect"/>
  }

  item def <'value assessment'> ValueAssessment specializes Assessments::AssessmentEvent {
  	doc /* assessment event to estimate the value of something */
  	// skos:definition xml:lang="en" (Assessments.rdf)
  	// rdfs:label xml:lang="en" (Assessments.rdf)
  	// RDF: fibo-fnd-arr-asmt:ValueAssessment
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-asmt;AssessmentEvent"/>
  }

  item def ClassificationScheme specializes Arrangements::Scheme { // RDF: fibo-fnd-arr-cls:ClassificationScheme
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-arr;Scheme"/>
  }
}
