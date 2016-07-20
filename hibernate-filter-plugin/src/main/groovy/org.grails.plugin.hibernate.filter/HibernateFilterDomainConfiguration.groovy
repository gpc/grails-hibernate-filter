package org.grails.plugin.hibernate.filter

import grails.core.GrailsDomainClass
import grails.util.Holders
import org.grails.orm.hibernate.cfg.HibernateMappingContextConfiguration
import org.hibernate.MappingException

class HibernateFilterDomainConfiguration extends HibernateMappingContextConfiguration {
	private boolean configLocked

	@Override
	protected void secondPassCompile() throws MappingException {
		if (configLocked) {
			return
		}

		super.secondPassCompile()

        def application = Holders.grailsApplication

        for (aClass in application.getArtefacts('Domain')) {
            def domainClass = (GrailsDomainClass) aClass
            def filters = domainClass.getPropertyValue('hibernateFilters')
			if (filters instanceof Closure) {
				new HibernateFilterBuilder(this, domainClass)
			}
		}

		configLocked = true
	}
}
