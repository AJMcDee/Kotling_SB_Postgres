package com.example.demo.repository

import org.springframework.data.repository.CrudRepository
import com.example.demo.model.Account
import org.springframework.stereotype.Repository

@Repository
public interface AccountRepository: CrudRepository<Account, Long> {
    fun findByIban(iban: String): Account
    fun existsByIban(iban:String): Boolean

    fun findByToken(token: String): Account
    fun existsByToken(token: String): Boolean

    fun findByIbanAndPassword(iban: String, password: String): Account
    fun existsByIbanAndPassword(iban: String, password: String): Boolean
    fun existsAccountByIban (iban:String): Boolean
}