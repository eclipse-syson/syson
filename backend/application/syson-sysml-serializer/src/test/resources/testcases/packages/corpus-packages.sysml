package BooleanTest {
	public item def A {
		protected ref part aReference : Referred;
	}
	alias alias1 for q::req1;
	attribute init1 : ScalarValues::Integer := 0;
	attribute default1 default = 10;
	state s parallel {
	}
	end e1;
	constant attribute ro;
	derived ref attribute y :> init1;
}
library package 'User Defined Extensions' {
	#Security enum def ClassificationLevel :> ScalarValues::Natural {
		#Security enum secret : ClassificationLevel = 2;
	}
}
package p1 {
	part p1 nonunique : P1;
}
public import all P2::*;
standard library package Cases {
	abstract case def Case :> Calculation {
		ref case self : Case :>> Calculation::self;
		objective obj : RequirementCheck[1] {
			subject subj default Case::result;
		}
	}
}
package {
}
