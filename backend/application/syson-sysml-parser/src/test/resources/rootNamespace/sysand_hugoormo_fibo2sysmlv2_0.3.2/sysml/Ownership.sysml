// ===================================================================
// Ownership.sysml
// Source-parity extracted from: FiBO_FND_OwnershipAndControl.sysml
// Primary source module: Ownership.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Foundations FND/OwnershipAndControl
// ===================================================================

package Ownership {
  doc/* This ontology defines high-level, ownership-related concepts, including owner, asset and ownership along with relationships between them. (source: dct:abstract, Ownership.rdf) */
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

  item def <'asset'> Asset specializes FiBO_FND_ExternalStubs::Undergoer {
  	doc /* something of monetary value that is owned or provides benefit to some party */
  	// skos:definition xml:lang="en" (Ownership.rdf)
  	// rdfs:label xml:lang="en" (Ownership.rdf)
  	// RDF: fibo-fnd-oac-own:Asset
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;Undergoer"/>
  }
  item def <'intangible asset'> IntangibleAsset specializes Asset {
  	doc /* identifiable, non-monetary asset that lacks physical substance */
  	// skos:definition xml:lang="en" (Ownership.rdf)
  	// rdfs:label xml:lang="en" (Ownership.rdf)
  	// RDF: fibo-fnd-oac-own:IntangibleAsset
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-oac-own;Asset"/>
  }
  item def <'owner'> Owner specializes FiBO_FND_ExternalStubs::Actor {
  	doc /* party that is legally recognized as having the right to possess, the privilege to use, and ability to transfer any rights or privileges associated with something, as permitted by law */
  	// skos:definition xml:lang="en" (Ownership.rdf)
  	// rdfs:label xml:lang="en" (Ownership.rdf)
  	// RDF: fibo-fnd-oac-own:Owner
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;Actor"/>
  }
  item def <'is owned and controlled by'> Ownership specializes FiBO_FND_ExternalStubs::Situation {
  	doc /* situation in which some party holds the legal title to something (explicitly or implicitly) and has the right to transfer that title and/or possession */
  	// skos:definition xml:lang="en" (Ownership.rdf)
    // rdfs:label xml:lang="en" (Ownership.rdf)
  	// RDF: fibo-fnd-oac-own:Ownership
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;Situation"/>
  }
  item def <'tangible asset'> TangibleAsset specializes Asset {
    doc /* asset that is a physical, measurable resource, i.e., one that takes a physical form */
    // skos:definition xml:lang="en" (Ownership.rdf)
  	// rdfs:label xml:lang="en" (Ownership.rdf)
  	// RDF: fibo-fnd-oac-own:TangibleAsset
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-oac-own;Asset"/>
  }
}
