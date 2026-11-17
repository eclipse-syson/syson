package Packets {
    part def Base {
        attribute packetHeader;
    }
    part def Packet :> Base {
        attribute packetSecondaryHeader redefines packetHeader;
    }
}
