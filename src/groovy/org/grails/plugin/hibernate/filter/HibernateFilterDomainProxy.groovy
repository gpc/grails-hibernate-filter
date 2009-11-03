package org.grails.plugin.hibernate.filter


public class HibernateFilterDomainProxy {

    def domainClass
    def filterName
    def aliasName

    def HibernateFilterDomainProxy(domainClass, String aliasName, String filterName) {
        this.domainClass = domainClass
        this.aliasName = aliasName
        this.filterName = filterName
    }

    def methodMissing(String name, args) {
        domainClass.withHibernateFilter(filterName) {

            def method = domainClass.metaClass.pickMethod(name, args.collect{it.getClass()} as Class[])
            if(!method) {
                domainClass.methodMissing(name, args)
            } else {
                method.invoke(domainClass, args)
            }
        }
    }

    String toString() {
        "${this.class.simpleName}: alias=${aliasName}, domain=${domainClass.simpleName}"
    }




}


