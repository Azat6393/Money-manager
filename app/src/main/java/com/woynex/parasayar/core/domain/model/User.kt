package com.woynex.parasayar.core.domain.model


data class User(
    val id: String? = null,
    var first_name: String? = null,
    var last_name: String? = null,
    val phone_number: String? = null,
    val email: String? = null
)