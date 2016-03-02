package test.domain.status


class Problem {

    String name
    Integer status = 1

    static belongsTo = Condition

    static hasMany = [
            conditions: Condition
    ]

    static hibernateFilters = {
        statusFilter(condition: "status = 1")
        collectionFilter(condition: 'status = 1', collection: 'conditions')
    }
}
