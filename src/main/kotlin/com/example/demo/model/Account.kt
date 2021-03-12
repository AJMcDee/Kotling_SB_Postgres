package com.example.demo.model

import javax.persistence.*;

data class UpdateRequest (val token: String, val amount: Double, val operation: String )
data class AccessRequest (val token: String)
data class LoginAttempt (val iban: String, val password: String)

@Entity
@Table(name = "accounts")
class Account(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    val id: Int? = 1,

    @Column(name = "fullname")
    val name: String = "",

    @Column(name = "iban")
    val iban: String = "",

    @Column(name = "password")
    val password: String = "",

    @Column(name = "balance")
    var balance: Double = 0.00,

    @Column(name = "token")
    var token: String = "default"
) {
    fun updateBalance(amount: Double, operation: String) {
        if (operation == "withdraw") {
            if (amount > this.balance) throw Error("Withdrawal amount too high")
            else this.balance -= amount;
        } else if (operation == "deposit") {
            this.balance += amount;
        }
    }

    override fun toString(): String {
        return "{Account for $name, IBAN $iban, now has balance $$balance.}"
    }
}
