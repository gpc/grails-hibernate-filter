package test.domain.status

import grails.test.spock.IntegrationSpec

class StatusFilterSpec extends IntegrationSpec {

    private problem
    private condition

    def setup() {
        this.problem = new Problem(name: 'problem').save(failOnError: true)
        this.condition = new Condition(name: 'condition').save(failOnError: true)
        condition.addToProblems(problem)

        // enable the Hibernate filters
        [Condition, Problem].each {
            it.enableHibernateFilter('published')
        }
    }

    void 'query condition only'() {
        expect:
        Condition.count == 1
    }

    void 'query problem only'() {
        expect:
        Problem.count == 1
    }

    void 'query that joins problem and condition'() {

        when:
        List<Problem> problems = Problem.executeQuery("""
                FROM            ${Problem.simpleName} p
                JOIN            p.conditions as c
                WHERE           c.id = ?""", [condition.id])

        then:
        problems.size() == 1
        problems[0].id == problem.id
    }
}