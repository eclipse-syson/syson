// ===================================================================
// MortgageOrigination.sysml
// Source-parity extracted from: FiBO_LOAN_RealEstateLoans.sysml
// Primary source module: MortgageOrigination.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Loan LOAN/RealEstateLoans
// ===================================================================
package Mortgage_Origination {
  doc/* This ontology covers a range of mortgage origination concepts, primarily in the form of controlled vocabularies that can be used to categorize aspects of a given mortgage loan. (source: dct:abstract, MortgageOrigination.rdf) */
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

  item def <'amortization type'> AmortizationType specializes FiBO_FND_ExternalStubs::Classifier {
  	doc /* classifier of amortization algorithms */
  	// skos:definition xml:lang="en" (MortgageOrigination.rdf)
  	// rdfs:label xml:lang="en" (MortgageOrigination.rdf)
  	// RDF: fibo-loan-reln-mo:AmortizationType
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
  }
  item def <'automated underwriting system'> AutomatedUnderwritingSystem specializes Agents::AutomatedSystem {
  	doc /* software system that collects the information necessary to approve a loan application and supports a mortgage lender's analysis of a new loan application */
  	// skos:definition xml:lang="en" (MortgageOrigination.rdf)
  	// rdfs:label xml:lang="en" (MortgageOrigination.rdf)
  	// RDF: fibo-loan-reln-mo:AutomatedUnderwritingSystem
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-aap-agt;AutomatedSystem"/>
  }
  item def <'cash-out status'> CashOutStatus specializes FiBO_FND_ExternalStubs::Classifier {
  	doc /* classifier indicating the extent to which funds are released to the borrower on a new loan origination that refinances an existing loan */
  	// skos:definition xml:lang="en" (MortgageOrigination.rdf)
  	// rdfs:label xml:lang="en" (MortgageOrigination.rdf)
  	// RDF: fibo-loan-reln-mo:CashOutStatus
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
  }
  item def <'charge category'> ChargeCategory specializes FiBO_FND_ExternalStubs::Classifier {
  	doc /* description of a product or service including its unit cost, number of units and total cost */
  	// skos:definition xml:lang="en" (MortgageOrigination.rdf)
  	// rdfs:label xml:lang="en" (MortgageOrigination.rdf)
  	// RDF: fibo-loan-reln-mo:ChargeCategory
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
  }
  item def <'dwelling capacity'> DwellingCapacity specializes FiBO_FND_ExternalStubs::Classifier {
  	doc /* classifier indicating how many dwellings some property has */
  	// skos:definition xml:lang="en" (MortgageOrigination.rdf)
  	// rdfs:label xml:lang="en" (MortgageOrigination.rdf)
  	// RDF: fibo-loan-reln-mo:DwellingCapacity
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
  }
  item def <'government-sponsored loan'> GovernmentSponsoredLoan specializes LoanSecuredByRealEstate {
  	doc /* category indicating whether the covered loan is secured by a manufactured home only or with land as well */
  	// skos:definition xml:lang="en" (MortgageOrigination.rdf)
  	// rdfs:label xml:lang="en" (MortgageOrigination.rdf)
  	// RDF: fibo-loan-reln-mo:GovernmentSponsoredLoan
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-reln-mtg;LoanSecuredByRealEstate"/>
  }
  item def <'loan secured by real estate'> LoanSecuredByRealEstate specializes Loans::Loan, Loans::CollateralizedLoan{ // RDF: fibo-loan-reln-mo:LoanSecuredByRealEstate
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-ln-ln;CollateralizedLoan"/>
      // RDF: fibo-loan-reln-mo:LoanSecuredByRealEstate
  }
  item def <'manufactured home legal classification'> ManufacturedHomeLegalClassification specializes FiBO_FND_ExternalStubs::Classifier {
  	doc /* category indicating whether the covered loan is secured by a manufactured home only or with land as well */
  	// skos:definition xml:lang="en" (MortgageOrigination.rdf)
  	// rdfs:label xml:lang="en" (MortgageOrigination.rdf)
  	// RDF: fibo-loan-reln-mo:ManufacturedHomeLegalClassification
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
  }
  item def <'mortgage indemnity guarantee'> MortgageIndemnityGuarantee specializes Guaranty::InsuranceBackedGuaranty {
  	doc /* insurance-backed guarantee that protects lenders if the mortgage repayments cease for some reason, perhaps due to death or a homeowner being made redundant at work */
  	// skos:definition xml:lang="en" (MortgageOrigination.rdf)
  	// rdfs:label xml:lang="en" (MortgageOrigination.rdf)
  	// RDF: fibo-loan-reln-mo:MortgageIndemnityGuarantee
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-gty;InsuranceBackedGuaranty"/>
  }
  item def <'mortgage indemnity guarantor'> MortgageIndemnityGuarantor specializes Guaranty::Guarantor, Guaranty::Insurer {
  	doc /* guarantor and insurer that provides mortgage insurance in the form of a mortgage indemnity guarantee (MIG) */
  	// skos:definition xml:lang="en" (MortgageOrigination.rdf)
  	// rdfs:label xml:lang="en" (MortgageOrigination.rdf)
  	// RDF: fibo-loan-reln-mo:MortgageIndemnityGuarantor
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-gty;Guarantor"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-gty;Insurer"/>
  }
  item def <'mortgage indemnity insurance policy'> MortgageIndemnityInsurancePolicy specializes Guaranty::InsurancePolicy {
  	doc /* insurance policy providing the mortgage indemnity guarantee */
  	// skos:definition xml:lang="en" (MortgageOrigination.rdf)
  	// rdfs:label xml:lang="en" (MortgageOrigination.rdf)
  	// RDF: fibo-loan-reln-mo:MortgageIndemnityInsurancePolicy
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-gty;InsurancePolicy"/>
  }
  item def <'mortgage loan purpose'> MortgageLoanPurpose specializes Loan_Products::LoanPurpose, FiBO_FND_ExternalStubs::Classifier {
  	doc /* the purpose for which mortgage loan proceeds will be used, such as real property purchase, dwelling construction, or loan refinancing */
  	// skos:definition xml:lang="en" (MortgageOrigination.rdf)
  	// rdfs:label xml:lang="en" (MortgageOrigination.rdf)
  	// RDF: fibo-loan-reln-mo:MortgageLoanPurpose
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-spc-prod;LoanPurpose"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
  }
  item def NMLSRId { // RDF: fibo-loan-reln-mo:NMLSR-ID
    // RDF: fibo-loan-reln-mo:NMLSRId
  }
  item def <'property usage'> PropertyUsage specializes FiBO_FND_ExternalStubs::Classifier {
  	doc /* a category indicating the manner in which the borrower intends to utilize the property */
  	// skos:definition xml:lang="en" (MortgageOrigination.rdf)
  	// rdfs:label xml:lang="en" (MortgageOrigination.rdf)
  	// RDF: fibo-loan-reln-mo:PropertyUsage
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
  }
  item def <'real property'> RealProperty specializes Accounting_Equity::PhysicalAsset, Real_Property::RealEstate {
  	doc /* insurance-backed guarantee that protects lenders if the mortgage repayments cease for some reason, perhaps due to death or a homeowner being made redundant at work */
  	// skos:definition xml:lang="en" (MortgageOrigination.rdf)
  	// rdfs:label xml:lang="en" (MortgageOrigination.rdf)
  	// RDF: fibo-loan-reln-mo:RealProperty
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-aeq;PhysicalAsset"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-plc-rp;RealEstate"/>
  }
}

// --- fibo-loan-reln-mtg ---
