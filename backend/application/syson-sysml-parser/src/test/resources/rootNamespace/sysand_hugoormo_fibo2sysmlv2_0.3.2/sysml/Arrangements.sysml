// ===================================================================
// Arrangements.sysml
// Source-parity extracted from: FiBO_FND_Arrangements.sysml
// Primary source module: Arrangements.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Foundations FND/Arrangements
// ===================================================================

package Arrangements {
  doc/* This ontology defines abstract structural concepts, extending the Commons concept of an arrangement to represent schemes. (source: dct:abstract, Arrangements.rdf) */
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

  item def <'scheme'> Scheme specializes FiBO_FND_ExternalStubs::Arrangement {
  	doc /* structure or means of organizing information such as a blueprint, schema, numbering system, organization structure, measurement system, plan, taxonomy, or language for organizing information */
  	// skos:definition xml:lang="en" (Arrangements.rdf)
  	// rdfs:label xml:lang="en" (Arrangements.rdf)
  	// RDF: fibo-fnd-arr-arr:Scheme
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-col;Arrangement"/>
  }

}
