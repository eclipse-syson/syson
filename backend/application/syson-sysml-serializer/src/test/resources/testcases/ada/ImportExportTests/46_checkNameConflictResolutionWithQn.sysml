package 'root && root' {
    package 'AA & BB' {
        item def ItemDef0;
        requirement def RecDef1 {
            requirementsSpecification : ItemDef0;
        }
        package 'JJ & GG' {
            item requirements : ItemDef0;
            item requirements2 : ItemDef0;
        }
    }
    package 'CC & DD' {
        private import 'AA & BB'::'JJ & GG'::*;
        requirement rec0 : 'AA & BB'::RecDef1 {
            requirementsSpecification :> 'AA & BB'::RecDef1::requirementsSpecification = 'root && root'::'AA & BB'::'JJ & GG'::requirements2;
        }
    }
}
