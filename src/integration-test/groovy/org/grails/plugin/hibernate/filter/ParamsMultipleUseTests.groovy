package org.grails.plugin.hibernate.filter

import grails.test.mixin.integration.Integration
import org.hibernate.Filter
import spock.lang.Ignore
import spock.lang.Specification
import test.Foo

//FIXME
@Ignore
@Integration
class ParamsMultipleUseTests extends Specification {

	def sessionFactory

	void setup() {

		new Foo(name:'foo0', enabled: true).save()
		new Foo(name:'foo1', enabled: true).save()
		new Foo(name:'foo2', enabled: true).save()

		sessionFactory.currentSession.flush()
		sessionFactory.currentSession.clear()
	}

    void testFilterWithMultiUseParams() {
        given:
        def foos = Foo.list()

        when:
        Filter filter = Foo.enableHibernateFilter('multipleUseParamFilter')  // id > :avoid or id < :avoid
        filter.setParameter('avoid',foos[0].id)

        def foos_filtered = Foo.list()

        then:
        foos.size() - 1 == foos_filtered.size()
    }

}
