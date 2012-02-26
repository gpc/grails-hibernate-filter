import org.codehaus.groovy.grails.commons.GrailsDomainClass
import org.grails.plugin.hibernate.filter.DefaultHibernateFiltersHolder
import grails.util.GrailsUtil

class HibernateFilterGrailsPlugin {
	def version = '0.2'
	def grailsVersion = '1.1 > *'

	def loadAfter = ['controllers', 'hibernate']
	def observe = ['controllers']

	def author = 'Scott Burch'
	def authorEmail = 'scott@bulldoginfo.com'
	def title = 'Hibernate Filter plugin'
	def description = 'Integrates Hibernate filtering into Grails'
	def documentation = 'http://grails.org/plugin/hibernate-filter'

	def doWithDynamicMethods = { ctx ->
		for (GrailsDomainClass dc in application.domainClasses) {
			addMethods dc.clazz, ctx
		}

		for (Class artefactClass in application.allArtefacts) {
			addDomainProxies artefactClass
		}
	}

	def onChange = { event ->
		if (event?.source) {
			addDomainProxies(event.source)
		}
	}

	private void addDomainProxies(clazz) {
		for (HibernateFilterDomainProxy proxy in DefaultHibernateFiltersHolder.domainAliasProxies) {
			clazz.metaClass."$proxy.aliasName" = proxy
		}
	}

	private void addMethods(clazz, ctx) {
		clazz.metaClass.static.withHibernateFilter = { String name, Closure closure ->
			def session = ctx.sessionFactory.currentSession
			def isFilterDisabled = session.getEnabledFilter(name) == null
			try {
				session.enableFilter name
				closure.doCall()
			}
			finally {
				if (isFilterDisabled) {
					session.disableFilter name
				}
			}
		}

		clazz.metaClass.static.withoutHibernateFilter = { String name, Closure closure ->
			def session = ctx.sessionFactory.currentSession
			def isFilterEnabled = session.getEnabledFilter(name) != null
			try {
				session.disableFilter(name)
				closure.doCall()
			}
			finally {
				if (isFilterEnabled) {
					session.enableFilter name
				}
			}
		}

		clazz.metaClass.static.enableHibernateFilter = { String name ->
			ctx.sessionFactory.currentSession.enableFilter name
		}

		clazz.metaClass.static.disableHibernateFilter = { String name ->
			ctx.sessionFactory.currentSession.disableFilter name
		}
	}
}
