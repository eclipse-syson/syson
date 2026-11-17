// ===================================================================
// FiBOOccurrences.sysml
// Source-parity extracted from: FiBO_FND_DatesAndTimes.sysml
// Primary source module: FiBOOccurrences.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Foundations FND/DatesAndTimes
// ===================================================================

package FiBOOccurrences {
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

  item def Calculation specializes FiBOOccurrence {
  	doc /* actual execution of some computation, computational process, or operation that was scheduled or triggered by something */
  	// skos:definition xml:lang="en" (FiBOOccurrences.rdf)
    // rdfs:label xml:lang="en" (FiBOOccurrences.rdf)
  	// RDF: fibo-fnd-dt-oc:Calculation
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-oc;FiBOOccurrence"/>
  }
  item def <'calculation event'> CalculationEvent specializes OccurrenceKind {
  	doc /* kind of event that is either scheduled or triggered by something, such as a related financial event, that causes a calculation to be performed */
  	// skos:definition xml:lang="en" (FiBOOccurrences.rdf)
  	// rdfs:label xml:lang="en" (FiBOOccurrences.rdf)
  	// RDF: fibo-fnd-dt-oc:CalculationEvent
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-oc;OccurrenceKind"/>
  }
  item def FiBOOccurrence { // RDF: fibo-fnd-dt-oc:FiBOOccurrence
    // RDF: fibo-fnd-dt-oc:FiBOOccurrence
  }
  item def <'FiBOOccurrence-based date'> OccurrenceBasedDate specializes Financial_Dates::CalculatedDate {
  	doc /* calculated date that is defined with respect to the FiBOOccurrence of some FiBOOccurrence kind */
  	// skos:definition xml:lang="en" (FiBOOccurrences.rdf)
  	// rdfs:label xml:lang="en" (FiBOOccurrences.rdf)
  	// RDF: fibo-fnd-dt-oc:OccurrenceBasedDate
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-fd;CalculatedDate"/>
  }
  item def <'FiBOOccurrence kind'> OccurrenceKind specializes FiBO_FND_ExternalStubs::Classifier {
  	doc /* classifier that specifies the general nature of an FiBOOccurrence (event) */
  	// skos:definition xml:lang="en" (FiBOOccurrences.rdf)
  	// rdfs:label xml:lang="en" (FiBOOccurrences.rdf)
  	// RDF: fibo-fnd-dt-oc:OccurrenceKind
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
  }

}
