package test.domain.status


class Problem {

    String name
    Integer status = 1

    static belongsTo = Condition

    static hasMany = [
            conditions: Condition
    ]

    static hibernateFilters = {
        published(condition: "status = 1")
        published(collection: 'conditions')
    }
}
