// ===================================================================
// FIBO_FND_GoalsAndObjectives.sysml
// CI Aggregator (public re-export)
// ===================================================================
package FIBO_FND_GoalsAndObjectives {
  doc/* Configuration Item (CI) package for FIBO_FND_GoalsAndObjectives and its exported model elements. */
  public import GoalsAndObjectives::*;
}

// ======================
// GoalsAndObjectives.sysml
// (feature-based from Objectives.rdf)
// ======================
package GoalsAndObjectives {

  // ---- Imports (private) ----
  private import ScalarValues::*;         // String, Integer, Boolean
  private import Time::*;                 // (no explicit Date types here, safe)
  // Wire to Dates & Times for periods used on Objective
  private import FinancialDates::*;       // ExplicitDatePeriod, etc.

  // ---- Minimal local CMNS-aligned bases (swap to shared Commons/FND packages later) ----
  item def ContextualIdentifier {         // cmns-cxtid:ContextualIdentifier
    attribute identifierValue: String[0..1];
    attribute contextNote: String[0..1];
  }
  item def ContextualName {               // cmns-cxtdsg:ContextualName
    attribute nameValue: String[0..1];
    attribute contextNote: String[0..1];
  }
  item def Situation {                    // cmns-pts:Situation
    attribute description: String[0..1];
  }
  // If you prefer exact cmns-dt:DatePeriod, you can later map this to your Commons Dates & Times
  // For now we use FinancialDates::ExplicitDatePeriod directly on Objective.

  // =========================================================
  // Core concepts: Goal, Objective, Strategy, Method
  // =========================================================

  item def Goal {
    doc/* desired result that a party envisions, plans, and to which it commits, in order to achieve a desired state */
    // (No explicit required features in RDF beyond being referenced from strategies/programs/projects)
  }

  item def Objective {
    doc/* short-term, quantitative, measurable result that a party seeks to attain in order to achieve its long-term goals */
    // RDF restriction: cmns-dt:hasDatePeriod some DatePeriod
    // We represent this with FinancialDates::ExplicitDatePeriod; swap to your Commons DatePeriod when wired.
    attribute datePeriod: FinancialDates::ExplicitDatePeriod[1..*];
  }

  item def Strategy {
    doc/* high-level plan or approach for achieving a specific goal, objective, solution or outcome */
    // RDF restriction: hasGoal some Goal; hasObjective some Objective
    attribute goal: Goal[1..*];
    attribute strategyObjective: Objective[1..*];
    // Helpful utility from the ontology vocab:
    attribute usesMethod: Method[0..*];   // uses method
  }

  item def BusinessStrategy specializes Strategy {
    doc/* strategy for achieving a specific business goal, objective, solution or outcome */
    // RDF restriction on BusinessStrategy: hasObjective some BusinessObjective
    attribute businessStrategyObjective: BusinessObjective[1..*];
  }

  item def SalesStrategy specializes BusinessStrategy {
    doc/* strategy for selling something, including allocating resources to the sales process, reducing costs, and optimizing revenues */
  }

  item def DistributionStrategy specializes Strategy {
    doc/* strategy for making a product or service available for purchase by distributing funds, such as in the form of a dividend or coupon */
  }

  item def Method {
    doc/* systematic procedure or approach for doing or calculating something, consisting of defined steps or rules to achieve a result */
    // RDF restriction: hasObjective onClass Objective
    attribute methodObjective: Objective[0..*];
  }

  // =========================================================
  // Objectives specializations
  // =========================================================

  item def FinancialObjective specializes Objective {
    doc/* objective that reflects the strategic financial direction of a party within a time frame and available resources */
  }

  item def InvestmentObjective specializes FinancialObjective {
    doc/* financial objective used by an investor to determine whether an investment is appropriate for themselves or on behalf of another party */
  }

  item def LocalInvestmentObjective specializes InvestmentObjective {
    doc/* financial objective that aims to support geographically centralized, community development needs */
  }

  item def TripleBottomLineObjective specializes InvestmentObjective {
    doc/* financial objective that integrates economic performance, environmental stewardship, and social responsibility as co-equal criteria for success */
  }

  item def BusinessObjective specializes Objective {
    doc/* objective that reflects the strategic goals and direction of a business within a time frame and available resources */
  }

  // =========================================================
  // Programs and Projects (situations with goals/objectives)
  // =========================================================

  item def Program specializes Situation {
    doc/* state of affairs and coordinated set of activities made up of a number of related projects in order to obtain benefits not available from managing them individually */
    // RDF restrictions:
    //  - hasGoal some Goal
    //  - hasObjective some Objective
    //  - isIdentifiedBy some ProgramIdentifier
    //  - hasName some ProgramName
    //  - cmns-col:hasMember some Project
    //  - cmns-dsg:hasDescription some xsd:string
    attribute goal: Goal[1..*];
    attribute programObjective: Objective[1..*];
    attribute programIdentifier: ProgramIdentifier[1..*];
    attribute programName: ProgramName[1..*];
    attribute member: Project[1..*];
  }

  item def Project specializes Situation {
    doc/* state of affairs and unique and temporary organization, designed to deliver a tangible output */
    // RDF restrictions:
    //  - hasGoal some Goal
    //  - hasObjective some Objective
    //  - isMemberOf some Program
    //  - isIdentifiedBy some ProjectIdentifier
    //  - hasName some ProjectName
    //  - hasDescription some xsd:string
    //  - fibo-fnd-dt-oc:hasInput / hasOutput (ranges not explicitly typed in RDF)
    attribute goal: Goal[1..*];
    attribute projectObjective: Objective[1..*];
    attribute isMemberOf: Program[0..*];
    attribute projectIdentifier: ProjectIdentifier[1..*];
    attribute projectName: ProjectName[1..*];
    attribute input: String[0..*];    // placeholder until wired to an explicit input type
    attribute output: String[0..*];   // placeholder until wired to an explicit output type
  }

  // =========================================================
  // Names and Identifiers (contextual)
  // =========================================================

  item def ProgramIdentifier specializes ContextualIdentifier {
    doc/* sequence of characters uniquely identifying a program within the context in which that program is administered */
    // inverse cmns-id:identifies captured on Program via 'programIdentifier'
  }

  item def ProgramName specializes ContextualName {
    doc/* contextual designation for a program within the context in which that program is administered */
    // inverse cmns-dsg:isNameOf captured on Program via 'programName'
  }

  item def ProjectIdentifier specializes ContextualIdentifier {
    doc/* sequence of characters uniquely identifying a project within the context in which that project or an overarching program is administered */
  }

  item def ProjectName specializes ContextualName {
    doc/* contextual designation for a project */
  }
}
