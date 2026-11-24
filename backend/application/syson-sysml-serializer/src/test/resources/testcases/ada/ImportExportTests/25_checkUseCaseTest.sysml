package UseCaseTest {

    part def System;
    part def User;

    use case def UseSystem {
        subject system : System;
        actor user : User;

        objective  {
            /* Goal */
        }

        include use case uc1 : UC1;
        include use case uc2 {
            subject = system;
            actor user = user;
        }
    }

    use case def UC1;

    part user : User;

    use case uc2 {
        actor :>> user;
    }

    use case u : UseSystem;

    part system : System {
        include UseCaseTest::uc2;
        perform UseCaseTest::u;
    }

}
