import org.codehaus.groovy.grails.commons.ApplicationHolder

class HibernateFilterGrailsPlugin {
    // the plugin version
    def version = "0.1"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.1.1 > *"
    // the other plugins this plugin depends on
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    def dependsOn = [controllers: '1.1.1 > *', hibernate: '1.1.1 > *']

    def loadAfter = ['controllers']
    def observe = ['controllers']

    // TODO Fill in these fields
    def author = "Scott Burch"
    def authorEmail = "scott@bulldoginfo.com"
    def title = "Integrates Hibernate filtering into Grails"
    def description = '''\\
Integrates Hibernate filtering into Grails
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/HibernateFilter+Plugin"

    def doWithSpring = {
        // TODO Implement runtime spring config (optional)
    }

    def doWithApplicationContext = {applicationContext ->
        // TODO Implement post initialization spring config (optional)
    }

    def doWithWebDescriptor = {xml ->
        // TODO Implement additions to web.xml (optional)
    }

    def doWithDynamicMethods = {ctx ->
        application.allArtefacts.each {artefact ->
            addMethods(artefact, ctx)
        }
    }

    def onChange = {event ->
        def clazz = application.getControllerClass(event.source?.name)
        addMethods(clazz, application.mainContext)
    }

    def onConfigChange = {event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }

    def addMethods(clazz, ctx) {

        clazz.metaClass.withHibernateFilter = {name, closure ->
            def session = ctx.sessionFactory.currentSession
            def isFilterDisabled = session.getEnabledFilter(name) == null
            try {
                session.enableFilter(name)
                closure.doCall()
            } catch (e) {
                throw e
            } finally {
                if(isFilterDisabled) {
                    session.disableFilter(name)
                }
            }
        }

        clazz.metaClass.withoutHibernateFilter = {name, closure ->
            def session = ctx.sessionFactory.currentSession
            def isFilterEnabled = session.getEnabledFilter(name) != null
            try {
                session.disableFilter(name)
                closure.doCall()
            } catch (e) {
                throw e
            } finally {
                if (isFilterEnabled) {
                    session.enableFilter(name)
                }
            }
        }

        clazz.metaClass.enableHibernateFilter = {name ->
            ctx.sessionFactory.currentSession.enableFilter(name)
        }

        clazz.metaClass.disableHibernateFilter = {name ->
            ctx.sessionFactory.currentSession.disableFilter(name)
        }
    }
}

