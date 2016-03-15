package test.domain.college

class Student {

    String name
    Integer status = 1

    static belongsTo = Course

    static hasMany = [
            courses: Course,
            loans: Loan,
            pens: Pen
    ]

    static hibernateFilters = {
        collegeFilter()
        collegeFilter(collection: 'loans')
        collegeFilter(collection: 'pens', joinTable: true)
    }
}