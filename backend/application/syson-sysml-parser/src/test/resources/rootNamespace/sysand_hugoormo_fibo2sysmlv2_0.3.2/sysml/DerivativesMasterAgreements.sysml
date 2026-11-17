// ===================================================================
// DerivativesMasterAgreements.sysml
// Source-parity extracted from: FiBO_DER_DerivativesContracts.sysml
// Primary source module: DerivativesMasterAgreements.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FIBO Derivatives DER/DerivativesContracts
// ===================================================================
package Derivatives_Master_Agreements {
  doc/* This ontology covers terms that make up the OTC Derivatives Master agreement as defined in the ISDA literature, at a high level, although most of the relevant provisions are provided in the higher-level Contracts ontology. (source: dct:abstract, DerivativesMasterAgreements.rdf) */
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

  item def <'account change notification obligation'> AccountChangeNotificationObligation specializes Legal_Capacity::NotificationObligation {
    	doc /* obligation to notify a counterparty of any changes in account details */
    	// skos:definition xml:lang="en" (DerivativesMasterAgreements.rdf)
    	// rdfs:label xml:lang="en" (DerivativesMasterAgreements.rdf)
    	// RDF: fibo-der-drc-ma:AccountChangeNotificationObligation
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;NotificationObligation"/>
    }

  item def <'derivative credit support agreement'> DerivativeCreditSupportAgreement specializes Debt::CreditEnhancementAgreement {
    	doc /* addendum to the master agreement that governs the exchange of collateral between parties in derivatives transactions */
    	// skos:definition xml:lang="en" (DerivativesMasterAgreements.rdf)
    	// rdfs:label xml:lang="en" (DerivativesMasterAgreements.rdf)
    	// RDF: fibo-der-drc-ma:DerivativeCreditSupportAgreement
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;CreditEnhancementAgreement"/>
    }

  item def <'derivative master agreement'> DerivativeMasterAgreement specializes Debt::CreditAgreement, Contracts::MasterAgreement {
    	doc /* master agreement covering derivatives transactions to be carried out between the parties to this contract */
    	// skos:definition xml:lang="en" (DerivativesMasterAgreements.rdf)
    	// rdfs:label xml:lang="en" (DerivativesMasterAgreements.rdf)
    	// RDF: fibo-der-drc-ma:DerivativeMasterAgreement
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;CreditAgreement"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;MasterAgreement"/>
    }

  item def <'master agreement early termination right'> MasterAgreementEarlyTerminationRight specializes Legal_Capacity::ContractualRight {
    	doc /* contractual right of a party to the master agreement to terminate the agreement early */
    	// skos:definition xml:lang="en" (DerivativesMasterAgreements.rdf)
    	// rdfs:label xml:lang="en" (DerivativesMasterAgreements.rdf)
    	// RDF: fibo-der-drc-ma:MasterAgreementEarlyTerminationRight
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;ContractualRight"/>
    }

}
