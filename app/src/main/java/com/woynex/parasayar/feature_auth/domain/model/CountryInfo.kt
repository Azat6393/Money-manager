package com.woynex.parasayar.feature_auth.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CountryInfo(
    val flag: String,
    val name: String,
    val number: String
)