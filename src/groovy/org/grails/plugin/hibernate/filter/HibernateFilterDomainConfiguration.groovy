package org.grails.plugin.hibernate.filter

import org.codehaus.groovy.grails.commons.GrailsDomainClass
import org.codehaus.groovy.grails.orm.hibernate.cfg.GrailsAnnotationConfiguration
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.hibernate.MappingException

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

		super.secondPassCompile()

		if (configLocked) {
			return
		}

        customiseConfiguration(grailsApplication)
		configLocked = true
	}

    /**
     * Do customisations of the Hibernate configuration which should only be performed once. The default implementation
     * adds any filters defined in the domain classes, but a subclass could replace or supplement this with another
     * once-off customisation e.g. <a href="http://burtbeckwith.com/blog/?p=465">changing the foreign key constraint names</a>
     */
    protected void customiseConfiguration(GrailsApplication grailsApplication) {

        grailsApplication.domainClasses.each { GrailsDomainClass domainClass ->
            def filters = domainClass.getPropertyValue('hibernateFilters')
            if (filters instanceof Closure) {
                new HibernateFilterBuilder(this, domainClass)
            }
        }
    }
}
