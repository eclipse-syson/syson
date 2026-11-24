package AnalysisMultiplicity {
    part def Vehicle;
    analysis def Inspect {
        subject vehicles : Vehicle [1..*];
    }
    analysis inspections : Inspect [2] {
        subject vehicles : Vehicle [3];
    }
}
