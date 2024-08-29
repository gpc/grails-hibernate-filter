package org.grails.plugin.hibernate.filter

import groovy.transform.CompileStatic

/**
 * Thrown if there is a misconfiguration in a hibernateFilters block.
 */

@CompileStatic
class HibernateFilterException extends RuntimeException {

	HibernateFilterException(String message) {
		super(message)
	}
}
