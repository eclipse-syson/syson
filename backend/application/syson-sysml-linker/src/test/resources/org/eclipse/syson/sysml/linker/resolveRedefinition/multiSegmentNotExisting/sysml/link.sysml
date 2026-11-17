attribute a {

}

part def part1 {
  public attribute attribute1;
}

part def part2 :> part1 {
  attribute redefinedAttr1 :>> NotExisting::attribute1 :> a;
}
