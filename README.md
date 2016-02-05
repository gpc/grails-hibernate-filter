grails-hibernate-filter
=======================

# Description
This is a fork of the original [Grails Hibernate Filter Plugin](http://grails.org/plugin/hibernate-filter) 
created from fork [appcela/grails-hibernate-plugin](https://github.com/appcela/grails-hibernate-filter) 
to make it work with the Grails 3 and Hibernate 4.

This repo contains two projects:
  
1.  hibernate-filter-plugin - with plugin code
1.  hibernate-filter-example - with example application using plugin 

# Usage

## Build Plugin File

Clone the repository and execute in main directory command:

    ./gradlew hibernate-filter-plugin:jar
    
You can publish it to your maven local repository using:

    ./gradlew hibernate-filter-plugin:publishToMavenLocal
    
## Running example application

To run example application use command:

    ./gradlew hibernate-filter-plugin:run
    
## Use plugin in your application

Add dependency in build.gradle:

    compile "org.grails.plugins:grails-hibernate-filter:<version_number>"
    
Configure dataSource in application.yml

    configClass: org.grails.plugin.hibernate.filter.HibernateFilterDomainConfiguration

# Usage

Please refer to the official [Grails Hibernate Filter Plugin](http://grails.org/plugin/hibernate-filter) for usage.

# Continuous integration server
[![Build Status](https://travis-ci.org/fingo/grails-hibernate-filter.svg?branch=master)](https://travis-ci.org/fingo/grails-hibernate-filter)
