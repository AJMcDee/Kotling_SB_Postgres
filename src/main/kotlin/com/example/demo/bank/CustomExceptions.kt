package com.example.demo.bank

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="No such account exists")
class AccountNotFoundException: Exception() {

}

@ResponseStatus(value=HttpStatus.FORBIDDEN, reason="Incorrect credentials")
class IncorrectCredentialsException: Exception() {

}

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Invalid Operation")
class InvalidOperationException: Exception() {

}

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Invalid transfer: please ensure your request contains an IBAN")
class BadTransferException: Exception() {

}