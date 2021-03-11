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

    @GetMapping("/save")
    fun process(): String {
        repository.save(Account(1,"Jack","DE12345","password12345",132.52,""))
        repository.save(Account(2,"Jill","DE23456","password12345",13522.52,""))
        repository.save(Account(3,"Jane","DE34567","password12345",1325.52,""))
        repository.save(Account(4,"Joseph","DE45678","password12345",32.52,""))
        repository.save(Account(5,"Julia","DE56789","password12345",12.52,""))
        return "Done"
    }

    @GetMapping("/accounts")
    fun sendAccounts(): List<Account> {
        return bankService.getAllAccounts();
    }

    @GetMapping("/account/{iban}")
    fun getAccount(@PathVariable iban: String): Account {
        return bankService.getAccountByIBAN(iban);
    }

    @PostMapping(path = ["/accounts"], consumes = ["application/json"], produces = ["application/json"])
    fun addMember(@RequestBody newAccount : Account): MutableList<Account> {
        return bankService.addAccount(newAccount);
    }

    @PostMapping(path = ["/authenticate"], consumes = ["application/json"], produces=["application/json"])
    fun authenticate(@RequestBody loginAttempt: LoginAttempt): String {
        return bankService.authenticate(loginAttempt)
    }

    @DeleteMapping("/account", consumes = ["application/json"], produces = ["application/json"])
    fun deleteAccount(@RequestBody accessRequest: AccessRequest): MutableList<Account> {
        return bankService.deleteAccount(accessRequest)
    }

    @PatchMapping(path = ["/account"], consumes = ["application/json"], produces=["application/json"])
    fun updateBalance(@RequestBody updateRequest: UpdateRequest): Account {
        return bankService.updateBalance(updateRequest)

    }

}