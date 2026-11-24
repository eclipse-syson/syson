package AxleAssembly_1 {
    private import Views::*;
    part def AxleAssembly;
    part def Differential;
    part rearAxleAssembly : AxleAssembly {
        part differential : Differential;
        part rearAxle;
    }
    view 'First-Level-All' {
        expose rearAxleAssembly;
        expose rearAxleAssembly::rearAxle;
        expose rearAxleAssembly::differential;
        render asTreeDiagram;
    }
} 
