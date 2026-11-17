// ===================================================================
// InterestRateSwaps.sysml
// Source-parity extracted from: FiBO_DER_RateDerivatives.sysml
// Primary source module: InterestRateSwaps.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FIBO Derivatives DER/RateDerivatives
// ===================================================================
package Interest_Rate_Swaps {
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

// InterestRateDerivative, RateBasedDerivative, Underlier
// RatesSwap, RateBasedLeg, BasisSwap, FixedLeg, FloatingLeg
// ContractLifecycleEventOccurrence
// ProjectedContractEventSchedule
// RecurrenceInterval
// ScalarQuantityValue
// StepEvent, StepSchedule

  item def <'cross-currency interest rate swap'> CrossCurrencyInterestRateSwap specializes InterestRateSwap {
  	doc /* floating rate leg of an inflation swap linked to an inflation index, such as the Consumer Price Index (CPI) */
  	// skos:definition xml:lang="en" (InterestRateSwaps.rdf)
  	// rdfs:label xml:lang="en" (InterestRateSwaps.rdf)
  	// RDF: fibo-der-rtd-irswp:CrossCurrencyInterestRateSwap
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-rtd-irswp;InterestRateSwap"/>
  }
  item def <'fixed fixed interest rate swap'> FixedFixedInterestRateSwap specializes InterestRateSwap {
  	doc /* interest rate swap in which both parties pay a fixed interest rate that they could not otherwise obtain outside of a swap arrangement */
  	// skos:definition xml:lang="en" (InterestRateSwaps.rdf)
  	// rdfs:label xml:lang="en" (InterestRateSwaps.rdf)
  	// RDF: fibo-der-rtd-irswp:FixedFixedInterestRateSwap
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-rtd-irswp;InterestRateSwap"/>
  }
  item def <'fixed float cross-currency interest rate swap'> FixedFloatCrossCurrencyInterestRateSwap specializes CrossCurrencyInterestRateSwap, FixedFloatInterestRateSwap {
  	doc /* interest rate swap in which fixed interest payments on the notional are exchanged for floating interest payments and the two streams of interest payments are in different currencies */
  	// skos:definition xml:lang="en" (InterestRateSwaps.rdf)
  	// rdfs:label xml:lang="en" (InterestRateSwaps.rdf)
  	// RDF: fibo-der-rtd-irswp:FixedFloatCrossCurrencyInterestRateSwap
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-rtd-irswp;CrossCurrencyInterestRateSwap"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-rtd-irswp;FixedFloatInterestRateSwap"/>
  }
  item def <'fixed float interest rate swap'> FixedFloatInterestRateSwap specializes InterestRateSwap {
  	doc /* interest rate swap in which fixed interest payments on the notional are exchanged for floating interest payments and the two streams of interest payments are in different currencies */
  	// skos:definition xml:lang="en" (InterestRateSwaps.rdf)
  	// rdfs:label xml:lang="en" (InterestRateSwaps.rdf)
  	// RDF: fibo-der-rtd-irswp:FixedFloatInterestRateSwap
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-rtd-irswp;InterestRateSwap"/>
  }
  item def <'fixed float single currency interest rate swap'> FixedFloatSingleCurrencyInterestRateSwap specializes FixedFloatInterestRateSwap {
  	doc /* interest rate swap in which fixed interest payments on the notional are exchanged for floating interest payments and where both payment streams are expressed in terms of the same currency */
  	// skos:definition xml:lang="en" (InterestRateSwaps.rdf)
  	// rdfs:label xml:lang="en" (InterestRateSwaps.rdf)
  	// RDF: fibo-der-rtd-irswp:FixedFloatSingleCurrencyInterestRateSwap
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-rtd-irswp;FixedFloatInterestRateSwap"/>
  }
  item def <'fixed interest rate leg'> FixedInterestRateLeg specializes Swaps::FixedLeg, InterestRateSwapLeg {
  	doc /* fixed leg that specifies fixed interest amounts and terms for the payment of that interest */
  	// skos:definition xml:lang="en" (InterestRateSwaps.rdf)
  	// rdfs:label xml:lang="en" (InterestRateSwaps.rdf)
  	// RDF: fibo-der-rtd-irswp:FixedInterestRateLeg
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-swp;FixedLeg"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-rtd-irswp;InterestRateSwapLeg"/>
  }
  item def <'float float cross-currency interest rate swap'> FloatFloatCrossCurrencyInterestRateSwap specializes FloatFloatInterestRateSwap {
  	doc /* interest rate swap that exchanges cashflows based on two different interest rates in different currencies */
  	// skos:definition xml:lang="en" (InterestRateSwaps.rdf)
  	// rdfs:label xml:lang="en" (InterestRateSwaps.rdf)
  	// RDF: fibo-der-rtd-irswp:FloatFloatCrossCurrencyInterestRateSwap
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-rtd-irswp;FloatFloatInterestRateSwap"/>
  }
  item def <'float float interest rate swap'> FloatFloatInterestRateSwap specializes Swaps::BasisSwap, InterestRateSwap {
  	doc /* interest rate swap that exchanges cashflows based on two different interest rates in different currencies */
  	// skos:definition xml:lang="en" (InterestRateSwaps.rdf)
  	// rdfs:label xml:lang="en" (InterestRateSwaps.rdf)
  	// RDF: fibo-der-rtd-irswp:FloatFloatInterestRateSwap
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-swp;BasisSwap"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-rtd-irswp;InterestRateSwap"/>
  }
  item def <'float float single currency interest rate swap'> FloatFloatSingleCurrencyInterestRateSwap specializes FloatFloatInterestRateSwap {
  	doc /* interest rate swap that exchanges cashflows based on two different floating interest rates in the same currency */
  	// skos:definition xml:lang="en" (InterestRateSwaps.rdf)
  	// rdfs:label xml:lang="en" (InterestRateSwaps.rdf)
  	// RDF: fibo-der-rtd-irswp:FloatFloatSingleCurrencyInterestRateSwap
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-rtd-irswp;FloatFloatInterestRateSwap"/>
  }
  item def <'floating interest rate leg'> FloatingInterestRateLeg specializes Swaps::FloatingLeg, InterestRateSwapLeg { // RDF: fibo-der-rtd-irswp:FloatingInterestRateLeg
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-swp;FloatingLeg"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-rtd-irswp;InterestRateSwapLeg"/>
    // RDF: fibo-der-rtd-irswp:FloatingInterestRateLeg
  }
  item def <'inflation leg'> InflationLeg specializes Swaps::FloatingLeg {
  	doc /* floating rate leg of an inflation swap linked to an inflation index, such as the Consumer Price Index (CPI) */
  	// skos:definition xml:lang="en" (InterestRateSwaps.rdf)
  	// rdfs:label xml:lang="en" (InterestRateSwaps.rdf)
  	// RDF: fibo-der-rtd-irswp:InflationLeg
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-swp;FloatingLeg"/>
  }
  item def <'inflation swap'> InflationSwap specializes Derivatives_Basics::RateBasedDerivative, Swaps::RatesSwap {
  	doc /* rate swap in which one party pays an amount calculated using an inflation rate index, and the other party pays an amount calculated using another inflation rate index, or a fixed or floating interest rate */
  	// skos:definition xml:lang="en" (InterestRateSwaps.rdf)
  	// rdfs:label xml:lang="en" (InterestRateSwaps.rdf)
  	// RDF: fibo-der-rtd-irswp:InflationSwap
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-bsc;RateBasedDerivative"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-swp;RatesSwap"/>
  }
  item def <'interest rate swap'> InterestRateSwap specializes Derivatives_Basics::InterestRateDerivative, Swaps::RatesSwap {
  	doc /* swap in which the periodic payments for one leg are based on an overnight interest rate index multiplied by the same notional amount upon which payments for the other leg of the swap are based */
  	// skos:definition xml:lang="en" (InterestRateSwaps.rdf)
  	// rdfs:label xml:lang="en" (InterestRateSwaps.rdf)
  	// RDF: fibo-der-rtd-irswp:InterestRateSwap
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-bsc;InterestRateDerivative"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-swp;RatesSwap"/>
  }
  item def <'interest rate swap leg'> InterestRateSwapLeg specializes Swaps::RateBasedLeg {
  	doc /* fixed leg that specifies fixed interest amounts and terms for the payment of that interest */
  	// skos:definition xml:lang="en" (InterestRateSwaps.rdf)
  	// rdfs:label xml:lang="en" (InterestRateSwaps.rdf)
  	// RDF: fibo-der-rtd-irswp:InterestRateSwapLeg
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-swp;RateBasedLeg"/>
  }
  item def <'notional step amount'> NotionalStepAmount specializes FiBO_FND_ExternalStubs::ScalarQuantityValue {
  	doc /* the amount of money that is subtracted from the notional on each step date */
  	// skos:definition xml:lang="en" (InterestRateSwaps.rdf)
  	// rdfs:label xml:lang="en" (InterestRateSwaps.rdf)
  	// RDF: fibo-der-rtd-irswp:NotionalStepAmount
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-qtu;ScalarQuantityValue"/>
    // subClassOf: ScalarQuantityValue - external CMNS
  }
  item def <'notional step change event'> NotionalStepChangeEvent specializes Financial_Products_and_Services::ContractLifecycleEventOccurrence, Debt_Instruments::StepEvent {
  	doc /* event in which a step change in the notional amount for a given swap leg occurs */
  	// skos:definition xml:lang="en" (InterestRateSwaps.rdf)
  	// rdfs:label xml:lang="en" (InterestRateSwaps.rdf)
  	// RDF: fibo-der-rtd-irswp:NotionalStepChangeEvent
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;ContractLifecycleEventOccurrence"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-dbti;StepEvent"/>
  }
  item def <'notional step period length'> NotionalStepPeriodLength specializes Financial_Dates::RecurrenceInterval {
  	doc /* recurrence interval indicating the frequency with which step changes occur, which is a multiple of the calculation period in the calculation schedule */
  	// skos:definition xml:lang="en" (InterestRateSwaps.rdf)
  	// rdfs:label xml:lang="en" (InterestRateSwaps.rdf)
  	// RDF: fibo-der-rtd-irswp:NotionalStepPeriodLength
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-fd;RecurrenceInterval"/>
  }
  item def <'notional step schedule'> NotionalStepSchedule specializes Debt::ProjectedContractEventSchedule, Debt_Instruments::StepSchedule {
  	doc /* schedule of changes in the notional amount on which interest is paid, comprising the regular sequence of step events */
  	// skos:definition xml:lang="en" (InterestRateSwaps.rdf)
  	// rdfs:label xml:lang="en" (InterestRateSwaps.rdf)
  	// RDF: fibo-der-rtd-irswp:NotionalStepSchedule
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;ProjectedContractEventSchedule"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-dbti;StepSchedule"/>
  }
  item def <'overnight index swap'> OvernightIndexSwap specializes InterestRateSwap {
  	doc /* swap in which the periodic payments for one leg are based on an overnight interest rate index multiplied by the same notional amount upon which payments for the other leg of the swap are based */
  	// skos:definition xml:lang="en" (InterestRateSwaps.rdf)
  	// rdfs:label xml:lang="en" (InterestRateSwaps.rdf)
  	// RDF: fibo-der-rtd-irswp:OvernightIndexSwap
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-rtd-irswp;InterestRateSwap"/>
  }
  item def <'overnight rate index leg'> OvernightRateIndexLeg specializes FloatingInterestRateLeg {
  	doc /* floating leg in which periodic payments are based on an overnight interest rate index multiplied by the same notional amount on which the payments for the other leg of the swap are based */
  	// skos:definition xml:lang="en" (InterestRateSwaps.rdf)
  	// rdfs:label xml:lang="en" (InterestRateSwaps.rdf)
  	// RDF: fibo-der-rtd-irswp:OvernightRateIndexLeg
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-rtd-irswp;FloatingInterestRateLeg"/>
  }
  item def <'plain vanilla interest rate swap'> PlainVanillaInterestRateSwap specializes FixedFloatSingleCurrencyInterestRateSwap {
  	doc /* fixed-float single currency interest rate swap in which interest payments are netted, the notional principal does not change, and there are no embedded options */
  	// skos:definition xml:lang="en" (InterestRateSwaps.rdf)
  	// rdfs:label xml:lang="en" (InterestRateSwaps.rdf)
  	// RDF: fibo-der-rtd-irswp:PlainVanillaInterestRateSwap
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-rtd-irswp;FixedFloatSingleCurrencyInterestRateSwap"/>
  }
  item def <'single currency interest rate swap'> SingleCurrencyInterestRateSwap specializes InterestRateSwap {
  	doc /* interest rate swap in which fixed interest payments on the notional are exchanged for floating interest payments and where both payment streams are expressed in terms of the same currency */
  	// skos:definition xml:lang="en" (InterestRateSwaps.rdf)
  	// rdfs:label xml:lang="en" (InterestRateSwaps.rdf)
  	// RDF: fibo-der-rtd-irswp:SingleCurrencyInterestRateSwap
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-rtd-irswp;InterestRateSwap"/>
  }
  item def <'zero coupon interest rate swap'> ZeroCouponInterestRateSwap specializes InterestRateSwap {
  	doc /* interest rate swap in which the fixed rate cash flows are compounded and paid once on the expiration date, rather than periodically; the payments on the other side (which can be based on a floating interest rate or a fixed rate) follow typical swap payment schedules */
  	// skos:definition xml:lang="en" (InterestRateSwaps.rdf)
  	// rdfs:label xml:lang="en" (InterestRateSwaps.rdf)
  	// RDF: fibo-der-rtd-irswp:ZeroCouponInterestRateSwap
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-rtd-irswp;InterestRateSwap"/>
  }
}

// --- RateDerivatives.rdf ---
