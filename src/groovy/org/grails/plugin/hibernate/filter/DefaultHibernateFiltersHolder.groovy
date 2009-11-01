package org.grails.plugin.hibernate.filter
/**
 * Created by IntelliJ IDEA.
 * User: scott
 * Date: Nov 1, 2009
 * Time: 12:03:00 PM
 * To change this template use File | Settings | File Templates.
 */

public class DefaultHibernateFiltersHolder {
    static List defaultFilters = []

    static void addFilter(String name) {
        defaultFilters << name
    }
}