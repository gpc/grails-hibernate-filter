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
        domainClass.newInstance().withHibernateFilter(filterName) {
            def method = domainClass.metaClass.pickMethod(name, args as Class[])
            method.invoke(domainClass, args)
        }
    }

    String toString() {
        "${this.class.simpleName}: alias=${aliasName}, domain=${domainClass}"
    }




}


