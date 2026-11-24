package VariantChainSpecializations {
    part def Component;
    part holder {
        part option : Component;
        part selectedType : Component;
    }
    variation part choices : Component {
        variant holder.option : holder.selectedType;
    }
}
