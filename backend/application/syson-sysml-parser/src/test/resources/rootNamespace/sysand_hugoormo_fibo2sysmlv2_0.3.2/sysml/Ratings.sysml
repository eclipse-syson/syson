// ===================================================================
// Ratings.sysml
// Source-parity extracted from: FiBO_FND_Arrangements.sysml
// Primary source module: Ratings.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Foundations FND/Arrangements
// ===================================================================

package Ratings {
  doc/* This ontology defines abstract concepts for representation of ratings and rating schemes, particularly for ratings describing aspects of business performance, credit worthiness, and investment quality at a high level. (source: dct:abstract, Ratings.rdf) */
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

  item def <'qualitative rating score'> QualitativeRatingScore specializes Ratings::RatingScore, FiBO_FND_ExternalStubs::CodeElement {
  	doc /* rating score that is represented as a qualitative code with respect to some rating scale */
  	// skos:definition xml:lang="en" (Ratings.rdf)
  	// rdfs:label xml:lang="en" (Ratings.rdf)
  	// RDF: fibo-fnd-arr-rt:QualitativeRatingScore
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cds;CodeElement"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-rt;RatingScore"/>
  }

  item def <'quantitative rating score'> QuantitativeRatingScore specializes Ratings::RatingScore {
  	doc /* rating score that is a simple numeric value on some scale, such as a credit rating for an individual */
  	// skos:definition xml:lang="en" (Ratings.rdf)
  	// rdfs:label xml:lang="en" (Ratings.rdf)
  	// RDF: fibo-fnd-arr-rt:QuantitativeRatingScore
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-rt;RatingScore"/>
  }

  item def Rating specializes Assessments::Opinion {
  	doc /* standing of something at a particular time, indicated by at least one scores with respect to some scale, based on an assessment by some party */
  	// skos:definition xml:lang="en" (Ratings.rdf)
    // rdfs:label xml:lang="en" (Ratings.rdf)
  	// RDF: fibo-fnd-arr-rt:Rating
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-asmt;Opinion"/>
  }

  item def <'rating agency'> RatingAgency specializes RatingIssuer, RatingScalePublisher {
  	doc /* rating issuer that is also a rating scale publisher, frequently but not always an independent rating service */
  	// skos:definition xml:lang="en" (Ratings.rdf)
  	// rdfs:label xml:lang="en" (Ratings.rdf)
  	// RDF: fibo-fnd-arr-rt:RatingAgency
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-rt;RatingIssuer"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-rt;RatingScalePublisher"/>
  }

  item def <'rating assessment activity'> RatingAssessmentActivity specializes Assessments::AssessmentActivity {
  	doc /* assessment activity resulting in a grade or score and potentially a report describing the score and the process used to determine that score */
  	// skos:definition xml:lang="en" (Ratings.rdf)
  	// rdfs:label xml:lang="en" (Ratings.rdf)
  	// RDF: fibo-fnd-arr-rt:RatingAssessmentActivity
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-asmt;AssessmentActivity"/>
  }

  item def RatingAssessmentEvent specializes Assessments::AssessmentEvent {
  	doc /* individual assessment resulting in a grade or score and potentially a report describing the score */
  	// skos:definition xml:lang="en" (Ratings.rdf)
    // rdfs:label xml:lang="en" (Ratings.rdf)
  	// RDF: fibo-fnd-arr-rt:RatingAssessmentEvent
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-asmt;AssessmentEvent"/>
  }

  item def <'rating issuer'> RatingIssuer specializes Parties::PartyRole {
  	doc /* party that is responsible for issuing ratings */
  	// skos:definition xml:lang="en" (Ratings.rdf)
  	// rdfs:label xml:lang="en" (Ratings.rdf)
  	// RDF: fibo-fnd-arr-rt:RatingIssuer
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;PartyRole"/>
  }

  item def <'rating party'> RatingParty specializes Parties::PartyRole {
  	doc /* person, organization or group that analyzes some aspect of something and develops a rating */
  	// skos:definition xml:lang="en" (Ratings.rdf)
  	// rdfs:label xml:lang="en" (Ratings.rdf)
  	// RDF: fibo-fnd-arr-rt:RatingParty
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;PartyRole"/>
  }

  item def <'rating report'> RatingReport specializes Assessments::AssessmentReport {
  	doc /* report describing a set of ratings */
  	// skos:definition xml:lang="en" (Ratings.rdf)
  	// rdfs:label xml:lang="en" (Ratings.rdf)
  	// RDF: fibo-fnd-arr-rt:RatingReport
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-asmt;AssessmentReport"/>
  }

  item def RatingScale specializes Assessments::ClassificationScheme {
  	doc /* system for assigning a value to something according to some scale with respect to quality, a standard, or ranking */
  	// skos:definition xml:lang="en" (Ratings.rdf)
    // rdfs:label xml:lang="en" (Ratings.rdf)
  	// RDF: fibo-fnd-arr-rt:RatingScale
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;ClassificationScheme"/>
  }

  item def <'rating scale publisher'> RatingScalePublisher specializes Parties::PartyRole {
  	doc /* party responsible for managing one or more rating schemes and potentially publishing ratings based on those schemes */
  	// skos:definition xml:lang="en" (Ratings.rdf)
  	// rdfs:label xml:lang="en" (Ratings.rdf)
  	// RDF: fibo-fnd-arr-rt:RatingScalePublisher
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;PartyRole"/>
  }

  item def <'rating score'> RatingScore specializes FiBO_FND_ExternalStubs::Classifier {
  	doc /* grade, classification, or ranking of for something in accordance with some rating scale */
  	// skos:definition xml:lang="en" (Ratings.rdf)
  	// rdfs:label xml:lang="en" (Ratings.rdf)
  	// RDF: fibo-fnd-arr-rt:RatingScore
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
  }

}
