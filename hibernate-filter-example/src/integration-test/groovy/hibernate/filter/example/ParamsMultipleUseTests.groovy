package hibernate.filter.example

import grails.test.mixin.integration.Integration
import grails.transaction.Rollback
import org.hibernate.Filter
import spock.lang.Specification

@Integration
@Rollback
class ParamsMultipleUseSpec extends Specification {

    def testFilterWithMultiUseParams() {
        given:
        new Foo(name: 'foo0', enabled: true).save(flush: true)
        new Foo(name: 'foo1', enabled: true).save(flush: true)
        new Foo(name: 'foo2', enabled: true).save(flush: true)

        def foos = Foo.list()

        when:
        Filter filter = Foo.enableHibernateFilter('multipleUseParamFilter')  // id > :avoid or id < :avoid
        filter.setParameter('avoid', foos[0].id)

        def foos_filtered = Foo.list()

        then:
        foos.size() - 1 == foos_filtered.size()
    }

}
