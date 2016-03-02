package org.grails.plugin.hibernate.filter

import grails.test.spock.IntegrationSpec
import spock.lang.Issue
import test.domain.status.Condition
import test.domain.status.Problem

class StatusFilterSpec extends IntegrationSpec {

    private Problem problem
    private Condition condition

    def setup() {
        this.problem = new Problem(name: 'problem').save(failOnError: true)
        this.condition = new Condition(name: 'condition').addToProblems(problem).save(failOnError: true)

        // enable the Hibernate filters
        Problem.enableHibernateFilter('statusFilter')
        Condition.enableHibernateFilter('statusFilter')
    }

    void 'query condition only'() {
        expect:
        Condition.count == 1
    }

    void 'query problem only'() {
        expect:
        Problem.count == 1
    }

    @Issue('GPHIBERNATEFILTER-19')
    void 'query that joins problem and condition'() {

        when:
        List<Problem> problems = Problem.executeQuery("""
                SELECT  p
                FROM    ${Problem.simpleName} p
                JOIN    p.conditions""")

        then:
        problems.size() == 1
    }

    @Issue('GPHIBERNATEFILTER-20')
    void 'collection filters are incorrectly applied to join table'() {

        // enable the collection filters in each class
        Problem.enableHibernateFilter('collectionFilter')
        Condition.enableHibernateFilter('collectionFilter')

        when:
        List<Problem> problems = Problem.executeQuery("""
                SELECT  p
                FROM    ${Problem.simpleName} p
                JOIN    p.conditions""")

        then:
        problems.size() == 1
    }
}