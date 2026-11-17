// ===================================================================
// FIBO_FND_ProductsAndServices.sysml
// CI Aggregator (public re-export of subpackages)
// ===================================================================
package FIBO_FND_ProductsAndServices {
  doc/* Configuration Item (CI) package for FIBO_FND_ProductsAndServices and its exported model elements. */
  public import ProductsAndServices::*;
  public import PaymentsAndSchedules::*;
}

// ======================
// ProductsAndServices.sysml
// (feature-based from ProductsAndServices.rdf)
// ======================
package ProductsAndServices {

  // ---- Imports (private) ----
  private import ScalarValues::*;        // String, Integer, Boolean, Real
  private import Time::*;                // Date
  private import CurrencyAmount::*;      // MonetaryAmount, MonetaryPrice
  private import Organizations::*;       // Organization (provider/manufacturer)
  private import Parties::*;             // Party (customer)
  private import Agreements::*;          // Agreement (terms)
  private import Contracts::*;           // ContractualElement, Warranty (if later wired)
  private import ClassificationSchemes::*; // ClassificationScheme (if later wired)

  // =========================================================
  // Core catalog entities (tangible/intangible deliverables as parts)
  // =========================================================

  part def Product {
    doc/* artifact, good, instrument, or packaged financial product offered for sale or distribution */
    attribute productName: ProductName[0..*];
    attribute productIdentifier: ProductIdentifier[0..*];
    attribute description: String[0..1];
    attribute category: ProductCategory[0..*];
    attribute manufacturer: Organizations::Organization[0..1];
    attribute provider: Organizations::Organization[0..1];
    attribute feature: ProductFeature[0..*];
    attribute listPrice: CurrencyAmount::MonetaryPrice[0..1];
  }

  part def Service {
    doc/* intangible activity or performance offered for sale or provided to fulfill a need */
    attribute serviceName: ServiceName[0..*];
    attribute serviceIdentifier: ServiceIdentifier[0..*];
    attribute description: String[0..1];
    attribute category: ServiceCategory[0..*];
    attribute provider: Organizations::Organization[0..1];
    attribute serviceLevel: ServiceLevel[0..*];
    attribute fee: CurrencyAmount::MonetaryPrice[0..*];
  }

  part def ProductBundle {
    doc/* combination of two or more products and/or services sold or offered together */
    attribute bundleName: ProductName[0..*];
    attribute includesProduct: Product[0..*];
    attribute includesService: Service[0..*];
    attribute bundlePrice: CurrencyAmount::MonetaryPrice[0..1];
  }

  // =========================================================
  // Offerings, terms, and catalog context
  // =========================================================

  part def Offering {
    doc/* specific commercial proposition to provide a product or service under stated terms (price, availability, conditions) */
    attribute offeringName: String[0..1];
    attribute offeredProduct: Product[0..*];
    attribute offeredService: Service[0..*];
    attribute price: CurrencyAmount::MonetaryPrice[0..*];
    attribute validFrom: Date[0..1];
    attribute validTo: Date[0..1];
    attribute terms: Agreements::Agreement[0..*];
    attribute notes: String[0..*];
  }

  item def EligibilityCriterion {
    doc/* rule or condition that must be met for a party to qualify for an offering */
    attribute criterionText: String[1..1];
  }

  item def Availability {
    doc/* stock, capacity, or readiness information for a product or service */
    attribute status: String[0..1];       // e.g., "InStock", "BackOrder", "Limited", "Retired"
    attribute quantity: Real[0..1];
    attribute locationNote: String[0..1];
  }

  // =========================================================
  // Identification, naming, classification
  // =========================================================

  item def ProductIdentifier {
    doc/* sequence of characters uniquely identifying a product within some context (e.g., SKU, ISIN for financial product) */
    attribute identifierValue: String[1..1];
    attribute schemeName: String[0..1];
    attribute issuingAuthority: String[0..1];
  }

  item def ProductName {
    doc/* contextual designation by which a product is known */
    attribute nameText: String[1..1];
  }

  item def ServiceIdentifier {
    doc/* sequence of characters uniquely identifying a service within some context */
    attribute identifierValue: String[1..1];
    attribute schemeName: String[0..1];
    attribute issuingAuthority: String[0..1];
  }

  item def ServiceName {
    doc/* contextual designation by which a service is known */
    attribute nameText: String[1..1];
  }

  item def ProductCategory {
    doc/* classification applied to products for cataloging or analytics */
    attribute categoryName: String[1..1];
    attribute withinScheme: ClassificationScheme[0..1];
  }

  item def ServiceCategory {
    doc/* classification applied to services for cataloging or analytics */
    attribute categoryName: String[1..1];
    attribute withinScheme: ClassificationScheme[0..1];
  }

  item def ProductFeature {
    doc/* characteristic or capability of a product or service */
    attribute label: String[1..1];
    attribute value: String[0..1];
  }

  item def ServiceLevel {
    doc/* agreed measurable level of service performance */
    attribute metric: String[1..1];       // e.g., availability %, response time
    attribute targetValue: String[0..1];  // scalar or text target
    attribute measurementNotes: String[0..1];
  }

  // =========================================================
  // Parties interacting with products/services
  // =========================================================

  item def Customer {
    doc/* party that consumes or purchases a product or service */
    attribute party: Parties::Party[1..1];
    attribute customerIdentifier: String[0..1];
  }

  item def Subscription {
    doc/* ongoing arrangement to receive products or services in return for consideration */
    attribute subscriber: Parties::Party[1..1];
    attribute subscribedService: Service[0..*];
    attribute subscribedProduct: Product[0..*];
    attribute startDate: Date[0..1];
    attribute endDate: Date[0..1];
    attribute billingPlan: String[0..1];
  }
}

// ======================
// PaymentsAndSchedules.sysml
// (feature-based from PaymentsAndSchedules.rdf)
// ======================
package PaymentsAndSchedules {

  // ---- Imports (private) ----
  private import ScalarValues::*;          // String, Integer, Boolean, Real
  private import Time::*;                  // Date, DateTime
  private import CurrencyAmount::*;        // MonetaryAmount
  private import FinancialDates::*;        // Duration, ExplicitDate, RegularSchedule, ExplicitDatePeriod
  private import CashFlows::*;             // CashFlow (for realization)
  private import Agreements::*;            // Agreement / terms linkage
  private import Contracts::*;             // ContractualElement (e.g., PaymentTerm)

  // =========================================================
  // Payment obligations, terms, and schedules
  // =========================================================

  item def PaymentObligation {
    doc/* enforceable requirement to transfer a monetary amount from a payer to a payee under specified conditions */
    attribute amount: CurrencyAmount::MonetaryAmount[1..1];
    attribute payer: Parties::Party[0..1];
    attribute payee: Parties::Party[0..1];
    attribute dueDate: FinancialDates::ExplicitDate[0..1];
    attribute description: String[0..1];
    attribute arisesFrom: Agreements::Agreement[0..*];
  }

  item def PaymentTerm specializes Contracts::ContractualElement {
    doc/* contractual provision defining amounts, due dates, and methods of payment */
    attribute paymentAmount: CurrencyAmount::MonetaryAmount[0..1];
    attribute dueDate: FinancialDates::ExplicitDate[0..1];
    attribute dueIn: FinancialDates::Duration[0..1];         // e.g., Net 30
    attribute gracePeriod: FinancialDates::Duration[0..1];
    attribute earlyPaymentDiscount: CurrencyAmount::MonetaryAmount[0..1];
    attribute latePaymentFee: CurrencyAmount::MonetaryAmount[0..1];
    attribute paymentMethod: String[0..1];                    // card, ach, wire, cash, crypto
  }

  item def BillingCycle {
    doc/* periodic pattern for invoicing and collection */
    attribute cycleSchedule: FinancialDates::RegularSchedule[0..1];
    attribute anchorDate: FinancialDates::ExplicitDate[0..1];
    attribute description: String[0..1];
  }

  item def PaymentSchedule {
    doc/* sequence or pattern of payment obligations over time */
    attribute schedule: FinancialDates::RegularSchedule[0..1];
    attribute paymentTerm: PaymentTerm[0..*];
    attribute firstPaymentDate: FinancialDates::ExplicitDate[0..1];
    attribute finalPaymentDate: FinancialDates::ExplicitDate[0..1];
  }

  item def RecurringPayment {
    doc/* payment obligation that recurs on a defined schedule */
    attribute templateAmount: CurrencyAmount::MonetaryAmount[1..1];
    attribute schedule: FinancialDates::RegularSchedule[1..1];
    attribute payer: Parties::Party[1..1];
    attribute payee: Parties::Party[1..1];
    attribute startDate: FinancialDates::ExplicitDate[0..1];
    attribute endDate: FinancialDates::ExplicitDate[0..1];
  }

  item def OneTimePayment {
    doc/* single payment obligation due at a specific date */
    attribute amount: CurrencyAmount::MonetaryAmount[1..1];
    attribute dueDate: FinancialDates::ExplicitDate[1..1];
    attribute payer: Parties::Party[1..1];
    attribute payee: Parties::Party[1..1];
    attribute note: String[0..1];
  }

  // =========================================================
  // Realization / linkage to cash flow events
  // =========================================================

  item def PaymentRealization {
    doc/* fulfillment of a payment obligation realized as one or more cash flows */
    attribute obligation: PaymentObligation[1..1];
    attribute realizedBy: CashFlows::CashFlow[1..*];
    attribute realizationDate: Date[0..1];
    attribute status: String[0..1];     // e.g., Pending, Settled, PartiallySettled, Failed, Reversed
  }

  // =========================================================
  // Invoicing and adjustments
  // =========================================================

  item def Invoice {
    doc/* document requesting payment for goods or services provided, listing items, quantities, prices, and terms */
    attribute invoiceNumber: String[1..1];
    attribute issueDate: Date[0..1];
    attribute dueDate: FinancialDates::ExplicitDate[0..1];
    attribute billTo: Parties::Party[0..1];
    attribute seller: Parties::Party[0..1];
    attribute lineItem: InvoiceLine[0..*];
    attribute total: CurrencyAmount::MonetaryAmount[0..1];
    attribute currency: CurrencyAmount::Currency[0..1];
    attribute relatesToAgreement: Agreements::Agreement[0..*];
  }

  item def InvoiceLine {
    doc/* line-level charge or credit associated with a product or service */
    attribute description: String[1..1];
    attribute quantity: Real[0..1];
    attribute unitPrice: CurrencyAmount::MonetaryPrice[0..1];
    attribute lineAmount: CurrencyAmount::MonetaryAmount[0..1];
    attribute product: ProductsAndServices::Product[0..1];
    attribute service: ProductsAndServices::Service[0..1];
  }

  item def CreditNote {
    doc/* document issued to correct or reduce amounts on a previously issued invoice */
    attribute creditNumber: String[1..1];
    attribute issueDate: Date[0..1];
    attribute adjustsInvoice: Invoice[1..1];
    attribute amount: CurrencyAmount::MonetaryAmount[1..1];
    attribute reason: String[0..1];
  }
}
