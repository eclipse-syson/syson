// ===================================================================
// OwnershipandControl.sysml
// Source-parity extracted from: FiBO_FND_OwnershipAndControl.sysml
// Primary source module: OwnershipAndControl.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Foundations FND/OwnershipAndControl
// ===================================================================

package Ownership_and_Control {
  doc/* This ontology brings the concepts of ownership and control together, in cases where the combined semantics are applicable, such as for a wholly owned subsidiary. (source: dct:abstract, OwnershipAndControl.rdf) */
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

  item def <'ownership control situation'> OwnershipControlSituation specializes FiBO_FND_ExternalStubs::Situation {
  	doc /* situation in which some party owns and controls something */
  	// skos:definition xml:lang="en" (OwnershipAndControl.rdf)
  	// rdfs:label xml:lang="en" (OwnershipAndControl.rdf)
  	// RDF: fibo-fnd-oac-oac:OwnershipControlSituation
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;Situation"/>
  }
}
