import hibernate.filter.example.User

class BootStrap {

    def init = { servletContext ->
        new User(firstName: 'John', lastName: 'Doe', active: true).save()
        new User(firstName: 'Not', lastName: 'Visible', active: false).save()

    }
    def destroy = {
    }
}
