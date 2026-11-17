// ===================================================================
// CurrencyContracts.sysml
// Source-parity extracted from: FiBO_DER_DerivativesContracts.sysml
// Primary source module: CurrencyContracts.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FIBO Derivatives DER/DerivativesContracts
// ===================================================================
package Currency_Contracts {
  doc/* This ontology defines concepts common to currency spot contracts and foreign exchange derivatives (forwards, options and swaps). (source: dct:abstract, CurrencyContracts.rdf) */
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

  item def <'currency derivative'> CurrencyDerivative specializes Financial_Instruments::CurrencyInstrument, Derivatives_Basics::DerivativeInstrument {
    	doc /* agreement to deliver and settle a given amount of money in one currency, in exchange for a given amount in another currency at an agreed rate of exchange */
    	// skos:definition xml:lang="en" (CurrencyContracts.rdf)
    	// rdfs:label xml:lang="en" (CurrencyContracts.rdf)
    	// RDF: fibo-der-drc-cur:CurrencyDerivative
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;CurrencyInstrument"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;DerivativeInstrument"/>
    }

  item def <'currency forward outright'> CurrencyForwardOutright specializes CurrencyForward {
    	doc /* forward contract in a foreign exchange market that locks in the price at which an entity must buy or sell a currency on a future date */
    	// skos:definition xml:lang="en" (CurrencyContracts.rdf)
    	// rdfs:label xml:lang="en" (CurrencyContracts.rdf)
    	// RDF: fibo-der-drc-cur:CurrencyForwardOutright
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-cur;CurrencyForward"/>
    }

  item def <'currency forward'> CurrencyForward specializes CurrencyDerivative, Futures_and_Forwards::Forward {
    	doc /* agreement to deliver and settle a given amount of money in one currency, in exchange for a given amount in another currency, at an agreed date in the future and at an agreed rate of exchange */
    	// skos:definition xml:lang="en" (CurrencyContracts.rdf)
    	// rdfs:label xml:lang="en" (CurrencyContracts.rdf)
    	// RDF: fibo-der-drc-cur:CurrencyForward
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-cur;CurrencyDerivative"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-ff;Forward"/>
    }

  item def <'currency future'> CurrencyFuture specializes CurrencyDerivative, Futures_and_Forwards::FinancialFuture {
    	doc /* futures contract whose underlying asset is an agreement to exchange a specified amount of one currency for another at some point in the future based on pricing stated in the contract */
    	// skos:definition xml:lang="en" (CurrencyContracts.rdf)
    	// rdfs:label xml:lang="en" (CurrencyContracts.rdf)
    	// RDF: fibo-der-drc-cur:CurrencyFuture
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-ff;FinancialFuture"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-cur;CurrencyDerivative"/>
    }

  item def <'currency option'> CurrencyOption specializes CurrencyDerivative, Options::VanillaOption {
    	doc /* option giving the buyer (holder) the right, but not the obligation, to buy or sell currency at a specified exchange rate during a specified period of time */
    	// skos:definition xml:lang="en" (CurrencyContracts.rdf)
    	// rdfs:label xml:lang="en" (CurrencyContracts.rdf)
    	// RDF: fibo-der-drc-cur:CurrencyOption
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-cur;CurrencyDerivative"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-opt;VanillaOption"/>
    }

  item def <'currency spot contract'> CurrencySpotContract specializes Financial_Instruments::CurrencyInstrument, Financial_Instruments::SpotContract {
    	doc /* foreign-exchange contract for immediate delivery */
    	// skos:definition xml:lang="en" (CurrencyContracts.rdf)
    	// rdfs:label xml:lang="en" (CurrencyContracts.rdf)
    	// RDF: fibo-der-drc-cur:CurrencySpotContract
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;CurrencyInstrument"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;SpotContract"/>
    }

  item def <'currency spot forward swap'> CurrencySpotForwardSwap specializes CurrencySwap {
    	doc /* foreign exchange agreement between two parties involving an exchange of two currencies at agreed fixed rates: (a) on the spot settlement date and (b) a reverse exchange on a later specified date */
    	// skos:definition xml:lang="en" (CurrencyContracts.rdf)
    	// rdfs:label xml:lang="en" (CurrencyContracts.rdf)
    	// RDF: fibo-der-drc-cur:CurrencySpotForwardSwap
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-cur;CurrencySwap"/>
    }

  item def <'currency swap'> CurrencySwap specializes CurrencyDerivative, Swaps::Swap {
    	doc /* foreign exchange agreement between two parties to exchange a given amount of one currency for another currency for spot delivery or for forward delivery at an agreed rate after a specified period of time */
    	// skos:definition xml:lang="en" (CurrencyContracts.rdf)
    	// rdfs:label xml:lang="en" (CurrencyContracts.rdf)
    	// RDF: fibo-der-drc-cur:CurrencySwap
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-cur;CurrencyDerivative"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-swp;Swap"/>
    }

  item def <'currency volatility option'> CurrencyVolatilityOption specializes CurrencyOption {
    	doc /* currency option whose underlying asset is based on the volatility of a foreign exchange rate */
    	// skos:definition xml:lang="en" (CurrencyContracts.rdf)
    	// rdfs:label xml:lang="en" (CurrencyContracts.rdf)
    	// RDF: fibo-der-drc-cur:CurrencyVolatilityOption
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-cur;CurrencyOption"/>
    }

  item def <'foreign exchange master agreement'> ForeignExchangeMasterAgreement specializes Contracts::MasterAgreement {
    	doc /* product-specific master agreement intended to reflect best market practice and to provide a standard agreement for participants in the foreign exchange markets */
    	// skos:definition xml:lang="en" (CurrencyContracts.rdf)
    	// rdfs:label xml:lang="en" (CurrencyContracts.rdf)
    	// RDF: fibo-der-drc-cur:ForeignExchangeMasterAgreement
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;MasterAgreement"/>
    }

  item def <'foreign exchange settlement netting provision'> ForeignExchangeSettlementNettingProvision specializes Contracts::SettlementNettingProvision {
    	doc /* settlement netting provision that is specific to foreign exchange contracts */
    	// skos:definition xml:lang="en" (CurrencyContracts.rdf)
    	// rdfs:label xml:lang="en" (CurrencyContracts.rdf)
    	// RDF: fibo-der-drc-cur:ForeignExchangeSettlementNettingProvision
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;SettlementNettingProvision"/>
    }

}
