// ===================================================================
// FinancialServicesEntities.sysml
// Source-parity extracted from: FiBO_FBC_FunctionalEntities.sysml
// Primary source module: FinancialServicesEntities.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Financial Business and Commerce FBC/FunctionalEntities
// Structural note: translated as a split file from FiBO_FBC_FunctionalEntities umbrella.
// ===================================================================
package Financial_Services_Entities {
  doc/* This ontology defines basic financial service providers, such as holding companies, financial institutions (both depository and non-depository institutions), and clearing houses at a relatively general level. Nuances specific to the institutions located in a specific country are defined in jurisdiction specific dependent ontologies. (source: dct:abstract, FinancialServicesEntities.rdf) */
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
    item def <'bank'> Bank specializes DepositoryInstitution {
      doc /* depository institution, usually a corporation, that accepts deposits, makes loans, pays checks, and performs related services, for individual members of the public, businesses or other organizations */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;DepositoryInstitution"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:Bank
    }

    item def <'bank holding company'> BankHoldingCompany specializes Financial_Products_and_Services::FinancialServiceProvider, HoldingCompany {
      doc /* any company that owns and/or has direct or indirect control of one or more banks; BHCs may also own nonbanking subsidiaries such as broker-dealers and asset managers */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;FinancialServiceProvider"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;HoldingCompany"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:BankHoldingCompany
    }

    item def <'banking product'> BankingProduct specializes Financial_Products_and_Services::FinancialProduct {
      doc /* product provided to consumers and businesses by a depository institution */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;FinancialProduct"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:BankingProduct
    }

    item def <'banking service'> BankingService specializes Financial_Products_and_Services::FinancialService {
      doc /* financial service offered by a depository institution */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;FinancialService"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:BankingService
    }

    item def <'brokerage firm'> BrokerageFirm specializes PrivateLabelMBSIssuance::BrokerDealer, NonDepositoryInstitution {
      doc /* firm in the business of buying and selling securities, operating as both a broker and a dealer, depending on the transaction */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;BrokerDealer"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;NonDepositoryInstitution"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:BrokerageFirm
    }

    item def <'business identifier code'> BusinessIdentifierCode specializes FiBO_FND_ExternalStubs::CodeElement, Functional_Entities::OrganizationIdentifier, FiBO_FND_CodeSets_And_Schemes::StructuredIdentifier{
      doc /* international identifier for financial and non-financial institutions used to facilitate automated processing of information for financial services */
        // skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
        // rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cxtid;StructuredIdentifier"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cds;CodeElement"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;OrganizationIdentifier"/>
      // RDF: fibo-fbc-fct-fse:BusinessIdentifierCode
      // subClassOf also: StructuredIdentifier - external CMNS
    }

    item def <'business identifier code scheme'> BusinessIdentifierCodeScheme specializes FiBO_FND_CodeSets_And_Schemes::IdentificationScheme, FiBO_FND_CodeSets_And_Schemes::CodeSet, FiBO_FND_ExternalStubs::OrganizationIdentificationScheme{
      doc /* scheme that specifies the elements of a unique business identifier code (BIC) scheme to identify financial and non-financial institutions used to facilitate automated processing of information for financial services */
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cds;CodeSet"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;OrganizationIdentificationScheme"/>
	  // R3 direct exact pair: materialized from BusinessIdentifierCodeScheme hasMember
	  // BusinessIdentifierCode and BusinessIdentifierCode isMemberOf BusinessIdentifierCodeScheme.
	  item businessIdentifierCode: BusinessIdentifierCode [0..*];
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:BusinessIdentifierCodeScheme
      // subClassOf also: CodeSet, OrganizationIdentificationScheme - external CMNS
    }

    item def <'business party prefix'> BusinessPartyPrefix specializes FiBO_FND_ExternalStubs::CodeElement, Functional_Entities::OrganizationIdentifier {
      doc /* four-character (4 alphanumeric) code associated with an organization for the purposes of banking telecommunications */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cds;CodeElement"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;OrganizationIdentifier"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:BusinessPartyPrefix
      // subClassOf: CodeElement - from FIBO_FND_Accounting (CurrencyAmount)
    }

    item def <'business party suffix'> BusinessPartySuffix specializes FiBO_FND_ExternalStubs::CodeElement {
      doc /* two-character (2 alphanumeric) code associated with the organization for the purposes of banking telecommunications */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cds;CodeElement"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:BusinessPartySuffix
      // subClassOf: CodeElement - from FIBO_FND_Accounting (CurrencyAmount)
    }

    item def <'central bank'> CentralBank specializes Bank, MonetaryAuthority {
      doc /* financial institution that is the monetary authority and major regulatory bank for a country (or group of countries) */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;Bank"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;MonetaryAuthority"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:CentralBank
    }

    item def <'central counterparty clearing house'> CentralCounterpartyClearingHouse specializes ClearingHouse {
      doc /* clearing house that helps facilitate trading in derivatives and equities markets */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;ClearingHouse"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:CentralCounterpartyClearingHouse
    }

    item def <'central securities depository'> CentralSecuritiesDepository specializes Financial_Products_and_Services::FinancialServiceProvider {
      doc /* functional entity that provides a central point for depositing financial instruments ('securities'), for example, bonds and shares */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;FinancialServiceProvider"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:CentralSecuritiesDepository
    }

    item def <'clearing bank'> ClearingBank specializes ClearingHouse, CommercialBank {
      doc /* commercial bank that facilitates payment and settlement of financial transactions, such as check clearing or facilitating trades between the sellers and buyers of securities or other financial instruments or contracts */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;ClearingHouse"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;CommercialBank"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:ClearingBank
    }

    item def <'clearing corporation'> ClearingCorporation specializes ClearingHouse {
      doc /* clearing house that is organized as a corporation */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;ClearingHouse"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:ClearingCorporation
    }

    item def <'clearing house'> ClearingHouse specializes Financial_Products_and_Services::FinancialServiceProvider {
      doc /* financial service provider that is exchange affiliated and provides clearing services, including the validation, delivery, and settlement of financial transactions, for financial intermediaries */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;FinancialServiceProvider"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:ClearingHouse
    }

    item def <'clearing service'> ClearingService specializes Financial_Products_and_Services::FinancialService {
      doc /* service provided on behalf of an institutional market participant by a clearing services provider following a trade that finalizes the transfer of security ownership */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;FinancialService"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:ClearingService
    }

    item def <'commercial bank'> CommercialBank specializes Bank {
      doc /* depository institution that engages in various financial services, such as accepting deposits and making loans */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;Bank"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:CommercialBank
    }

    item def <'commercial finance company'> CommercialFinanceCompany specializes FinanceCompany {
      doc /* finance company that makes loans to manufacturers and wholesalers, secured by accounts receivable, inventories, and equipment */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;FinanceCompany"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:CommercialFinanceCompany
    }

    item def <'commodity trading advisor'> CommodityTradingAdvisor specializes NonDepositoryInstitution {
      doc /* party that directly or indirectly advises others as to the value or advisability of buying or selling futures contracts or options */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;NonDepositoryInstitution"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:CommodityTradingAdvisor
    }

    item def <'consumer finance company'> ConsumerFinanceCompany specializes FinanceCompany {
      doc /* finance company that lends to individuals under the small loans laws of the jurisdiction in which they operate */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;FinanceCompany"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:ConsumerFinanceCompany
    }

    item def <'contractual savings institution'> ContractualSavingsInstitution specializes Ownership_Parties::Investor, NonDepositoryInstitution {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-opty;Investor"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;NonDepositoryInstitution"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:ContractualSavingsInstitution
    }

    item def <'credit union'> CreditUnion specializes Functional_Entities::CooperativeSociety, DepositoryInstitution {
      doc /* not-for-profit depository institution that makes personal loans and offers other consumer banking services, organized for the purpose of promoting thrift among its members and creating a source of credit for provident or productive purposes */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-fct-fct;CooperativeSociety"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;DepositoryInstitution"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:CreditUnion
    }

    item def <'data processing service'> DataProcessingService specializes Financial_Products_and_Services::FinancialService {
      doc /* financial service offered by an entity primarily engaged in providing infrastructure for hosting or data processing services */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;FinancialService"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:DataProcessingService
    }

    item def <'data processing servicer'> DataProcessingServicer specializes Financial_Products_and_Services::FinancialServiceProvider {
      doc /* finance services provider primarily engaged in providing infrastructure for hosting or data processing services */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;FinancialServiceProvider"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:DataProcessingServicer
    }

    item def <'depository institution'> DepositoryInstitution specializes FinancialInstitution {
      doc /* any financial institution engaged in the business of receiving demand deposits from the public or other institutions */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;FinancialInstitution"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:DepositoryInstitution
    }

    item def <'development bank'> DevelopmentBank specializes FinancialInstitution {
      doc /* national or regional financial institution designed to provide medium- and long-term capital for productive investment, often accompanied by technical assistance, in poor countries */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;FinancialInstitution"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:DevelopmentBank
    }

    item def <'electronic funds transfer service'> ElectronicFundsTransferService specializes Financial_Products_and_Services::FinancialService {
      doc /* service involving any transfer of funds other than a transaction involving a paper instrument, that is initiated through an electronic terminal, telephone, or computer and that orders or authorizes a financial institution to debit or credit an account */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;FinancialService"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:ElectronicFundsTransferService
    }

    item def <'face amount certificate company'> FaceAmountCertificateCompany specializes InvestmentCompany {
      doc /* investment company which is engaged or proposes to engage in the business of issuing face-amount certificates of the installment type, or which has been engaged in such business and has any such certificate outstanding */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;InvestmentCompany"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:FaceAmountCertificateCompany
    }

    item def <'finance company'> FinanceCompany specializes NonDepositoryInstitution {
      doc /* financial intermediary in the business of making loans that obtains its financing from banks, institutions, and other money market sources rather than from deposits */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;NonDepositoryInstitution"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:FinanceCompany
    }

    item def <'financial institution'> FinancialInstitution specializes Financial_Products_and_Services::FinancialServiceProvider {
      doc /* financial service provider identified as either a government agency or privately owned entity that collects funds from the public and from other institutions, and invests those funds in financial assets, such as loans, securities, bank deposits, and income-generating property */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;FinancialServiceProvider"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:FinancialInstitution
    }

    item def <'financial service provider identifier'> FinancialServiceProviderIdentifier specializes Parties::PartyRoleIdentifier {
      doc /* identifier that is officially allocated to a financial service provider based on a function that they provide, typically in a jurisdiction over which a regulatory agency has some jurisdiction */
      // skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
      // rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pty-pty;PartyRoleIdentifier"/>
      // RDF: fibo-fbc-fct-fse:FinancialServiceProviderIdentifier
    }

    item def <'financial service provider identifier scheme'> FinancialServiceProviderIdentifierScheme specializes FiBO_FND_CodeSets_And_Schemes::IdentificationScheme {
      doc /* scheme that defines the financial service provider identifier per the issuing registration authority or regulatory agency */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-id;IdentificationScheme"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:FinancialServiceProviderIdentifierScheme
      // subClassOf: IdentificationScheme - from FIBO_FND_Arrangements (ArrangementsPackage)
    }

    item def <'futures commission merchant'> FuturesCommissionMerchant specializes NonDepositoryInstitution {
      doc /* party that does both of the following: (1) solicits or accepts orders to buy or sell futures contracts, options on futures, retail off-exchange forex contracts, or swaps and (2) accepts money or other assets from customers to support such orders */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;NonDepositoryInstitution"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:FuturesCommissionMerchant
    }

    item def <'holding company'> HoldingCompany specializes Functional_Entities::FunctionalBusinessEntity {
      doc /* business entity established to own stock in another company, typically to own enough voting shares to have some level of control over that company's policies and management */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-fct-fct;FunctionalBusinessEntity"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:HoldingCompany
    }

    item def <'insurance company'> InsuranceCompany specializes RiskPoolingInstitution {
      doc /* non-depository institution whose primary and predominant business activity is the writing of insurance or the reinsuring of risks underwritten by insurance companies, and that provides compensation based on the happening of at least one contingency */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;RiskPoolingInstitution"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:InsuranceCompany
    }

    item def <'insurance service'> InsuranceService specializes Financial_Products_and_Services::FinancialService {
      doc /* financial service in which the insurer promises to provide compensation for specific potential future losses in exchange for a periodic payment */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;FinancialService"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:InsuranceService
    }

    item def <'investment bank'> InvestmentBank specializes FinancialInstitution {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;FinancialInstitution"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:InvestmentBank
    }

    item def <'investment company'> InvestmentCompany specializes NonDepositoryInstitution {
      doc /* any issuer which: (a) is or holds itself out as being engaged primarily, or proposes to engage primarily, in the business of investing, reinvesting, or trading in securities; (b) is engaged or proposes to engage in the business of issuing face-amount certificates of the installment type, or has been engaged in such business and has any such certificate outstanding; or (c) is engaged or proposes to engage in the business of investing, reinvesting, owning, holding, or trading in securities, and owns or proposes to acquire investment securities having a value exceeding 40 per centum of the value of such issuer&apos;s total assets (exclusive of Government securities and cash items) on an unconsolidated basis */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;NonDepositoryInstitution"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:InvestmentCompany
    }

    item def <'investment service'> InvestmentService specializes Financial_Products_and_Services::FinancialService {
      doc /* financial service designed to assist investors in using capital to create more money, either through income-producing vehicles or through more risk-oriented ventures to result in capital gains, including but not limited to providing investment advice, asset and portfolio management, and brokerage services */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;FinancialService"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:InvestmentService
    }

    item def <'management company'> ManagementCompany specializes InvestmentCompany {
      doc /* investment company that sells and manages a portfolio of securities other than a face-amount certificate company or unit investment fund */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;InvestmentCompany"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:ManagementCompany
    }

    item def <'merchant service'> MerchantService specializes Financial_Products_and_Services::FinancialService {
      doc /* financial service provided by a financial institution to a merchant or other business, including but not limited to managing financial transactions via a secure channel */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;FinancialService"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:MerchantService
    }

    item def <'monetary authority'> MonetaryAuthority specializes Regulatory_Agencies::RegulatoryAgency {
      doc /* regulatory agency that controls the monetary policy, regulation and supply of money in some country or group of countries */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-rga;RegulatoryAgency"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:MonetaryAuthority
    }

    item def <'money services business'> MoneyServicesBusiness specializes NonDepositoryInstitution {
      doc /* any person doing business, whether or not on a regular basis or as an organized business concern, in one of the following capacities: (1) currency dealer or exchanger, (2) check casher, (3) issuer of traveler's checks, money orders, or stored value, (4) seller or redeemer of traveler's checks, money orders, or stored value, (5) money transmitter, or (6) postal service */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;NonDepositoryInstitution"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:MoneyServicesBusiness
    }

    item def <'mortgage company'> MortgageCompany specializes Financial_Products_and_Services::FinancialServiceProvider {
      doc /* financial service provider that originates and/or funds mortgages for residential or commercial property */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;FinancialServiceProvider"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:MortgageCompany
    }

    item def <'non-depository institution'> NonDepositoryInstitution specializes FinancialInstitution {
      doc /* financial institution that does not have a full banking license and typically is not supervised by a national or international banking regulatory agency */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;FinancialInstitution"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:NonDepositoryInstitution
    }

    item def <'payment service'> PaymentService specializes MerchantService {
      doc /* financial service that involves acceptance of electronic payments by a variety of payment methods including credit card, bank-based payments such as direct debit, bank transfer, and real-time bank transfer based on online banking */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;MerchantService"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:PaymentService
    }

    item def <'payroll service'> PayrollService specializes Financial_Products_and_Services::FinancialService {
      doc /* financial service, typically provided to small businesses that are not large enough to have an internal finance organization, that involves managing payment of wages to employees */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;FinancialService"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:PayrollService
    }

    item def <'principal underwriter'> PrincipalUnderwriter specializes Underwriter {
      doc /* underwriter who, as principal, purchases from an investment company, or pursuant to some contract has the right to purchase from such company, any security for distribution, or who as agent for such company sells or has the right to sell any security to a dealer or to the public, excluding any dealer who purchases from such company through sn underwriter acting as an agent for such company */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;Underwriter"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:PrincipalUnderwriter
    }

    item def <'registered investment advisor'> RegisteredInvestmentAdvisor specializes Financial_Products_and_Services::FinancialServiceProvider, Financial_Products_and_Services::RegisteredAgent {
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;FinancialServiceProvider"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;RegisteredAgent"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:RegisteredInvestmentAdvisor
    }

    item def <'risk pooling institution'> RiskPoolingInstitution specializes NonDepositoryInstitution {
      doc /* financial institution that provides some financial service while spreading the financial risk inherent in that service to lower the probability of a catastrophic financial event by aggregating customers across diverse dimensions as a risk management practice */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;NonDepositoryInstitution"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:RiskPoolingInstitution
    }

    item def <'sales finance company'> SalesFinanceCompany specializes FinanceCompany {
      doc /* finance company that purchases retail and wholesale paper from automobile and other consumer and commercial goods dealers */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;FinanceCompany"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:SalesFinanceCompany
    }

    item def <'savings association'> SavingsAssociation specializes DepositoryInstitution {
      doc /* depository institution that is (a) any federal savings bank or association chartered under section 1464 of the Federal Deposit Insurance Act; (b) any state chartered building and loan association, savings and loan association, or homestead association; or (c) any cooperative bank (other than a cooperative bank which is a state bank as defined in subsection (a)(2)) of the Federal Deposit Insurance Act, which is organized and operating according to the laws of the State (as defined in subsection (a)(3)) in which it is chartered or organized; and (c) any corporation (other than a bank) that the board of directors and the comptroller of the currency jointly determine to be operating in substantially the same manner as such a depository institution */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;DepositoryInstitution"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:SavingsAssociation
    }

    item def <'self-regulating organization'> SelfRegulatingOrganization specializes Regulatory_Agencies::RegulatoryAgency {
      doc /* non-governmental organization that has the power to create and exercise some degree of regulatory authority over an industry or profession in some country or group of countries */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-rga;RegulatoryAgency"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:SelfRegulatingOrganization
    }

    item def <'trust company'> TrustCompany specializes FinancialInstitution {
      doc /* financial institution that acts as a fiduciary, trustee or agent of trusts and agencies */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;FinancialInstitution"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:TrustCompany
    }

    item def <'underwriter'> Underwriter specializes Financial_Products_and_Services::FinancialServiceProvider {
      doc /* financial service provider that evaluates and assumes another party's risk for a fee, such as a commission, premium, spread or interest */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;FinancialServiceProvider"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:Underwriter
    }

    item def <'underwriting arrangement'> UnderwritingArrangement specializes Contracts::WrittenContract {
      doc /* written contract between parties that commits the underwriter to assuming risk */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-agr-ctr;WrittenContract"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:UnderwritingArrangement
      // subClassOf: WrittenContract - from FIBO_FND_Agreements (AgreementsPackage)
    }

    item def <'unit investment trust'> UnitInvestmentTrust specializes InvestmentCompany {
      doc /* investment company which (a) is organized under a trust indenture, contract of custodianship or agency, or similar instrument, (b) does not have a board of directors, and (c) issues only redeemable securities, each of which represents an undivided interest in a unit of specified securities; but does not include a voting trust */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;InvestmentCompany"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:UnitInvestmentTrust
    }

    item def <'wealth management service'> WealthManagementService specializes Financial_Products_and_Services::FinancialService {
      doc /* financial service that combines financial and investment advice, accounting and tax services, retirement planning and legal or estate planning for one set fee */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;FinancialService"/>
    	// skos:definition xml:lang="en" (FinancialServicesEntities.rdf)
    	// rdfs:label xml:lang="en" (FinancialServicesEntities.rdf)
      // RDF: fibo-fbc-fct-fse:WealthManagementService
    }

}

// --- fibo-fbc-fct-mkt (Markets) ---
