interface def WheelFastenerInterface {
	attribute maxTorque : Torque;
	constraint {
		lugNutPort.threadDia == shankPort.threadDia
	}
}
connection def Drives {
	end part driver : Person crosses driven.driver;
}
#derivation connection {
	end #derive ::> 'SYS-010';
}
interface producer_2.publicationPort to server_2.publicationPort;
connection bus : C connect (d1, d2, d3, d4);
connection def Violation {
	end [*] ref sit : Situation;
}
allocation allocation1 : A {
}
interface def ComponentExchange :> ArcadiaElement {
	ref allocatedExchangeItems : ExchangeItem[*];
}
interface def PublicationInterface {
	end source : ~PublicationPort;
}
abstract interface binaryInterfaces : BinaryInterface[0..*] nonunique :> interfaces, binaryConnections {
}
