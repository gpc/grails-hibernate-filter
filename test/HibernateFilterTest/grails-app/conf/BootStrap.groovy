class BootStrap {

     def init = { servletContext ->
         def enabledFoo = new Foo(name:'enabledFoo', enabled: true).save()
         enabledFoo.addToBars(new Bar(name:'enabledBar', enabled:true))
         enabledFoo.addToBars(new Bar(name:'diabledBar', enabled:false))

         def disabledFoo = new Foo(name:'disabledFoo').save()
         disabledFoo.addToBars(new Bar(name:'enabled_bar', enabled:true))
         disabledFoo.addToBars(new Bar(name:'disabled_bar', enabled:false))

//         def nameFoo = new Foo(name:'someName').save()
//         nameFoo.addToBars(new Bar(name:'name_bar', enabled: true))
     }
     def destroy = {
     }
} 