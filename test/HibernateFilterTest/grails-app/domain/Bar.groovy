class Bar {

    String name
    boolean enabled

    static belongsTo = [foo: Foo]

    static constraints = {}
}
