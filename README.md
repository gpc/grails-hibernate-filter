grails-hibernate-filter
=======================

# Description
This is a fork of the original [Grails Hibernate Filter Plugin](http://grails.org/plugin/hibernate-filter) 
created from fork [fingo/grails-hibernate-plugin](https://github.com/fingo/grails-hibernate-filter) 
to make it work with Grails 3.2.3 > *, Hibernate 5, and GORM 6.

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

    ./gradlew hibernate-filter-example:bootRun
    
## Installation

Add dependency in build.gradle:

    repositories {
        maven { url "https://dl.bintray.com/goodstartgenetics/grails3-plugins/" }
    }
    
    dependencies {
        compile "org.grails.plugins:hibernate-filter-plugin:0.5.1"
    }

# Usage

Please refer to the official [Grails Hibernate Filter Plugin](http://grails.org/plugin/hibernate-filter) for usage.

# Continuous integration server
[![Build Status](https://travis-ci.org/alexkramer/grails-hibernate-filter.svg?branch=master)](https://travis-ci.org/alexkramer/grails-hibernate-filter)
