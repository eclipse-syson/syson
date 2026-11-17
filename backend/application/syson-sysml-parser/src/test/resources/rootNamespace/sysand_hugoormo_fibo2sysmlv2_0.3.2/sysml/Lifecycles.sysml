// ===================================================================
// Lifecycles.sysml
// Source-parity extracted from: FiBO_FND_Arrangements.sysml
// Primary source module: Lifecycles.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Foundations FND/Arrangements
// ===================================================================

package Lifecycles {
  doc/* This ontology defines a set of basic concepts for lifecycles, including the various stages and events that make up a given lifecycle, for use in describing product, trade, instrument, production, and other lifecycles in FIBO. (source: dct:abstract, Lifecycles.rdf) */
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

  item def <'lifecycle'> Lifecycle specializes FiBO_FND_ExternalStubs::Arrangement {
  	doc /* arrangement that compares the cyclical nature of families, organizations, processes, products, marketing, and order management, portfolio management or other systems with the cradle to grave life stages (birth, growth, maturity, decay, and death) of living organisms */
  	// skos:definition xml:lang="en" (Lifecycles.rdf)
  	// rdfs:label xml:lang="en" (Lifecycles.rdf)
  	// RDF: fibo-fnd-arr-lif:Lifecycle
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-col;Arrangement"/>
  }

  item def <'lifecycle event'> LifecycleEvent specializes FiBOOccurrences::OccurrenceKind {
  	doc /* kind of event that occurs during one or more stages of a lifecycle */
  	// skos:definition xml:lang="en" (Lifecycles.rdf)
  	// rdfs:label xml:lang="en" (Lifecycles.rdf)
  	// RDF: fibo-fnd-arr-lif:LifecycleEvent
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-oc;OccurrenceKind"/>
  }

  item def <'lifecycle event FiBOOccurrence'> LifecycleEventOccurrence specializes FiBOOccurrences::FiBOOccurrence {
  	doc /* realization of an event in a stage of a lifecycle */
  	// skos:definition xml:lang="en" (Lifecycles.rdf)
  	// rdfs:label xml:lang="en" (Lifecycles.rdf)
  	// RDF: fibo-fnd-arr-lif:LifecycleEventOccurrence
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-oc;FiBOOccurrence"/>
  }

  item def <'lifecycle FiBOOccurrence'> LifecycleOccurrence specializes FiBOOccurrences::FiBOOccurrence {
  	doc /* realization of a lifecycle */
  	// skos:definition xml:lang="en" (Lifecycles.rdf)
  	// rdfs:label xml:lang="en" (Lifecycles.rdf)
  	// RDF: fibo-fnd-arr-lif:LifecycleOccurrence
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-oc;FiBOOccurrence"/>
  }

  item def <'lifecycle stage'> LifecycleStage specializes FiBO_FND_ExternalStubs::Classifier {
  	doc /* phase in a lifecycle */
  	// skos:definition xml:lang="en" (Lifecycles.rdf)
  	// rdfs:label xml:lang="en" (Lifecycles.rdf)
  	// RDF: fibo-fnd-arr-lif:LifecycleStage
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
  }

  item def <'lifecycle stage FiBOOccurrence'> LifecycleStageOccurrence specializes FiBOOccurrences::FiBOOccurrence {
  	doc /* realization of a phase in a given lifecycle */
  	// skos:definition xml:lang="en" (Lifecycles.rdf)
  	// rdfs:label xml:lang="en" (Lifecycles.rdf)
  	// RDF: fibo-fnd-arr-lif:LifecycleStageOccurrence
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-oc;FiBOOccurrence"/>
  }

  item def <'lifecycle status'> LifecycleStatus specializes FiBO_FND_ExternalStubs::Classifier {
  	doc /* classifier indicating the position or state of something at a particular point in its life-cycle */
  	// skos:definition xml:lang="en" (Lifecycles.rdf)
  	// rdfs:label xml:lang="en" (Lifecycles.rdf)
  	// RDF: fibo-fnd-arr-lif:LifecycleStatus
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
  }

}
