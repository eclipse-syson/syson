// ===================================================================
// USPostalServiceAddresses.sysml
// Source-parity extracted from: FiBO_FND_Places.sysml
// Primary source module: USPostalServiceAddresses.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Foundations FND/Places/NorthAmerica
// ===================================================================

package US_Postal_Service_Addresses {
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

  item def <'complete address'> CompleteAddress specializes StandardizedAddress {
    	doc /* delivery address that has all the address elements necessary to allow an exact match with the current Postal Service ZIP+4 and City State files to obtain the finest level of ZIP+4 and delivery point codes for the delivery address */
    	// skos:definition xml:lang="en" (USPostalServiceAddresses.rdf)
    	// rdfs:label xml:lang="en" (USPostalServiceAddresses.rdf)
    	// RDF: fibo-fnd-plc-uspsa:CompleteAddress
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-plc-uspsa;StandardizedAddress"/>
    }
  item def <'delivery address code set'> DeliveryAddressCodeSet specializes FiBO_FND_CodeSets_And_Schemes::CodeSet, FiBO_FND_CodeSets_And_Schemes::IdentificationScheme {
    	doc /* system of numeric codes that substitute for specified delivery point details according to the U.S. Postal Service Publication 28 */
    	// skos:definition xml:lang="en" (USPostalServiceAddresses.rdf)
    	// rdfs:label xml:lang="en" (USPostalServiceAddresses.rdf)
    	// RDF: fibo-fnd-plc-uspsa:DeliveryAddressCodeSet
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cds;CodeSet"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-id;IdentificationScheme"/>
    }
  item def <'delivery point code'> DeliveryPointCode specializes FiBO_FND_ExternalStubs::CodeElement {
    	doc /* specific set of digits between 00 and 99 assigned to a delivery point */
    	// skos:definition xml:lang="en" (USPostalServiceAddresses.rdf)
    	// rdfs:label xml:lang="en" (USPostalServiceAddresses.rdf)
    	// RDF: fibo-fnd-plc-uspsa:DeliveryPointCode
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cds;CodeElement"/>
    }
  item def <'delivery point code set'> DeliveryPointCodeSet specializes FiBO_FND_CodeSets_And_Schemes::CodeSet {
    	doc /* system of numeric codes that substitute for specified delivery point details according to the U.S. Postal Service Publication 28 */
    	// skos:definition xml:lang="en" (USPostalServiceAddresses.rdf)
    	// rdfs:label xml:lang="en" (USPostalServiceAddresses.rdf)
    	// RDF: fibo-fnd-plc-uspsa:DeliveryPointCodeSet
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cds;CodeSet"/>
    }
  item def <'Department of State address'> DepartmentOfStateAddress specializes Addresses::PhysicalAddress {
    	doc /* delivery address whose delivery address line uses 'UNIT' followed by the unit identifier, followed by 'BOX' followed by box number, in place of a street address, 'DPO' as the literal value for the city, and the appropriate armed forces subdivision code in place of a subdivision (state) code */
    	// skos:definition xml:lang="en" (USPostalServiceAddresses.rdf)
    	// rdfs:label xml:lang="en" (USPostalServiceAddresses.rdf)
    	// RDF: fibo-fnd-plc-uspsa:DepartmentOfStateAddress
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-plc-adr;PhysicalAddress"/>
    }
  item def <'Department of State unit component'> DepartmentOfStateUnitComponent specializes Addresses::SupplementalAddressComponent {
    	doc /* component of a Department of State address that includes 'UNIT' followed by the unit identifier */
    	// skos:definition xml:lang="en" (USPostalServiceAddresses.rdf)
    	// rdfs:label xml:lang="en" (USPostalServiceAddresses.rdf)
    	// RDF: fibo-fnd-plc-uspsa:DepartmentOfStateUnitComponent
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-plc-adr;SupplementalAddressComponent"/>
    }
  item def <'general delivery address'> GeneralDeliveryAddress specializes Addresses::PhysicalAddress {
    	doc /* delivery address that uses the words 'GENERAL DELIVERY', uppercase preferred, spelled out (no abbreviation), in place of a street address */
    	// skos:definition xml:lang="en" (USPostalServiceAddresses.rdf)
    	// rdfs:label xml:lang="en" (USPostalServiceAddresses.rdf)
    	// RDF: fibo-fnd-plc-uspsa:GeneralDeliveryAddress
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-plc-adr;PhysicalAddress"/>
    }
  item def <'highway contract route'> HighwayContractRoute specializes Addresses::SupplementalAddressComponent {
    	doc /* highway contract route associated with an address */
    	// skos:definition xml:lang="en" (USPostalServiceAddresses.rdf)
    	// rdfs:label xml:lang="en" (USPostalServiceAddresses.rdf)
    	// RDF: fibo-fnd-plc-uspsa:HighwayContractRoute
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-plc-adr;SupplementalAddressComponent"/>
    }
  item def <'highway contract address'> HighwayContractRouteAddress specializes Addresses::PhysicalAddress {
    	doc /* delivery address whose delivery address line uses the abbreviation 'HC', followed by the route identifier, followed by 'BOX' followed by box number, in place of a street address */
    	// skos:definition xml:lang="en" (USPostalServiceAddresses.rdf)
    	// rdfs:label xml:lang="en" (USPostalServiceAddresses.rdf)
    	// RDF: fibo-fnd-plc-uspsa:HighwayContractRouteAddress
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-plc-adr;PhysicalAddress"/>
    }
  item def <'international address'> InternationalAddress specializes Addresses::PhysicalAddress {
    	doc /* physical address that explicitly includes a country */
    	// skos:definition xml:lang="en" (USPostalServiceAddresses.rdf)
    	// rdfs:label xml:lang="en" (USPostalServiceAddresses.rdf)
    	// RDF: fibo-fnd-plc-uspsa:InternationalAddress
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-plc-adr;PhysicalAddress"/>
    }
  item def <'mailbox'> Mailbox specializes Addresses::SupplementalAddressComponent {
    	doc /* mailbox, other than a U.S. Post Office box, associated with an address */
    	// skos:definition xml:lang="en" (USPostalServiceAddresses.rdf)
    	// rdfs:label xml:lang="en" (USPostalServiceAddresses.rdf)
    	// RDF: fibo-fnd-plc-uspsa:Mailbox
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-plc-adr;SupplementalAddressComponent"/>
    }
  item def <'overseas military address'> OverseasMilitaryAddress specializes Addresses::PhysicalAddress {
    	doc /* delivery address whose delivery address line uses an abbreviation for the unit or command such as 'CMR', 'PSC', or 'UNIT', or 'HC', followed by the unit identifier, followed by 'BOX' followed by box number, in place of a street address, either 'APO' or 'FPO' as the literal value for the city and the appropriate armed forces subdivision code in place of a subdivision (state) code */
    	// skos:definition xml:lang="en" (USPostalServiceAddresses.rdf)
    	// rdfs:label xml:lang="en" (USPostalServiceAddresses.rdf)
    	// RDF: fibo-fnd-plc-uspsa:OverseasMilitaryAddress
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-plc-adr;PhysicalAddress"/>
    }
  item def <'private mail box address'> PrivateMailBoxAddress specializes Addresses::ConventionalStreetAddress {
    	doc /* delivery address provided by a commercial mail receiving company that includes a supplementary address line containing the abbreviation 'PMB' or the pound "#" symbol followed by the mailbox number; alternatively, 'PMB' or '#" and the mailbox number can be appended to the street address */
    	// skos:definition xml:lang="en" (USPostalServiceAddresses.rdf)
    	// rdfs:label xml:lang="en" (USPostalServiceAddresses.rdf)
    	// RDF: fibo-fnd-plc-uspsa:PrivateMailBoxAddress
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-plc-adr;ConventionalStreetAddress"/>
    }
  item def <'Puerto Rico address'> PuertoRicoAddress specializes Addresses::ConventionalStreetAddress {
    	doc /* delivery address for a delivery point in Puerto Rico that may include a supplementary address line containing the abbreviation 'URB' followed by the name of the urbanization area that is appropriate for that address */
    	// skos:definition xml:lang="en" (USPostalServiceAddresses.rdf)
    	// rdfs:label xml:lang="en" (USPostalServiceAddresses.rdf)
    	// RDF: fibo-fnd-plc-uspsa:PuertoRicoAddress
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-plc-adr;ConventionalStreetAddress"/>
    }
  item def <'highway contract route address'> RuralRoute specializes Addresses::SupplementalAddressComponent {
    	doc /* mail route outside the city or township limits in a rural area associated with an address */
    	// skos:definition xml:lang="en" (USPostalServiceAddresses.rdf)
    	// rdfs:label xml:lang="en" (USPostalServiceAddresses.rdf)
    	// RDF: fibo-fnd-plc-uspsa:RuralRoute
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-plc-adr;SupplementalAddressComponent"/>
    }
  item def <'rural route address'> RuralRouteAddress specializes Addresses::PhysicalAddress {
    	doc /* delivery address whose delivery address line uses the abbreviation 'RR', followed by the route identifier, followed by 'BOX' followed by box number, in place of a street address */
    	// skos:definition xml:lang="en" (USPostalServiceAddresses.rdf)
    	// rdfs:label xml:lang="en" (USPostalServiceAddresses.rdf)
    	// RDF: fibo-fnd-plc-uspsa:RuralRouteAddress
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-plc-adr;PhysicalAddress"/>
    }
  item def <'standardized address'> StandardizedAddress specializes Addresses::PhysicalAddress {
    	doc /* delivery address that is fully spelled out, abbreviated by using the Postal Service standard abbreviations or as given in the current Postal Service ZIP+4 file */
    	// skos:definition xml:lang="en" (USPostalServiceAddresses.rdf)
    	// rdfs:label xml:lang="en" (USPostalServiceAddresses.rdf)
    	// RDF: fibo-fnd-plc-uspsa:StandardizedAddress
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-plc-adr;PhysicalAddress"/>
    }
  item def <'U.S. Postal Service address identifier'> USPostalServiceAddressIdentifier specializes Addresses::PhysicalAddressIdentifier {
    	doc /* combined with the ZIP + 4 code, the delivery point code provides a unique identifier for every deliverable address served by the USPS */
    	// skos:definition xml:lang="en" (USPostalServiceAddresses.rdf)
    	// rdfs:label xml:lang="en" (USPostalServiceAddresses.rdf)
    	// RDF: fibo-fnd-plc-uspsa:USPostalServiceAddressIdentifier
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-plc-adr;PhysicalAddressIdentifier"/>
    }
  item def <'urbanization'> Urbanization specializes FiBO_FND_ExternalStubs::CountrySubdivision {
    	doc /* an area, sector, or development within a larger geographic area */
    	// skos:definition xml:lang="en" (USPostalServiceAddresses.rdf)
    	// rdfs:label xml:lang="en" (USPostalServiceAddresses.rdf)
    	// RDF: fibo-fnd-plc-uspsa:Urbanization
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-loc;CountrySubdivision"/>
    }
  item def <'Zip Code'> ZIPCode specializes Addresses::Postcode {
    	doc /* five-digit code code assigned to a delivery address indicating the state and post office or postal zone */
    	// skos:definition xml:lang="en" (USPostalServiceAddresses.rdf)
    	// rdfs:label xml:lang="en" (USPostalServiceAddresses.rdf)
    	// RDF: fibo-fnd-plc-uspsa:ZIPCode
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-plc-adr;Postcode"/>
    }
  item def <'ZIP+4 Code'> ZIPPlus4Code specializes Addresses::Postcode {
    	doc /* nine-digit number consisting of five digits, a hyphen, and four digits, which the USPS describes by its trademark ZIP+4 */
    	// skos:definition xml:lang="en" (USPostalServiceAddresses.rdf)
    	// rdfs:label xml:lang="en" (USPostalServiceAddresses.rdf)
    	// RDF: fibo-fnd-plc-uspsa:ZIPPlus4Code
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-plc-adr;Postcode"/>
    }
  item def <'zip code scheme'> ZipCodeScheme specializes FiBO_FND_CodeSets_And_Schemes::CodeSet, FiBO_FND_CodeSets_And_Schemes::IdentificationScheme {
    	doc /* system used in the U.S. to facilitate the delivery of mail, consisting of a five- or nine-digit code Zone Improvement Plan (ZIP) printed directly after the address, the first five digits (initial code) indicating the state and post office or postal zone, the last four (expanded code) the box section or number, portion of a rural route, building, or other specific delivery location */
    	// skos:definition xml:lang="en" (USPostalServiceAddresses.rdf)
    	// rdfs:label xml:lang="en" (USPostalServiceAddresses.rdf)
    	// RDF: fibo-fnd-plc-uspsa:ZipCodeScheme
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cds;CodeSet"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-id;IdentificationScheme"/>
    }
}
