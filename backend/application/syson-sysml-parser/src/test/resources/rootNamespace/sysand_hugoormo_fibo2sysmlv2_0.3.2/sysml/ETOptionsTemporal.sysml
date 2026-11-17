// ===================================================================
// OptionsTemporal.sysml
// Source-parity extracted from: FiBO_MD_DerivativesTemporal.sysml
// Primary source module: ETOptionsTemporal.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Market Data MD/DerivativesTemporal
// ===================================================================
package ETOptionsTemporal {
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

  item def <'delta'> Delta specializes OptionsGreek {
  	doc /* First derivative of option value with respect to theoretical price is a delta (or on a position). Theoretical price */
  	// skos:definition xml:lang="en" (ETOptionsTemporal.rdf)
  	// rdfs:label xml:lang="en" (ETOptionsTemporal.rdf)
  	// RDF: fibo-md-drvt-opt:Delta
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-derx-eto;OptionsGreek"/>
  }
  item def <'option daily settlement price'> OptionDailySettlementPrice specializes Instrument_Pricing::ClosingPrice, Instrument_Pricing::SecurityPrice {
  	doc /* The official price at the end of a trading session. This price is established by The Options Clearing Corporation and is used to determine changes in account equity, margin requirements, and for other purposes. */
  	// skos:definition xml:lang="en" (ETOptionsTemporal.rdf)
  	// rdfs:label xml:lang="en" (ETOptionsTemporal.rdf)
  	// RDF: fibo-md-drvt-opt:OptionDailySettlementPrice
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-ip;ClosingPrice"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-ip;SecurityPrice"/>
  }
  item def <'options greek'> OptionsGreek specializes Instrument_Pricing::PriceAnalytic {
  	doc /* quantity representing the sensitivity of the price of an option or options to a change in underlying parameters on which the value depends */
  	// skos:definition xml:lang="en" (ETOptionsTemporal.rdf)
  	// rdfs:label xml:lang="en" (ETOptionsTemporal.rdf)
  	// RDF: fibo-md-drvt-opt:OptionsGreek
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-ip;PriceAnalytic"/>
  }
  item def <'options theta'> OptionsTheta specializes OptionsGreek {
  	doc /* A measure of the rate of change in an option&apos;s theoretical value for a one-unit change in time to the option&apos;s expiration date. Action: add terms that define or influence this. */
  	// skos:definition xml:lang="en" (ETOptionsTemporal.rdf)
  	// rdfs:label xml:lang="en" (ETOptionsTemporal.rdf)
  	// RDF: fibo-md-drvt-opt:OptionsTheta
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-derx-eto;OptionsGreek"/>
  }
  item def <'options vega'> OptionsVega specializes OptionsGreek {
  	doc /* A measure of the rate of change in an option&apos;s theoretical value for a one-unit change in the volatility assumption. Action: add terms that define or influence this. */
  	// skos:definition xml:lang="en" (ETOptionsTemporal.rdf)
  	// rdfs:label xml:lang="en" (ETOptionsTemporal.rdf)
  	// RDF: fibo-md-drvt-opt:OptionsVega
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-derx-eto;OptionsGreek"/>
  }
  item def <'option theoretical value'> OptionTheoreticalValue specializes Currency_Amount::MonetaryAmount {
  	doc /* fair value of the option as determined by an option pricing model */
  	// skos:definition xml:lang="en" (ETOptionsTemporal.rdf)
  	// rdfs:label xml:lang="en" (ETOptionsTemporal.rdf)
  	// RDF: fibo-md-drvt-opt:OptionTheoreticalValue
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-cur;MonetaryAmount"/>
  }
}
