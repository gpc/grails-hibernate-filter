package org.grails.plugin.hibernate.filter
/**
 * Created by IntelliJ IDEA.
 * User: scott
 * Date: Nov 1, 2009
 * Time: 12:03:00 PM
 * To change this template use File | Settings | File Templates.
 */

public class DefaultHibernateFiltersHolder {
    final static List defaultFilters = []
    final static List domainAliasProxies = []
    final static Map defaultFilterCallbacks = [:]

    static void addDefaultFilter(String name) {
        defaultFilters << name
    }

    static void addDomainAliasProxy(HibernateFilterDomainProxy proxy) {
        domainAliasProxies << proxy
    }

    static void addDefaultFilterCallback(String name, Closure closure) {
        defaultFilterCallbacks[name] = closure        
    }
}