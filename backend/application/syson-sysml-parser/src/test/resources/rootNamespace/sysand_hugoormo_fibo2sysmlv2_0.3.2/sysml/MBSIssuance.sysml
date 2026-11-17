// ===================================================================
// MBSIssuance.sysml
// Source-parity extracted from: FiBO_BP_SecuritiesIssuance.sysml
// Primary source module: MBSIssuance.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Business Processes BP/SecuritiesIssuance
// ===================================================================
package MBS_Issuance {
  doc/* Ontology of the overall process of issuing mortgage backed securities. These are the process elements that are common to different kinds of MBS issuance (agency and private label). (source: dct:abstract, MBSIssuance.rdf) */
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

  item def MBSSecuritizationProcess specializes Debt_Issuance::PoolBackedSecuritySecuritizationProcess {
  	doc /* A part or section of a prospectus for a securities issue. */
  	// skos:definition xml:lang="en" (MBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (MBSIssuance.rdf)
  	// RDF: fibo-bp-iss-mbsi:MBSSecuritizationProcess
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-dbti;PoolBackedSecuritySecuritizationProcess"/>
  }
  item def ProspectusPart specializes Contracts::ContractualElement {
  	doc /* A part or section of a prospectus for a securities issue. */
  	// skos:definition xml:lang="en" (MBSIssuance.rdf)
  	// rdfs:label xml:lang="en" (MBSIssuance.rdf)
  	// RDF: fibo-bp-iss-mbsi:ProspectusPart
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractualElement"/>
  }
}

// --- MuniIssuance.rdf ---
