// ===================================================================
// IdentifiersAndIndices.sysml
// Source-parity extracted from: FiBO_FND_Arrangements.sysml
// Primary source module: IdentifiersAndIndices.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Foundations FND/Arrangements
// ===================================================================

package Identifiers_and_Indices {
  doc/* This ontology defines abstract concepts for representation of indices and indexing schemes, as well as reusable identifiers, for reuse in other ontologies. (source: dct:abstract, IdentifiersAndIndices.rdf) */
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

  item def <'index'> Index specializes FiBO_FND_ExternalStubs::Reference {
  	doc /* indirect shortcut derived from and pointing into, a greater volume of values, data, information or knowledge */
  	// skos:definition xml:lang="en" (IdentifiersAndIndices.rdf)
  	// rdfs:label xml:lang="en" (IdentifiersAndIndices.rdf)
  	// RDF: fibo-fnd-arr-id:Index
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-doc;Reference"/>
  }

  item def <'indexing scheme'> IndexingScheme specializes Arrangements::Scheme {
  	doc /* system for indexing values, data, information, or knowledge */
  	// skos:definition xml:lang="en" (IdentifiersAndIndices.rdf)
  	// rdfs:label xml:lang="en" (IdentifiersAndIndices.rdf)
  	// RDF: fibo-fnd-arr-id:IndexingScheme
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-arr;Scheme"/>
  }

  item def <'reassignable identifier'> ReassignableIdentifier specializes FiBO_FND_ExternalStubs::Identifier {
  	doc /* identifier that uniquely identifies something for a given time period, and that may be reused to identify something else at a different point in time */
  	// skos:definition xml:lang="en" (IdentifiersAndIndices.rdf)
  	// rdfs:label xml:lang="en" (IdentifiersAndIndices.rdf)
  	// RDF: fibo-fnd-arr-id:ReassignableIdentifier
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-id;Identifier"/>
  }

}
