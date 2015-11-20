package org.grails.plugin.hibernate.filter

import grails.test.mixin.integration.Integration
import grails.transaction.Rollback
import spock.lang.Ignore
import spock.lang.Specification
import test.Bar
import test.Foo
import test.Foo2

//FIXME
@Ignore
@Integration
class FilterSpec extends Specification {

	def sessionFactory

	def setup() {
		Foo enabledFoo = new Foo(name:'enabledFoo', enabled: true)
		enabledFoo.getBars().add new Bar(name:'enabledBar', enabled: true)
		enabledFoo.getBars().add new Bar(name:'diabledBar', enabled: false)
		enabledFoo.save()

		Foo disabledFoo = new Foo(name:'disabledFoo', enabled: true)
		disabledFoo.addToBars new Bar(name:'enabled_bar', enabled: true)
		disabledFoo.addToBars new Bar(name:'disabled_bar', enabled: false)
		disabledFoo.save()

		Foo2 enabledFoo2 = new Foo2(name:'enabledFoo2', enabled: true, wahoo: 'wahoo1')
		enabledFoo2.addToBars new Bar(name:'enabledBar', enabled: true)
		enabledFoo2.addToBars new Bar(name:'diabledBar', enabled: false)
		enabledFoo2.save()

		Foo disabledFoo2 = new Foo2(name:'disabledFoo2', enabled: true, wahoo: 'wahoo2')
		disabledFoo2.addToBars new Bar(name:'enabled_bar', enabled: true)
		disabledFoo2.addToBars new Bar(name:'disabled_bar', enabled: false)
		disabledFoo2.save()

		sessionFactory.currentSession.flush()
		sessionFactory.currentSession.clear()
	}

	def testDefaultFilters() {
		expect:
		int enabledCount = Foo.countByEnabled(true)

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

	void testDefaultFiltersWithSubclass() {
		int enabledCount = Foo2.countByEnabled(true)

		Foo2.withHibernateFilters {
			def foos = Foo2.list()
			assertEquals enabledCount, foos.size()

			for (Foo2 foo in foos) {
				assertTrue foo.enabled
				for (Bar bar in foo.bars) {
					assertTrue bar.enabled
				}
			}
		}
	}
}
