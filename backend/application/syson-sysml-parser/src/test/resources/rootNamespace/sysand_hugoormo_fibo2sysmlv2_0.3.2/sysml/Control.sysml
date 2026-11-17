// ===================================================================
// Control.sysml
// Source-parity extracted from: FiBO_FND_OwnershipAndControl.sysml
// Primary source module: Control.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Foundations FND/OwnershipAndControl
// ===================================================================

package Control {
  doc/* This ontology defines high-level, control-related concepts, including the distinction between de jure and de facto control, the former being derived with reference to terms in the Legal Capacity ontology. (source: dct:abstract, Control.rdf) */
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

  item def <'control'> Control specializes FiBO_FND_ExternalStubs::Situation {
  	doc /* situation in which some party has the power to direct or strongly influence the direction of the management and policies related to something */
  	// skos:definition xml:lang="en" (Control.rdf)
  	// rdfs:label xml:lang="en" (Control.rdf)
  	// RDF: fibo-fnd-oac-ctl:Control
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;Situation"/>
  }
  item def <'controlled thing'> ControlledThing specializes FiBO_FND_ExternalStubs::Undergoer {
  	doc /* something over which some party exercises some form of control with respect to some situation */
  	// skos:definition xml:lang="en" (Control.rdf)
  	// rdfs:label xml:lang="en" (Control.rdf)
  	// RDF: fibo-fnd-oac-ctl:ControlledThing
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;Undergoer"/>
  }
  item def <'controlling party'> ControllingParty specializes FiBO_FND_ExternalStubs::Actor {
  	doc /* actor that exercises some form of control in the context of some situation */
  	// skos:definition xml:lang="en" (Control.rdf)
  	// rdfs:label xml:lang="en" (Control.rdf)
  	// RDF: fibo-fnd-oac-ctl:ControllingParty
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;Actor"/>
  }
  item def <'de facto control'> DeFactoControl specializes Control {
  	doc /* control that exists informally and is accepted, although not formally recognized */
  	// skos:definition xml:lang="en" (Control.rdf)
  	// rdfs:label xml:lang="en" (Control.rdf)
  	// RDF: fibo-fnd-oac-ctl:DeFactoControl
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-oac-ctl;Control"/>
  }
  item def <'de jure control'> DeJureControl specializes Control, Legal_Capacity::LegalConstruct {
  	doc /* control that exists as a matter of law, i.e., legitimate, legal control of something */
  	// skos:definition xml:lang="en" (Control.rdf)
  	// rdfs:label xml:lang="en" (Control.rdf)
  	// RDF: fibo-fnd-oac-ctl:DeJureControl
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;LegalConstruct"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-oac-ctl;Control"/>
  }
}
