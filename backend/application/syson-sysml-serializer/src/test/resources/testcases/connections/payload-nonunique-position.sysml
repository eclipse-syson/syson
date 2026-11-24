package PayloadNonuniquePosition {
    flow late of : Data nonunique from source to destination;
    flow early of nonunique : Data from source to destination;
    flow namedLate of data : Data nonunique from source to destination;
    flow namedEarly of data nonunique : Data from source to destination;
    flow lateWithMultiplicity of data : Data [2] nonunique ordered from source to destination;
    action receive {
        accept data : Data nonunique;
        accept data nonunique : Data;
        accept : Data [2] ordered nonunique via inlet;
        accept other : Data [2] nonunique ordered;
    }
}
