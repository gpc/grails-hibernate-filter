package hibernate.filter.example

import org.grails.plugin.hibernate.filter.DefaultHibernateFiltersHolder

/**
 * TODO: Figure out why interceptor included in plugin is not being included
 */
class MyHibernateFilterInterceptor {

    def sessionFactory

    MyHibernateFilterInterceptor() {
        matchAll()
    }

    boolean before() {
        def session = sessionFactory.currentSession
        DefaultHibernateFiltersHolder.defaultFilters.each {
            session.enableFilter it
        }
        true
    }

}
