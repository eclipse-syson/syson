public item def A {
  protected item  myB : B;
}

public item def B {
  protected item  myA : A;
}

connection def B1 {
  end item  aEnd : A crosses bEnd.myA;
  end item  bEnd : B crosses aEnd.myB;
}
