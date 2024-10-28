package AliasImport {
    package Definitions {
        part def Vehicle;
        alias Car for Vehicle;
    }
    package Usages {
        public import Definitions::Car;
        part vehicle : Car;
    }
}
