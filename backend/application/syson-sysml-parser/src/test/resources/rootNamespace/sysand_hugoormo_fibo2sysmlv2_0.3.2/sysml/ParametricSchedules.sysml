// ===================================================================
// ParametricSchedules.sysml
// Source-parity extracted from: FiBO_SEC_Securities.sysml
// Primary source module: ParametricSchedules.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Securities SEC/Securities
// ===================================================================
package Parametric_Schedules {
  doc/* This ontology defines concepts related to parametric schedules, including how to represent individual schedules as well as related date periods, explicit dates, and other concepts needed for parametric schedule representation. (source: dct:abstract, ParametricSchedules.rdf) */
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

  item def <'auction date rule'> AuctionDateRule specializes Business_Dates::BusinessRecurrenceIntervalConvention {
    	doc /* business recurrence interval convention that is a published rule for defining the date of some auction event */
    	// skos:definition xml:lang="en" (ParametricSchedules.rdf)
    	// rdfs:label xml:lang="en" (ParametricSchedules.rdf)
    	// RDF: fibo-sec-sec-sch:AuctionDateRule
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-bd;BusinessRecurrenceIntervalConvention"/>
    }

  item def <'date returned by settlement date rule'> DateReturnedBySettlementDateRule specializes Financial_Dates::CalculatedDate {
    	doc /* calculated date that is determined via a settlement rule */
    	// skos:definition xml:lang="en" (ParametricSchedules.rdf)
    	// rdfs:label xml:lang="en" (ParametricSchedules.rdf)
    	// RDF: fibo-sec-sec-sch:DateReturnedBySettlementDateRule
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-fd;CalculatedDate"/>
    }

  item def <'date returned by trading date rule'> DateReturnedByTradingDateRule specializes Financial_Dates::CalculatedDate {
    	doc /* calculated date that is determined via a trading rule */
    	// skos:definition xml:lang="en" (ParametricSchedules.rdf)
    	// rdfs:label xml:lang="en" (ParametricSchedules.rdf)
    	// RDF: fibo-sec-sec-sch:DateReturnedByTradingDateRule
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-fd;CalculatedDate"/>
    }

  item def <'floating-rate note date'> FloatingRateNoteDate specializes Financial_Dates::CalculatedDate {
    	doc /* calculated date associated with a floating-rate note, also known as a floater or FRN, which is a debt instrument with a variable interest rate */
    	// skos:definition xml:lang="en" (ParametricSchedules.rdf)
    	// rdfs:label xml:lang="en" (ParametricSchedules.rdf)
    	// RDF: fibo-sec-sec-sch:FloatingRateNoteDate
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-fd;CalculatedDate"/>
    }

  item def <'floating-rate note date rule'> FloatingRateNoteDateRule specializes Business_Dates::BusinessRecurrenceIntervalConvention {
    	doc /* business day adjustment rule applied to floating-rate note instruments */
    	// skos:definition xml:lang="en" (ParametricSchedules.rdf)
    	// rdfs:label xml:lang="en" (ParametricSchedules.rdf)
    	// RDF: fibo-sec-sec-sch:FloatingRateNoteDateRule
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-bd;BusinessRecurrenceIntervalConvention"/>
    }

  item def <'International Money Market (IMM) Australian Dollar (AUD) trading date rule'> InternationalMoneyMarketAustralianDollarTradingDateRule specializes TradingDateRule {
    	doc /* trading date rule defined as the last trading day of an Australian Stock Exchange (ASX) 90-Day Bank Accepted Futures and Options product, one Sydney business day preceding the second Friday of the relevant settlement month */
    	// skos:definition xml:lang="en" (ParametricSchedules.rdf)
    	// rdfs:label xml:lang="en" (ParametricSchedules.rdf)
    	// RDF: fibo-sec-sec-sch:InternationalMoneyMarketAustralianDollarTradingDateRule
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-sch;TradingDateRule"/>
    }

  item def <'International Money Market (IMM) Canadian Dollar (CAD) trading date rule'> InternationalMoneyMarketCanadianDollarTradingDateRule specializes TradingDateRule {
    	doc /* trading date rule defined as the last trading day / expiration day of the Canadian Derivatives Exchange (Bourse do Montreal Inc.), three month Bankers' Acceptance Futures (Ticker symbol BAX), the second London banking day prior to the third Wednesday of the contract month */
    	// skos:definition xml:lang="en" (ParametricSchedules.rdf)
    	// rdfs:label xml:lang="en" (ParametricSchedules.rdf)
    	// RDF: fibo-sec-sec-sch:InternationalMoneyMarketCanadianDollarTradingDateRule
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-sch;TradingDateRule"/>
    }

  item def <'International Money Market (IMM) New Zealand Dollar (NZD) trading date rule'> InternationalMoneyMarketNewZealandDollarTradingDateRule specializes TradingDateRule {
    	doc /* trading date rule defined as the last trading day of an Australian Stock Exchange (ASX) New Zealand (NZ) 90-Day Bank Accepted Futures and Options product, the first Wednesday after the ninth day of the relevant settlement month */
    	// skos:definition xml:lang="en" (ParametricSchedules.rdf)
    	// rdfs:label xml:lang="en" (ParametricSchedules.rdf)
    	// RDF: fibo-sec-sec-sch:InternationalMoneyMarketNewZealandDollarTradingDateRule
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-sch;TradingDateRule"/>
    }

  item def <'International Money Market (IMM) settlement date rule'> InternationalMoneyMarketSettlementDateRule specializes SettlementDateRule {
    	doc /* settlement date rule as defined in the International Money Market (IMM) settlement dates calendar */
    	// skos:definition xml:lang="en" (ParametricSchedules.rdf)
    	// rdfs:label xml:lang="en" (ParametricSchedules.rdf)
    	// RDF: fibo-sec-sec-sch:InternationalMoneyMarketSettlementDateRule
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-sch;SettlementDateRule"/>
    }

  item def <'scheduled calculation period end event'> ScheduledCalculationPeriodEndEvent specializes FiBOOccurrences::OccurrenceKind {
    	doc /* the end date of a specific calculation period */
    	// skos:definition xml:lang="en" (ParametricSchedules.rdf)
    	// rdfs:label xml:lang="en" (ParametricSchedules.rdf)
    	// RDF: fibo-sec-sec-sch:ScheduledCalculationPeriodEndEvent
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-oc;OccurrenceKind"/>
    }

  item def <'scheduled calculation period start event'> ScheduledCalculationPeriodStartEvent specializes FiBOOccurrences::OccurrenceKind {
    	doc /* the start of a specific calculation period */
    	// skos:definition xml:lang="en" (ParametricSchedules.rdf)
    	// rdfs:label xml:lang="en" (ParametricSchedules.rdf)
    	// RDF: fibo-sec-sec-sch:ScheduledCalculationPeriodStartEvent
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-oc;OccurrenceKind"/>
    }

  item def <'settlement date rule'> SettlementDateRule specializes Business_Dates::BusinessRecurrenceIntervalConvention {
    	doc /* convention for determining settlement dates by reference to some jurisdiction or as set by a given exchange or similar venue */
    	// skos:definition xml:lang="en" (ParametricSchedules.rdf)
    	// rdfs:label xml:lang="en" (ParametricSchedules.rdf)
    	// RDF: fibo-sec-sec-sch:SettlementDateRule
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-bd;BusinessRecurrenceIntervalConvention"/>
    }

  item def <'trading date rule'> TradingDateRule specializes Business_Dates::BusinessRecurrenceIntervalConvention {
    	doc /* convention for determining trading dates defined with reference to some trading date calendar published by some trading facility or exchange */
    	// skos:definition xml:lang="en" (ParametricSchedules.rdf)
    	// rdfs:label xml:lang="en" (ParametricSchedules.rdf)
    	// RDF: fibo-sec-sec-sch:TradingDateRule
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-bd;BusinessRecurrenceIntervalConvention"/>
    }

  item def <'US Treasury bill auction date rule'> USTreasuryBillAuctionDateRule specializes AuctionDateRule {
    	doc /* rule for setting auction dates for US Treasury bills */
    	// skos:definition xml:lang="en" (ParametricSchedules.rdf)
    	// rdfs:label xml:lang="en" (ParametricSchedules.rdf)
    	// RDF: fibo-sec-sec-sch:USTreasuryBillAuctionDateRule
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-sch;AuctionDateRule"/>
    }

  item def <'US Treasury bill date'> USTreasuryBillDate specializes Financial_Dates::CalculatedDate {
    	doc /* auction date for US 13 week and 26 week Treasury bills */
    	// skos:definition xml:lang="en" (ParametricSchedules.rdf)
    	// rdfs:label xml:lang="en" (ParametricSchedules.rdf)
    	// RDF: fibo-sec-sec-sch:USTreasuryBillDate
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-fd;CalculatedDate"/>
    }

}

// --- fibo-sec-sec-us ---
