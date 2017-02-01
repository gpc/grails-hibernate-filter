package hibernate.filter.example

class Loan {

    Integer amount
    Integer status = 1

    static belongsTo = [borrower: Student]

    static hibernateFilters = {
        collegeFilter()
    }
}
