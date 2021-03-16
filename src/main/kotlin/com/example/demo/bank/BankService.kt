package com.example.demo.bank

import org.springframework.stereotype.Service
import kotlin.math.floor
import com.example.demo.model.*
import com.example.demo.repository.AccountRepository
import org.springframework.beans.factory.annotation.Autowired
import com.example.demo.bank.BankClient
import org.springframework.http.HttpStatus

@Service
class BankService (private val bankClient: BankClient) {

    companion object {
        fun generateIBAN() = "DE" + floor(Math.random() * 9999999).toInt()
    }

    @Autowired
    lateinit var repository: AccountRepository

    fun getAllAccounts(): MutableList<Account> {
        var accounts: MutableList<Account> = mutableListOf<Account>()
        repository.findAll().forEach {
            accounts.add(it)
        }
        return accounts
    }

    fun getAccountByIBAN(iban: String): Account {
        if (!repository.existsByIban(iban)) throw AccountNotFoundException()
        return repository.findByIban(iban)
       }

    fun addAccount(newAccount: Account): MutableList<Account> {
        if (!repository.existsAccountByIban(newAccount.iban)) {
            repository.save(newAccount)
        }
        return getAllAccounts()

    }

    fun authenticate(loginAttempt : LoginAttempt): String {
        val iban = loginAttempt.iban
        val password = loginAttempt.password
        if (!repository.existsByIbanAndPassword(iban, password)) throw IncorrectCredentialsException()

        val currentAccount = repository.findByIbanAndPassword(iban, password)
            val token = getNewToken()
            currentAccount.token = token;
            repository.save(currentAccount);
            return token
    }


    fun verifyAccountAccess(token: String) : Account {
        if (!repository.existsByToken(token)) throw IncorrectCredentialsException()
        return repository.findByToken(token)
    }

    fun deleteAccount(token: String) : MutableList<Account> {
        repository.delete(verifyAccountAccess(token))
        return getAllAccounts()
    }

    fun updateBalance(token: String, updateRequest: UpdateRequest) : Account {
        var currentAccount = verifyAccountAccess(token)


        bankClient.sendNewTransaction(currentAccount.iban, updateRequest)


        currentAccount.updateBalance(updateRequest.amount,updateRequest.operation)
        repository.save(currentAccount)
        return currentAccount
    }

    private fun getNewToken() : String {
        var token = ""
        val randomCollection = "r29afKw02Pmlgp9201Odfqzxru"
        for (i in 1..25) {
            token += randomCollection[floor(Math.random()*25).toInt()]
        }
        return token
    }

}
