attribute a {

}

part def part1 {
  public attribute attribute1 {
    protected attribute attribute2;
  }
}

part def part2 :> part1 {
  attribute redefinedAttr1 :>> attribute1::attribute2 :> a;
}
