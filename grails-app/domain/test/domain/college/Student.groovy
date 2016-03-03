package test.domain.college

class Student {

    String name
    Integer status = 1

    static belongsTo = Course

    static hasMany = [
            courses: Course,
            loans: Loan
    ]

    static hibernateFilters = {
        rootFilter(condition: 'status = 1')
        collectionFilter(condition: 'status = 1', collection: 'loans')
    }
}