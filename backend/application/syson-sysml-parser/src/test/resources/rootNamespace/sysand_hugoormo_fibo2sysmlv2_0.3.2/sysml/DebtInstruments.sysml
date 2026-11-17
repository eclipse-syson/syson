// ===================================================================
// DebtInstruments.sysml
// Source-parity extracted from: FiBO_SEC_Debt.sysml
// Primary source module: DebtInstruments.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Securities SEC/Debt
// ===================================================================
package Debt_Instruments {
  doc/* This ontology defines concepts that are specific to debt instruments (tradable and non-tradable). (source: dct:abstract, DebtInstruments.rdf) */
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

// FinancialInstrument, FinancialInstrumentOffering

  item def <'debt instrument'> DebtInstrument specializes Financial_Instruments::FinancialInstrument, Debt::CreditAgreement {
  	doc /* a strip that represents the non-principal portion of the monthly payments on the underlying debt instrument, such as a bond */
  	// skos:definition xml:lang="en" (DebtInstruments.rdf)
  	// rdfs:label xml:lang="en" (DebtInstruments.rdf)
  	// rdfs:label xml:lang="en" (FinancialInstruments.rdf)
  	// RDF: fibo-sec-dbt-dbti:DebtInstrument
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;CreditAgreement"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;FinancialInstrument"/>
  }
  item def <'fixed income security'> FixedIncomeSecurity specializes Debt_Instruments::TradableDebtInstrument {
  	doc /* futures contract with an interest-bearing instrument as the underlying asset */
  	// skos:definition xml:lang="en" (DebtInstruments.rdf)
  	// rdfs:label xml:lang="en" (DebtInstruments.rdf)
  	// rdfs:label xml:lang="en" (DebtInstruments.rdf)
  	// RDF: fibo-sec-dbt-dbti:FixedIncomeSecurity
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-dbti;TradableDebtInstrument"/>
  }
  item def <'tradable debt instrument'> TradableDebtInstrument specializes Financial_Instruments::DebtInstrument, Financial_Instruments::Security {
  	doc /* The Average Life analytic at the time the security was issued. */
  	// skos:definition xml:lang="en" (DebtInstruments.rdf)
  	// rdfs:label xml:lang="en" (DebtInstruments.rdf)
  	// rdfs:label xml:lang="en" (SecurityBasedDerivatives.rdf)
  	// RDF: fibo-sec-dbt-dbti:TradableDebtInstrument
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;Security"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;DebtInstrument"/>
  }
  item def <'non-tradable debt instrument'> NonTradableDebtInstrument specializes Financial_Instruments::DebtInstrument, Contracts::WrittenContract {
  	doc /* a debt instrument that may not be bought or sold */
  	// skos:definition xml:lang="en" (DebtInstruments.rdf)
  	// rdfs:label xml:lang="en" (DebtInstruments.rdf)
  	// RDF: fibo-sec-dbt-dbti:NonTradableDebtInstrument
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;WrittenContract"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;DebtInstrument"/>
  }
  item def <'issued debt'> IssuedDebt specializes Debt::Debt {
  	doc /* a strip that represents the principal portion of the monthly payments on the underlying debt instrument, such as a bond */
  	// skos:definition xml:lang="en" (DebtInstruments.rdf)
  	// rdfs:label xml:lang="en" (DebtInstruments.rdf)
  	// RDF: fibo-sec-dbt-dbti:IssuedDebt
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;Debt"/>
  }
  item def <'publicly issued debt'> PubliclyIssuedDebt specializes IssuedDebt {
  	doc /* a tradeable debt instrument created either through the process of removing coupons from a bond and then selling the separate parts as a zero coupon bond and an interest paying coupon bond or through taking the opposite position from some variant in the options market */
  	// skos:definition xml:lang="en" (DebtInstruments.rdf)
  	// rdfs:label xml:lang="en" (DebtInstruments.rdf)
  	// RDF: fibo-sec-dbt-dbti:PubliclyIssuedDebt
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-dbti;IssuedDebt"/>
  }
  item def <'variable income security'> VariableIncomeSecurity specializes Debt_Instruments::TradableDebtInstrument {
  	doc /* tradeable debt instrument that provide their owners with a rate of return that is dynamic and determined by market forces */
  	// skos:definition xml:lang="en" (DebtInstruments.rdf)
  	// rdfs:label xml:lang="en" (DebtInstruments.rdf)
  	// RDF: fibo-sec-dbt-dbti:VariableIncomeSecurity
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-dbti;TradableDebtInstrument"/>
  }
  item def <'strip'> Strip specializes FixedIncomeSecurity {
  	doc /* a tradeable debt instrument created either through the process of removing coupons from a bond and then selling the separate parts as a zero coupon bond and an interest paying coupon bond or through taking the opposite position from some variant in the options market */
  	// skos:definition xml:lang="en" (DebtInstruments.rdf)
  	// rdfs:label xml:lang="en" (DebtInstruments.rdf)
  	// RDF: fibo-sec-dbt-dbti:Strip
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-dbti;FixedIncomeSecurity"/>
  }
  item def <'interest-only strip'> InterestOnlyStrip specializes Strip {
  	doc /* a strip that represents the non-principal portion of the monthly payments on the underlying debt instrument, such as a bond */
  	// skos:definition xml:lang="en" (DebtInstruments.rdf)
  	// rdfs:label xml:lang="en" (DebtInstruments.rdf)
  	// RDF: fibo-sec-dbt-dbti:InterestOnlyStrip
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-dbti;Strip"/>
  }
  item def <'principal-only strip'> PrincipalOnlyStrip specializes Strip {
  	doc /* a strip that represents the principal portion of the monthly payments on the underlying debt instrument, such as a bond */
  	// skos:definition xml:lang="en" (DebtInstruments.rdf)
  	// rdfs:label xml:lang="en" (DebtInstruments.rdf)
  	// RDF: fibo-sec-dbt-dbti:PrincipalOnlyStrip
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-dbti;Strip"/>
  }
  item def <'debt offering'> DebtOffering specializes Issuance_Process::SecuritiesOffering {
  	doc /* an offering of one or more debt instruments for sale */
  	// skos:definition xml:lang="en" (DebtInstruments.rdf)
  	// rdfs:label xml:lang="en" (DebtInstruments.rdf)
  	// RDF: fibo-sec-dbt-dbti:DebtOffering
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-iss;SecuritiesOffering"/>
  }
  item def <'relative price'> RelativePrice specializes Instrument_Pricing::SecurityPrice {
  	doc /* security price specified in comparison with either a stated or market value for a debt instrument at some point in time */
  	// skos:definition xml:lang="en" (DebtInstruments.rdf)
  	// rdfs:label xml:lang="en" (DebtInstruments.rdf)
  	// RDF: fibo-sec-dbt-dbti:RelativePrice
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-ip;SecurityPrice"/>
  }
  item def <'notification provision'> NotificationProvision specializes Contracts::ContractualCommitment, Financial_Instruments::RedemptionProvision {
  	doc /* provision indicating that notification is required prior to the FiBOOccurrence of a relevant event */
  	// skos:definition xml:lang="en" (DebtInstruments.rdf)
  	// rdfs:label xml:lang="en" (DebtInstruments.rdf)
  	// RDF: fibo-sec-dbt-dbti:NotificationProvision
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractualCommitment"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;RedemptionProvision"/>
  }
  item def <'call event'> CallEvent specializes RedemptionEvent {
  	doc /* an event associated with the call schedule for a debt instrument */
  	// skos:definition xml:lang="en" (DebtInstruments.rdf)
  	// rdfs:label xml:lang="en" (DebtInstruments.rdf)
  	// RDF: fibo-sec-dbt-dbti:CallEvent
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-dbti;RedemptionEvent"/>
  }
  item def <'call feature'> CallFeature specializes Financial_Instruments::RedemptionProvision {
  	doc /* redemption provision defining the rights of the issuer to buy back a security at a call price after a call protection period */
  	// skos:definition xml:lang="en" (DebtInstruments.rdf)
  	// rdfs:label xml:lang="en" (DebtInstruments.rdf)
  	// RDF: fibo-sec-dbt-dbti:CallFeature
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;RedemptionProvision"/>
  }
  item def <'call notification provision'> CallNotificationProvision specializes Contracts::NotificationProvision {
  	doc /* provision of a call feature defining notification requirements */
  	// skos:definition xml:lang="en" (DebtInstruments.rdf)
  	// rdfs:label xml:lang="en" (DebtInstruments.rdf)
  	// RDF: fibo-sec-dbt-dbti:CallNotificationProvision
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-dbti;NotificationProvision"/>
  }
  item def <'call premium'> CallPremium specializes Currency_Amount::MonetaryPrice {
  	doc /* the price over par paid by an issuer to redeem securities when exercising a call provision */
  	// skos:definition xml:lang="en" (DebtInstruments.rdf)
  	// rdfs:label xml:lang="en" (DebtInstruments.rdf)
  	// RDF: fibo-sec-dbt-dbti:CallPremium
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-cur;MonetaryPrice"/>
  }
  item def <'call schedule'> CallSchedule specializes Financial_Dates::Schedule {
  	doc /* a schedule of call prices and when they are in effect */
  	// skos:definition xml:lang="en" (DebtInstruments.rdf)
  	// rdfs:label xml:lang="en" (DebtInstruments.rdf)
  	// RDF: fibo-sec-dbt-dbti:CallSchedule
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-fd;Schedule"/>
  }
  item def <'call window'> CallWindow specializes FiBO_FND_ExternalStubs::DatePeriod {
  	doc /* an explicit period of time prior to a call date during which an issuer or agent must give notice to holders */
  	// skos:definition xml:lang="en" (DebtInstruments.rdf)
  	// rdfs:label xml:lang="en" (DebtInstruments.rdf)
  	// RDF: fibo-sec-dbt-dbti:CallWindow
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-dt;DatePeriod"/>
  }
  item def <'fully-indexed interest rate'> FullyIndexedInterestRate specializes Debt::FloatingInterestRate, Analytics::Expression {
  	doc /* a variable interest rate that is calculated by adding a margin to a specified index rate */
  	// skos:definition xml:lang="en" (DebtInstruments.rdf)
  	// rdfs:label xml:lang="en" (DebtInstruments.rdf)
  	// RDF: fibo-sec-dbt-dbti:FullyIndexedInterestRate
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;FloatingInterestRate"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-qtu;Expression"/>
  }
  item def <'make whole feature'> MakeWholeFeature specializes CallFeature {
  	doc /* a call provision allowing the issuer to pay off remaining debt early */
  	// skos:definition xml:lang="en" (DebtInstruments.rdf)
  	// rdfs:label xml:lang="en" (DebtInstruments.rdf)
  	// RDF: fibo-sec-dbt-dbti:MakeWholeFeature
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-dbti;CallFeature"/>
  }
  item def <'margin'> Margin specializes FiBO_FND_ExternalStubs::Variable {
  	doc /* a variable that is added to a specified index rate to determine the fully indexed interest rate charged to a borrower on a credit balance */
  	// skos:definition xml:lang="en" (DebtInstruments.rdf)
  	// rdfs:label xml:lang="en" (DebtInstruments.rdf)
  	// RDF: fibo-sec-dbt-dbti:Margin
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-qtu;Variable"/>
  }
  item def <'prescriptive event'> PrescriptiveEvent specializes FiBOOccurrences::OccurrenceKind {
  	doc /* The return on the debt instrument at the stated price. */
  	// skos:definition xml:lang="en" (DebtInstruments.rdf)
  	// rdfs:label xml:lang="en" (DebtInstruments.rdf)
  	// rdfs:label xml:lang="en" (DebtInstruments.rdf)
  	// RDF: fibo-sec-dbt-dbti:PrescriptiveEvent
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-oc;OccurrenceKind"/>
  }
  item def <'put event'> PutEvent specializes RedemptionEvent {
  	doc /* an event associated with the put schedule for a debt instrument, i.e., an event involving the 'put', or surrender of the instrument by the holder */
  	// skos:definition xml:lang="en" (DebtInstruments.rdf)
  	// rdfs:label xml:lang="en" (DebtInstruments.rdf)
  	// RDF: fibo-sec-dbt-dbti:PutEvent
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-dbti;RedemptionEvent"/>
  }
  item def <'put notification provision'> PutNotificationProvision specializes Contracts::NotificationProvision {
  	doc /* a provision of a put feature defining notification requirements */
  	// skos:definition xml:lang="en" (DebtInstruments.rdf)
  	// rdfs:label xml:lang="en" (DebtInstruments.rdf)
  	// RDF: fibo-sec-dbt-dbti:PutNotificationProvision
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-dbti;NotificationProvision"/>
  }
  item def <'put premium'> PutPremium specializes Currency_Amount::MonetaryPrice {
  	doc /* an amount over par that a debt instrument holder must pay to sell the security early */
  	// skos:definition xml:lang="en" (DebtInstruments.rdf)
  	// rdfs:label xml:lang="en" (DebtInstruments.rdf)
  	// RDF: fibo-sec-dbt-dbti:PutPremium
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-cur;MonetaryPrice"/>
  }
  item def <'put schedule'> PutSchedule specializes Financial_Dates::Schedule {
  	doc /* a schedule that defines the events associated with the put feature of a debt instrument, i.e, the dates on which the debt instrument may be sold at what price by the holder */
  	// skos:definition xml:lang="en" (DebtInstruments.rdf)
  	// rdfs:label xml:lang="en" (DebtInstruments.rdf)
  	// RDF: fibo-sec-dbt-dbti:PutSchedule
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-fd;Schedule"/>
  }
  item def <'put window'> PutWindow specializes FiBO_FND_ExternalStubs::DatePeriod {
  	doc /* an explicit period of time prior to a put date during which holder or agent must give notice to an issuer */
  	// skos:definition xml:lang="en" (DebtInstruments.rdf)
  	// rdfs:label xml:lang="en" (DebtInstruments.rdf)
  	// RDF: fibo-sec-dbt-dbti:PutWindow
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-dt;DatePeriod"/>
  }
  item def <'redemption event'> RedemptionEvent specializes PrescriptiveEvent {
  	doc /* The return on the debt instrument at the stated price. */
  	// skos:definition xml:lang="en" (DebtInstruments.rdf)
  	// rdfs:label xml:lang="en" (DebtInstruments.rdf)
  	// rdfs:label xml:lang="en" (DebtInstruments.rdf)
  	// RDF: fibo-sec-dbt-dbti:RedemptionEvent
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-dbti;PrescriptiveEvent"/>
  }
  item def <'step event'> StepEvent specializes FiBOOccurrences::CalculationEvent, PrescriptiveEvent {
  	doc /* event that prescribes a change in a contractual term, such as a rate or notional amount, for a given contract */
  	// skos:definition xml:lang="en" (DebtInstruments.rdf)
  	// rdfs:label xml:lang="en" (DebtInstruments.rdf)
  	// RDF: fibo-sec-dbt-dbti:StepEvent
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-oc;CalculationEvent"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-dbti;PrescriptiveEvent"/>
  }
  item def <'step schedule'> StepSchedule specializes Financial_Dates::Schedule {
  	doc /* schedule specifying the date(s) and change in a contractual term(s), e.g., rate or notional amount, for a contract that has a feature where stipulated changes occur at specified intervals or on specified dates */
  	// skos:definition xml:lang="en" (DebtInstruments.rdf)
  	// rdfs:label xml:lang="en" (DebtInstruments.rdf)
  	// RDF: fibo-sec-dbt-dbti:StepSchedule
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-fd;Schedule"/>
  }
}

// --- DistributedLoans.rdf ---
