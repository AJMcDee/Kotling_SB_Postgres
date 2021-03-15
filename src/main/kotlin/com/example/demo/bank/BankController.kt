package com.example.demo.bank

import com.example.demo.model.AccessRequest
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.web.bind.annotation.*
import org.springframework.beans.factory.annotation.Autowired

import com.example.demo.model.Account
import com.example.demo.model.LoginAttempt
import com.example.demo.model.UpdateRequest
import com.example.demo.repository.AccountRepository


@SpringBootApplication
@CrossOrigin(origins = arrayOf("http://localhost:8080"))
@RestController("/")

class BankController (private val bankService: BankService){

    @Autowired
    lateinit var repository: AccountRepository

    @GetMapping("/accounts")
    fun sendAccounts(): List<Account> {
        return bankService.getAllAccounts();
    }

    @GetMapping("/account/{iban}")
    fun getAccount(@PathVariable iban: String): Account {
        return bankService.getAccountByIBAN(iban);
    }

    @PostMapping(path = ["/accounts"])
    fun addMember(@RequestBody newAccount : Account): MutableList<Account> {
        return bankService.addAccount(newAccount);
    }

    @PostMapping(path = ["/authenticate"])
    fun authenticate(@RequestBody loginAttempt: LoginAttempt): String {
        return bankService.authenticate(loginAttempt)
    }

    @DeleteMapping("/account")
    fun deleteAccount(@RequestBody accessRequest: AccessRequest): MutableList<Account> {
        return bankService.deleteAccount(accessRequest)
    }

    @PatchMapping(path = ["/account"])
    fun updateBalance(@RequestBody updateRequest: UpdateRequest): Account {
        return bankService.updateBalance(updateRequest)

    }

}