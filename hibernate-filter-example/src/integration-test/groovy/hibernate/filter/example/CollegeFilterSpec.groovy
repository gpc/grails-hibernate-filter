package hibernate.filter.example

import org.hibernate.SessionFactory
import spock.lang.Specification
import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration

@Integration
@Rollback
class CollegeFilterSpec extends Specification {

    SessionFactory sessionFactory

    void 'query owning entity of many-to-many relationship'() {
        Course.enableHibernateFilter('collegeFilter')
        [0, 1].each { status ->
            new Course(name: 'course', status: status).save(failOnError: true)
        }

        expect:
        Course.count == 1
    }

    void 'query owned entity of many-to-many relationship'() {
        Student.enableHibernateFilter('collegeFilter')
        [0, 1].each { status ->
            new Student(name: 'student', status: status).save(failOnError: true)
        }

        expect:
        Student.count == 1
    }

    void 'query association of many-to-many relationship'() {
        Course.enableHibernateFilter('collegeFilter')
        new Course(name: 'course')
                .addToStudents(name: 'valid', status: 1)
                .addToStudents(name: 'invalid', status: 0)
                .save(failOnError: true, flush: true)

        sessionFactory.currentSession.clear()

        when:
        def course = Course.findByName('course')

        then:
        course.students.size() == 1
        course.students.every { it.status == 1 }
    }

    void 'query many-to-many relationship with HQL'() {
        Course.enableHibernateFilter('collegeFilter')
        new Course(name: 'invalidCourse', status: 0).save(failOnError: true, flush: true)
        new Course(name: 'validCourse')
                .addToStudents(name: 'valid', status: 1)
                .addToStudents(name: 'invalid', status: 0)
                .save(failOnError: true, flush: true)

        sessionFactory.currentSession.clear()

        when:
        def courses = Course.executeQuery("""
                SELECT DISTINCT c
                FROM            Course c
                JOIN            c.students""")

        then:
        courses.size() == 1
        courses[0].name == 'validCourse'
        courses[0].students.size() == 1
    }

    void 'query unidirectional one-to-many relationship'() {
        Student.enableHibernateFilter('collegeFilter')
        new Student(name: 'Mark')
                .addToPens(name: 'valid pen', status: 1)
                .addToPens(name: 'invalid pen', status: 0)
                .save(failOnError: true, flush: true)

        sessionFactory.currentSession.clear()

        when:
        def student = Student.findByName('Mark')

        then:
        student.pens.size() == 1
    }

    void 'query one-to-many relationship with HQL'() {
        Student.enableHibernateFilter('collegeFilter')
        new Student(name: 'invalid', status: 0).save(failOnError: true)
        def validStudent = new Student(name: 'valid').save(failOnError: true)

        [0, 1].each { status ->
            new Loan(amount: 100, status: status, borrower: validStudent).save(failOnError: true)
        }

        sessionFactory.currentSession.clear()

        when:
        def students = Student.executeQuery("""
                SELECT DISTINCT s
                FROM            Student s
                JOIN            s.loans l
                WHERE           l.amount > 0""")

        then:
        students.size() == 1
        students[0].name == 'valid'
        students[0].loans.size() == 1
        students[0].loans.every { it.status == 1 }
    }

    void 'query owned entity of one-to-many relationship'() {
        Student.enableHibernateFilter('collegeFilter')
        def borrower = new Student(name: 'Mark').save(failOnError: true)

        [0, 1].each { status ->
            new Loan(amount: 100 * status, status: status, borrower: borrower).save(failOnError: true)
        }

        expect:
        Loan.count == 1
    }

    void 'query association of one-to-many relationship'() {
        Student.enableHibernateFilter('collegeFilter')
        new Student(name: 'Mark')
                .addToLoans(amount: 100, status: 1)
                .addToLoans(amount: 200, status: 0)
                .save(failOnError: true)

        sessionFactory.currentSession.clear()

        when:
        def student = Student.findByName('Mark')

        then:
        student.loans.size() == 1
        student.loans.every { it.status == 1 }
    }
}