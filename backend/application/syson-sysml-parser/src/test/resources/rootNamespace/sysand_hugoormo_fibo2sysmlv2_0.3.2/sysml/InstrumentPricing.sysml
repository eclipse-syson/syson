// ===================================================================
// InstrumentPricing.sysml
// Source-parity extracted from: FiBO_FBC_FinancialInstruments.sysml
// Primary source module: InstrumentPricing.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Financial Business and Commerce FBC/FinancialInstruments
// Structural note: translated as a split file from FiBO_FBC_FinancialInstruments umbrella.
// ===================================================================
package Instrument_Pricing {
  doc/* This ontology provides a basic set of definitions related to pricing, yield, and spread that are extended in other instrument-specific ontologies. (source: dct:abstract, InstrumentPricing.rdf) */
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

    item def <'adjusted closing price'> AdjustedClosingPrice specializes ClosingPrice {
      doc /* amended closing price to reflect a security's value after accounting for any corporate actions, such as stock splits, dividends, and rights offerings */
      // skos:definition xml:lang="en" (InstrumentPricing.rdf)
      // rdfs:label xml:lang="en" (InstrumentPricing.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-ip;ClosingPrice"/>
      // RDF: fibo-fbc-fi-ip:AdjustedClosingPrice
    }

    item def <'best bid'> BestBid specializes BidPrice {
      doc /* highest bid price a prospective buyer is willing to pay at a particular time for a given security */
      // skos:definition xml:lang="en" (InstrumentPricing.rdf)
      // rdfs:label xml:lang="en" (InstrumentPricing.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-ip;BidPrice"/>
      // RDF: fibo-fbc-fi-ip:BestBid
    }

    item def <'best offer'> BestOffer specializes OfferPrice {
      doc /* lowest price acceptable to a prospective seller for a given security at a particular point in time */
      // skos:definition xml:lang="en" (InstrumentPricing.rdf)
      // rdfs:label xml:lang="en" (InstrumentPricing.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-ip;OfferPrice"/>
      // RDF: fibo-fbc-fi-ip:BestOffer
    }

    item def <'bid ask spread'> BidAskSpread specializes PriceSpread {
      doc /* difference between an offer (ask) price and a bid price */
      // skos:definition xml:lang="en" (InstrumentPricing.rdf)
      // rdfs:label xml:lang="en" (InstrumentPricing.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-ip;PriceSpread"/>
      // RDF: fibo-fbc-fi-ip:BidAskSpread
    }

    item def <'bid price'> BidPrice specializes SecurityPrice {
      doc /* price a prospective buyer is willing to pay */
      // skos:definition xml:lang="en" (InstrumentPricing.rdf)
      // rdfs:label xml:lang="en" (InstrumentPricing.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-ip;SecurityPrice"/>
      // RDF: fibo-fbc-fi-ip:BidPrice
    }

    item def <'calculated price'> CalculatedPrice specializes Currency_Amount::MonetaryPrice {
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-cur;MonetaryPrice"/>
    	doc /* monetary price determined by a formula */     	// skos:definition xml:lang="en" (CurrencyAmount.rdf)
    	// rdfs:label xml:lang="en" (CurrencyAmount.rdf)
      // RDF: fibo-fbc-fi-ip:CalculatedPrice
    }

    item def <'clean price'> CleanPrice specializes SecurityPrice {
      doc /* debt instrument price that does not include accrued interest */
      // skos:definition xml:lang="en" (InstrumentPricing.rdf)
      // rdfs:label xml:lang="en" (InstrumentPricing.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-ip;SecurityPrice"/>
      // RDF: fibo-fbc-fi-ip:CleanPrice
    }

    item def <'closing price'> ClosingPrice specializes MarketPrice {
      doc /* cash value of the last transacted price before the market closes */
      // skos:definition xml:lang="en" (InstrumentPricing.rdf)
      // rdfs:label xml:lang="en" (InstrumentPricing.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-ip;MarketPrice"/>
      // RDF: fibo-fbc-fi-ip:ClosingPrice
    }

    item def <'closing price determination method'> ClosingPriceDeterminationMethod specializes PriceDeterminationMethod {
      doc /* strategy for calculating or otherwise determining an official closing price */
      // skos:definition xml:lang="en" (InstrumentPricing.rdf)
      // rdfs:label xml:lang="en" (InstrumentPricing.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-ip;PriceDeterminationMethod"/>
      // RDF: fibo-fbc-fi-ip:ClosingPriceDeterminationMethod
    }

    item def <'collection of security prices'> CollectionOfSecurityPrices specializes Financial_Dates::DatedStructuredCollection {
      doc /* collection consisting of a series of prices, each of which has a specific date and time associated with it, for some security */
      // skos:definition xml:lang="en" (InstrumentPricing.rdf)
      // rdfs:label xml:lang="en" (InstrumentPricing.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-fd;DatedStructuredCollection"/>
      // RDF: fibo-fbc-fi-ip:CollectionOfSecurityPrices
      // subClassOf: DatedStructuredCollection - external CMNS
    }

    item def <'composite market'> CompositeMarket specializes FiBO_FND_ExternalStubs::StructuredCollection {
      doc /* group of exchanges and trading venues referenced for pricing purposes */
      // skos:definition xml:lang="en" (InstrumentPricing.rdf)
      // rdfs:label xml:lang="en" (InstrumentPricing.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-col;StructuredCollection"/>
      // RDF: fibo-fbc-fi-ip:CompositeMarket
      // subClassOf: StructuredCollection - external CMNS
    }

    item def <'derived price'> DerivedPrice specializes CalculatedPrice, SecurityPrice {
      doc /* price that stems from another source or calculation rather than being quoted or based on actual trading data */
      // skos:definition xml:lang="en" (InstrumentPricing.rdf)
      // rdfs:label xml:lang="en" (InstrumentPricing.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-cur;CalculatedPrice"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-ip;SecurityPrice"/>
      // RDF: fibo-fbc-fi-ip:DerivedPrice
    }

    item def <'dirty price'> DirtyPrice specializes SecurityPrice {
      doc /* debt instrument price that includes accrued interest */
      // skos:definition xml:lang="en" (InstrumentPricing.rdf)
      // rdfs:label xml:lang="en" (InstrumentPricing.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-ip;SecurityPrice"/>
      // RDF: fibo-fbc-fi-ip:DirtyPrice
    }

    item def <'high price'> HighPrice specializes SecurityPrice {
      doc /* highest price for a given security over the period specified */
      // skos:definition xml:lang="en" (InstrumentPricing.rdf)
      // rdfs:label xml:lang="en" (InstrumentPricing.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-ip;SecurityPrice"/>
      // RDF: fibo-fbc-fi-ip:HighPrice
    }

    item def <'internal rate of return'> InternalRateOfReturn specializes RateOfReturn {
      doc /* discount rate that results in a net present value (NPV) of zero for a series of future cash flows */
      // skos:definition xml:lang="en" (InstrumentPricing.rdf)
      // rdfs:label xml:lang="en" (InstrumentPricing.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-ip;RateOfReturn"/>
      // RDF: fibo-fbc-fi-ip:InternalRateOfReturn
    }

    item def <'intra day price'> IntraDayPrice specializes MarketPrice {
      doc /* price for a given security at some point between the opening and official closing price on an exchange */
      // skos:definition xml:lang="en" (InstrumentPricing.rdf)
      // rdfs:label xml:lang="en" (InstrumentPricing.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-ip;MarketPrice"/>
      // RDF: fibo-fbc-fi-ip:IntraDayPrice
    }

    item def <'low price'> LowPrice specializes SecurityPrice {
      doc /* lowest value for a given security over the period specified */
      // skos:definition xml:lang="en" (InstrumentPricing.rdf)
      // rdfs:label xml:lang="en" (InstrumentPricing.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-ip;SecurityPrice"/>
      // RDF: fibo-fbc-fi-ip:LowPrice
    }

    item def <'market price'> MarketPrice specializes SecurityPrice {
      doc /* last reported price at which a security was sold */
      // skos:definition xml:lang="en" (InstrumentPricing.rdf)
      // rdfs:label xml:lang="en" (InstrumentPricing.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-ip;SecurityPrice"/>
      // RDF: fibo-fbc-fi-ip:MarketPrice
    }

    item def <'mid price'> MidPrice specializes Analytics::ArithmeticMean, SecurityPrice {
      doc /* arithmetic mean between bid and offer prices */
      // skos:definition xml:lang="en" (InstrumentPricing.rdf)
      // rdfs:label xml:lang="en" (InstrumentPricing.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-utl-alx;ArithmeticMean"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-ip;SecurityPrice"/>
      // RDF: fibo-fbc-fi-ip:MidPrice
      // subClassOf: ArithmeticMean - from FIBO_FND_Utilities (UtilitiesPackage)
    }

    item def <'offer price'> OfferPrice specializes SecurityPrice {
      doc /* price suggested by a prospective seller at a particular time for a given security */
      // skos:definition xml:lang="en" (InstrumentPricing.rdf)
      // rdfs:label xml:lang="en" (InstrumentPricing.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-ip;SecurityPrice"/>
      // RDF: fibo-fbc-fi-ip:OfferPrice
    }

    item def <'official closing price'> OfficialClosingPrice specializes ClosingPrice {
      doc /* price of the final trade of a security at the end of a trading day on a given exchange */
      // skos:definition xml:lang="en" (InstrumentPricing.rdf)
      // rdfs:label xml:lang="en" (InstrumentPricing.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-ip;ClosingPrice"/>
      // RDF: fibo-fbc-fi-ip:OfficialClosingPrice
    }

    item def <'opening price'> OpeningPrice specializes MarketPrice {
      doc /* price at which something first trades at the start of a trading day */
      // skos:definition xml:lang="en" (InstrumentPricing.rdf)
      // rdfs:label xml:lang="en" (InstrumentPricing.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-ip;MarketPrice"/>
      // RDF: fibo-fbc-fi-ip:OpeningPrice
    }

    item def <'price analytic'> PriceAnalytic specializes Analytics::ScopedMeasure {
      doc /* statistical measure involving security prices */
      // skos:definition xml:lang="en" (InstrumentPricing.rdf)
      // rdfs:label xml:lang="en" (InstrumentPricing.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-utl-alx;ScopedMeasure"/>
      // RDF: fibo-fbc-fi-ip:PriceAnalytic
      // subClassOf: ScopedMeasure - from FIBO_FND_Utilities (UtilitiesPackage)
    }

    item def <'price determination method'> PriceDeterminationMethod specializes Objectives::Strategy {
      doc /* strategy for calculating or otherwise establishing a price for something */
      // skos:definition xml:lang="en" (InstrumentPricing.rdf)
      // rdfs:label xml:lang="en" (InstrumentPricing.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-gao-obj;Strategy"/>
      // RDF: fibo-fbc-fi-ip:PriceDeterminationMethod
      // subClassOf: Strategy - external CMNS
    }

    item def <'price spread'> PriceSpread specializes Analytics::Difference {
      doc /* difference between two prices */
      // skos:definition xml:lang="en" (InstrumentPricing.rdf)
      // rdfs:label xml:lang="en" (InstrumentPricing.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-utl-alx;Difference"/>
      // RDF: fibo-fbc-fi-ip:PriceSpread
      // subClassOf: Difference - external CMNS
    }

    item def <'pricing model'> PricingModel specializes Analytics::Expression {
      doc /* expression used to determine a value for an instrument at a given point in time */
      // skos:definition xml:lang="en" (InstrumentPricing.rdf)
      // rdfs:label xml:lang="en" (InstrumentPricing.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-qtu;Expression"/>
      // RDF: fibo-fbc-fi-ip:PricingModel
      // subClassOf: Expression - external CMNS
    }

    item def <'rate of return'> RateOfReturn specializes FiBO_FND_ExternalStubs::Percentage {
      doc /* net gain or loss on an investment over a specified time period, expressed as a percentage of the investment's initial cost or value as of a specific point in time */
      // skos:definition xml:lang="en" (InstrumentPricing.rdf)
      // rdfs:label xml:lang="en" (InstrumentPricing.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-qtu;Percentage"/>
      // RDF: fibo-fbc-fi-ip:RateOfReturn
      // subClassOf: Percentage - external CMNS/FND
    }

    item def <'security price'> SecurityPrice specializes Currency_Amount::MonetaryPrice {
      doc /* monetary price for a financial instrument at some point in time */
      // skos:definition xml:lang="en" (InstrumentPricing.rdf)
      // rdfs:label xml:lang="en" (InstrumentPricing.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-cur;MonetaryPrice"/>
      // RDF: fibo-fbc-fi-ip:SecurityPrice
      // subClassOf: MonetaryPrice - from FIBO_FND_Accounting (CurrencyAmount)
    }

    item def <'trading day'> TradingDay specializes FiBO_FND_ExternalStubs::ExplicitDatePeriod {
      doc /* time span that a particular trading venue is open */
      // skos:definition xml:lang="en" (InstrumentPricing.rdf)
      // rdfs:label xml:lang="en" (InstrumentPricing.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-dt;ExplicitDatePeriod"/>
      // RDF: fibo-fbc-fi-ip:TradingDay
      // subClassOf: ExplicitDatePeriod - from FIBO_FND_DatesAndTimes (FinancialDates)
    }

    item def <'trading session'> TradingSession specializes FiBO_FND_ExternalStubs::ExplicitDatePeriod {
      doc /* window of time within a trading day in which orders may be placed and filled */
      // skos:definition xml:lang="en" (InstrumentPricing.rdf)
      // rdfs:label xml:lang="en" (InstrumentPricing.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-dt;ExplicitDatePeriod"/>
      // RDF: fibo-fbc-fi-ip:TradingSession
      // subClassOf: ExplicitDatePeriod - from FIBO_FND_DatesAndTimes (FinancialDates)
    }

    item def <'volume-weighted average price'> VolumeWeightedAveragePrice specializes PriceAnalytic {
      doc /* average price at which a given security has traded throughout a trading day, determined by multiplying each trade by its volume, adding the results, then dividing by the volume traded for the day */
      // skos:definition xml:lang="en" (InstrumentPricing.rdf)
      // rdfs:label xml:lang="en" (InstrumentPricing.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-ip;PriceAnalytic"/>
      // RDF: fibo-fbc-fi-ip:VolumeWeightedAveragePrice
    }

    item def <'volume-weighted open price'> VolumeWeightedOpenPrice specializes PriceAnalytic {
      doc /* price determined by multiplying each trade by its volume, adding the results, then dividing by the volume over a certain period during the trading day (rather than over the course of the entire day) */
      // skos:definition xml:lang="en" (InstrumentPricing.rdf)
      // rdfs:label xml:lang="en" (InstrumentPricing.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-ip;PriceAnalytic"/>
      // RDF: fibo-fbc-fi-ip:VolumeWeightedOpenPrice
    }

    item def <'yield'> Yield specializes FiBO_FND_ExternalStubs::Percentage {
      doc /* return on the investor's capital investment */
      // skos:definition xml:lang="en" (InstrumentPricing.rdf)
      // rdfs:label xml:lang="en" (InstrumentPricing.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-qtu;Percentage"/>
      // RDF: fibo-fbc-fi-ip:Yield
      // subClassOf: Percentage - external CMNS/FND
    }

    item def <'yield spread'> YieldSpread specializes Analytics::Difference {
      doc /* the spread between the yields of two items */
      // skos:definition xml:lang="en" (InstrumentPricing.rdf)
      // rdfs:label xml:lang="en" (InstrumentPricing.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-utl-alx;Difference"/>
      // RDF: fibo-fbc-fi-ip:YieldSpread
      // subClassOf: Difference - external CMNS
    }

}

// --- fibo-fbc-fi-stl (Settlement) ---
