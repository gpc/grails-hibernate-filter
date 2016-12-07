package org.grails.plugin.hibernate.filter

import org.hibernate.SessionFactory

class HibernateFilterInterceptor {

    SessionFactory sessionFactory

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
