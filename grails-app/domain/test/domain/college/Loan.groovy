package test.domain.college

class Loan {

    Integer amount
    Integer status = 1

    static belongsTo = [borrower: Student]

    static hibernateFilters = {
        rootFilter(condition: 'status = 1')
    }
}
