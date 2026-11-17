// ===================================================================
// Options.sysml
// Source-parity extracted from: FiBO_DER_DerivativesContracts.sysml
// Primary source module: Options.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FIBO Derivatives DER/DerivativesContracts
// ===================================================================
package Options {
  doc/* Concepts common to all option contracts. An option gives one party (the holder) the right to purchase or sell the underlying instrument at a given time or times in the future (as determined by the exercise convention), if they choose to do so. (source: dct:abstract, Options.rdf) */
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

  item def <'basket option'> BasketOption specializes ExoticOption {
    	doc /* option whose underlying asset is a group, or basket, of commodities, securities, indices, or currencies */
    	// skos:definition xml:lang="en" (Options.rdf)
    	// rdfs:label xml:lang="en" (Options.rdf)
    	// RDF: fibo-der-drc-opt:BasketOption
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-opt;ExoticOption"/>
    }

  item def <'bond option'> BondOption specializes FixedIncomeOption {
    	doc /* option giving the buyer (holder) the right, but not the obligation, to buy or sell (depending on whether it is a call or a put) a bond at a certain price on or before a specified date */
    	// skos:definition xml:lang="en" (Options.rdf)
    	// rdfs:label xml:lang="en" (Options.rdf)
    	// RDF: fibo-der-drc-opt:BondOption
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-opt;FixedIncomeOption"/>
    }

  item def <'butterfly'> Butterfly specializes OptionTradingStrategy {
    	doc /* strategy that combines bull and bear spreads with a fixed risk and capped profit */
    	// skos:definition xml:lang="en" (Options.rdf)
    	// rdfs:label xml:lang="en" (Options.rdf)
    	// RDF: fibo-der-drc-opt:Butterfly
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-opt;OptionTradingStrategy"/>
    }

  item def <'call option'> CallOption specializes Option {
    	doc /* option giving the buyer (holder) the right, but not the obligation, to buy the assets specified at a fixed price or formula, on or before a specified date */
    	// skos:definition xml:lang="en" (Options.rdf)
    	// rdfs:label xml:lang="en" (Options.rdf)
    	// RDF: fibo-der-drc-opt:CallOption
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;Option"/>
    }

  item def <'capped option'> CappedOption specializes VanillaOption {
    	doc /* option with an established profit cap or cap price */
    	// skos:definition xml:lang="en" (Options.rdf)
    	// rdfs:label xml:lang="en" (Options.rdf)
    	// RDF: fibo-der-drc-opt:CappedOption
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-opt;VanillaOption"/>
    }

  item def <'collar'> Collar specializes OptionTradingStrategy {
    	doc /* option trading strategy that involves buying a downside put and selling an upside call that is implemented to protect against large losses, but which also limits large upside gains */
    	// skos:definition xml:lang="en" (Options.rdf)
    	// rdfs:label xml:lang="en" (Options.rdf)
    	// RDF: fibo-der-drc-opt:Collar
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-opt;OptionTradingStrategy"/>
    }

  item def <'condor spread'> CondorSpread specializes OptionTradingStrategy {
    	doc /* non-directional options strategy that limits both gains and losses while seeking to profit from either low or high volatility */
    	// skos:definition xml:lang="en" (Options.rdf)
    	// rdfs:label xml:lang="en" (Options.rdf)
    	// RDF: fibo-der-drc-opt:CondorSpread
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-opt;OptionTradingStrategy"/>
    }

  item def <'covered call'> CoveredCall specializes CallOption {
    	doc /* call option in which the seller (investor) owns an equivalent amount of the underlying security */
    	// skos:definition xml:lang="en" (Options.rdf)
    	// rdfs:label xml:lang="en" (Options.rdf)
    	// RDF: fibo-der-drc-opt:CoveredCall
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-opt;CallOption"/>
    }

  item def <'equity option'> EquityOption specializes VanillaOption, Security_Based_Derivatives::EquityDerivative {
    	doc /* option giving the buyer (holder) the right, but not the obligation, to buy (via a call option) or sell (via a put option) the underlying equity assets specified at a pre-determined price (i.e., the strike price, fixed or calculated), on or before a specified date (the expiration date) */
    	// skos:definition xml:lang="en" (Options.rdf)
    	// rdfs:label xml:lang="en" (Options.rdf)
    	// RDF: fibo-der-drc-opt:EquityOption
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-opt;VanillaOption"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-sbd-sbd;EquityDerivative"/>
    }

  item def <'exotic option'> ExoticOption specializes Derivatives_Basics::OverTheCounterDerivativeInstrument, Option {
    	doc /* option that has a non-standard payout structure or other feature */
    	// skos:definition xml:lang="en" (Options.rdf)
    	// rdfs:label xml:lang="en" (Options.rdf)
    	// RDF: fibo-der-drc-opt:ExoticOption
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-bsc;OverTheCounterDerivativeInstrument"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;Option"/>
    }

  item def <'extrinsic value'> ExtrinsicValue specializes Currency_Amount::MonetaryAmount {
    	doc /* measure of the difference between the market price of an option, called the premium, and its intrinsic value */
    	// skos:definition xml:lang="en" (Options.rdf)
    	// rdfs:label xml:lang="en" (Options.rdf)
    	// RDF: fibo-der-drc-opt:ExtrinsicValue
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-cur;MonetaryAmount"/>
    }

  item def <'fence'> Fence specializes OptionTradingStrategy {
    	doc /* option trading strategy that uses options to limit the range of possible returns on a financial instrument */
    	// skos:definition xml:lang="en" (Options.rdf)
    	// rdfs:label xml:lang="en" (Options.rdf)
    	// RDF: fibo-der-drc-opt:Fence
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-opt;OptionTradingStrategy"/>
    }

  item def <'fixed income option'> FixedIncomeOption specializes VanillaOption, Security_Based_Derivatives::DebtInstrumentDerivative {
    	doc /* option giving the buyer (holder) the right, but not the obligation, to buy (via a call option) or sell (via a put option) the underlying fixed income assets specified at a pre-determined price (i.e., the strike price, fixed or calculated), on or before a specified date (the expiration date) */
    	// skos:definition xml:lang="en" (Options.rdf)
    	// rdfs:label xml:lang="en" (Options.rdf)
    	// RDF: fibo-der-drc-opt:FixedIncomeOption
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-opt;VanillaOption"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-sbd-sbd;DebtInstrumentDerivative"/>
    }

  item def <'interest rate option'> InterestRateOption specializes Derivatives_Basics::InterestRateDerivative, VanillaOption {
    	doc /* option that giving the buyer (holder) the right, but not the obligation, to receive a cash payment if market interest rate of a reference rate is higher or lower, depending on the option, than the strike rate of the option */
    	// skos:definition xml:lang="en" (Options.rdf)
    	// rdfs:label xml:lang="en" (Options.rdf)
    	// RDF: fibo-der-drc-opt:InterestRateOption
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-bsc;InterestRateDerivative"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-opt;VanillaOption"/>
    }

  item def <'intrinsic value'> IntrinsicValue specializes Currency_Amount::MonetaryAmount {
    	doc /* measure of what an asset is worth, i.e. with respect to its current price */
    	// skos:definition xml:lang="en" (Options.rdf)
    	// rdfs:label xml:lang="en" (Options.rdf)
    	// RDF: fibo-der-drc-opt:IntrinsicValue
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-cur;MonetaryAmount"/>
    }

  item def <'iron butterfly'> IronButterfly specializes Butterfly {
    	doc /* butterfly strategy that consists of two call options and two put options, three strike prices and the same expiration date */
    	// skos:definition xml:lang="en" (Options.rdf)
    	// rdfs:label xml:lang="en" (Options.rdf)
    	// RDF: fibo-der-drc-opt:IronButterfly
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-opt;Butterfly"/>
    }

  item def <'iron condor'> IronCondor specializes CondorSpread {
    	doc /* condor strategy consisting of two puts (one long and one short) and two calls (one long and one short), and four strike prices, all with the same expiration date */
    	// skos:definition xml:lang="en" (Options.rdf)
    	// rdfs:label xml:lang="en" (Options.rdf)
    	// RDF: fibo-der-drc-opt:IronCondor
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-opt;CondorSpread"/>
    }

  item def <'long-term equity anticipation security'> LongTermEquityAnticipationSecurity specializes EquityOption {
    	doc /* equity option that allows the holder to buy or sell shares of stock with expiration dates that are longer than one year, and typically up to three years from issue */
    	// skos:definition xml:lang="en" (Options.rdf)
    	// rdfs:label xml:lang="en" (Options.rdf)
    	// RDF: fibo-der-drc-opt:LongTermEquityAnticipationSecurity
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-opt;EquityOption"/>
    }

  item def <'moneyness'> Moneyness specializes FiBO_FND_ExternalStubs::Classifier {
    	doc /* classifier for a derivative relating its strike price to the price of its underlying asset */
    	// skos:definition xml:lang="en" (Options.rdf)
    	// rdfs:label xml:lang="en" (Options.rdf)
    	// RDF: fibo-der-drc-opt:Moneyness
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
      // subClassOf: Classifier - external CMNS
    }

  item def <'option holder'> OptionHolder specializes Ownership::Owner {
    	doc /* party that owns an option */
    	// skos:definition xml:lang="en" (Options.rdf)
    	// rdfs:label xml:lang="en" (Options.rdf)
    	// RDF: fibo-der-drc-opt:OptionHolder
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-oac-own;Owner"/>
    }

  item def <'option issuer'> OptionIssuer specializes Financial_Instruments::Issuer {
    	doc /* issuer granting the rights defined in the option in exchange for some consideration */
    	// skos:definition xml:lang="en" (Options.rdf)
    	// rdfs:label xml:lang="en" (Options.rdf)
    	// RDF: fibo-der-drc-opt:OptionIssuer
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;Issuer"/>
    }

  item def <'option on future'> OptionOnFuture specializes VanillaOption {
    	doc /* option that gives the holder the right, but not the obligation, to buy or sell a specific futures contract at a specified price on or before the option&apos;s expiration date */
    	// skos:definition xml:lang="en" (Options.rdf)
    	// rdfs:label xml:lang="en" (Options.rdf)
    	// RDF: fibo-der-drc-opt:OptionOnFuture
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-opt;VanillaOption"/>
    }

  item def <'option premium formula'> OptionPremiumFormula specializes Analytics::Expression {
    	doc /* expression used to calculate the premium based either on the price per option or percentage of the notional amount */
    	// skos:definition xml:lang="en" (Options.rdf)
    	// rdfs:label xml:lang="en" (Options.rdf)
    	// RDF: fibo-der-drc-opt:OptionPremiumFormula
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-qtu;Expression"/>
      // subClassOf: Expression - external CMNS
    }

  item def <'option premium'> OptionPremium specializes Instrument_Pricing::MarketPrice {
    	doc /* current market price of an option contract */
    	// skos:definition xml:lang="en" (Options.rdf)
    	// rdfs:label xml:lang="en" (Options.rdf)
    	// RDF: fibo-der-drc-opt:OptionPremium
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-ip;MarketPrice"/>
    }

  item def <'option trading strategy'> OptionTradingStrategy specializes Financial_Products_and_Services::TradingStrategy {
    	doc /* trading tactic involving more than one option type, strike price, or expiration date on the same underlying asset */
    	// skos:definition xml:lang="en" (Options.rdf)
    	// rdfs:label xml:lang="en" (Options.rdf)
    	// RDF: fibo-der-drc-opt:OptionTradingStrategy
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;TradingStrategy"/>
    }

  item def <'option'> Option specializes Derivatives_Basics::DerivativeInstrument {
    	doc /* derivative instrument that grants to the holder either the privilege to purchase or the privilege to sell the assets specified at a predetermined price or formula at or within a time period in the future */
    	// skos:definition xml:lang="en" (Options.rdf)
    	// rdfs:label xml:lang="en" (Options.rdf)
    	// RDF: fibo-der-drc-opt:Option
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;DerivativeInstrument"/>
    }

  item def <'protective collar'> ProtectiveCollar specializes Collar {
    	doc /* collar that consists of a covered call and protective put */
    	// skos:definition xml:lang="en" (Options.rdf)
    	// rdfs:label xml:lang="en" (Options.rdf)
    	// RDF: fibo-der-drc-opt:ProtectiveCollar
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-opt;Collar"/>
    }

  item def <'protective put'> ProtectivePut specializes PutOption {
    	doc /* put option giving the buyer (holder) the right, but not the obligation, to sell the assets specified at with a strike price equal or close to the current price of the underlying asset, on or before a specified date */
    	// skos:definition xml:lang="en" (Options.rdf)
    	// rdfs:label xml:lang="en" (Options.rdf)
    	// RDF: fibo-der-drc-opt:ProtectivePut
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-opt;PutOption"/>
    }

  item def <'put option'> PutOption specializes Option {
    	doc /* option giving the buyer (holder) the right, but not the obligation, to sell the assets specified at a fixed price or formula, on or before a specified date */
    	// skos:definition xml:lang="en" (Options.rdf)
    	// rdfs:label xml:lang="en" (Options.rdf)
    	// RDF: fibo-der-drc-opt:PutOption
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;Option"/>
    }

  item def <'risk reversal'> RiskReversal specializes OptionTradingStrategy {
    	doc /* option trading strategy that consists of being short (selling) an out of the money put and being long (i.e., buying) an out of the money call, both with the same maturity */
    	// skos:definition xml:lang="en" (Options.rdf)
    	// rdfs:label xml:lang="en" (Options.rdf)
    	// RDF: fibo-der-drc-opt:RiskReversal
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-opt;OptionTradingStrategy"/>
    }

  item def <'standardized options terms'> StandardizedOptionsTerms specializes Financial_Instruments::StandardizedTerms {
    	doc /* standardized contract terms established by a securities or options exchange or by an options clearing entity */
    	// skos:definition xml:lang="en" (Options.rdf)
    	// rdfs:label xml:lang="en" (Options.rdf)
    	// RDF: fibo-der-drc-opt:StandardizedOptionsTerms
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;StandardizedTerms"/>
    }

  item def <'straddle'> Straddle specializes OptionTradingStrategy {
    	doc /* neutral option trading strategy that involves simultaneously buying both a put option and a call option for the underlying security with the same strike price and the same expiration date */
    	// skos:definition xml:lang="en" (Options.rdf)
    	// rdfs:label xml:lang="en" (Options.rdf)
    	// RDF: fibo-der-drc-opt:Straddle
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-opt;OptionTradingStrategy"/>
    }

  item def <'strangle'> Strangle specializes OptionTradingStrategy {
    	doc /* option trading strategy in which the investor holds a position in both a call and a put option with different strike prices, but with the same expiration date and underlying asset */
    	// skos:definition xml:lang="en" (Options.rdf)
    	// rdfs:label xml:lang="en" (Options.rdf)
    	// RDF: fibo-der-drc-opt:Strangle
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-opt;OptionTradingStrategy"/>
    }

  item def <'strike price'> StrikePrice specializes Currency_Amount::MonetaryPrice {
    	doc /* price at which a lot of the underlying asset may be bought or sold when the contract is exercised */
    	// skos:definition xml:lang="en" (Options.rdf)
    	// rdfs:label xml:lang="en" (Options.rdf)
    	// RDF: fibo-der-drc-opt:StrikePrice
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-cur;MonetaryPrice"/>
    }

  item def <'strip strategy'> StripStrategy specializes OptionTradingStrategy {
    	doc /* option trading strategy that involves a sequential set of options with periodic reset dates, such that each period between resets behaves like an individual option */
    	// skos:definition xml:lang="en" (Options.rdf)
    	// rdfs:label xml:lang="en" (Options.rdf)
    	// RDF: fibo-der-drc-opt:StripStrategy
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-opt;OptionTradingStrategy"/>
    }

  item def <'vanilla option'> VanillaOption specializes Option {
    	doc /* common option giving the buyer (holder) the right, but not the obligation, to buy (via a call option) or sell (via a put option) the underlying assets specified at a pre-determined price (i.e., the strike price, fixed or calculated), on or before a specified date (the expiration date) */
    	// skos:definition xml:lang="en" (Options.rdf)
    	// rdfs:label xml:lang="en" (Options.rdf)
    	// RDF: fibo-der-drc-opt:VanillaOption
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;Option"/>
    }

}
