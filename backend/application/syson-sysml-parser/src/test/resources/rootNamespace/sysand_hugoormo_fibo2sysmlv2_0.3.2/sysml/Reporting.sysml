// ===================================================================
// Reporting.sysml
// Source-parity extracted from: FiBO_FND_Arrangements.sysml
// Primary source module: Reporting.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Foundations FND/Arrangements
// ===================================================================

package Reporting {
  doc/* This ontology defines the notion of a Report and related party concepts. (source: dct:abstract, Reporting.rdf) */
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

  item def Report specializes FiBO_FND_ExternalStubs::Document {
        doc /* document that provides a structured description of something, prepared on ad hoc, periodic, recurring, regular, or as required basis */
  	// skos:definition xml:lang="en" (Reporting.rdf)
        // rdfs:label xml:lang="en" (Reporting.rdf)
  	// RDF: fibo-fnd-arr-rep:Report
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-doc;Document"/>
  }

  item def <'reporting party'> ReportingParty specializes Parties::PartyRole {
  	doc /* party providing a report, typically in response to some contractual, legal, regulatory or other business requirement */
  	// skos:definition xml:lang="en" (Reporting.rdf)
  	// rdfs:label xml:lang="en" (Reporting.rdf)
  	// RDF: fibo-fnd-arr-rep:ReportingParty
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;PartyRole"/>
  }

  item def Request specializes FiBOOccurrences::FiBOOccurrence {
  	doc /* event in which some party asks another party for something at some point in time */
  	// skos:definition xml:lang="en" (Reporting.rdf)
        // rdfs:label xml:lang="en" (Reporting.rdf)
  	// RDF: fibo-fnd-arr-rep:Request
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-oc;FiBOOccurrence"/>
  }

  item def <'request activity'> RequestActivity specializes FiBOOccurrences::OccurrenceKind {
  	doc /* activity in which some party asks another party for something or to do something */
  	// skos:definition xml:lang="en" (Reporting.rdf)
  	// rdfs:label xml:lang="en" (Reporting.rdf)
  	// RDF: fibo-fnd-arr-rep:RequestActivity
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-oc;OccurrenceKind"/>
  }

  item def <'requester'> Requester specializes Parties::PartyRole {
  	doc /* party that asks for something */
  	// skos:definition xml:lang="en" (Reporting.rdf)
  	// rdfs:label xml:lang="en" (Reporting.rdf)
  	// RDF: fibo-fnd-arr-rep:Requester
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;PartyRole"/>
  }

  item def <'submitter'> Submitter specializes Parties::PartyRole {
        doc /* party presenting something, such as a regulatory report */
  	// skos:definition xml:lang="en" (Reporting.rdf)
  	// rdfs:label xml:lang="en" (Reporting.rdf)
  	// RDF: fibo-fnd-arr-rep:Submitter
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;PartyRole"/>
  }

}
