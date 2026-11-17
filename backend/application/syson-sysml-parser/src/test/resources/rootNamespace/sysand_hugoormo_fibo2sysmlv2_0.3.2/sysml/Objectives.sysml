// ===================================================================
// Objectives.sysml
// Source-parity extracted from: FiBO_FND_GoalsAndObjectives.sysml
// Primary source module: Objectives.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Foundations FND/GoalsAndObjectives
// ===================================================================

package Objectives {
  doc/* This ontology defines concepts including goal, objective, program, and strategy. Objectives are defined as being distinct from goals, in that they constitute time limited and measurable targets which some entity may seek to attain in pursuit of its goals. (source: dct:abstract, Objectives.rdf) */
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

  item def <'aim'> Aim {
		doc /* intention specifying a desired direction, condition, or situation toward which an agent's actions are directed, without requiring precise scope or measurable criteria */
		// skos:definition xml:lang="en" (Objectives.rdf)
		// rdfs:label xml:lang="en" (Objectives.rdf)
    // RDF: fibo-fnd-gao-obj:Aim
  }
  item def <'approach'> Approach {
		doc /* way of addressing an aim or problem characterized by high-level planning and systematic execution, without presupposing scope or granularity */
		// skos:definition xml:lang="en" (Objectives.rdf)
		// rdfs:label xml:lang="en" (Objectives.rdf)
    // RDF: fibo-fnd-gao-obj:Approach
  }
  item def <'business objective'> BusinessObjective specializes Objective {
  	doc /* objective that reflects the strategic goals and direction of a business within a time frame and available resources */
  	// skos:definition xml:lang="en" (Objectives.rdf)
  	// rdfs:label xml:lang="en" (Objectives.rdf)
  	// RDF: fibo-fnd-gao-obj:BusinessObjective
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-gao-obj;Objective"/>
  }
  item def <'business strategy'> BusinessStrategy specializes Strategy {
  	doc /* strategy for achieving a specific business goal, objective, solution or outcome */
  	// skos:definition xml:lang="en" (Objectives.rdf)
  	// rdfs:label xml:lang="en" (Objectives.rdf)
  	// RDF: fibo-fnd-gao-obj:BusinessStrategy
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-gao-obj;Strategy"/>
  }
  item def <'distribution strategy'> DistributionStrategy specializes Strategy {
  	doc /* strategy for making a product or service available, or for distributing funds, such as in the form of a dividend or coupon */
  	// skos:definition xml:lang="en" (Objectives.rdf)
  	// rdfs:label xml:lang="en" (Objectives.rdf)
  	// RDF: fibo-fnd-gao-obj:DistributionStrategy
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-gao-obj;Strategy"/>
  }
  item def <'financial objective'> FinancialObjective specializes Objective {
  	doc /* objective that reflects the strategic financial goals and direction of a party within a time frame and available resources */
  	// skos:definition xml:lang="en" (Objectives.rdf)
  	// rdfs:label xml:lang="en" (Objectives.rdf)
  	// RDF: fibo-fnd-gao-obj:FinancialObjective
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-gao-obj;Objective"/>
  }
  item def <'goal'> Goal specializes Aim {
  	doc /* aim distinguished by broad scope and aspirational character, often qualitative and long-term, in order to achieve a desired state */
  	// skos:definition xml:lang="en" (Objectives.rdf)
  	// rdfs:label xml:lang="en" (Objectives.rdf)
  	// RDF: fibo-fnd-gao-obj:Goal
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-gao-obj;Aim"/>
  }
  item def <'investment objective'> InvestmentObjective specializes FinancialObjective {
  	doc /* financial objective used by an investor to determine whether or not a given potential investment is appropriate for themselves or on behalf of another party */
  	// skos:definition xml:lang="en" (Objectives.rdf)
  	// rdfs:label xml:lang="en" (Objectives.rdf)
  	// RDF: fibo-fnd-gao-obj:InvestmentObjective
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-gao-obj;FinancialObjective"/>
  }
  item def <'local investment objective'> LocalInvestmentObjective specializes InvestmentObjective {
  	doc /* financial objective that aims to support geographically centralized, community development needs */
  	// skos:definition xml:lang="en" (Objectives.rdf)
  	// rdfs:label xml:lang="en" (Objectives.rdf)
  	// RDF: fibo-fnd-gao-obj:LocalInvestmentObjective
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-gao-obj;InvestmentObjective"/>
  }
  item def <'method'> Method specializes Approach {
  	doc /* approach distinguished by systematic, repeatable procedures and operational specificity for doing or calculating something, consisting of defined steps or rules to achieve a result */
  	// skos:definition xml:lang="en" (Objectives.rdf)
  	// rdfs:label xml:lang="en" (Objectives.rdf)
  	// RDF: fibo-fnd-gao-obj:Method
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-gao-obj;Approach"/>
  }
  item def <'objective'> Objective specializes Aim {
  	doc /* aim distinguished by specific scope and measurable criteria, often quantitative and short-term that a party seeks to attain, typically in order to achieve its long-term goals */
  	// skos:definition xml:lang="en" (Objectives.rdf)
  	// rdfs:label xml:lang="en" (Objectives.rdf)
  	// RDF: fibo-fnd-gao-obj:Objective
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-gao-obj;Aim"/>
  }
  item def <'program'> Program specializes FiBO_FND_ExternalStubs::Situation {
    doc /* state of affairs and coordinated set of activities designed to obtain benefits not available from managing them individually */
    // skos:definition xml:lang="en" (Objectives.rdf)
    // rdfs:label xml:lang="en-US" (Objectives.rdf)
    // rdfs:label xml:lang="en-GB" (Objectives.rdf)
    // RDF: fibo-fnd-gao-obj:Program
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;Situation"/>
  }
  item def <'program identifier'> ProgramIdentifier specializes FiBO_FND_ExternalStubs::ContextualIdentifier {
  	doc /* sequence of characters uniquely identifying a specific program within the context in which that program is administered */
  	// skos:definition xml:lang="en" (Objectives.rdf)
    // rdfs:label xml:lang="en-US" (Objectives.rdf)
    // rdfs:label xml:lang="en-GB" (Objectives.rdf)
  	// RDF: fibo-fnd-gao-obj:ProgramIdentifier
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cxtid;ContextualIdentifier"/>
  }
  item def <'program name'> ProgramName specializes FiBO_FND_CodeSets_And_Schemes::ContextualName {
  	doc /* contextual designation for a program within the context in which that program is administered */
  	// skos:definition xml:lang="en" (Objectives.rdf)
    // rdfs:label xml:lang="en-US" (Objectives.rdf)
    // rdfs:label xml:lang="en-GB" (Objectives.rdf)
  	// RDF: fibo-fnd-gao-obj:ProgramName
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cxtdsg;ContextualName"/>
  }
  item def <'project'> Project specializes FiBO_FND_ExternalStubs::Situation {
    doc /* state of affairs and unique and temporary organization, designed to deliver a tangible output */
    // skos:definition xml:lang="en" (Objectives.rdf)
    // rdfs:label xml:lang="en" (Objectives.rdf)
    // RDF: fibo-fnd-gao-obj:Project
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;Situation"/>
  }
  item def <'project identifier'> ProjectIdentifier specializes FiBO_FND_ExternalStubs::ContextualIdentifier {
  	doc /* sequence of characters uniquely identifying a specific project within the context in which that project or an overarching program is administered */
  	// skos:definition xml:lang="en" (Objectives.rdf)
  	// rdfs:label xml:lang="en" (Objectives.rdf)
  	// RDF: fibo-fnd-gao-obj:ProjectIdentifier
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cxtid;ContextualIdentifier"/>
  }
  item def <'project name'> ProjectName specializes FiBO_FND_CodeSets_And_Schemes::ContextualName {
  	doc /* contextual designation for a project */
  	// skos:definition xml:lang="en" (Objectives.rdf)
  	// rdfs:label xml:lang="en" (Objectives.rdf)
  	// RDF: fibo-fnd-gao-obj:ProjectName
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cxtdsg;ContextualName"/>
  }
  item def <'sales strategy'> SalesStrategy specializes BusinessStrategy {
  	doc /* strategy for selling something, including allocating resources to the sales process, reducing costs, and optimizing revenues */
  	// skos:definition xml:lang="en" (Objectives.rdf)
  	// rdfs:label xml:lang="en" (Objectives.rdf)
  	// RDF: fibo-fnd-gao-obj:SalesStrategy
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-gao-obj;BusinessStrategy"/>
  }
  item def <'strategy'> Strategy specializes Approach {
  	doc /* high-level approach that guides decision-making and the coordination of actions and plans in pursuit of some aim */
  	// skos:definition xml:lang="en" (Objectives.rdf)
  	// rdfs:label xml:lang="en" (Objectives.rdf)
  	// RDF: fibo-fnd-gao-obj:Strategy
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-gao-obj;Approach"/>
  }
  item def <'study'> Study specializes FiBO_FND_ExternalStubs::Situation {
  	doc /* analytical activity that investigates a specified area of interest, to determine its characteristics, relationships, constraints, or implications */
  	// skos:definition xml:lang="en" (Objectives.rdf)
  	// rdfs:label xml:lang="en" (Objectives.rdf)
  	// RDF: fibo-fnd-gao-obj:Study
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;Situation"/>
  }
  item def <'triple bottom line objective'> TripleBottomLineObjective specializes InvestmentObjective {
  	doc /* financial objective that integrates economic performance, environmental stewardship, and social responsibility as co-equal criteria for success */
  	// skos:definition xml:lang="en" (Objectives.rdf)
  	// rdfs:label xml:lang="en" (Objectives.rdf)
  	// RDF: fibo-fnd-gao-obj:TripleBottomLineObjective
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-gao-obj;InvestmentObjective"/>
  }

}
