package org.grails.plugin.hibernate.filter

import org.codehaus.groovy.grails.orm.hibernate.cfg.DefaultGrailsDomainConfiguration
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.hibernate.MappingException
import org.codehaus.groovy.grails.commons.GrailsClass
import org.hibernate.engine.FilterDefinition
import org.codehaus.groovy.grails.commons.DomainClassArtefactHandler


class HibernateFilterDomainConfiguration extends DefaultGrailsDomainConfiguration {
    private GrailsApplication grailsApplication
    private boolean configLocked;

    public void setGrailsApplication(GrailsApplication grailsApplication) {
        super.setGrailsApplication(grailsApplication);
        this.grailsApplication = grailsApplication;
    }

    protected void secondPassCompile() throws MappingException {
        if (configLocked) {
            return;
        }

        super.secondPassCompile();

        grailsApplication.domainClasses.each {domainClass ->

            Closure filters = domainClass.getPropertyValue("hibernateFilters");
            if(filters) {
                new HibernateFilterBuilder(this, domainClass)
            }
        }
        this.configLocked = true;
    }
}