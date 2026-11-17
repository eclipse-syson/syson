package Operational_Behavior {
    
    action exchangePayload_bb subsets MyOperationalOntology::operationalActions {
        out item payload subsets Operational_Configuration::operationalPayloads redefines MyOperationalOntology::OperationalAction::outputParameter; 
        
        perform evaluateQuality {
            in redefines initialQuality;
            out redefines gainedQuality;

            comment exchangePayloadBbModelerComment about exchangePayload_bb locale "en_US"
            /* Root operational behavior exception: L0 represents the observed/stipulated
             * value stream. See
             * MyFramework::MyFrameworkConfigurationBehaviorSemantics::rootOperationalBehaviorExceptionRule.
             */
            assign gainedQuality := 30.0;
        }
        bind payload.quality = evaluateQuality.gainedQuality;
        
    }

    action exchangePayload_gb subsets exchangePayload_bb {
        action producePayload subsets SystemOfInterest_Behavior::producePayload_bb;
        action consumePayload subsets InteractingSystem_Behavior::consumePayload_bb;
    }

    action evaluateQuality subsets MyOperationalOntology::operationalActions {
        doc /* This action is a placeholder. In a real situation it would 
        * process of a combination of parameters within the action.
        */
            in initialQuality defined by ScalarValues::Real redefines MyOperationalOntology::OperationalAction::inputParameter;
            out gainedQuality defined by ScalarValues::Real redefines MyOperationalOntology::OperationalAction::outputParameter;
            in qualityContribution defined by ScalarValues::Real;
        }
}
