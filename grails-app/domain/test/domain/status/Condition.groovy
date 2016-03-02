package test.domain.status


class Condition {

    String name
    Integer status = 1

    static hasMany = [
            problems: Problem
    ]

    static hibernateFilters = {
        published(condition: "status = 1")
        published(collection: 'problems')
    }
}
