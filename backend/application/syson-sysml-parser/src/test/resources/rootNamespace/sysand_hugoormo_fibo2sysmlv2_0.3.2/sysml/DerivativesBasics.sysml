// ===================================================================
// DerivativesBasics.sysml
// Source-parity extracted from: FiBO_DER_DerivativesContracts.sysml
// Primary source module: DerivativesBasics.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FIBO Derivatives DER/DerivativesContracts
// ===================================================================
package Derivatives_Basics {
  doc/* This ontology defines basic terminology common to derivative and over-the-counter (OTC) contracts. (source: dct:abstract, DerivativesBasics.rdf) */
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

  item def <'calculation agent'> CalculationAgent specializes Financial_Products_and_Services::ThirdPartyAgent {
    	doc /* cash-settled derivative where the parties agree to exchange on the maturity of the contract the difference between the current value of the underlying asset and the initial value of that asset when the contract is initiated */
    	// skos:definition xml:lang="en" (DerivativesBasics.rdf)
    	// rdfs:label xml:lang="en" (DerivativesBasics.rdf)
    	// RDF: fibo-der-drc-bsc:CalculationAgent
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;ThirdPartyAgent"/>
    }

  item def <'cashflow expression'> CashflowExpression specializes Analytics::Expression {
    	doc /* expression that specifies a calculation of a cash flow as a component of a cashflow formula */
    	// skos:definition xml:lang="en" (DerivativesBasics.rdf)
    	// rdfs:label xml:lang="en" (DerivativesBasics.rdf)
    	// RDF: fibo-der-drc-bsc:CashflowExpression
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-qtu;Expression"/>
      // subClassOf: Expression - external CMNS
    }

  item def <'cashflow formula'> CashflowFormula specializes Analytics::Formula {
    	doc /* formula for determining cashflows for a derivative instrument */
    	// skos:definition xml:lang="en" (DerivativesBasics.rdf)
    	// rdfs:label xml:lang="en" (DerivativesBasics.rdf)
    	// RDF: fibo-der-drc-bsc:CashflowFormula
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-utl-alx;Formula"/>
    }

  item def <'cashflow terms'> CashflowTerms specializes DerivativeTerms, Debt::DebtTerms {
    	doc /* terms setting out a cashflow structure of payments committed to by one party to a contract */
    	// skos:definition xml:lang="en" (DerivativesBasics.rdf)
    	// rdfs:label xml:lang="en" (DerivativesBasics.rdf)
    	// RDF: fibo-der-drc-bsc:CashflowTerms
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-bsc;DerivativeTerms"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;DebtTerms"/>
    }

  item def <'contract for difference'> ContractForDifference specializes DerivativeInstrument {
    	doc /* cash-settled derivative where the parties agree to exchange on the maturity of the contract the difference between the current value of the underlying asset and the initial value of that asset when the contract is initiated */
    	// skos:definition xml:lang="en" (DerivativesBasics.rdf)
    	// rdfs:label xml:lang="en" (DerivativesBasics.rdf)
    	// RDF: fibo-der-drc-bsc:ContractForDifference
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;DerivativeInstrument"/>
    }

  item def <'credit derivative'> CreditDerivative specializes DerivativeInstrument {
    	doc /* derivative instrument that is a privately held, negotiable bilateral contract traded over-the-counter (OTC) between two parties in a creditor/debtor relationship, enabling the creditor to effectively transfer some or all of the risk of a debtor defaulting to a third party */
    	// skos:definition xml:lang="en" (DerivativesBasics.rdf)
    	// rdfs:label xml:lang="en" (DerivativesBasics.rdf)
    	// RDF: fibo-der-drc-bsc:CreditDerivative
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;DerivativeInstrument"/>
    }

  item def <'derivative instrument'> DerivativeInstrument specializes Financial_Instruments::FinancialInstrument {
    	doc /* financial instrument that confers on its holders certain rights or obligations, whose value is derived from one or more underlying assets */
    	// skos:definition xml:lang="en" (DerivativesBasics.rdf)
    	// rdfs:label xml:lang="en" (DerivativesBasics.rdf)
    	// RDF: fibo-der-drc-bsc:DerivativeInstrument
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;FinancialInstrument"/>
    }

  item def <'derivative settlement terms'> DerivativeSettlementTerms specializes DerivativeTerms, Settlement::SettlementTerms {
    	doc /* agreement to exchange an interest rate commitment on a notional amount at a future date */
    	// skos:definition xml:lang="en" (DerivativesBasics.rdf)
    	// rdfs:label xml:lang="en" (DerivativesBasics.rdf)
    	// RDF: fibo-der-drc-bsc:DerivativeSettlementTerms
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-bsc;DerivativeTerms"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;SettlementTerms"/>
    }

  item def <'derivative terms'> DerivativeTerms specializes Contracts::ContractualCommitment {
    	doc /* contractual terms specific to derivative contracts, including terms related to payments and delivery between parties */
    	// skos:definition xml:lang="en" (DerivativesBasics.rdf)
    	// rdfs:label xml:lang="en" (DerivativesBasics.rdf)
    	// RDF: fibo-der-drc-bsc:DerivativeTerms
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractualCommitment"/>
    }

  item def <'derivatives clearing organization'> DerivativesClearingOrganization specializes Financial_Services_Entities::ClearingHouse {
    	doc /* moneyness classifier that refers to an option whose value in terms of its strike price is the same or close to the current market price of the underlying security */
    	// skos:definition xml:lang="en" (DerivativesBasics.rdf)
    	// rdfs:label xml:lang="en" (DerivativesBasics.rdf)
    	// rdfs:label xml:lang="en" (DerivativesBasics.rdf)
    	// RDF: fibo-der-drc-bsc:DerivativesClearingOrganization
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;ClearingHouse"/>
    }

  item def <'economic rate-based derivative'> EconomicRateBasedDerivative specializes RateBasedDerivative {
    	doc /* rate-based derivative whose underlier is some economic indicator */
    	// skos:definition xml:lang="en" (DerivativesBasics.rdf)
    	// rdfs:label xml:lang="en" (DerivativesBasics.rdf)
    	// RDF: fibo-der-drc-bsc:EconomicRateBasedDerivative
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-bsc;RateBasedDerivative"/>
    }

  item def <'forward rate agreement'> ForwardRateAgreement specializes InterestRateDerivative {
    	doc /* agreement to exchange an interest rate commitment on a notional amount at a future date */
    	// skos:definition xml:lang="en" (DerivativesBasics.rdf)
    	// rdfs:label xml:lang="en" (DerivativesBasics.rdf)
    	// RDF: fibo-der-drc-bsc:ForwardRateAgreement
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-bsc;InterestRateDerivative"/>
    }

  item def <'interest rate derivative'> InterestRateDerivative specializes RateBasedDerivative {
    	doc /* assessment activity to estimate the value of an underlying asset of a derivative */
    	// skos:definition xml:lang="en" (DerivativesBasics.rdf)
    	// rdfs:label xml:lang="en" (DerivativesBasics.rdf)
    	// RDF: fibo-der-drc-bsc:InterestRateDerivative
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-bsc;RateBasedDerivative"/>
    }

  item def <'introducing broker'> IntroducingBroker specializes Financial_Services_Entities::NonDepositoryInstitution, Financial_Products_and_Services::Broker {
    	doc /* broker that solicits or accepts orders for derivatives that are traded on or subject to the rules of an exchange */
    	// skos:definition xml:lang="en" (DerivativesBasics.rdf)
    	// rdfs:label xml:lang="en" (DerivativesBasics.rdf)
    	// RDF: fibo-der-drc-bsc:IntroducingBroker
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;NonDepositoryInstitution"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;Broker"/>
    }

  item def <'observable value'> ObservableValue specializes FiBO_FND_ExternalStubs::Specification {
    	doc /* specification for the value for something discernible and for which evidence can be obtained */
    	// skos:definition xml:lang="en" (DerivativesBasics.rdf)
    	// rdfs:label xml:lang="en" (DerivativesBasics.rdf)
    	// RDF: fibo-der-drc-bsc:ObservableValue
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-doc;Specification"/>
      // subClassOf: Specification - external CMNS
    }

  item def <'over-the-counter derivative instrument'> OverTheCounterDerivativeInstrument specializes OverTheCounterInstrument, DerivativeInstrument {
    	doc /* derivative instrument that is not listed on an organized exchange */
    	// skos:definition xml:lang="en" (DerivativesBasics.rdf)
    	// rdfs:label xml:lang="en" (DerivativesBasics.rdf)
    	// RDF: fibo-der-drc-bsc:OverTheCounterDerivativeInstrument
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-bsc;OverTheCounterInstrument"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;DerivativeInstrument"/>
    }

  item def <'over-the-counter instrument'> OverTheCounterInstrument specializes Financial_Instruments::FinancialInstrument {
    	doc /* financial instrument and bilateral contract that is not listed on an organized exchange */
    	// skos:definition xml:lang="en" (DerivativesBasics.rdf)
    	// rdfs:label xml:lang="en" (DerivativesBasics.rdf)
    	// RDF: fibo-der-drc-bsc:OverTheCounterInstrument
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;FinancialInstrument"/>
    }

  item def <'parametric cashflow terms'> ParametricCashflowTerms specializes CashflowTerms {
    	doc /* terms for a set of cashflows defined according to a mathematical formula */
    	// skos:definition xml:lang="en" (DerivativesBasics.rdf)
    	// rdfs:label xml:lang="en" (DerivativesBasics.rdf)
    	// RDF: fibo-der-drc-bsc:ParametricCashflowTerms
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-bsc;CashflowTerms"/>
    }

  item def <'paying party'> PayingParty specializes Contracts::ContractParty, Payments_and_Schedules::Payer, Products_and_Services::Buyer {
    	doc /* party responsible for making payments in a transaction specified in a contract */
    	// skos:definition xml:lang="en" (DerivativesBasics.rdf)
    	// rdfs:label xml:lang="en" (DerivativesBasics.rdf)
    	// RDF: fibo-der-drc-bsc:PayingParty
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractParty"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-psch;Payer"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-pas;Buyer"/>
    }

  item def <'rate-based derivative'> RateBasedDerivative specializes DerivativeInstrument {
    	doc /* derivative instrument where the holder has the right but may not have the obligation, depending on the nature of the instrument, to enter into the underlying contract, or pay or receive payment related to the underlying financial rate (or rate contract) on a specified future date based on a specified future rate and term */
    	// skos:definition xml:lang="en" (DerivativesBasics.rdf)
    	// rdfs:label xml:lang="en" (DerivativesBasics.rdf)
    	// RDF: fibo-der-drc-bsc:RateBasedDerivative
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;DerivativeInstrument"/>
    }

  item def <'receiving counterparty'> ReceivingParty specializes Contracts::ContractParty, Payments_and_Schedules::Payee, Products_and_Services::Seller {
    	doc /* party that receives payments in a transaction specified in a contract */
    	// skos:definition xml:lang="en" (DerivativesBasics.rdf)
    	// rdfs:label xml:lang="en" (DerivativesBasics.rdf)
    	// RDF: fibo-der-drc-bsc:ReceivingParty
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractParty"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-psch;Payee"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-pas;Seller"/>
    }

  item def <'underlier'> Underlier specializes FiBO_FND_ExternalStubs::Undergoer {
    	doc /* something that can be assigned a value in the marketplace that forms the basis for a derivative or pool-backed instrument */
    	// skos:definition xml:lang="en" (DerivativesBasics.rdf)
    	// rdfs:label xml:lang="en" (DerivativesBasics.rdf)
    	// RDF: fibo-der-drc-bsc:Underlier
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;Undergoer"/>
    }

  item def <'underlying asset valuation'> UnderlyingAssetValuation specializes Assessments::ValueAssessment {
    	doc /* assessment activity to estimate the value of an underlying asset of a derivative */
    	// skos:definition xml:lang="en" (DerivativesBasics.rdf)
    	// rdfs:label xml:lang="en" (DerivativesBasics.rdf)
    	// RDF: fibo-der-drc-bsc:UnderlyingAssetValuation
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-asmt;ValueAssessment"/>
    }

  item def <'unique product identifier registry entry'> UniqueProductIdentifierRegistryEntry specializes FiBO_FND_ExternalStubs::RegistryEntry {
    	doc /* entry in a unique product identifier registry */
    	// skos:definition xml:lang="en" (DerivativesBasics.rdf)
    	// rdfs:label xml:lang="en" (DerivativesBasics.rdf)
    	// RDF: fibo-der-drc-bsc:UniqueProductIdentifierRegistryEntry
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-ra;RegistryEntry"/>
      // subClassOf: RegistryEntry - external CMNS
    }

  item def <'unique product identifier service provider'> UniqueProductIdentifierServiceProvider specializes FiBO_FND_ExternalStubs::RegistrationAuthority {
    	doc /* organization designated by an external body of financial regulators to assign UPIs and operate a UPI reference data library */
    	// skos:definition xml:lang="en" (DerivativesBasics.rdf)
    	// rdfs:label xml:lang="en" (DerivativesBasics.rdf)
    	// RDF: fibo-der-drc-bsc:UniqueProductIdentifierServiceProvider
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-ra;RegistrationAuthority"/>
      // subClassOf: RegistrationAuthority - external CMNS
    }

  item def <'unique product identifier'> UniqueProductIdentifier specializes Financial_Instruments::FinancialInstrumentIdentifier, FiBO_FND_CodeSets_And_Schemes::StructuredIdentifier {
    	doc /* indicates costs, such as up front costs, brokerage fees and the like, that must be paid on delivery */
    	// skos:definition xml:lang="en" (DerivativesBasics.rdf)
    	// rdfs:label xml:lang="en" (DerivativesBasics.rdf)
    	// RDF: fibo-der-drc-bsc:UniqueProductIdentifier
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;FinancialInstrumentIdentifier"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cxtid;StructuredIdentifier"/>
      // subClassOf: StructuredIdentifier - external CMNS
    }

  item def <'valuation terms'> ValuationTerms specializes DerivativeTerms {
    	doc /* contract terms specific to valuation of the underlying asset(s) */
    	// skos:definition xml:lang="en" (DerivativesBasics.rdf)
    	// rdfs:label xml:lang="en" (DerivativesBasics.rdf)
    	// RDF: fibo-der-drc-bsc:ValuationTerms
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-der-drc-bsc;DerivativeTerms"/>
    }

}
