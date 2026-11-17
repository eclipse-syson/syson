package P1 {
  metadata def Metadata1 {
    attribute attribute1;
  }

  package P2 {
    filter @Metadata1 and (as Metadata1).attribute1;
  }
}