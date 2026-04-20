package org.example.sealedpoc.model

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonSubTypes

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(value = PaymentResult.Success::class, name = "success"),
    JsonSubTypes.Type(value = PaymentResult.Declined::class, name = "declined"),
    JsonSubTypes.Type(value = PaymentResult.Error::class, name = "error")
)
sealed class PaymentResult {
    data class Success(val transactionId: String, val amount: Double) : PaymentResult()
    data class Declined(val reason: String) : PaymentResult()
    data class Error(val exception: String) : PaymentResult()
}
