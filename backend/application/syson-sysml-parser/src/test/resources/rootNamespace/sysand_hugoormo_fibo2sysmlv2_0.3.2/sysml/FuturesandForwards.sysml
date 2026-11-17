// ===================================================================
// FuturesandForwards.sysml
// Source-parity extracted from: FiBO_DER_DerivativesContracts.sysml
// Primary source module: FuturesAndForwards.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FIBO Derivatives DER/DerivativesContracts
// ===================================================================
package Futures_and_Forwards {
  doc/* This ontology defines concepts for derivative contracts, including forwards and futures, representing a commitment to sell or purchase the underlier at a defined price at a given time in the future. (source: dct:abstract, FuturesAndForwards.rdf) */
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

  item def <'basket future'> BasketFuture specializes FinancialFuture {
    	doc /* futures contract whose underlying asset is a basket of securities and/or indices */
    	// skos:definition xml:lang="en" (FuturesAndForwards.rdf)
    	// rdfs:label xml:lang="en" (FuturesAndForwards.rdf)
    	// RDF: fibo-der-drc-ff:BasketFuture
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-ff;FinancialFuture"/>
    }

  item def <'bond future'> BondFuture specializes DebtInstrumentFuture {
    	doc /* futures contract whose underlying asset is at least one bond */
    	// skos:definition xml:lang="en" (FuturesAndForwards.rdf)
    	// rdfs:label xml:lang="en" (FuturesAndForwards.rdf)
    	// RDF: fibo-der-drc-ff:BondFuture
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-ff;DebtInstrumentFuture"/>
    }

  item def <'debt instrument future'> DebtInstrumentFuture specializes FinancialFuture, Security_Based_Derivatives::DebtInstrumentDerivative {
    	doc /* futures contract whose underlying asset is at least one debt instrument */
    	// skos:definition xml:lang="en" (FuturesAndForwards.rdf)
    	// rdfs:label xml:lang="en" (FuturesAndForwards.rdf)
    	// RDF: fibo-der-drc-ff:DebtInstrumentFuture
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-ff;FinancialFuture"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-sbd-sbd;DebtInstrumentDerivative"/>
    }

  item def <'dividend adjustment period'> DividendAdjustmentPeriod specializes FiBO_FND_ExternalStubs::DatePeriod {
    	doc /* date period used to calculate the difference, if any between an anticipated dividend and the actual dividend distributed in that period */
    	// skos:definition xml:lang="en" (FuturesAndForwards.rdf)
    	// rdfs:label xml:lang="en" (FuturesAndForwards.rdf)
    	// RDF: fibo-der-drc-ff:DividendAdjustmentPeriod
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-dt;DatePeriod"/>
      // subClassOf: DatePeriod - external CMNS
    }

  item def <'dividend future'> DividendFuture specializes FinancialFuture, Security_Based_Derivatives::EquityDerivative {
    	doc /* futures contract whose underlying asset is at least one stock dividend */
    	// skos:definition xml:lang="en" (FuturesAndForwards.rdf)
    	// rdfs:label xml:lang="en" (FuturesAndForwards.rdf)
    	// RDF: fibo-der-drc-ff:DividendFuture
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-ff;FinancialFuture"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-sbd-sbd;EquityDerivative"/>
    }

  item def <'equity forward'> EquityForward specializes Forward {
    	doc /* forward contract to buy or sell the underlying equity stock, equity index, basket of equity stock, equity futures contract, or equity option at a specified future date at the price specified at the outset of the contract */
    	// skos:definition xml:lang="en" (FuturesAndForwards.rdf)
    	// rdfs:label xml:lang="en" (FuturesAndForwards.rdf)
    	// RDF: fibo-der-drc-ff:EquityForward
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-ff;Forward"/>
    }

  item def <'equity future'> EquityFuture specializes FinancialFuture, Security_Based_Derivatives::EquityDerivative {
    	doc /* futures contract whose underlying asset is at least one equity security, specifically a publicly issued and traded share */
    	// skos:definition xml:lang="en" (FuturesAndForwards.rdf)
    	// rdfs:label xml:lang="en" (FuturesAndForwards.rdf)
    	// RDF: fibo-der-drc-ff:EquityFuture
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-ff;FinancialFuture"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-sbd-sbd;EquityDerivative"/>
    }

  item def <'financial future'> FinancialFuture specializes Future {
    	doc /* futures contract based on underlying assets excluding commodities */
    	// skos:definition xml:lang="en" (FuturesAndForwards.rdf)
    	// rdfs:label xml:lang="en" (FuturesAndForwards.rdf)
    	// RDF: fibo-der-drc-ff:FinancialFuture
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;Future"/>
    }

  item def <'forward contract adjustment method'> ForwardContractAdjustmentMethod specializes Objectives::Strategy {
    	doc /* method by which adjustments will be made to the contract should one or more of a number of extraordinary events occur */
    	// skos:definition xml:lang="en" (FuturesAndForwards.rdf)
    	// rdfs:label xml:lang="en" (FuturesAndForwards.rdf)
    	// RDF: fibo-der-drc-ff:ForwardContractAdjustmentMethod
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-gao-obj;Strategy"/>
    }

  item def <'forward'> Forward specializes Derivatives_Basics::OverTheCounterDerivativeInstrument, Derivatives_Basics::DerivativeInstrument {
    	doc /* derivative instrument that is privately negotiated between parties to buy the underlier at a specified future date at the price specified in the contract */
    	// skos:definition xml:lang="en" (FuturesAndForwards.rdf)
    	// rdfs:label xml:lang="en" (FuturesAndForwards.rdf)
    	// RDF: fibo-der-drc-ff:Forward
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-bsc;OverTheCounterDerivativeInstrument"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;DerivativeInstrument"/>
    }

  item def <'future on future'> FutureOnFuture specializes FinancialFuture {
    	doc /* futures contract whose underlying asset is at least one (other) futures contract */
    	// skos:definition xml:lang="en" (FuturesAndForwards.rdf)
    	// rdfs:label xml:lang="en" (FuturesAndForwards.rdf)
    	// RDF: fibo-der-drc-ff:FutureOnFuture
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-ff;FinancialFuture"/>
    }

  item def <'future on option'> FutureOnOption specializes FinancialFuture {
    	doc /* futures contract whose underlying asset is at least one option contract */
    	// skos:definition xml:lang="en" (FuturesAndForwards.rdf)
    	// rdfs:label xml:lang="en" (FuturesAndForwards.rdf)
    	// RDF: fibo-der-drc-ff:FutureOnOption
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-ff;FinancialFuture"/>
    }

  item def <'future on swap'> FutureOnSwap specializes FinancialFuture {
    	doc /* futures contract whose underlying asset is at least one swap contract */
    	// skos:definition xml:lang="en" (FuturesAndForwards.rdf)
    	// rdfs:label xml:lang="en" (FuturesAndForwards.rdf)
    	// RDF: fibo-der-drc-ff:FutureOnSwap
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-ff;FinancialFuture"/>
    }

  item def <'future'> Future specializes Derivatives_Basics::DerivativeInstrument {
    	doc /* derivative instrument that obligates the buyer to receive and the seller to deliver the assets specified at an agreed price, at some later point in time */
    	// skos:definition xml:lang="en" (FuturesandForwards.rdf)
    	// rdfs:label xml:lang="en" (FuturesandForwards.rdf)
    	// RDF: fibo-der-drc-ff:Future
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;DerivativeInstrument"/>
    }

  item def <'index future'> IndexFuture specializes FinancialFuture {
    	doc /* futures contract whose underlying asset is at least one reference index or economic indicator */
    	// skos:definition xml:lang="en" (FuturesAndForwards.rdf)
    	// rdfs:label xml:lang="en" (FuturesAndForwards.rdf)
    	// RDF: fibo-der-drc-ff:IndexFuture
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-ff;FinancialFuture"/>
    }

  item def <'interest rate future'> InterestRateFuture specializes FinancialFuture {
    	doc /* futures contract with an interest-bearing instrument as the underlying asset */
    	// skos:definition xml:lang="en" (FuturesAndForwards.rdf)
    	// rdfs:label xml:lang="en" (FuturesAndForwards.rdf)
    	// RDF: fibo-der-drc-ff:InterestRateFuture
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-ff;FinancialFuture"/>
    }

  item def <'money market future'> MoneyMarketFuture specializes DebtInstrumentFuture {
    	doc /* futures contract with a money market instrument as the underlying asset */
    	// skos:definition xml:lang="en" (FuturesAndForwards.rdf)
    	// rdfs:label xml:lang="en" (FuturesAndForwards.rdf)
    	// RDF: fibo-der-drc-ff:MoneyMarketFuture
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-ff;DebtInstrumentFuture"/>
    }

  item def <'standardized futures listing terms'> StandardizedFuturesListingTerms specializes Derivatives_Basics::DerivativeTerms, Financial_Instruments::StandardizedTerms {
    	doc /* contract terms established by a derivatives exchange that apply to any listing of a futures contract on that exchange. */
    	// skos:definition xml:lang="en" (FuturesAndForwards.rdf)
    	// rdfs:label xml:lang="en" (FuturesAndForwards.rdf)
    	// RDF: fibo-der-drc-ff:StandardizedFuturesListingTerms
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-bsc;DerivativeTerms"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;StandardizedTerms"/>
    }

  item def <'standardized futures terms'> StandardizedFuturesTerms specializes Derivatives_Basics::DerivativeTerms, Financial_Instruments::StandardizedTerms {
    	doc /* contract terms established by a derivatives exchange that apply to any futures contract traded on that exchange */
    	// skos:definition xml:lang="en" (FuturesAndForwards.rdf)
    	// rdfs:label xml:lang="en" (FuturesAndForwards.rdf)
    	// RDF: fibo-der-drc-ff:StandardizedFuturesTerms
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-bsc;DerivativeTerms"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;StandardizedTerms"/>
    }

}
