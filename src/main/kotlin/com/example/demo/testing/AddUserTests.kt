package com.example.demo.testing

import com.example.demo.model.Account
import junit.framework.Assert.assertFalse
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
import kotlin.math.floor


@SpringBootTest
class AddUserTests() {
    @Autowired
    val restTemplate = TestRestTemplate()


    // For a new account
    val randomIBAN = "DE" + floor(Math.random() * 99999999)
    val testAccount: Account = Account(null,"Test Name",randomIBAN,"password",99.99)
    val headers: HttpHeaders = HttpHeaders()
    var request: HttpEntity<Account> = HttpEntity<Account>(testAccount, headers)
    val entity = restTemplate.postForEntity<String>("http://localhost:8080/accounts", request, String.javaClass)



    @Test
    fun `Posting a new account returns an array`(){
        assertThat(entity.statusCode, equalTo(HttpStatus.OK))
        assertThat(entity.body, startsWith("["))
        assertThat(entity.body, endsWith("]"))
    }

    @Test
    fun `Posting a new account returns an array including the new account`(){
        assertThat(entity.body, containsString(randomIBAN))
    }


    // To test for duplicates or overwriting

    fun countMatches(string: String, pattern: String): Int {
        return string.split(pattern)
            .dropLastWhile{it.isEmpty()}
            .toTypedArray().size - 1
    }

    val existingAccount = Account(null, "HorseHead","DE6543", "password", 94.44, "default")
    var existingRequest: HttpEntity<Account> = HttpEntity<Account>(existingAccount, headers)
    val existingEntity = restTemplate.postForEntity<String>("http://localhost:8080/accounts", existingRequest, String.javaClass)
    val bodyReturned = existingEntity.body.toString()

    @Test
    fun `Posting an existing account returns the original account`(){

        assertThat("PotatoHead returns",entity.body, containsString("PotatoHead"))
        assertFalse(bodyReturned.contains("HorseHead"))
    }

    @Test
    fun `Posting an existing account returns only one account with that IBAN`() {

        val count = countMatches(bodyReturned, randomIBAN)
        assertThat(count, equalTo(1))

    }


}