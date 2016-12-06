package org.grails.plugin.hibernate.filter

import grails.core.GrailsDomainClass
import grails.util.Holders
import org.hibernate.MappingException
import org.hibernate.boot.spi.InFlightMetadataCollector
import org.hibernate.cfg.SecondPass

class HibernateFilterDomainConfiguration implements SecondPass {

    protected InFlightMetadataCollector mappings

    public HibernateFilterDomainConfiguration(InFlightMetadataCollector mappings) {
        this.mappings = mappings
    }

	public void doSecondPass(Map persistentClasses) throws MappingException {
		def application = Holders.grailsApplication

		for (aClass in application.getArtefacts('Domain')) {
			def domainClass = (GrailsDomainClass) aClass
			def filters = domainClass.getPropertyValue('hibernateFilters')
			if (filters instanceof Closure) {
				new HibernateFilterBuilder(mappings, domainClass, persistentClasses.get(domainClass.fullName))
			}
		}
	}
}
