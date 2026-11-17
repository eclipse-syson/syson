// ===================================================================
// CorporateActions.sysml
// Source-parity extracted from: FiBO_CAE_CorporateEvents.sysml
// Primary source module: CorporateActions.rdf
// Source mirrored path: C:/Users/EORMOHU3W/GitHub_Repositories/FiBo2SysMLv2/FiBO2SysMLv2_Project/FiBO Corporate Action and Events CAE/CorporateEvents
// ===================================================================
package Corporate_Actions {
  doc/* This ontology provides a high level overview of actions including corporate, market, and regulatory actions, ranging from business oriented events such as address and name changes, to those that are more specific to securities. (source: dct:abstract, CorporateActions.rdf) */
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

  item def <'action classification scheme'> ActionClassificationScheme specializes Assessments::ClassificationScheme, FiBO_FND_CodeSets_And_Schemes::CodeSet {
    	doc /* classifier that distinguishes the kinds of actions and events that may be announced, initiated or carried out by an organization that affects a legal entity or the securities it issues, such as income-oriented events */
    	// skos:definition xml:lang="en" (CorporateActions.rdf)
    	// rdfs:label xml:lang="en" (CorporateActions.rdf)
    	// RDF: fibo-cae-ce-act:ActionClassificationScheme
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;ClassificationScheme"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cds;CodeSet"/>
    }

  item def <'action classifier'> ActionClassifier specializes FiBOOccurrences::OccurrenceKind, FiBO_FND_ExternalStubs::Classifier {
    	doc /* classifier that distinguishes the kinds of actions and events that may be announced, initiated or carried out by an organization that affects a legal entity or the securities it issues, such as income-oriented events */
    	// skos:definition xml:lang="en" (CorporateActions.rdf)
    	// rdfs:label xml:lang="en" (CorporateActions.rdf)
    	// RDF: fibo-cae-ce-act:ActionClassifier
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-oc;OccurrenceKind"/>
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
    }

  item def <'action group'> ActionGroup specializes FiBO_FND_ExternalStubs::Classifier {
    	doc /* classifier that differentiates corporate actions based on a GLEIF specific grouping */
    	// skos:definition xml:lang="en" (CorporateActions.rdf)
    	// rdfs:label xml:lang="en" (CorporateActions.rdf)
    	// RDF: fibo-cae-ce-act:ActionGroup
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&cmns-cls;Classifier"/>
    }

  item def <'action status'> ActionStatus specializes Lifecycles::LifecycleStatus {
    	doc /* state of some action at some point in time */
    	// skos:definition xml:lang="en" (CorporateActions.rdf)
    	// rdfs:label xml:lang="en" (CorporateActions.rdf)
    	// RDF: fibo-cae-ce-act:ActionStatus
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-arr-lif;LifecycleStatus"/>
    }

  item def <'action'> Action specializes FiBOOccurrences::FiBOOccurrence {
    	doc /* event announced, initiated or carried out by an organization that affects a legal entity or the securities it issues and may have a material impact on that entity&apos;s stakeholders, such as shareholders and creditors */
    	// skos:definition xml:lang="en" (CorporateActions.rdf)
    	// rdfs:label xml:lang="en" (CorporateActions.rdf)
    	// RDF: fibo-cae-ce-act:Action
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-fnd-dt-oc;FiBOOccurrence"/>
    }

  item def <'business strategy classifier'> BusinessStrategyClassifier specializes ActionClassifier {
    	doc /* classifier of corporate actions that involve improving liquidity or changing the overall structure of the organization through diversification, combining and closing parts of the business, etc, to increase long-term profitability */
    	// skos:definition xml:lang="en" (CorporateActions.rdf)
    	// rdfs:label xml:lang="en" (CorporateActions.rdf)
    	// RDF: fibo-cae-ce-act:BusinessStrategyClassifier
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;ActionClassifier"/>
    }

  item def <'change action'> ChangeAction specializes Action, MandatoryCorporateAction {
    	doc /* corporate action to disseminate information regarding a change further described in the corporate action details */
    	// skos:definition xml:lang="en" (CorporateActions.rdf)
    	// rdfs:label xml:lang="en" (CorporateActions.rdf)
    	// RDF: fibo-cae-ce-act:ChangeAction
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;MandatoryCorporateAction"/>
    }

  item def <'class action'> ClassAction specializes VoluntaryCorporateAction {
    	doc /* corporate action involving a situation where interested parties seek restitution for financial loss */
    	// skos:definition xml:lang="en" (CorporateActions.rdf)
    	// rdfs:label xml:lang="en" (CorporateActions.rdf)
    	// RDF: fibo-cae-ce-act:ClassAction
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;VoluntaryCorporateAction"/>
    }

  item def <'consent solicitation'> ConsentSolicitation specializes VoluntaryCorporateAction {
    	doc /* corporate action that is a procedure that aims to obtain consent of holder to a proposal by the issuer or a third party without convening a meeting */
    	// skos:definition xml:lang="en" (CorporateActions.rdf)
    	// rdfs:label xml:lang="en" (CorporateActions.rdf)
    	// RDF: fibo-cae-ce-act:ConsentSolicitation
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;VoluntaryCorporateAction"/>
    }

  item def <'corporate action'> CorporateAction specializes Action {
    	doc /* action carried out by or specifically relating to a legal entity that may affect the securities it issues and may have a material impact on its stakeholders, such as shareholders and creditors */
    	// skos:definition xml:lang="en" (CorporateActions.rdf)
    	// rdfs:label xml:lang="en" (CorporateActions.rdf)
    	// RDF: fibo-cae-ce-act:CorporateAction
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;Action"/>
    }

  item def <'disclosure action'> DisclosureAction specializes Action, VoluntaryCorporateAction {
    	doc /* corporate action involving a request for holders or beneficial owners to disclose their name, location and holdings of any issue to the issuer */
    	// skos:definition xml:lang="en" (CorporateActions.rdf)
    	// rdfs:label xml:lang="en" (CorporateActions.rdf)
    	// RDF: fibo-cae-ce-act:DisclosureAction
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;VoluntaryCorporateAction"/>
    }

  item def <'divestiture'> Divestiture specializes MandatoryCorporateAction {
    	doc /* corporate action involving the sales of subsidiary business interests or investments */
    	// skos:definition xml:lang="en" (CorporateActions.rdf)
    	// rdfs:label xml:lang="en" (CorporateActions.rdf)
    	// RDF: fibo-cae-ce-act:Divestiture
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;MandatoryCorporateAction"/>
    }

  item def <'income-oriented classifier'> IncomeOrientedClassifier specializes ActionClassifier {
    	doc /* classifier of corporate actions that impacts income to shareholders */
    	// skos:definition xml:lang="en" (CorporateActions.rdf)
    	// rdfs:label xml:lang="en" (CorporateActions.rdf)
    	// RDF: fibo-cae-ce-act:IncomeOrientedClassifier
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;ActionClassifier"/>
    }

  item def <'legal form change'> LegalFormChange specializes ChangeAction, MandatoryCorporateAction {
    	doc /* corporate action indicating a modification of the legal form of the organization */
    	// skos:definition xml:lang="en" (CorporateActions.rdf)
    	// rdfs:label xml:lang="en" (CorporateActions.rdf)
    	// RDF: fibo-cae-ce-act:LegalFormChange
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;MandatoryCorporateAction"/>
    }

  item def <'liquidation'> Liquidation specializes Divestiture {
    	doc /* corporate action related to winding up a business, including but not limited to distribution of cash, assets, or both */
    	// skos:definition xml:lang="en" (CorporateActions.rdf)
    	// rdfs:label xml:lang="en" (CorporateActions.rdf)
    	// RDF: fibo-cae-ce-act:Liquidation
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;Divestiture"/>
    }

  item def <'mandatory corporate action'> MandatoryCorporateAction specializes CorporateAction {
    	doc /* action initiated by the board of directors of a corporation that affects all shareholders */
    	// skos:definition xml:lang="en" (CorporateActions.rdf)
    	// rdfs:label xml:lang="en" (CorporateActions.rdf)
    	// RDF: fibo-cae-ce-act:MandatoryCorporateAction
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;CorporateAction"/>
    }

  item def <'mandatory with choice corporate action'> MandatoryWithChoiceCorporateAction specializes MandatoryCorporateAction {
    	doc /* mandatory corporate action where shareholders are given an opportunity to choose among several options */
    	// skos:definition xml:lang="en" (CorporateActions.rdf)
    	// rdfs:label xml:lang="en" (CorporateActions.rdf)
    	// RDF: fibo-cae-ce-act:MandatoryWithChoiceCorporateAction
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;MandatoryCorporateAction"/>
    }

  item def <'market action'> MarketAction specializes Action {
    	doc /* action that is initiated by a market, such as a securities exchange, that affects a legal entity or the securities it issues and may have a material impact on that entity&apos;s stakeholders, such as shareholders and creditors */
    	// skos:definition xml:lang="en" (CorporateActions.rdf)
    	// rdfs:label xml:lang="en" (CorporateActions.rdf)
    	// RDF: fibo-cae-ce-act:MarketAction
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;Action"/>
    }

  item def <'merger / acquisition'> MergerAcquisition specializes MandatoryCorporateAction {
    	doc /* corporate action involving the consolidation of legal entities or assets */
    	// skos:definition xml:lang="en" (CorporateActions.rdf)
    	// rdfs:label xml:lang="en" (CorporateActions.rdf)
    	// RDF: fibo-cae-ce-act:MergerAcquisition
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;MandatoryCorporateAction"/>
    }

  item def <'notification'> Notification specializes MandatoryCorporateAction {
    	doc /* corporate action involving information provided by the issuer having no accounting/financial impact on the holder */
    	// skos:definition xml:lang="en" (CorporateActions.rdf)
    	// rdfs:label xml:lang="en" (CorporateActions.rdf)
    	// RDF: fibo-cae-ce-act:Notification
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;MandatoryCorporateAction"/>
    }

  item def <'organization address change'> OrganizationAddressChange specializes ChangeAction, Notification {
    	doc /* information action that provides details of address changes for a legal entity */
    	// skos:definition xml:lang="en" (CorporateActions.rdf)
    	// rdfs:label xml:lang="en" (CorporateActions.rdf)
    	// RDF: fibo-cae-ce-act:OrganizationAddressChange
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;Notification"/>
    }

  item def <'organization name change'> OrganizationNameChange specializes ChangeAction, Notification {
    	doc /* information action that provides details of name changes for a legal entity */
    	// skos:definition xml:lang="en" (CorporateActions.rdf)
    	// rdfs:label xml:lang="en" (CorporateActions.rdf)
    	// RDF: fibo-cae-ce-act:OrganizationNameChange
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;Notification"/>
    }

  item def <'priority issue'> PriorityIssue specializes VoluntaryCorporateAction {
    	doc /* corporate action that is a public offer where, due to a limited amount of securities available, priority is given to existing shareholders */
    	// skos:definition xml:lang="en" (CorporateActions.rdf)
    	// rdfs:label xml:lang="en" (CorporateActions.rdf)
    	// RDF: fibo-cae-ce-act:PriorityIssue
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;VoluntaryCorporateAction"/>
    }

  item def <'regulatory action'> RegulatoryAction specializes Action {
    	doc /* action initiated by a regulatory agency that affects a legal entity or the securities it issues and may have a material impact on that entity&apos;s stakeholders, such as shareholders and creditors */
    	// skos:definition xml:lang="en" (CorporateActions.rdf)
    	// rdfs:label xml:lang="en" (CorporateActions.rdf)
    	// RDF: fibo-cae-ce-act:RegulatoryAction
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;Action"/>
    }

  item def <'spin off'> SpinOff specializes Divestiture {
    	doc /* corporate action involving the distribution of subsidiary stock to the shareholders of the parent company without a surrender of shares */
    	// skos:definition xml:lang="en" (CorporateActions.rdf)
    	// rdfs:label xml:lang="en" (CorporateActions.rdf)
    	// RDF: fibo-cae-ce-act:SpinOff
        // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;Divestiture"/>
    }

  item def <'voluntary corporate action'> VoluntaryCorporateAction specializes CorporateAction {
    	doc /* event in which the shareholders elect to participate and must respond in order for the issuer to process the action */
    	// skos:definition xml:lang="en" (CorporateActions.rdf)
    	// rdfs:label xml:lang="en" (CorporateActions.rdf)
    	// RDF: fibo-cae-ce-act:VoluntaryCorporateAction
      // OWL-subClassOf: <rdfs:subClassOf rdf:resource="&fibo-cae-ce-act;CorporateAction"/>
    }

}
