package ConnectionInterfaceAlternatives {
    part source;
    part destination;

    abstract individual occurrence def UniqueEvent;
    event source;

    binding namedBinding bind source = destination;
    connection namedConnection connect source to destination;
    connect source to destination;

    interface def DetailedInterface {
        part def Payload;
        alias payloadAlias for Payload;
        private import ConnectionInterfaceAlternatives::source;
        end port leftEnd;
        end port rightEnd;
        variant port optionalEnd;
        then port laterEnd;
    }
    interface connect source to destination;
    allocate source to destination;
    message source to destination;
}
