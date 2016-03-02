package test.domain.status


class Condition {

    String name
    Integer status = 1

    static hasMany = [
            problems: Problem
    ]

    static hibernateFilters = {
        statusFilter(condition: 'status = 1')
        collectionFilter(condition: 'status = 1', collection: 'problems')
    }
}
