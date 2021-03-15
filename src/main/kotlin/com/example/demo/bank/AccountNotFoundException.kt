package com.example.demo.bank

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="No such account exists")
class AccountNotFoundException(iban: String) : RuntimeException() {

}
