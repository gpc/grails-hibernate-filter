import org.grails.plugin.hibernate.filter.DefaultHibernateFiltersHolder
import org.hibernate.HibernateException
import org.codehaus.groovy.grails.web.context.ServletContextHolder


class HibernateFilterFilters {

	def sessionFactory

	def filters = {
		enableHibernateFilters(controller:'*', action:'*') {
			before = {
		                def session = getHibernateSession()
		                if(session) {
					for (String name in DefaultHibernateFiltersHolder.defaultFilters) {
						session.enableFilter name
					}
		                }
			}
		}
	}
	
    private getHibernateSession() {
        def session
        try {
            // Get via the normal session factory
            session = sessionFactory.currentSession
        }
        catch(HibernateException ignored) {
            // If this is webflow, try to return the session from flow scope
            session = ServletContextHolder.servletContext?.flowScope?.get("session")
        }

        return session
    }
	
}
