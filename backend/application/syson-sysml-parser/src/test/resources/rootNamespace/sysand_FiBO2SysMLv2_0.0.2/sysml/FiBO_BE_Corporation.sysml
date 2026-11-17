// ===================================================================
// FIBO_BE_Corporations.sysml
// CI Aggregator (public re-export of Corporations)
// Paste this whole file as-is.
// ===================================================================
package FIBO_BE_Corporations {
  doc/* Configuration Item (CI) package for FIBO_BE_Corporations and its exported model elements. */
  public import Corporations::*;
}

// ======================
// Corporations.sysml
// ======================
package Corporations {

  // ---- Imports (private) ----
  private import ScalarValues::*;                 // String, Integer, Real, Boolean
  private import Time::*;                         // Date, DateTime
  private import Organizations::*;                // Organization / Enterprise / BusinessUnit
  private import FormalOrganizations::*;          // FormalOrganization
  private import Parties::*;                      // Party (for roles where needed)
  private import JurisdictionPackage::*;          // Jurisdiction (FND Law)
  private import LegalCorePackage::*;             // Law, LegalInstrument (FND Law)
  private import Documents::*;                    // Document, Text
  private import Ownership::*;                    // Owner, OwnershipInterest (FND Ownership & Control) â€” optional
  private import Control::*;                      // ControlRelationship (FND Ownership & Control) â€” optional
  private import Addresses::*;                    // PostalAddress
  private import Locations::*;                    // GeopoliticalEntity
  private import FinancialDates::*;               // Duration (for mandate terms if needed)

  // ---- Enums / code lists ----

  enum def CorporationKind {
    doc/* high-level legal categories for corporations; concrete legal forms may be added per jurisdiction */
    enum Public;               // publicly held / listed
    enum Private;              // closely held / private
    enum NonProfit;            // not-for-profit
    enum Close;                // closely-held / closed corporation
    enum Benefit;              // public benefit / B-Corp (where recognized)
    enum Professional;         // professional corp (PC/PA)
    enum Other;
  }

  enum def IncorporationStatus {
    enum Proposed; enum Active; enum InLiquidation; enum Dissolved; enum StruckOff; enum Administrated; enum Merged;
  }

  enum def OfficerRoleKind {
    enum ChiefExecutiveOfficer; enum ChiefFinancialOfficer; enum ChiefOperatingOfficer;
    enum President; enum VicePresident; enum Treasurer; enum Secretary; enum GeneralCounsel; enum Other;
  }

  enum def CompanyIdentifierScheme {
    enum LEI;                   // ISO 17442
    enum LocalCompanyNumber;    // national/company registry number
    enum TaxId;                 // tax identification (e.g., EIN)
    enum BusinessRegistryId;    // registry-specific id
    enum DUNS;                  // D-U-N-S number
    enum Other;
  }

  // ---- Core corporate abstractions ----

  // A corporation is a FormalOrganization
  part def Corporation specializes FormalOrganization {
    doc/* legal entity formed under corporate law, with separate legal personality and limited liability (unless otherwise specified) */
    attribute kind: CorporationKind[0..1];
    attribute status: IncorporationStatus[0..1];
    attribute dateOfIncorporation: Date[0..1];
    attribute dateOfDissolution: Date[0..1];
    attribute registeredOffice: Addresses::PostalAddress[0..1];

    attribute governingLaw: LegalCorePackage::Law[0..1];
    attribute companyIdentifier: CompanyIdentifier[0..*];
    attribute articles: ArticlesOfIncorporation[0..1];
    attribute bylaws: CorporateBylaws[0..1];
    attribute board: BoardOfDirectors[0..1];
    attribute officer: CorporateOfficer[0..*];
    attribute shareRegistry: ShareRegister[0..1];
    attribute beneficialOwner: BeneficialOwner[0..*];
    attribute controller: Control::Controller[0..*];
    attribute controlRelationship: Control::ControlRelationship[0..*];
    attribute website: String[0..*];
  }

  // Common specializations (jurisdiction-neutral)
  part def PublicCorporation   specializes Corporation { }
  part def PrivateCorporation  specializes Corporation { }
  part def NonProfitCorporation specializes Corporation { }
  part def CloseCorporation    specializes Corporation { }
  part def BenefitCorporation  specializes Corporation { }
  part def ProfessionalCorporation specializes Corporation { }

  // Legal formation & governance documents
  item def ArticlesOfIncorporation {
    doc/* constitutive document filed with the competent registry establishing the corporationâ€™s existence, name, purpose, share structure, and registered office */
    attribute document: Document[0..1];
    attribute filingDate: Date[0..1];
    attribute filedWith: Jurisdiction[0..1];
    attribute text: Text[0..*];
  }

  item def CorporateBylaws {
    doc/* internal rules adopted by a corporation to govern its management, board procedures, officer roles, and shareholder meetings */
    attribute document: Document[0..1];
    attribute adoptionDate: Date[0..1];
    attribute lastAmendedDate: Date[0..1];
    attribute text: Text[0..*];
  }

  // Registration details
  item def CompanyIdentifier {
    doc/* an identifier assigned to a corporation under a specific scheme (e.g., LEI, registry number, tax id) */
    attribute scheme: CompanyIdentifierScheme[1..1];
    attribute value: String[1..1];
    attribute assigningAuthority: Organizations::Organization[0..1];
    attribute assignedOn: Date[0..1];
    attribute statusNote: String[0..1];
  }

  item def CompanyRegistration {
    doc/* record of corporate registration in a particular jurisdiction and registry */
    attribute corporation: Corporation[1..1];
    attribute registryName: String[0..1];
    attribute registrationNumber: String[0..1];
    attribute registrationDate: Date[0..1];
    attribute inJurisdiction: Jurisdiction[0..1];
    attribute status: IncorporationStatus[0..1];
  }

  // Governance structure
  part def BoardOfDirectors {
    doc/* governing body appointed or elected to oversee corporate management and act in the best interests of the shareholders */
    attribute corporation: Corporation[1..1];
    attribute director: Director[1..*];
    attribute chair: Director[0..1];
    attribute committee: BoardCommittee[0..*];
  }

  part def BoardCommittee {
    doc/* subset of directors tasked with specific oversight responsibilities (e.g., audit, remuneration, risk) */
    attribute name: String[1..1];
    attribute directorMember: Director[1..*];
    attribute charterDocument: Document[0..1];
  }

  part def Director {
    doc/* natural person or, where permitted, a corporate director serving on the board with fiduciary duties to the corporation */
    attribute person: Parties::Party[0..1];          // typically a natural person
    attribute appointedOn: Date[0..1];
    attribute resignedOn: Date[0..1];
    attribute independent: Boolean[0..1];
  }

  part def CorporateOfficer {
    doc/* individual entrusted with executive responsibility under the bylaws (e.g., CEO, CFO, Secretary) */
    attribute person: Parties::Party[0..1];
    attribute roleKind: OfficerRoleKind[1..1];
    attribute appointedOn: Date[0..1];
    attribute resignedOn: Date[0..1];
    attribute employmentContractRef: Document[0..1];
  }

  // Share capital & registers
  item def ShareClass {
    doc/* defined class of shares with specific rights, preferences, and restrictions (e.g., common, preferred) */
    attribute className: String[1..1];
    attribute votingRightsDescription: String[0..1];
    attribute dividendPolicyDescription: String[0..1];
    attribute liquidationPreferenceText: String[0..1];
    attribute parValueText: String[0..1];
    attribute isRestricted: Boolean[0..1];
  }

  item def ShareRegister {
    doc/* official record of issued shares and registered holders maintained by the corporation or a transfer agent */
    attribute maintainedBy: Organizations::Organization[0..1];
    attribute shareRegisterEntry: ShareRegisterEntry[0..*];
    attribute lastUpdated: DateTime[0..1];
  }

  item def ShareRegisterEntry {
    doc/* a position in the share register referencing a holder, class, and quantity */
    attribute corporation: Corporation[1..1];
    attribute holder: Owner[1..1];   // owner role from Ownership CI
    attribute shareClass: ShareClass[1..1];
    attribute quantity: Real[1..1];
    attribute asOfDate: Date[0..1];
    attribute restrictedLegend: String[0..1];
  }

  // Corporate actions (lightweight hooks here; full modeling lives in CAE domain)
  item def CorporateActionReference {
    doc/* pointer to an external corporate action event affecting capital structure or entitlements */
    attribute corporation: Corporation[1..1];
    attribute actionType: String[1..1];     // e.g., dividend, split, merger, rights issue
    attribute effectiveDate: Date[0..1];
    attribute referenceId: String[0..1];
  }

  // Lifecycle changes
  item def IncorporationEvent {
    doc/* event establishing the corporation as a legal person upon acceptance of filings */
    attribute corporation: Corporation[1..1];
    attribute filing: ArticlesOfIncorporation[0..1];
    attribute inJurisdiction: Jurisdiction[0..1];
    attribute effectiveOn: Date[0..1];
    attribute registerEntry: CompanyRegistration[0..1];
  }

  item def DissolutionEvent {
    doc/* event terminating the legal existence of a corporation following liquidation or strike-off procedure */
    attribute corporation: Corporation[1..1];
    attribute effectiveOn: Date[0..1];
    attribute reasonText: String[0..1];
    attribute legalBasis: LegalCorePackage::Law[0..*];
  }

  item def NameChangeEvent {
    doc/* event recording a change of the corporationâ€™s legal or trading name */
    attribute corporation: Corporation[1..1];
    attribute previousName: String[1..1];
    attribute newName: String[1..1];
    attribute effectiveOn: Date[0..1];
  }

  item def RegisteredOfficeChangeEvent {
    doc/* event recording a change of the corporationâ€™s registered office */
    attribute corporation: Corporation[1..1];
    attribute previousAddress: Addresses::PostalAddress[0..1];
    attribute newAddress: Addresses::PostalAddress[1..1];
    attribute effectiveOn: Date[0..1];
  }

  // Disclosure & filings
  item def CompanyFiling {
    doc/* document filed with a registry or market authority for compliance or disclosure purposes */
    attribute corporation: Corporation[1..1];
    attribute title: String[1..1];
    attribute filingDate: Date[0..1];
    attribute filedWith: Organizations::Organization[0..1]; // registry or authority
    attribute document: Document[0..1];
  }

  // Cross-links to markets (optional â€“ detailed modeling lives in FBC/SEC)
  item def ListingReference {
    doc/* reference to a market listing for the corporationâ€™s equity */
    attribute corporation: Corporation[1..1];
    attribute venueName: String[0..1];
    attribute ticker: String[0..1];
    attribute micCode: String[0..1];
    attribute listedFrom: Date[0..1];
    attribute listedTo: Date[0..1];
  }
}
