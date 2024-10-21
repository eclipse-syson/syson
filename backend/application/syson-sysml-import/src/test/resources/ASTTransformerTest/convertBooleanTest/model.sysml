package BooleanTest {

    // isReference
	private part def Referred {
	}
	public item def A {
		protected ref part aReference: Referred;
	}

	// isRecursive
	private import q::**;
	requirement def R;
	part p;
	part q {
		requirement req1 : R;
		satisfy req1 by p;
		assert satisfy req1 by q;
	}

	// isNegated
	requirement req2 : R;
	not satisfy req2 by p;

	// isAlias
	alias alias1 for q::req1;

	// isInitial
	attribute init1 : ScalarValues::Integer := 0;

	// isVariation
	variation part def V :> A {
	}

	// isIndividual
	individual occurrence def Ind {
		snapshot snapshot1;
		timeslice timeslice1;
	}

	// isDefault
	attribute default1 default = 10;

	// isParallel
	state s parallel {
		state s1;
		state s2;
	}
	
	// isAbstract
	abstract item def B;

	// isEnd
	end e1;

	// isReadOnly
	readonly attribute ro[0..2];

	// isDerived
	derived ref attribute y :> init1;

	// No Exemples found:
	// * isNonUnique
	// * isOrdered
	// * isSufficient
}