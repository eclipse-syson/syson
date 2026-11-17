// ===================================================================
// FIBO_FND_DatesAndTimes.sysml
// CI Aggregator (public re-export of Dates & Times packages)
// ===================================================================
package FIBO_FND_DatesAndTimes {
  doc/* Configuration Item (CI) package for FIBO_FND_DatesAndTimes and its exported model elements. */
  public import BusinessDates::*;
  public import FinancialDates::*;
  public import FiOccurrences::*;
}

// ======================
// BusinessDates.sysml
// (feature-based from BusinessDates.rdf)
// ======================
package BusinessDates {

  private import ScalarValues::*;   // String, Integer, Boolean
  private import Time::*;           // Date
  private import FinancialDates::*;

  // Core business-day concepts

  item def Convention {
    doc/* widely accepted or established way of doing 'something' within some community of practice */
  }

  item def BusinessDayConvention specializes Convention {
    doc/* convention that enumerates the possible ways to handle a date that falls on a weekend or holiday */
    // Typically applied to calculated dates and schedule generation
  }

  item def BusinessDayAdjustment {
    doc/* adjustment applied to a date to move it to an acceptable business day according to a specified convention */
    attribute convention: BusinessDayConvention[0..1];
  }

  item def BusinessRecurrenceInterval {
    doc/* recurrence interval that is defined with respect to a business-day convention that determines how recurring days should be handled */
    attribute convention: BusinessRecurrenceIntervalConvention[0..1];
  }

  item def BusinessRecurrenceIntervalConvention specializes Convention {
    doc/* convention that specifies how a recurring date is figured, e.g., end-of-month, a day of the week, or more specifically, a T-bill auction date */
  }

  item def DayOfMonth {
    doc/* specific, recurring day of the month */
    attribute dayNumber: Integer[1..1];
  }

  item def DayOfWeek {
    doc/* named day of the week used when determining business-day handling */
    attribute name: String[1..1]; // e.g., Monday, Tuesday
  }

  item def EndOfMonth {
    doc/* the last day of a calendar month, irrespective of the length of the calendar month */
  }

  // How these conventions get used on dates/schedules

  // Reuse / reference classes that also appear in FinancialDates/Occurrences.
  // Keep them as minimal stubs here to avoid duplicate full definitions.
  // When a date is calculated and then adjusted for business-day rules:
  item def CalculatedDate {
    attribute businessDayAdjustment: BusinessDayAdjustment[0..1];
    attribute businessDayConvention: BusinessDayConvention[0..1];
  }

  // When a regular schedule is created and recurrence is interpreted via business-day rules:
  item def RegularSchedule {
    attribute businessRecurrenceIntervalConvention: BusinessRecurrenceIntervalConvention[0..1];
  }
}

// ======================
// FinancialDates.sysml
// (feature-based from FinancialDates.rdf)
// ======================
package FinancialDates {

  private import ScalarValues::*;   // String, Integer, Boolean
  private import Time::*;           // Date, DateTime

  // Minimal external stubs (wire later)
  item def Duration {
    attribute dayCount: Integer[0..1];
    attribute durationText: String[0..1];
  }
  item def ExplicitDate {
    attribute dateValue: Date[0..1];
  }
  item def ExplicitDatePeriod {
    attribute startsOn: ExplicitDate[0..1];
    attribute endsOn: ExplicitDate[0..1];
  }
  item def CombinedDateTime {
    attribute dateValue: Date[0..1];
    attribute dateTimeValue: DateTime[0..1];
  }

  // Enumerations

  enum def TimeDirection {
    doc/* indicates whether a calendar-specified date is figured from the start or the end of a calendar period */
    enum FromStart;
    enum FromEnd;
  }

  // Schedules and building blocks

  item def Schedule {
    doc/* collection of events, observations, or other occurrences and the associated dates and/or times when they will be done */
    attribute overallPeriod: ExplicitDatePeriod[0..1];  // hasOverallPeriod
  }

  item def RegularSchedule specializes Schedule {
    doc/* schedule with a consistent recurrence interval across its elements */
    attribute recurrenceInterval: RecurrenceInterval[0..1]; // hasRecurrenceInterval
    attribute initialStub: ScheduleStub[0..1];              // hasInitialStub
    attribute finalStub: ScheduleStub[0..1];                // hasFinalStub
  }

  item def AdHocSchedule specializes Schedule {
    doc/* schedule consisting of some number of individual events that are not necessarily recurring */
    attribute entrySchedule: AdHocScheduleEntry[0..*];             // entries
  }

  item def AdHocScheduleEntry {
    doc/* entry, including a date or date and time, among multiple non-regularly-recurring entries in a schedule */
    attribute date: ExplicitDate[0..1];
    attribute dateTime: CombinedDateTime[0..1];
  }

  // Recurrence & calendar periods

  item def RecurrenceInterval {
    doc/* time interval that is consistent between elements of a regular schedule */
    attribute calendarPeriod: CalendarPeriod[0..1];        // hasCalendarPeriod
    attribute explicitRecurrenceInterval: ExplicitRecurrenceInterval[0..1];
    attribute timeDirection: TimeDirection[0..1];          // hasTimeDirection
  }

  item def ExplicitRecurrenceInterval {
    doc/* recurrence interval defined via an explicit duration */
    attribute duration: Duration[0..1];
  }

  item def CalendarPeriod {
    doc/* time interval that occurs within a system that defines the structure and length of a segment of the year with respect to that system */
    attribute label: String[0..1];
  }

  item def CalendarSpecifiedInterval specializes RecurrenceInterval {
    doc/* recurrence interval that is defined with respect to the calendar, e.g., forward from the beginning of the month, or backwards from the end */
  }

  item def ScheduleStub {
    doc/* date period before the start of the recurring part of a schedule or after the end of the recurring part, which may be associated with a specific occurrence kind */
    attribute period: ExplicitDatePeriod[0..1];
  }

  // Calculated/relative/specified dates

  item def CalculatedDate {
    doc/* date that is or will be determined based on some formula */
    attribute anchorDate: AnchorDate[0..1];         // hasAnchorDate
    attribute relativeDuration: Duration[0..1];     // hasRelativeDuration
    attribute asOfDate: ExplicitDate[0..1];         // hasAsOfDate
  }

  item def RelativeDate specializes CalculatedDate {
    doc/* calculated date that is some duration before or after another date */
  }

  item def SpecifiedDate specializes CalculatedDate {
    doc/* calculated date that is defined by a rule captured as a string via hasDateSpecification */
    attribute dateSpecification: String[0..1];      // hasDateSpecification
  }

  // Anchor & periods

  item def AnchorDate {
    doc/* fixed reference point within a series or timeline */
    attribute date: ExplicitDate[0..1];
  }

  item def CalculationPeriod {
    doc/* explicit period from the start to the end of an interval or range within which a computational process or operation occurs */
    attribute period: ExplicitDatePeriod[0..1];
    attribute calculationPeriodLength: CalculationPeriodLength[0..1];
  }

  item def CalculationPeriodLength {
    doc/* explicit number of days from the adjusted start date to the adjusted end date of the current calculation period, calculated in accordance with the applicable day count fraction */
    attribute numberOfDays: Integer[0..1];
  }

  // Misc support

  item def BusinessCalendar {
    doc/* schedule and communications tool used to manage time-dependent obligations, events, and coordinate activities within and/or across organizations */
    attribute openingDateTime: CombinedDateTime[0..1]; // hasOpeningDateTime
  }

  item def Age {
    doc/* length of time that something or someone has been alive or existed */
    attribute duration: Duration[0..1];
  }

  // Useful generic annotations (common properties appearing in the RDF set)
  item def DatedThing {
    attribute dateAdded: ExplicitDate[0..1];     // hasDateAdded
    attribute dateReceived: ExplicitDate[0..1];  // hasDateReceived
  }
}

// ======================
// FiOccurrences.sysml
// (feature-based from Occurrences.rdf)
// ======================
package FiOccurrences {

  private import ScalarValues::*;   // String, Integer, Boolean
  private import Time::*;           // Date, DateTime

  // Minimal external stubs (wire later)
  private import FinancialDates::*; // reuse CombinedDateTime where helpful

  item def FiOccurrenceKind {
    doc/* classifier that specifies the general nature of an occurrence (event) */
    attribute occurrenceKind: FiOccurrence[0..*];   // hasOccurrence
  }

  item def FiOccurrence {
    doc/* realized event, e.g., something that happens at a point in time or over an interval */
    attribute eventDate: Date[0..1];                // hasEventDate
    attribute eventDateValue: FinancialDates::CombinedDateTime[0..1]; // hasEventDateValue
  }

  item def FiOccurrenceBasedDate specializes FinancialDates::CalculatedDate {
    doc/* calculated date that is defined with respect to the occurrence of some occurrence kind */
    attribute isTriggeredBy: FiOccurrenceKind[1..*]; // isTriggeredBy
  }

  item def Calculation {
    doc/* actual execution of some computation, computational process, or operation that was scheduled or triggered by something */
    attribute input: ScalarValues::String[0..*];   // hasInput (placeholder; wire to proper 'Thing' later)
    attribute output: ScalarValues::String[0..*];  // hasOutput
  }

  item def CalculationEvent specializes FiOccurrenceKind {
    doc/* kind of event that is either scheduled or triggered, often associated with a calculated financial event, that causes a calculation to be performed */
  }
}



