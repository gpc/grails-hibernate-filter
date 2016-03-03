package org.grails.plugin.hibernate.filter

import grails.test.spock.IntegrationSpec
import org.hibernate.SessionFactory
import spock.lang.Issue
import test.domain.status.Condition
import test.domain.status.Problem

class StatusFilterSpec extends IntegrationSpec {

    SessionFactory sessionFactory

    def setup() {
        [0, 1].each { status ->
            new Problem(name: 'problem', status: status).save(failOnError: true)
            new Condition(name: 'condition', status: status).save(failOnError: true)
        }

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

    @Issue('https://github.com/burtbeckwith/grails-hibernate-filter/issues/8')
    void 'query that joins problem and condition'() {

        Condition.findByStatus(1).addToProblems(Problem.findByStatus(1))

        when:
        List<Problem> problems = Problem.executeQuery("""
                SELECT  p
                FROM    Problem p
                JOIN    p.conditions""")

        then:
        problems.size() == 1
    }

    // this test is currently failing due to the error described in the issue below
    @Issue('https://github.com/burtbeckwith/grails-hibernate-filter/issues/9')
    void 'test collection hibernate filters'() {

        setup: "Add a valid and invalid condition to a valid problem. When we retrieve the problem the invalid condition be inaccessible"
        def validProblem = Problem.findByStatus(1)
        new Condition(name: 'invalidCondition', status: 0).addToProblems(validProblem).save(failOnError: true)
        new Condition(name: 'validCondition', status: 1).addToProblems(validProblem).save(failOnError: true)

        sessionFactory.currentSession.clear()

        // enable the collection filters
        Problem.enableHibernateFilter('collectionFilter')
        Condition.enableHibernateFilter('collectionFilter')

        when:
        List<Problem> problems = Problem.executeQuery("""
                SELECT  p
                FROM    Problem p
                JOIN    p.conditions""")

        then:
        problems.size() == 1
        problems.conditions.size() == 1
    }
}