package org.grails.plugin.hibernate.filter

import org.hibernate.boot.spi.InFlightMetadataCollector
import org.hibernate.boot.spi.MetadataContributor
import org.jboss.jandex.IndexView

/**
 * Created by akramer on 12/6/16.
 */
class HibernateFilterBinder implements MetadataContributor {

    /**
     * Perform the contributions.
     *
     * @param metadataCollector The metadata collector, representing the in-flight metadata being built
     * @param jandexIndex The Jandex index
     */
    public void contribute(InFlightMetadataCollector metadataCollector, IndexView jandexIndex) {
        metadataCollector.addSecondPass(new HibernateFilterDomainConfiguration(metadataCollector))
    }
}
