package com.example.demo.testing

import com.example.demo.model.Account
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
class AddUserTests() {
    @Autowired
    val restTemplate = TestRestTemplate()

    val testAccount: Account = Account(null,"PotatoHead","DE6543","woofwoof",99.99)
    val headers: HttpHeaders = HttpHeaders()
    var request: HttpEntity<Account> = HttpEntity<Account>(testAccount, headers)
    val entity = restTemplate.postForEntity<String>("http://localhost:8080/accounts", request, String.javaClass)



    @Test
    fun `Adding a user returns all users`(){
        assertThat(entity.statusCode, equalTo(HttpStatus.OK))
        assertThat(entity.body, startsWith("["))
        assertThat(entity.body, endsWith("]"))
    }

    @Test
    fun `Adding a user returns that user`(){
        assertThat(entity.body, containsString("PotatoHead"))
    }


}