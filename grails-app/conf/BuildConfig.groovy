grails.project.work.dir = 'target'
grails.project.source.level = 1.6
grails.project.dependency.resolver = "maven"

grails.project.dependency.resolution = {

	inherits 'global'
	log 'warn'

	repositories {
        grailsPlugins()
        grailsHome()
        mavenLocal()
        grailsCentral()
        mavenCentral()
	}

	dependencies {}

	plugins {
        compile ":hibernate4:4.3.10"

        build(':release:3.0.1', ':rest-client-builder:2.0.3') {
			export = false
		}
	}
}
