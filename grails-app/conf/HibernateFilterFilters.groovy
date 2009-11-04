import org.grails.plugin.hibernate.filter.DefaultHibernateFiltersHolder

class HibernateFilterFilters {

    def filters = {
        all(controller:'*', action:'*') {
            before = {
                def session = grailsApplication.mainContext.sessionFactory.currentSession

                DefaultHibernateFiltersHolder.defaultFilters.each {name ->
                    session.enableFilter(name)
                }

                DefaultHibernateFiltersHolder.defaultFilterCallbacks.each {name, closure ->
                    if(closure.doCall(grailsApplication)) {
println "ENABLING FILTER: ${name}"                        
                        session.enableFilter(name)                        
                    }
                }
            }
            after = {
                
            }
            afterView = {
                
            }
        }
    }
    
}
