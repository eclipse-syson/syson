package UseCaseIncludesComments {
    part def System;
    part def User;
    use case def Subtask;

    use case def Main {
        subject system : System;
        actor user : User;
        include use case firstTask : Subtask;
        include use case secondTask {
            subject = system;
        }
        // Preserve a trailing use-case comment.
    }
}
