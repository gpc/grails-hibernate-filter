grails-hibernate-filter
=======================

This is a fork of the original [Grails Hibernate Filter Plugin](http://grails.org/plugin/hibernate-filter) 
created from fork [appcela/grails-hibernate-plugin](https://github.com/appcela/grails-hibernate-filter) 
to make it work with the Grails 3.

# Installation

## 1. Build Plugin Zip File

Currently this is no pulbic repo with this artifact so local maven repository should be used.
Clone this repo and publish to your local maven repo using:

    ./gradlew publishToMavenLocal
    
## 2. Install This Plugin

Add dependency in build.gradle:

    compile "org.grails.plugins:grails-hibernate-filter:<version_number>"
    
Configure dataSource in application.yml

    configClass: org.grails.plugin.hibernate.filter.HibernateFilterDomainConfiguration

# Usage

Please refer to the official [Grails Hibernate Filter Plugin](http://grails.org/plugin/hibernate-filter) for usage.

# Continuous integration server
[![Build Status](https://travis-ci.org/fingo/grails-hibernate-filter.svg?branch=master)](https://travis-ci.org/fingo/grails-hibernate-filter)
