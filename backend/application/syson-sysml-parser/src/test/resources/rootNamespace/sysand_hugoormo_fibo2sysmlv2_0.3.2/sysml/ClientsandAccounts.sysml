// ===================================================================
// ClientsandAccounts.sysml
// Source-parity extracted from: FiBO_FBC_ProductsAndServices.sysml
// Primary source module: ClientsAndAccounts.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Financial Business and Commerce FBC/ProductsAndServices
// Structural note: translated as a split file from FiBO_FBC_ProductsAndServices umbrella.
// ===================================================================
package Clients_and_Accounts {
  doc/* This ontology provides basic concepts related to accounts, including internal accounts such as ledger accounts as well as accounts managed for external parties, that are commonly used by financial services providers. (source: dct:abstract, ClientsAndAccounts.rdf) */
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
// Holding, FinancialService
// BankingProduct, FinancialServiceProviderIdentifier
// CashInstrument
// ServiceProvider
// CorporateOfficer (LendingOfficer parent)

  item def <'account'> Account {
    doc /* container for records associated with a business arrangement for regular transactions and services */
      // skos:definition xml:lang="en" (ClientsandAccounts.rdf)
      // rdfs:label xml:lang="en" (ClientsandAccounts.rdf)
    // RDF: fibo-fbc-pas-caa:Account
  }
  item def <'account as an asset'> AccountAsAnAsset specializes Accounting_Equity::FinancialAsset {
    doc /* financial asset in the form of an account */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-aeq;FinancialAsset"/>
  	// skos:definition xml:lang="en" (ClientsAndAccounts.rdf)
  	// rdfs:label xml:lang="en" (ClientsAndAccounts.rdf)
    // RDF: fibo-fbc-pas-caa:AccountAsAnAsset
    // subClassOf: FinancialAsset - from FIBO_FND_Accounting (AccountingEquity)
  }
  item def <'account holder'> AccountHolder specializes Ownership::Owner {
    doc /* party that owns an account */
      // skos:definition xml:lang="en" (ClientsandAccounts.rdf)
      // rdfs:label xml:lang="en" (ClientsandAccounts.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-oac-own;Owner"/>
    // RDF: fibo-fbc-pas-caa:AccountHolder
    // subClassOf: Owner - from FIBO_FND_OwnershipAndControl (OwnershipControlPackage)
  }
  item def <'account identifier'> AccountIdentifier specializes FiBO_FND_ExternalStubs::RegisteredIdentifier {
    doc /* identifier that denotes an account */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-ra;RegisteredIdentifier"/>
  	// skos:definition xml:lang="en" (ClientsAndAccounts.rdf)
  	// rdfs:label xml:lang="en" (ClientsAndAccounts.rdf)
    // RDF: fibo-fbc-pas-caa:AccountIdentifier
    // subClassOf: RegisteredIdentifier - from FIBO_FND_Accounting
  }
  item def <'accounting transaction'> AccountingTransaction specializes REA_Transactions::TransactionEvent {
    doc /* event recognized by an entry in the records of an account */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-pas;TransactionEvent"/>
  	// skos:definition xml:lang="en" (ClientsAndAccounts.rdf)
  	// rdfs:label xml:lang="en" (ClientsAndAccounts.rdf)
    // RDF: fibo-fbc-pas-caa:AccountingTransaction
    // subClassOf: TransactionEvent - from FIBO_FND_TransactionsExt
  }
  item def <'account ownership'> AccountOwnership specializes Financial_Products_and_Services::Holding {
    doc /* holding of an account */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;Holding"/>
  	// skos:definition xml:lang="en" (ClientsAndAccounts.rdf)
  	// rdfs:label xml:lang="en" (ClientsAndAccounts.rdf)
    // RDF: fibo-fbc-pas-caa:AccountOwnership
  }
  item def <'account provider'> AccountProvider specializes Functional_Entities::ServiceProvider {
    doc /* party that provides and services an account */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-org;ServiceProvider"/>
  	// skos:definition xml:lang="en" (ClientsAndAccounts.rdf)
  	// rdfs:label xml:lang="en" (ClientsAndAccounts.rdf)
    // RDF: fibo-fbc-pas-caa:AccountProvider
  }
  item def <'account-specific service agreement'> AccountSpecificServiceAgreement specializes Products_and_Services::ServiceAgreement {
    doc /* service-agreement that is account-specific, applicable in cases where a client might hold multiple accounts with differing terms and conditions */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-pas;ServiceAgreement"/>
  	// skos:definition xml:lang="en" (ClientsAndAccounts.rdf)
  	// rdfs:label xml:lang="en" (ClientsAndAccounts.rdf)
    // RDF: fibo-fbc-pas-caa:AccountSpecificServiceAgreement
    // subClassOf: ServiceAgreement - from FIBO_FND_ProductsAndServices (ProductsServicesPackage)
  }
  item def <'account statement'> AccountStatement specializes FiBO_FND_ExternalStubs::LegalDocument, FiBO_FND_ExternalStubs::Record{
    doc /* periodic summary of account activity for a given period of time */
      // skos:definition xml:lang="en" (ClientsandAccounts.rdf)
      // rdfs:label xml:lang="en" (ClientsandAccounts.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-doc;Record"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-doc;LegalDocument"/>
    // RDF: fibo-fbc-pas-caa:AccountStatement
    // subClassOf also: Record - external CMNS
  }
  item def <'balance'> Balance specializes Currency_Amount::MonetaryAmount {
    doc /* amount of money available or owed */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-cur;MonetaryAmount"/>
  	// skos:definition xml:lang="en" (ClientsAndAccounts.rdf)
  	// rdfs:label xml:lang="en" (ClientsAndAccounts.rdf)
    // RDF: fibo-fbc-pas-caa:Balance
    // subClassOf: MonetaryAmount - from FIBO_FND_Accounting (CurrencyAmount)
  }
  item def <'bank account identifier'> BankAccountIdentifier specializes AccountIdentifier {
    doc /* identifier that identifies a demand deposit account provided by a bank */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-caa;AccountIdentifier"/>
  	// skos:definition xml:lang="en" (ClientsAndAccounts.rdf)
  	// rdfs:label xml:lang="en" (ClientsAndAccounts.rdf)
    // RDF: fibo-fbc-pas-caa:BankAccountIdentifier
  }
  item def <'bank identifier'> BankIdentifier specializes Financial_Services_Entities::FinancialServiceProviderIdentifier {
    doc /* identifier that uniquely identifies the financial institution and, when appropriate, the branch of that financial institution servicing an account */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;FinancialServiceProviderIdentifier"/>
  	// skos:definition xml:lang="en" (ClientsAndAccounts.rdf)
  	// rdfs:label xml:lang="en" (ClientsAndAccounts.rdf)
    // RDF: fibo-fbc-pas-caa:BankIdentifier
  }
  item def <'basic bank account identifier'> BasicBankAccountIdentifier specializes BankAccountIdentifier, FiBO_FND_CodeSets_And_Schemes::StructuredIdentifier{
    doc /* identifier that uniquely identifies an individual account at a specific financial institution in a particular country and which includes a bank identifier of the financial institution servicing that account */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cxtid;StructuredIdentifier"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-caa;BankAccountIdentifier"/>
  	// skos:definition xml:lang="en" (ClientsAndAccounts.rdf)
  	// rdfs:label xml:lang="en" (ClientsAndAccounts.rdf)
    // RDF: fibo-fbc-pas-caa:BasicBankAccountIdentifier
    // subClassOf also: StructuredIdentifier - external CMNS
  }
  item def <'brokerage account'> BrokerageAccount specializes InvestmentAccount {
      doc /* account offered by a broker that allows the investor to deposit funds and place investment orders */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-caa;InvestmentAccount"/>
  	// skos:definition xml:lang="en" (ClientsAndAccounts.rdf)
  	// rdfs:label xml:lang="en" (ClientsAndAccounts.rdf)
    // RDF: fibo-fbc-pas-caa:BrokerageAccount
  }
  item def <'certificate of deposit'> CertificateOfDeposit specializes Financial_Instruments::CashInstrument {
      doc /* cash instrument associated with a time deposit account that cannot be withdrawn for a certain period of time (term) */
      // skos:definition xml:lang="en" (ClientsandAccounts.rdf)
      // rdfs:label xml:lang="en" (ClientsandAccounts.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fi-fi;CashInstrument"/>
    // RDF: fibo-fbc-pas-caa:CertificateOfDeposit
  }
  item def <'chart of accounts'> ChartOfAccounts specializes FiBO_FND_ExternalStubs::Arrangement, Documents::FinancialRecord{
    doc /* structured system of financial account codes used to classify, record, and organize an entity's financial transactions in accordance with applicable legal, regulatory, and reporting requirements */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-doc;FinancialRecord"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-col;Arrangement"/>
  	// skos:definition xml:lang="en" (ClientsAndAccounts.rdf)
  	// rdfs:label xml:lang="en" (ClientsAndAccounts.rdf)
    // RDF: fibo-fbc-pas-caa:ChartOfAccounts
    // subClassOf: Arrangement - from FIBO_FND_Arrangements (ArrangementsPackage)
    // subClassOf also: FinancialRecord - external CMNS
  }
  item def <'close date'> CloseDate specializes FiBO_FND_ExternalStubs::Date {
    doc /* date on which something was closed */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-dt;ExplicitDate"/>
  	// skos:definition xml:lang="en" (ClientsAndAccounts.rdf)
  	// rdfs:label xml:lang="en" (ClientsAndAccounts.rdf)
    // RDF: fibo-fbc-pas-caa:CloseDate
    // subClassOf: ExplicitDate - from FIBO_FND_DatesAndTimes (FinancialDates)
  }
  item def <'commercial lending service'> CommercialLendingService specializes Financial_Products_and_Services::FinancialService {
    doc /* service involving the extension of credit to support business activities */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-fpas;FinancialService"/>
  	// skos:definition xml:lang="en" (ClientsAndAccounts.rdf)
  	// rdfs:label xml:lang="en" (ClientsAndAccounts.rdf)
    // RDF: fibo-fbc-pas-caa:CommercialLendingService
  }
  item def <'customer account'> CustomerAccount specializes Account {
      doc /* account that represents an identified, named collection of balances and cumulative totals used to summarize customer transaction-related activity over a designated period of time */
    // skos:definition xml:lang="en" (ClientsandAccounts.rdf)
    // rdfs:label xml:lang="en" (ClientsandAccounts.rdf)
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-caa;Account"/>
    // RDF: fibo-fbc-pas-caa:CustomerAccount
  }
  item def <'customer account holder'> CustomerAccountHolder specializes AccountHolder {
      doc /* party that owns a customer account */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-caa;AccountHolder"/>
  	// skos:definition xml:lang="en" (ClientsAndAccounts.rdf)
  	// rdfs:label xml:lang="en" (ClientsAndAccounts.rdf)
    // RDF: fibo-fbc-pas-caa:CustomerAccountHolder
  }
  item def <'demand deposit account'> DemandDepositAccount specializes TransactionDepositAccount {
    doc /* non-interest-bearing deposit account in which deposits are payable immediately on demand, or that are issued with an original maturity or required notice period of less than seven days, or that represent funds for which the depository institution does not reserve the right to require at least seven days' written notice of an intended withdrawal */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-caa;TransactionDepositAccount"/>
  	// skos:definition xml:lang="en" (ClientsAndAccounts.rdf)
  	// rdfs:label xml:lang="en" (ClientsAndAccounts.rdf)
    // RDF: fibo-fbc-pas-caa:DemandDepositAccount
  }
  item def <'deposit account'> DepositAccount specializes Financial_Services_Entities::BankingProduct, InvestmentOrDepositAccount {
    doc /* account that provides a record of money placed with a depository institution for safekeeping and management */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-fct-fse;BankingProduct"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-caa;InvestmentOrDepositAccount"/>
  	// skos:definition xml:lang="en" (ClientsAndAccounts.rdf)
  	// rdfs:label xml:lang="en" (ClientsAndAccounts.rdf)
    // RDF: fibo-fbc-pas-caa:DepositAccount
  }
  item def <'fee'> Fee specializes Currency_Amount::MonetaryAmount {
    doc /* charge for services performed */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-acc-cur;MonetaryAmount"/>
  	// skos:definition xml:lang="en" (ClientsAndAccounts.rdf)
  	// rdfs:label xml:lang="en" (ClientsAndAccounts.rdf)
    // RDF: fibo-fbc-pas-caa:Fee
    // subClassOf: MonetaryAmount - from FIBO_FND_Accounting (CurrencyAmount)
  }
  item def <'general ledger'> GeneralLedger specializes Documents::FinancialRecord {
    doc /* organized collection of ledger accounts used to record and summarize changes in position as transactions are posted during an accounting period */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-doc;FinancialRecord"/>
  	// skos:definition xml:lang="en" (ClientsAndAccounts.rdf)
  	// rdfs:label xml:lang="en" (ClientsAndAccounts.rdf)
    // RDF: fibo-fbc-pas-caa:GeneralLedger
    // subClassOf: FinancialRecord - external CMNS
  }
  item def <'individual transaction'> IndividualTransaction specializes Financial_Dates::DatedCollectionConstituent, FiBOOccurrences::FiBOOccurrence{
    doc /* event that has a monetary impact and is documented in the records associated with an account */
      // skos:definition xml:lang="en" (ClientsandAccounts.rdf)
      // rdfs:label xml:lang="en" (ClientsandAccounts.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-oc;FiBOOccurrence"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-fd;DatedCollectionConstituent"/>
    // RDF: fibo-fbc-pas-caa:IndividualTransaction
    // subClassOf also: FiBOOccurrence - external CMNS
  }
  item def <'international bank account identifier'> InternationalBankAccountIdentifier specializes BankAccountIdentifier, FiBO_FND_CodeSets_And_Schemes::StructuredIdentifier{
    doc /* identifier for a bank account that is an expanded version of the basic bank account number (BBAN), intended for use internationally */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cxtid;StructuredIdentifier"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-caa;BankAccountIdentifier"/>
  	// skos:definition xml:lang="en" (ClientsAndAccounts.rdf)
  	// rdfs:label xml:lang="en" (ClientsAndAccounts.rdf)
    // RDF: fibo-fbc-pas-caa:InternationalBankAccountIdentifier
    // subClassOf also: StructuredIdentifier - external CMNS
  }
  item def <'investment account'> InvestmentAccount specializes InvestmentOrDepositAccount {
    doc /* account that provides a record of deposits of funds and/or securities held at a financial institution */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-caa;InvestmentOrDepositAccount"/>
  	// skos:definition xml:lang="en" (ClientsAndAccounts.rdf)
  	// rdfs:label xml:lang="en" (ClientsAndAccounts.rdf)
    // RDF: fibo-fbc-pas-caa:InvestmentAccount
  }
  item def <'investment or deposit account'> InvestmentOrDepositAccount specializes Account {
    doc /* account associated with a product or service that requires the account holder to provide funds for management by the account provider */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-caa;Account"/>
  	// skos:definition xml:lang="en" (ClientsandAccounts.rdf)
  	// rdfs:label xml:lang="en" (ClientsandAccounts.rdf)
    // RDF: fibo-fbc-pas-caa:InvestmentOrDepositAccount
  }
  item def <'ledger account'> LedgerAccount specializes Account {
    doc /* individual record for one element or sub-element in a ledger that records and summarizes increases, decreases, and balances associated with that specific element */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-caa;Account"/>
  	// skos:definition xml:lang="en" (ClientsAndAccounts.rdf)
  	// rdfs:label xml:lang="en" (ClientsAndAccounts.rdf)
    // RDF: fibo-fbc-pas-caa:LedgerAccount
  }
  item def <'ledger account classifier'> LedgerAccountClassifier specializes FiBO_FND_ExternalStubs::Classifier {
    doc /* classifier used to organize an entity's financial accounts in accordance with applicable legal, regulatory, and reporting requirements */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
  	// skos:definition xml:lang="en" (ClientsAndAccounts.rdf)
  	// rdfs:label xml:lang="en" (ClientsAndAccounts.rdf)
    // RDF: fibo-fbc-pas-caa:LedgerAccountClassifier
    // subClassOf: Classifier - external CMNS
  }
  item def <'lending officer'> LendingOfficer specializes Executives::CorporateOfficer, RelationshipManager {
    doc /* corporate officer that has overarching responsibility for the relationship with a specific borrower or account */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-be-oac-exec;CorporateOfficer"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-caa;RelationshipManager"/>
  	// skos:definition xml:lang="en" (ClientsAndAccounts.rdf)
  	// rdfs:label xml:lang="en" (ClientsAndAccounts.rdf)
    // RDF: fibo-fbc-pas-caa:LendingOfficer
  }
  item def <'loan or credit account'> LoanOrCreditAccount specializes Account {
    doc /* account associated with a service in which the account holder receives funds from the account provider under certain terms and conditions for repayment */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-caa;Account"/>
  	// skos:definition xml:lang="en" (ClientsAndAccounts.rdf)
  	// rdfs:label xml:lang="en" (ClientsAndAccounts.rdf)
    // RDF: fibo-fbc-pas-caa:LoanOrCreditAccount
  }
  item def <'non-transaction deposit account'> NonTransactionDepositAccount specializes DepositAccount {
    doc /* any deposit account that is not explicitly considered a transaction account */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-caa;DepositAccount"/>
  	// skos:definition xml:lang="en" (ClientsAndAccounts.rdf)
  	// rdfs:label xml:lang="en" (ClientsAndAccounts.rdf)
    // RDF: fibo-fbc-pas-caa:NonTransactionDepositAccount
  }
  item def <'open date'> OpenDate specializes FiBO_FND_ExternalStubs::Date {
    doc /* date on which something was created */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-dt;ExplicitDate"/>
  	// skos:definition xml:lang="en" (ClientsAndAccounts.rdf)
  	// rdfs:label xml:lang="en" (ClientsAndAccounts.rdf)
    // RDF: fibo-fbc-pas-caa:OpenDate
    // subClassOf: ExplicitDate - from FIBO_FND_DatesAndTimes (FinancialDates)
  }
  item def <'payment due date'> PaymentDueDate specializes FiBO_FND_ExternalStubs::Date {
    doc /* date by which payment of the current outstanding balance, part thereof, or a minimum amount due must be made to the creditor */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-dt;ExplicitDate"/>
  	// skos:definition xml:lang="en" (ClientsAndAccounts.rdf)
  	// rdfs:label xml:lang="en" (ClientsAndAccounts.rdf)
    // RDF: fibo-fbc-pas-caa:PaymentDueDate
    // subClassOf: ExplicitDate - from FIBO_FND_DatesAndTimes (FinancialDates)
  }
  item def <'posting date'> PostingDate specializes FiBO_FND_ExternalStubs::Date {
    doc /* date that determines in which posting period a document or journal entry is added to an account record */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-dt;ExplicitDate"/>
  	// skos:definition xml:lang="en" (ClientsAndAccounts.rdf)
  	// rdfs:label xml:lang="en" (ClientsAndAccounts.rdf)
    // RDF: fibo-fbc-pas-caa:PostingDate
    // subClassOf: ExplicitDate - from FIBO_FND_DatesAndTimes (FinancialDates)
  }
  item def <'relationship manager'> RelationshipManager specializes Executives::ResponsibleParty {
    doc /* responsible party who manages a client's account and oversees their relationship with the service provider */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-bauth;ResponsibleParty"/>
  	// skos:definition xml:lang="en" (ClientsAndAccounts.rdf)
  	// rdfs:label xml:lang="en" (ClientsAndAccounts.rdf)
    // RDF: fibo-fbc-pas-caa:RelationshipManager
    // subClassOf: ResponsibleParty - external CMNS
  }
  item def <'time certificate of deposit account'> TimeCertificateOfDepositAccount specializes TimeDepositAccount {
    doc /* time deposit account that allows deposits evidenced by a negotiable or nonnegotiable instrument, or a deposit in book entry form evidenced by a receipt or similar acknowledgement issued by the bank, that provides, on its face, that the amount of such deposit is payable to the bearer, to any specified person, or to the order of a specified person, as follows: (1) on a certain date not less than seven days after the date of deposit, (2) at the expiration of a specified period not less than seven days after the date of the deposit, or (3) upon written notice to the bank which is to be given not less than seven days before the date of withdrawal. */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-caa;TimeDepositAccount"/>
  	// skos:definition xml:lang="en" (ClientsAndAccounts.rdf)
  	// rdfs:label xml:lang="en" (ClientsAndAccounts.rdf)
    // RDF: fibo-fbc-pas-caa:TimeCertificateOfDepositAccount
  }
  item def <'time deposit account'> TimeDepositAccount specializes NonTransactionDepositAccount, Products_and_Services::ContractualProduct{
    doc /* deposit account that the depositor does not have a right, and is not permitted, to make withdrawals from within six days after the date of deposit unless the deposit is subject to an early withdrawal penalty of at least seven days' simple interest on amounts withdrawn within the first six days after deposit */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-pas-pas;ContractualProduct"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-caa;NonTransactionDepositAccount"/>
  	// skos:definition xml:lang="en" (ClientsAndAccounts.rdf)
  	// rdfs:label xml:lang="en" (ClientsAndAccounts.rdf)
    // RDF: fibo-fbc-pas-caa:TimeDepositAccount
    // subClassOf also: ContractualProduct - from FIBO_FND_ProductsAndServices
  }
  item def <'time deposit open account'> TimeDepositOpenAccount specializes TimeDepositAccount {
    doc /* time deposit account that allows deposits (other than time certificates of deposit) for which there is in force a written contract with the depositor that neither the whole nor any part of such deposit may be withdrawn prior to (1) the date of maturity, which shall be not less than seven days after the date of the deposit, or (2) the expiration of a specified period of written notice of not less than seven days */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-caa;TimeDepositAccount"/>
  	// skos:definition xml:lang="en" (ClientsAndAccounts.rdf)
  	// rdfs:label xml:lang="en" (ClientsAndAccounts.rdf)
    // RDF: fibo-fbc-pas-caa:TimeDepositOpenAccount
  }
  item def <'transaction category'> TransactionCategory specializes FiBO_FND_ExternalStubs::Classifier, FiBO_FND_ExternalStubs::CodeElement{
    doc /* high-level classifier for an individual transaction */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cds;CodeElement"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
  	// skos:definition xml:lang="en" (ClientsAndAccounts.rdf)
  	// rdfs:label xml:lang="en" (ClientsAndAccounts.rdf)
    // RDF: fibo-fbc-pas-caa:TransactionCategory
    // subClassOf also: CodeElement - external CMNS
  }
  item def <'transaction date'> TransactionDate specializes FiBO_FND_ExternalStubs::Date {
    doc /* date on which a specific transaction was initiated */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-dt;ExplicitDate"/>
  	// skos:definition xml:lang="en" (ClientsAndAccounts.rdf)
  	// rdfs:label xml:lang="en" (ClientsAndAccounts.rdf)
    // RDF: fibo-fbc-pas-caa:TransactionDate
    // subClassOf: ExplicitDate - from FIBO_FND_DatesAndTimes (FinancialDates)
  }
  item def <'transaction deposit account'> TransactionDepositAccount specializes DepositAccount {
    doc /* deposit account from which the depositor / account holder is permitted to make transfers or withdrawals by negotiable / transferable instruments, payment orders of withdrawal, telephone transfers, and so forth, and that may be accessible via an electronic device such as an automated teller machine (ATM), remote service unit (RSU), mobile device, and by debit card */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fbc-pas-caa;DepositAccount"/>
  	// skos:definition xml:lang="en" (ClientsAndAccounts.rdf)
  	// rdfs:label xml:lang="en" (ClientsAndAccounts.rdf)
    // RDF: fibo-fbc-pas-caa:TransactionDepositAccount
  }
  item def <'transaction identifier'> TransactionIdentifier specializes FiBO_FND_ExternalStubs::Identifier {
    doc /* identifier for an individual transaction associated with an account */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-id;Identifier"/>
  	// skos:definition xml:lang="en" (ClientsAndAccounts.rdf)
  	// rdfs:label xml:lang="en" (ClientsAndAccounts.rdf)
    // RDF: fibo-fbc-pas-caa:TransactionIdentifier
    // subClassOf: Identifier - from FIBO_FND_Accounting
  }
  item def <'transaction record'> TransactionRecord specializes Financial_Dates::DatedStructuredCollection, FiBO_FND_ExternalStubs::Record, FiBO_FND_ExternalStubs::Registry{
    doc /* record of transactions associated with an account */
      // skos:definition xml:lang="en" (ClientsandAccounts.rdf)
      // rdfs:label xml:lang="en" (ClientsandAccounts.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-doc;Record"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-ra;Registry"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-fd;DatedStructuredCollection"/>
    // RDF: fibo-fbc-pas-caa:TransactionRecord
    // subClassOf also: Record, Registry - external CMNS
  }
  item def <'transaction record identifier'> TransactionRecordIdentifier specializes FiBO_FND_ExternalStubs::RegisteredIdentifier {
    doc /* unique identifier for record of transactions */
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-ra;RegisteredIdentifier"/>
  	// skos:definition xml:lang="en" (ClientsAndAccounts.rdf)
  	// rdfs:label xml:lang="en" (ClientsAndAccounts.rdf)
    // RDF: fibo-fbc-pas-caa:TransactionRecordIdentifier
    // subClassOf: RegisteredIdentifier - from FIBO_FND_Accounting
  }
  item def <'transaction subcategory'> TransactionSubcategory specializes FiBO_FND_ExternalStubs::Classifier, FiBO_FND_ExternalStubs::CodeElement{
    doc /* second-level classifier for a transaction, e.g., direct deposit, check, cash advance, withdrawal, payment, purchase, and so forth */
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cds;CodeElement"/>
    // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
  	// skos:definition xml:lang="en" (ClientsAndAccounts.rdf)
  	// rdfs:label xml:lang="en" (ClientsAndAccounts.rdf)
    // RDF: fibo-fbc-pas-caa:TransactionSubcategory
    // subClassOf also: CodeElement - external CMNS
  }
}

// --- FinancialProductsAndServices.rdf ---
