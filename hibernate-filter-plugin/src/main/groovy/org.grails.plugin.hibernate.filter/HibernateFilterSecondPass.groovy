package org.grails.plugin.hibernate.filter

import grails.util.GrailsClassUtils
import groovy.transform.CompileStatic
import org.grails.datastore.mapping.model.PersistentEntity
import org.grails.orm.hibernate.cfg.HibernateMappingContext
import org.hibernate.MappingException
import org.hibernate.boot.spi.InFlightMetadataCollector
import org.hibernate.cfg.SecondPass
import org.hibernate.mapping.PersistentClass

@CompileStatic
class HibernateFilterSecondPass implements SecondPass {

    HibernateMappingContext mappingContext
    protected InFlightMetadataCollector mappings

    HibernateFilterSecondPass(InFlightMetadataCollector mappings, HibernateMappingContext mappingContext) {
        this.mappings = mappings
        this.mappingContext = mappingContext
    }

    void doSecondPass(Map persistentClasses) throws MappingException {
        mappingContext.persistentEntities.each { PersistentEntity persistentEntity ->
            //Grails way only we can get static closure. Will see what could be other way.
            // https://stackoverflow.com/questions/51086626/grails-upgrade-to-3-3-x-cannot-access-persistententity-relationshipmap-and-per
            if (GrailsClassUtils.getStaticFieldValue(persistentEntity.javaClass, "hibernateFilters")) {
                new HibernateFilterBuilder(
                        mappings,
                        persistentEntity,
                        (PersistentClass) persistentClasses.get(persistentEntity.name)
                )
            }
        }
    }
}