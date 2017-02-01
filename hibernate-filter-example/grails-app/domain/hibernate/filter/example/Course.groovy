package hibernate.filter.example

class Course {

    String name
    Integer status = 1

    static hasMany = [
            students: Student
    ]

    static mapping = {
        students(joinTable: 'courses_students', key: 'course_id')
    }

    static hibernateFilters = {
        collegeFilter(condition: 'status = 1')
        collegeFilter(condition: 'status = 1', collection: 'students', joinTable: true)
    }
}