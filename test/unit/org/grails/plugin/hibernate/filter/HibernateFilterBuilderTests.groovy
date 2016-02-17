package org.grails.plugin.hibernate.filter

import org.hibernate.internal.FilterConfiguration
import grails.test.GrailsUnitTestCase

import org.codehaus.groovy.grails.commons.DefaultGrailsDomainClass
import org.hibernate.cfg.Configuration.MappingsImpl
import org.hibernate.engine.spi.FilterDefinition
import org.hibernate.mapping.Collection
import org.hibernate.mapping.PersistentClass
import org.hibernate.mapping.RootClass
import org.hibernate.mapping.Set
import org.hibernate.type.LongType

import test.Foo

class HibernateFilterBuilderTests extends GrailsUnitTestCase {

	void testBuilder() {

		RootClass root = new RootClass()
		Set association = new Set(new MappingsImpl(), root)

		HibernateFilterDomainConfiguration configuration = new HibernateFilterDomainConfiguration() {
			PersistentClass getClassMapping(String entityName) { root }
			Collection getCollectionMapping(String role) { association }
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

        List<FilterConfiguration> filters = root.filters
        assertEquals 5, filters.size()
        assertEquals 'enabled=1', findFilterCondition(filters, 'fooEnabledFilter')
        assertEquals ':name = name', findFilterCondition(filters, 'fooNameFilter')
        assertEquals 'enabled=1', findFilterCondition(filters, 'closureDefaultFilter')
        assertEquals '(organisation_id = :oragnisationId or organisation_id in (:organisationIds))', findFilterCondition(filters, 'inListFilter')

		FilterDefinition fd = configuration.filterDefinitions.inListFilter
		assertNotNull fd
		assertEquals 'inListFilter', fd.filterName
		assertEquals '(organisation_id = :oragnisationId or organisation_id in (:organisationIds))', fd.defaultFilterCondition
		assertTrue fd.parameterTypes.oragnisationId instanceof LongType
		assertTrue fd.parameterTypes.organisationIds instanceof LongType

        List<FilterConfiguration> associationFilters = association.filters
		assertEquals 1, associationFilters.size()
		assertEquals 'enabled=1', findFilterCondition(associationFilters, 'barEnabledFilter')
	}

    private String findFilterCondition(List<FilterConfiguration> filters, String filterName) {
        filters.find { it.name == filterName }.condition
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
