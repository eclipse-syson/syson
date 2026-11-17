// ===================================================================
// Swaps.sysml
// Source-parity extracted from: FiBO_DER_DerivativesContracts.sysml
// Primary source module: Swaps.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FIBO Derivatives DER/DerivativesContracts
// ===================================================================
package Swaps {
  doc/* This ontology defines concepts specific to swap contracts, including relevant trading organizations, data repositories, and intermediaries. (source: dct:abstract, Swaps.rdf) */
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

  item def <'basis swap'> BasisSwap specializes RatesSwap {
    	doc /* floating leg of a correlation swap that pays an amount based on the observed average correlation of the prices of a collection of underlying products */
    	// skos:definition xml:lang="en" (Swaps.rdf)
    	// rdfs:label xml:lang="en" (Swaps.rdf)
    	// RDF: fibo-der-drc-swp:BasisSwap
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-swp;RatesSwap"/>
    }

  item def <'correlation leg'> CorrelationLeg specializes PerformanceBasedVariableLeg {
    	doc /* floating leg of a correlation swap that pays an amount based on the observed average correlation of the prices of a collection of underlying products */
    	// skos:definition xml:lang="en" (Swaps.rdf)
    	// rdfs:label xml:lang="en" (Swaps.rdf)
    	// RDF: fibo-der-drc-swp:CorrelationLeg
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-swp;PerformanceBasedVariableLeg"/>
    }

  item def <'correlation swap'> CorrelationSwap specializes StatisticalSwap {
    	doc /* over-the-counter statistical derivative that allows one to hedge risks associated with the observed average correlation of a collection of underlying products */
    	// skos:definition xml:lang="en" (Swaps.rdf)
    	// rdfs:label xml:lang="en" (Swaps.rdf)
    	// RDF: fibo-der-drc-swp:CorrelationSwap
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-swp;StatisticalSwap"/>
    }

  item def <'dispersion leg'> DispersionLeg specializes PerformanceBasedVariableLeg {
    	doc /* statistical derivative used to hedge on the magnitude of a price movement of an underlying asset */
    	// skos:definition xml:lang="en" (Swaps.rdf)
    	// rdfs:label xml:lang="en" (Swaps.rdf)
    	// RDF: fibo-der-drc-swp:DispersionLeg
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-swp;PerformanceBasedVariableLeg"/>
    }

  item def <'dispersion swap'> DispersionSwap specializes StatisticalSwap {
    	doc /* statistical derivative used to hedge on the magnitude of a price movement of an underlying asset */
    	// skos:definition xml:lang="en" (Swaps.rdf)
    	// rdfs:label xml:lang="en" (Swaps.rdf)
    	// RDF: fibo-der-drc-swp:DispersionSwap
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-swp;StatisticalSwap"/>
    }

  item def <'excess return swap'> ExcessReturnSwap specializes IndexReturnSwap {
    	doc /* return swap in which the change in the level of the index will be equal to the returns generated primarily by the changes in price of each of the contracts that comprise the index */
    	// skos:definition xml:lang="en" (Swaps.rdf)
    	// rdfs:label xml:lang="en" (Swaps.rdf)
    	// RDF: fibo-der-drc-swp:ExcessReturnSwap
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-swp;IndexReturnSwap"/>
    }

  item def <'fixed leg'> FixedLeg specializes SwapLeg {
    	doc /* return swap in which payments are based on a fee paid to the seller of the swap and on a floating reference price based on changes in the level of an index from an initial level to a level observed on some valuation date(s) */
    	// skos:definition xml:lang="en" (Swaps.rdf)
    	// rdfs:label xml:lang="en" (Swaps.rdf)
    	// RDF: fibo-der-drc-swp:FixedLeg
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-swp;SwapLeg"/>
    }

  item def <'fixed payment leg'> FixedPaymentLeg specializes SwapLeg {
    	doc /* swap leg that specifies contractual terms associated with a schedule of payments for any swap calculated by reference to a fixed annual rate */
    	// skos:definition xml:lang="en" (Swaps.rdf)
    	// rdfs:label xml:lang="en" (Swaps.rdf)
    	// RDF: fibo-der-drc-swp:FixedPaymentLeg
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-swp;SwapLeg"/>
    }

  item def <'floating leg'> FloatingLeg specializes SwapLeg {
    	doc /* swap leg whose paying party is obligated to make payments from time to time over the term of a swap calculated by reference to some variable rate, calculated based on the change in value of some underlying asset, or consisting of at least one payment of some floating amount */
    	// skos:definition xml:lang="en" (Swaps.rdf)
    	// rdfs:label xml:lang="en" (Swaps.rdf)
    	// RDF: fibo-der-drc-swp:FloatingLeg
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-swp;SwapLeg"/>
    }

  item def <'index return swap'> IndexReturnSwap specializes ReturnSwap {
    	doc /* return swap in which payments are based on a fee paid to the seller of the swap and on a floating reference price based on changes in the level of an index from an initial level to a level observed on some valuation date(s) */
    	// skos:definition xml:lang="en" (Swaps.rdf)
    	// rdfs:label xml:lang="en" (Swaps.rdf)
    	// RDF: fibo-der-drc-swp:IndexReturnSwap
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-swp;ReturnSwap"/>
    }

  item def <'major swap participant'> MajorSwapParticipant specializes Financial_Services_Entities::NonDepositoryInstitution {
    	doc /* swap leg of a rate-based swap based on a floating interest, floating inflation or fixed interest rate */
    	// skos:definition xml:lang="en" (Swaps.rdf)
    	// rdfs:label xml:lang="en" (Swaps.rdf)
    	// RDF: fibo-der-drc-swp:MajorSwapParticipant
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;NonDepositoryInstitution"/>
    }

  item def <'performance-based variable leg'> PerformanceBasedVariableLeg specializes FloatingLeg {
    	doc /* floating leg of a swap that depends on some statistical measure of the performance of the underlier */
    	// skos:definition xml:lang="en" (Swaps.rdf)
    	// rdfs:label xml:lang="en" (Swaps.rdf)
    	// RDF: fibo-der-drc-swp:PerformanceBasedVariableLeg
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-swp;FloatingLeg"/>
    }

  item def <'rate-based leg'> RateBasedLeg specializes SwapLeg {
    	doc /* swap leg of a rate-based swap based on a floating interest, floating inflation or fixed interest rate */
    	// skos:definition xml:lang="en" (Swaps.rdf)
    	// rdfs:label xml:lang="en" (Swaps.rdf)
    	// RDF: fibo-der-drc-swp:RateBasedLeg
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-swp;SwapLeg"/>
    }

  item def <'rates swap'> RatesSwap specializes Swap {
    	doc /* leg of a swap in which the income generated by some underlier is paid */
    	// skos:definition xml:lang="en" (Swaps.rdf)
    	// rdfs:label xml:lang="en" (Swaps.rdf)
    	// RDF: fibo-der-drc-swp:RatesSwap
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-swp;Swap"/>
    }

  item def <'realized variable leg'> RealizedVariableLeg specializes PerformanceBasedVariableLeg {
    	doc /* performance-based leg wherein the payment is netted at maturity rather than periodically */
    	// skos:definition xml:lang="en" (Swaps.rdf)
    	// rdfs:label xml:lang="en" (Swaps.rdf)
    	// RDF: fibo-der-drc-swp:RealizedVariableLeg
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-swp;PerformanceBasedVariableLeg"/>
    }

  item def <'return leg'> ReturnLeg specializes FloatingLeg {
    	doc /* leg of a swap in which the income generated by some underlier is paid */
    	// skos:definition xml:lang="en" (Swaps.rdf)
    	// rdfs:label xml:lang="en" (Swaps.rdf)
    	// RDF: fibo-der-drc-swp:ReturnLeg
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-swp;FloatingLeg"/>
    }

  item def <'return swap'> ReturnSwap specializes Swap {
    	doc /* swap in which one leg, the return leg, is based on income generated from some underlier */
    	// skos:definition xml:lang="en" (Swaps.rdf)
    	// rdfs:label xml:lang="en" (Swaps.rdf)
    	// RDF: fibo-der-drc-swp:ReturnSwap
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-swp;Swap"/>
    }

  item def <'simple return leg'> SimpleReturnLeg specializes ReturnLeg {
    	doc /* return leg based strictly on the income generated by the underlying asset, excluding appreciation, decrease in value, capital gains, and fees on any increase in value */
    	// skos:definition xml:lang="en" (Swaps.rdf)
    	// rdfs:label xml:lang="en" (Swaps.rdf)
    	// RDF: fibo-der-drc-swp:SimpleReturnLeg
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-swp;ReturnLeg"/>
    }

  item def <'statistical swap'> StatisticalSwap specializes Swap {
    	doc /* swap leg that specifies a fixed amount, &apos;the strike&apos;, quoted at the time of execution */
    	// skos:definition xml:lang="en" (Swaps.rdf)
    	// rdfs:label xml:lang="en" (Swaps.rdf)
    	// RDF: fibo-der-drc-swp:StatisticalSwap
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-swp;Swap"/>
    }

  item def <'strike leg'> StrikeLeg specializes FixedLeg {
    	doc /* swap leg that specifies a fixed amount, 'the strike', quoted at the time of execution */
    	// skos:definition xml:lang="en" (Swaps.rdf)
    	// rdfs:label xml:lang="en" (Swaps.rdf)
    	// RDF: fibo-der-drc-swp:StrikeLeg
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-swp;FixedLeg"/>
    }

  item def <'swap confirmation'> SwapConfirmation specializes SwapLifecycleEvent, Products_and_Services::TransactionConfirmation {
    	doc /* formal confirmation that codifies the terms and conditions specific to a lifecycle event with respect to the overall transaction between the parties */
    	// skos:definition xml:lang="en" (Swaps.rdf)
    	// rdfs:label xml:lang="en" (Swaps.rdf)
    	// RDF: fibo-der-drc-swp:SwapConfirmation
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-swp;SwapLifecycleEvent"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-pas;TransactionConfirmation"/>
    }

  item def <'swap data repository'> SwapDataRepository specializes Financial_Products_and_Services::FinancialServiceProvider {
    	doc /* financial service provider that provides a central facility for swap data reporting and recordkeeping */
    	// skos:definition xml:lang="en" (Swaps.rdf)
    	// rdfs:label xml:lang="en" (Swaps.rdf)
    	// RDF: fibo-der-drc-swp:SwapDataRepository
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;FinancialServiceProvider"/>
    }

  item def <'swap dealer'> SwapDealer specializes Financial_Services_Entities::NonDepositoryInstitution, Financial_Products_and_Services::Dealer {
    	doc /* non-depository institution such as one that deals in swaps, makes a market in swaps, regularly enters into swaps with counterparties as an ordinary course of business for its own account, and engages in any activity causing the person to be commonly known in the trade as a dealer/market maker in swaps */
    	// skos:definition xml:lang="en" (Swaps.rdf)
    	// rdfs:label xml:lang="en" (Swaps.rdf)
    	// RDF: fibo-der-drc-swp:SwapDealer
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;NonDepositoryInstitution"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;Dealer"/>
    }

  item def <'swap leg event'> SwapLegEvent specializes SwapLifecycleEvent {
    	doc /* swap lifecycle event, such as a payment or rate reset event, that applies to one leg of a swap */
    	// skos:definition xml:lang="en" (Swaps.rdf)
    	// rdfs:label xml:lang="en" (Swaps.rdf)
    	// RDF: fibo-der-drc-swp:SwapLegEvent
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-swp;SwapLifecycleEvent"/>
    }

  item def <'swap leg'> SwapLeg specializes Derivatives_Basics::CashflowTerms, SwapTerms { // RDF: fibo-der-drc-swp:SwapLeg
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-bsc;CashflowTerms"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-swp;SwapTerms"/>
      // RDF: fibo-der-drc-swp:SwapLeg
    }

  item def <'swap lifecycle event identifier'> SwapLifecycleEventIdentifier specializes FiBO_FND_ExternalStubs::Identifier {
    	doc /* identifier that uniquely identifies a specific event in the lifecycle of a swap */
    	// skos:definition xml:lang="en" (Swaps.rdf)
    	// rdfs:label xml:lang="en" (Swaps.rdf)
    	// RDF: fibo-der-drc-swp:SwapLifecycleEventIdentifier
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-id;Identifier"/>
      // subClassOf: Identifier - external CMNS
    }

  item def <'swap lifecycle event'> SwapLifecycleEvent specializes Lifecycles::LifecycleEvent {
    	doc /* event that occurs during the lifecycle of a swap, where the overall set of events includes those specific to the issuer, front office, middle office, and/or back office processes */
    	// skos:definition xml:lang="en" (Swaps.rdf)
    	// rdfs:label xml:lang="en" (Swaps.rdf)
    	// RDF: fibo-der-drc-swp:SwapLifecycleEvent
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-lif;LifecycleEvent"/>
    }

  item def <'swap party'> SwapParty specializes Contracts::ContractParty {
    	doc /* party to a swap and therefore a legal party to the contract that embodies that transaction */
    	// skos:definition xml:lang="en" (Swaps.rdf)
    	// rdfs:label xml:lang="en" (Swaps.rdf)
    	// RDF: fibo-der-drc-swp:SwapParty
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractParty"/>
    }

  item def <'swap paying party'> SwapPayingParty specializes Derivatives_Basics::PayingParty, SwapParty {
    	doc /* swap party responsible for making payments for a given leg of the transaction as defined in the contract */
    	// skos:definition xml:lang="en" (Swaps.rdf)
    	// rdfs:label xml:lang="en" (Swaps.rdf)
    	// RDF: fibo-der-drc-swp:SwapPayingParty
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-bsc;PayingParty"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-swp;SwapParty"/>
    }

  item def <'swap receiving party'> SwapReceivingParty specializes Derivatives_Basics::ReceivingParty, SwapParty {
    	doc /* swap party that receives payments for a given leg of the transaction as defined in the contract */
    	// skos:definition xml:lang="en" (Swaps.rdf)
    	// rdfs:label xml:lang="en" (Swaps.rdf)
    	// RDF: fibo-der-drc-swp:SwapReceivingParty
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-bsc;ReceivingParty"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-swp;SwapParty"/>
    }

  item def <'swap terms'> SwapTerms specializes Derivatives_Basics::DerivativeTerms {
    	doc /* legal contractual terms of a swap transaction */
    	// skos:definition xml:lang="en" (Swaps.rdf)
    	// rdfs:label xml:lang="en" (Swaps.rdf)
    	// RDF: fibo-der-drc-swp:SwapTerms
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-bsc;DerivativeTerms"/>
    }

  item def <'swap'> Swap specializes Derivatives_Basics::DerivativeInstrument {
    	doc /* return leg based on the income generated by the underlying asset as well as any appreciation, decrease in value, capital gains, and fees on any increase in value */
    	// skos:definition xml:lang="en" (Swaps.rdf)
    	// rdfs:label xml:lang="en" (Swaps.rdf)
    	// RDF: fibo-der-drc-swp:Swap
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;DerivativeInstrument"/>
    }

  item def <'total return leg'> TotalReturnLeg specializes ReturnLeg {
    	doc /* return leg based on the income generated by the underlying asset as well as any appreciation, decrease in value, capital gains, and fees on any increase in value */
    	// skos:definition xml:lang="en" (Swaps.rdf)
    	// rdfs:label xml:lang="en" (Swaps.rdf)
    	// RDF: fibo-der-drc-swp:TotalReturnLeg
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-swp;ReturnLeg"/>
    }

  item def <'total return swap'> TotalReturnSwap specializes Derivatives_Basics::CreditDerivative, ReturnSwap {
    	doc /* return swap where the seller agrees to pay the other party the difference in value of some underlying asset multiplied by an agreed-upon notional value should the asset value increase between specified periods of time */
    	// skos:definition xml:lang="en" (Swaps.rdf)
    	// rdfs:label xml:lang="en" (Swaps.rdf)
    	// RDF: fibo-der-drc-swp:TotalReturnSwap
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-bsc;CreditDerivative"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-swp;ReturnSwap"/>
    }

  item def <'unique swap identifier'> UniqueSwapIdentifier specializes Financial_Instruments::FinancialInstrumentIdentifier {
    	doc /* identifier for a swap that is used in recordkeeping and swap data reporting */
    	// skos:definition xml:lang="en" (Swaps.rdf)
    	// rdfs:label xml:lang="en" (Swaps.rdf)
    	// RDF: fibo-der-drc-swp:UniqueSwapIdentifier
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;FinancialInstrumentIdentifier"/>
    }

}
