public item def A {
  private item  myB : B;
}

public item def B {
  public item  myA : A;
}

connection def B1 {
  end item  aEnd : A crosses bEnd.myA;
  end item  bEnd : B crosses aEnd.myB;
}
