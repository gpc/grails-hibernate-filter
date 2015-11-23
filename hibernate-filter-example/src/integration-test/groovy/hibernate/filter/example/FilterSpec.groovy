package hibernate.filter.example

import grails.test.mixin.integration.Integration
import grails.transaction.Rollback
import spock.lang.Specification

@Integration
@Rollback
class FilterSpec extends Specification {

    /**
     * this can't be spock's {@code setup ( )} method because save operation on the domain object must be executed
     * from {@code given/when} part of the test; only then hibernate session is created
     */
    def prepareData() {
        Foo enabledFoo = new Foo(name: 'enabledFoo', enabled: true)
        enabledFoo.addToBars new Bar(name: 'enabledBar', enabled: true)
        enabledFoo.addToBars new Bar(name: 'diabledBar', enabled: false)
        enabledFoo.save(flush: true)

        Foo disabledFoo = new Foo(name: 'disabledFoo', enabled: true)
        disabledFoo.addToBars new Bar(name: 'enabled_bar', enabled: true)
        disabledFoo.addToBars new Bar(name: 'disabled_bar', enabled: false)
        disabledFoo.save(flush: true)

        SubFoo enabledSubFoo = new SubFoo(name: 'enabledSubFoo', enabled: true, wahoo: 'wahoo1')
        enabledSubFoo.addToBars new Bar(name: 'enabledBar', enabled: true)
        enabledSubFoo.addToBars new Bar(name: 'diabledBar', enabled: false)
        enabledSubFoo.save(flush: true)

        Foo disabledSubFoo = new SubFoo(name: 'disabledSubFoo', enabled: true, wahoo: 'wahoo2')
        disabledSubFoo.addToBars new Bar(name: 'enabled_bar', enabled: true)
        disabledSubFoo.addToBars new Bar(name: 'disabled_bar', enabled: false)
        disabledSubFoo.save(flush: true)
    }

    def testDefaultFilters() {
        when:
        prepareData()

        then:
        int enabledCount = Foo.countByEnabled(true)

        and:
        def foos = []   //assignment only to help compiler
        Foo.withHibernateFilters {
            foos = Foo.list()
        }

        then:
        enabledCount == foos.size()
        for (Foo foo in foos) {
            foo.enabled
            for (Bar bar in foo.bars) {
                bar.enabled
            }
        }
    }

    void testDefaultFiltersWithSubclass() {

        when:
        prepareData()

        then:
        int enabledCount = SubFoo.countByEnabled(true)

        and:
        def foos = []
        SubFoo.withHibernateFilters {
            foos = SubFoo.list()
        }

        then:
        foos.size() == enabledCount

        for (SubFoo foo in foos) {
            foo.enabled
            for (Bar bar in foo.bars) {
                bar.enabled
            }
        }
    }
}
