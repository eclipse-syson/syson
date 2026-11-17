// ===================================================================
// FinancialDates.sysml
// Source-parity extracted from: FiBO_FND_DatesAndTimes.sysml
// Primary source module: FinancialDates.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Foundations FND/DatesAndTimes
// ===================================================================

package Financial_Dates {
  doc/* This ontology provides definitions of date and schedule concepts for use in other FIBO ontologies. (source: dct:abstract, FinancialDates.rdf) */
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

  item def <'calculated date'> CalculatedDate specializes FiBO_FND_ExternalStubs::Date {
  	doc /* date that is or will be determined based on some formula */
  	// skos:definition xml:lang="en" (FinancialDates.rdf)
  	// rdfs:label xml:lang="en" (FinancialDates.rdf)
  	// RDF: fibo-fnd-dt-fd:CalculatedDate
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-dt;Date"/>
  }
  item def RegularSchedule specializes Schedule, FiBO_FND_ExternalStubs::StructuredCollection{ // RDF: fibo-fnd-dt-fd:RegularSchedule
		doc /* schedule whose time intervals recur regularly */
		// skos:definition xml:lang="en" (FinancialDates.rdf)
		// rdfs:label xml:lang="en" (FinancialDates.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-col;StructuredCollection"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-fd;Schedule"/>
      // RDF: fibo-fnd-dt-fd:RegularSchedule
  }
  item def <'ad hoc schedule'> AdHocSchedule specializes Schedule {
  	doc /* the day and time at which something closes */
  	// skos:definition xml:lang="en" (FinancialDates.rdf)
  	// rdfs:label xml:lang="en" (FinancialDates.rdf)
  	// RDF: fibo-fnd-dt-fd:AdHocSchedule
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-fd;Schedule"/>
  }
  item def <'ad hoc schedule entry'> AdHocScheduleEntry specializes DatedCollectionConstituent {
  	doc /* entry, including a date or date and time, among multiple non-regularly-recurring entries in a schedule */
  	// skos:definition xml:lang="en" (FinancialDates.rdf)
  	// rdfs:label xml:lang="en" (FinancialDates.rdf)
  	// RDF: fibo-fnd-dt-fd:AdHocScheduleEntry
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-fd;DatedCollectionConstituent"/>
  }
  item def <'age'> Age specializes FiBO_FND_ExternalStubs::Duration {
  	doc /* length of time that something or someone has been alive or existed */
  	// skos:definition xml:lang="en" (FinancialDates.rdf)
  	// rdfs:label xml:lang="en" (FinancialDates.rdf)
  	// RDF: fibo-fnd-dt-fd:Age
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-dt;Duration"/>
  }
  item def <'anchor date'> AnchorDate specializes FiBO_FND_ExternalStubs::Date {
  	doc /* fixed reference point within a series or timeline */
  	// skos:definition xml:lang="en" (FinancialDates.rdf)
  	// rdfs:label xml:lang="en" (FinancialDates.rdf)
  	// RDF: fibo-fnd-dt-fd:AnchorDate
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-dt;ExplicitDate"/>
  }
  item def <'business calendar'> BusinessCalendar specializes Schedule {
  	doc /* schedule and communications tool used to manage meetings, plan events, and coordinate activities within and/or across organizations */
  	// skos:definition xml:lang="en" (FinancialDates.rdf)
  	// rdfs:label xml:lang="en" (FinancialDates.rdf)
  	// RDF: fibo-fnd-dt-fd:BusinessCalendar
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-fd;Schedule"/>
  }
  item def <'calculation period'> CalculationPeriod specializes FiBO_FND_ExternalStubs::ExplicitDatePeriod {
  	doc /* explicit period from the start to the end of a specific interval or range within which a computational process or operation occurs */
  	// skos:definition xml:lang="en" (FinancialDates.rdf)
  	// rdfs:label xml:lang="en" (FinancialDates.rdf)
  	// RDF: fibo-fnd-dt-fd:CalculationPeriod
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-dt;ExplicitDatePeriod"/>
  }
  item def <'calculation period length'> CalculationPeriodLength specializes FiBO_FND_ExternalStubs::ExplicitDuration {
  	doc /* explicit number of days from the adjusted effective or start date to the adjusted termination or end date calculated in accordance with the applicable day count fraction */
  	// skos:definition xml:lang="en" (FinancialDates.rdf)
  	// rdfs:label xml:lang="en" (FinancialDates.rdf)
  	// RDF: fibo-fnd-dt-fd:CalculationPeriodLength
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-dt;ExplicitDuration"/>
  }
  item def <'calendar period'> CalendarPeriod specializes FiBO_FND_ExternalStubs::TimeInterval {
  	doc /* time interval that occurs within a system that fixes the beginning and length of a segment of the year with respect to that system */
  	// skos:definition xml:lang="en" (FinancialDates.rdf)
  	// rdfs:label xml:lang="en" (FinancialDates.rdf)
  	// RDF: fibo-fnd-dt-fd:CalendarPeriod
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-dt;TimeInterval"/>
  }
  item def CalendarSpecifiedInterval specializes Financial_Dates::RecurrenceInterval {
  	doc /* recurrence interval that is defined as the nth day of some calendar period (such as a calendar month), and a time direction (forward from the beginning of the month, or backwards from the end) */
  	// skos:definition xml:lang="en" (FinancialDates.rdf)
    // rdfs:label xml:lang="en" (FinancialDates.rdf)
  	// RDF: fibo-fnd-dt-fd:CalendarSpecifiedInterval
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-fd;RecurrenceInterval"/>
  }
  item def <'dated collection constituent'> DatedCollectionConstituent specializes FiBO_FND_ExternalStubs::Constituent {
  	doc /* element of a collection that is associated with a date and time */
  	// skos:definition xml:lang="en" (FinancialDates.rdf)
  	// rdfs:label xml:lang="en" (FinancialDates.rdf)
  	// RDF: fibo-fnd-dt-fd:DatedCollectionConstituent
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-col;Constituent"/>
  }
  item def <'dated structured collection'> DatedStructuredCollection specializes FiBO_FND_ExternalStubs::StructuredCollection {
  	doc /* structured collection whose elements are required to have a date and time */
  	// skos:definition xml:lang="en" (FinancialDates.rdf)
  	// rdfs:label xml:lang="en" (FinancialDates.rdf)
  	// RDF: fibo-fnd-dt-fd:DatedStructuredCollection
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-col;StructuredCollection"/>
  }
  item def <'explicit recurrence interval'> ExplicitRecurrenceInterval specializes Financial_Dates::RecurrenceInterval {
  	doc /* recurrence interval defined via an explicit duration */
  	// skos:definition xml:lang="en" (FinancialDates.rdf)
  	// rdfs:label xml:lang="en" (FinancialDates.rdf)
  	// RDF: fibo-fnd-dt-fd:ExplicitRecurrenceInterval
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-fd;RecurrenceInterval"/>
  }
  item def <'recurrence interval'> RecurrenceInterval specializes FiBO_FND_ExternalStubs::TimeInterval {
  	doc /* time interval that is consistent between elements of a regular schedule */
  	// skos:definition xml:lang="en" (FinancialDates.rdf)
  	// rdfs:label xml:lang="en" (FinancialDates.rdf)
  	// RDF: fibo-fnd-dt-fd:RecurrenceInterval
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-dt;TimeInterval"/>
  }
  item def <'relative date'> RelativeDate specializes Financial_Dates::CalculatedDate {
  	doc /* calculated date that is some duration before or after another date */
  	// skos:definition xml:lang="en" (FinancialDates.rdf)
  	// rdfs:label xml:lang="en" (FinancialDates.rdf)
  	// RDF: fibo-fnd-dt-fd:RelativeDate
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-fd;CalculatedDate"/>
  }
  item def <'schedule'> Schedule specializes FiBO_FND_ExternalStubs::Collection {
  	doc /* collection of events, observations, or other FiBOOccurrences and the associated dates and/or times when they will be done */
  	// skos:definition xml:lang="en" (FinancialDates.rdf)
  	// rdfs:label xml:lang="en" (FinancialDates.rdf)
  	// RDF: fibo-fnd-dt-fd:Schedule
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-col;Collection"/>
  }
  item def <'schedule stub'> ScheduleStub specializes FiBO_FND_ExternalStubs::Collection {
  	doc /* date period before the start of the recurring part of a schedule or after the end of the recurring part, which may be associated with a specific FiBOOccurrence kind */
  	// skos:definition xml:lang="en" (FinancialDates.rdf)
  	// rdfs:label xml:lang="en" (FinancialDates.rdf)
  	// RDF: fibo-fnd-dt-fd:ScheduleStub
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-col;Collection"/>
  }
  item def <'specified date'> SpecifiedDate specializes Financial_Dates::CalculatedDate {
  	doc /* calculated date that is defined by a rule that is captured as a string by the 'hasDateSpecification' property */
  	// skos:definition xml:lang="en" (FinancialDates.rdf)
  	// rdfs:label xml:lang="en" (FinancialDates.rdf)
  	// RDF: fibo-fnd-dt-fd:SpecifiedDate
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-fd;CalculatedDate"/>
  }
  item def <'time direction'> TimeDirection specializes FiBO_FND_ExternalStubs::TemporalEntity {
  	doc /* enumeration that indicates whether a calendar-specified date is figured from the start or the end of a calendar period */
  	// skos:definition xml:lang="en" (FinancialDates.rdf)
  	// rdfs:label xml:lang="en" (FinancialDates.rdf)
  	// RDF: fibo-fnd-dt-fd:TimeDirection
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-dt;TemporalEntity"/>
  }
}
