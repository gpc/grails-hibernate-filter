package org.grails.plugin.hibernate.filter

import grails.test.spock.IntegrationSpec
import test.domain.Foo
import org.hibernate.Filter

class ParamsMultipleUseSpec extends IntegrationSpec {

    def setup() {
        3.times {
            new Foo(name: it, enabled: true).save()
        }
    }

    void testFilterWithMultiUseParams() {
        def foos = Foo.list()

        when:
        Filter filter = Foo.enableHibernateFilter('multipleUseParamFilter')  // id > :avoid or id < :avoid
        filter.setParameter('avoid', foos[0].id)

        then:
        foos.size() - 1 == Foo.count
    }
}
