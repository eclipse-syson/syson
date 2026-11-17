// ===================================================================
// CreditEvents.sysml
// Source-parity extracted from: FiBO_FBC_DebtAndEquities.sysml
// Primary source module: CreditEvents.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Financial Business and Commerce FBC/DebtAndEquities
// Structural note: translated as a split file from FiBO_FBC_DebtAndEquities umbrella.
// ===================================================================
package Credit_Events {
  doc/* This ontology defines a range of credit events, that is events in which some payment or payments are not made. These include credit events relating to a specific debt obligation and events relating to the business entity as a whole. Note: the events defined herein are primarily business rather than consumer oriented, and are specified fairly generally. Many credit events are jurisdiction-specific, such as Chapter 11 restructuring and Chapter 7 bankruptcy in the United States. This ontology is designed to facilitate jurisdiction and instrument-specific extensions as needed. (source: dct:abstract, CreditEvents.rdf) */
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

    item def <'bankruptcy'> Bankruptcy specializes EntitySpecificCreditEvent {
      doc /* credit event involving a change in state or condition in which a party becomes insolvent */
      // skos:definition xml:lang="en" (CreditEvents.rdf)
      // rdfs:label xml:lang="en" (CreditEvents.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-cre;EntitySpecificCreditEvent"/>
      // RDF: fibo-fbc-dae-cre:Bankruptcy
    }

    item def <'credit event'> CreditEvent specializes FiBO_FND_ExternalStubs::FiOccurrence, FiBOOccurrences::FiBOOccurrence{
      doc /* event signifying a sudden change in credit standing, such as bankruptcy or a violation of a bond indenture or loan agreement, that raises doubts about the party's ability to meet current or future obligations */
      // skos:definition xml:lang="en" (CreditEvents.rdf)
      // rdfs:label xml:lang="en" (CreditEvents.rdf)
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-oc;FiBOOccurrence"/>
      // RDF: fibo-fbc-dae-cre:CreditEvent
      // subClassOf: FiBOOccurrence - external CMNS
    }

    item def <'cross default provision'> CrossDefaultProvision specializes Contracts::ConditionPrecedent {
      doc /* contract provision that triggers a default under one contract if the borrower defaults on another related obligation */
      // skos:definition xml:lang="en" (CreditEvents.rdf)
      // rdfs:label xml:lang="en" (CreditEvents.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ConditionPrecedent"/>
      // RDF: fibo-fbc-dae-cre:CrossDefaultProvision
      // subClassOf: ConditionPrecedent - from FIBO_FND_Agreements (AgreementsPackage)
    }

    item def <'default event'> DefaultEvent specializes Credit_Default_Swaps::ObligationSpecificCreditEvent {
      doc /* credit event representing a failure to meet a contractual obligation, such as failure to repay a debt including interest or principal on a loan or security */
      // skos:definition xml:lang="en" (CreditEvents.rdf)
      // rdfs:label xml:lang="en" (CreditEvents.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-cre;ObligationSpecificCreditEvent"/>
      // RDF: fibo-fbc-dae-cre:DefaultEvent
    }

    item def <'distressed ratings downgrade'> DistressedRatingsDowngrade specializes Downgrade {
      doc /* credit event triggered when the credit rating of an obligation is downgraded to a distressed debt level */
      // skos:definition xml:lang="en" (CreditEvents.rdf)
      // rdfs:label xml:lang="en" (CreditEvents.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-cre;Downgrade"/>
      // RDF: fibo-fbc-dae-cre:DistressedRatingsDowngrade
    }

    item def <'downgrade'> Downgrade specializes CreditEvent {
      doc /* credit event triggered when the credit rating of a party or obligation is lowered */
      // skos:definition xml:lang="en" (CreditEvents.rdf)
      // rdfs:label xml:lang="en" (CreditEvents.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-cre;CreditEvent"/>
      // RDF: fibo-fbc-dae-cre:Downgrade
    }

    item def <'entity-specific credit event'> EntitySpecificCreditEvent specializes CreditEvent {
      doc /* credit event that applies to a given legal person */
      // skos:definition xml:lang="en" (CreditEvents.rdf)
      // rdfs:label xml:lang="en" (CreditEvents.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-cre;CreditEvent"/>
      // RDF: fibo-fbc-dae-cre:EntitySpecificCreditEvent
    }

    item def <'failure to pay'> FailureToPay specializes DefaultEvent {
      doc /* default event that is triggered following any applicable grace period in which a payment obligation is missed */
      // skos:definition xml:lang="en" (CreditEvents.rdf)
      // rdfs:label xml:lang="en" (CreditEvents.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-cre;DefaultEvent"/>
      // RDF: fibo-fbc-dae-cre:FailureToPay
    }

    item def <'failure to pay interest'> FailureToPayInterest specializes FailureToPay {
      doc /* default event that where either an expected interest payment is missed altogether or the amount paid is less than the required amount */
      // skos:definition xml:lang="en" (CreditEvents.rdf)
      // rdfs:label xml:lang="en" (CreditEvents.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-cre;FailureToPay"/>
      // RDF: fibo-fbc-dae-cre:FailureToPayInterest
    }

    item def <'failure to pay principal'> FailureToPayPrincipal specializes FailureToPay {
      doc /* default event that where either an expected principal payment is missed altogether or the amount paid is less than the required amount */
      // skos:definition xml:lang="en" (CreditEvents.rdf)
      // rdfs:label xml:lang="en" (CreditEvents.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-cre;FailureToPay"/>
      // RDF: fibo-fbc-dae-cre:FailureToPayPrincipal
    }

    item def <'filing for bankruptcy'> FilingForBankruptcy specializes EntitySpecificCreditEvent {
      doc /* credit event that involves a request to a court to be recognized as bankrupt */
      // skos:definition xml:lang="en" (CreditEvents.rdf)
      // rdfs:label xml:lang="en" (CreditEvents.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-cre;EntitySpecificCreditEvent"/>
      // RDF: fibo-fbc-dae-cre:FilingForBankruptcy
    }

    item def <'hard credit event'> HardCreditEvent specializes DefaultEvent {
      doc /* default event that is not repairable */
      // skos:definition xml:lang="en" (CreditEvents.rdf)
      // rdfs:label xml:lang="en" (CreditEvents.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-cre;DefaultEvent"/>
      // RDF: fibo-fbc-dae-cre:HardCreditEvent
    }

    item def <'installment default'> InstallmentDefault specializes DefaultEvent {
      doc /* default event involving non-payment of several installment payments as scheduled in the terms of the agreement, or non-payment of a call by the beneficial owner */
      // skos:definition xml:lang="en" (CreditEvents.rdf)
      // rdfs:label xml:lang="en" (CreditEvents.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-cre;DefaultEvent"/>
      // RDF: fibo-fbc-dae-cre:InstallmentDefault
    }

    item def <'interest obligation in light of default'> InterestObligationInLightOfDefault specializes Legal_Capacity::ContingentObligation, Legal_Capacity::ContractualObligation {
      doc /* obligation in respect of default(s) in the performance of any payment obligation */
      // skos:definition xml:lang="en" (CreditEvents.rdf)
      // rdfs:label xml:lang="en" (CreditEvents.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;ContingentObligation"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-law-lcap;ContractualObligation"/>
      // RDF: fibo-fbc-dae-cre:InterestObligationInLightOfDefault
      // subClassOf: ContingentObligation, ContractualObligation - external CMNS/FND
    }

    item def <'maturity extension'> MaturityExtension specializes Credit_Default_Swaps::ObligationSpecificCreditEvent, Corporate_Actions::CorporateAction{
      doc /* credit event involving extension of payments beyond the original maturity date of the obligation */
      // skos:definition xml:lang="en" (CreditEvents.rdf)
      // rdfs:label xml:lang="en" (CreditEvents.rdf)
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;CorporateAction"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-cre;ObligationSpecificCreditEvent"/>
      // RDF: fibo-fbc-dae-cre:MaturityExtension
    }

    item def <'moratorium'> Moratorium specializes EntitySpecificCreditEvent {
      doc /* entity-specific credit event involving a temporary suspension of payments until related issues are resolved */
      // skos:definition xml:lang="en" (CreditEvents.rdf)
      // rdfs:label xml:lang="en" (CreditEvents.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-cre;EntitySpecificCreditEvent"/>
      // RDF: fibo-fbc-dae-cre:Moratorium
    }

    item def <'obligation acceleration'> ObligationAcceleration specializes Credit_Default_Swaps::ObligationSpecificCreditEvent {
      doc /* credit event triggered when one or more reference obligations become immediately due and payable as a result of a default or covenant breach on the reference entity's other debt instruments, subject to a materiality threshold */
      // skos:definition xml:lang="en" (CreditEvents.rdf)
      // rdfs:label xml:lang="en" (CreditEvents.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-cre;ObligationSpecificCreditEvent"/>
      // RDF: fibo-fbc-dae-cre:ObligationAcceleration
    }

    item def <'obligation default'> ObligationDefault specializes DefaultEvent, Credit_Default_Swaps::ObligationSpecificCreditEvent {
      doc /* credit event triggered as a result of an obligation-specific default */
      // skos:definition xml:lang="en" (CreditEvents.rdf)
      // rdfs:label xml:lang="en" (CreditEvents.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-cre;DefaultEvent"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-cre;ObligationSpecificCreditEvent"/>
      // RDF: fibo-fbc-dae-cre:ObligationDefault
    }

    item def <'obligation restructuring'> ObligationRestructuring specializes Credit_Default_Swaps::ObligationSpecificCreditEvent {
      doc /* credit event that materially impacts an obligation, such as an interest rate reduction, principal reduction, deferral of interest or principal, change in priority ranking, or change in currency or composition of payment */
      // skos:definition xml:lang="en" (CreditEvents.rdf)
      // rdfs:label xml:lang="en" (CreditEvents.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-cre;ObligationSpecificCreditEvent"/>
      // RDF: fibo-fbc-dae-cre:ObligationRestructuring
    }

    item def <'obligation-specific credit event'> ObligationSpecificCreditEvent specializes CreditEvent, Credit_Default_Swaps::TriggeringEvent{
      doc /* credit event that relates to an individual credit agreement or debt instrument (reference obligation) */
      // skos:definition xml:lang="en" (CreditEvents.rdf)
      // rdfs:label xml:lang="en" (CreditEvents.rdf)
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-cr-cds;TriggeringEvent"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-cre;CreditEvent"/>
      // RDF: fibo-fbc-dae-cre:ObligationSpecificCreditEvent
    }

    item def <'repudiation'> Repudiation specializes Credit_Default_Swaps::ObligationSpecificCreditEvent {
      doc /* credit event involving the refusal to honor the terms of a contract */
      // skos:definition xml:lang="en" (CreditEvents.rdf)
      // rdfs:label xml:lang="en" (CreditEvents.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-cre;ObligationSpecificCreditEvent"/>
      // RDF: fibo-fbc-dae-cre:Repudiation
    }

    item def <'soft credit event'> SoftCreditEvent specializes DefaultEvent {
      doc /* default event that is repairable */
      // skos:definition xml:lang="en" (CreditEvents.rdf)
      // rdfs:label xml:lang="en" (CreditEvents.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-cre;DefaultEvent"/>
      // RDF: fibo-fbc-dae-cre:SoftCreditEvent
    }

    item def <'write-down'> WriteDown specializes Credit_Default_Swaps::ObligationSpecificCreditEvent {
      doc /* obligation-specific credit event whereby the book value of the obligation, such as the outstanding principal amount, is reduced */
      // skos:definition xml:lang="en" (CreditEvents.rdf)
      // rdfs:label xml:lang="en" (CreditEvents.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-cre;ObligationSpecificCreditEvent"/>
      // RDF: fibo-fbc-dae-cre:WriteDown
    }

}

// --- fibo-fbc-dae-crt (CreditRatings) ---
