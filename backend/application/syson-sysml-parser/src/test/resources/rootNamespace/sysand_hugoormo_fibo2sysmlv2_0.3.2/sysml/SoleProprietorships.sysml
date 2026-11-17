// ===================================================================
// SoleProprietorships.sysml
// Source-parity extracted from: FiBO_BE_SoleProprietorships.sysml
// Primary source module: SoleProprietorships.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Business Entities BE/SoleProprietorships
// Structural note: translated as a single-file package from its source umbrella.
// ===================================================================
package Sole_Proprietorships {
  doc/* This ontology defines the fundamental concepts for representing sole proprietorships -- i.e., organizations that are owned by an individual that is responsible for the liabilities of the organization. (source: dct:abstract, SoleProprietorships.rdf) */
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

// BusinessEntity, LegalPerson
// EntityOwner

  item def <'sole proprietor'> SoleProprietor specializes Ownership_Parties::EntityOwner {
    doc /* party that owns a business, has the rights to all profits from that business and is considered a single entity (unincorporated) together with that business for tax and liability purposes */
    // skos:definition xml:lang="en" (SoleProprietorships.rdf)
    // rdfs:label xml:lang="en" (SoleProprietorships.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-opty;EntityOwner"/>
    // RDF: fibo-be-sps-sps:SoleProprietor
  }

  item def <'sole proprietorship'> SoleProprietorship specializes Legal_Persons::BusinessEntity, Legal_Persons::LegalPerson {
    doc /* unincorporated business owned by a single person */
    // skos:definition xml:lang="en" (SoleProprietorships.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-le-lp;BusinessEntity"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;LegalPerson"/>
  	// rdfs:label xml:lang="en" (SoleProprietorships.rdf)
    // RDF: fibo-be-sps-sps:SoleProprietorship
  }
}
