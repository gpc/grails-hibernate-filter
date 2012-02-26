package org.grails.plugin.hibernate.filter

import org.codehaus.groovy.grails.orm.hibernate.cfg.GrailsAnnotationConfiguration
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.hibernate.MappingException
import org.hibernate.engine.FilterDefinition

class HibernateFilterDomainConfiguration extends GrailsAnnotationConfiguration {

	private GrailsApplication grailsApplication
	private boolean configLocked

	@Override
	void setGrailsApplication(GrailsApplication grailsApplication) {
		super.setGrailsApplication grailsApplication
		this.grailsApplication = grailsApplication
	}

	@Override
	protected void secondPassCompile() throws MappingException {
		if (configLocked) {
			return
		}

		super.secondPassCompile()

		for (domainClass in grailsApplication.domainClasses) {
			def filters = domainClass.getPropertyValue('hibernateFilters')
			if (filters instanceof Closure) {
				new HibernateFilterBuilder(this, domainClass)
			}
		}

		configLocked = true
	}
}
