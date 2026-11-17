// ===================================================================
// USFinancialServicesEntities.sysml
// Source-parity extracted from: FiBO_FBC_FunctionalEntities.sysml (split)
// Primary source modules: USNationalInformationCenterControlledVocabularies.rdf; USNationalInformationCenterControlledVocabularies.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Financial Business and Commerce FBC/FunctionalEntities/NorthAmericanEntities
// Structural note: translated as a split file from FiBO_FBC_FunctionalEntities umbrella with NorthAmericanEntities lineage.
// ===================================================================
package US_National_Bank_Institutions {
  doc/* RDF: fibo-fbc-fct-nic */
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
item def <'National Information Center (NIC) controlled vocabulary'> NationalInformationCenterControlledVocabulary specializes FiBO_FND_ExternalStubs::Classifier, FiBO_FND_ExternalStubs::CodeElement {
    doc /* controlled vocabulary that characterizes some feature or aspect of content about a financial service provider managed in the National Information Center (NIC) repository */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cds;CodeElement"/>
    	// skos:definition xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
    	// rdfs:label xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
      // RDF: fibo-fbc-fct-nic:NationalInformationCenterControlledVocabulary
      // subClassOf: Classifier, CodeElement - external CMNS
    }

item def <'National Information Center (NIC) entity type classifier'> NICEntityTypeClassifier specializes NationalInformationCenterControlledVocabulary {
    doc /* controlled vocabulary and codes for describing the nature of the functional entities managed by the National Information Center (NIC) repository */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-usnic;NationalInformationCenterControlledVocabulary"/>
    	// skos:definition xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
    	// rdfs:label xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
      // RDF: fibo-fbc-fct-nic:NICEntityTypeClassifier
    }

item def <'agreement corporation'> AgreementCorporation specializes Financial_Services_Entities::FinancialInstitution {
    doc /* corporation chartered by a state to engage in international banking, so named because the corporation enters into an 'agreement' with the Fed's Board of Governors that it will limit its activities to those permitted */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;FinancialInstitution"/>
    	// skos:definition xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
    	// rdfs:label xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
      // RDF: fibo-fbc-fct-usjfse:AgreementCorporation
    }

item def <'bankers&apos; bank'> BankersBank specializes Financial_Services_Entities::CommercialBank, USBank {
    doc /* commercial bank owned exclusively (except to the extent directors' qualifying shares are required by law) by other depository institutions or depository institution holding companies, the activities of which are limited by its articles of association exclusively to providing services to or for other depository institutions, their holding companies, and the officers, directors, and employees of such institutions and companies, and to providing correspondent banking services at the request of other depository institutions or their holding companies */
      // skos:definition xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
      // rdfs:label xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;CommercialBank"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-usfse;USBank"/>
      // RDF: fibo-fbc-fct-usjfse:BankersBank
    }

item def <'branch of a depository institution'> BranchOfADepositoryInstitution specializes Legal_Persons::Branch {
    doc /* any office or any place of business located in any State of the United States at which deposits are received */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-le-fbo;Branch"/>
    	// skos:definition xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
    	// rdfs:label xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
      // RDF: fibo-fbc-fct-usjfse:BranchOfADepositoryInstitution
      // subClassOf: Branch - from FiBO_BE_LegalEntities (LegalEntities)
    }

item def <'cooperative bank'> CooperativeBank specializes ThriftInstitution {
    doc /* state-chartered savings association that is organized and operates according to the laws of the state in which it is chartered or organized, including Massachusetts, New Hampshire, Rhode Island or Vermont */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-usfse;ThriftInstitution"/>
    	// skos:definition xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
    	// rdfs:label xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
      // RDF: fibo-fbc-fct-usjfse:CooperativeBank
    }

item def <'domestic branch of a foreign bank'> DomesticBranchOfAForeignBank specializes BranchOfADepositoryInstitution, FiBO_BE_CorporationTypes_US::OfficeOfAForeignBank{
    doc /* place of business of a foreign bank, located in any state, at which deposits are received, and that is not an agency as defined in https://www.ecfr.gov/current/title-12/chapter-II/subchapter-A/part-211/subpart-B/section-211.21 */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-usfse;OfficeOfAForeignBank"/> (parent not yet in model)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-usfse;BranchOfADepositoryInstitution"/>
    	// skos:definition xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
    	// rdfs:label xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
      // RDF: fibo-fbc-fct-usjfse:DomesticBranchOfAForeignBank
    }

item def <'domestic entity'> DomesticEntity specializes Financial_Products_and_Services::FinancialServiceProvider {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;FinancialServiceProvider"/>
    	doc /* financial service provider that is incorporated and domiciled in the United States */
    	// skos:definition xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
    	// rdfs:label xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
      // RDF: fibo-fbc-fct-usjfse:DomesticEntity
    }

item def <'edge corporation'> EdgeCorporation specializes Financial_Services_Entities::FinancialInstitution {
    doc /* corporation chartered by the Federal Reserve to engage in international banking and financial operations */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;FinancialInstitution"/>
    	// skos:definition xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
    	// rdfs:label xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
      // RDF: fibo-fbc-fct-usjfse:EdgeCorporation
    }

item def <'farm credit system institution'> FarmCreditSystemInstitution specializes Financial_Services_Entities::FinancialInstitution, DomesticEntity {
    doc /* federally-chartered financial institution that is supervised, examined, and regulated by the Farm Credit Administration and operates in accordance with the Farm Credit Act of 1971, as amended, 12 U.S.C. 2001 et seq */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;FinancialInstitution"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-usfse;DomesticEntity"/>
    	// skos:definition xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
    	// rdfs:label xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
      // RDF: fibo-fbc-fct-usjfse:FarmCreditSystemInstitution
    }

item def <'federal credit union'> FederalCreditUnion specializes USCreditUnion {
    doc /* credit union that has a federal affiliation */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-usfse;USCreditUnion"/>
    	// skos:definition xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
    	// rdfs:label xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
      // RDF: fibo-fbc-fct-usjfse:FederalCreditUnion
    }

item def <'federal savings bank'> FederalSavingsBank specializes SavingsBank {
    doc /* savings bank that has a federal affiliation */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-usfse;SavingsBank"/>
    	// skos:definition xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
    	// rdfs:label xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
      // RDF: fibo-fbc-fct-usjfse:FederalSavingsBank
    }

item def <'financial holding company'> FinancialHoldingCompany specializes Financial_Services_Entities::BankHoldingCompany {
    doc /* financial entity engaged in a broad range of banking-related activities as permitted under the Gramm-Leach-Bliley Act of 1999 */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;BankHoldingCompany"/>
    	// skos:definition xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
    	// rdfs:label xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
      // RDF: fibo-fbc-fct-usjfse:FinancialHoldingCompany
    }

item def <'foreign bank'> ForeignBank specializes Financial_Services_Entities::Bank {
    doc /* financial institution organized under the laws of a foreign country, a territory of the United States, Puerto Rico, Guam, American Samoa, or the Virgin Islands, which engages in the business of banking, or any subsidiary or affiliate, organized under such laws, of any such institution */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;Bank"/>
    	// skos:definition xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
    	// rdfs:label xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
      // RDF: fibo-fbc-fct-usjfse:ForeignBank
    }

item def <'foreign banking organization'> ForeignBankingOrganization specializes Financial_Products_and_Services::FinancialServiceProvider {
    doc /* financial service provider that is headquartered outside the United States and that can acquire or establish freestanding banks or bank holding companies in the United States */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;FinancialServiceProvider"/>
    	// skos:definition xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
    	// rdfs:label xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
      // RDF: fibo-fbc-fct-usjfse:ForeignBankingOrganization
    }

item def <'industrial bank'> IndustrialBank specializes Financial_Services_Entities::Bank {
    doc /* limited service financial institution that raises funds by selling certificates called 'investment shares' and by accepting deposits */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;Bank"/>
    	// skos:definition xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
    	// rdfs:label xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
      // RDF: fibo-fbc-fct-usjfse:IndustrialBank
    }

item def <'intermediate holding company'> IntermediateHoldingCompany specializes Financial_Services_Entities::BankHoldingCompany {
    doc /* bank holding company established or designated by a foreign banking organization under subpart O of the Federal Reserve Board's Regulation YY (12 CFR part 252) */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;BankHoldingCompany"/>
    	// skos:definition xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
    	// rdfs:label xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
      // RDF: fibo-fbc-fct-usjfse:IntermediateHoldingCompany
    }

item def <'mutual savings bank'> MutualSavingsBank specializes SavingsBank {
    doc /* financial institution that accepts deposits primarily from individuals and places a large portion of its funds into mortgage loans */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-usfse;SavingsBank"/>
    	// skos:definition xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
    	// rdfs:label xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
      // RDF: fibo-fbc-fct-usjfse:MutualSavingsBank
    }

item def <'national bank'> NationalBank specializes Financial_Services_Entities::CommercialBank, USBank, FederalReserveSystemMember{
    doc /* commercial bank whose charter is approved by the Office of the Comptroller of the Currency (OCC) rather than by a state banking agency */
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-usjrga;FederalReserveSystemMember"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;CommercialBank"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-usfse;USBank"/>
    	// skos:definition xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
    	// rdfs:label xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
      // RDF: fibo-fbc-fct-usjfse:NationalBank
      // Note: also FederalReserveSystemMember - via USNationalInformationCenterControlledVocabularies.rdf
    }

item def <'savings bank'> SavingsBank specializes ThriftInstitution {
    doc /* banking institution organized to accept savings deposits and pay interest on those savings deposits */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-usfse;ThriftInstitution"/>
    	// skos:definition xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
    	// rdfs:label xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
      // RDF: fibo-fbc-fct-usjfse:SavingsBank
    }

item def <'savings loan association'> SavingsLoanAssociation specializes ThriftInstitution {
    doc /* financial institution that accepts deposits primarily from individuals and channels its funds primarily into residential mortgage loans */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-usfse;ThriftInstitution"/>
    	// skos:definition xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
    	// rdfs:label xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
      // RDF: fibo-fbc-fct-usjfse:SavingsLoanAssociation
    }

item def <'savings loan holding company'> SavingsLoanHoldingCompany specializes Financial_Services_Entities::HoldingCompany, Financial_Products_and_Services::FinancialServiceProvider {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;HoldingCompany"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;FinancialServiceProvider"/>
    	doc /* company that directly or indirectly controls a savings association or related holding company, and explicitly excludes any company that is also a bank holding company */
    	// skos:definition xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
    	// rdfs:label xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
      // RDF: fibo-fbc-fct-usjfse:SavingsLoanHoldingCompany
    }

item def <'state-chartered bank'> StateCharteredBank specializes Financial_Services_Entities::CommercialBank, USBank {
    doc /* commercial bank whose charter is approved by a state banking regulator */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;CommercialBank"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-usfse;USBank"/>
    	// skos:definition xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
    	// rdfs:label xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
      // RDF: fibo-fbc-fct-usjfse:StateCharteredBank
    }

item def <'state savings bank'> StateSavingsBank specializes SavingsBank {
    doc /* savings bank that has a state affiliation */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-usfse;SavingsBank"/>
    	// skos:definition xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
    	// rdfs:label xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
      // RDF: fibo-fbc-fct-usjfse:StateSavingsBank
    }

item def <'thrift institution'> ThriftInstitution specializes Financial_Services_Entities::SavingsAssociation, DomesticEntity {
    doc /* savings association that primarily accepts savings account deposits and invests most of the proceeds in mortgages */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;SavingsAssociation"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-usfse;DomesticEntity"/>
    	// skos:definition xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
    	// rdfs:label xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
      // RDF: fibo-fbc-fct-usjfse:ThriftInstitution
    }

item def <'U.S. bank'> USBank specializes Financial_Services_Entities::Bank, DomesticEntity {
    doc /* bank that is licensed to conduct business in the United States */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;Bank"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-usfse;DomesticEntity"/>
    	// skos:definition xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
    	// rdfs:label xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
      // RDF: fibo-fbc-fct-usjfse:USBank
    }

item def <'U.S. bank holding company'> USBankHoldingCompany specializes Financial_Services_Entities::BankHoldingCompany, DomesticEntity {
    doc /* bank holding company that is licensed to conduct business in the United States and is regulated and supervised by the Federal Reserve in accordance with the Bank Holding Company Act of 1956 */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;BankHoldingCompany"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-usfse;DomesticEntity"/>
    	// skos:definition xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
    	// rdfs:label xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
      // RDF: fibo-fbc-fct-usjfse:USBankHoldingCompany
    }

item def <'U.S. credit union'> USCreditUnion specializes Financial_Services_Entities::CreditUnion, ThriftInstitution {
    doc /* cooperative association organized for the purpose of promoting thrift among its members and creating a source of credit for provident or productive purposes */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;CreditUnion"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-usfse;ThriftInstitution"/>
    	// skos:definition xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
    	// rdfs:label xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
      // RDF: fibo-fbc-fct-usjfse:USCreditUnion
    }

item def <'agency of a foreign banking organization'> AgencyOfAForeignBankingOrganization specializes Financial_Services_Entities::FinancialInstitution {
    doc /* place of business of a foreign bank, located in any state, at which credit balances are maintained, checks are paid, money is lent, or, to the extent not prohibited by state or federal law, deposits are accepted from a person or entity that is not a citizen or resident of the United States */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;FinancialInstitution"/>
    	// skos:definition xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
    	// rdfs:label xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
      // RDF: fibo-fbc-fct-usjrga:AgencyOfAForeignBankingOrganization
    }

item def <'ABA IIN registry entry'> ABAIINRegistryEntry specializes FiBO_FND_ExternalStubs::RegistryEntry {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-ra;RegistryEntry"/>
    	doc /* an entry in the ABA IIN registry, a repository of financial institution characteristics collected by the ABA for those institutions to which they issue IINs */
    	// skos:definition xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
    	// rdfs:label xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
      // RDF: fibo-fbc-fct-usrga:ABAIINRegistryEntry
      // subClassOf: RegistryEntry - external CMNS
    }

item def <'ABA RTN registry entry'> ABARTNRegistryEntry specializes FiBO_FND_ExternalStubs::RegistryEntry {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-ra;RegistryEntry"/>
    	doc /* an entry in the ABA RTN registry, a repository of financial institution characteristics collected by the ABA Registrar on behalf of the ABA */
    	// skos:definition xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
    	// rdfs:label xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
      // RDF: fibo-fbc-fct-usrga:ABARTNRegistryEntry
      // subClassOf: RegistryEntry - external CMNS
    }

item def <'employer identification number'> EmployerIdentificationNumber specializes TaxpayerIdentificationNumber, Functional_Entities::OrganizationIdentifier {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-usjrga;TaxpayerIdentificationNumber"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;OrganizationIdentifier"/>
    	doc /* unique nine-digit number assigned by the Internal Revenue Service (IRS) to business entities operating in the United States for the purposes of identification */
    	// skos:definition xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
    	// rdfs:label xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
      // RDF: fibo-fbc-fct-usrga:EmployerIdentificationNumber
      // subClassOf: TaxpayerIdentificationNumber (same pkg); OrganizationIdentifier - from FiBO_BE_FunctionalEntities
    }

item def <'employer identification numbering scheme'> EmployerIdentificationNumberingScheme specializes TaxpayerIdentificationNumberingScheme {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-usjrga;TaxpayerIdentificationNumberingScheme"/>
    	doc /* taxpayer identification numbering scheme used in the United States to identify business entities */
    	// skos:definition xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
    	// rdfs:label xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
      // RDF: fibo-fbc-fct-usrga:EmployerIdentificationNumberingScheme
    }

item def <'FDIC Certificate Number'> FDICCertificateNumber specializes Financial_Services_Entities::FinancialServiceProviderIdentifier, FiBO_FND_ExternalStubs::RegisteredIdentifier {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;FinancialServiceProviderIdentifier"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-ra;RegisteredIdentifier"/>
    	doc /* identifier issued to a depository institution by the FDIC on approval of that institution's application for insurance */
    	// skos:definition xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
    	// rdfs:label xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
      // RDF: fibo-fbc-fct-usrga:FDICCertificateNumber
      // subClassOf: RegisteredIdentifier - external CMNS
    }

item def <'FDIC registry entry'> FDICRegistryEntry specializes FiBO_FND_ExternalStubs::RegistryEntry {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-ra;RegistryEntry"/>
    	doc /* an entry in the FDIC institution directory, a repository of financial institution characteristics collected by the FDIC related to the institutions they insure */
    	// skos:definition xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
    	// rdfs:label xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
      // RDF: fibo-fbc-fct-usrga:FDICRegistryEntry
      // subClassOf: RegistryEntry - external CMNS
    }

item def <'federal government entity'> FederalGovernmentEntity specializes Government_Entities::GovernmentBody {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-ge-ge;GovernmentBody"/>
    	doc /* formal organization that is an independent agency, instrumentality or other permanent or semi-permanent organization in the machinery of government in the United States, authorized by the executive branch or by Congress, that operates at the national (federal) level */
    	// skos:definition xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
    	// rdfs:label xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
      // RDF: fibo-fbc-fct-usrga:FederalGovernmentEntity
      // subClassOf: GovernmentBody - from FiBO_BE_GovernmentEntities (GovernmentEntities)
    }

item def <'Federal Reserve district'> FederalReserveDistrict specializes FiBO_FND_ExternalStubs::Jurisdiction {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-rga;Jurisdiction"/>
    	doc /* a region of the US identifying the jurisdiction of a Federal Reserve Bank, numbered and named for the city in which that reserve bank is located */
    	// skos:definition xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
    	// rdfs:label xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
      // RDF: fibo-fbc-fct-usrga:FederalReserveDistrict
      // subClassOf: Jurisdiction - external CMNS
    }

item def <'Federal Reserve district bank'> FederalReserveDistrictBank specializes Financial_Services_Entities::CentralBank, FederalReserveSystemMember {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;CentralBank"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-usjrga;FederalReserveSystemMember"/>
    	doc /* Federal Reserve district and member bank, with jurisdiction over a specific region of the US, named for the city in which the reserve bank is located */
    	// skos:definition xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
    	// rdfs:label xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
      // RDF: fibo-fbc-fct-usrga:FederalReserveDistrictBank
    }

item def <'Federal Reserve district identifier'> FederalReserveDistrictIdentifier specializes FiBO_FND_ExternalStubs::GeographicRegionIdentifier {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-loc;GeographicRegionIdentifier"/>
    	doc /* identifier associated with a Federal Reserve district */
    	// skos:definition xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
    	// rdfs:label xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
      // RDF: fibo-fbc-fct-usrga:FederalReserveDistrictIdentifier
      // subClassOf: GeographicRegionIdentifier - external CMNS
    }

item def <'Federal Reserve System member'> FederalReserveSystemMember specializes Financial_Services_Entities::FinancialInstitution, FiBO_FND_ExternalStubs::OrganizationMember {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;FinancialInstitution"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;OrganizationMember"/>
    	doc /* financial institution that is a member of the Federal Reserve System (FRS) */
    	// skos:definition xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
    	// rdfs:label xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
      // RDF: fibo-fbc-fct-usrga:FederalReserveSystemMember
      // subClassOf: OrganizationMember - external CMNS
    }

item def <'Federal Reserve System non-member institution'> FederalReserveSystemNonMemberInstitution specializes Financial_Services_Entities::DepositoryInstitution {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;DepositoryInstitution"/>
      // RDF: fibo-fbc-fct-usrga:FederalReserveSystemNonMemberInstitution
    }

item def <'issuer identification number'> IssuerIdentificationNumber specializes Financial_Services_Entities::FinancialServiceProviderIdentifier, FiBO_FND_ExternalStubs::RegisteredIdentifier {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;FinancialServiceProviderIdentifier"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-ra;RegisteredIdentifier"/>
      // RDF: fibo-fbc-fct-usrga:IssuerIdentificationNumber
      // subClassOf: RegisteredIdentifier - external CMNS
    }

item def <'National Information Center (NIC) registry entry'> NICRegistryEntry specializes FiBO_FND_ExternalStubs::RegistryEntry {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-ra;RegistryEntry"/>
    	doc /* an entry in the the National Information Center (NIC) repository, a repository of financial data and institution characteristics collected by the Federal Reserve System */
    	// skos:definition xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
    	// rdfs:label xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
      // RDF: fibo-fbc-fct-usrga:NICRegistryEntry
      // subClassOf: RegistryEntry - external CMNS
    }

item def <'primary federal regulator'> PrimaryFederalRegulator specializes Regulatory_Agencies::RegulatoryAgency {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-rga;RegulatoryAgency"/>
    	doc /* federal regulatory agency that is designated as the main agency responsible for oversight of a given institution for an institution */
    	// skos:definition xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
    	// rdfs:label xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
      // RDF: fibo-fbc-fct-usrga:PrimaryFederalRegulator
    }

item def <'Research, Statistics, Supervision and Regulation, and Discount and Credit identifier'> ResearchStatisticsSupervisionDiscountIdentifier specializes Financial_Services_Entities::FinancialServiceProviderIdentifier, FiBO_FND_ExternalStubs::RegisteredIdentifier {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;FinancialServiceProviderIdentifier"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-ra;RegisteredIdentifier"/>
    	doc /* unique identifier assigned by the Federal Reserve to financial institutions for regulatory and oversight purposes */
    	// skos:definition xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
    	// rdfs:label xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
      // RDF: fibo-fbc-fct-usrga:ResearchStatisticsSupervisionDiscountIdentifier
      // subClassOf: RegisteredIdentifier - external CMNS
    }

item def <'routing transit number'> RoutingTransitNumber specializes Financial_Services_Entities::FinancialServiceProviderIdentifier, FiBO_FND_ExternalStubs::RegisteredIdentifier {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;FinancialServiceProviderIdentifier"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-ra;RegisteredIdentifier"/>
    	doc /* unique nine digit identifier, used primarily in the United States, to identify a banking or other financial institution for clearing funds, and, as it appears on a check, denotes the banking institution that holds the account from which funds are to be drawn */
    	// skos:definition xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
    	// rdfs:label xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
      // RDF: fibo-fbc-fct-usrga:RoutingTransitNumber
      // subClassOf: RegisteredIdentifier - external CMNS
    }

item def <'state government entity or agency'> StateGovernmentEntity specializes Government_Entities::GovernmentBody {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-ge-ge;GovernmentBody"/>
      // RDF: fibo-fbc-fct-usrga:StateGovernmentEntity
      // subClassOf: GovernmentBody - from FiBO_BE_GovernmentEntities (GovernmentEntities)
    }

item def <'state member bank'> StateMemberBank specializes StateCharteredBank, FederalReserveSystemMember {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-usfse;StateCharteredBank"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-usjrga;FederalReserveSystemMember"/>
    	doc /* state-chartered bank that is a member of the Federal Reserve System (FRS) */
    	// skos:definition xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
    	// rdfs:label xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
      // RDF: fibo-fbc-fct-usrga:StateMemberBank
    }

item def <'taxpayer identification number'> TaxpayerIdentificationNumber specializes Parties::TaxIdentifier {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pty-pty;TaxIdentifier"/>
    	doc /* identification number used by the Internal Revenue Service (IRS) in the administration of tax laws in the United States */
    	// skos:definition xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
    	// rdfs:label xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
      // RDF: fibo-fbc-fct-usrga:TaxpayerIdentificationNumber
      // subClassOf: TaxIdentifier - external CMNS
    }

item def <'taxpayer identification numbering scheme'> TaxpayerIdentificationNumberingScheme specializes Parties::TaxIdentificationScheme {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pty-pty;TaxIdentificationScheme"/>
    	doc /* tax identification scheme used in the United States */
    	// skos:definition xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
    	// rdfs:label xml:lang="en" (USNationalInformationCenterControlledVocabularies.rdf)
      // RDF: fibo-fbc-fct-usrga:TaxpayerIdentificationNumberingScheme
      // subClassOf: TaxIdentificationScheme - external CMNS
    }

}
