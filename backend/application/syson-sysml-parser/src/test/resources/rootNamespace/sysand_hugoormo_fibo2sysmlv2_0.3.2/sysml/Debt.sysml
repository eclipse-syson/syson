// ===================================================================
// Debt.sysml
// Source-parity extracted from: FiBO_FBC_DebtAndEquities.sysml
// Primary source module: Debt.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Financial Business and Commerce FBC/DebtAndEquities
// Structural note: translated as a split file from FiBO_FBC_DebtAndEquities umbrella.
// ===================================================================
package Debt {
  doc/* This ontology defines concepts that are common to all debt instruments, such as debt, borrower, lender, debtor, creditor, interest, principal, and the like. It is designed to be used by various other FIBO specifications, including but not limited to SEC/Debt and LOAN. (source: dct:abstract, Debt.rdf) */
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

    item def <'accrual'> Accrual specializes FiBO_FND_ExternalStubs::Role {
      doc /* the process of accumulating interest or other income that has been earned but not paid */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-rlcmp;Role"/>
    	// skos:definition xml:lang="en" (Debt.rdf)
    	// rdfs:label xml:lang="en" (Debt.rdf)
      // RDF: fibo-fbc-dae-dbt:Accrual
      // subClassOf: Role - external CMNS
    }

    item def <'accrued interest'> AccruedInterest specializes Interest {
      doc /* amount of interest that has been incurred, as of a specific date, on a loan or other financial obligation but has not yet been paid out */
      // skos:definition xml:lang="en" (Debt.rdf)
      // rdfs:label xml:lang="en" (Debt.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;Interest"/>
      // RDF: fibo-fbc-dae-dbt:AccruedInterest
    }

    item def <'amortization'> Amortization specializes FiBO_FND_ExternalStubs::Role {
      doc /* the process of reduction of debt or other costs through periodic charges to assets or liabilities, such as through principal payments on mortgages */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-rlcmp;Role"/>
    	// skos:definition xml:lang="en" (Debt.rdf)
    	// rdfs:label xml:lang="en" (Debt.rdf)
      // RDF: fibo-fbc-dae-dbt:Amortization
      // subClassOf: Role - external CMNS
    }

    item def <'amortization schedule'> AmortizationSchedule specializes Payments_and_Schedules::PaymentSchedule, ProjectedContractEventSchedule {
      doc /* schedule of periodic payments (repayment installments) that specify changes in the balance of the debt over time */
      // skos:definition xml:lang="en" (Debt.rdf)
      // rdfs:label xml:lang="en" (Debt.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-psch;PaymentSchedule"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;ProjectedContractEventSchedule"/>
      // RDF: fibo-fbc-dae-dbt:AmortizationSchedule
    }

    item def <'borrower'> Borrower specializes Contracts::ContractParty, Debtor, Products_and_Services::Customer{
      doc /* party to a credit agreement that is obligated to repay the amount borrowed (principal) with interest and other fees according to the terms of the instrument */
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-pas;Customer"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractParty"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;Debtor"/>
    	// skos:definition xml:lang="en" (Debt.rdf)
    	// rdfs:label xml:lang="en" (Debt.rdf)
      // RDF: fibo-fbc-dae-dbt:Borrower
      // subClassOf also: Customer - external CMNS
    }

    item def <'borrower identification scheme'> BorrowerIdentificationScheme specializes Parties::PartyRoleIdentificationScheme {
      doc /* system for allocating identifiers to borrowers */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pty-pty;PartyRoleIdentificationScheme"/>
    	// skos:definition xml:lang="en" (Debt.rdf)
    	// rdfs:label xml:lang="en" (Debt.rdf)
      // RDF: fibo-fbc-dae-dbt:BorrowerIdentificationScheme
      // subClassOf: PartyRoleIdentificationScheme - from FIBO_FND_Parties
    }

    item def <'borrower identifier'> BorrowerIdentifier specializes Parties::PartyRoleIdentifier {
      doc /* sequence of characters, capable of uniquely identifying a borrower */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pty-pty;PartyRoleIdentifier"/>
    	// skos:definition xml:lang="en" (Debt.rdf)
    	// rdfs:label xml:lang="en" (Debt.rdf)
      // RDF: fibo-fbc-dae-dbt:BorrowerIdentifier
      // subClassOf: PartyRoleIdentifier - from FIBO_FND_Parties
    }

    item def <'borrowing capacity'> BorrowingCapacity specializes Currency_Amount::MonetaryAmount {
      doc /* upper bound on the total amount of money that a lender believes a party has the ability to repay an obligation when due, as of some point in time */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-cur;MonetaryAmount"/>
    	// skos:definition xml:lang="en" (Debt.rdf)
    	// rdfs:label xml:lang="en" (Debt.rdf)
      // RDF: fibo-fbc-dae-dbt:BorrowingCapacity
      // subClassOf: MonetaryAmount - from FIBO_FND_Accounting (CurrencyAmount)
    }

    item def <'capital lease'> CapitalLease specializes Lease {
      doc /* lease that must be reflected on an organization's balance sheet as an asset and as a corresponding liability */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;Lease"/>
    	// skos:definition xml:lang="en" (Debt.rdf)
    	// rdfs:label xml:lang="en" (Debt.rdf)
      // RDF: fibo-fbc-dae-dbt:CapitalLease
    }

    item def <'collateral'> Collateral specializes FiBO_FND_ExternalStubs::Undergoer {
      doc /* something pledged as security to ensure fulfillment of an obligation to another party, to lend money, extend credit, or provision securities */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;Undergoer"/>
    	// skos:definition xml:lang="en" (Debt.rdf)
    	// rdfs:label xml:lang="en" (Debt.rdf)
      // RDF: fibo-fbc-dae-dbt:Collateral
      // subClassOf: Undergoer - external CMNS
    }

    item def <'collateral agreement'> CollateralAgreement specializes Contracts::WrittenContract {
      doc /* collateral agreement */
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;WrittenContract"/>
    	// skos:definition xml:lang="en" (Debt.rdf)
    	// rdfs:label xml:lang="en" (Debt.rdf)
      // RDF: fibo-fbc-dae-dbt:CollateralAgreement
    }

    item def <'collateral value as of date'> CollateralValueAsOfDate specializes Assessments::AppraisedValue {
      doc /* appraised value of the collateral for an obligation as of a given date */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-asmt;AppraisedValue"/>
    	// skos:definition xml:lang="en" (Debt.rdf)
    	// rdfs:label xml:lang="en" (Debt.rdf)
      // RDF: fibo-fbc-dae-dbt:CollateralValueAsOfDate
      // subClassOf: AppraisedValue - external CMNS/FND
    }

    item def <'committed credit facility'> CommittedCreditFacility specializes CreditFacility {
      doc /* credit facility that is a confirmed source of financing for the borrower, as long as the borrower meets the conditions of the agreement */
      // skos:definition xml:lang="en" (Debt.rdf)
      // rdfs:label xml:lang="en" (Debt.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;CreditFacility"/>
      // RDF: fibo-fbc-dae-dbt:CommittedCreditFacility
    }

    item def <'committed sub-facility'> CommittedSubFacility specializes SubFacility {
      doc /* contractually committed portion of a credit facility that is available to the borrower and may be associated with some specific collateral */
      // skos:definition xml:lang="en" (Debt.rdf)
      // rdfs:label xml:lang="en" (Debt.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;SubFacility"/>
      // RDF: fibo-fbc-dae-dbt:CommittedSubFacility
    }

    item def <'credit agreement'> CreditAgreement specializes Contracts::WrittenContract {
      doc /* contractual agreement in which a debtor receives something of value and typically agrees to repay the creditor by some date in the future, in some form (e.g., cash, securities, etc.), generally with interest */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;WrittenContract"/>
    	// skos:definition xml:lang="en" (Debt.rdf)
    	// rdfs:label xml:lang="en" (Debt.rdf)
      // RDF: fibo-fbc-dae-dbt:CreditAgreement
      // subClassOf: WrittenContract - from FIBO_FND_Agreements (AgreementsPackage)
    }

    item def <'credit agreement repaid at maturity'> CreditAgreementRepaidAtMaturity specializes CreditAgreement {
      doc /* credit agreement in which accrued interest may be periodically repaid or paid at maturity, but principal is paid at maturity */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;CreditAgreement"/>
    	// skos:definition xml:lang="en" (Debt.rdf)
    	// rdfs:label xml:lang="en" (Debt.rdf)
      // RDF: fibo-fbc-dae-dbt:CreditAgreementRepaidAtMaturity
    }

    item def <'credit agreement repaid periodically'> CreditAgreementRepaidPeriodically specializes CreditAgreement {
      doc /* credit agreement in which the principal and accrued interest may be periodically repaid or exchanged */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;CreditAgreement"/>
    	// skos:definition xml:lang="en" (Debt.rdf)
    	// rdfs:label xml:lang="en" (Debt.rdf)
      // RDF: fibo-fbc-dae-dbt:CreditAgreementRepaidPeriodically
    }

    item def <'credit enhancement agreement'> CreditEnhancementAgreement specializes CollateralAgreement {
      doc /* collateral agreement that governs the exchange of collateral between parties to mitigate counterparty credit risk */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;CollateralAgreement"/>
    	// skos:definition xml:lang="en" (Debt.rdf)
    	// rdfs:label xml:lang="en" (Debt.rdf)
      // RDF: fibo-fbc-dae-dbt:CreditEnhancementAgreement
    }

    item def <'credit enhancement beneficiary'> CreditEnhancementBeneficiary specializes Agreements::Beneficiary, Contracts::Counterparty {
      doc /* party that benefits from the collateral or guarantee established under the agreement, i.e., that is protected against counterparty credit risk because the collateral or guarantee serves as security for the obligation(s) owed to them */
      // skos:definition xml:lang="en" (Debt.rdf)
      // rdfs:label xml:lang="en" (Debt.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-agr;Beneficiary"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;Counterparty"/>
      // RDF: fibo-fbc-dae-dbt:CreditEnhancementBeneficiary
      // subClassOf: Beneficiary, Counterparty - from FIBO_FND_Agreements (AgreementsPackage)
    }

    item def <'credit facility'> CreditFacility specializes CreditAgreementRepaidPeriodically {
      doc /* credit agreement that allows the borrower to periodically take out money over an extended period of time rather than reapplying for a loan every time they need funds */
      // skos:definition xml:lang="en" (Debt.rdf)
      // rdfs:label xml:lang="en" (Debt.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;CreditAgreementRepaidPeriodically"/>
      // RDF: fibo-fbc-dae-dbt:CreditFacility
    }

    item def <'creditor'> Creditor specializes Agreements::Obligee {
      doc /* a party to whom an obligation, such as an amount of money, or good, or performance of some service exists */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-agr;Obligee"/>
    	// skos:definition xml:lang="en" (Debt.rdf)
    	// rdfs:label xml:lang="en" (Debt.rdf)
      // RDF: fibo-fbc-dae-dbt:Creditor
      // subClassOf: Obligee - from FIBO_FND_Agreements (AgreementsPackage)
    }

    item def <'day-count convention'> DayCountConvention specializes Business_Dates::BusinessRecurrenceIntervalConvention, Analytics::Expression{
      doc /* a business recurrence interval convention that is used to calculate the number of days in an interest payment, which applies to the amount of accrued interest or the present value for debt instruments */
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-qtu;Expression"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-bd;BusinessRecurrenceIntervalConvention"/>
    	// skos:definition xml:lang="en" (Debt.rdf)
    	// rdfs:label xml:lang="en" (Debt.rdf)
      // RDF: fibo-fbc-dae-dbt:DayCountConvention
      // subClassOf: BusinessRecurrenceIntervalConvention - from FIBO_FND_DatesAndTimes
      // subClassOf also: Expression - external CMNS
    }

    item def <'debt'> Debt specializes Agreements::Commitment {
      doc /* obligation to pay something, such as an amount of money, good, service, or instrument */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-agr;Commitment"/>
    	// skos:definition xml:lang="en" (Debt.rdf)
    	// rdfs:label xml:lang="en" (Debt.rdf)
      // RDF: fibo-fbc-dae-dbt:Debt
      // subClassOf: Commitment - from FIBO_FND_Agreements (AgreementsPackage)
    }

    item def <'debtor'> Debtor specializes MuniIssuance::Obligor {
      doc /* a party that owes a debt or other obligation to another party */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-agr;Obligor"/>
    	// skos:definition xml:lang="en" (Debt.rdf)
    	// rdfs:label xml:lang="en" (Debt.rdf)
      // RDF: fibo-fbc-dae-dbt:Debtor
      // subClassOf: Obligor - from FIBO_FND_Agreements (AgreementsPackage)
    }

    item def <'debt terms'> DebtTerms specializes Contracts::ContractualCommitment {
      doc /* contract terms that specify the formal rights and obligations of borrower and lender under a contract in which funds are lent from the one party to the other */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractualCommitment"/>
    	// skos:definition xml:lang="en" (Debt.rdf)
    	// rdfs:label xml:lang="en" (Debt.rdf)
      // RDF: fibo-fbc-dae-dbt:DebtTerms
      // subClassOf: ContractualCommitment - from FIBO_FND_Agreements (AgreementsPackage)
    }

    item def <'explicit contract event schedule'> ExplicitContractEventSchedule specializes Financial_Dates::AdHocSchedule {
      doc /* schedule of events, including but not limited to payment events, rate reset events and others that will occur over the lifetime of the credit agreement */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-fd;AdHocSchedule"/>
    	// skos:definition xml:lang="en" (Debt.rdf)
    	// rdfs:label xml:lang="en" (Debt.rdf)
      // RDF: fibo-fbc-dae-dbt:ExplicitContractEventSchedule
      // subClassOf: AdHocSchedule - from FIBO_FND_DatesAndTimes
    }

    item def <'fixed interest rate'> FixedInterestRate specializes Currency_Amount::InterestRate {
      doc /* interest rate that does not fluctuate over the lifetime of a loan or other debt instrument */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-cur;InterestRate"/>
    	// skos:definition xml:lang="en" (Debt.rdf)
    	// rdfs:label xml:lang="en" (Debt.rdf)
      // RDF: fibo-fbc-dae-dbt:FixedInterestRate
      // subClassOf: InterestRate - from FIBO_FND_Accounting (CurrencyAmount)
    }

    item def <'floating interest rate'> FloatingInterestRate specializes VariableInterestRate {
      doc /* variable interest rate that is based on a specific index or benchmark rate */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;VariableInterestRate"/>
    	// skos:definition xml:lang="en" (Debt.rdf)
    	// rdfs:label xml:lang="en" (Debt.rdf)
      // RDF: fibo-fbc-dae-dbt:FloatingInterestRate
    }

    item def <'full amortization'> FullAmortization specializes Amortization {
      doc /* amortization in which the very last payment (which, if the schedule was calculated correctly, should be equal to all others) pays off all remaining principal and interest on the loan */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;Amortization"/>
    	// skos:definition xml:lang="en" (Debt.rdf)
    	// rdfs:label xml:lang="en" (Debt.rdf)
      // RDF: fibo-fbc-dae-dbt:FullAmortization
    }

    item def <'interest'> Interest specializes Currency_Amount::MonetaryAmount {
      doc /* the cost of using credit, or another's money, expressed as a rate per period of time, payable by a debtor to a creditor in consideration of the credit extended to the debtor */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-cur;MonetaryAmount"/>
    	// skos:definition xml:lang="en" (Debt.rdf)
    	// rdfs:label xml:lang="en" (Debt.rdf)
      // RDF: fibo-fbc-dae-dbt:Interest
      // subClassOf: MonetaryAmount - from FIBO_FND_Accounting (CurrencyAmount)
    }

    item def <'interest calculation'> InterestCalculation specializes Financial_Products_and_Services::ContractLifecycleEventOccurrence, FiBOOccurrences::Calculation{
      doc /* event reflecting the calculation of interest */
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-oc;Calculation"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;ContractLifecycleEventOccurrence"/>
    	// skos:definition xml:lang="en" (Debt.rdf)
    	// rdfs:label xml:lang="en" (Debt.rdf)
      // RDF: fibo-fbc-dae-dbt:InterestCalculation
      // subClassOf also: Calculation - external CMNS
    }

    item def <'interest calculation schedule'> InterestCalculationSchedule specializes ProjectedContractEventSchedule {
      doc /* regular, contract-specific schedule including the dates on which interest is calculated */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;ProjectedContractEventSchedule"/>
    	// skos:definition xml:lang="en" (Debt.rdf)
    	// rdfs:label xml:lang="en" (Debt.rdf)
      // RDF: fibo-fbc-dae-dbt:InterestCalculationSchedule
    }

    item def <'interest payment'> InterestPayment specializes Payments_and_Schedules::Payment {
      doc /* event reflecting the actual payment of interest */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-psch;Payment"/>
    	// skos:definition xml:lang="en" (Debt.rdf)
    	// rdfs:label xml:lang="en" (Debt.rdf)
      // RDF: fibo-fbc-dae-dbt:InterestPayment
      // subClassOf: Payment - from FIBO_FND_ProductsAndServices (ProductsServicesPackage)
    }

    item def <'interest payment schedule'> InterestPaymentSchedule specializes Payments_and_Schedules::PaymentSchedule, ProjectedContractEventSchedule {
      doc /* regular, contract-specific schedule including the dates on which interest is due to be paid */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-psch;PaymentSchedule"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;ProjectedContractEventSchedule"/>
    	// skos:definition xml:lang="en" (Debt.rdf)
    	// rdfs:label xml:lang="en" (Debt.rdf)
      // RDF: fibo-fbc-dae-dbt:InterestPaymentSchedule
    }

    item def <'interest payment terms'> InterestPaymentTerms specializes DebtTerms {
      doc /* contract terms for payment of interest on a debt */
      // skos:definition xml:lang="en" (Debt.rdf)
      // rdfs:label xml:lang="en" (Debt.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;DebtTerms"/>
      // RDF: fibo-fbc-dae-dbt:InterestPaymentTerms
    }

    item def <'interest rate reset'> InterestRateReset specializes InterestCalculation {
      doc /* event reflecting a potential adjustment to an interest rate, typically corresponding to a change in the underlying benchmark interest rate or index specified in the contract */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;InterestCalculation"/>
    	// skos:definition xml:lang="en" (Debt.rdf)
    	// rdfs:label xml:lang="en" (Debt.rdf)
      // RDF: fibo-fbc-dae-dbt:InterestRateReset
    }

    item def <'interest rate reset schedule'> InterestRateResetSchedule specializes ProjectedContractEventSchedule {
      doc /* regular, contract-specific schedule including the dates on which a rate reset, and corresponding actual rate, is recalculated */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;ProjectedContractEventSchedule"/>
    	// skos:definition xml:lang="en" (Debt.rdf)
    	// rdfs:label xml:lang="en" (Debt.rdf)
      // RDF: fibo-fbc-dae-dbt:InterestRateResetSchedule
    }

    item def <'interest rate setting event'> InterestRateSettingEvent specializes InterestCalculation {
      doc /* event on which an initial rate for a given contract is set, which may be relative the the occurrence of some other contract lifecycle event, such as the execution date */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;InterestCalculation"/>
    	// skos:definition xml:lang="en" (Debt.rdf)
    	// rdfs:label xml:lang="en" (Debt.rdf)
      // RDF: fibo-fbc-dae-dbt:InterestRateSettingEvent
    }

    item def <'lease'> Lease specializes CreditAgreementRepaidPeriodically {
      doc /* credit agreement permitting the use of real estate, equipment or another asset, such as a vehicle, by the owner of that asset (the lessor) to a user (the lessee) for a specific period of time in return for payment as specified in the agreement */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;CreditAgreementRepaidPeriodically"/>
    	// skos:definition xml:lang="en" (Debt.rdf)
    	// rdfs:label xml:lang="en" (Debt.rdf)
      // RDF: fibo-fbc-dae-dbt:Lease
    }

    item def <'lender'> Lender specializes Contracts::ContractParty, Creditor {
      doc /* a party that extends credit or money to a borrower with the expectation of being repaid, usually with interest */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractParty"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;Creditor"/>
    	// skos:definition xml:lang="en" (Debt.rdf)
    	// rdfs:label xml:lang="en" (Debt.rdf)
      // RDF: fibo-fbc-dae-dbt:Lender
    }

    item def <'managed interest rate'> ManagedInterestRate specializes VariableInterestRate {
      doc /* variable interest rate charged by a financial institution for borrowing that is not prescribed as a margin over base rate but is set from time to time by the institution */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;VariableInterestRate"/>
    	// skos:definition xml:lang="en" (Debt.rdf)
    	// rdfs:label xml:lang="en" (Debt.rdf)
      // RDF: fibo-fbc-dae-dbt:ManagedInterestRate
    }

    item def <'motor vehicle lease'> MotorVehicleLease specializes Lease {
      doc /* lease of a motor vehicle for a fixed period of time at an agreed amount of money */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;Lease"/>
    	// skos:definition xml:lang="en" (Debt.rdf)
    	// rdfs:label xml:lang="en" (Debt.rdf)
      // RDF: fibo-fbc-dae-dbt:MotorVehicleLease
    }

    item def <'negative amortization'> NegativeAmortization specializes Amortization {
      doc /* amortization in which the payments made do not cover the interest due */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;Amortization"/>
    	// skos:definition xml:lang="en" (Debt.rdf)
    	// rdfs:label xml:lang="en" (Debt.rdf)
      // RDF: fibo-fbc-dae-dbt:NegativeAmortization
    }

    item def <'non-physical collateral'> NonPhysicalCollateral specializes Collateral {
      doc /* asset pledged as collateral that is a financial asset, rather than physical asset */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;Collateral"/>
    	// skos:definition xml:lang="en" (Debt.rdf)
    	// rdfs:label xml:lang="en" (Debt.rdf)
      // RDF: fibo-fbc-dae-dbt:NonPhysicalCollateral
    }

    item def <'partial amortization'> PartialAmortization specializes Amortization {
      doc /* amortization in which the very last payment the last payment due may be a large balloon payment of all remaining principal and interest */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;Amortization"/>
    	// skos:definition xml:lang="en" (Debt.rdf)
    	// rdfs:label xml:lang="en" (Debt.rdf)
      // RDF: fibo-fbc-dae-dbt:PartialAmortization
    }

    item def <'physical collateral'> PhysicalCollateral specializes Collateral {
      doc /* asset pledged as collateral that has a material form, i.e., is a physical asset of the obligor */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;Collateral"/>
    	// skos:definition xml:lang="en" (Debt.rdf)
    	// rdfs:label xml:lang="en" (Debt.rdf)
      // RDF: fibo-fbc-dae-dbt:PhysicalCollateral
    }

    item def <'principal'> Principal specializes FiBO_FND_ExternalStubs::Aspect {
      doc /* principal */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Aspect"/>
    	// skos:definition xml:lang="en" (Debt.rdf)
    	// rdfs:label xml:lang="en" (Debt.rdf)
      // RDF: fibo-fbc-dae-dbt:Principal
      // subClassOf: Aspect - external CMNS
    }

    item def <'principal payment'> PrincipalPayment specializes Payments_and_Schedules::Payment {
      doc /* event reflecting the actual payment of some amount of the principal of a debt */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-psch;Payment"/>
    	// skos:definition xml:lang="en" (Debt.rdf)
    	// rdfs:label xml:lang="en" (Debt.rdf)
      // RDF: fibo-fbc-dae-dbt:PrincipalPayment
      // subClassOf: Payment - from FIBO_FND_ProductsAndServices (ProductsServicesPackage)
    }

    item def <'principal payment schedule'> PrincipalPaymentSchedule specializes Payments_and_Schedules::PaymentSchedule, ProjectedContractEventSchedule {
      doc /* regular, contract-specific schedule including the dates on which some percentage or all of the principal is due to be (re)paid */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-psch;PaymentSchedule"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;ProjectedContractEventSchedule"/>
    	// skos:definition xml:lang="en" (Debt.rdf)
    	// rdfs:label xml:lang="en" (Debt.rdf)
      // RDF: fibo-fbc-dae-dbt:PrincipalPaymentSchedule
    }

    item def <'principal repayment terms'> PrincipalRepaymentTerms specializes DebtTerms {
      doc /* contract terms that specify requirements for repayment of the principal */
      // skos:definition xml:lang="en" (Debt.rdf)
      // rdfs:label xml:lang="en" (Debt.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;DebtTerms"/>
      // RDF: fibo-fbc-dae-dbt:PrincipalRepaymentTerms
    }

    item def <'projected contract event schedule'> ProjectedContractEventSchedule specializes Financial_Dates::RegularSchedule {
      doc /* schedule of events, including but not limited to anticipated payment events, rate reset events and others that are expected to occur over the lifetime of the contract */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-fd;RegularSchedule"/>
    	// skos:definition xml:lang="en" (Debt.rdf)
    	// rdfs:label xml:lang="en" (Debt.rdf)
      // RDF: fibo-fbc-dae-dbt:ProjectedContractEventSchedule
      // subClassOf: RegularSchedule - from FIBO_FND_DatesAndTimes (FinancialDates)
    }

    item def <'rate reset time of day'> RateResetTimeOfDay specializes FiBO_FND_ExternalStubs::TimeOfDay {
      doc /* time of day that an interest rate is reset, as indicated by some interest rate authority or market data provider */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-dt;TimeOfDay"/>
    	// skos:definition xml:lang="en" (Debt.rdf)
    	// rdfs:label xml:lang="en" (Debt.rdf)
      // RDF: fibo-fbc-dae-dbt:RateResetTimeOfDay
      // subClassOf: TimeOfDay - external CMNS
    }

    item def <'retail credit facility'> RetailCreditFacility specializes CommittedCreditFacility {
      doc /* credit facility that is a loan or line of credit used by retailers and real estate companies */
      // skos:definition xml:lang="en" (Debt.rdf)
      // rdfs:label xml:lang="en" (Debt.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;CommittedCreditFacility"/>
      // RDF: fibo-fbc-dae-dbt:RetailCreditFacility
    }

    item def <'revolving line of credit'> RevolvingLineOfCredit specializes CommittedCreditFacility {
      doc /* credit facility that enables the borrower to withdraw funds, repay, and withdraw again */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;CommittedCreditFacility"/>
    	// skos:definition xml:lang="en" (Debt.rdf)
    	// rdfs:label xml:lang="en" (Debt.rdf)
      // RDF: fibo-fbc-dae-dbt:RevolvingLineOfCredit
    }

    item def <'security agreement'> SecurityAgreement specializes CollateralAgreement {
      doc /* collateral agreement that grants a financial interest in some collateral to a party that is not an owner of that collateral, specifying terms including relative duties and rights, over and above those specified in the primary contract, regarding the disposition of the asset used as collateral */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;CollateralAgreement"/>
    	// skos:definition xml:lang="en" (Debt.rdf)
    	// rdfs:label xml:lang="en" (Debt.rdf)
      // RDF: fibo-fbc-dae-dbt:SecurityAgreement
    }

    item def <'sub-facility'> SubFacility specializes Contracts::WrittenContract {
      doc /* portion of a credit facility extended to the borrower for some purpose, possibly per some schedule specified in the facility */
      // skos:definition xml:lang="en" (Debt.rdf)
      // rdfs:label xml:lang="en" (Debt.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;WrittenContract"/>
      // RDF: fibo-fbc-dae-dbt:SubFacility
      // subClassOf: WrittenContract - from FIBO_FND_Agreements (AgreementsPackage)
    }

    item def <'uncommitted credit facility'> UncommittedCreditFacility specializes CreditFacility {
      doc /* credit facility that has yet to be confirmed as a source of financing for the borrower */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;CreditFacility"/>
    	// skos:definition xml:lang="en" (Debt.rdf)
    	// rdfs:label xml:lang="en" (Debt.rdf)
      // RDF: fibo-fbc-dae-dbt:UncommittedCreditFacility
    }

    item def <'uncommitted sub-facility'> UncommittedSubFacility specializes SubFacility {
      doc /* sub-facility that has yet to be confirmed as a source of financing for the borrower */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;SubFacility"/>
    	// skos:definition xml:lang="en" (Debt.rdf)
    	// rdfs:label xml:lang="en" (Debt.rdf)
      // RDF: fibo-fbc-dae-dbt:UncommittedSubFacility
    }

    item def <'variable interest rate'> VariableInterestRate specializes Currency_Amount::InterestRate {
      doc /* an interest rate that is allowed to vary over the maturity of a loan or other debt instrument */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-cur;InterestRate"/>
    	// skos:definition xml:lang="en" (Debt.rdf)
    	// rdfs:label xml:lang="en" (Debt.rdf)
      // RDF: fibo-fbc-dae-dbt:VariableInterestRate
      // subClassOf: InterestRate - from FIBO_FND_Accounting (CurrencyAmount)
    }

}

// --- fibo-fbc-dae-gty (Guaranty) ---

