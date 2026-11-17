// ===================================================================
// EquitySwaps.sysml
// Source-parity extracted from: FiBO_DER_SecurityBasedDerivatives.sysml
// Primary source module: EquitySwaps.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FIBO Derivatives DER/SecurityBasedDerivatives
// ===================================================================
package Equity_Swaps {
  doc/* This ontology defines concepts specific to swap contracts in which one leg gives some form of return on an equity asset, including dividend returns, total asset returns equity dispersion and correlation measurement terms. Many of these return calculations are based on a variety of calculation methods and may vary widely. (source: dct:abstract, EquitySwaps.rdf) */
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

// DispersionLeg, SimpleReturnLeg, CorrelationSwap, ReturnSwap, ReturnLeg, Swap, TotalReturnSwap, DispersionSwap
// CashflowTerms
// ExplicitDatePeriod

  item def <'dispersion swap index constituents leg'> DispersionSwapIndexConstituentsLeg specializes Swaps::DispersionLeg {
  	doc /* dispersion leg whose underlier is a defined set of constituents of a given equity index */
  	// skos:definition xml:lang="en" (EquitySwaps.rdf)
  	// rdfs:label xml:lang="en" (EquitySwaps.rdf)
  	// RDF: fibo-der-sbd-eqs:DispersionSwapIndexConstituentsLeg
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-swp;DispersionLeg"/>
  }
  item def <'dispersion swap index leg'> DispersionSwapIndexLeg specializes Swaps::DispersionLeg {
  	doc /* dispersion leg whose underlier is an equity index */
  	// skos:definition xml:lang="en" (EquitySwaps.rdf)
  	// rdfs:label xml:lang="en" (EquitySwaps.rdf)
  	// RDF: fibo-der-sbd-eqs:DispersionSwapIndexLeg
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-swp;DispersionLeg"/>
  }
  item def <'dividend leg'> DividendLeg specializes Swaps::SimpleReturnLeg {
  	doc /* floating leg of a dividend swap */
  	// skos:definition xml:lang="en" (EquitySwaps.rdf)
  	// rdfs:label xml:lang="en" (EquitySwaps.rdf)
  	// RDF: fibo-der-sbd-eqs:DividendLeg
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-swp;SimpleReturnLeg"/>
  }
  item def <'dividend swap'> DividendSwap specializes EquitySwap {
  	doc /* equity swap that has at least one leg whose underlier is a dividend stream */
  	// skos:definition xml:lang="en" (EquitySwaps.rdf)
  	// rdfs:label xml:lang="en" (EquitySwaps.rdf)
  	// RDF: fibo-der-sbd-eqs:DividendSwap
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-sbd-eqs;EquitySwap"/>
  }
  item def <'equity correlation swap'> EquityCorrelationSwap specializes Swaps::CorrelationSwap, EquitySwap {
  	doc /* correlation swap that allows one to hedge risks associated with the observed average correlation of a collection of underlying equity products */
  	// skos:definition xml:lang="en" (EquitySwaps.rdf)
  	// rdfs:label xml:lang="en" (EquitySwaps.rdf)
  	// RDF: fibo-der-sbd-eqs:EquityCorrelationSwap
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-swp;CorrelationSwap"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-sbd-eqs;EquitySwap"/>
  }
  item def <'equity price return swap'> EquityPriceReturnSwap specializes Swaps::ReturnSwap, EquitySwap {
  	doc /* return swap whose return leg underlier is based on equities */
  	// skos:definition xml:lang="en" (EquitySwaps.rdf)
  	// rdfs:label xml:lang="en" (EquitySwaps.rdf)
  	// RDF: fibo-der-sbd-eqs:EquityPriceReturnSwap
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-swp;ReturnSwap"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-sbd-eqs;EquitySwap"/>
  }
  item def <'equity return leg'> EquityReturnLeg specializes Swaps::ReturnLeg {
  	doc /* return leg whose income is based on equities */
  	// skos:definition xml:lang="en" (EquitySwaps.rdf)
  	// rdfs:label xml:lang="en" (EquitySwaps.rdf)
  	// RDF: fibo-der-sbd-eqs:EquityReturnLeg
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-swp;ReturnLeg"/>
  }
  item def <'equity swap'> EquitySwap specializes Swaps::Swap, Security_Based_Derivatives::EquityDerivative {
  	doc /* swap whose payments are linked to the change in value of underlying equities (e.g. shares, basket of equities or index) or their cashflow(s) */
  	// skos:definition xml:lang="en" (EquitySwaps.rdf)
  	// rdfs:label xml:lang="en" (EquitySwaps.rdf)
  	// RDF: fibo-der-sbd-eqs:EquitySwap
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-swp;Swap"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-sbd-sbd;EquityDerivative"/>
  }
  item def <'equity total return swap'> EquityTotalReturnSwap specializes Swaps::TotalReturnSwap, EquitySwap {
  	doc /* total return swap whose return leg underlier is based on equities */
  	// skos:definition xml:lang="en" (EquitySwaps.rdf)
  	// rdfs:label xml:lang="en" (EquitySwaps.rdf)
  	// RDF: fibo-der-sbd-eqs:EquityTotalReturnSwap
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-swp;TotalReturnSwap"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-sbd-eqs;EquitySwap"/>
  }
  item def <'equity variance swap'> EquityVarianceSwap specializes Swaps::DispersionSwap, EquitySwap {
  	doc /* dispersion swap in which the parties agree to exchange payments based on the difference between (i) the realized variance of the price changes of a specified equity underlier over a stated observation period and (ii) a fixed amount of variance that is agreed when the contract is executed */
  	// skos:definition xml:lang="en" (EquitySwaps.rdf)
  	// rdfs:label xml:lang="en" (EquitySwaps.rdf)
  	// RDF: fibo-der-sbd-eqs:EquityVarianceSwap
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-swp;DispersionSwap"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-sbd-eqs;EquitySwap"/>
  }
  item def <'equity volatility swap'> EquityVolatilitySwap specializes Swaps::DispersionSwap, EquitySwap {
  	doc /* dispersion swap that is a forward contract on the variability of movements in the price of its underlying equities */
  	// skos:definition xml:lang="en" (EquitySwaps.rdf)
  	// rdfs:label xml:lang="en" (EquitySwaps.rdf)
  	// RDF: fibo-der-sbd-eqs:EquityVolatilitySwap
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-swp;DispersionSwap"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-sbd-eqs;EquitySwap"/>
  }
  item def <'qualifying dividend period'> QualifyingDividendPeriod specializes FiBO_FND_ExternalStubs::ExplicitDatePeriod {
  	doc /* date period over which the sum of all qualifying dividends paid by the relevant issuers as identified in the swap terms is calculated */
  	// skos:definition xml:lang="en" (EquitySwaps.rdf)
  	// rdfs:label xml:lang="en" (EquitySwaps.rdf)
  	// RDF: fibo-der-sbd-eqs:QualifyingDividendPeriod
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-dt;ExplicitDatePeriod"/>
    // subClassOf: ExplicitDatePeriod - external CMNS/FND
  }
  item def <'special dividend leg terms'> SpecialDividendLegTerms specializes Derivatives_Basics::CashflowTerms {
  	doc /* terms that are invoked if special dividend and memorial dividends are applicable */
  	// skos:definition xml:lang="en" (EquitySwaps.rdf)
  	// rdfs:label xml:lang="en" (EquitySwaps.rdf)
  	// RDF: fibo-der-sbd-eqs:SpecialDividendLegTerms
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-bsc;CashflowTerms"/>
  }
}

// --- SecurityBasedDerivatives.rdf ---
