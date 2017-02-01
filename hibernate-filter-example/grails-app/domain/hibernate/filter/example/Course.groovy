package hibernate.filter.example

class Course {

    String name
    Integer status = 1

    static hasMany = [
            students: Student
    ]

    static hibernateFilters = {
        collegeFilter(condition: 'status = 1')
        collegeFilter(collection: 'students', joinTable: true)
    }
}