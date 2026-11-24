occurrence def MQTTforwarding {
	ref part mqttsf = AHFNorway_LocalCloudDD.MQTTServer {
		then event occurrence send_returnallitems;
	}
	ref part apscf :> AHFNorway_LocalCloudDD.APISConsumer {
	}
	message sendallitems1 of Return_AllItems from mq to mqttsf.receive_returnallitems;
}
occurrence cruiseControlInteraction_a : CruiseControlInteraction {
	message :>> setSpeedMessage = driver_a.driverBehavior.sendSetSpeed.sentMessage;
}
occurrence def CruiseControlInteraction {
	then message fuelCommandForwardingMessage of fuelCommand : FuelCommand = fuelCommandMessage.fuelCommand from vehicle.engineController.fuelCommandForwarded to vehicle.engine.fuelCommandReceived;
}
