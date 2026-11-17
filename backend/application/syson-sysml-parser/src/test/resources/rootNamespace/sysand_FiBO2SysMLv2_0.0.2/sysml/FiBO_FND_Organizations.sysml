// ===================================================================
// FIBO_FND_Organizations.sysml
// CI Aggregator (public re-export)
// ===================================================================
package FIBO_FND_Organizations {
  doc/* Configuration Item (CI) package for FIBO_FND_Organizations and its exported model elements. */
  public import Organizations::*;
  public import FormalOrganizations::*;
}

// ======================
// Organizations.sysml
// (updated: core types are parts)
// ======================
package Organizations {

  // ---- Imports (private) ----
  private import ScalarValues::*;     // String, Boolean, Integer
  private import Time::*;             // Date
  private import Addresses::*;        // Address
  private import Locations::*;        // Location

  // ---- Minimal local CMNS-aligned bases (wired later to shared FND/Commons packages) ----
  item def ContextualIdentifier {     // cmns-cxtid:ContextualIdentifier
    attribute identifierValue: String[0..1];
    attribute contextNote: String[0..1];
  }
  item def ContextualName {           // cmns-cxtdsg:ContextualName
    attribute nameValue: String[0..1];
    attribute contextNote: String[0..1];
  }

  // =========================================================
  // Core organization concepts (as parts)
  // =========================================================

  part def Organization {
    doc/* group of people and assets with a particular purpose and an internal governance structure */
    attribute organizationName: OrganizationName[0..*];
    attribute organizationIdentifier: OrganizationIdentifier[0..*];
    attribute description: String[0..1];
    attribute establishedDate: Date[0..1];
    attribute dissolutionDate: Date[0..1];
    attribute principalPlaceOfBusiness: Addresses::Address[0..1];
    attribute placeOfFormation: Locations::Location[0..1];
    // structural relations
    attribute parentOrganization: Organization[0..1];
    attribute subsidiary: Organization[0..*];
    attribute organizationalUnit: OrganizationalUnit[0..*];
  }

  part def OrganizationalUnit specializes Organization {
    doc/* division or sub-unit within an organization that performs a specific function or set of functions */
    attribute partOf: Organization[0..1];
  }

  // Names and identifiers (contextual, value-like)
  item def OrganizationName specializes ContextualName {
    doc/* contextual designation by which an organization is known */
  }

  item def OrganizationIdentifier specializes ContextualIdentifier {
    doc/* sequence of characters that uniquely identifies an organization within some context */
    attribute identifierValue: String[0..1];
    attribute issuingAuthority: String[0..1];
  }
}

// ======================
// FormalOrganizations.sysml
// (updated: all specializations are parts)
// ======================
package FormalOrganizations {

  // ---- Imports (private) ----
  private import ScalarValues::*;       // String, Boolean
  private import Time::*;               // Date
  private import Organizations::*;      // base Organization, names/ids
  private import Addresses::*;          // Address
  private import JurisdictionPackage::*;       // Jurisdiction (from Law CI)

  // ---- Minimal stubs (wire later as we add more FND packages) ----
  item def OrganizationRegistrationScheme { 
    doc/* system for allocating registration identifiers to organizations in some jurisdiction or domain */
    attribute schemeName: String[0..1];
    attribute schemeOperator: String[0..1];
  }

  // =========================================================
  // Core legal/org status (as parts)
  // =========================================================

  part def FormalOrganization specializes Organizations::Organization {
    doc/* organization that is recognized in some legal jurisdiction with rights and obligations under the law */
    attribute legalName: Organizations::OrganizationName[0..*];
    attribute tradingName: Organizations::OrganizationName[0..*];
    attribute registeredAddress: Addresses::Address[0..1];
    attribute registrationIdentifier: RegistrationIdentifier[0..*];
    attribute registrationScheme: OrganizationRegistrationScheme[0..1];
    attribute registrationDate: Date[0..1];
    attribute incorporationDate: Date[0..1];
    attribute isIncorporated: Boolean[0..1];
    attribute legalForm: String[0..1];
    attribute inJurisdiction: JurisdictionPackage::Jurisdiction[0..1];
  }

  part def RegisteredOrganization specializes FormalOrganization {
    doc/* organization that has been recorded in an official register maintained by a competent authority */
  }

  part def IncorporatedCompany specializes RegisteredOrganization {
    doc/* registered organization formed as a separate legal entity under company law */
  }

  part def Corporation specializes IncorporatedCompany {
    doc/* incorporated company treated as a single legal entity distinct from its owners */
  }

  part def LimitedLiabilityCompany specializes IncorporatedCompany {
    doc/* incorporated company in which the liability of members or subscribers is limited to what they have invested or guaranteed to the company */
  }

  part def Partnership specializes FormalOrganization {
    doc/* formal organization formed by two or more persons to carry on a business as co-owners */
    attribute hasGeneralPartner: Organizations::OrganizationIdentifier[0..*];  // placeholder until Parties/Roles are wired
    attribute hasLimitedPartner: Organizations::OrganizationIdentifier[0..*];  // placeholder
  }

  part def GeneralPartnership specializes Partnership {
    doc/* partnership in which partners share equally in both responsibility and liability */
  }

  part def LimitedPartnership specializes Partnership {
    doc/* partnership with at least one general partner and one or more limited partners whose liability is limited */
  }

  part def NonProfitOrganization specializes FormalOrganization {
    doc/* organization that uses surplus revenues to further a purpose rather than distributing profits to members or owners */
  }

  part def GovernmentOrganization specializes FormalOrganization {
    doc/* organization that is part of or controlled by a government and exercises public authority */
    attribute administeredBy: JurisdictionPackage::Jurisdiction[0..1];
  }

  part def InternationalOrganization specializes FormalOrganization {
    doc/* organization with activities or members in multiple countries */
  }

  // =========================================================
  // Registration and identification (value-like)
  // =========================================================

  item def RegistrationIdentifier specializes Organizations::OrganizationIdentifier {
    doc/* identifier assigned to an organization by a registration authority or scheme */
    attribute registrationAuthority: String[0..1];
    attribute registrationScheme: OrganizationRegistrationScheme[0..1];
  }

  // =========================================================
  // Structural relationships (containers, still items)
  // =========================================================

  item def CorporateGroup {
    doc/* set of companies consisting of a parent and its subsidiaries, treated as a single economic entity */
    attribute parent: FormalOrganization[1..1];
    attribute member: FormalOrganization[1..*];
  }

  part def HoldingCompany specializes FormalOrganization {
    doc/* company that owns enough voting stock in one or more other companies to control their policies and management */
    attribute holds: FormalOrganization[0..*];
  }

  part def Subsidiary specializes FormalOrganization {
    doc/* company controlled by another company, typically referred to as the parent or holding company */
    attribute parent: FormalOrganization[0..1];
  }
}
