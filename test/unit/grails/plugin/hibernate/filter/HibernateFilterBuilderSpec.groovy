package grails.plugin.hibernate.filter

import org.codehaus.groovy.grails.commons.DefaultGrailsDomainClass
import test.domain.Foo
import org.hibernate.cfg.Configuration.MappingsImpl
import org.hibernate.engine.spi.FilterDefinition
import org.hibernate.internal.FilterConfiguration
import org.hibernate.mapping.Collection
import org.hibernate.mapping.PersistentClass
import org.hibernate.mapping.RootClass
import org.hibernate.mapping.Set
import org.hibernate.type.LongType
import spock.lang.Specification

class HibernateFilterBuilderSpec extends Specification {

    def setup() {
        DefaultHibernateFiltersHolder.defaultFilters.clear()
        DefaultHibernateFiltersHolder.domainAliasProxies.clear()
        DefaultHibernateFiltersHolder.defaultFilterCallbacks.clear()
    }

    def cleanup() {
        DefaultHibernateFiltersHolder.defaultFilters.clear()
        DefaultHibernateFiltersHolder.domainAliasProxies.clear()
        DefaultHibernateFiltersHolder.defaultFilterCallbacks.clear()
    }

	void "test HibernateFilterBuilder"() {

        given:
		RootClass root = new RootClass()
		Set association = new Set(new MappingsImpl(), root)

		HibernateFilterDomainConfiguration configuration = new HibernateFilterDomainConfiguration() {
			PersistentClass getClassMapping(String entityName) { root }
			Collection getCollectionMapping(String role) { association }
		}

        when:
		new HibernateFilterBuilder(configuration, new DefaultGrailsDomainClass(Foo))

        then:
		['fooEnabledFilter', 'barEnabledFilter'] == DefaultHibernateFiltersHolder.defaultFilters
		1 == DefaultHibernateFiltersHolder.domainAliasProxies.size()

        when:
		HibernateFilterDomainProxy proxy = DefaultHibernateFiltersHolder.domainAliasProxies[0]

        then:
		'fooEnabledFilter' == proxy.filterName
		'EnabledFoo' == proxy.aliasName
		proxy.domainClassInstance instanceof Foo
		1 == DefaultHibernateFiltersHolder.defaultFilterCallbacks.size()

        when:
		def callback = DefaultHibernateFiltersHolder.defaultFilterCallbacks['closureDefaultFilter']

        then:
		callback instanceof Closure
		!callback()

        when:
        List<FilterConfiguration> filters = root.filters

        then:
        5 == filters.size()
        'enabled=1' == findFilterCondition(filters, 'fooEnabledFilter')
        ':name = name' == findFilterCondition(filters, 'fooNameFilter')
        'enabled=1' == findFilterCondition(filters, 'closureDefaultFilter')
        '(organisation_id = :organisationId or organisation_id in (:organisationIds))' == findFilterCondition(filters, 'inListFilter')

        when:
		FilterDefinition fd = configuration.filterDefinitions.inListFilter

        then: 'test filters defined for root domain class'
		'inListFilter' == fd.filterName
		'(organisation_id = :organisationId or organisation_id in (:organisationIds))' == fd.defaultFilterCondition
		fd.parameterTypes.organisationId instanceof LongType
		fd.parameterTypes.organisationIds instanceof LongType

        when: 'test filters defined for association of root domain class'
        List<FilterConfiguration> associationFilters = association.filters

        then:
		1 == associationFilters.size()
		'enabled=1' == findFilterCondition(associationFilters, 'barEnabledFilter')
	}

    private String findFilterCondition(List<FilterConfiguration> filters, String filterName) {
        filters.find { it.name == filterName }.condition
    }
}
