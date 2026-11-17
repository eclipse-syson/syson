// ===================================================================
// CreditRatings.sysml
// Source-parity extracted from: FiBO_FBC_DebtAndEquities.sysml
// Primary source module: CreditRatings.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Financial Business and Commerce FBC/DebtAndEquities
// Structural note: translated as a split file from FiBO_FBC_DebtAndEquities umbrella.
// ===================================================================
package Credit_Ratings {
  doc/* This ontology defines the concept of a credit rating, along with credit watch and outlook qualifying terms. There are credit ratings for individuals, for organizations and for instruments. These are referenced extensively in the securities models but are also applicable to business entities generally and in the context of lending and account maintenance. (source: dct:abstract, CreditRatings.rdf) */
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

    item def <'credit inquiry'> CreditInquiry specializes Reporting::RequestActivity {
      doc /* request from a lender to a credit repository to obtain information regarding a prospective borrower's creditworthiness */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-rep;RequestActivity"/>
    	// skos:definition xml:lang="en" (CreditRatings.rdf)
    	// rdfs:label xml:lang="en" (CreditRatings.rdf)
      // RDF: fibo-fbc-dae-crt:CreditInquiry
      // subClassOf: RequestActivity - from FIBO_FND_Arrangements (ArrangementsPackage)
    }

    item def <'credit inquiry type'> CreditInquiryType specializes FiBO_FND_ExternalStubs::Classifier {
      doc /* classifier indicating whether a credit inquiry is a result of a borrower's direct authorization or by some indirect means */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
    	// skos:definition xml:lang="en" (CreditRatings.rdf)
    	// rdfs:label xml:lang="en" (CreditRatings.rdf)
      // RDF: fibo-fbc-dae-crt:CreditInquiryType
      // subClassOf: Classifier - external CMNS
    }

    item def <'credit message'> CreditMessage specializes FiBO_FND_ExternalStubs::Notice {
      doc /* notice on a credit report to alert a reviewer of special or exceptional information */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-doc;Notice"/>
    	// skos:definition xml:lang="en" (CreditRatings.rdf)
    	// rdfs:label xml:lang="en" (CreditRatings.rdf)
      // RDF: fibo-fbc-dae-crt:CreditMessage
      // subClassOf: Notice - external CMNS
    }

    item def <'credit message type'> CreditMessageType specializes FiBO_FND_ExternalStubs::Classifier {
      doc /* classifier that categorizes credit messages */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
    	// skos:definition xml:lang="en" (CreditRatings.rdf)
    	// rdfs:label xml:lang="en" (CreditRatings.rdf)
      // RDF: fibo-fbc-dae-crt:CreditMessageType
      // subClassOf: Classifier - external CMNS
    }

    item def <'credit quality'> CreditQuality specializes Ratings::RatingScore {
      doc /* classifier for borrowers, rates, or holdings in the lending market with respect to the quality of the credit */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-rt;RatingScore"/>
    	// skos:definition xml:lang="en" (CreditRatings.rdf)
    	// rdfs:label xml:lang="en" (CreditRatings.rdf)
      // RDF: fibo-fbc-dae-crt:CreditQuality
      // subClassOf: RatingScore - from FIBO_FND_Arrangements (ArrangementsPackage)
    }

    item def <'credit rating'> CreditRating specializes Ratings::Rating {
      doc /* assessment of creditworthiness of a borrower generally or with respect to a particular debt or financial obligation */
      // skos:definition xml:lang="en" (CreditRatings.rdf)
      // rdfs:label xml:lang="en" (CreditRatings.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-rt;Rating"/>
      // RDF: fibo-fbc-dae-crt:CreditRating
      // subClassOf: Rating - from FIBO_FND_Arrangements (ArrangementsPackage)
    }

    item def <'credit rating agency'> CreditRatingAgency specializes Financial_Products_and_Services::FinancialServiceProvider, Ratings::RatingAgency {
      doc /* rating agency that publishes reports assessing the creditworthiness of a borrower or legal entity, either generally or with respect to a specific obligation */
      // skos:definition xml:lang="en" (CreditRatings.rdf)
      // rdfs:label xml:lang="en" (CreditRatings.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;FinancialServiceProvider"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-rt;RatingAgency"/>
      // RDF: fibo-fbc-dae-crt:CreditRatingAgency
      // subClassOf: RatingAgency - from FIBO_FND_Arrangements (ArrangementsPackage)
    }

    item def <'credit rating model'> CreditRatingModel specializes Analytics::Expression {
      doc /* algorithm for computing a credit rating */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-qtu;Expression"/>
    	// skos:definition xml:lang="en" (CreditRatings.rdf)
    	// rdfs:label xml:lang="en" (CreditRatings.rdf)
      // RDF: fibo-fbc-dae-crt:CreditRatingModel
      // subClassOf: Expression - from FIBO_FND_Accounting (CurrencyAmount)
    }

    item def <'credit score model type'> CreditRatingModelType specializes FiBO_FND_ExternalStubs::Classifier {
      doc /* a type corresonding to a family of credit scoring algorithms sharing common characteristics */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
    	// skos:definition xml:lang="en" (CreditRatings.rdf)
    	// rdfs:label xml:lang="en" (CreditRatings.rdf)
      // RDF: fibo-fbc-dae-crt:CreditRatingModelType
      // subClassOf: Classifier - external CMNS
    }

    item def <'credit rating scale'> CreditRatingScale specializes Ratings::RatingScale {
      doc /* rating scale for assigning a credit rating to individual, entity, or instrument, including defining a set of codes or numeric scores and indicating how an assessment that is assigned a score based on that scale means */
      // skos:definition xml:lang="en" (CreditRatings.rdf)
      // rdfs:label xml:lang="en" (CreditRatings.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-rt;RatingScale"/>
      // RDF: fibo-fbc-dae-crt:CreditRatingScale
      // subClassOf: RatingScale - from FIBO_FND_Arrangements (ArrangementsPackage)
    }

    item def <'credit report'> CreditReport specializes Ratings::RatingReport {
      doc /* report describing the creditworthiness and related credit attributes of a borrower */
      // skos:definition xml:lang="en" (CreditRatings.rdf)
      // rdfs:label xml:lang="en" (CreditRatings.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-rt;RatingReport"/>
      // RDF: fibo-fbc-dae-crt:CreditReport
      // subClassOf: RatingReport - from FIBO_FND_Arrangements (ArrangementsPackage)
    }

    item def <'credit report category'> CreditReportCategory specializes FiBO_FND_ExternalStubs::Classifier {
      doc /* classifier for credit reports, often available from multiple vendors */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
    	// skos:definition xml:lang="en" (CreditRatings.rdf)
    	// rdfs:label xml:lang="en" (CreditRatings.rdf)
      // RDF: fibo-fbc-dae-crt:CreditReportCategory
      // subClassOf: Classifier - external CMNS
    }

    item def <'credit report product'> CreditReportProduct specializes Financial_Products_and_Services::FinancialProduct, Products_and_Services::OffTheShelfProduct {
      doc /* branded credit report offered in the marketplace */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;FinancialProduct"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-pas;OffTheShelfProduct"/>
    	// skos:definition xml:lang="en" (CreditRatings.rdf)
    	// rdfs:label xml:lang="en" (CreditRatings.rdf)
      // RDF: fibo-fbc-dae-crt:CreditReportProduct
      // subClassOf: OffTheShelfProduct - from FIBO_FND_ProductsAndServices (ProductsServicesPackage)
    }

    item def <'credit tradeline'> CreditTradeline specializes Reporting::Report {
      doc /* report derived from the transaction history of a credit account */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-rep;Report"/>
    	// skos:definition xml:lang="en" (CreditRatings.rdf)
    	// rdfs:label xml:lang="en" (CreditRatings.rdf)
      // RDF: fibo-fbc-dae-crt:CreditTradeline
      // subClassOf: Report - external CMNS
    }

    item def <'credit watch direction'> CreditWatchDirection specializes FiBO_FND_ExternalStubs::Classifier {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
      // RDF: fibo-fbc-dae-crt:CreditWatchDirection
      // subClassOf: Classifier - external CMNS
    }

    item def <'credit watch outlook'> CreditWatchOutlook specializes Ratings::Rating {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-rt;Rating"/>
    	// skos:definition xml:lang="en" (CreditRatings.rdf)
    	// rdfs:label xml:lang="en" (CreditRatings.rdf)
      // RDF: fibo-fbc-dae-crt:CreditWatchOutlook
      // subClassOf: Rating - from FIBO_FND_Arrangements (ArrangementsPackage)
    }

    item def <'instrument credit rating'> InstrumentCreditRating specializes InvestmentCreditRating {
      doc /* investment credit rating that provides an opinion of creditworthiness of an instrument, typically with some relationship to the creditworthiness of the issuer(s) */
      // skos:definition xml:lang="en" (CreditRatings.rdf)
      // rdfs:label xml:lang="en" (CreditRatings.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-crt;InvestmentCreditRating"/>
      // RDF: fibo-fbc-dae-crt:InstrumentCreditRating
    }

    item def <'investment credit rating'> InvestmentCreditRating specializes CreditRating {
      doc /* credit rating that provides an opinion of creditworthiness of an investment, including but not limited to an instrument or organization */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-crt;CreditRating"/>
    	// skos:definition xml:lang="en" (CreditRatings.rdf)
    	// rdfs:label xml:lang="en" (CreditRatings.rdf)
      // RDF: fibo-fbc-dae-crt:InvestmentCreditRating
    }

    item def <'organization credit rating'> OrganizationCreditRating specializes CreditRating {
      doc /* credit rating that provides an opinion of creditworthiness of an organization */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-crt;CreditRating"/>
    	// skos:definition xml:lang="en" (CreditRatings.rdf)
    	// rdfs:label xml:lang="en" (CreditRatings.rdf)
      // RDF: fibo-fbc-dae-crt:OrganizationCreditRating
    }

    item def <'short term credit rating'> ShortTermCreditRating specializes InvestmentCreditRating {
      doc /* credit rating that provides an opinion of the probability of an individual or organization going into default within a year */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-dae-crt;InvestmentCreditRating"/>
    	// skos:definition xml:lang="en" (CreditRatings.rdf)
    	// rdfs:label xml:lang="en" (CreditRatings.rdf)
      // RDF: fibo-fbc-dae-crt:ShortTermCreditRating
    }

}

// --- fibo-fbc-dae-dbt (FBC_DebtPackage) ---
