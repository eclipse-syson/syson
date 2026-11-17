// ===================================================================
// ExerciseConventions.sysml
// Source-parity extracted from: FiBO_SEC_Debt.sysml
// Primary source module: ExerciseConventions.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Securities SEC/Debt
// ===================================================================
package Exercise_Conventions {
  doc/* This ontology defines the various kinds of exercise conventions that are common to debt and options instruments. They are distinguished primarily in terms of the date period during which an optional contract clause may be exercised. (source: dct:abstract, ExerciseConventions.rdf) */
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

  item def <'exercise terms'> ExerciseTerms specializes Contracts::ContractualCommitment {
  	doc /* contract terms specific to the conditions, conventions and other stipulations related to the exercise of an option or entitlement */
  	// skos:definition xml:lang="en" (ExerciseConventions.rdf)
  	// rdfs:label xml:lang="en" (ExerciseConventions.rdf)
  	// RDF: fibo-sec-dbt-exc:ExerciseTerms
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractualCommitment"/>
  }
  item def <'exercise convention'> ExerciseConvention specializes Business_Dates::Convention {
  	doc /* convention that determines when the holder or future holder of an option can implement the rights defined in the option */
  	// skos:definition xml:lang="en" (ExerciseConventions.rdf)
  	// rdfs:label xml:lang="en" (ExerciseConventions.rdf)
  	// RDF: fibo-sec-dbt-exc:ExerciseConvention
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-bd;Convention"/>
  }
  item def <'American exercise terms'> AmericanExerciseTerms specializes ExerciseTerms {
  	doc /* exercise terms that stipulate that an option may be exercised on or before the date of expiration */
  	// skos:definition xml:lang="en" (ExerciseConventions.rdf)
  	// rdfs:label xml:lang="en" (ExerciseConventions.rdf)
  	// RDF: fibo-sec-dbt-exc:AmericanExerciseTerms
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-ex;ExerciseTerms"/>
  }
  item def <'Bermudan exercise terms'> BermudanExerciseTerms specializes ExerciseTerms {
  	doc /* exercise terms that stipulate that an option may only be exercised on predetermined dates within some exercise window, often on one day each month or at the date of expiration */
  	// skos:definition xml:lang="en" (ExerciseConventions.rdf)
  	// rdfs:label xml:lang="en" (ExerciseConventions.rdf)
  	// RDF: fibo-sec-dbt-exc:BermudanExerciseTerms
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-ex;ExerciseTerms"/>
  }
  item def <'canary exercise terms'> CanaryExerciseTerms specializes BermudanExerciseTerms {
  	doc /* exercise terms that stipulate that an option may only be exercised on predetermined dates until the first step is reached, but not after that point */
  	// skos:definition xml:lang="en" (ExerciseConventions.rdf)
  	// rdfs:label xml:lang="en" (ExerciseConventions.rdf)
  	// RDF: fibo-sec-dbt-exc:CanaryExerciseTerms
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-ex;BermudanExerciseTerms"/>
  }
  item def <'European exercise terms'> EuropeanExerciseTerms specializes ExerciseTerms {
  	doc /* exercise terms that stipulate that an option may only be exercised at the date of expiration */
  	// skos:definition xml:lang="en" (ExerciseConventions.rdf)
  	// rdfs:label xml:lang="en" (ExerciseConventions.rdf)
  	// RDF: fibo-sec-dbt-exc:EuropeanExerciseTerms
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-ex;ExerciseTerms"/>
  }
}

// --- MortgageBackedSecurities.rdf ---
