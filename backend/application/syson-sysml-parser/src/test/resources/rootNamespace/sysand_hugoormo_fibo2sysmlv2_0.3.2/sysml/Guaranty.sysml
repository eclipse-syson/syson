// ===================================================================
// Guaranty.sysml
// Source-parity extracted from: FiBO_FBC_DebtAndEquities.sysml
// Primary source module: Guaranty.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Financial Business and Commerce FBC/DebtAndEquities
// Structural note: translated as a split file from FiBO_FBC_DebtAndEquities umbrella.
// ===================================================================
package Guaranty {
  doc/* This ontology defines concepts related to contractual guaranty. (source: dct:abstract, Guaranty.rdf) */
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

    item def <'collateralized guaranty'> CollateralizedGuaranty specializes Guaranty {
        doc /* guaranty that takes the form of some asset that is pledged by a borrower to a lender (usually in return for a loan) */
        // skos:definition xml:lang="en" (Guaranty.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-gty;Guaranty"/>
    	// rdfs:label xml:lang="en" (Guaranty.rdf)
      // RDF: fibo-fbc-dae-gty:CollateralizedGuaranty
    }

    item def <'government guaranty'> GovernmentGuaranty specializes Guaranty {
        doc /* guaranty provided by a government entity, such as for a government-backed security */
        // skos:definition xml:lang="en" (Guaranty.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-gty;Guaranty"/>
    	// rdfs:label xml:lang="en" (Guaranty.rdf)
      // RDF: fibo-fbc-dae-gty:GovernmentGuaranty
    }

    item def <'guarantor'> Guarantor specializes Contracts::ContractThirdParty, Control_Parties::DeJureControllingInterestParty {
        doc /* party that guarantees, endorses, or provides indemnity for some obligation on behalf of some other party */
        // skos:definition xml:lang="en" (Guaranty.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractThirdParty"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-cpty;DeJureControllingInterestParty"/>
    	// rdfs:label xml:lang="en" (Guaranty.rdf)
      // RDF: fibo-fbc-dae-gty:Guarantor
      // subClassOf: ContractThirdParty - from FIBO_FND_Agreements (AgreementsPackage)
    }

    item def <'guaranty'> Guaranty specializes Agreements::Commitment {
        doc /* commitment whereby something is formally assured if a party with primary liability fails to perform */
        // skos:definition xml:lang="en" (Guaranty.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-agr;Commitment"/>
    	// rdfs:label xml:lang="en" (Guaranty.rdf)
      // RDF: fibo-fbc-dae-gty:Guaranty
      // subClassOf: Commitment - from FIBO_FND_Agreements (AgreementsPackage)
    }

    item def <'insurance-backed guaranty'> InsuranceBackedGuaranty specializes Guaranty {
        doc /* guaranty that is realized as an insurance policy */
        // skos:definition xml:lang="en" (Guaranty.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-gty;Guaranty"/>
    	// rdfs:label xml:lang="en" (Guaranty.rdf)
      // RDF: fibo-fbc-dae-gty:InsuranceBackedGuaranty
    }

    item def <'insurance policy'> InsurancePolicy specializes Contracts::WrittenContract {
        doc /* contract that (1) puts an indemnity cover into effect, (2) serves as a legal evidence of the insurance agreement, (3) sets out the exact terms on which the indemnity cover has been provided, and (4) states associated information such as the (a) specific risks and perils covered, (b) duration of coverage, (c) amount of premium, (d) mode of premium payment, and (e) deductibles, if any */
        // skos:definition xml:lang="en" (Guaranty.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;WrittenContract"/>
    	// rdfs:label xml:lang="en" (Guaranty.rdf)
      // RDF: fibo-fbc-dae-gty:InsurancePolicy
      // subClassOf: WrittenContract - from FIBO_FND_Agreements (AgreementsPackage)
    }

    item def <'insurer'> Insurer specializes Contracts::ContractPrincipal, Financial_Products_and_Services::FinancialServiceProvider {
        doc /* financial service provider that issues an insurance policy */
        // skos:definition xml:lang="en" (Guaranty.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractPrincipal"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;FinancialServiceProvider"/>
    	// rdfs:label xml:lang="en" (Guaranty.rdf)
      // RDF: fibo-fbc-dae-gty:Insurer
    }

    item def <'joint guaranty'> JointGuaranty specializes Guaranty {
        doc /* guaranty provided by at least two parties, jointly and severally */
        // skos:definition xml:lang="en" (Guaranty.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-gty;Guaranty"/>
    	// rdfs:label xml:lang="en" (Guaranty.rdf)
      // RDF: fibo-fbc-dae-gty:JointGuaranty
    }

    item def <'letter of credit'> LetterOfCredit specializes Debt::CommittedCreditFacility {
        doc /* letter from a bank or other creditworthy institution guaranteeing that a buyer's payment to a seller will be received on time and for the correct amount */
        // skos:definition xml:lang="en" (Guaranty.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-dbt;CommittedCreditFacility"/>
    	// rdfs:label xml:lang="en" (Guaranty.rdf)
      // RDF: fibo-fbc-dae-gty:LetterOfCredit
    }

    item def <'letter of credit guaranty'> LetterOfCreditGuaranty specializes CollateralizedGuaranty {
        doc /* guaranty that takes the form of a letter of credit, i.e., a document issued by a bank guaranteeing the payment up to a stated amount for a specified period */
        // skos:definition xml:lang="en" (Guaranty.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-gty;CollateralizedGuaranty"/>
    	// rdfs:label xml:lang="en" (Guaranty.rdf)
      // RDF: fibo-fbc-dae-gty:LetterOfCreditGuaranty
    }

    item def <'negative pledge'> NegativePledge specializes Guaranty {
        doc /* guaranty whereby the issuer will not pledge any assets if doing so would result in less security for lender(s) or investor(s) */
        // skos:definition xml:lang="en" (Guaranty.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-gty;Guaranty"/>
    	// rdfs:label xml:lang="en" (Guaranty.rdf)
      // RDF: fibo-fbc-dae-gty:NegativePledge
    }

    item def <'policyholder'> Policyholder specializes Contracts::Counterparty {
        doc /* counterparty to and typically owner of an insurance policy */
        // skos:definition xml:lang="en" (Guaranty.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;Counterparty"/>
    	// rdfs:label xml:lang="en" (Guaranty.rdf)
      // RDF: fibo-fbc-dae-gty:Policyholder
      // subClassOf: Counterparty - from FIBO_FND_Agreements (AgreementsPackage)
    }

    item def <'priority level'> PriorityLevel specializes FiBO_FND_ExternalStubs::Classifier {
        doc /* relative ranking that a guaranty has in the context of a contract, for example for a credit enhancement priority */
        // skos:definition xml:lang="en" (Guaranty.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
    	// rdfs:label xml:lang="en" (Guaranty.rdf)
      // RDF: fibo-fbc-dae-gty:PriorityLevel
      // subClassOf: Classifier - external CMNS
    }

}
