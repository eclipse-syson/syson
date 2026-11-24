package InterfaceAllocationMessage {
    port def Socket;
    part supplier { port output : Socket; }
    part consumer { port input : Socket; }

    interface def Link {
        end port sender;
        end port receiver;
        flow data from sender to receiver;
    }
    interface link : Link connect supplier.output to consumer.input;
    allocation def Placement;
    allocation placement : Placement allocate supplier to consumer;
    message update from supplier.output to consumer.input;
}
