package root {
    part def PartDef1;
    concern def ConcernDef1 {
        stakeholder s : PartDef1;
    }
    concern concernUsage1 : ConcernDef1 {
        stakeholder s1;
    }
    requirement def R1Def {
        stakeholder s : PartDef1;
    }
    requirement r1 : R1Def {
        stakeholder s1;
    }
}
