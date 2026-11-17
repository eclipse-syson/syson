// ===================================================================
// HomeMortgageDisclosureActHMDACoveredMortgages.sysml
// Source-parity extracted from: FiBO_LOAN_RealEstateLoans.sysml
// Primary source module: HomeMortgageDisclosureActHMDACoveredMortgages.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Loan LOAN/RealEstateLoans
// ===================================================================
package Home_Mortgage_Disclosure_Act_HMDA_Covered_Mortgages {
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

  item def <'construction type'> ConstructionType specializes FiBO_FND_ExternalStubs::Classifier {
  	doc /* particular kind of construction */
  	// skos:definition xml:lang="en" (HomeMortgageDisclosureActHMDACoveredMortgages.rdf)
  	// rdfs:label xml:lang="en" (HomeMortgageDisclosureActHMDACoveredMortgages.rdf)
  	// RDF: fibo-loan-reln-hmda:ConstructionType
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
  }
  item def <'ethnicity'> Ethnicity specializes FiBO_FND_ExternalStubs::Classifier {
  	doc /* category based on a cultural factors, including nationality, regional culture, ancestry, and language */
  	// skos:definition xml:lang="en" (HomeMortgageDisclosureActHMDACoveredMortgages.rdf)
  	// rdfs:label xml:lang="en" (HomeMortgageDisclosureActHMDACoveredMortgages.rdf)
  	// RDF: fibo-loan-reln-hmda:Ethnicity
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
  }
  item def HMDACoveredLoanContract { // RDF: fibo-loan-reln-hmda:HMDA-CoveredLoanContract
    // RDF: fibo-loan-reln-hmda:HMDACoveredLoanContract
  }
  item def HMDADisposition { // RDF: fibo-loan-reln-hmda:HMDA-Disposition
    // RDF: fibo-loan-reln-hmda:HMDADisposition
  }
  item def HMDAPreApprovalRequest { // RDF: fibo-loan-reln-hmda:HMDA-PreApprovalRequest
    // RDF: fibo-loan-reln-hmda:HMDAPreApprovalRequest
  }
  item def HMDAReport { // RDF: fibo-loan-reln-hmda:HMDA-Report
    // RDF: fibo-loan-reln-hmda:HMDAReport
  }
  item def <'how submitted'> HowSubmitted specializes FiBO_FND_ExternalStubs::Classifier {
  	doc /* category indicating whether the applicant or borrower submitted the application for the covered loan directly to the reporting financial institution */
  	// skos:definition xml:lang="en" (HomeMortgageDisclosureActHMDACoveredMortgages.rdf)
  	// rdfs:label xml:lang="en" (HomeMortgageDisclosureActHMDACoveredMortgages.rdf)
  	// RDF: fibo-loan-reln-hmda:HowSubmitted
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
  }
  item def <'race'> Race specializes FiBO_FND_ExternalStubs::Classifier {
  	doc /* a category based on a person's physical characteristics, such as bone structure and skin, hair, or eye color */
  	// skos:definition xml:lang="en" (HomeMortgageDisclosureActHMDACoveredMortgages.rdf)
  	// rdfs:label xml:lang="en" (HomeMortgageDisclosureActHMDACoveredMortgages.rdf)
  	// RDF: fibo-loan-reln-hmda:Race
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
  }
  item def <'universal loan identifier'> UniversalLoanIdentifier specializes Financial_Instruments::FinancialInstrumentIdentifier {
  	doc /* unique identifier given to unequivocally identify a specific loan secured by real estate */
  	// skos:definition xml:lang="en" (HomeMortgageDisclosureActHMDACoveredMortgages.rdf)
  	// rdfs:label xml:lang="en" (HomeMortgageDisclosureActHMDACoveredMortgages.rdf)
  	// RDF: fibo-loan-reln-hmda:UniversalLoanIdentifier
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;FinancialInstrumentIdentifier"/>
  }
}

// --- fibo-loan-reln-mo ---
