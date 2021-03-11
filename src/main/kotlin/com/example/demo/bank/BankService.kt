package com.example.demo.bank

import org.springframework.stereotype.Service
import kotlin.math.floor
import com.example.demo.model.*
import com.example.demo.repository.AccountRepository
import org.springframework.beans.factory.annotation.Autowired

@Service
class BankService {

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
        return repository.findByIban(iban)
    }

    fun addAccount(newAccount: Account): MutableList<Account> {
        repository.save(newAccount)
        return getAllAccounts()
    }

    fun authenticate(loginAttempt : LoginAttempt): String {
        val currentAccount = repository.findByIbanAndPassword(loginAttempt.iban, loginAttempt.password)
        if (currentAccount == null) {
            throw Error("No such account exists")
        } else {
            val token = getNewToken()
            currentAccount.token = token;
            repository.save(currentAccount);
            return token
        }
    }


    fun verifyAccountAccess(accessRequest: AccessRequest) : Account {
        return repository.findByToken(accessRequest.token)
    }

    fun deleteAccount(accessRequest: AccessRequest) : MutableList<Account> {
        repository.delete(verifyAccountAccess(accessRequest))
        return getAllAccounts()
    }

    fun updateBalance(updateRequest: UpdateRequest) : Account {
        val accessRequest = AccessRequest(updateRequest.token)
        var currentAccount = verifyAccountAccess(accessRequest)
        currentAccount.updateBalance(updateRequest.amount,updateRequest.operation)
        return repository.save(currentAccount)
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
