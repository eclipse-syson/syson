public package Other {
  metadata def Metadata1;
}

public package P1 {
  private import Other::*;

  metadata Metadata1;
}
