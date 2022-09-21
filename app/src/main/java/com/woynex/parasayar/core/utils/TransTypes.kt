package com.woynex.parasayar.core.utils

object TransTypes {
    const val INCOME = "income"
    const val EXPENSE = "expense"
    const val TRANSFER = "transfer"
}

sealed interface TransType {
    object Income : TransType
    object Expense : TransType
    object Transfer : TransType
}