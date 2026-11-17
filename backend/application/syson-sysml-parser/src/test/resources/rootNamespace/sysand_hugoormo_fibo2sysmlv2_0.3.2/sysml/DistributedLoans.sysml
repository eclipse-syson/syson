// ===================================================================
// DistributedLoans.sysml
// Source-parity extracted from: FiBO_SEC_Debt.sysml
// Primary source module: DistributedLoans.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Securities SEC/Debt
// ===================================================================
package Distributed_Loans {
  doc/* This ontology defines contracts which give the holder some formal participation in some loan. (source: dct:abstract, DistributedLoans.rdf) */
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

// Syndicate, SyndicateMember
// PartyRole
// Loan

  item def <'administrative agent'> AdministrativeAgent specializes Functional_Entities::SyndicateMember {
  	doc /* role of a financial institution (often one of the primary lenders in the syndicate) designated to act as an intermediary between the borrowers and the syndicate lenders */
  	// skos:definition xml:lang="en" (DistributedLoans.rdf)
  	// rdfs:label xml:lang="en" (DistributedLoans.rdf)
  	// RDF: fibo-sec-dbt-dl:AdministrativeAgent
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-fct-fct;SyndicateMember"/>
  }
  item def <'bookrunner'> Bookrunner specializes Financial_Services_Entities::FinancialInstitution {
  	doc /* financial institution (typically a commercial or investment bank) responsible for coordinating the arrangement, structuring, and marketing of the loan to potential lenders */
  	// skos:definition xml:lang="en" (DistributedLoans.rdf)
  	// rdfs:label xml:lang="en" (DistributedLoans.rdf)
  	// RDF: fibo-sec-dbt-dl:Bookrunner
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;FinancialInstitution"/>
  }
  item def <'documentation agent'> DocumentationAgent specializes Functional_Entities::SyndicateMember {
  	doc /* financial institution designated to oversee the drafting, negotiation, and finalization of the loan documentation */
  	// skos:definition xml:lang="en" (DistributedLoans.rdf)
  	// rdfs:label xml:lang="en" (DistributedLoans.rdf)
  	// RDF: fibo-sec-dbt-dl:DocumentationAgent
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-fct-fct;SyndicateMember"/>
  }
  item def <'finance syndicate'> FinanceSyndicate specializes Functional_Entities::Syndicate {
  	doc /* group of financial institutions or lenders that collectively agree to provide funding for a large loan to a single borrower */
  	// skos:definition xml:lang="en" (DistributedLoans.rdf)
  	// rdfs:label xml:lang="en" (DistributedLoans.rdf)
  	// RDF: fibo-sec-dbt-dl:FinanceSyndicate
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-fct-fct;Syndicate"/>
  }
  item def <'finance syndicate member'> FinanceSyndicateMember specializes Functional_Entities::SyndicateMember, Financial_Products_and_Services::FinancialServiceProvider {
  	doc /* syndicate member that is a financial services provider that contributes funds to a syndicated loan or loan participation note */
  	// skos:definition xml:lang="en" (DistributedLoans.rdf)
  	// rdfs:label xml:lang="en" (DistributedLoans.rdf)
  	// RDF: fibo-sec-dbt-dl:FinanceSyndicateMember
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;FinancialServiceProvider"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-fct-fct;SyndicateMember"/>
  }
  item def <'lead arranger'> LeadArranger specializes Financial_Services_Entities::FinancialInstitution {
  	doc /* financial institution that spearheads the loan structuring and syndication process on behalf of the borrower */
  	// skos:definition xml:lang="en" (DistributedLoans.rdf)
  	// rdfs:label xml:lang="en" (DistributedLoans.rdf)
  	// RDF: fibo-sec-dbt-dl:LeadArranger
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;FinancialInstitution"/>
  }
  item def <'loan participation note'> LoanParticipationNote specializes Debt::CreditFacility, Commercial_Loans::CommercialLoan, Debt_Instruments::FixedIncomeSecurity {
  	doc /* credit facility and fixed-income security that may be distributed across a group of lenders */
  	// skos:definition xml:lang="en" (DistributedLoans.rdf)
  	// rdfs:label xml:lang="en" (DistributedLoans.rdf)
  	// RDF: fibo-sec-dbt-dl:LoanParticipationNote
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;CreditFacility"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-spc-com;CommercialLoan"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-dbt-dbti;FixedIncomeSecurity"/>
  }
  item def <'syndicated loan'> SyndicatedLoan specializes Debt::CreditFacility, Commercial_Loans::CommercialLoan {
  	doc /* credit agreement in which a group of lenders, known as a syndicate, collectively provides a large loan to a single borrower */
  	// skos:definition xml:lang="en" (DistributedLoans.rdf)
  	// rdfs:label xml:lang="en" (DistributedLoans.rdf)
  	// RDF: fibo-sec-dbt-dl:SyndicatedLoan
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;CreditFacility"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-loan-spc-com;CommercialLoan"/>
  }
  item def <'syndication agent'> SyndicationAgent specializes Financial_Services_Entities::FinancialInstitution {
  	doc /* financial institution (typically a commercial or investment bank) designated to help structure, arrange, and manage the loan syndication process */
  	// skos:definition xml:lang="en" (DistributedLoans.rdf)
  	// rdfs:label xml:lang="en" (DistributedLoans.rdf)
  	// RDF: fibo-sec-dbt-dl:SyndicationAgent
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;FinancialInstitution"/>
  }
}

// --- ExerciseConventions.rdf ---
