attribute specificGravityOfGasoline : Real = 0.76;
#service  def APISService {
}
#clouddd AHFNorway_LocalCloudDD :> ArrowheadCore {
	#systemdd APISProducer {
		state APISPbehavior {
			entry send new Publish("Return_AllItems") via apisc.APIS_MQTT;
			then WaitOnData;
			state WaitOnData;
			accept cl : CallGiveItems via tellu.APIS_HTTP do action {
				then action giveItems {
				}
			} then WaitOnData;
		}
	}
}
:> ISQ::power = system.subparts->select{ in sys : Part; sys istype PowerConsumer }->collect{ in sys : PowerConsumer; sys.powerLoad }->sum();
deltaV :> ISQ::speed = (v_inf ^ 2 + 2 * mu_Moon / r_periapsis) ^ (1 / 2) - (mu_Moon / r_periapsis) ^ (1 / 2);
satisfy 'SYS-010' by 'Comet Interceptor mission context'.'Comet Interceptor space system'.'Comet Interceptor space segment';
abstract flow def F;
private ref #Classified #Security z1;
enum def Choice;
variation attribute def DiameterChoices :> Diameter {
	variant attribute diameterLarge;
}
attribute def TranslationRotationSequence :> CoordinateTransformation, List {
	attribute :>> elements : TranslationOrRotation[1..*] ordered nonunique;
}
attribute def DimensionOneUnit :> DerivedUnit {
	attribute :>> unitPowerFactors = ();
}
individual UnitedStatesWithJohnAsPresident : UnitedStates {
	timeslice item UnitedStatesWhenJohnIsPresident[*] : UnitedStates {
	}
}
constraint def MassLimit {
	mass <= maxMass
}
calc def excludingOnce {
	in seq[1..*] nonunique ordered;
	seq->excludingAt(position)
}
reliability : RatioValue = eulerNumber ^ (-(failureRate * missionTime));
