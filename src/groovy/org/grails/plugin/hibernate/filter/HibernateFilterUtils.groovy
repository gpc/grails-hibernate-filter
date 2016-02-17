package org.grails.plugin.hibernate.filter

import org.hibernate.Session
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Utility methods.
 *
 * @author Burt Beckwith
 */
class HibernateFilterUtils {

	private static final Logger LOG = LoggerFactory.getLogger(this)

	/**
	 * Register proxies based on 'hibernateFilters' configuration.
	 * @param artefactClass the artifact class
	 */
	static void addDomainProxies(Class artefactClass) {
		for (HibernateFilterDomainProxy proxy in DefaultHibernateFiltersHolder.domainAliasProxies) {
			LOG.debug "Adding proxy {} in class {}", proxy, artefactClass.name
			artefactClass.metaClass."$proxy.aliasName" = proxy
		}
	}

	/**
	 * Register domain class metaclass methods.
	 * @param clazz the domain class
	 * @param ctx the application context
	 */
	static void addDomainClassMethods(Class clazz, ctx) {

		LOG.debug "addDomainClassMethods for class {}", clazz.name

		clazz.metaClass.static.withHibernateFilter = { String name, Closure closure ->
			Session session = ctx.sessionFactory.currentSession
			withHibernateFilters session, [name], closure
		}

		clazz.metaClass.static.withHibernateFilters = { Closure closure ->
			Session session = ctx.sessionFactory.currentSession
			withHibernateFilters session, DefaultHibernateFiltersHolder.defaultFilters, closure
		}

		clazz.metaClass.static.withoutHibernateFilter = { String name, Closure closure ->
			Session session = ctx.sessionFactory.currentSession
			withoutHibernateFilters session, [name], closure
		}

		clazz.metaClass.static.withoutHibernateFilters = { Closure closure ->
			Session session = ctx.sessionFactory.currentSession
			withoutHibernateFilters session, DefaultHibernateFiltersHolder.defaultFilters, closure
		}

		// enable the named filter and return it (org.hibernate.Filter)
		clazz.metaClass.static.enableHibernateFilter = { String name ->
			ctx.sessionFactory.currentSession.enableFilter name
		}

		// enable the named filter (void)
		clazz.metaClass.static.disableHibernateFilter = { String name ->
			ctx.sessionFactory.currentSession.disableFilter name
		}
	}

	/**
	 * Execute the closure with the specified filter(s) enabled. Re-disable any that were disabled.
	 * @param session the Hibernate session
	 * @param names the filter name(s)
	 * @param closure the closure
	 * @return the closure's return value
	 */
	static withHibernateFilters(Session session, names, Closure closure) {
		def previouslyDisabled = [:]
		for (String name in names) {
			previouslyDisabled[name] = session.getEnabledFilter(name) == null
		}
		try {
			for (String name in names) {
				session.enableFilter name
			}
			closure.doCall()
		}
		finally {
			for (String name in names) {
				if (previouslyDisabled[name]) {
					session.disableFilter name
				}
			}
		}
	}

	/**
	 * Execute the closure with the specified filter(s) disabled. Re-enable any that were enabled.
	 * @param session the Hibernate session
	 * @param names the filter name(s)
	 * @param closure the closure
	 * @return the closure's return value
	 */
	static withoutHibernateFilters(Session session, names, Closure closure) {
		def previouslyEnabled = [:]
		for (String name in names) {
			previouslyEnabled[name] = session.getEnabledFilter(name) != null
		}
		try {
			for (String name in names) {
				session.disableFilter name
			}
			closure.doCall()
		}
		finally {
			for (String name in names) {
				if (previouslyEnabled[name]) {
					session.enableFilter name
				}
			}
		}
	}
}
