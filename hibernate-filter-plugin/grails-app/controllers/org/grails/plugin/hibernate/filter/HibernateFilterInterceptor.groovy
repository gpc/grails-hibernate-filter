package org.grails.plugin.hibernate.filter

class HibernateFilterInterceptor {

    def sessionFactory

    HibernateFilterInterceptor() {
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
