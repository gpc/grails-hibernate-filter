import org.codehaus.groovy.grails.commons.GrailsDomainClass
import grails.plugin.hibernate.filter.HibernateFilterUtils

class HibernateFilterGrailsPlugin {
	def version = '0.4.0'
	def grailsVersion = '2.5 > *'
	def loadAfter = ['controllers', 'hibernate']
	def observe = ['*']
	def pluginExcludes = ['grails-app/domain/**']

	def author = 'Scott Burch'
	def authorEmail = 'scott@bulldoginfo.com'
	def title = 'Hibernate Filter plugin'
	def description = 'Integrates Hibernate filtering into Grails'
	def documentation = 'http://grails.org/plugin/hibernate-filter'

	def license = 'APACHE'
	def developers = [[name: 'Burt Beckwith', email: 'beckwithb@vmware.com'],
	                  [name: 'Alex Shneyderman', email: 'a.shneyderman@gmail.com']]
	def issueManagement = [system: 'JIRA', url: 'http://jira.grails.org/browse/GPHIBERNATEFILTER']
	def scm = [url: 'https://github.com/burtbeckwith/grails-hibernate-filter']

	def doWithDynamicMethods = { ctx ->
		for (GrailsDomainClass dc in application.domainClasses) {
			HibernateFilterUtils.addDomainClassMethods dc.clazz, ctx
		}

		for (Class artefactClass in application.allArtefacts) {
			HibernateFilterUtils.addDomainProxies artefactClass
		}
	}

	def onChange = { event ->

		if (!(event.source instanceof Class)) return
		if (!event.application.isArtefact(event.source)) return

		HibernateFilterUtils.addDomainProxies event.source

		if (event.application.isDomainClass(event.source)) {
			HibernateFilterUtils.addDomainClassMethods event.source, event.ctx
		}
	}
}
