package org.grails.plugin.hibernate.filter

class HibernateFilterDomainProxy {

	String filterName
	String aliasName
	Object domainClassInstance

	HibernateFilterDomainProxy(Object domainClassInstance, String aliasName, String filterName) {
		this.domainClassInstance = domainClassInstance
		this.aliasName = aliasName
		this.filterName = filterName
	}

	def methodMissing(String name, args) {
		domainClassInstance.getClass().withHibernateFilter(filterName) {
			def method = domainClassInstance.getClass().metaClass.pickMethod(name, args.collect{it.getClass()} as Class[])
			if (method) {
				method.invoke(domainClassInstance, args)
			}
			else {
				domainClassInstance.getClass().metaClass.invokeStaticMethod domainClassInstance, name, args
			}
		}
	}

	String toString() {
		"${getClass().simpleName}: alias=${aliasName}, domain=${domainClassInstance.getClass().name}"
	}
}
