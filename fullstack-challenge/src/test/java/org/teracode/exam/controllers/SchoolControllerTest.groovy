package org.teracode.exam.controllers

import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.teracode.exam.controllers.advices.SchoolControllerAdvice
import org.teracode.exam.daos.StudentsDao
import org.teracode.exam.daos.implementations.StudentsMockDao
import org.teracode.exam.services.StudentsService
import org.teracode.exam.services.implementations.StudentsBasicService
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class SchoolControllerTest extends Specification {
    private MockMvc mockMvc
    private SchoolController controller
    private StudentsService service
    private StudentsDao repository

    void setup() {
        repository = new StudentsMockDao()
        service = new StudentsBasicService(repository,)
        controller = new SchoolController(service)
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new SchoolControllerAdvice())
                .build()
    }

    def "When invocked getStudentsByLastnameInitial method with invalid initial -> returns 400 bad request"() {
        given:
        def initial = "1"

        when:
        def results = mockMvc.perform(get('/school/students/{lastNameInitial}', initial)
                .contentType(MediaType.APPLICATION_JSON))

        then:
        results.andExpect(status().isBadRequest())
        results.andExpect(jsonPath('$.error').value('Invalid last name initial'))
    }

    def "When invocked getStudentsByLastnameInitial method with valid initial -> returns data and 200 ok"() {
        given:
        def initial = "r"

        when:
        def results = mockMvc.perform(get('/school/students/{lastNameInitial}', initial)
                .contentType(MediaType.APPLICATION_JSON))

        then:
        results.andExpect(status().isOk())
        results.andExpect(jsonPath('$').exists())
        results.andExpect(jsonPath('$').isArray())
    }

    def "When invocked getStudentsTakingSubjects method -> returns data and 200 ok"() {
        when:
        def results = mockMvc.perform(get('/school/students')
                .contentType(MediaType.APPLICATION_JSON))

        then:
        noExceptionThrown()
        and:
        results.andExpect(status().isOk())
        results.andExpect(jsonPath('$').exists())
        results.andExpect(jsonPath('$').isArray())
    }

    def "When invocked getStudentsTakingSubjects method with invalid subject id -> returns error and 400 bad request"() {
        given:
        def id = 'a'

        when:
        def results = mockMvc.perform(get('/school/students/by-subject/{id}', id)
                .contentType(MediaType.APPLICATION_JSON))

        then:
        results.andExpect(status().isBadRequest())
        results.andExpect(jsonPath('$.error').value('Invalid request parameter'))
    }

    def "When invocked getStudentsTakingSubjects method with valid id -> returns data and 200 ok"() {
        given:
        def id = 1L

        when:
        def results = mockMvc.perform(get('/school/students/by-subject/{id}', id)
                .contentType(MediaType.APPLICATION_JSON))

        then:
        results.andExpect(status().isOk())
        results.andExpect(jsonPath('$').exists())
        results.andExpect(jsonPath('$').isArray())
    }
}
