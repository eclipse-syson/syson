// ===================================================================
// BusinessDates.sysml
// Source-parity extracted from: FiBO_FND_DatesAndTimes.sysml
// Primary source module: BusinessDates.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Foundations FND/DatesAndTimes
// ===================================================================

package Business_Dates {
  doc/* This ontology extends definitions of date and schedule concepts from the FinancialDates ontology with concepts defining dates that may be adjusted when they fall on weekends or holidays as defined in a given business center, for use in other FIBO ontologies. (source: dct:abstract, BusinessDates.rdf) */
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

  item def <'business day convention'> BusinessDayConvention specializes BusinessRecurrenceIntervalConvention {
  	doc /* convention that enumerates the possible ways to handle a date that falls on a weekend or holiday */
  	// skos:definition xml:lang="en" (BusinessDates.rdf)
  	// rdfs:label xml:lang="en" (BusinessDates.rdf)
  	// RDF: fibo-fnd-dt-bd:BusinessDayConvention
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-bd;BusinessRecurrenceIntervalConvention"/>
  }
  item def <'business recurrence interval'> BusinessRecurrenceInterval specializes Financial_Dates::RecurrenceInterval {
  	doc /* recurrence interval that is defined per a specific convention that determines how recurring days should be handled */
  	// skos:definition xml:lang="en" (BusinessDates.rdf)
  	// rdfs:label xml:lang="en" (BusinessDates.rdf)
  	// RDF: fibo-fnd-dt-bd:BusinessRecurrenceInterval
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-fd;RecurrenceInterval"/>
  }
  item def <'business recurrence interval convention'> BusinessRecurrenceIntervalConvention specializes Convention {
  	doc /* convention that specifies how recurring days should be handled, such as the end of the month, a particular day of the month, a day of the week, or more specifically, a t-bill auction date */
  	// skos:definition xml:lang="en" (BusinessDates.rdf)
  	// rdfs:label xml:lang="en" (BusinessDates.rdf)
  	// RDF: fibo-fnd-dt-bd:BusinessRecurrenceIntervalConvention
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-bd;Convention"/>
  }
  item def Convention { // RDF: fibo-fnd-dt-bd:Convention
    doc /* widely accepted or established way of doing 'something' within some community of practice */
    // skos:definition xml:lang="en" (BusinessDates.rdf)
    // rdfs:label xml:lang="en" (BusinessDates.rdf)
    // RDF: fibo-fnd-dt-bd:Convention
  }
  item def <'day of month'> DayOfMonth specializes BusinessRecurrenceInterval {
  	doc /* specific, recurring day of the month */
  	// skos:definition xml:lang="en" (BusinessDates.rdf)
  	// rdfs:label xml:lang="en" (BusinessDates.rdf)
  	// RDF: fibo-fnd-dt-bd:DayOfMonth
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-bd;BusinessRecurrenceInterval"/>
  }
  item def <'day of the week'> DayOfWeek specializes BusinessRecurrenceInterval {
  	doc /* specific, recurring day of the week */
  	// skos:definition xml:lang="en" (BusinessDates.rdf)
  	// rdfs:label xml:lang="en" (BusinessDates.rdf)
  	// RDF: fibo-fnd-dt-bd:DayOfWeek
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-bd;BusinessRecurrenceInterval"/>
  }
  item def <'end of the month'> EndOfMonth specializes BusinessRecurrenceInterval {
  	doc /* the last day of a calendar month, irrespective of the length of the calendar month */
  	// skos:definition xml:lang="en" (BusinessDates.rdf)
  	// rdfs:label xml:lang="en" (BusinessDates.rdf)
  	// RDF: fibo-fnd-dt-bd:EndOfMonth
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-bd;BusinessRecurrenceInterval"/>
  }
}
