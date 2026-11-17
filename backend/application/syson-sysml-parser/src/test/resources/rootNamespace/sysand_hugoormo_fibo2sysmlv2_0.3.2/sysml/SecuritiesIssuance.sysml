// ===================================================================
// SecuritiesIssuance.sysml
// Source-parity extracted from: FiBO_SEC_Securities.sysml
// Primary source module: SecuritiesIssuance.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Securities SEC/Securities
// ===================================================================
package Securities_Issuance {
  doc/* This ontology defines the fundamental concepts for issuing securities, including securities offering, offering document, offering statement, securities underwriter, prospectus, and so forth. (source: dct:abstract, SecuritiesIssuance.rdf) */
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

  item def <'best efforts offering'> BestEffortsOffering specializes Issuance_Process::SecuritiesOffering {
    	doc /* securities offering whereby investment bankers commit to doing their best to sell the securities offered, but do not assume the full risk of an underwriter */
    	// skos:definition xml:lang="en" (SecuritiesIssuance.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesIssuance.rdf)
    	// RDF: fibo-sec-sec-iss:BestEffortsOffering
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-iss;SecuritiesOffering"/>
    }

  item def <'conversion terms'> ConversionTerms specializes Financial_Instruments::RedemptionProvision {
    	doc /* contract terms specifying when and how a security may be converted to another security (usually of the same issuer) */
    	// skos:definition xml:lang="en" (SecuritiesIssuance.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesIssuance.rdf)
    	// RDF: fibo-sec-sec-iss:ConversionTerms
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;RedemptionProvision"/>
    }

  item def <'convertible security'> ConvertibleSecurity specializes Financial_Instruments::Security {
    	doc /* security that can be converted into another security */
    	// skos:definition xml:lang="en" (SecuritiesIssuance.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesIssuance.rdf)
    	// RDF: fibo-sec-sec-iss:ConvertibleSecurity
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;Security"/>
    }

  item def <'exempt issuer'> ExemptIssuer specializes Financial_Instruments::Issuer {
    	doc /* issuer that issues securities that are excused from certain regulatory reporting requirements */
    	// skos:definition xml:lang="en" (SecuritiesIssuance.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesIssuance.rdf)
    	// RDF: fibo-sec-sec-iss:ExemptIssuer
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;Issuer"/>
    }

  item def <'exempt offering'> ExemptOffering specializes PublicOffering {
    	doc /* public offering involving securities that are excused from certain regulatory reporting requirements */
    	// skos:definition xml:lang="en" (SecuritiesIssuance.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesIssuance.rdf)
    	// RDF: fibo-sec-sec-iss:ExemptOffering
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-iss;PublicOffering"/>
    }

  item def <'exempt transaction'> ExemptTransaction specializes Financial_Instruments::SecuritiesTransaction {
    	doc /* securities transaction for which there is no requirement to register the transaction with a regulatory agency */
    	// skos:definition xml:lang="en" (SecuritiesIssuance.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesIssuance.rdf)
    	// RDF: fibo-sec-sec-iss:ExemptTransaction
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;SecuritiesTransaction"/>
    }

  item def <'financial instrument short name'> FinancialInstrumentShortName specializes FiBO_FND_ExternalStubs::Identifier {
    	doc /* abbreviated name for a financial instrument within a defined structure as specified in ISO 18774 */
    	// skos:definition xml:lang="en" (SecuritiesIssuance.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesIssuance.rdf)
    	// RDF: fibo-sec-sec-iss:FinancialInstrumentShortName
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-id;Identifier"/>
    }

  item def <'firm commitment offering'> FirmCommitmentOffering specializes Issuance_Process::SecuritiesOffering {
    	doc /* securities offering whereby the underwriter purchases the securities outright for their own account */
    	// skos:definition xml:lang="en" (SecuritiesIssuance.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesIssuance.rdf)
    	// RDF: fibo-sec-sec-iss:FirmCommitmentOffering
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-iss;SecuritiesOffering"/>
    }

  item def <'offering document'> OfferingDocument specializes Publishers::Publication, FiBO_FND_ExternalStubs::LegalDocument {
    	doc /* legal document that states the objectives, risks and terms of an investment */
    	// skos:definition xml:lang="en" (SecuritiesIssuance.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesIssuance.rdf)
    	// RDF: fibo-sec-sec-iss:OfferingDocument
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-fct-pub;Publication"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-doc;LegalDocument"/>
    }

  item def <'offering statement'> OfferingStatement specializes IssuanceDocuments::OfferingDocument {
    	doc /* offering memorandum that conforms to Regulation A, Offering Statement, of the Securities Act of 1933 */
    	// skos:definition xml:lang="en" (SecuritiesIssuance.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesIssuance.rdf)
    	// RDF: fibo-sec-sec-iss:OfferingStatement
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-iss;OfferingDocument"/>
    }

  item def <'private offering'> PrivateOffering specializes Issuance_Process::SecuritiesOffering {
    	doc /* offering of securities made privately to a limited number of qualified potential investors */
    	// skos:definition xml:lang="en" (SecuritiesIssuance.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesIssuance.rdf)
    	// RDF: fibo-sec-sec-iss:PrivateOffering
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-iss;SecuritiesOffering"/>
    }

  item def <'private placement memorandum'> PrivatePlacementMemorandum specializes IssuanceDocuments::OfferingDocument {
    	doc /* legal document stating the objectives, risks and terms of investment involved with a private placement */
    	// skos:definition xml:lang="en" (SecuritiesIssuance.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesIssuance.rdf)
    	// RDF: fibo-sec-sec-iss:PrivatePlacementMemorandum
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-iss;OfferingDocument"/>
    }

  item def <'public offering'> PublicOffering specializes Issuance_Process::SecuritiesOffering {
    	doc /* offering of securities for sale to the investment public, after compliance with registration requirements of the relevant regulatory authorities */
    	// skos:definition xml:lang="en" (SecuritiesIssuance.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesIssuance.rdf)
    	// RDF: fibo-sec-sec-iss:PublicOffering
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-sec-sec-iss;SecuritiesOffering"/>
    }

  item def <'securities offering'> SecuritiesOffering specializes Financial_Products_and_Services::Offering, Agreements::Agreement{ // RDF: fibo-sec-sec-iss:SecuritiesOffering
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-agr;Agreement"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;Offering"/>
        // RDF: fibo-sec-sec-iss:SecuritiesOffering
    }

  item def <'security form'> SecurityForm specializes Contracts::ContractDocument {
    	doc /* nature of the proof of ownership of a security */
    	// skos:definition xml:lang="en" (SecuritiesIssuance.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesIssuance.rdf)
    	// RDF: fibo-sec-sec-iss:SecurityForm
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractDocument"/>
    }

  item def <'security underwriter'> SecurityUnderwriter specializes Financial_Services_Entities::Underwriter, Financial_Products_and_Services::ThirdPartyAgent, Contracts::ContractThirdParty {
    	doc /* party that has purchased from an issuer with a view to, or sells for an issuer in connection with, the distribution of any security, or participates or has a direct or indirect participation in any such undertaking, or participates or has a participation in the direct or indirect underwriting of any such undertaking */
    	// skos:definition xml:lang="en" (SecuritiesIssuance.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesIssuance.rdf)
    	// RDF: fibo-sec-sec-iss:SecurityUnderwriter
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;Underwriter"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;ThirdPartyAgent"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;ContractThirdParty"/>
    }

  item def <'security underwriting arrangement'> SecurityUnderwritingArrangement specializes Financial_Services_Entities::UnderwritingArrangement {
    	doc /* underwriting agreement between an organization (typically an investment bank) and a securities issuer that commits the underwriter to assuming risk involved in buying a new issue of securities and reselling it to the public */
    	// skos:definition xml:lang="en" (SecuritiesIssuance.rdf)
    	// rdfs:label xml:lang="en" (SecuritiesIssuance.rdf)
    	// RDF: fibo-sec-sec-iss:SecurityUnderwritingArrangement
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;UnderwritingArrangement"/>
    }

}

// --- fibo-sec-sec-lst ---
