// ===================================================================
// FIBO_FBC_FunctionalEntities.sysml
// CI Aggregator (public re-export of subpackages)
// Paste this whole file as-is.
// ===================================================================
package FIBO_FBC_FunctionalEntities {
  doc/* Configuration Item (CI) package for FIBO_FBC_FunctionalEntities and its exported model elements. */
  public import FinancialServicesEntities::*;
  public import Markets::*;
  public import MarketsIndividuals::*;
  public import BusinessRegistries::*;
  public import RegistrationAuthorities::*;
  public import InternationalRegistriesAndAuthorities::*;
  public import RegulatoryAgencies::*;
  public import BusinessCenters::*;
  public import BusinessCentersIndividuals::*;
}

// ======================
// FinancialServicesEntities.sysml
// (feature-based from FinancialServicesEntities.rdf)
// ======================
package FinancialServicesEntities {

  private import ScalarValues::*;
  private import Time::*;
  private import Organizations::*;          // Organization / FormalOrganization
  private import Parties::*;                // Party (optional ownership/use)
  private import JurisdictionPackage::*;           // Jurisdiction
  private import CurrencyAmount::*;         // Currency for operating currency, etc.

  // ---- Core financial service providers (processors = parts) ----

  part def FinancialServiceProvider specializes Organizations::Organization {
    doc/* organization that provides financial services such as banking, brokerage, asset management, or insurance */
    attribute lei: String[0..1];
    attribute operatingJurisdiction: Jurisdiction[0..*];
    attribute operatingCurrency: CurrencyAmount::Currency[0..*];
    attribute businessLineText: String[0..*];
  }

  part def Bank specializes FinancialServiceProvider {
    doc/* deposit-taking institution providing credit, payments, and treasury services */
    attribute swiftBic: String[0..1];
  }

  part def BrokerDealer specializes FinancialServiceProvider {
    doc/* firm that trades securities for its own account or on behalf of customers */
    attribute membershipVenueMic: String[0..*];
  }

  part def InvestmentFirm specializes FinancialServiceProvider {
    doc/* firm providing investment services such as portfolio management, advisory, or execution */
  }

  part def AssetManager specializes FinancialServiceProvider {
    doc/* firm managing assets on behalf of clients through pooled vehicles or mandates */
    attribute aumAmountText: String[0..1];
  }

  part def InsuranceCompany specializes FinancialServiceProvider {
    doc/* insurer underwriting risks and issuing insurance contracts */
  }

  part def Custodian specializes FinancialServiceProvider {
    doc/* institution responsible for safekeeping of financial instruments and servicing */
  }

  part def TradeRepository specializes FinancialServiceProvider {
    doc/* entity that maintains a centralized electronic record of transactions */
  }
}

// ======================
// Markets.sysml
// (feature-based from Markets.rdf)
// ======================
package Markets {

  private import ScalarValues::*;
  private import Time::*;
  private import Organizations::*;
  private import Parties::*;
  private import JurisdictionPackage::*;
  private import CurrencyAmount::*;

  // ---- Identifiers ----
  item def MarketIdentifierCode {
    doc/* ISO 10383 Market Identifier Code (MIC) */
    attribute mic: String[1..1];
    attribute operatingMic: String[0..1];
    attribute segment: String[0..1];
  }

  // ---- Market operators and venues (processors = parts) ----
  part def MarketOperator specializes Organizations::Organization {
    doc/* organization that operates one or more trading venues or exchanges */
    attribute mic: MarketIdentifierCode[0..*];
    attribute jurisdiction: Jurisdiction[0..*];
  }

  part def TradingVenue {
    doc/* facility where multiple third-party buying and selling interests interact, resulting in contracts */
    attribute name: String[1..1];
    attribute mic: MarketIdentifierCode[0..1];
    attribute operator: MarketOperator[0..1];
    attribute venueTypeText: String[0..1];  // Exchange, MTF, OTF, ATS, SI
    attribute countryCode: String[0..1];
    attribute timeZone: String[0..1];
    attribute tradingCurrency: CurrencyAmount::Currency[0..*];
  }

  part def Exchange specializes TradingVenue {
    doc/* regulated market or national securities exchange */
  }

  part def MultilateralTradingFacility specializes TradingVenue { }
  part def OrganizedTradingFacility   specializes TradingVenue { }
  part def AlternativeTradingSystem   specializes TradingVenue { }
  part def SystematicInternaliser     specializes TradingVenue { }

  // ---- Post-trade market infrastructures (processors = parts) ----
  part def CentralCounterparty {
    doc/* clearing house interposing itself between buyers and sellers to become counterparty to all trades */
    attribute name: String[1..1];
    attribute lei: String[0..1];
    attribute jurisdiction: Jurisdiction[0..*];
  }

  part def CentralSecuritiesDepository {
    doc/* institution providing securities accounts, central safekeeping, and settlement services */
    attribute name: String[1..1];
    attribute bic: String[0..1];
    attribute jurisdiction: Jurisdiction[0..*];
  }

  // ---- Membership & listings (items) ----
  item def VenueMembership {
    doc/* membership relationship between a participant and a trading venue */
    attribute member: Parties::Party[1..1];
    attribute venue: TradingVenue[1..1];
    attribute membershipId: String[0..1];
    attribute active: Boolean[0..1];
  }

  item def ListingRecord {
    doc/* record of a financial instrument admitted to trading or listed on a venue */
    attribute venue: TradingVenue[1..1];
    attribute instrumentIdentifier: String[1..1]; // ISIN/FIGI/etc.
    attribute ticker: String[0..1];
    attribute listingDate: Date[0..1];
    attribute delistingDate: Date[0..1];
  }
}

// ======================
// MarketsIndividuals.sysml
// (feature-based from MarketsIndividuals.rdf)
// ======================
package MarketsIndividuals {

  private import ScalarValues::*;
  private import Time::*;
  private import Markets::*;

  // Canonical example instances (items referencing codes/venues)
  item def Example_MIC_XNAS {
    doc/* example MIC record for NASDAQ - All Markets */
    attribute id: Markets::MarketIdentifierCode[1..1];
  }

  item def Example_MIC_XNYS {
    doc/* example MIC record for NYSE - All Markets */
    attribute id: Markets::MarketIdentifierCode[1..1];
  }
}

// ======================
// BusinessRegistries.sysml
// (feature-based from BusinessRegistries.rdf)
// ======================
package BusinessRegistries {

  private import ScalarValues::*;
  private import Time::*;
  private import Organizations::*;
  private import JurisdictionPackage::*;
  private import Parties::*;

  // Registry operator (processor = part)
  part def BusinessRegistry specializes Organizations::Organization {
    doc/* organization responsible for registering legal entities and maintaining public records */
    attribute jurisdiction: Jurisdiction[1..1];
    attribute registryName: String[0..1];
    attribute website: String[0..1];
  }

  // Registration record (item)
  item def BusinessRegistration {
    doc/* registration entry for a legal entity maintained by a business registry */
    attribute registry: BusinessRegistry[1..1];
    attribute registeredEntity: Parties::Party[1..1];
    attribute registrationNumber: String[1..1];
    attribute registrationDate: Date[0..1];
    attribute statusText: String[0..1];
  }
}

// ======================
// RegistrationAuthorities.sysml
// (feature-based from RegistrationAuthorities.rdf)
// ======================
package RegistrationAuthorities {

  private import ScalarValues::*;
  private import Time::*;
  private import Organizations::*;
  private import JurisdictionPackage::*;

  part def RegistrationAuthority specializes Organizations::Organization {
    doc/* authority designated to allocate identifiers or maintain official registers (e.g., LEI, company numbers) */
    attribute authorityCode: String[0..1];
    attribute jurisdiction: Jurisdiction[0..*];
  }

  item def IdentifierAssignmentScheme {
    doc/* scheme managed by a registration authority for issuing identifiers */
    attribute name: String[1..1];
    attribute description: String[0..1];
    attribute authority: RegistrationAuthority[1..1];
    attribute identifierPattern: String[0..1];
  }
}

// ======================
// InternationalRegistriesAndAuthorities.sysml
// (feature-based from InternationalRegistriesAndAuthorities.rdf)
// ======================
package InternationalRegistriesAndAuthorities {

  private import ScalarValues::*;
  private import Time::*;
  private import Organizations::*;
  private import RegistrationAuthorities::*;
  private import JurisdictionPackage::*;

  part def InternationalOrganization specializes Organizations::Organization {
    doc/* international or supranational body with global or multi-jurisdictional mandate */
    attribute shortName: String[0..1];
    attribute jurisdictionCoverageText: String[0..1];
  }

  part def ISORegistrationAuthority specializes RegistrationAuthorities::RegistrationAuthority {
    doc/* ISO-designated registration authority for specific code lists or standards */
    attribute isoStandardCode: String[1..1]; // e.g., 10383 MIC, 17442 LEI
  }

  item def CodeScheme {
    doc/* international code scheme maintained by an authority (e.g., MIC, ISIN, LEI) */
    attribute name: String[1..1];
    attribute standardReference: String[0..1];
    attribute maintainedBy: RegistrationAuthorities::RegistrationAuthority[1..1];
    attribute description: String[0..1];
  }
}

// ======================
// RegulatoryAgencies.sysml
// (feature-based from RegulatoryAgencies.rdf)
// ======================
package RegulatoryAgencies {

  private import ScalarValues::*;
  private import Time::*;
  private import Organizations::*;
  private import JurisdictionPackage::*;

  // Regulators (processors = parts)
  part def Regulator specializes Organizations::Organization {
    doc/* public authority that creates, interprets, or enforces regulations within a defined scope */
    attribute jurisdiction: Jurisdiction[1..*];
    attribute scopeText: String[0..*];     // e.g., securities, banking, insurance, markets
    attribute website: String[0..1];
  }

  part def SecuritiesRegulator specializes Regulator { }
  part def BankingRegulator   specializes Regulator { }
  part def InsuranceRegulator specializes Regulator { }
  part def MarketRegulator    specializes Regulator { }
  part def DataProtectionAuthority specializes Regulator { }

  // Regulatory references (items)
  item def RegulationReference {
    doc/* reference to a specific regulation, directive, or rule administered by a regulator */
    attribute title: String[1..1];
    attribute citation: String[0..1];
    attribute regulator: Regulator[0..1];
    attribute effectiveDate: Date[0..1];
  }
}

// ======================
// BusinessCenters.sysml
// (feature-based from BusinessCenters.rdf)
// ======================
package BusinessCenters {

  private import ScalarValues::*;
  private import Time::*;
  private import Locations::*;              // TimeZone if defined; else use String
  private import JurisdictionPackage::*;           // Jurisdiction
  private import FinancialDates::*;         // for calendars/schedules

  // A Business Center is used to define calendars for date adjustments (items)
  item def BusinessCenter {
    doc/* location-specific code defining the set of days treated as business days for financial date calculations */
    attribute code: String[1..1];            // e.g., "GBLO", "USNY", "JPTO"
    attribute cityName: String[0..1];
    attribute countryCode: String[0..1];
    attribute timeZoneText: String[0..1];
    attribute jurisdiction: Jurisdiction[0..1];
  }

  item def BusinessDayCalendar {
    doc/* calendar used to determine business vs. non-business days for one or more business centers */
    attribute name: String[1..1];
    attribute center: BusinessCenter[1..*];
    attribute holidayDescription: String[0..*]; // free-text placeholder for holiday rules
    attribute validFrom: Date[0..1];
    attribute validTo: Date[0..1];
  }

  item def CenterPairing {
    doc/* commonly used pair or list of centers for date adjustment (e.g., "GBLO/USNY") */
    attribute label: String[1..1];
    attribute centers: BusinessCenter[2..*];
  }
}

// ======================
// BusinessCentersIndividuals.sysml
// (feature-based from BusinessCentersIndividuals.rdf)
// ======================
package BusinessCentersIndividuals {

  private import ScalarValues::*;
  private import Time::*;
  private import BusinessCenters::*;

  // Canonical example individuals (items)
  item def Center_GBLO specializes BusinessCenters::BusinessCenter {
    doc/* London business center (United Kingdom) */
  }

  item def Center_USNY specializes BusinessCenters::BusinessCenter {
    doc/* New York business center (United States) */
  }
}
