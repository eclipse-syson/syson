// ==========================================
// FIBO_FND_AgentsAndPeople.sysml
// Configuration Item (CI) Aggregator
// ==========================================
package FIBO_FND_AgentsAndPeople {
  doc/* Configuration Item (CI) package for FIBO_FND_AgentsAndPeople and its exported model elements. */

  // Use public imports so downstream packages can access both subpackages directly
  public import Agents::*;
  public import People::*;
}
// ======================
// Agents.sysml (feature-based)
// (from Agents.rdf)
// ======================
package Agents {

  // ---- Imports (private) ----
  private import ScalarValues::*;    // String

  // ---- Elements ----

  item def Agent {
    doc/* entity that can act autonomously to achieve goals or perform tasks */
    // Textual names captured in the RDF via cmns-dsg:hasTextualName
    attribute textualName: String[0..*];
  }

  item def AutomatedSystem specializes Agent {
    doc/* system that reduces or eliminates the need for human involvement in order to complete a task */
  }

  item def SoftwareAgent specializes AutomatedSystem {
    doc/* software component that acts autonomously on behalf of a user or another program */
  }
}

// ======================
// People.sysml (feature-based, expanded from People.rdf)
// ======================
package People {

  // ---- Imports (private) ----
  private import ScalarValues::*;   // String, Integer, Real, Boolean
  private import Time::*;           // Date
  private import Addresses::*;      // Address
  private import Locations::*;      // Country, Location

  // ---- Minimal local CMNS-aligned bases (replace with shared CMNS packages when available) ----
  item def Identifier {                   // Commons Identifiers
    attribute identifierValue: String[0..1];
  }
  item def CodeElement {                  // Commons Codes
    attribute code: String[0..1];
    attribute codeSystem: String[0..1];
  }

  // =========================
  // Core person concepts
  // =========================

  item def Person {
    doc/* individual human being, with consciousness of self */
    // Datatype features
    attribute honorificPrefix: String[0..*];
    attribute honorificSuffix: String[0..*];

    // Reified birth/death info (OWL ranges are classes)
    attribute dateOfBirth: DateOfBirth[0..1];
    attribute dateOfDeath: DateOfDeath[0..1];
    attribute placeOfBirth: PlaceOfBirth[0..1];

    // Names and identifiers
    attribute personName: PersonName[1..*];
    attribute personIdentifier: PersonIdentifier[0..*];
    attribute nationalIdentificationNumber: NationalIdentificationNumber[0..*];
    attribute passportNumber: PassportNumber[0..*];
    attribute driversLicenseIdentifier: DriversLicenseIdentifier[0..*];
    attribute birthCertificateIdentifier: BirthCertificateIdentifier[0..*];
    attribute deathCertificateIdentifier: DeathCertificateIdentifier[0..*];

    // Residency and citizenship
    attribute citizenship: Locations::Country[0..*];
    attribute primaryResidence: Addresses::Address[0..1];
    attribute residence: Addresses::Address[0..*];

    // Contacts (liaison roles / points of contact)
    attribute contact: Contact[0..*];
  }

  item def NaturalPerson specializes Person { }

  // Lifecycle / legal status specializations
  item def Adult specializes NaturalPerson {
    doc/* person who has attained the age of majority as defined in some jurisdiction */
  }
  item def Minor specializes NaturalPerson {
    doc/* person under the age of majority in some jurisdiction */
  }
  item def EmancipatedMinor specializes Minor {
    doc/* person under the age of majority who is freed from control by parents or guardians */
  }
  item def IncapacitatedAdult specializes Adult {
    doc/* adult lacking legal capacity to act on their own behalf */
  }
  item def LegallyCapableAdult specializes Adult {
    doc/* adult who has the legal capacity to act on their own behalf */
  }
  item def LegalWorkingAgePerson specializes NaturalPerson {
    doc/* person who has attained the minimum age at which they may legally be employed in some jurisdiction */
  }

  // Age concepts
  item def AgeOfMajority {
    doc/* age at which someone acquires the rights and responsibilities of an adult in some jurisdiction */
    attribute minimumYears: Integer[0..1];
    attribute jurisdictionNote: String[0..1];
  }
  item def LegalAge {
    doc/* threshold used to confer specific legal rights and responsibilities in a jurisdiction */
    attribute description: String[0..1];
  }
  item def LegalWorkingAge {
    doc/* minimum age at which a person may legally be employed in some jurisdiction */
    attribute minimumYears: Integer[0..1];
  }

  // =========================
  // Names
  // =========================

  item def PersonName {
    doc/* designation by which someone is known in some context */
    // Datatype properties in People.rdf
    attribute fullLegalName: String[0..1];  // hasFullLegalName
    attribute firstName: String[0..*];      // hasFirstName / given name
    attribute middleName: String[0..*];     // hasMiddleName
    attribute familyName: String[0..1];     // hasFamilyName
    attribute surname: String[0..1];        // hasSurname (synonym, kept distinct per RDF)
    attribute nameSuffix: String[0..*];     // hasNameSuffix
  }

  // =========================
  // Birth / Death (reified per RDF)
  // =========================

  item def DateOfBirth {
    doc/* explicit date, i.e., the day, month and year, on which an individual was born */
    attribute date: Date;
  }
  item def DateOfDeath {
    doc/* explicit date, i.e., the day, month and year, on which an individual died */
    attribute date: Date;
  }
  item def PlaceOfBirth {
    doc/* place where an individual was born */
    attribute place: Locations::Location;
  }

  // =========================
  // Contacts
  // =========================

  item def Contact {
    doc/* role associated with a party serving as a designated point of communication, typically within a system or process */
    attribute isPointOfContactFor: PersonName[0..*];
  }

  item def ContactRecord {
    doc/* record about a party in a specific communicative or liaison role */
    attribute contact: Contact[1..1];
    attribute note: String[0..*];
  }

  // =========================
  // Identity documents (and schemes / identifiers)
  // =========================

  item def IdentityDocument {
    doc/* official or legal document used to verify someoneâ€™s identity */
    attribute documentNumber: String[0..1];
    attribute issuingAuthority: String[0..1];
    attribute issueDate: Date[0..1];
    attribute expirationDate: Date[0..1];
  }

  // Birth certificate
  item def BirthCertificate specializes IdentityDocument {
    doc/* an original document certifying the circumstances of the birth of a child, or a certified copy or representation of the ensuing registration of that birth */
  }
  item def BirthCertificateIdentificationScheme {
    doc/* system for allocating identifiers to birth certificates */
    attribute schemeName: String[0..1];
  }
  item def BirthCertificateIdentifier specializes CodeElement, Identifier {
    doc/* identifier associated with a vital record documenting the birth of a child */
    attribute tag: String;
    attribute scheme: BirthCertificateIdentificationScheme[0..1];
  }

  // Death certificate
  item def DeathCertificate specializes IdentityDocument {
    doc/* document issued by a competent authority registering the date, location and cause of a personâ€™s death */
  }
  item def DeathCertificateIdentificationScheme {
    doc/* system for allocating identifiers to death certificates */
    attribute schemeName: String[0..1];
  }
  item def DeathCertificateIdentifier specializes CodeElement, Identifier {
    doc/* identifier associated with a vital record documenting the death of an individual */
    attribute tag: String;
    attribute scheme: DeathCertificateIdentificationScheme[0..1];
  }

  // Passport
  item def Passport specializes IdentityDocument { }
  item def PassportNumberIdentificationScheme {
    doc/* system for allocating passport numbers */
    attribute schemeName: String[0..1];
  }
  item def PassportNumber specializes CodeElement, Identifier {
    doc/* identifier that uniquely identifies a passport */
    attribute tag: String;
    attribute scheme: PassportNumberIdentificationScheme[0..1];
  }

  // Driver's license
  item def DriversLicense specializes IdentityDocument { }
  item def DriversLicenseIdentificationScheme {
    doc/* system for allocating driverâ€™s license identifiers */
    attribute schemeName: String[0..1];
  }
  item def DriversLicenseIdentifier specializes CodeElement, Identifier {
    doc/* identifier associated with a driverâ€™s license */
    attribute tag: String;
    attribute scheme: DriversLicenseIdentificationScheme[0..1];
  }

  // National identification number
  item def NationalIdentificationNumberScheme {
    doc/* system for allocating national identification numbers issued by an authority in some country */
    attribute schemeName: String[0..1];
    attribute issuingCountry: Locations::Country[0..1];
  }
  item def NationalIdentificationNumber specializes CodeElement, Identifier {
    doc/* identifier assigned to a person by a national authority for identification purposes */
    attribute tag: String;
    attribute scheme: NationalIdentificationNumberScheme[0..1];
  }

  // Person-specific identifier wrapper (general)
  item def PersonIdentifier specializes CodeElement, Identifier {
    doc/* identifier used to distinguish and reference a specific person */
    attribute tag: String;
    attribute issuer: String[0..1];
  }
}




