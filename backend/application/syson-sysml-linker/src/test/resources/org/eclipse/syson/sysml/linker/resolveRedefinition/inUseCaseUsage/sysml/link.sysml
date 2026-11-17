package P1 {
  use case useCase1 {
    actor actor1;
    actor actor2;
  
    then include useCase2 {
      actor :>> actor1 = useCase1::actor1;
      actor :>> actor2 = useCase1::actor2;
    }
  
    then include useCase3 {
      actor :>> actor1 = useCase1::actor1;
      actor :>> actor2 = useCase1::actor2;
    }
  }
 
  use case useCase2 {
    actor actor1;
    actor actor2;
  }
 
  use case useCase3 {
    actor actor1;
    actor actor2;
  }
}