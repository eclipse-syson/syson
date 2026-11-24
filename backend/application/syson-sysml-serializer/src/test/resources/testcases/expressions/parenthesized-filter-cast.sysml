package ParenthesizedFilterCast {
    metadata def Safety {
        attribute isMandatory;
    }
    part vehicle;
    package Mandatory {
        public import vehicle::**;
        filter @Safety and (as Safety).isMandatory;
    }
}
