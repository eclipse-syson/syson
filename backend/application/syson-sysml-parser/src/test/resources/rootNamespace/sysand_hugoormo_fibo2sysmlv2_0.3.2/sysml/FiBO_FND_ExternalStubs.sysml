package FiBO_FND_ExternalStubs {
	// Primary source module(s): CMNS external type stubs (for current translated content)
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

 doc /*
	 * FiBO Foundations - External CMNS Type Stubs
	 *
	 * INTERIM STUB FILE - DO NOT USE IN PRODUCTION
	 *
	 * This package provides placeholder item definitions for all external types
	 * sourced from the CMNS (Common Model for Network Standards) ontology layer
	 * used as FIBO's metamodel. These stubs ensure syntactic completeness of
	 * the SysML model while the proper SysML Library element bindings are pending.
	 *
	 * Each stub will be replaced by a proper specializes reference to a SysML Library
	 * element (e.g., ScalarValues, Base, ISQ, etc.) in a future iteration.
	 *
	 * OWL namespace prefixes covered:
	 *   cmns-cls  - Classifications
	 *   cmns-cds  - Codes and Code Sets
	 *   cmns-col  - Collections
	 *   cmns-dsg  - Designations
	 *   cmns-id   - Identifiers
	 *   cmns-ra   - Registration Authority
	 *   cmns-pts  - Parties, Roles, Situations
	 *   cmns-doc  - Documents
	 *   cmns-ge   - Geopolitical Entities
	 *   cmns-qtu  - Quantities and Units
	 *   cmns-dt   - Dates and Times
	 */
	// -- cmns-cls : Classifications ------------------------------------

	/** cmns-cls:Classifier - root classifier type */
	// RDF: cmns-cls:Classifier
	item def Classifier {}

	/** cmns-cls:ClassificationScheme - scheme for classifying things */
	// RDF: cmns-cls:ClassificationScheme
	item def ClassificationScheme specializes Classifier {}

	// -- cmns-cds : Code Sets ------------------------------------------

	/** cmns-cds:CodeElement - individual code value within a code set */
	// RDF: cmns-cds:CodeElement
	item def CodeElement {}

	/** cmns-cds:CodeSet - a set of code elements */
	// RDF: cmns-cds:CodeSet
	item def CodeSet {}

	// -- cmns-col : Collections ----------------------------------------

	/** cmns-col:Collection - unordered group of elements */
	// RDF: cmns-col:Collection
	item def Collection {}

	/** cmns-col:StructuredCollection - collection with imposed structure */
	// RDF: cmns-col:StructuredCollection
	item def StructuredCollection specializes Collection {}

	/** cmns-col:Constituent - element that is part of a collection */
	// RDF: cmns-col:Constituent
	item def Constituent {}

	/** cmns-col:Record - structured information record */
	// RDF: cmns-col:Record
	item def Record {}

	// -- cmns-dsg : Designations ---------------------------------------

	/** cmns-dsg:Name - a designation that is a name */
	// RDF: cmns-dsg:Name
	item def Name {}

	/** cmns-dsg:Reference - a designation that refers to something */
	// RDF: cmns-dsg:Reference
	item def Reference {}

	// -- cmns-id : Identifiers -----------------------------------------

	/** cmns-id:Identifier - a designation used to uniquely identify something */
	// RDF: cmns-id:Identifier
	item def Identifier {}

	/** cmns-id:IdentificationScheme - scheme for assigning identifiers */
	// RDF: cmns-id:IdentificationScheme
	item def IdentificationScheme {}

	/** cmns-id:ContextualIdentifier - identifier valid within a given context */
	// RDF: cmns-id:ContextualIdentifier
	item def ContextualIdentifier specializes Identifier {}

	// -- cmns-ra : Registration Authority -------------------------------

	/** cmns-ra:RegistryEntry - an entry in a registry */
	// RDF: cmns-ra:RegistryEntry
	item def RegistryEntry {}

	/** cmns-ra:Registry - a register of entries */
	// RDF: cmns-ra:Registry
	item def Registry {}

	/** cmns-ra:RegisteredIdentifier - identifier assigned by a registration authority */
	// RDF: cmns-ra:RegisteredIdentifier
	item def RegisteredIdentifier specializes Identifier {}

	/** cmns-ra:RegistrationScheme - scheme governing registration */
	// RDF: cmns-ra:RegistrationScheme
	item def RegistrationScheme {}

	// -- cmns-pts : Parties, Roles, Situations -------------------------

	/** cmns-pts:Situation - a set of circumstances */
	// RDF: cmns-pts:Situation
	item def Situation {}

	/** cmns-pts:Aspect - a facet or dimension of a situation */
	// RDF: cmns-pts:Aspect
	item def Aspect {}

	/** cmns-pts:Undergoer - a party that undergoes something in a situation */
	// RDF: cmns-pts:Undergoer
	item def Undergoer {}

	/** cmns-pts:Actor - a party that acts in a situation */
	// RDF: cmns:Actor
	item def Actor {}

	/** cmns-pts:Role - a role played by a party */
	// RDF: cmns:Role
	item def Role {}

	/** cmns-pts:AgentRole - a role in which an agent acts for another party */
	// RDF: cmns:AgentRole
	item def AgentRole specializes Role {}

	/** cmns-pts:FunctionalRole - a role associated with a function */
	// RDF: cmns:FunctionalRole
	item def FunctionalRole specializes Role {}

	/** cmns-pts:FiOccurrence - a financial FiBOOccurrence or event */
	// RDF: cmns:FiOccurrence
	item def FiOccurrence {}

	/** cmns-pts:Context - a set of circumstances providing background */
	// RDF: cmns:Context
	item def Context {}

	/** cmns-pts:Party - a party that can play roles */
	// RDF: cmns-pts:Party
	item def Party {}

	/** cmns-pts:OrganizationMember - membership role in an organization */
	// RDF: fibo-fnd-org:OrganizationMember
	item def OrganizationMember specializes Role {}

	// -- cmns-doc : Documents and Notices -------------------------------

	/** cmns-doc:Document - a document */
	// RDF: cmns:Document
	item def Document {}

	/** cmns-doc:LegalDocument - a document having legal standing */
	// RDF: cmns:LegalDocument
	item def LegalDocument specializes Document {}

	/** cmns-doc:Notice - a formal notice document */
	// RDF: cmns:Notice
	item def Notice specializes Document {}

	/** cmns-doc:Certificate - a document certifying something */
	// RDF: cmns:Certificate
	item def Certificate specializes Document {}

	/** cmns-doc:Specification - a document specifying requirements */
	// RDF: cmns:Specification
	item def Specification specializes Document {}

	// -- cmns-ge : Geopolitical Entities -------------------------------

	/** cmns-ge:GeopoliticalEntity - a geopolitically defined entity */
	// RDF: cmns:GeopoliticalEntity
	item def GeopoliticalEntity {}

	/** cmns-ge:GeographicRegion - a geographic region */
	// RDF: cmns:GeographicRegion
	item def GeographicRegion {}

	/** cmns-ge:CountrySubdivision - first-level or other administrative subdivision of a country */
	// RDF: cmns:CountrySubdivision
	item def CountrySubdivision specializes GeographicRegion {}

	/** cmns-loc:Country - a sovereign country */
	// RDF: cmns-loc:Country
	item def Country specializes GeopoliticalEntity {}

	/** cmns-loc:Municipality - a local administrative municipality */
	// RDF: cmns-loc:Municipality
	item def Municipality specializes GeographicRegion {}

	/** cmns-ge:GeographicRegionIdentifier - an identifier for a geographic region */
	// RDF: cmns-id:GeographicRegionIdentifier
	item def GeographicRegionIdentifier specializes Identifier {}

	/** cmns-ge:Location - a location */
	// RDF: cmns:Location
	item def Location {}

	/** cmns-ge:PhysicalLocation - a physical location */
	// RDF: cmns:PhysicalLocation
	item def PhysicalLocation specializes Location {}

	/** cmns-ge:VirtualLocation - a virtual or online location */
	// RDF: cmns:VirtualLocation
	item def VirtualLocation specializes Location {}

	/** cmns-ge:Jurisdiction - a jurisdiction */
	// RDF: cmns:Jurisdiction
	item def Jurisdiction {}

	// -- cmns-org : Organizations --------------------------------------

	 item def Organization {
        doc /* */
        // RDF: cmns-org:Organization
    }

	/** cmns-org:LegalEntity - organization recognized as a legal person */
	// RDF: cmns-org:LegalEntity
	item def LegalEntity specializes Organization {}

	/** cmns-org:LegalPerson - legal person abstraction */
	// RDF: cmns-org:LegalPerson
	item def LegalPerson specializes LegalEntity {}

	/** cmns-org:Service - service offered or performed by an organization */
	// RDF: cmns-org:Service
	item def Service {}

	/** cmns-ra:Registrar - party that performs registration */
	// RDF: cmns-ra:Registrar
	item def Registrar specializes Organization {}

	/** cmns-ra:RegistrationAuthority - authority responsible for registration */
	// RDF: cmns-ra:RegistrationAuthority
	item def RegistrationAuthority specializes Organization {}
	
	/** cmns-org:OrganizationalSubUnit - a sub-unit of an organization */
	// RDF: fibo-fnd-org:OrganizationalSubUnit
	item def OrganizationalSubUnit {}

	/** cmns-org:MemberBearingOrganization - an organization that has members */
	// RDF: fibo-fnd-org:MemberBearingOrganization
	item def MemberBearingOrganization {}

	/** cmns-org:Membership - a membership in an organization */
	// RDF: cmns:Membership
	item def Membership {}

	/** cmns-org:OrganizationIdentificationScheme - scheme for identifying organizations */
	// RDF: fibo-fnd-org:OrganizationIdentificationScheme
	item def OrganizationIdentificationScheme specializes IdentificationScheme {}

	// -- cmns-qtu : Quantities and Units -------------------------------

	/** cmns-qtu:Measure - a measured quantity */
	// RDF: cmns:Measure
	item def Measure {}

	/** cmns-qtu:Percentage - a measure expressed as a percentage */
	// RDF: cmns:Percentage
	item def Percentage specializes Measure {}

	/** cmns-qtu:ScalarQuantityValue - a scalar quantity value */
	// RDF: cmns:ScalarQuantityValue
	item def ScalarQuantityValue specializes Measure {}

	/** cmns-qtu:QuantityKind - a kind of quantity */
	// RDF: cmns:QuantityKind
	item def QuantityKind {}

	/** cmns-qtu:MeasurementUnit - a unit of measurement */
	// RDF: cmns:MeasurementUnit
	item def MeasurementUnit {}

	/** cmns-qtu:Variable - a variable quantity */
	// RDF: cmns:Variable
	item def Variable {}

	/** scalar:String - placeholder scalar string until library binding is established */
	// RDF: cmns:Text
	item def String {}

	/** cmns-txt:Text - textual value placeholder */
	// RDF: cmns-txt:Text
	item def Text specializes String {}

	// -- cmns-dt : Dates and Times -------------------------------------

	/** cmns-dt:Date - a calendar date */
	// RDF: cmns:Date
	item def Date {}

	/** cmns-dt:ExplicitDate - explicit calendar date */
	// RDF: cmns-dt:ExplicitDate
	item def ExplicitDate specializes Date {}

	/** cmns-dt:DateTimeStamp - date and time stamp */
	// RDF: cmns-dt:DateTimeStamp
	item def DateTimeStamp {}

	/** cmns-dt:DatePeriod - a period between two dates */
	// RDF: cmns:DatePeriod
	item def DatePeriod {}

	/** cmns-dt:ExplicitDatePeriod - a date period with explicit start and end */
	// RDF: cmns:ExplicitDatePeriod
	item def ExplicitDatePeriod specializes DatePeriod {}

	/** cmns-dt:Duration - a duration of time */
	// RDF: cmns:Duration
	item def Duration {}

	/** cmns-dt:ExplicitDuration - an explicitly stated duration */
	// RDF: cmns:ExplicitDuration
	item def ExplicitDuration specializes Duration {}

	/** cmns-dt:TimeInterval - an interval of time */
	// RDF: cmns:TimeInterval
	item def TimeInterval {}

	/** cmns-dt:TemporalEntity - a temporal entity (date, period, or interval) */
	// RDF: cmns:TemporalEntity
	item def TemporalEntity {}

	/** cmns-dt:TimeOfDay - a time of day */
	// RDF: cmns:TimeOfDay
	item def TimeOfDay {}

	/** cmns-arr:Arrangement - an arrangement or ordering */
	// RDF: cmns:Arrangement
	item def Arrangement {}
}
