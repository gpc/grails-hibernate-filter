import org.grails.plugin.hibernate.filter.HibernateFilterConnectionSourceFactory

// Place your Spring DSL code here
beans = {
    hibernateConnectionSourceFactory(HibernateFilterConnectionSourceFactory)
}
