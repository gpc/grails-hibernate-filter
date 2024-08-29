package hibernate.filter.example

class Bar {

    String name
    Boolean enabled

    static belongsTo = [foo: Foo]

    String toString() {
        "Bar($id):$name:$enabled"
    }
}