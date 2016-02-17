package org.grails.plugin.hibernate.filter

import grails.test.spock.IntegrationSpec
import test.domain.Foo
import test.domain.FooSubclass
import org.hibernate.SessionFactory

class FilterSpec extends IntegrationSpec {

    SessionFactory sessionFactory

	def setup() {

        saveFoo new Foo(name:'enabledFoo', enabled: true)
        saveFoo new Foo(name:'disabledFoo', enabled: false)
        saveFoo new FooSubclass(name:'enabledSubclass', enabled: true)
        saveFoo new FooSubclass(name:'disabledFooSubclass', enabled: false)
        sessionFactory.currentSession.clear()
	}

    private saveFoo(Foo foo) {
        foo.addToBars(name: 'enabledBar', enabled: true)
        foo.addToBars(name: 'disabledBar', enabled: false)
        foo.save(failOnError: true)
    }

	void 'test default filters'() {
		int enabledCount = Foo.countByEnabled(true)

		expect:
		Foo.withHibernateFilters {
			def foos = Foo.list()
			enabledCount == foos.size()

            foos.each { foo ->
                foo.bars.every { it.enabled }
            }
		}
	}

	void 'test default filters with subclass'() {
		int enabledCount = FooSubclass.countByEnabled(true)

		expect:
		FooSubclass.withHibernateFilters {
			def subFoos = FooSubclass.list()
			enabledCount == subFoos.size()

            subFoos.each { foo ->
                foo.bars.every { it.enabled }
            }
		}
	}
}
