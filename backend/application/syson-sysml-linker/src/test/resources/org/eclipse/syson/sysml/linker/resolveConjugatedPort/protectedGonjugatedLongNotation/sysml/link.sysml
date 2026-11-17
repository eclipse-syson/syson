protected port def P {
  public attribute attribute1;
}

part def Part1 {
  port chargeInPort : ~P::'~P' {
    attribute :>> attribute1 = 1;
  }
}
