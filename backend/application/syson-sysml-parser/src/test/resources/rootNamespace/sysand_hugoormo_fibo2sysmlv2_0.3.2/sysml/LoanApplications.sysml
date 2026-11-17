// ===================================================================
// LoanApplications.sysml
// Source-parity extracted from: FiBO_LOAN_LoansGeneral.sysml
// Primary source module: LoanApplications.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Loan LOAN/LoansGeneral
// ===================================================================
package Loan_Applications {
  doc/* This ontology defines concepts for the loan application process, along with kinds of loan application and the parties thereto. Another major thread of this ontology is credit risk assessment at the highest level, which includes security agreements, pre-approvals, and credit reports. (source: dct:abstract, LoanApplications.rdf) */
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

  item def <'all borrowers&apos; monthly income'> AllBorrowersMonthlyIncome specializes Accounting_Equity::Income, Currency_Amount::MonetaryAmount {
  	doc /* assessment report detailing information about the borrower and their credit history that may be relevant to the loan application */
  	// skos:definition xml:lang="en" (LoanApplications.rdf)
  	// rdfs:label xml:lang="en" (LoanApplications.rdf)
  	// RDF: fibo-loan-ln-app:AllBorrowersMonthlyIncome
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-aeq;Income"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-cur;MonetaryAmount"/>
  }
  item def <'borrower assessment'> BorrowerAssessment specializes Assessments::AssessmentReport {
  	doc /* assessment report detailing information about the borrower and their credit history that may be relevant to the loan application */
  	// skos:definition xml:lang="en" (LoanApplications.rdf)
  	// rdfs:label xml:lang="en" (LoanApplications.rdf)
  	// RDF: fibo-loan-ln-app:BorrowerAssessment
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-asmt;AssessmentReport"/>
  }
  item def <'borrower monthly income'> BorrowerMonthlyIncome specializes Accounting_Equity::Income, Currency_Amount::MonetaryAmount {
  	doc /* report providing the methodology and supporting documentation used to verify the borrower&apos;s income */
  	// skos:definition xml:lang="en" (LoanApplications.rdf)
  	// rdfs:label xml:lang="en" (LoanApplications.rdf)
  	// RDF: fibo-loan-ln-app:BorrowerMonthlyIncome
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-aeq;Income"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-cur;MonetaryAmount"/>
  }
  item def <'credit risk assessment'> CreditRiskAssessment specializes Assessments::AssessmentActivity { // RDF: fibo-loan-ln-app:CreditRiskAssessment
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-asmt;AssessmentActivity"/>
      // RDF: fibo-loan-ln-app:CreditRiskAssessment
  }
  item def <'income verification report'> IncomeVerificationReport specializes Assessments::AssessmentReport {
  	doc /* report providing the methodology and supporting documentation used to verify the borrower's income */
  	// skos:definition xml:lang="en" (LoanApplications.rdf)
  	// rdfs:label xml:lang="en" (LoanApplications.rdf)
  	// RDF: fibo-loan-ln-app:IncomeVerificationReport
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-asmt;AssessmentReport"/>
  }
  item def <'individual person credit rating'> IndividualPersonCreditRating specializes Credit_Ratings::CreditRating {
  	doc /* credit rating that provides an opinion of the creditworthiness of a person */
  	// skos:definition xml:lang="en" (LoanApplications.rdf)
  	// rdfs:label xml:lang="en" (LoanApplications.rdf)
  	// RDF: fibo-loan-ln-app:IndividualPersonCreditRating
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-crt;CreditRating"/>
  }
  item def <'loan application'> LoanApplication specializes FiBO_FND_ExternalStubs::Document {
  	doc /* date on which an application was signed and submitted */
  	// skos:definition xml:lang="en" (LoanApplications.rdf)
  	// rdfs:label xml:lang="en" (LoanApplications.rdf)
  	// RDF: fibo-loan-ln-app:LoanApplication
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-doc;Document"/>
  }
  item def <'loan application at agreement stage'> LoanApplicationAtAgreementStage specializes LoanApplicationStatus {
  	doc /* underwriting was performed manually */
  	// skos:definition xml:lang="en" (LoanApplications.rdf)
  	// rdfs:label xml:lang="en" (LoanApplications.rdf)
  	// RDF: fibo-loan-ln-app:LoanApplicationAtAgreementStage
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-ln-app;LoanApplicationStatus"/>
  }
  item def <'loan application at disbursement stage'> LoanApplicationAtDisbursementStage specializes LoanApplicationStatus {
  	doc /* underwriting was performed manually */
  	// skos:definition xml:lang="en" (LoanApplications.rdf)
  	// rdfs:label xml:lang="en" (LoanApplications.rdf)
  	// RDF: fibo-loan-ln-app:LoanApplicationAtDisbursementStage
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-ln-app;LoanApplicationStatus"/>
  }
  item def <'loan application phase'> LoanApplicationPhase specializes Lifecycles::LifecycleStage {
  	doc /* underwriting was performed manually */
  	// skos:definition xml:lang="en" (LoanApplications.rdf)
  	// rdfs:label xml:lang="en" (LoanApplications.rdf)
  	// RDF: fibo-loan-ln-app:LoanApplicationPhase
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-lif;LifecycleStage"/>
  }
  item def <'loan application status'> LoanApplicationStatus specializes Lifecycles::LifecycleStatus {
  	doc /* underwriting was performed manually */
  	// skos:definition xml:lang="en" (LoanApplications.rdf)
  	// rdfs:label xml:lang="en" (LoanApplications.rdf)
  	// RDF: fibo-loan-ln-app:LoanApplicationStatus
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-lif;LifecycleStatus"/>
  }
  item def <'loan at application stage'> LoanAtApplicationStage specializes LoanApplicationStatus {
  	doc /* underwriting was performed manually */
  	// skos:definition xml:lang="en" (LoanApplications.rdf)
  	// rdfs:label xml:lang="en" (LoanApplications.rdf)
  	// RDF: fibo-loan-ln-app:LoanAtApplicationStage
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-ln-app;LoanApplicationStatus"/>
  }
  item def <'pre-approval contract'> PreApprovalContract specializes Contracts::WrittenContract {
  	doc /* written commitment to lend when specified conditions are met, such as finding suitable property, and unchanged creditworthiness */
  	// skos:definition xml:lang="en" (LoanApplications.rdf)
  	// rdfs:label xml:lang="en" (LoanApplications.rdf)
  	// RDF: fibo-loan-ln-app:PreApprovalContract
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;WrittenContract"/>
  }
  item def <'pre-approval request'> PreApprovalRequest specializes Reporting::RequestActivity {
  	doc /* request from a potential borrower that a lender commit to pre-approving the borrower for a loan of up to a specified amount of money */
  	// skos:definition xml:lang="en" (LoanApplications.rdf)
  	// rdfs:label xml:lang="en" (LoanApplications.rdf)
  	// RDF: fibo-loan-ln-app:PreApprovalRequest
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-rep;RequestActivity"/>
  }
  item def <'public record'> PublicRecord specializes FiBO_FND_ExternalStubs::Record {
  	doc /* record about an action involving a party that is publicly available from a court or other government agency */
  	// skos:definition xml:lang="en" (LoanApplications.rdf)
  	// rdfs:label xml:lang="en" (LoanApplications.rdf)
  	// RDF: fibo-loan-ln-app:PublicRecord
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-doc;Record"/>
  }
  item def <'public record category'> PublicRecordCategory specializes FiBO_FND_ExternalStubs::Classifier {
  	doc /* classifier of public records relevant to a loan application, e.g. tax lien, wage garnishment, foreclosure */
  	// skos:definition xml:lang="en" (LoanApplications.rdf)
  	// rdfs:label xml:lang="en" (LoanApplications.rdf)
  	// RDF: fibo-loan-ln-app:PublicRecordCategory
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
  }
  item def <'total debt expense ratio'> TotalDebtExpenseRatio specializes Currency_Amount::PercentageMonetaryAmount {
  	doc /* ratio of all monthly debt payments of all borrowers, including proposed expenses, with respect to the income of the borrowers as relied upon to make a credit decision */
  	// skos:definition xml:lang="en" (LoanApplications.rdf)
  	// rdfs:label xml:lang="en" (LoanApplications.rdf)
  	// RDF: fibo-loan-ln-app:TotalDebtExpenseRatio
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-cur;PercentageMonetaryAmount"/>
  }
  item def <'underwriting automation'> UnderwritingAutomation specializes FiBO_FND_ExternalStubs::Classifier {
  	doc /* classifier indicating whether a loan was underwritten manually or using an automated underwriting system */
  	// skos:definition xml:lang="en" (LoanApplications.rdf)
  	// rdfs:label xml:lang="en" (LoanApplications.rdf)
  	// RDF: fibo-loan-ln-app:UnderwritingAutomation
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
  }
  item def <'underwriting decision'> UnderwritingDecision specializes FiBO_FND_ExternalStubs::Classifier {
  	doc /* classifier providing a loan approval recommendation determined either manually or by an automated underwriting system */
  	// skos:definition xml:lang="en" (LoanApplications.rdf)
  	// rdfs:label xml:lang="en" (LoanApplications.rdf)
  	// RDF: fibo-loan-ln-app:UnderwritingDecision
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
  }
}

// --- fibo-loan-ln-ev ---
