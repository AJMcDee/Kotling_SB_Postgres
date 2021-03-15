package com.example.tests.testing

import com.example.demo.bank.BankService
import org.springframework.boot.test.web.client.getForEntity
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
class DeleteUserTests() {
    @Autowired
    val restTemplate = TestRestTemplate()
    val headers: HttpHeaders = HttpHeaders()

    @Test
    fun `Deleting a user removes them from the database`() {
        // Create a new user
        val randomIBAN = BankService.generateIBAN();
        val newAccount: Account = Account(null, "Test Name", randomIBAN, "password", 99.99)
        val headers: HttpHeaders = HttpHeaders()
        val request: HttpEntity<Account> = HttpEntity<Account>(newAccount, headers)
        val entity = restTemplate.postForEntity<String>("http://localhost:8080/accounts", request, String.javaClass)
        assertThat(entity.statusCode, equalTo(HttpStatus.OK))

        // They now exist in the database
        val getEntity = restTemplate.getForEntity<String>("http://localhost:8080/account/$randomIBAN")
        assertThat(getEntity.statusCode, equalTo(HttpStatus.OK))
        assertThat(getEntity.body, containsString(randomIBAN))

        // Log them in
        val testLogin = LoginAttempt(randomIBAN, "password")
        val loginRequest: HttpEntity<LoginAttempt> = HttpEntity<LoginAttempt>(testLogin, headers)
        val loginEntity =
            restTemplate.postForEntity<String>("http://localhost:8080/authenticate", loginRequest, String.javaClass)
        assertThat(loginEntity.statusCode, equalTo(HttpStatus.OK))
        val token = loginEntity.body.toString()

        // Delete them
        restTemplate.delete("http://localhost:8080/account/$token")

        // They're now not in the database
        val databaseCheckEntity = restTemplate.getForEntity<String>("http://localhost:8080/accounts")
        assertThat(databaseCheckEntity.statusCode, equalTo(HttpStatus.OK))
        assertThat(databaseCheckEntity.body, not(containsString(randomIBAN)))
    }
}


