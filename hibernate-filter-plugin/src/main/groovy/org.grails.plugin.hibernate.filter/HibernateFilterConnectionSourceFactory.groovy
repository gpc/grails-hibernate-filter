package org.grails.plugin.hibernate.filter

import org.grails.orm.hibernate.connections.HibernateConnectionSourceFactory

/**
 * Created by akramer on 12/6/16.
 */
class HibernateFilterConnectionSourceFactory extends HibernateConnectionSourceFactory {

    public HibernateFilterConnectionSourceFactory(Class...classes) {
        super(classes)
        this.metadataContributor = new HibernateFilterBinder()
    }
}
