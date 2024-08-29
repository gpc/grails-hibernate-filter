package hibernate.filter.example

class Student {

    String name
    Integer status = 1

    static belongsTo = Course

    static hasMany = [
            courses: Course,
            loans: Loan,
            pens: Pen
    ]

    static mapping = {
        courses(joinTable: 'courses_students', key: 'student_id')
    }

    static hibernateFilters = {
        collegeFilter(condition: 'status = 1')
        collegeFilter(condition: 'status = 1', collection: 'loans')
        collegeFilter(condition: 'status = 1', collection: 'pens', joinTable: true)
    }
}