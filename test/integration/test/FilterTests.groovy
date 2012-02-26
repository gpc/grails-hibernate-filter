package test

class FilterTests extends GroovyTestCase {

	def sessionFactory

	protected void setUp() {
		super.setUp()

		Foo enabledFoo = new Foo(name:'enabledFoo', enabled: true)
		enabledFoo.addToBars new Bar(name:'enabledBar', enabled: true)
		enabledFoo.addToBars new Bar(name:'diabledBar', enabled: false)
		enabledFoo.save()

		Foo disabledFoo = new Foo(name:'disabledFoo', enabled: true)
		disabledFoo.addToBars new Bar(name:'enabled_bar', enabled: true)
		disabledFoo.addToBars new Bar(name:'disabled_bar', enabled: false)
		disabledFoo.save()

		sessionFactory.currentSession.flush()
		sessionFactory.currentSession.clear()
	}

	void testDefaultFilters() {
		int enabledCount = Foo.countByEnabled(true)

		Foo.withHibernateFilters {
			def foos = Foo.list()
			assertEquals enabledCount, foos.size()

			for (Foo foo in foos) {
				assertTrue foo.enabled
				for (Bar bar in foo.bars) {
					assertTrue bar.enabled
				}
			}
		}
	}
}
