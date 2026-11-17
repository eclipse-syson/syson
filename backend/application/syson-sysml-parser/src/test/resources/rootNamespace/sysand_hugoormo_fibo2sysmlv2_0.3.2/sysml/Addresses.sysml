// ===================================================================
// Addresses.sysml
// Source-parity extracted from: FiBO_FND_Places.sysml
// Primary source module: Addresses.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Foundations FND/Places
// ===================================================================

package Addresses {
  doc/* This ontology provides high level definitions for addresses and address components including elements that are common to addressing standards. (source: dct:abstract, Addresses.rdf) */
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

  item def <'address'> Address specializes Identifiers_and_Indices::Index {
    	doc /* index to a location to which communications may be delivered */
    	// skos:definition xml:lang="en" (Addresses.rdf)
    	// rdfs:label xml:lang="en" (Addresses.rdf)
    	// RDF: fibo-fnd-plc-adr:Address
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-id;Index"/>
    }
  item def <'address component'> AddressComponent specializes FiBO_FND_ExternalStubs::Aspect {
    	doc /* aspect of an address */
    	// skos:definition xml:lang="en" (Addresses.rdf)
    	// rdfs:label xml:lang="en" (Addresses.rdf)
    	// RDF: fibo-fnd-plc-adr:AddressComponent
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Aspect"/>
    }
  item def <'addressing scheme'> AddressingScheme specializes Identifiers_and_Indices::IndexingScheme {
    	doc /* system for allocating addresses to objects */
    	// skos:definition xml:lang="en" (Addresses.rdf)
    	// rdfs:label xml:lang="en" (Addresses.rdf)
    	// RDF: fibo-fnd-plc-adr:AddressingScheme
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-id;IndexingScheme"/>
    }
  item def ConventionalStreetAddress specializes Addresses::PhysicalAddress {
    	doc /* physical address that identifies a location on a street to which communications may be delivered */
    	// skos:definition xml:lang="en" (Addresses.rdf)
    // rdfs:label xml:lang="en" (Addresses.rdf)
    	// RDF: fibo-fnd-plc-adr:ConventionalStreetAddress
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-plc-adr;PhysicalAddress"/>
    }
  item def <'geographic directional symbol'> GeographicDirectionalSymbol specializes FiBO_FND_ExternalStubs::CodeElement {
    	doc /* code element that gives directional information for postal delivery */
    	// skos:definition xml:lang="en" (Addresses.rdf)
    	// rdfs:label xml:lang="en" (Addresses.rdf)
    	// RDF: fibo-fnd-plc-adr:GeographicDirectionalSymbol
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cds;CodeElement"/>
    }
  item def PhysicalAddress specializes Address { // RDF: fibo-fnd-plc-adr:PhysicalAddress
		doc /* physical address where communications can be addressed, papers served or representatives located for any kind of organization or person */
		// skos:definition xml:lang="en" (Addresses.rdf)
		// rdfs:label xml:lang="en" (Addresses.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-plc-adr;Address"/>
      // RDF: fibo-fnd-plc-adr:PhysicalAddress
    }
  item def <'physical address identifier'> PhysicalAddressIdentifier specializes FiBO_FND_ExternalStubs::CodeElement, FiBO_FND_ExternalStubs::Identifier {
    	doc /* unique identifier for a physical address */
    	// skos:definition xml:lang="en" (Addresses.rdf)
    	// rdfs:label xml:lang="en" (Addresses.rdf)
    	// RDF: fibo-fnd-plc-adr:PhysicalAddressIdentifier
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cds;CodeElement"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-id;Identifier"/>
    }
  item def <'physical addressing scheme'> PhysicalAddressingScheme specializes AddressingScheme {
    	doc /* scheme for specifying physical addresses according to a country specific standard */
    	// skos:definition xml:lang="en" (Addresses.rdf)
    	// rdfs:label xml:lang="en" (Addresses.rdf)
    	// RDF: fibo-fnd-plc-adr:PhysicalAddressingScheme
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-plc-adr;AddressingScheme"/>
    }
  item def <'post code area'> PostCodeArea specializes FiBO_FND_ExternalStubs::GeographicRegion {
    	doc /* physical area uniquely identified by some postal code */
    	// skos:definition xml:lang="en" (Addresses.rdf)
    	// rdfs:label xml:lang="en" (Addresses.rdf)
    	// RDF: fibo-fnd-plc-adr:PostCodeArea
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-loc;GeographicRegion"/>
    }
  item def <'post office box'> PostOfficeBox specializes Addresses::SupplementalAddressComponent {
    	doc /* post office box associated with an address */
    	// skos:definition xml:lang="en" (Addresses.rdf)
    	// rdfs:label xml:lang="en" (Addresses.rdf)
    	// RDF: fibo-fnd-plc-adr:PostOfficeBox
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-plc-adr;SupplementalAddressComponent"/>
    }
  item def <'post office box address'> PostOfficeBoxAddress specializes Addresses::PhysicalAddress {
    	doc /* physical address used to deliver communications to a designated container at a local post office or the local equivalent for the jurisdiction, where mail is held until the recipient collects it, rather than to a given house, building, or other location */
    	// skos:definition xml:lang="en" (Addresses.rdf)
    	// rdfs:label xml:lang="en" (Addresses.rdf)
    	// RDF: fibo-fnd-plc-adr:PostOfficeBoxAddress
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-plc-adr;PhysicalAddress"/>
    }
  item def PostOfficeBoxDesignator specializes SupplementalAddressDesignator {
    	doc /* designator used, together with an identifier, for a post office box */
    	// skos:definition xml:lang="en" (Addresses.rdf)
    // rdfs:label xml:lang="en" (Addresses.rdf)
    	// RDF: fibo-fnd-plc-adr:PostOfficeBoxDesignator
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-plc-adr;SupplementalAddressDesignator"/>
    }
  item def <'postcode'> Postcode specializes FiBO_FND_ExternalStubs::CodeElement, FiBO_FND_ExternalStubs::GeographicRegionIdentifier {
    	doc /* sequence of characters used to assist in the sorting of mail */
    	// skos:definition xml:lang="en" (Addresses.rdf)
    	// rdfs:label xml:lang="en" (Addresses.rdf)
    	// RDF: fibo-fnd-plc-adr:Postcode
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cds;CodeElement"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-loc;GeographicRegionIdentifier"/>
    }
  item def <'postdirectional symbol'> PostdirectionalSymbol specializes GeographicDirectionalSymbol {
    	doc /* geographic directional symbol that follows the street name and street suffix in a street address */
    	// skos:definition xml:lang="en" (Addresses.rdf)
    	// rdfs:label xml:lang="en" (Addresses.rdf)
    	// RDF: fibo-fnd-plc-adr:PostdirectionalSymbol
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-plc-adr;GeographicDirectionalSymbol"/>
    }
  item def <'predirectional symbol'> PredirectionalSymbol specializes GeographicDirectionalSymbol {
    	doc /* geographic directional symbol that occurs after the primary street number but before the street name in a street address */
    	// skos:definition xml:lang="en" (Addresses.rdf)
    	// rdfs:label xml:lang="en" (Addresses.rdf)
    	// RDF: fibo-fnd-plc-adr:PredirectionalSymbol
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-plc-adr;GeographicDirectionalSymbol"/>
    }
  item def <'primary address number'> PrimaryAddressNumber specializes AddressComponent {
    	doc /* address component that identifies a location with respect to a given street */
    	// skos:definition xml:lang="en" (Addresses.rdf)
    	// rdfs:label xml:lang="en" (Addresses.rdf)
    	// RDF: fibo-fnd-plc-adr:PrimaryAddressNumber
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-plc-adr;AddressComponent"/>
    }
  item def <'region-specific identifier'> RegionSpecificIdentifier specializes FiBO_FND_ExternalStubs::GeographicRegionIdentifier {
    	doc /* geographic region or subdivision identifier used internally by a country or other region */
    	// skos:definition xml:lang="en" (Addresses.rdf)
    	// rdfs:label xml:lang="en" (Addresses.rdf)
    	// RDF: fibo-fnd-plc-adr:RegionSpecificIdentifier
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-loc;GeographicRegionIdentifier"/>
    }
  item def <'secondary unit'> SecondaryUnit specializes AddressComponent {
    	doc /* address component that identifies an individual unit within a larger structure, such as an apartment, office, hangar, slip, mailbox, and so forth, at a given street address */
    	// skos:definition xml:lang="en" (Addresses.rdf)
    	// rdfs:label xml:lang="en" (Addresses.rdf)
    	// RDF: fibo-fnd-plc-adr:SecondaryUnit
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-plc-adr;AddressComponent"/>
    }
  item def <'secondary unit designator'> SecondaryUnitDesignator specializes FiBO_FND_ExternalStubs::Classifier {
    	doc /* classifier for a smaller structure or component within a larger facility, such as an apartment, office, mail stop, or other similar designation */
    	// skos:definition xml:lang="en" (Addresses.rdf)
    	// rdfs:label xml:lang="en" (Addresses.rdf)
    	// RDF: fibo-fnd-plc-adr:SecondaryUnitDesignator
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
    }
  item def <'secondary unit indicator'> SecondaryUnitIndicator specializes AddressComponent {
    	doc /* index to the specific unit within a secondary unit, such as a building or apartment, at a particular street address */
    	// skos:definition xml:lang="en" (Addresses.rdf)
    	// rdfs:label xml:lang="en" (Addresses.rdf)
    	// RDF: fibo-fnd-plc-adr:SecondaryUnitIndicator
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-plc-adr;AddressComponent"/>
    }
  item def StreetAddress specializes AddressComponent { // RDF: fibo-fnd-plc-adr:StreetAddress
		doc /* index to a location that consists of a primary address number, predirectional, street name, suffix, postdirectional, and an optional secondary unit */
		// skos:definition xml:lang="en" (Addresses.rdf)
		// rdfs:label xml:lang="en" (Addresses.rdf)
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-plc-adr;AddressComponent"/>
      // RDF: fibo-fnd-plc-adr:StreetAddress
    }
  item def <'street name'> StreetName specializes AddressComponent, FiBO_FND_ExternalStubs::Name {
    	doc /* identifier for a street in some context (e.g., city, municipality, geographic region) */
    	// skos:definition xml:lang="en" (Addresses.rdf)
    	// rdfs:label xml:lang="en" (Addresses.rdf)
    	// RDF: fibo-fnd-plc-adr:StreetName
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-dsg;Name"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-plc-adr;AddressComponent"/>
    }
  item def <'street suffix'> StreetSuffix specializes AddressComponent, FiBO_FND_ExternalStubs::Classifier {
    	doc /* classifier for a street or other delivery location, such as a dwelling located along a waterway */
    	// skos:definition xml:lang="en" (Addresses.rdf)
    	// rdfs:label xml:lang="en" (Addresses.rdf)
    	// RDF: fibo-fnd-plc-adr:StreetSuffix
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-plc-adr;AddressComponent"/>
    }
  item def <'structure name'> StructureName specializes AddressComponent, FiBO_FND_ExternalStubs::Name {
    	doc /* name for a building, house, office complex, shopping center, or other structure or group of structures */
    	// skos:definition xml:lang="en" (Addresses.rdf)
    	// rdfs:label xml:lang="en" (Addresses.rdf)
    	// RDF: fibo-fnd-plc-adr:StructureName
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-dsg;Name"/>
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-plc-adr;AddressComponent"/>
    }
  item def SupplementalAddressComponent specializes AddressComponent {
    	doc /* address component that provides additional information that is important to ensuring proper delivery of communications */
    	// skos:definition xml:lang="en" (Addresses.rdf)
    // rdfs:label xml:lang="en" (Addresses.rdf)
    	// RDF: fibo-fnd-plc-adr:SupplementalAddressComponent
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-plc-adr;AddressComponent"/>
    }
  item def <'supplemental address designator'> SupplementalAddressDesignator specializes FiBO_FND_ExternalStubs::Classifier {
    	doc /* classifier for supplemental address information, such as a highway contract route, rural route, building complex, shopping center, condominium complex, mail box, or other similar designation */
    	// skos:definition xml:lang="en" (Addresses.rdf)
    	// rdfs:label xml:lang="en" (Addresses.rdf)
    	// RDF: fibo-fnd-plc-adr:SupplementalAddressDesignator
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
    }
  item def <'supplemental address unit'> SupplementalAddressUnit specializes AddressComponent {
    	doc /* address component that includes a specific route, box, apartment, condominium or other indicator or unit associated with a specific address */
    	// skos:definition xml:lang="en" (Addresses.rdf)
    	// rdfs:label xml:lang="en" (Addresses.rdf)
    	// RDF: fibo-fnd-plc-adr:SupplementalAddressUnit
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-plc-adr;AddressComponent"/>
    }
  item def <'virtual address'> VirtualAddress specializes Address {
    	doc /* address identifying a virtual, i.e. non-physical, location */
    	// skos:definition xml:lang="en" (Addresses.rdf)
    	// rdfs:label xml:lang="en" (Addresses.rdf)
    	// RDF: fibo-fnd-plc-adr:VirtualAddress
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-plc-adr;Address"/>
    }
}
