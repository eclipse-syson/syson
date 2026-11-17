// ===================================================================
// FIBO_SEC_Debt.sysml
// CI Aggregator (public re-export of SEC Debt subpackages)
// Paste this whole file as-is.
// ===================================================================
package FIBO_SEC_Debt {
  doc/* Configuration Item (CI) package for FIBO_SEC_Debt and its exported model elements. */
  public import DebtInstruments::*;
  public import Bonds::*;
  public import TradedShortTermDebt::*;
  public import AssetBackedSecurities::*;
  public import MortgageBackedSecurities::*;
  public import PoolBackedSecurities::*;
  public import CollateralizedDebtObligations::*;
  public import SyntheticCDOs::*;
  public import DistributedLoans::*;
  public import ExerciseConventions::*;
}

// ======================
// DebtInstruments.sysml
// ======================
package DebtInstruments {

  // ---- Imports (private) ----
  private import ScalarValues::*;          // String, Integer, Real, Boolean
  private import Time::*;                  // Date, DateTime
  private import CurrencyAmount::*;        // MonetaryAmount, Currency
  private import FinancialDates::*;        // Duration, ExplicitDate, ExplicitDatePeriod, RegularSchedule
  private import Organizations::*;         // Organization
  private import FormalOrganizations::*;   // FormalOrganization
  private import Parties::*;               // Party
  private import ExerciseConventions::*;   // DayCount, BusinessDayConvention, YieldConvention, CouponType

  // ---- Core abstractions ----

  item def DebtSecurity {
    doc/* security representing a contractual obligation to pay interest and repay principal */
    attribute securityId: String[0..*];                        // ISIN/CUSIP/etc. (use FBC identifiers if wired)
    attribute issuer: FormalOrganization[0..1];
    attribute guarantor: FormalOrganization[0..*];
    attribute currency: Currency[0..1];
    attribute faceValue: MonetaryAmount[0..1];
    attribute issueDate: Date[0..1];
    attribute maturityDate: Date[0..1];
    attribute couponType: CouponType[0..1];
    attribute couponRatePct: Real[0..1];
    attribute dayCount: DayCount[0..1];
    attribute businessDayConvention: BusinessDayConvention[0..1];
    attribute yieldConvention: YieldConvention[0..1];
    attribute paymentSchedule: RegularSchedule[0..1];
    attribute rankingText: String[0..1];                       // seniority / subordination text
    attribute governingLawText: String[0..1];
  }

  item def Note specializes DebtSecurity {
    doc/* debt security generally with original maturity > 1 year, often unsecured */
  }

  item def Debenture specializes DebtSecurity {
    doc/* long-term unsecured debt obligation backed by issuer creditworthiness */
  }

  item def SecuredDebtSecurity specializes DebtSecurity {
    doc/* debt security backed by specific collateral */
    attribute collateralDescription: String[0..1];
    attribute collateralAgent: FormalOrganization[0..1];
  }

  item def CallableDebt specializes DebtSecurity {
    doc/* debt security with issuer call rights */
    attribute callSchedule: CallSchedule[0..*];
  }

  item def PuttableDebt specializes DebtSecurity {
    doc/* debt security with holder put rights */
    attribute putSchedule: PutSchedule[0..*];
  }

  item def ConvertibleDebt specializes DebtSecurity {
    doc/* debt convertible into equity on defined terms */
    attribute conversionRatio: Real[0..1];
    attribute conversionPrice: MonetaryAmount[0..1];
    attribute conversionWindow: ExplicitDatePeriod[0..1];
  }

  item def PerpetualDebt specializes DebtSecurity {
    doc/* debt with no stated maturity (perpetual) */
  }

  // Embedded option schedules (reused by bonds as well)
  item def CallSchedule {
    attribute callDate: Date[1..1];
    attribute callPricePer100: Real[0..1];
    attribute notes: String[0..1];
  }
  item def PutSchedule {
    attribute putDate: Date[1..1];
    attribute putPricePer100: Real[0..1];
    attribute notes: String[0..1];
  }
}

// ======================
// Bonds.sysml
// ======================
package Bonds {

  private import ScalarValues::*;
  private import Time::*;
  private import CurrencyAmount::*;
  private import FinancialDates::*;
  private import FormalOrganizations::*;
  private import ExerciseConventions::*;
  private import DebtInstruments::*;       // DebtSecurity, schedules

  item def Bond specializes DebtSecurity {
    doc/* generic bond instrument */
  }

  item def GovernmentBond specializes Bond {
    doc/* bond issued by a sovereign, sub-sovereign, or agency */
    attribute levelText: String[0..1];                    // central, state/province, municipal, agency
  }

  item def TreasuryBond specializes GovernmentBond { }
  item def TreasuryNote  specializes GovernmentBond { }
  item def TreasuryBill  specializes GovernmentBond {
    doc/* short-term discount instrument (t-bill) */
    attribute discountYieldPct: Real[0..1];
  }

  item def CorporateBond specializes Bond {
    doc/* bond issued by a corporate issuer */
    attribute sectorText: String[0..1];                   // industrial, financial, utility, etc.
  }

  item def FloatingRateNote specializes Bond {
    doc/* floating-rate bond with index + spread */
    attribute referenceIndexText: String[0..1];           // e.g., SOFR 3M
    attribute spreadBps: Real[0..1];
    attribute resetFrequencyMonths: Integer[0..1];
    attribute resetConvention: ResetConvention[0..1];
  }

  item def InflationLinkedBond specializes Bond {
    doc/* bond indexed to an inflation measure (e.g., CPI) */
    attribute referenceCpiText: String[0..1];
    attribute indexationLagMonths: Integer[0..1];
  }

  item def ZeroCouponBond specializes Bond {
    doc/* discount bond with no periodic coupons; repays face at maturity */
  }

  item def SubordinatedBond specializes CorporateBond {
    doc/* bond contractually subordinated to senior debt */
    attribute subordinationText: String[0..1];
  }

  item def PerpetualBond specializes Bond { }
  item def ConvertibleBond specializes ConvertibleDebt { }
  item def CallableBond    specializes CallableDebt  { }
  item def PuttableBond    specializes PuttableDebt  { }
}

// ==============================
// TradedShortTermDebt.sysml
// ==============================
package TradedShortTermDebt {

  private import ScalarValues::*;
  private import Time::*;
  private import CurrencyAmount::*;
  private import FormalOrganizations::*;
  private import DebtInstruments::*;
  private import ExerciseConventions::*;

  item def TradedShortTermDebtInstrument specializes DebtSecurity {
    doc/* money market instrument with short original maturity, typically discount or simple interest */
    attribute originalMaturityDays: Integer[0..1];
  }

  item def CommercialPaper specializes TradedShortTermDebtInstrument {
    attribute programIssuer: FormalOrganization[0..1];
    attribute programSizeAmount: MonetaryAmount[0..1];
    attribute isAssetBacked: Boolean[0..1];
  }

  item def CertificateOfDeposit specializes TradedShortTermDebtInstrument {
    attribute depositBank: FormalOrganization[0..1];
  }

  item def BankersAcceptance specializes TradedShortTermDebtInstrument {
    attribute acceptingBank: FormalOrganization[0..1];
  }

  item def TreasuryBill specializes TradedShortTermDebtInstrument {
    attribute discountYieldPct: Real[0..1];
  }
}

// ==================================
// AssetBackedSecurities.sysml
// ==================================
package AssetBackedSecurities {

  private import ScalarValues::*;
  private import Time::*;
  private import CurrencyAmount::*;
  private import FinancialDates::*;
  private import FormalOrganizations::*;
  private import PoolBackedSecurities::*;   // Tranche, CreditEnhancement, Pool, Waterfall

  item def AssetBackedSecurity {
    doc/* security collateralized by a pool of financial assets other than first-lien residential mortgages */
    attribute issuer: FormalOrganization[0..1];            // trust/SPV
    attribute sponsor: FormalOrganization[0..1];
    attribute servicer: FormalOrganization[0..1];
    attribute collateralPool: AssetPool[0..1];
    attribute tranche: Tranche[0..*];
    attribute creditEnhancement: CreditEnhancement[0..*];
    attribute waterfall: PaymentWaterfall[0..1];
    attribute currency: Currency[0..1];
  }

  item def ConsumerABS specializes AssetBackedSecurity {
    doc/* ABS backed by consumer receivables (auto, credit card, student loans, etc.) */
    attribute collateralTypeText: String[0..1];
  }

  item def EquipmentABS specializes AssetBackedSecurity { }
  item def AutoLoanABS  specializes ConsumerABS { }
  item def AutoLeaseABS specializes ConsumerABS { }
  item def CreditCardABS specializes ConsumerABS { }
  item def StudentLoanABS specializes ConsumerABS { }
}

// ==================================
// MortgageBackedSecurities.sysml
// ==================================
package MortgageBackedSecurities {

  private import ScalarValues::*;
  private import Time::*;
  private import CurrencyAmount::*;
  private import FinancialDates::*;
  private import FormalOrganizations::*;
  private import PoolBackedSecurities::*;
  private import AssetBackedSecurities::*;

  item def MortgageBackedSecurity {
    doc/* security collateralized by pools of mortgage loans */
    attribute issuer: FormalOrganization[0..1];
    attribute servicer: FormalOrganization[0..1];
    attribute collateralPool: MortgagePool[0..1];
    attribute tranche: Tranche[0..*];
    attribute waterfall: PaymentWaterfall[0..1];
    attribute currency: Currency[0..1];
    attribute weightedAverageCouponPct: Real[0..1];      // WAC
    attribute weightedAverageMaturityMonths: Integer[0..1]; // WAM
  }

  item def AgencyMBS specializes MortgageBackedSecurity {
    doc/* MBS guaranteed by an agency/GSE */
    attribute guarantor: FormalOrganization[0..1];
  }

  item def PrivateLabelMBS specializes MortgageBackedSecurity { }

  item def PassThroughMBS specializes MortgageBackedSecurity {
    doc/* structure where principal and interest are passed through to investors pro rata */
  }

  item def CollateralizedMortgageObligation specializes MortgageBackedSecurity {
    doc/* tranched MBS (CMO) */
  }

  // Collateral
  item def MortgagePool specializes AssetPool {
    attribute poolFactor: Real[0..1];
    attribute averageFico: Real[0..1];
    attribute propertyTypeText: String[0..1];
  }
}

// ==================================
// PoolBackedSecurities.sysml
// ==================================
package PoolBackedSecurities {

  private import ScalarValues::*;
  private import Time::*;
  private import CurrencyAmount::*;
  private import FinancialDates::*;
  private import FormalOrganizations::*;

  // Pools
  item def AssetPool {
    doc/* pool of financial assets that generate cash flows */
    attribute poolId: String[0..1];
    attribute currentBalance: MonetaryAmount[0..1];
    attribute numberOfLoans: Integer[0..1];
    attribute cutOffDate: Date[0..1];
  }

  // Tranches
  item def Tranche {
    doc/* class of security with defined priority and coupon */
    attribute className: String[0..1];
    attribute initialBalance: MonetaryAmount[0..1];
    attribute couponRatePct: Real[0..1];
    attribute priorityLevel: Integer[0..1];
    attribute legalFinalMaturity: Date[0..1];
    attribute expectedMaturity: Date[0..1];
  }

  // Waterfall & CE
  item def PaymentWaterfall {
    doc/* rules for allocating cash flows to tranches (interest, principal, losses) */
    attribute description: String[0..1];
    attribute paymentFrequencyMonths: Integer[0..1];
  }

  item def CreditEnhancement {
    doc/* mechanism providing protection against collateral losses */
    attribute kind: String[0..1];                         // Subordination, OC, Reserve, Guarantee
    attribute provider: FormalOrganization[0..1];
    attribute amountOrPercentText: String[0..1];
  }
}

// ===============================================
// CollateralizedDebtObligations.sysml (cash CDOs)
// ===============================================
package CollateralizedDebtObligations {

  private import ScalarValues::*;
  private import Time::*;
  private import CurrencyAmount::*;
  private import FinancialDates::*;
  private import FormalOrganizations::*;
  private import PoolBackedSecurities::*;

  item def CollateralizedDebtObligation {
    doc/* tranched obligations backed by a portfolio of debt instruments (cash CDO) */
    attribute issuer: FormalOrganization[0..1];
    attribute manager: FormalOrganization[0..1];
    attribute referencePortfolio: AssetPool[0..1];
    attribute tranche: Tranche[0..*];
    attribute creditEnhancement: CreditEnhancement[0..*];
    attribute waterfall: PaymentWaterfall[0..1];
    attribute reinvestmentPeriod: ExplicitDatePeriod[0..1];
  }

  item def CLO specializes CollateralizedDebtObligation {
    doc/* CDO backed primarily by leveraged loans */
  }

  item def CBO specializes CollateralizedDebtObligation {
    doc/* CDO backed primarily by bonds */
  }
}

// ======================
// SyntheticCDOs.sysml
// ======================
package SyntheticCDOs {

  private import ScalarValues::*;
  private import Time::*;
  private import CurrencyAmount::*;
  private import FinancialDates::*;
  private import FormalOrganizations::*;
  private import PoolBackedSecurities::*;

  item def SyntheticCDO {
    doc/* tranched structure referencing a portfolio via credit default swaps (no physical assets) */
    attribute issuer: FormalOrganization[0..1];
    attribute swapCounterparty: FormalOrganization[0..1];
    attribute referenceObligationsText: String[0..1];  // summary of CDS reference portfolio
    attribute tranche: Tranche[0..*];
    attribute creditEnhancement: CreditEnhancement[0..*];
    attribute waterfall: PaymentWaterfall[0..1];
  }
}

// ======================
// DistributedLoans.sysml
// ======================
package DistributedLoans {

  private import ScalarValues::*;
  private import Time::*;
  private import CurrencyAmount::*;
  private import FinancialDates::*;
  private import FormalOrganizations::*;
  private import Parties::*;

  item def SyndicatedLoan {
    doc/* loan extended by a group of lenders and administered by an agent bank */
    attribute borrower: Parties::Party[0..1];
    attribute agentBank: FormalOrganization[0..1];
    attribute lenders: FormalOrganization[0..*];
    attribute commitmentAmount: MonetaryAmount[0..1];
    attribute currency: Currency[0..1];
    attribute marginBps: Real[0..1];
    attribute maturityDate: Date[0..1];
    attribute interestResetFrequencyMonths: Integer[0..1];
  }

  item def LoanParticipation {
    doc/* transfer of an economic interest in a loan without novation */
    attribute seller: FormalOrganization[0..1];
    attribute buyer: FormalOrganization[0..1];
    attribute percentageSold: Real[0..1];
    attribute settlementDate: Date[0..1];
  }
}

// ======================
// ExerciseConventions.sysml
// ======================
package ExerciseConventions {

  private import ScalarValues::*;
  private import Time::*;

  enum def CouponType {
    doc/* coupon form for interest calculation */
    enum Fixed; enum Floating; enum Zero; enum StepUp; enum StepDown; enum InflationLinked; enum Other;
  }

  enum def DayCount {
    doc/* day count fraction for accrual calculations */
    enum ACT_360; enum ACT_365F; enum ACT_ACT_ISDA; enum Thirty_360; enum ThirtyE_360; enum Other;
  }

  enum def BusinessDayConvention {
    doc/* date adjustment rule when scheduled date falls on non-business day */
    enum Following; enum ModifiedFollowing; enum Preceding; enum ModifiedPreceding; enum Unadjusted; enum Other;
  }

  enum def ResetConvention { enum InAdvance; enum InArrears; }

  enum def YieldConvention {
    doc/* yield quotation basis */
    enum Street; enum Treasury; enum MoneyMarket; enum Discount; enum True; enum Simple; enum Other;
  }
}



