package test

import grails.test.spock.IntegrationSpec

class FilterSpec extends IntegrationSpec {

	def setup() {

		Foo enabledFoo = new Foo(name:'enabledFoo', enabled: true)
		enabledFoo.addToBars new Bar(name:'enabledBar', enabled: true)
		enabledFoo.addToBars new Bar(name:'disabledBar', enabled: false)
		enabledFoo.save()

		Foo disabledFoo = new Foo(name:'disabledFoo', enabled: true)
		disabledFoo.addToBars new Bar(name:'enabled_bar', enabled: true)
		disabledFoo.addToBars new Bar(name:'disabled_bar', enabled: false)
		disabledFoo.save()

		Foo2 enabledFoo2 = new Foo2(name:'enabledFoo2', enabled: true, wahoo: 'wahoo1')
		enabledFoo2.addToBars new Bar(name:'enabledBar', enabled: true)
		enabledFoo2.addToBars new Bar(name:'disabledBar', enabled: false)
		enabledFoo2.save()

		Foo2 disabledFoo2 = new Foo2(name:'disabledFoo2', enabled: true, wahoo: 'wahoo2')
        disabledFoo2.addToBars new Bar(name:'enabled_bar', enabled: true)
        disabledFoo2.addToBars new Bar(name:'disabled_bar', enabled: false)
        disabledFoo2.save()
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
		int enabledCount = Foo2.countByEnabled(true)

		expect:
		Foo2.withHibernateFilters {
			def foos = Foo2.list()
			assert enabledCount == foos.size()

			for (Foo2 foo in foos) {
				assert foo.enabled
				for (Bar bar in foo.bars) {
					assert bar.enabled
				}
			}
		}
	}
}
