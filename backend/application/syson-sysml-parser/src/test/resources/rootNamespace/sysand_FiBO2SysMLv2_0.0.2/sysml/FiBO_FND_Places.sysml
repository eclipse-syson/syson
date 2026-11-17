// ===================================================================
// FIBO_FND_Places.sysml
// CI Aggregator (public re-export of Places subpackages)
// ===================================================================
package FIBO_FND_Places {
  doc/* Configuration Item (CI) package for FIBO_FND_Places and its exported model elements. */
  public import Addresses::*;
  public import Facilities::*;
  public import Locations::*;
  public import VirtualPlaces::*;
  public import RealPropertyPackage::*;
}

// ======================
// Addresses.sysml
// ======================
package Addresses {

  private import ScalarValues::*;   // String, Integer, Boolean, Real
  private import Time::*;           // Date
  private import Locations::*;      // Country, Region, City, Location

  // ===== Core address model =====

  item def Address {
    doc/* structured designation used to identify a place or location for the purposes of describing, routing, or delivering communications or goods */
    attribute addressLine: String[0..*];
    attribute attention: String[0..1];
    attribute careOf: String[0..1];
    attribute buildingNumber: String[0..1];
    attribute streetName: String[0..1];
    attribute unitOrApartment: String[0..1];
    attribute postOfficeBox: String[0..1];
    attribute postalCode: String[0..1];
    attribute locality: String[0..1];
    attribute regionName: String[0..1];
    attribute countryName: String[0..1];
    attribute isoCountryCode: String[0..1];
    attribute validFrom: Date[0..1];
    attribute validTo: Date[0..1];
  }

  item def PostalAddress specializes Address {
    doc/* address designed for postal delivery, typically including a postal code and locality */
    attribute city: Locations::City[0..1];
    attribute region: Locations::Region[0..1];
    attribute country: Locations::Country[0..1];
  }

  item def AddressIdentifier {
    doc/* sequence of characters that uniquely identifies an address in some registry or scheme */
    attribute identifierValue: String[1..1];
    attribute issuingAuthority: String[0..1];
    attribute schemeName: String[0..1];
  }

  item def AddressUsage {
    doc/* classification and timing of how an address is or was used */
    attribute usageKind: String[0..1];
    attribute startDate: Date[0..1];
    attribute endDate: Date[0..1];
  }

  item def AddressAssignment {
    doc/* assignment of an address to some place or party for a period */
    attribute address: Address[1..1];
    attribute forPlace: Locations::Location[0..1];
    attribute usage: AddressUsage[0..*];
    attribute identifiers: AddressIdentifier[0..*];
  }
}

// ======================
// Locations.sysml
// ======================
package Locations {

  private import ScalarValues::*;   // String, Real
  private import Time::*;           // Date

  item def TimeZone {
    attribute tzId: String[0..1];
    attribute utcOffset: String[0..1];
  }

  // ===== Core location model =====

  part def Location {
    doc/* identifiable position or area in physical space, optionally with geopolitical context */
    attribute name: String[0..1];
    attribute description: String[0..1];
  }

  part def GeographicLocation specializes Location {
    doc/* location identified by geographic coordinates in an agreed reference system */
    attribute latitude: Real[0..1];
    attribute longitude: Real[0..1];
    attribute elevationMeters: Real[0..1];
    attribute crs: CoordinateReferenceSystem[0..1];
    attribute timeZone: TimeZone[0..1];
  }

  item def CoordinateReferenceSystem {
    doc/* coordinate reference system defining how coordinates map to the Earth (e.g., WGS 84) */
    attribute name: String[0..1];
    attribute epsgCode: String[0..1];
  }

  part def GeopoliticalEntity specializes Location {
    doc/* bounded area or entity defined by political or administrative authority */
    attribute code: String[0..1];
  }

  part def Country specializes GeopoliticalEntity { }
  part def Region  specializes GeopoliticalEntity { }
  part def City    specializes GeopoliticalEntity { }

  item def Boundary {
    doc/* line or polygon that encloses or delimits a geographic area */
    attribute wkt: String[0..1];
    attribute crs: CoordinateReferenceSystem[0..1];
  }

  part def AreaOfInterest specializes Location {
    doc/* geographic area defined for a specific purpose (e.g., trading region, compliance zone) */
    attribute boundary: Boundary[0..1];
  }
}

// ======================
// Facilities.sysml
// ======================
package Facilities {

  private import ScalarValues::*;         
  private import Time::*;                 
  private import Addresses::*;            
  private import Locations::*; 
  private import Organizations::*;                        

  // ===== Core facility model =====

  part def Facility {
    doc/* fixed physical installation or infrastructure with a specific purpose or function */
    attribute facilityName: String[0..1];
    attribute description: String[0..1];
    attribute locatedAt: GeographicLocation[0..1];
    attribute address: Addresses::Address[0..1];
    attribute ownerOrganization: Organization[0..1];
    attribute commissioningDate: Date[0..1];
    attribute decommissioningDate: Date[0..1];
  }

  part def Site specializes Facility {
    doc/* land area prepared or used for industrial, commercial, or operational activities (may contain multiple buildings) */
    attribute areaSquareMeters: Real[0..1];
  }

  part def Building specializes Facility {
    doc/* roofed construction for permanent or temporary use */
    attribute numberOfFloors: Integer[0..1];
    attribute grossFloorAreaSqM: Real[0..1];
  }

  part def Floor specializes Facility {
    doc/* level within a building */
    attribute levelNumber: Integer[0..1];
    attribute inBuilding: Building[0..1];
  }

  part def Room specializes Facility {
    doc/* enclosed space within a floor or building */
    attribute roomNumber: String[0..1];
    attribute onFloor: Floor[0..1];
  }
}

// ======================
// VirtualPlaces.sysml
// ======================
package VirtualPlaces {

  private import ScalarValues::*;       
  private import Time::*;               

  // ===== Core virtual place model =====

  item def VirtualPlace {
    doc/* identifiable location in a virtual or networked environment used for interaction or addressing */
    attribute name: String[0..1];
    attribute description: String[0..1];
  }

  item def NetworkAddress specializes VirtualPlace {
    doc/* address used for routing communications in a network */
    attribute addressValue: String[1..1];
    attribute scheme: String[0..1];
  }

  item def EmailAddress specializes NetworkAddress {
    doc/* electronic mail address */
  }

  item def WebAddress specializes NetworkAddress {
    doc/* world wide web address (URL/URI) */
  }

  item def DomainName specializes VirtualPlace {
    doc/* hierarchical human-readable name used to identify resources on the internet */
    attribute fqdn: String[1..1];
    attribute punycode: String[0..1];
    attribute tld: String[0..1];
  }
}

// ======================
// RealProperty.sysml
// ======================
package RealPropertyPackage {

  private import ScalarValues::*;        
  private import Time::*;                
  private import Addresses::*;           
  private import Locations::*;
  private import Parties::*;  
  private import Organizations::*; 
  private import FormalOrganizations::*;
  private import JurisdictionPackage::*; 
  private import LegalCorePackage::*;                   

  enum def TenureKind {
    doc/* classification of the legal or customary basis by which land or property is held */
    enum Freehold;
    enum Leasehold;
    enum Commonhold;
    enum Usufruct;
    enum Easement;
    enum Other;
  }

  // ===== Core property model (as parts) =====

  part def RealProperty {
    doc/* land and anything permanently attached to it (immovable property) considered as an object of rights and obligations */
    attribute propertyIdentifier: PropertyIdentifier[0..*];
    attribute propertyAddress: Addresses::PostalAddress[0..*];
    attribute description: String[0..1];
    attribute locatedAt: Locations::GeographicLocation[0..1];
    attribute boundary: Locations::Boundary[0..1];
    attribute inJurisdiction: Jurisdiction[0..1];
  }

  part def Parcel specializes RealProperty {
    doc/* legally defined plot or tract of land delineated by a boundary */
    attribute parcelNumber: String[0..1];
    attribute areaSquareMeters: Real[0..1];
  }

  part def BuildingProperty specializes RealProperty {
    doc/* property consisting predominantly of a building and the land it occupies */
    attribute stories: Integer[0..1];
    attribute grossFloorAreaSqM: Real[0..1];
  }

  part def PropertyUnit specializes RealProperty {
    doc/* independently transferable or leasable unit within a larger property (e.g., apartment, condo unit, office suite) */
    attribute unitNumber: String[0..1];
    attribute inBuilding: BuildingProperty[0..1];
  }

  item def PropertyIdentifier {
    doc/* sequence of characters uniquely identifying a property under a registry or scheme */
    attribute identifierValue: String[1..1];
    attribute registeringAuthority: String[0..1];
    attribute schemeName: String[0..1];
  }

  item def TitleDocument {
    doc/* legal instrument evidencing ownership or rights in real property (e.g., deed, title) */
    attribute titleNumber: String[0..1];
    attribute issueDate: Date[0..1];
    attribute inJurisdiction: Jurisdiction[0..1];
    attribute relatesTo: RealProperty[0..*];
  }

  item def PropertyRight {
    doc/* legal right associated with a real property (e.g., ownership, easement, leasehold) */
    attribute kind: TenureKind[0..1];
    attribute holderParty: Party[0..1];
    attribute holderOrganization: FormalOrganization[0..1];
    attribute startDate: Date[0..1];
    attribute endDate: Date[0..1];
    attribute basisLaw: Law[0..1];
  }

  item def ValuationRecord {
    doc/* record of an assessment of the market or taxable value of a real property as of a date */
    attribute property: RealProperty[1..1];
    attribute valuationAmount: Real[1..1];
    attribute currencyCode: String[0..1];
    attribute valuationDate: Date[0..1];
    attribute appraiserName: String[0..1];
  }

  item def OccupancyRecord {
    doc/* record describing the occupancy status or use of a real property over time */
    attribute property: RealProperty[1..1];
    attribute occupiedByParty: Party[0..1];
    attribute occupiedByOrganization: FormalOrganization[0..1];
    attribute useDescription: String[0..1];
    attribute startDate: Date[0..1];
    attribute endDate: Date[0..1];
  }
}
