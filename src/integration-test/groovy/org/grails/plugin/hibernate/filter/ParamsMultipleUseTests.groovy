package org.grails.plugin.hibernate.filter

import org.hibernate.Filter
import test.Foo

//FIXME
class ParamsMultipleUseTests extends GroovyTestCase {

	def sessionFactory

	void setUp() {

		new Foo(name:'foo0', enabled: true).save()
		new Foo(name:'foo1', enabled: true).save()
		new Foo(name:'foo2', enabled: true).save()

		sessionFactory.currentSession.flush()
		sessionFactory.currentSession.clear()
	}

    void testFilterWithMultiUseParams() {
        def foos = Foo.list()

        Filter filter = Foo.enableHibernateFilter('multipleUseParamFilter')  // id > :avoid or id < :avoid
        filter.setParameter('avoid',foos[0].id)

        def foos_filtered = Foo.list()
		assertEquals(foos.size() - 1, foos_filtered.size())
    }

}
