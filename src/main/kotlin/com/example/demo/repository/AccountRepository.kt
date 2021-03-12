package com.example.demo.repository

import org.springframework.data.repository.CrudRepository
import com.example.demo.model.Account
import org.springframework.stereotype.Repository

@Repository
public interface AccountRepository: CrudRepository<Account, Long> {
    fun findByIban(iban: String): Account
    fun findByToken(token: String): Account
    fun findByIbanAndPassword(iban: String, password: String): Account
    fun existsAccountByIban (iban:String): Boolean
}