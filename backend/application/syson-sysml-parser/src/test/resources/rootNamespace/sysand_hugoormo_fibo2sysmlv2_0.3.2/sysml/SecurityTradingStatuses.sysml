// ===================================================================
// SecurityTradingStatuses.sysml
// Source-parity extracted from: FiBO_MD_TemporalCore.sysml
// Primary source module: SecurityTradingStatuses.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Market Data MD/TemporalCore
// ===================================================================
package SecurityTradingStatuses {
  doc/* This ontology defines the various kinds of trading status that a security may be in at any given point in time. These includes such terms as active and halted, inactive and so on, along with their qualifying terms. (source: dct:abstract, SecurityTradingStatuses.rdf) */
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

  item def <'active'> Active specializes ExchangeSecurityTradingStatus, SecurityTradingStatus {
  	doc /* Security is actively traded */
  	// skos:definition xml:lang="en" (SecurityTradingStatuses.rdf)
  	// rdfs:label xml:lang="en" (SecurityTradingStatuses.rdf)
  	// RDF: fibo-md-temx-trs:Active
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-temx-trs;SecurityTradingStatus"/>
  }
  item def <'actively trading'> ActivelyTrading specializes SecurityTradingStatus, ExchangeSecurityTradingStatus {
  	doc /* Security is actively traded on the exchange or trading facility */
  	// skos:definition xml:lang="en" (SecurityTradingStatuses.rdf)
  	// rdfs:label xml:lang="en" (SecurityTradingStatuses.rdf)
  	// RDF: fibo-md-temx-trs:ActivelyTrading
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-temx-trs;ExchangeSecurityTradingStatus"/>
  }
  item def <'exchange security trading status'> ExchangeSecurityTradingStatus specializes SecurityTradingStatus, Lifecycles::LifecycleStatus {
  	doc /* trading status of a listed security on a given trading exchange */
  	// skos:definition xml:lang="en" (SecurityTradingStatuses.rdf)
  	// rdfs:label xml:lang="en" (SecurityTradingStatuses.rdf)
  	// RDF: fibo-md-temx-trs:ExchangeSecurityTradingStatus
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-lif;LifecycleStatus"/>
  }
  item def <'inactive'> Inactive specializes ExchangeSecurityTradingStatus, SecurityTradingStatus {
  	doc /* The security has been issued into the secondary market. */
  	// skos:definition xml:lang="en" (SecurityTradingStatuses.rdf)
  	// rdfs:label xml:lang="en" (SecurityTradingStatuses.rdf)
  	// RDF: fibo-md-temx-trs:Inactive
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-temx-trs;SecurityTradingStatus"/>
  }
  item def <'issued'> Issued specializes SecurityLifecycleStatus, PrivateLabelMBSIssuance::PoolLifecycleStateSelection {
  	doc /* a program of offerings of medium term notes; a set of issues where the maturity is defined after the rest of the terms have been registered with some authority; these are registered up front so that then the company wants to borrow more money they don&apos;t have to go through the registration period but have the facility up front to issue another security. */
  	// skos:definition xml:lang="en" (SecurityTradingStatuses.rdf)
  	// rdfs:label xml:lang="en" (SecurityTradingStatuses.rdf)
  	// RDF: fibo-md-temx-trs:Issued
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-bp-iss-pmbs;PoolLifecycleStateSelection"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-temx-trs;SecurityLifecycleStatus"/>
  }
  item def <'security lifecycle status'> SecurityLifecycleStatus specializes Lifecycles::LifecycleStatus {
  	doc /* status of a security within its lifecycle */
  	// skos:definition xml:lang="en" (SecurityTradingStatuses.rdf)
  	// rdfs:label xml:lang="en" (SecurityTradingStatuses.rdf)
  	// RDF: fibo-md-temx-trs:SecurityLifecycleStatus
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-lif;LifecycleStatus"/>
  }
  item def <'security trading status'> SecurityTradingStatus specializes Lifecycles::LifecycleStatus {
  	doc /* status of the security in terms of whether it is trading or not, and any special considerations relating to trading */
  	// skos:definition xml:lang="en" (SecurityTradingStatuses.rdf)
  	// rdfs:label xml:lang="en" (SecurityTradingStatuses.rdf)
  	// RDF: fibo-md-temx-trs:SecurityTradingStatus
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-lif;LifecycleStatus"/>
  }
  item def <'trading halted'> TradingHalted specializes SecurityTradingStatus, ExchangeSecurityTradingStatus {
  	doc /* Used to refer to a security that trades after the date of issue but before the time at which the certificates are delivered */
  	// skos:definition xml:lang="en" (SecurityTradingStatuses.rdf)
  	// rdfs:label xml:lang="en" (SecurityTradingStatuses.rdf)
  	// RDF: fibo-md-temx-trs:TradingHalted
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-temx-trs;ExchangeSecurityTradingStatus"/>
  }
  item def <'when distributed'> WhenDistributed specializes SecurityLifecycleStatus {
  	doc /* Used to refer to a security that trades after the date of issue but before the time at which the certificates are delivered */
  	// skos:definition xml:lang="en" (SecurityTradingStatuses.rdf)
  	// rdfs:label xml:lang="en" (SecurityTradingStatuses.rdf)
  	// RDF: fibo-md-temx-trs:WhenDistributed
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-temx-trs;SecurityLifecycleStatus"/>
  }
  item def <'when issued'> WhenIssued specializes SecurityLifecycleStatus {
  	doc /* When Issued or Gray Market trading is when a security is traded ahead of the date at which it is to be issued. */
  	// skos:definition xml:lang="en" (SecurityTradingStatuses.rdf)
  	// rdfs:label xml:lang="en" (SecurityTradingStatuses.rdf)
  	// RDF: fibo-md-temx-trs:WhenIssued
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-temx-trs;SecurityLifecycleStatus"/>
  }
  item def <'worthless'> Worthless specializes SecurityLifecycleStatus {
  	doc /* Announcement by the regulator that the security has become worthless. */
  	// skos:definition xml:lang="en" (SecurityTradingStatuses.rdf)
  	// rdfs:label xml:lang="en" (SecurityTradingStatuses.rdf)
  	// RDF: fibo-md-temx-trs:Worthless
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-md-temx-trs;SecurityLifecycleStatus"/>
  }
}
