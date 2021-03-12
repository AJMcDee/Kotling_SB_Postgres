package com.example.demo.testing

import com.example.demo.model.Account
import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.junit.*
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.boot.test.json.*

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