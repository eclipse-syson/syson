// ===================================================================
// FIBO_FND_Utilities.sysml
// CI Aggregator (public re-export of Utilities subpackages)
// ===================================================================
package FIBO_FND_Utilities {
  doc/* Configuration Item (CI) package for FIBO_FND_Utilities and its exported model elements. */
  public import Analytics::*;
  public import AnnotationVocabulary::*;
}

// ======================
// AnnotationVocabulary.sysml
// ======================
package AnnotationVocabulary {

  // ---- Imports (private) ----
  private import ScalarValues::*;     // String, Real, Boolean
  private import Time::*;             // Date, DateTime

  // ===== Core annotation types =====

  item def SubjectRef {
    doc/* generic contextual reference to the thing an annotation or observation is about */
    attribute referenceText: String[0..1];
    attribute contextNote: String[0..1];
  }

  item def Tag {
    doc/* simple keyword or marker used to label or cluster related things */
    attribute value: String[1..1];
    attribute scheme: String[0..1];      // optional scheme/namespace for tags
    attribute note: String[0..1];
  }

  item def SourceCitation {
    doc/* reference to a source (publication, dataset, URL) supporting an annotation */
    attribute title: String[0..1];
    attribute uri: String[0..1];
    attribute publisher: String[0..1];
    attribute publicationDate: Date[0..1];
  }

  item def Annotation {
    doc/* descriptive note, label, or definition attached to one or more things */
    attribute aboutElement: SubjectRef[0..*];    // annotated subjects
    attribute text: String[1..1];
    attribute anotationLanguage: String[0..1];    // BCP-47 tag, e.g., en, en-US
    attribute createdOn: DateTime[0..1];
    attribute author: String[0..1];
    attribute tag: Tag[0..*];
    attribute confidence: Real[0..1];    // 0.0..1.0 if used
    attribute source: SourceCitation[0..*];
  }

  // Specializations

  item def LabelAnnotation specializes Annotation {
    doc/* human-friendly label for a thing, optionally localized */
    attribute label: String[1..1];
    attribute localization: String[0..1];
  }

  item def DefinitionAnnotation specializes Annotation {
    doc/* definitional text establishing the intended meaning of a term */
    attribute term: String[1..1];
    attribute definitionText: String[1..1];
  }

  item def NoteAnnotation specializes Annotation {
    doc/* general-purpose comment or remark */
  }

  item def ExampleAnnotation specializes Annotation {
    doc/* example illustrating usage or typical instances */
  }

  item def ChangeNoteAnnotation specializes Annotation {
    doc/* note describing a change, such as addition, modification, or deprecation */
    attribute changeDate: DateTime[0..1];
    attribute changeType: String[0..1];  // e.g., Added, Modified, Deprecated
  }
}

// ======================
// Analytics.sysml
// ======================
package Analytics {

  // ---- Imports (private) ----
  private import ScalarValues::*;        // String, Real, Integer, Boolean
  private import Time::*;                // Date, DateTime
  private import AnnotationVocabulary::*;// Tag, Annotation (optional linkage)
  private import FinancialDates::*;      // Duration, ExplicitDatePeriod (optional windows)

  // ===== Metrics, measures, and indicators =====

  item def Metric {
    doc/* named, computable quantity with an optional unit and formula */
    attribute name: String[1..1];
    attribute description: String[0..1];
    attribute unit: String[0..1];         // textual unit label
    attribute formula: String[0..1];      // human-readable expression
    attribute tag: Tag[0..*];
  }

  item def Indicator specializes Metric {
    doc/* metric intended to signal state or trend for decision-making */
  }

  item def Measure {
    doc/* realized value of a metric at a point in time and context */
    attribute metric: Metric[1..1];
    attribute value: Real[1..1];
    attribute observedAt: DateTime[0..1];
    attribute context: String[0..1];      // free-text context or scope
  }

  // ===== Data structures =====

  item def DataPoint {
    doc/* atomic key/value datum optionally tied to a time and subject */
    attribute key: String[1..1];
    attribute value: String[1..1];
    attribute observedAt: DateTime[0..1];
    attribute measuredSubject: SubjectRef[0..1];
  }

  item def Dataset {
    doc/* named collection of data points or measures with optional lineage */
    attribute name: String[1..1];
    attribute description: String[0..1];
    attribute createdOn: DateTime[0..1];
    attribute dataPoint: DataPoint[0..*];
    attribute measure: Measure[0..*];
    attribute source: String[0..1];
  }

  // ===== Analytics methods and models =====

  item def Algorithm {
    doc/* procedure or set of rules used to compute, transform, or infer */
    attribute name: String[1..1];
    attribute version: String[0..1];
    attribute description: String[0..1];
  }

  item def Model {
    doc/* parameterized artifact produced by training an algorithm on data */
    attribute name: String[1..1];
    attribute version: String[0..1];
    attribute algorithm: Algorithm[0..1];
    attribute trainedOn: Dataset[0..1];
    attribute producedMetric: Metric[0..*];
    attribute createdOn: DateTime[0..1];
  }

  // ===== Results, thresholds, and windows =====

  enum def ComparisonOperator {
    enum LessThan;
    enum LessOrEqual;
    enum Equal;
    enum GreaterOrEqual;
    enum GreaterThan;
    enum NotEqual;
  }

  item def Threshold {
    doc/* rule comparing a metric to a bound to trigger an alert or classification */
    attribute metric: Metric[1..1];
    attribute operator: ComparisonOperator[1..1];
    attribute thresholdValue: Real[1..1];
    attribute validFrom: DateTime[0..1];
    attribute validTo: DateTime[0..1];
  }

  item def Score {
    doc/* numeric result typically produced by a model or evaluation routine */
    attribute metric: Metric[1..1];
    attribute value: Real[1..1];
    attribute computedAt: DateTime[0..1];
    attribute confidence: Real[0..1];
    attribute explanation: String[0..1];
  }

  item def TimeWindow {
    doc/* temporal window used to aggregate or assess metrics */
    attribute windowStart: DateTime[0..1];
    attribute windowEnd: DateTime[0..1];
    attribute rollingPeriod: Duration[0..1];   // optional rolling window
  }

  // ===== Observations =====

  item def Observation {
    doc/* act of observing one or more measures for a subject at a time */
    attribute observedSubject: SubjectRef[0..1];
    attribute observedAt: DateTime[0..1];
    attribute measure: Measure[1..*];
    attribute note: String[0..1];
  }
}
