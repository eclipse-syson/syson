package P1 {
  connection def Connection1 {
    end end1;
    end end2;
  }

  part part1 {
    part part2 {
      part end1;
    }
  }

  connection connection1 : Connection1[0..1]
    connect part1.part2.end1 to part3.part4.end2;

  part part3 {
    part part4 {
      part end2;
    }
  }
}