// ===================================================================
// FIBOAnnotationVocabulary.sysml
// Source-parity extracted from: FiBO_FND_Utilities.sysml
// Primary source module: AnnotationVocabulary.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Foundations FND/Utilities
// ===================================================================

package FIBO_Annotation_Vocabulary {
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

  item def <'maturity level'> MaturityLevel specializes Lifecycles::LifecycleStage, FiBO_FND_ExternalStubs::Classifier {
    doc /* classifier used to indicate the state of an artifact with respect to its development lifecycle */
  	// skos:definition xml:lang="en" (AnnotationVocabulary.rdf)
  	// rdfs:label xml:lang="en" (AnnotationVocabulary.rdf)
  	// RDF: fibo-fnd-utl-av:MaturityLevel
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-lif;LifecycleStage"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
  }
  item def <'module'> Module specializes FiBO_FND_ExternalStubs::Classifier {
  	doc /* classifier used to indicate a category used to modularize something based on principles of the model driven architecture methodology (MDA), including but not limited to separation of concerns, coherence, and establishing clear logical boundaries in order to increase reusability and maintainability */
  	// skos:definition xml:lang="en" (AnnotationVocabulary.rdf)
  	// rdfs:label xml:lang="en" (AnnotationVocabulary.rdf)
  	// RDF: fibo-fnd-utl-av:Module
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
  }
}
