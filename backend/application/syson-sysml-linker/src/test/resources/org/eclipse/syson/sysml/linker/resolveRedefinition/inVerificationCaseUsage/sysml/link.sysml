package P1 {
	requirement def Requirement1;
		
	part def Part1;
		
	verification def Verifivation1 {
		objective objective1 {
			verify requirement requirement1 : Requirement1;
		}
	}

	requirement myRequirement1 : Requirement1 {
		subject subject1 : Part1;
	}

	verification verifivation1 : Verifivation1 {
		subject part1 : Part1;
		objective objective1 {
			verify myRequirement1 :>> requirement1;
		}
	}
}