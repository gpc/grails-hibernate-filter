class FooController {

    def index = {
        def tests = [:]
        this.properties.each {
            if(it.key =~ /^test/) {
                tests[it.key] = it.value
            }
        }

        def results = [:]
        tests.each { key, value ->
            results [key] = value()
        }

        return [results: results]
    }

    def testFooCount = {Foo.list().size() == Foo.findAllByEnabled(true).size()}

    def testBarCount = {
        def foo = Foo.list()[0]
        foo.bars.size() == Bar.findAllByEnabledAndFoo(true, foo).size()
    }

    def testExcludeFooFilterCount = {
        Foo.withoutHibernateFilter('fooEnabledFilter') {
            Foo.list().size() == 2
        }
    }

    def testUseAlias = {
        Foo.withoutHibernateFilter('fooEnabledFilter') {
            EnabledFoo.list().size() == Foo.findAllByEnabled(true).size() 
        }
    }

    def testNameParameterPassed = {
        Foo.enableHibernateFilter('fooNameFilter').setParameter('name', 'enabledFoo')
        def result = Foo.list().size() == 1
        Foo.disableHibernateFilter('fooNameFilter')
        return result
    }

    def testNameParameterFail = {
        Foo.enableHibernateFilter('fooNameFilter').setParameter('name', 'wrongName')
        def result = Foo.list().size() == 0
        Foo.disableHibernateFilter('fooNameFilter')
        return result
    }


    
}



