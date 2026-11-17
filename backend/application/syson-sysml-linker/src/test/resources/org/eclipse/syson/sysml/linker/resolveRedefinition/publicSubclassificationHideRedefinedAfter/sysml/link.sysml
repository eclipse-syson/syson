attribute a {

}

part def part1 {
  public attribute attribute1;
}

part def part2 :> part1 {
  attribute redefinedAttr1 :>> attribute1 :> a;
  attribute attribute2 :> attribute1;
}
