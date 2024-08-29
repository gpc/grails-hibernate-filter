package org.grails.plugin.hibernate.filter

import grails.util.GrailsClassUtils
import org.grails.datastore.mapping.model.PersistentEntity
import org.hibernate.boot.spi.InFlightMetadataCollector
import org.hibernate.engine.spi.FilterDefinition
import org.hibernate.mapping.PersistentClass
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.hibernate.mapping.Collection

/**
 * Add the filters from the domain closure.
 */
class HibernateFilterBuilder {

    private Logger log = LoggerFactory.getLogger(getClass())

    InFlightMetadataCollector mappings
    PersistentEntity persistentEntity
    PersistentClass persistentClass

    HibernateFilterBuilder(InFlightMetadataCollector mappings, PersistentEntity persistentEntity, PersistentClass persistentClass) {
        this.persistentEntity = persistentEntity
        this.mappings = mappings
        this.persistentClass = persistentClass

        Closure filtersClosure = GrailsClassUtils.getStaticFieldValue(persistentEntity.javaClass, "hibernateFilters") as Closure
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
                "Invalid arguments in hibernateFilters closure [class:$persistentEntity.name, name:$name]")
    }

    // Add a previously registered filter
    private void addFilter(String name, Map options = [:]) {
        // Use supplied condition if there is one, otherwise take the condition
        // that is already part of the named filter
        String condition = options.condition ?:
                mappings.filterDefinitions[name].defaultFilterCondition

        // for condition with parameter
        String[] paramTypes = (options.types ?: options.paramTypes ?: '').tokenize(',') as String[]

        // Don't add a filter definition twice - if it is not added already, create the filter
        if (!mappings.getFilterDefinitions().get(name)) {
            Map paramsMap = [:]
            int counter = 0
            def matcher = condition =~ /:(\w+)/
            matcher.each { match ->
                String paramName = match[1]
                if (!paramsMap.get(paramName)) {
                    String typeName = paramTypes[counter++].trim()
                    //TODO need to check as method getting depreciated. it works for conditions as colon.
                    def type = mappings.getTypeResolver().basic(typeName)
                    paramsMap[paramName.trim()] = type
                }
            }
            mappings.addFilterDefinition new FilterDefinition(name, condition, paramsMap)
        }

        // If this is a collection, add the filter to the collection,
        // else add the condition to the base class
        def entity = options.collection ?
                mappings.getCollectionBinding("${persistentEntity.name}.$options.collection") :
                persistentClass

        if (entity == null) {
            if (options.collection && !persistentEntity.isRoot()) {
                def clazz = persistentEntity.parentEntity
                while (clazz != Object && !entity) {
                    entity = mappings.getCollectionBinding("${clazz.name}.$options.collection")
                }
                if (!entity) {
                    log.warn "Collection $options.collection not found in $persistentEntity.name or any superclass"
                    return
                }
            } else {
                log.warn "Entity not found for filter definition $options"
                return
            }
        }

        def filterArgs = [name, condition, true, [:], [:]]
        if (entity instanceof Collection && options.joinTable) {
            entity.addManyToManyFilter(*filterArgs)
        } else {
            entity.addFilter(*filterArgs)
        }

        if (options.default) {
            if (options.default instanceof Closure) {
                DefaultHibernateFiltersHolder.addDefaultFilterCallback name, options.default
            } else {
                DefaultHibernateFiltersHolder.addDefaultFilter name
            }
        }

        // store any domain alias proxies to be injected later
        if (options.aliasDomain && persistentEntity.isRoot()) {
            DefaultHibernateFiltersHolder.addDomainAliasProxy(
                    new HibernateFilterDomainProxy(persistentEntity.newInstance(), options.aliasDomain, name))
        }
    }
}