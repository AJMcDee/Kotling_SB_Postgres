package com.example.demo.bank

import com.example.demo.model.TransactionRequest
import com.example.demo.model.UpdateRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// Any BankService actions which require communication with another microservice

@Component
class BankClient  {


    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }

    @Autowired
    lateinit var restTemplate: RestTemplate

    private fun createTransactionRequest(iban: String, updateRequest: UpdateRequest): TransactionRequest {
        var fromIban: String? = null
        var toIban: String? = null
        val amount = updateRequest.amount
        val type = updateRequest.operation
        val date = getFormattedDateAsString()

        when (updateRequest.operation) {
            "deposit" -> toIban = iban
            "withdraw" -> fromIban = iban
            "transfer" -> {
                fromIban = iban
                toIban = updateRequest.toIban
            }
            else -> throw InvalidOperationException()
        }

        return TransactionRequest(toIban, fromIban, amount, type, date)
    }

    fun sendNewTransaction(iban: String, updateRequest: UpdateRequest) {
        val transactionRequest = createTransactionRequest(iban, updateRequest)
        val result = restTemplate.postForObject("http://localhost:8081/transactions", transactionRequest, String.javaClass)
        println(result)
    }

    private fun getFormattedDateAsString() : String {
        val current = LocalDate.now();
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        return current.format(formatter)
    }






}