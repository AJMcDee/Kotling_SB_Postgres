package com.example.demo.testing


import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.junit.*
import org.springframework.boot.test.web.client.getForEntity



@SpringBootTest
class GetUserTest() {
    @Autowired
    val restTemplate = TestRestTemplate()

    val getEntity = restTemplate.getForEntity<String>("http://localhost:8080/account/DE6543")

    @Test
    fun `Getting with IBAN returns positive status code`(){
        assertThat(getEntity.statusCode, equalTo(HttpStatus.OK))
        println(getEntity.body)
    }

    @Test
    fun `Getting by IBAN returns the correct user`(){
        assertThat(getEntity.body, containsString("DE6543"))
    }

    @Test
    fun `Requesting a non-existing IBAN returns a BAD REQUEST status`(){
        val badGetEntity = restTemplate.getForEntity<String>("http://localhost:8080/account/AUS2041")
        assertThat(badGetEntity.statusCode, equalTo(HttpStatus.BAD_REQUEST))
    }


}