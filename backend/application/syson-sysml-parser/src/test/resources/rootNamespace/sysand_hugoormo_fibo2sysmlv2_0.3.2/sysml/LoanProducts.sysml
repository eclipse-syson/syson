// ===================================================================
// LoanProducts.sysml
// Source-parity extracted from: FiBO_LOAN_LoanSpecific.sysml
// Primary source module: LoanProducts.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Loan LOAN/LoansSpecific
// ===================================================================
package Loan_Products {
  doc/* This ontology describes loan and mortgage loan products. A product as defined here is something marketed and offered for sale, in this case a loan, such that when a customer takes on the product, they become signatories to a corresponding loan contract. A loan product in this sense is a contractual, off-the-shelf product with terms corresponding to those in the actual contract when it is signed. (source: dct:abstract, LoanProducts.rdf) */
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

  item def <'auto loan purpose'> AutoLoanPurpose specializes LoanPurpose { // RDF: fibo-loan-ln-lnp:AutoLoanPurpose
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-spc-prod;LoanPurpose"/>
    // RDF: fibo-loan-ln-lnp:AutoLoanPurpose
  }
  item def <'bridging loan purpose'> BridgingLoanPurpose specializes LoanPurpose { // RDF: fibo-loan-ln-lnp:BridgingLoanPurpose
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-spc-prod;LoanPurpose"/>
    // RDF: fibo-loan-ln-lnp:BridgingLoanPurpose
  }
  item def <'construction loan purpose'> ConstructionLoanPurpose specializes LoanPurpose { // RDF: fibo-loan-ln-lnp:ConstructionLoanPurpose
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-spc-prod;LoanPurpose"/>
    // RDF: fibo-loan-ln-lnp:ConstructionLoanPurpose
  }
  item def <'credit card receivable purpose'> CreditCardReceivablePurpose specializes LoanPurpose {
  	doc /* description of a product or service including its unit cost, number of units and total cost */
  	// skos:definition xml:lang="en" (LoanProducts.rdf)
  	// rdfs:label xml:lang="en" (LoanProducts.rdf)
  	// RDF: fibo-loan-ln-lnp:CreditCardReceivablePurpose
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-spc-prod;LoanPurpose"/>
  }
  item def <'home equity loan purpose'> HomeEquityLoanPurpose specializes LoanPurpose {
  	doc /* description of a product or service including its unit cost, number of units and total cost */
  	// skos:definition xml:lang="en" (LoanProducts.rdf)
  	// rdfs:label xml:lang="en" (LoanProducts.rdf)
  	// RDF: fibo-loan-ln-lnp:HomeEquityLoanPurpose
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-spc-prod;LoanPurpose"/>
  }
  item def <'line item'> LineItem specializes FiBO_FND_ExternalStubs::Constituent {
  	doc /* description of a product or service including its unit cost, number of units and total cost */
  	// skos:definition xml:lang="en" (LoanProducts.rdf)
  	// rdfs:label xml:lang="en" (LoanProducts.rdf)
  	// RDF: fibo-loan-ln-lnp:LineItem
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-col;Constituent"/>
  }
  item def <'loan market category'> LoanMarketCategory specializes Classification_Schemes::IndustrySectorClassifier {
  	doc /* category indicating the market domain in which the loan product is offered */
  	// skos:definition xml:lang="en" (LoanProducts.rdf)
  	// rdfs:label xml:lang="en" (LoanProducts.rdf)
  	// RDF: fibo-loan-ln-lnp:LoanMarketCategory
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-cls;IndustrySectorClassifier"/>
  }
  item def <'loan product'> LoanProduct specializes Debt::CreditFacility, Financial_Products_and_Services::FinancialProduct {
  	doc /* financial product that is realized as a loan that a party may acquire from a lending institution with specific characteristics and terms */
  	// skos:definition xml:lang="en" (LoanProducts.rdf)
  	// rdfs:label xml:lang="en" (LoanProducts.rdf)
  	// RDF: fibo-loan-ln-lnp:LoanProduct
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;CreditFacility"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;FinancialProduct"/>
  }
  item def <'loan product disclosure right'> LoanProductDisclosureRight specializes Loans_Regulatory::ProductDisclosureRight {
  	doc /* Right to fair representation of the facts about Loan products before entrering into agreements for same. */
  	// skos:definition xml:lang="en" (LoanProducts.rdf)
  	// rdfs:label xml:lang="en" (LoanProducts.rdf)
  	// RDF: fibo-loan-ln-lnp:LoanProductDisclosureRight
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-ln-reg;ProductDisclosureRight"/>
  }
  item def <'loan offering'> LoanProductOffering specializes Financial_Products_and_Services::Offering {
  	doc /* A selection of different types of loan purpose, being the purpose for which and manner in which loan (credit) draw-down amounts are to be used. This shows the purpose for which credit is to be used, and implies certain kinds of fact that relate to that specific type of loan e.g. mortgages. These are also identified as tranche types in tranches of a credit facility. */
  	// skos:definition xml:lang="en" (LoanProducts.rdf)
  	// rdfs:label xml:lang="en" (LoanProducts.rdf)
  	// RDF: fibo-loan-ln-lnp:LoanProductOffering
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;Offering"/>
  }
  item def <'loan purpose'> LoanPurpose specializes Objectives::Objective {
  	doc /* A selection of different types of loan purpose, being the purpose for which and manner in which loan (credit) draw-down amounts are to be used. This shows the purpose for which credit is to be used, and implies certain kinds of fact that relate to that specific type of loan e.g. mortgages. These are also identified as tranche types in tranches of a credit facility. */
  	// skos:definition xml:lang="en" (LoanProducts.rdf)
  	// rdfs:label xml:lang="en" (LoanProducts.rdf)
  	// RDF: fibo-loan-ln-lnp:LoanPurpose
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-gao-obj;Objective"/>
  }
  item def <'mortgage loan purpose'> MortgageLoanPurpose specializes LoanPurpose, FiBO_FND_ExternalStubs::Classifier {
  	doc /* the purpose for which mortgage loan proceeds will be used, such as real property purchase, dwelling construction, or loan refinancing */
  	// skos:definition xml:lang="en" (LoanProducts.rdf)
  	// rdfs:label xml:lang="en" (LoanProducts.rdf)
  	// RDF: fibo-loan-ln-lnp:MortgageLoanPurpose
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-spc-prod;LoanPurpose"/>
  }
  item def <'mortgage product'> MortgageProduct specializes LoanProduct { // RDF: fibo-loan-ln-lnp:MortgageProduct
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-spc-prod;LoanProduct"/>
    // RDF: fibo-loan-ln-lnp:MortgageProduct
  }
  item def <'motorhome loan purpose'> MotorhomeLoanPurpose specializes LoanPurpose {
  	doc /* relates an item to its unit cost */
  	// skos:definition xml:lang="en" (LoanProducts.rdf)
  	// rdfs:label xml:lang="en" (LoanProducts.rdf)
  	// RDF: fibo-loan-ln-lnp:MotorhomeLoanPurpose
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-spc-prod;LoanPurpose"/>
  }
  item def <'ordinary loan purpose'> OrdinaryLoanPurpose specializes LoanPurpose {
  	doc /* relates an item to its unit cost */
  	// skos:definition xml:lang="en" (LoanProducts.rdf)
  	// rdfs:label xml:lang="en" (LoanProducts.rdf)
  	// RDF: fibo-loan-ln-lnp:OrdinaryLoanPurpose
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-spc-prod;LoanPurpose"/>
  }
  item def <'overdraft facility purpose'> OverdraftFacilityPurpose specializes LoanPurpose {
  	doc /* relates an item to its unit cost */
  	// skos:definition xml:lang="en" (LoanProducts.rdf)
  	// rdfs:label xml:lang="en" (LoanProducts.rdf)
  	// RDF: fibo-loan-ln-lnp:OverdraftFacilityPurpose
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-spc-prod;LoanPurpose"/>
  }
  item def <'residential houseboat loan purpose'> ResidentialHouseboatLoanPurpose specializes LoanPurpose {
  	doc /* relates an item to its unit cost */
  	// skos:definition xml:lang="en" (LoanProducts.rdf)
  	// rdfs:label xml:lang="en" (LoanProducts.rdf)
  	// RDF: fibo-loan-ln-lnp:ResidentialHouseboatLoanPurpose
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-spc-prod;LoanPurpose"/>
  }
  item def <'student loan purpose'> StudentLoanPurpose specializes LoanPurpose {
  	doc /* relates an item to its unit cost */
  	// skos:definition xml:lang="en" (LoanProducts.rdf)
  	// rdfs:label xml:lang="en" (LoanProducts.rdf)
  	// RDF: fibo-loan-ln-lnp:StudentLoanPurpose
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-spc-prod;LoanPurpose"/>
  }
  item def <'working capital purpose'> WorkingCapitalPurpose specializes LoanPurpose {
  	doc /* relates an item to its unit cost */
  	// skos:definition xml:lang="en" (LoanProducts.rdf)
  	// rdfs:label xml:lang="en" (LoanProducts.rdf)
  	// RDF: fibo-loan-ln-lnp:WorkingCapitalPurpose
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-spc-prod;LoanPurpose"/>
  }
}

// --- fibo-loan-ln-mar ---
