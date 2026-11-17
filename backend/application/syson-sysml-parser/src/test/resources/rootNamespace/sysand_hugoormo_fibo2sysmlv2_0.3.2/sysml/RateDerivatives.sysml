// ===================================================================
// RateDerivatives.sysml
// Source-parity extracted from: FiBO_DER_RateDerivatives.sysml
// Primary source module: RateDerivatives.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FIBO Derivatives DER/RateDerivatives
// ===================================================================
package Rate_Derivatives {
  doc/* This ontology defines concepts that are common to derivatives based on variation in some defined variable, such as an economic rate, an interest rate or an index value. (source: dct:abstract, RateDerivatives.rdf) */
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

// EconomicRateBasedDerivative, ForwardRateAgreement, InterestRateDerivative, RateBasedDerivative, Underlier

  item def EconomicRateBasedDerivativeInstrument specializes Derivatives_Basics::EconomicRateBasedDerivative { // RDF: fibo-der-rtd-rtd:EconomicRateBasedDerivativeInstrument
    // RDF: fibo-der-rtd-rtd:EconomicRateBasedDerivativeInstrument
  }
  item def EconomicRateObservable specializes Derivatives_Basics::Underlier { // RDF: fibo-der-rtd-rtd:EconomicRateObservable
    // RDF: fibo-der-rtd-rtd:EconomicRateObservable
  }
  item def ForeignExchangeRateObservable specializes Derivatives_Basics::Underlier { // RDF: fibo-der-rtd-rtd:ForeignExchangeRateObservable
    // RDF: fibo-der-rtd-rtd:ForeignExchangeRateObservable
  }
  item def <'forward rate agreement'> ForwardRateAgreement specializes Derivatives_Basics::InterestRateDerivative {
  	doc /* agreement to exchange an interest rate commitment on a notional amount at a future date */
  	// skos:definition xml:lang="en" (RateDerivatives.rdf)
  	// rdfs:label xml:lang="en" (RateDerivatives.rdf)
  	// RDF: fibo-der-rtd-rtd:ForwardRateAgreement
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-bsc;InterestRateDerivative"/>
    // subClassOf: ForwardRateAgreement from DerivativesBasics (same local name)
  }
  item def InterestRateDerivativeInstrument specializes Derivatives_Basics::InterestRateDerivative { // RDF: fibo-der-rtd-rtd:InterestRateDerivativeInstrument
    // RDF: fibo-der-rtd-rtd:InterestRateDerivativeInstrument
  }
  item def InterestRateObservable specializes Derivatives_Basics::Underlier { // RDF: fibo-der-rtd-rtd:InterestRateObservable
    // RDF: fibo-der-rtd-rtd:InterestRateObservable
  }
  item def RateBasedDerivativeInstrument specializes Derivatives_Basics::RateBasedDerivative { // RDF: fibo-der-rtd-rtd:RateBasedDerivativeInstrument
    // RDF: fibo-der-rtd-rtd:RateBasedDerivativeInstrument
  }
  item def RateBasedObservable specializes Derivatives_Basics::Underlier { // RDF: fibo-der-rtd-rtd:RateBasedObservable
  }
    // RDF: fibo-der-rtd-rtd:RateBasedObservable
}
