enum def Enum1 {
    <e1> enumeration1;
    enumeration2;
}
part part1 {
    attribute attribute1 : Enum1 = Enum1::enumeration1;
    attribute attribute2 : Enum1 = Enum1::enumeration1;
    attribute attribute3 : Enum1 = Enum1::e1;
    attribute attribute4 : Enum1 = Enum1::enumeration2;
    attribute attribute5 = Enum1::enumeration1;
}
