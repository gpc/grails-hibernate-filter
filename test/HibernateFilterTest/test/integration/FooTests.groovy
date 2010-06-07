import grails.test.*

class FooTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
        def enabledFoo = new Foo(name:'enabledFoo', enabled: true).save()
        enabledFoo.addToBars(new Bar(name:'enabledBar', enabled:true))
        enabledFoo.addToBars(new Bar(name:'diabledBar', enabled:false))

        def disabledFoo = new Foo(name:'disabledFoo').save()
        disabledFoo.addToBars(new Bar(name:'enabled_bar', enabled:true))
        disabledFoo.addToBars(new Bar(name:'disabled_bar', enabled:false))
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testDefaultFilters() {
        def foos = Foo.list()
        assertEquals Foo.findAllWhere(enabled:true).size(), foos.size()
        foos.each { foo ->
            assertTrue foo.enabled
            foo.bars.each { bar -> assertTrue bar.enabled}
        }

    }
}
