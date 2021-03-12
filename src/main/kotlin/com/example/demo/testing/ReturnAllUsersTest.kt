package com.example.demo.testing

import org.hamcrest.CoreMatchers.endsWith
import org.hamcrest.CoreMatchers.startsWith
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestComponent
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.junit.*
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.web.server.LocalServerPort

@SpringBootTest
class ReturnAllUsersTest() {

    @Autowired
    val restTemplate = TestRestTemplate()

    @Test
    fun `Running get request returns positive HTTP status`() {
        val entity = restTemplate.getForEntity<String>("http://localhost:8080/accounts")
        assertThat(entity.statusCode, equalTo(HttpStatus.OK))
    }

    @Test
    fun `Get-requesting all accounts returns an array`() {
        val entity = restTemplate.getForEntity<String>("http://localhost:8080/accounts")
        assertThat(entity.statusCode, equalTo(HttpStatus.OK))
        assertThat(entity.body, startsWith("["))
        assertThat(entity.body, endsWith("]"))
    }

}