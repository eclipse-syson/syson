// ===================================================================
// VirtualPlaces.sysml
// Source-parity extracted from: FiBO_FND_Places.sysml
// Primary source module: VirtualPlaces.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Foundations FND/Places
// ===================================================================

package Virtual_Places {
  doc/* This ontology provides scaffolding for use in describing virtual location-oriented concepts. (source: dct:abstract, VirtualPlaces.rdf) */
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

  item def <'electronic mail address'> ElectronicMailAddress specializes Addresses::VirtualAddress {
    	doc /* virtual address that defines an electronic messaging endpoint to which email messages can be delivered, typically via an Simple Mail Transfer Protocol (SMTP) based communications system */
    	// skos:definition xml:lang="en" (VirtualPlaces.rdf)
    	// rdfs:label xml:lang="en" (VirtualPlaces.rdf)
    	// RDF: fibo-fnd-plc-vrt:ElectronicMailAddress
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-plc-adr;VirtualAddress"/>
    }
  item def <'network location'> NetworkLocation specializes FiBO_FND_ExternalStubs::VirtualLocation {
    	doc /* a virtual location that may be identified by a network address (an identifier for a node or interface) */
    	// skos:definition xml:lang="en" (VirtualPlaces.rdf)
    	// rdfs:label xml:lang="en" (VirtualPlaces.rdf)
    	// RDF: fibo-fnd-plc-vrt:NetworkLocation
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-loc;VirtualLocation"/>
    }
  item def <'notional place'> NotionalPlace specializes FiBO_FND_ExternalStubs::Location {
    	doc /* an abstract place; one of many commonly understood concepts such as domestic, Eurozone etc. */
    	// skos:definition xml:lang="en" (VirtualPlaces.rdf)
    	// rdfs:label xml:lang="en" (VirtualPlaces.rdf)
    	// RDF: fibo-fnd-plc-vrt:NotionalPlace
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-loc;Location"/>
    }
  item def <'telephone number'> TelephoneNumber specializes Addresses::VirtualAddress {
    	doc /* virtual address that may be assigned to a fixed-line telephone subscriber station connected to a telephone line or to a wireless electronic telephony device, such as a radio telephone or a mobile telephone, or to other devices or services for data transmission via the public switched telephone network (PSTN) or other public and private networks */
    	// skos:definition xml:lang="en" (VirtualPlaces.rdf)
    	// rdfs:label xml:lang="en" (VirtualPlaces.rdf)
    	// RDF: fibo-fnd-plc-vrt:TelephoneNumber
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-plc-adr;VirtualAddress"/>
    }
}
