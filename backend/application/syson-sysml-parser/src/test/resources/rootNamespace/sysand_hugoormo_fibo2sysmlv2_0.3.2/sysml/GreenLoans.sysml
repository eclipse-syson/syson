// ===================================================================
// GreenLoans.sysml
// Source-parity extracted from: FiBO_LOAN_LoanSpecific.sysml
// Primary source module: GreenLoans.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Loan LOAN/LoansSpecific
// ===================================================================
package Green_Loans {
  doc/* The green loans ontology defines concepts specific to loans made available exclusively to finance or refinance, in whole or in part, new and/or existing eligible 'green projects'. Examples include loans whose purpose involves renewable energy, energy efficiency, climate change adaptation and green buildings that meet regional, national or internationally recognized standards or certifications. (source: dct:abstract, GreenLoans.rdf) */
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

  item def <'assessment boundary'> AssessmentBoundary specializes FiBO_FND_ExternalStubs::Context, FiBO_FND_ExternalStubs::Specification {
  	doc /* credit agreement and/or contingent facility (such as a bonding line, guarantee line or letter of credit) made available exclusively to finance, re-finance or guarantee, in whole or in part, new and/or existing eligible green projects that are aligned to the four core components of the Green Loan Principles (GLP) */
  	// skos:definition xml:lang="en" (GreenLoans.rdf)
  	// rdfs:label xml:lang="en" (GreenLoans.rdf)
  	// RDF: fibo-loan-ln-grn:AssessmentBoundary
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cxtdsg;Context"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-doc;Specification"/>
  }
  item def <'environmental program'> EnvironmentalProgram specializes Objectives::Program {
  	doc /* broad scale initiative, activity, or investment aimed at improving environmental sustainability, reducing ecological harm, or addressing environmental challenges */
  	// skos:definition xml:lang="en" (GreenLoans.rdf)
  	// rdfs:label xml:lang="en" (GreenLoans.rdf)
  	// RDF: fibo-loan-ln-grn:EnvironmentalProgram
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-gao-obj;Program"/>
  }
  item def <'environmental project'> EnvironmentalProject specializes Objectives::Project {
  	doc /* specific initiative, activity, or investment aimed at improving environmental sustainability, reducing ecological harm, or addressing environmental challenges */
  	// skos:definition xml:lang="en" (GreenLoans.rdf)
  	// rdfs:label xml:lang="en" (GreenLoans.rdf)
  	// RDF: fibo-loan-ln-grn:EnvironmentalProject
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-gao-obj;Project"/>
  }
  item def <'green loan'> GreenLoan specializes Loans::Loan, Debt::CreditAgreementRepaidPeriodically {
  	doc /* credit agreement and/or contingent facility (such as a bonding line, guarantee line or letter of credit) made available exclusively to finance, re-finance or guarantee, in whole or in part, new and/or existing eligible green projects that are aligned to the four core components of the Green Loan Principles (GLP) */
  	// skos:definition xml:lang="en" (GreenLoans.rdf)
  	// rdfs:label xml:lang="en" (GreenLoans.rdf)
  	// RDF: fibo-loan-ln-grn:GreenLoan
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;CreditAgreementRepaidPeriodically"/>
  }
  item def <'green project'> GreenProject specializes EnvironmentalProject {
  	doc /* observation (measurement) of a key performance indicator measured at specific date and time */
  	// skos:definition xml:lang="en" (GreenLoans.rdf)
  	// rdfs:label xml:lang="en" (GreenLoans.rdf)
  	// RDF: fibo-loan-ln-grn:GreenProject
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-spc-grn;EnvironmentalProject"/>
  }
  item def <'green project use of proceeds provision'> GreenProjectUseOfProceedsProvision specializes Contracts::UseOfProceedsProvision {
  	doc /* use of proceeds provision specifying that funds obtained through financing, such as through a credit agreement, offering, warrant, or other instrument are intended to be used for Green Projects (including other related and supporting expenditures, including research and development) */
  	// skos:definition xml:lang="en" (GreenLoans.rdf)
  	// rdfs:label xml:lang="en" (GreenLoans.rdf)
  	// RDF: fibo-loan-ln-grn:GreenProjectUseOfProceedsProvision
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;UseOfProceedsProvision"/>
  }
  item def <'observed indicator value'> ObservedIndicatorValue specializes Financial_Dates::DatedCollectionConstituent {
  	doc /* observation (measurement) of a key performance indicator measured at specific date and time */
  	// skos:definition xml:lang="en" (GreenLoans.rdf)
  	// rdfs:label xml:lang="en" (GreenLoans.rdf)
  	// RDF: fibo-loan-ln-grn:ObservedIndicatorValue
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-fd;DatedCollectionConstituent"/>
  }
  item def <'observed indicator value structure'> ObservedIndicatorValueStructure specializes Financial_Dates::DatedStructuredCollection {
  	doc /* collection of observations for some key performance indicator measured over a specified window of time */
  	// skos:definition xml:lang="en" (GreenLoans.rdf)
  	// rdfs:label xml:lang="en" (GreenLoans.rdf)
  	// RDF: fibo-loan-ln-grn:ObservedIndicatorValueStructure
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-fd;DatedStructuredCollection"/>
  }
  item def <'sustainability business objective'> SustainabilityBusinessObjective specializes Objectives::BusinessObjective {
  	doc /* objective related to improving the sustainability profile of the business */
  	// skos:definition xml:lang="en" (GreenLoans.rdf)
  	// rdfs:label xml:lang="en" (GreenLoans.rdf)
  	// RDF: fibo-loan-ln-grn:SustainabilityBusinessObjective
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-gao-obj;BusinessObjective"/>
  }
  item def <'sustainability business strategy'> SustainabilityBusinessStrategy specializes Objectives::BusinessStrategy {
  	doc /* strategy for achieving specific business objectives related to sustainability (from an environmental and/or social and/or governance (ESG) perspective) */
  	// skos:definition xml:lang="en" (GreenLoans.rdf)
  	// rdfs:label xml:lang="en" (GreenLoans.rdf)
  	// RDF: fibo-loan-ln-grn:SustainabilityBusinessStrategy
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-gao-obj;BusinessStrategy"/>
  }
  item def <'sustainability key performance indicator'> SustainabilityKeyPerformanceIndicator specializes Analytics::KeyPerformanceIndicator {
  	doc /* measurable performance indicator that is sustainability specific */
  	// skos:definition xml:lang="en" (GreenLoans.rdf)
  	// rdfs:label xml:lang="en" (GreenLoans.rdf)
  	// RDF: fibo-loan-ln-grn:SustainabilityKeyPerformanceIndicator
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-utl-alx;KeyPerformanceIndicator"/>
  }
  item def <'sustainability key performance indicator identifier'> SustainabilityKeyPerformanceIndicatorIdentifier specializes FiBO_FND_ExternalStubs::ContextualIdentifier {
  	doc /* credit agreement and/or contingent facility (such as a bonding line, guarantee line or letter of credit) for which the economic characteristics can vary depending on whether the borrower achieves ambitious, material and quantifiable predetermined sustainability performance objectives aligned with Sustainability-Linked Loan Principles (SSLP) */
  	// skos:definition xml:lang="en" (GreenLoans.rdf)
  	// rdfs:label xml:lang="en" (GreenLoans.rdf)
  	// RDF: fibo-loan-ln-grn:SustainabilityKeyPerformanceIndicatorIdentifier
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cxtid;ContextualIdentifier"/>
  }
  item def <'sustainability-linked loan'> SustainabilityLinkedLoan specializes GreenLoan, Debt::CreditAgreementRepaidPeriodically, Commercial_Loans::CommercialLoan {
  	doc /* credit agreement and/or contingent facility (such as a bonding line, guarantee line or letter of credit) for which the economic characteristics can vary depending on whether the borrower achieves ambitious, material and quantifiable predetermined sustainability performance objectives aligned with Sustainability-Linked Loan Principles (SSLP) */
  	// skos:definition xml:lang="en" (GreenLoans.rdf)
  	// rdfs:label xml:lang="en" (GreenLoans.rdf)
  	// RDF: fibo-loan-ln-grn:SustainabilityLinkedLoan
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;CreditAgreementRepaidPeriodically"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-spc-com;CommercialLoan"/>
  }
  item def <'sustainability performance target'> SustainabilityPerformanceTarget specializes Financial_Dates::DatedStructuredCollection {
  	doc /* collection of quantitative target values used to calibrate the level of achievement a borrower makes with respect to a key performance indicator, by date, including, but not limited to, the methodology used to calculate its value at any point over the lifetime of a loan */
  	// skos:definition xml:lang="en" (GreenLoans.rdf)
  	// rdfs:label xml:lang="en" (GreenLoans.rdf)
  	// RDF: fibo-loan-ln-grn:SustainabilityPerformanceTarget
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-fd;DatedStructuredCollection"/>
  }
  item def <'sustainability performance target identifier'> SustainabilityPerformanceTargetIdentifier specializes FiBO_FND_ExternalStubs::ContextualIdentifier {
  	doc /* financial institution appointed to help design, implement, and monitor the sustainability aspects of a syndicated green loan */
  	// skos:definition xml:lang="en" (GreenLoans.rdf)
  	// rdfs:label xml:lang="en" (GreenLoans.rdf)
  	// RDF: fibo-loan-ln-grn:SustainabilityPerformanceTargetIdentifier
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cxtid;ContextualIdentifier"/>
  }
  item def <'sustainability structuring agent'> SustainabilityStructuringAgent specializes Functional_Entities::SyndicateMember {
  	doc /* financial institution appointed to help design, implement, and monitor the sustainability aspects of a syndicated green loan */
  	// skos:definition xml:lang="en" (GreenLoans.rdf)
  	// rdfs:label xml:lang="en" (GreenLoans.rdf)
  	// RDF: fibo-loan-ln-grn:SustainabilityStructuringAgent
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-fct-fct;SyndicateMember"/>
  }
  item def <'target indicator value'> TargetIndicatorValue specializes Financial_Dates::DatedCollectionConstituent {
  	doc /* target value for a key performance indicator as of a specific date and time */
  	// skos:definition xml:lang="en" (GreenLoans.rdf)
  	// rdfs:label xml:lang="en" (GreenLoans.rdf)
  	// RDF: fibo-loan-ln-grn:TargetIndicatorValue
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-fd;DatedCollectionConstituent"/>
  }
  item def <'transition loan'> TransitionLoan specializes GreenLoan, Debt::CreditAgreementRepaidPeriodically, Commercial_Loans::CommercialLoan {
  	doc /* credit agreement and/or contingent facility (such as a bonding line, guarantee line or letter of credit) designed to help a business or organization shift from carbon-intensive or environmentally harmful practices to more sustainable and environmentally friendly operations */
  	// skos:definition xml:lang="en" (GreenLoans.rdf)
  	// rdfs:label xml:lang="en" (GreenLoans.rdf)
  	// RDF: fibo-loan-ln-grn:TransitionLoan
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;CreditAgreementRepaidPeriodically"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-spc-com;CommercialLoan"/>
  }
  item def <'transition project'> TransitionProject specializes EnvironmentalProject {
  	doc /* strategy for achieving specific business objectives related to sustainability in the context of long-term decarbonization or sustainability transitions */
  	// skos:definition xml:lang="en" (GreenLoans.rdf)
  	// rdfs:label xml:lang="en" (GreenLoans.rdf)
  	// RDF: fibo-loan-ln-grn:TransitionProject
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-spc-grn;EnvironmentalProject"/>
  }
  item def <'transition strategy'> TransitionStrategy specializes Objectives::BusinessStrategy {
  	doc /* strategy for achieving specific business objectives related to sustainability in the context of long-term decarbonization or sustainability transitions */
  	// skos:definition xml:lang="en" (GreenLoans.rdf)
  	// rdfs:label xml:lang="en" (GreenLoans.rdf)
  	// RDF: fibo-loan-ln-grn:TransitionStrategy
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-gao-obj;BusinessStrategy"/>
  }
  item def <'transition use of proceeds provision'> TransitionUseOfProceedsProvision specializes Contracts::UseOfProceedsProvision {
  	doc /* use of proceeds provision specifying that funds obtained through financing, such as through a credit agreement, offering, warrant, or other instrument are intended to be used to fund specific projects, investments, or operational changes that support a company's transition toward sustainability */
  	// skos:definition xml:lang="en" (GreenLoans.rdf)
  	// rdfs:label xml:lang="en" (GreenLoans.rdf)
  	// RDF: fibo-loan-ln-grn:TransitionUseOfProceedsProvision
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;UseOfProceedsProvision"/>
  }
}

// --- fibo-loan-ln-lnp ---
