// ===================================================================
// CashFlows.sysml
// Source-parity extracted from: FiBO_FND_Accounting.sysml
// Primary source module: CashFlows.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Foundations FND/Accounting
// ===================================================================

package CashFlows {
  doc/* This ontology defines cash flow concepts for use in defining other FIBO ontology elements. (source: dct:abstract, CashFlows.rdf) */
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

  item def <'cash flow'> CashFlow specializes Financial_Dates::DatedCollectionConstituent {
  	doc /* event that relates to or triggers some aspect of a credit default swap */
  	// skos:definition xml:lang="en" (CashFlows.rdf)
  	// rdfs:label xml:lang="en" (CashFlows.rdf)
  	// RDF: fibo-fnd-acc-csf:CashFlow
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-fd;DatedCollectionConstituent"/>
  }

  item def <'cash flow structure'> CashFlowStructure specializes Financial_Dates::DatedStructuredCollection {
  	doc /* the structure related to one or more cash flows */
  	// skos:definition xml:lang="en" (CashFlows.rdf)
  	// rdfs:label xml:lang="en" (CashFlows.rdf)
  	// RDF: fibo-fnd-acc-csf:CashFlowStructure
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-fd;DatedStructuredCollection"/>
  }

    item def <'triggering event'> TriggeringEvent specializes FiBOOccurrences::FiBOOccurrence {
  	doc /* event that relates to or triggers some aspect of a credit default swap */
    // skos:definition xml:lang="en" (CashFlows.rdf)
  	// rdfs:label xml:lang="en" (CashFlows.rdf)
  	// RDF: fibo-fnd-acc-csf:TriggeringEvent
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-oc;FiBOOccurrence"/>
  }

}
