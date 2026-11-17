// ===================================================================
// FuturesTemporal.sysml
// Source-parity extracted from: FiBO_MD_DerivativesTemporal.sysml
// Primary source module: FuturesTemporal.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Market Data MD/DerivativesTemporal
// ===================================================================
package Futures_Temporal {
  doc/* Exchange traded futures date and time dependent terms such as prices and margining. Also covers greeks (thetas etc.) (source: dct:abstract, FuturesTemporal.rdf) */
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

  item def <'exchange futures price'> ExchangeFuturesPrice specializes Instrument_Pricing::SecurityPrice {
  	doc /* Review whether this exists */
  	// skos:definition xml:lang="en" (FuturesTemporal.rdf)
  	// rdfs:label xml:lang="en" (FuturesTemporal.rdf)
  	// RDF: fibo-md-drvt-fut:ExchangeFuturesPrice
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-ip;SecurityPrice"/>
  }
  item def <'futures closing price'> FuturesClosingPrice specializes ExchangeFuturesPrice {
  	doc /* Review whether this exists */
  	// skos:definition xml:lang="en" (FuturesTemporal.rdf)
  	// rdfs:label xml:lang="en" (FuturesTemporal.rdf)
  	// RDF: fibo-md-drvt-fut:FuturesClosingPrice
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-der-fut;ExchangeFuturesPrice"/>
  }
  item def <'futures daily settlement price'> FuturesDailySettlementPrice specializes ExchangeFuturesPrice, Instrument_Pricing::SecurityPrice {
  	doc /* Review whether this exists */
  	// skos:definition xml:lang="en" (FuturesTemporal.rdf)
  	// rdfs:label xml:lang="en" (FuturesTemporal.rdf)
  	// RDF: fibo-md-drvt-fut:FuturesDailySettlementPrice
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-ip;SecurityPrice"/>
  }
  item def <'futures greek'> FuturesGreek specializes Instrument_Pricing::PriceAnalytic {
  	doc /* quantity representing the sensitivity of the price of a future or futures to a change in underlying parameters on which the value depends */
  	// skos:definition xml:lang="en" (FuturesTemporal.rdf)
  	// rdfs:label xml:lang="en" (FuturesTemporal.rdf)
  	// RDF: fibo-md-drvt-fut:FuturesGreek
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-ip;PriceAnalytic"/>
  }
  item def <'futures theta'> FuturesTheta specializes FuturesGreek {
  	doc /* A party who has an account for futures trading at a derivatives exchange. Further notes: The trader opens an account with a given amount known as a margin. */
  	// skos:definition xml:lang="en" (FuturesTemporal.rdf)
  	// rdfs:label xml:lang="en" (FuturesTemporal.rdf)
  	// RDF: fibo-md-drvt-fut:FuturesTheta
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-der-fut;FuturesGreek"/>
  }
  item def <'futures trading account holder'> FuturesTradingAccountHolder { // RDF: fibo-md-drvt-fut:FuturesTradingAccountHolder
    // RDF: fibo-md-drvt-fut:FuturesTradingAccountHolder
  }
  item def <'futures vega'> FuturesVega specializes FuturesGreek {
  	doc /* money or securities put up as a good faith deposit assuring that a future contract will be fulfilled */
  	// skos:definition xml:lang="en" (FuturesTemporal.rdf)
  	// rdfs:label xml:lang="en" (FuturesTemporal.rdf)
  	// RDF: fibo-md-drvt-fut:FuturesVega
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-der-fut;FuturesGreek"/>
  }
  item def <'initial margin'> InitialMargin specializes Ownership::Asset {
  	doc /* money or securities put up as a good faith deposit assuring that a future contract will be fulfilled */
  	// skos:definition xml:lang="en" (FuturesTemporal.rdf)
  	// rdfs:label xml:lang="en" (FuturesTemporal.rdf)
  	// RDF: fibo-md-drvt-fut:InitialMargin
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-oac-own;Asset"/>
  }
  item def <'minimum margin'> MinimumMargin specializes Ownership::Asset {
  	doc /* The lowest amount an account can reach before needing to be replenished. */
  	// skos:definition xml:lang="en" (FuturesTemporal.rdf)
  	// rdfs:label xml:lang="en" (FuturesTemporal.rdf)
  	// RDF: fibo-md-drvt-fut:MinimumMargin
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-oac-own;Asset"/>
  }
}

// --- fibo-md-drvt-opt ---
