package org.grails.plugin.hibernate.filter

import groovy.transform.CompileStatic
import org.grails.orm.hibernate.connections.HibernateConnectionSourceFactory
import org.hibernate.boot.spi.InFlightMetadataCollector
import org.hibernate.boot.spi.MetadataContributor
import org.jboss.jandex.IndexView

/**
 * Created by akramer on 12/6/16.
 */
@CompileStatic
class HibernateFilterBinder implements MetadataContributor {

    HibernateConnectionSourceFactory hibernateConnectionSourceFactory

    /**
     * Perform the contributions.
     *
     * @param metadataCollector The metadata collector, representing the in-flight metadata being built
     * @param jandexIndex The Jandex index
     */
    void contribute(InFlightMetadataCollector metadataCollector, IndexView jandexIndex) {
        metadataCollector.addSecondPass(new HibernateFilterSecondPass(metadataCollector, hibernateConnectionSourceFactory.mappingContext))
    }
}