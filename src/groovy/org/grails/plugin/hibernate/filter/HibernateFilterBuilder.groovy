package org.grails.plugin.hibernate.filter

import grails.util.Metadata

import org.codehaus.groovy.grails.commons.GrailsDomainClass
import org.hibernate.cfg.Mappings
import org.hibernate.engine.spi.FilterDefinition
import org.hibernate.type.TypeFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Add the filters from the domain closure.
 */
class HibernateFilterBuilder {

	private Logger log = LoggerFactory.getLogger(getClass())
	private boolean grails1 = Metadata.current.getGrailsVersion().startsWith('1')
	private Mappings mappings

	HibernateFilterDomainConfiguration configuration
	GrailsDomainClass domainClass

	HibernateFilterBuilder(HibernateFilterDomainConfiguration configuration, GrailsDomainClass domainClass) {
		this.configuration = configuration
		this.domainClass = domainClass
		mappings = configuration.createMappings()

		Closure filtersClosure = domainClass.getPropertyValue('hibernateFilters')
		filtersClosure.delegate = this
		filtersClosure.resolveStrategy = Closure.DELEGATE_ONLY
		filtersClosure()
	}

	def methodMissing(String name, args) {
		args = [name] + args.collect { it }
		def filterMethod = metaClass.getMetaMethod('addFilter', args.collect { it.getClass() } as Object[])
		if (filterMethod) {
			return filterMethod.invoke(this, args as Object[])
		}

		throw new HibernateFilterException(
			"Invalid arguments in hibernateFilters closure [class:$domainClass.name, name:$name]")
	}

	// Add a previously registered filter
	private void addFilter(String name, Map options = [:]) {
		// Use supplied condition if there is one, otherwise take the condition
		// that is already part of the named filter
		String condition = options.condition ?:
			configuration.filterDefinitions[name].defaultFilterCondition

		// for condition with parameter
		String[] paramTypes = (options.types ?: options.paramTypes ?: '').tokenize(',') as String[]

		// Don't add a filter definition twice - if it is not added already, create the filter
		if (!configuration.getFilterDefinitions().get(name)) {
			def paramsMap = [:]
			int counter = 0
			def matcher = condition =~ /:(\w+)/
			matcher.each { match ->
				String paramName = match[1]
				if (!paramsMap.get(paramName)) {
					String typeName = paramTypes[counter++].trim()
					def type = grails1 ? TypeFactory.basic(typeName) : mappings.getTypeResolver().basic(typeName)
					paramsMap[paramName.trim()] = type
				}
			}
			configuration.addFilterDefinition new FilterDefinition(name, condition, paramsMap)
		}

		// If this is a collection, add the filter to the collection,
		// else add the condition to the base class
		def entity = options.collection ?
			configuration.getCollectionMapping("${domainClass.fullName}.$options.collection") :
			configuration.getClassMapping(domainClass.fullName)

		if (entity == null) {
			if (options.collection && !domainClass.isRoot()) {
				def clazz = domainClass.clazz.superclass
				while (clazz != Object && !entity) {
					entity = configuration.getCollectionMapping("${clazz.name}.$options.collection")
				}
				if (!entity) {
					log.warn "Collection $options.collection not found in $domainClass.fullName or any superclass"
					return
				}
			}
			else {
				log.warn "Entity not found for filter definition $options"
				return
			}
		}

		// now add the filter to the class or collection
		entity.addFilter name, condition, true, null, null

		// TODO: may be able to refactor this so that the factory creates the
		// session with the filters rather than enabling them on each request
		if (options.default) {
			if (options.default instanceof Closure) {
				DefaultHibernateFiltersHolder.addDefaultFilterCallback name, options.default
			}
			else {
				DefaultHibernateFiltersHolder.addDefaultFilter name
			}
		}

		// store any domain alias proxies to be injected later
		if (options.aliasDomain && domainClass.isRoot()) {
			DefaultHibernateFiltersHolder.addDomainAliasProxy(
				new HibernateFilterDomainProxy(domainClass.reference.wrappedInstance, options.aliasDomain, name))
		}
	}
}
