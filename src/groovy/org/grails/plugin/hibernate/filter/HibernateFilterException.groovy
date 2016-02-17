package org.grails.plugin.hibernate.filter

/**
 * Thrown if there is a misconfiguration in a hibernateFilters block.
 */
class HibernateFilterException extends RuntimeException {

	HibernateFilterException(String message) {
		super(message)
	}
}
