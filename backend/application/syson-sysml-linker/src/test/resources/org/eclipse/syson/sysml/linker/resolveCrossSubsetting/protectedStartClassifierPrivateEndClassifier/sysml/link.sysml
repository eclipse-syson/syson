protected item def A {
  public item  myB : B;
}

private item def B {
  public item  myA : A;
}

connection def B1 {
  end item  aEnd : A crosses bEnd.myA;
  end item  bEnd : B crosses aEnd.myB;
}
