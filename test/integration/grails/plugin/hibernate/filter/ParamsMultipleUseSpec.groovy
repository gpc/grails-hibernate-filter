package grails.plugin.hibernate.filter

import grails.test.spock.IntegrationSpec
import test.domain.Foo
import org.hibernate.Filter

class ParamsMultipleUseSpec extends IntegrationSpec {

    def setup() {
        new Foo(name: 'foo0', enabled: true).save()
        new Foo(name: 'foo1', enabled: true).save()
        new Foo(name: 'foo2', enabled: true).save()
    }

    void testFilterWithMultiUseParams() {
        def foos = Foo.list()

        when:
        Filter filter = Foo.enableHibernateFilter('multipleUseParamFilter')  // id > :avoid or id < :avoid
        filter.setParameter('avoid', foos[0].id)

        def filteredFoos = Foo.list()

        then:
        foos.size() - 1 == filteredFoos.size()
    }
}
