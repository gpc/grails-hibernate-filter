package hibernate.filter.example


import grails.test.mixin.integration.Integration
import grails.transaction.*
import org.grails.plugin.hibernate.filter.DefaultHibernateFiltersHolder
import org.grails.core.DefaultGrailsDomainClass
import org.grails.plugin.hibernate.filter.HibernateFilterBuilder
import org.grails.plugin.hibernate.filter.HibernateFilterDomainConfiguration
import org.grails.plugin.hibernate.filter.HibernateFilterDomainProxy
import org.hibernate.cfg.Configuration
import org.hibernate.engine.spi.FilterDefinition
import org.hibernate.mapping.PersistentClass
import org.hibernate.mapping.RootClass
import org.hibernate.type.LongType
import org.hibernate.mapping.Set
import org.hibernate.mapping.Collection

import spock.lang.*

@Integration
@Rollback
class HibernateFilterBuilderSpec extends Specification {

    def testBuilder() {
        given:
        DefaultHibernateFiltersHolder.defaultFilters.clear()
        DefaultHibernateFiltersHolder.domainAliasProxies.clear()
        DefaultHibernateFiltersHolder.defaultFilterCallbacks.clear()

        RootClass root = new RootClass()
        Set set = new Set(new Configuration().createMappings(), root)

        HibernateFilterDomainConfiguration configuration = new HibernateFilterDomainConfiguration() {
            PersistentClass getClassMapping(String entityName) { root }

            Collection getCollectionMapping(String role) { set }
        }

        when:
        new HibernateFilterBuilder(configuration, new DefaultGrailsDomainClass(Foo))

        then:
        DefaultHibernateFiltersHolder.defaultFilters == ['fooEnabledFilter', 'barEnabledFilter']

        DefaultHibernateFiltersHolder.domainAliasProxies.size() == 1
        HibernateFilterDomainProxy proxy = DefaultHibernateFiltersHolder.domainAliasProxies.get(0)
        proxy.filterName == 'fooEnabledFilter'
        proxy.aliasName == 'EnabledFoo'
        proxy.domainClassInstance instanceof Foo

        DefaultHibernateFiltersHolder.defaultFilterCallbacks.size() == 1
        def callback = DefaultHibernateFiltersHolder.defaultFilterCallbacks['closureDefaultFilter']
        callback instanceof Closure
        !callback()

        FilterDefinition fd = configuration.filterDefinitions.inListFilter
        fd
        fd.filterName == 'inListFilter'
        fd.defaultFilterCondition == '(organisation_id = :oragnisationId or organisation_id in (:organisationIds))'
        fd.parameterTypes.oragnisationId instanceof LongType
        fd.parameterTypes.organisationIds instanceof LongType

        set.filters.size() == 1

        def first = set.filters.first()
        first.name == 'barEnabledFilter'
        first.condition == 'enabled=1'

        root.filters.size() == 5
        root.filters.find {
            it.name == 'fooEnabledFilter' && it.condition == 'enabled=1'
        }

        root.filters.find {
            it.name == 'fooNameFilter' && it.condition == ':name = name'
        }

        root.filters.find {
            it.name == 'closureDefaultFilter' && it.condition == 'enabled=1'
        }
        root.filters.find {
            it.name == 'inListFilter' && it.condition == '(organisation_id = :oragnisationId or organisation_id in (:organisationIds))'
        }
    }
}

