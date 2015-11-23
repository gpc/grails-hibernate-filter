//package org.grails.plugin.hibernate.filter
//
//import org.grails.core.DefaultGrailsDomainClass
//import org.hibernate.cfg.Configuration
//import org.hibernate.engine.FilterDefinition
//import org.hibernate.mapping.PersistentClass
//import org.hibernate.mapping.RootClass
//import org.hibernate.type.LongType
//import org.hibernate.mapping.Set
//import org.hibernate.mapping.Collection
//
//import test.*
//import spock.lang.Specification
//
//class HibernateFilterBuilderSpec extends Specification {
//
//	def testBuilder() {
//		given:
//		DefaultHibernateFiltersHolder.defaultFilters.clear()
//		DefaultHibernateFiltersHolder.domainAliasProxies.clear()
//		DefaultHibernateFiltersHolder.defaultFilterCallbacks.clear()
//
//		RootClass root = new RootClass()
//		Set set = new Set(new Configuration().createMappings(), root)
//
//		HibernateFilterDomainConfiguration configuration = new HibernateFilterDomainConfiguration() {
//			PersistentClass getClassMapping(String entityName) { root }
//			Collection getCollectionMapping(String role) { set }
//		}
//
//		when:
//		new HibernateFilterBuilder(configuration, new DefaultGrailsDomainClass(Foo))
//
//		then:
//		DefaultHibernateFiltersHolder.defaultFilters == ['fooEnabledFilter', 'barEnabledFilter']
//
//		DefaultHibernateFiltersHolder.domainAliasProxies.size() == 1
//		HibernateFilterDomainProxy proxy = DefaultHibernateFiltersHolder.domainAliasProxies.get( 0 )
//		proxy.filterName == 'fooEnabledFilter'
//		proxy.aliasName == 'EnabledFoo'
//		proxy.domainClassInstance instanceof Foo
//
//		DefaultHibernateFiltersHolder.defaultFilterCallbacks.size() == 1
//		def callback = DefaultHibernateFiltersHolder.defaultFilterCallbacks['closureDefaultFilter']
//		callback instanceof Closure
//		!callback()
//
//		FilterDefinition fd = configuration.filterDefinitions.inListFilter
//		fd
//		fd.filterName == 'inListFilter'
//		fd.defaultFilterCondition == '(organisation_id = :oragnisationId or organisation_id in (:organisationIds))'
//		fd.parameterTypes.oragnisationId instanceof LongType
//		fd.parameterTypes.organisationIds instanceof LongType
//
//		set.filterMap.size() == 1
//		set.filterMap.barEnabledFilter == 'enabled=1'
//
//		root.filterMap.size() == 5
//		'enabled=1' == root.filterMap.fooEnabledFilter
//		':name = name' == root.filterMap.fooNameFilter
//		'enabled=1' == root.filterMap.closureDefaultFilter
//		'(organisation_id = :oragnisationId or organisation_id in (:organisationIds))' == root.filterMap.inListFilter
//	}
//}
