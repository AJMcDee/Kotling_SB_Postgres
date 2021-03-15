package com.example.demo.testing

import com.example.demo.model.Account
import com.example.demo.model.LoginAttempt
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.junit.*
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders


@SpringBootTest
class AuthenticationTests() {
    @Autowired
    val restTemplate = TestRestTemplate()
    val headers: HttpHeaders = HttpHeaders()



    @Test
    fun `Posting correct login information returns a valid token`(){
        val testLogin = LoginAttempt("DE6543","woofwoof")
        var request: HttpEntity<LoginAttempt> = HttpEntity<LoginAttempt>(testLogin, headers)
        val entity = restTemplate.postForEntity<String>("http://localhost:8080/authenticate", request, String.javaClass)


        if (entity.statusCode == HttpStatus.OK) {
            assertThat(entity.body?.length, equalTo(25))
        }
    }

    @Test
    fun `Posting incorrect password returns FORBIDDEN status`(){
        val badLogin = LoginAttempt("DE6543","wrongpassword")
        var request: HttpEntity<LoginAttempt> = HttpEntity<LoginAttempt>(badLogin, headers)
        val entity = restTemplate.postForEntity<String>("http://localhost:8080/authenticate", request, String.javaClass)
        assertThat(entity.statusCode, equalTo(HttpStatus.FORBIDDEN))
    }

    @Test
    fun `Posting incorrect IBAN returns FORBIDDEN status`(){
        val badLogin = LoginAttempt("AUSSIEOYOY","woofwoof")
        var request: HttpEntity<LoginAttempt> = HttpEntity<LoginAttempt>(badLogin, headers)
        val entity = restTemplate.postForEntity<String>("http://localhost:8080/authenticate", request, String.javaClass)
        assertThat(entity.statusCode, equalTo(HttpStatus.FORBIDDEN))
    }


}