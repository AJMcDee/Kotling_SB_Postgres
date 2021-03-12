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
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders


@SpringBootTest
class GetUserTest() {
    @Autowired
    val restTemplate = TestRestTemplate()

    val getEntity = restTemplate.getForEntity<String>("http://localhost:8080/account/DE123")

    @Test
    fun `Posting get with IBAN returns positive status code`(){

        assertThat(getEntity.statusCode, equalTo(HttpStatus.OK))
        println(getEntity.body)
    }

    @Test
    fun `Getting a user by IBAN returns that user`(){
        assertThat(getEntity.body, containsString("DE123"))
    }


}