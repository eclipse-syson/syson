// ===================================================================
// Loans.sysml
// Source-parity extracted from: FiBO_LOAN_LoansGeneral.sysml
// Primary source module: Loans.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Loan LOAN/LoansGeneral
// ===================================================================
package Loans {
  doc/* This ontology is the top-level, and most fundamental ontology for the LOAN module, extending the Debt ontology to define concepts common to all loans. It includes the primary obligations to fund the loan and to pay it back according to payment schedules. Kinds of loans covered in this ontology include open and closed end, secured and unsecured. (source: dct:abstract, Loans.rdf) */
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

  item def <'closed-end credit'> ClosedEndCredit specializes Debt::CreditAgreement {
  	doc /* credit agreement in which the loan principal cannot be increased after funds are dispersed in full when the loan closes */
  	// skos:definition xml:lang="en" (Loans.rdf)
  	// rdfs:label xml:lang="en" (Loans.rdf)
  	// RDF: fibo-loan-ln-ln:ClosedEndCredit
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;CreditAgreement"/>
  }
  item def <'collateralized loan'> CollateralizedLoan specializes SecuredLoan {
  	doc /* secured loan that is secured with cash or other acceptable collateral (real property, securities or other assets) provided by the borrower as specified in the collateral agreement */
  	// skos:definition xml:lang="en" (Loans.rdf)
  	// rdfs:label xml:lang="en" (Loans.rdf)
  	// RDF: fibo-loan-ln-ln:CollateralizedLoan
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-ln-ln;SecuredLoan"/>
  }
  item def <'co-maker'> Comaker specializes Contracts::ContractThirdParty {
  	doc /* loan that is secured with respect to repayment of principal and interest by guaranty */
  	// skos:definition xml:lang="en" (Loans.rdf)
  	// rdfs:label xml:lang="en" (Loans.rdf)
  	// RDF: fibo-loan-ln-ln:Comaker
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractThirdParty"/>
  }
  item def <'combined loan-to-value ratio'> CombinedLoanToValueRatio specializes Currency_Amount::PercentageMonetaryAmount {
  	doc /* ratio of the total amount of debt that is secured by the asset(s) and the appraised value of the asset(s) securing the financing */
  	// skos:definition xml:lang="en" (Loans.rdf)
  	// rdfs:label xml:lang="en" (Loans.rdf)
  	// RDF: fibo-loan-ln-ln:CombinedLoanToValueRatio
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-cur;PercentageMonetaryAmount"/>
  }
  item def <'guaranteed loan'> GuaranteedLoan specializes SecuredLoan {
  	doc /* loan that is secured with respect to repayment of principal and interest by guaranty */
  	// skos:definition xml:lang="en" (Loans.rdf)
  	// rdfs:label xml:lang="en" (Loans.rdf)
  	// RDF: fibo-loan-ln-ln:GuaranteedLoan
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-ln-ln;SecuredLoan"/>
  }
  item def <'individual payment transaction'> IndividualPaymentTransaction specializes Clients_and_Accounts::IndividualTransaction, Payments_and_Schedules::Payment {
  	doc /* actual payment of principal, interest, fees, or other related amounts towards fulfillment of a debt obligation */
  	// skos:definition xml:lang="en" (Loans.rdf)
  	// rdfs:label xml:lang="en" (Loans.rdf)
  	// RDF: fibo-loan-ln-ln:IndividualPaymentTransaction
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-caa;IndividualTransaction"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-psch;Payment"/>
  }
  item def <'interest payment terms'> InterestPaymentTerms specializes Debt::DebtTerms {
  	doc /* secured loan that is secured with cash or other acceptable collateral (real property, securities or other assets) provided by the borrower as specified in the collateral agreement */
  	// skos:definition xml:lang="en" (Loans.rdf)
  	// rdfs:label xml:lang="en" (Loans.rdf)
  	// RDF: fibo-loan-ln-ln:InterestPaymentTerms
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;DebtTerms"/>
  }
  item def <'lender lien position'> LenderLienPosition specializes FiBO_FND_ExternalStubs::Classifier {
  	doc /* principal repayment terms related to payment of the loan prior to maturity */
  	// skos:definition xml:lang="en" (Loans.rdf)
  	// rdfs:label xml:lang="en" (Loans.rdf)
  	// RDF: fibo-loan-ln-ln:LenderLienPosition
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
  }
  item def <'loan'> Loan specializes Debt::CreditAgreementRepaidPeriodically, Financial_Instruments::DebtInstrument {
  	doc /* Right to fair representation of the facts about Loan products before entrering into agreements for same. */
  	// skos:definition xml:lang="en" (Loans.rdf)
  	// rdfs:label xml:lang="en" (Loans.rdf)
  	// RDF: fibo-loan-ln-ln:Loan
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;CreditAgreementRepaidPeriodically"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;DebtInstrument"/>
  }
  item def <'loan payment schedule'> LoanPaymentSchedule specializes Payments_and_Schedules::PaymentSchedule {
  	doc /* regular or explicit (ad hoc) payment schedule associated with a given loan-specific account */
  	// skos:definition xml:lang="en" (Loans.rdf)
  	// rdfs:label xml:lang="en" (Loans.rdf)
  	// RDF: fibo-loan-ln-ln:LoanPaymentSchedule
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-psch;PaymentSchedule"/>
  }
  item def <'loan-specific customer account'> LoanSpecificCustomerAccount specializes Clients_and_Accounts::CustomerAccount, Clients_and_Accounts::LoanOrCreditAccount { // RDF: fibo-loan-ln-ln:LoanSpecificCustomerAccount
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-caa;CustomerAccount"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-caa;LoanOrCreditAccount"/>
      // RDF: fibo-loan-ln-ln:LoanSpecificCustomerAccount
  }
  item def <'loan-to-value ratio'> LoanToValueRatio specializes Currency_Amount::PercentageMonetaryAmount {
  	doc /* ratio, expressed as a percentage, between the principal amount of the loan and the appraised value of the asset securing the financing */
  	// skos:definition xml:lang="en" (Loans.rdf)
  	// rdfs:label xml:lang="en" (Loans.rdf)
  	// RDF: fibo-loan-ln-ln:LoanToValueRatio
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-cur;PercentageMonetaryAmount"/>
  }
  item def <'open-end credit'> OpenEndCredit specializes Debt::CreditAgreement {
  	doc /* credit agreement that may be extended up to an agreed credit limit and paid down at any time within the period of the line, if any, and on which interest is charged only on the outstanding balance */
  	// skos:definition xml:lang="en" (Loans.rdf)
  	// rdfs:label xml:lang="en" (Loans.rdf)
  	// RDF: fibo-loan-ln-ln:OpenEndCredit
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;CreditAgreement"/>
  }
  item def <'ownership interest'> OwnershipInterest specializes FiBO_FND_ExternalStubs::Classifier {
  	doc /* classifier indicating the nature of the applicant's or borrower's ownership or leasehold interest in an asset used as collateral for the loan */
  	// skos:definition xml:lang="en" (Loans.rdf)
  	// rdfs:label xml:lang="en" (Loans.rdf)
  	// RDF: fibo-loan-ln-ln:OwnershipInterest
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
  }
  item def <'payment history'> PaymentHistory specializes Clients_and_Accounts::TransactionRecord {
  	doc /* record of actual payments of principal, interest, and other related amounts made by a borrower to a lender or servicer in order to fulfill their re-payment obligation */
  	// skos:definition xml:lang="en" (Loans.rdf)
  	// rdfs:label xml:lang="en" (Loans.rdf)
  	// RDF: fibo-loan-ln-ln:PaymentHistory
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-caa;TransactionRecord"/>
  }
  item def <'pre-payment terms'> PrepaymentTerms specializes Debt::PrincipalRepaymentTerms {
  	doc /* principal repayment terms related to payment of the loan prior to maturity */
  	// skos:definition xml:lang="en" (Loans.rdf)
  	// rdfs:label xml:lang="en" (Loans.rdf)
  	// RDF: fibo-loan-ln-ln:PrepaymentTerms
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;PrincipalRepaymentTerms"/>
  }
  item def <'principal repayment terms'> PrincipalRepaymentTerms specializes Debt::DebtTerms {
  	doc /* credit agreement in which the loan principal cannot be increased after funds are dispersed in full when the loan closes */
  	// skos:definition xml:lang="en" (Loans.rdf)
  	// rdfs:label xml:lang="en" (Loans.rdf)
  	// RDF: fibo-loan-ln-ln:PrincipalRepaymentTerms
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;DebtTerms"/>
  }
  item def <'secured loan'> SecuredLoan specializes Loan {
  	doc /* secondary or lower position in the order of seniority in which the law recognizes lenders&apos; claims against a property */
  	// skos:definition xml:lang="en" (Loans.rdf)
  	// rdfs:label xml:lang="en" (Loans.rdf)
  	// RDF: fibo-loan-ln-ln:SecuredLoan
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-ln-ln;Loan"/>
  }
  item def <'security agreement'> SecurityAgreement specializes Debt::CollateralAgreement {
  	doc /* portion of a credit facility extended to the borrower for some purpose, possibly per some schedule specified in the facility */
  	// skos:definition xml:lang="en" (Loans.rdf)
  	// rdfs:label xml:lang="en" (Loans.rdf)
  	// RDF: fibo-loan-ln-ln:SecurityAgreement
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;CollateralAgreement"/>
  }
  item def <'servicer'> Servicer specializes Financial_Products_and_Services::FinancialServiceProvider, Contracts::ContractThirdParty {
  	doc /* any actor within the DTCC Underwriting Process or any other Underwriting Process. Note: At present all these Actors (e.g. Underwriter) are defined simply as Process Actors and not as specializations of this type of Actor. Therefore this Actor is a Union of all of those. */
  	// skos:definition xml:lang="en" (Loans.rdf)
  	// rdfs:label xml:lang="en" (Loans.rdf)
  	// rdfs:label xml:lang="en" (Loans.rdf)
  	// RDF: fibo-loan-ln-ln:Servicer
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;FinancialServiceProvider"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractThirdParty"/>
  }
  item def <'total outstanding principal'> TotalOutstandingPrincipal specializes Debt::Principal {
  	doc /* indicates whether the contractual terms include or would have included repayment of the outstanding principal sum at the end of a loan period, prior to which only partial or no payments were made on the principal */
  	// skos:definition xml:lang="en" (Loans.rdf)
  	// rdfs:label xml:lang="en" (Loans.rdf)
  	// RDF: fibo-loan-ln-ln:TotalOutstandingPrincipal
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;Principal"/>
  }
  item def <'unsecured loan'> UnsecuredLoan specializes Loan {
  	doc /* loan granted based on the strength of the borrower's credit history or reputation in the community */
  	// skos:definition xml:lang="en" (Loans.rdf)
  	// rdfs:label xml:lang="en" (Loans.rdf)
  	// RDF: fibo-loan-ln-ln:UnsecuredLoan
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-ln-ln;Loan"/>
  }
}

// --- fibo-loan-ln-reg ---
