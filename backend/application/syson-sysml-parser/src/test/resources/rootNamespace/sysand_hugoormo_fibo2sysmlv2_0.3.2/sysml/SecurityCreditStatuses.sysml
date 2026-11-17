// ===================================================================
// SecurityCreditStatuses.sysml
// Source-parity extracted from: FiBO_MD_TemporalCore.sysml
// Primary source module: SecurityCreditStatuses.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Market Data MD/TemporalCore
// ===================================================================
package SecurityCreditStatuses {
  doc/* This ontology extends the credit status ontology to define credit status concepts that are specific to issued securities. These include cashflow status and the basic credit statuses of being OK or in default. Note that in application data models these concepts would be represented as one or more selectable code lists or enumerations. (source: dct:abstract, SecurityCreditStatuses.rdf) */
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

  item def <'credit o k'> CreditOK specializes SecurityCreditStatus {
  	doc /* Not defaulted. */
  	// skos:definition xml:lang="en" (SecurityCreditStatuses.rdf)
  	// rdfs:label xml:lang="en" (SecurityCreditStatuses.rdf)
  	// RDF: fibo-md-temx-crs:CreditOK
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-temx-crs;SecurityCreditStatus"/>
  }
  item def <'in default'> InDefault specializes SecurityCreditStatus {
  	doc /* The issuer has failed to pay somthing that they are contractually obliged to pay. */
  	// skos:definition xml:lang="en" (SecurityCreditStatuses.rdf)
  	// rdfs:label xml:lang="en" (SecurityCreditStatuses.rdf)
  	// RDF: fibo-md-temx-crs:InDefault
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-temx-crs;SecurityCreditStatus"/>
  }
  item def <'security cashflow status'> SecurityCashflowStatus specializes Lifecycles::LifecycleStatus {
  	doc /* The status of the cashflow due to the holder from the security. */
  	// skos:definition xml:lang="en" (SecurityCreditStatuses.rdf)
  	// rdfs:label xml:lang="en" (SecurityCreditStatuses.rdf)
  	// RDF: fibo-md-temx-crs:SecurityCashflowStatus
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-lif;LifecycleStatus"/>
  }
  item def <'security credit status'> SecurityCreditStatus specializes Lifecycles::LifecycleStatus { // RDF: fibo-md-temx-crs:SecurityCreditStatus
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-lif;LifecycleStatus"/>
  }
      // RDF: fibo-md-temx-crs:SecurityCreditStatus
}

// --- fibo-md-temx-trs ---
