// ===================================================================
// Parties.sysml
// Source-parity extracted from: FiBO_FND_Parties.sysml
// Primary source module: Parties.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Foundations FND/Parties
// ===================================================================

package Parties {
  doc/* This ontology extends the Commons Parties and Situations ontology with identifiers for party roles and very general tax identifiers. (source: dct:abstract, Parties.rdf) */
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

  item def PartyRole { // RDF: fibo-fnd-pty-pty:PartyRole
    // RDF: fibo-fnd-pty-pty:PartyRole
  }
  item def <'party role identification scheme'> PartyRoleIdentificationScheme specializes FiBO_FND_CodeSets_And_Schemes::IdentificationScheme {
  	doc /* system for allocating identifiers to roles that parties play */
  	// skos:definition xml:lang="en" (Parties.rdf)
  	// rdfs:label xml:lang="en" (Parties.rdf)
  	// RDF: fibo-fnd-pty-pty:PartyRoleIdentificationScheme
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-id;IdentificationScheme"/>
  }
  item def PartyRoleIdentifier specializes FiBO_FND_ExternalStubs::Identifier {
  	doc /* sequence of characters, capable of uniquely identifying a party based on a specific role that they play in some context */
  	// skos:definition xml:lang="en" (Parties.rdf)
    // rdfs:label xml:lang="en" (Parties.rdf)
  	// RDF: fibo-fnd-pty-pty:PartyRoleIdentifier
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-id;Identifier"/>
  }
  item def <'tax identification scheme'> TaxIdentificationScheme specializes FiBO_FND_CodeSets_And_Schemes::IdentificationScheme {
  	doc /* identification scheme used to identify taxpayers in some jurisdiction */
  	// skos:definition xml:lang="en" (Parties.rdf)
  	// rdfs:label xml:lang="en" (Parties.rdf)
  	// RDF: fibo-fnd-pty-pty:TaxIdentificationScheme
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-id;IdentificationScheme"/>
  }
  item def <'tax identifier'> TaxIdentifier specializes FiBO_FND_ExternalStubs::Identifier {
  	doc /* identifier assigned to a taxpayer that enables compulsory financial charges and other levies to be imposed on the taxpayer by a governmental organization in order to fund government spending and various public expenditures */
  	// skos:definition xml:lang="en" (Parties.rdf)
  	// rdfs:label xml:lang="en" (Parties.rdf)
  	// RDF: fibo-fnd-pty-pty:TaxIdentifier
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-id;Identifier"/>
  }
}
