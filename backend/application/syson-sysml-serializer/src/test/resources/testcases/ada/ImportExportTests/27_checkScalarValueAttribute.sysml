package Occurrences {
    private import ScalarValues::*;
    occurrence def Occurrence1 {
        attribute a : Integer;
        attribute b : Integer default = 1;
        attribute c : Integer = 1;
        attribute d : Integer := 3;
        attribute e : MyEnum default = MyEnum::enum1;
        attribute f : Real = 1.0;
        attribute g = 3.14;
        attribute h = 2.5E-10;
        attribute i = .5;
        attribute j = 1E+3;
    }
    enum def MyEnum {
        enum1;
    }
}
