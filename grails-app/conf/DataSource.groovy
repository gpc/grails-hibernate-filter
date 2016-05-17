import org.grails.plugin.hibernate.filter.HibernateFilterDomainConfiguration

dataSource {
	pooled = true
	driverClassName = 'org.h2.Driver'
	username = 'sa'
	password = ''
	dbCreate = 'create-drop'
	url = 'jdbc:h2:mem:devDb;MVCC=TRUE'
	logSql = true
	configClass = HibernateFilterDomainConfiguration
}

hibernate {
	cache.use_second_level_cache = false
	cache.use_query_cache = false
	cache.region.factory_class = 'org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory' // Hibernate 4
	format_sql = true
	use_sql_comments = true
}
