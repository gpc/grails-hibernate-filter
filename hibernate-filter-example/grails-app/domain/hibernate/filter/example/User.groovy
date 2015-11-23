package hibernate.filter.example

class User {

    static constraints = {
    }

    String firstName
    String lastName
    boolean active = true

    static hibernateFilters = {
        activeFilter(condition: 'active=1', default: true)
    }
}
