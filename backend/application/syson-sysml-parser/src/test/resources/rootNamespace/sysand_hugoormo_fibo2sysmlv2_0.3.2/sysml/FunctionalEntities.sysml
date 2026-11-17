// ===================================================================
// FunctionalEntities.sysml
// Source-parity extracted from: FiBO_BE_FunctionalEntities.sysml
// Primary source module: FunctionalEntities.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Business Entities BE/FunctionalEntities
// Structural note: paired split with Publishers.sysml from the same source umbrella.
// ===================================================================
package Functional_Entities {
  doc/* This ontology defines the fundamental concepts for entities defined by their function, such as the relationship to the various forms which one or another functionally-defined entity may take. It also includes a number of basic types of entity defined by function, such as business and non-profit. The concepts in this ontology are intended to be extensible in other ontologies which may be dedicated to specific kinds of functionally-defined business entity or organization. (source: dct:abstract, FunctionalEntities.rdf) */
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

  item def <'association'> Association specializes Legal_Persons::NotForProfitOrganization {
      doc /* not-for-profit organization that is owned by and acts on behalf of its members */
      // skos:definition xml:lang="en" (FunctionalEntities.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-le-fbo;NotForProfitOrganization"/>
    	// rdfs:label xml:lang="en" (FunctionalEntities.rdf)
      // RDF: fibo-be-fct-fct:Association
    }

  item def <'cooperative society'> CooperativeSociety specializes FunctionalBusinessEntity {
      doc /* organization owned by and operated for the benefit of those using its services */
      // skos:definition xml:lang="en" (FunctionalEntities.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-fct-fct;FunctionalBusinessEntity"/>
    	// rdfs:label xml:lang="en" (FunctionalEntities.rdf)
      // RDF: fibo-be-fct-fct:CooperativeSociety
    }

  item def <'family office'> FamilyOffice specializes FunctionalBusinessEntity {
      doc /* organization that assumes the day-to-day administration and management of a family's affairs */
      // skos:definition xml:lang="en" (FunctionalEntities.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-fct-fct;FunctionalBusinessEntity"/>
    	// rdfs:label xml:lang="en" (FunctionalEntities.rdf)
      // RDF: fibo-be-fct-fct:FamilyOffice
    }

  item def <'functional business entity'> FunctionalBusinessEntity specializes FunctionalEntity {
      doc /* functional entity defined in terms of the nature of the commercial activity it conducts */
      // skos:definition xml:lang="en" (FunctionalEntities.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-fct-fct;FunctionalEntity"/>
    	// rdfs:label xml:lang="en" (FunctionalEntities.rdf)
      // RDF: fibo-be-fct-fct:FunctionalBusinessEntity
    }

  item def <'functional entity'> FunctionalEntity specializes FiBO_FND_ExternalStubs::FunctionalRole {
      doc /* party defined in terms of a function they or it performs */
      // skos:definition xml:lang="en" (FunctionalEntities.rdf)
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-rlcmp;FunctionalRole"/>
    	// rdfs:label xml:lang="en" (FunctionalEntities.rdf)
      // RDF: fibo-be-fct-fct:FunctionalEntity
    }

  item def <'merchant'> Merchant specializes FunctionalBusinessEntity {
      doc /* party engaged in the purchase and sales of goods produced by others for profit */
      // skos:definition xml:lang="en" (FunctionalEntities.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-fct-fct;FunctionalBusinessEntity"/>
    	// rdfs:label xml:lang="en" (FunctionalEntities.rdf)
      // RDF: fibo-be-fct-fct:Merchant
    }

  item def <'merchant category code'> MerchantCategoryCode specializes Classification_Schemes::IndustrySectorClassifier, FiBO_FND_ExternalStubs::CodeElement {
      doc /* code used internationally to classify a merchant */
      // skos:definition xml:lang="en" (FunctionalEntities.rdf)
      // rdfs:label xml:lang="en" (FunctionalEntities.rdf)
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-cls;IndustrySectorClassifier"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cds;CodeElement"/>
      // RDF: fibo-be-fct-fct:MerchantCategoryCode
      attribute codeValue[0..1];
    }

  item def <'merchant category code scheme'> MerchantCategoryCodeScheme specializes Classification_Schemes::IndustrySectorClassificationScheme {
      doc /* scheme defining a set of codes for classifying merchant services */
      // skos:definition xml:lang="en" (FunctionalEntities.rdf)
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-cls;IndustrySectorClassificationScheme"/>
    	// rdfs:label xml:lang="en" (FunctionalEntities.rdf)
      // RDF: fibo-be-fct-fct:MerchantCategoryCodeScheme
    }

  item def <'merchant identifier'> MerchantIdentifier specializes FiBO_FND_ExternalStubs::Identifier {
      doc /* unique identifier for a merchant that is used, for example, for transaction interchange purposes */
      // skos:definition xml:lang="en" (FunctionalEntities.rdf)
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-id;Identifier"/>
    	// rdfs:label xml:lang="en" (FunctionalEntities.rdf)
      // RDF: fibo-be-fct-fct:MerchantIdentifier
      attribute identifierValue[0..1];
    }

  item def OrganizationIdentifier {
      // RDF: fibo-be-fct-fct:OrganizationIdentifier
      attribute identifierValue[0..1];
    }

  item def ServiceProvider specializes FunctionalEntity {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-fct-fct;FunctionalEntity"/>
      // RDF: fibo-be-fct-fct:ServiceProvider
    }

  item def <'syndicate'> Syndicate specializes FiBO_FND_ExternalStubs::Organization {
      doc /* temporary, self-organizing group of people, companies, corporations or entities organized as an alliance whose purpose is to transact some specific business, or to pursue or promote a shared interest */
      // skos:definition xml:lang="en" (FunctionalEntities.rdf)
      // rdfs:label xml:lang="en" (FunctionalEntities.rdf)
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;Organization"/>
      // OWL-Restriction: <owl:onProperty rdf:resource="&cmns-col;hasMember"/>
      // OWL-Restriction: <owl:someValuesFrom rdf:resource="&cmns-pts;Party"/>
      // R9 addendum (approved role-chain flattening): materialized from paired
      // hasMember/isMemberOf role-chain evidence in FunctionalEntities.rdf.
      item syndicateMember: SyndicateMember [0..*];
      // RDF: fibo-be-fct-fct:Syndicate
    }

  item def <'syndicate member'> SyndicateMember specializes FiBO_FND_ExternalStubs::OrganizationMember, Parties::PartyRole {
      doc /* role of an individual or organization that is a member of an alliance set up to pool resources and share risk with respect to a large transaction */
      // skos:definition xml:lang="en" (FunctionalEntities.rdf)
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;OrganizationMember"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-pts;PartyRole"/>
    	// rdfs:label xml:lang="en" (FunctionalEntities.rdf)
      // RDF: fibo-be-fct-fct:SyndicateMember
    }

}

// --- fibo-be-fct-pub ---
