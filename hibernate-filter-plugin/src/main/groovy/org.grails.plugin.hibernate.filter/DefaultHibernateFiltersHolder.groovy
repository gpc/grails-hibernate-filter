package org.grails.plugin.hibernate.filter
import groovy.transform.CompileStatic

/**
 * Used by HibernateFilterBuilder to store information from hibernateFilters blocks.
 */
@CompileStatic
class DefaultHibernateFiltersHolder {

	static final List<String> defaultFilters = []
	static final List<HibernateFilterDomainProxy> domainAliasProxies = []
	static final Map<String, Closure> defaultFilterCallbacks = [:]

	static void addDefaultFilter(String name) {
		defaultFilters << name
	}

	static void addDomainAliasProxy(HibernateFilterDomainProxy proxy) {
		domainAliasProxies << proxy
	}

	static void addDefaultFilterCallback(String name, Closure closure) {
		defaultFilterCallbacks[name] = closure
	}
}
