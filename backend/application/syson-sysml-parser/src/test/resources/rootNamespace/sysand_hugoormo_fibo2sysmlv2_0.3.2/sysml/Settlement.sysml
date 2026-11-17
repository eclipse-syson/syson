// ===================================================================
// Settlement.sysml
// Source-parity extracted from: FiBO_FBC_FinancialInstruments.sysml
// Primary source module: Settlement.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Financial Business and Commerce FBC/FinancialInstruments
// Structural note: translated as a split file from FiBO_FBC_FinancialInstruments umbrella.
// ===================================================================
package Settlement {
  doc/* This ontology defines high-level concepts for settlement that are applicable across FIBO domain areas, such as for loans, securities, and derivatives. (source: dct:abstract, Settlement.rdf) */
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

    item def <'cash settlement terms'> CashSettlementTerms specializes Objectives::DistributionStrategy, SettlementTerms {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;SettlementTerms"/>
      // RDF: fibo-fbc-fi-stl:CashSettlementTerms
      // subClassOf: DistributionStrategy - external FND
    }

    item def <'physical settlement terms'> PhysicalSettlementTerms specializes SettlementTerms {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;SettlementTerms"/>
      // RDF: fibo-fbc-fi-stl:PhysicalSettlementTerms
    }

    item def <'settlement'> Settlement specializes Financial_Products_and_Services::ContractLifecycleEvent {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;ContractLifecycleEvent"/>
    	doc /* act of finalizing a transaction, including but not limited to finalizing accounting, exchanging consideration, and/or legally recording documents, as applicable */     	// skos:definition xml:lang="en" (Settlement.rdf)
    	// rdfs:label xml:lang="en" (Settlement.rdf)
      // RDF: fibo-fbc-fi-stl:Settlement
    }

    item def <'settlement convention'> SettlementConvention specializes Business_Dates::Convention {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-bd;Convention"/>
    	doc /* convention employed to determine the closing date (from the stated settlement date) in the process of settling a transaction on which securities or interests in securities are delivered, usually against (in simultaneous exchange for) payment of some consideration */     	// skos:definition xml:lang="en" (Settlement.rdf)
    	// rdfs:label xml:lang="en" (Settlement.rdf)
      // RDF: fibo-fbc-fi-stl:SettlementConvention
      // subClassOf: Convention - external FND/CMNS
    }

    item def <'settlement event'> SettlementEvent specializes Financial_Products_and_Services::ContractLifecycleEventOccurrence {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;ContractLifecycleEventOccurrence"/>
      // RDF: fibo-fbc-fi-stl:SettlementEvent
    }

    item def <'settlement terms'> SettlementTerms specializes Contracts::ContractualCommitment {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractualCommitment"/>
    	doc /* contract terms that define the commitment to and mechanism for settling one or more sides of a transaction */     	// skos:definition xml:lang="en" (FinancialProductsAndServices.rdf)
    	// rdfs:label xml:lang="en" (FinancialProductsAndServices.rdf)
      // RDF: fibo-fbc-fi-stl:SettlementTerms
      // subClassOf: ContractualCommitment - from FIBO_FND_Agreements (AgreementsPackage)
    }

}
