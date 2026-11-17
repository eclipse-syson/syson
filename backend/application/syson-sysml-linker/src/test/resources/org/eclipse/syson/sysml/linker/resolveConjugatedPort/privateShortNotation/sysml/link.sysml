private port def P {
  public attribute attribute1;
}

part def Part1 {
  port chargeInPort : ~P {
    attribute :>> attribute1 = 1;
  }
}
