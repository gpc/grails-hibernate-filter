package test

import grails.test.spock.IntegrationSpec

class FilterSpec extends IntegrationSpec {

	def setup() {

        saveFoo new Foo(name:'enabledFoo', enabled: true)
        saveFoo new Foo(name:'disabledFoo', enabled: false)
        saveFoo new FooSubclass(name:'enabledSubclass', enabled: true, wahoo: 'wahoo1')
        saveFoo new FooSubclass(name:'disabledFooSubclass', enabled: false, wahoo: 'wahoo2')
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
			assert enabledCount == foos.size()

			for (Foo foo in foos) {
				assert foo.enabled
				for (Bar bar in foo.bars) {
					assert bar.enabled
				}
			}
		}
	}

	void 'test default filters with subclass'() {
		int enabledCount = FooSubclass.countByEnabled(true)

		expect:
		FooSubclass.withHibernateFilters {
			def foos = FooSubclass.list()
			assert enabledCount == foos.size()

			for (FooSubclass foo in foos) {
				assert foo.enabled
				for (Bar bar in foo.bars) {
					assert bar.enabled
				}
			}
		}
	}
}
