package com.example.tests.testing

import com.example.demo.bank.BankClient
import com.example.demo.bank.BankService
import com.example.demo.bank.IncorrectCredentialsException
import org.springframework.boot.test.web.client.getForEntity
import com.example.demo.model.Account
import com.example.demo.model.LoginAttempt
import com.example.demo.model.UpdateRequest
import com.example.demo.repository.AccountRepository
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.`object`.HasToString.hasToString
import org.hamcrest.core.IsEqual.equalTo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.junit.*
import org.junit.jupiter.api.BeforeAll
import org.mockito.Mockito
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity


@SpringBootTest
class TransactionTests() {
    @Autowired
    val restTemplate = TestRestTemplate()



    val headers = HttpHeaders()
    val startingBalance = 100.02
    val amountToChange = 20.00




    fun createTestUser() : String {
        // Create a new user
        val randomIBAN = BankService.generateIBAN();
        val newAccount: Account = Account(null, "Test Name", randomIBAN, "password", startingBalance)
        val headers: HttpHeaders = HttpHeaders()
        val request: HttpEntity<Account> = HttpEntity<Account>(newAccount, headers)
        val entity = restTemplate.postForEntity<String>("http://localhost:8080/accounts", request, String.javaClass)
        assertThat(entity.statusCode, equalTo(HttpStatus.OK))

        // They now exist in the database
        val getEntity = restTemplate.getForEntity<String>("http://localhost:8080/account/$randomIBAN")
        assertThat(getEntity.statusCode, equalTo(HttpStatus.OK))
        assertThat(getEntity.body, containsString(randomIBAN))

        return randomIBAN
    }


    fun getTestToken(iban: String): String {
        val testLogin = LoginAttempt(iban,"password")
        var request: HttpEntity<LoginAttempt> = HttpEntity<LoginAttempt>(testLogin, headers)
        val entity = restTemplate.postForEntity<String>("http://localhost:8080/authenticate", request, String.javaClass)
        println(entity.body)
        var token : String;
        if (entity.statusCode == HttpStatus.OK) {
            token = entity.body.toString()
        } else {
            throw IncorrectCredentialsException()
        }
        return token
    }

    @Test
    fun `Depositing money returns the updated balance`() {
        val testIban = createTestUser()
        val token = getTestToken(testIban)


        val depositRequest = UpdateRequest(amountToChange,"deposit", null)
        val expectedBalance: Double = startingBalance + amountToChange
        val depositEntity = restTemplate.postForEntity<String>("http://localhost:8080/account/$token", depositRequest, String.javaClass)
        assertThat(depositEntity.statusCode, equalTo(HttpStatus.OK))
        assertThat(depositEntity.body, containsString(expectedBalance.toString()))
    }

    @Test
    fun `Withdrawing money returns the updated balance`() {
        val testIban = createTestUser()
        val token = getTestToken(testIban)

        val withdrawalRequest = UpdateRequest(amountToChange,"withdraw", null)
        val expectedBalance: Double = startingBalance - amountToChange
        val depositEntity = restTemplate.postForEntity<String>("http://localhost:8080/account/$token", withdrawalRequest, String.javaClass)
        assertThat(depositEntity.statusCode, equalTo(HttpStatus.OK))
        assertThat(depositEntity.body, containsString(expectedBalance.toString()))
    }

    @Test
    fun `Transferring money returns the updated balance`() {
        val testIban = createTestUser()
        val token = getTestToken(testIban)

        val transferRequest = UpdateRequest(amountToChange,"transfer", "2222")
        val expectedBalance: Double = startingBalance - amountToChange
        val depositEntity = restTemplate.postForEntity<String>("http://localhost:8080/account/$token", transferRequest, String.javaClass)
        assertThat(depositEntity.statusCode, equalTo(HttpStatus.OK))
        assertThat(depositEntity.body, containsString(expectedBalance.toString()))
    }

    @Test
    fun `Transferring money without a "to" account should throw an error`() {
        val testIban = createTestUser()
        val token = getTestToken(testIban)

        val transferRequest = UpdateRequest(amountToChange,"transfer", null)
        val depositEntity = restTemplate.postForEntity<String>("http://localhost:8080/account/$token", transferRequest, String.javaClass)

        assertThat(depositEntity.statusCode, equalTo(HttpStatus.BAD_REQUEST))
    }


}




