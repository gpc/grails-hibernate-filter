package org.grails.plugin.hibernate.filter

class HibernateFilterDomainProxy {

	String filterName
	String aliasName
	def domainClass

	HibernateFilterDomainProxy(domainClass, String aliasName, String filterName) {
		this.domainClass = domainClass
		this.aliasName = aliasName
		this.filterName = filterName
	}

	def methodMissing(String name, args) {
		domainClass.withHibernateFilter(filterName) {
			def method = domainClass.metaClass.pickMethod(name, args.collect{it.getClass()} as Class[])
			if (method) {
				method.invoke(domainClass, args)
			}
			else {
				domainClass.metaClass.invokeStaticMethod domainClass, name, args
			}
		}
	}

	String toString() {
		"${getClass().simpleName}: alias=${aliasName}, domain=${domainClass.getClass().simpleName}"
	}
}
