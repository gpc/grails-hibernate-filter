package org.grails.plugin.hibernate.filter

import org.grails.plugin.hibernate.filter.DefaultHibernateFiltersHolder

class HibernateFilterInterceptor {

    def sessionFactory

    HibernateFilterInterceptor() {
        match(controller: '*', action: '*')
    }

    boolean before() {
        def session = sessionFactory.currentSession
        for (String name in DefaultHibernateFiltersHolder.defaultFilters) {
            session.enableFilter name
        }
        true
    }
}
