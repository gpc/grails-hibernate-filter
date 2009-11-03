package org.grails.plugin.hibernate.filter

import org.codehaus.groovy.grails.commons.GrailsClass
import org.hibernate.engine.FilterDefinition
import org.codehaus.groovy.grails.commons.ApplicationHolder as AH


/**
 * Add the filters from the domain closure
 */

public class HibernateFilterBuilder {

    HibernateFilterDomainConfiguration configuration
    GrailsClass domainClass

    public HibernateFilterBuilder(HibernateFilterDomainConfiguration configuration, GrailsClass domainClass) {
        this.configuration = configuration
        this.domainClass = domainClass
        Closure filtersClosure = domainClass.getPropertyValue('hibernateFilters')
        filtersClosure.delegate = this
        filtersClosure.setResolveStrategy(Closure.DELEGATE_ONLY)
        filtersClosure.doCall()
    }

    def methodMissing(String name, args) {
        args = [name] + args.collect{it}
        def filterMethod = this.metaClass.getMetaMethod('addFilter', args.collect{it.getClass()} as Object[])
        if(!filterMethod) {
            throw new HibernateFilterException("Invalid arguments in hibernateFilters closure [class:${domainClass.name}, name:${name}]")
        } else {
            filterMethod.invoke(this, args as Object[])
        }
    }

    // Add a previously registered filter
    private void addFilter(String name) {
        addFilter(name, [:])
    }

    private void addFilter(String name, Map options) {
        // Use supplied condition if there is one, otherwise take the condition that is already part of the named filter
        def condition = options.condition ? options.condition : configuration.getFilterDefinitions().get(name).getDefaultFilterCondition();


        // Don't add a filter definition twice - if it is not added already, create the filter
        if(!configuration.getFilterDefinitions().get(name)) {
            configuration.addFilterDefinition(new FilterDefinition(name, condition, [:]))
        }

        // If this is a collection, add the filter to the collection, else add the condition to the base class
        def entity = options.collection ? configuration.getCollectionMapping("${domainClass.name}.${options.collection}") : configuration.getClassMapping(domainClass.getName())

        // now add the filter to the class or collection
        entity.addFilter(name, condition);


        // TODO: may be able to refactor this so that the factory creates the session with the filters rather than enabling them on each request
        if(options.default) {
            DefaultHibernateFiltersHolder.addDefaultFilter(name)
        }

        // store any domain alias proxies to be injected later
        if(options.aliasDomain) {
            DefaultHibernateFiltersHolder.addDomainAliasProxy(new HibernateFilterDomainProxy(domainClass, options.aliasDomain, name))
        }
    }
}