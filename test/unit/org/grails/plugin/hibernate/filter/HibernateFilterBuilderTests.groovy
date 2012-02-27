package org.grails.plugin.hibernate.filter

import test.Foo

import grails.test.GrailsUnitTestCase

import org.codehaus.groovy.grails.commons.DefaultGrailsDomainClass
import org.codehaus.groovy.grails.commons.GrailsDomainClass
import org.hibernate.cfg.Configuration.MappingsImpl
import org.hibernate.engine.FilterDefinition
import org.hibernate.mapping.Collection
import org.hibernate.mapping.PersistentClass
import org.hibernate.mapping.RootClass
import org.hibernate.mapping.Set
import org.hibernate.type.LongType

import test.Foo

class HibernateFilterBuilderTests extends GrailsUnitTestCase {

	void testBuilder() {

		RootClass root = new RootClass()
		Set set = new Set(new MappingsImpl(), root)

		HibernateFilterDomainConfiguration configuration = new HibernateFilterDomainConfiguration() {
			PersistentClass getClassMapping(String entityName) { root }
			Collection getCollectionMapping(String role) { set }
		}

		new HibernateFilterBuilder(configuration, new DefaultGrailsDomainClass(Foo))

		assertEquals(['fooEnabledFilter', 'barEnabledFilter'], DefaultHibernateFiltersHolder.defaultFilters)

		assertEquals 1, DefaultHibernateFiltersHolder.domainAliasProxies.size()
		HibernateFilterDomainProxy proxy = DefaultHibernateFiltersHolder.domainAliasProxies[0]
		assertEquals 'fooEnabledFilter', proxy.filterName
		assertEquals 'EnabledFoo', proxy.aliasName
		assertTrue proxy.domainClassInstance instanceof Foo

		assertEquals 1, DefaultHibernateFiltersHolder.defaultFilterCallbacks.size()
		def callback = DefaultHibernateFiltersHolder.defaultFilterCallbacks['closureDefaultFilter']
		assertTrue callback instanceof Closure
		assertFalse callback()

		assertEquals 4, root.filterMap.size()
		assertEquals 'enabled=1', root.filterMap.fooEnabledFilter
		assertEquals ':name = name', root.filterMap.fooNameFilter
		assertEquals 'enabled=1', root.filterMap.closureDefaultFilter
		assertEquals '(organisation_id = :oragnisationId or organisation_id in (:organisationIds))', root.filterMap.inListFilter

		FilterDefinition fd = configuration.filterDefinitions.inListFilter
		assertNotNull fd
		assertEquals 'inListFilter', fd.filterName
		assertEquals '(organisation_id = :oragnisationId or organisation_id in (:organisationIds))', fd.defaultFilterCondition
		assertTrue fd.parameterTypes.oragnisationId instanceof LongType
		assertTrue fd.parameterTypes.organisationIds instanceof LongType

		assertEquals 1, set.filterMap.size()
		assertEquals 'enabled=1', set.filterMap.barEnabledFilter
	}

	@Override
	protected void setUp() {
		super.setUp()
		DefaultHibernateFiltersHolder.defaultFilters.clear()
		DefaultHibernateFiltersHolder.domainAliasProxies.clear()
		DefaultHibernateFiltersHolder.defaultFilterCallbacks.clear()
	}

	@Override
	protected void tearDown() {
		super.tearDown()
		DefaultHibernateFiltersHolder.defaultFilters.clear()
		DefaultHibernateFiltersHolder.domainAliasProxies.clear()
		DefaultHibernateFiltersHolder.defaultFilterCallbacks.clear()
	}
}
