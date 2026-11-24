part part0 {
	message of es : EngineStatus from vehicle.sendStatus to driver.trigger2;
}
part wheelHubAssy1 {
	interface wheelHubInterface : WheelHubInterface connect [1]wheel1.lugNutCompositePort to [1]hub1.shankCompositePort;
}
part missionContext : ContextDefinitions::MissionContext {
	#moe attribute transportTime :> ISQ::time;
}
#servicedd port def APIS_DD :> APISService {
}
part AHFN_LocalCloudDD_Seqs = AHFNorway_LocalCloudDD {
	occurrence def APIS_transfer_lifetime {
		ref part apsc = AHFNorway_LocalCloudDD.APISConsumer {
			then event forw2 : MQTTforwarding;
		}
	}
	interface APIS_transfer_interface : Interfaces::Interface connect (tlu ::> AHFNorway_LocalCloudDD.TellUConsumer.apisp.APIS_HTTP, // Note.
	apsph ::> AHFNorway_LocalCloudDD.APISProducer.tellu.APIS_HTTP, apspm ::> AHFNorway_LocalCloudDD.APISProducer.apisc.APIS_MQTT, apsc ::> AHFNorway_LocalCloudDD.APISConsumer.apisp.APIS_MQTT, mqget ::> AHFNorway_LocalCloudDD.MQTTServer.getTopic, mqgive ::> AHFNorway_LocalCloudDD.MQTTServer.giveTopic) {
		succession first subscribe_returnallitems.start then sendallitems.done;
	}
}
part def ReindeerDrawnSleighAssembly {
	connection : Drives connect [1] santa to [1] sleigh;
}
part 'Comet Interceptor mission context' {
	part 'Comet Interceptor space system' {
		exhibit 'Mission phases';
	}
}
part def ApolloLaunchEscapeSystem :> HardwareComponent, PowerConsumer {
	attribute :>> failureRate = 1E-7 [1 / h];
}
#architectureDesignSpace part controlSystem {
	part sensor[3] : Sensor {
		perform action redefines senseState = provideControl.senseState;
	}
}
variation part def EngineChoices :> Engine {
	variant '6cylEngine' {
		variation port :>> autoPort {
		}
	}
}
part vehicle4Cyl :> PartsTree::vehicleFamily {
	perform action :>> providePowerFamily {
		action :>> amplifyTorque = amplifyTorque::amplifyTorqueManual;
	}
}
item def OrdinaryBag specializes Bag {
}
part vehicleFuelEconomyAnalysisContext {
	analysis cityFuelEconomyAnalysis : FuelEconomyAnalysis {
		in requirement fuelEconomyRequirement = cityFuelEconomyRequirement;
	}
}
part def Wheel :> SpatialItem {
	part lugBolts : LugBolt[1..numberOfBolts] :> subSpatialParts;
	assert constraint {
		(1 .. numberOfBolts)->forAll{ in i : Natural; private attribute lbcf = lugBolts#(i).coordinateFrame; lbcf.transformation == trs }
	}
}
part filteredMassThing :> compositeThing {
	attribute redefines totalMass = mass + sum(subcomponents.totalMass.?{p > minMass});
}
part vehicle : Vehicle {
	flow of [1] Fuel from eng.engineFuelPort.fuelReturn to tankAssy.fuelTankPort.fuelReturn;
	flow of Fuel [1] from eng.engineFuelPort.fuelReturn to tankAssy.fuelTankPort.fuelReturn;
}
part systemOfInterest_gb subsets systemOfInterest_bb {
	perform action producePayload_gb redefines systemOfInterest_bb.producePayload_bb references SystemOfInterest_Behavior::producePayload_gb;
}
part systemOfInterest_wb subsets systemOfInterest_gb {
	connection systemConnections_wb subsets systemConnections_gb[1] {
		interface systemOfInterestStackInterface defined by SystemOfInterest_Taxonomy::MySystemStackInterface {
			flow builtPayload redefines mySystemStackAppFlow;
		}
	}
}
part realization_2 : PubSubSequence {
	flow :>> publish_message from producer.producerBehavior.publish.request to server.serverBehavior.publishing.request {
		event producer.publish_request[1];
	}
}
part def Vehicle4 {
	assert mc {
	}
}
part analysisContext {
	analysis analysisPlan : AnalysisPlan {
	}
}
part def VehicleController {
	exhibit state 'controller states' : 'Controller States';
}
part vehicle1_c1 : VehicleA {
	perform 'provide power' :>> VehicleA::'provide power' {
	}
	exhibit 'vehicle states' :>> VehicleA::'vehicle states' {
		/* Note. */
	}
}
part p1 {
	attribute c : Integer = +1;
}
part system : Component {
	part 'Mouse Controller' : Component {
		port 'Cable Conector' : ComponentPort {
			in [0..*] : ExchangeItem = (rawOpticalData);
		}
	}
	allocation : ExchangeAllocation allocate 'Root Function'.'Optical Data' to cable;
}
part realization_2 : PubSubSequence {
	message :>> deliver_message : Transfers::MessageTransfer {
		end :>> source = server;
		end :>> target = consumer;
	}
}
abstract part def MultistageRocket :> System {
	part stages[1..*] ordered : RocketStage;
}
part def OrdinaryCaroBay specializes CargoBay {
	item redefines payload[0..1] : BottomlessGiftBag;
}
part vehicle : Vehicle {
	flow of [1] Fuel from eng.engineFuelPort.fuelReturn to tankAssy.fuelTankPort.fuelReturn;
}
#architecture part controlSystemInstance subsets controlSystem {
	interface sscInt5 subsets sensorSignalConnections connect sensorSignalOutPort references sensor3.sensorSignalOutPort to sensorSignalInPort references computer3.sensorSignalInPort;
}
part def TypedOrderedContainer :> Types::Base::Element, Types::Other {
	item values : Types::Value :>> values [0..*] ordered;
}
part def ReferenceContainer :> Types::Base {
	ref value : Types::Value :>> value [0..*];
}
part 'System.Variant' : Types::Configuration::System {
	value :> Types::Configuration::Values::value = Library::Domain::Units::SI::Symbols::'Some.Name'::'Other.Name'::unit;
}
