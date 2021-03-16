package com.example.demo.model

data class TransactionRequest(
    val toIban: String?,
    val fromIban: String?,
    val amount: Double,
    val type: String,
    val date:String
) {

}


