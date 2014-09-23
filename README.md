grails-hibernate-filter
=======================

This is a fork of the original [Grails Hibernate Filter Plugin](http://grails.org/plugin/hibernate-filter) to make it work with the latest Grails version 2.4.3 and Hibernate 4.

# Installation

## 1. Build Plugin Zip File

To install this plugin, please run the following command to build the plugin zip file first.

    grails maven-install
    
The following plugin zip file will be generated,

- grails-hibernate-filter-0.3.2.appcela.zip

## 2. Install This Plugin

Copy the generated plugin zip file to your Grails project's "`lib/`" folder.

## 3. Add Plugin Dependency

Include the following in your "`grails-app/conf/BuildConfig.groovy`" file's plugins dependency,

    compile ":hibernate-filter:0.3.2.appcela"
    
Then you should be all set.    
