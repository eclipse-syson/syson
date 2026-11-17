// ===================================================================
// ExoticOptions.sysml
// Source-parity extracted from: FiBO_DER_DerivativesContracts.sysml
// Primary source module: ExoticOptions.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FIBO Derivatives DER/DerivativesContracts
// ===================================================================
package Exotic_Options {
  doc/* This ontology covers exotic options, a category of options contracts that differ from traditional options in their payment structures, expiration dates, and strike prices. The underlying asset or security can vary with exotic options allowing for more investment alternatives. Exotic options are hybrid securities that are often customizable to the needs of the investor, and most are traded over the counter (OTC). (source: dct:abstract, ExoticOptions.rdf) */
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

  item def <'Asian option classifier'> AsianOptionClassifier specializes Securities_Classification::FinancialInstrumentClassifier {
    	doc /* method used for calculating the average rate or price of an Asian option */
    	// skos:definition xml:lang="en" (ExoticOptions.rdf)
    	// rdfs:label xml:lang="en" (ExoticOptions.rdf)
    	// RDF: fibo-der-drc-exo:AsianOptionClassifier
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-cls;FinancialInstrumentClassifier"/>
    }

  item def <'Asian option'> AsianOption specializes Options::ExoticOption {
    	doc /* option whose exercise terms involve a payoff determined by the average underlying price (either the strike price or the settlement price) of the underlying asset over a predetermined period */
    	// skos:definition xml:lang="en" (ExoticOptions.rdf)
    	// rdfs:label xml:lang="en" (ExoticOptions.rdf)
    	// RDF: fibo-der-drc-exo:AsianOption
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-opt;ExoticOption"/>
    }

  item def <'averaging strategy'> AveragingStrategy specializes Objectives::Strategy {
    	doc /* method used for calculating the average rate or price of an Asian option */
    	// skos:definition xml:lang="en" (ExoticOptions.rdf)
    	// rdfs:label xml:lang="en" (ExoticOptions.rdf)
    	// RDF: fibo-der-drc-exo:AveragingStrategy
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-gao-obj;Strategy"/>
    }

  item def <'barrier option'> BarrierOption specializes Options::ExoticOption {
    	doc /* option whose final exercise depends upon the path taken by the price of an underlying instrument, i.e., whose payoff depends on whether or not the underlying asset has reached or exceeded a predetermined price */
    	// skos:definition xml:lang="en" (ExoticOptions.rdf)
    	// rdfs:label xml:lang="en" (ExoticOptions.rdf)
    	// RDF: fibo-der-drc-exo:BarrierOption
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-opt;ExoticOption"/>
    }

  item def <'chooser option'> ChooserOption specializes Options::ExoticOption {
    	doc /* exotic option that allows its holder (buyer) to exercise the right to buy (call) or sell (put) specified assets at a fixed price whereby the buyer does not have to decide whether the contract will be a put or a call until an agreed future date, prior to expiration */
    	// skos:definition xml:lang="en" (ExoticOptions.rdf)
    	// rdfs:label xml:lang="en" (ExoticOptions.rdf)
    	// RDF: fibo-der-drc-exo:ChooserOption
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-opt;ExoticOption"/>
    }

  item def <'cliquet option'> CliquetOption specializes Options::ExoticOption {
    	doc /* exotic option that is a series of at-the-money (ATM) options, either puts or calls, where each successive option becomes active when the previous one expires */
    	// skos:definition xml:lang="en" (ExoticOptions.rdf)
    	// rdfs:label xml:lang="en" (ExoticOptions.rdf)
    	// RDF: fibo-der-drc-exo:CliquetOption
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-opt;ExoticOption"/>
    }

  item def <'commodore option'> CommodoreOption specializes Options::ExoticOption {
    	doc /* exotic option consisting of a number of digital barrier options that pay a coupon if a pre-determined level of the underlying or basket of underlyings is reached */
    	// skos:definition xml:lang="en" (ExoticOptions.rdf)
    	// rdfs:label xml:lang="en" (ExoticOptions.rdf)
    	// RDF: fibo-der-drc-exo:CommodoreOption
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-opt;ExoticOption"/>
    }

  item def <'compound option'> CompoundOption specializes Options::ExoticOption {
    	doc /* exotic option for which the underlying asset is another option */
    	// skos:definition xml:lang="en" (ExoticOptions.rdf)
    	// rdfs:label xml:lang="en" (ExoticOptions.rdf)
    	// RDF: fibo-der-drc-exo:CompoundOption
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-opt;ExoticOption"/>
    }

  item def <'digital option'> DigitalOption specializes BarrierOption {
    	doc /* exotic option that has a pre-determined payout if the option is in-the-money and the payoff condition is satisfied */
    	// skos:definition xml:lang="en" (ExoticOptions.rdf)
    	// rdfs:label xml:lang="en" (ExoticOptions.rdf)
    	// RDF: fibo-der-drc-exo:DigitalOption
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-exo;BarrierOption"/>
    }

  item def <'double barrier option'> DoubleBarrierOption specializes BarrierOption {
    	doc /* barrier option applied to currencies or over the counter stocks that works as a binary, or digital option in that it pays out only under defined circumstances, or it is worthless, at expiration */
    	// skos:definition xml:lang="en" (ExoticOptions.rdf)
    	// rdfs:label xml:lang="en" (ExoticOptions.rdf)
    	// RDF: fibo-der-drc-exo:DoubleBarrierOption
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-exo;BarrierOption"/>
    }

  item def <'fixed lookback strike expression'> FixedLookbackStrikeExpression specializes Derivatives_Basics::CashflowExpression {
    	doc /* cashflow expression specifying the arguments required to calculate the fixed price at which the lookback option is to be exercised */
    	// skos:definition xml:lang="en" (ExoticOptions.rdf)
    	// rdfs:label xml:lang="en" (ExoticOptions.rdf)
    	// RDF: fibo-der-drc-exo:FixedLookbackStrikeExpression
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-bsc;CashflowExpression"/>
    }

  item def <'floating lookback strike expression'> FloatingLookbackStrikeExpression specializes Derivatives_Basics::CashflowExpression {
    	doc /* cashflow expression specifying the arguments required to calculate the best projected price at which the lookback option may be exercised */
    	// skos:definition xml:lang="en" (ExoticOptions.rdf)
    	// rdfs:label xml:lang="en" (ExoticOptions.rdf)
    	// RDF: fibo-der-drc-exo:FloatingLookbackStrikeExpression
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-bsc;CashflowExpression"/>
    }

  item def <'forward start option'> ForwardStartOption specializes Options::ExoticOption {
    	doc /* exotic option that is fully specified with respect to a set expiry date, underlying asset and other parameters, purchased and paid for on initiation, but that becomes active at a set activation date with a strike price determined at the time of activation */
    	// skos:definition xml:lang="en" (ExoticOptions.rdf)
    	// rdfs:label xml:lang="en" (ExoticOptions.rdf)
    	// RDF: fibo-der-drc-exo:ForwardStartOption
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-opt;ExoticOption"/>
    }

  item def <'interest rate cap option'> InterestRateCapOption specializes Options::InterestRateOption {
    	doc /* interest rate derivative in which the buyer receives payments at the end of each period in which the interest rate exceeds the agreed strike price */
    	// skos:definition xml:lang="en" (ExoticOptions.rdf)
    	// rdfs:label xml:lang="en" (ExoticOptions.rdf)
    	// RDF: fibo-der-drc-exo:InterestRateCapOption
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-opt;InterestRateOption"/>
    }

  item def <'interest rate floor option'> InterestRateFloorOption specializes Options::InterestRateOption {
    	doc /* interest rate derivative in which the buyer receives payments at the end of each period in which the interest rate is below the agreed strike price */
    	// skos:definition xml:lang="en" (ExoticOptions.rdf)
    	// rdfs:label xml:lang="en" (ExoticOptions.rdf)
    	// RDF: fibo-der-drc-exo:InterestRateFloorOption
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-opt;InterestRateOption"/>
    }

  item def <'knock-in option'> KnockInOption specializes BarrierOption {
    	doc /* barrier option that is not triggered until a certain price threshold is met */
    	// skos:definition xml:lang="en" (ExoticOptions.rdf)
    	// rdfs:label xml:lang="en" (ExoticOptions.rdf)
    	// RDF: fibo-der-drc-exo:KnockInOption
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-exo;BarrierOption"/>
    }

  item def <'knock-out option'> KnockOutOption specializes BarrierOption {
    	doc /* barrier option with a built-in mechanism to expire as worthless if a specified price level in the underlying asset is reached */
    	// skos:definition xml:lang="en" (ExoticOptions.rdf)
    	// rdfs:label xml:lang="en" (ExoticOptions.rdf)
    	// RDF: fibo-der-drc-exo:KnockOutOption
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-exo;BarrierOption"/>
    }

  item def <'lookback option'> LookbackOption specializes Options::ExoticOption {
    	doc /* exotic option that minimizes the uncertainties related to the timing of market entry, providing the holder the advantage of knowing history when determining when to exercise their option */
    	// skos:definition xml:lang="en" (ExoticOptions.rdf)
    	// rdfs:label xml:lang="en" (ExoticOptions.rdf)
    	// RDF: fibo-der-drc-exo:LookbackOption
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-opt;ExoticOption"/>
    }

  item def <'lookback strike terms'> LookbackStrikeTerms specializes Derivatives_Basics::DerivativeTerms {
    	doc /* terms specifying the value of the underlying asset based on analysis during a specific period, typically ending in the maturity of the option, whereby the payoff is determined by comparing the strike price with the value of the selected price */
    	// skos:definition xml:lang="en" (ExoticOptions.rdf)
    	// rdfs:label xml:lang="en" (ExoticOptions.rdf)
    	// RDF: fibo-der-drc-exo:LookbackStrikeTerms
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-bsc;DerivativeTerms"/>
    }

  item def <'low exercise price option'> LowExercisePriceOption specializes Options::CallOption, Options::ExoticOption {
    	doc /* exotic option that is a European-style call option with an exercise price of one cent that mimics a futures contract */
    	// skos:definition xml:lang="en" (ExoticOptions.rdf)
    	// rdfs:label xml:lang="en" (ExoticOptions.rdf)
    	// RDF: fibo-der-drc-exo:LowExercisePriceOption
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-opt;CallOption"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-opt;ExoticOption"/>
    }

  item def <'mountain range option'> MountainRangeOption specializes Options::ExoticOption {
    	doc /* exotic option based on multiple underlying securities */
    	// skos:definition xml:lang="en" (ExoticOptions.rdf)
    	// rdfs:label xml:lang="en" (ExoticOptions.rdf)
    	// RDF: fibo-der-drc-exo:MountainRangeOption
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-opt;ExoticOption"/>
    }

  item def <'observed best value'> ObservedBestValue specializes Instrument_Pricing::MarketPrice {
    	doc /* observed value of the underlying asset as of some date during the lookback period that is considered the best value from the perspective of the option holder during the lookback period (depending on whether the option is a call or put) */
    	// skos:definition xml:lang="en" (ExoticOptions.rdf)
    	// rdfs:label xml:lang="en" (ExoticOptions.rdf)
    	// RDF: fibo-der-drc-exo:ObservedBestValue
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-ip;MarketPrice"/>
    }

  item def <'projected value at maturity'> ProjectedValueAtMaturity specializes Instrument_Pricing::CalculatedPrice {
    	doc /* expected value of the underlying asset at maturity calculated as of some date during the lookback period */
    	// skos:definition xml:lang="en" (ExoticOptions.rdf)
    	// rdfs:label xml:lang="en" (ExoticOptions.rdf)
    	// RDF: fibo-der-drc-exo:ProjectedValueAtMaturity
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-cur;CalculatedPrice"/>
    }

  item def <'rainbow option'> RainbowOption specializes Options::ExoticOption {
    	doc /* exotic option linked to the performances of two or more underlying assets */
    	// skos:definition xml:lang="en" (ExoticOptions.rdf)
    	// rdfs:label xml:lang="en" (ExoticOptions.rdf)
    	// RDF: fibo-der-drc-exo:RainbowOption
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-opt;ExoticOption"/>
    }

  item def <'swaption'> Swaption specializes Options::ExoticOption {
    	doc /* over-the-counter option that confers the right but not the obligation, to enter into a swap transaction at some time in the future */
    	// skos:definition xml:lang="en" (ExoticOptions.rdf)
    	// rdfs:label xml:lang="en" (ExoticOptions.rdf)
    	// RDF: fibo-der-drc-exo:Swaption
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-opt;ExoticOption"/>
    }

}
