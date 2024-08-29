package hibernate.filter.example

class User {

    static constraints = {
    }

    String firstName
    String lastName
    boolean active = true

    static hibernateFilters = {
        activeFilter(condition: 'active=true', default: true)
    }

    static mapping = {
        table name: "USER_INFO"
    }
}
