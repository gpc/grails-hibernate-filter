package org.grails.plugin.hibernate.filter

import grails.core.GrailsClass
import grails.plugins.*
import groovy.transform.CompileStatic
import org.grails.core.artefact.DomainClassArtefactHandler

@CompileStatic
class HibernateFilterGrailsPlugin extends Plugin
{

	// the version or versions of Grails the plugin is designed for
	def grailsVersion = "3.0.3 > *"
	def loadAfter = ['controllers', 'hibernate']
	def observe = ['*']
	def pluginExcludes = []

	def author = 'Scott Burch'
	def authorEmail = 'scott@bulldoginfo.com'
	def title = 'Hibernate Filter plugin'
	def description = 'Integrates Hibernate filtering into Grails 3'
	def documentation = 'http://grails.org/plugin/hibernate-filter'

	def license = 'APACHE'
	def developers = [
			[name: 'Burt Beckwith', email: 'beckwithb@vmware.com'],
			[name: 'Alex Shneyderman', email: 'a.shneyderman@gmail.com'],
			[name: 'Piotr Chowaniec', email: 'piotr.chowaniec@gmail.com']]
	def issueManagement = [system: 'JIRA', url: 'http://jira.grails.org/browse/GPHIBERNATEFILTER']
	def scm = [url: 'https://github.com/burtbeckwith/grails-hibernate-filter']
	def profiles = ['web']

	void doWithDynamicMethods()
	{
		for( GrailsClass dc in grailsApplication.getArtefacts( DomainClassArtefactHandler.TYPE ) )
		{
			HibernateFilterUtils.addDomainClassMethods dc.clazz, getApplicationContext()
		}

		for( Class artefactClass in grailsApplication.allArtefacts )
		{
			HibernateFilterUtils.addDomainProxies artefactClass
		}
	}
}
