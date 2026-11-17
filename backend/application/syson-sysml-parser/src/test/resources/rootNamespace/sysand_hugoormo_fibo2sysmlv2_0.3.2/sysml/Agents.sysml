// ===================================================================
// Agents.sysml
// Source-parity extracted from: FIBO_FND_AgentsAndPeople.sysml
// Primary source module: Agents.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Foundations FND/AgentsAndPeople
// ===================================================================

package Agents {
  doc/* This ontology extends the Commons 1.1 Parties and Situations ontology to define defines the concept of a software system, which may or may not be autonomous. Other concepts that were originally defined in this ontology have been replaced with their corresponding concepts in Commons. (source: dct:abstract, Agents.rdf) */
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

    item def <'automated system'> AutomatedSystem specializes Agent {
    	doc /* system that reduces or eliminates the need for human involvement in order to complete a task */
    	// skos:definition xml:lang="en" (Agents.rdf)
    	// rdfs:label xml:lang="en" (Agents.rdf)
    	// RDF: fibo-fnd-aap-agt:AutomatedSystem
      // cmns-av:adaptedFrom: https://www.reference.com/technology/automated-system-c85583d0f17a632
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;Agent"/>
    }
    item def Agent {
      // RDF: cmns-pts:Agent (profiled in Agents.rdf)
      // OWL-restriction: <owl:onProperty rdf:resource="&cmns-dsg;hasTextualName"/>
      // OWL-minQualifiedCardinality: 0
      // OWL-onDataRange: <owl:onDataRange rdf:resource="&cmns-txt;Text"/>
    }

    item def SoftwareAgent specializes AutomatedSystem {
      // RDF: cmns-pts:SoftwareAgent (profiled in Agents.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-aap-agt;AutomatedSystem"/>
    }
}
