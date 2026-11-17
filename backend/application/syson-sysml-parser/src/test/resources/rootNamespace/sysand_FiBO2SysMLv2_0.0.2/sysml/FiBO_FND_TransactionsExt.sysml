// ===================================================================
// FIBO_FND_TransactionsExt.sysml
// CI Aggregator (public re-export of extended transaction packages)
// ===================================================================
package FIBO_FND_TransactionsExt {
  doc/* Configuration Item (CI) package for FIBO_FND_TransactionsExt and its exported model elements. */
  public import MarketTransactions::*;
  public import REATransactions::*;
  public import SecuritiesTransactions::*;
}

// ======================
// MarketTransactions.sysml
// ======================
package MarketTransactions {

  private import ScalarValues::*;          
  private import Time::*;                  
  private import CurrencyAmount::*;        
  private import CashFlows::*;             
  private import Agreements::*;            
  private import Contracts::*;             
  private import Parties::*;               
  private import ProductsAndServices::*;   
  private import FinancialDates::*;        

  // ===== Market transaction abstractions (all items) =====

  item def MarketTransaction {
    doc/* transaction executed in a market context, involving the exchange of assets, goods, or services for consideration */
    attribute transactionId: String[1..1];
    attribute tradeDate: Date[0..1];
    attribute valueDate: Date[0..1];
    attribute settlementDate: Date[0..1];
    attribute buyer: Parties::Party[1..1];
    attribute seller: Parties::Party[1..1];
    attribute tradedItem: ProductsAndServices::Product[0..*];
    attribute tradedService: ProductsAndServices::Service[0..*];
    attribute price: CurrencyAmount::MonetaryPrice[0..1];
    attribute considerationAmount: CurrencyAmount::MonetaryAmount[0..1];
    attribute cashFlow: CashFlows::CashFlow[0..*];
    attribute agreement: Agreements::Agreement[0..*];
    attribute relatedContract: Contracts::Contract[0..*];
  }

  item def Trade specializes MarketTransaction {
    doc/* individual executed transaction representing an exchange of assets, goods, or services between a buyer and a seller */
    attribute tradeType: String[0..1];
    attribute executionVenue: String[0..1];
    attribute broker: Parties::Party[0..1];
  }

  item def SettlementInstruction {
    doc/* instruction specifying how, when, and where a trade should be settled */
    attribute settlementDate: Date[0..1];
    attribute settlementCurrency: CurrencyAmount::Currency[0..1];
    attribute settlementAmount: CurrencyAmount::MonetaryAmount[0..1];
    attribute payer: Parties::Party[1..1];
    attribute payee: Parties::Party[1..1];
  }

  item def MarketOrder {
    doc/* order placed by a buyer or seller to execute a trade according to defined parameters */
    attribute orderId: String[1..1];
    attribute orderType: String[0..1];
    attribute orderDateTime: DateTime[0..1];
    attribute quantity: Real[0..1];
    attribute priceLimit: CurrencyAmount::MonetaryPrice[0..1];
    attribute placedBy: Parties::Party[1..1];
  }

  item def TransactionLifecycle {
    doc/* lifecycle of a market transaction from initiation to settlement and reporting */
    attribute transaction: MarketTransaction[1..1];
    attribute schedule: FinancialDates::RegularSchedule[0..1];
    attribute status: String[0..1];
    attribute lastUpdate: DateTime[0..1];
  }

  item def TransactionIdentifier {
    doc/* unique code or string that identifies a transaction within a market or system */
    attribute identifierValue: String[1..1];
    attribute scheme: String[0..1];
  }
}

// ======================
// REATransactions.sysml
// ======================
package REATransactions {

  private import ScalarValues::*;          
  private import Time::*;                  
  private import CurrencyAmount::*;        
  private import RealPropertyPackage::*;          
  private import Parties::*;               
  private import Agreements::*;            
  private import Contracts::*;             
  private import LegalCorePackage::*;             
  private import JurisdictionPackage::*;          

  // ===== Real estate transaction concepts (all items) =====

  item def RealEstateTransaction {
    doc/* transaction involving the transfer, lease, or encumbrance of real property */
    attribute transactionId: String[1..1];
    attribute subjectProperty: RealProperty[1..1];
    attribute transactionType: String[0..1];
    attribute buyer: Parties::Party[0..1];
    attribute seller: Parties::Party[0..1];
    attribute considerationAmount: CurrencyAmount::MonetaryAmount[0..1];
    attribute effectiveDate: Date[0..1];
    attribute closingDate: Date[0..1];
    attribute contract: Contracts::Contract[0..1];
    attribute governingLaw: LegalCorePackage::Law[0..1];
    attribute inJurisdiction: Jurisdiction[0..1];
  }

  item def Conveyance specializes RealEstateTransaction {
    doc/* transaction transferring ownership of real property from one party to another */
    attribute deed: TitleDocument[0..1];
  }

  item def LeaseTransaction specializes RealEstateTransaction {
    doc/* transaction granting temporary possession of real property for a defined term in exchange for rent */
    attribute leaseTermStart: Date[0..1];
    attribute leaseTermEnd: Date[0..1];
    attribute rentAmount: CurrencyAmount::MonetaryAmount[0..1];
  }

  item def MortgageTransaction specializes RealEstateTransaction {
    doc/* transaction in which real property is pledged as security for a loan */
    attribute principalAmount: CurrencyAmount::MonetaryAmount[0..1];
    attribute interestRate: Real[0..1];
    attribute lender: Parties::Party[1..1];
    attribute borrower: Parties::Party[1..1];
    attribute maturityDate: Date[0..1];
  }

  item def EasementGrant specializes RealEstateTransaction {
    doc/* transaction granting a non-possessory right to use anotherâ€™s land for a specific purpose */
    attribute easementDescription: String[0..1];
    attribute benefitingParty: Parties::Party[0..1];
  }

  item def TransactionDocument {
    doc/* legal document recording the details of a real estate transaction */
    attribute documentId: String[1..1];
    attribute transaction: RealEstateTransaction[1..1];
    attribute issueDate: Date[0..1];
  }
}

// ======================
// SecuritiesTransactions.sysml
// ======================
package SecuritiesTransactions {

  private import ScalarValues::*;          
  private import Time::*;                  
  private import CurrencyAmount::*;        
  private import CashFlows::*;             
  private import Agreements::*;            
  private import Contracts::*;             
  private import Parties::*;               
  private import FinancialDates::*;        
  private import LegalCorePackage::*;             
  private import JurisdictionPackage::*;          

  // ===== Securities transaction model (all items) =====

  item def SecuritiesTransaction {
    doc/* transaction involving the purchase, sale, or transfer of a security or group of securities */
    attribute tradeIdentifier: String[1..1];
    attribute tradeDate: Date[0..1];
    attribute settlementDate: Date[0..1];
    attribute buyer: Parties::Party[1..1];
    attribute seller: Parties::Party[1..1];
    attribute securityId: String[0..1];
    attribute quantity: Real[0..1];
    attribute pricePerUnit: CurrencyAmount::MonetaryPrice[0..1];
    attribute totalConsideration: CurrencyAmount::MonetaryAmount[0..1];
    attribute settlementCashFlow: CashFlows::CashFlow[0..1];
    attribute governingLaw: LegalCorePackage::Law[0..1];
    attribute inJurisdiction: Jurisdiction[0..1];
  }

  item def EquityTrade specializes SecuritiesTransaction {
    doc/* securities transaction involving shares of stock */
    attribute shareClass: String[0..1];
    attribute exchange: String[0..1];
  }

  item def BondTrade specializes SecuritiesTransaction {
    doc/* securities transaction involving fixed income instruments such as bonds */
    attribute couponRate: Real[0..1];
    attribute maturityDate: Date[0..1];
    attribute faceValue: CurrencyAmount::MonetaryAmount[0..1];
  }

  item def DerivativesTrade specializes SecuritiesTransaction {
    doc/* transaction involving a derivative contract based on an underlying asset, index, or rate */
    attribute derivativeType: String[0..1];
    attribute notionalAmount: CurrencyAmount::MonetaryAmount[0..1];
    attribute expirationDate: Date[0..1];
    attribute underlyingAsset: String[0..1];
  }

  item def RepoTransaction specializes SecuritiesTransaction {
    doc/* repurchase agreement (repo) transaction involving sale and subsequent repurchase of securities */
    attribute repurchaseDate: Date[0..1];
    attribute repurchasePrice: CurrencyAmount::MonetaryAmount[0..1];
    attribute repoRate: Real[0..1];
  }

  item def SettlementRecord {
    doc/* record of settlement details for a securities transaction */
    attribute transaction: SecuritiesTransaction[1..1];
    attribute settledAmount: CurrencyAmount::MonetaryAmount[1..1];
    attribute settlementDate: Date[1..1];
    attribute settlementStatus: String[0..1];
  }

  item def TradeConfirmation {
    doc/* document sent between parties to confirm details of a securities transaction */
    attribute tradeIdentifier: String[1..1];
    attribute buyer: Parties::Party[1..1];
    attribute seller: Parties::Party[1..1];
    attribute tradeDate: Date[0..1];
    attribute settlementDate: Date[0..1];
    attribute quantity: Real[0..1];
    attribute price: CurrencyAmount::MonetaryPrice[0..1];
    attribute confirmed: Boolean[0..1];
    attribute confirmationDate: Date[0..1];
  }
}
