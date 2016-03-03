package test.domain.college

class Course {

    String name
    Integer status = 1

    static hasMany = [
            students: Student
    ]

    static hibernateFilters = {
        rootFilter(condition: 'status = 1')
        collectionFilter(condition: 'status = 1', collection: 'students')
    }
}