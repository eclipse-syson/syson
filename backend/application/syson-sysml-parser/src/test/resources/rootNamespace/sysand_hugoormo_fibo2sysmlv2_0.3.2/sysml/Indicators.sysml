// ===================================================================
// Indicators.sysml
// Source-parity extracted from: FiBO_IND_Indicators.sysml
// Primary source module: Indicators.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Indices and Indicators IND/Indicators
// ===================================================================
package Indicators {
  doc/* This ontology provides the concepts common to all market rates, indices and indicators; that is concepts descriptive of the numeric parameters themselves. These are modeled independently of the values they may take over time. (source: dct:abstract, Indicators.rdf) */
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

  item def <'daily average market rate'> DailyAverageMarketRate specializes MarketRate {
  	doc /* overall level of a given rate, calculated as the sum of some selected observed values of the rates for a particular reference rate, foreign exchange rate, lending rate, or other market rate divided by the number of samples collected over the course of a twenty-four (24) hour period for a specific date */
  	// skos:definition xml:lang="en" (Indicators.rdf)
  	// rdfs:label xml:lang="en" (Indicators.rdf)
  	// RDF: fibo-ind-ind-ind:DailyAverageMarketRate
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ind-ind;MarketRate"/>
  }
  item def <'end-of-day market rate'> EndOfDayMarketRate specializes MarketRate {
  	doc /* value of a given market rate of the end of the business day for a specific date */
  	// skos:definition xml:lang="en" (Indicators.rdf)
  	// rdfs:label xml:lang="en" (Indicators.rdf)
  	// RDF: fibo-ind-ind-ind:EndOfDayMarketRate
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ind-ind;MarketRate"/>
  }
  item def <'historical price volatility'> HistoricalPriceVolatility specializes HistoricalVolatility, PriceVolatility {
  	doc /* historical volatility measure of past trading ranges of prices of underlying securities and indexes */
  	// skos:definition xml:lang="en" (Indicators.rdf)
  	// rdfs:label xml:lang="en" (Indicators.rdf)
  	// RDF: fibo-ind-ind-ind:HistoricalPriceVolatility
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ind-ind;HistoricalVolatility"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ind-ind;PriceVolatility"/>
  }
  item def <'historical volatility'> HistoricalVolatility specializes Volatility {
  	doc /* measure of volatility that uses actual values for pricing, rates, and other measurements calculated over some prior period */
  	// skos:definition xml:lang="en" (Indicators.rdf)
  	// rdfs:label xml:lang="en" (Indicators.rdf)
  	// RDF: fibo-ind-ind-ind:HistoricalVolatility
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ind-ind;Volatility"/>
  }
  item def <'implied price volatility'> ImpliedPriceVolatility specializes ImpliedVolatility, PriceVolatility {
  	doc /* measure of volatility that represents the expected fluctuations of an underlying stock or index over a specific time frame */
  	// skos:definition xml:lang="en" (Indicators.rdf)
  	// rdfs:label xml:lang="en" (Indicators.rdf)
  	// RDF: fibo-ind-ind-ind:ImpliedPriceVolatility
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ind-ind;ImpliedVolatility"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ind-ind;PriceVolatility"/>
  }
  item def <'implied volatility'> ImpliedVolatility specializes Volatility {
  	doc /* measure of volatility that is a forward-looking metric used to calculate probability */
  	// skos:definition xml:lang="en" (Indicators.rdf)
  	// rdfs:label xml:lang="en" (Indicators.rdf)
  	// RDF: fibo-ind-ind-ind:ImpliedVolatility
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ind-ind;Volatility"/>
  }
  item def <'market rate'> MarketRate specializes Currency_Amount::Ratio {
  	doc /* derivative instrument where the holder has the right but may not have the obligation, depending on the nature of the instrument, to enter into the underlying contract, or pay or receive payment related to the underlying financial rate (or rate contract) on a specified future date based on a specified future rate and term */
  	// skos:definition xml:lang="en" (Indicators.rdf)
  	// rdfs:label xml:lang="en" (Indicators.rdf)
  	// RDF: fibo-ind-ind-ind:MarketRate
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-qtu;Ratio"/>
  }
  item def <'market spread'> MarketSpread specializes Analytics::ScopedMeasure {
  	doc /* statistical measure providing the difference (or spread) between two market rates */
  	// skos:definition xml:lang="en" (Indicators.rdf)
  	// rdfs:label xml:lang="en" (Indicators.rdf)
  	// RDF: fibo-ind-ind-ind:MarketSpread
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-utl-alx;ScopedMeasure"/>
  }
  item def <'price structure'> PriceStructure specializes Financial_Dates::DatedStructuredCollection {
  	doc /* structured collection of prices, such as market prices for some index or security, such that volatility or other analyses may be performed over the structure */
  	// skos:definition xml:lang="en" (Indicators.rdf)
  	// rdfs:label xml:lang="en" (Indicators.rdf)
  	// RDF: fibo-ind-ind-ind:PriceStructure
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-fd;DatedStructuredCollection"/>
  }
  item def <'price volatility'> PriceVolatility specializes Volatility {
  	doc /* statistical measure of the rate of change in pricing for a given security or market index */
  	// skos:definition xml:lang="en" (Indicators.rdf)
  	// rdfs:label xml:lang="en" (Indicators.rdf)
  	// RDF: fibo-ind-ind-ind:PriceVolatility
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-ind-ind-ind;Volatility"/>
  }
  item def <'quoted price'> QuotedPrice specializes Currency_Amount::MonetaryPrice {
  	doc /* a monetary price quoted by some publisher on a given date */
  	// skos:definition xml:lang="en" (Indicators.rdf)
  	// rdfs:label xml:lang="en" (Indicators.rdf)
  	// RDF: fibo-ind-ind-ind:QuotedPrice
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-cur;MonetaryPrice"/>
  }
  item def <'term structure'> TermStructure specializes Financial_Dates::DatedStructuredCollection {
  	doc /* structured collection of rates, such as interest rates, or bond yields with different terms to maturity, such that a yield curve may be constructed for the structure */
  	// skos:definition xml:lang="en" (Indicators.rdf)
  	// rdfs:label xml:lang="en" (Indicators.rdf)
  	// RDF: fibo-ind-ind-ind:TermStructure
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-fd;DatedStructuredCollection"/>
  }
  item def <'volatility'> Volatility specializes Analytics::Dispersion {
  	doc /* statistical measure of the dispersion around the average of some random variable over some period of time */
  	// skos:definition xml:lang="en" (Indicators.rdf)
  	// rdfs:label xml:lang="en" (Indicators.rdf)
  	// RDF: fibo-ind-ind-ind:Volatility
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-utl-alx;Dispersion"/>
  }
}
