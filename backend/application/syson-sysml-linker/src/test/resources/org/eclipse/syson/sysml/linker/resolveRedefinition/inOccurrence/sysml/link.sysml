package P1 {
  abstract occurrence def Occurence1;
  occurrence def Occurence2 :> Occurence1;

  abstract occurrence occurences2 : Occurence2[*] nonunique;

  item def Item1 :> Occurence1 {
    occurrence :>> occurences2;
  }
}